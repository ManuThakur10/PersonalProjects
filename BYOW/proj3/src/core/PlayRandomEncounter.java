package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;

public class PlayRandomEncounter { //OVER WINTER, TRY ABSTRACTING ROOMS TO MAKE THINGS EASIER!
    private final RandomEncounter randomEncounter;
    private final TERenderer ter = new TERenderer();

    private TETile avatarTile = Tileset.AVATAR;
    private Avatar thisAvatar;
    private Movement thisMovement;
    private TETile[][] clonedTilesWithAvatar;
    private long prevActionTimestamp;
    private long prevFrameTimestamp; //initialized to zero!
    private boolean isGameOver;
    private boolean readyToQuit;
    private boolean lightBoxToggle;
    private final int SIXTEEN = 16;
    private int score;
    private static final int EIGHTY = 80;
    private static final int FORTY = 40;

    public PlayRandomEncounter(RandomEncounter randomEncounter) {
        this.randomEncounter = randomEncounter;
        int[] randomGrassCoords = randomEncounter.getRandomGrassCoords();

        thisAvatar = new Avatar(randomGrassCoords[0], randomGrassCoords[1], avatarTile);
        thisMovement = new Movement(randomEncounter.getWidth(), randomEncounter.getHeight(),
                this.randomEncounter.getTiles(), this.thisAvatar);
        thisMovement.setGroundTile(Tileset.FLOWER);
        thisMovement.setCoinTile(Tileset.MOUNTAIN);
        this.score = 7;
    }

    public Avatar getAvatar() {
        return thisAvatar;
    }

    public void runGame() {
        thisMovement.setGroundTile(Tileset.FLOWER);
        ter.initialize(randomEncounter.getWidth(), randomEncounter.getHeight() + 5); //offset for gui
        while (!isGameOver) {
            if (shouldRenderNewFrame()) {
                updateGame();
                scoreChecker();
                lightBox();
                renderBoard();
                mouseHover();
            }
        }
    }

    public void lightBox() {
        int maxX = Math.min(getAvatar().getX() + 3, randomEncounter.getWidth() - 1);
        int minX = Math.max(getAvatar().getX() - 3, 0);
        int maxY = Math.min(getAvatar().getY() + 3, randomEncounter.getHeight() - 1);
        int minY = Math.max(getAvatar().getY() - 3, 0);
        if (lightBoxToggle) {
            for (int x1 = 0; x1 < minX; x1++) {
                for (int y1 = 0; y1 < randomEncounter.getHeight(); y1++) {
                    clonedTilesWithAvatar[x1][y1] = Tileset.NOTHING;
                }
            }
            for (int x2 = maxX; x2 < randomEncounter.getWidth(); x2++) {
                for (int y1 = 0; y1 < randomEncounter.getHeight(); y1++) {
                    clonedTilesWithAvatar[x2][y1] = Tileset.NOTHING;
                }
            }
            for (int y2 = 0; y2 < minY; y2++) {
                for (int x3 = 0; x3 < randomEncounter.getWidth(); x3++) {
                    clonedTilesWithAvatar[x3][y2] = Tileset.NOTHING;
                }
            }
            for (int y2 = maxY; y2 < randomEncounter.getHeight(); y2++) {
                for (int x3 = 0; x3 < randomEncounter.getWidth(); x3++) {
                    clonedTilesWithAvatar[x3][y2] = Tileset.NOTHING;
                }
            }
        }
    }

    public void renderBoard() {
        //CALL LIGHTBOX()
        ter.renderFrame(clonedTilesWithAvatar);
    }

    public void scoreChecker() {
        if (score < 0) {
            isGameOver = true;
        }
    }

    public void updateGame() {
        if (StdDraw.hasNextKeyTyped()) {
            char helper = StdDraw.nextKeyTyped();
            if ((helper == 'q' || helper == 'Q') && readyToQuit) {
                isGameOver = true;
            } else if (helper == 'a') {
                thisMovement.moveLeft();
                readyToQuit = false;
            } else if (helper == 'd') {
                thisMovement.moveRight();
                readyToQuit = false;
            } else if (helper == 's') {
                thisMovement.moveDown();
                readyToQuit = false;
            } else if (helper == 'w') {
                thisMovement.moveUp();
                readyToQuit = false;
            } else if (helper == ':') {
                readyToQuit = true;
            } else if (helper == 'r') {
                lightBoxToggle = !lightBoxToggle;
            } else {
                readyToQuit = false;
            }
        }

        TETile[][] clonedTiles = randomEncounter.getTilesCopy();
        //.clone() results in a shallow copy, which for some reason keeps previous positions
        // of the character. I don't know why, though.
        clonedTiles[thisAvatar.getX()][thisAvatar.getY()] = thisAvatar.getAvatar();
        if (randomEncounter.getTiles()[thisAvatar.getX()][thisAvatar.getY()] == randomEncounter.getCoinTile()) {
            randomEncounter.setTile(randomEncounter.getGroundTile(), thisAvatar.getX(), thisAvatar.getY());
            score--;
        }
        clonedTilesWithAvatar = clonedTiles;
    }

    public void mouseHover() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        if (mouseX > 0 && mouseX < randomEncounter.getWidth() && mouseY > 0 && mouseY < randomEncounter.getHeight()) {
            TETile underMouse = clonedTilesWithAvatar[mouseX][mouseY];
            String displayTile = underMouse.description();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(5, randomEncounter.getHeight() + 3, "Tile type: " + displayTile, 0);
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

    public RandomEncounter getThisRE() {
        return randomEncounter;
    }

    public TETile[][] getClonedTilesWithAvatar() {
        return clonedTilesWithAvatar;
    }

    //    public static void main (String[] args) {
    ////        TERenderer rend = new TERenderer();
    ////        rend.initialize(80, 40);
    //        RandomEncounter test = new RandomEncounter(EIGHTY, 40);
    //        PlayRandomEncounter helper = new PlayRandomEncounter(test);
    //        helper.runGame();
    //    }
}
