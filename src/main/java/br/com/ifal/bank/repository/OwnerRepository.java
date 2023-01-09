package br.com.ifal.bank.repository;

import br.com.ifal.bank.database.BuidConection;
import br.com.ifal.bank.model.Owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerRepository {

    public String addOwner(Owner owner) throws SQLException {
        Connection con = new BuidConection().getCon();
        PreparedStatement ps;
        ResultSet rs;
        String ownerId = null;

        String insertOwnerQuery = "insert into owner(name, cpf, birthDate) values(?,?,?) returning cpf";

        verifyCpf(owner);

        ps = con.prepareStatement(insertOwnerQuery);

        ps.setString(1, owner.getName());
        ps.setString(2, owner.getCpf());
        ps.setString(3, owner.getBirthDate());
        rs = ps.executeQuery();

        if(rs.next()) {
            ownerId = rs.getString("cpf");
        }

        ps.close();
        con.close();

        return ownerId;
    }

    private static void verifyCpf(Owner owner) throws SQLException {
        Connection con = new BuidConection().getCon();

        String verifyIfCpfAlreadyExistsQuery = "select cpf from owner where cpf = ?";

        String validationCpf = null;
        PreparedStatement ps = con.prepareStatement(verifyIfCpfAlreadyExistsQuery);;
        ps.setString(1, owner.getCpf());
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            validationCpf = rs.getString("cpf");
        }

        if(validationCpf != null){
            throw new SQLException("Não é possível cadastrar mais de uma conta poupança com o mesmo cpf");
        }

        ps.close();
        con.close();
    }
}
