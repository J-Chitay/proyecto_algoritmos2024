
//librerias
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Conexion {
    //cramos las variables para conexion
    String db = "dbproyecto_algoritmos";
    String url = "jdbc:mysql://localhost:3306/" +db;
    String user = "root";
    String password = "";
    String driver = "com.mysql.cj.jdbc.Driver";
    Connection cx;
    
    

    public Conexion() {
    }

    public Conexion(String dbproyecto_algoritmos) {
        this.db = db;
    }

    public Connection conectar(){
        try {
            //cargamos el driver
            Class.forName(driver);
            //establecemos las conexion de la base de datosm, usando nuestra variables
            cx=DriverManager.getConnection(url, user, password);
            //mensaje de conectado
            System.out.println("se conecto a base de datos" + db);
            } catch (ClassNotFoundException | SQLException ex) {
                //mostramos mensaje de error
                System.out.println("No se conecto a base de datos" + db);
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx;
    }
    
    public void desconectar(){
        try {
            cx.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        Conexion conn = new Conexion();
        conn.conectar();
    }

    Connection getConnection() {
        try {
            // Cargar el controlador JDBC
            Class.forName(driver);
            // Establecer la conexión con la base de datos
            cx = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos: " + db);
        } catch (ClassNotFoundException | SQLException ex) {
            // Manejo de errores si ocurre un problema al conectar
            System.out.println("Error al conectar a la base de datos: " + ex.getMessage());
            ex.printStackTrace(); // Imprime el error en la consola
        }
        return cx;
    }

   
}
