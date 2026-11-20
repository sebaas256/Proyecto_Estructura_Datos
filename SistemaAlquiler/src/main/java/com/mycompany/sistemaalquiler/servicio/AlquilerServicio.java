package com.mycompany.sistemaalquiler.servicio;

import com.mycompany.sistemaalquiler.datos.FacturaDAO;
import com.mycompany.sistemaalquiler.estructuras.Cola;
import com.mycompany.sistemaalquiler.estructuras.Pila;
import com.mycompany.sistemaalquiler.modelo.Cliente;
import com.mycompany.sistemaalquiler.modelo.ContratoAlquiler;
import com.mycompany.sistemaalquiler.modelo.Inmueble;
import com.mycompany.sistemaalquiler.modelo.VentaRegistro;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class AlquilerServicio {

    public static final double IVA_DEFAULT = 0.13;
    public static final double CARGO_ADMINISTRATIVO = 15.00; // pago adicional fijo por contrato
    public static final double DESCUENTO_MAXIMO = 0.50;

    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final InmuebleServicio inmuebleServicio;

    // COLA (FIFO): las ventas se atienden en el orden en que se registraron
    private final Cola<ContratoAlquiler> colaVentas = new Cola<>();
    // PILA (LIFO): ventas ya procesadas; la ultima en procesarse es la primera que se puede anular
    private final Pila<ContratoAlquiler> pilaProcesadas = new Pila<>();
    // Historial completo (BD + sesion actual) para los reportes
    private final ArrayList<VentaRegistro> historialVentas = new ArrayList<>();

    public AlquilerServicio(InmuebleServicio inmuebleServicio) {
        this.inmuebleServicio = inmuebleServicio;
        ContratoAlquiler.iniciarSecuencia(facturaDAO.ultimaSecuencia());
        historialVentas.addAll(facturaDAO.listarRegistros());
    }

    // ---------- DESCUENTOS (estructuras selectivas switch / if) ----------
    public static String nombreMetodoPago(int metodo) {
        return switch (metodo) {
            case 1 -> "Efectivo";
            case 2 -> "Tarjeta";
            case 3 -> "Cheque";
            default -> "Otro";
        };
    }

    public static double descuentoPorMetodo(int metodo) {
        return switch (metodo) {
            case 1 -> 0.05;   // efectivo
            case 2 -> 0.02;   // tarjeta
            case 3 -> 0.03;   // cheque
            default -> 0.0;
        };
    }

    public static double descuentoPorPlazo(LocalDate inicio, LocalDate fin, boolean porMes) {
        long dias = Math.max(1, ChronoUnit.DAYS.between(inicio, fin));
        int meses = (int) Math.round(dias / 30.0);
        if (porMes && meses >= 12) {
            return 0.15;
        }
        if (porMes && meses >= 6) {
            return 0.10;
        }
        if (porMes && meses >= 3) {
            return 0.05;
        }
        return 0.0;
    }

    // ---------- COLA DE VENTAS ----------
    public ContratoAlquiler encolarContrato(Cliente cliente, Inmueble inmueble,
            LocalDate fechaInicio, LocalDate fechaFin,
            boolean porMes, String metodoPago, double porcentajeDescuento) {
        if (cliente == null || inmueble == null || fechaInicio == null || fechaFin == null) {
            return null;
        }
        if (!"Disponible".equalsIgnoreCase(inmueble.getEstado())) {
            IO.print("El inmueble " + inmueble.getCodigo() + " no esta disponible (estado actual: " + inmueble.getEstado() + ").\n");
            return null;
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            IO.print("La fecha de fin debe ser posterior a la fecha de inicio.\n");
            return null;
        }
        // Un mismo inmueble no puede estar dos veces en la cola
        for (ContratoAlquiler pendiente : colaVentas) {
            if (pendiente.getInmueble().getId() == inmueble.getId()) {
                IO.print("El inmueble " + inmueble.getCodigo() + " ya esta en la cola de ventas pendientes.\n");
                return null;
            }
        }

        ContratoAlquiler contrato = new ContratoAlquiler(cliente, inmueble, fechaInicio, fechaFin,
                porMes, metodoPago, Math.min(DESCUENTO_MAXIMO, porcentajeDescuento),
                IVA_DEFAULT, CARGO_ADMINISTRATIVO);
        colaVentas.encolar(contrato);
        return contrato;
    }

    public int cantidadEnCola() {
        return colaVentas.tamanio();
    }

    // Atiende la cola en orden FIFO: marca el inmueble como alquilado, guarda y muestra la factura
    public ArrayList<ContratoAlquiler> procesarCola(String usuario) {
        ArrayList<ContratoAlquiler> procesados = new ArrayList<>();
        while (!colaVentas.estaVacia()) {
            ContratoAlquiler c = colaVentas.desencolar();

            // el inmueble queda ocupado hasta que termine el contrato
            boolean actualizado = inmuebleServicio.marcarComoAlquilado(c.getInmueble().getCodigo(), c.getFechaFin());
            if (!actualizado) {
                IO.print("Aviso: no se pudo actualizar el estado de " + c.getInmueble().getCodigo() + " en la BD.\n");
                c.getInmueble().setEstado("Alquilado");
                c.getInmueble().setFechaDisponible(c.getFechaFin());
            }

            if (!facturaDAO.insertar(c, usuario)) {
                IO.print("Aviso: la factura " + c.getNumeroFactura() + " no pudo guardarse en la BD.\n");
            }
            historialVentas.add(VentaRegistro.de(c));
            pilaProcesadas.push(c);
            procesados.add(c);
            IO.print(c.generarFacturaTexto() + "\n");
        }
        return procesados;
    }

    // ---------- ANULAR (usa la pila: LIFO) ----------
    public ContratoAlquiler ultimaVentaProcesada() {
        return pilaProcesadas.peek();
    }

    // Anula la ultima venta procesada en esta sesion: borra la factura y libera el inmueble
    public ContratoAlquiler anularUltimaVenta() {
        if (pilaProcesadas.estaVacia()) {
            return null;
        }
        ContratoAlquiler c = pilaProcesadas.pop();
        facturaDAO.eliminarPorNumero(c.getNumeroFactura());
        inmuebleServicio.liberarInmueble(c.getInmueble().getCodigo());
        for (int i = historialVentas.size() - 1; i >= 0; i--) {
            if (historialVentas.get(i).numeroFactura().equals(c.getNumeroFactura())) {
                historialVentas.remove(i);
                break;
            }
        }
        return c;
    }

    public ArrayList<VentaRegistro> getHistorialVentas() {
        return historialVentas;
    }
}
