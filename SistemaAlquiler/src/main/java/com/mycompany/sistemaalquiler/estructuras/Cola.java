package com.mycompany.sistemaalquiler.estructuras;

import java.util.Iterator;

/**
 * Cola generica (FIFO: First In, First Out).
 * El primero que entra es el primero que sale, como la fila de un banco.
 * Se implementa con nodos enlazados: 'frente' es por donde sale el elemento
 * y 'fin' es por donde entra el nuevo.
 */
public class Cola<T> implements Iterable<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;
        Nodo(T dato) { this.dato = dato; }
    }

    private Nodo frente;   // proximo en salir
    private Nodo fin;      // ultimo en entrar
    private int tamanio;

    // Agrega al final de la cola
    public void encolar(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    // Saca y devuelve el elemento del frente (null si esta vacia)
    public T desencolar() {
        if (frente == null) return null;
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return dato;
    }

    // Mira el frente sin sacarlo
    public T verFrente() { return frente == null ? null : frente.dato; }

    public boolean estaVacia() { return frente == null; }

    public int tamanio() { return tamanio; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo actual = frente;
            public boolean hasNext() { return actual != null; }
            public T next() {
                T dato = actual.dato;
                actual = actual.siguiente;
                return dato;
            }
        };
    }
}
