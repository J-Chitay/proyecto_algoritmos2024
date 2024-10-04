import java.sql.*;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class crearProducto extends javax.swing.JPanel {
    Conexion conn;
    Connection cn;
    Statement st;
    ResultSet rs;
    DefaultTableModel modelo;
    int id;
    
    public crearProducto() {
        initComponents();
        conn = new Conexion();  // Instanciar la conexión
        conn.conectar();
        llenarComboBoxCategorias();
        llenarComboBoxCaracteristicas();
    }
    
    // Método para llenar el JComboBox con las categorías de la base de datos
    private void llenarComboBoxCategorias() {
        try {
            String sql = "SELECT id, nombreCategoria FROM categorias";
            cn = conn.getConnection();  // Conexión a la base de datos
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Limpiar el ComboBox antes de llenarlo
            comboBoxCategorias.removeAllItems();

            // Agregar cada categoría al ComboBox
            while (rs.next()) {
                String categoria = rs.getString("nombreCategoria");
                comboBoxCategorias.addItem(categoria);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar categorías: " + e.getMessage());
        }
    }
    
    private void llenarComboBoxCaracteristicas() {
        try {
            String sql = "SELECT id, caracteristica FROM caracteristicas";
            cn = conn.getConnection();  // Conexión a la base de datos
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Limpiar el ComboBox antes de llenarlo
            cbmCaracteristica1.removeAllItems();
            cbmCaracteristica2.removeAllItems();

            // Agregar cada categoría al ComboBox
            while (rs.next()) {
                String caracteristica = rs.getString("caracteristica");
                cbmCaracteristica1.addItem(caracteristica);
                cbmCaracteristica2.addItem(caracteristica);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar categorías: " + e.getMessage());
        }
    }
    
    void listar(){
        // Consulta SQL
        String sql ="SELECT * FROM productos";
        
        try{
            cn = conn.getConnection();
            // Crear el statement y ejecutar la consulta
            st = cn.createStatement();
            rs=st.executeQuery(sql);
            // Arreglo para almacenar los datos de cada fila
            Object[]productos=new Object[7];
            // Obtener el modelo de la tabla
            modelo=(DefaultTableModel)tabledatos.getModel();
            // Limpiar las filas existentes en el modelo de la tabla
            modelo.setRowCount(0);
            // Iterar sobre los resultados de la consulta
            while (rs.next()){
                productos[0]=rs.getInt("id");
                productos[1]=rs.getString("nombreProducto");
                productos[2]=rs.getString("id_categoria");
                productos[3]=rs.getString("id_caracteristica1");
                productos[4]=rs.getString("id_caracteristica2");
                productos[5]=rs.getString("precio");
                productos[6]=rs.getString("stock");
                productos[7]=rs.getString("descripcion");
                // Añadir fila al modelo
                modelo.addRow(productos);
            }
            // Actualizar el modelo de la tabla
            tabledatos.setModel(modelo);
        } catch(SQLException e){
            // Manejar errores e imprimirlos en la consola
            System.out.println("Error al listar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    void limpiartabla(){
        for(int i=0; i<=tabledatos.getRowCount();i++){
            modelo.removeRow(i);
            i=i-1;
        }
    }
    
    void nuevo(){
        txtid.setText("");
        txtproducto.setText("");
        txtprecio.setText("");
        txtstock.setText("");
        txtdescripcion.setText("");
        txtproducto.requestFocus();
        txtprecio.requestFocus();
        txtstock.requestFocus();
        txtdescripcion.requestFocus();
    }
    
    
    void Agregar() {
        String nombreProducto = txtproducto.getText();
        String id_categoria = comboBoxCategorias.getSelectedItem().toString();
        String id_caracteristica1 = cbmCaracteristica1.getSelectedItem().toString();
        String id_caracteristica2 = cbmCaracteristica2.getSelectedItem().toString();
        String precio = txtprecio.getText();
        String stock = txtstock.getText();
        String descripcion = txtdescripcion.getText();

        if (nombreProducto.equals("") || precio.equals("") || stock.equals("")){
            JOptionPane.showMessageDialog(null, "Los campos nombre, precio, stock no pueden estar vacíos");
        } else {
            try {
                cn = conn.getConnection();
                // Verificar si el usuario ya existe
                String sqlVerificar = "SELECT COUNT(*) FROM productos WHERE nombreProducto = ?";
                PreparedStatement pstVerificar = cn.prepareStatement(sqlVerificar);
                pstVerificar.setString(1, nombreProducto);
                ResultSet rs = pstVerificar.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // Si el usuario ya existe, mostrar un mensaje
                    JOptionPane.showMessageDialog(null, "El producto ya existe");
                } else {
                    // Si no existe, insertar el nuevo usuario
                    String sqlInsertar = "INSERT INTO productos(nombreProducto, id_categoria, id_caracteristica1, id_caracteristica2, precio, stock, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstInsertar = cn.prepareStatement(sqlInsertar);
                    pstInsertar.setString(1, nombreProducto);                 
                    pstInsertar.setString(2, id_categoria);
                    pstInsertar.setString(3, id_caracteristica1);
                    pstInsertar.setString(4, id_caracteristica2);
                    pstInsertar.setString(5, precio);
                    pstInsertar.setString(6, stock);
                    pstInsertar.setString(7, descripcion);
                    pstInsertar.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Producto agregado exitosamente");
                    limpiartabla();
                }
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "Error al agregar el usuario: " + e.getMessage());
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
        jPanel2 = new javax.swing.JPanel();
        txtid = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtdescripcion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        txtproducto = new javax.swing.JTextField();
        comboBoxCategorias = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cbmCaracteristica1 = new javax.swing.JComboBox<>();
        cbmCaracteristica2 = new javax.swing.JComboBox<>();
        txtprecio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtstock = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnmodificar = new javax.swing.JButton();
        btnagregar = new javax.swing.JButton();
        btnnuevo = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabledatos = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(102, 255, 102));

        jLabel1.setText("REGISTRO DE PRODUCTOS");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        txtid.setEditable(false);
        txtid.setEnabled(false);

        jLabel2.setText("NOMBRE PRODUCTO");

        jLabel3.setText("Id");

        txtdescripcion.setColumns(20);
        txtdescripcion.setRows(5);
        jScrollPane2.setViewportView(txtdescripcion);

        jLabel4.setText("CATEGORIA");

        jLabel5.setText("CARACTERISTICAS");

        jLabel6.setText("PRECIO");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(140, 140, 140)
                        .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(87, 87, 87)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtproducto, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                            .addComponent(comboBoxCategorias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbmCaracteristica1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbmCaracteristica2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtprecio))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtstock, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtstock, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(cbmCaracteristica1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(cbmCaracteristica2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Operaciones"));

        btnmodificar.setText("MODIFICAR");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });

        btnagregar.setText("AGREGAR");
        btnagregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnagregarActionPerformed(evt);
            }
        });

        btnnuevo.setText("NUEVO");
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });

        btneliminar.setText("ELIMINAR");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnnuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(btnagregar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(435, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnnuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(7, Short.MAX_VALUE)
                    .addComponent(btnagregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(17, 17, 17)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista"));

        tabledatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PRODUCTO", "CATEGORIA", "CARACTERISTICA1", "CARACTERISTICA2", "PRECIO", "STOCK", "DESCRIPCION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true, true, true, true
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
        if (tabledatos.getColumnModel().getColumnCount() > 0) {
            tabledatos.getColumnModel().getColumn(0).setMinWidth(30);
            tabledatos.getColumnModel().getColumn(0).setPreferredWidth(30);
            tabledatos.getColumnModel().getColumn(0).setMaxWidth(30);
            tabledatos.getColumnModel().getColumn(5).setMinWidth(40);
            tabledatos.getColumnModel().getColumn(5).setPreferredWidth(40);
            tabledatos.getColumnModel().getColumn(5).setMaxWidth(40);
            tabledatos.getColumnModel().getColumn(6).setMinWidth(30);
            tabledatos.getColumnModel().getColumn(6).setPreferredWidth(30);
            tabledatos.getColumnModel().getColumn(6).setMaxWidth(30);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(221, 221, 221))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 3, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        /*modificar();
        listar();
        nuevo();*/
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btnagregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnagregarActionPerformed
        Agregar();
        listar();
        nuevo();
    }//GEN-LAST:event_btnagregarActionPerformed

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_btnnuevoActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        /*eliminar();
        listar();
        nuevo();*/
    }//GEN-LAST:event_btneliminarActionPerformed

    private void tabledatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledatosMouseClicked
        int fila=tabledatos.getSelectedRow();
        if(fila==-1){
            JOptionPane.showMessageDialog(null, "productio no seleccionado");
        }else{
            id=Integer.parseInt((String)tabledatos.getValueAt(fila, 0).toString());
            String nombreProducto=(String)tabledatos.getValueAt(fila, 1);
            String id_categoria=(String)tabledatos.getValueAt(fila, 2);
            String id_caracteristica1=(String)tabledatos.getValueAt(fila, 3);
            String id_caracteristica2=(String)tabledatos.getValueAt(fila, 4);
            String precio=(String)tabledatos.getValueAt(fila, 5);
            String stock=(String)tabledatos.getValueAt(fila, 6);
            String descripcion=(String)tabledatos.getValueAt(fila, 7);
            txtid.setText(""+id);
            txtproducto.setText(nombreProducto);
            comboBoxCategorias.setSelectedItem(id_categoria);
            cbmCaracteristica1.setSelectedItem(id_caracteristica1);
            cbmCaracteristica2.setSelectedItem(id_caracteristica2);
            txtprecio.setText(precio);
            txtstock.setText(stock);
            txtdescripcion.setText(descripcion);
        }
    }//GEN-LAST:event_tabledatosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnagregar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JComboBox<String> cbmCaracteristica1;
    private javax.swing.JComboBox<String> cbmCaracteristica2;
    private javax.swing.JComboBox<String> comboBoxCategorias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabledatos;
    private javax.swing.JTextArea txtdescripcion;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtprecio;
    private javax.swing.JTextField txtproducto;
    private javax.swing.JTextField txtstock;
    // End of variables declaration//GEN-END:variables
}
