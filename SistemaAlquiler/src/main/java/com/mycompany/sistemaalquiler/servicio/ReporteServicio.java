package com.mycompany.sistemaalquiler.servicio;

import com.mycompany.sistemaalquiler.estructuras.Ordenamiento;
import com.mycompany.sistemaalquiler.modelo.VentaRegistro;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Reportes de ventas. Usa estructuras repetitivas + acumuladores y arreglos
 * (por ejemplo un arreglo de 12 posiciones con el total de cada mes).
 */
public class ReporteServicio {

    private static final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private static final String LINEA = "====================================================================\n";
    private static final String SEPARADOR = "--------------------------------------------------------------------\n";

    // Reporte general: lista todas las ventas y suma los totales
    public static String general(List<VentaRegistro> ventas) {
        if (ventas.isEmpty()) {
            return "No hay ventas/alquileres en el historial.\n";
        }
        double totalBase = 0, totalDescuentos = 0, totalCargos = 0, totalIva = 0, totalGeneral = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(LINEA).append("                       REPORTE GENERAL DE VENTAS\n").append(LINEA);
        for (VentaRegistro v : ventas) {
            sb.append(lineaVenta(v));
            totalBase += v.base();
            totalDescuentos += v.descuento();
            totalCargos += v.cargos();
            totalIva += v.iva();
            totalGeneral += v.total();
        }
        sb.append(SEPARADOR);
        sb.append("Total de transacciones: ").append(ventas.size()).append("\n");
        sb.append(String.format("Suma montos base:   $%,10.2f%n", totalBase));
        sb.append(String.format("Total descuentos:   $%,10.2f%n", totalDescuentos));
        sb.append(String.format("Cargos adicionales: $%,10.2f%n", totalCargos));
        sb.append(String.format("Total IVA:          $%,10.2f%n", totalIva));
        sb.append(String.format("INGRESOS TOTALES:   $%,10.2f%n", totalGeneral));
        sb.append(LINEA);
        return sb.toString();
    }

    // Reporte de un dia: total vendido y cantidad de transacciones
    public static String diario(List<VentaRegistro> ventas, LocalDate dia) {
        double total = 0;
        int cantidad = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(LINEA).append("                  REPORTE DE VENTAS DEL ").append(dia).append("\n").append(LINEA);
        for (VentaRegistro v : ventas) {
            if (v.fecha().equals(dia)) {
                sb.append(lineaVenta(v));
                total += v.total();
                cantidad++;
            }
        }
        if (cantidad == 0) {
            sb.append("No hubo ventas en esa fecha.\n");
        }
        sb.append(SEPARADOR);
        sb.append("Cantidad de transacciones: ").append(cantidad).append("\n");
        sb.append(String.format("Total vendido:             $%,10.2f%n", total));
        sb.append(LINEA);
        return sb.toString();
    }

    // Reporte mensual de un anio: dos arreglos de 12 posiciones (una por mes)
    public static String mensual(List<VentaRegistro> ventas, int anio) {
        double[] totalPorMes = new double[12];
        int[] cantidadPorMes = new int[12];

        for (VentaRegistro v : ventas) {
            if (v.fecha().getYear() == anio) {
                int posicion = v.fecha().getMonthValue() - 1;   // enero = posicion 0
                totalPorMes[posicion] += v.total();
                cantidadPorMes[posicion]++;
            }
        }

        double totalAnio = 0;
        int cantidadAnio = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(LINEA).append("                  REPORTE MENSUAL DE VENTAS ").append(anio).append("\n").append(LINEA);
        sb.append(String.format("%-12s %14s %16s%n", "Mes", "Transacciones", "Total vendido"));
        sb.append(SEPARADOR);
        for (int i = 0; i < 12; i++) {
            sb.append(String.format("%-12s %14d $%,15.2f%n", MESES[i], cantidadPorMes[i], totalPorMes[i]));
            totalAnio += totalPorMes[i];
            cantidadAnio += cantidadPorMes[i];
        }
        sb.append(SEPARADOR);
        sb.append(String.format("%-12s %14d $%,15.2f%n", "TOTAL " + anio, cantidadAnio, totalAnio));
        sb.append(LINEA);
        return sb.toString();
    }

    // Reporte anual: un arreglo con una posicion por cada anio con ventas
    public static String anual(List<VentaRegistro> ventas) {
        if (ventas.isEmpty()) {
            return "No hay ventas/alquileres en el historial.\n";
        }
        int anioMenor = ventas.get(0).fecha().getYear();
        int anioMayor = anioMenor;
        for (VentaRegistro v : ventas) {
            anioMenor = Math.min(anioMenor, v.fecha().getYear());
            anioMayor = Math.max(anioMayor, v.fecha().getYear());
        }

        int cantidadAnios = anioMayor - anioMenor + 1;
        double[] totalPorAnio = new double[cantidadAnios];
        int[] cantidadPorAnio = new int[cantidadAnios];
        for (VentaRegistro v : ventas) {
            int posicion = v.fecha().getYear() - anioMenor;
            totalPorAnio[posicion] += v.total();
            cantidadPorAnio[posicion]++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(LINEA).append("                      REPORTE ANUAL DE VENTAS\n").append(LINEA);
        sb.append(String.format("%-12s %14s %16s%n", "Anio", "Transacciones", "Total vendido"));
        sb.append(SEPARADOR);
        for (int i = 0; i < cantidadAnios; i++) {
            sb.append(String.format("%-12d %14d $%,15.2f%n", anioMenor + i, cantidadPorAnio[i], totalPorAnio[i]));
        }
        sb.append(LINEA);
        return sb.toString();
    }

    // Ventas ordenadas por monto, de mayor a menor (algoritmo de seleccion)
    public static String rankingPorTotal(List<VentaRegistro> ventas) {
        if (ventas.isEmpty()) {
            return "No hay ventas/alquileres en el historial.\n";
        }
        ArrayList<VentaRegistro> ordenadas = new ArrayList<>(ventas);
        Ordenamiento.seleccion(ordenadas, Comparator.comparingDouble(VentaRegistro::total).reversed());

        StringBuilder sb = new StringBuilder();
        sb.append(LINEA).append("        VENTAS ORDENADAS POR MONTO (mayor a menor)\n").append(LINEA);
        int puesto = 1;
        for (VentaRegistro v : ordenadas) {
            sb.append(String.format("%2d. ", puesto++)).append(lineaVenta(v));
        }
        sb.append(LINEA);
        return sb.toString();
    }

    private static String lineaVenta(VentaRegistro v) {
        return String.format("%s | %s | Inm: %-9s | %-18s | %-8s | $%,10.2f%n",
                v.numeroFactura(), v.fecha(), v.inmuebleCodigo(), v.cliente(), v.metodoPago(), v.total());
    }
}
