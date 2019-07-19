package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class FindSolutionsTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(300);

    @Test
    public void noSolution() {
        for (String s : NO_SOLUTIONS) {
            IQPuzzlerPro h = new IQPuzzlerPro(s);
            String[] sol = h.getSolutions();
            assertTrue("getSolutions must not return null. It should return an empty array if no solutions are found", sol != null);
            assertTrue("Objective " + s + " has no solutions, but you returned " + sol.length, sol.length == 0);
        }

    }

    @Test
    public void oneSolution() {
        for (String s : ONE_SOLUTION) {
            IQPuzzlerPro h = new IQPuzzlerPro(s);
            String[] sol = h.getSolutions();
            assertTrue("getSolutions must not return null. It should return an empty array if no solutions are found", sol != null);
            assertTrue("Objective " + s + " has one solution, but you returned " + sol.length, sol.length == 1);
            checkIncorrectTrivialSolution(s, sol[0]);
        }
    }

    @Test
    public void solutionContainsObjective() {
        for (String s : ONE_SOLUTION) {
            IQPuzzlerPro h = new IQPuzzlerPro(s);
            String[] sol = h.getSolutions();
            assertTrue("getSolutions must not return null. It should return an empty array if no solutions are found", sol != null);
            HashSet<String> piecePlacements = new HashSet<>();
            for (int i = 0; i < sol[0].length(); i += 4) {
                piecePlacements.add(sol[0].substring(i, i + 4));
            }
            for (int i = 0; i < s.length(); i += 4) {
                String placement = s.substring(i, i + 4);
                assertTrue("objective " + s + " contained the placement " + placement + " which is not found in the solution " + sol[0], piecePlacements.contains(placement));
            }
        }
    }

    @Test
    public void containsValidPlacements() {
        for (String s : ONE_SOLUTION) {
            IQPuzzlerPro h = new IQPuzzlerPro(s);
            String[] sol = h.getSolutions();
            assertTrue("getSolutions must not return null. It should return an empty array if no solutions are found", sol != null);
            for (int i = 0; i < sol[0].length(); i += 4) {
                String piecePlacement = sol[0].substring(i, i + 4);
                assertTrue("solution contains an invalid piece placement: " + piecePlacement, IQPuzzlerPro.isValidPiecePlacement(piecePlacement));
            }
            checkIncorrectTrivialSolution(s, sol[0]);
        }
    }

    @Test
    public void containsAllPiecesUniqe() {
        for (String s : ONE_SOLUTION) {
            IQPuzzlerPro h = new IQPuzzlerPro(s);
            String[] sol = h.getSolutions();
            assertTrue("getSolutions must not return null. It should return an empty array if no solutions are found", sol != null);
            HashSet<Piece> pieces = new HashSet<>();
            for (int i = 0; i < sol[0].length(); i += 4) {
                String piecePlacement = sol[0].substring(i, i + 4);
                Piece p = Piece.valueOf(piecePlacement.substring(0, 1));
                assertTrue("solution contains the piece " + p + " more than once", pieces.add(p));
            }
            for (Piece p : Piece.values()) {
                assertTrue("solution did not contain piece " + p, pieces.contains(p));
            }
            checkIncorrectTrivialSolution(s, sol[0]);
        }
    }

    @Test
    public void piecesCoverBoard() {
        for (String s : ONE_SOLUTION) {
            IQPuzzlerPro h = new IQPuzzlerPro(s);
            String[] sol = h.getSolutions();
            assertTrue("getSolutions must not return null. It should return an empty array if no solutions are found", sol != null);
            HashSet<Integer> coveredSpaces = new HashSet<>();
            for (int i = 0; i < sol[0].length(); i += 4) {
                String piecePlacement = sol[0].substring(i, i + 4);
                Piece p = Piece.valueOf(piecePlacement.substring(0, 1));
                int[] covered = p.getCovered(piecePlacement.charAt(1), piecePlacement.charAt(2), piecePlacement.charAt(3));
                for (int space : covered) {
                    assertTrue("part of piece " + p + " is off the board", space != -1);
                    assertTrue("piece " + p + " overlaps another piece at space " + space, coveredSpaces.add(Integer.valueOf(space)));
                }
            }
            for (int j = 0; j < IQPuzzlerPro.SPACES; j++) {
                assertTrue("solution does not cover space " + j, coveredSpaces.contains(Integer.valueOf(j)));
            }
        }
    }

    private void checkIncorrectTrivialSolution(String s, String s1) {
        if (s1 == IQPuzzlerPro.TRIVIAL_SOLUTION) {
            assertEquals("getSolutions returned the wrong solution " + s1 + " for the given objective", s.equals(ONE_SOLUTION[0]));
        }
    }

    private static final String[] NO_SOLUTIONS = {"ADDDBCBDCACDDJABEICDFGAAGGDGHECFIEAFKBAELAAB", "ADDACGABDEABECBAFJBBGGBBHACBJFDCKAAE", "AGAACBCADEDCEIBBFIAEGAAGHEBAIHCAKCAA", "AABABACBCGABDEABECBAFDDCGGBHHJABJHCCKAAE"};
    private static final String[] ONE_SOLUTION = {"ADDDBCBDCACDDGAAGGDGHECFIEAFKBAELAAB", "AGDDDIAAECCDFAAHGADGHBABJEBBKEAELDAD", "AAADBGAADACDEBBCFGCFIDBDJCDCKBAALFAB", "AEDCBEAADAADGHAEHBAEIDBHJADCLACC", "AADDBCADCFAADDABEBCDGAAHHDDAIFCF", "AFAABEDCCABDDCABIAAHJADCKEADLGBD"};
}
