package com.bonfirecode.sistema_pedidos_satelite.repository;

import com.bonfirecode.sistema_pedidos_satelite.domain.RastreioLogistico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RastreioLogisticoRepository extends JpaRepository<RastreioLogistico, UUID> {

    // Método para achar o rastreio baseado no ID do Pedido
    Optional<RastreioLogistico> findByPedidoId(UUID pedidoId);
}