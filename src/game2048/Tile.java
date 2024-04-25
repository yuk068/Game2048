package game2048;

public class Tile {

    private NumberNode numberNode;

    protected Tile() {

    }

    protected Tile(NumberNode numberNode) {
        this.numberNode = numberNode;
    }

    protected boolean isOccupied() {
        return getNumberNode() != null;
    }

    protected void setNumberNode(NumberNode numberNode) {
        this.numberNode = numberNode;
    }

    protected NumberNode getNumberNode() {
        return numberNode;
    }

    protected void nullifyRecentlyMerged() {
        if (isOccupied()) numberNode.setRecentlyMerged(false);
    }

}
