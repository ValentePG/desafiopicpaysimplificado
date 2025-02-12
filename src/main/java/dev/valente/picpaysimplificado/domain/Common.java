package dev.valente.picpaysimplificado.domain;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Common {
    private long id;
    private String fullName;
    private String cpf;
    private String email;
    private String password;
    private Wallet wallet;
}
