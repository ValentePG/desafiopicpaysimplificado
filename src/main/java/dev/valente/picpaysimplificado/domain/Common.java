package dev.valente.picpaysimplificado.domain;

import lombok.Data;

@Data
public class Common {
    private long id;
    private String fullName;
    private String cpf;
    private String email;
    private String password;
    private Wallet wallet;
}
