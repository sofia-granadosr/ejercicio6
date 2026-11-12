import java.util.ArrayList;
import java.util.Collections;

public class Controlador {
    private ArrayList<IntegracionAPI> integraciones;
    private VistaConsola vista;

    public Controlador(VistaConsola vista) {
        this.vista = vista;
        this.integraciones = new ArrayList<IntegracionAPI>();
        cargarInicial();
    }

    private void cargarInicial() {
        integraciones.add(new TarjetaCredito("PAG-01", "Visanet", "Visanet Guatemala", "https://api.visanet.com.gt", 180, true, 2.5, "Visa"));
        integraciones.add(new TarjetaCredito("PAG-02", "Credomatic", "Credomatic", "https://api.credomatic.com", 210, true, 2.8, "Mastercard"));
        integraciones.add(new TransferenciaBancaria("PAG-03", "BAM Transferencias", "Banco Agromercantil", "https://api.bam.com.gt", 320, true, 1.0, "BAM"));
        integraciones.add(new TransferenciaBancaria("PAG-04", "Banrural Transferencias", "Banrural", "https://api.banrural.com.gt", 410, false, 1.2, "Banrural"));
        integraciones.add(new BilleteraDigital("PAG-05", "Tigo Money", "Tigo", "https://api.tigomoney.gt", 150, true, 1.5, "GTQ"));
        integraciones.add(new BilleteraDigital("PAG-06", "PayPal", "PayPal Inc", "https://api.paypal.com", 270, true, 3.4, "USD"));
        integraciones.add(new ProveedorLogistico("LOG-01", "Forza Delivery", "Forza", "https://api.forza.com.gt", 230, true, 12.5));
        integraciones.add(new ProveedorLogistico("LOG-02", "Cargo Expreso", "Cargo Expreso", "https://api.cargoexpreso.com", 350, true, 10.0));
        integraciones.add(new CertificadorFEL("FEL-01", "Infile", "Infile S.A.", "https://api.infile.com.gt", 140, true, "1234567-8"));
        integraciones.add(new CertificadorFEL("FEL-02", "Digifact", "Digifact", "https://api.digifact.com.gt", 190, true, "8765432-1"));
        integraciones.add(new ApiConsulta("CON-01", "Tipo de cambio Banguat", "Banguat", "https://api.banguat.gob.gt", 95, true, "Tipo de cambio del dia"));
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            vista.mostrarMenu();
            opcion = vista.leerEntero("Elija una opcion: ");
            if (opcion == 1) {
                vista.mostrarLista(integraciones);
            } else if (opcion == 2) {
                buscar();
            } else if (opcion == 3) {
                Collections.sort(integraciones);
                vista.mostrarMensaje("Catalogo ordenado por latencia (menor a mayor).");
                vista.mostrarLista(integraciones);
            } else if (opcion == 4) {
                cobrar();
            } else if (opcion == 5) {
                reintegrar();
            } else if (opcion == 6) {
                documentar();
            } else if (opcion == 7) {
                auditar();
            } else if (opcion == 0) {
                vista.mostrarMensaje("Hasta luego.");
            } else {
                vista.mostrarMensaje("Opcion no valida.");
            }
        }
    }

    public IntegracionAPI buscarPorId(String id) {
        for (IntegracionAPI api : integraciones) {
            if (api.getId().equalsIgnoreCase(id)) {
                return api;
            }
        }
        return null;
    }

    private IntegracionAPI pedirApi() {
        String id = vista.leerTexto("Ingrese el id de la API: ");
        IntegracionAPI api = buscarPorId(id);
        if (api == null) {
            vista.mostrarMensaje("No existe una API con ese id.");
        }
        return api;
    }

    private void buscar() {
        IntegracionAPI api = pedirApi();
        if (api != null) {
            vista.mostrarMensaje(api.getConfiguracion());
        }
    }

    private void cobrar() {
        IntegracionAPI api = pedirApi();
        if (api != null) {
            double monto = vista.leerDecimal("Monto: Q");
            if (monto <= 0) {
                vista.mostrarMensaje("Monto no valido.");
            } else {
                vista.mostrarMensaje(procesarCobro(api, monto));
            }
        }
    }

    private void reintegrar() {
        IntegracionAPI api = pedirApi();
        if (api != null) {
            double monto = vista.leerDecimal("Monto: Q");
            if (monto <= 0) {
                vista.mostrarMensaje("Monto no valido.");
            } else {
                vista.mostrarMensaje(procesarReintegro(api, monto));
            }
        }
    }

    private void documentar() {
        IntegracionAPI api = pedirApi();
        if (api != null) {
            vista.mostrarMensaje(procesarDocumento(api));
        }
    }

    private void auditar() {
        IntegracionAPI api = pedirApi();
        if (api != null) {
            vista.mostrarMensaje(procesarAuditoria(api));
        }
    }

    public String procesarCobro(Transaccional t, double monto) {
        return t.cobrar(monto);
    }

    public String procesarCobro(IntegracionAPI api, double monto) {
        if (api instanceof Transaccional) {
            return procesarCobro((Transaccional) api, monto);
        }
        return "Peticion ignorada: " + api.getNombre() + " no permite cobros.";
    }

    public String procesarReintegro(Transaccional t, double monto) {
        return t.reintegrar(monto);
    }

    public String procesarReintegro(IntegracionAPI api, double monto) {
        if (api instanceof Transaccional) {
            return procesarReintegro((Transaccional) api, monto);
        }
        return "Peticion ignorada: " + api.getNombre() + " no permite reintegros.";
    }

    public String procesarDocumento(Documentable d) {
        return d.generarDocumento();
    }

    public String procesarDocumento(IntegracionAPI api) {
        if (api instanceof Documentable) {
            return procesarDocumento((Documentable) api);
        }
        return "Peticion ignorada: " + api.getNombre() + " no genera documentos.";
    }

    public String procesarAuditoria(Auditable a) {
        return a.obtenerRegistro();
    }

    public String procesarAuditoria(IntegracionAPI api) {
        if (api instanceof Auditable) {
            return procesarAuditoria((Auditable) api);
        }
        return "Peticion ignorada: " + api.getNombre() + " no es auditable.";
    }
}
