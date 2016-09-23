package GeneticAlgorithm;

import static java.lang.Math.random;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Jeff Niu
 */
public class GeneticAlgorithm {

    /* Private Static */
    private static final double DEFAULT_UNIFORMITY = 0.5;
    private static final double DEFAULT_MUTABILITY = 0.5;
    private static final double DEFAULT_VOLATILITY = 0.2;
    private static final int DEFAULT_TOURNAMENT_SIZE = 3;
    private static final int DEFAULT_ELITE_SIZE = 1;
    private static final int DEFAULT_POPULATION_SIZE = 100;
    private static final int DEFAULT_GENERATIONS = 100;

    /* Private Final */
    private final Function f;
    private final Solution soln;
    private final Bound[] R;

    /* Private */
    private double uniformity;
    private double mutability;
    private double volatility;
    private int tournamentSize;
    private int eliteSize;
    private int populationSize;
    private int generations;

    /* Constructor */
    public GeneticAlgorithm(Function f,
            Solution soln, Bound[] R) {
        this.f = f;
        this.soln = soln;
        this.R = R;

        uniformity = DEFAULT_UNIFORMITY;
        mutability = DEFAULT_MUTABILITY;
        volatility = DEFAULT_VOLATILITY;
        tournamentSize = DEFAULT_TOURNAMENT_SIZE;
        eliteSize = DEFAULT_ELITE_SIZE;
        populationSize = DEFAULT_POPULATION_SIZE;
        generations = DEFAULT_GENERATIONS;
    }

    /* Methods */
    public double[] solve() {
        Population population = new Population(populationSize, this);
        population.generate(R);
        population.calcFitness(f, soln);
        population.order();
        for (int gen = 0; gen < generations; gen++) {
            Population evolved = population.evolve();
            evolved.calcFitness(f, soln);
            evolved.order();
            population = evolved;
        }
        return population.get(0).getValues();
    }

    /* Getters and Setters */
    public void setUniformity(double uniformity) {
        this.uniformity = uniformity;
    }

    public void setMutability(double mutability) {
        this.mutability = mutability;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public void setEliteSize(int eliteSize) {
        this.eliteSize = eliteSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public double getUniformity() {
        //return uniformity;
        return random();
    }

    public double getMutability() {
        return mutability;
    }

    public double getVolatility() {
        return volatility;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public int getEliteSize() {
        return eliteSize;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getGenerations() {
        return generations;
    }

    public Bound[] getRanges() {
        return R;
    }


}
