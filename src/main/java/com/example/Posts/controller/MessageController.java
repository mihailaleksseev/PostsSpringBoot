package com.example.Posts.controller;

import com.example.Posts.domain.Message;
import com.example.Posts.domain.Views;
import com.example.Posts.repository.MessageRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер сообщений
 */

@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageRepo messageRepo;

    @Autowired
    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    public Message getOne(@PathVariable("id") Message message) { // когда в урле приходи id мы по нему ищем message
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageRepo.save(message);
    }

    // ctrl + alt + v
    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDb,  // сообщение из БД
            @RequestBody Message message)               // сообщение от пользователя
    {
        BeanUtils.copyProperties(message, messageFromDb, "id"); //Метод BeanUtils.copyProperties позволяет копировать все поля в новый бин из старого, кроме id

        return messageRepo.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
    }

    // ctrl + alt + m  выносит выделенный фрагмент в отдельный класс
    /**
     * // GET all
     * fetch('/message/').then(response => response.json().then(console.log))
     *
     * // GET one
     * fetch('/message/2').then(response => response.json().then(console.log))
     *
     * // POST add new one
     * fetch(
     *   '/message',
     *   {
     *     method: 'POST',
     *     headers: { 'Content-Type': 'application/json' },
     *     body: JSON.stringify({ text: 'Fourth message (4)', id: 10 })
     *   }
     * ).then(result => result.json().then(console.log))
     *
     * // PUT save existing
     * fetch(
     *   '/message/4',
     *   {
     *     method: 'PUT',
     *     headers: { 'Content-Type': 'application/json' },
     *     body: JSON.stringify({ text: 'Fourth message', id: 10 })
     *   }
     * ).then(result => result.json().then(console.log));
     *
     * // DELETE existing
     * fetch('/message/4', { method: 'DELETE' }).then(result => console.log(result))
     */
}
