package views;

import constants.Colors;
import controllers.OrderController;
import daos.CategoryDAO;
import models.Category;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SelectCategoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public SelectCategoryPanel(OrderController controller) {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        JLabel lblTitle = new JLabel("SELECCIONAR CATEGORÍA");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 30, 1030, 40);
        add(lblTitle);
        
        JButton btnGoCart = new JButton("Ver Orden (" + controller.getCartItems().size() + ")");
        btnGoCart.setBackground(Colors.CARAMEL_ROAST.getColor());
        btnGoCart.setForeground(Colors.CREAMY_LATTE.getColor());
        btnGoCart.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGoCart.setFocusPainted(false);
        btnGoCart.setBounds(800, 30, 200, 40);
        btnGoCart.setBorder(null);
        btnGoCart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.showCart();
            }
        });
        add(btnGoCart);

        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAll();

        int x = 50;
        int y = 100;
        int width = 200;
        int height = 150;
        int gap = 30;

        for (Category cat : categories) {
            if (cat.isActive()) {
                JButton btnCat = new JButton(cat.getName());
                btnCat.setBackground(Colors.WARM_CAPP.getColor());
                btnCat.setForeground(Colors.CREAMY_LATTE.getColor());
                btnCat.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btnCat.setFocusPainted(false);
                btnCat.setBorder(null);
                btnCat.setBounds(x, y, width, height);

                btnCat.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        controller.showProductsByCategory(cat);
                    }
                });

                add(btnCat);

                x += width + gap;
                if (x + width > 1000) {
                    x = 50;
                    y += height + gap;
                }
            }
        }
    }
}
