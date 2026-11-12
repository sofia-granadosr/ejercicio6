public class Main {
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola();
        Controlador controlador = new Controlador(vista);
        controlador.iniciar();
    }
}
