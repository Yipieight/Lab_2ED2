package org.example;

import java.util.HashMap;
import java.util.Map;

public class ArithmeticCompression {
    private Map<Character, Double> probabilities;

    // Constructor para inicializar las probabilidades
    public ArithmeticCompression(String data) {
        probabilities = new HashMap<>();
        calculateProbabilities(data);
    }

    // Función para calcular las probabilidades de cada carácter en el string
    private void calculateProbabilities(String data) {
        Map<Character, Integer> frequency = new HashMap<>();
        for (char ch : data.toCharArray()) {
            frequency.put(ch, frequency.getOrDefault(ch, 0) + 1);
        }
        int total = data.length();
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            char character = entry.getKey();
            int freq = entry.getValue();
            double probability = (double) freq / total;
            probabilities.put(character, probability);

            // Mensaje de depuración para imprimir las probabilidades calculadas
            System.out.println("[DEBUG] Character: '" + character 
                + "' Frequency: " + freq 
                + " Total: " + total 
                + " => Probability: " + probability);
        }
    }

    // Función para obtener el tamaño comprimido de los datos en bytes usando codificación aritmética
    public double getCompressedSize(String data) {
        double compressedBits = 0.0;

        // Calcular el tamaño comprimido en bits
        for (char ch : data.toCharArray()) {
            if (!probabilities.containsKey(ch)) {
                System.err.println("[DEBUG][ERROR] Character: '" + ch + "' not found in probabilities!");
                continue;
            }

            double probability = probabilities.get(ch);
            double logValue = -Math.log(probability) / Math.log(2); // Bits necesarios para codificar el carácter
            compressedBits += logValue;

            // Mensaje de depuración para imprimir la contribución de cada carácter
            System.out.println("[DEBUG] Character: '" + ch 
                + "', Probability: " + probability 
                + ", -log2(Probability): " + logValue 
                + ", Accumulated Bits: " + compressedBits);
        }

        // Convertir bits a bytes (8 bits por byte) y redondear hacia arriba
        double compressedSizeInBytes = Math.ceil(compressedBits / 8.0);

        // Mensaje de depuración para imprimir el tamaño final comprimido en bytes
        System.out.println("[DEBUG] Final compressed size in bytes: " + compressedSizeInBytes);

        return compressedSizeInBytes;
    }
}
