package comp1110.ass2;

import org.junit.Test;

import static comp1110.ass2.TestUtility.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class isGameOverTest {

    @Test
    public void trivialOver() {
        String placement = "";
        assertTrue("Simple card placement" + placement + "should be finished, but ", Rule.isGameOver(placement));
    }
//test case if there is no placement on the board at all,
// the game should be over already.
    @Test
    public void trivialSetUp() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String r = TestUtility.shufflePlacement(PLACEMENTS[i]);
            assertFalse("Sample placement " + r + " is just the setup if the game, but ,", Rule.isGameOver(r));
        }
    }
//test case when the game has just setup, so all the piece is on the board,
//it is impossible for a game to be finished when it just start.
    @Test
    public void testWhilePlaying() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String r = TestUtility.shufflePlacement(PLACEMENTS[i]);
            char t = WarringStatesGame.generateMove(r);
            String updated = WarringStatesGame.updateBoard(r, t);
            assertFalse("Sample placement " + r + " is still in the playing process, but ,", Rule.isGameOver(updated));
        }
    }
//test case when the game start to play,
//that is the game has already start and some piece has been remove
//but it did not finished.
    @Test
    public void testOnlyOne() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String r = TestUtility.shufflePlacement(PLACEMENTS[i]);
            String last=r.substring(0,3);
            assertTrue("Sample placement " + last + " is the last placement and the game should be over", Rule.isGameOver(last));
        }
            }
//test case when with one more step,
//the game will finish
//when a moveSequence have n move and it is legal
//in n-1 move. The game can not be over.
}