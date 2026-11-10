import java.util.ArrayList;
import java.util.List;

public class HeapSort {

    public static List<Registro> ordenar(List<Registro> pendientes) {
        List<Registro> copia = new ArrayList<>(pendientes);
        int n = copia.size();
        if (n <= 1) return copia;

        // Construir max-heap sobre la copia
        for (int i = (n / 2) - 1; i >= 0; i--) {
            siftDown(copia, n, i);
        }

        // Extraer elementos uno a uno
        for (int i = n - 1; i > 0; i--) {
            Registro temp = copia.get(0);
            copia.set(0, copia.get(i));
            copia.set(i, temp);

            siftDown(copia, i, 0);
        }

        // Invertir para que quede en orden de atención (mayor prioridad primero)
        List<Registro> resultadoOrdenAtencion = new ArrayList<>();
        for (int i = copia.size() - 1; i >= 0; i--) {
            resultadoOrdenAtencion.add(copia.get(i));
        }
        return resultadoOrdenAtencion;
    }

    private static void siftDown(List<Registro> arr, int n, int i) {
        int mayor = i;
        int izq = 2 * i + 1;
        int der = 2 * i + 2;

        if (izq < n && tieneMayorPrioridad(arr.get(izq), arr.get(mayor))) {
            mayor = izq;
        }
        if (der < n && tieneMayorPrioridad(arr.get(der), arr.get(mayor))) {
            mayor = der;
        }
        if (mayor != i) {
            Registro swap = arr.get(i);
            arr.set(i, arr.get(mayor));
            arr.set(mayor, swap);
            siftDown(arr, n, mayor);
        }
    }

    private static boolean tieneMayorPrioridad(Registro a, Registro b) {
        if (a.getPrioridad() != b.getPrioridad()) {
            return a.getPrioridad() > b.getPrioridad();
        }
        return a.getId() < b.getId();
    }
}