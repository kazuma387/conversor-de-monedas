import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Principal {
     // Constantes y variables estáticas para uso en toda la clase
    private static final String CONVERSION_FILE = "conversions.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Scanner scanner = new Scanner(System.in);
    private static final ConversionDeMonedas conversion = new ConversionDeMonedas();

    /*
     * Guarda la lista de conversiones en un archivo JSON.
     * @param conversions: Lista de conversiones a guardar
     */
    private static void guardarConversiones(List<String> conversions) {
        try (FileWriter writer = new FileWriter(CONVERSION_FILE)) {
            gson.toJson(conversions, writer);
        } catch (IOException e) {
            System.out.println("Error al guardar conversiones: " + e.getMessage());
        }
    }

    /*
     * Carga la lista de conversiones desde un archivo JSON.
     * @return Lista de conversiones cargadas o una lista vacía si hay error
     */
    private static List<String> cargarConversiones() {
        List<String> conversions = new ArrayList<>();
        File file = new File(CONVERSION_FILE);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                conversions = gson.fromJson(reader, type);
            } catch (IOException e) {
                System.out.println("Error al cargar conversiones: " + e.getMessage());
            }
        }
        return conversions;
    }

    /*
     * Actualiza la lista de conversiones con la última conversión realizada.
     * @param conversion Objeto ConversionDeMonedas que contiene la última conversión
     * @param totalConversiones: Lista total de conversiones a actualizar
     */
    private static void actualizarConversiones(ConversionDeMonedas conversion, List<String> totalConversiones) {
        String ultimaConversion = conversion.getUltimaConversion();
        if (ultimaConversion != null && !totalConversiones.contains(ultimaConversion)) {
            totalConversiones.add(ultimaConversion);
            guardarConversiones(totalConversiones);
        }
    }

    /*
     * Realiza una conversión de moneda y muestra el resultado.
     * @param origen: Código de la moneda de origen
     * @param destino: Código de la moneda de destino
     */
    private static void realizarConversion(String origen, String destino) {
        try {
            System.out.println("Ingrese el valor a convertir:");
            double cantidad = scanner.nextDouble();
            double convertido = conversion.convertirCantidad(cantidad, origen, destino);
            System.out.printf("El valor de %.2f [%s] es equivalente a %.2f [%s]%n", cantidad, origen, convertido, destino);
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese un número válido.");
        } finally {
            scanner.nextLine(); // Limpiar el buffer
        }
    }

    // Muestra el menú principal de opciones para el usuario.
    private static void mostrarMenu() {
        System.out.println("\n*************************************************");
        System.out.println("Sea bienvenido/a al conversor de monedas");
        System.out.println("\n 1) Dolar =>> Peso Argentino \n 2) Peso Argentino =>> Dolar");
        System.out.println(" 3) Dolar =>> Real brasileño \n 4) Real brasileño =>> Dolar");
        System.out.println(" 5) Dolar =>> Peso colombiano \n 6) Peso colombiano =>> Dolar");
        System.out.println(" 7) Elegir desde una lista de monedas la moneda origen y la de destino");
        System.out.println(" 8) Historial de conversiones \n 9) Salir");
        System.out.println("Elige una opción valida");
        System.out.println("*************************************************\n");
    }

    // Método principal que ejecuta el programa de conversión de monedas.
    public static void main(String[] args) {
        List<String> totalConversiones = cargarConversiones();

        while (true) {
            mostrarMenu();
            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1" -> realizarConversion("USD", "ARS");
                    case "2" -> realizarConversion("ARS", "USD");
                    case "3" -> realizarConversion("USD", "BRL");
                    case "4" -> realizarConversion("BRL", "USD");
                    case "5" -> realizarConversion("USD", "COP");
                    case "6" -> realizarConversion("COP", "USD");
                    case "7" -> {
                        List<String> monedas = conversion.monedasDisponibles();
                        System.out.println("Monedas disponibles: " + monedas);
                        String origen = obtenerMonedaValida("origen", monedas);
                        String destino = obtenerMonedaValida("destino", monedas);
                        realizarConversion(origen, destino);
                    }
                    case "8" -> mostrarHistorial(totalConversiones);
                    case "9" -> {
                        System.out.println("Saliendo del programa...");
                        return;
                    }
                    default -> System.out.println("Opción inválida. Elija un número de las opciones disponibles.");
                }
                actualizarConversiones(conversion, totalConversiones);
            } catch (Exception e) {
                System.out.println("Se ha producido un error: " + e.getClass().getSimpleName());
                System.out.println("Por favor, intente nuevamente.");
            }
        }
    }

    /*
     * Solicita al usuario que ingrese una moneda válida.
     * @param tipo: Tipo de moneda (origen o destino)
     * @param monedas: Lista de monedas válidas
     * @return Código de la moneda seleccionada
     */
    private static String obtenerMonedaValida(String tipo, List<String> monedas) {
        String moneda;
        do {
            System.out.println("Ingresa la moneda de " + tipo + ":");
            moneda = scanner.nextLine().toUpperCase();
            if (!monedas.contains(moneda)) {
                System.out.println("Moneda no encontrada. Por favor, intenta de nuevo.");
            }
        } while (!monedas.contains(moneda) || moneda.isEmpty());
        return moneda;
    }

    /*
     * Muestra el historial de conversiones realizadas.
     * @param totalConversiones: Lista de todas las conversiones realizadas
     */
    private static void mostrarHistorial(List<String> totalConversiones) {
        System.out.println("Historial de conversiones:");
        if (totalConversiones.isEmpty()) {
            System.out.println("No ha realizado ninguna conversión hasta ahora");
        } else {
            totalConversiones.forEach(System.out::println);
        }
    }
}