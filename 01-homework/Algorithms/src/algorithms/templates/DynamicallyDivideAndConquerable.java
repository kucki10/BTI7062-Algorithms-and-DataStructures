package algorithms.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DynamicallyDivideAndConquerable<OutputType> extends DivideAndConquerable<OutputType> {

    @Override
    List<? extends DynamicallyDivideAndConquerable<OutputType>> decompose();


    default OutputType divideAndConquer(Map<Integer, OutputType> cache) {
        if (this.isBasic()) {
            return this.baseFun();
        }

        List<? extends DynamicallyDivideAndConquerable<OutputType>> subComponents = this.decompose();

        List<OutputType> intermediateResults = new ArrayList<>(subComponents.size());

        subComponents.forEach(
                subComponent -> {

                    //Cache
                    OutputType cached = cache.get(subComponent.hashCode());
                    if (cached != null) {
                        //If the object is already memoized, use the cached one
                        intermediateResults.add(cached);

                    } else {
                        //Object is not found in cache, calculate it
                        OutputType newResult = subComponent.divideAndConquer(cache);

                        //Cache the new result
                        cache.put(subComponent.hashCode(), newResult);

                        intermediateResults.add(newResult);
                    }
                }
        );

        return recombine(intermediateResults);
    }

}