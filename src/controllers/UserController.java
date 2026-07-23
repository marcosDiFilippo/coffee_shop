package controllers;

import dtos.UserDTO;
import models.User;
import services.UserService;
import views.Dashboard;
import views.UserFormDialog;
import views.UsersPanel;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UserController {
    private UsersPanel panel;
    private UserService service;
    private List<User> currentUsers;

    public UserController(UsersPanel panel) {
        this.panel = panel;
        this.service = new UserService();
        loadUsers();
    }

    public void loadUsers() {
        DefaultTableModel model = panel.getTableModel();
        model.setRowCount(0);

        currentUsers = service.getAllUsers();
        for (User user : currentUsers) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            String status = user.isActive() ? "Activo" : "Inactivo";
            model.addRow(new Object[]{
                user.getId(),
                fullName,
                user.getEmail(),
                user.getPhone(),
                user.getRol() != null ? user.getRol().getDisplayName() : "",
                status,
                ""
            });
        }
    }

    public void openEditDialog(Long userId) {
        User targetDto = null;
        for (User user : currentUsers) {
            if (user.getId().equals(userId)) {
                targetDto = user;
                break;
            }
        }
        
        if (targetDto != null) {
            Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(panel);
            UserFormDialog formDialog = new UserFormDialog(dashboard, this, targetDto);
            formDialog.setVisible(true);
        }
    }

    public void saveUser(UserDTO dto, UserFormDialog formDialog) {
        if (dto.getFirstName().trim().isEmpty() || dto.getLastName().trim().isEmpty() || dto.getUsername().trim().isEmpty()) {
            JOptionPane.showMessageDialog(formDialog, "Los campos Nombre, Apellido y Usuario son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dto.getId() == null && dto.getPassword().trim().isEmpty()) {
            JOptionPane.showMessageDialog(formDialog, "La contraseña es obligatoria para nuevos usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = service.saveUser(dto);
        if (success) {
            JOptionPane.showMessageDialog(formDialog, "Usuario guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            formDialog.dispose();
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(formDialog, "Error al guardar el usuario. Verifique que el nombre de usuario no exista ya.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toggleUser(Long userId, boolean currentActive) {
        boolean success = service.toggleUserStatus(userId, currentActive);
        if (success) {
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(panel, "Error al actualizar el estado del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
