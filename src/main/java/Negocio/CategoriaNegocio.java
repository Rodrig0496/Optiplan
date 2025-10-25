/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;
import Util.ConexionBD;
import Entidades.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Mi Equipo
 */
public class CategoriaNegocio {
    public List<Categoria> obtenerCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT CategoriaID, Nombre FROM Categorias";

        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            Categoria cat = new Categoria();
            cat.setCategoriaID(rs.getInt("CategoriaID"));
            cat.setNombre(rs.getString("Nombre"));
            lista.add(cat);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return lista;
    }
}
