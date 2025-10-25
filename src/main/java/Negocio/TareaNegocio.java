/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;
import Entidades.Tarea;
import Util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Mi Equipo
 */
public class TareaNegocio {
    public List<Tarea> obtenerTareasPorEstado(int usuarioID, String estado) {
        String sql = "SELECT * FROM Tareas WHERE UsuarioID = ? AND Estado = ?";
        List<Tarea> tareas = new ArrayList<>();

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioID);
            ps.setString(2, estado);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tarea tarea = new Tarea();
                tarea.setTareaID(rs.getInt("TareaID"));
                tarea.setUsuarioID(rs.getInt("UsuarioID"));
                tarea.setTitulo(rs.getString("Titulo"));
                tarea.setDescripcion(rs.getString("Descripcion"));
                tarea.setCategoriaID(rs.getInt("CategoriaID"));
                tarea.setPrioridad(rs.getString("Prioridad"));
                tarea.setEstado(rs.getString("Estado"));
                tarea.setFecha(rs.getDate("Fecha"));
                tarea.setHoraInicio(rs.getTime("HoraInicio"));
                tarea.setHoraFin(rs.getTime("HoraFin"));
                tarea.setFechaCreacion(rs.getDate("FechaCreacion"));

                
                tarea.setRecordatorioActivo(rs.getBoolean("RecordatorioActivo"));
                tarea.setHoraRecordatorio(rs.getTime("HoraRecordatorio"));

                tareas.add(tarea);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
        }
        return tareas;
    }

    // 🔹 Nuevo método para obtener el nombre de la categoría
    public String obtenerNombreCategoria(int categoriaID) {
        String nombre = "Sin categoría";
        String sql = "SELECT Nombre FROM Categorias WHERE CategoriaID = ?";

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoriaID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("Nombre");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre de categoría: " + e.getMessage());
        }
        return nombre;
    }
    public boolean insertarTarea(Tarea tarea) {
        String sql = "INSERT INTO Tareas (UsuarioID, Titulo, Descripcion, CategoriaID, Prioridad, Estado, Fecha, HoraInicio, HoraFin, FechaCreacion) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tarea.getUsuarioID());
            ps.setString(2, tarea.getTitulo());
            ps.setString(3, tarea.getDescripcion());
            ps.setInt(4, tarea.getCategoriaID());
            ps.setString(5, tarea.getPrioridad());
            ps.setString(6, tarea.getEstado());
            ps.setDate(7, new java.sql.Date(tarea.getFecha().getTime()));
            ps.setTime(8, tarea.getHoraInicio());
            ps.setTime(9, tarea.getHoraFin());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar tarea: " + e.getMessage());
            return false;
        }
    }
    public boolean eliminarTarea(int tareaID) {
        String sql = "DELETE FROM Tareas WHERE TareaID = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tareaID);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            return false;
        }
    }
    public boolean cambiarEstadoTarea(int tareaID, String nuevoEstado) {
        String sql = "UPDATE Tareas SET Estado = ? WHERE TareaID = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, tareaID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }
    
    public List<Tarea> obtenerTareasPorRangoFechas(int usuarioID, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        String sql = "SELECT * FROM Tareas WHERE UsuarioID = ? AND Fecha BETWEEN ? AND ? AND Estado = 'Pendiente'";
        List<Tarea> tareas = new java.util.ArrayList<>();

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioID);
            // Convertir java.util.Date a java.sql.Date
            ps.setDate(2, new java.sql.Date(fechaInicio.getTime()));
            ps.setDate(3, new java.sql.Date(fechaFin.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tarea tarea = new Tarea();
                tarea.setTareaID(rs.getInt("TareaID"));
                tarea.setUsuarioID(rs.getInt("UsuarioID"));
                tarea.setTitulo(rs.getString("Titulo"));
                tarea.setDescripcion(rs.getString("Descripcion"));
                tarea.setCategoriaID(rs.getInt("CategoriaID"));
                tarea.setPrioridad(rs.getString("Prioridad"));
                tarea.setEstado(rs.getString("Estado"));
                tarea.setFecha(rs.getDate("Fecha"));
                tarea.setHoraInicio(rs.getTime("HoraInicio"));
                tarea.setHoraFin(rs.getTime("HoraFin"));
                tarea.setFechaCreacion(rs.getDate("FechaCreacion"));
                tareas.add(tarea);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tareas por rango: " + e.getMessage());
        }
        return tareas;
    }
    public boolean actualizarTituloTarea(int tareaID, String nuevoTitulo) {
        String sql = "UPDATE Tareas SET Titulo = ? WHERE TareaID = ?";

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoTitulo);
            ps.setInt(2, tareaID);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar título: " + e.getMessage());
            return false;
        }
    }
    
    public boolean activarRecordatorio(int tareaID, Time horaRecordatorio) {
        String sql = "UPDATE Tareas SET RecordatorioActivo = TRUE, HoraRecordatorio = ? WHERE TareaID = ?";

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            

            ps.setTime(1, horaRecordatorio);
            ps.setInt(2, tareaID);

            int filas = ps.executeUpdate();
            boolean exito = filas > 0;

            
            return exito;

        } catch (SQLException e) {
            System.err.println("Error al activar recordatorio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean desactivarRecordatorio(int tareaID) {
        String sql = "UPDATE Tareas SET RecordatorioActivo = FALSE, HoraRecordatorio = NULL WHERE TareaID = ?";

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tareaID);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al desactivar recordatorio: " + e.getMessage());
            return false;
        }
    }

    public List<Tarea> obtenerTareasConRecordatoriosActivos() {
        String sql = "SELECT * FROM Tareas WHERE RecordatorioActivo = TRUE AND Estado = 'Pendiente'";
        List<Tarea> tareas = new ArrayList<>();

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tarea tarea = new Tarea();
                tarea.setTareaID(rs.getInt("TareaID"));
                tarea.setUsuarioID(rs.getInt("UsuarioID"));
                tarea.setTitulo(rs.getString("Titulo"));
                tarea.setDescripcion(rs.getString("Descripcion"));
                tarea.setCategoriaID(rs.getInt("CategoriaID"));
                tarea.setPrioridad(rs.getString("Prioridad"));
                tarea.setEstado(rs.getString("Estado"));
                tarea.setFecha(rs.getDate("Fecha"));
                tarea.setHoraInicio(rs.getTime("HoraInicio"));
                tarea.setHoraFin(rs.getTime("HoraFin"));
                tarea.setFechaCreacion(rs.getDate("FechaCreacion"));
                tarea.setRecordatorioActivo(rs.getBoolean("RecordatorioActivo"));
                tarea.setHoraRecordatorio(rs.getTime("HoraRecordatorio"));
                tareas.add(tarea);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener recordatorios: " + e.getMessage());
        }
        return tareas;
    }
}
