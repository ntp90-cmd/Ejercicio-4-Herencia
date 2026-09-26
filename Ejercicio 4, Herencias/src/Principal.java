import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Principal {
    private static Scanner entrada;
    private static RentaMovil empresa;

    public static void main(String[] args) {
        entrada = new Scanner(System.in);
        empresa = new RentaMovil();

        try {
            cargarDatosIniciales();

            System.out.println("RENTAMOVIL");
            System.out.println("Se cargaron seis vehiculos disponibles. Ingresos iniciales: Q0.00.");

            boolean continuar = true;

            while (continuar) {
                try {
                    mostrarMenu();
                    continuar = ejecutarOpcion(leerEntero("Seleccione una opcion: "));
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nLa entrada ha finalizado.");
        } finally {
            entrada.close();
            System.out.println("Programa finalizado.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n----- Menu -----"
                + "\n1. Registrar vehiculo"
                + "\n2. Consultar flota"
                + "\n3. Cotizar alquiler"
                + "\n4. Confirmar alquiler"
                + "\n5. Registrar devolucion"
                + "\n6. Mostrar reporte general"
                + "\n7. Salir");
    }

    private static boolean ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: {
                System.out.println("1. Automovil\n2. Motocicleta\n3. Camioneta de carga");

                int tipo = leerEntero("Tipo de vehiculo: ");

                if (tipo < 1 || tipo > 3) {
                    throw new IllegalArgumentException("Seleccione un tipo del 1 al 3.");
                }

                String placa = leerTexto("Placa: ");
                String marca = leerTexto("Marca: ");
                String modelo = leerTexto("Modelo: ");
                double tarifa = leerDecimal("Tarifa diaria en quetzales (mayor que 0): ");

                Vehiculo vehiculo;

                if (tipo == 1) {
                    int pasajeros = leerEntero("Cantidad de pasajeros (mayor que 0): ");
                    boolean automatico = leerConfirmacion(
                            "Tiene transmision automatica? (s/n): ");

                    vehiculo = new Automovil(
                            placa, marca, modelo, tarifa, pasajeros, automatico);
                } else if (tipo == 2) {
                    int cilindraje = leerEntero("Cilindraje en cc (mayor que 0): ");

                    vehiculo = new Motocicleta(
                            placa, marca, modelo, tarifa, cilindraje);
                } else {
                    double capacidad = leerDecimal(
                            "Capacidad maxima en toneladas (ejemplo: 1.5): ");

                    vehiculo = new CamionetaCarga(
                            placa, marca, modelo, tarifa, capacidad);
                }

                empresa.registrarVehiculo(vehiculo);
                System.out.println("Vehiculo registrado y disponible.");
                break;
            }

            case 2:
                System.out.println(empresa.consultarFlota());
                break;

            case 3: {
                String placa = leerTexto("Placa del vehiculo: ");
                Vehiculo vehiculo = empresa.buscarVehiculo(placa);
                int dias = leerEntero("Dias de alquiler (entero mayor que 0): ");
                double total = empresa.cotizar(placa, dias);

                System.out.println(vehiculo);
                System.out.printf(Locale.US,
                        "Cotizacion por %d dia(s): Q%.2f%n", dias, total);
                System.out.println(
                        "La cotizacion no confirma el alquiler ni modifica los ingresos.");
                break;
            }

            case 4: {
                String placa = leerTexto("Placa del vehiculo: ");
                Vehiculo vehiculo = empresa.buscarVehiculo(placa);

                if (!vehiculo.isDisponible()) {
                    throw new IllegalStateException(
                            "El vehiculo ya esta alquilado. Debe devolverse antes de otro alquiler.");
                }

                int dias = leerEntero("Dias de alquiler (entero mayor que 0): ");
                double total = empresa.cotizar(placa, dias);

                System.out.println(vehiculo);
                System.out.printf(Locale.US,
                        "Total por %d dia(s): Q%.2f%n", dias, total);

                if (leerConfirmacion("Confirma el alquiler? (s/n): ")) {
                    double cobrado = empresa.confirmarAlquiler(placa, dias);

                    System.out.printf(Locale.US,
                            "Alquiler confirmado. Cobro registrado: Q%.2f%n", cobrado);
                } else {
                    System.out.println(
                            "Operacion cancelada. Disponibilidad e ingresos sin cambios.");
                }
                break;
            }

            case 5: {
                String placa = leerTexto("Placa del vehiculo a devolver: ");
                empresa.devolverVehiculo(placa);

                System.out.println(
                        "Devolucion registrada. Vehiculo disponible e ingresos sin cambios.");
                break;
            }

            case 6:
                System.out.println(empresa.generarReporte());
                break;

            case 7:
                return false;

            default:
                System.out.println("Opcion invalida. Escriba un numero del 1 al 7.");
        }

        return true;
    }

    private static void cargarDatosIniciales() {
        empresa.registrarVehiculo(
                new Automovil("A001", "Toyota", "Yaris", 150, 5, false));

        empresa.registrarVehiculo(
                new Automovil("A002", "Honda", "Civic", 150, 5, true));

        empresa.registrarVehiculo(
                new Motocicleta("M001", "Yamaha", "FZ", 100, 250));

        empresa.registrarVehiculo(
                new Motocicleta("M002", "Honda", "CB", 100, 300));

        empresa.registrarVehiculo(
                new CamionetaCarga("C001", "Toyota", "Hilux", 200, 1.5));

        empresa.registrarVehiculo(
                new CamionetaCarga("C002", "Isuzu", "D-Max", 250, 2.0));
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("Error: este dato no puede estar vacio.");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: escriba un numero entero, sin letras ni decimales. Ejemplo: 3.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                double numero = Double.parseDouble(entrada.nextLine().trim());

                if (Double.isFinite(numero)) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: escriba un numero valido. Use punto para los decimales, por ejemplo 1.5.");
                continue;
            }

            System.out.println("Error: escriba un numero finito.");
        }
    }

    private static boolean leerConfirmacion(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase(Locale.ROOT);

            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }

            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }

            System.out.println("Error: responda s para si o n para no.");
        }
    }
}