package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private int maxLength = 15;

    private boolean validate(String regEx, int numberOfResults) {
        RegExGenerator generator = new RegExGenerator(this.maxLength);
        List<String> results;
        try {
            results = generator.generate(regEx, numberOfResults);
        } catch (IncorrectFormatException incorrectFormatException) {
            return false;
        }
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                        (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                        (item1, item2) -> item1 && item2);
    }


    @Test
    public void testAnyCharacter() {
        assertTrue(validate(".", 1));
    }

    @Test
    public void testMultipleCharacters() {
        assertTrue(validate("...", 1));
    }

    @Test
    public void testLiteral() {
        assertTrue(validate("\\@", 1));
    }

    @Test
    public void testLiteralDotCharacter() {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        assertTrue(validate("[abc]+", 1));
    }


    @Test
    public void testZeroOrMoreCharacter() {
        assertTrue(validate("z*", 10));
    }

    @Test
    public void testNegativeOrError() {
        assertFalse(validate("jkl++", 10));
        assertFalse(validate("[]", 15));
        assertFalse(validate("[[e]", 10));
        assertFalse(validate("[]]", 10));
    }

    @Test
    public void testDotWithQuantifiers() {
        assertTrue(validate(".*", 10));
        assertTrue(validate(".+", 10));
        assertTrue(validate(".?", 10));
    }

    @Test
    public void testALotOfSets() {
        assertTrue(validate("[aeqdc]q[wdca]+[hjk]*[rwxc]?..*", 10));
    }

    @Test
    //No puede haber caracteres reservados dentro de un Set
    public void testEscapedLiteralsOnSet() {
        assertFalse(validate("[scsd[]dwd]*", 5));
        assertFalse(validate("[a*dwdq]", 10));
        assertFalse(validate("[a3?]", 10));
        assertFalse(validate("[avvcs+]", 10));
        assertFalse(validate("[qw.c??]", 20));
    }

    @Test
    public void testEscapedLiteralWithQuantifiers() {
        assertTrue(validate("\\.*", 10));
        assertTrue(validate("\\+*", 10));
        assertTrue(validate("\\?*", 10));
        assertTrue(validate("\\**", 10));
        assertTrue(validate("\\+?", 10));
        assertTrue(validate("\\*+", 10));
    }

    @Test
    public void testExhaustive() {
        assertTrue(validate("ab*.+\\h\\+[cde]?.[fg]", 1));
    }

    @Test
    public void testVeryExhaustive() {
        assertTrue(validate("wqv\\..+d*w?[zxy]+p", 50));
    }

    @Test
    public void testAnyCharacterWithAsterisk() {
        assertTrue(validate(".*", 1));
    }

    @Test
    public void testLiteralWithAsterisk() {
        assertTrue(validate("&*", 1));
    }

    @Test
    public void testAnyCharacterWithPlus() {
        assertTrue(validate(".*", 1));
    }

    @Test
    public void testLiteralWithPlus() {
        assertTrue(validate("´+", 1));
    }

    @Test
    public void testAnyCharacterWithQuestionMark() {
        assertTrue(validate(".?", 1));
    }

    @Test
    public void testMultipleAnyCharacters() {
        assertTrue(validate(".*.*.*", 1));
    }

    @Test
    public void testaaaMultipleAnyaCharacters() {
        assertTrue(validate("2.123.*", 1));
    }

    @Test
    public void testBackslashedDotWithLiteral() {
        assertTrue(validate("\\..72.*", 1));
    }

    @Test
    public void testBackslashedDot() {
        assertTrue(validate("\\.", 1));
    }

    @Test
    public void testBackslashedLiteral() {
        assertTrue(validate("\\7", 1));
    }

    @Test
    public void testNumber() {
        assertTrue(validate("23.2212", 1));
    }

    @Test
    public void testAlpha() {
        assertTrue(validate("sdsd+.6.231.3.*", 1));
    }

    @Test
    public void testWithBackSlashAndAlpha() {
        assertTrue(validate("\\*zxe?.2.3.1", 1));
    }

    @Test
    public void testNormalSetAndLiteral() {
        assertTrue(validate("[asvg]llld", 1));
    }

    @Test
    public void testNormalSetAndLiteralWithQuantifier() {
        assertTrue(validate("[asvg]llld*", 1));
    }
}
