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
    private static void guardarConversionEnArchivo(List<String> conversions) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (FileWriter writer = new FileWriter("conversions.json")) {
        gson.toJson(conversions, writer);
    } catch (IOException e) {
        System.out.println("Error al guardar conversiones: " + e.getMessage());
    }
}

private static List<String> cargarConversionDesdeArchivo() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<String> conversions = new ArrayList<>();
    File file = new File("conversions.json");
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

// actualizar y guardar historial de conversiones
private static void actualizarYGuardarConversion(ConversionDeMonedas conversion, List<String> totalConversiones) {
    String ultimaConversion = conversion.getUltimaConversion();
    if (ultimaConversion != null && !totalConversiones.contains(ultimaConversion)) {
        totalConversiones.add(ultimaConversion);
    }
    guardarConversionEnArchivo(totalConversiones);
}

    public static void main(String[] args) throws Exception {
        
        Scanner lectura = new Scanner(System.in);
        ConversionDeMonedas conversion = new ConversionDeMonedas();

        double cantidad, convertir;
        String monedaOrigen, monedaDestino, opcion;

        // Cargar historial de conversiones
        List<String> totalConversiones = cargarConversionDesdeArchivo();

        while (true) {
            try {
                System.out.println("\n*************************************************");
                System.out.println("Sea bienvenido/a al conversor de monedas");
                System.out.println("\n 1) Dolar =>> Peso Argentino \n 2) Peso Argentino =>> Dolar"); 
                System.out.println(" 3) Dolar =>> Real brasileño \n 4) Real brasileño =>> Dolar");
                System.out.println(" 5) Dolar =>> Peso colombiano \n 6) Peso colombiano =>> Dolar");
                System.out.println(" 7) Elegir desde una lista de monedas la moneda origen y la de destino");
                System.out.println(" 8) Historial de conversiones \n 9) Salir");
                System.out.println("Elige una opción valida");
                System.out.println("*************************************************\n");
                
                opcion = lectura.nextLine();
                switch (opcion) {
                    case "1":
                    System.out.println("Ingrese el valor a convertir:");
                    cantidad = lectura.nextDouble();
                    convertir = conversion.convertirCantidad(cantidad, "USD", "ARS");
                    System.out.println("El valor de " + cantidad + " [USD] es equivalente a "+ convertir + " [ARS]");
                    // Registrar la conversión
                    actualizarYGuardarConversion(conversion, totalConversiones);
                    lectura.nextLine();
                    break;

                    case "2":
                    System.out.println("Ingrese el valor a convertir:");
                    cantidad = lectura.nextDouble();
                    convertir = conversion.convertirCantidad(cantidad, "ARS", "USD");
                    System.out.println("El valor de " + cantidad + " [ARS] es equivalente a "+ convertir + " [USD]");
                    // Registrar la conversión
                    actualizarYGuardarConversion(conversion, totalConversiones);
                    lectura.nextLine();
                    break;

                    case "3":
                    System.out.println("Ingrese el valor a convertir:");
                    cantidad = lectura.nextDouble();
                    convertir = conversion.convertirCantidad(cantidad, "USD", "BRL");
                    System.out.println("El valor de " + cantidad + " [USD] es equivalente a "+ convertir + " [BRL]");
                    // Registrar la conversión
                    actualizarYGuardarConversion(conversion, totalConversiones);
                    lectura.nextLine();
                    break;

                    case "4":
                    System.out.println("Ingrese el valor a convertir:");
                    cantidad = lectura.nextDouble();
                    convertir = conversion.convertirCantidad(cantidad, "BRL", "USD");
                    System.out.println("El valor de " + cantidad + " [BRL] es equivalente a "+ convertir + " [USD]");
                    // Registrar la conversión
                    actualizarYGuardarConversion(conversion, totalConversiones);
                    lectura.nextLine();
                    break;

                    case "5":
                    System.out.println("Ingrese el valor a convertir:");
                    cantidad = lectura.nextDouble();
                    convertir = conversion.convertirCantidad(cantidad, "USD", "COP");
                    System.out.println("El valor de " + cantidad + " [USD] es equivalente a "+ convertir + " [COP]");
                    // Registrar la conversión
                    actualizarYGuardarConversion(conversion, totalConversiones);
                    lectura.nextLine();
                    break;

                    case "6":
                    System.out.println("Ingrese el valor a convertir:");
                    cantidad = lectura.nextDouble();
                    convertir = conversion.convertirCantidad(cantidad, "COP", "USD");
                    System.out.println("El valor de " + cantidad + " [COP] es equivalente a "+ convertir + " [USD]");
                    // Registrar la conversión
                    actualizarYGuardarConversion(conversion, totalConversiones);
                    lectura.nextLine();
                    break;

                    case "7":
                    System.out.println("Lista de monedas disponibles:");
                    List<String> listaDeMonedas = conversion.monedasDisponibles();
                    System.out.println(listaDeMonedas);

                    do {
                        System.out.println("Ingresa la moneda de origen:");
                        monedaOrigen = lectura.nextLine().toUpperCase();
                        if (!listaDeMonedas.contains(monedaOrigen)) {
                            System.out.println("Moneda no encontrada. Por favor, intenta de nuevo.");
                        }
                    } while (!listaDeMonedas.contains(monedaOrigen) || monedaOrigen.isEmpty());

                    do {
                        System.out.println("Ingresa la moneda de destino:");
                        monedaDestino = lectura.nextLine().toUpperCase();
                        if (!listaDeMonedas.contains(monedaDestino)) {
                            System.out.println("Moneda no encontrada. Por favor, intenta de nuevo.");
                        }
                    } while (!listaDeMonedas.contains(monedaDestino) || monedaDestino.isEmpty());

                    System.out.println("Ingresa la cantidad a convertir:");
                    cantidad = lectura.nextDouble();
                    convertir = conversion.convertirCantidad(cantidad, monedaOrigen, monedaDestino);
                    System.out.println("El valor de " + cantidad + " [" + monedaOrigen + "] es equivalente a "+ convertir + " [" + monedaDestino + "]");
                    // Registrar la conversión
                    actualizarYGuardarConversion(conversion, totalConversiones);
                    lectura.nextLine();
                    break;

                    case "8":
                    System.out.println("Historial de conversiones:");
                    if (totalConversiones.isEmpty()) {
                        System.out.println("No ha realizado ninguna conversion hasta ahora");
                        break;
                    } else {
                        totalConversiones.forEach(System.out::println);
                        break;
                    }
                    
                    case "9":
                    guardarConversionEnArchivo(totalConversiones);
                    System.out.println("Salir");
                    break;

                    default:
                    System.out.println("Opcion invalida, elija un numero de las opciones disponibles");
                    break;
                }
                // para salir del programa
                if (opcion.equals("9")) {
                    System.out.println("Saliendo del programa...");
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("solo numeros y si son decimales separarlos por coma");
                System.out.println("Por favor, intente nuevamente.");
                lectura.nextLine();
            } catch (Exception e) {
                System.out.println("Se ha producido un error: " + e.getClass().getSimpleName());
                System.out.println("Por favor, intente nuevamente.");
                lectura.nextLine();
            } 
        } 
        lectura.close();
    }
}
