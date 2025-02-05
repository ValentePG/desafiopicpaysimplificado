package dev.valente.picpaysimplificado.repository;

import dev.valente.picpaysimplificado.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final JdbcClient jdbcClient;

    public void saveTransaction(Transaction transaction) {
        jdbcClient.sql("""
                INSERT INTO tbl_transaction (amount, payee_wallet_id, payer_wallet_id, date) VALUES (?, ?, ?, ?)
        """)
                .param(transaction.getAmount())
                .param(transaction.getPayee_wallet_id())
                .param(transaction.getPayer_wallet_id())
                .param(transaction.getDate())
                .update();
    }
}
