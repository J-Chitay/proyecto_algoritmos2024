/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author david
 */
public class Proveedor {
    private int id; // ID del proveedor
    private String nombre; // Nombre del proveedor

    // Constructor
    public Proveedor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Método toString para mostrar el nombre en el JComboBox
    @Override
    public String toString() {
        return nombre; // Esto mostrará el nombre del proveedor en el JComboBox
    }

}
