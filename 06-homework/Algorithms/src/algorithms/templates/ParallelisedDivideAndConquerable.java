package algorithms.templates;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.ConcurrentUtils;


public interface ParallelisedDivideAndConquerable<OutputType> extends DivideAndConquerable<OutputType> {

    @Override
    List<? extends ParallelisedDivideAndConquerable<OutputType>> decompose();

    default OutputType divideAndConquer(ThreadPoolExecutor executor) {

        if (this.isBasic()) {
            return this.baseFun();
        }

        List<? extends ParallelisedDivideAndConquerable<OutputType>> subComponents = this.decompose();

        List<OutputType> intermediateResults = new Vector<>(subComponents.size());
        List<Future<OutputType>> futureResults = new Vector<>(subComponents.size());

        subComponents.forEach(subComponent-> {
            try {
                futureResults.add(executor.submit(() -> {
                    return subComponent.divideAndConquer(executor);
                }));
            } catch (RejectedExecutionException exception) {
                //NOTE: Do not add them to immediate results
                //Because this would sometimes mix up the ordering (left would be right)
                //Which can cause IndexOutOfBound Exceptions
                futureResults.add(ConcurrentUtils.constantFuture(subComponent.divideAndConquer(executor)));
            }
        });

      futureResults.forEach(x -> {
            try {
                intermediateResults.add(x.get());
            } catch (ExecutionException | InterruptedException exception) {
                exception.printStackTrace();
            }
        });

        return recombine(intermediateResults);
    }
}
