package com.cesde.educlic.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El cliente envia solo el id: "usuario": { "id": 1 }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Builder.Default
    @Column(name = "fecha_pedido", nullable = false, updatable = false)
    private LocalDateTime fechaPedido = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    // Datos de envio embebidos directamente en el pedido (checkout simple,
    // sin catalogo de direcciones guardadas por usuario)
    @NotBlank(message = "El nombre del destinatario es obligatorio")
    @Column(name = "nombre_destinatario", nullable = false, length = 150)
    private String nombreDestinatario;

    @NotBlank(message = "El telefono de contacto es obligatorio")
    @Column(name = "telefono_contacto", nullable = false, length = 30)
    private String telefonoContacto;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(nullable = false, length = 255)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Column(length = 500)
    private String notas;

    // @JsonManagedReference + @JsonBackReference (en DetallePedido.pedido)
    // evitan el bucle infinito Pedido -> detalles -> pedido -> detalles...
    // al convertir a JSON.
    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePedido> detalles = new ArrayList<>();

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }
}