package com.dian.service.pedido.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.dian.service.pedido.config.KafkaConstants;
import com.dian.service.pedido.model.Pedido;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = KafkaConstants.TOPIC_PEDIDOS, groupId = KafkaConstants.GROUP_ID_FACTURACION, containerFactory = "pedidoKafkaListenerContainerFactory")
    public void consumirPedido(String mensaje) {
        try {
            JsonNode root = mapper.readTree(mensaje);
            JsonNode payload = root.get("payload");

            // Convertir el payload JSON al objeto Pedido
            Pedido pedido = mapper.treeToValue(payload, Pedido.class);

            logger.info("Pedido consumido en FACTURACIÓN: " + pedido);
        } catch (Exception e) {
            logger.error("Error al procesar mensaje Avro JSON: " + e.getMessage());
        }
    }
}