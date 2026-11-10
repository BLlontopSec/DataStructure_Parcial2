import java.util.ArrayList;
import java.util.List;

public class ArbolGeneral
{
    public static class NodoGeneral {
        Registro registro;
        List<NodoGeneral> hijos;

        public NodoGeneral(Registro registro) {
            this.registro = registro;
            this.hijos = new ArrayList<>();
        }
    }

    private NodoGeneral raiz;

    public ArbolGeneral() { this.raiz = null;}

    public boolean estaVacio() { return raiz == null; }

    public NodoGeneral getRaiz() { return raiz; }

    // Agregar un registro al árbol general buscando a su padre
    public boolean agregar(Registro nuevoRegistro) {
        if (raiz == null) {
            if (nuevoRegistro.getPadre() == 0) {
                raiz = new NodoGeneral(nuevoRegistro);
                return true;
            }
            return false; // La raíz debe tener padre 0
        }

        NodoGeneral padreNode = buscarNodo(raiz, nuevoRegistro.getPadre());
        if (padreNode != null) {
            padreNode.hijos.add(new NodoGeneral(nuevoRegistro));
            return true;
        }
        return false; // Padre no encontrado
    }

    private NodoGeneral buscarNodo(NodoGeneral actual, int id) {
        if (actual == null) return null;
        if (actual.registro.getId() == id) return actual;

        for (NodoGeneral hijo : actual.hijos) {
            NodoGeneral encontrado = buscarNodo(hijo, id);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    public void preorden(NodoGeneral nodo, List<Integer> resultado) {
        if (nodo == null) return;
        resultado.add(nodo.registro.getId());
        for (NodoGeneral hijo : nodo.hijos)
            preorden(hijo, resultado);
    }

    public void postorden(NodoGeneral nodo, List<Integer> resultado) {
        if (nodo == null) return;
        for (NodoGeneral hijo : nodo.hijos)
            postorden(hijo, resultado);
        resultado.add(nodo.registro.getId());
    }

    public void porNiveles(List<Integer> resultado) {
        if (raiz == null) return;
        List<NodoGeneral> cola = new ArrayList<>();
        cola.add(raiz);

        while (!cola.isEmpty()) {
            NodoGeneral actual = cola.remove(0);
            resultado.add(actual.registro.getId());
            cola.addAll(actual.hijos);
        }
    }

    // --- Altura y Hojas ---
    // Altura = número de aristas del camino más largo hasta una hoja; nodo sin hijos tiene altura 0[cite: 3, 4].
    public int calcularAltura(NodoGeneral nodo) {
        if (nodo == null || nodo.hijos.isEmpty()) {
            return 0;
        }
        int maxAlturaHijo = 0;
        for (NodoGeneral hijo : nodo.hijos) {
            maxAlturaHijo = Math.max(maxAlturaHijo, calcularAltura(hijo));
        }
        return 1 + maxAlturaHijo;
    }

    public int contarHojas(NodoGeneral nodo) {
        if (nodo == null) return 0;
        if (nodo.hijos.isEmpty()) return 1;

        int totalHojas = 0;
        for (NodoGeneral hijo : nodo.hijos) {
            totalHojas += contarHojas(hijo);
        }
        return totalHojas;
    }

    // --- Cancelación en cascada ---
    public int cancelarSubarbol(int id) {
        NodoGeneral objetivo = buscarNodo(raiz, id);
        if (objetivo == null) return 0;
        return cancelarRecursivo(objetivo);
    }

    private int cancelarRecursivo(NodoGeneral nodo) {
        int count = 0;
        // Solo cancela si está pendiente (los atendidos conservan su estado)[cite: 4]
        if ("PENDIENTE".equals(nodo.registro.getEstado())) {
            nodo.registro.setEstado("CANCELADO");
            count++;
        }
        for (NodoGeneral hijo : nodo.hijos) {
            count += cancelarRecursivo(hijo);
        }
        return count;
    }
}