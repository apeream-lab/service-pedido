package com.dian.service.pedido.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dian.service.pedido.config.KafkaConstants;
import com.dian.service.pedido.model.Pedido;

import org.apache.kafka.connect.data.*;
import org.apache.kafka.connect.json.JsonConverter;

import java.lang.reflect.Field;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static String buildConnectMessage(Object pojo) throws Exception {
        Class<?> clazz = pojo.getClass();
        String schemaName = clazz.getName();

        // 1️⃣ Crear el Schema dinámicamente
        SchemaBuilder builder = SchemaBuilder.struct().name(schemaName);

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> type = field.getType();

            // Detectar tipo básico y asignar tipo Kafka Connect
            if (type == String.class)
                builder.field(field.getName(), Schema.STRING_SCHEMA);
            else if (type == int.class || type == Integer.class)
                builder.field(field.getName(), Schema.INT32_SCHEMA);
            else if (type == long.class || type == Long.class)
                builder.field(field.getName(), Schema.INT64_SCHEMA);
            else if (type == float.class || type == Float.class)
                builder.field(field.getName(), Schema.FLOAT32_SCHEMA);
            else if (type == double.class || type == Double.class)
                builder.field(field.getName(), Schema.FLOAT64_SCHEMA);
            else if (type == boolean.class || type == Boolean.class)
                builder.field(field.getName(), Schema.BOOLEAN_SCHEMA);
            else
                builder.field(field.getName(), Schema.STRING_SCHEMA); // fallback genérico
        }

        Schema schema = builder.build();

        // 2️⃣ Crear el Struct (payload)
        Struct struct = new Struct(schema);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            struct.put(field.getName(), field.get(pojo));
        }

        // 3️⃣ Convertir a JSON Connect
        JsonConverter converter = new JsonConverter();
        try {
            Map<String, Object> configs = new HashMap<>();
            configs.put("schemas.enable", true);
            converter.configure(configs, false);

            byte[] jsonBytes = converter.fromConnectData("pedidos", schema, struct);
            return new String(jsonBytes, StandardCharsets.UTF_8);
        } finally {
            try {
                converter.close();
            } catch (Exception e) {
                // ignore close exceptions
            }
        }
    }

    public void enviarPedido(Pedido pedido) {
        // Construir mensaje Avro en JSON legible
        try {
            String mensajeAvroJson = buildConnectMessage(pedido);
            // Enviar a Kafka
            this.kafkaTemplate.send(KafkaConstants.TOPIC_PEDIDOS, pedido.getId(), mensajeAvroJson);
            logger.info("Pedido enviado a Kafka: " + pedido);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
