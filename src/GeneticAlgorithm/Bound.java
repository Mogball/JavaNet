package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jeff Niu
 */
public class Bound {

    public final double lower, upper;
    
    public Bound() {
        this(0.0, 0.0);
    }

    public Bound(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
}

    public double rand() {
        return Math.random() * range() + lower;
    }

    public int randInt() {
        return (int) Math.round(rand());
    }

    public double range() {
        return upper - lower;
    }

    public Iterator<Integer> randString(int size, boolean repeat) {
        if (repeat) {
            List<Integer> values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                values.add(randInt());
            }
            return values.iterator();
        } else {
            Set<Integer> values = new LinkedHashSet<>(size);
            while (values.size() < size) {
                values.add(randInt());
            }
            return values.iterator();
        }
    }

}
