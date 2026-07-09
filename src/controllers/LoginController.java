package controllers;

import dtos.LoginDTO;
import models.User;
import services.AuthService;
import views.Login;

import javax.swing.JOptionPane;

public class LoginController {

    private Login loginView;
    private AuthService authService;

    public LoginController(Login loginView) {
        this.loginView = loginView;
        this.authService = new AuthService();
    }

    public void login(LoginDTO loginDTO) {
        User user = authService.login(loginDTO);

        if (user != null) {
            System.out.println("Login exitoso. Bienvenido " + user.getFirstName() + " " + user.getLastName() + " (Rol: " + user.getRol() + ")");
            loginView.dispose();
        } else {
            JOptionPane.showMessageDialog(loginView, "Credenciales incorrectas o usuario inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
