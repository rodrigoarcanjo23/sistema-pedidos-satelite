package com.bonfirecode.sistema_pedidos_satelite.repository;

import com.bonfirecode.sistema_pedidos_satelite.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    
    // O Spring Boot cria a query de busca automaticamente só pelo nome do método!
    List<Pedido> findByViaproClienteId(Long viaproClienteId);
    
    List<Pedido> findByRepresentanteId(Long representanteId);
}