import java.util.Locale;

public abstract class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private double tarifaDiaria;
    private boolean disponible;

    protected Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacia.");
        }

        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacia.");
        }

        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacio.");
        }

        if (!Double.isFinite(tarifaDiaria) || tarifaDiaria <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser mayor que cero y finita.");
        }

        this.placa = placa.trim().toUpperCase(Locale.ROOT);
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tarifaDiaria = tarifaDiaria;
        this.disponible = true;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public double calcularCosto(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias de alquiler deben ser enteros mayores que cero.");
        }

        double costo = tarifaDiaria * dias;

        if (!Double.isFinite(costo)) {
            throw new IllegalArgumentException("El costo calculado es demasiado grande.");
        }

        return costo;
    }

    public void alquilar() {
        if (!disponible) {
            throw new IllegalStateException("El vehiculo ya esta alquilado.");
        }

        disponible = false;
    }

    public void devolver() {
        if (disponible) {
            throw new IllegalStateException("El vehiculo ya esta disponible; no tiene un alquiler activo.");
        }

        disponible = true;
    }

    public abstract String getCategoria();

    @Override
    public String toString() {
        return String.format(Locale.US,
                "Placa: %s | Marca: %s | Modelo: %s | Categoria: %s | Tarifa diaria: Q%.2f | Estado: %s",
                placa, marca, modelo, getCategoria(), tarifaDiaria,
                disponible ? "Disponible" : "Alquilado");
    }
}