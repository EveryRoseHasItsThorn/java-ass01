package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static comp1110.ass2.TestUtility.PLACEMENTS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test objective:
 * <p>
 * Determine whether a move sequence is valid.
 * To be valid, the move sequence must be comprised of 1..N location characters
 * showing the location to move for Zhang Yi, and each move must be valid
 * given the placement that would have resulted from the preceding sequence
 * of moves.
 */
public class MoveSequenceValidTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testGood() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (String moveSequence : TestUtility.MOVE_SEQUENCES[i]) {
                assertTrue("Move sequence " + moveSequence + " should be valid, but was rejected", WarringStatesGame.isMoveSequenceValid(setup, moveSequence));
            }
        }
    }

    @Test
    public void testBadLocation() {
        Random r = new Random();
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (String moveSequence : TestUtility.MOVE_SEQUENCES[i]) {
                int removeChar = r.nextInt(moveSequence.length() - 1);
                moveSequence = moveSequence.substring(0, removeChar) + TestUtility.getBadLocation(r) + moveSequence.substring(removeChar + 1);
                assertFalse("Move sequence " + moveSequence + " for setup " + setup + " is invalid, but was accepted", WarringStatesGame.isMoveSequenceValid(setup, moveSequence));
            }
        }
        checkSimpleValid();
    }

    @Test
    public void testDuplicateMove() {
        Random r = new Random();
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (String moveSequence : TestUtility.MOVE_SEQUENCES[i]) {
                int duplicateChar = r.nextInt(moveSequence.length() - 1);
                moveSequence = moveSequence.substring(0, duplicateChar) + moveSequence.charAt(duplicateChar) + moveSequence.substring(duplicateChar);
                assertFalse("Move sequence " + moveSequence + " for setup " + setup + " is invalid (duplicated move), but was accepted", WarringStatesGame.isMoveSequenceValid(setup, moveSequence));
            }
        }
        checkSimpleValid();
    }

    @Test
    public void testEmptyChar() {
        Random r = new Random();
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (String moveSequence : TestUtility.MOVE_SEQUENCES[i]) {
                int duplicateChar = r.nextInt(moveSequence.length() - 2) + 1;
                moveSequence = moveSequence.substring(0, duplicateChar) + moveSequence.charAt(r.nextInt(duplicateChar)) + moveSequence.substring(duplicateChar + 1);
                assertFalse("Move sequence " + moveSequence + " for setup " + setup + " is invalid (attempted move to empty space), but was accepted", WarringStatesGame.isMoveSequenceValid(setup, moveSequence));
            }
        }
        checkSimpleValid();
    }

    @Test
    public void testBadMoves() {
        Random r = new Random();
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (String moveSequence : TestUtility.BAD_MOVE_SEQUENCES[i]) {
                assertFalse("Move sequence " + moveSequence + " for setup " + setup + " is invalid, but was accepted", WarringStatesGame.isMoveSequenceValid(setup, moveSequence));
            }
        }
        checkSimpleValid();
    }

    private void checkSimpleValid() {
        assertTrue("Move sequence " + TestUtility.MOVE_SEQUENCES[0][0] + " for setup " + PLACEMENTS[0] + " should be valid, but was rejected", WarringStatesGame.isMoveSequenceValid(TestUtility.PLACEMENTS[0], TestUtility.MOVE_SEQUENCES[0][0]));
    }
}
