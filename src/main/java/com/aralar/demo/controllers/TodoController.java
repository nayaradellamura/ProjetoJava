package com.aralar.demo.controllers;

import com.aralar.demo.models.Todo;
import com.aralar.demo.repositories.TodoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.Binding;
import java.util.List;
import java.util.Map;

@Controller
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/")
    public ModelAndView list(){
          return new ModelAndView(
                  "todo/list",
                  Map.of("todos", todoRepository.findAll(Sort.by("deadLine")))
          );
    }

    @GetMapping("/create")
    public ModelAndView create () {
        return new ModelAndView("todo/form", Map.of("todo", new Todo()));
    }

    @PostMapping("/create")
    public String create(@Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo/form";
        }
        todoRepository.save(todo);
        return "redirect:/";
    }
    
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        var todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ModelAndView("todo/form", Map.of("todo", todo.get()));
    }

    @PostMapping("/edit/{id}")
    public String edit(@Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo/form";
        }
        todoRepository.save(todo);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete (@PathVariable Long id) {
        var todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ModelAndView("todo/delete", Map.of("todo", todo.get()));
    }

    @PostMapping("/delete/{id}")
    public String delete(Todo todo) {
        todoRepository.delete(todo);
        return "redirect:/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@PathVariable Long id) {
        var optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var todo = optionalTodo.get();
        todo.markHasFinished();
        todoRepository.save(todo);
        return "redirect:/";
    }
}
