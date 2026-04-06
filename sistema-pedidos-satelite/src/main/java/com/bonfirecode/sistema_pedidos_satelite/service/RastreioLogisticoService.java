package com.bonfirecode.sistema_pedidos_satelite.service;

import com.bonfirecode.sistema_pedidos_satelite.domain.Pedido;
import com.bonfirecode.sistema_pedidos_satelite.domain.RastreioLogistico;
import com.bonfirecode.sistema_pedidos_satelite.repository.PedidoRepository;
import com.bonfirecode.sistema_pedidos_satelite.repository.RastreioLogisticoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RastreioLogisticoService {

    private final RastreioLogisticoRepository rastreioRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public RastreioLogistico atualizarRastreio(UUID pedidoId, String codigo, String transportadora, String status, String url) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado para atualização de rastreio."));

        // Busca se já existe um rastreio para este pedido. Se não existir, cria um novo.
        RastreioLogistico rastreio = rastreioRepository.findByPedidoId(pedidoId)
                .orElse(RastreioLogistico.builder().pedido(pedido).build());

        rastreio.setCodigoRastreio(codigo);
        rastreio.setTransportadora(transportadora);
        rastreio.setStatusEntrega(status);
        rastreio.setUrlRastreio(url);
        rastreio.setDataAtualizacao(LocalDateTime.now());

        return rastreioRepository.save(rastreio);
    }
}