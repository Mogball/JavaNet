package NeuralNetwork;

import GeneticAlgorithm.Bound;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A population represents a set of individuals.
 */
public class Population implements Serializable {

    private static final long serialVersionUID = 64783L;

    private final Individual[] population;

    public Population(int nP) {
        population = new Individual[nP];
    }

    public void generate(int[] L, Bound b) {
        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual();
            population[i].generate(L, b);
        }
    }

    public Individual get(int i) {
        return population[i];
    }

    public void set(Individual I, int i) {
        population[i] = I;
    }

    public int size() {
        return population.length;
    }

    public void costFitness(NeuralNetwork NN, Matrix x, Matrix y) {
        for (Individual I : population) {
            NN.loadIndividual(I);
            double J = NN.costFunction(x, y);
            double fitness = 1 / J;
            I.setFitness(fitness);
        }
    }

    public void order() {
        Arrays.sort(population, (Individual I1, Individual I2)
                -> I2.compareTo(I1));
    }

    Individual tournamentSelection(int nT, double p0) {
        // Randomly select a group by generating a string of
        // individual indices with no repeat
        Population T = new Population(nT);
        if (nT < population.length) {
            Bound b = new Bound(0, population.length - 1);
            Iterator<Integer> indices = b.randString(nT, false);
            int i = 0;
            while (indices.hasNext()) {
                int index = indices.next();
                Individual I = get(index);
                T.set(I, i);
                i++;
            }
        } else {
            for (int i = 0; i < population.length; i++) {
                T.set(get(i), i);
            }
        }

        // Order the tournament population by fitness
        T.order();

        // If p = 1, use deterministic tournament selection
        if (p0 == 1.0) {
            return T.get(0);
        } else {
            Bound P = new Bound(0.0, geomCDF(nT, p0));
            int k = (int) Math.floor(inverseGeomCDF(P.rand(), p0));
            return T.get(k);
        }
    }

    /**
     * @param nE the elite size, number of elite children
     * @param nT the tournament size
     * @param p0 the base tournament probability
     * @param iM the individual mutability
     * @param gM the gene mutability
     * @param V the mutation volatility
     * @return an evolved population
     */
    public Population evolve(int nE, int nT, double p0,
            double iM, double gM, double V) {
        // Evolve the new population
        int nP = population.length;
        Population newPop = new Population(nP);

        // Elite children
        for (int i = 0; i < nE; i++) {
            Individual I = new Individual();
            I.setGenome(get(i).getGenome());
            newPop.set(I, i);
        }

        // Crossover non-elite children
        for (int i = nE; i < nP; i++) {
            Individual I1 = tournamentSelection(nT, p0);
            Individual I2 = tournamentSelection(nT, p0);
            Individual I = I1.crossover(I2);
            newPop.set(I, i);
        }

        // Mutate non-elite children
        for (int i = nE; i < nP; i++) {
            if (Math.random() <= iM) {
                Individual I = newPop.get(i);
                Individual M = I.mutate(gM, V);
                newPop.set(M, i);
            }
        }
        return newPop;
    }

    /**
     * @param n sample max k + 1
     * @param p0 base probability
     * @return the cumulative probability
     */
    private double geomCDF(double n, double p0) {
        return 1 - Math.pow(1 - p0, n);
    }

    /**
     * @param P the cumulative probability
     * @param p0 the base probability
     * @return the index k
     */
    private double inverseGeomCDF(double P, double p0) {
        return Math.log(1 - P) / Math.log(1 - p0);
    }

}
