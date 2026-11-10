import java.util.List;

public class BST {

    public static class NodoBST {
        Registro registro;
        NodoBST left, right;

        public NodoBST(Registro registro) {
            this.registro = registro;
            this.left = null;
            this.right = null;
        }
    }

    protected NodoBST raiz;

    public BST() { this.raiz = null; }

    public boolean estaVacio() { return raiz == null; }

    public void insertar(Registro registro) { raiz = insertarRecursivo(raiz, registro); }

    private NodoBST insertarRecursivo(NodoBST actual, Registro registro) {
        if (actual == null)
            return new NodoBST(registro);
        if (registro.getId() < actual.registro.getId())
            actual.left = insertarRecursivo(actual.left, registro);
        else if (registro.getId() > actual.registro.getId())
            actual.right = insertarRecursivo(actual.right, registro);

        return actual;
    }

    public Registro buscar(int id) {
        NodoBST resultado = buscarRecursivo(raiz, id);
        return (resultado != null) ? resultado.registro : null;
    }

    private NodoBST buscarRecursivo(NodoBST actual, int id) {
        if (actual == null || actual.registro.getId() == id)
            return actual;
        if (id < actual.registro.getId())
            return buscarRecursivo(actual.left, id);

        return buscarRecursivo(actual.right, id);
    }

    public void inorden(NodoBST actual, List<Registro> resultado) {
        if (actual != null) {
            inorden(actual.left, resultado);
            resultado.add(actual.registro);
            inorden(actual.right, resultado);
        }
    }

    // Árbol vacío = altura -1; hoja = altura 0[cite: 4].
    public int altura() { return calcularAltura(raiz);}

    protected int calcularAltura(NodoBST nodo) {
        if (nodo == null)
            return -1;
        int alturaIzq = calcularAltura(nodo.left);
        int alturaDer = calcularAltura(nodo.right);
        return 1 + Math.max(alturaIzq, alturaDer);
    }

    public void eliminar(int id) { raiz = eliminarRecursivo(raiz, id);}

    private NodoBST eliminarRecursivo(NodoBST actual, int id) {
        if (actual == null) return null;

        if (id < actual.registro.getId())
            actual.left = eliminarRecursivo(actual.left, id);
        else if (id > actual.registro.getId())
            actual.right = eliminarRecursivo(actual.right, id);
        else {
            // Nodo con un solo hijo o sin hijos
            if (actual.left == null) return actual.right;
            else if (actual.right == null) return actual.left;
            // Nodo con dos hijos: se usa el sucesor (menor del subárbol derecho)
            actual.registro = encontrarMin(actual.right);
            actual.right = eliminarRecursivo(actual.right, actual.registro.getId());
        }
        return actual;
    }

    private Registro encontrarMin(NodoBST nodo) {
        Registro min = nodo.registro;
        while (nodo.left != null) {
            min = nodo.left.registro;
            nodo = nodo.left;
        }
        return min;
    }
}