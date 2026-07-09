package controllers;

import dtos.LoginDTO;
import models.User;
import services.AuthService;
import views.Login;
import views.Dashboard;

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
            Dashboard dashboard = new Dashboard(user);
            new DashboardController(dashboard, user);
            dashboard.setVisible(true);
            loginView.dispose();
        } else {
            JOptionPane.showMessageDialog(loginView, "Credenciales incorrectas o usuario inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
