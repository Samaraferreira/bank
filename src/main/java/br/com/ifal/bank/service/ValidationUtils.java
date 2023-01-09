package br.com.ifal.bank.service;

public class ValidationUtils {

    public static boolean isValidCpf(String cpf) {
        return cpf.length() == 11;
    }
}
