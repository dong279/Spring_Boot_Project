package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.entity.BookDetail;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.exception.ErrorCode;
import com.rookies6.myspringbootlab.repository.BookDetailRepository;
import com.rookies6.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public BookDTO.Response getBookById(Long id) {
        // 상세정보까지 한 번에 가져오기 위해 JOIN FETCH 쿼리를 쓴다
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        // 상세정보가 함께 넘어온 경우에만 만들어 붙인다
        if (request.getBookDetail() != null) {
            BookDetail detail = toDetailEntity(request.getBookDetail());
            book.setBookDetail(detail);   // 편의 메서드가 detail.setBook(book)까지 해준다
        }

        // cascade = ALL 이므로 Book만 저장해도 BookDetail이 함께 저장된다
        Book saved = bookRepository.save(book);
        return BookDTO.Response.fromEntity(saved);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book existBook = findBookOrThrow(id);

        // ISBN을 다른 값으로 바꾸려는 경우에만 중복 검사
        if (request.getIsbn() != null && !request.getIsbn().equals(existBook.getIsbn())) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
            }
            existBook.setIsbn(request.getIsbn());
        }

        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }

        if (request.getBookDetail() != null) {
            updateDetail(existBook, request.getBookDetail());
        }

        // 영속 상태이므로 변경 감지(Dirty Checking)로 UPDATE가 나간다. save는 생략 가능
        return BookDTO.Response.fromEntity(existBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        // cascade + orphanRemoval 이므로 BookDetail도 함께 삭제된다
        bookRepository.delete(findBookOrThrow(id));
    }

    private Book findBookOrThrow(Long id) {
        return bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }

    private BookDetail toDetailEntity(BookDTO.BookDetailDTO dto) {
        return BookDetail.builder()
                .description(dto.getDescription())
                .language(dto.getLanguage())
                .pageCount(dto.getPageCount())
                .publisher(dto.getPublisher())
                .coverImageUrl(dto.getCoverImageUrl())
                .edition(dto.getEdition())
                .build();
    }

    private void updateDetail(Book book, BookDTO.BookDetailDTO dto) {
        BookDetail detail = book.getBookDetail();

        // 아직 상세정보가 없던 책이면 새로 만들어 붙인다
        if (detail == null) {
            book.setBookDetail(toDetailEntity(dto));
            return;
        }

        if (dto.getDescription() != null)   detail.setDescription(dto.getDescription());
        if (dto.getLanguage() != null)      detail.setLanguage(dto.getLanguage());
        if (dto.getPageCount() != null)     detail.setPageCount(dto.getPageCount());
        if (dto.getPublisher() != null)     detail.setPublisher(dto.getPublisher());
        if (dto.getCoverImageUrl() != null) detail.setCoverImageUrl(dto.getCoverImageUrl());
        if (dto.getEdition() != null)       detail.setEdition(dto.getEdition());
    }
}