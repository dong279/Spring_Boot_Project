package com.rookies6.myspringbootlab.controller;

import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {
    private final BookRepository bookRepository;

    @PostMapping
    public Book createbook(@RequestBody Book bookDetails){
        return bookRepository.save(bookDetails);
    }

    @GetMapping
    public List<Book>getBook(){
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book ResponseEntity(@PathVariable Long id){
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book existBook = optionalBook.orElseThrow(
                ()-> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND)
        );
        return existBook;
    }

    @GetMapping("/isbn/{isbn}/")
    public Book getBookIsbn(@PathVariable String isbn){
        return bookRepository.findByIsbn(isbn).orElseThrow(
                ()-> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND)
        );
    }

    @PutMapping("/{id}")
    public Book putBookId(@PathVariable Long id, @RequestBody Book bookDetails){
        Book existBook = bookRepository.findById(id).orElseThrow(
                ()-> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND)
        );

        existBook.setTitle(bookDetails.getTitle());
        existBook.setAuthor(bookDetails.getAuthor());
        existBook.setPrice(bookDetails.getPrice());
        existBook.setPublishDate(bookDetails.getPublishDate());

        return bookRepository.save(existBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id){
        Book existBook = bookRepository.findById(id).orElseThrow(
                ()-> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND)
        );
        bookRepository.delete(existBook);

        return ResponseEntity.ok("Id = " + id + "Book이 삭제 되었습니다.");
    }
}
