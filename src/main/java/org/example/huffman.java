package org.example;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;

class Huffman {

    // Nodo para el árbol de Huffman
    static class Nodo implements Comparable<Nodo> {
        char caracter;
        int frecuencia;
        Nodo izquierda, derecha;

        public Nodo(char caracter, int frecuencia) {
            this.caracter = caracter;
            this.frecuencia = frecuencia;
            this.izquierda = null;
            this.derecha = null;
        }

        public Nodo(int frecuencia, Nodo izquierda, Nodo derecha) {
            this.caracter = '\0'; // Nodo interno
            this.frecuencia = frecuencia;
            this.izquierda = izquierda;
            this.derecha = derecha;
        }

        @Override
        public int compareTo(Nodo otro) {
            return this.frecuencia - otro.frecuencia;
        }
    }

    // Construir el árbol de Huffman
    public Nodo construirArbolHuffman(Map<Character, Integer> frecuencias) {
        PriorityQueue<Nodo> cola = new PriorityQueue<>();

        // Añadir nodos con frecuencias a la cola
        for (Map.Entry<Character, Integer> entry : frecuencias.entrySet()) {
            cola.add(new Nodo(entry.getKey(), entry.getValue()));
        }

        // Combinar nodos hasta que quede uno
        while (cola.size() > 1) {
            Nodo izquierda = cola.poll();
            Nodo derecha = cola.poll();
            Nodo nuevoNodo = new Nodo(izquierda.frecuencia + derecha.frecuencia, izquierda, derecha);
            cola.add(nuevoNodo);
        }

        return cola.poll(); // Raíz del árbol de Huffman
    }

    // Generar los códigos Huffman a partir del árbol
    public void generarCodigos(Nodo raiz, String codigo, Map<Character, String> codigoHuffman) {
        if (raiz == null) {
            return;
        }

        // Si es un nodo hoja, asignar el código
        if (raiz.izquierda == null && raiz.derecha == null) {
            codigoHuffman.put(raiz.caracter, codigo);
        }

        // Recorrer subárbol izquierdo y derecho
        generarCodigos(raiz.izquierda, codigo + "0", codigoHuffman);
        generarCodigos(raiz.derecha, codigo + "1", codigoHuffman);
    }

    // Método para codificar una frase utilizando Huffman
    public int codificar(String frase) {
        // Paso 1: Calcular las frecuencias de los caracteres
        Map<Character, Integer> frecuencias = new HashMap<>();
        for (char c : frase.toCharArray()) {
            frecuencias.put(c, frecuencias.getOrDefault(c, 0) + 1);
        }

        // Paso 2: Construir el árbol de Huffman
        Nodo raiz = construirArbolHuffman(frecuencias);

        // Paso 3: Generar los códigos Huffman
        Map<Character, String> codigoHuffman = new HashMap<>();
        generarCodigos(raiz, "", codigoHuffman);

        // Mostrar los códigos generados
        System.out.println("Códigos Huffman para cada carácter:");
        for (Map.Entry<Character, String> entry : codigoHuffman.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Paso 4: Codificar la frase
        StringBuilder fraseCodificada = new StringBuilder();
        for (char c : frase.toCharArray()) {
            fraseCodificada.append(codigoHuffman.get(c));
        }

        System.out.println("Frase codificada en bits: " + fraseCodificada);
        System.out.println("Número total de bits: " + fraseCodificada.length());
        return fraseCodificada.length();
    }
}

