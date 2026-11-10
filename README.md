# Examen 02 – Estructuras de Datos (2026-2)

**Árboles, BST, AVL, Heaps y Heap-Sort** – Universidad Nacional de Colombia, Facultad de Ingeniería

## Equipo 7

- Adrien Roho
- Bryan Llontop
- Diego Castillo

Repositorio (opcional): <https://github.com/BLlontopSec/DataStructure_Parcial2>

## Aviso sobre el uso de inteligencia artificial

**Todo el proyecto fue realizado con inteligencia artificial**: el código Java, los casos de prueba, el
archivo `pruebas.txt`, este README y el informe fueron generados con herramientas de IA (Gemini y Claude).
El detalle de las herramientas, tres ejemplos de solicitudes y una respuesta de la IA que fue verificada y
corregida se encuentra en [`bitacoraIA.md`](bitacoraIA.md).

## Contexto elegido

**I.2 – Simulación de partículas.** Cada registro es una tarea de análisis de una partícula:

| Campo | Significado |
|---|---|
| `id` | Entero positivo y único |
| `nombre` | Texto breve, sin espacios |
| `prioridad` | De 1 a 10; 10 es la más urgente (urgencia de análisis) |
| `padre` | ID del registro padre; `0` indica la raíz (hay una sola) |
| `estado` | `PENDIENTE`, `ATENDIDO` o `CANCELADO` |

Desempate: primero mayor prioridad; si empatan, menor ID.

## Versión de Java

**Java 21.** El código compila con `javac --release 21`.

## Estructura del proyecto

```
.
├── src/
│   ├── Main.java           Programa principal: lee los comandos e integra las estructuras
│   ├── Registro.java       Registro (id, nombre, prioridad, padre, estado)
│   ├── ArbolGeneral.java   Árbol general: recorridos, altura, hojas, cancelarSubarbol
│   ├── BST.java            Árbol binario de búsqueda (clave = id)
│   ├── AVL.java            Árbol AVL con rotaciones LL, RR, LR y RL
│   ├── BinaryHeap.java     Max-heap binario (insertar, consultarMaximo, extraerMaximo, buildHeap)
│   └── HeapSort.java       Heap-Sort sobre una copia de los registros pendientes
├── pruebas/                Entradas de los casos obligatorios de la sección 5
│   ├── CasoA.txt           Integración completa
│   ├── CasoB.txt           Empates de prioridad
│   ├── CasoC.txt           Errores de registro
│   ├── CasoD_LL.txt        Rotaciones AVL (LL, RR, LR, RL)
│   ├── CasoD_RR.txt
│   ├── CasoD_LR.txt
│   ├── CasoD_RL.txt
│   ├── CasoE.txt           BST frente a AVL (1..7 y eliminar la clave 4)
│   └── CasoF.txt           Heap y Heap-Sort
├── README.md               Este archivo
├── pruebas.txt             Entrada, salida obtenida y conclusión de los casos A–F
├── informe.pdf             Informe (máximo 4 páginas)
└── bitacoraIA.md           Bitácora de uso de IA
```

Al compilar se crea la carpeta `out/` con los `.class`; no forma parte de la entrega.

## Estructuras y dónde se usan

| Estructura | Archivo | Para qué se usa |
|---|---|---|
| Árbol general | `ArbolGeneral.java` | Jerarquía padre–hijo, recorridos y cancelación en cascada |
| BST | `BST.java` | Índice por ID sin balanceo (se compara con el AVL) |
| AVL | `AVL.java` | Índice por ID balanceado; responde `CONSULTAR` |
| Max-heap | `BinaryHeap.java` | Cola de atención; responde `SIGUIENTE` |
| Heap-Sort | `HeapSort.java` | Ordena una copia de los pendientes para `REPORTE` |

Los cuatro índices guardan referencias al mismo objeto `Registro`, de modo que un cambio de estado se ve
en todos a la vez. Los registros atendidos o cancelados permanecen en el BST y en el AVL como historial.

## Cómo compilar

Desde la raíz del proyecto:

