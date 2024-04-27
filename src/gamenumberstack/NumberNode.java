package gamenumberstack;

public record NumberNode(int value) implements Comparable<NumberNode> {

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(NumberNode o) {
        return Integer.compare(value, o.value);
    }

}
