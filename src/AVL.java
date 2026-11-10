import java.util.List;

public class AVL {

    public static class NodoAVL {
        Registro registro;
        NodoAVL left, right;
        int height;

        public NodoAVL(Registro registro) {
            this.registro = registro;
            this.left = null;
            this.right = null;
            this.height = 0; // Hoja tiene altura 0 según convención
        }
    }

    private NodoAVL raiz;

    public AVL() {
        this.raiz = null;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public NodoAVL getRaiz() {
        return raiz;
    }

    // --- Altura auxiliar ---
    private int height(NodoAVL nodo) {
        return (nodo == null) ? -1 : nodo.height; // Árbol vacío = -1
    }

    private int getBalance(NodoAVL nodo) {
        if (nodo == null) return 0;
        return height(nodo.left) - height(nodo.right); // Altura izquierda - altura derecha
    }

    private void actualizarAltura(NodoAVL nodo) {
        if (nodo != null) {
            nodo.height = 1 + Math.max(height(nodo.left), height(nodo.right));
        }
    }

    // --- Rotaciones ---

    // Rotación Simple a la Derecha (LL)
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.left;
        NodoAVL T2 = x.right;

        // Realizar rotación
        x.right = y;
        y.left = T2;

        // Actualizar alturas
        actualizarAltura(y);
        actualizarAltura(x);

        return x; // Nueva raíz
    }

    // Rotación Simple a la Izquierda (RR)
    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.right;
        NodoAVL T2 = y.left;

        // Realizar rotación
        y.left = x;
        x.right = T2;

        // Actualizar alturas
        actualizarAltura(x);
        actualizarAltura(y);

        return y; // Nueva raíz
    }

    // --- Inserción ---
    public void insertar(Registro registro) {
        raiz = insertarRecursivo(raiz, registro);
    }

    private NodoAVL insertarRecursivo(NodoAVL actual, Registro registro) {
        // 1. Inserción normal de BST
        if (actual == null) {
            return new NodoAVL(registro);
        }

        if (registro.getId() < actual.registro.getId()) {
            actual.left = insertarRecursivo(actual.left, registro);
        } else if (registro.getId() > actual.registro.getId()) {
            actual.right = insertarRecursivo(actual.right, registro);
        } else {
            return actual; // No se admiten claves repetidas
        }

        // 2. Actualizar altura del nodo actual
        actualizarAltura(actual);

        // 3. Obtener el factor de balance para verificar si se desbalanceó
        int balance = getBalance(actual);

        // --- Casos de Desbalanceo ---

        // Caso LL (Left-Left)
        if (balance > 1 && registro.getId() < actual.left.registro.getId()) {
            return rotacionDerecha(actual);
        }

        // Caso RR (Right-Right)
        if (balance < -1 && registro.getId() > actual.right.registro.getId()) {
            return rotacionIzquierda(actual);
        }

        // Caso LR (Left-Right)
        if (balance > 1 && registro.getId() > actual.left.registro.getId()) {
            actual.left = rotacionIzquierda(actual.left);
            return rotacionDerecha(actual);
        }

        // Caso RL (Right-Left)
        if (balance < -1 && registro.getId() < actual.right.registro.getId()) {
            actual.right = rotacionDerecha(actual.right);
            return rotacionIzquierda(actual);
        }

        return actual; // Nodo sin cambios
    }

    // --- Búsqueda ---
    public Registro buscar(int id) {
        NodoAVL resultado = buscarRecursivo(raiz, id);
        return (resultado != null) ? resultado.registro : null;
    }

    private NodoAVL buscarRecursivo(NodoAVL actual, int id) {
        if (actual == null || actual.registro.getId() == id) {
            return actual;
        }
        if (id < actual.registro.getId()) {
            return buscarRecursivo(actual.left, id);
        }
        return buscarRecursivo(actual.right, id);
    }

    // --- Recorrido Inorden ---
    public void inorden(NodoAVL actual, List<Registro> resultado) {
        if (actual != null) {
            inorden(actual.left, resultado);
            resultado.add(actual.registro);
            inorden(actual.right, resultado);
        }
    }

    // --- Altura pública ---
    public int altura() {
        return height(raiz);
    }

    // --- Eliminación ---
    public void eliminar(int id) {
        raiz = eliminarRecursivo(raiz, id);
    }

    private NodoAVL eliminarRecursivo(NodoAVL actual, int id) {
        if (actual == null) return null;

        if (id < actual.registro.getId()) {
            actual.left = eliminarRecursivo(actual.left, id);
        } else if (id > actual.registro.getId()) {
            actual.right = eliminarRecursivo(actual.right, id);
        } else {
            // Nodo con un solo hijo o sin hijos
            if (actual.left == null) return actual.right;
            else if (actual.right == null) return actual.left;

            // Nodo con dos hijos: obtener el sucesor (menor del subárbol derecho)
            NodoAVL sucesor = encontrarMinNodo(actual.right);
            actual.registro = sucesor.registro;
            actual.right = eliminarRecursivo(actual.right, sucesor.registro.getId());
        }

        if (actual == null) return null;

        // Actualizar altura
        actualizarAltura(actual);

        // Obtener factor de balance
        int balance = getBalance(actual);

        // Balancear el árbol tras la eliminación
        // LL
        if (balance > 1 && getBalance(actual.left) >= 0) {
            return rotacionDerecha(actual);
        }
        // LR
        if (balance > 1 && getBalance(actual.left) < 0) {
            actual.left = rotacionIzquierda(actual.left);
            return rotacionDerecha(actual);
        }
        // RR
        if (balance < -1 && getBalance(actual.right) <= 0) {
            return rotacionIzquierda(actual);
        }
        // RL
        if (balance < -1 && getBalance(actual.right) > 0) {
            actual.right = rotacionDerecha(actual.right);
            return rotacionIzquierda(actual);
        }

        return actual;
    }

    private NodoAVL encontrarMinNodo(NodoAVL nodo) {
        NodoAVL actual = nodo;
        while (actual.left != null) {
            actual = actual.left;
        }
        return actual;
    }

    // Comprueba que cada nodo tenga factor de balance en [-1, 1] y alturas consistentes
    public boolean estaBalanceado() {
        return verificarBalance(raiz);
    }

    private boolean verificarBalance(NodoAVL nodo) {
        if (nodo == null) return true;
        if (Math.abs(getBalance(nodo)) > 1) return false;
        if (nodo.height != 1 + Math.max(height(nodo.left), height(nodo.right))) return false;
        return verificarBalance(nodo.left) && verificarBalance(nodo.right);
    }
}
