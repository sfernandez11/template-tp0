package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {
    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) throws IncorrectFormatException {
        List<String> results = new ArrayList<>();
        if (regEx.isEmpty()) {
            return results;
        }
        List<String> tokens = new RegExParser().parse(regEx);
        String actualResult = "";
        StringGenerator generator = new StringGenerator(this.maxLength);
        for (int i = 0; i < numberOfResults - 1; i++) {
            for (String token : tokens) {
                actualResult = actualResult.concat(generator.randomGenerate(token));
            }
            results.add(actualResult);
            actualResult = "";
        }
        return results;
    }
}