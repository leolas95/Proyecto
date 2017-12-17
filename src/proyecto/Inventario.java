package proyecto;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Representa una fila de la tabla en cada dia de la simulacion. Guarda el inventario inicial, demanda, faltante,
 * etc para ese dia.
 * Posiblemente renombrarla a algo como "Fila", "DiaSimulacion", etc ?
 */
public class Inventario {

    private SimpleIntegerProperty dia;
    private SimpleIntegerProperty invInicio;
    private SimpleIntegerProperty aleatorioDemanda;
    private SimpleIntegerProperty Demanda;
    private SimpleIntegerProperty invFinal;
    private SimpleFloatProperty invProm;
    private SimpleStringProperty faltante;
    private SimpleStringProperty noOrden;
    private SimpleStringProperty aleatorioEntrega;
    private SimpleStringProperty tiempoEntrega;
    private SimpleStringProperty aleatorioEspera;
    private SimpleStringProperty tiempoEspera;

    public Inventario(){
        dia = new SimpleIntegerProperty();
        invInicio = new SimpleIntegerProperty();
        aleatorioDemanda = new SimpleIntegerProperty();
        Demanda = new SimpleIntegerProperty();
        invFinal = new SimpleIntegerProperty();
        invProm = new SimpleFloatProperty();
        faltante = new SimpleStringProperty();
        noOrden = new SimpleStringProperty();
        aleatorioEntrega = new SimpleStringProperty();
        tiempoEntrega = new SimpleStringProperty();
        aleatorioEspera = new SimpleStringProperty();
        tiempoEspera = new SimpleStringProperty();
    }

    public Inventario(int dia, int invInicio, int aleatorioDemanda,
                      int demanda, int invFinal, float invProm,
                      int faltante, int noOrden, boolean hayOrdenesPendiente, int aleatorioEntrega,
                      int tiempoEntrega, int aleatorioEspera,
                      int tiempoEspera) {
        this.dia = new SimpleIntegerProperty();
        this.invInicio = new SimpleIntegerProperty();
        this.aleatorioDemanda = new SimpleIntegerProperty();
        this.Demanda = new SimpleIntegerProperty();
        this.invFinal = new SimpleIntegerProperty();
        this.invProm = new SimpleFloatProperty();
        this.faltante = new SimpleStringProperty();
        this.noOrden = new SimpleStringProperty();
        this.aleatorioEntrega = new SimpleStringProperty();
        this.tiempoEntrega = new SimpleStringProperty();
        this.aleatorioEspera = new SimpleStringProperty();
        this.tiempoEspera = new SimpleStringProperty();

        setDia(dia);
        setInvInicio(invInicio);
        setAleatorioDemanda(aleatorioDemanda);
        setDemanda(demanda);
        setInvFinal(invFinal);
        setInvProm(invProm);
        setFaltante(faltante);
        setNoOrden(noOrden, hayOrdenesPendiente);
        setAleatorioEntrega(aleatorioEntrega);
        setTiempoEntrega(tiempoEntrega);
        setAleatorioEspera(aleatorioEspera);
        setTiempoEspera(tiempoEspera);
    }


    void setDia(int dia) {
        this.dia.set(dia);
    }

    void setInvInicio(int invInicio) {
        this.invInicio.set(invInicio);
    }

    void setAleatorioDemanda(int aleatorioDemanda) {
        this.aleatorioDemanda.set(aleatorioDemanda);
    }

    void setDemanda(int Demanda) {
        this.Demanda.set(Demanda);
    }

    void setInvFinal(int invFinal) {
        this.invFinal.set(invFinal);
    }

    void setInvProm(float invProm) {
        this.invProm.set(invProm);
    }

    void setFaltante(int faltante) {
        if (faltante == 0)
            this.faltante.set("");
        else
            this.faltante.set(String.valueOf(faltante));
    }

    void setNoOrden(int noOrden, boolean hayOrdenPendiente) {
        if (hayOrdenPendiente)
            this.noOrden.set(String.valueOf(noOrden));
        else
            this.noOrden.set("");
    }

    void setAleatorioEntrega(int aleatorioEntrega) {
        if (aleatorioEntrega < 0)
            this.aleatorioEntrega.set("");
        else
            this.aleatorioEntrega.set(String.valueOf(aleatorioEntrega));
    }

    void setTiempoEntrega(int tiempoEntrega) {
        if (tiempoEntrega <= 0)
            this.tiempoEntrega.set("");
        else
            this.tiempoEntrega.set(String.valueOf(tiempoEntrega));
    }

    void setAleatorioEspera(int aleatorioEspera) {
        if (aleatorioEspera < 0)
            this.aleatorioEspera.set("");
        else
            this.aleatorioEspera.set(String.valueOf(aleatorioEspera));

    }

    void setTiempoEspera(int tiempoEspera) {
        if (tiempoEspera < 0)
            this.tiempoEspera.set("");
        else
            this.tiempoEspera.set(String.valueOf(tiempoEspera));
    }

    // OJO: Aunque el IDE marque los get como que no se utilizan, hay que dejarlos para que los datos se puedan mostrar
    // en la tabla.

    public int getDia() {
        return dia.get();
    }

    public SimpleIntegerProperty diaProperty() {
        return dia;
    }

    public int getInvInicio() {
        return invInicio.get();
    }

    public SimpleIntegerProperty invInicioProperty() {
        return invInicio;
    }

    public int getAleatorioDemanda() {
        return aleatorioDemanda.get();
    }

    public SimpleIntegerProperty aleatorioDemandaProperty() {
        return aleatorioDemanda;
    }

    public int getDemanda() {
        return Demanda.get();
    }

    public SimpleIntegerProperty demandaProperty() {
        return Demanda;
    }

    public int getInvFinal() {
        return invFinal.get();
    }

    public SimpleIntegerProperty invFinalProperty() {
        return invFinal;
    }

    public float getInvProm() {
        return invProm.get();
    }

    public SimpleFloatProperty invPromProperty() {
        return invProm;
    }

    public String getFaltante() {
        return faltante.get();
    }

    public SimpleStringProperty faltanteProperty() {
        return faltante;
    }

    public String getNoOrden() {
        return noOrden.get();
    }

    public SimpleStringProperty noOrdenProperty() {
        return noOrden;
    }

    public String getAleatorioEntrega() {
        return aleatorioEntrega.get();
    }

    public SimpleStringProperty aleatorioEntregaProperty() {
        return aleatorioEntrega;
    }

    public String getTiempoEntrega() {
        return tiempoEntrega.get();
    }

    public SimpleStringProperty tiempoEntregaProperty() {
        return tiempoEntrega;
    }

    public String getAleatorioEspera() {
        return aleatorioEspera.get();
    }

    public SimpleStringProperty aleatorioEsperaProperty() {
        return aleatorioEspera;
    }

    public String getTiempoEspera() {
        return tiempoEspera.get();
    }

    public SimpleStringProperty tiempoEsperaProperty() {
        return tiempoEspera;
    }
}
