package dev.valente.picpaysimplificado.repository;

import dev.valente.picpaysimplificado.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<Transaction> getAllTransactions() {
        return jdbcClient.sql("SELECT * FROM tbl_transaction")
                .query(Transaction.class)
                .list();
    }
}
