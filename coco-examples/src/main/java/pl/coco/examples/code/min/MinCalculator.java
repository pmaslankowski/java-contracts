package pl.coco.examples.code.min;

import java.util.Collection;

import pl.coco.api.code.Contract;

public abstract class MinCalculator {

    /**
     * Finds minimum in given non-empty collection of integers.
     * 
     * @param collection collection
     * @return smallest number from given collection
     */
    public int min(Collection<Integer> collection) {
        // Contracts:
        // Precondition: input collection must not be empty:
        Contract.requires(!collection.isEmpty());

        // Postcondition: returned value should be in collection
        Contract.ensures(
                Contract.exists(collection, i -> Contract.result(Integer.class).equals(i)));
        // Postcondition: returned value must be less or equal than all elements in collection
        Contract.ensures(Contract.forAll(collection, i -> Contract.result(Integer.class) <= i));

        // we have to return something, but we should think of this method as an abstract method
        return 0;
    }
}
