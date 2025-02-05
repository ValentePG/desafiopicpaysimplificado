package dev.valente.picpaysimplificado.domain;

public enum WalletType {

    COMMON_WALLET_TYPE(1),
    SHOPKEEPER_WALLET_TYPE(2);

    private final int value;

    WalletType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
