package dev.valente.picpaysimplificado.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Wallet {
    private long id;
    private BigDecimal balance;
    private WalletType walletType;
    private List<Transactions> transactions;

    public Wallet(long id, BigDecimal balance, WalletType walletType, List<Transactions> transactions) {
        this.id = id;
        this.balance = balance.setScale(2);
        this.walletType = walletType;
        this.transactions = transactions;
    }
}
