package com.example.Posts.controller;

import com.example.Posts.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер сообщений
 */

@RestController
@RequestMapping("message")
public class MessageController {

    private int counter = 5; //Костыльная версия счетчика для подставления актуального id

    //Временный костыль вместо базы данных
    public List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
       add(new HashMap<String, String>() {{put("id", "1"); put("text", "message one 1"); }});
       add(new HashMap<String, String>() {{put("id", "2"); put("text", "message 2"); }});
       add(new HashMap<String, String>() {{put("id", "3"); put("text", "message 3"); }});
       add(new HashMap<String, String>() {{put("id", "4"); put("text", "message 4"); }});
    }};

    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getMessage(id);
    }

    // ctrl + alt + m  выносит выделенный фрагмент в отдельный класс
    private Map<String, String> getMessage(@PathVariable String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst()    //Фильтруем пользователей и вытаскиваем первого с нужным id
                .orElseThrow(NotFoundException::new); //Выбрасываем ошибку если пользователь с таким id не найден
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }

    // ctrl + alt + v
    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) {
        Map<String, String> messageFromDb = getMessage(id);
        messageFromDb.putAll(message);
        messageFromDb.put("id", id);

        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getMessage(id);
        messages.remove(message);
    }

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
