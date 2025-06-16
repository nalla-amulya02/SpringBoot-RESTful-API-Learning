package com.amulya.books.controller;

import com.amulya.books.entity.Book;
import com.amulya.books.entity.request.BookRequest;
import com.amulya.books.exception.BookErrorResponse;
import com.amulya.books.exception.BookNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@RestController
//@RequestMapping("/api/book") -applies to each endpoint in the controller
public class BooksController {

    private final List<Book> books = new ArrayList<>();

    public void initialize() {
        books.addAll(List.of(
                new Book(1,"t1", "a1", "c1",3),
                new Book(2,"t2", "a2", "c2",4),
                new Book(3,"t3", "a3", "c3",2),
                new Book(4,"t4", "a3", "c1",3)


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
    @GetMapping("/api/books/{id}")    // index
    public Book getBookById(@PathVariable int id) {
        return books.get(id);
    }

    //    localhost:8080/api/book/t1
//    path variables -  to identify something very specific -old
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



//    creating a new bookrequest DTO and get book by id
    @GetMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.OK)
public Book getBookByBookId(@PathVariable @Min(value = 1) int id){
        return books.stream()
                .filter(book -> book.getId()==id)
                .findFirst()
//                .orElse(null);
                .orElseThrow(
                        () -> new BookNotFoundException("book not found at given id" + id)
                );
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

    //    post mapping -old
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/books")
    public void putBookByBookRequest(@Valid @RequestBody BookRequest bookreq){    //new with DTO
        long id = books.isEmpty()?1:(books.get(books.size()-1).getId()+1);
        books.add(convertToBook(id,bookreq));
    }


//    put request
//pass a path variable to identify the item you are updating and the request body to which it has to be updated
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/books/{title}")
    public void deleteBook(@PathVariable @Size(min = 1,max = 30) String title){
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }



    private Book convertToBook(long id, BookRequest book){
        return new Book(id,book.getTitle(), book.getAuthor(), book.getCategory(),book.getRating());
    }

    @ExceptionHandler
    public ResponseEntity<BookErrorResponse> handlingMet(Exception excep){
        BookErrorResponse bookErrorResponse = new BookErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                excep.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(bookErrorResponse,HttpStatus.NOT_FOUND);
    }
}