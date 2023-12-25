package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.util.Random;

public class AutograderBuddy {
    private static final int SIXTY = 60;
    private static final int THIRTY = 30;


    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        long seedLong = 0;
        char[] parsedString = input.toCharArray();
        if (parsedString[0] == 'n' || parsedString[0] == 'N') {
            String newInput = getNewInput(input);
            seedLong = inputToSeed(input);
            World ourWorld = new World(SIXTY, THIRTY, seedLong);
            PlayGame helperGame = new PlayGame(ourWorld, newInput, Tileset.AVATAR);
            quitAndSaveLocal(parsedString, helperGame);
            return helperGame.getClonedTilesWithAvatar();
        } else if (parsedString[0] == 'l' || parsedString[0] == 'L') {
            if (FileUtils.fileExists("thisGame.txt")) {
                String readFile = FileUtils.readFile("thisGame.txt");
                String newInput = getNewInput(readFile) + getNewInput(input);
                //need to add old AND new moves to same world
                seedLong = inputToSeed(readFile);
                World ourWorld = new World(SIXTY, THIRTY, seedLong);
                PlayGame helperGame = new PlayGame(ourWorld, newInput, Tileset.AVATAR);
                quitAndSaveLocal(parsedString, helperGame);
                return helperGame.getClonedTilesWithAvatar();
            }
        }
        return null;
    }

    private static void quitAndSaveLocal(char[] parsedString, PlayGame helperGame) {
        if (parsedString[parsedString.length - 2] == ':' && (parsedString[parsedString.length - 1] == 'q'
                || parsedString[parsedString.length - 1] == 'Q')) {
            FileUtils.writeFile("thisGame.txt", "n" + helperGame.getThisWorld().getSeed()
                    + "s" + helperGame.getCurrWorldMoves());
        }
    }

    /*
    Returns the characters after seed input (N#S), AKA the moves. Stops at the first non-alphabetic AKA the : of :q.
    */

    private static String getNewInput(String input) { //MOVES
        char[] readFileToArray = input.toCharArray();
        String newInput = "";
        int index = 1;
        if (readFileToArray[0] == 'n' || readFileToArray[0] == 'N') { //Single quotes for chars, double for strings!
            while (index < readFileToArray.length && (readFileToArray[index] != 's' && readFileToArray[index] != 'S')) {
                index++;
            }
            index++; //moves on after the finishing S in (N#S)
        } else if (readFileToArray[0] == 'l' || readFileToArray[0] == 'L') {
            index = 1;
        }
        while (index < readFileToArray.length && Character.isAlphabetic(readFileToArray[index])) {
            newInput += readFileToArray[index];
            index++;
        }
        return newInput;
    }

    private static long inputToSeed(String input) { //SEED
        Random r = new Random();
        long seedLong;
        String stringBuild = "";
        char[] parsedString = input.toCharArray();
        if (parsedString[0] == 'n' || parsedString[0] == 'N') { //Single quotes for chars, double for strings!
            int index = 1;
            while (index < parsedString.length && (parsedString[index] != 's' && parsedString[index] != 'S')) {
                stringBuild += parsedString[index];
                index++;
            }
        }
        if (stringBuild.isBlank()) {
            seedLong = r.nextLong();
        } else {
            seedLong = Long.parseLong(stringBuild);
        }
        return seedLong;
    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.GRASS.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
