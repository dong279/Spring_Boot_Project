package com.rookies6.myspringbootlab;

import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.repository.BookRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    void testCreateBook(){
        Book book1 = new Book();
        book1.setTitle("스프링 부트 입문");
        book1.setAuthor("홍길동");
        book1.setIsbn("9788956746425");
        book1.setPrice(30000);
        book1.setPublishDate(LocalDate.of(2025, 5, 7));

        Book book2 = new Book();
        book2.setTitle("JPA 프로그래밍");
        book2.setAuthor("박둘리");
        book2.setIsbn("9788956746432");
        book2.setPrice(35000);
        book2.setPublishDate(LocalDate.of(2025, 4, 30));

        Book addbook1 = bookRepository.save(book1);
        Book addbook2 = bookRepository.save(book2);

        assertThat(addbook1.getId()).isNotNull();
        assertThat(addbook2.getTitle()).isEqualTo("JPA 프로그래밍");
    }
    @Test
    @Order(2)
    void testFindByIsbn(){
        Optional<Book> findByIsbn = bookRepository.findByIsbn("9788956746425");

        assertThat(findByIsbn).isPresent();
        assertThat(findByIsbn.get().getTitle()).isEqualTo("스프링 부트 입문");
    }
    @Test
    @Order(3)
    void testFindByAuthor(){
        List<Book> byAuthor = bookRepository.findByAuthor("홍길동");

        assertThat(byAuthor).isNotEmpty();
        assertThat(byAuthor.get(0).getAuthor()).isEqualTo("홍길동");
    }
    @Test
    @Order(4)
    @Rollback(value = false)
    void testUpdateBook(){
       Book book = bookRepository.findByIsbn("9788956746425")
               .orElseThrow(()-> new IllegalArgumentException("도서없음"));

       book.setPrice(32000);
       Book updated = bookRepository.save(book);

       assertThat(updated.getPrice()).isEqualTo(32000);
    }
    @Test
    @Order(5)
    @Rollback(value = false)
    void testDeleteBook(){
        Book book = bookRepository.findByIsbn("9788956746432")
                .orElseThrow(()-> new IllegalArgumentException("도서없음"));
        bookRepository.deleteById(book.getId());

        assertThat(bookRepository.findByIsbn("9788956746432")).isEmpty();
    }
}
