package enums;

public enum OrderStatus {
    PENDING("Pendiente"),
    PREPARING("Preparando"),
    READY("Listo"),
    DELIVERED("Entregado"),
    CANCELLED("Cancelado");

    private String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OrderStatus fromString(String dbValue) {
        if (dbValue == null) return null;
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(dbValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + dbValue);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
