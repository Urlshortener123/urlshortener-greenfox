package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="urls")
public class ShortenedUrl {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String url;
 private String uuid;
 @Column(name = "creation_date")
 @Temporal(TemporalType.DATE)
 private LocalDate creationDate;

 @ManyToOne
 @JoinColumn(name= "user_id")
 private User user;

}
