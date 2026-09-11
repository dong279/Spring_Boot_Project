package com.rookies6.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String language;

    @Column(name = "page_count")
    private Integer pageCount;

    private String publisher;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    private String edition;

    // 연관관계의 주인 — 외래키를 이 엔티티가 들고 있다
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;
}