package controllers;

import dtos.ProductDTO;
import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.Product;
import services.ProductService;
import views.products.ProductsPanel;
import views.products.ProductFormDialog;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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

    public void saveProduct(ProductDTO dto, ProductFormDialog formDialog) {
        try {
            service.saveProduct(dto);
            formDialog.dispose();
            loadProducts();
        } catch (InvalidDataException | TransactionFailedException e) {
            JOptionPane.showMessageDialog(formDialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(formDialog, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toggleProduct(Long id, boolean currentAvailable) {
        try {
            service.toggleProductAvailability(id, currentAvailable);
            loadProducts();
        } catch (TransactionFailedException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}
