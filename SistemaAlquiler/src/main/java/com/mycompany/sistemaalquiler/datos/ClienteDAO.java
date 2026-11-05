package com.mycompany.sistemaalquiler.datos;

import com.mycompany.sistemaalquiler.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    //convierte una fila de la BD en un objeto Cliente para evitar repetir codigo
    private Cliente mapear(ResultSet rs) throws SQLException{
        Cliente c = new Cliente(
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dui"),
                rs.getInt("edad"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("direccion"));
         c.setId(rs.getInt("id"));
         return c;
    }
    
    //Inserta y recupera el id generado por postgres
    public boolean insertar (Cliente c){
        String sql = "INSERT INTO clientes (codigo, nombre, apellido, dui, edad, correo, telefono, direccion)" +
                "VALUES (?,?,?,?,?,?,?,?) RETURNING id";
        try(Connection con = ConexionDB.obtener();
                PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            ps.setString(4, c.getDui());
            ps.setInt(5, c.getEdad());
            ps.setString(6, c.getCorreo());
            ps.setString(7, c.getTelefono());
            ps.setString(8, c.getDireccion());
            
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    c.setId(rs.getInt(1));
                    return true;
                }
            }
            } catch (SQLException e) {
                IO.print("Error al insertar cliente: " + e.getMessage());
            }
        return false;
        }
    
    //Buscar por DUI
    public Cliente buscarPorDui(String dui){
        String sql = "SELECT * FROM clientes WHERE dui = ?";
        try(Connection con = ConexionDB.obtener();
                PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dui);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    return mapear(rs);
                }
            }
            } catch (SQLException e) {
                IO.print("Error al buscar cliente: " + e.getMessage());
            }
        return null;
        } 
    
    //Listar todos
    public List<Cliente> listar(){
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY id";
        try(Connection con  = ConexionDB.obtener();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
                ){
            while(rs.next()){
                lista.add(mapear(rs));
            }
        }catch(SQLException e){
            IO.print("Error al listar los clientes: " + e.getMessage());
        }
        return lista;
    }
    
    //Actualiza los datos de un cliente existente
    public boolean actualizar(Cliente c){
        String sql = "UPDATE cliente SET nombre = ?, apellido = ?, dui = ?, edad = ?, correo = ? WHERE id = ?";
        try(Connection con = ConexionDB.obtener();
                PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getDui());
            ps.setInt(4, c.getEdad());
            ps.setString(5, c.getCorreo());
            ps.setInt(6, c.getId());
            return ps.executeUpdate() > 0;
    }catch (SQLException e){
        IO.print("Error al actualizar cliente: " + e.getMessage() );
                return false;
        }
    }
    
    //Delete que elimina por id
    public boolean eliminar(int id){
        String sql = "DELETE FROM clientes WHERE id = ?";
        try(Connection con = ConexionDB.obtener();
                PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            IO.print("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
    }
   
