package com.rookies6.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "books")
@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column(unique = true)
    private String isbn;

    @Column
    private LocalDate publishDate;

    @Column
    private Integer price;
}
