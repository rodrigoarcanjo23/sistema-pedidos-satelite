package com.bonfirecode.sistema_pedidos_satelite.integration;

import com.bonfirecode.sistema_pedidos_satelite.integration.dto.ProdutoViaproDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaproApiClient {

    private final RestClient restClient;

    // Construtor: Aqui nós configuramos a URL base do Viapro ERP
    public ViaproApiClient() {
        this.restClient = RestClient.builder()
                // Supondo que o Viapro ERP roda localmente na porta 8080
                .baseUrl("http://localhost:8080/api/v1/integracao") 
                .build();
    }

    // Método que vai buscar os dados do produto lá no ERP
    public ProdutoViaproDTO buscarProdutoPorId(Long viaproProdutoId) {
        // Por enquanto, como o Viapro não tem esse endpoint ainda, 
        // vamos deixar a estrutura pronta. 
        // Quando o Viapro estiver no ar, o código fará um GET real:
        /*
        return restClient.get()
                .uri("/produtos/{id}", viaproProdutoId)
                .retrieve()
                .body(ProdutoViaproDTO.class);
        */

        // MOCK TEMPORÁRIO PARA TESTARMOS:
        return new ProdutoViaproDTO(
                viaproProdutoId, 
                "Produto Simulado do Viapro", 
                new java.math.BigDecimal("150.00"), 
                "http://foto.com/img.jpg", 
                100
        );
    }
}