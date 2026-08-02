package controllers;

import dtos.CategoryDTO;
import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.Category;
import services.CategoryService;
import views.categories.CategoriesPanel;
import views.categories.CategoryFormDialog;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CategoryController {

    private CategoriesPanel panel;
    private CategoryService service;

    public CategoryController() {
        this.service = new CategoryService();
    }

    public CategoryController(CategoriesPanel panel) {
        this.panel = panel;
        this.service = new CategoryService();
        loadCategories();
    }

    public void loadCategories() {
        DefaultTableModel model = panel.getTableModel();
        model.setRowCount(0);

        List<Category> categories = this.getAllCategories();
        for (Category cat : categories) {
            String status = cat.isActive() ? "Activa" : "Inactiva";
            String reqSize = cat.isRequiresSize() ? "Sí" : "No";
            model.addRow(new Object[]{cat.getId(), cat.getName(), cat.getDescription(), status, reqSize, ""});
        }
    }

    public List<Category> getAllCategories () {
        return service.getAllCategories();
    }

    public void saveCategory(CategoryDTO dto, CategoryFormDialog formDialog) {
        try {
            service.saveCategory(dto);
            formDialog.dispose();
            loadCategories();
        } catch (InvalidDataException | TransactionFailedException e) {
            JOptionPane.showMessageDialog(formDialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(formDialog, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toggleCategory(Long id, boolean currentActive) {
        try {
            boolean success = service.toggleCategoryStatus(id, currentActive);
            if (success) {
                loadCategories();
            } else {
                JOptionPane.showMessageDialog(panel, "Error al actualizar el estado de la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteCategoryHard(Long id) {
        try {
            service.deleteCategoryHard(id);
            JOptionPane.showMessageDialog(panel, "Categoría eliminada con éxito. Tambien se ha eliminado existosamente todos sus productos y ventas asociadas", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            loadCategories();
        } catch (InvalidDataException | TransactionFailedException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}
