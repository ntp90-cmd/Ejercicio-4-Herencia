import java.util.ArrayList;
import java.util.Locale;

public class RentaMovil {
    private ArrayList<Vehiculo> vehiculos;
    private double ingresosAcumulados;

    public RentaMovil() {
        vehiculos = new ArrayList<>();
        ingresosAcumulados = 0.0;
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo no puede ser null.");
        }

        for (Vehiculo registrado : vehiculos) {
            if (registrado.getPlaca().equals(vehiculo.getPlaca())) {
                throw new IllegalArgumentException(
                        "Ya existe un vehiculo con la placa " + vehiculo.getPlaca() + ".");
            }
        }

        vehiculos.add(vehiculo);
    }

    public Vehiculo buscarVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacia.");
        }

        String placaBuscada = placa.trim().toUpperCase(Locale.ROOT);

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equals(placaBuscada)) {
                return vehiculo;
            }
        }

        throw new IllegalArgumentException(
                "No existe un vehiculo con la placa " + placaBuscada + ".");
    }

    public String consultarFlota() {
        if (vehiculos.isEmpty()) {
            return "No hay vehiculos registrados.";
        }

        StringBuilder resultado = new StringBuilder("FLOTA DE RENTAMOVIL\n");

        for (Vehiculo vehiculo : vehiculos) {
            resultado.append(vehiculo).append('\n');
        }

        return resultado.toString();
    }

    public double cotizar(String placa, int dias) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        return vehiculo.calcularCosto(dias);
    }

    public double confirmarAlquiler(String placa, int dias) {
        Vehiculo vehiculo = buscarVehiculo(placa);

        if (!vehiculo.isDisponible()) {
            throw new IllegalStateException(
                    "El vehiculo ya esta alquilado. Debe devolverse antes de otro alquiler.");
        }

        double total = vehiculo.calcularCosto(dias);
        double nuevoIngreso = ingresosAcumulados + total;

        if (!Double.isFinite(nuevoIngreso)) {
            throw new IllegalArgumentException("El total de ingresos seria demasiado grande.");
        }

        vehiculo.alquilar();
        ingresosAcumulados = nuevoIngreso;

        return total;
    }

    public void devolverVehiculo(String placa) {
        buscarVehiculo(placa).devolver();
    }

    public double getIngresosAcumulados() {
        return ingresosAcumulados;
    }

    public String generarReporte() {
        ArrayList<String> categorias = new ArrayList<>();
        int disponibles = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.isDisponible()) {
                disponibles++;
            }

            if (!categorias.contains(vehiculo.getCategoria())) {
                categorias.add(vehiculo.getCategoria());
            }
        }

        StringBuilder reporte = new StringBuilder("REPORTE GENERAL\n");

        reporte.append("Vehiculos registrados: ").append(vehiculos.size()).append('\n');
        reporte.append("Disponibles: ").append(disponibles).append('\n');
        reporte.append("Alquilados: ").append(vehiculos.size() - disponibles).append('\n');

        for (String categoria : categorias) {
            reporte.append(categoria)
                    .append(" | Registrados: ").append(contarVehiculos(categoria))
                    .append(" | Disponibles: ").append(contarDisponibles(categoria))
                    .append(" | Alquilados: ").append(contarAlquilados(categoria))
                    .append('\n');
        }

        reporte.append(String.format(Locale.US,
                "Ingresos acumulados: Q%.2f", ingresosAcumulados));

        return reporte.toString();
    }

    private int contarVehiculos(String categoria) {
        int cantidad = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getCategoria().equals(categoria)) {
                cantidad++;
            }
        }

        return cantidad;
    }

    private int contarDisponibles(String categoria) {
        int cantidad = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getCategoria().equals(categoria) && vehiculo.isDisponible()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    private int contarAlquilados(String categoria) {
        return contarVehiculos(categoria) - contarDisponibles(categoria);
    }
}