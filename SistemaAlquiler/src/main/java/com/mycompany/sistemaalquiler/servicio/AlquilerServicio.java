/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.servicio;

/**
 *
 * @author xd
 */
import com.mycompany.sistemaalquiler.datos.FacturaDAO;
import com.mycompany.sistemaalquiler.modelo.Cliente;
import com.mycompany.sistemaalquiler.modelo.ContratoAlquiler;
import com.mycompany.sistemaalquiler.modelo.Inmueble;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class AlquilerServicio {

    public static final double IVA_DEFAULT = 0.13;
    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final InmuebleServicio inmuebleServicio;
    private final Queue<ContratoAlquiler> colaVentas = new ArrayDeque<>();
    private final ArrayList<ContratoAlquiler> historialVentas = new ArrayList<>();

    public AlquilerServicio(InmuebleServicio inmuebleServicio) {
        this.inmuebleServicio = inmuebleServicio;
    }

    public static double calcularDescuentoSugerido(LocalDate inicio, LocalDate fin, boolean porMes) {
        long dias = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(inicio, fin));
        int meses = (int) Math.round(dias / 30.0);
        if (porMes && meses >= 6) {
            return 0.10;
        }
        if (porMes && meses >= 3) {
            return 0.05;
        }
        return 0.0;
    }

    public ContratoAlquiler encolarContrato(Cliente cliente, Inmueble inmueble,
            LocalDate fechaInicio, LocalDate fechaFin,
            boolean porMes, double porcentajeDescuento) {
        if (cliente == null || inmueble == null || fechaInicio == null || fechaFin == null) {
            return null;
        }
        if (!"Disponible".equalsIgnoreCase(inmueble.getEstado())) {
            IO.print("El inmueble " + inmueble.getCodigo() + " no está disponible (estado actual: " + inmueble.getEstado() + ").\n");
            return null;
        }
        if (fechaFin.isBefore(fechaInicio)) {
            IO.print("La fecha de fin no puede ser anterior a la fecha de inicio.\n");
            return null;
        }

        ContratoAlquiler contrato = new ContratoAlquiler(
                cliente, inmueble, fechaInicio, fechaFin, porMes, porcentajeDescuento, IVA_DEFAULT
        );
        colaVentas.offer(contrato);
        return contrato;
    }

    public int cantidadEnCola() {
        return colaVentas.size();
    }

    public ArrayList<ContratoAlquiler> procesarCola() {
        ArrayList<ContratoAlquiler> procesados = new ArrayList<>();
        while (!colaVentas.isEmpty()) {
            ContratoAlquiler c = colaVentas.poll();
            boolean actualizado = inmuebleServicio.actualizarEstadoInmueble(
                    c.getInmueble().getCodigo(), "Alquilado", c.getCliente() != null ? null : null
            );
            if (!actualizado) {
                c.getInmueble().setEstado("Alquilado");
            }
            boolean guardada = facturaDAO.insertar(c);
            if (!guardada) {
                IO.print("Aviso: la factura " + c.getNumeroFactura() + " se generó en memoria pero no pudo guardarse en BD.\n");
            }
            historialVentas.add(c);
            procesados.add(c);
            IO.print(c.generarFacturaTexto() + "\n"); // Req. 8: Factura
        }
        return procesados;
    }

    // Reporte general 
    public String generarReporteVentas() {
        if (historialVentas.isEmpty()) {
            return "No hay ventas/alquileres procesados en el historial.\n";
        }

        double totalBase = 0;
        double totalDescuentos = 0;
        double totalIva = 0;
        double totalGeneral = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================================\n");
        sb.append("                       REPORTE GENERAL DE VENTAS                    \n");
        sb.append("====================================================================\n");

        for (ContratoAlquiler c : historialVentas) {
            sb.append(String.format("%s | Inm: %-8s | Cliente: %-18s | Total: $%,9.2f%n",
                    c.getNumeroFactura(),
                    c.getInmueble().getCodigo(),
                    c.getCliente().getNombre() + " " + c.getCliente().getApellido(),
                    c.getTotalPagar()
            ));
            totalBase += c.getPrecioBase();
            totalDescuentos += c.getMontoDescuento();
            totalIva += c.getMontoIva();
            totalGeneral += c.getTotalPagar();
        }

        sb.append("--------------------------------------------------------------------\n");
        sb.append("Total de operaciones: ").append(historialVentas.size()).append("\n");
        sb.append(String.format("Suma montos base:   $%,10.2f%n", totalBase));
        sb.append(String.format("Total descuentos:   $%,10.2f%n", totalDescuentos));
        sb.append(String.format("Total IVA:          $%,10.2f%n", totalIva));
        sb.append(String.format("INGRESOS TOTALES:   $%,10.2f%n", totalGeneral));
        sb.append("====================================================================\n");
        return sb.toString();
    }

    public ArrayList<ContratoAlquiler> getHistorialVentas() {
        return historialVentas;
    }

}
