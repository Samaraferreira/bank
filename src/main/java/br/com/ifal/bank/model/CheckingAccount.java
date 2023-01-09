package br.com.ifal.bank.model;

public class CheckingAccount extends Account {

    public CheckingAccount(Owner owner) {
        this.setOwner(owner);
        this.setBalance(0);
        this.setType("CC");
    }
}
