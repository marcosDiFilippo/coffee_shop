package views.users;

import constants.Colors;
import controllers.UserController;
import views.Dashboard;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UsersPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton btnAddUser;
    private JTable tableUsers;
    private DefaultTableModel tableModel;
    private UserController controller;
    private JComboBox<String> cmbFilter;

    public UsersPanel(Dashboard dashboard) {
        setBackground(Colors.CREAMY_LATTE.getColor());
        setLayout(null);
        setBounds(0, 0, 1030, 660);

        btnAddUser = new JButton("Agregar Usuario");
        btnAddUser.setBackground(Colors.WARM_CAPP.getColor());
        btnAddUser.setForeground(Colors.CREAMY_LATTE.getColor());
        btnAddUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddUser.setFocusPainted(false);
        btnAddUser.setBounds(30, 30, 180, 40);
        btnAddUser.setBorder(null);
        btnAddUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Dashboard dashboard = (Dashboard) SwingUtilities.getWindowAncestor(UsersPanel.this);
                UserFormDialog formDialog = new UserFormDialog(dashboard, controller, null);
                formDialog.setVisible(true);
            }
        });
        add(btnAddUser);

        String[] filterOptions = {"Todos", "Clientes", "Trabajadores"};
        cmbFilter = new JComboBox<>(filterOptions);
        cmbFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbFilter.setBounds(230, 30, 180, 40);
        cmbFilter.setBackground(Color.WHITE);
        cmbFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    controller.loadUsers(cmbFilter.getSelectedItem().toString());
                }
            }
        });
        add(cmbFilter);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 90, 970, 530);
        add(scrollPane);

        String[] columnNames = {"ID", "Nombre Completo", "Email", "Teléfono", "Rol", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        tableUsers = new JTable(tableModel);
        tableUsers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableUsers.setRowHeight(40);
        tableUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableUsers.getTableHeader().setBackground(Colors.MOCHA_BEAN.getColor());
        tableUsers.getTableHeader().setForeground(Colors.CREAMY_LATTE.getColor());
        tableUsers.getTableHeader().setReorderingAllowed(false);
        
        tableUsers.removeColumn(tableUsers.getColumnModel().getColumn(0));

        tableUsers.getColumnModel().getColumn(5).setPreferredWidth(200);

        tableUsers.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    if (value.toString().equals("Activo")) {
                        c.setForeground(Colors.MOCHA_BEAN.getColor());
                    } else {
                        c.setForeground(Color.RED);
                    }
                }
                return c;
            }
        });

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridBagLayout());
        actionPanel.setBackground(Colors.CREAMY_LATTE.getColor());
        
        JButton btnTableEdit = new JButton("Editar");
        btnTableEdit.setBackground(Colors.CARAMEL_ROAST.getColor());
        btnTableEdit.setForeground(Colors.CREAMY_LATTE.getColor());
        btnTableEdit.setFocusPainted(false);
        btnTableEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableUsers.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    TableCellEditor editor = tableUsers.getCellEditor();
                    if (editor != null) editor.stopCellEditing();
                    
                    controller.openEditDialog(id);
                }
            }
        });
        
        JButton btnTableToggle = new JButton("Deshabilitar");
        btnTableToggle.setBackground(Colors.MOCHA_BEAN.getColor());
        btnTableToggle.setForeground(Colors.CREAMY_LATTE.getColor());
        btnTableToggle.setFocusPainted(false);
        btnTableToggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!btnTableToggle.isVisible()) return;
                int row = tableUsers.getEditingRow();
                if (row != -1) {
                    Long id = (Long) tableModel.getValueAt(row, 0);
                    if (id.equals(dashboard.getCurrentUser().getId())) {
                        TableCellEditor editor = tableUsers.getCellEditor();
                        if (editor != null) editor.stopCellEditing();
                        return;
                    }
                    boolean active = tableModel.getValueAt(row, 5).equals("Activo");
                    
                    TableCellEditor editor = tableUsers.getCellEditor();
                    if (editor != null) editor.stopCellEditing();
                    
                    controller.toggleUser(id, active);
                }
            }
        });
        
        actionPanel.add(btnTableEdit);
        actionPanel.add(btnTableToggle);

        tableUsers.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            private JPanel panel = new JPanel(new GridBagLayout());
            private JButton bEdit = new JButton("Editar");
            private JButton bToggle = new JButton("Deshabilitar");
            {
                panel.setBackground(Colors.CREAMY_LATTE.getColor());
                bEdit.setBackground(Colors.CARAMEL_ROAST.getColor());
                bEdit.setForeground(Colors.CREAMY_LATTE.getColor());
                bEdit.setFocusPainted(false);
                bToggle.setBackground(Colors.MOCHA_BEAN.getColor());
                bToggle.setForeground(Colors.CREAMY_LATTE.getColor());
                bToggle.setFocusPainted(false);
                panel.add(bEdit);
                panel.add(bToggle);
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (isSelected) panel.setBackground(table.getSelectionBackground());
                else panel.setBackground(table.getBackground());
                
                Object statusValue = table.getModel().getValueAt(row, 5);
                if (statusValue != null && statusValue.toString().equals("Inactivo")) {
                    bToggle.setText("Habilitar");
                } else {
                    bToggle.setText("Deshabilitar");
                }
                
                Object rowId = table.getModel().getValueAt(row, 0);
                if (rowId != null && rowId.equals(dashboard.getCurrentUser().getId())) {
                    bToggle.setVisible(false);
                } else {
                    bToggle.setVisible(true);
                }
                
                return panel;
            }
        });

        tableUsers.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (isSelected) actionPanel.setBackground(table.getSelectionBackground());
                else actionPanel.setBackground(table.getBackground());

                Object statusValue = table.getModel().getValueAt(row, 5);
                if (statusValue != null && statusValue.toString().equals("Inactivo")) {
                    btnTableToggle.setText("Habilitar");
                } else {
                    btnTableToggle.setText("Deshabilitar");
                }
                
                Object rowId = table.getModel().getValueAt(row, 0);
                if (rowId != null && rowId.equals(dashboard.getCurrentUser().getId())) {
                    btnTableToggle.setVisible(false);
                } else {
                    btnTableToggle.setVisible(true);
                }
                
                return actionPanel;
            }
            @Override
            public Object getCellEditorValue() {
                return null;
            }
        });

        scrollPane.setViewportView(tableUsers);
        
        controller = new UserController(this);
    }

    public JTable getTableUsers() {
        return tableUsers;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
