package br.com.ifal.bank.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
