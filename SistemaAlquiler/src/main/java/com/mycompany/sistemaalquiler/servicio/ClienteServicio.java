/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.servicio;

import com.mycompany.sistemaalquiler.datos.ClienteDAO;
import com.mycompany.sistemaalquiler.estructuras.Busqueda;
import com.mycompany.sistemaalquiler.estructuras.ListaEnlazada;
import com.mycompany.sistemaalquiler.estructuras.Ordenamiento;
import com.mycompany.sistemaalquiler.estructuras.Pila;
import com.mycompany.sistemaalquiler.modelo.Cliente;
import java.util.ArrayList;
import java.util.Comparator;

public class ClienteServicio {
    private final ClienteDAO dao = new ClienteDAO();
    private final ListaEnlazada<Cliente> clientes = new ListaEnlazada<>();
    private final Pila<Cliente> historial = new Pila<>();
    private int siguienteCodigo = 1;
    
    public ClienteServicio(){
        cargarDesdeBD();
    }
    
    private void cargarDesdeBD(){
        for (Cliente c: dao.listar()){
            clientes.agregar(c);
            if (c.getId() >= siguienteCodigo){
                siguienteCodigo = c.getId() +1;
            }
        }
    }
    
    public String generarCodigo(){
        return String.format("CLI-%04d", siguienteCodigo);
    }
    
    //Registrar: valida duplicado -> guarda en bd -> agrega a lista -> y a la pila
    public boolean registrar(Cliente c){
        if(buscarPorDui(c.getDui()) != null){
            IO.print("Ya existe un cliente con ese DUI.\n");
            return false;
        }
        if (dao.insertar(c)){
            clientes.agregar(c);
            historial.push(c);
            siguienteCodigo++;
            return true;
        }
        return false;
    }
    
    //Deshacer: saca el ultimo registro de la pila y lo borra de la BD y lista
    public Cliente deshacerUltimo(){
        if (historial.estaVacia()) return null;
        Cliente ultimo = historial.peek();
        if (dao.eliminar(ultimo.getId())){
            historial.pop();
            clientes.eliminar(ultimo);
            return ultimo;
        }
        return null; //retornar null como por ejemplo cuando tenga facturas pendientes(no se pudo)
    }
    
    //Busquedas(recorren la lista enlazada)
    public Cliente buscarPorDui(String dui){
        for (Cliente c: clientes){
            if(c.getDui().equals(dui)) return c;
        }
        return null;
    }
    
    public Cliente buscarPorCodigo(String codigo){
        for (Cliente c : clientes){
            if(c.getCodigo().equalsIgnoreCase(codigo)) return c;
        }
        return null;
    }
    
    public ArrayList<Cliente> buscarPorNombre(String texto){
        ArrayList<Cliente> resultados = new ArrayList<>();
        for (Cliente c: clientes){
            if(c.getNombre().toLowerCase().contains(texto.toLowerCase())){
                resultados.add(c);
            }
        }
        return resultados;
    }
    
    //Busqueda binaria por DUI: ordena una copia por DUI y divide el rango a la mitad
    public Cliente buscarPorDuiBinaria(String dui){
        ArrayList<Cliente> ordenados = clientes.aArrayList();
        Ordenamiento.insercion(ordenados, Comparator.comparing(Cliente::getDui));
        int posicion = Busqueda.binaria(ordenados, dui.trim(), Cliente::getDui);
        return posicion >= 0 ? ordenados.get(posicion) : null;
    }
    
    //Listado de clientes ordenado alfabeticamente por nombre y apellido (burbuja)
    public ArrayList<Cliente> listarOrdenadosPorNombre(){
        ArrayList<Cliente> lista = clientes.aArrayList();
        Ordenamiento.burbuja(lista, Comparator.comparing(
                (Cliente c) -> c.getNombre() + " " + c.getApellido(), String.CASE_INSENSITIVE_ORDER));
        return lista;
    }
    
    //Ultimo cliente registrado en esta sesion (tope de la pila) sin deshacerlo
    public Cliente ultimoRegistrado(){
        return historial.peek();
    }
    
    public ListaEnlazada<Cliente> getClientes() {return clientes;}
}
