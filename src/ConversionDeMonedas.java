import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import com.google.gson.Gson;

public class ConversionDeMonedas {
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/d0c996c7437ba63677dddb06/latest/";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /*
     * new ArrayList<>() crea una nueva lista vacía.
     * Collections.synchronizedList() envuelve esta lista en una versión sincronizada.
     */
    private final List<String> historial = Collections.synchronizedList(new ArrayList<>());

    /*
     * Obtiene la información de tipo de cambio para una moneda específica.
     * @param monedaOrigen Código de la moneda de origen
     * @return Objeto TipoDeCambio con la información de conversión
     * @throws RuntimeException si la moneda no se encuentra o hay un error en la API
     */
    public TipoDeCambio monedaUsada(String monedaOrigen) {
        String direccion = API_BASE_URL + monedaOrigen;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), TipoDeCambio.class);
        } catch (Exception e) {
            throw new RuntimeException("Moneda no encontrada o error en la API", e);
        }
    }

    /*
     * Convierte una cantidad de una moneda a otra.
     * @param cantidad: Cantidad a convertir
     * @param monedaOrigen: Código de la moneda de origen
     * @param monedaDestino: Código de la moneda de destino
     * @return Cantidad convertida
     */
    public double convertirCantidad(double cantidad, String monedaOrigen, String monedaDestino) {
        TipoDeCambio tipoDeCambio = monedaUsada(monedaOrigen);
        double tasaDeCambio = tipoDeCambio.getConversion_rates().get(monedaDestino);
        double resultado = Math.round((tasaDeCambio * cantidad) * 10000.0) / 10000.0;

        registrarConversion(cantidad, monedaOrigen, resultado, monedaDestino);
        return resultado;
    }

    /*
     * Registra una conversión en el historial.
     * @param cantidadOrigen: Cantidad original
     * @param monedaOrigen: Código de la moneda de origen
     * @param cantidadDestino: Cantidad convertida
     * @param monedaDestino: Código de la moneda de destino
     */
    private void registrarConversion(double cantidadOrigen, String monedaOrigen, double cantidadDestino, String monedaDestino) {
        String registro = String.format("%s: %.2f %s = %.2f %s", 
            LocalDateTime.now().format(formatter),
            cantidadOrigen, monedaOrigen, cantidadDestino, monedaDestino);
        historial.add(registro);
    }

    /*
     * Obtiene la lista de monedas disponibles para conversión.
     * @return Lista de códigos de monedas disponibles
     */
    public List<String> monedasDisponibles() {
        TipoDeCambio tipoDeCambio = monedaUsada("USD");
        return new ArrayList<>(tipoDeCambio.getConversion_rates().keySet());
    }

    /*
     * Obtiene el historial completo de conversiones.
     * @return Lista de conversiones realizadas
     */
    public List<String> historialDeConversiones() {
        return new ArrayList<>(historial);
    }

    /*
     * Obtiene la última conversión realizada.
     * @return String con la información de la última conversión, o null si no hay conversiones
     */
    public String getUltimaConversion() {
        return historial.isEmpty() ? null : historial.get(historial.size() - 1);
    }
}
