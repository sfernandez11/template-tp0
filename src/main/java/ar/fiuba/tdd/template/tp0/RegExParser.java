package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import static ar.fiuba.tdd.template.tp0.Constants.*;

public class RegExParser {

    private int regExLength;

    public RegExParser() {
    }

    private static boolean isReservedChar(char character) {
        String reservedChars = ".*+?[]\\";
        return reservedChars.contains("" + character);
    }

    private static boolean isQuantifier(char character) {
        return ((character == Quantifier0o1) || (character == Quantifier0oN) || (character == Quantifier1oN));
    }

    private static boolean isCloseSet(char character) {
        return character == CloseSet;
    }

    private boolean isLastChar(int index) {
        return (this.regExLength == (index + 1));
    }

    private int checkQuantifier(List<String> tokens, String regEx, int index, String auxString) {
        index++;
        char actualChar = regEx.charAt(index);
        if (isQuantifier(actualChar)) {
            auxString = auxString + actualChar;
        } else {
            index--;
        }
        tokens.add(auxString);
        return index;
    }

    private void checkSetField(int actualIndex, int squareIndex) throws IncorrectFormatException {
        if (squareIndex == -1) {
            throw new IncorrectFormatException("El Set se abre([) pero nunca se cierra(])");
        }
        if ((squareIndex - actualIndex) == 1) {
            throw new IncorrectFormatException("No puede haber un Set vacio");
        }
    }

    private void checkSetChars(String aux, int actual) throws IncorrectFormatException {
        if (isReservedChar(aux.charAt(actual))) {
            throw new IncorrectFormatException("No puede haber un Reserved Char dentro de un Set");
        }
    }

    private int readEscapedChar(List<String> tokens, String regEx, int index) {
        int actualIndex = index + 1;
        String auxString = "";
        char auxChar = regEx.charAt(actualIndex);
        auxString = auxString + auxChar;
        if (isReservedChar(auxChar)) {
            auxString = BackSlashStr + auxString;
        }
        if (! isLastChar(actualIndex)) {
            actualIndex = checkQuantifier(tokens, regEx, actualIndex, auxString);
            return actualIndex;
        }
        tokens.add(auxString);
        return actualIndex;
    }

    private int readCharSet(List<String> tokens, String regEx, int index) throws IncorrectFormatException {
        int actualIndex = index;
        int squareBracketIndex = regEx.indexOf(CloseSet, actualIndex);
        checkSetField(actualIndex, squareBracketIndex);
        String auxString = regEx.substring(actualIndex, squareBracketIndex + 1);
        for (int i = 1; i < auxString.length() - 1; i++) {
            checkSetChars(auxString, i);
        }
        actualIndex = squareBracketIndex;
        if (! isLastChar(actualIndex)) {
            actualIndex = checkQuantifier(tokens, regEx, actualIndex, auxString);
            return actualIndex;
        }
        tokens.add(auxString);
        return actualIndex;
    }

    private int readLiteralChar(List<String> tokens, String regEx, int index) {
        int actualIndex = index;
        String auxString = "";
        auxString = auxString + regEx.charAt(actualIndex);
        if (! isLastChar(actualIndex)) {
            actualIndex = checkQuantifier(tokens, regEx, actualIndex, auxString);
            return actualIndex;
        }
        tokens.add(auxString);
        return actualIndex;
    }

    public List<String> parse(String regEx) throws IncorrectFormatException {
        List<String> tokens = new ArrayList<>();
        this.regExLength = regEx.length();
        for (int index = 0; index < regExLength; index++) {
            if (Utility.isBackSlash(regEx.charAt(index))) {
                index = readEscapedChar(tokens, regEx, index);
            } else if (Utility.isOpenSet(regEx.charAt(index))) {
                index = readCharSet(tokens, regEx, index);
            } else  if (isQuantifier(regEx.charAt(index)) || isCloseSet(regEx.charAt(index))) {
                throw new IncorrectFormatException("Error en el parseo, la RegEx tiene formato invalido.");
            } else {
                index = readLiteralChar(tokens, regEx, index);
            }
        }
        return tokens;
    }
}