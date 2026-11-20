package com.mycompany.sistemaalquiler.ui;

import com.mycompany.sistemaalquiler.datos.UsuarioDB;
import com.mycompany.sistemaalquiler.estructuras.Busqueda;
import com.mycompany.sistemaalquiler.modelo.Cliente;
import com.mycompany.sistemaalquiler.modelo.ContratoAlquiler;
import com.mycompany.sistemaalquiler.modelo.Inmueble;
import com.mycompany.sistemaalquiler.modelo.TipoInmueble;
import com.mycompany.sistemaalquiler.servicio.AlquilerServicio;
import com.mycompany.sistemaalquiler.servicio.ClienteServicio;
import com.mycompany.sistemaalquiler.servicio.InmuebleServicio;
import com.mycompany.sistemaalquiler.servicio.ReporteServicio;
import com.mycompany.sistemaalquiler.validacion.Validacion;
import java.time.LocalDate;
import java.util.ArrayList;

public class MenuPrincipal {

    // Los servicios se crean UNA SOLA VEZ para mantener listas, cola y pila en memoria
    private final ClienteServicio clienteServicio = new ClienteServicio();
    private final InmuebleServicio inmuebleServicio = new InmuebleServicio();
    private final AlquilerServicio alquilerServicio = new AlquilerServicio(inmuebleServicio);

    public void mostrar() {
        int opcion;
        do {
            IO.print("\n============== SISTEMA GERENCIAL DE ALQUILER DE INMUEBLES ==============\n");
            IO.print("1- Registrar inmueble.\n");
            IO.print("2- Registrar cliente.\n");
            IO.print("3- Buscar inmueble / cliente.\n");
            IO.print("4- Control de catalogo (disponibles, proximos a liberarse y ordenamiento).\n");
            IO.print("5- Nueva venta / alquiler (permite encolar multiples ventas).\n");
            IO.print("6- Reporte de ventas.\n");
            IO.print("7- Gestionar usuarios.\n");
            IO.print("8- Deshacer ultimo registro de cliente / anular ultima venta.\n");
            IO.print("0- Salir.\n");
            IO.print("========================================================================\n");

            opcion = Consola.leerEntero("Ingrese una opcion: ");

            switch (opcion) {
                case 1 -> registrarInmueble();
                case 2 -> registrarCliente();
                case 3 -> buscar();
                case 4 -> controlCatalogo();
                case 5 -> nuevaVenta();
                case 6 -> reportes();
                case 7 -> gestionarUsuarios();
                case 8 -> deshacer();
                case 0 -> IO.print("Saliendo del sistema...\n");
                default -> IO.print("Ingrese una opcion valida.\n");
            }
        } while (opcion != 0);
    }

    // ===================== 1. REGISTRO DE INMUEBLES =====================
    private void registrarInmueble() {
        String codigo = inmuebleServicio.generarCodigo();
        String nombre = Consola.leerTexto("Nombre: ");

        IO.print("--- Tipos de inmueble ---\n");
        for (TipoInmueble t : inmuebleServicio.getTipos()) {
            IO.print(t + "\n");
        }
        int tipoId;
        do {
            tipoId = Consola.leerEntero("Seleccione el tipo: ");
            if (!inmuebleServicio.existeTipo(tipoId)) {
                IO.print("Tipo invalido, elija uno de la lista.\n");
            }
        } while (!inmuebleServicio.existeTipo(tipoId));

        double precioMensual = Consola.leerPrecio("Precio mensual: ");
        double precioDiario = Consola.leerPrecio("Precio diario: ");
        int capacidad = Consola.leerEnteroNoNegativo("Capacidad (personas): ");
        int habitaciones = Consola.leerEnteroNoNegativo("Habitaciones: ");
        int banos = Consola.leerEnteroNoNegativo("Baños: ");
        double area = Consola.leerPrecio("Area (m2): ");
        String caracteristicas = IO.readln("Caracteristicas (opcional): ").trim();
        if (caracteristicas.isEmpty()) {
            caracteristicas = null;
        }
        String direccion = Consola.leerTexto("Direccion: ");
        String estado = Consola.leerEstado();
        LocalDate fecha = Consola.leerFechaOpcional("Fecha de disponibilidad (AAAA-MM-DD, Enter para omitir): ");

        Inmueble i = new Inmueble(codigo, nombre, tipoId, precioMensual, precioDiario,
                capacidad, habitaciones, banos, area, caracteristicas, direccion, estado, fecha);

        if (inmuebleServicio.registrar(i)) {
            IO.print("Inmueble registrado con codigo " + codigo + "\n");
        }
    }

