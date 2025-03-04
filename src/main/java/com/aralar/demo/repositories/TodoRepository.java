package com.aralar.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aralar.demo.models.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
