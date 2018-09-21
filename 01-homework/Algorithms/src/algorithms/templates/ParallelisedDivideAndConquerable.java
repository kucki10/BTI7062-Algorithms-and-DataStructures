package algorithms.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public interface ParallelisedDivideAndConquerable<OutputType> extends DivideAndConquerable<OutputType> {

    default OutputType divideAndConquer(ThreadPoolExecutor executor) {
        if (this.isBasic()) {
            return this.baseFun();
        }

        List<? extends DivideAndConquerable<OutputType>> subcomponents = this.decompose();

        List<OutputType> intermediateResults = new ArrayList<OutputType>(subcomponents.size());

        subcomponents.forEach(subcomponent-> {
            executor.execute(() -> {
                intermediateResults.add(subcomponent.divideAndConquer());
            });
        });

        //TODO: WAIT until tasks have finished?

        return recombine(intermediateResults);
    }
}
