package com.mpt.journal.model;

public enum OrderStatus {
        NEW("Новый"),
        IN_PROGRESS("В процессе выполнения"),
        COMPLETED("Выполнен"),
        CANCELED("Отменен");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
