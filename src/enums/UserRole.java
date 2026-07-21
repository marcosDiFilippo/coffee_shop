package enums;

public enum UserRole {
    CUSTOMER("Cliente"),
    EMPLOYEE("Empleado"),
    MANAGER("Gerente"),
    ADMIN("Administrador");

    private String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UserRole fromString(String dbValue) {
        if (dbValue == null) return null;
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(dbValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol no válido: " + dbValue);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
