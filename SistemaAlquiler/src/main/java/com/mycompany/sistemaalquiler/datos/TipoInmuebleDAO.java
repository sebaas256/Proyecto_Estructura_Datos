
package com.mycompany.sistemaalquiler.datos;

import com.mycompany.sistemaalquiler.modelo.TipoInmueble;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoInmuebleDAO {

    private TipoInmueble mapear(ResultSet rs) throws SQLException {
        return new TipoInmueble(rs.getInt("id"), rs.getString("nombre"));
    }

    // Lista todos los tipos para mostrarlos en el menú
    public List<TipoInmueble> listar() {
        List<TipoInmueble> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM tipos_inmueble ORDER BY id";
        try (Connection con = ConexionDB.obtener();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            IO.print("Error al listar tipos de inmueble: " + e.getMessage() + "\n");
        }
        return lista;
    }

    // Busca un tipo por id (sirve para validar lo que eligió el usuario)
    public TipoInmueble buscarPorId(int id) {
        String sql = "SELECT id, nombre FROM tipos_inmueble WHERE id = ?";
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            IO.print("Error al buscar tipo de inmueble: " + e.getMessage() + "\n");
        }
        return null;
    }
}
