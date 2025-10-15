package com.dian.service.pedido.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dian.service.pedido.dto.RequestPedido;
import com.dian.service.pedido.service.PedidoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarPedido(@Valid @RequestBody RequestPedido entity) {
        try {
            String resultado = this.pedidoService.registrarPedido(entity);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar el pedido: " + e.getMessage());
        }
    }

}
