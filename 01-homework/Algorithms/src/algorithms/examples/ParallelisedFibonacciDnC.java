package algorithms.examples;

import algorithms.templates.DivideAndConquerable;
import algorithms.templates.ParallelisedDivideAndConquerable;

import java.util.ArrayList;
import java.util.List;

public class ParallelisedFibonacciDnC implements ParallelisedDivideAndConquerable<Long> {

    private Long n;

    public ParallelisedFibonacciDnC(Long n){
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
    public List<? extends ParallelisedDivideAndConquerable<Long>> decompose() {
        ArrayList<ParallelisedDivideAndConquerable<Long>> directChildren = new ArrayList<>();

        directChildren.add(new ParallelisedFibonacciDnC(n-2));
        directChildren.add(new ParallelisedFibonacciDnC(n-1));

        return directChildren;
    }

    @Override
    public Long recombine(List<Long> intermediateResults) {
        return intermediateResults.stream().mapToLong(x -> x).sum();
    }
}