```
javac -encoding UTF-8 -d out src/*.java
```

## Cómo ejecutar

Modo interactivo (se escriben los comandos uno por línea; finaliza con `Ctrl+Z` y Enter en Windows o
`Ctrl+D` en Linux/macOS):

```
java -cp out Main
```

Modo archivo (la entrada se toma de un archivo de comandos):

```
java -cp out Main < pruebas/CasoA.txt
```

Para ejecutar todos los casos en PowerShell:

```
foreach ($f in Get-ChildItem pruebas\Caso*.txt) { "=== $($f.Name) ==="; Get-Content $f | java -cp out Main }
```

## Comandos (uno por línea)

| Comando | Qué hace |
|---|---|
| `REGISTRAR id nombre prioridad padre` | Agrega un registro al árbol general, BST, AVL y heap |
| `CONSULTAR id` | Busca por ID en el AVL y muestra datos y estado |
| `SIGUIENTE` | Atiende el pendiente de mayor prioridad (o muestra `SIN PENDIENTES`) |
| `CANCELAR id` | Cancela los pendientes del subárbol y actualiza el heap |
| `REPORTE` | Lista los pendientes en orden de atención usando Heap-Sort |
| `ESTADISTICAS` | Recorridos, altura y hojas del árbol general; alturas de BST y AVL; raíz e inordenes; pendientes |
| `ELIMINAR id` | Borrado explícito solo en BST y AVL (usado en el Caso E) |

Las líneas vacías y las que empiezan por `#` se ignoran. `REGISTRAR` rechaza (sin modificar ninguna
estructura): ID duplicado, padre inexistente, segunda raíz y prioridad fuera de 1–10. `CONSULTAR` y
`CANCELAR` de un ID inexistente muestran un error.

## Ejemplo corto de uso

Entrada (`pruebas/CasoA.txt`):

```
REGISTRAR 10 A 2 0
REGISTRAR 20 B 5 10
REGISTRAR 30 C 3 10
CONSULTAR 20
SIGUIENTE
CANCELAR 10
REPORTE
ESTADISTICAS
```

Salida:

```
OK: registro 10
OK: registro 20
OK: registro 30
ID=20 NOMBRE B PRIORIDAD=5 PADRE 10 ESTADO PENDIENTE
ATENDIDO: 20 (B)
CANCELADOS: 2

Preorden: 10,20,30
Postorden: 20,30,10
Por niveles: 10,20,30
Altura arbol general: 1
Hojas: 2
Altura BST: 2
Altura AVL: 1
Raiz AVL: 20
Inorden BST: 10,20,30
Inorden AVL: 10,20,30
AVL balanceado: true
Pendientes: 0
```

La línea vacía es el `REPORTE` sin pendientes.

## Casos de prueba

Cada caso obligatorio tiene su archivo de entrada en `pruebas/`. En [`pruebas.txt`](pruebas.txt) están la
entrada, la salida obtenida y si coincide con lo esperado.

| Caso | Archivo(s) | Qué verifica |
|---|---|---|
| A | `CasoA.txt` | Integración completa de las seis operaciones |
| B | `CasoB.txt` | Desempate por menor ID |
| C | `CasoC.txt` | ID duplicado y padre inexistente |
| D | `CasoD_LL.txt`, `CasoD_RR.txt`, `CasoD_LR.txt`, `CasoD_RL.txt` | Rotaciones AVL; raíz final 20 |
| E | `CasoE.txt` | BST altura 6 frente a AVL altura 2; eliminar la clave 4 |
| F | `CasoF.txt` | Heap con prioridades `[4,10,3,5,1,8,9,2]` y Heap-Sort |

## Convenciones

- Altura = número de aristas del camino más largo hasta una hoja (árbol vacío = −1, hoja = 0).
- Al eliminar un nodo con dos hijos en BST y AVL se usa el **sucesor** (menor clave del subárbol derecho).
- No se implementa detección de ciclos: un registro nuevo tiene un ID inexistente y se cuelga de un padre ya
  existente, por lo que nunca se forma un ciclo.
