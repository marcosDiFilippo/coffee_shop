package models;

import enums.UserRole;

public class User extends BaseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean active;
    private UserRole rol;

    public User() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserRole getRol() {
        return rol;
    }

    public void setRol(UserRole rol) {
        this.rol = rol;
    }
}
