package views;

import constants.Colors;
import models.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btnLogout;

    public Dashboard(User currentUser) {
        setTitle("Sistema de Gestión de Cafetería - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        
        contentPane = new JPanel();
        contentPane.setBackground(Colors.CREAMY_LATTE.getColor());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Colors.WARM_CAPP.getColor());
        sidebarPanel.setBounds(0, 0, 250, 720);
        sidebarPanel.setLayout(null);
        contentPane.add(sidebarPanel);
        
        JLabel lblMenuTitle = new JLabel("MENÚ");
        lblMenuTitle.setForeground(Colors.CREAMY_LATTE.getColor());
        lblMenuTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMenuTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblMenuTitle.setBounds(0, 20, 250, 30);
        sidebarPanel.add(lblMenuTitle);
        
        JButton btnNewOrder = new JButton("Nueva Orden (POS)");
        btnNewOrder.setBackground(Colors.WARM_CAPP.getColor());
        btnNewOrder.setForeground(Colors.CREAMY_LATTE.getColor());
        btnNewOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNewOrder.setBounds(0, 80, 250, 45);
        btnNewOrder.setFocusPainted(false);
        btnNewOrder.setBorder(null);
        sidebarPanel.add(btnNewOrder);
        
        JButton btnOrderHistory = new JButton("Historial de Pedidos");
        btnOrderHistory.setBackground(Colors.WARM_CAPP.getColor());
        btnOrderHistory.setForeground(Colors.CREAMY_LATTE.getColor());
        btnOrderHistory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOrderHistory.setBounds(0, 125, 250, 45);
        btnOrderHistory.setFocusPainted(false);
        btnOrderHistory.setBorder(null);
        sidebarPanel.add(btnOrderHistory);
        
        JButton btnProducts = new JButton("Productos");
        btnProducts.setBackground(Colors.WARM_CAPP.getColor());
        btnProducts.setForeground(Colors.CREAMY_LATTE.getColor());
        btnProducts.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnProducts.setBounds(0, 170, 250, 45);
        btnProducts.setFocusPainted(false);
        btnProducts.setBorder(null);
        sidebarPanel.add(btnProducts);
        
        JButton btnCategories = new JButton("Categorías");
        btnCategories.setBackground(Colors.WARM_CAPP.getColor());
        btnCategories.setForeground(Colors.CREAMY_LATTE.getColor());
        btnCategories.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCategories.setBounds(0, 215, 250, 45);
        btnCategories.setFocusPainted(false);
        btnCategories.setBorder(null);
        sidebarPanel.add(btnCategories);
        
        JButton btnSizes = new JButton("Tamaños de Bebidas");
        btnSizes.setBackground(Colors.WARM_CAPP.getColor());
        btnSizes.setForeground(Colors.CREAMY_LATTE.getColor());
        btnSizes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSizes.setBounds(0, 260, 250, 45);
        btnSizes.setFocusPainted(false);
        btnSizes.setBorder(null);
        sidebarPanel.add(btnSizes);
        
        JButton btnIngredients = new JButton("Ingredientes (Inventario)");
        btnIngredients.setBackground(Colors.WARM_CAPP.getColor());
        btnIngredients.setForeground(Colors.CREAMY_LATTE.getColor());
        btnIngredients.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngredients.setBounds(0, 305, 250, 45);
        btnIngredients.setFocusPainted(false);
        btnIngredients.setBorder(null);
        sidebarPanel.add(btnIngredients);
        
        JButton btnStockMovements = new JButton("Movimientos de Stock");
        btnStockMovements.setBackground(Colors.WARM_CAPP.getColor());
        btnStockMovements.setForeground(Colors.CREAMY_LATTE.getColor());
        btnStockMovements.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnStockMovements.setBounds(0, 350, 250, 45);
        btnStockMovements.setFocusPainted(false);
        btnStockMovements.setBorder(null);
        sidebarPanel.add(btnStockMovements);
        
        if (currentUser.getRol().equals("MANAGER") || currentUser.getRol().equals("ADMIN")) {
            JButton btnUsers = new JButton("Usuarios / Staff");
            btnUsers.setBackground(Colors.WARM_CAPP.getColor());
            btnUsers.setForeground(Colors.CREAMY_LATTE.getColor());
            btnUsers.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnUsers.setBounds(0, 395, 250, 45);
            btnUsers.setFocusPainted(false);
            btnUsers.setBorder(null);
            sidebarPanel.add(btnUsers);
        }
        
        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(Colors.MOCHA_BEAN.getColor());
        btnLogout.setForeground(Colors.CREAMY_LATTE.getColor());
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setBounds(0, 630, 250, 45);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(null);
        sidebarPanel.add(btnLogout);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Colors.CARAMEL_ROAST.getColor());
        headerPanel.setBounds(250, 0, 1030, 60);
        headerPanel.setLayout(null);
        contentPane.add(headerPanel);
        
        JLabel lblShopName = new JLabel("LOS TOSTADORES DE CAFÉ");
        lblShopName.setForeground(Colors.WARM_CAPP.getColor());
        lblShopName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblShopName.setBounds(20, 15, 400, 30);
        headerPanel.add(lblShopName);
        
        String userInfo = currentUser.getFirstName() + " " + currentUser.getLastName() + " | " + currentUser.getRol();
        JLabel lblUserInfo = new JLabel(userInfo);
        lblUserInfo.setForeground(Colors.WARM_CAPP.getColor());
        lblUserInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUserInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUserInfo.setBounds(600, 15, 390, 30);
        headerPanel.add(lblUserInfo);
        
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setBackground(Colors.CREAMY_LATTE.getColor());
        mainContentPanel.setBounds(250, 60, 1030, 660);
        mainContentPanel.setLayout(null);
        contentPane.add(mainContentPanel);
        
        JLabel lblWelcomeMsg = new JLabel("Bienvenido al Sistema - Seleccione un módulo en el menú lateral");
        lblWelcomeMsg.setForeground(Colors.MOCHA_BEAN.getColor());
        lblWelcomeMsg.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblWelcomeMsg.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcomeMsg.setBounds(0, 250, 1030, 40);
        mainContentPanel.add(lblWelcomeMsg);
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }
}
