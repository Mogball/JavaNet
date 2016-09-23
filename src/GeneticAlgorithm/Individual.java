package GeneticAlgorithm;

import java.util.Arrays;

/* Private Static Classes */
class Individual implements Comparable<Individual> {

    /* Private Final */
    private final GeneticAlgorithm GA;

    /* Private */
    private double[] r;
    private double fitness;

    /* Constructor */
    Individual(GeneticAlgorithm GA) {
        this.GA = GA;
        r = null;
        fitness = Double.NaN;
    }

    /* Methods */
    void generate(Bound[] R) {
        r = new double[R.length];
        for (int i = 0; i < R.length; i++) {
            r[i] = R[i].rand();
        }
    }

    void setFitness(double fitness) {
        this.fitness = fitness;
    }

    double[] getValues() {
        return r;
    }

    /* Override */
    @Override
    public int compareTo(Individual ind) {
        double v = fitness - ind.fitness;
        if (v < 0) {
            return -1;
        } else if (v > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(r);
    }

    /* Genetic Operator */
    Individual crossover(Individual ind) {
        Individual newInd = new Individual(GA);
        final double[] newr = new double[r.length];
        final double[] r0 = ind.getValues();
        for (int i = 0; i < r.length; i++) {
            final double w = GA.getUniformity();
            final double w0 = 1.0 - w;
            newr[i] = w * r[i] + w0 * r0[i];
        }
        newInd.setValues(newr);
        return newInd;
    }

    void mutate(Bound[] R) {
        if (Math.random() <= GA.getMutability()) {
            for (int i = 0; i < r.length; i++) {
                Bound E = new Bound(0.0, R[i].range());
                double epsilon = E.rand();
                if (Math.random() <= 0.5) {
                    r[i] += epsilon;
                } else {
                    r[i] -= epsilon;
                }
            }
        }
    }

    /* Private Methods */
    private void setValues(double[] r) {
        this.r = r;
    }

}
