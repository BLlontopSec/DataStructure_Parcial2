import java.util.ArrayList;
import java.util.List;

public class BinaryHeap {
    private ArrayList<Registro> heap;

    public BinaryHeap() {
        this.heap = new ArrayList<>();
    }

    public boolean estaVacio() {
        return heap.isEmpty();
    }

    public int tamano() {
        return heap.size();
    }

    // --- Inserción ---
    public void insertar(Registro registro) {
        heap.add(registro);
        siftUp(heap.size() - 1);
    }

    // --- Consultar Máximo ---
    public Registro consultarMaximo() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    // --- Extraer Máximo ---
    public Registro extraerMaximo() {
        if (heap.isEmpty()) return null;
        Registro maximo = heap.get(0);
        Registro ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            siftDown(0);
        }
        return maximo;
    }

    // --- Build Heap ---
    public void buildHeap(List<Registro> registrosPendientes) {
        heap = new ArrayList<>(registrosPendientes);
        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void siftUp(int i) {
        int padre = (i - 1) / 2;
        while (i > 0 && tieneMayorPrioridad(heap.get(i), heap.get(padre))) {
            intercambiar(i, padre);
            i = padre;
            padre = (i - 1) / 2;
        }
    }

    private void siftDown(int i) {
        int mayor = i;
        int izq = 2 * i + 1;
        int der = 2 * i + 2;

        if (izq < heap.size() && tieneMayorPrioridad(heap.get(izq), heap.get(mayor))) {
            mayor = izq;
        }
        if (der < heap.size() && tieneMayorPrioridad(heap.get(der), heap.get(mayor))) {
            mayor = der;
        }
        if (mayor != i) {
            intercambiar(i, mayor);
            siftDown(mayor);
        }
    }

    // Criterio de prioridad: mayor prioridad numérica; ante empate, menor ID
    public boolean tieneMayorPrioridad(Registro a, Registro b) {
        if (a.getPrioridad() != b.getPrioridad()) {
            return a.getPrioridad() > b.getPrioridad();
        }
        return a.getId() < b.getId();
    }

    private void intercambiar(int i, int j) {
        Registro temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}