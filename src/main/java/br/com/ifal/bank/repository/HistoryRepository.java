package br.com.ifal.bank.repository;

import br.com.ifal.bank.database.BuidConection;
import br.com.ifal.bank.model.History;
import br.com.ifal.bank.model.SavingsAccount;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class HistoryRepository {
    public void save(String cpf, String type, String movement) {
        Connection con = new BuidConection().getCon();

        PreparedStatement ps = null;

        String sqlInsert = "insert into history(cpf_account, type_account, movement) values(?,?,?)";

        try {
            ps = con.prepareStatement(sqlInsert);

            ps.setString(1, cpf);
            ps.setString(2, type);
            ps.setString(3, movement);


            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public ArrayList<History> select(String cpf) {
        Connection con = new BuidConection().getCon();
        ArrayList<History> histories = new ArrayList<>();

        PreparedStatement ps = null;

        String sqlInsert = "SELECT * FROM history WHERE cpf_account = ?";

        try {
            ps = con.prepareStatement(sqlInsert);

            ps.setString(1, cpf);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                History history = new History(rs.getString("cpf_account"),
                        rs.getDate("created_at"),
                        rs.getString("type_account"),
                        rs.getString("movement"));

                histories.add(history);
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return histories;

    }

}
