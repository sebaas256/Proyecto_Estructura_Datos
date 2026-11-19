/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.modelo;

/**
 *
 * @author xd
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContratoAlquiler {

    private static int secuenciaFactura = 1000;

    private final String numeroFactura;
    private final Cliente cliente;
    private final Inmueble inmueble;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final long diasContratados;
    private final int mesesContratados;
    private final boolean porMes;

    private final double precioBase;
    private final double porcentajeDescuento; //0.10 = 10%
    private final double montoDescuento;
    private final double subtotal;
    private final double tasaIva;           //0.13 = 13%
    private final double montoIva;
    private final double totalPagar;

    public ContratoAlquiler(Cliente cliente, Inmueble inmueble,
            LocalDate fechaInicio, LocalDate fechaFin,
            boolean porMes, double porcentajeDescuento, double tasaIva) {
        this.numeroFactura = "FAC-" + (++secuenciaFactura);
        this.cliente = cliente;
        this.inmueble = inmueble;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porMes = porMes;

        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        this.diasContratados = Math.max(1, dias);
        this.mesesContratados = (int) Math.max(1, Math.round(this.diasContratados / 30.0));

        // Monto base por mes o por día
        if (porMes) {
            this.precioBase = inmueble.getPrecio_mensual() * this.mesesContratados;
        } else {
            this.precioBase = inmueble.getPrecio_diario() * this.diasContratados;
        }

        //Descuento
        this.porcentajeDescuento = Math.max(0.0, porcentajeDescuento);
        this.montoDescuento = this.precioBase * this.porcentajeDescuento;
        this.subtotal = this.precioBase - this.montoDescuento;

        //IVA
        this.tasaIva = Math.max(0.0, tasaIva);
        this.montoIva = this.subtotal * this.tasaIva;
        this.totalPagar = this.subtotal + this.montoIva;
    }

    //Generación de factura 
    public String generarFacturaTexto() {
        String modalidad = porMes ? (mesesContratados + " mes(es)") : (diasContratados + " día(s)");
        return """
               =========================================================
                               FACTURA DE ALQUILER
               =========================================================
               Factura N°:   %s
               Fecha emisión:%s
               Cliente:      %s %s (DUI: %s | Cód: %s)
               Inmueble:     %s - %s
               Dirección:    %s
               Período:      %s al %s (%s)
               ---------------------------------------------------------
               Monto base:           $%,10.2f
               Descuento (%5.1f%%):  -$%,10.2f
               Subtotal:             $%,10.2f
               IVA (%5.1f%%):        +$%,10.2f
               =========================================================
               TOTAL A PAGAR:        $%,10.2f
               =========================================================
               """.formatted(
                numeroFactura,
                LocalDate.now(),
                cliente.getNombre(), cliente.getApellido(), cliente.getDui(), cliente.getCodigo(),
                inmueble.getCodigo(), inmueble.getNombre(),
                inmueble.getDireccion(),
                fechaInicio, fechaFin, modalidad,
                precioBase,
                porcentajeDescuento * 100, montoDescuento,
                subtotal,
                tasaIva * 100, montoIva,
                totalPagar
        );
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Inmueble getInmueble() {
        return inmueble;
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

}