    // ===================== 2. REGISTRO DE CLIENTES =====================
    private void registrarCliente() {
        String codigo = clienteServicio.generarCodigo();
        String nombre = Consola.leerTexto("Nombre: ");
        String apellido = Consola.leerTexto("Apellido: ");
        String dui = Consola.leerDui("DUI: ");
        int edad = Consola.leerEdad("Edad: ");
        String correo = Consola.leerCorreo("Correo: ");
        String telefono = Consola.leerTelefono("Telefono: ");
        String direccion = Consola.leerTexto("Direccion: ");

        Cliente c = new Cliente(codigo, nombre, apellido, dui, edad, correo, telefono, direccion);
        if (clienteServicio.registrar(c)) {
            IO.print("Cliente registrado con codigo " + codigo + "\n");
        }
    }

    // ===================== 3. BUSQUEDAS =====================
    private void buscar() {
        IO.print("1- Buscar inmueble por codigo (busqueda binaria)\n");
        IO.print("2- Buscar inmueble por nombre\n");
        IO.print("3- Buscar inmueble por tipo\n");
        IO.print("4- Buscar cliente por DUI (busqueda binaria)\n");
        IO.print("5- Buscar cliente por codigo\n");
        IO.print("6- Buscar cliente por nombre\n");
        IO.print("7- Listar clientes ordenados por nombre\n");
        int opcion = Consola.leerEntero("Opcion: ");

        switch (opcion) {
            case 1 -> {
                Inmueble i = inmuebleServicio.buscarPorCodigoBinaria(Consola.leerTexto("Codigo: "));
                IO.print(i != null ? i + "\n" : "No encontrado.\n");
                IO.print("(Busqueda binaria: " + Busqueda.comparaciones + " comparaciones)\n");
            }
            case 2 -> {
                ArrayList<Inmueble> r = inmuebleServicio.buscarPorNombre(Consola.leerTexto("Nombre: "));
                mostrarInmuebles(r, "Sin resultados.\n");
            }
            case 3 -> {
                for (TipoInmueble t : inmuebleServicio.getTipos()) {
                    IO.print(t + "\n");
                }
                ArrayList<Inmueble> r = inmuebleServicio.buscarPorTipo(Consola.leerEntero("Tipo: "));
                mostrarInmuebles(r, "Sin resultados.\n");
            }
            case 4 -> {
                Cliente c = clienteServicio.buscarPorDuiBinaria(Consola.leerDui("DUI: "));
                IO.print(c != null ? c + "\n" : "No encontrado.\n");
                IO.print("(Busqueda binaria: " + Busqueda.comparaciones + " comparaciones)\n");
            }
            case 5 -> {
                Cliente c = clienteServicio.buscarPorCodigo(Consola.leerTexto("Codigo: "));
                IO.print(c != null ? c + "\n" : "No encontrado.\n");
            }
            case 6 -> {
                ArrayList<Cliente> r = clienteServicio.buscarPorNombre(Consola.leerTexto("Nombre: "));
                mostrarClientes(r);
            }
            case 7 -> mostrarClientes(clienteServicio.listarOrdenadosPorNombre());
            default -> IO.print("Opcion invalida.\n");
        }
    }

