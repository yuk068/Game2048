package gamenumberstack;

public class NumberNode implements Comparable<NumberNode> {

    private int value;

    public NumberNode() {
    }

    public NumberNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(NumberNode o) {
        return Integer.compare(value, o.value);
    }

}
