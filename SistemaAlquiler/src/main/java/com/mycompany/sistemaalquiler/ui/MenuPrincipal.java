package com.mycompany.sistemaalquiler.ui;

import com.mycompany.sistemaalquiler.datos.UsuarioDB;
import com.mycompany.sistemaalquiler.modelo.Cliente;
import com.mycompany.sistemaalquiler.modelo.ContratoAlquiler;
import com.mycompany.sistemaalquiler.modelo.Inmueble;
import com.mycompany.sistemaalquiler.modelo.TipoInmueble;
import com.mycompany.sistemaalquiler.servicio.AlquilerServicio;
import com.mycompany.sistemaalquiler.servicio.ClienteServicio;
import com.mycompany.sistemaalquiler.servicio.InmuebleServicio;
import com.mycompany.sistemaalquiler.validacion.Validacion;
import java.time.LocalDate;
import java.util.ArrayList;

public class MenuPrincipal {

    public void mostrar() {
        // Instanciamos los servicios UNA SOLA VEZ para mantener listas y cola en memoria
        ClienteServicio clienteServicio = new ClienteServicio();
        InmuebleServicio inmuebleServicio = new InmuebleServicio();
        AlquilerServicio alquilerServicio = new AlquilerServicio(inmuebleServicio);

        int opccion;
        do {
            IO.print("\n============== SISTEMA GERENCIAL DE ALQUILER DE INMUEBLES ==============\n");
            IO.print("1- Registrar inmueble.\n");
            IO.print("2- Registrar cliente.\n");
            IO.print("3- Buscar inmueble / cliente.\n");
            IO.print("4- Control de catalogo (disponibles y proximos a liberarse).\n");
            IO.print("5- Nueva venta / alquiler (permite encolar multiples ventas).\n");
            IO.print("6- Reporte de ventas.\n");
            IO.print("7- Gestionar usuarios.\n");
            IO.print("0- Salir.\n");
            IO.print("========================================================================\n");

            opccion = Consola.leerEntero("Ingrese una opccion: ");

            switch (opccion) {
                case 1 -> {
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

                case 2 -> {
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

                case 3 -> {
                    IO.print("1- Buscar inmueble por codigo\n2- Buscar inmueble por nombre\n");
                    IO.print("3- Buscar inmueble por tipo\n4- Buscar cliente por DUI\n");
                    IO.print("5- Buscar cliente por codigo\n6- Buscar cliente por nombre\n");
                    int opc = Consola.leerEntero("Opccion: ");

                    switch (opc) {
                        case 1 -> {
                            Inmueble i = inmuebleServicio.buscarPorCodigo(Consola.leerTexto("Codigo: "));
                            IO.print(i != null ? i + "\n" : "No encontrado.\n");
                        }
                        case 2 -> {
                            ArrayList<Inmueble> r = inmuebleServicio.buscarPorNombre(Consola.leerTexto("Nombre: "));
                            if (r.isEmpty()) {
                                IO.print("Sin resultados.\n");
                            }
                            for (Inmueble i : r) {
                                IO.print(i + "\n");
                            }
                        }
                        case 3 -> {
                            for (TipoInmueble t : inmuebleServicio.getTipos()) {
                                IO.print(t + "\n");
                            }
                            ArrayList<Inmueble> r = inmuebleServicio.buscarPorTipo(Consola.leerEntero("Tipo: "));
                            if (r.isEmpty()) {
                                IO.print("Sin resultados.\n");
                            }
                            for (Inmueble i : r) {
                                IO.print(i + "\n");
                            }
                        }
                        case 4 -> {
                            Cliente c = clienteServicio.buscarPorDui(Consola.leerDui("DUI: "));
                            IO.print(c != null ? c + "\n" : "No encontrado.\n");
                        }
                        case 5 -> {
                            Cliente c = clienteServicio.buscarPorCodigo(Consola.leerTexto("Codigo: "));
                            IO.print(c != null ? c + "\n" : "No encontrado.\n");
                        }
                        case 6 -> {
                            ArrayList<Cliente> r = clienteServicio.buscarPorNombre(Consola.leerTexto("Nombre: "));
                            if (r.isEmpty()) {
                                IO.print("Sin resultados.\n");
                            }
                            for (Cliente c : r) {
                                IO.print(c + "\n");
                            }
                        }
                        default ->
                            IO.print("Opccion invalida.\n");
                    }
                }

                case 4 -> {
                    IO.print("--- CONTROL DE CATALOGO ---\n");
                    IO.print("1- Ver inmuebles disponibles\n");
                    IO.print("2- Ver catalogo completo\n");
                    IO.print("3- Proximos a liberarse (en 15 dias)\n");
                    IO.print("4- Cambiar estado de un inmueble\n");
                    int sub = Consola.leerEntero("Seleccione: ");

                    switch (sub) {
                        case 1 -> {
                            ArrayList<Inmueble> disp = inmuebleServicio.disponibles();
                            if (disp.isEmpty()) {
                                IO.print("No hay inmuebles disponibles.\n");
                            } else {
                                for (Inmueble i : disp) {
                                    IO.print(i + " | Diario: $" + i.getPrecio_diario() + "\n");
                                }
                            }
                        }
                        case 2 -> {
                            ArrayList<Inmueble> todos = inmuebleServicio.obtenerCatalogoCompleto();
                            if (todos.isEmpty()) {
                                IO.print("El catalogo esta vacio.\n");
                            } else {
                                for (Inmueble i : todos) {
                                    IO.print(i + "\n");
                                }
                            }
                        }
                        case 3 -> {
                            ArrayList<Inmueble> prox = inmuebleServicio.proximosADisponerse(15);
                            if (prox.isEmpty()) {
                                IO.print("Ningun inmueble se libera en los proximos 15 dias.\n");
                            } else {
                                for (Inmueble i : prox) {
                                    IO.print(i + " | Disponible el: " + i.getFechaDisponible() + "\n");
                                }
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
                        default ->
                            IO.print("Opccion invalida.\n");
                    }
                }

                case 5 -> {
                    int agregarMas;
                    do {
                        IO.print("\n--- REGISTRO DE ALQUILER / VENTA ---\n");
                        String duiCliente = Consola.leerDui("DUI del cliente: ");
                        Cliente cliente = clienteServicio.buscarPorDui(duiCliente);
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

                        String codInmueble = Consola.leerTexto("Codigo del inmueble a alquilar: ");
                        Inmueble inmueble = inmuebleServicio.buscarPorCodigo(codInmueble);
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
                            if (fechaFin == null || fechaFin.isBefore(fechaInicio)) {
                                IO.print("Debe ingresar una fecha fin valida (igual o posterior a inicio).\n");
                            }
                        } while (fechaFin == null || fechaFin.isBefore(fechaInicio));

                        int mod;
                        do {
                            mod = Consola.leerEntero("Modalidad de cobro (1- Por mes | 2- Por dia): ");
                        } while (mod != 1 && mod != 2);
                        boolean porMes = (mod == 1);

                        //Descuento sugerido o manual
                        double descSugerido = AlquilerServicio.calcularDescuentoSugerido(fechaInicio, fechaFin, porMes);
                        IO.print(String.format("Descuento sugerido por plazo: %.0f%%%n", descSugerido * 100));
                        double descPorcentaje;
                        do {
                            descPorcentaje = Consola.leerDecimal("Ingrese porcentaje de descuento (0 a 50, ej. 10 para 10%): ");
                            if (descPorcentaje < 0 || descPorcentaje > 50) {
                                IO.print("El descuento debe estar entre 0% y 50%.\n");
                            }
                        } while (descPorcentaje < 0 || descPorcentaje > 50);

                        ContratoAlquiler encolado = alquilerServicio.encolarContrato(
                                cliente, inmueble, fechaInicio, fechaFin, porMes, descPorcentaje / 100.0
                        );

                        if (encolado != null) {
                            IO.print("Operacion agregada a la cola. Pendientes en cola: "
                                    + alquilerServicio.cantidadEnCola() + "\n");
                        }

                        agregarMas = Consola.leerEntero("¿Desea agregar otra venta a la cola? (1- Si | 0- No, procesar ahora): ");
                    } while (agregarMas == 1);

                    if (alquilerServicio.cantidadEnCola() > 0) {
                        IO.print("\nProcesando " + alquilerServicio.cantidadEnCola() + " operacion(es) en cola...\n");
                        alquilerServicio.procesarCola();
                    }
                }

                case 6 -> {
                    IO.print(alquilerServicio.generarReporteVentas());
                }

                case 7 -> {
                    if (!Login.esAdmin()) {
                        IO.print("Acceso denegado. Solo los usuarios con rol ADMIN pueden gestionar usuarios.\n");
                        break;
                    }

                    IO.print("--- GESTION DE USUARIOS (Sesion: " + Login.getUsuarioActual() + ") ---\n");
                    IO.print("1- Registrar nuevo usuario\n");
                    int subOp = Consola.leerEntero("Seleccione una opcion: ");

                    if (subOp == 1) {
                        String nombreCompleto = Consola.leerTexto("Nombre completo: ");
                        String usuario = Consola.leerTexto("Nombre de usuario: ");
                        String clave = Consola.leerTexto("Contrasena: ");

                        IO.print("1- ADMIN\n2- VENDEDOR\n");
                        int opRol;
                        do {
                            opRol = Consola.leerEntero("Seleccione el rol: ");
                        } while (opRol != 1 && opRol != 2);
                        String rol = (opRol == 1) ? "ADMIN" : "VENDEDOR";

                        if (!Validacion.textoValido(nombreCompleto) || !Validacion.textoValido(usuario) || !Validacion.textoValido(clave)) {
                            IO.print("Todos los campos son obligatorios.\n");
                        } else if (UsuarioDB.crearUsuario(nombreCompleto.trim(), usuario.trim(), clave, rol)) {
                            IO.print("Usuario '" + usuario.trim() + "' creado con rol " + rol + ".\n");
                        } else {
                            IO.print("No se pudo crear el usuario (verifique si el nombre de usuario ya existe).\n");
                        }
                    } else {
                        IO.print("Opcion invalida.\n");
                    }
                }
                case 0 -> {
                    IO.print("Saliendo del sistema...\n");
                }
                default -> {
                    IO.print("Ingrese una opccion valida.\n");
                }
            }
        } while (opccion != 0);
    }
}
