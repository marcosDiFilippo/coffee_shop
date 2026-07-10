package controllers;

import views.Dashboard;
import views.Login;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardController implements ActionListener {

    private Dashboard dashboardView;

    public DashboardController(Dashboard dashboardView) {
        this.dashboardView = dashboardView;
        this.dashboardView.getBtnLogout().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dashboardView.getBtnLogout()) {
            handleLogout();
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                dashboardView,
                "Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dashboardView.dispose();
            Login loginView = new Login();
            loginView.setVisible(true);
        }
    }
}
