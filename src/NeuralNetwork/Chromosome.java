package NeuralNetwork;

import GeneticAlgorithm.Bound;
import java.io.Serializable;

class Chromosome implements Serializable {

    private static final long serialVersionUID = 145671L;

    /**
     * Standard bound on first order mutations.
     */
    private static final Bound firstOrder = new Bound(-0.5, 0.5);
    /**
     * Standard bound on second order mutations.
     */
    private static final Bound secondOrder = new Bound(0.5, 2.0);
    /**
     * Chance of second order mutation.
     */
    private static final double advancedMutationChance = 0.2;
    /**
     * Chance of migration crossover.
     */
    private static final double advancedCrossoverChance = 0.2;

    private Matrix G;

    Chromosome(Matrix G) {
        this.G = G;
    }

    Matrix getGenes() {
        return G;
    }

    void setGenes(Matrix G) {
        this.G = G;
    }

    @Override
    public String toString() {
        return G.toString();
    }

    /* Genetic Operators */
    /**
     * Mutate this gene.
     *
     * @param gM gene mutability, the chance that one gene is mutated
     * @param V volatility, the greatness of each mutation
     * @return a mutated chromosome
     */
    Chromosome mutate(double gM, double V) {
        int m = G.getRows();
        int n = G.getColumns();
        Matrix H = new Matrix(m, n);
        double[][] A = G.toArray();
        double[][] B = H.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = A[i][j];
                if (Math.random() <= gM) {
                    if (Math.random() <= advancedMutationChance) {
                        B[i][j] = secondOrderMutation(B[i][j], V);
                    } else {
                        B[i][j] = firstOrderMutation(B[i][j], V);
                    }
                }
            }
        }
        return new Chromosome(H);
    }

    /**
     * A first order mutation will add or subtract a random value.
     *
     */
    private double firstOrderMutation(double gene, double V) {
        return gene + firstOrder.rand() * V;
    }

    /**
     * A second order mutation will multiply the gene by a factor.
     *
     */
    private double secondOrderMutation(double gene, double V) {
        return gene * secondOrder.rand() * V;
    }

    Chromosome crossover(Chromosome C) {
        if (Math.random() <= advancedCrossoverChance) {
            return migrateCrossover(C);
        } else {
            return averageCrossover(C);
        }
    }

    /**
     * Crossover two chromosomes by splicing them into a new one at a
     * random pivot position.
     */
    Chromosome migrateCrossover(Chromosome C) {
        double[][] A = G.toArray();
        double[][] B = C.getGenes().toArray();
        double[] X = Matrix.toSingleArray(A);
        double[] Y = Matrix.toSingleArray(B);
        int pivot = new Bound(0, X.length - 1).randInt();
        for (int i = pivot; i < X.length; i++) {
            X[i] = Y[i];
        }
        Matrix H = new Matrix(X, A.length);
        return new Chromosome(H);
    }

    /**
     * Crossover two chromosomes by computing the average of each
     * corresponding value with a random weight.
     */
    Chromosome averageCrossover(Chromosome C) {
        double[][] A = G.toArray();
        double[][] B = C.getGenes().toArray();
        int m = G.getRows();
        int n = G.getColumns();
        Matrix H = new Matrix(m, n);
        double[][] X = H.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double wa = Math.random();
                double wb = 1 - wa;
                X[i][j] = wa * A[i][j] + wb * B[i][j];
            }
        }
        return new Chromosome(H);
    }

}
