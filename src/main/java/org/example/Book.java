
package org.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {

    @SerializedName("isbn")
    @Expose
    private String isbn;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    //variables de comprension para guardar
    private String namesize;
    private String namesizehuffman;
    private String namesizearithmetic;

    public Book(String isbn) {
        this.isbn = isbn;
    }

    public Book() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    

    public String getNamesize() {
        return namesize;
    }

    public void setNamesize(String namesize) {
        this.namesize = namesize;
    }

    public String getNamesizehuffman() {
        return namesizehuffman;
    }

    public void setNamesizehuffman(String namesizehuffman) {
        this.namesizehuffman = namesizehuffman;
    }

    public String getNamesizearithmetic() {
        return namesizearithmetic;
    }

    public void setNamesizearithmetic(String namesizearithmetic) {
        this.namesizearithmetic = namesizearithmetic;
    }

    @Override
    public String toString() {
        return "Book [isbn=" + isbn + ", name=" + name + ", author=" + author + ", category=" + category + ", price="
                + price + ", quantity=" + quantity + ", namesize=" + namesize + ", namesizehuffman=" + namesizehuffman
                + ", namesizearithmetic=" + namesizearithmetic + "]";
    }

    


}