    // ===================== 4. CONTROL DE CATALOGO =====================
    private void controlCatalogo() {
        IO.print("--- CONTROL DE CATALOGO ---\n");
        IO.print("1- Ver inmuebles disponibles\n");
        IO.print("2- Ver catalogo completo\n");
        IO.print("3- Proximos a liberarse (en 15 dias)\n");
        IO.print("4- Cambiar estado de un inmueble\n");
        IO.print("5- Ver catalogo ordenado (precio / nombre / capacidad)\n");
        int opcion = Consola.leerEntero("Seleccione: ");

        switch (opcion) {
            case 1 -> {
                ArrayList<Inmueble> disp = inmuebleServicio.disponibles();
                if (disp.isEmpty()) {
                    IO.print("No hay inmuebles disponibles.\n");
                }
                for (Inmueble i : disp) {
                    IO.print(i + " | Diario: $" + i.getPrecio_diario() + "\n");
                }
            }
            case 2 -> mostrarInmuebles(inmuebleServicio.obtenerCatalogoCompleto(), "El catalogo esta vacio.\n");
            case 3 -> {
                ArrayList<Inmueble> prox = inmuebleServicio.proximosADisponerse(15);
                if (prox.isEmpty()) {
                    IO.print("Ningun inmueble se libera en los proximos 15 dias.\n");
                }
                for (Inmueble i : prox) {
                    IO.print(i + " | Disponible el: " + i.getFechaDisponible() + "\n");
                }
            }
            case 4 -> {
                String cod = Consola.leerTexto("Codigo del inmueble: ");
                String nuevoEstado = Consola.leerEstado();
                LocalDate fechaDisp = Consola.leerFechaOpcional("Nueva fecha disponible (AAAA-MM-DD, Enter para omitir): ");
                if (inmuebleServicio.actualizarEstadoInmueble(cod, nuevoEstado, fechaDisp)) {
                    IO.print("Estado actualizado correctamente.\n");
                } else {
                    IO.print("No se pudo actualizar (verifique el codigo).\n");
                }
            }
            case 5 -> {
                IO.print("Ordenar por: 1- Precio mensual | 2- Nombre | 3- Capacidad\n");
                int criterio = Consola.leerEnRango("Criterio: ", 1, 3);
                ArrayList<Inmueble> lista = inmuebleServicio.obtenerCatalogoCompleto();
                String algoritmo = inmuebleServicio.ordenarCatalogo(lista, criterio);
                mostrarInmuebles(lista, "El catalogo esta vacio.\n");
                IO.print("(Ordenado con el algoritmo de " + algoritmo + ")\n");
            }
            default -> IO.print("Opcion invalida.\n");
        }
    }

