public class CamionetaCarga extends Vehiculo {
    private double capacidadToneladas;

    public CamionetaCarga(String placa, String marca, String modelo, double tarifaDiaria,
                         double capacidadToneladas) {
        super(placa, marca, modelo, tarifaDiaria);

        if (!Double.isFinite(capacidadToneladas) || capacidadToneladas <= 0) {
            throw new IllegalArgumentException("La capacidad de carga debe ser mayor que cero y finita.");
        }

        this.capacidadToneladas = capacidadToneladas;
    }

    public double getCapacidadToneladas() {
        return capacidadToneladas;
    }

    @Override
    public double calcularCosto(int dias) {
        double costo = super.calcularCosto(dias) + 100.0 * capacidadToneladas * dias;

        if (!Double.isFinite(costo)) {
            throw new IllegalArgumentException("El costo calculado es demasiado grande.");
        }

        return costo;
    }

    @Override
    public String getCategoria() {
        return "Camioneta de carga";
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Capacidad maxima: " + capacidadToneladas + " toneladas";
    }
}