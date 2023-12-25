package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import java.awt.*;
public class PlayGame {
    private final TERenderer ter = new TERenderer();


    private World thisWorld;
    private Avatar thisAvatar;
    private Movement thisMovement;
    private TETile[][] clonedTilesWithAvatar;
    private long prevActionTimestamp;
    private long prevFrameTimestamp; //initialized to zero!
    private boolean isGameOver;
    private boolean readyToQuit;
    private boolean lightBoxToggle;
    private String currWorldMoves;
    private TETile avatarTile = Tileset.AVATAR;
    private boolean openRE;
    private static final int SIXTEEN = 16;
    RandomEncounter helperRE;
    PlayRandomEncounter helperPlayRE;
    public PlayGame(World thisWorld, TETile avatarTile) {
        this.thisWorld = thisWorld;
        this.avatarTile = avatarTile;
        int[] randomGrassCoords = thisWorld.getRandomGrassCoords();
        this.thisAvatar = new Avatar(randomGrassCoords[0], randomGrassCoords[1], avatarTile);
        this.thisMovement = new Movement(thisWorld.getWidth(), thisWorld.getHeight(),
                this.thisWorld.getTiles(), this.thisAvatar);
        helperRE = new RandomEncounter(thisWorld.getWidth(), thisWorld.getHeight());
        helperPlayRE = new PlayRandomEncounter(helperRE);
        currWorldMoves = "";
    }

    public PlayGame(World thisWorld, String loadedGame, TETile avatarTile) {
        this.thisWorld = thisWorld;
        int[] randomGrassCoords = thisWorld.getRandomGrassCoords(); //should still give same pseudorandom #s
        this.avatarTile = avatarTile;
        this.thisAvatar = new Avatar(randomGrassCoords[0], randomGrassCoords[1], avatarTile);
        this.thisMovement = new Movement(thisWorld.getWidth(), thisWorld.getHeight(),
                this.thisWorld.getTiles(), this.thisAvatar);
        currWorldMoves = loadedGame;
        helperRE = new RandomEncounter(thisWorld.getWidth(), thisWorld.getHeight());
        helperPlayRE = new PlayRandomEncounter(helperRE);
        loadedGameHelper(loadedGame);
        TETile[][] clonedTiles = thisWorld.getTilesCopy();
        clonedTiles[thisAvatar.getX()][thisAvatar.getY()] = thisAvatar.getAvatar();
        clonedTilesWithAvatar = clonedTiles;
        lightBox();



    }

    public void loadedGameHelper(String loadedGame) {
        char[] loadedGameToArray = loadedGame.toCharArray();
        for (int a = 0; a < loadedGameToArray.length; a++) {
            char helper = loadedGameToArray[a];
            if (helper == 'a' || helper == 'A') {
                thisMovement.moveLeft();
            } else if (helper == 'd' || helper == 'D') {
                thisMovement.moveRight();
            } else if (helper == 's' || helper == 'S') {
                thisMovement.moveDown();
            } else if (helper == 'w' || helper == 'W') {
                thisMovement.moveUp();
            } else if (helper == 'r' || helper == 'R') {
                lightBoxToggle = !lightBoxToggle;
            }
        }
    }
    public Avatar getAvatar() {
        return thisAvatar;
    }

    public void runGame() {
        ter.initialize(thisWorld.getWidth(), thisWorld.getHeight() + 5); //offset for gui
        while (!isGameOver) {
            if (shouldRenderNewFrame()) {
                updateGame();
                lightBox();
                renderBoard();
                mouseHover();
                if (openRE) {
                    playRE();
                    openRE = false;
                }
            }
        }
    }

    private void playRE() {
        helperPlayRE.runGame();
    }

    public void lightBox() {
        int maxX = Math.min(getAvatar().getX() + 3, getThisWorld().getWidth() - 1);
        int minX = Math.max(getAvatar().getX() - 3, 0);
        int maxY = Math.min(getAvatar().getY() + 3, getThisWorld().getHeight() - 1);
        int minY = Math.max(getAvatar().getY() - 3, 0);
        if (lightBoxToggle) {
            for (int x1 = 0; x1 < minX; x1++) {
                for (int y1 = 0; y1 < getThisWorld().getHeight(); y1++) {
                    clonedTilesWithAvatar[x1][y1] = Tileset.NOTHING;
                }
            }
            for (int x2 = maxX; x2 < getThisWorld().getWidth(); x2++) {
                for (int y1 = 0; y1 < getThisWorld().getHeight(); y1++) {
                    clonedTilesWithAvatar[x2][y1] = Tileset.NOTHING;
                }
            }
            for (int y2 = 0; y2 < minY; y2++) {
                for (int x3 = 0; x3 < getThisWorld().getWidth(); x3++) {
                    clonedTilesWithAvatar[x3][y2] = Tileset.NOTHING;
                }
            }
            for (int y2 = maxY; y2 < getThisWorld().getHeight(); y2++) {
                for (int x3 = 0; x3 < getThisWorld().getWidth(); x3++) {
                    clonedTilesWithAvatar[x3][y2] = Tileset.NOTHING;
                }
            }
        }
    }

