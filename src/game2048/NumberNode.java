package game2048;

import java.util.Random;

public class NumberNode implements Comparable<NumberNode> {

    private int value;
    private boolean recentlyMerged;
    private static final double TWO_NODE_RATE = 0.9;

    public NumberNode() {

    }

    public NumberNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isRecentlyMerged() {
        return recentlyMerged;
    }

    public void setRecentlyMerged(boolean recentlyMerged) {
        this.recentlyMerged = recentlyMerged;
    }

    public void setRandomInitialValue() {
        double rate = new Random().nextDouble();
        value = rate < TWO_NODE_RATE ? 2 : 4;
    }

    public NumberNode mergeWith(NumberNode other) {
        if (this.value == other.value) {
            return new NumberNode(this.value * 2);
        } else {
            throw new IllegalArgumentException("Cannot merge nodes with different values");
        }
    }

    @Override
    public int compareTo(NumberNode o) {
        return Integer.compare(value, o.value);
    }

}
