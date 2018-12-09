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

    boolean isBigEnoughToMultiThread();

    default OutputType divideAndConquer(ThreadPoolExecutor executor) {
        if (this.isBasic()) {
            return this.baseFun();
        }

        List<? extends ParallelisedDivideAndConquerable<OutputType>> subComponents = this.decompose();

        List<OutputType> intermediateResults = new ArrayList<OutputType>(subComponents.size());
        List<Future<OutputType>> futureResults = new ArrayList<>(subComponents.size());

        subComponents.forEach(subComponent-> {

            if (isBigEnoughToMultiThread()) {
                try {
                    futureResults.add(executor.submit(() -> subComponent.divideAndConquer(executor)));
                } catch (RejectedExecutionException exception) {
                    intermediateResults.add(subComponent.divideAndConquer(executor));
                }
            } else {
                intermediateResults.add(subComponent.divideAndConquer(executor));
            }

        });

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