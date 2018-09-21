package algorithms.examples;

import algorithms.templates.DivideAndConquerable;

import java.util.ArrayList;
import java.util.List;

public class FibonacciDnC implements DivideAndConquerable<Long> {

    private Long n;

    public FibonacciDnC(Long n){
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
    public List<? extends DivideAndConquerable<Long>> decompose() {
        ArrayList<DivideAndConquerable<Long>> directChildren = new ArrayList<>();

        directChildren.add(new FibonacciDnC(n-2));
        directChildren.add(new FibonacciDnC(n-1));

        return directChildren;
    }

    @Override
    public Long recombine(List<Long> intermediateResults) {
        return intermediateResults.stream().mapToLong(x -> x).sum();
    }
}
