package NeuralNetwork;

import GeneticAlgorithm.Bound;
import java.io.Serializable;
import java.util.Arrays;

public class Individual
        implements Serializable,
        Comparable<Individual> {

    private static final long serialVersionUID = 17265L;

    /* Private */
    private Chromosome[] genome;
    public double fitness;

    /* Constructor */
    public Individual() {
        genome = null;
        fitness = 0;
    }

    /* Methods */
    public void generate(int[] L, Bound b) {
        final int N = L.length;
        genome = new Chromosome[N - 1];
        for (int i = 0; i < N - 1; i++) {
            Matrix X = Matrix.random(L[i], L[i + 1], b);
            genome[i] = new Chromosome(X);
        }
    }

    /**
     * @return the genome
     */
    Chromosome[] getGenome() {
        return genome;
    }

    /**
     * @param genome the genome
     */
    void setGenome(Chromosome[] genome) {
        this.genome = genome;
    }

    /**
     * @param fitness the fitness
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    Individual mutate(double gM, double V) {
        Individual N = new Individual();
        Chromosome[] newGenome = new Chromosome[genome.length];
        for (int i = 0; i < genome.length; i++) {
            newGenome[i] = genome[i].mutate(gM, V);
        }
        N.setGenome(newGenome);
        return N;
    }

    Individual crossover(Individual O) {
        Individual N = new Individual();
        Chromosome[] otherGenome = O.getGenome();
        Chromosome[] newGenome = new Chromosome[genome.length];
        for (int i = 0; i < genome.length; i++) {
            newGenome[i] = genome[i].crossover(otherGenome[i]);
        }
        N.setGenome(newGenome);
        return N;
    }
    @Override
    public int compareTo(Individual I) {
        final double v = fitness - I.fitness;
        if (v < 0) {
            return -1;
        } else if (v > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @return the genome as a string
     */
    @Override
    public String toString() {
        return Arrays.toString(genome);
    }

}
