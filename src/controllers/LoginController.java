package controllers;

import dtos.LoginDTO;
import exceptions.InvalidDataException;
import exceptions.UserAuthenticationException;
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
        try {
            User user = authService.login(loginDTO);
            Dashboard dashboard = new Dashboard(user);
            new DashboardController(dashboard);
            dashboard.setVisible(true);
            loginView.dispose();
        } catch (UserAuthenticationException | InvalidDataException e) {
            JOptionPane.showMessageDialog(loginView, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginView, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}
