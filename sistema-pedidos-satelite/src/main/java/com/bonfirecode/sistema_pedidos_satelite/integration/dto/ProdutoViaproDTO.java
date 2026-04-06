package com.bonfirecode.sistema_pedidos_satelite.integration.dto;

import java.math.BigDecimal;

// Este é o formato exato que o Viapro ERP vai nos devolver quando perguntarmos sobre um produto
public record ProdutoViaproDTO(
    Long id,
    String nome,
    BigDecimal precoAtual,
    String urlFoto,
    Integer saldoDisponivel
) {}