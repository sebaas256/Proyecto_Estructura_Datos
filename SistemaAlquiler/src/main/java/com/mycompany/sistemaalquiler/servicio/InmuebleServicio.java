package com.mycompany.sistemaalquiler.servicio;

import com.mycompany.sistemaalquiler.datos.InmueblesDAO;
import com.mycompany.sistemaalquiler.datos.TipoInmuebleDAO;
import com.mycompany.sistemaalquiler.estructuras.ListaEnlazada;
import com.mycompany.sistemaalquiler.modelo.Inmueble;
import com.mycompany.sistemaalquiler.modelo.TipoInmueble;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        ArrayList<Inmueble> lista = new ArrayList<>();
        for (Inmueble i : inmuebles) {
            lista.add(i);
        }
        return lista;
    }

    public boolean marcarComoAlquilado(String codigo, LocalDate fechaFinContrato) {
        return actualizarEstadoInmueble(codigo, "Alquilado", fechaFinContrato);
    }

    public boolean liberarInmueble(String codigo) {
        return actualizarEstadoInmueble(codigo, "Disponible", null);
    }

}
