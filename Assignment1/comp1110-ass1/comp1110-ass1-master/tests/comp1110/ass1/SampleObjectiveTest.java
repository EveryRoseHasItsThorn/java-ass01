package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class SampleObjectiveTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(30);

    private int isInSample(int difficulty, String objective) {
        for (int i = 0; i < IQPuzzlerPro.SAMPLE_OBJECTIVES[difficulty].length; i++) {
            if (objective == IQPuzzlerPro.SAMPLE_OBJECTIVES[difficulty][i])
                return i;
        }
        return -1;
    }

    @Test
    public void usesSamples() {
        String o = IQPuzzlerPro.establishSimpleObjective(0);
        assertTrue("Objective "+o+" is not drawn from IQPuzzlerPro.SAMPLE_OBJECTIVES", isInSample(0, o) != -1);
    }

    @Test
    public void usesSamplesCorrectly() {
        String o;
        for (int d = 0; d < 4; d++) {
            o = IQPuzzlerPro.establishSimpleObjective(2.5*d);
            assertTrue("Objective " + o + " is not drawn from IQPuzzlerPro.SAMPLE_OBJECTIVES["+d+"]", isInSample(d, o) != -1);
        }
    }

    private static final int NUM_SAMPLES = 1000;
    @Test
    public void usesSamplesWell() {
        String o;
        int[] samples = new int[MAX_SAMPLE_INDEX];
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < samples.length; i++) { samples[i] = 0; }
            for (int i = 0; i < NUM_SAMPLES; i++) {
                o = IQPuzzlerPro.establishSimpleObjective(2.5*d);
                int idx = isInSample(d, o);
                assertTrue("Objective " + o + " is not drawn from IQPuzzlerPro.SAMPLE_OBJECTIVES["+d+"]", idx != -1);
                if (idx >= 0)
                    samples[idx]++;
            }
            int expected = NUM_SAMPLES / IQPuzzlerPro.SAMPLE_OBJECTIVES[d].length;
            for (int i = 0; i < IQPuzzlerPro.SAMPLE_OBJECTIVES[d].length; i++) {
                assertTrue(samples[i] + " samples drawn from index " + i + " at difficulty "+d+", but expected about "+expected, samples[i] > expected/3);
            }
        }
    }

    private static final int MAX_SAMPLE_INDEX;
    static {
        int max = 0;
        for (int i = 0; i < IQPuzzlerPro.SAMPLE_OBJECTIVES.length; i++) {
            if (IQPuzzlerPro.SAMPLE_OBJECTIVES[i].length > max)
                max = IQPuzzlerPro.SAMPLE_OBJECTIVES[i].length;
        }
        MAX_SAMPLE_INDEX = max;
    }
}
