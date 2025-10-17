package com.dian.service.pedido.model;

import lombok.Data;

@Data
public class Pedido {
    private String id;
    private String producto;
    private int cantidad;
    private float precio;
    private String cliente;
    private String correo;
}
