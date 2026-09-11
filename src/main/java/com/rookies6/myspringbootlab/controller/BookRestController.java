package com.rookies6.myspringbootlab.controller;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    //GET /api/books/search/author?author=Robert
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    //GET /api/books/search/title?title=Clean
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> getBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(
            @Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response created = bookService.createBook(request);
        //HttpStatus.CREATED - 201
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    //PUT, PATCH 둘 다 받는다 (입력값이 있는 필드만 수정)
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<BookDTO.Response> updateBook(
            @PathVariable Long id,
            @RequestBody BookDTO.Request request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        //noContent() - 204
        return ResponseEntity.noContent().build();
    }
}