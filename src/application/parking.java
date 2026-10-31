package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

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

        int opcion;

        do {
            System.out.println("\n===== SISTEMA DE PARQUEADERO =====");
            System.out.println("1. Registrar entrada");
            System.out.println("2. Registrar salida");
            System.out.println("3. Reporte de ocupacion");
            System.out.println("4. Reporte financiero");
            System.out.println("5. Salir");

            opcion = validarDato(
                    "Seleccione una opcion: ", 1, 5
            );

            switch (opcion) {

                case 1:
                    registroEntrada();
                    break;

                case 2:
                    registroSalida();
                    break;

                case 3:
                    reporteOcupacion();
                    break;

                case 4:
                    reporteFinanciero();
                    break;

                case 5:
                    System.out.println("Programa finalizado.");
                    break;
            }

        } while (opcion != 5);

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


    // Metodo para registrar la salida de un vehiculo
    static void registroSalida() {

        // Comprobar si existen vehiculos registrados
        if (celdasOcupadas == 0) {
            System.out.println("No hay vehiculos en el parqueadero.");
            return;
        }

        // Solicitar la celda del vehiculo
        int numeroCelda = validarDato(
                "Ingrese el numero de celda (1-10): ",
                1, CAPACIDAD_TOTAL
        );

        // Convertir numero de celda a posicion del arreglo
        int celda = numeroCelda - 1;

        // Comprobar si la celda esta ocupada
        if (tiposVehiculos[celda] == null) {
            System.out.println("La celda seleccionada esta libre.");
            return;
        }

        // Consultar los datos del vehiculo
        String tipo = tiposVehiculos[celda];
        LocalDateTime fechaIngreso = fechasIngreso[celda];

        // Solicitar y validar la fecha de salida
        LocalDateTime fechaSalida;

        do {
            fechaSalida = leerFechaHora(
                    "Fecha y hora de salida (yyyy-MM-dd HH:mm): "
            );

            if (fechaSalida.isBefore(fechaIngreso)) {
                System.out.println(
                        "La salida no puede ser anterior al ingreso."
                );
            }

        } while (fechaSalida.isBefore(fechaIngreso));

        // Mostrar los datos de la salida
        System.out.println("Salida validada correctamente.");
        System.out.println("Celda: " + numeroCelda);
        System.out.println("Vehiculo: " + tipo);
        System.out.println("Ingreso: " +
                fechaIngreso.format(FORMATO_FECHA_HORA));
        System.out.println("Salida: " +
                fechaSalida.format(FORMATO_FECHA_HORA));


        // Calcular el valor a pagar
        long total = liquidacion(
                tipo, fechaIngreso, fechaSalida
        );

        // Actualizar el recaudo segun el vehiculo
        if (tipo.equals("Carro")) {
            recaudoCarros += total;
        } else {
            recaudoMotos += total;
        }

        // Liberar la celda
        tiposVehiculos[celda] = null;
        fechasIngreso[celda] = null;

        // Actualizar la ocupacion
        celdasOcupadas--;
        celdasLibres++;

        System.out.println("Pago registrado correctamente.");
        System.out.println("Celda liberada: " + numeroCelda);

    }


    // Metodo para calcular el valor del parqueo
    static long liquidacion(
            String tipo,
            LocalDateTime fechaIngreso,
            LocalDateTime fechaSalida) {

        // Calcular los dias completos de 24 horas
        long dias = ChronoUnit.DAYS.between(
                fechaIngreso, fechaSalida
        );

        // Calcular los minutos restantes
        long minutos = Duration.between(
                fechaIngreso.plusDays(dias),
                fechaSalida
        ).toMinutes();

        // Redondear las horas restantes hacia arriba
        long horas = (minutos + 59) / 60;

        // Seleccionar las tarifas segun el vehiculo
        int tarifaHora;
        int tarifaDia;

        if (tipo.equals("Carro")) {
            tarifaHora = TARIFA_HORA_CARRO;
            tarifaDia = TARIFA_DIA_CARRO;
        } else {
            tarifaHora = TARIFA_HORA_MOTO;
            tarifaDia = TARIFA_DIA_MOTO;
        }

        // Si quedan 8 horas o mas, cobrar otro dia
        if (horas >= UMBRAL_HORAS_DIA) {
            dias++;
            horas = 0;
        }

        // Calcular el total
        long total = dias * tarifaDia + horas * tarifaHora;

        // Mostrar el detalle de la liquidacion
        System.out.println(" LIQUIDACION ");
        System.out.println("Dias cobrados: " + dias);
        System.out.println("Horas cobradas: " + horas);
        System.out.println("Tarifa diaria: $" + tarifaDia);
        System.out.println("Tarifa por hora: $" + tarifaHora);
        System.out.println("Total a pagar: $" + total);

        return total;
    }


    // Metodo para mostrar la ocupacion del parqueadero
    static void reporteOcupacion() {

        System.out.println(" REPORTE DE OCUPACION ");
        System.out.println("Capacidad total: " + CAPACIDAD_TOTAL);
        System.out.println("Celdas ocupadas: " + celdasOcupadas);
        System.out.println("Celdas disponibles: " + celdasLibres);
    }


    // Metodo para mostrar el recaudo del parqueadero
    static void reporteFinanciero() {

        // Sumar el recaudo de carros y motos
        long recaudoTotal = recaudoCarros + recaudoMotos;

        System.out.println(" REPORTE FINANCIERO ");
        System.out.println("Recaudo de carros: $" + recaudoCarros);
        System.out.println("Recaudo de motos: $" + recaudoMotos);
        System.out.println("Recaudo total: $" + recaudoTotal);
    }


}
