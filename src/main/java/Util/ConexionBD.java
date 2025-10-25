/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mi Equipo
 */
public class ConexionBD {
    private static ConexionBD instancia;
    private Connection conexion;
    private String url = "jdbc:mysql://localhost:3306/optiPlan_bd";
    private String usuario = "root";
    private String contrasena = "";

    // Constructor privado
    private ConexionBD() {
        conectar();
    }

    // Método Singleton
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    // Crear la conexión una sola vez
    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("Conexión a BD establecida");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar con la BD: " + e.getMessage());
        }
    }

    // Obtener la misma conexión siempre
    public Connection getConexion() {
        try {
            // Verificar si la conexión sigue activa
            if (conexion == null || conexion.isClosed()) {
                System.out.println("Reconectando a la BD...");
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("Error verificando conexión: " + e.getMessage());
            conectar(); // Intentar reconectar
        }
        return conexion;
    }

    // Método para cerrar la conexión
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión a BD cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
