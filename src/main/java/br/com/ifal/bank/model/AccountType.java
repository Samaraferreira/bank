package br.com.ifal.bank.model;

public enum AccountType {
    SAVINGS("CP", "Poupança"),
    CHECKING("CC", "Corrente");

    private String abbreviation;
    private String description;
    
    AccountType(String abbreviation, String description) {
        this.abbreviation = abbreviation;
        this.description = description;
    }
    
    public static String getDescription(String value) {
        for(AccountType accountType : AccountType.values()) {
            if(accountType.abbreviation.equals(value)) {
                return accountType.description;
            }
        }
        return "";
    }
}
