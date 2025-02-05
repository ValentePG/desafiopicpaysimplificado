package dev.valente.picpaysimplificado.domain;

import lombok.Data;

@Data
public class ShopKeeper {
    private long id;
    private String fullName;
    private String cnpj;
    private String email;
    private String password;
    private Wallet wallet;
}
