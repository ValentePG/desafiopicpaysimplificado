package dev.valente.picpaysimplificado.repository;

import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class WalletRepository {

    private final JdbcClient jdbcClient;

    public Wallet getWallet(long walletId) {
        return jdbcClient.sql("SELECT * FROM tbl_wallet WHERE id = ?")
                .param(walletId)
                .query(Wallet.class)
                .single();
    }

    public void updateWallet(BigDecimal amount, Wallet wallet) {
        jdbcClient.sql("UPDATE tbl_wallet SET amount = ? WHERE id = ?")
                .param(amount)
                .param(wallet.getId());
    }
}
