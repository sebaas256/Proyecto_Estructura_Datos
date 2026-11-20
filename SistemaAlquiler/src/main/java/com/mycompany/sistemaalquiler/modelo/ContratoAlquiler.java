package com.mycompany.sistemaalquiler.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Un alquiler/venta con todos sus calculos: monto base, descuento, cargos
 * adicionales, IVA y total. Tambien genera el texto de la factura.
 */
public class ContratoAlquiler {

    private static int secuenciaFactura = 1000;

    // Evita repetir numeros de factura despues de reiniciar el programa
    // (se alimenta con el ultimo numero guardado en la BD).
    public static void iniciarSecuencia(int ultimaGuardada) {
        if (ultimaGuardada > secuenciaFactura) {
            secuenciaFactura = ultimaGuardada;
        }
    }

    private final String numeroFactura;
    private final LocalDateTime fechaEmision;
    private final Cliente cliente;
    private final Inmueble inmueble;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final long diasContratados;
    private final int mesesContratados;
    private final boolean porMes;
    private final String metodoPago;

    private final double precioBase;
    private final double porcentajeDescuento; // 0.10 = 10%
    private final double montoDescuento;
    private final double subtotal;            // base - descuento
    private final double cargosAdicionales;   // pagos adicionales (gastos administrativos)
    private final double tasaIva;             // 0.13 = 13%
    private final double montoIva;
    private final double totalPagar;

    public ContratoAlquiler(Cliente cliente, Inmueble inmueble,
            LocalDate fechaInicio, LocalDate fechaFin,
            boolean porMes, String metodoPago, double porcentajeDescuento,
            double tasaIva, double cargosAdicionales) {
        this.numeroFactura = "FAC-" + (++secuenciaFactura);
        this.fechaEmision = LocalDateTime.now();
        this.cliente = cliente;
        this.inmueble = inmueble;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porMes = porMes;
        this.metodoPago = metodoPago;

        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        this.diasContratados = Math.max(1, dias);
        this.mesesContratados = (int) Math.max(1, Math.round(this.diasContratados / 30.0));

        // Monto base por mes o por dia
        if (porMes) {
            this.precioBase = inmueble.getPrecio_mensual() * this.mesesContratados;
        } else {
            this.precioBase = inmueble.getPrecio_diario() * this.diasContratados;
        }

        // Descuento (nunca mas del 50%)
        this.porcentajeDescuento = Math.min(0.50, Math.max(0.0, porcentajeDescuento));
        this.montoDescuento = this.precioBase * this.porcentajeDescuento;
        this.subtotal = this.precioBase - this.montoDescuento;

        // Cargos adicionales + IVA sobre (subtotal + cargos)
        this.cargosAdicionales = Math.max(0.0, cargosAdicionales);
        double baseImponible = this.subtotal + this.cargosAdicionales;
        this.tasaIva = Math.max(0.0, tasaIva);
        this.montoIva = baseImponible * this.tasaIva;
        this.totalPagar = baseImponible + this.montoIva;
    }

    // Generacion de factura
    public String generarFacturaTexto() {
        Period p = Period.between(fechaInicio, fechaFin);
        String modalidad = porMes ? "cobro por mes" : "cobro por dia";
        return """
               =========================================================
                               FACTURA DE ALQUILER
               =========================================================
               Factura N°:    %s
               Fecha emision: %s
               Cliente:       %s %s (DUI: %s | Cod: %s)
               Inmueble:      %s - %s
               Direccion:     %s
               Periodo:       %s al %s (%s)
               Duracion:      %d anio(s), %d mes(es), %d dia(s)  [%d dias en total]
               Metodo de pago: %s
               ---------------------------------------------------------
               Monto base:              $%,10.2f
               Descuento (%5.1f%%):     -$%,10.2f
               Subtotal:                $%,10.2f
               Cargos adicionales:     +$%,10.2f
               IVA (%5.1f%%):           +$%,10.2f
               =========================================================
               TOTAL A PAGAR:           $%,10.2f
               =========================================================
               """.formatted(
                numeroFactura,
                fechaEmision.toLocalDate(),
                cliente.getNombre(), cliente.getApellido(), cliente.getDui(), cliente.getCodigo(),
                inmueble.getCodigo(), inmueble.getNombre(),
                inmueble.getDireccion(),
                fechaInicio, fechaFin, modalidad,
                p.getYears(), p.getMonths(), p.getDays(), diasContratados,
                metodoPago,
                precioBase,
                porcentajeDescuento * 100, montoDescuento,
                subtotal,
                cargosAdicionales,
                tasaIva * 100, montoIva,
                totalPagar
        );
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public double getMontoDescuento() {
        return montoDescuento;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getCargosAdicionales() {
        return cargosAdicionales;
    }

    public double getMontoIva() {
        return montoIva;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public long getDiasContratados() {
        return diasContratados;
    }

    public int getMesesContratados() {
        return mesesContratados;
    }

    public int getAniosContratados() {
        return mesesContratados / 12;
    }
}
