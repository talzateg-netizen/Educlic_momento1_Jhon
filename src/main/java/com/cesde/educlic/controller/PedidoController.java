package com.cesde.educlic.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.educlic.model.entity.Pedido;
import com.cesde.educlic.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorUsuario(usuarioId));
    }

    // NUEVO 1: GET /api/pedidos/estado/PENDIENTE (o cualquier estado)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorEstado(estado));
    }

    // NUEVO 2: GET /api/pedidos/pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<Pedido>> listarPendientes() {
        return ResponseEntity.ok(pedidoService.listarPedidosPendientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.crearPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (pedidoService.buscarPorId(id).isPresent()) {
            pedidoService.eliminarPedido(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}