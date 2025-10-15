package com.dian.service.pedido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dian.service.pedido.dto.RequestPedido;
import com.dian.service.pedido.model.Pedido;

@Service
public class PedidoService {
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

            // Simular procesamiento del pedido (sin Kafka)
            System.out.println("=====================================");
            System.out.println("              PEDIDO REGISTRADO");
            System.out.println("=====================================");
            System.out.println("ID Pedido: " + pedidoRegistrado.getId());
            System.out.println("Cliente: " + pedidoRegistrado.getUsuario().getNombre() + " "
                    + pedidoRegistrado.getUsuario().getApellido());
            System.out.println("Producto: " + pedidoRegistrado.getProducto());
            System.out.println("Cantidad: " + pedidoRegistrado.getCantidad());
            System.out.println("Precio: $" + pedidoRegistrado.getPrecio());
            System.out.println("Total: $" + (pedidoRegistrado.getCantidad() * pedidoRegistrado.getPrecio()));
            System.out.println("=====================================");

            // TODO: Enviar el pedido a Kafka cuando esté disponible
            this.kafkaProducerService.enviarPedido(pedidoRegistrado);

            return "Pedido registrado exitosamente. ID: " + pedido.getId();
        } catch (Exception e) {
            e.printStackTrace();

            return "Error al registrar el pedido: " + e.getMessage();

        }
    }
}
