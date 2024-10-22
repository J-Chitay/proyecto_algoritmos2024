import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class pedidos extends javax.swing.JPanel {

    Conexion conn;
    Connection cn;
    Statement st;
    ResultSet rs;
    DefaultTableModel modelo;
    int id;
    
    private JComboBox<Producto> comboProducto;
    private JComboBox<Proveedor> comboProveedor;
    
    
    // Variables globales para almacenar el pedido
    List<Producto> productosSeleccionados = new ArrayList<>();
    // Variables globales para almacenar el pedido
    List<Proveedor> proveedorSeleccionados = new ArrayList<>();
    double totalPedido = 0.0;
    // Declara el JComboBox para objetos Producto
    
    public pedidos() {
        initComponents();
        
        conn = new Conexion();  // Instanciar la conexión
        conn.conectar();
        listar();
        // Crear el JComboBox de productos manualmente
        comboProducto = new JComboBox<>();  // JComboBox para objetos Producto
        // Agregar el combo al panel manualmente
        comboProducto.setBounds(55, 130, 190, 30);  // Ajusta la posición (x, y) y el tamaño (ancho, alto)
        jPanel11.add(comboProducto);  // jPanel1 es tu contenedor principal
        jPanel11.revalidate();
        jPanel11.repaint();
        
        
         // Crear el JComboBox de productos manualmente
        comboProveedor = new JComboBox<>();  // JComboBox para objetos Producto
        // Agregar el combo al panel manualmente
        comboProveedor.setBounds(55, 170, 190, 30);  // Ajusta la posición (x, y) y el tamaño (ancho, alto)
        jPanel11.add(comboProveedor);  // jPanel1 es tu contenedor principal
        jPanel11.revalidate();
        jPanel11.repaint();
        
        llenarComboBoxProductos();
        llenarComboBoxProveedores();
    }

    
    private void llenarComboBoxProductos() {
        try {
            Connection cn = conn.getConnection();  // Conexión a la base de datos
            String sql = "SELECT id, nombreProducto, precio FROM productos";  // Asegúrate de seleccionar más detalles si es necesario
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            comboProducto.removeAllItems(); // Limpiar el combo box por si ya tiene datos

            // Recorrer los resultados y agregar los productos al combo box
            while (rs.next()) {
                // Crear un nuevo objeto Producto con los datos obtenidos
                Producto producto = new Producto(rs.getInt("id"), rs.getString("nombreProducto"), rs.getDouble("precio"));

                comboProducto.addItem(producto);
            }

            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage());
        }
    }
    
    private void llenarComboBoxProveedores() {
            try {
                Connection cn = conn.getConnection();  // Conexión a la base de datos
                String sql = "SELECT id_proveedor, nombre_proveedor FROM proveedores";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                comboProveedor.removeAllItems(); // Limpiar el combo box por si ya tiene datos

                // Recorrer los resultados y agregar los proveedores al combo box
                while (rs.next()) {
                    // Crear un nuevo objeto Proveedor con los datos obtenidos
                    Proveedor proveedor = new Proveedor(rs.getInt("id_proveedor"), rs.getString("nombre_proveedor"));

                    comboProveedor.addItem(proveedor); // Añadir el proveedor al JComboBox
                }

                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar Proveedores: " + e.getMessage());
            }
    }
    
    private Producto obtenerProductoSeleccionado() {
        // Obtener el producto seleccionado desde el JComboBox
        Producto productoSeleccionado = (Producto) comboProducto.getSelectedItem();

        // Verificar si no es null
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un producto.");
            return null;
        }

        // Devolver el producto seleccionado
        return productoSeleccionado;
    }
    
    public int obtenerIdProductoPorNombre(String nombreProducto) {
        int idProducto = -1; // Inicializar con un valor inválido por defecto

        try {
            Connection cn = conn.getConnection(); // Asegúrate de tener tu conexión a la base de datos
            String sql = "SELECT id FROM productos WHERE nombreProducto = ?"; // Consulta para obtener el ID del producto
            PreparedStatement pstmt = cn.prepareStatement(sql);
            pstmt.setString(1, nombreProducto); // Establecer el nombre del producto en la consulta

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idProducto = rs.getInt("id"); // Obtener el ID del producto
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado: " + nombreProducto);
            }

            cn.close(); // Cerrar la conexión
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del producto: " + e.getMessage());
        }

        return idProducto;
    }
    
    
    private void agregarProducto() {
        try {
            // Obtener el producto seleccionado desde el JComboBox
            Producto productoSeleccionado = (Producto) comboProducto.getSelectedItem();

            // Validar que se haya seleccionado un producto
            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto.");
                return;
            }

            // Obtener el texto de la cantidad ingresada
            String cantidadText = txtCantidad.getText().trim();

            // Verificar que el campo de cantidad no esté vacío
            if (cantidadText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad.");
                return;
            }

            // Intentar convertir la cantidad a un número entero
            int cantidad = Integer.parseInt(cantidadText);

            // Verificar que la cantidad sea mayor a cero
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }

            // Calcular el subtotal del producto
            double subtotal = productoSeleccionado.getPrecio() * cantidad;

            // Agregar el producto a la lista temporal
            productoSeleccionado.setCantidad(cantidad);  // Establecer la cantidad
            productosSeleccionados.add(productoSeleccionado);

            // Actualizar el total del pedido
            totalPedido += subtotal;

            // Mostrar el producto en el JTextArea
            areaPedido.append(productoSeleccionado.getNombreProducto() + " - Cantidad: " + cantidad + " - Subtotal: " + subtotal + "\n");

            // Actualizar el total en el JLabel
            lblTotal.setText("Total del pedido: Q" + totalPedido);

            // Limpiar el campo de cantidad para la siguiente entrada
            txtCantidad.setText("");
            
            JOptionPane.showMessageDialog(this, "Producto agregado");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad numérica válida.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + e.getMessage());
        }
    }
    
    private double calcularSubtotal(int idProducto, int cantidad) {
        double precioProducto = 0.0;

        try {
            Connection cn = conn.getConnection();
            String sql = "SELECT precio FROM productos WHERE id = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idProducto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                precioProducto = rs.getDouble("precio");
            }

            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el precio del producto: " + e.getMessage());
        }

        // Multiplicar el precio por la cantidad para obtener el subtotal
        return precioProducto * cantidad;
    }
    
    
    private void confirmarPedido() {
        // Verificar que el área de texto no esté vacío
        if (areaPedido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en el pedido para confirmar.");
            return;
        }

        Connection cn = null;
        try {
            // Obtener la conexión a la base de datos
            cn = conn.getConnection();
            cn.setAutoCommit(false); // Iniciar transacción

            // Insertar el pedido en la tabla pedidos
            String sqlPedido = "INSERT INTO pedidos (id_proveedor, fecha_creacion, estado, total) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtPedido = cn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);

            // Obtener el proveedor seleccionado
            Proveedor proveedorSeleccionado = (Proveedor) comboProveedor.getSelectedItem();
            if (proveedorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor.");
                return;
            }
            int idProveedor = proveedorSeleccionado.getId();

            pstmtPedido.setInt(1, idProveedor);  // Asignar el ID del proveedor
            pstmtPedido.setDate(2, new java.sql.Date(System.currentTimeMillis()));  // Fecha de creación del pedido
            pstmtPedido.setString(3, "pendiente");  // Estado inicial del pedido
            pstmtPedido.setDouble(4, totalPedido);  // Total del pedido

            pstmtPedido.executeUpdate();

            // Obtener el id del pedido generado
            ResultSet rs = pstmtPedido.getGeneratedKeys();
            int idPedido = 0;
            if (rs.next()) {
                idPedido = rs.getInt(1);  // Obtener el ID del pedido recién generado
            }

            // Insertar los detalles del pedido en la tabla detalle_pedido
            String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtDetalle = cn.prepareStatement(sqlDetalle);

            // Procesar cada línea del área de texto
            String[] lineas = areaPedido.getText().split("\n");
            for (String linea : lineas) {
                // Formato: "Producto - Cantidad: X - Subtotal: Y"
                String[] partes = linea.split(" - "); // Separar por " - "
                if (partes.length != 3) continue;  // Verificar que la línea tiene 3 partes

                String nombreProducto = partes[0].trim();  // Producto
                String cantidadStr = partes[1].replace("Cantidad: ", "").trim();  // Cantidad
                String subtotalStr = partes[2].replace("Subtotal: ", "").trim();  // Subtotal

                int cantidad = Integer.parseInt(cantidadStr);
                double subtotal = Double.parseDouble(subtotalStr);

                // Obtener el ID del producto a partir del nombre (asumiendo que tienes un método para eso)
                int idProducto = obtenerIdProductoPorNombre(nombreProducto);

                pstmtDetalle.setInt(1, idPedido);  // ID del pedido
                pstmtDetalle.setInt(2, idProducto);  // ID del producto
                pstmtDetalle.setInt(3, cantidad);  // Cantidad del producto
                pstmtDetalle.setDouble(4, subtotal);  // Subtotal

                pstmtDetalle.addBatch();  // Añadir al batch
            }

            pstmtDetalle.executeBatch();  // Ejecutar todas las inserciones de detalles del pedido

            // Confirmar la transacción
            cn.commit();

            // Limpiar los campos después de confirmar el pedido
            areaPedido.setText("");  // Limpiar el área de texto
            lblTotal.setText("Total del pedido: 0.00");  // Reiniciar el total

            JOptionPane.showMessageDialog(this, "Pedido confirmado exitosamente!");

        } catch (SQLException e) {
            try {
                if (cn != null) {
                    cn.rollback();  // Revertir los cambios en caso de error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error al confirmar el pedido: " + e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();  // Cerrar la conexión
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    void listar(){
        // Consulta SQL
        String sql ="SELECT * FROM pedidos";
        
        try{
            cn = conn.getConnection();
            // Crear el statement y ejecutar la consulta
            st = cn.createStatement();
            rs=st.executeQuery(sql);
            // Arreglo para almacenar los datos de cada fila
            Object[]pedidos=new Object[5];
            // Obtener el modelo de la tabla
            modelo=(DefaultTableModel)tabledatos.getModel();
            // Limpiar las filas existentes en el modelo de la tabla
            modelo.setRowCount(0);
            // Iterar sobre los resultados de la consulta
            while (rs.next()){
                pedidos[0]=rs.getInt("id_pedido");
                pedidos[1]=rs.getInt("id_proveedor");
                pedidos[2]=rs.getDate("fecha_creacion");
                pedidos[3]=rs.getString("estado");
                pedidos[4]=rs.getDouble("total");
                // Añadir fila al modelo
                modelo.addRow(pedidos);
            }
            // Actualizar el modelo de la tabla
            tabledatos.setModel(modelo);
        } catch(SQLException e){
            // Manejar errores e imprimirlos en la consola
            System.out.println("Error al listar los pedidos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    private void actualizarEstadoPedido() {
        // Verificar si los campos están vacíos
        if (txtid.getText().isEmpty() || comboEstado.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de pedido y seleccione un estado.");
            return;
        }

        try {
            // Obtener el ID del pedido y el estado seleccionado
            int idPedido = Integer.parseInt(txtid.getText());
            String nuevoEstado = comboEstado.getSelectedItem().toString();

            // Conectar a la base de datos
            Connection cn = conn.getConnection();
            String sql = "UPDATE pedidos SET estado = ? WHERE id_pedido = ?";

            PreparedStatement pstmt = cn.prepareStatement(sql);
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idPedido);

            // Ejecutar la actualización
            int filasActualizadas = pstmt.executeUpdate();

            // Verificar si se actualizó algún registro
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this, "Estado del pedido actualizado correctamente.");
                listar(); // Refrescar la tabla
            } else {
                JOptionPane.showMessageDialog(this, "El ID de pedido no existe.");
            }

            // Cerrar conexión
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID de pedido debe ser un número válido.");
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

        jPanel2 = new javax.swing.JPanel();
        txtid1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtpassword = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtuser = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabledatos = new javax.swing.JTable();
        txtid = new javax.swing.JTextField();
        txtestado = new javax.swing.JTextField();
        comboEstado = new javax.swing.JComboBox<>();
        btnActualizar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaPedido = new javax.swing.JTextArea();
        txtCantidad = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnAgregarProducto = new javax.swing.JButton();
        btnConfirmarPedido = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        txtid1.setEditable(false);
        txtid1.setEnabled(false);

        jLabel2.setText("USUARIO");

        jLabel3.setText("Id");

        jLabel4.setText("PASSWORD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtid1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtuser, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtid1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtuser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("PEDIDOS PROVEEDORES");

        lblTotal.setText("jLabel2");

        tabledatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID PEDIDO", "ID PROVEEDOR", "FECHA CREACION", "ESTADO", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true
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
        jScrollPane2.setViewportView(tabledatos);

        comboEstado.setBackground(new java.awt.Color(13, 110, 253));
        comboEstado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboEstado.setForeground(new java.awt.Color(255, 255, 255));
        comboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pendiente", "en curso", "completado" }));

        btnActualizar.setBackground(new java.awt.Color(13, 110, 253));
        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        jLabel13.setText("ID");

        jLabel14.setText("ESTADO");

        areaPedido.setColumns(20);
        areaPedido.setRows(5);
        jScrollPane1.setViewportView(areaPedido);

        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });

        jLabel11.setText("CANTIDAD");

        jLabel12.setText("AREA DEL PEDIDO");

        btnAgregarProducto.setBackground(new java.awt.Color(13, 110, 253));
        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setText("AGREGAR PRODUCTO");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnConfirmarPedido.setBackground(new java.awt.Color(13, 110, 253));
        btnConfirmarPedido.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnConfirmarPedido.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmarPedido.setText("CONFIRMAR PEDIDO");
        btnConfirmarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarPedidoActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(13, 110, 253));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("CANCELAR PEDIDO");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTotal)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtid)
                            .addComponent(txtestado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 14, Short.MAX_VALUE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnConfirmarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(70, 70, 70))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirmarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtestado, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(lblTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(97, 97, 97))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)))))
                .addGap(49, 49, 49))
        );

        add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 590));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        agregarProducto();
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnConfirmarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarPedidoActionPerformed
        confirmarPedido();
    }//GEN-LAST:event_btnConfirmarPedidoActionPerformed

    private void tabledatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledatosMouseClicked
        int fila=tabledatos.getSelectedRow();
        if(fila==-1){
            JOptionPane.showMessageDialog(null, "pedido no seleccionado");
        }else{
            id=Integer.parseInt((String)tabledatos.getValueAt(fila, 0).toString());
            String estado=(String)tabledatos.getValueAt(fila, 3);

            txtid.setText(""+id);
            txtestado.setText(estado);

        }
    }//GEN-LAST:event_tabledatosMouseClicked

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizarEstadoPedido();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        char c = evt.getKeyChar();
        // Si no es un número (dígito) o la tecla de retroceso (para borrar), lo ignoramos
        if (!Character.isDigit(c)) {
            evt.consume(); // Evitar que el carácter se ingrese
        }
    }//GEN-LAST:event_txtCantidadKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaPedido;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnConfirmarPedido;
    private javax.swing.JComboBox<String> comboEstado;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tabledatos;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtestado;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtid1;
    private javax.swing.JTextField txtpassword;
    private javax.swing.JTextField txtuser;
    // End of variables declaration//GEN-END:variables
}
