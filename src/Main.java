import java.util.ArrayList;
import java.util.List;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) {
        ArbolGeneral arbolGeneral = new ArbolGeneral();
        BST bst = new BST();
        AVL avl = new AVL();
        BinaryHeap heap = new BinaryHeap();
        // Entrada y salida en UTF-8 para evitar problemas de codificacion (BOM, tildes)
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().replace("﻿", "").trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue;

            String[] partes = linea.split("\\s+");
            String comando = partes[0].toUpperCase();

            try {
                switch (comando) {
                    case "REGISTRAR":
                        if (partes.length < 5) {
                            System.out.println("ERROR: formato de registro incompleto.");
                            break;
                        }
                        int id = Integer.parseInt(partes[1]);
                        String nombre = partes[2];
                        int prioridad = Integer.parseInt(partes[3]);
                        int padre = Integer.parseInt(partes[4]);

                        // Validaciones de errores obligatorias
                        if (prioridad < 1 || prioridad > 10) {
                            System.out.println("ERROR: prioridad invalida (" + prioridad + ")");
                            break;
                        }
                        if (bst.buscar(id) != null) {
                            System.out.println("ERROR: ID duplicado (" + id + ")");
                            break;
                        }
                        if (padre == 0 && !arbolGeneral.estaVacio()) {
                            System.out.println("ERROR: ya existe una raiz (" + arbolGeneral.getRaiz().registro.getId() + ")");
                            break;
                        }
                        if (padre != 0 && bst.buscar(padre) == null) {
                            System.out.println("ERROR: el padre " + padre + " no existe");
                            break;
                        }

                        Registro nuevo = new Registro(id, nombre, prioridad, padre);
                        boolean agregadoArbol = arbolGeneral.agregar(nuevo);
                        if (agregadoArbol) {
                            bst.insertar(nuevo);
                            avl.insertar(nuevo);
                            heap.insertar(nuevo);
                            System.out.println("OK: registro " + id);
                        } else {
                            System.out.println("ERROR: no se pudo agregar al árbol general.");
                        }
                        break;

                    case "CONSULTAR":
                        if (partes.length < 2) {
                            System.out.println("ERROR: falta el ID a consultar.");
                            break;
                        }
                        int idConsultar = Integer.parseInt(partes[1]);
                        Registro regConsultado = avl.buscar(idConsultar);
                        if (regConsultado == null) {
                            System.out.println("ERROR: el id " + idConsultar + " no existe");
                        } else {
                            System.out.println("ID=" + regConsultado.getId() + " NOMBRE " + regConsultado.getNombre() +
                                    " PRIORIDAD=" + regConsultado.getPrioridad() +
                                    " PADRE " + regConsultado.getPadre() + " ESTADO " + regConsultado.getEstado());
                        }
                        break;

                    case "SIGUIENTE":
                        Registro siguienteReg = null;
                        while (!heap.estaVacio()) {
                            Registro candidato = heap.extraerMaximo();
                            if ("PENDIENTE".equals(candidato.getEstado())) {
                                siguienteReg = candidato;
                                break;
                            }
                        }
                        if (siguienteReg == null) {
                            System.out.println("SIN PENDIENTES");
                        } else {
                            siguienteReg.setEstado("ATENDIDO");
                            System.out.println("ATENDIDO: " + siguienteReg.getId() + " (" + siguienteReg.getNombre() + ")");
                        }
                        break;

                    case "CANCELAR":
                        if (partes.length < 2) {
                            System.out.println("ERROR: falta el ID a cancelar.");
                            break;
                        }
                        int idCancelar = Integer.parseInt(partes[1]);
                        Registro regCancelar = bst.buscar(idCancelar);
                        if (regCancelar == null) {
                            System.out.println("ERROR: el id " + idCancelar + " no existe");
                            break;
                        }
                        int canceladosCount = arbolGeneral.cancelarSubarbol(idCancelar);
                        reconstruirHeap(arbolGeneral, heap);
                        System.out.println("CANCELADOS: " + canceladosCount);
                        break;

                    case "ELIMINAR":
                        // Borrado explicito en BST y AVL (Caso E); el historial del resto se conserva
                        if (partes.length < 2) {
                            System.out.println("ERROR: falta el ID a eliminar.");
                            break;
                        }
                        int idEliminar = Integer.parseInt(partes[1]);
                        bst.eliminar(idEliminar);
                        avl.eliminar(idEliminar);
                        System.out.println("ELIMINADO de BST y AVL: " + idEliminar +
                                " (BST lo encuentra: " + (bst.buscar(idEliminar) != null) +
                                ", AVL lo encuentra: " + (avl.buscar(idEliminar) != null) + ")");
                        break;

                    case "REPORTE":
                        List<Registro> pendientes = obtenerPendientes(arbolGeneral);
                        List<Registro> ordenados = HeapSort.ordenar(pendientes);
                        for (Registro r : ordenados) {
                            System.out.print(r.getId() + "(" + r.getPrioridad() + ") ");
                        }
                        System.out.println();
                        break;

                    case "ESTADISTICAS":
                        List<Integer> pre = new ArrayList<>();
                        arbolGeneral.preorden(arbolGeneral.getRaiz(), pre);
                        List<Integer> post = new ArrayList<>();
                        arbolGeneral.postorden(arbolGeneral.getRaiz(), post);
                        List<Integer> niveles = new ArrayList<>();
                        arbolGeneral.porNiveles(niveles);

                        System.out.println("Preorden: " + listToString(pre));
                        System.out.println("Postorden: " + listToString(post));
                        System.out.println("Por niveles: " + listToString(niveles));
                        System.out.println("Altura arbol general: " + arbolGeneral.calcularAltura(arbolGeneral.getRaiz()));
                        System.out.println("Hojas: " + arbolGeneral.contarHojas(arbolGeneral.getRaiz()));
                        System.out.println("Altura BST: " + bst.altura());
                        System.out.println("Altura AVL: " + avl.altura());
                        System.out.println("Raiz AVL: " + (avl.estaVacio() ? "-" : avl.getRaiz().registro.getId()));
                        List<Registro> inBst = new ArrayList<>();
                        bst.inorden(bst.raiz, inBst);
                        List<Registro> inAvl = new ArrayList<>();
                        avl.inorden(avl.getRaiz(), inAvl);
                        System.out.println("Inorden BST: " + registrosToString(inBst));
                        System.out.println("Inorden AVL: " + registrosToString(inAvl));
                        System.out.println("AVL balanceado: " + avl.estaBalanceado());
                        System.out.println("Pendientes: " + obtenerPendientes(arbolGeneral).size());
                        break;

                    default:
                        System.out.println("ERROR: comando desconocido " + comando);
                }
            } catch (Exception e) {
                System.out.println("ERROR en la ejecución del comando: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static List<Registro> obtenerPendientes(ArbolGeneral arbol) {
        List<Registro> pendientes = new ArrayList<>();
        if (!arbol.estaVacio()) {
            recolectarPendientesRecursivo(arbol.getRaiz(), pendientes);
        }
        return pendientes;
    }

    private static void recolectarPendientesRecursivo(ArbolGeneral.NodoGeneral nodo, List<Registro> lista) {
        if (nodo == null) return;
        if ("PENDIENTE".equals(nodo.registro.getEstado())) {
            lista.add(nodo.registro);
        }
        for (ArbolGeneral.NodoGeneral hijo : nodo.hijos) {
            recolectarPendientesRecursivo(hijo, lista);
        }
    }

    private static void reconstruirHeap(ArbolGeneral arbol, BinaryHeap heap) {
        List<Registro> pendientes = obtenerPendientes(arbol);
        heap.buildHeap(pendientes);
    }

    private static String registrosToString(List<Registro> lista) {
        List<Integer> ids = new ArrayList<>();
        for (Registro r : lista) ids.add(r.getId());
        return listToString(ids);
    }

    private static String listToString(List<Integer> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i));
            if (i < lista.size() - 1) sb.append(",");
        }
        return sb.toString();
    }
}