
import java.sql.*;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class crearCaracteristicass extends javax.swing.JPanel {

    Conexion conn;
    Connection cn;
    Statement st;
    ResultSet rs;
    DefaultTableModel modelo;
    int id;
    
    public crearCaracteristicass() {
        initComponents();
        conn = new Conexion();  // Instanciar la conexión
        conn.conectar();
        listar();
    }
    
    
    void listar(){
        // Consulta SQL
        String sql ="SELECT * FROM caracteristicas";
        
        try{
            cn = conn.getConnection();
            // Crear el statement y ejecutar la consulta
            st = cn.createStatement();
            rs=st.executeQuery(sql);
            // Arreglo para almacenar los datos de cada fila
            Object[]caracteristicas=new Object[3];
            // Obtener el modelo de la tabla
            modelo=(DefaultTableModel)tabledatos.getModel();
            // Limpiar las filas existentes en el modelo de la tabla
            modelo.setRowCount(0);
            // Iterar sobre los resultados de la consulta
            while (rs.next()){
                caracteristicas[0]=rs.getInt("id");
                caracteristicas[1]=rs.getString("caracteristica");
                caracteristicas[2]=rs.getString("descripcion");
                // Añadir fila al modelo
                modelo.addRow(caracteristicas);
            }
            // Actualizar el modelo de la tabla
            tabledatos.setModel(modelo);
        } catch(SQLException e){
            // Manejar errores e imprimirlos en la consola
            System.out.println("Error al listar las categorías: " + e.getMessage());
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
        txtcaracteristica.setText("");
        txtdescripcion.setText("");
        txtcaracteristica.requestFocus();
        txtdescripcion.requestFocus();
    }
    
    void Agregar() {
        String caracteristicas = txtcaracteristica.getText();
        String descripcion = txtdescripcion.getText();

        if (caracteristicas.equals("")) {
            JOptionPane.showMessageDialog(null, "La caja de caracteristica está vacía");
        } else {
            try {
                cn = conn.getConnection();
                // Verificar si la categoría ya existe
                String sqlVerificar = "SELECT COUNT(*) FROM caracteristicas WHERE caracteristica = ?";
                PreparedStatement pstVerificar = cn.prepareStatement(sqlVerificar);
                pstVerificar.setString(1, caracteristicas);
                ResultSet rs = pstVerificar.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // Si la categoría ya existe, mostrar un mensaje
                    JOptionPane.showMessageDialog(null, "La caracteristica ya existe");
                } else {
                    // Si no existe, insertar la nueva categoría
                    String sqlInsertar = "INSERT INTO caracteristicas(caracteristica, descripcion) VALUES (?, ?)";
                    PreparedStatement pstInsertar = cn.prepareStatement(sqlInsertar);
                    pstInsertar.setString(1, caracteristicas);
                    pstInsertar.setString(2, descripcion);
                    pstInsertar.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Caracteristica agregada exitosamente");
                    limpiartabla();
                }
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(null, "Error al agregar la categoría: " + e.getMessage());
            }
        }
    }
    
    void modificar(){
        String caracteristica = txtcaracteristica.getText();
        String descripcion = txtdescripcion.getText();
        String sql="UPDATE caracteristicas set caracteristica='"+ caracteristica +"', descripcion='"+ descripcion +"' WHERE id="+id;
        if(caracteristica.equals("")){
            JOptionPane.showMessageDialog(null, "Debe ingresar datos");
        }else{
            try{
                cn=conn.getConnection();
                st=cn.createStatement();
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Caracteristica actualizado");
            }catch(Exception e){
                limpiartabla();
            }
        }
    }
    
    void eliminar() {
        int filaseleccionado = tabledatos.getSelectedRow();

        if (filaseleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila");
        } else {
            // Confirmar la eliminación del usuario
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar esta caracteeristica?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    // Obtener el usuario de la fila seleccionada
                    String caracteristica = (String) tabledatos.getValueAt(filaseleccionado, 1);  // Asumiendo que la columna 1 es 'usuario'

                    String sql = "DELETE FROM caracteristicas WHERE caracteristica = ?";
                    cn = conn.getConnection();
                    PreparedStatement pstEliminar = cn.prepareStatement(sql);
                    pstEliminar.setString(1, caracteristica);
                    pstEliminar.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Caracteristica eliminado");
                    limpiartabla();
                } catch (Exception e) {
                    //JOptionPane.showMessageDialog(null, "Error al eliminar el usuario: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Eliminación cancelada");
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
        txtcaracteristica = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtdescripcion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnmodificar = new javax.swing.JButton();
        btnagregar = new javax.swing.JButton();
        btnnuevo = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabledatos = new javax.swing.JTable();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("REGISTRO DE CARACTERISTICAS");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        txtid.setEditable(false);
        txtid.setEnabled(false);

        jLabel2.setText("NOMBRE CARACTERISTICA");

        jLabel3.setText("Id");

        txtdescripcion.setColumns(20);
        txtdescripcion.setRows(5);
        jScrollPane2.setViewportView(txtdescripcion);

        jLabel4.setText("DESCRIPCION");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtcaracteristica, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtcaracteristica, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Operaciones"));

        btnmodificar.setBackground(new java.awt.Color(13, 110, 253));
        btnmodificar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnmodificar.setForeground(new java.awt.Color(255, 255, 255));
        btnmodificar.setText("MODIFICAR");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });

        btnagregar.setBackground(new java.awt.Color(13, 110, 253));
        btnagregar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnagregar.setForeground(new java.awt.Color(255, 255, 255));
        btnagregar.setText("AGREGAR");
        btnagregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnagregarActionPerformed(evt);
            }
        });

        btnnuevo.setBackground(new java.awt.Color(13, 110, 253));
        btnnuevo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnnuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnnuevo.setText("NUEVO");
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });

        btneliminar.setBackground(new java.awt.Color(13, 110, 253));
        btneliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(255, 255, 255));
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
                    .addContainerGap(426, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnnuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(35, Short.MAX_VALUE)
                    .addComponent(btnagregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(17, 17, 17)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista"));

        tabledatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CARACTERISTICA", "DESCRIPCION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true
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
            tabledatos.getColumnModel().getColumn(0).setMinWidth(40);
            tabledatos.getColumnModel().getColumn(0).setPreferredWidth(40);
            tabledatos.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(197, 197, 197))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 580));
    }// </editor-fold>//GEN-END:initComponents

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        modificar();
        listar();
        nuevo();
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
        eliminar();
        listar();
        nuevo();
    }//GEN-LAST:event_btneliminarActionPerformed

    private void tabledatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledatosMouseClicked
        int fila=tabledatos.getSelectedRow();
        if(fila==-1){
            JOptionPane.showMessageDialog(null, "Usuario no seleccionado");
        }else{
            id=Integer.parseInt((String)tabledatos.getValueAt(fila, 0).toString());
            String caracteristica=(String)tabledatos.getValueAt(fila, 1);
            String descripcion=(String)tabledatos.getValueAt(fila, 2);
            txtid.setText(""+id);
            txtcaracteristica.setText(caracteristica);
            txtdescripcion.setText(descripcion);

        }
    }//GEN-LAST:event_tabledatosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnagregar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabledatos;
    private javax.swing.JTextField txtcaracteristica;
    private javax.swing.JTextArea txtdescripcion;
    private javax.swing.JTextField txtid;
    // End of variables declaration//GEN-END:variables
}
