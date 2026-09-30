package com.cesde.educlic.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.educlic.model.entity.Pedido;
import com.cesde.educlic.model.enums.EstadoPedido;
import com.cesde.educlic.service.PedidoService;

import lombok.RequiredArgsConstructor;

// Usado por checkout.html (crear pedido) y perfil.html (historial)
// Body esperado en crear:
// {
//   "usuario": { "id": 1 },
//   "nombreDestinatario": "...", "telefonoContacto": "...",
//   "direccion": "...", "ciudad": "...", "codigoPostal": "...", "notas": "...",
//   "detalles": [ { "producto": { "id": 5 }, "cantidad": 2 } ]
// }
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(pedido));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar(@RequestParam(required = false) Long usuarioId) {
        if (usuarioId != null) {
            return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
        }
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, estado));
    }
}