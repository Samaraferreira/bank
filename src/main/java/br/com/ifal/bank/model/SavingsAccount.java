package br.com.ifal.bank.model;

public class SavingsAccount extends Account {
    public SavingsAccount(Owner owner){
        this.setOwner(owner);
        this.setBalance(0);
        this.setType("CP");
    }
}
