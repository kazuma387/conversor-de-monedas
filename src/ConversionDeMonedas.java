import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class ConversionDeMonedas {
    private List<String> historial = new ArrayList<>();

    // traer informacion del API de conversiones
    public TipoDeCambio monedaUsada(String monedaOrigen) {
        String direccion = "https://v6.exchangerate-api.com/v6/d0c996c7437ba63677dddb06/latest/" + monedaOrigen;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return new Gson().fromJson(response.body(), TipoDeCambio.class);
        } catch (Exception e) {
            throw new RuntimeException("Moneda no encontrada");
        }
    }

    // para convertir una cantidad colocando la moneda origen y la de destino
    public double convertirCantidad(double cantidad, String monedaOrigen, String monedaDestino) {
        TipoDeCambio tipoDeCambio = monedaUsada(monedaOrigen);
        double tasaDeCambio = tipoDeCambio.getConversion_rates().get(monedaDestino);
        double resultado = Math.round((tasaDeCambio * cantidad) * 10000.0) / 10000.0;

        // Registrar la conversión
        LocalDateTime ahora = LocalDateTime.now();
        String registro = String.format("%s: %.2f %s = %.2f %s", 
            ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            cantidad, monedaOrigen, resultado, monedaDestino);
        historial.add(registro);
        return resultado;
    }

    // para saber que monedas estan disponibles para hacer las conversiones
    public List<String> monedasDisponibles() {
    TipoDeCambio tipoDeCambio = monedaUsada("USD");
    return new ArrayList<>(tipoDeCambio.getConversion_rates().keySet());
   }

   // para el historial de conversiones hechas
   public List<String> historialDeConversiones() {
    return new ArrayList<>(historial);
   }

   // para saber cual fue la ultima conversion
   public String getUltimaConversion() {
    if (!historial.isEmpty()) {
        return historial.get(historial.size() - 1);
    }
    return null;
}
}
