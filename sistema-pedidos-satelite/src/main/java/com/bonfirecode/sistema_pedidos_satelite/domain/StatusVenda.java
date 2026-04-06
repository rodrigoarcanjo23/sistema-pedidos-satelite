package com.bonfirecode.sistema_pedidos_satelite.domain;

public enum StatusVenda {
    RASCUNHO,
    PAUSADO,
    EM_ANALISE,
    ENVIADO_FINANCEIRO,
    FATURADO,
    CANCELADO,
    CANCELADO_POR_REVISAO // Usado quando criarmos uma nova versão do pedido
}