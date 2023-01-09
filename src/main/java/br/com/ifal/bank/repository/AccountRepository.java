package br.com.ifal.bank.repository;

import br.com.ifal.bank.database.BuidConection;
import br.com.ifal.bank.model.Account;
import br.com.ifal.bank.model.CheckingAccount;
import br.com.ifal.bank.model.Owner;
import br.com.ifal.bank.model.SavingsAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRepository {

    public Integer insertSavingsAccount(String ownerId, double balance, String type){
        Connection con = new BuidConection().getCon();

        Integer accountID = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String insertAccountQuery = "insert into savings_account(owner_id, balance, type_account) values(?,?,?) returning id";

        try {

            ps = con.prepareStatement(insertAccountQuery);
            ps.setString(1, ownerId);
            ps.setDouble(2, balance);
            ps.setString(3, type);
            rs = ps.executeQuery();

            if (rs.next()) {
                accountID = rs.getInt("id");
            }

            ps.close();
            con.close();
            return accountID;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return accountID;
        }
    }

    public Integer insertCheckingAccount(String ownerId, double balance, String type){

        Connection con = new BuidConection().getCon();

        Integer accountID = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String insertAccountQuery = "insert into checking_account(owner_id, balance, type_account) values(?,?,?) returning id";

        try{
            ps = con.prepareStatement(insertAccountQuery);
            ps.setString(1, ownerId);
            ps.setDouble(2, balance);
            ps.setString(3, type);
            rs = ps.executeQuery();

            if (rs.next()) {
                accountID = rs.getInt("id");
            }

            ps.close();
            con.close();
            return accountID;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return accountID;
        }
    }

    public double depositAccount(String type, String cpf, double value){

        Connection con = new BuidConection().getCon();

        double currentBalance = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{

            if(type.equals("CP")) {
                String updateBalanceSavingsQuery = "update savings_account set balance = ? where owner_id = ? returning balance";
                String selectBalanceSavingsQuery = "select balance from savings_account where owner_id = ?";

                ps = con.prepareStatement(selectBalanceSavingsQuery);
                ps.setString(1, cpf);

                rs = ps.executeQuery();

                double balanceSavings = 0;
                if (rs.next()) {
                    balanceSavings = rs.getDouble("balance");
                }

                ps.close();

                ps = con.prepareStatement(updateBalanceSavingsQuery);

                currentBalance = balanceSavings + value;
                ps.setDouble(1, balanceSavings + value);

                ps.setString(2, cpf);

            } else if(type.equals("CC")) {
                String updateBalanceCheckingQuery = "update checking_account set balance = ? where owner_id = ? returning balance";
                String selectBalanceCheckingQuery = "select balance from checking_account where owner_id = ?";

                ps = con.prepareStatement(selectBalanceCheckingQuery);
                ps.setString(1, cpf);

                rs = ps.executeQuery();

                double balanceCheckings = 0;
                if (rs.next()) {
                    balanceCheckings = rs.getDouble("balance");
                }

                ps.close();

                ps = con.prepareStatement(updateBalanceCheckingQuery);

                currentBalance = balanceCheckings + value;
                ps.setDouble(1, currentBalance);

                ps.setString(2, cpf);

            }

            ps.executeQuery();

            ps.close();
            con.close();

            return currentBalance;
        }catch(Exception e){
            if(!e.getMessage().equals("No results were returned by the query.")){
                System.out.println(e.getMessage());
            }
            return currentBalance;
        }
    }

    public double withdrawAccount(String type, String cpf, double value){

        Connection con = new BuidConection().getCon();

        double currentBalance = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            if(type.equals("CP")) {
                String updateBalanceSavingsQuery = "update savings_account set balance = ? where owner_id = ? returning balance";
                String selectBalanceSavingsQuery = "select balance from savings_account where owner_id = ?";

                ps = con.prepareStatement(selectBalanceSavingsQuery);

                ps.setString(1, cpf);

                rs = ps.executeQuery();

                double balanceSavings = 0;
                if (rs.next()) {
                    balanceSavings = rs.getDouble("balance");
                }

                ps.close();

                ps = con.prepareStatement(updateBalanceSavingsQuery);

                if(balanceSavings < value){
                    throw new ArithmeticException("O valor do saque não pode ser maior que o saldo!");
                }

                currentBalance = balanceSavings - value;
                ps.setDouble(1, currentBalance);

                ps.setString(2, cpf);

            } else if(type.equals("CC")) {
                String updateBalanceCheckingQuery = "update checking_account set balance = ? where owner_id = ? returning balance";
                String selectBalanceCheckingQuery = "select balance from checking_account where owner_id = ?";

                ps = con.prepareStatement(selectBalanceCheckingQuery);
                ps.setString(1, cpf);

                rs = ps.executeQuery();

                double balanceCheckings = 0;
                if (rs.next()) {
                    balanceCheckings = rs.getDouble("balance");
                }

                ps.close();

                ps = con.prepareStatement(updateBalanceCheckingQuery);

                if(balanceCheckings < value){
                    throw new ArithmeticException("O valor do saque não pode ser maior que o saldo!");
                }

                currentBalance = balanceCheckings - value;
                ps.setDouble(1, currentBalance);

                ps.setString(2, cpf);

            }

            ps.executeQuery();

            ps.close();
            con.close();

            return currentBalance;
        } catch(Exception e) {
            if(!e.getMessage().equals("No results were returned by the query.")) {
                System.out.println(e.getMessage());
            }
            return currentBalance;
        }
    }

    public Account validateAccount(String type, String cpf)  {

        Connection con = new BuidConection().getCon();

        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectSavingsOwnerQuery = "select name, cpf, birthdate from savings_account sa inner join owner on (sa.owner_id = owner.cpf) where owner_id = ?";
        String selectCheckingOwnerQuery = "select name, cpf, birthdate from checking_account ca inner join owner on (ca.owner_id = owner.cpf) where owner_id = ?";
        Account account = null;

        try{
            if(type.equals("CP")){
                ps = con.prepareStatement(selectSavingsOwnerQuery);

                ps.setString(1, cpf);


                rs = ps.executeQuery();

                if(rs.next()){
                    Owner owner = new Owner(rs.getString("name"), rs.getString("cpf"), rs.getString("birthdate"));
                    account = new SavingsAccount(owner);
                }

                ps.close();
            }else if(type.equals("CC")){
                ps = con.prepareStatement(selectCheckingOwnerQuery);
                ps.setString(1, cpf);

                rs = ps.executeQuery();

                if(rs.next()){
                    Owner owner = new Owner(rs.getString("name"), rs.getString("cpf"), rs.getString("birthdate"));
                    account = new CheckingAccount(owner);
                }

                ps.close();
            }

            con.close();
            return account;

        }catch(Exception e){
            if(!e.getMessage().equals("No results were returned by the query.")){
                System.out.println(e.getMessage());
            }
            return account;
        }
    }

    public Double getCredit(String cpf, double value){
        Connection con = new BuidConection().getCon();

        PreparedStatement ps = null;
        ResultSet rs = null;
        String idClient = null;
        double credit = 0;

        String sqlValidation = "select id, owner_id from checking_account where owner_id = ?";
        String sqlSelect = "select debit_balance from credit_checking_account where cpf_account = ?";
        String sqlInsert = "insert into credit_checking_account(cpf_account, debit_balance)" +
                "values(?,?) returning debit_balance";
        try{
            ps = con.prepareStatement(sqlValidation);

            ps.setString(1, cpf);

            rs = ps.executeQuery();

            if(rs.next()){
                idClient = rs.getString("owner_id");
            }

            ps.close();

            if(idClient == null){
                throw new SQLException("Não existe nenhuma conta corrente registrada com esse cpf!");
            }else{
                ps = con.prepareStatement(sqlSelect);

                ps.setString(1, cpf);

                rs = ps.executeQuery();
                if(rs != null){
                    if(rs.next()){
                        credit = rs.getDouble("debit_balance");
                    }

                    ps.close();

                }
                ps = con.prepareStatement(sqlInsert);

                if((credit + value) > 10000){
                    throw new ArithmeticException("Limite máximo de crédito atingido. Pague sua fatura ou tente solicitar um valor menor");
                }else{
                    ps.setString(1, cpf);
                    ps.setDouble(2, value);

                    rs = ps.executeQuery();

                    if(rs.next()){
                        credit = rs.getDouble("debit_balance");
                    }

                    ps.close();
                    con.close();
                }
            }

            return credit;

        }catch(Exception e){
            if(!e.getMessage().equals("No results were returned by the query.")){
                System.out.println(e.getMessage());
            }
            return credit;
        }
    }

    public double showDebit(String cpf){

        Connection con = new BuidConection().getCon();
        PreparedStatement ps = null;
        ResultSet rs = null;
        double credit = 0;

        String sqlSelect = "select debit_balance from credit_checking_account where cpf_account = ?";

        try {
            ps = con.prepareStatement(sqlSelect);

            ps.setString(1, cpf);

            rs = ps.executeQuery();

            if(rs.next()){
                credit = rs.getDouble("debit_balance");
            }

            return credit;
        }catch (Exception e){
            if(!e.getMessage().equals("No results were returned by the query.")){
                System.out.println(e.getMessage());
            }
            credit = -1;
            return credit;
        }

    }
//    public double payCredit(String cpf, double value)
}