    // ===================== 5. NUEVA VENTA / ALQUILER =====================
    private void nuevaVenta() {
        int agregarMas;
        do {
            IO.print("\n--- REGISTRO DE ALQUILER / VENTA ---\n");
            Cliente cliente = clienteServicio.buscarPorDui(Consola.leerDui("DUI del cliente: "));
            if (cliente == null) {
                IO.print("Cliente no encontrado. Registrelo primero en la opcion 2.\n");
                break;
            }

            ArrayList<Inmueble> disponibles = inmuebleServicio.disponibles();
            if (disponibles.isEmpty()) {
                IO.print("No hay inmuebles disponibles para alquilar.\n");
                break;
            }
            IO.print("Inmuebles disponibles:\n");
            for (Inmueble inm : disponibles) {
                IO.print("  " + inm + " | Diario: $" + inm.getPrecio_diario() + "\n");
            }

            Inmueble inmueble = inmuebleServicio.buscarPorCodigo(Consola.leerTexto("Codigo del inmueble a alquilar: "));
            if (inmueble == null || !"Disponible".equalsIgnoreCase(inmueble.getEstado())) {
                IO.print("Inmueble invalido o no disponible.\n");
                break;
            }

            LocalDate fechaInicio = Consola.leerFechaOpcional("Fecha inicio (AAAA-MM-DD, Enter para hoy): ");
            if (fechaInicio == null) {
                fechaInicio = LocalDate.now();
            }
            LocalDate fechaFin;
            do {
                fechaFin = Consola.leerFechaOpcional("Fecha fin (AAAA-MM-DD): ");
                if (fechaFin == null || !fechaFin.isAfter(fechaInicio)) {
                    IO.print("Debe ingresar una fecha fin posterior a la fecha de inicio.\n");
                }
            } while (fechaFin == null || !fechaFin.isAfter(fechaInicio));

            boolean porMes = Consola.leerEnRango("Modalidad de cobro (1- Por mes | 2- Por dia): ", 1, 2) == 1;

            // Descuentos: por metodo de pago (switch) + por periodo largo
            IO.print("Metodo de pago: 1- Efectivo (5% desc.) | 2- Tarjeta (2%) | 3- Cheque (3%)\n");
            int metodo = Consola.leerEnRango("Seleccione el metodo: ", 1, 3);
            double descMetodo = AlquilerServicio.descuentoPorMetodo(metodo);
            double descPlazo = AlquilerServicio.descuentoPorPlazo(fechaInicio, fechaFin, porMes);
            IO.print(String.format("Descuento por metodo de pago: %.0f%% | por periodo largo: %.0f%%%n",
                    descMetodo * 100, descPlazo * 100));

            ContratoAlquiler encolado = alquilerServicio.encolarContrato(cliente, inmueble,
                    fechaInicio, fechaFin, porMes,
                    AlquilerServicio.nombreMetodoPago(metodo), descMetodo + descPlazo);

            if (encolado != null) {
                IO.print("Operacion agregada a la cola. Pendientes en cola: "
                        + alquilerServicio.cantidadEnCola() + "\n");
            }

            agregarMas = Consola.leerEntero("¿Desea agregar otra venta a la cola? (1- Si | 0- No, procesar ahora): ");
        } while (agregarMas == 1);

        if (alquilerServicio.cantidadEnCola() > 0) {
            IO.print("\nProcesando " + alquilerServicio.cantidadEnCola() + " operacion(es) en cola...\n");
            alquilerServicio.procesarCola(Login.getUsuarioActual());
        }
    }

    // ===================== 6. REPORTES =====================
    private void reportes() {
        IO.print("--- REPORTE DE VENTAS ---\n");
        IO.print("1- Reporte general\n");
        IO.print("2- Ventas de un dia\n");
        IO.print("3- Ventas por mes (de un anio)\n");
        IO.print("4- Ventas por anio\n");
        IO.print("5- Ventas ordenadas por monto\n");
        int opcion = Consola.leerEntero("Seleccione: ");
        ArrayList<com.mycompany.sistemaalquiler.modelo.VentaRegistro> ventas = alquilerServicio.getHistorialVentas();

        switch (opcion) {
            case 1 -> IO.print(ReporteServicio.general(ventas));
            case 2 -> {
                LocalDate dia = Consola.leerFechaOpcional("Fecha (AAAA-MM-DD, Enter para hoy): ");
                IO.print(ReporteServicio.diario(ventas, dia == null ? LocalDate.now() : dia));
            }
            case 3 -> {
                int anio = Consola.leerEnRango("Anio (ej. 2026): ", 2000, 2100);
                IO.print(ReporteServicio.mensual(ventas, anio));
            }
            case 4 -> IO.print(ReporteServicio.anual(ventas));
            case 5 -> IO.print(ReporteServicio.rankingPorTotal(ventas));
            default -> IO.print("Opcion invalida.\n");
        }
    }

