package com.amulya.books.controller;

import com.amulya.books.entity.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@RestController
//@RequestMapping("/api/book") -applies to each endpoint in the controller
public class BooksController {

    private final List<Book> books = new ArrayList<>();

    public void initialize() {
        books.addAll(List.of(
                new Book("t1", "a1", "c1"),
                new Book("t2", "a2", "c2"),
                new Book("t3", "a3", "c3")

        ));
    }

    public BooksController() {
        initialize();
    }

    @GetMapping
    public String firstAPI() {
//        initialize();
        return "hello world";
    }

    @GetMapping("/api/books")
    public List<Book> secondAPI() {
        return books;
    }

    //    path variables and parameters
    @GetMapping("/api/books/{id}")
    public Book getBookById(@PathVariable int id) {
        return books.get(id);
    }

    //    localhost:8080/api/book/t1
//    path variables -  to identify somethign very specific
    @GetMapping("/api/book/{title}")
    public Book getBookByTitle(@PathVariable String title) {
//        for(Book book:books){
//            if(book.getTitle().equalsIgnoreCase(title)){
//                return book;
//            }
//        }
//        return null;
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }


    //    query parameters -> key value filtering
//    localhost:8080/api/book/?category=c1   -> return complete list of books with c1 category
//    for query params we use @RequestParam annotation and use required = false to declare it as not compulsory in req
//    api/books returns all and api/books/?category=c1 return all  c1 category books
    @GetMapping("/api/book")
    public List<Book> getBookByCategory(@RequestParam(required = false) String category) {
        if (category == null) {
            return books;
        } else {
//            List<Book> result = new ArrayList<>();
//            for(Book b:books){
//                if(b.getCategory().equalsIgnoreCase(category)){
//                    result.add(b);
//                }
//            }
//            return result;
            return books.stream()
                    .filter(book -> book.getCategory().equalsIgnoreCase(category))
                    .toList();
        }
    }

    //    post mapping
    @PostMapping("/api/books")
    public void putBook(@RequestBody Book newBook) {
//        for(Book b :books){
//            if(b.getTitle().equalsIgnoreCase(newBook.getTitle())){
//                return;
//            }
//        }
//        books.add(newBook);
        boolean isNewBook = books.stream()
                .noneMatch(book -> book.getTitle().equalsIgnoreCase(newBook.getTitle()));
        if (isNewBook) {
            books.add(newBook);
        }
    }

//    put request
//pass a path variable to identify the item you are updating and the request body to which it has to be updated
    @PutMapping("/api/books/{title}")
    public void updateBooks(@PathVariable String title, @RequestBody Book updatedBook){
        for(int i =0; i< books.size() ;i++){
            if(books.get(i).getTitle().equalsIgnoreCase(title)){
                books.set(i,updatedBook);
                return;
            }
        }
    }
//delete request -> pass the path variable to identify the record to delete
    @DeleteMapping("/api/books/{title}")
    public void deleteBook(@PathVariable  String title){
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }
}