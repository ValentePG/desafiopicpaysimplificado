package dev.valente.picpaysimplificado.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Wallet {
    private long id;
    private BigDecimal balance;
    private WalletType walletType;

    public Wallet(long id, BigDecimal balance, int wallet_type) {
        this.id = id;
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
        this.walletType = WalletType.convertToWalletType(wallet_type);
    }
}
