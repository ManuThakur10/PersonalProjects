package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Movement {
    private int WIDTH;

    private int GAME_HEIGHT;

    World world;
    TETile[][] worldArray;
    Avatar playerAvatar;
    private TETile groundTile = Tileset.GRASS;
    private TETile wallTile = Tileset.WALL;
    private TETile coinTile = Tileset.FLOWER;
    public Movement(int width, int gameHeight, TETile[][] worldArray, Avatar avatar) {
        this.WIDTH = width;
        this.GAME_HEIGHT = gameHeight;
        this.worldArray = worldArray;
        this.playerAvatar = avatar;
    }

    public boolean canMove(int deltaX, int deltaY) {
        if (worldArray[playerAvatar.getX() + deltaX][playerAvatar.getY() + deltaY] == groundTile
                || worldArray[playerAvatar.getX() + deltaX][playerAvatar.getY() + deltaY] == coinTile) {
            return true;
        }
        return false;
    }
    public void setGroundTile(TETile groundTile) {
        this.groundTile = groundTile;
    }

    public void setCoinTile(TETile coinTile) {
        this.coinTile = coinTile;
    }

    public void moveUp() {
        if (canMove(0, 1)) {
            playerAvatar.setY(1);
        }
    }
    public void moveDown() {
        if (canMove(0, -1)) {
            playerAvatar.setY(-1);
        }
    }
    public void moveLeft() {
        if (canMove(-1, 0)) {
            playerAvatar.setX(-1);
        }
    }
    public void moveRight() {
        if (canMove(1, 0)) {
            playerAvatar.setX(1);
        }
    }



}
