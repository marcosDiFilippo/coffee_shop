package views;

import constants.Colors;
import controllers.OrderController;
import daos.ProductDAO;
import models.Category;
import models.Product;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProductsByCategoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public ProductsByCategoryPanel(OrderController controller, Category category) {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        JLabel lblTitle = new JLabel("PRODUCTOS - " + category.getName().toUpperCase());
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 30, 1030, 40);
        add(lblTitle);

        JButton btnBack = new JButton("Volver a Categorías");
        btnBack.setBackground(Colors.MOCHA_BEAN.getColor());
        btnBack.setForeground(Colors.CREAMY_LATTE.getColor());
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setFocusPainted(false);
        btnBack.setBounds(30, 30, 180, 40);
        btnBack.setBorder(null);
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.showCategories();
            }
        });
        add(btnBack);
        
        JButton btnGoCart = new JButton("Ver Carrito");
        btnGoCart.setBackground(Colors.CARAMEL_ROAST.getColor());
        btnGoCart.setForeground(Colors.CREAMY_LATTE.getColor());
        btnGoCart.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGoCart.setFocusPainted(false);
        btnGoCart.setBounds(820, 30, 180, 40);
        btnGoCart.setBorder(null);
        btnGoCart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.showCart();
            }
        });
        add(btnGoCart);

        JPanel listContainer = new JPanel();
        listContainer.setBackground(Colors.CREAMY_LATTE.getColor());
        listContainer.setLayout(null);
        
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAll();
        
        int y = 0;
        for (Product prod : products) {
            if (prod.isAvailable() && prod.getCategoryId().equals(category.getId())) {
                JPanel itemPanel = new JPanel();
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setLayout(null);
                itemPanel.setBounds(0, y, 950, 80);
                
                JLabel lblName = new JLabel(prod.getName());
                lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
                lblName.setForeground(Colors.MOCHA_BEAN.getColor());
                lblName.setBounds(20, 25, 400, 30);
                itemPanel.add(lblName);
                
                JLabel lblPrice = new JLabel("$ " + prod.getBasePrice().toString());
                lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
                lblPrice.setForeground(Colors.WARM_CAPP.getColor());
                lblPrice.setBounds(450, 25, 150, 30);
                itemPanel.add(lblPrice);
                
                JButton btnAdd = new JButton("Agregar a la orden");
                btnAdd.setBackground(Colors.CARAMEL_ROAST.getColor());
                btnAdd.setForeground(Colors.CREAMY_LATTE.getColor());
                btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnAdd.setFocusPainted(false);
                btnAdd.setBorder(null);
                btnAdd.setBounds(750, 20, 180, 40);
                btnAdd.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        controller.addToCart(prod);
                    }
                });
                itemPanel.add(btnAdd);
                
                listContainer.add(itemPanel);
                y += 90;
            }
        }
        
        listContainer.setPreferredSize(new Dimension(950, y));
        
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBounds(30, 90, 970, 530);
        scrollPane.setBorder(null);
        add(scrollPane);
    }
}
