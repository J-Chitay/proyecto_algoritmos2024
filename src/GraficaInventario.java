import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GraficaInventario {
    Conexion conn;
    Connection cn;
    Statement st;
    ResultSet rs;
    
    private JPanel panelGrafica; // Este es el panel donde se mostrará la gráfica

    /*// Constructor que toma un JPanel y llama al método para mostrar la gráfica
    public GraficaInventario(JPanel panel) {
        this.panelGrafica = panel; // Asigna el panel recibido
        this.conn = new Conexion(); // Asegúrate de inicializar la conexión
        mostrarGraficaInventario(); // Llama al método para mostrar la gráfica
    }

    // Método para crear la gráfica y mostrarla en el panel
    private void mostrarGraficaInventario() {
        // Crear el dataset que contendrá los datos
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Conectar a la base de datos y obtener los datos de la tabla productos
        try {
            Connection cn = conn.getConnection(); // Método para obtener la conexión
            String sql = "SELECT nombreProducto, stock FROM productos"; // Consulta para obtener datos
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Llenar el dataset con los datos
            while (rs.next()) {
                String nombreProducto = rs.getString("nombreProducto");
                int stock = rs.getInt("stock");
                    System.out.println("Producto: " + nombreProducto + ", Stock: " + stock); // Verificar los datos
                dataset.addValue(stock, "Stock", nombreProducto); // Agregar al dataset
            }

            // Crear la gráfica
            JFreeChart grafica = ChartFactory.createBarChart(
                    "Inventario de Productos", // Título
                    "Productos",               // Eje X
                    "Stock",                   // Eje Y
                    dataset                    // Dataset
            );

            // Crear un panel para la gráfica
            ChartPanel chartPanel = new ChartPanel(grafica);
            chartPanel.setPreferredSize(new Dimension(600, 200)); // Establecer tamaño
            panelGrafica.setVisible(true);

            // Limpiar el panel y agregar la gráfica
            panelGrafica.removeAll();
            panelGrafica.add(chartPanel, BorderLayout.CENTER); // Agregar al panel
            panelGrafica.validate();
            panelGrafica.repaint();

            // Cerrar la conexión
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelGrafica, "Error al obtener datos de la base de datos: " + e.getMessage());
        }
    }*/
    

    // Constructor que toma un JPanel y llama al método para mostrar la gráfica
    public GraficaInventario(JPanel panel) {
        this.panelGrafica = panel; // Asigna el panel recibido
        mostrarGraficaInventario(); // Llama al método para mostrar la gráfica
    }

    // Método para crear la gráfica y mostrarla en el panel
    private void mostrarGraficaInventario() {
        // Crear el dataset con datos ficticios
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Simulación de datos de inventario
        dataset.addValue(10, "Stock", "Producto A");
        dataset.addValue(20, "Stock", "Producto B");
        dataset.addValue(15, "Stock", "Producto C");
        dataset.addValue(30, "Stock", "Producto D");

        // Crear la gráfica
        JFreeChart grafica = ChartFactory.createBarChart(
                "Inventario de Productos", // Título
                "Productos",               // Eje X
                "Stock",                   // Eje Y
                dataset                    // Dataset
        );

        // Crear un panel para la gráfica
        ChartPanel chartPanel = new ChartPanel(grafica);
        chartPanel.setPreferredSize(new Dimension(800, 400)); // Establecer tamaño

        // Limpiar el panel y agregar la gráfica
        panelGrafica.removeAll();
        panelGrafica.add(chartPanel, BorderLayout.CENTER); // Agregar al panel
        panelGrafica.revalidate(); // Validar el panel
        panelGrafica.repaint(); // Repintar el panel
    }
}
