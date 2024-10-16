
public class Producto {
    private int id;
    private String nombreProducto;
    private double precio;
    private int cantidad;

    // Constructor
    public Producto(int idProducto, String nombreProducto, double precio) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
    }

    // Getters y setters
    public int getIdProducto() { 
        return id; 
    }

    public String getNombreProducto() { 
        return nombreProducto; 
    }

    public double getPrecio() { 
        return precio; 
    }

    public int getCantidad() { 
        return cantidad; 
    }

    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
    }
    
    @Override
    public String toString() {
        return nombreProducto;  // Mostrará el nombre del producto en el comboBox
    }
}
