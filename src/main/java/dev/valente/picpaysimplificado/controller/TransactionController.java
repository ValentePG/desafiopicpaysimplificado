package dev.valente.picpaysimplificado.controller;

import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.dto.TransactionRequest;
import dev.valente.picpaysimplificado.dto.TransactionResponse;
import dev.valente.picpaysimplificado.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transfer")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest transaction) {

        var payeeId = transaction.payee();
        var payerId = transaction.payer();
        var valueOfTransaction = transaction.value();

        var newTransaction = Transaction
                .builder()
                .payeeWalletId(payeeId)
                .payerWalletId(payerId)
                .amount(valueOfTransaction)
                .build();

        var transactionSuccess = transactionService.processTransaction(newTransaction);

        var transactionResponse = TransactionResponse.builder()
                .id(transactionSuccess.getId())
                .payee(transactionSuccess.getPayeeWalletId())
                .payer(transactionSuccess.getPayerWalletId())
                .amount(transactionSuccess.getAmount())
                .build();

        return ResponseEntity.ok().body(transactionResponse);
    }
}
