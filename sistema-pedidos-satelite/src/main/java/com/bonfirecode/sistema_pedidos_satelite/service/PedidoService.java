package com.bonfirecode.sistema_pedidos_satelite.service;

import com.bonfirecode.sistema_pedidos_satelite.domain.ItemPedido;
import com.bonfirecode.sistema_pedidos_satelite.domain.Pedido;
import com.bonfirecode.sistema_pedidos_satelite.domain.StatusVenda;
import com.bonfirecode.sistema_pedidos_satelite.integration.ViaproApiClient;
import com.bonfirecode.sistema_pedidos_satelite.integration.dto.ProdutoViaproDTO;
import com.bonfirecode.sistema_pedidos_satelite.repository.ItemPedidoRepository;
import com.bonfirecode.sistema_pedidos_satelite.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ViaproApiClient viaproApiClient; // <-- Injetamos nossa antena aqui!

    @Transactional
    public Pedido iniciarNovoPedido(Long viaproClienteId, Long representanteId) {
        Pedido novoPedido = Pedido.builder()
                .viaproClienteId(viaproClienteId)
                .representanteId(representanteId)
                .statusVenda(StatusVenda.RASCUNHO)
                .versao(1)
                .valorTotal(BigDecimal.ZERO)
                .build();
        
        return pedidoRepository.save(novoPedido);
    }

    // <-- Método ATUALIZADO para usar a Integração
    @Transactional
    public ItemPedido adicionarItem(UUID pedidoId, Long viaproProdutoId, Integer quantidade) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);

        if (pedido.isBloqueadoParaEdicao()) {
            throw new IllegalStateException("Ação bloqueada! Este pedido já foi enviado ao financeiro. Solicite uma revisão.");
        }

        // MÁGICA ACONTECENDO: Buscamos a "verdade" lá no Viapro ERP!
        ProdutoViaproDTO produtoReal = viaproApiClient.buscarProdutoPorId(viaproProdutoId);

        // Montamos o item usando os dados oficiais do ERP (o Snapshot daquele momento)
        ItemPedido novoItem = ItemPedido.builder()
                .pedido(pedido)
                .viaproProdutoId(produtoReal.id())
                .nomeProdutoSnapshot(produtoReal.nome())
                .precoUnitarioSnapshot(produtoReal.precoAtual())
                .quantidade(quantidade)
                .build();
        
        // Atualiza o valor total do pedido
        BigDecimal valorItem = novoItem.getPrecoUnitarioSnapshot().multiply(BigDecimal.valueOf(novoItem.getQuantidade()));
        pedido.setValorTotal(pedido.getValorTotal().add(valorItem));
        
        pedidoRepository.save(pedido);
        return itemPedidoRepository.save(novoItem);
    }

    @Transactional
    public Pedido enviarParaFinanceiro(UUID pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        if (pedido.getStatusVenda() != StatusVenda.RASCUNHO && pedido.getStatusVenda() != StatusVenda.EM_ANALISE) {
            throw new IllegalStateException("Apenas pedidos em rascunho ou análise podem ser enviados ao financeiro.");
        }
        pedido.setStatusVenda(StatusVenda.ENVIADO_FINANCEIRO);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido solicitarRevisao(UUID pedidoOriginalId) {
        Pedido pedidoOriginal = buscarPedidoOuFalhar(pedidoOriginalId);
        if (!pedidoOriginal.isBloqueadoParaEdicao()) {
            throw new IllegalStateException("Este pedido não está bloqueado, você pode editá-lo diretamente.");
        }

        pedidoOriginal.setStatusVenda(StatusVenda.CANCELADO_POR_REVISAO);
        pedidoRepository.save(pedidoOriginal);

        Pedido pedidoRevisado = Pedido.builder()
                .viaproClienteId(pedidoOriginal.getViaproClienteId())
                .representanteId(pedidoOriginal.getRepresentanteId())
                .statusVenda(StatusVenda.RASCUNHO)
                .versao(pedidoOriginal.getVersao() + 1)
                .pedidoOriginalId(pedidoOriginal.getPedidoOriginalId() != null ? pedidoOriginal.getPedidoOriginalId() : pedidoOriginal.getId())
                .valorTotal(pedidoOriginal.getValorTotal())
                .build();
        
        return pedidoRepository.save(pedidoRevisado);
    }

    private Pedido buscarPedidoOuFalhar(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado no sistema satélite."));
    }
}