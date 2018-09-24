package algorithms.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public interface ParallelisedDivideAndConquerable<OutputType> extends DivideAndConquerable<OutputType> {

    @Override
    List<? extends ParallelisedDivideAndConquerable<OutputType>> decompose();

    default OutputType divideAndConquer(ThreadPoolExecutor executor) {
        if (this.isBasic()) {
            return this.baseFun();
        }

        List<? extends ParallelisedDivideAndConquerable<OutputType>> subcomponents = this.decompose();

        List<OutputType> intermediateResults = new ArrayList<OutputType>(subcomponents.size());
        List<Future<OutputType>> futureResults = new ArrayList<>(subcomponents.size());


        subcomponents.forEach(subcomponent-> {
            //OutputType result = subcomponent.divideAndConquer();
            try {
                futureResults.add(executor.submit(() -> subcomponent.divideAndConquer(executor)));
            } catch (RejectedExecutionException exception) {
                intermediateResults.add(subcomponent.divideAndConquer(executor));
            }
        });

        // Wait for all future results
        // future.get() is blocking so we need to check if its done

        /*
        while (!futureResults.stream().allMatch(Future::isDone)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException exception) {
                // ignored
            }

        }
        */

        futureResults.forEach(x -> {
            try {
                intermediateResults.add(x.get());
            } catch (ExecutionException | InterruptedException exception) {
                System.out.println("something went wrong");
                exception.printStackTrace();
            }
        });

        return recombine(intermediateResults);
    }
}
