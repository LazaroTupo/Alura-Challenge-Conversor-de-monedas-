package org.alura.challenges;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private static final String[] CURRENCIES = {
            "USD - Dólar Estadounidense",
            "EUR - Euro",
            "GBP - Libra Esterlina",
            "JPY - Yen Japonés",
            "KRW - Won Surcoreano",
            "MXN - Peso Mexicano",
            "BRL - Real Brasileño",
            "ARS - Peso Argentino",
            "CLP - Peso Chileno",
            "PEN - Sol Peruano",
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(ANSI_CYAN + "\nCONVERSOR DE MONEDAS" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Divisas disponibles:" + ANSI_RESET);
        for (String currency : CURRENCIES) {
            System.out.println("  " + currency);
        }

        // Selección de divisa origen
        String fromCurrency = selectCurrency(scanner, "Ingrese el código de la divisa de origen (ej: USD): ");

        // Selección de divisa destino
        String toCurrency = selectCurrency(scanner, "Ingrese el código de la divisa de destino (ej: EUR): ");

        // Validar que no sean la misma divisa
        while (fromCurrency.equals(toCurrency)) {
            System.out.println(ANSI_RED + "¡No puede convertir la misma divisa!" + ANSI_RESET);
            toCurrency = selectCurrency(scanner, "Ingrese una divisa de destino diferente: ");
        }

        // Ingresar monto a convertir
        double amount = inputAmount(scanner);

        // Realizar conversión
        try {
            double convertedAmount = ApiClient.convertCurrency(fromCurrency, toCurrency, amount);

            System.out.printf("\n%s%.2f %s = %s%.2f %s%s\n",
                    ANSI_GREEN, amount, fromCurrency,
                    ANSI_YELLOW, convertedAmount, toCurrency,
                    ANSI_RESET);

        } catch (IOException e) {
            System.out.println(ANSI_RED + "Error al conectar con la API: " + e.getMessage() + ANSI_RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Error en las divisas: " + e.getMessage() + ANSI_RESET);
        }

        scanner.close();
    }

    private static String selectCurrency(Scanner scanner, String message) {
        while (true) {
            System.out.print(ANSI_BLUE + message + ANSI_RESET);
            String input = scanner.nextLine().trim().toUpperCase();

            // Buscar coincidencia en las divisas disponibles
            for (String currency : CURRENCIES) {
                if (currency.startsWith(input + " ")) {
                    return input; // Devuelve el código de la divisa (USD, EUR, etc.)
                }
            }

            System.out.println(ANSI_RED + "Divisa no válida. Intente nuevamente." + ANSI_RESET);
            System.out.println("Divisas disponibles: ");
            for (String currency : CURRENCIES) {
                System.out.println("  " + currency);
            }
        }
    }

    private static double inputAmount(Scanner scanner) {
        while (true) {
            System.out.print(ANSI_BLUE + "Ingrese el monto a convertir: " + ANSI_RESET);
            String input = scanner.nextLine();

            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println(ANSI_RED + "El monto debe ser mayor a 0." + ANSI_RESET);
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "Ingrese un número válido." + ANSI_RESET);
            }
        }
    }
}