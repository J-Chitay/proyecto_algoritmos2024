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
        txtid = new javax.swing.JTextField();
        btnMovimientos = new javax.swing.JButton();
        dateInicio = new com.toedter.calendar.JDateChooser();
        dateFinal = new com.toedter.calendar.JDateChooser();
        panelGrafica = new javax.swing.JPanel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        btnInventario.setText("Inventario Actual");
        btnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioActionPerformed(evt);
            }
        });

        btnMovimientos.setText("Movimientos");
        btnMovimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovimientosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGraficaLayout = new javax.swing.GroupLayout(panelGrafica);
        panelGrafica.setLayout(panelGraficaLayout);
        panelGraficaLayout.setHorizontalGroup(
            panelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );
        panelGraficaLayout.setVerticalGroup(
            panelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 287, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(dateFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(40, 40, 40)
                        .addComponent(btnMovimientos))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(panelGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMovimientos))
                .addGap(79, 79, 79)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(dateFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(55, 55, 55)
                .addComponent(panelGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 570));
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelGrafica;
    private javax.swing.JTextField txtid;
    // End of variables declaration//GEN-END:variables
}
