package controllers;

import exceptions.InvalidDataException;
import exceptions.TransactionFailedException;
import models.Size;
import services.SizeService;
import views.Dashboard;
import views.DrinkSizeForm;
import views.DrinkSizePanel;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class DrinkSizeController {

    private DrinkSizePanel panel;
    private SizeService service;
    private Dashboard dashboard;

    public DrinkSizeController(DrinkSizePanel panel, Dashboard dashboard) {
        this.panel = panel;
        this.dashboard = dashboard;
        this.service = new SizeService();
        initController();
        loadSizes();
    }

    private void initController() {
        panel.getBtnNewSize().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openForm(null);
            }
        });
    }

    public void loadSizes() {
        try {
            DefaultTableModel model = panel.getTableModel();
            model.setRowCount(0);
            List<Size> sizes = service.getAllSizes();
            for (Size size : sizes) {
                String status = size.isActive() ? "Activo" : "Inactivo";
                model.addRow(new Object[]{
                    size.getId(),
                    size.getName(),
                    size.getPriceMultiplier(),
                    status,
                    "Opciones"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openForm(Size size) {
        DrinkSizeForm form = new DrinkSizeForm(dashboard, size);
        
        form.getBtnCancel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                form.dispose();
            }
        });

        form.getBtnSave().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveSize(form);
            }
        });

        form.setVisible(true);
    }

    private void saveSize(DrinkSizeForm form) {
        try {
            String name = form.getTxtName().getText();
            String multiplierStr = form.getTxtMultiplier().getText();
            BigDecimal multiplier;
            try {
                multiplier = new BigDecimal(multiplierStr);
            } catch (NumberFormatException ex) {
                throw new InvalidDataException("El multiplicador de precio debe ser un número decimal válido.");
            }

            Size size = new Size();
            size.setId(form.getCurrentSizeId());
            size.setName(name);
            size.setPriceMultiplier(multiplier);

            service.saveSize(size);
            form.dispose();
            loadSizes();
        } catch (InvalidDataException | TransactionFailedException ex) {
            JOptionPane.showMessageDialog(form, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(form, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toggleSize(Long id, boolean currentActive) {
        try {
            service.toggleSize(id, !currentActive);
            loadSizes();
        } catch (InvalidDataException | TransactionFailedException ex) {
            JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, "Ha ocurrido un error en el sistema, por favor vuelva intentarlo.", "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}
