interface Transaccional {
    String cobrar(double monto);
    String reintegrar(double monto);
}

interface Documentable {
    String generarDocumento();
}

interface Auditable {
    String obtenerRegistro();
}

abstract class IntegracionAPI implements Comparable<IntegracionAPI> {
    private String id;
    private String nombre;
    private String proveedor;
    private String url;
    private int latenciaMs;
    private boolean conectada;

    public IntegracionAPI(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada) {
        this.id = id;
        this.nombre = nombre;
        this.proveedor = proveedor;
        this.url = url;
        this.latenciaMs = latenciaMs;
        this.conectada = conectada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLatenciaMs() {
        return latenciaMs;
    }

    public void setLatenciaMs(int latenciaMs) {
        this.latenciaMs = latenciaMs;
    }

    public boolean isConectada() {
        return conectada;
    }

    public void setConectada(boolean conectada) {
        this.conectada = conectada;
    }

    public String getEstado() {
        if (conectada) {
            return "CONECTADA";
        }
        return "DESCONECTADA";
    }

    public abstract String getTipo();

    public String getConfiguracion() {
        return "ID: " + id + "\n"
                + "Nombre: " + nombre + "\n"
                + "Proveedor: " + proveedor + "\n"
                + "Tipo: " + getTipo() + "\n"
                + "URL: " + url + "\n"
                + "Latencia promedio: " + latenciaMs + " ms\n"
                + "Estado: " + getEstado();
    }

    @Override
    public int compareTo(IntegracionAPI otra) {
        return Integer.compare(this.latenciaMs, otra.latenciaMs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntegracionAPI)) {
            return false;
        }
        IntegracionAPI otra = (IntegracionAPI) obj;
        return this.id.equalsIgnoreCase(otra.id);
    }

    @Override
    public String toString() {
        return id + " | " + nombre + " | " + getTipo() + " | " + latenciaMs + " ms | " + getEstado();
    }
}

abstract class PasarelaPago extends IntegracionAPI implements Transaccional, Auditable {
    private double comision;

    public PasarelaPago(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada, double comision) {
        super(id, nombre, proveedor, url, latenciaMs, conectada);
        this.comision = comision;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    @Override
    public String cobrar(double monto) {
        if (!isConectada()) {
            return "No se pudo cobrar, " + getNombre() + " esta desconectada.";
        }
        double total = monto + (monto * comision / 100);
        return "Cobro realizado en " + getNombre() + " por Q" + total + " (comision " + comision + "%).";
    }

    @Override
    public String reintegrar(double monto) {
        if (!isConectada()) {
            return "No se pudo reintegrar, " + getNombre() + " esta desconectada.";
        }
        return "Reintegro realizado en " + getNombre() + " por Q" + monto + ".";
    }

    @Override
    public String obtenerRegistro() {
        return "[AUDITORIA] " + getId() + " - " + getNombre() + " - pasarela de pago - " + getEstado();
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nComision: " + comision + "%";
    }
}

class TarjetaCredito extends PasarelaPago {
    private String red;

    public TarjetaCredito(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada, double comision, String red) {
        super(id, nombre, proveedor, url, latenciaMs, conectada, comision);
        this.red = red;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    @Override
    public String getTipo() {
        return "Tarjeta de credito";
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nRed: " + red;
    }
}

class TransferenciaBancaria extends PasarelaPago {
    private String banco;

    public TransferenciaBancaria(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada, double comision, String banco) {
        super(id, nombre, proveedor, url, latenciaMs, conectada, comision);
        this.banco = banco;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    @Override
    public String getTipo() {
        return "Transferencia bancaria";
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nBanco: " + banco;
    }
}

class BilleteraDigital extends PasarelaPago {
    private String moneda;

    public BilleteraDigital(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada, double comision, String moneda) {
        super(id, nombre, proveedor, url, latenciaMs, conectada, comision);
        this.moneda = moneda;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Override
    public String getTipo() {
        return "Billetera digital";
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nMoneda: " + moneda;
    }
}

class ProveedorLogistico extends IntegracionAPI implements Documentable, Auditable {
    private double tarifaPorKilo;

    public ProveedorLogistico(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada, double tarifaPorKilo) {
        super(id, nombre, proveedor, url, latenciaMs, conectada);
        this.tarifaPorKilo = tarifaPorKilo;
    }

    public double getTarifaPorKilo() {
        return tarifaPorKilo;
    }

    public void setTarifaPorKilo(double tarifaPorKilo) {
        this.tarifaPorKilo = tarifaPorKilo;
    }

    public String cotizar(double peso) {
        return "Cotizacion de " + getNombre() + ": Q" + (peso * tarifaPorKilo) + " por " + peso + " kg.";
    }

    public String cotizar(double peso, String destino) {
        return "Cotizacion de " + getNombre() + " hacia " + destino + ": Q" + (peso * tarifaPorKilo + 15) + " por " + peso + " kg.";
    }

    public String rastrear(String guia) {
        return "Paquete " + guia + " en transito con " + getNombre() + ".";
    }

    @Override
    public String generarDocumento() {
        if (!isConectada()) {
            return "No se pudo generar la guia, " + getNombre() + " esta desconectada.";
        }
        return "<guiaEnvio>\n  <proveedor>" + getNombre() + "</proveedor>\n  <tarifaPorKilo>" + tarifaPorKilo + "</tarifaPorKilo>\n</guiaEnvio>";
    }

    @Override
    public String obtenerRegistro() {
        return "[AUDITORIA] " + getId() + " - " + getNombre() + " - logistica - " + getEstado();
    }

    @Override
    public String getTipo() {
        return "Proveedor logistico";
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nTarifa por kilo: Q" + tarifaPorKilo;
    }
}

class CertificadorFEL extends IntegracionAPI implements Documentable, Auditable {
    private String nit;

    public CertificadorFEL(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada, String nit) {
        super(id, nombre, proveedor, url, latenciaMs, conectada);
        this.nit = nit;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    @Override
    public String generarDocumento() {
        if (!isConectada()) {
            return "No se pudo generar la factura, " + getNombre() + " esta desconectada.";
        }
        return "<factura>\n  <certificador>" + getNombre() + "</certificador>\n  <nit>" + nit + "</nit>\n</factura>";
    }

    @Override
    public String obtenerRegistro() {
        return "[AUDITORIA] " + getId() + " - " + getNombre() + " - certificador FEL - " + getEstado();
    }

    @Override
    public String getTipo() {
        return "Certificador FEL";
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nNIT: " + nit;
    }
}

class ApiConsulta extends IntegracionAPI {
    private String dato;

    public ApiConsulta(String id, String nombre, String proveedor, String url, int latenciaMs, boolean conectada, String dato) {
        super(id, nombre, proveedor, url, latenciaMs, conectada);
        this.dato = dato;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String consultar() {
        return "Consulta a " + getNombre() + ": " + dato;
    }

    @Override
    public String getTipo() {
        return "API de consulta";
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nDato que consulta: " + dato;
    }
}