    public void renderBoard() {
        //CALL LIGHTBOX()
        ter.renderFrame(clonedTilesWithAvatar);
    }

    public void updateGame() {
        if (StdDraw.hasNextKeyTyped()) {
            char helper = StdDraw.nextKeyTyped();
            if ((helper == 'q' || helper == 'Q') && readyToQuit) {
                isGameOver = true;
                quitAndSave();
            } else if (helper == 'a' || helper == 'A') {
                currWorldMoves += 'a';
                thisMovement.moveLeft();
                readyToQuit = false;
            } else if (helper == 'd' || helper == 'D') {
                currWorldMoves += 'd';
                thisMovement.moveRight();
                readyToQuit = false;
            } else if (helper == 's' || helper == 'S') {
                currWorldMoves += 's';
                thisMovement.moveDown();
                readyToQuit = false;
            } else if (helper == 'w' || helper == 'W') {
                currWorldMoves += 'w';
                thisMovement.moveUp();
                readyToQuit = false;
            } else if (helper == ':') {
                readyToQuit = true;
            } else if (helper == 'r' || helper == 'R') {
                currWorldMoves += 'r';
                lightBoxToggle = !lightBoxToggle;
            } else {
                readyToQuit = false;
            }
        }
        TETile[][] clonedTiles = thisWorld.getTilesCopy();
        //.clone() results in a shallow copy, which for some reason keeps previous positions
        // of the character. I don't know why, though.
        reChecker();
        clonedTiles[thisAvatar.getX()][thisAvatar.getY()] = thisAvatar.getAvatar();
        clonedTilesWithAvatar = clonedTiles;
    }

    public void reChecker() {
        if (thisWorld.getTiles()[thisAvatar.getX()][thisAvatar.getY()] == thisWorld.getCoinTile()) {
            thisWorld.setTile(thisWorld.getGroundTile(), thisAvatar.getX(), thisAvatar.getY());
            openRE = true;
        }
    }

    public void quitAndSave() {
        int helper = 0;
        if (avatarTile == Tileset.AVATAR) {
            helper += 1;
        } else if (avatarTile == Tileset.AVATARCYAN) {
            helper += 2;
        } else if (avatarTile == Tileset.AVATARORANGE) {
            helper += 3;
        }
        FileUtils.writeFile("thisGame.txt", helper + "n" + thisWorld.getSeed() + "s" + currWorldMoves);
        //looks like this overrides anything in the file so it works perfectly
        System.exit(0);
    }

    public void mouseHover() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        if (mouseX > 0 && mouseX < thisWorld.getWidth() && mouseY > 0 && mouseY < thisWorld.getHeight()) {
            TETile underMouse = clonedTilesWithAvatar[mouseX][mouseY];
            String displayTile = underMouse.description();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(5, thisWorld.getHeight() + 3, "Tile type: " + displayTile, 0);
            StdDraw.show();
        }
    }

    public boolean shouldRenderNewFrame() {
        if (frameDeltaTime() > SIXTEEN) {
            resetFrameTimer();
            return true;
        }
        return false;
    }

    private long actionDeltaTime() {
        return System.currentTimeMillis() - prevActionTimestamp;
    }

    private long frameDeltaTime() {
        return System.currentTimeMillis() - prevFrameTimestamp;
    }

    private void resetFrameTimer() {
        prevFrameTimestamp = System.currentTimeMillis();
    }

    public World getThisWorld() {
        return thisWorld;
    }

    public TETile[][] getClonedTilesWithAvatar() {
        return clonedTilesWithAvatar;
    }

    public String getCurrWorldMoves() {
        return currWorldMoves;
    }

    //    public static void main(String[] args) {
    //        long seed = 10;
    //        World testWorld = new World(50, 40, seed);
    //        PlayGame testGame = new PlayGame(testWorld, "ssss");
    //        testGame.runGame();
    //
    //    }
}
