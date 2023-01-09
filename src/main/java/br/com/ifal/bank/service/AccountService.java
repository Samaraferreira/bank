package br.com.ifal.bank.service;

import br.com.ifal.bank.model.*;
import br.com.ifal.bank.repository.AccountRepository;
import br.com.ifal.bank.repository.OwnerRepository;

import java.util.Scanner;

import static br.com.ifal.bank.service.ValidationUtils.isValidBirthDate;
import static br.com.ifal.bank.service.ValidationUtils.isValidCpf;

public class AccountService {

    static Scanner scan = new Scanner(System.in);

    private AccountRepository accountRepository;
    private OwnerRepository ownerRepository;

    public AccountService(AccountRepository accountRepository, OwnerRepository ownerRepository) {
        this.accountRepository = accountRepository;
        this.ownerRepository = ownerRepository;
    }

    public String addSavingsAccount() throws Exception {

        System.out.println("Conta Poupança\n");
        System.out.print("Nome: ");
        String name = scan.nextLine();
        System.out.print("CPF: ");
        String cpf = scan.nextLine();
        System.out.print("Data de nascimento: ");
        String birthDate = scan.nextLine();

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        if(!isValidBirthDate(birthDate)){
            throw new IllegalArgumentException("A data de nascimento precisa ser composta de" +
                    " 10 caracteres, incluindo as barras de separação. Ex: 12/04/2003");
        }

        Owner owner = new Owner(name, cpf, birthDate);
        SavingsAccount s = new SavingsAccount(owner);

        String ownerId = ownerRepository.addOwner(owner);

        if(accountRepository.insertSavingsAccount(ownerId, s.getBalance(), s.getType()) != null){
            return "Conta Aberta com sucesso!";
        } else {
            throw new Exception("Falha ao abrir conta!");
        }
    }

    public String addCheckingAccount() throws Exception {

        System.out.println("Conta Corrente\n");
        System.out.print("Nome: ");
        String name = scan.nextLine();
        System.out.print("CPF: ");
        String cpf = scan.nextLine();
        System.out.print("Data de nascimento: ");
        String birthDate = scan.nextLine();

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        if(!isValidBirthDate(birthDate)){
            throw new IllegalArgumentException("A data de nascimento precisa ser composta de" +
                    " 10 caracteres, incluindo as barras de separação. Ex: 12/04/2003");
        }

        Owner owner = new Owner(name, cpf, birthDate);
        CheckingAccount c = new CheckingAccount(owner);

        String ownerId = ownerRepository.addOwner(owner);

        if(accountRepository.insertCheckingAccount(ownerId, c.getBalance(), c.getType()) != null){
            return "Conta Aberta com sucesso!";
        } else {
            throw new Exception("Falha ao abrir conta!");
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

        if(value <= 0) {
            throw new ArithmeticException("O valor do depósito tem que ser maior que 0!");
        }

        double currentBalance = accountRepository.depositAccount(account.getType(), cpf, value);
        if(currentBalance != 0){
            return currentBalance;
        }else{
            throw new ArithmeticException("Falha ao realizar depósito!");
        }
    }

    public double withdraw(Account account) {

        System.out.println("Saque");
        System.out.println();

        System.out.print("Informe o cpf do proprietário da conta: ");
        String cpf = scan.nextLine();
        System.out.print("Informe o valor do saque: ");
        double value = Double.parseDouble(scan.nextLine());

        if(!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        double currentBalance = accountRepository.withdrawAccount(account.getType(), cpf, value);
        if(currentBalance != 0){
            return currentBalance;
        }else{
            throw new ArithmeticException("Falha ao realizar saque!");
        }
    }

    public double credit(Account account) {

        System.out.println("Sistema de liberação de crédito\n");

        System.out.print("Informe o cpf do proprietário da conta: ");
        String cpf = scan.nextLine();
        System.out.print("Valor do cŕedito: ");
        double value = Double.parseDouble(scan.nextLine());

        if(!cpf.equals(account.getOwner().getCpf())){
            throw new RuntimeException("O CPF está incorreto!");
        }

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

    public double payDebitAccount(String cpf, double value, Account account){

        if(!cpf.equals(account.getOwner().getCpf())){
            throw new RuntimeException("O CPF está incorreto!");
        }

        if(value <= 0){
            throw new ArithmeticException("O valor do depósito tem que ser maior que 0!");
        }

        double debitValue = accountRepository.payDebit(cpf, value);
        if(debitValue != -1){
            return debitValue;
        }else{
            throw new RuntimeException("Houve um erro inesperado. Não foi possível realizar o pagamento do seu débito");
        }
    }

    public void deleteSavingAccount(Account account, String cpf) {
        if(!cpf.equals(account.getCpf())){
            throw new RuntimeException("O CPF está incorreto!");
        }

        try {
            accountRepository.deleteSavingAccount(account.getCpf());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCheckingAccount(Account account, String cpf) {
        if(!cpf.equals(account.getCpf())){
            throw new RuntimeException("O CPF está incorreto!");
        }

        try {
            accountRepository.deleteCheckingAccount(account.getCpf());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
