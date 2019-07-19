package comp1110.ass2;

import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static comp1110.ass2.TestUtility.LEGAL_MOVE;
import static comp1110.ass2.TestUtility.PLACEMENTS;
import static org.junit.Assert.*;

public class updateBoardTest {

    @Test
    public void legalTest() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            for (char move : LEGAL_MOVE[i]) {
                assertTrue("after move" + move + " the placement" + PLACEMENTS[i] + "length mod 3 = 0", (WarringStatesGame.updateBoard(PLACEMENTS[i], move).length()) % 3 == 0);
            }
        }
    }
    // all placement must be legal, so after update,
    // the new placement should be legal too
    //(basis requirement is the length of placement mod 3 equal to 0.
    @Test
    public void lengthTest() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String sample = TestUtility.shufflePlacement(PLACEMENTS[i]);
            Random r = new Random();
            char move = (char) (r.nextInt(26) + 'A');
            String afterMove = WarringStatesGame.updateBoard(sample, move);
            if (WarringStatesGame.isMoveLegal(sample, move))
                assertTrue("the length of plecement after legal move should decrease 3 or more", afterMove.length() <= sample.length() - 3);
        }
    }
    // after update,the length of the updated placement should be equal
    // or smaller than the length of origin placement minus 3
    // equal: the move just take one piece.
    // smaller: the move takes more than one piece.
    @Test
    public void zhangYiTest() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String sample = TestUtility.shufflePlacement(PLACEMENTS[i]);
            Random r = new Random();
            char move = (char) (r.nextInt(26) + 'A');
            String afterMove = WarringStatesGame.updateBoard(sample, move);
            if (WarringStatesGame.isMoveLegal(sample, move)) {
                char obj = '/';
                for (int t = 0; t < afterMove.length(); t += 3)
                    if (afterMove.charAt(t) == 'z') {
                        obj = afterMove.charAt(t + 2);
                        assertTrue("zhangYi should have already moved to " + move + " but now at " + obj, obj == move);
                    }
            }
        }
    }
    // test the location of zhangYi after each move.
    // the origin zhangYiLocation should be discarded,
    // the the new zhangYiLocation should be in the move destionation.
    @Test
    public void nonePiecePlacement() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            Set<Character> after = new HashSet<>();
            char ori = '/';
            String sample = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (int l = 0; l < sample.length(); l += 3)
                if (sample.charAt(i) == 'z')
                    ori = sample.charAt(i + 2);
            Random r = new Random();
            char move = (char) (r.nextInt(26) + 'A');
            String afterMove = WarringStatesGame.updateBoard(sample, move);
            if (WarringStatesGame.isMoveLegal(sample, move))
                for (int d = 0; d < afterMove.length(); d += 3)
                    after.add(afterMove.charAt(d + 2));
            assertFalse("the grid" + ori + " should be empty, but not", after.contains(ori)&&ori!='/');
        }
    }
    // test for the piece arrangement. after the each step of move,
    // the origin placement of origin zhangYi should no longer exist
    // in the placement string.
}