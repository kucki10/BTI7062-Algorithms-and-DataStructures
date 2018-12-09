package algorithms.templates;
import java.util.List;
import java.util.ArrayList;

public interface DivideAndConquerable<OutputType> {

    boolean isBasic();
    OutputType baseFun();

    List<? extends DivideAndConquerable<OutputType>> decompose();

    OutputType recombine (List<OutputType> intermediateResults);


    default OutputType divideAndConquer() {
        if (this.isBasic()) {
             return this.baseFun();
         }

         List<? extends DivideAndConquerable<OutputType>> subComponents = this.decompose();

         List<OutputType> intermediateResults = new ArrayList<OutputType>(subComponents.size());

        subComponents.forEach(
                subComponent->intermediateResults.add(subComponent.divideAndConquer())
         );

         return recombine(intermediateResults);
     }


    // see next slide ...
    default List <? extends DivideAndConquerable<OutputType>> stump() {
        return new ArrayList<DivideAndConquerable<OutputType>>(0);
    }
}