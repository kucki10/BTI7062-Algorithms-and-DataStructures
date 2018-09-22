package algorithms.examples;

import algorithms.templates.DivideAndConquerable;
import algorithms.templates.DynamicallyDivideAndConquerable;

import java.util.ArrayList;
import java.util.List;

public class DynamicallyFibonacciDnC implements DynamicallyDivideAndConquerable<Long> {

    private Long n;

    public DynamicallyFibonacciDnC(Long n){
        this.n = n;
    }

    @Override
    public boolean isBasic() {
        return this.n == 1 || this.n == 0;
    }

    @Override
    public Long baseFun() {
        return this.n;
    }

    @Override
    public List<? extends DynamicallyDivideAndConquerable<Long>> decompose() {
        List<DynamicallyDivideAndConquerable<Long>> directChildren = new ArrayList<>();

        directChildren.add(new DynamicallyFibonacciDnC(n-2));
        directChildren.add(new DynamicallyFibonacciDnC(n-1));

        return directChildren;
    }

    @Override
    public Long recombine(List<Long> intermediateResults) {
        return intermediateResults.stream().mapToLong(x -> x).sum();
    }


    @Override
    public int hashCode() {
        return n.intValue();
    }
}
