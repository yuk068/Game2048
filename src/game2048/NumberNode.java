package game2048;

import java.util.Random;

public class NumberNode implements Comparable<NumberNode> {

    private int value;
    private boolean recentlyMerged;
    private static final double TWO_NODE_RATE = 0.9;

    protected NumberNode() {

    }

    protected NumberNode(int value) {
        this.value = value;
    }

    protected int getValue() {
        return value;
    }

    protected boolean isRecentlyMerged() {
        return recentlyMerged;
    }

    protected void setRecentlyMerged(boolean recentlyMerged) {
        this.recentlyMerged = recentlyMerged;
    }

    protected void setRandomInitialValue() {
        double rate = new Random().nextDouble();
        value = rate < TWO_NODE_RATE ? 2 : 4;
    }

    protected NumberNode mergeWith(NumberNode other) {
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
