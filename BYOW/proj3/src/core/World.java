package core;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.LazyPrimMST;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class World {
    private final TERenderer ter = new TERenderer();
    private static final int TEN = 10;
    private static final int TWENTY = 20;
    private static final int FIFTEEN = 15;
    private TETile groundTile = Tileset.GRASS;
    private TETile wallTile = Tileset.WALL;

    private TETile[][] tiles;
    private int width;
    private int height;
    private long seed;
    private Random r;
    //     private String[][] tester;
    private EdgeWeightedGraph testGraph;
    private HashMap<Integer, int[]> roomToCoords;
    private ArrayList<Integer> finalSorted;
    private LazyPrimMST pleaseWorkMST;
    private int roomCount;
    private Avatar worldAvatar;
    private boolean createRE;
    private TETile coinTile = Tileset.FLOWER;
    public World(int width, int height, long inputSeed) {
        this.seed = inputSeed;
        this.width = width;
        this.height = height;
        tiles = new TETile[width][height];
        //        tester = new String[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
                //                tester[x][y] = "nothing";
            }
        }
        r = new Random(seed);
        roomToCoords = new HashMap<>();
        finalSorted = new ArrayList<>();
        createRE = false;
        roomCreator();
        roomSorter();
        hallCreator();
        hallFixer();
        //Deal with randomness here
    }


    public void roomCreator() {
        roomCount = 0;
        while (roomCount < (r.nextInt(TEN) + FIFTEEN)) {
            int randomX = r.nextInt(width);
            int randomY = r.nextInt(height);
            generateRoom(randomX, randomY);
        }
    }

    public void generateRoom(int currX, int currY) {
        int roomWidth = Math.abs(r.nextInt() % FIFTEEN) + 5; //Ensures room width of at least 5
        int roomHeight = Math.abs(r.nextInt() % FIFTEEN) + 5; //Ensures room height of at least 5
        int maxX = Math.min((currX + roomWidth), width) - r.nextInt(3);
        int maxY = Math.min((currY + roomHeight), height) - r.nextInt(3);
        boolean testBool = true;
        for (int[] i: roomToCoords.values()) {
            if (!(currX > i[2] || maxX < i[0] || maxY < i[1] || currY > i[3])) { //intersecting box theorem
                testBool = false;
                break;
            }
        }
        if (maxY - currY < 4 || maxX - currX < 4) { //only rooms of at least width 5 are created
            testBool = false;
        }
        if (testBool) {
            for (int i = currX; i < maxX; i++) {
                for (int d = currY; d < maxY; d++) {
                    if (i == currX || i == maxX - 1 || d == currY || d == maxY - 1) {
                        //                        tester[i][d] = "wall";
                        tiles[i][d] = Tileset.WALL;
                    } else {
                        //                        tester[i][d] = "grass";
                        tiles[i][d] = Tileset.GRASS;
                    }
                }
            }
            if (!createRE) {
                int reX = r.nextInt((maxX - 1) - (currX + 1)) + (currX + 1);
                int reY = r.nextInt((maxY - 1) - (currY + 1)) + (currY + 1);
                tiles[reX][reY] = coinTile;
                createRE = true;
            }
            roomToCoords.put(roomCount, new int[6]);
            roomToCoords.get(roomCount)[0] = currX;
            roomToCoords.get(roomCount)[1] = currY;
            roomToCoords.get(roomCount)[2] = maxX - 1;
            roomToCoords.get(roomCount)[3] = maxY - 1;
            roomToCoords.get(roomCount)[4] = (maxX - 1 + currX) / 2; //center X
            roomToCoords.get(roomCount)[5] = (maxY - 1 + currY) / 2; //center Y
            this.roomCount++;
        }
    }

    public void roomSorter() {
        finalSorted.addAll(roomToCoords.keySet());
        testGraph = new EdgeWeightedGraph(finalSorted.size());
        for (int i: finalSorted) {
            int[] iArray = roomToCoords.get(i);
            for (int a: finalSorted) {
                if (a != i) {
                    int[] aArray = roomToCoords.get(a);
                    Edge temp = new Edge(i, a, Math.sqrt(
                            Math.pow(
                                    Math.abs(iArray[4] - aArray[4]), 2)
                                    + Math.pow(Math.abs(iArray[5] - aArray[5]), 2)));
                    //using pythagorean theorem for edge weight
                    testGraph.addEdge(temp);
                }
            }
        }
        pleaseWorkMST = new LazyPrimMST(testGraph);

    }
    public TETile[][] getTiles() {
        return tiles;
    }
    // build your own world!

    public void hallCreator() {
        for (Edge i: pleaseWorkMST.edges()) {
            int roomOne = i.either();
            int roomTwo = i.other(roomOne);
            int[] coordsListOne = roomToCoords.get(roomOne);
            int roomOneX = (Math.abs(r.nextInt()) % ((coordsListOne[2] - 1) - (coordsListOne[0] + 1)))
                    + (coordsListOne[0] + 1);
            //makes sure to exclude walls from each side
            int roomOneY = (Math.abs(r.nextInt()) % ((coordsListOne[3] - 1) - (coordsListOne[1] + 1)))
                    + (coordsListOne[1] + 1);

            int[] coordsListTwo = roomToCoords.get(roomTwo);
            int roomTwoX = (Math.abs(r.nextInt()) % ((coordsListTwo[2] - 1) - (coordsListTwo[0] + 1)))
                    + (coordsListTwo[0] + 1);
            int roomTwoY = (Math.abs(r.nextInt()) % ((coordsListTwo[3] - 1) - (coordsListTwo[1] + 1)))
                    + (coordsListTwo[1] + 1);

            generateHall(roomOneX, roomOneY, roomTwoX, roomTwoY);
        }
    }

    public void generateHall(int x1, int y1, int x2, int y2) {
        if (x1 >= x2) {
            for (int a = x2; a <= x1; a++) {
                tiles[a][y2] = Tileset.GRASS;
                if (tiles[a][y2 + 1] == Tileset.NOTHING) {
                    tiles[a][y2 + 1].setHallwayAdjacentToGrassTrue();
                }
                if (tiles[a][y2 - 1] == Tileset.NOTHING) {
                    tiles[a][y2 - 1].setHallwayAdjacentToGrassTrue();
                }
            }
        } else {
            for (int a = x2; a > x1; a--) {
                tiles[a][y2] = Tileset.GRASS;
                if (tiles[a][y2 + 1] == Tileset.NOTHING) {
                    tiles[a][y2 + 1].setHallwayAdjacentToGrassTrue();
                }
                if (tiles[a][y2 - 1] == Tileset.NOTHING) {
                    tiles[a][y2 - 1].setHallwayAdjacentToGrassTrue();
                }
            }
        }
        if (y1 >= y2) {
            for (int b = y2; b <= y1; b++) {
                tiles[x1][b] = Tileset.GRASS;
                if (tiles[x1 + 1][b] == Tileset.NOTHING) {
                    tiles[x1 + 1][b].setHallwayAdjacentToGrassTrue();
                }
                if (tiles[x1 - 1][b] == Tileset.NOTHING) {
                    tiles[x1 - 1][b].setHallwayAdjacentToGrassTrue();
                }
            }
        } else {
            for (int b = y2; b > y1; b--) {
                tiles[x1][b] = Tileset.GRASS;
                if (tiles[x1 + 1][b] == Tileset.NOTHING) {
                    tiles[x1 + 1][b].setHallwayAdjacentToGrassTrue();
                }
                if (tiles[x1 - 1][b] == Tileset.NOTHING) {
                    tiles[x1 - 1][b].setHallwayAdjacentToGrassTrue();
                }
            }
        }
    }

    public void hallFixer() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] == Tileset.GRASS) {
                    //if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    // makes all grass tiles against edges of world walls
                    //tiles[x][y] = Tileset.FLOWER; //NOT NEEDED IF EVERYTHING WORKS PROPERLY!
                    //}
                    if (tiles[x + 1][y + 1] == Tileset.NOTHING) { //fixes diagonal holes in turning hallways
                        // AND everything else, too! - wouldn't work if had dead end hallway
                        tiles[x + 1][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x + 1][y - 1] == Tileset.NOTHING) {
                        tiles[x + 1][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x - 1][y + 1] == Tileset.NOTHING) {
                        tiles[x - 1][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x - 1][y - 1] == Tileset.NOTHING) {
                        tiles[x - 1][y - 1] = Tileset.WALL;
                    }
                }
                //                if (tiles[x][y] == Tileset.NOTHING) {
                //                    if (tiles[x][y].getHallwayAdjacentToGrass()) {
                //                        tiles[x][y] = Tileset.FLOWER;
                //                    } NOT NEEDED, BUT WHY DOES THIS SET EVERY NOTHING TILE TO A FLOWER??
                //                }
            }
        }
    }

    public int[] getRandomGrassCoords() {
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

    public void renderWorld() {
        ter.renderFrame(tiles);
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public TETile getGroundTile() {
        return this.groundTile;
    }

    public TETile getWallTile() {
        return this.wallTile;
    }

    public long getSeed() {
        return seed;
    }

    public TETile getCoinTile() {
        return coinTile;
    }

    public void setTile(TETile tile, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = tile;
        }
    }
}
