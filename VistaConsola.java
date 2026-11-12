import java.util.ArrayList;
import java.util.Scanner;

public class VistaConsola {
    private Scanner scanner;

    public VistaConsola() {
        scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println();
        System.out.println("===== INTEGRACIONES API =====");
        System.out.println("1. Listar integraciones");
        System.out.println("2. Buscar API por id");
        System.out.println("3. Ordenar por latencia");
        System.out.println("4. Procesar cobro");
        System.out.println("5. Procesar reintegro");
        System.out.println("6. Generar documento");
        System.out.println("7. Ver registro de auditoria");
        System.out.println("0. Salir");
    }

    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarLista(ArrayList<IntegracionAPI> lista) {
        System.out.println();
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ". " + lista.get(i));
        }
    }
}
