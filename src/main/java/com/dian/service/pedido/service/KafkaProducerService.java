package com.dian.service.pedido.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dian.service.pedido.config.KafkaConstants;
import com.dian.service.pedido.model.Pedido;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    public void enviarPedido(Pedido pedido) {
        String clave = String.valueOf(pedido.getId());
        this.kafkaTemplate.send(KafkaConstants.TOPIC_PEDIDOS, clave, pedido);
        logger.info("Pedido enviado a Kafka: " + pedido);
    }

}
