package views;

import constants.Colors;
import controllers.OrderController;
import dtos.OrderItemDTO;

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
import java.math.BigDecimal;
import java.util.List;

public class OrderPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public OrderPanel(OrderController controller, List<OrderItemDTO> cartItems) {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        JLabel lblTitle = new JLabel("ORDEN");
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 30, 1030, 40);
        add(lblTitle);

        JButton btnBack = new JButton("Seguir Agregando");
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

        JPanel listContainer = new JPanel();
        listContainer.setBackground(Colors.CREAMY_LATTE.getColor());
        listContainer.setLayout(null);
        
        int y = 0;
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO item : cartItems) {
            total = total.add(item.getSubtotal());

            JPanel itemPanel = new JPanel();
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setLayout(null);
            itemPanel.setBounds(0, y, 950, 80);
            
            String displayName = item.getProduct().getName();
            if (item.getSize() != null) {
                displayName += " (" + item.getSize().getName() + ")";
            }

            JLabel lblName = new JLabel(displayName);
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblName.setForeground(Colors.MOCHA_BEAN.getColor());
            lblName.setBounds(20, 25, 400, 30);
            itemPanel.add(lblName);
            
            JLabel lblPrice = new JLabel("$ " + item.getSubtotal().setScale(2, java.math.RoundingMode.HALF_UP).toString());
            lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblPrice.setForeground(Colors.WARM_CAPP.getColor());
            lblPrice.setBounds(450, 25, 100, 30);
            itemPanel.add(lblPrice);
            
            JButton btnMinus = new JButton("-");
            btnMinus.setBackground(Colors.WARM_CAPP.getColor());
            btnMinus.setForeground(Color.WHITE);
            btnMinus.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnMinus.setFocusPainted(false);
            btnMinus.setBorder(null);
            btnMinus.setBounds(600, 20, 40, 40);
            btnMinus.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    controller.decreaseQuantity(item);
                }
            });
            itemPanel.add(btnMinus);
            
            JLabel lblQty = new JLabel(String.valueOf(item.getQuantity()));
            lblQty.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblQty.setHorizontalAlignment(SwingConstants.CENTER);
            lblQty.setBounds(650, 25, 40, 30);
            itemPanel.add(lblQty);
            
            JButton btnPlus = new JButton("+");
            btnPlus.setBackground(Colors.WARM_CAPP.getColor());
            btnPlus.setForeground(Color.WHITE);
            btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnPlus.setFocusPainted(false);
            btnPlus.setBorder(null);
            btnPlus.setBounds(700, 20, 40, 40);
            btnPlus.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    controller.increaseQuantity(item);
                }
            });
            itemPanel.add(btnPlus);
            
            JButton btnTrash = new JButton("X");
            btnTrash.setBackground(Color.RED);
            btnTrash.setForeground(Color.WHITE);
            btnTrash.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnTrash.setFocusPainted(false);
            btnTrash.setBorder(null);
            btnTrash.setBounds(850, 20, 50, 40);
            btnTrash.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    controller.removeItem(item);
                }
            });
            itemPanel.add(btnTrash);
            
            listContainer.add(itemPanel);
            y += 90;
        }
        
        listContainer.setPreferredSize(new Dimension(950, y));
        
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBounds(30, 90, 970, 450);
        scrollPane.setBorder(null);
        add(scrollPane);

        JLabel lblTotal = new JLabel("TOTAL: $ " + total.setScale(2, java.math.RoundingMode.HALF_UP).toString());
        lblTotal.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBounds(500, 560, 280, 50);
        add(lblTotal);

        JButton btnConfirm = new JButton("Confirmar Orden");
        btnConfirm.setBackground(Colors.WARM_CAPP.getColor());
        btnConfirm.setForeground(Colors.CREAMY_LATTE.getColor());
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBounds(800, 560, 200, 50);
        btnConfirm.setBorder(null);
        if (cartItems.isEmpty()) {
            btnConfirm.setEnabled(false);
        }
        btnConfirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!cartItems.isEmpty()) {
                    views.CustomerDialog dialog = new views.CustomerDialog((javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(OrderPanel.this), controller);
                    dialog.setVisible(true);
                }
            }
        });
        add(btnConfirm);
    }
}
