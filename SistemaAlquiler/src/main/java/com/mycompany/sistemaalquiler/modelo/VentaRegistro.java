package com.mycompany.sistemaalquiler.modelo;

import java.time.LocalDate;

/**
 * Resumen de una venta ya facturada. Se usa para los reportes: puede venir
 * de la base de datos (ventas de dias anteriores) o de un contrato recien
 * procesado, asi el reporte siempre incluye todo el historial.
 */
public record VentaRegistro(String numeroFactura, LocalDate fecha, String cliente,
        String inmuebleCodigo, String metodoPago, double base, double descuento,
        double cargos, double iva, double total) {

    public static VentaRegistro de(ContratoAlquiler c) {
        return new VentaRegistro(
                c.getNumeroFactura(),
                c.getFechaEmision().toLocalDate(),
                c.getCliente().getNombre() + " " + c.getCliente().getApellido(),
                c.getInmueble().getCodigo(),
                c.getMetodoPago(),
                c.getPrecioBase(),
                c.getMontoDescuento(),
                c.getCargosAdicionales(),
                c.getMontoIva(),
                c.getTotalPagar());
    }
}
