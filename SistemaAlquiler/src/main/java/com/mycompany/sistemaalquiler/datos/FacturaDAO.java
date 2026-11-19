package com.mycompany.sistemaalquiler.datos;

/**
 *
 * @author xd
 */
import com.mycompany.sistemaalquiler.modelo.ContratoAlquiler;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Types;

public class FacturaDAO {

    public boolean insertar(ContratoAlquiler c) {
        String sql = """
                INSERT INTO facturas (
                    numero_factura,
                    cliente_id,
                    usuario_id,
                    inmueble_id,
                    fecha_emision,
                    fecha_inicio_contrato,
                    fecha_fin_contrato,
                    dias_contratados,
                    meses_contratados,
                    anios_contratados,
                    metodo_pago_id,
                    subtotal,
                    descuento_id,
                    monto_descuento,
                    monto_iva,
                    monto_cargos_adicionales,
                    total
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConexionDB.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {

            int anios = (int) (c.getMesesContratados() / 12);

            ps.setString(1, c.getNumeroFactura());
            ps.setInt(2, c.getCliente().getId());
            ps.setNull(3, Types.BIGINT); // usuario_id opcional
            ps.setInt(4, c.getInmueble().getId());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setDate(6, Date.valueOf(c.getFechaInicio()));
            ps.setDate(7, Date.valueOf(c.getFechaFin()));
            ps.setLong(8, c.getDiasContratados());
            ps.setInt(9, c.getMesesContratados());
            ps.setInt(10, anios);
            ps.setNull(11, Types.BIGINT); // metodo_pago_id 
            ps.setDouble(12, c.getSubtotal());
            ps.setNull(13, Types.BIGINT); // descuento_id 
            ps.setDouble(14, c.getMontoDescuento());
            ps.setDouble(15, c.getMontoIva());
            ps.setDouble(16, 0.0); // monto_cargos_adicionales
            ps.setDouble(17, c.getTotalPagar());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            IO.print("Error al guardar factura en BD: " + e.getMessage() + "\n");
            return false;
        }
    }
}
