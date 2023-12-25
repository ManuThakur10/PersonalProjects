package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.awt.*;
import java.math.BigInteger;
import java.util.Random;

import static java.awt.Font.*;
public class StartMenu {
    private static final double POINT2 = 0.2;
    private static final int THIRTY = 30;
    private static final int TWENTY = 20;
    private static final int FORTY = 40;
    private static final int FIFTY = 50;
    private static final int SIXTY = 60;
    private static final double POINT5 = 0.5;
    private static final double POINT4 = 0.4;

    private static final double POINT8 = 0.8;
    private static final double POINT3 = 0.3;
    private static final double POINT35 = 0.35;
    private static final double POINT37 = 0.37;
    private static final double POINT25 = 0.25;
    private static final double POINT03 = 0.03;
    private static final double POINT7 = 0.7;
    private PlayGame testGame;
    private TETile avatarTile = Tileset.AVATAR;
    boolean quitOutOfAvatarMenu;
    boolean quitOutOfStartMenu;
    int avatarMenuCircle = 0;
    //useful for determining whether to use loaded avatar tile or newly selected one
    private boolean toggle1;
    private boolean toggle2;
    private boolean toggle3;
    private final int FIFTEEN = 15;

    public StartMenu() {
        startMenuInitializer();
    }

    public void startMenuInitializer() {
        startMenuDisplay();
        quitOutOfAvatarMenu = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char nextKey = StdDraw.nextKeyTyped();
                if (nextKey == 'n' || nextKey == 'N') {
                    inputMenu();
                } else if (nextKey == 'l' || nextKey == 'L') {
                    loadMenu();
                } else if (nextKey == 'q' || nextKey == 'Q') {
                    System.exit(0);
                } else if (nextKey == 'f' || nextKey == 'F') {
                    changeAvatarMenu();
                }
            }

        }
    }

    public static void startMenuDisplay() {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setPenRadius(5.0); //Had a dream about this!
        Font bigFont = new Font(SANS_SERIF, PLAIN, THIRTY);
        StdDraw.setFont(bigFont);
        StdDraw.text(POINT5, POINT8, "CS61B: The Game");
        Font smallFont = new Font(SANS_SERIF, PLAIN, TWENTY);
        StdDraw.setFont(smallFont);
        StdDraw.text(POINT5, POINT4, "New Game (N)");
        StdDraw.text(POINT5, POINT35, "Load Game (L)");
        StdDraw.text(POINT5, POINT3, "Change Avatar (F)");
        StdDraw.text(POINT5, POINT25, "Quit (Q)");
        StdDraw.show();
    }
    public void inputMenu() {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        Font bigFont = new Font(SANS_SERIF, PLAIN, THIRTY);
        StdDraw.setFont(bigFont);
        StdDraw.text(POINT5, POINT8, "Start New Game");
        Font smallFont = new Font(SANS_SERIF, PLAIN, TWENTY);
        StdDraw.setFont(smallFont);
        StdDraw.text(POINT3, POINT4, "Seed: ");
        inputAction();
    }

    public void inputAction() {
        double inputX = 0;
        String seed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character nextKey = StdDraw.nextKeyTyped();
                if (Character.isDigit(nextKey)) { //makes sure to get a valid seed!
                    seed += nextKey;
                    String insert = String.valueOf(nextKey);
                    StdDraw.text(POINT37 + inputX, POINT4, insert);
                    inputX += POINT03;
                }
                if (nextKey == 'S' || nextKey == 's') {
                    break;
                }
            }
        }
        long inputSeed;
        Random testRandom = new Random();
        if (seed.isBlank()) {
            inputSeed = testRandom.nextLong();
        } else {
            BigInteger seedToBigIntForCompare = new BigInteger(seed);
            if (seedToBigIntForCompare.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0
                    && seedToBigIntForCompare.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) >= 0) {
                inputSeed = Long.parseLong(seed);
            } else {
                inputSeed = testRandom.nextLong();
            }
        }
        World testWorld = new World(SIXTY, FORTY, inputSeed);
        testGame = new PlayGame(testWorld, avatarTile);
        testGame.runGame();
    }

    public void loadMenu() {
        //boolean inAvatarMenu = false;
        //boolean inGameMenu = false;
        if (FileUtils.fileExists("thisGame.txt")) {
            String loadedGame = FileUtils.readFile("thisGame.txt");
            char[] charArray = loadedGame.toCharArray();
            String seed = "";
            int index = 2;
            if (avatarMenuCircle == 0) {
                if (charArray[0] == '1') {
                    avatarTile = Tileset.AVATAR;
                } else if (charArray[0] == '2') {
                    avatarTile = Tileset.AVATARCYAN;
                } else if (charArray[0] == '3') {
                    avatarTile = Tileset.AVATARORANGE;
                }
            }
            if (charArray[1] == 'n' || charArray[1] == 'N') { //Single quotes for chars, double for strings!
                while (index < charArray.length && (charArray[index] != 's' && charArray[index] != 'S')) {
                    seed += charArray[index];
                    index++;
                }
            }
            long seedLong = Long.parseLong(seed);
            World loadWorld = new World(SIXTY, FORTY, seedLong);
            String loadedMoves = "";
            for (int i = index; i < charArray.length; i++) {
                loadedMoves += charArray[i];
            }
            PlayGame testLoadGame = new PlayGame(loadWorld, loadedMoves, avatarTile);
            testLoadGame.runGame();
        }
    }
    public void changeAvatarMenu() {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        Font bigFont = new Font(SANS_SERIF, PLAIN, THIRTY);
        StdDraw.setFont(bigFont);
        StdDraw.text(POINT5, POINT8, "Change Avatar");
        Font smallFont = new Font(SANS_SERIF, PLAIN, FIFTEEN);
        StdDraw.setFont(smallFont);
        StdDraw.text(POINT3, POINT4, "White - 1");
        StdDraw.setPenColor(Color.cyan);
        StdDraw.text(POINT5, POINT4, "Cyan - 2");
        StdDraw.setPenColor(Color.orange);
        StdDraw.text(POINT7, POINT4, "Orange - 3");
        StdDraw.setPenColor(Color.white);
        StdDraw.text(POINT5, POINT2, "Press q to go back");
        changeAvatarAction();
    }
    public void changeAvatarAction() {
        while (!quitOutOfAvatarMenu) {
            textDrawer(avatarMenuCircle);
            if (StdDraw.hasNextKeyTyped()) {
                char nextKey = StdDraw.nextKeyTyped();
                if (nextKey == '1') {
                    avatarTile = Tileset.AVATAR;
                    avatarMenuCircle = 1;
                } else if (nextKey == '2') {
                    avatarTile = Tileset.AVATARCYAN;
                    avatarMenuCircle = 2;
                } else if (nextKey == '3') {
                    avatarTile = Tileset.AVATARORANGE;
                    avatarMenuCircle = 3;
                } else if (nextKey == 'q' || nextKey == 'Q') {
                    quitOutOfAvatarMenu = true;
                    startMenuInitializer();
                }
            }
        }
    }

    public int getAvatarMenuCircle() {
        return avatarMenuCircle;
    }
    public void textDrawer(int avatarMenuCircleInput) {

        if (avatarMenuCircle == 1) {
            if (!toggle1) {
                StdDraw.setPenColor(Color.white);
                Font smallerFont = new Font(SANS_SERIF, PLAIN, 9);
                StdDraw.setFont(smallerFont);
                StdDraw.text(POINT3, POINT3, "Currently Selected");
                StdDraw.setPenColor(Color.black);
                StdDraw.text(POINT5, POINT3, "Currently Selected");
                StdDraw.text(POINT7, POINT3, "Currently Selected");
                toggle1 = true;
                toggle2 = false;
                toggle3 = false;
            }
        }
        if (avatarMenuCircle == 2) {
            if (!toggle2) {
                StdDraw.setPenColor(Color.white);
                Font smallerFont = new Font(SANS_SERIF, PLAIN, 9);
                StdDraw.setFont(smallerFont);
                StdDraw.text(POINT5, POINT3, "Currently Selected");
                StdDraw.setPenColor(Color.black);
                StdDraw.text(POINT3, POINT3, "Currently Selected");
                StdDraw.text(POINT7, POINT3, "Currently Selected");
                toggle1 = false;
                toggle2 = true;
                toggle3 = false;
            }
        }
        if (avatarMenuCircle == 3) {
            if (!toggle3) {
                StdDraw.setPenColor(Color.white);
                Font smallerFont = new Font(SANS_SERIF, PLAIN, 9);
                StdDraw.setFont(smallerFont);
                StdDraw.text(POINT7, POINT3, "Currently Selected");
                StdDraw.setPenColor(Color.black);
                StdDraw.text(POINT3, POINT3, "Currently Selected");
                StdDraw.text(POINT5, POINT3, "Currently Selected");
                toggle1 = false;
                toggle2 = false;
                toggle3 = true;
            }
        }
    }

    public void seedParse(int index, char[] charArray) {

    }
}
