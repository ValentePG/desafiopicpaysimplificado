package dev.valente.picpaysimplificado.utils;

import dev.valente.picpaysimplificado.domain.Wallet;
import dev.valente.picpaysimplificado.domain.WalletType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class WalletUtils {

    public static Wallet PAYER_WALLET = new Wallet(1L, BigDecimal.valueOf(300.0),
            WalletType.COMMON_WALLET_TYPE.getValue());
    public static Wallet PAYEE_WALLET = new Wallet(2L, BigDecimal.valueOf(200.0),
            WalletType.SHOPKEEPER_WALLET_TYPE.getValue());

    public static List<Wallet> LIST_OF_WALLETS = List.of(PAYEE_WALLET, PAYER_WALLET);
}
