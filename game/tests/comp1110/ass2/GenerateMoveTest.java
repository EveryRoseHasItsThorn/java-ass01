package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static comp1110.ass2.TestUtility.*;
import static junit.framework.TestCase.assertTrue;

/**
 * Test objective:
 * <p>
 * Generate a legal move, given the provided placement string.
 * A move is valid if:
 * - the location char is in the range A .. Z or 0..9
 * - there is a card at the chosen location;
 * - the destination location is different to Zhang Yi's current location;
 * - the destination is in the same row or column of the grid as Zhang Yi's current location; and
 * - drawing a line from Zhang Yi's current location through the card at the chosen location,
 * there are no other cards along the line from the same kingdom as the chosen card
 * that are further away from Zhang Yi.
 * If there is no legal move available, return the null character '\0'.
 */
public class GenerateMoveTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testStartingMove() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String placement = PLACEMENTS[i];
            char move = WarringStatesGame.generateMove(placement);
            assertTrue("WarringStatesGame.generateMove returned illegal move \'" + move + "\' for starting placement " + placement, String.valueOf(TestUtility.LEGAL_MOVE[i]).indexOf(move) >= 0);
        }
    }

    @Test
    public void testMoveWithBlanks() {
        for (int i = 0; i < INCOMPLETE_PLACEMENTS.length; i++) {
            String placement = INCOMPLETE_PLACEMENTS[i];
            char move = WarringStatesGame.generateMove(placement);
            assertTrue("WarringStatesGame.generateMove returned illegal move \'" + move + "\' for placement " + placement, String.valueOf(TestUtility.LEGAL_MOVE_INCOMPLETE[i]).indexOf(move) >= 0);
        }
    }

    @Test
    public void testNoValidMoves() {
        for (int i = 0; i < FINAL_PLACEMENT.length; i++) {
            String placement = FINAL_PLACEMENT[i];
            char move = WarringStatesGame.generateMove(placement);
            assertTrue("No valid move exists for placement " + placement + ", but WarringStatesGame.generateMove returned " + move, move == '\0');
        }
        checkSimpleValid();
    }

    private void checkSimpleValid() {
        String placement = "z91a02b6U";
        char move = WarringStatesGame.generateMove(placement);
        assertTrue("WarringStatesGame.generateMove returned illegal move \'" + move + "\' for placement " + placement + ", should have returned \'2\'", move == '2');
    }
}
