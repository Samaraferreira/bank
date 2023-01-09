package br.com.ifal.bank.service;

import br.com.ifal.bank.exception.NotFoundException;
import br.com.ifal.bank.model.Account;
import br.com.ifal.bank.model.AccountType;
import br.com.ifal.bank.repository.AccountRepository;

public class AuthenticationService {

    private AccountRepository accountRepository;

    public AuthenticationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean isValidAuthenticationType(String authenticationType) {
        return authenticationType.equals("CP") || authenticationType.equals("CC");
    }

    public Account authentication(String accountType, String cpf) throws Exception {
        Account account = accountRepository.validationAccount(accountType, cpf);
        if(account != null){
            return account;
        } else {
            throw new NotFoundException("Não existe nenhuma conta " +
                    AccountType.getDescription(accountType) + " com esse cpf");
        }
    }
}
