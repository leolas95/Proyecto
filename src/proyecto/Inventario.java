package proyecto;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Inventario {

    private SimpleIntegerProperty dia;
    private SimpleIntegerProperty invInicio;
    private SimpleIntegerProperty aleatorioDemanda;
    private SimpleIntegerProperty Demanda;
    private SimpleIntegerProperty invFinal;
    private SimpleFloatProperty invProm;
    private SimpleIntegerProperty faltante;
    private SimpleIntegerProperty noOrden;
    private SimpleIntegerProperty aleatorioEntrega;
    private SimpleIntegerProperty tiempoEntrega;
    private SimpleIntegerProperty aleatorioEspera;
    private SimpleIntegerProperty tiempoEspera;

    public Inventario(){
        dia = new SimpleIntegerProperty();
        invInicio = new SimpleIntegerProperty();
        aleatorioDemanda = new SimpleIntegerProperty();
        Demanda = new SimpleIntegerProperty();
        invFinal = new SimpleIntegerProperty();
        invProm = new SimpleFloatProperty();
        faltante = new SimpleIntegerProperty();
        noOrden = new SimpleIntegerProperty();
        aleatorioEntrega = new SimpleIntegerProperty();
        tiempoEntrega = new SimpleIntegerProperty();
        aleatorioEspera = new SimpleIntegerProperty();
        tiempoEspera = new SimpleIntegerProperty();
    }


    public SimpleIntegerProperty diaProperty() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia.set(dia);
    }


    public SimpleIntegerProperty invInicioProperty() {
        return invInicio;
    }

    public void setInvInicio(int invInicio) {
        this.invInicio.set(invInicio);
    }


    public SimpleIntegerProperty aleatorioDemandaProperty() {
        return aleatorioDemanda;
    }

    public void setAleatorioDemanda(int aleatorioDemanda) {
        this.aleatorioDemanda.set(aleatorioDemanda);
    }

    public SimpleIntegerProperty DemandaProperty() {
        return Demanda;
    }

    public void setDemanda(int Demanda) {
        this.Demanda.set(Demanda);
    }

    public SimpleIntegerProperty invFinalProperty() {
        return invFinal;
    }

    public void setInvFinal(int invFinal) {
        this.invFinal.set(invFinal);
    }


    public SimpleFloatProperty invPromProperty() {
        return invProm;
    }

    public void setInvProm(float invProm) {
        this.invProm.set(invProm);
    }


    public SimpleIntegerProperty faltanteProperty() {
        return faltante;
    }

    public void setFaltante(int faltante) {
        this.faltante.set(faltante);
        System.out.println("set faltante = " + this.faltante.get());
    }

    public SimpleIntegerProperty noOrdenProperty() {
        return noOrden;
    }

    public void setNoOrden(int noOrden) {
        this.noOrden.set(noOrden);
    }


    public SimpleIntegerProperty aleatorioEntregaProperty() {
        return aleatorioEntrega;
    }

    public void setAleatorioEntrega(int aleatorioEntrega) {
        this.aleatorioEntrega.set(aleatorioEntrega);
    }


    public SimpleIntegerProperty tiempoEntregaProperty() {
        return tiempoEntrega;
    }

    public void setTiempoEntrega(int tiempoEntrega) {
        this.tiempoEntrega.set(tiempoEntrega);
    }


    public SimpleIntegerProperty aleatorioEsperaProperty() {
        return aleatorioEspera;
    }

    public void setAleatorioEspera(int aleatorioEspera) {
        this.aleatorioEspera.set(aleatorioEspera);
    }

    public SimpleIntegerProperty tiempoEsperaProperty() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera.set(tiempoEspera);
    }
}
