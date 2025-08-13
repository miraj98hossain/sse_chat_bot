package com.example.sse_chat_bot.utils;

public enum SenderType {
    SERVER("Server"),
    USER("User");

    private final String displayName;

    SenderType(String displayName) {
        this.displayName = displayName;
    }

    public static SenderType getPaymentTypeFromString(String displayName) {
        for (SenderType paymentType : SenderType.values()) {
            if (paymentType.displayName.equals(displayName)) {
                return paymentType;
            }
        }

        throw new IllegalArgumentException("Unknown string value" + displayName);
    }
}
