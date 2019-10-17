package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static comp1110.ass2.TestUtility.BASE_ITERATIONS;
import static comp1110.ass2.TestUtility.PLACEMENTS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test objective:
 * <p>
 * Determine whether a placement string is well-formed:
 * - it consists of exactly N three-character card placements (where N = 1 .. 36);
 * - each card placement is well-formed
 * - no card appears more than once in the placement
 * - no location contains more than one card
 */
public class PlacementWellFormedTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    private static final int NUM_CARDS = 36;

    @Test
    public void testEmpty() {
        assertFalse("Null placement string is not OK, but passed", WarringStatesGame.isPlacementWellFormed(null));
        assertFalse("Empty placement string is not OK, but passed", WarringStatesGame.isPlacementWellFormed(""));
        Random r = new Random();
        checkSimpleValid(r);
    }

    @Test
    public void testIncomplete() {
        Random r = new Random();
        for (String placement : PLACEMENTS) {
            String incompleteCard = placement.substring(r.nextInt(NUM_CARDS) * 3 + 1);
            assertFalse("Placement string '" + incompleteCard + "'was incomplete, but passed", WarringStatesGame.isPlacementWellFormed(incompleteCard));
            String incompleteLocation = placement.substring(r.nextInt(NUM_CARDS) * 3 + 2);
            assertFalse("Placement string '" + incompleteLocation + "'was incomplete, but passed", WarringStatesGame.isPlacementWellFormed(incompleteLocation));
        }
        checkSimpleValid(r);
    }

    @Test
    public void testGood() {
        Random r = new Random();

        for (int i = 0; i < PLACEMENTS.length; i++) {
            String p = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (int j = 0; j < BASE_ITERATIONS; j++) {
                int start = r.nextInt(4);
                int end = start + r.nextInt(NUM_CARDS - start) + 1;
                String test = p.substring(3 * start, 3 * end);
                assertTrue("Placement '" + test + "' is well-formed, but failed ", WarringStatesGame.isPlacementWellFormed(test));
            }
        }
    }

    @Test
    public void testBad() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String badCard = CardPlacementWellFormedTest.getCardPlacementBadCard(r);
            assertFalse("Placement '" + badCard + "' is badly formed, but passed", WarringStatesGame.isPlacementWellFormed(badCard));
            String badLocation = TestUtility.getCardPlacementBadLocation(r);
            assertFalse("Placement '" + badLocation + "' is badly formed, but passed", WarringStatesGame.isPlacementWellFormed(badLocation));
        }
        checkSimpleValid(r);
    }

    @Test
    public void testDuplicate() {
        Random r = new Random();

        for (int i = 0; i < PLACEMENTS.length; i++) {
            String p = PLACEMENTS[i];
            int dup = r.nextInt(NUM_CARDS);
            int victim = (dup + 1 + r.nextInt(NUM_CARDS-1)) % NUM_CARDS;

            String duplicateCard = p.substring(3 * dup, 3 * dup + 2);
            String base = p.substring(0, 3 * victim) + duplicateCard + p.substring(3 * victim + 2, p.length());
            String test = TestUtility.shufflePlacement(base);
            assertFalse("Placement '" + test + "' uses card '" + duplicateCard + "' twice, but passed.", WarringStatesGame.isPlacementWellFormed(test));

            String duplicateLocation = p.substring(3 * dup + 2, 3 * (dup + 1));
            base = p.substring(0, 3 * victim + 2) + duplicateLocation + p.substring(3 * (victim + 1), p.length());
            test = TestUtility.shufflePlacement(base);
            assertFalse("Placement '" + test + "' uses location '" + duplicateLocation + "' twice, but passed.", WarringStatesGame.isPlacementWellFormed(test));
        }
        checkSimpleValid(r);
    }

    public void checkSimpleValid(Random r) {
        assertTrue("Valid non-empty placement string is OK, but failed", WarringStatesGame.isPlacementWellFormed(TestUtility.randomCard(r) + TestUtility.randomLocation(r)));
    }
}

