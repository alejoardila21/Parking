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
        registroEntrada();
        SCANNER.close();
    }


    // Metodo para registrar la entrada de un vehiculo
    static void registroEntrada() {

        // Buscar una celda disponible
        int celda = celdaDisponible();

        // Comprobar si el parqueadero esta lleno
        if (celda == -1) {
            System.out.println("El parqueadero esta lleno.");
            return;
        }


        // Solicitar el tipo de vehiculo
        System.out.println("1. Carro");
        System.out.println("2. Moto");

        int tipo = validarDato(
                "Seleccione el tipo de vehiculo: ", 1, 2
        );


        // Solicitar y validar la fecha y hora de ingreso
        LocalDateTime fechaIngreso = leerFechaHora(
                "Fecha y hora de ingreso (yyyy-MM-dd HH:mm): "
        );

        // Guardar los datos en los arreglos
        tiposVehiculos[celda] = tipo == 1 ? "Carro" : "Moto";
        fechasIngreso[celda] = fechaIngreso;

        // Actualizar la ocupacion
        celdasOcupadas++;
        celdasLibres--;

        System.out.println("Vehiculo registrado correctamente.");
        System.out.println("Celda asignada: " + (celda + 1));
    }


    // Metodo para buscar una celda disponible
    static int celdaDisponible() {

        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {

            if (tiposVehiculos[i] == null) {
                return i;
            }
        }

        return -1;
    }


    // Metodo para leer campos numericos de una fecha
    static int leerDigitos(String texto, int inicio, int cantidad) {

        int valor = 0;

        for (int i = inicio; i < inicio + cantidad; i++) {

            char caracter = texto.charAt(i);

            if (caracter < '0' || caracter > '9') {
                return -1;
            }

            valor = valor * 10 + caracter - '0';
        }

        return valor;
    }


    // Metodo para validar el formato y los rangos
    static boolean formatoFechaHoraValido(String texto) {

        if (texto.length() != 16
                || texto.charAt(4) != '-'
                || texto.charAt(7) != '-'
                || texto.charAt(10) != ' '
                || texto.charAt(13) != ':') {
            return false;
        }

        int anio = leerDigitos(texto, 0, 4);
        int mes = leerDigitos(texto, 5, 2);
        int dia = leerDigitos(texto, 8, 2);
        int hora = leerDigitos(texto, 11, 2);
        int minuto = leerDigitos(texto, 14, 2);

        return anio >= 1
                && mes >= 1 && mes <= 12
                && dia >= 1 && dia <= 31
                && hora >= HORA_APERTURA
                && hora <= HORA_CIERRE
                && (hora != HORA_CIERRE || minuto == 0)
                && minuto >= 0 && minuto <= 59;
    }


    // Metodo para solicitar y validar fecha y hora
    static LocalDateTime leerFechaHora(String mensaje) {

        while (true) {

            System.out.print(mensaje);
            String texto = SCANNER.nextLine().trim();

            if (!formatoFechaHoraValido(texto)) {
                System.out.println(
                        "Fecha u hora invalidas. Use yyyy-MM-dd HH:mm "
                                + "y el horario 08:00-22:00."
                );
                continue;
            }

            LocalDateTime fecha;

            try {
                fecha = LocalDateTime.parse(
                        texto, FORMATO_FECHA_HORA
                );
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("La fecha ingresada no existe.");
                continue;
            }

            // Comprobar que el calendario no haya ajustado la fecha
            if (!fecha.format(FORMATO_FECHA_HORA).equals(texto)) {
                System.out.println("La fecha ingresada no existe.");
                continue;
            }

            return fecha;
        }
    }


    // Metodo para validar datos numericos
    static int validarDato(String mensaje, int minimo, int maximo) {

        while (true) {

            System.out.print(mensaje);
            String texto = SCANNER.nextLine().trim();

            try {
                int numero = Integer.parseInt(texto);

                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }

            } catch (NumberFormatException e) {
                // El dato ingresado no es un numero entero
            }

            System.out.println("Dato invalido. Intente nuevamente.");
        }
    }

}
