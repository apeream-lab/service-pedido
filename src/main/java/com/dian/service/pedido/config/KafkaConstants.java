package com.dian.service.pedido.config;

public interface KafkaConstants {
    // Nombre del tópico
    String TOPIC_PEDIDOS = "pedidos";

    // Grupo del consumidor para facturación
    String GROUP_ID_FACTURACION = "grupo-factura";
}
