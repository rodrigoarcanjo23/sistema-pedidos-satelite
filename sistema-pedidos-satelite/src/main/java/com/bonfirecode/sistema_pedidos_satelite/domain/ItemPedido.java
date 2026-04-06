package com.bonfirecode.sistema_pedidos_satelite.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "itens_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relacionamento: Vários itens pertencem a um Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(name = "viapro_produto_id", nullable = false)
    private Long viaproProdutoId; // Referência do produto no ERP

    @Column(name = "nome_produto_snapshot", nullable = false)
    private String nomeProdutoSnapshot; // "Fotografia" do nome no ato da venda

    @Column(name = "preco_unitario_snapshot", precision = 15, scale = 2, nullable = false)
    private BigDecimal precoUnitarioSnapshot; // "Fotografia" do preço no ato da venda

    @Column(nullable = false)
    private Integer quantidade;
}