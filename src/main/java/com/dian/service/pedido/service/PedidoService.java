package com.dian.service.pedido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dian.service.pedido.dto.RequestPedido;
import com.dian.service.pedido.model.Pedido;

@Service
public class PedidoService {
    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public String registrarPedido(RequestPedido pedido) {
        try {
            Pedido pedidoRegistrado = new Pedido();
            pedidoRegistrado.setId(UUID.randomUUID().toString());
            pedidoRegistrado.setProducto(pedido.getProducto());
            pedidoRegistrado.setCantidad(pedido.getCantidad());
            pedidoRegistrado.setPrecio(pedido.getPrecio());
            pedidoRegistrado.setCliente(pedido.getCliente());
            pedidoRegistrado.setCorreo(pedido.getCorreo());
            logger.info("Request transformado en pedido: " + pedidoRegistrado);
            this.kafkaProducerService.enviarPedido(pedidoRegistrado);

            return "Pedido registrado exitosamente. " + pedido.getCantidad() + " unidades de " + pedido.getProducto()
                    + "ID: " + pedidoRegistrado.getId();
        } catch (Exception e) {
            logger.error("Error al registrar el pedido", e);

            return "Error al registrar el pedido: " + e.getMessage();

        }
    }
}
