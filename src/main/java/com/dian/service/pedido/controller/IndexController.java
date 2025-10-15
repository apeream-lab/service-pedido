package com.dian.service.pedido.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/server")
public class IndexController {
    @GetMapping("/ping")
    public String getMethodName() {
        return "Servidor Corriendo";
    }

}
