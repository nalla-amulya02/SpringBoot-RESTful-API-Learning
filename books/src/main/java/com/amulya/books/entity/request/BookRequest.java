package com.amulya.books.entity.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class BookRequest {
    @Size(min = 1, max = 30,message = "title os between 1 and 30 characters")
    private String title;

    @Size(min = 1,max = 20)
    private String author;

    @Size(min = 1,max = 10)
    private String category;

    @Min(value = 1,message = "rating must be atleast 1")
    @Max(value = 5)
    private int rating;

    public BookRequest(int rating, String category, String author, String title) {
        this.rating = rating;
        this.category = category;
        this.author = author;
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String cateogry) {
        this.category = cateogry;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
