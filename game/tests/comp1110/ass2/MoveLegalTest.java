package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static comp1110.ass2.TestUtility.*;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test objective:
 * <p>
 * Determine whether a given move is legal given a provided valid placement:
 * - the location char is in the range A .. Z or 0..9
 * - there is a card at the chosen location;
 * - the location is in the same row or column of the grid as Zhang Yi's current position; and
 * - drawing a line from Zhang Yi's current location through the card at the chosen location,
 * there are no other cards along the line from the same kingdom as the chosen card
 * that are further away from Zhang Yi.
 */
public class MoveLegalTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testGood() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            for (char move : LEGAL_MOVE[i]) {
                assertTrue("Move '" + move + "', should be legal, but was not", WarringStatesGame.isMoveLegal(PLACEMENTS[i], move));
            }
        }
    }

    @Test
    public void testBadLocationChar() {
        Random r = new Random();
        for (int i = 0; i < PLACEMENTS.length; i++) {
            for (int iter = 0; iter < BASE_ITERATIONS; iter++) {
                char move = TestUtility.getBadLocation(r);
                assertFalse("Move '" + move + "', should be illegal (bad location), but was legal", WarringStatesGame.isMoveLegal(PLACEMENTS[i], move));
                // don't pass all false
                move = LEGAL_MOVE[i][0];
                assertTrue("Move '" + move + "', should be legal, but was not", WarringStatesGame.isMoveLegal(PLACEMENTS[i], move));
            }
        }
    }

    @Test
    public void testBadRowCol() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            for (char move : ILLEGAL_MOVE_ROW_COL[i]) {
                assertFalse("Move '" + move + "', should be illegal (not in same row/column), but was legal", WarringStatesGame.isMoveLegal(PLACEMENTS[i], move));
            }
        }
        Random r = new Random();
        checkSimpleValid(r);
    }

    @Test
    public void testNoCard() {
        for (int i = 0; i < INCOMPLETE_PLACEMENTS.length; i++) {
            for (char move : LEGAL_MOVE_INCOMPLETE[i]) {
                assertTrue("Setup " + INCOMPLETE_PLACEMENTS[i] + " move '" + move + "', should be legal, but was not", WarringStatesGame.isMoveLegal(INCOMPLETE_PLACEMENTS[i], move));
            }
            for (char move : ILLEGAL_MOVE_NO_CARD[i]) {
                assertFalse("Setup " + INCOMPLETE_PLACEMENTS[i] + " move '" + move + "', should be illegal (no card at that location), but was legal", WarringStatesGame.isMoveLegal(INCOMPLETE_PLACEMENTS[i], move));
            }
        }
    }

    @Test
    public void testFurtherAway() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            for (char move : ILLEGAL_MOVE_FURTHER[i]) {
                assertFalse("Move '" + move + "', should be illegal (a card of the same kingdom was further away in the same direction from Zhang Yi), but was not", WarringStatesGame.isMoveLegal(PLACEMENTS[i], move));
            }
        }
        Random r = new Random();
        checkSimpleValid(r);
    }

    void checkSimpleValid(Random r) {
        int p = r.nextInt(PLACEMENTS.length);
        assertTrue("Move '" + LEGAL_MOVE[0][0] + "', should be legal, but was not", WarringStatesGame.isMoveLegal(PLACEMENTS[p], LEGAL_MOVE[p][r.nextInt(LEGAL_MOVE[p].length)]));
    }
}
