package pedidos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final String URL = "https://dummyjson.com/carts?limit=0";

    
    public static RespostaCarrinhos buscarCarrinhos() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Erro: a API respondeu com status HTTP " + response.statusCode() + ".");
                return null;
            }
            return new ObjectMapper().readValue(response.body(), RespostaCarrinhos.class);

        } catch (JsonProcessingException e) {
            System.err.println("Erro: JSON malformado ou inesperado: " + e.getOriginalMessage());
        } catch (IOException e) {
            System.err.println("Erro de conexão com a API (" + e.getClass().getSimpleName()
                    + "). Verifique sua internet e tente novamente.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Erro: a requisição foi interrompida.");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: URL inválida: " + e.getMessage());
        }
        return null;
    }
}
