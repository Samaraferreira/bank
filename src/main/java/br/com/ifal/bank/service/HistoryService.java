package br.com.ifal.bank.service;

import br.com.ifal.bank.model.Account;
import br.com.ifal.bank.model.History;
import br.com.ifal.bank.repository.AccountRepository;
import br.com.ifal.bank.repository.HistoryRepository;

import java.util.ArrayList;

import static br.com.ifal.bank.service.ValidationUtils.isValidCpf;

public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public ArrayList<History> getHistory(Account account) {
        if(!isValidCpf(account.getCpf())) {
            throw new IllegalArgumentException("O CPF precisa ser composto de 11 números!");
        }

        return historyRepository.select(account.getCpf());
    }
}
