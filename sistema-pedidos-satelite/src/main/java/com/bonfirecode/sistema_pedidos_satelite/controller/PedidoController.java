package com.bonfirecode.sistema_pedidos_satelite.controller;

import com.bonfirecode.sistema_pedidos_satelite.domain.ItemPedido;
import com.bonfirecode.sistema_pedidos_satelite.domain.Pedido;
import com.bonfirecode.sistema_pedidos_satelite.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    public record IniciarPedidoDTO(Long viaproClienteId, Long representanteId) {}
    
    // Olha como o DTO ficou limpo! O frontend só manda o ID e a Quantidade.
    public record NovoItemDTO(Long viaproProdutoId, Integer quantidade) {}

    @PostMapping
    public ResponseEntity<Pedido> iniciarPedido(@RequestBody IniciarPedidoDTO dto) {
        Pedido pedido = pedidoService.iniciarNovoPedido(dto.viaproClienteId(), dto.representanteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<ItemPedido> adicionarItem(@PathVariable UUID pedidoId, @RequestBody NovoItemDTO dto) {
        // Chamamos o service passando só os dados vitais
        ItemPedido itemSalvo = pedidoService.adicionarItem(pedidoId, dto.viaproProdutoId(), dto.quantidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(itemSalvo);
    }

    @PatchMapping("/{pedidoId}/enviar-financeiro")
    public ResponseEntity<Pedido> enviarParaFinanceiro(@PathVariable UUID pedidoId) {
        Pedido pedidoAtualizado = pedidoService.enviarParaFinanceiro(pedidoId);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @PostMapping("/{pedidoId}/revisoes")
    public ResponseEntity<Pedido> solicitarRevisao(@PathVariable UUID pedidoId) {
        Pedido pedidoRevisado = pedidoService.solicitarRevisao(pedidoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRevisado);
    }
}