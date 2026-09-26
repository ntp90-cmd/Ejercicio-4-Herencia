public class Automovil extends Vehiculo {
    private int cantidadPasajeros;
    private boolean automatico;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria,
                     int cantidadPasajeros, boolean automatico) {
        super(placa, marca, modelo, tarifaDiaria);

        if (cantidadPasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero.");
        }

        this.cantidadPasajeros = cantidadPasajeros;
        this.automatico = automatico;
    }

    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public boolean isAutomatico() {
        return automatico;
    }

    @Override
    public double calcularCosto(int dias) {
        double costo = super.calcularCosto(dias);

        if (automatico) {
            costo += 50.0 * dias;
        }

        if (!Double.isFinite(costo)) {
            throw new IllegalArgumentException("El costo calculado es demasiado grande.");
        }

        return costo;
    }

    @Override
    public String getCategoria() {
        return "Automovil";
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Pasajeros: " + cantidadPasajeros
                + " | Transmision: " + (automatico ? "Automatica" : "Manual");
    }
}