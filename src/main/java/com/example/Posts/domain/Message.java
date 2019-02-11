package com.example.Posts.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // Показываем что это сущность
@Table // Показываем что это таблица и ее следует искать в бд
@Data
public class Message {

    // Указываем необходимые поля

    @Id //указываем что это id и автоматическую генерацию
    @JsonView(Views.IdName.class)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonView(Views.IdName.class)
    private String text;

    @Column (updatable = false) //Поле необновляемо
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") //Изменяем формат даты
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;
}
