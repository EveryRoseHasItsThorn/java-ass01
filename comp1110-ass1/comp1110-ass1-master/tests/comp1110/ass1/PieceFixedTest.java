package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;
import java.util.HashSet;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class PieceFixedTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(300);

    @Test
    public void onePiece() {
        assertTrue("piece G is part of the objective, but isPieceFixed returned false", new IQPuzzlerPro("GABA").isPieceFixed('G'));
        assertFalse("piece H is not part of the objective, but isPieceFixed returned false", new IQPuzzlerPro("GABA").isPieceFixed('H'));
        assertTrue("piece K is part of the objective, but isPieceFixed returned false", new IQPuzzlerPro("KDDH").isPieceFixed('K'));
        assertTrue("piece F is part of the objective, but isPieceFixed returned false", new IQPuzzlerPro("FECA").isPieceFixed('F'));
        assertTrue("piece I is part of the objective, but isPieceFixed returned false", new IQPuzzlerPro("IFAG").isPieceFixed('I'));
    }

    @Test
    public void sampleTest() {
        for (int i = 0; i < IQPuzzlerPro.SAMPLE_OBJECTIVES.length; i++) {
            for (int j = 0; j < IQPuzzlerPro.SAMPLE_OBJECTIVES[i].length; j++) {
                String objective = IQPuzzlerPro.SAMPLE_OBJECTIVES[i][j];
                IQPuzzlerPro puzzle = new IQPuzzlerPro(objective);
                HashSet<Piece> free = new HashSet<>();
                free.addAll(Arrays.asList(Piece.values()));
                for (int k = 0; k < objective.length(); k += 4) {
                    assertTrue("piece " + objective.charAt(k) + " is part of the objective, but isPieceFixed returned false", puzzle.isPieceFixed(objective.charAt(k)));
                    free.remove(Piece.valueOf(String.valueOf(objective.charAt(k))));
                }
                for (Piece freePiece : free) {
                    assertFalse("piece " + freePiece + " is not part of the objective, but isPieceFixed returned true", puzzle.isPieceFixed(freePiece.getId()));
                }
            }
        }
    }
}
