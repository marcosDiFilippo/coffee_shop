package controllers;

import dtos.ProductDTO;
import models.Product;
import services.ProductService;
import views.ProductsPanel;
import views.ProductFormDialog;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class ProductController {

    private ProductsPanel panel;
    private ProductService service;

    public ProductController(ProductsPanel panel) {
        this.panel = panel;
        this.service = new ProductService();
        loadProducts();
    }

    public void loadProducts() {
        DefaultTableModel model = panel.getTableModel();
        model.setRowCount(0);

        List<Product> products = service.getAllProducts();
        for (Product prod : products) {
            String status = prod.isAvailable() ? "Disponible" : "No Disponible";
            String catName = prod.getCategory() != null ? prod.getCategory().getName() : "";
            model.addRow(new Object[]{
                prod.getImagePath(),
                prod.getId(),
                prod.getName(),
                prod.getBasePrice(),
                status,
                catName,
                prod.getDescription(),
                prod.getCategoryId(),
                ""
            });
        }
    }

    public void saveProduct(ProductDTO dto, File imageFile, ProductFormDialog formDialog) {
        ProductDTO saved = service.saveProduct(dto, imageFile);
        if (saved != null) {
            formDialog.dispose();
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(formDialog, "Error al guardar el producto. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toggleProduct(Long id, boolean currentAvailable) {
        boolean success = service.toggleProductAvailability(id, currentAvailable);
        if (success) {
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(panel, "Error al actualizar el estado del producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
