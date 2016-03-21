package ar.fiuba.tdd.template.tp0;

public class IncorrectFormatException extends Exception {

    private String msg;

    public IncorrectFormatException(String msg) {
        this.msg = msg;
    }
}

