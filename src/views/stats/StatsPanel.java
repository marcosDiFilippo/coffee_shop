package views.stats;

import constants.Colors;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.Color;

public class StatsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel lblTotalProductsValue;
    private JLabel lblTotalRevenueValue;
    private DefaultTableModel tableModel;

    public StatsPanel() {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        JLabel lblTitle = new JLabel("ESTADÍSTICAS DEL SISTEMA");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(0, 30, 1030, 40);
        add(lblTitle);

        JPanel pnlTotalProducts = new JPanel();
        pnlTotalProducts.setBackground(Colors.WARM_CAPP.getColor());
        pnlTotalProducts.setBounds(170, 100, 320, 120);
        pnlTotalProducts.setLayout(null);
        add(pnlTotalProducts);

        JLabel lblTotalProductsTitle = new JLabel("Total de Productos Cargados");
        lblTotalProductsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalProductsTitle.setForeground(Color.WHITE);
        lblTotalProductsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalProductsTitle.setBounds(10, 20, 300, 30);
        pnlTotalProducts.add(lblTotalProductsTitle);

        lblTotalProductsValue = new JLabel("-");
        lblTotalProductsValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalProductsValue.setForeground(Color.WHITE);
        lblTotalProductsValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotalProductsValue.setBounds(10, 60, 300, 40);
        pnlTotalProducts.add(lblTotalProductsValue);

        JPanel pnlTotalRevenue = new JPanel();
        pnlTotalRevenue.setBackground(Colors.WARM_CAPP.getColor());
        pnlTotalRevenue.setBounds(540, 100, 320, 120);
        pnlTotalRevenue.setLayout(null);
        add(pnlTotalRevenue);

        JLabel lblTotalRevenueTitle = new JLabel("Ingresos Totales (Entregados)");
        lblTotalRevenueTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalRevenueTitle.setForeground(Color.WHITE);
        lblTotalRevenueTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalRevenueTitle.setBounds(10, 20, 300, 30);
        pnlTotalRevenue.add(lblTotalRevenueTitle);

        lblTotalRevenueValue = new JLabel("$ -");
        lblTotalRevenueValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalRevenueValue.setForeground(Color.WHITE);
        lblTotalRevenueValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotalRevenueValue.setBounds(10, 60, 300, 40);
        pnlTotalRevenue.add(lblTotalRevenueValue);

        JLabel lblTableTitle = new JLabel("Producto Más Pedido por Categoría");
        lblTableTitle.setForeground(Colors.MOCHA_BEAN.getColor());
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTableTitle.setBounds(115, 260, 800, 30);
        add(lblTableTitle);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(115, 300, 800, 320);
        scrollPane.setBorder(new LineBorder(Colors.MOCHA_BEAN.getColor(), 2));
        add(scrollPane);

        String[] columnNames = {"Categoría", "Producto Más Vendido", "Cantidad Vendida"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Colors.MOCHA_BEAN.getColor());
        table.getTableHeader().setForeground(Color.WHITE);
        scrollPane.setViewportView(table);
    }

    public JLabel getLblTotalProductsValue() {
        return lblTotalProductsValue;
    }

    public JLabel getLblTotalRevenueValue() {
        return lblTotalRevenueValue;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
