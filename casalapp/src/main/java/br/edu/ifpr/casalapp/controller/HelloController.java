package br.edu.ifpr.casalapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Sua rota existente
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    // NOVA rota para a página inicial
    @GetMapping("/")
    public String home() {
        return "Bem-vindo à API do CasalApp!";
    }
}