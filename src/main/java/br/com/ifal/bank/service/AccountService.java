package br.com.ifal.bank.service;

import br.com.ifal.bank.model.Account;
import br.com.ifal.bank.model.CheckingAccount;
import br.com.ifal.bank.model.Credit;
import br.com.ifal.bank.model.SavingsAccount;
import br.com.ifal.bank.repository.AccountRepository;

import java.util.Scanner;

import static br.com.ifal.bank.service.ValidationUtils.isValidCpf;

public class AccountService {

    static Scanner scan = new Scanner(System.in);

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String addSavingsAccount() {

        System.out.println("Conta Poupança\n");
        System.out.print("Nome: ");
        String nameSavingsAccount = scan.nextLine();
        System.out.print("CPF: ");
        String cpf = scan.nextLine();
        System.out.print("Data de nascimento: ");
        String birthDateSavingsAccount = scan.nextLine();

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        SavingsAccount s = new SavingsAccount(nameSavingsAccount, cpf, birthDateSavingsAccount);

        if(accountRepository.insertSavingsAccount(s.getName(), s.getCpf(), s.getBirthDate(), s.getBalance(), s.getType()) != null){
            return "Conta Aberta com sucesso!";
        }else{
            throw new NullPointerException("Falha ao abrir conta!");
        }
    }

    public String addCheckingAccount(){

        System.out.println("Conta Corrente\n");
        System.out.print("Nome: ");
        String nameCheckingAccount = scan.nextLine();
        System.out.print("CPF: ");
        String cpf = scan.nextLine();
        System.out.print("Data de nascimento: ");
        String birthDateCheckingAccount = scan.nextLine();

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        CheckingAccount c = new CheckingAccount(nameCheckingAccount, cpf, birthDateCheckingAccount);

        if(accountRepository.insertCheckingAccount(c.getName(), c.getCpf(), c.getBirthDate(), c.getBalance(), c.getType()) != null){
            return "Conta aberta com sucesso!";
        }else{
            throw new NullPointerException("Falha ao abrir conta!");
        }
    }

    public double deposit(Account account){

        System.out.println("Depósito");
        System.out.println();

        System.out.print("Informe o cpf do proprietário da conta: ");
        String cpf = scan.nextLine();
        System.out.print("Informe o valor do depósito: ");
        double value = Double.parseDouble(scan.nextLine());

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        if(value <= 0){
            throw new ArithmeticException("O valor do depósito tem que ser maior que 0!");
        }

        double currentBalance = accountRepository.depositAccount(account.getType(), cpf, value);
        if(currentBalance != 0){
            return currentBalance;
        }else{
            throw new ArithmeticException("Falha ao realizar depósito!");
        }
    }

    public double withdraw(String type){

        System.out.println("Saque");
        System.out.println();

        System.out.print("Informe o cpf do proprietário da conta: ");
        String cpf = scan.nextLine();
        System.out.print("Informe o valor do saque: ");
        double value = Double.parseDouble(scan.nextLine());

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        double currentBalance = accountRepository.withdrawAccount(type, cpf, value);
        if(currentBalance != 0){
            return currentBalance;
        }else{
            throw new ArithmeticException("Falha ao realizar saque!");
        }
    }

    public double credit() {

        System.out.println("Sistema de liberação de crédito\n");

        System.out.print("Informe o cpf do proprietário da conta: ");
        String cpf = scan.nextLine();
        System.out.print("Valor do cŕedito: ");
        double value = Double.parseDouble(scan.nextLine());
        Credit c = new Credit(cpf, value);

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        if(value <= 0){
            throw new ArithmeticException("O valor do crédito tem que ser maior que 0!");
        }

        double creditValue = accountRepository.getCredit(c.getCpf(), c.getCredit());
        if(creditValue != 0){
            return creditValue;
        }else{
            throw new RuntimeException("Não foi possível obter cŕedito");
        }
    }

    public double showDebitAccount(String cpf) {
        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        double debitValue = accountRepository.showDebit(cpf);
        if(debitValue != -1){
            return debitValue;
        }else{
            throw new RuntimeException("Houve um erro inesperado. Não foi possível verificar o seu débito");
        }
    }

}
