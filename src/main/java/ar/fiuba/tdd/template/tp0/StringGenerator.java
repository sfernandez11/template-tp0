package ar.fiuba.tdd.template.tp0;

import java.util.Random;

import static ar.fiuba.tdd.template.tp0.Constants.*;

public class StringGenerator {
    private int maxLength;

    public StringGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    private static boolean isDot(char character) {
        return (character == Dot);
    }

    private boolean isExcludedNumber(int index) {
        return ((index == ExcludeNumber1) || (index == ExcludeNumber2) || (index == ExcludeNumber3));
    }

    private int hasQuantifier(String token) {
        int tokenLength = token.length();
        char tokenQuantifier = token.charAt(tokenLength - 1);
        return getNumberOfRepetitions(tokenQuantifier);
    }

    private String charToString(char character) {
        return new StringBuilder().append(character).toString();
    }

    private int getNumberOfRepetitions(char quantifier) {
        switch (quantifier) {
            case Quantifier0o1: {
                return getRandomNumber(0, 1);
            }
            case Quantifier0oN: {
                return getRandomNumber(0, this.maxLength);
            }
            case Quantifier1oN: {
                return getRandomNumber(1, this.maxLength);
            }
            default: {
                return 1;
            }
        }
    }

    private int getRandomNumber(int minBound, int maxBound) {
        Random random = new Random();
        return (minBound + (random.nextInt(maxBound - minBound + 1)));
    }

    private String generateLiteralString(String token) {
        String result = "";
        char first = token.charAt(0);
        String auxString = charToString(first);
        boolean dot = isDot(first);
        int repetitions = hasQuantifier(token);
        for (int i = 0; i < repetitions ; i++) {
            if (dot) {
                int randomIndex = getRandomNumber(0, ASCIISpace);
                while (isExcludedNumber(randomIndex)) {
                    randomIndex = getRandomNumber(0, ASCIISpace);
                }
                auxString = charToString((char) randomIndex);
            }
            result = result.concat(auxString);
        }
        return result;
    }

    private String generateBSString(String token) {
        String result = "";
        String auxString = charToString(token.charAt(1));
        int repetitions = 1;
        if (token.length() > 2) {
            repetitions = hasQuantifier(token);
        }
        for (int i = 0; i < repetitions; i++) {
            result = result.concat(auxString);
        }
        return result;
    }

    private String generateSetString(String token) {
        String result = "";
        String auxString;
        token = token.replace(BackSlashStr, "");
        int squareIndex = token.indexOf("]");
        String set = token.substring(1, squareIndex);
        int repetitions = hasQuantifier(token);
        for (int i = 0; i < repetitions; i++) {
            int randomIndex = getRandomNumber(0, set.length() - 1);
            auxString = charToString(set.charAt(randomIndex));
            result = result.concat(auxString);
        }
        return result;
    }

    public String randomGenerate(String token) {
        String result;
        char first = token.charAt(0);
        if (Utility.isBackSlash(first)) {
            result = generateBSString(token);
        } else if (Utility.isOpenSet(first)) {
            result = generateSetString(token);
        } else {
            result = generateLiteralString(token);
        }
        return result;
    }
}
