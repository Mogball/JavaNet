package GeneticAlgorithm;

import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Jeff Niu
 */
class Population {

    /* Private Final */
    private final GeneticAlgorithm GA;
    private final Individual[] population;

    /* Constructor */
    Population(int size, GeneticAlgorithm GA) {
        this.GA = GA;
        population = new Individual[size];
    }

    /* Methods */
    void generate(Bound[] R) {
        for (int i = 0; i < population.length; i++) {
            Individual ind = new Individual(GA);
            ind.generate(R);
            population[i] = ind;
        }
    }

    void calcFitness(Function f, Solution soln) {
        for (Individual ind : population) {
            double fitness = soln.calcFitness(f, ind.getValues());
            ind.setFitness(fitness);
        }
        order();
    }

    void order() {
        Arrays.sort(population, (Individual ind1, Individual ind2) -> ind2.compareTo(ind1));
    }

    Individual get(int i) {
        return population[i];
    }

    public void set(Individual ind, int i) {
        population[i] = ind;
    }

    private Individual tournament() {
        final int tournSize = GA.getTournamentSize();
        final Bound b = new Bound(0.0, population.length - 1);
        final Population tourn = new Population(tournSize, GA);
        final Iterator<Integer> indices = b.randString(tournSize, false);
        int i = 0;
        while (indices.hasNext()) {
            int index = indices.next();
            Individual ind = get(index);
            tourn.set(ind, i);
            i++;
        }
        tourn.order();
        return tourn.get(0);
    }

    @Override
    public String toString() {
        return Arrays.toString(population);
    }

    /* Genetic Operator */
    Population evolve() {
        int popSize = GA.getPopulationSize();
        int eliteSize = GA.getPopulationSize();
        Population evolved = new Population(popSize, GA);
        // Elite
        for (int i = 0; i < eliteSize; i++) {
            Individual elite = get(i);
            evolved.set(elite, i);
        }
        // Crossover
        for (int i = eliteSize; i < popSize; i++) {
            Individual ind1 = tournament();
            Individual ind2 = tournament();
            Individual newInd = ind1.crossover(ind2);
            evolved.set(newInd, i);
        }
        // Mutate
        for (int i = eliteSize; i < popSize; i++) {
            evolved.get(i).mutate(GA.getRanges());
        }
        return evolved;
    }

}
