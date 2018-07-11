package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InterestingObjectiveTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(300);

    private static final Set<String> SAMPLE_OBJECTIVE_STRINGS;

    static {
        SAMPLE_OBJECTIVE_STRINGS = new HashSet<>();
        SAMPLE_OBJECTIVE_STRINGS.add(IQPuzzlerPro.TRIVIAL_OBJECTIVE);
        for (int d = 0; d < 4; d++) {
            for (int i = 0; i < IQPuzzlerPro.SAMPLE_OBJECTIVES[d].length; i++)
                SAMPLE_OBJECTIVE_STRINGS.add(IQPuzzlerPro.SAMPLE_OBJECTIVES[d][i]);
        }
    }


    private static final int FRESH_SAMPLES = 10;

    @Test
    public void usesFreshObjectives() {
        Random r = new Random();
        for (int i = 0; i < FRESH_SAMPLES; i++) {
            String o = IQPuzzlerPro.establishInterestingObjective(r.nextDouble() * 10);
            assertNotNull("No interesting objectives returned.", o);
            assertTrue("Objective " + o + " is used in the provided samples.", !SAMPLE_OBJECTIVE_STRINGS.contains(o));
        }
    }

    @Test
    public void deterministicallyDistinguishesDifficulty() {
        Map<String, Double> diffmap = new HashMap<>();
        for (int d = 0; d < 8; d++) {
            double diff = d * 10.0 / 8.0;
            for (int i = 0; i < FRESH_SAMPLES; i++) {
                String objective = IQPuzzlerPro.establishInterestingObjective(diff).toString();
                if (diffmap.containsKey(objective))
                    assertTrue("Objective " + objective + " is generated with difficulty " + diffmap.get(objective) + " and " + diff, diffmap.get(objective) == diff);
                else
                    diffmap.put(objective, diff);
            }

        }
    }

    private static final int MIN_DIFF_OBJECTIVES = 10;

    @Test
    public void generatesManyObjectives() {
        for (int d = 0; d < 8; d++) {
            double diff = d * 10.0 / 8.0;
            Set<String> objectives = new HashSet<>();
            for (int i = 0; i < FRESH_SAMPLES; i++) {
                objectives.add(IQPuzzlerPro.establishInterestingObjective(diff).toString());
            }
            int results = objectives.size();
            assertTrue("Only generated " + results + " different objectives, for difficulty " + diff + " (expected at least " + MIN_DIFF_OBJECTIVES + ")", results >= MIN_DIFF_OBJECTIVES);
        }
    }
}
