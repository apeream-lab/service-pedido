package com.dian.service.pedido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dian.service.pedido.model.Pedido;

@Service
public class KafkaProducerService {
    private static final String TOPIC = "pedidos";

    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    public void enviarPedido(Pedido pedido) {
        String clave = String.valueOf(pedido.getId());
        this.kafkaTemplate.send(TOPIC, clave, pedido);
        System.out.println("Pedido enviado a Kafka: " + pedido);
    }

}
