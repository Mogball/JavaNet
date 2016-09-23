/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneticAlgorithm;

/**
 *
 * @author Jeff Niu
 */
public enum Solution {

    MAX, MIN;

    double calcFitness(Function f, double[] r) {
        double y = f.apply(r);
        if (this == MIN) {
            return -y;
        } else {
            return y;
        }
    }

}
