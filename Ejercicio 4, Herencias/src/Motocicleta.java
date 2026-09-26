public class Motocicleta extends Vehiculo {
    private int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria,
                      int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria);

        if (cilindraje <= 0) {
            throw new IllegalArgumentException("El cilindraje debe ser mayor que cero.");
        }

        this.cilindraje = cilindraje;
    }

    public int getCilindraje() {
        return cilindraje;
    }

    @Override
    public double calcularCosto(int dias) {
        double costo = super.calcularCosto(dias);

        if (cilindraje > 250) {
            costo += 75.0;
        }

        if (!Double.isFinite(costo)) {
            throw new IllegalArgumentException("El costo calculado es demasiado grande.");
        }

        return costo;
    }

    @Override
    public String getCategoria() {
        return "Motocicleta";
    }

    @Override
    public String toString() {
        return super.toString() + " | Cilindraje: " + cilindraje + " cc";
    }
}