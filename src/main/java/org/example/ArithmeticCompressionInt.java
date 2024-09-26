package org.example;

import java.util.*;

public class ArithmeticCompressionInt {
    public Map<Character, int[]> probabilities = new HashMap<>();
    public String source;

    private static final int NUMBER_BITS = 16;
    private static final int DEFAULT_LOW = 0;
    private static final int DEFAULT_HIGH = 0xFFFF;
    private static final int MSD = 0x8000;
    private static final int SSD = 0x4000;
    private static final int TOO = 0x3FFF;

    private int scale;

    public ArithmeticCompressionInt(String source) {
        this.source = source;
        calculateProbabilities();
    }

    public ArithmeticCompressionInt(Map<Character, int[]> probabilities, int scale) {
        this.probabilities.putAll(probabilities);
        this.scale = scale;
    }

    private void calculateProbabilities() {
        Map<Character, Integer> frequencies = new HashMap<>();

        for (char symbol : source.toCharArray()) {
            frequencies.put(symbol, frequencies.getOrDefault(symbol, 0) + 1);
        }

        List<Map.Entry<Character, Integer>> sortedFreqs = new ArrayList<>(frequencies.entrySet());
        sortedFreqs.sort((a, b) -> {
            int cmp = a.getValue().compareTo(b.getValue());
            return cmp != 0 ? cmp : Character.compare(a.getKey(), b.getKey());
        });

        scale = source.length();
        int low = 0;

        for (Map.Entry<Character, Integer> symbol : sortedFreqs) {
            int high = low + symbol.getValue();
            probabilities.put(symbol.getKey(), new int[]{low, high});
            low = high;
        }
    }

    public String compress(String input) {
        StringBuilder outputStream = new StringBuilder();
        int low = DEFAULT_LOW;
        int high = DEFAULT_HIGH;
        long underflowBits = 0;

        for (char symbol : input.toCharArray()) {
            long range = (long) (high - low) + 1;
            high = (int) (low + (range * probabilities.get(symbol)[1]) / scale - 1);
            low = (int) (low + (range * probabilities.get(symbol)[0]) / scale);

            while (true) {
                if ((high & MSD) == (low & MSD)) {
                    boolean bit = (high & MSD) != 0;
                    outputStream.append(bit ? "1" : "0");
                    while (underflowBits > 0) {
                        bit = (high & MSD) == 0;
                        outputStream.append(bit ? "1" : "0");
                        underflowBits--;
                    }
                } else {
                    if ((low & SSD) != 0 && (high & SSD) == 0) {
                        underflowBits++;
                        low &= TOO;
                        high |= SSD;
                    } else {
                        break;
                    }
                }
                low <<= 1;
                high <<= 1;
                high |= 1;
                low &= 0xFFFF;
                high &= 0xFFFF;
            }
        }

        boolean finalBit = (low & SSD) != 0;
        outputStream.append(finalBit ? "1" : "0");
        underflowBits++;
        while (underflowBits > 0) {
            boolean bit = (low & SSD) == 0;
            outputStream.append(bit ? "1" : "0");
            underflowBits--;
        }

        if (outputStream.length() % 8 != 0) {
            outputStream.append("0".repeat(8 - outputStream.length() % 8));
        }

        return outputStream.toString();
    }

    public String decompress(String input, int size) throws RuntimeException {
        StringBuilder retval = new StringBuilder();
        int code = 0;
        int low = DEFAULT_LOW;
        int high = DEFAULT_HIGH;

        for (int i = 0; i < NUMBER_BITS; i++) {
            code <<= 1;
            code |= input.charAt(0) == '1' ? 1 : 0;
            input = input.substring(1);
        }

        for (int i = 0; i < size; i++) {
            long range = (long) (high - low) + 1;
            int scaledValue = (int) (((long)(code - low + 1) * scale - 1) / range);

            char c = '\0';
            for (Map.Entry<Character, int[]> symbol : probabilities.entrySet()) {
                if (scaledValue >= symbol.getValue()[0] && scaledValue < symbol.getValue()[1]) {
                    c = symbol.getKey();
                    break;
                }
            }

            if (c == '\0') throw new RuntimeException("Decoding Error");

            retval.append(c);

            range = (long) (high - low) + 1;
            high = (int) (low + (range * probabilities.get(c)[1]) / scale - 1);
            low = (int) (low + (range * probabilities.get(c)[0]) / scale);

            while (true) {
                if ((high & MSD) == (low & MSD)) {
                    // Shift out the most significant bit
                } else {
                    if ((low & SSD) == SSD && (high & SSD) == 0) {
                        code ^= SSD;
                        low &= TOO;
                        high |= SSD;
                    } else {
                        break;
                    }
                }
                low <<= 1;
                high <<= 1;
                high |= 1;
                code <<= 1;
                low &= 0xFFFF;
                high &= 0xFFFF;
                code &= 0xFFFF;
                if (input.length() == 0) break;
                code |= input.charAt(0) == '1' ? 1 : 0;
                input = input.substring(1);
            }
        }
        return retval.toString();
    }

    
}