    // ===================== 7. USUARIOS =====================
    private void gestionarUsuarios() {
        if (!Login.esAdmin()) {
            IO.print("Acceso denegado. Solo los usuarios con rol ADMIN pueden gestionar usuarios.\n");
            return;
        }

        IO.print("--- GESTION DE USUARIOS (Sesion: " + Login.getUsuarioActual() + ") ---\n");
        IO.print("1- Registrar nuevo usuario\n");
        int opcion = Consola.leerEntero("Seleccione una opcion: ");

        if (opcion != 1) {
            IO.print("Opcion invalida.\n");
            return;
        }

        String nombreCompleto = Consola.leerTexto("Nombre completo: ");
        String usuario = Consola.leerTexto("Nombre de usuario: ");
        String clave = Consola.leerTexto("Contrasena: ");

        IO.print("1- ADMIN\n2- VENDEDOR\n");
        String rol = Consola.leerEnRango("Seleccione el rol: ", 1, 2) == 1 ? "ADMIN" : "VENDEDOR";

        if (!Validacion.textoValido(nombreCompleto) || !Validacion.textoValido(usuario) || !Validacion.textoValido(clave)) {
            IO.print("Todos los campos son obligatorios.\n");
        } else if (UsuarioDB.crearUsuario(nombreCompleto.trim(), usuario.trim(), clave, rol)) {
            IO.print("Usuario '" + usuario.trim() + "' creado con rol " + rol + ".\n");
        } else {
            IO.print("No se pudo crear el usuario (verifique si el nombre de usuario ya existe).\n");
        }
    }

    // ===================== 8. DESHACER (usa PILAS) =====================
    private void deshacer() {
        if (!Login.esAdmin()) {
            IO.print("Acceso denegado. Solo los usuarios con rol ADMIN pueden deshacer operaciones.\n");
            return;
        }
        IO.print("--- DESHACER (la ultima operacion es la primera en deshacerse: LIFO) ---\n");
        IO.print("1- Deshacer ultimo cliente registrado\n");
        IO.print("2- Anular ultima venta procesada\n");
        int opcion = Consola.leerEnRango("Seleccione: ", 1, 2);

        if (opcion == 1) {
            Cliente ultimo = clienteServicio.ultimoRegistrado();
            if (ultimo == null) {
                IO.print("No hay clientes registrados en esta sesion para deshacer.\n");
            } else if (Consola.leerEnRango("Se eliminara " + ultimo + ". ¿Confirmar? (1- Si | 0- No): ", 0, 1) == 1) {
                Cliente eliminado = clienteServicio.deshacerUltimo();
                IO.print(eliminado != null
                        ? "Cliente " + eliminado.getCodigo() + " eliminado.\n"
                        : "No se pudo deshacer (puede que el cliente ya tenga facturas).\n");
            }
        } else {
            ContratoAlquiler ultima = alquilerServicio.ultimaVentaProcesada();
            if (ultima == null) {
                IO.print("No hay ventas procesadas en esta sesion para anular.\n");
            } else if (Consola.leerEnRango("Se anulara la factura " + ultima.getNumeroFactura()
                    + " del inmueble " + ultima.getInmueble().getCodigo() + ". ¿Confirmar? (1- Si | 0- No): ", 0, 1) == 1) {
                ContratoAlquiler anulada = alquilerServicio.anularUltimaVenta();
                IO.print("Venta " + anulada.getNumeroFactura() + " anulada. El inmueble "
                        + anulada.getInmueble().getCodigo() + " quedo disponible.\n");
            }
        }
    }

    // ===================== AUXILIARES =====================
    private void mostrarInmuebles(ArrayList<Inmueble> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            IO.print(mensajeVacio);
        }
        for (Inmueble i : lista) {
            IO.print(i + "\n");
        }
    }

    private void mostrarClientes(ArrayList<Cliente> lista) {
        if (lista.isEmpty()) {
            IO.print("Sin resultados.\n");
        }
        for (Cliente c : lista) {
            IO.print(c + "\n");
        }
    }
}
