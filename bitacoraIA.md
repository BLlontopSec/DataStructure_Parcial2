# Bitácora de Uso de Inteligencia Artificial
**Curso:** Estructuras de Datos (2026-2)  
**Proyecto:** Examen 02 (ABP-IA) — Contexto: Simulación de partículas  

---

## 1. Herramientas de IA Utilizadas
* **Gemini (Google):** Utilizado para el análisis de requerimientos del proyecto, diseño de la arquitectura modular de datos, resolución de dudas conceptuales sobre rotaciones AVL y estructuración de los casos de prueba obligatorios A-F.

---

## 2. Ejemplos de Solicitudes (Prompts) Realizadas

### Solicitud 1 (Diseño de Estructuras):
> *"Necesito implementar un Árbol AVL en Java 21 para un sistema de simulación de partículas donde las claves son IDs de tipo entero. Explícame cómo estructurar el nodo y los métodos de actualización de altura usando las variables `left` y `right`."*

### Solicitud 2 (Depuración y Corrección):
> *"Mi método `extraerMaximo` en el Max-Heap binario no está desempatando correctamente cuando dos tareas de partículas tienen la misma prioridad de urgencia. ¿Cómo debo ajustar la condición para que el de menor ID salga primero?"*

### Solicitud 3 (Diseño de Casos de Prueba):
> *"Genera la secuencia de comandos para probar el caso A (integración completa) considerando el contexto de simulación de partículas, donde la raíz sea un Análisis General y sus hijos sean FiltroElectrones y FiltroQuarks."*

---

## 3. Respuesta Verificada y Corregida

* **Respuesta recibida por la IA:**  
  Durante la implementación de la operación `CANCELAR id`, la IA sugirió eliminar físicamente los nodos cancelados tanto del Árbol General como del BST y del AVL.

* **Verificación y Corrección realizada por el estudiante:**  
  Al revisar las reglas del examen (específicamente la **Regla f** y las observaciones del Anexo A), identifiqué que la sugerencia de la IA era **incorrecta**. Las reglas establecen que:
  1. Los registros cancelados o atendidos **deben permanecer** en el BST y en el AVL como historial para que la operación `CONSULTAR id` siga funcionando y retorne su estado real.
  2. La cancelación en el Árbol General solo debe cambiar el estado del atributo `estado` a `"CANCELADO"` en los nodos dependientes que sigan `"PENDIENTE".
  3. El Max-Heap de atención es la única estructura que se reconstruye (`buildHeap`) filtrando los registros pendientes para evitar que las tareas canceladas sean atendidas posteriormente.

* **Comprobación:**  
  Implementé la lógica conservando los nodos en el BST/AVL y ejecuté el **Caso A**. Al realizar `CONSULTAR 20` después de cancelar el subárbol, el programa retornó exitosamente la información del registro mostrando su nuevo estado `CANCELADO`, tal como lo exige la rúbrica.