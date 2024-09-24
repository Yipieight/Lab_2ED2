
# Sistema de Gestión de Inventarios con Compresión de Datos

## Descripción del Proyecto

Este proyecto forma parte del curso de **Estructura de Datos II**, y tiene como objetivo implementar soluciones de compresión de datos utilizando **Codificación Huffman** y **Compresión Aritmética** para optimizar el almacenamiento y transmisión de los registros de inventario de la librería "Libros y Más". Dado el crecimiento del inventario y la expansión a nuevas sucursales, es necesario reducir los costos asociados con el almacenamiento y la transmisión de estos datos.

## Objetivos

- Implementar la Codificación Huffman para comprimir el campo **"Nombre del libro"** en el inventario.
- Aplicar la Compresión Aritmética en el mismo campo para comparar su eficiencia.
- Optimizar el uso del espacio en almacenamiento sin comprometer la integridad ni accesibilidad de los datos.

## Estructura del Código

El proyecto se organiza en varias clases, cada una encargada de una función específica:

### Clases Principales

1. **`Book.java`**:
   - Define los atributos de un libro, como `isbn`, `name`, `author`, `category`, `price` y `quantity`.

2. **`BTree.java`**:
   - Implementa un **Árbol B** para organizar los registros del inventario y optimizar las búsquedas y actualizaciones de manera eficiente.

3. **`ArithmeticCompression.java`**:
   - Implementa el algoritmo de **Compresión Aritmética**, que comprime el campo "Nombre del libro" basado en la frecuencia de aparición de los caracteres.

4. **`huffman.java`**:
   - Implementa la **Codificación Huffman**, que construye un árbol binario para representar los caracteres según su frecuencia de aparición, asignando códigos binarios más cortos a los caracteres más frecuentes.

5. **`Main.java`**:
   - Punto de entrada del programa, desde donde se ejecutan las operaciones de inserción, actualización, eliminación y búsqueda en el inventario, además de aplicar los algoritmos de compresión.

## Algoritmos Implementados

### Codificación Huffman

La **Codificación Huffman** se utiliza para comprimir el campo **Nombre del libro**. Se basa en una **cola de prioridad** para construir el árbol de Huffman, donde los símbolos con menor frecuencia de aparición se colocan más profundamente en el árbol. Las características principales de la implementación son:

- Los símbolos con menor probabilidad se priorizan.
- En caso de que dos símbolos tengan la misma probabilidad, se sigue el orden natural de los símbolos.

### Compresión Aritmética

La **Compresión Aritmética** también se aplica sobre el campo **Nombre del libro**. Este algoritmo permite comprimir secuencias de caracteres basándose en la probabilidad de aparición de los símbolos, manteniendo un rango continuo de valores.

#### Detalles del Algoritmo:

- Los símbolos se ordenan por probabilidad.
- Se utilizan enteros de precisión infinita y un rango de valores entre 0 y 65,535 (0xffff) para realizar los cálculos de compresión.
- Se utilizan las variables `low` y `high` para definir los rangos de codificación.

## Estructura de Datos Utilizada

- **Árbol B**: Utilizado para organizar los registros de inventario de manera eficiente, mejorando el tiempo de búsqueda y actualización de datos.
- **Cola de Prioridad**: Utilizada para construir el árbol de Huffman.
- **Rangos Numéricos**: Utilizados en la compresión aritmética para mantener los rangos de compresión sin pérdida de precisión.

## Funcionalidades

### Operaciones sobre el Inventario

El sistema permite realizar las siguientes operaciones sobre el inventario:

- **INSERT**: Inserta un nuevo libro en el inventario.
- **PATCH**: Actualiza la información de un libro existente.
- **DELETE**: Elimina un libro del inventario.
- **SEARCH**: Permite buscar un libro en el inventario.

### Pruebas

El sistema debe ser probado utilizando un archivo CSV que contenga registros en formato JSON. Cada operación de búsqueda debe devolver la información del libro encontrado, incluyendo los tamaños originales y comprimidos del campo "Nombre del libro" utilizando Huffman y Compresión Aritmética.

#### Ejemplo de Bitácora de Entrada

```
INSERT; {"isbn":"1234567890","name":"Cien Anos de Soledad","author":"Gabriel Garcia Marquez","category":"Ficcion","price":"20.00","quantity":"10"}
INSERT; {"isbn":"0987654321","name":"El Principito","author":"Antoine de Saint-Exupery","category":"Ficcion","price":"15.00","quantity":"5"}
PATCH; {"isbn":"0987654321","author":"Antoine de Saint-Exupery","price":"18.00"}
DELETE; {"isbn":"1234567890"}
```

#### Ejemplo de Bitácora de Salida

```
{"isbn":"1122334455","name":"Don Quijote de la Mancha","author":"Miguel de Cervantes","category":"Clasicos","price":"25.00","quantity":"7","namesize":"48","namesizehuffman":"93","namesizearithmetic":"12"}
{"isbn":"0987654321","name":"El Principito","author":"Antoine de Saint-Exupery","category":"Ficcion","price":"18.00","quantity":"5","namesize":"26","namesizehuffman":"44","namesizearithmetic":"6"}
Equal: 0
Decompress: 0
Huffman: 2
Arithmetic: 0
```

## Preguntas y Respuestas Basadas en Pruebas

1. **¿Cuál es el mejor algoritmo para comprimir estos datos?**
   La Codificación Huffman tiende a ser más eficiente para textos donde hay una distribución clara de frecuencias en los caracteres.

2. **¿Qué cambios se deberían hacer en el algoritmo menos eficiente?**
   En el caso de que la Compresión Aritmética sea menos eficiente, se pueden ajustar las probabilidades de los símbolos para que se adapten mejor a la distribución de los datos.

3. **¿Cómo afectaron estos nuevos requerimientos al sistema original del Lab01?**
   Los nuevos requerimientos de compresión añaden complejidad, pero la reducción en los costos de almacenamiento y transmisión justifica los cambios realizados.

4. **¿Qué recomendaciones harías para mejorar los algoritmos?**
   Se recomienda mejorar el manejo de símbolos repetidos y ajustar los cálculos de precisión en la Compresión Aritmética para obtener mejores resultados.

## Conclusión

El sistema desarrollado no solo optimiza el almacenamiento de datos del inventario de la librería "Libros y Más", sino que también demuestra la eficacia de los algoritmos de compresión vistos en clase. La correcta implementación de la **Codificación Huffman** y la **Compresión Aritmética** reduce significativamente los costos de almacenamiento y transmisión, manteniendo la integridad de los datos.

