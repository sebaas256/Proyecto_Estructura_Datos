package com.mycompany.sistemaalquiler.estructuras;

import java.util.List;
import java.util.function.Function;

/**
 * Algoritmos de busqueda genericos. 'clavePara' indica que campo de cada
 * elemento se compara (por ejemplo Cliente::getDui o Inmueble::getCodigo).
 * Ambos devuelven la posicion del elemento o -1 si no existe.
 */
public class Busqueda {

    // Cantidad de comparaciones de la ultima busqueda (para comparar lineal vs binaria)
    public static int comparaciones;

    /**
     * LINEAL: revisa uno por uno desde el inicio. Funciona con cualquier
     * lista, ordenada o no. O(n).
     */
    public static <T, K> int lineal(List<T> lista, K clave, Function<T, K> clavePara) {
        comparaciones = 0;
        for (int i = 0; i < lista.size(); i++) {
            comparaciones++;
            if (clavePara.apply(lista.get(i)).equals(clave)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * BINARIA: exige la lista ORDENADA por esa misma clave. Compara con el
     * elemento del medio y descarta la mitad donde no puede estar. O(log n).
     */
    public static <T, K extends Comparable<K>> int binaria(List<T> lista, K clave, Function<T, K> clavePara) {
        comparaciones = 0;
        int bajo = 0;
        int alto = lista.size() - 1;
        while (bajo <= alto) {
            int medio = (bajo + alto) / 2;
            comparaciones++;
            int resultado = clavePara.apply(lista.get(medio)).compareTo(clave);
            if (resultado == 0) {
                return medio;
            } else if (resultado < 0) {
                bajo = medio + 1;      // la clave esta en la mitad derecha
            } else {
                alto = medio - 1;      // la clave esta en la mitad izquierda
            }
        }
        return -1;
    }
}
