import java.sql.*;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ventas extends javax.swing.JPanel {
    Conexion conn;
    Connection cn;
    Statement st;
    ResultSet rs;
    DefaultTableModel modelo;
    int id;
    private String nombreUsuario;
    
    public ventas(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;  // Guardas el nombre de usuario
        initComponents();
        conn = new Conexion();  // Instanciar la conexión
        conn.conectar();
        listar();
        txtproducto.setEditable(false);
        txtcantidad.setEnabled(false);
        btnVender.setEnabled(false);
        
        lblUser.setText("Usuario: " + nombreUsuario);
    }
    
    void listar(){
        // Consulta SQL
        String sql ="SELECT * FROM ventas";
        
        try{
            cn = conn.getConnection();
            // Crear el statement y ejecutar la consulta
            st = cn.createStatement();
            rs=st.executeQuery(sql);
            // Arreglo para almacenar los datos de cada fila
            Object[]ventas=new Object[4];
            // Obtener el modelo de la tabla
            modelo=(DefaultTableModel)tabledatos.getModel();
            // Limpiar las filas existentes en el modelo de la tabla
            modelo.setRowCount(0);
            // Iterar sobre los resultados de la consulta
            while (rs.next()){
                ventas[0]=rs.getInt("idVenta");
                ventas[1]=rs.getString("codigoProducto");
                ventas[2]=rs.getString("cantidadVendida");
                ventas[3]=rs.getString("fechaHora");
                // Añadir fila al modelo
                modelo.addRow(ventas);
            }
            // Actualizar el modelo de la tabla
            tabledatos.setModel(modelo);
        } catch(SQLException e){
            // Manejar errores e imprimirlos en la consola
            System.out.println("Error al listar los ventas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    void buscarProducto() {
        String codigoProducto = txtid.getText();

        if (codigoProducto.equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el código del producto");
        } else {
            try {
                // Consulta para verificar si el producto existe y obtener su nombre y stock
                String sql = "SELECT nombreProducto, stock FROM productos WHERE id = ?";
                cn = conn.getConnection();
                PreparedStatement pstBuscar = cn.prepareStatement(sql);
                pstBuscar.setString(1, codigoProducto);
                ResultSet rs = pstBuscar.executeQuery();

                if (rs.next()) {
                    // Producto encontrado
                    String nombreProducto = rs.getString("nombreProducto");
                    int stockActual = rs.getInt("stock");

                    // Mostrar el nombre y el stock disponible al usuario
                    txtproducto.setText(nombreProducto); // Mostramos el nombre del producto
                    txtstock.setText(String.valueOf(stockActual));
                    JOptionPane.showMessageDialog(null, "Producto encontrado. Stock disponible: " + stockActual);

                    // Habilitar campo para ingresar la cantidad de venta
                    txtcantidad.setEnabled(true);
                    btnVender.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(null, "El producto no está registrado");
                    txtproducto.setText(""); // Limpiamos el nombre si no se encuentra el producto
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + e.getMessage());
            }
        }
    }
    
    void confirmarVenta() {
        String codigoProducto = txtid.getText();
        String cantidadVenta = txtcantidad.getText();

        if (cantidadVenta.equals("")) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la cantidad de productos a vender");
        } else {
            try {
                // Convertir la cantidad de venta a número
                int cantidadAComprar = Integer.parseInt(cantidadVenta);

                // Obtener el stock actual
                String sqlStock = "SELECT stock FROM productos WHERE id = ?";
                PreparedStatement pstStock = cn.prepareStatement(sqlStock);
                pstStock.setString(1, codigoProducto);
                ResultSet rsStock = pstStock.executeQuery();

                if (rsStock.next()) {
                    int stockActual = rsStock.getInt("stock");

                    // Verificar si la cantidad es suficiente
                    if (cantidadAComprar > stockActual) {
                        JOptionPane.showMessageDialog(null, "No hay suficiente stock para realizar la venta");
                    } else {
                        // Actualizar el stock
                        String sqlActualizarStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";
                        PreparedStatement pstActualizar = cn.prepareStatement(sqlActualizarStock);
                        pstActualizar.setInt(1, cantidadAComprar);
                        pstActualizar.setString(2, codigoProducto);
                        pstActualizar.executeUpdate();

                        // Registrar la venta
                        String sqlRegistrarVenta = "INSERT INTO ventas(codigoProducto, cantidadVendida, fechaHora) VALUES (?, ?, NOW())";
                        PreparedStatement pstVenta = cn.prepareStatement(sqlRegistrarVenta);
                        pstVenta.setString(1, codigoProducto);
                        pstVenta.setInt(2, cantidadAComprar);
                        pstVenta.executeUpdate();
                        
                        JOptionPane.showMessageDialog(null, "Venta realizada correctamente");
                        
                        String sqlInsertarMovimiento = "INSERT INTO historial_stock (id_producto, operacion, cantidad, usuario) VALUES (?, ?, ?, ?)";
                        PreparedStatement pstMovimiento = cn.prepareStatement(sqlInsertarMovimiento);
                        pstMovimiento.setString(1, codigoProducto); // ID del producto seleccionado
                        pstMovimiento.setString(2, "salida"); // 'entrada' o 'salida'
                        pstMovimiento.setString(3, cantidadVenta); // Cantidad agregada o retirada
                        pstMovimiento.setString(4, nombreUsuario); // Usuario que realiza la operación
                        pstMovimiento.executeUpdate();
                
                        JOptionPane.showMessageDialog(null, "Se agrego el movimiento al historial");

                        
                        listar();
                        // Limpiar los campos
                        txtid.setText("");
                        txtcantidad.setText("");
                        txtcantidad.setEnabled(false);
                        btnVender.setEnabled(false);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al realizar la venta: " + e.getMessage());
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
        txtid = new javax.swing.JTextField();
        txtproducto = new javax.swing.JTextField();
        txtcantidad = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnVender = new javax.swing.JButton();
        txtstock = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabledatos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        txtid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtidKeyTyped(evt);
            }
        });

        txtcantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcantidadKeyTyped(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnVender.setText("Vender");
        btnVender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenderActionPerformed(evt);
            }
        });

        jLabel1.setText("VENDER PRODUCTOS");

        tabledatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID VENTA", "ID PRODUCTO", "CANTIDAD VENDIDA", "FECHA Y HORA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabledatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabledatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabledatos);

        jLabel2.setText("ID");

        jLabel3.setText("NOMBRE");

        jLabel4.setText("STOCK");

        jLabel5.setText("VENDER");

        lblUser.setText("jLabel6");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtstock, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                    .addComponent(txtcantidad)
                    .addComponent(txtproducto)
                    .addComponent(txtid))
                .addGap(105, 105, 105)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVender, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 38, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(227, 227, 227)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUser)
                .addGap(87, 87, 87))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblUser))
                .addGap(69, 69, 69)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVender, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtstock, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(56, 56, 56)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 570));
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarProducto();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenderActionPerformed
        confirmarVenta();
    }//GEN-LAST:event_btnVenderActionPerformed

    private void tabledatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledatosMouseClicked
        
    }//GEN-LAST:event_tabledatosMouseClicked

    private void txtidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtidKeyTyped
        char c = evt.getKeyChar();
        // Si no es un número (dígito) o la tecla de retroceso (para borrar), lo ignoramos
        if (!Character.isDigit(c)) {
            evt.consume(); // Evitar que el carácter se ingrese
        }
    }//GEN-LAST:event_txtidKeyTyped

    private void txtcantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcantidadKeyTyped
        char c = evt.getKeyChar();
        // Si no es un número (dígito) o la tecla de retroceso (para borrar), lo ignoramos
        if (!Character.isDigit(c)) {
            evt.consume(); // Evitar que el carácter se ingrese
        }
    }//GEN-LAST:event_txtcantidadKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnVender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTable tabledatos;
    private javax.swing.JTextField txtcantidad;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtproducto;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}
