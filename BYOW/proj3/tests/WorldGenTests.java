import core.AutograderBuddy;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;

public class WorldGenTests {
    @Test
    public void basicTest() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n1234567890123456789ssss");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
//        StdDraw.pause(5000); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicInteractivityTest() {
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n5197880843569031643sssss");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
//        StdDraw.pause(5000);
        // TODO: write a test that uses an input like "n123swasdwasd"
    }

    @Test
    public void basicSaveTest() {
        TETile[][] tiles1 = AutograderBuddy.getWorldFromInput("N999SDDDWWWDDDaaa");
        TERenderer ter = new TERenderer();
        ter.initialize(tiles1.length, tiles1[0].length);
        ter.renderFrame(tiles1);
        StdDraw.pause(5000);
        // TODO: write a test that calls getWorldFromInput twice, with "n123swasd:q" and with "lwasd"
    }

    @Test
    public void basicSaveTest2() {
        TETile[][] tiles2 = AutograderBuddy.getWorldFromInput("N999SDDD:Q");
        tiles2 = AutograderBuddy.getWorldFromInput("LWWWDDDaaa");
        TERenderer ter = new TERenderer();
        ter.initialize(tiles2.length, tiles2[0].length);
        ter.renderFrame(tiles2);
        StdDraw.pause(5000);

        // TODO: write a test that calls getWorldFromInput twice, with "n123swasd:q" and with "lwasd"
    }


    @Test
    public void basicSaveTest4() {
        TETile[][] tiles2 = AutograderBuddy.getWorldFromInput("N999SDDD:Q");
        tiles2 = AutograderBuddy.getWorldFromInput("LWWW:Q");
        tiles2 = AutograderBuddy.getWorldFromInput("LDDDaaa:Q");
        TERenderer ter = new TERenderer();
        ter.initialize(tiles2.length, tiles2[0].length);
        ter.renderFrame(tiles2);
        StdDraw.pause(5000);

        // TODO: write a test that calls getWorldFromInput twice, with "n123swasd:q" and with "lwasd"
    }
}