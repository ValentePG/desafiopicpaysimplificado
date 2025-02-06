package dev.valente.picpaysimplificado.repository;

import dev.valente.picpaysimplificado.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final JdbcClient jdbcClient;

    public Transaction saveTransaction(Transaction transaction) {
        return jdbcClient.sql("""
        INSERT INTO tbl_transaction (amount, payee_wallet_id, payer_wallet_id, date) VALUES (?, ?, ?, ?) RETURNING *
        """)
                .param(transaction.getAmount())
                .param(transaction.getPayeeWalletId())
                .param(transaction.getPayerWalletId())
                .param(transaction.getDate())
                .query(Transaction.class)
                .single();
    }

    public Transaction getTransaction(long transaction) {
        return jdbcClient.sql("SELECT * FROM tbl_transaction WHERE id = ?")
                .param(transaction)
                .query(Transaction.class)
                .single();
    }
}
