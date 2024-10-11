import java.sql.*;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class stock extends javax.swing.JPanel {

    Conexion conn;
    Connection cn;
    Statement st;
    ResultSet rs;
    DefaultTableModel modelo;
    int id;
    
    public stock() {
        initComponents();
        conn = new Conexion();  // Instanciar la conexión
        conn.conectar();
        txtproducto.setEditable(false);
        txtstock.setEnabled(false);
        btnAgregar.setEnabled(false);
    }
    
    
    void buscarProducto() {
    String idProducto = txtid.getText();

    if (idProducto.equals("")) {
        JOptionPane.showMessageDialog(null, "Debe ingresar el ID del producto");
    } else {
        try {
            // Consulta para verificar si el producto existe y obtener el nombre y stock
            String sql = "SELECT nombreProducto, stock FROM productos WHERE id = ?";
            cn = conn.getConnection();
            PreparedStatement pstBuscar = cn.prepareStatement(sql);
            pstBuscar.setString(1, idProducto);
            ResultSet rs = pstBuscar.executeQuery();

            if (rs.next()) {
                // Producto encontrado, habilitar el campo para ingresar el stock adicional
                String nombreProducto = rs.getString("nombreProducto");
                int stockActual = rs.getInt("stock");

                // Mostrar el nombre del producto y stock actual
                txtproducto.setText(nombreProducto);  // Mostrar nombre del producto en el textbox
                JOptionPane.showMessageDialog(null, "Producto encontrado. Stock actual: " + stockActual);

                // Habilitar el campo para ingresar la cantidad de stock y el botón de actualización
                txtstock.setEnabled(true);
                btnAgregar.setEnabled(true);

                // Deshabilitar el campo de ID y el botón de búsqueda
                txtid.setEnabled(false);
                btnBuscar.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "El producto no existe");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + e.getMessage());
        }
    }
    
}
    
    void actualizarStock() {
        String idProducto = txtid.getText();
        String cantidadStock = txtstock.getText();

        if (cantidadStock.equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad de stock");
        } else {
            try {
                // Convertir la cantidad ingresada a número
                int cantidadNueva = Integer.parseInt(cantidadStock);

                // Consulta para actualizar el stock del producto
                String sqlActualizar = "UPDATE productos SET stock = stock + ? WHERE id = ?";
                PreparedStatement pstActualizar = cn.prepareStatement(sqlActualizar);
                pstActualizar.setInt(1, cantidadNueva);
                pstActualizar.setString(2, idProducto);
                pstActualizar.executeUpdate();

                JOptionPane.showMessageDialog(null, "Stock actualizado correctamente");

                // Limpiar y resetear los campos
                txtid.setText("");
                txtstock.setText("");
                txtstock.setEnabled(false);
                btnAgregar.setEnabled(false);
                txtid.setEnabled(true);
                btnBuscar.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar el stock: " + e.getMessage());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        txtproducto = new javax.swing.JTextField();
        txtstock = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel1.setText("INGRESO DE STOCK PRODUCTOS");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtid)
                            .addComponent(txtproducto)
                            .addComponent(txtstock, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                        .addGap(118, 118, 118)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel1)
                .addGap(97, 97, 97)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtproducto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtstock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 570));
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarProducto();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        actualizarStock();
    }//GEN-LAST:event_btnAgregarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtproducto;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}