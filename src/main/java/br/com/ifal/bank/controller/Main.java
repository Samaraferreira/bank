package br.com.ifal.bank.controller;

import br.com.ifal.bank.model.Account;
import br.com.ifal.bank.repository.AccountRepository;
import br.com.ifal.bank.service.AccountService;
import br.com.ifal.bank.service.AuthenticationService;

import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);
    static AuthenticationService authenticationService = new AuthenticationService(new AccountRepository());
    static AccountService accountService = new AccountService(new AccountRepository());

    public static void main(String[] args) {
        int opInicio = -1;

        for (;;) {
            try {
                System.out.println("""
                        
                        --- Banco Gringotts ---
                        
                        1 - Login
                        2 - Abrir conta
                        0 - Sair""");
                System.out.print("Selecione uma opção --> ");
                opInicio = scan.nextInt();

                if (opInicio == 0) {
                    break;
                }

                if(opInicio < 0 || opInicio > 2) {
                    throw new IllegalArgumentException("Informe uma opção válida!");
                }

                switch (opInicio) {
                    case 1 -> {
                        try {
                            System.out.println("\n-- Login");
                            System.out.println("""
                                    
                                    Informe o tipo da conta que deseja logar:
                                    CP - Conta Poupança
                                    CC - Conta Corrente""");

                            System.out.print("Tipo --> ");
                            String typeAccountAuthentication = scan.nextLine() + scan.nextLine();

                            if (!authenticationService.isValidAuthenticationType(typeAccountAuthentication)) {
                                throw new IllegalArgumentException("Informe um tipo válido de conta!");
                            }

                            System.out.print("CPF do proprietário da conta --> ");
                            String cpf = scan.nextLine();

                            Account account = authenticationService.authentication(typeAccountAuthentication, cpf);
                            accessAccount(account);
                        } catch (Exception e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        try {
                            System.out.println("""
                                    Selecione o tipo de conta
                                    1 - Abrir conta poupança
                                    2 - Abrir conta corrente""");

                            System.out.print("Tipo --> ");
                            int opOpenAccount = scan.nextInt();

                            if (opOpenAccount == 1) {
                                System.out.println(accountService.addSavingsAccount());
                            } else if (opOpenAccount == 2){
                                System.out.println(accountService.addCheckingAccount());
                            } else {
                                throw new IllegalArgumentException("Informe uma opção válida");
                            }
                        } catch (RuntimeException e) {
                            if (e.getMessage() == null) {
                                System.out.println("O tipo do valor informado é diferente do solicitado!");
                            } else {
                                System.out.println("Erro: " + e.getMessage());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void accessAccount(Account account) {
        int op = -1;

        do {
            try{
                System.out.println("\n\n-- Acesso a conta ");
                System.out.println("\nBem vindo(a), " + account.getName() + "\n");
                System.out.print("""
                        Menu:
                        1 - Realizar depósito
                        2 - Realizar saque""");
                if(account.getType().equals("CC")){
                    System.out.print("""

                            3 - Solicitar crédito
                            4 - Pagar emprestimo""");
                }
                System.out.println("\n0 - Logout");
                System.out.print("Selecione uma opção: ");
                op = Integer.parseInt(scan.nextLine());

                if(account.getType().equals("CC")) {
                    if(op < 0 || op > 4){
                        throw new IllegalArgumentException("Informe uma opção válida");
                    }
                } else{
                    if(op < 0 || op > 2){
                        throw new IllegalArgumentException("Informe uma opção válida");
                    }
                }

                switch (op) {
                    case 1 -> {
                        double currentBalanceDeposit = accountService.deposit(account);
                        System.out.println("Depósito realizado com sucesso!" +
                                "\n Saldo atual: R$ " + currentBalanceDeposit);
                    }
                    case 2 -> {
                        double currentBalanceWithdraw = accountService.withdraw(account.getType());
                        if (currentBalanceWithdraw != 0) {
                            System.out.println("Saque realizado com sucesso!" +
                                    "\n Saldo atual: R$ " + currentBalanceWithdraw);
                        }
                    }
                    case 3 -> {
                        double creditValue = accountService.credit();
                        System.out.println("Empréstimo realizado com sucesso!");
                        System.out.println("Limite utilizado: R$ "
                                + creditValue + "/10000.00");
                    }
                    case 4 -> {
                        System.out.println("Pagar emprestimo\n");
                        System.out.println("Débito atual: R$ " + accountService.showDebitAccount(account.getCpf()) + "\n");
                        System.out.print("informe o valor que deseja abater da dívida: ");
                    }
                }

            } catch (Exception e){
                if (e.getMessage() == null) {
                    System.out.println("O tipo do valor informado é diferente do solicitado!");
                } else {
                    System.out.println("Erro: " + e.getMessage());
                }
            }

        } while(op != 0);
    }
}
