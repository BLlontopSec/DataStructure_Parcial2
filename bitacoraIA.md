# Bitácora de Uso de Inteligencia Artificial
**Curso:** Estructuras de Datos (2026-2) · **Equipo 7:** Adrien Roho, Bryan Llontop, Diego Castillo  
**Proyecto:** Examen 02 (ABP-IA) — Contexto I.2: Simulación de partículas

Todo el proyecto (código, casos de prueba, README e informe) se realizó con IA.

---

## 1. Herramientas de IA utilizadas
* **Gemini (Google):** análisis de requerimientos, diseño de la arquitectura de datos, dudas sobre rotaciones AVL y primera versión del código y de los casos A–F.
* **Claude (Claude Code, Anthropic):** lectura del enunciado, revisión y ejecución de los casos, corrección de errores de codificación, creación de los casos faltantes, `README.md`, `pruebas.txt` e informe.

---

## 2. Ejemplos de solicitudes (prompts)

1. *(Gemini)* "Necesito implementar un Árbol AVL en Java 21 para un sistema de simulación de partículas donde las claves son IDs enteros. Explícame cómo estructurar el nodo y los métodos de actualización de altura usando `left` y `right`."
2. *(Gemini)* "Mi método `extraerMaximo` en el Max-Heap no desempata cuando dos tareas tienen la misma prioridad. ¿Cómo ajusto la condición para que salga primero el de menor ID?"
3. *(Claude)* "Verifica por ti mismo si los casos parecen funcionales, yo tengo un duda." Luego: "Redacta los casos faltantes, pruébalos y dime si todo va bien. Si hay un problema de codificación, arréglalo."

---

## 3. Respuestas verificadas y corregidas

**Caso 1 – Cancelación (corregida por el equipo).**
* **Respuesta de la IA:** al implementar `CANCELAR id`, sugirió eliminar físicamente los nodos cancelados del árbol general, del BST y del AVL.
* **Verificación:** al revisar la regla (f) del enunciado, la sugerencia resultó **incorrecta**: los registros cancelados o atendidos deben permanecer en BST y AVL como historial para que `CONSULTAR id` devuelva su estado real. La cancelación solo cambia el estado a `CANCELADO` en los descendientes que siguen `PENDIENTE`, y el heap se reconstruye (`buildHeap`) solo con los pendientes.
* **Comprobación:** en el Caso A, `CONSULTAR 20` tras cancelar el subárbol sigue mostrando el registro con su estado.

**Caso 2 – Casos de prueba (errores detectados al revisar con Claude).**
* **Hallazgo:** al ejecutar los casos, Claude detectó que (a) `CasoA.txt` y `CasoB.txt` tenían un BOM UTF-8 que hacía fallar el primer comando, (b) `CasoA.txt` no coincidía con el enunciado (prioridad 3 en vez de 2 y otros nombres) y (c) `CasoC`, `CasoD_*`, `CasoE` y `CasoF` estaban vacíos.
* **Corrección y comprobación:** se quitó el BOM y `Main` pasó a leer en UTF-8; se escribieron los casos exactos del enunciado. Se reejecutaron los nueve casos y sus salidas coinciden con lo esperado (ver `pruebas.txt`). Además, una prueba aleatoria de 5000 inserciones y eliminaciones comprobó que el AVL se mantiene balanceado.
