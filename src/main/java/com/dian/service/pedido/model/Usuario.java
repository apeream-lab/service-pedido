package com.dian.service.pedido.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Usuario {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;
    
    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;
}
