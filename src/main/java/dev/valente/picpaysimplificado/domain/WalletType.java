package dev.valente.picpaysimplificado.domain;

import lombok.Getter;

@Getter
public enum WalletType {

    COMMON_WALLET_TYPE(1),
    SHOPKEEPER_WALLET_TYPE(2);

    private final int value;

    WalletType(int value) {
        this.value = value;
    }

    public static WalletType convertToWalletType(int walletIntValue) {
        return switch (walletIntValue) {
            case 1 -> COMMON_WALLET_TYPE;
            case 2 -> SHOPKEEPER_WALLET_TYPE;
            default -> throw new IllegalStateException("Unexpected value: " + walletIntValue);
        };
    }
}
