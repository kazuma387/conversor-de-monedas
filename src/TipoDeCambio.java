import java.util.Map;

public class TipoDeCambio {
    private String base_code;
    private Map<String, Double> conversion_rates;

    public String getBase_code() {
        return this.base_code;
    }

    public Map<String, Double> getConversion_rates() {
        return this.conversion_rates;
    }

    @Override
    public String toString() {
        return "Moneda base: " + base_code + "\n" + "tipos de cambio: " + conversion_rates;
    }

    

    

}
