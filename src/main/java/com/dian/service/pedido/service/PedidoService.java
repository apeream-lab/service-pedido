package com.dian.service.pedido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
            // Crear el objeto Pedido (sin enviar a Kafka por ahora)
            Pedido pedidoRegistrado = new Pedido();
            pedidoRegistrado.setId(pedido.getId());
            pedidoRegistrado.setProducto(pedido.getProducto());
            pedidoRegistrado.setCantidad(pedido.getCantidad());
            pedidoRegistrado.setPrecio(pedido.getPrecio());
            pedidoRegistrado.setUsuario(pedido.getUsuario());
            logger.info("Request transformado en pedido: " + pedidoRegistrado);
            this.kafkaProducerService.enviarPedido(pedidoRegistrado);

            return "Pedido registrado exitosamente. ID: " + pedido.getId();
        } catch (Exception e) {
            logger.error("Error al registrar el pedido", e);

            return "Error al registrar el pedido: " + e.getMessage();

        }
    }
}
