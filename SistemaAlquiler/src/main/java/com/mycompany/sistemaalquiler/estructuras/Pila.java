/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.estructuras;

public class Pila <T>{
    private class Nodo{
        T dato;
        Nodo abajo;
        Nodo(T dato) {this.dato = dato; }
    }
    
    private Nodo tope;
    private int tamanio;
    
    public void push(T dato){
        Nodo nuevo = new Nodo(dato);
        nuevo.abajo = tope;
        tope = nuevo;
        tamanio++;
    }
    
    public T pop(){
        if(tope == null) return null;
        T dato = tope.dato;
        tope = tope.abajo;
        tamanio--;
        return dato;
    }
    
    public T peek() {return tope == null ? null : tope.dato; }
    public boolean estaVacia() {return tope == null; }
    public int tamanio(){return tamanio;
    }
}
