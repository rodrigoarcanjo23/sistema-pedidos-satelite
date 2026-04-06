package com.bonfirecode.sistema_pedidos_satelite.controller;

import com.bonfirecode.sistema_pedidos_satelite.domain.RastreioLogistico;
import com.bonfirecode.sistema_pedidos_satelite.service.RastreioLogisticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/rastreio")
@RequiredArgsConstructor
public class RastreioLogisticoController {

    private final RastreioLogisticoService rastreioService;

    // DTO interno para receber os dados do ERP/Transportadora
    public record AtualizacaoRastreioDTO(String codigoRastreio, String transportadora, String statusEntrega, String urlRastreio) {}

    // Este é o endpoint que o sistema externo vai chamar para atualizar o status
    @PutMapping
    public ResponseEntity<RastreioLogistico> receberAtualizacaoLogistica(
            @PathVariable UUID pedidoId, 
            @RequestBody AtualizacaoRastreioDTO dto) {
        
        RastreioLogistico rastreioAtualizado = rastreioService.atualizarRastreio(
                pedidoId, 
                dto.codigoRastreio(), 
                dto.transportadora(), 
                dto.statusEntrega(), 
                dto.urlRastreio()
        );
        
        return ResponseEntity.ok(rastreioAtualizado);
    }
}