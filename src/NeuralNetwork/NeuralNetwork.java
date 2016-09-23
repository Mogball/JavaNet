package NeuralNetwork;

import GeneticAlgorithm.Bound;
import java.io.Serializable;
import static java.lang.Math.exp;
import static java.lang.Math.pow;

/**
 *
 * @author Jeff Niu
 */
public class NeuralNetwork implements Serializable {

    private static final long serialVersionUID = 156L;

    /**
     * Apply the derivative of the sigmoid function to a scalar.
     *
     * @param z scalar
     * @return sigmoid'(z)
     */
    public static double sigmoidPrime(double z) {
        return exp(-z) / pow(1 + exp(-z), 2);
    }

    /**
     * Apply the sigmoid prime function element-wise to a matrix.
     *
     * @param Z the matrix
     * @return sigmoid'(Z)
     */
    public static Matrix sigmoidPrime(Matrix Z) {
        int m = Z.getRows();
        int n = Z.getColumns();
        double[][] A = Z.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = sigmoidPrime(A[i][j]);
            }
        }
        return new Matrix(A, m, n);
    }


    /* Hyperparameters */
    private final int N;
    private final int[] L; // hyperparam
    /**
     * Input and output standardization factors.
     */
    private final double xDot, yDot;
    /**
     * The bias.
     */
    private final double b;
    /**
     * Sigmoid parameter.
     */
    private final double c;

    /* Parameters */
    /**
     * An array that contains the matrix of weights for each synapse layer.
     */
    private Matrix[] W;
    /**
     * An array that contains the matrix of synapse activities.
     */
    private Matrix[] Z;
    /**
     * An array that contain the matrix of neuron activities.
     */
    private Matrix[] A;

    /* Constructor */
    public NeuralNetwork(final int[] L,
            double xDot, double yDot,
            double b, double c) {
        this.L = L;
        this.xDot = xDot;
        this.yDot = yDot;
        this.b = b;
        this.c = c;

        // Set the index of the output layer
        N = L.length;

        // There will be N - 1 synapse layers
        W = new Matrix[N - 1];

        // There are N layers
        Z = new Matrix[N];
        A = new Matrix[N];
    }

    /* Public Static Methods */
    public double sigmoid(double z) {
        return 1 / (1 + exp(-c * z));
    }

    public Matrix sigmoid(Matrix Z) {
        int m = Z.getRows();
        int n = Z.getColumns();
        double[][] A = Z.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = sigmoid(A[i][j]);
            }
        }
        return new Matrix(A, m, n);
    }

    void generate(Bound b) {
        Individual I = new Individual();
        I.generate(L, b);
        loadIndividual(I);
    }

    public void loadIndividual(Individual I) {
        Chromosome[] genome = I.getGenome();
        for (int i = 0; i < W.length; i++) {
            W[i] = genome[i].getGenes();
        }
    }

    public Matrix forward(Matrix x) {
        // Perform standardization on the inputs
        Z[0] = x; // the input activations
        A[0] = Z[0].divide(xDot); // first neuron activity

        // Forward propagation
        for (int n = 1; n < N; n++) {
            Z[n] = A[n - 1].dot(W[n - 1]).plus(b);
            A[n] = sigmoid(Z[n]);
        }

        // The final activity is the standardized outputs
        Matrix yHat = A[N - 1].times(yDot);
        return yHat;
    }

    public double costFunction(Matrix x, Matrix y) {
        // Compute the predicted values
        Matrix yHat = forward(x);

        // Apply the cost formula
        double J = y.minus(yHat).square().sum() * 0.5;
        return J;
    }

}
