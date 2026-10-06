package pedidos;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        RespostaCarrinhos resposta = ApiClient.buscarCarrinhos();
        if (resposta == null) return;

        List<Carrinho> carrinhos = resposta.getCarts();
        System.out.println(resposta);

        Function<Carrinho, String> formatar = c -> String.format(Locale.US,
                "Carrinho #%d | Usuário: %d | Itens: %d | Total: US$ %.2f",
                c.getId(), c.getUserId(), c.getTotalQuantity(), c.getTotal());

        System.out.println("\n== Carrinhos com total acima de US$ 1.000 ==");
        carrinhos.stream()
                .filter(c -> c.getTotal() > 1000)
                .forEach(c -> System.out.println(formatar.apply(c)));

        System.out.println("\n== Produtos com desconto acima de 15% ==");
        carrinhos.stream()
                .flatMap(c -> c.getProducts().stream())
                .filter(p -> p.getDiscountPercentage() > 15)
                .map(ProdutoCarrinho::getTitle)
                .distinct()
                .forEach(System.out::println);

        System.out.println("\n== Carrinhos por economia (maior -> menor) ==");
        carrinhos.stream()
                .sorted(Comparator.comparingDouble(Carrinho::getEconomia).reversed())
                .forEach(c -> System.out.printf(Locale.US, "Carrinho #%d | Economia: US$ %.2f%n",
                        c.getId(), c.getEconomia()));

        System.out.println("\n== Soma do discountedTotal ==");
        double soma = carrinhos.stream()
                .map(Carrinho::getDiscountedTotal)
                .reduce(0.0, Double::sum);
        System.out.printf(Locale.US, "US$ %.2f%n", soma);

        System.out.println("\n== Carrinhos por número de produtos ==");
        carrinhos.stream()
                .collect(Collectors.groupingBy(Carrinho::getTotalProducts, TreeMap::new, Collectors.counting()))
                .forEach((produtos, qtd) ->
                        System.out.println(produtos + " produto(s): " + qtd + " carrinho(s)"));

        System.out.println("\n== Carrinho de maior valor ==");
        carrinhos.stream()
                .max(Comparator.comparingDouble(Carrinho::getTotal))
                .ifPresentOrElse(
                        c -> System.out.println(formatar.apply(c)),
                        () -> System.out.println("Nenhum carrinho encontrado."));
    }
}
