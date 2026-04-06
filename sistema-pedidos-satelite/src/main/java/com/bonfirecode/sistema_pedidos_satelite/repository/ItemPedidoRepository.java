package com.bonfirecode.sistema_pedidos_satelite.repository;

import com.bonfirecode.sistema_pedidos_satelite.domain.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {

    // Método para trazer todos os itens de um pedido específico
    List<ItemPedido> findByPedidoId(UUID pedidoId);
}