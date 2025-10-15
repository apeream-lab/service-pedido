package com.dian.service.pedido.model;

import lombok.Data;

@Data
public class Pedido {
    private int id;
    private String producto;
    private int cantidad;
    private float precio;
    private Usuario usuario;
}
