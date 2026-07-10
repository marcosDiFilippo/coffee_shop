package controllers;

import dtos.CategoryDTO;
import models.Category;
import services.CategoryService;
import views.CategoriesPanel;
import views.CategoryFormDialog;
import views.Dashboard;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoryController implements ActionListener {

    private CategoriesPanel panel;
    private CategoryService service;
    private CategoryFormDialog formDialog;
    private Long currentEditingId;

    public CategoryController(CategoriesPanel panel) {
        this.panel = panel;
        this.service = new CategoryService();
        this.panel.getBtnAddCategory().addActionListener(this);

        this.panel.getBtnTableEdit().addActionListener(e -> {
            int row = this.panel.getTableCategories().getEditingRow();
            if (row != -1) {
                Long id = (Long) this.panel.getTableModel().getValueAt(row, 0);
                String name = (String) this.panel.getTableModel().getValueAt(row, 1);
                String desc = (String) this.panel.getTableModel().getValueAt(row, 2);
                boolean active = this.panel.getTableModel().getValueAt(row, 3).equals("Activa");
                
                TableCellEditor editor = this.panel.getTableCategories().getCellEditor();
                if (editor != null) {
                    editor.stopCellEditing();
                }

                CategoryDTO dto = new CategoryDTO(id, name, desc, active);
                openEditModal(dto);
            }
        });

        this.panel.getBtnTableToggle().addActionListener(e -> {
            int row = this.panel.getTableCategories().getEditingRow();
            if (row != -1) {
                Long id = (Long) this.panel.getTableModel().getValueAt(row, 0);
                boolean active = this.panel.getTableModel().getValueAt(row, 3).equals("Activa");
                
                TableCellEditor editor = this.panel.getTableCategories().getCellEditor();
                if (editor != null) {
                    editor.stopCellEditing();
                }

                toggleCategory(id, active);
            }
        });

        loadCategories();
    }

    public void loadCategories() {
        DefaultTableModel model = panel.getTableModel();
        model.setRowCount(0);

        List<Category> categories = service.getAllCategories();
        for (Category cat : categories) {
            String status = cat.isActive() ? "Activa" : "Inactiva";
            model.addRow(new Object[]{cat.getId(), cat.getName(), cat.getDescription(), status, ""});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panel.getBtnAddCategory()) {
            openCreateModal();
        }
    }

    private void openCreateModal() {
        currentEditingId = null;
        
        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(panel);
        formDialog = new CategoryFormDialog(dashboard);
        
        formDialog.getBtnSave().addActionListener(e -> saveCategory());
        formDialog.getBtnCancel().addActionListener(e -> formDialog.dispose());
        
        formDialog.setVisible(true);
    }

    public void openEditModal(CategoryDTO dto) {
        currentEditingId = dto.getId();
        
        Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(panel);
        formDialog = new CategoryFormDialog(dashboard);
        
        formDialog.getTxtName().setText(dto.getName());
        formDialog.getTxtDescription().setText(dto.getDescription());
        
        formDialog.getBtnSave().addActionListener(e -> saveCategory());
        formDialog.getBtnCancel().addActionListener(e -> formDialog.dispose());
        
        formDialog.setVisible(true);
    }

    private void saveCategory() {
        String name = formDialog.getTxtName().getText();
        String description = formDialog.getTxtDescription().getText();

        CategoryDTO dto = new CategoryDTO(currentEditingId, name, description, true);
        
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
