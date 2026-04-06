package com.bonfirecode.sistema_pedidos_satelite.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "viapro_cliente_id", nullable = false)
    private Long viaproClienteId; // ID do cliente lá no ERP

    @Column(name = "representante_id", nullable = false)
    private Long representanteId; // Quem está logado e fez a venda

    @Enumerated(EnumType.STRING)
    @Column(name = "status_venda", nullable = false)
    private StatusVenda statusVenda;

    @Column(nullable = false)
    private Integer versao; // Começa sempre em 1

    @Column(name = "pedido_original_id")
    private UUID pedidoOriginalId; // Se for uma revisão, guarda o ID da versão 1

    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Relacionamento bidirecional com os itens do pedido
    @Builder.Default // Garante que o Lombok inicialize a lista vazia ao usar o Pedido.builder()
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    // --- REGRAS DE NEGÓCIO DA ENTIDADE ---

    // A nossa Trava de Segurança blindada!
    public boolean isBloqueadoParaEdicao() {
        return this.statusVenda == StatusVenda.ENVIADO_FINANCEIRO ||
               this.statusVenda == StatusVenda.FATURADO ||
               this.statusVenda == StatusVenda.CANCELADO ||
               this.statusVenda == StatusVenda.CANCELADO_POR_REVISAO;
    }
}