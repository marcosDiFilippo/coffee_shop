package controllers;

import dtos.CategoryTopProductDTO;
import services.StatsService;
import views.StatsPanel;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.util.List;

public class StatsController {

    private StatsPanel panel;
    private StatsService service;

    public StatsController(StatsPanel panel) {
        this.panel = panel;
        this.service = new StatsService();
        loadStats();
    }

    private void loadStats() {
        try {
            int totalProducts = service.getTotalProducts();
            BigDecimal totalRevenue = service.getTotalRevenue();
            List<CategoryTopProductDTO> topProducts = service.getTopProductsByCategory();

            panel.getLblTotalProductsValue().setText(String.valueOf(totalProducts));
            panel.getLblTotalRevenueValue().setText("$ " + totalRevenue.toString());

            DefaultTableModel model = panel.getTableModel();
            model.setRowCount(0);

            for (CategoryTopProductDTO dto : topProducts) {
                model.addRow(new Object[]{
                    dto.getCategoryName(),
                    dto.getProductName(),
                    dto.getTotalSold()
                });
            }
        } catch (exceptions.TransactionFailedException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}
