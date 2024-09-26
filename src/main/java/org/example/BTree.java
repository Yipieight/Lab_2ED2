package org.example;

import java.util.HashMap;

public class BTree {
    private int t; // Grado mínimo del árbol B
    private Node root; // Nodo raíz del árbol B
    private HashMap<String, Book> bookIndex = new HashMap<>();

    // Clase Node
    private class Node {
        int n; // Número de claves en el nodo
        boolean leaf; // Verdadero si el nodo es una hoja
        Book[] keys; // Array de claves
        Node[] children; // Array de hijos

        // Constructor del nodo
        Node(boolean leaf) {
            this.leaf = leaf;
            this.keys = new Book[2 * t - 1];
            this.children = new Node[2 * t];
            this.n = 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < n; i++) {
                sb.append(keys[i].getIsbn());
                if (i < n - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    // Constructor del árbol B
    public BTree(int t) {
        this.t = t;
        this.root = new Node(true);
    }

    // Método de inserción en el árbol B
    public void insert(Book k) {
        bookIndex.put(k.getName(),k);
        long isbn = Long.parseLong(k.getIsbn());
        Node r = root;
        if (r.n == 2 * t - 1) {
            Node s = new Node(false);
            root = s;
            s.children[0] = r;
            splitChild(s, 0, r);
            insertNonFull(s, k);
        } else {
            insertNonFull(r, k);
        }
    }

    // Método auxiliar para insertar en un nodo no lleno
    private void insertNonFull(Node x, Book k) {
        int i = (int) x.n - 1;
        long isbn = Long.parseLong(k.getIsbn());
        if (x.leaf) {
            while (i >= 0 && Long.parseLong(x.keys[i].getIsbn()) > isbn) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = k;
            x.n++;
        } else {
            while (i >= 0 && Long.parseLong(x.keys[i].getIsbn()) > isbn) {
                i--;
            }
            i++;
            if (x.children[i].n == 2 * t - 1) {
                splitChild(x, i, x.children[i]);
                if (Long.parseLong(x.keys[i].getIsbn()) < isbn) {
                    i++;
                }
            }
            insertNonFull(x.children[i], k);
        }
    }

    // Método auxiliar para dividir un hijo
    private void splitChild(Node x, int i, Node y) {
        Node z = new Node(y.leaf);
        z.n = t - 1;
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }
        if (!y.leaf) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
            }
        }
        y.n = t - 1;
        for (int j = (int) x.n; j >= i + 1; j--) {
            x.children[j + 1] = x.children[j];
        }
        x.children[i + 1] = z;
        for (int j = (int) x.n - 1; j >= i; j--) {
            x.keys[j + 1] = x.keys[j];
        }
        x.keys[i] = y.keys[t - 1];
        x.n++;
    }

    // Método de búsqueda por ISBN
    public Book search(String isbn) {
        return search(root, Long.parseLong(isbn));
    }

    private Book search(Node x, long isbn) {
        int i = 0;
        while (i < x.n && Long.parseLong(x.keys[i].getIsbn()) < isbn) {
            i++;
        }
        if (i < x.n && Long.parseLong(x.keys[i].getIsbn()) == isbn) {
            return x.keys[i];
        } else if (x.leaf) {
            return null;
        } else {
            return search(x.children[i], isbn);
        }
    }

    // Método para imprimir el árbol B
    public void printTree() {
        printTree(root, 0, "", true);
    }

    private void printTree(Node x, int level, String prefix, boolean isLast) {
        if (x != null) {
            System.out.print(prefix);
            if (!isLast) {
                System.out.print("  │");
            } else {
                System.out.print("  ");
            }
            System.out.println("Nivel " + level + ": " + x.toString());
            for (int i = 0; i <= x.n; i++) {
                if (!x.leaf) {
                    boolean newIsLast = i == x.n;
                    printTree(x.children[i], level + 1, prefix + (isLast ? "  " : "  │"), newIsLast);
                }
            }
        }
    }

    // Método de actualización
    public void update(String isbn, String field, String newValue) {
        Book book = search(isbn);
        if (book != null) {
            switch (field) {
                case "name":
                    book.setName(newValue);
                case "author":
                    book.setAuthor(newValue);
                    break;
                case "category":
                    book.setCategory(newValue);
                    break;
                case "price":
                    book.setPrice(newValue);
                    break;
                case "quantity":
                    book.setQuantity(newValue);
                    break;
            }
        }
    }

    // Método de eliminación
    public void delete(String isbn) {
        if (root == null) {
            return;
        }
        delete(root, Long.parseLong(isbn));
        if (root.n == 0 && (root.children[0] != null)) {
            if (root.leaf) {
                root = null;
            } else {
                root = root.children[0];
            }
        }

    }

    private void delete(Node x, long isbn) {
        int idx = findKey(x, isbn);
        if (idx < x.n && Long.parseLong(x.keys[idx].getIsbn()) == isbn) {
            if (x.leaf) {
                removeFromLeaf(x, idx);
            } else {
                removeFromNonLeaf(x, idx);
            }
        } else {
            if (x.leaf) {
                return;
            }
            boolean flag = (idx == x.n);
            if (x.children[idx].n < t) {
                fill(x, idx);
            }
            if (flag && idx > x.n) {
                delete(x.children[idx - 1], isbn);
            } else {
                delete(x.children[idx], isbn);
            }
        }
    }

