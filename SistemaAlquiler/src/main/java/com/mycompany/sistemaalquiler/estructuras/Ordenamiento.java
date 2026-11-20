package com.mycompany.sistemaalquiler.estructuras;

import java.util.Comparator;
import java.util.List;

/**
 * Algoritmos de ordenamiento implementados a mano (sin usar Collections.sort).
 * Son genericos: sirven para clientes, inmuebles, ventas, etc. El criterio
 * de orden se entrega con un Comparator, y todos ordenan la lista "en sitio".
 */
public class Ordenamiento {

    // Cantidad de comparaciones del ultimo ordenamiento (para explicarlo en la defensa)
    public static long comparaciones;

    private static <T> void intercambiar(List<T> lista, int i, int j) {
        T temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }

    /**
     * BURBUJA: compara pares vecinos y los intercambia si estan al reves.
     * En cada vuelta el mayor "sube" al final. Si una vuelta completa no
     * intercambia nada, la lista ya esta ordenada y se corta. O(n^2).
     */
    public static <T> void burbuja(List<T> lista, Comparator<T> cmp) {
        comparaciones = 0;
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            boolean huboCambio = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparaciones++;
                if (cmp.compare(lista.get(j), lista.get(j + 1)) > 0) {
                    intercambiar(lista, j, j + 1);
                    huboCambio = true;
                }
            }
            if (!huboCambio) break;
        }
    }

    /**
     * SELECCION: busca el menor de la parte desordenada y lo coloca en su
     * posicion definitiva. Hace pocos intercambios (maximo n-1). O(n^2).
     */
    public static <T> void seleccion(List<T> lista, Comparator<T> cmp) {
        comparaciones = 0;
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < n; j++) {
                comparaciones++;
                if (cmp.compare(lista.get(j), lista.get(menor)) < 0) {
                    menor = j;
                }
            }
            if (menor != i) {
                intercambiar(lista, i, menor);
            }
        }
    }

    /**
     * INSERCION: toma cada elemento y lo inserta en su lugar dentro de la
     * parte ya ordenada, corriendo los mayores un espacio a la derecha
     * (como ordenar cartas en la mano). Muy buena si ya viene casi ordenada.
     */
    public static <T> void insercion(List<T> lista, Comparator<T> cmp) {
        comparaciones = 0;
        for (int i = 1; i < lista.size(); i++) {
            T clave = lista.get(i);
            int j = i - 1;
            while (j >= 0) {
                comparaciones++;
                if (cmp.compare(lista.get(j), clave) > 0) {
                    lista.set(j + 1, lista.get(j));
                    j--;
                } else {
                    break;
                }
            }
            lista.set(j + 1, clave);
        }
    }
}
