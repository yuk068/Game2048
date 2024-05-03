package gamenumberstack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class NumberNodeStack {

    private static final Random RANDOM = new Random();
    protected int size; // Current number of elements
    protected final int maxSize; // Maximum size a stack can get (initial size + 1)
    protected final Stack<NumberNode> numberNodeStack; // Concrete stack implementation

    public NumberNodeStack(int maxSize, int bottomValue) {
        numberNodeStack = new Stack<>();
        size = 0;
        this.maxSize = maxSize;
        initializeNumberNodeStack(bottomValue);
    }

    public boolean isEmpty() {
        return numberNodeStack.isEmpty();
    }

    public boolean isFull() {
        return size == maxSize;
    }

    public void initializeNumberNodeStack(int bottomValue) {
        numberNodeStack.push(new NumberNode(bottomValue));
        Set<Integer> present = new HashSet<>();
        present.add(bottomValue);
        size++;
        for (int i = 0; i < maxSize - 2; i++) {
            int value;
            while (true) {
                value = RANDOM.nextInt(maxSize - 1) + 1;
                if (!present.contains(value)) {
                    present.add(value);
                    break;
                }
            }
            numberNodeStack.push(new NumberNode(value));
            size++;
        }
    }

    public boolean moveTo(NumberNodeStack another) {
        if (isEmpty() || another.isFull() || this.equals(another)) return false;
        another.numberNodeStack.push(numberNodeStack.pop());
        this.size--;
        another.size++;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder numberNodeStack = new StringBuilder("[");
        int i = 0;
        for (NumberNode node : this.numberNodeStack) {
            numberNodeStack.append(node.getValue()).append(i != size - 1 ? ", " : "");
            i++;
        }
        while (i < maxSize) {
            numberNodeStack.append(", _");
            i++;
        }
        return numberNodeStack.append("]").toString();
    }

}
