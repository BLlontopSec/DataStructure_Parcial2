public class Registro
{
    private int id;
    private String nombre;
    private int prioridad; // 1 a 10 (10 es más urgente)
    private int padre;     // 0 indica raíz
    private String estado; // "PENDIENTE", "ATENDIDO", "CANCELADO"

    public Registro(int id, String nombre, int prioridad, int padre) {
        this.id = id;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.padre = padre;
        this.estado = "PENDIENTE"; //Todo registro inicia como pendiente
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    public int getPadre() { return padre; }
    public void setPadre(int padre) { this.padre = padre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return id + "(" + prioridad + ")";
    }
}