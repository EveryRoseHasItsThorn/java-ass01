package comp1110.ass2;

import org.junit.Test;

import static comp1110.ass2.TestUtility.PLACEMENTS;
import static org.junit.Assert.*;

public class getAllSupportersTest {

    @Test
    public void supportersEnoughTest() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (int j = 0; j < TestUtility.MOVE_SEQUENCES[i].length; j++) {
                String moveSequence = TestUtility.MOVE_SEQUENCES[i][j];
                String allSupporters = "";
                for (int p = 0; p < 4; p++) {
                    String supporters = WarringStatesGame.getSupporters(setup, moveSequence, 4, p);
                    allSupporters = allSupporters + supporters;
                }
                assertTrue("the supporters of all players should be larger or equal than" + moveSequence + " move." + " but the total supporters " + allSupporters + " is smaller", +allSupporters.length() / 2 >= moveSequence.length());
            }
        }
    }

    //test the case when after setup and a sequence of move,
    //the total supporter of all player must greater than the numbers of movement,
    // that is, in each move, there will be at least one piece arranged to a player.
    @Test
    public void characterEqualInTotalTest() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (int j = 0; j < TestUtility.MOVE_SEQUENCES[i].length; j++) {
                String moveSequence = TestUtility.MOVE_SEQUENCES[i][j];
                String allSupprter = "";
                for (int p = 0; p < 4; p++) {
                    String supporters = WarringStatesGame.getSupporters(setup, moveSequence, 4, p);
                    allSupprter = allSupprter + supporters;
                }
                String finalPlacement = setup;
                for (char r : moveSequence.toCharArray()) {
                    finalPlacement = WarringStatesGame.updateBoard(finalPlacement, r);

                }
                assertTrue("the supporters of every player plus the remaining peice should equal to 35", finalPlacement.length() / 3 + allSupprter.length() / 2 == 36);

            }
        }
    }
    //test the relationship between a supporter and the remaining piece on the board.
    // if the numbers of supporter in total of all players is equal to n, than there must
    // be 36-n pieces remaining on the board.
}