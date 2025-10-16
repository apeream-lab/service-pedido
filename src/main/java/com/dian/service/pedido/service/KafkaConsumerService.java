package com.dian.service.pedido.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.dian.service.pedido.config.KafkaConstants;
import com.dian.service.pedido.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = KafkaConstants.TOPIC_PEDIDOS, groupId = KafkaConstants.GROUP_ID_FACTURACION, containerFactory = "pedidoKafkaListenerContainerFactory")
    public void consumirPedido(Pedido pedido) {
        if (pedido != null) {
            logger.info("Pedido consumido en FACTURACIÓN: " + pedido);
        } else {
            logger.warn("Pedido nulo recibido en FACTURACIÓN");
        }
    }

}