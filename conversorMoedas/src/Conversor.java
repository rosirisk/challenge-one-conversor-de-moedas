import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class Conversor {

    public double requestData(String urlStr, String currencyFilter) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        String req_result = jsonobj.get("result").getAsString();
        if (req_result.equals("success")){
            JsonObject conversionRates = jsonobj.get("conversion_rates").getAsJsonObject();
            return conversionRates.get(currencyFilter).getAsDouble();
        }else{
            System.out.println("Falha ao consumir a API. Verifique a requisição e sua conexão!");
            throw new RuntimeException("Falha ao buscar o currencyFilter");
        }

    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Conversor conversor = new Conversor();


        double taxaDolar = 0;
        double taxaReal = 0;
        double taxaPeso = 0;
        double taxaEuro = 0;
        String chave = "Insira sua chave aqui";

        Map<String, String> endpoints = Map.of("BRL", "https://v6.exchangerate-api.com/v6/"+chave+"/latest/BRL",
                "USD", "https://v6.exchangerate-api.com/v6/"+chave+"/latest/USD",
                "ARS", "https://v6.exchangerate-api.com/v6/"+chave+"latest/ARS",
                "EUR", "https://v6.exchangerate-api.com/v6/"+chave+"latest/EUR");


        while (true) {
            System.out.println("Bem-vindo ao Conversor de Moedas!");
            System.out.println("----------------------------------");
            System.out.println("Escolha uma opção válida para conversão:");

            System.out.println("1. Dólar para Real");
            System.out.println("2. Real para Dólar");
            System.out.println("3. Peso para Real");
            System.out.println("4. Real para Peso");
            System.out.println("5. Euro para Real");
            System.out.println("6. Real para Euro");
            System.out.println("0. Sair");

            System.out.println("----------------------------------");
            int escolha = scanner.nextInt();

            if (escolha == 0) {
                System.out.println("Obrigado por usar o conversor de moedas!");
                break;
            } else if (escolha >= 1 && escolha <= 6) {
                System.out.print("Digite o valor a ser convertido: ");
                double valor = scanner.nextDouble();

                double valorConvertido;
                switch (escolha) {
                    case 1:
                        taxaDolar = conversor.requestData(endpoints.get("USD"), "BRL");
                        System.out.println();
                        valorConvertido = valor * taxaDolar;
                        System.out.println("Valor convertido em dólar: $" + valorConvertido);

                        break;
                    case 2:
                        taxaReal = conversor.requestData(endpoints.get("BRL"), "USD");
                        valorConvertido = valor * taxaReal;
                        System.out.println("Valor convertido em reais: R$" + valorConvertido);
                        break;
                    case 3:
                        taxaPeso = conversor.requestData(endpoints.get("ARS"), "BRL");
                        valorConvertido = valor * taxaPeso;
                        System.out.println("Valor convertido em pesos: $" + valorConvertido);
                        break;
                    case 4:
                        taxaReal = conversor.requestData(endpoints.get("BRL"), "ARS");
                        System.out.println(endpoints.get("BRL"));
                        valorConvertido = valor * taxaReal;
                        System.out.println("Valor convertido em reais: R$" + valorConvertido);
                        break;
                    case 5:
                        valorConvertido = valor * taxaEuro;
                        System.out.println("Valor convertido em euros: €" + valorConvertido);
                        break;
                    case 6:
                        valorConvertido = valor * taxaReal;
                        System.out.println("Valor convertido em reais: R$" + valorConvertido);
                        break;
                }
            } else {
                System.out.println("Opção inválida. Por favor, tente novamente.");
            }
        }
    }
}
