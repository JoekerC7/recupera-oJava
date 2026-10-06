# Sistema de Análise de Pedidos (DummyJSON)

Consome `GET https://dummyjson.com/carts?limit=0` com `HttpClient`, desserializa com Jackson
e gera relatório com Streams e Lambdas (sem `for`/`while`).

## Requisitos
- Java 17+
- Maven 3.8+

## Executar
```
mvn compile exec:java
```

## Estrutura
- `RespostaCarrinhos`, `Carrinho`, `ProdutoCarrinho`: modelos (`@JsonIgnoreProperties(ignoreUnknown = true)`)
- `ApiClient`: requisição HTTP + tratamento de erros (conexão, status != 200, JSON malformado)
- `Main`: filter, flatMap+map, sorted, reduce, groupingBy e desafios (max/Optional e formatação por lambda)
