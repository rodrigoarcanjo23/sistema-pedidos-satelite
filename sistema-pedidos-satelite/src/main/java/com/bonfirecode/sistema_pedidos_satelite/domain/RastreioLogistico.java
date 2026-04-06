package com.bonfirecode.sistema_pedidos_satelite.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rastreio_logistico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RastreioLogistico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relacionamento 1 para 1: Um pedido tem um rastreio (nesta versão)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Column(name = "codigo_rastreio")
    private String codigoRastreio;

    @Column(name = "transportadora")
    private String transportadora; // Ex: Correios, JadLog

    @Column(name = "status_entrega")
    private String statusEntrega; // Ex: EM_TRANSITO, ENTREGUE

    @Column(name = "url_rastreio")
    private String urlRastreio;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}