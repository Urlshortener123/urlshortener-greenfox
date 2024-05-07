package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="urls")
public class ShortenedUrl {

 private String url;
 private String uuid;
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
}
