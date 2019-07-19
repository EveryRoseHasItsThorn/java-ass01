package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static comp1110.ass2.TestUtility.*;

/**
 * Test objective:
 * <p>
 * Determine whether a card placement is well-formed according to the following:
 * - it consists of exactly three characters
 * - the first character is in the range a..g (kingdom) or is z (Zhang Yi)
 * - the second character is numeric, and is a valid character number for that kingdom (9 for Zhang Yi)
 * - the third character is in the range A .. Z or 0..9 (location)
 */
public class CardPlacementWellFormedTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testSimple() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String p = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (int j = 0; j < p.length(); j += 3) {
                String test = p.substring(j, j + 3);
                assertTrue("Simple card placement string '" + test + "', should be OK, but was not", WarringStatesGame.isCardPlacementWellFormed(test));
            }
        }
    }

    @Test
    public void testCase() {
        for (int i = 0; i < PLACEMENTS[0].length(); i += 3) {
            String placement = PLACEMENTS[0].substring(i, i + 3);
            String upper = placement.toUpperCase();
            assertFalse("Simple card placement string '" + upper + "', has upper case kingdom, but passed", WarringStatesGame.isCardPlacementWellFormed(upper));
            if (placement.charAt(2) >= 'A') {
                String lower = placement.toLowerCase();
                assertFalse("Simple card placement string '" + lower + "', has lower case location, but passed", WarringStatesGame.isCardPlacementWellFormed(lower));
            }
            assertTrue("Simple card placement string '" + placement + "' failed, but should have passed", WarringStatesGame.isCardPlacementWellFormed(placement));
        }
    }

    @Test
    public void testGood() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String a = TestUtility.randomCard(r);
            char b = TestUtility.randomLocation(r);
            String test = "" + a + b;
            assertTrue("Well-formed card placement string '" + test + "' failed", WarringStatesGame.isCardPlacementWellFormed(test));
        }
    }

    @Test
    public void testBadCard() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String test = getCardPlacementBadCard(r);
            assertFalse("Badly-formed card placement string '" + test + "' passed", WarringStatesGame.isCardPlacementWellFormed(test));
        }
        checkSimpleValid(r);
    }

    public static String getCardPlacementBadCard(Random r) {
        char k, c;
        switch (r.nextInt(4)) {
            case 0:
                k = (char) (r.nextInt('a'));
                c = (char) (r.nextInt(8) + '0');
                break;
            case 1:
                k = (char) (r.nextInt(255 - 'g' - 1) + 'g' + 1);
                c = (char) (r.nextInt(8) + '0');
                break;
            case 2:
                k = (char) (r.nextInt(7) + 'a');
                c = (char) (r.nextInt('0'));
                break;
            default:
                k = (char) (r.nextInt(7) + 'a');
                c = (char) (r.nextInt(255 - '9' - 1) + '9' + 1);
        }
        return "" + k + c + TestUtility.randomLocation(r);
    }

    @Test
    public void testBadLocation() {
        Random r = new Random();
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            String test = getCardPlacementBadLocation(r);
            assertFalse("Badly-formed card placement string '" + test + "' passed", WarringStatesGame.isCardPlacementWellFormed(test));
        }
        checkSimpleValid(r);
    }

    public void checkSimpleValid(Random r) {
        String randomPlacement = TestUtility.randomCard(r) + TestUtility.randomLocation(r);
        assertTrue("Valid non-empty placement string " + randomPlacement +" is OK, but failed", WarringStatesGame.isCardPlacementWellFormed(randomPlacement));
    }
}
