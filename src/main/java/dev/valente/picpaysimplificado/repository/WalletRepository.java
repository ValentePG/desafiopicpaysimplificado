package dev.valente.picpaysimplificado.repository;

import dev.valente.picpaysimplificado.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WalletRepository {

    private final JdbcClient jdbcClient;

    public Optional<Wallet> getWallet(long id) {
        return jdbcClient.sql("SELECT * FROM tbl_wallet WHERE id = ?")
                .param(id)
                .query(Wallet.class)
                .optional();
    }

    public int updateWallet(Wallet wallet) {
        return jdbcClient.sql("UPDATE tbl_wallet SET balance = ?, version = version + 1 WHERE id = ? AND version = ?")
                .param(wallet.getBalance())
                .param(wallet.getId())
                .param(wallet.getVersion())
                .update();
    }
}
