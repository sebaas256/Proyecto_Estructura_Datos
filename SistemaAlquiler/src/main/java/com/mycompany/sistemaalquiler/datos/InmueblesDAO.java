
package com.mycompany.sistemaalquiler.datos;

import com.mycompany.sistemaalquiler.modelo.Inmueble;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class InmueblesDAO {
    private Inmueble mapear(ResultSet rs) throws SQLException{
        Inmueble i = new Inmueble(
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getInt("tipo_inmueble_id"),
                rs.getDouble("precio_mensual"),
                rs.getDouble("precio_diario"),
                rs.getInt("capacidad"),
                rs.getInt("habitaciones"),
                rs.getInt("banos"),
                rs.getDouble("area_m2"),
                rs.getString("caracteristicas"),
                rs.getString("direccion"),
                rs.getString("estado"),
                rs.getObject("fecha_disponible", LocalDate.class));
        i.setId(rs.getInt("id"));
        return i;
    }
    
    public boolean insertar(Inmueble i){
        String sql = "INSERT INTO inmuebles (codigo, nombre, tipo_inmueble_id, precio_mensual, precio_diario, capacidad, habitaciones, banos, area_m2, caracteristicas, direccion, estado, fecha_disponible)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id";
        try (Connection con = ConexionDB.obtener();
                PreparedStatement ps = con.prepareStatement(sql)){
           
            ps.setString(1, i.getCodigo());
            ps.setString(2, i.getNombre());
            ps.setInt(3, i.getTipo_inmueble_id());
            ps.setDouble(4, i.getPrecio_mensual());
            ps.setDouble(5, i.getPrecio_diario());
            ps.setInt(6, i.getCapacidad());
            ps.setInt(7, i.getHabitaciones());
            ps.setInt(8, i.getBanos());
            ps.setDouble(9, i.getArea_m2());
            ps.setString(10, i.getCaracteristicas());
            ps.setString(11, i.getDireccion());
            ps.setString(12, i.getEstado());
            if (i.getFechaDisponible() != null) {
                ps.setObject(13, i.getFechaDisponible());
            } else {
                ps.setNull(13, java.sql.Types.DATE);
            }
            
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    i.setId(rs.getInt(1));
                    return true;
                }
            }
        }catch(SQLException e){
            IO.print("error al insertar inmueble: " + e.getMessage());
        }
        return false;
    }
    
    public Inmueble buscarPorCodigo(String codigo){
        String sql = "SELECT * FROM inmuebles WHERE codigo = ?";
        try(Connection con = ConexionDB.obtener();
                PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, codigo);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return mapear(rs);
                }
            }
        }catch (SQLException e){
            IO.print("Error al buscar por codigo: "+ e.getMessage());
        }
        return null;
    }
    
    public List<Inmueble> listar(){
        List<Inmueble> lista = new ArrayList<>();
        String sql = "SELECT * FROM inmuebles order by id";
        try (Connection con = ConexionDB.obtener();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)){
            while(rs.next()){
                lista.add(mapear(rs));
            }
        }catch (SQLException e){
            IO.print("Error al listar los inmuebles: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean eliminar(int id){
        String sql = "DELETE FROM inmuebles WHERE id = ?";
        try(Connection con = ConexionDB.obtener();
                PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            IO.print("Error al eliminar inmueble: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizarEstado(int id, String estado, LocalDate fechaDisponible) {
    String sql = "UPDATE inmuebles SET estado = ?, fecha_disponible = ? WHERE id = ?";
    try (Connection con = ConexionDB.obtener();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, estado);
        if (fechaDisponible != null) {
            ps.setObject(2, fechaDisponible);
        } else {
            ps.setNull(2, java.sql.Types.DATE);
        }
        ps.setInt(3, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        IO.print("Error al actualizar estado: " + e.getMessage() + "\n");
        return false;
    }
}
}
