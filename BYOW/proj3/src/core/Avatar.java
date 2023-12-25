package core;

import tileengine.TETile;

public class Avatar {
    private int xCord;
    private int yCord;
    private TETile tile;
    public Avatar(int x, int y, TETile tile) {
        this.xCord = x;
        this.yCord = y;
        this.tile = tile;
    }

    public int getX() {
        return xCord;
    }
    public int getY() {
        return yCord;
    }

    public void setX(int deltaX) {
        xCord += deltaX;
    }

    public void setY(int deltaY) {
        yCord += deltaY;
    }

    public TETile getAvatar() {
        return tile;
    }
}
