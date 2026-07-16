package controllers;

import dtos.CategoryDTO;
import models.Category;
import services.CategoryService;
import views.CategoriesPanel;
import views.CategoryFormDialog;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CategoryController {

    private CategoriesPanel panel;
    private CategoryService service;

    public CategoryController(CategoriesPanel panel) {
        this.panel = panel;
        this.service = new CategoryService();
        loadCategories();
    }

    public void loadCategories() {
        DefaultTableModel model = panel.getTableModel();
        model.setRowCount(0);

        List<Category> categories = service.getAllCategories();
        for (Category cat : categories) {
            String status = cat.isActive() ? "Activa" : "Inactiva";
            String reqSize = cat.isRequiresSize() ? "Sí" : "No";
            model.addRow(new Object[]{cat.getId(), cat.getName(), cat.getDescription(), status, reqSize, ""});
        }
    }

    public void saveCategory(CategoryDTO dto, CategoryFormDialog formDialog) {
        CategoryDTO saved = service.saveCategory(dto);
        if (saved != null) {
            formDialog.dispose();
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(formDialog, "Error al guardar la categoría. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toggleCategory(Long id, boolean currentActive) {
        boolean success = service.toggleCategoryStatus(id, currentActive);
        if (success) {
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(panel, "Error al actualizar el estado de la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
