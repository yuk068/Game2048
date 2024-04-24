package game2048;

public class Tile {

    private NumberNode numberNode;
    private final int posX;
    private final int posY;

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Tile(NumberNode numberNode, int posX, int posY) {
        this.numberNode = numberNode;
        this.posX = posX;
        this.posY = posY;
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

    @Override
    public String toString() {
        return "[" + posX + ", " + posY +
                ": " + (isOccupied() ? numberNode.getValue() : 0) + "]";
    }

}
