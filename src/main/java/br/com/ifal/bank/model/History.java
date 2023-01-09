package br.com.ifal.bank.model;

import java.util.Date;

public class History {
    private String cpf;
    private Date createAt;
    private String type;

    private String movement;

    public History(String cpf, Date createAt, String type, String movement) {
        this.cpf = cpf;
        this.createAt = createAt;
        this.type = type;
        this.movement = movement;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
