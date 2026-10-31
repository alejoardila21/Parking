package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class parking {

    // Constantes del parqueadero
    static final int CAPACIDAD_TOTAL = 10;

    static final int TARIFA_HORA_CARRO = 4000;
    static final int TARIFA_DIA_CARRO = 30000;

    static final int TARIFA_HORA_MOTO = 2000;
    static final int TARIFA_DIA_MOTO = 15000;

    static final int UMBRAL_HORAS_DIA = 8;

    static final int HORA_APERTURA = 8;
    static final int HORA_CIERRE = 22;

    // Formato de fecha y hora
    static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Scanner para leer datos
    static final Scanner SCANNER = new Scanner(System.in);

    // Arreglos paralelos
    static String[] tiposVehiculos = new String[CAPACIDAD_TOTAL];

    static LocalDateTime[] fechasIngreso =
            new LocalDateTime[CAPACIDAD_TOTAL];

    // Variables de ocupacion
    static int celdasOcupadas = 0;
    static int celdasLibres = CAPACIDAD_TOTAL;

    // Variables de recaudo
    static long recaudoCarros = 0;
    static long recaudoMotos = 0;

    public static void main(String[] args) {

    }

    // Metodo para registrar la entrada de un vehiculo
    static void registroEntrada() {

        int celda = -1;

        // Buscar la primera celda libre
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (tiposVehiculos[i] == null) {
                celda = i;
                break;
            }
        }

        // Comprobar si el parqueadero esta lleno
        if (celda == -1) {
            System.out.println("El parqueadero esta lleno.");
            return;
        }

        // Solicitar el tipo de vehiculo
        String tipo;

        do {
            System.out.println("1. Carro");
            System.out.println("2. Moto");
            System.out.print("Seleccione el tipo de vehiculo: ");

            tipo = SCANNER.nextLine().trim();

            if (!tipo.equals("1") && !tipo.equals("2")) {
                System.out.println("Opcion invalida.");
            }

        } while (!tipo.equals("1") && !tipo.equals("2"));

        // Solicitar la fecha y hora de ingreso
        System.out.print("Fecha y hora de ingreso (yyyy-MM-dd HH:mm): ");
        String fechaTexto = SCANNER.nextLine().trim();

        LocalDateTime fechaIngreso =
                LocalDateTime.parse(fechaTexto, FORMATO_FECHA_HORA);

        // Guardar los datos en los arreglos
        tiposVehiculos[celda] = tipo.equals("1") ? "Carro" : "Moto";
        fechasIngreso[celda] = fechaIngreso;

        // Actualizar la ocupacion
        celdasOcupadas++;
        celdasLibres--;

        System.out.println("Vehiculo registrado correctamente.");
        System.out.println("Celda asignada: " + (celda + 1));
    }

}
