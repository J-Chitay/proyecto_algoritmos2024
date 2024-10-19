import java.awt.BorderLayout;
import java.awt.Dimension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class informesYgraficas extends javax.swing.JPanel {
    Conexion conn;
    Connection cn;
    Statement st;
    ResultSet rs;
    
    
    public informesYgraficas() {
        initComponents();
        
        conn = new Conexion();  // Instanciar la conexión
        conn.conectar();
        
        JPanel panelGrafica = new JPanel(); // Asegúrate de que este panel esté en tu interfaz
        new GraficaInventario(panelGrafica); // Crear la gráfica
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(informesYgraficas::new);
    }

    
    
    private void generarInformeInventario() {
        String sql = "SELECT "
                     + "p.nombreProducto, "
                     + "c.nombreCategoria, "
                     + "p.stock, "
                     + "p.precio, "
                     + "(p.precio * p.stock) AS valorTotalStock, "
                     + "5 AS puntoReorden "
                     + "FROM productos p "
                     + "JOIN categorias c ON p.id_categoria = c.id";

        try {
            Connection cn = conn.getConnection();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Crear un modelo para la tabla
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Nombre Producto");
            modelo.addColumn("Categoría");
            modelo.addColumn("Stock");
            modelo.addColumn("Precio");
            modelo.addColumn("Valor Total Stock");
            modelo.addColumn("Punto de Reorden");

            // Llenar el modelo con datos de la consulta
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getString("nombreProducto");
                fila[1] = rs.getString("nombreCategoria");
                fila[2] = rs.getInt("stock");
                fila[3] = rs.getDouble("precio");
                fila[4] = rs.getDouble("valorTotalStock");
                fila[5] = rs.getInt("puntoReorden");

                modelo.addRow(fila);
            }

            // Mostrar los resultados en un JTable
            JTable tablaInforme = new JTable(modelo);
            JScrollPane scrollPane = new JScrollPane(tablaInforme);
            JOptionPane.showMessageDialog(null, scrollPane, "Informe de Inventario", JOptionPane.INFORMATION_MESSAGE);

            // Preguntar si desea exportar a CSV
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea exportar este informe a CSV?", "Exportar a CSV", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                // Crear el contenido para el archivo CSV
                StringBuilder csvContent = new StringBuilder();
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    csvContent.append(modelo.getColumnName(i)).append(",");
                }
                csvContent.deleteCharAt(csvContent.length() - 1); // Eliminar la última coma
                csvContent.append("\n"); // Nueva línea

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        csvContent.append(modelo.getValueAt(i, j)).append(",");
                    }
                    csvContent.deleteCharAt(csvContent.length() - 1); // Eliminar la última coma
                    csvContent.append("\n"); // Nueva línea
                }

                // Guardar el archivo CSV
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar CSV");
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    // Comprobar si el archivo ya existe y preguntar al usuario si quiere sobrescribirlo
                    if (fileToSave.exists()) {
                        int confirm = JOptionPane.showConfirmDialog(null, "El archivo ya existe. ¿Desea sobrescribirlo?", "Confirmar Sobrescritura", JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) {
                            return; // Salir del método si no se desea sobrescribir
                        }
                    }

                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        writer.write(csvContent.toString());
                        JOptionPane.showMessageDialog(null, "Reporte generado y guardado exitosamente.");
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage());
                    }
                }
            }

            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage());
        }
    }
    
    
    private void generarInformeMovimientosStock() {
        // Verificar que el campo de ID no esté vacío
        if (txtid.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de producto válido.");
            return; // Salir del método si está vacío
        }

        int idProducto;
        try {
            // Obtener el ID del producto desde el JTextField
            idProducto = Integer.parseInt(txtid.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID del producto debe ser un número entero.");
            return; // Salir del método si no se puede convertir
        }

        // Obtener las fechas de los JDateChooser
        Date fechaInicio = dateInicio.getDate();
        Date fechaFin = dateFinal.getDate();

        // Verificar que las fechas no sean nulas
        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione ambas fechas.");
            return; // Salir del método si falta alguna fecha
        }

        // Asegurarse de que las fechas son correctas y convertirlas a java.sql.Date
        java.sql.Date sqlFechaInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFechaFin = new java.sql.Date(fechaFin.getTime());

        // Consulta SQL para obtener los movimientos de stock
        String sql = "SELECT h.fecha, h.operacion, h.cantidad, h.usuario AS usuario "
                   + "FROM historial_stock h "
                   + "WHERE h.id_producto = ? AND h.fecha BETWEEN ? AND ?";

        try (Connection cn = conn.getConnection();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, idProducto); // Establecer el ID del producto
            pst.setDate(2, sqlFechaInicio); // Establecer la fecha de inicio
            pst.setDate(3, sqlFechaFin); // Establecer la fecha de fin

            ResultSet rs = pst.executeQuery();

            // Crear un modelo para la tabla
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Fecha");
            modelo.addColumn("Operación");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Usuario");

            // Verificar si hay resultados
            boolean hayResultados = false;

            // Llenar el modelo con datos de la consulta
            while (rs.next()) {
                hayResultados = true; // Se encontró al menos un resultado
                Object[] fila = new Object[4];
                fila[0] = rs.getDate("fecha"); // Asegurarse de que rs.getDate esté retornando un java.sql.Date
                fila[1] = rs.getString("operacion");
                fila[2] = rs.getInt("cantidad");
                fila[3] = rs.getString("usuario");
                modelo.addRow(fila);
            }

            if (!hayResultados) {
                JOptionPane.showMessageDialog(this, "No se encontraron movimientos para el ID de producto proporcionado.");
                return; // Salir del método si no hay resultados
            }

            // Mostrar los resultados en un JTable
            JTable tablaInforme = new JTable(modelo);
            JScrollPane scrollPane = new JScrollPane(tablaInforme);
            JOptionPane.showMessageDialog(null, scrollPane, "Informe de Movimientos de Stock", JOptionPane.INFORMATION_MESSAGE);

            // Preguntar si desea exportar a CSV
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea exportar este informe a CSV?", "Exportar a CSV", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                // Crear el contenido para el archivo CSV
                StringBuilder csvContent = new StringBuilder();
                csvContent.append("Fecha,Operación,Cantidad,Usuario\n"); // Encabezados

                // Agregar los datos al contenido CSV
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    csvContent.append(modelo.getValueAt(i, 0)).append(",")
                              .append(modelo.getValueAt(i, 1)).append(",")
                              .append(modelo.getValueAt(i, 2)).append(",")
                              .append(modelo.getValueAt(i, 3)).append("\n");
                }

                // Guardar el archivo CSV
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar CSV");
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    FileWriter writer = new FileWriter(fileToSave);
                    writer.write(csvContent.toString());
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Reporte generado y guardado exitosamente.");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage());
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
        btnInventario = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtid = new javax.swing.JTextField();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateFinal = new com.toedter.calendar.JDateChooser();
        btnMovimientos = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnInventario.setBackground(new java.awt.Color(13, 110, 253));
        btnInventario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnInventario.setText("INVENTARIO ACTUAL");
        btnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("INFORMES");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informe Producto"));

        btnMovimientos.setBackground(new java.awt.Color(13, 110, 253));
        btnMovimientos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMovimientos.setForeground(new java.awt.Color(255, 255, 255));
        btnMovimientos.setText("GENERAR INFORME PRODUCTOS");
        btnMovimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovimientosActionPerformed(evt);
            }
        });

        jLabel2.setText("ID");

        jLabel3.setText("FECHA INICIO");

        jLabel4.setText("FECHA FINAL");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                            .addComponent(dateFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(84, 84, 84)
                .addComponent(btnMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel3))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(76, 76, 76))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dateFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(241, 241, 241)
                        .addComponent(btnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(289, 289, 289)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(110, 110, 110))
                    .addComponent(btnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 590));
    }// </editor-fold>//GEN-END:initComponents

    private void btnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioActionPerformed
        generarInformeInventario();
    }//GEN-LAST:event_btnInventarioActionPerformed

    private void btnMovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovimientosActionPerformed
        generarInformeMovimientosStock();
    }//GEN-LAST:event_btnMovimientosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInventario;
    private javax.swing.JButton btnMovimientos;
    private com.toedter.calendar.JDateChooser dateFinal;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtid;
    // End of variables declaration//GEN-END:variables
}
