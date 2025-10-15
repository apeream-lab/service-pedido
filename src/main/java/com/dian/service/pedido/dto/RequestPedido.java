package com.dian.service.pedido.dto;

import com.dian.service.pedido.model.Usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RequestPedido {
    @NotNull(message = "El ID del pedido es obligatorio")
    @Min(value = 1, message = "El ID debe ser mayor a 0")
    private int id;
    
    @NotBlank(message = "El producto es obligatorio")
    private String producto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private int cantidad;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private float precio;
    
    @Valid
    @NotNull(message = "Los datos del usuario son obligatorios")
    private Usuario usuario;
}
