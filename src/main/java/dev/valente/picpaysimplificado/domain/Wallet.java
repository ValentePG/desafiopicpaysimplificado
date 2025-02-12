package dev.valente.picpaysimplificado.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@ToString
public class Wallet {
    private long id;
    private BigDecimal balance;
    private WalletType walletType;
    private int version;

    public Wallet(long id, BigDecimal balance, int wallet_type) {
        this.id = id;
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
        this.walletType = WalletType.convertToWalletType(wallet_type);
    }
}
