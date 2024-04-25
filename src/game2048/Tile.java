package game2048;

public class Tile {

    private NumberNode numberNode;

    public Tile() {

    }

    public Tile(NumberNode numberNode) {
        this.numberNode = numberNode;
    }

    public boolean isOccupied() {
        return getNumberNode() != null;
    }

    public void setNumberNode(NumberNode numberNode) {
        this.numberNode = numberNode;
    }

    public NumberNode getNumberNode() {
        return numberNode;
    }

    public void nullifyRecentlyMerged() {
        if (isOccupied()) numberNode.setRecentlyMerged(false);
    }

}
