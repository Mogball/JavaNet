package NeuralNetwork;

import GeneticAlgorithm.Bound;
import java.io.Serializable;
import static java.lang.Math.floor;
import static java.lang.Math.log10;

public class Matrix implements Serializable {

    private static final long serialVersionUID = 154526L;

    public static double[] toSingleArray(double[][] A) {
        double[] B = new double[A.length * A[0].length];
        for (int i = 0; i < A[0].length; i++) {
            for (int j = 0; j < A.length; j++) {
                B[i * A.length + j] = A[j][i];
            }
        }
        return B;
    }

    public static Matrix random(int m, int n, Bound b) {
        double[][] A = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = b.rand();
            }
        }
        return new Matrix(A, m, n);
    }

    /**
     * Array of values in the matrix.
     */
    private double[][] A;
    /**
     * The numbers of rows and columns.
     */
    private final int m, n;

    /**
     * Construct an empty matrix.
     *
     * @param m number of rows
     * @param n number of columns
     */
    public Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        A = new double[m][n];
    }

    /**
     * Fill a matrix with a scalar value.
     *
     * @param m number of rows
     * @param n number of columns
     * @param s scalar
     */
    public Matrix(int m, int n, double s) {
        this.m = m;
        this.n = n;
        A = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = s;
            }
        }
    }

    /**
     * Create a matrix from an array of values.
     *
     * @param A values
     */
    public Matrix(double[][] A) {
        m = A.length;
        n = A[0].length;
        for (int i = 0; i < m; i++) {
            if (A[i].length != n) {
                throw new IllegalArgumentException(
                        "All rows must have the same length.");
            }
        }
        this.A = A;
    }

    public Matrix(double[][] A, int m, int n) {
        this.A = A;
        this.m = m;
        this.n = n;
    }

    public Matrix(double vals[], int m) {
        this.m = m;
        n = (m != 0 ? vals.length / m : 0);
        if (m * n != vals.length) {
            throw new IllegalArgumentException(
                    "Array length must be a multiple of m.");
        }
        A = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = vals[i + j * m];
            }
        }
    }

    public Matrix copy() {
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j];
            }
        }
        return X;
    }

    public double[][] toArray() {
        return A;
    }

    public int getRows() {
        return m;
    }

    public int getColumns() {
        return n;
    }

    public double get(int i, int j) {
        return A[i][j];
    }

    public void set(int i, int j, double s) {
        A[i][j] = s;
    }

    public Matrix transpose() {
        Matrix X = new Matrix(n, m);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[j][i] = A[i][j];
            }
        }
        return X;
    }

    public Matrix uminus() {
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = -A[i][j];
            }
        }
        return X;
    }

    public Matrix plus(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B.A[i][j];
            }
        }
        return X;
    }

    public Matrix minus(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B.A[i][j];
            }
        }
        return X;
    }

    /**
     * Element-by-element scalar multiplication.
     *
     * @param B other matrix
     * @return a_ij * b_ij
     */
    public Matrix multiply(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] * B.A[i][j];
            }
        }
        return X;
    }

    /**
     * Element-by-element scalar division.
     *
     * @param B other matrix
     * @return a_ij / b_ij
     */
    public Matrix divide(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] / B.A[i][j];
            }
        }
        return X;
    }

    /**
     * Scalar multiplication.
     *
     * @param s scalar
     * @return sA
     */
    public Matrix times(double s) {
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = s * A[i][j];
            }
        }
        return X;
    }

    /**
     * Scalar division.
     *
     * @param s scalar
     * @return A / s
     */
    public Matrix divide(double s) {
        return times(1 / s);
    }

    /**
     * Add a scalar to each element.
     *
     * @param s a scalar
     * @return a_ij + s
     */
    public Matrix plus(double s) {
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = s + A[i][j];
            }
        }
        return X;
    }

    /**
     * Add a scalar to each element.
     *
     * @param s a scalar
     * @return a_ij - s
     */
    public Matrix minus(double s) {
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - s;
            }
        }
        return X;
    }

    /**
     * Linear algebra matrix multiplication.
     *
     * @param B other matrix
     * @return AB
     */
    public Matrix dot(Matrix B) {
        if (B.m != n) {
            throw new IllegalArgumentException(
                    "Matrix inner dimensions must agree.");
        }
        Matrix X = new Matrix(m, B.n);
        double[][] C = X.toArray();
        double[] Bcolj = new double[n];
        for (int j = 0; j < B.n; j++) {
            for (int k = 0; k < n; k++) {
                Bcolj[k] = B.A[k][j];
            }
            for (int i = 0; i < m; i++) {
                double[] Arowi = A[i];
                double s = 0;
                for (int k = 0; k < n; k++) {
                    s += Arowi[k] * Bcolj[k];
                }
                C[i][j] = s;
            }
        }
        return X;
    }

    /**
     * Element wise squaring.
     *
     * @return a_ij * a_ij
     */
    public Matrix square() {
        Matrix X = new Matrix(m, n);
        double[][] C = X.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] * A[i][j];
            }
        }
        return X;
    }

    /**
     * Sum all the elements in this matrix.
     *
     * @return sum(A)
     */
    public double sum() {
        double S = 0.0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                S += A[i][j];
            }
        }
        return S;
    }

    /**
     * Get the maximum entry in this Matrix.
     *
     * @return max(A)
     */
    public double max() {
        double max = A[0][0];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] > max) {
                    max = A[i][j];
                }
            }
        }
        return max;
    }

    /**
     * Get the minimum entry in this Matrix.
     *
     * @return min(A)
     */
    public double min() {
        double min = A[0][0];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] < min) {
                    min = A[i][j];
                }
            }
        }
        return min;
    }

    @Override
    public String toString() {
        String p = String.valueOf(5 + (int) floor(log10(max())));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            sb.append('[');
            for (int j = 0; j < n - 1; j++) {
                double a = A[i][j];
                sb.append(String.format("%" + p + ".3f", a));
                sb.append(',').append(' ');
            }
            double a = A[i][n - 1];
            sb.append(String.format("%" + p + ".3f", a));
            sb.append(']').append('\n');
        }
        return sb.toString();
    }

    private void checkMatrixDimensions(Matrix B) {
        if (B.m != m || B.n != n) {
            throw new IllegalArgumentException(
                    "Matrix dimensions must agree.");
        }
    }

}
