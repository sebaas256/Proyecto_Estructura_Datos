/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.ui;

import com.mycompany.sistemaalquiler.modelo.Cliente;
import com.mycompany.sistemaalquiler.modelo.Inmueble;
import com.mycompany.sistemaalquiler.modelo.TipoInmueble;
import com.mycompany.sistemaalquiler.servicio.ClienteServicio;
import com.mycompany.sistemaalquiler.servicio.InmuebleServicio;
import java.time.LocalDate;
import java.util.ArrayList;

public class MenuPrincipal {
    public void mostrar(){
        int opccion;
        do {            
            IO.print("==============SISTEMA GERENCIAL DE ALQUILER DE INMUEBLES==============\n");
            IO.print("1- Registrar inmueble.\n");
            IO.print("2- Registrar cliente.\n");
            IO.print("3- Buscar inmueble / cliente.\n");
            IO.print("4- Ver catalogo disponible.\n");
            IO.print("5- Nueva venta / alquiler.\n");
            IO.print("6- Reporte de ventas.\n");
            IO.print("7- Gestionar usuarios.\n");
            IO.print("0- Salir.\n");
            IO.print("======================================================================\n");
            
            opccion = Consola.leerEntero("Ingrese una opccion: ");
            ClienteServicio clienteServicio = new ClienteServicio();
            InmuebleServicio inmuebleServicio = new InmuebleServicio();
            
            
            switch(opccion){
                case 1->{
                    String codigo = inmuebleServicio.generarCodigo();
                    String nombre = Consola.leerTexto("Nombre: ");
                    
                    IO.print("--- Tipos de inmueble ---\n");
                    for (TipoInmueble t : inmuebleServicio.getTipos()) {
                        IO.print(t + "\n");
                    }
                    int tipoId;
                    do{
                        tipoId = Consola.leerEntero("Seleccione el tipo: ");
                        if (!inmuebleServicio.existeTipo(tipoId)){
                            IO.print("Tipo invalido, elija uno de la lista.\n");
                        }
                    }while(!inmuebleServicio.existeTipo(tipoId));
                    double precioMensual = Consola.leerPrecio("Precio mensual: ");
                    double precioDiario = Consola.leerPrecio("Precio diario: ");
                    int capacidad = Consola.leerEnteroNoNegativo("Capacidad (personas): ");
                    int habitaciones = Consola.leerEnteroNoNegativo("Habitaciones: ");
                    int banos = Consola.leerEnteroNoNegativo("Baños: ");
                    double area = Consola.leerPrecio("Area (m2): ");
                    String caracteristicas = IO.readln("Caracteristicas (opcional): ").trim();
                    if (caracteristicas.isEmpty()) caracteristicas = null;
                    String direccion = Consola.leerTexto("Direccion: ");
                    String estado = Consola.leerEstado();
                    LocalDate fecha = Consola.leerFechaOpcional("Fecha de disponibilidad (AAAA-MM-DD, Enter para omitir): ");

                    Inmueble i = new Inmueble(codigo, nombre, tipoId, precioMensual, precioDiario,
                                capacidad, habitaciones, banos, area, caracteristicas, direccion, estado, fecha);

                     if (inmuebleServicio.registrar(i)) {
                        IO.print("Inmueble registrado con código " + codigo + "\n");
                     }
                }
                case 2 ->{
                    String codigo = clienteServicio.generarCodigo();
                    String nombre = Consola.leerTexto("Nombre: ");
                    String apellido = Consola.leerTexto("Apellido: ");
                    String dui = Consola.leerDui("DUI: ");
                    int edad = Consola.leerEdad("Edad: ");
                    String correo = Consola.leerCorreo("Correo: ");
                    String telefono = Consola.leerTexto("Telefono: ");
                    String direccion = Consola.leerTexto("Direccion: ");
                    
                    Cliente c = new Cliente(codigo, nombre, apellido, dui, edad, correo, telefono, direccion);
                    if (clienteServicio.registrar(c)){
                        IO.print("Cliente registrado con codigo " + codigo + "\n");
                    }
                }
                case 3->{
                    IO.print("1- Buscar inmueble por codigo\n2- Buscar inmueble por nombre\n");
                    IO.print("3- Buscar inmueble por tipo\n4- Buscar cliente por DUI\n");
                    IO.print("5- Buscar cliente por codigo\n6- Buscar cliente por nombre\n");
                    int opc = Consola.leerEntero("Opccion: ");
                    
                    switch(opc){
                        case 1->{
                            Inmueble i = inmuebleServicio.buscarPorCodigo(Consola.leerTexto("Codigo: "));
                            IO.print(i != null ? i + "\n" : "No encontrado.\n");
                        }
                        case 2->{
                            ArrayList<Inmueble> r = inmuebleServicio.buscarPorNombre(Consola.leerTexto("Nombre: "));
                            if (r.isEmpty()) IO.print("Sin resultados. \n");
                            for(Inmueble i : r) IO.print(i + "\n");
                        }
                        case 3->{
                            for (TipoInmueble t : inmuebleServicio.getTipos()) IO.print(t + "\n");
                            ArrayList<Inmueble> r = inmuebleServicio.buscarPorTipo(Consola.leerEntero("Tipo: "));
                            if (r.isEmpty()) IO.print("Sin resultados.\n");
                            for (Inmueble i : r) IO.print(i + "\n");
                        }
                        case 4->{
                            Cliente c = clienteServicio.buscarPorDui(Consola.leerDui("DUI: "));
                            IO.print(c != null ? c + "\n" : "No encontrado.\n");
                        }
                        case 5->{
                            Cliente c = clienteServicio.buscarPorCodigo(Consola.leerTexto("Codigo: "));
                            IO.print(c != null ? c + "\n" : "No encontrado.\n");
                        }
                        case 6->{
                            ArrayList<Cliente> r = clienteServicio.buscarPorNombre(Consola.leerTexto("Nombre: "));
                            if (r.isEmpty()) IO.print("Sin resultados.\n");
                            for (Cliente c : r) IO.print(c + "\n");
                        }
                        default -> IO.print("Opccion invalida.\n");
                    }
                }
                case 4->{
                    
                }
                case 5->{
                    
                }
                case 6->{
                    
                }
                case 7->{
                    
                }
                case 0->{
                    
                }
                default ->{
                    IO.print("Ingres una opccion valida.\n");
                }
            }
        } while (opccion != 0);
    }
}
