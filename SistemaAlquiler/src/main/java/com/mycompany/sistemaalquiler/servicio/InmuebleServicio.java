package com.mycompany.sistemaalquiler.servicio;

import com.mycompany.sistemaalquiler.datos.InmueblesDAO;
import com.mycompany.sistemaalquiler.datos.TipoInmuebleDAO;
import com.mycompany.sistemaalquiler.estructuras.Busqueda;
import com.mycompany.sistemaalquiler.estructuras.ListaEnlazada;
import com.mycompany.sistemaalquiler.estructuras.Ordenamiento;
import com.mycompany.sistemaalquiler.modelo.Inmueble;
import com.mycompany.sistemaalquiler.modelo.TipoInmueble;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InmuebleServicio {

    private final InmueblesDAO dao = new InmueblesDAO();
    private final TipoInmuebleDAO tipoDAO = new TipoInmuebleDAO();
    private final ListaEnlazada<Inmueble> inmuebles = new ListaEnlazada<>();
    private final List<TipoInmueble> tipos;
    private int siguienteCodigo = 1;

    public InmuebleServicio() {
        tipos = tipoDAO.listar();
        for (Inmueble i : dao.listar()) {
            inmuebles.agregar(i);
            if (i.getId() >= siguienteCodigo) {
                siguienteCodigo = i.getId() + 1;
            }
        }
    }

    public String generarCodigo() {
        return String.format("INM-%04d", siguienteCodigo);
    }

    //Tipos
    public List<TipoInmueble> getTipos() {
        return tipos;
    }

    public boolean existeTipo(int id) {
        for (TipoInmueble t : tipos) {
            if (t.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public String nombreTipo(int id) {
        for (TipoInmueble t : tipos) {
            if (t.getId() == id) {
                return t.getNombre();
            }
        }
        return "Desconocido";
    }

    //Registro
    public boolean registrar(Inmueble i) {
        if (buscarPorCodigo(i.getCodigo()) != null) {
            IO.print("Ya existe un inmueble con ese código.\n");
            return false;
        }
        if (dao.insertar(i)) {
            inmuebles.agregar(i);
            siguienteCodigo++;
            return true;
        }
        return false;
    }

    // Busqueda (Recorren las listas enlazadas)
    public Inmueble buscarPorCodigo(String codigo) {
        for (Inmueble i : inmuebles) {
            if (i.getCodigo().equalsIgnoreCase(codigo)) {
                return i;
            }
        }
        return null;
    }

    public ArrayList<Inmueble> buscarPorNombre(String texto) {
        ArrayList<Inmueble> resultados = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(i);
            }
        }
        return resultados;
    }

    public ArrayList<Inmueble> buscarPorTipo(int tipoId) {
        ArrayList<Inmueble> resultados = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getTipo_inmueble_id() == tipoId) {
                resultados.add(i);
            }
        }
        return resultados;
    }

    //Catalogo
    public ArrayList<Inmueble> disponibles() {
        ArrayList<Inmueble> resultados = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            if (i.getEstado().equals("Disponible")) {
                resultados.add(i);
            }
        }
        return resultados;
    }

    // Inmuebles alquilados que se liberan dentro de los próximos 'dias' días
    public ArrayList<Inmueble> proximosADisponerse(int dias) {
        ArrayList<Inmueble> resultados = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        for (Inmueble i : inmuebles) {
            LocalDate f = i.getFechaDisponible();
            if (i.getEstado().equals("Alquilado") && f != null) {
                long faltan = ChronoUnit.DAYS.between(hoy, f);
                if (faltan >= 0 && faltan <= dias) {
                    resultados.add(i);
                }
            }
        }
        return resultados;
    }

    public ListaEnlazada<Inmueble> getInmuebles() {
        return inmuebles;
    }

    public boolean actualizarEstadoInmueble(String codigo, String nuevoEstado, LocalDate fechaDisponible) {
        Inmueble inm = buscarPorCodigo(codigo);
        if (inm == null || nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return false;
        }
        String estadoLimpio = nuevoEstado.trim();
        boolean actualizado = dao.actualizarEstado(inm.getId(), estadoLimpio, fechaDisponible);
        if (actualizado) {
            inm.setEstado(estadoLimpio);
            inm.setFechaDisponible(fechaDisponible);
        }
        return actualizado;
    }

    public ArrayList<Inmueble> obtenerCatalogoCompleto() {
        return inmuebles.aArrayList();
    }

    // BUSQUEDA BINARIA por codigo: ordena una copia por codigo y luego divide el rango a la mitad
    public Inmueble buscarPorCodigoBinaria(String codigo) {
        ArrayList<Inmueble> ordenados = inmuebles.aArrayList();
        Ordenamiento.insercion(ordenados, Comparator.comparing(Inmueble::getCodigo));
        int posicion = Busqueda.binaria(ordenados, codigo.trim().toUpperCase(), Inmueble::getCodigo);
        return posicion >= 0 ? ordenados.get(posicion) : null;
    }

    // Ordena la lista recibida segun el criterio y devuelve el nombre del algoritmo usado
    // criterio: 1 = precio mensual, 2 = nombre, 3 = capacidad
    public String ordenarCatalogo(ArrayList<Inmueble> lista, int criterio) {
        switch (criterio) {
            case 1 -> {
                Ordenamiento.seleccion(lista, Comparator.comparingDouble(Inmueble::getPrecio_mensual));
                return "seleccion";
            }
            case 2 -> {
                Ordenamiento.insercion(lista, Comparator.comparing(Inmueble::getNombre, String.CASE_INSENSITIVE_ORDER));
                return "insercion";
            }
            default -> {
                Ordenamiento.burbuja(lista, Comparator.comparingInt(Inmueble::getCapacidad));
                return "burbuja";
            }
        }
    }

    public boolean marcarComoAlquilado(String codigo, LocalDate fechaFinContrato) {
        return actualizarEstadoInmueble(codigo, "Alquilado", fechaFinContrato);
    }

    public boolean liberarInmueble(String codigo) {
        return actualizarEstadoInmueble(codigo, "Disponible", null);
    }

}
