package br.com.ifal.bank.model;

public class Credit {
    private String cpf;
    private double credit;

    public Credit(String cpf, double credit){
        setCpf(cpf);
        setCredit(credit);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
}
