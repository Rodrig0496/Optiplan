/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;
import Entidades.Usuario;
import Util.ConexionBD;
import java.sql.*;
import java.util.Optional;
/**
 *
 * @author Mi Equipo
 */
public class UsuarioNegocio {
    // Registrar un usuario
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (Nombre, Correo, Contrasena) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // Login por correo y contraseña
    public Usuario login(String correo, String contrasena) {
        String sql = "SELECT * FROM Usuarios WHERE Correo = ? AND Contrasena = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUsuarioID(rs.getInt("UsuarioID"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setContrasena(rs.getString("Contrasena"));
                usuario.setFechaRegistro(new java.util.Date(rs.getTimestamp("FechaRegistro").getTime()));
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en login: " + e.getMessage());
        }
        return null;
    }

    // Verificar si un correo ya existe
    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM Usuarios WHERE Correo = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("❌ Error al verificar correo: " + e.getMessage());
            return false;
        }
    }
}
