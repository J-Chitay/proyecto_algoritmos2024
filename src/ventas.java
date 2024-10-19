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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabledatos = new javax.swing.JTable();
        lblUser = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtcantidad = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtstock = new javax.swing.JTextField();
        txtproducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnVender = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
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

        lblUser.setText("jLabel6");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Operaciones"));

        txtcantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcantidadKeyTyped(evt);
            }
        });

        jLabel5.setText("VENDER");

        jLabel4.setText("STOCK");

        txtstock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtstockKeyTyped(evt);
            }
        });

        jLabel3.setText("NOMBRE");

        jLabel2.setText("ID");

        txtid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtidKeyTyped(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(13, 110, 253));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnVender.setBackground(new java.awt.Color(13, 110, 253));
        btnVender.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVender.setForeground(new java.awt.Color(255, 255, 255));
        btnVender.setText("VENDER");
        btnVender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtcantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                    .addComponent(txtid)
                    .addComponent(txtproducto)
                    .addComponent(txtstock))
                .addGap(55, 55, 55)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVender, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(194, 194, 194))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVender, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtstock, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(125, 125, 125)
                        .addComponent(lblUser)
                        .addGap(87, 87, 87))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(28, 28, 28))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(lblUser))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1)))
                .addGap(23, 23, 23)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 590));
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

    private void txtstockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtstockKeyTyped
       char c = evt.getKeyChar();
        // Si no es un número (dígito) o la tecla de retroceso (para borrar), lo ignoramos
        if (!Character.isDigit(c)) {
            evt.consume(); // Evitar que el carácter se ingrese
        }
    }//GEN-LAST:event_txtstockKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnVender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTable tabledatos;
    private javax.swing.JTextField txtcantidad;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtproducto;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}
