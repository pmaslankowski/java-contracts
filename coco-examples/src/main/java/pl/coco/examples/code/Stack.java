package pl.coco.examples.code;

import java.util.Arrays;

import pl.coco.api.code.Contract;
import pl.coco.api.code.InvariantMethod;

public class Stack {

    private static final int INITIAL_SIZE = 64;

    private int top = -1;
    private int[] arr = new int[INITIAL_SIZE];

    public boolean isEmpty() {
        return top < 0;
    }

    public void push(int x) {
        Contract.ensures(!this.isEmpty());

        if (top == (arr.length - 1)) {
            arr = Arrays.copyOf(arr, 2 * arr.length);
        }

        arr[++top] = x;
    }

    public int pop() {
        Contract.requires(!this.isEmpty());

        return arr[top--];
    }

    public int peek() {
        Contract.requires(!this.isEmpty());

        return arr[top];
    }

    @InvariantMethod
    private void invariant() {
        Contract.classInvariant(top >= -1);
        Contract.classInvariant(top <= arr.length);
    }
}
