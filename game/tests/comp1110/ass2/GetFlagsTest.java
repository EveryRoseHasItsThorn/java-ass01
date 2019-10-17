package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static comp1110.ass2.TestUtility.PLACEMENTS;
import static org.junit.Assert.assertTrue;

/**
 * Test objective:
 * <p>
 * Given a setup and move sequence, determine which player controls the flag of each kingdom
 * after all the moves in the sequence have been played.
 */
public class GetFlagsTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void testComprehensive() {
        for (int i = 0; i < PLACEMENTS.length; i++) {
            String setup = TestUtility.shufflePlacement(PLACEMENTS[i]);
            for (int j = 0; j < TestUtility.MOVE_SEQUENCES[i].length; j++) {
                String moveSequence = TestUtility.MOVE_SEQUENCES[i][j];
                checkFlags(TestUtility.FLAGS2[i][j], WarringStatesGame.getFlags(setup, moveSequence, 2));
                checkFlags(TestUtility.FLAGS2_SEVEN_MOVES[i][j], WarringStatesGame.getFlags(setup, moveSequence.substring(0, 7), 2));
                checkFlags(TestUtility.FLAGS3[i][j], WarringStatesGame.getFlags(setup, moveSequence, 3));
                checkFlags(TestUtility.FLAGS3_SEVEN_MOVES[i][j], WarringStatesGame.getFlags(setup, moveSequence.substring(0, 7), 3));
                checkFlags(TestUtility.FLAGS4[i][j], WarringStatesGame.getFlags(setup, moveSequence, 4));
                checkFlags(TestUtility.FLAGS4_SEVEN_MOVES[i][j], WarringStatesGame.getFlags(setup, moveSequence.substring(0, 7), 4));
            }
        }
    }

    @Test
    public void testTie() {
        checkFlags(new int[]{-1, 0, -1, 0, -1, -1, -1}, WarringStatesGame.getFlags("b5Ab6Bb0Cg1De1Eb2Fb1Gc0He0Ia5Jf0Ka7Le3Mz9Na6Oa1Pc4Qd1Re2Sd2Tg0Uf1Vb4Wf2Xc3Ya2Zc20b31c52c13a04d35d06a47a38d49", "B59", 2));
        checkFlags(new int[]{-1, -1, 0, 0, 0, 1, -1}, WarringStatesGame.getFlags("d3Ad4Be1Ca0Dc1Ed0Fa3Gc0Hc3Ia1Jb5Kb1Lc5Mg1Nf1Oa7Pb3Qe2Ra4Sd2Tb6Ua6Vb2Wd1Xf2Yb0Ze00b41a22a53e34c45z96c27g08f09", "ICAY0", 2));checkFlags(new int[]{1, 0, 2, -1, 1, 1, -1}, WarringStatesGame.getFlags("b5Af0Ba3Cc3Dd1Ee3Fb3Ge0Hf2Ib2Jg1Kb1Lz9Mc2Nf1Oa2Pd2Qb6Re2Sc1Ta5Uc5Vd0We1Xb4Yd4Za40c01d32a73c44a05a16a67g08b09", "ASUCONRLF9X30Y1D74", 3));
        checkFlags(new int[]{0, 1, 1, 1, 0, 2, 0}, WarringStatesGame.getFlags("e3Ad2Ba6Ca5Dc3Eg0Ff1Gb5Hf0Id4Ja4Kf2Ld1Mc1Nb4Oa7Pe0Qe2Rc2Sc5Te1Uc0Vg1Wa3Xd0Yb0Za20b11b22b33d34a05z96c47a18b69", "5NOR9LJHK2WE87V1YAF", 3));
        checkFlags(new int[]{-1, 3, 1, -1, -1, 2, -1}, WarringStatesGame.getFlags("d1Aa3Bf1Cf2Db1Ed4Fb2Gc1He3Iz9Je1Kd0La5Ma2Nc2Ob4Pc5Qb0Rc4Se0Ta1Ub5Vc3Wa0Xb6Ya7Zd30g11d22a63f04a45e26g07b38c09", "VS4G", 4));
    }

    @Test
    public void testDoubleTie() {
        checkFlags(new int[]{1, 0, 0, 1, -1, -1, 1}, WarringStatesGame.getFlags("a0Aa2Bb3Cd2Db1Ee1Fc4Gz9Hg0Id3Jb4Ke2Lc5Mc1Nf1Oe3Pd0Qa4Rc3Sc0Tf0Ua6Vd4Wa3Xd1Yb6Za50b51a72e03b04g15f26b27c28a19", "T5BAEWXR", 2));
        checkFlags(new int[]{0, 1, 0, 0, -1, -1, 1}, WarringStatesGame.getFlags("b0Ae2Ba7Cg1Dz9Eb6Fb3Gc3Ha4Ig0Jf0Kb4Lb1Md3Nd0Oc5Pa0Qa6Rb2Sd1Tc1Ud2Vc0Wa3Xe0Yc4Zb50a11f22d43a24a55e16e37c28f19", "DCI01VPJLF3RQ", 2));
        checkFlags(new int[]{0, -1, 2, 1, 0, 2, -1}, WarringStatesGame.getFlags("a6Ad1Be0Cc3Dc4Ed3Fb5Ge2Hc5Ic1Jb2Kd2La4Mf1Ne1Oa2Pb1Qc2Rb3Sb6Tf2Ua3Va7Wa1Xg1Yf0Zz90a01d42d03g04e35a56b47b08c09", "IC65HZ13RO", 3));
        checkFlags(new int[]{2, 3, 1, 0, 2, 1, -1}, WarringStatesGame.getFlags("a3Ac5Be1Cb4Dc2Ef2Fd3Gb6Hd4Ie3Jd1Kg1Lb2Mg0Na4Oz9Pa2Qd2Re2Se0Tc3Ub0Va5Wc1Xd0Yb3Zb50f11a12a73f04b15a66c47c08a09", "79X32Y0UC64MRFAGKEW85BTV", 4));
    }

    private void checkFlags(int[] expected, int[] actual) {
        assertTrue("Flags array is null!", actual != null);
        assertTrue("Flags array should be length 7", actual.length == 7);
        for (int i = 0; i < expected.length; i++) {
            assertTrue("Expected " + expected[i] + " but got " + actual[i], expected[i] == actual[i]);
        }
    }
}
