package core;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class RandomEncounter {
    private TETile[][] tiles;
    private boolean collectedCoins;
    private int width;
    private int height;
    private TETile groundTile = Tileset.FLOWER;
    private TETile coinTile = Tileset.MOUNTAIN;
    private int roomMinX;
    private int roomMinY;
    private int roomMaxX;
    private int roomMaxY;
    private static final double POINT75 = 0.75;
    private String currMoves;
    public RandomEncounter(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        roomCreate();
        coinCreate();
        currMoves = "";

    }
    private void roomCreate() {
        roomMinX = (int) Math.floor(width * 0.25);
        roomMaxX = (int) Math.floor(width * POINT75);
        roomMinY = 0;
        roomMaxY = (int) Math.floor(height * 0.5);
        for (int x = roomMinX; x < roomMaxX; x++) {
            for (int y = 0; y < roomMaxY; y++) {
                if (x == roomMinX || x == roomMaxX - 1 || y == roomMinY || y == roomMaxY - 1) {
                    //                        tester[i][d] = "wall";
                    tiles[x][y] = Tileset.WATER;
                } else {
                    //                        tester[i][d] = "grass";
                    tiles[x][y] = Tileset.FLOWER;
                }
            }
        }
    }

    public void coinCreate() {
        Random r = new Random();
        int numCoins = 7;
        while (numCoins >= 0) {
            int x = r.nextInt(roomMaxX - roomMinX) + roomMinX;
            int y = r.nextInt(roomMaxY - roomMinY) + roomMinY;
            if (tiles[x][y] == groundTile) {
                tiles[x][y] = coinTile;
                numCoins--;
            }
        }

    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public int[] getRandomGrassCoords() {
        Random r = new Random();
        int[] returnArr = new int[2];
        int x = r.nextInt(width);
        int y = r.nextInt(height);
        while (!(tiles[x][y] == groundTile)) {
            x = r.nextInt(width);
            y = r.nextInt(height);
        }
        returnArr[0] = x;
        returnArr[1] = y;
        return returnArr;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public TETile[][] getTilesCopy() {
        TETile[][] returnTiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                returnTiles[x][y] = tiles[x][y];
            }
        }
        return returnTiles;
    }

    public void setTile(TETile tile, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = tile;
        }
    }
    public TETile getCoinTile() {
        return coinTile;
    }
    public TETile getGroundTile() {
        return groundTile;
    }
}
