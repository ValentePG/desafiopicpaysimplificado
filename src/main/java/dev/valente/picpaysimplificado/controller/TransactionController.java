package dev.valente.picpaysimplificado.controller;

import dev.valente.picpaysimplificado.domain.Transaction;
import dev.valente.picpaysimplificado.dto.TransactionRequest;
import dev.valente.picpaysimplificado.dto.TransactionResponse;
import dev.valente.picpaysimplificado.exception.ApiError;
import dev.valente.picpaysimplificado.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
    @Operation(
        summary = "Solicitar transferência",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Json correto para requisição ser aceita (campo valor também aceita 50.0, 50.00)",
                required = true,
                content = @Content(schema = @Schema(implementation = TransactionRequest.class))),
        responses = {
                @ApiResponse(description = "Solicita transferência",
                        responseCode = "200",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = TransactionResponse.class))),
                @ApiResponse(description = "Retorna Bad request se qualquer um dos campos forem vazios",
                        responseCode = "400",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ApiError.class)))
        }
    )
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid TransactionRequest transaction) {

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
