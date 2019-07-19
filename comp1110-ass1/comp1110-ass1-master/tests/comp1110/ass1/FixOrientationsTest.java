package comp1110.ass1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;


public class FixOrientationsTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(30);

    @Test
    public void testEmpty() {
        String out = IQPuzzlerPro.fixOrientations("");
        assertTrue("Expected an empty string, but got \"" + out + "\"", out.equals(""));
    }

    @Test
    public void testStandardOrientations() {
        String out = IQPuzzlerPro.fixOrientations("ABAABDABCAACDAAD");
        assertTrue("Expected \"ABAABDABCAACDAAD\", but got \"" + out + "\"", out.equals("ABAABDABCAACDAAD"));
        out = IQPuzzlerPro.fixOrientations("AAAABBBACCABDFDC");
        assertTrue("Expected \"AAAABBBACCABDFDC\", but got \"" + out + "\"", out.equals("AAAABBBACCABDFDC"));
    }

    @Test
    public void testFlippedOrientationsNoChange() {
        String out = IQPuzzlerPro.fixOrientations("CABAHAADJAAFGAAGIAAH");
        assertTrue("Expected \"CABAHAADJAAFGAAGIAAH\", but got \"" + out + "\"", out.equals("CABAHAADJAAFGAAGIAAH"));
    }

    @Test
    public void testFlippedOrientationsChange() {
        String out = IQPuzzlerPro.fixOrientations("ADDHBCBHCACHDGAAEECFFHBAGGDGHICDIEAFJJABKBAELAAB");
        assertTrue("Expected \"ADDDBCBDCACDDGAAEECBFHBAGGDGHICDIEAFJJABKBAELAAB\", but got \"" + out + "\"", out.equals("ADDDBCBDCACDDGAAEECBFHBAGGDGHICDIEAFJJABKBAELAAB"));
    }

    @Test
    public void testJavadocExamples() {
        String out = IQPuzzlerPro.fixOrientations("ADDDBGAACECF");
        assertTrue("Expected \"ADDDBGAACECB\", but got \"" + out + "\"", out.equals("ADDDBGAACECB"));
        out = IQPuzzlerPro.fixOrientations("BDAGFGEHGABA");
        assertTrue("Expected \"BDACFGEHGABA\", but got \"" + out + "\"", out.equals("BDACFGEHGABA"));
    }
}
