package dev.valente.picpaysimplificado.repository;

import dev.valente.picpaysimplificado.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WalletRepository {

    private final JdbcClient jdbcClient;

    public List<Wallet> getWallets(long payeeId, long payerId) {
        return jdbcClient.sql("SELECT * FROM tbl_wallet WHERE id IN (?,?)")
                .param(payeeId)
                .param(payerId)
                .query(Wallet.class)
                .list();
    }

    public void updateWallets(Wallet payeeWallet, Wallet payerWallet) {
        jdbcClient.sql("UPDATE tbl_wallet SET balance = CASE " +
                        "WHEN id = ? THEN ? " +
                        "WHEN id = ? THEN ? " +
                        "ELSE balance " +
                        "END " +
                        "WHERE id IN (?,?)")
                .param(payeeWallet.getId())
                .param(payeeWallet.getBalance())
                .param(payerWallet.getId())
                .param(payerWallet.getBalance())
                .param(payerWallet.getId())
                .param(payeeWallet.getId())
                .update();
    }
}
