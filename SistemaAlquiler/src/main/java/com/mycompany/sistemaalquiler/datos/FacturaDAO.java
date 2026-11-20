package com.mycompany.sistemaalquiler.datos;

import com.mycompany.sistemaalquiler.modelo.ContratoAlquiler;
import com.mycompany.sistemaalquiler.modelo.VentaRegistro;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    // Guarda la factura. El usuario y el metodo de pago se buscan por nombre
    // dentro del mismo INSERT; si no existen en la BD, quedan en NULL.
    public boolean insertar(ContratoAlquiler c, String nombreUsuario) {
        String sql = """
                INSERT INTO facturas (
                    numero_factura, cliente_id, usuario_id, inmueble_id,
                    fecha_emision, fecha_inicio_contrato, fecha_fin_contrato,
                    dias_contratados, meses_contratados, anios_contratados,
                    metodo_pago_id, subtotal, descuento_id, monto_descuento,
                    monto_iva, monto_cargos_adicionales, total
                ) VALUES (
                    ?, ?, (SELECT id FROM usuarios WHERE nombre_usuario = ?), ?,
                    ?, ?, ?,
                    ?, ?, ?,
                    (SELECT id FROM metodos_pago WHERE LOWER(nombre) = LOWER(?)), ?, NULL, ?,
                    ?, ?, ?
                )
                """;

        try (Connection con = ConexionDB.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNumeroFactura());
            ps.setInt(2, c.getCliente().getId());
            ps.setString(3, nombreUsuario);
            ps.setInt(4, c.getInmueble().getId());
            ps.setTimestamp(5, Timestamp.valueOf(c.getFechaEmision()));
            ps.setDate(6, Date.valueOf(c.getFechaInicio()));
            ps.setDate(7, Date.valueOf(c.getFechaFin()));
            ps.setLong(8, c.getDiasContratados());
            ps.setInt(9, c.getMesesContratados());
            ps.setInt(10, c.getAniosContratados());
            ps.setString(11, c.getMetodoPago());
            ps.setDouble(12, c.getSubtotal());
            ps.setDouble(13, c.getMontoDescuento());
            ps.setDouble(14, c.getMontoIva());
            ps.setDouble(15, c.getCargosAdicionales());
            ps.setDouble(16, c.getTotalPagar());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            IO.print("Error al guardar factura en BD: " + e.getMessage() + "\n");
            return false;
        }
    }

    // Ultimo numero de factura guardado (FAC-1005 -> 1005). Si no hay, 1000.
    public int ultimaSecuencia() {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(numero_factura FROM 5) AS INTEGER)), 1000) "
                + "FROM facturas WHERE numero_factura ~ '^FAC-[0-9]+$'";
        try (Connection con = ConexionDB.obtener();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            IO.print("No se pudo leer la ultima factura: " + e.getMessage() + "\n");
        }
        return 1000;
    }

    // Todas las ventas guardadas, para que los reportes incluyan el historial completo
    public List<VentaRegistro> listarRegistros() {
        List<VentaRegistro> lista = new ArrayList<>();
        String sql = """
                SELECT f.numero_factura, f.fecha_emision,
                       c.nombre || ' ' || c.apellido AS cliente,
                       i.codigo AS inmueble,
                       COALESCE(m.nombre, 'N/D') AS metodo,
                       f.subtotal + f.monto_descuento AS base,
                       f.monto_descuento, f.monto_cargos_adicionales,
                       f.monto_iva, f.total
                FROM facturas f
                JOIN clientes c ON c.id = f.cliente_id
                JOIN inmuebles i ON i.id = f.inmueble_id
                LEFT JOIN metodos_pago m ON m.id = f.metodo_pago_id
                ORDER BY f.id
                """;
        try (Connection con = ConexionDB.obtener();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new VentaRegistro(
                        rs.getString("numero_factura"),
                        rs.getTimestamp("fecha_emision").toLocalDateTime().toLocalDate(),
                        rs.getString("cliente"),
                        rs.getString("inmueble"),
                        rs.getString("metodo"),
                        rs.getDouble("base"),
                        rs.getDouble("monto_descuento"),
                        rs.getDouble("monto_cargos_adicionales"),
                        rs.getDouble("monto_iva"),
                        rs.getDouble("total")));
            }
        } catch (SQLException e) {
            IO.print("Error al cargar el historial de ventas: " + e.getMessage() + "\n");
        }
        return lista;
    }

    // Se usa al anular una venta
    public boolean eliminarPorNumero(String numeroFactura) {
        String sql = "DELETE FROM facturas WHERE numero_factura = ?";
        try (Connection con = ConexionDB.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroFactura);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            IO.print("Error al eliminar factura: " + e.getMessage() + "\n");
            return false;
        }
    }
}
