package org.example;

import com.google.gson.Gson;

import java.io.*;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Main {

    public static int Equal = 0;
    public static int Decompress = 0;
    public static int Huffman = 0;
    public static int Arithmetic = 0;

    public static void main(String[] args) throws IOException {

        BTree bTree = new BTree(2); // Crear un árbol B de ordenamiento 5
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        //Aqui se agrega el archivo de Book.csv
        BufferedReader r = new BufferedReader(new FileReader("100Klab01_books.csv"));
        //Aqui se agrega el archivo de Search.csv
        BufferedReader br = new BufferedReader(new FileReader("100Klab01_search.csv"));

        String jsonLine = "";
        File archivo = new File("Output_Jose_Garcia_Lab2.txt");
        PrintWriter archivotxt = new PrintWriter(archivo);

        while ((jsonLine = r.readLine()) != null) {
            String[] parts = jsonLine.split(";");
            String action = parts[0];
            String jsonData = parts[1];

            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

            switch (action) {
                case "INSERT":
                    Book book = gson.fromJson(jsonObject, Book.class);
                    bTree.insert(book);
                    break;

                case "PATCH":

                    String isbnToPatch = jsonObject.get("isbn").getAsString();

                    if (jsonObject.has("name")) {
                        bTree.update(isbnToPatch, "name", jsonObject.get("name").getAsString());
                    }

                    if (jsonObject.has("author")) {
                        bTree.update(isbnToPatch, "author", jsonObject.get("author").getAsString());
                    }
                    if (jsonObject.has("category")) {
                        bTree.update(isbnToPatch, "category", jsonObject.get("category").getAsString());
                    }
                    if (jsonObject.has("price")) {
                        bTree.update(isbnToPatch, "price", jsonObject.get("price").getAsString());
                    }
                    if (jsonObject.has("quantity")) {
                        bTree.update(isbnToPatch, "quantity", jsonObject.get("quantity").getAsString());
                    }
                    break;

                case "DELETE":
                    try {
                        String isbnToDelete = isbnToDelete = jsonObject.get("isbn").getAsString();
                        bTree.delete(isbnToDelete);
                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        break;
                    }
            }
        }
        while ((jsonLine = br.readLine()) != null) {
            String[] parts = jsonLine.split(";");
            String action = parts[0];
            String jsonData = parts[1];

            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

            switch (action) {
                case "SEARCH":
                    String nameToSearch = jsonObject.get("name").getAsString();
                    Book book1 = bTree.searchByName(nameToSearch);
                    if (book1 == null) {
                        continue;
                    }
                    bTree.getLenghOfEatchMethod(book1);
                    archivotxt.println(gson.toJson(book1));
            }
        }

        archivotxt.println("Equal: " + Equal);
        archivotxt.println("Decompress: "+Decompress);
        archivotxt.println("Huffman: "+Huffman);
        archivotxt.println("Arithmetic: "+Arithmetic);

        archivotxt.close();
        bTree.printTree();

    }
}