    private int findKey(Node x, long isbn) {
        int idx = 0;
        while (idx < x.n && Long.parseLong(x.keys[idx].getIsbn()) < isbn) {
            idx++;
        }
        return idx;
    }

    private void removeFromLeaf(Node x, int idx) {
        for (int i = idx + 1; i < x.n; i++) {
            x.keys[i - 1] = x.keys[i];
        }
        x.n--;
    }

    private void removeFromNonLeaf(Node x, int idx) {
        Book k = x.keys[idx];
        if (x.children[idx].n >= t) {
            Book pred = getPredecessor(x, idx);
            x.keys[idx] = pred;
            delete(x.children[idx], Long.parseLong(pred.getIsbn()));
        } else if (x.children[idx + 1].n >= t) {
            Book succ = getSuccessor(x, idx);
            x.keys[idx] = succ;
            delete(x.children[idx + 1], Long.parseLong(succ.getIsbn()));
        } else {
            merge(x, idx);
            delete(x.children[idx], Long.parseLong(k.getIsbn()));
        }
    }

    private Book getPredecessor(Node x, int idx) {
        Node current = x.children[idx];
        while (!current.leaf) {
            current = current.children[(int) current.n];
        }
        return current.keys[(int) current.n - 1];
    }

    private Book getSuccessor(Node x, int idx) {
        Node current = x.children[idx + 1];
        while (!current.leaf) {
            current = current.children[0];
        }
        return current.keys[0];
    }

    private void fill(Node x, int idx) {
        if (idx != 0 && x.children[idx - 1].n >= t) {
            borrowFromPrev(x, idx);
        } else if (idx != x.n && x.children[idx + 1].n >= t) {
            borrowFromNext(x, idx);
        } else {
            if (idx != x.n) {
                merge(x, idx);
            } else {
                merge(x, idx - 1);
            }
        }
    }

    private void borrowFromPrev(Node x, int idx) {
        Node child = x.children[idx];
        Node sibling = x.children[idx - 1];
        for (int i = (int) child.n - 1; i >= 0; i--) {
            child.keys[i + 1] = child.keys[i];
        }
        if (!child.leaf) {
            for (int i = (int) child.n; i >= 0; i--) {
                child.children[i + 1] = child.children[i];
            }
        }
        child.keys[0] = x.keys[idx - 1];
        if (!child.leaf) {
            child.children[0] = sibling.children[(int) sibling.n];
        }
        x.keys[idx - 1] = sibling.keys[(int) sibling.n - 1];
        child.n++;
        sibling.n--;
    }

    private void borrowFromNext(Node x, int idx) {
        Node child = x.children[idx];
        Node sibling = x.children[idx + 1];
        child.keys[(int) child.n] = x.keys[idx];
        if (!child.leaf) {
            child.children[(int) child.n + 1] = sibling.children[0];
        }
        x.keys[idx] = sibling.keys[0];
        for (int i = 1; i < sibling.n; i++) {
            sibling.keys[i - 1] = sibling.keys[i];
        }
        if (!sibling.leaf) {
            for (int i = 1; i <= sibling.n; i++) {
                sibling.children[i - 1] = sibling.children[i];
            }
        }
        child.n++;
        sibling.n--;
    }

    private void merge(Node x, int idx) {
        Node child = x.children[idx];
        Node sibling = x.children[idx + 1];
        child.keys[t - 1] = x.keys[idx];
        for (int i = 0; i < sibling.n; i++) {
            child.keys[i + t] = sibling.keys[i];
        }
        if (!child.leaf) {
            for (int i = 0; i <= sibling.n; i++) {
                child.children[i + t] = sibling.children[i];
            }
        }
        for (int i = idx + 1; i < x.n; i++) {
            x.keys[i - 1] = x.keys[i];
        }
        for (int i = idx + 2; i <= x.n; i++) {
            x.children[i - 1] = x.children[i];
        }
        child.n += sibling.n + 1;
        x.n--;
    }

    // Método de búsqueda por nombre
    public Book searchByName(String name) {
        return bookIndex.get(name);
    }

    public void getLenghOfEatchMethod(Book book) {
        int size = addNameSize(book);
        int sizeHuffman = (addNameSizeHuffman(book));
        int sizeArithmetic = addNameSizeArithmetic(book);



        if (size == sizeHuffman && size == sizeArithmetic) {
            Main.Equal++;
        }


        if (size < sizeHuffman && size < sizeArithmetic) {
            Main.Decompress++;
        }


        if (sizeHuffman < size && sizeHuffman < sizeArithmetic) {
            Main.Huffman++;
        }


        if (sizeArithmetic < size && sizeArithmetic < sizeHuffman) {
            Main.Arithmetic++;
        }

        if (sizeArithmetic == sizeHuffman) {
            Main.Huffman++;
        }

    }

    public int addNameSize(Book book) {
        int size = book.getName().length() * 2;
        book.setNamesize(String.valueOf(size));
        return size;
    }

    public int addNameSizeHuffman(Book book) {
        Huffman huffman = new Huffman();
        int size = huffman.codificar(book.getName());
        double result = (double) size / 8;
        book.setNamesizehuffman(String.valueOf(size));
        return (int) Math.ceil(result);
    }

    public int addNameSizeArithmetic(Book book) {
        ArithmeticCompressionInt compressor = new ArithmeticCompressionInt(book.getName());
        var hola = compressor.compress(book.getName());
        int result = (int) (hola.length() / 8);
        book.setNamesizearithmetic(String.valueOf(hola.length()/8));
        return result;
    }

}
