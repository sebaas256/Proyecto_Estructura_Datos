/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.estructuras;

import java.util.Iterator;

//<T> es un comodin para el tipo de dato en ves de crear muchas listas como clientes, inmuebles... se coloca ese comodin y se usa esta lista generica
//Es como una caja que se puede ocupar con diferentes etiquetas
//Iterable indica que se puede iterar sobre esta lista (for-each)
public class ListaEnlazada <T> implements  Iterable<T>{ 
    private class Nodo{
        T dato;
        Nodo siguiente;
        Nodo(T dato) {this.dato = dato;}
    }
    
    private Nodo cabeza;
    private int tamanio;
    
    public void agregar(T dato){
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null){
            cabeza = nuevo;
        }else{
            Nodo actual = cabeza;
            while(actual.siguiente != null){
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamanio++;
    }
    
    public boolean eliminar(T dato){
        if(cabeza == null) return false;
        if(cabeza.dato.equals(dato)){
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }
        Nodo actual = cabeza;
        while (actual.siguiente != null){
            if (actual.siguiente.dato.equals(dato)){
                actual.siguiente = actual.siguiente.siguiente;
                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
    
    public int tamanio() {return tamanio;}
    public boolean estaVacia(){return cabeza == null; }
    
    @Override
    public Iterator<T> iterator(){
        return new Iterator<T>(){
            private Nodo actual = cabeza;
            public boolean hasNext() {return actual != null;}  //hasNext devuelve true si todavia hay elementos por leer y false si llego al final
            public T next(){
                T dato = actual.dato;
                actual = actual.siguiente;
                return dato;
            }
        };
    }
}
