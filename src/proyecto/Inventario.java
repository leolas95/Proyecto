package proyecto;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Inventario {

    public SimpleIntegerProperty dia;
    public SimpleIntegerProperty invInicio;
    public SimpleFloatProperty aleatorioDemanda;
    public SimpleIntegerProperty Demanda;
    public SimpleIntegerProperty invFinal;
    public SimpleIntegerProperty invProm;
    public SimpleIntegerProperty Faltante;
    public SimpleIntegerProperty noOrden;
    public SimpleFloatProperty aleatorioEntrega;
    public SimpleIntegerProperty tiempoEntrega;
    public SimpleFloatProperty aleatorioEspera;
    public SimpleIntegerProperty tiempoEspera;

    public Inventario(){
        dia = new SimpleIntegerProperty();
        invInicio = new SimpleIntegerProperty();
        aleatorioDemanda = new SimpleFloatProperty();
        Demanda = new SimpleIntegerProperty();
        invFinal = new SimpleIntegerProperty();
        invProm = new SimpleIntegerProperty();
        Faltante = new SimpleIntegerProperty();
        noOrden = new SimpleIntegerProperty();
        aleatorioEntrega = new SimpleFloatProperty();
        tiempoEntrega = new SimpleIntegerProperty();
        aleatorioEspera = new SimpleFloatProperty();
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


    public SimpleFloatProperty aleatorioDemandaProperty() {
        return aleatorioDemanda;
    }

    public void setAleatorioDemanda(float aleatorioDemanda) {
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


    public SimpleIntegerProperty invPromProperty() {
        return invProm;
    }

    public void setInvProm(int invProm) {
        this.invProm.set(invProm);
    }


    public SimpleIntegerProperty faltanteProperty() {
        return Faltante;
    }

    public void setFaltante(int Faltante) {
        this.Faltante.set(Faltante);
    }

    public SimpleIntegerProperty noOrdenProperty() {
        return noOrden;
    }

    public void setNoOrden(int noOrden) {
        this.noOrden.set(noOrden);
    }


    public SimpleFloatProperty aleatorioEntregaProperty() {
        return aleatorioEntrega;
    }

    public void setAleatorioEntrega(float aleatorioEntrega) {
        this.aleatorioEntrega.set(aleatorioEntrega);
    }


    public SimpleIntegerProperty tiempoEntregaProperty() {
        return tiempoEntrega;
    }

    public void setTiempoEntrega(int tiempoEntrega) {
        this.tiempoEntrega.set(tiempoEntrega);
    }


    public SimpleFloatProperty aleatorioEsperaProperty() {
        return aleatorioEspera;
    }

    public void setAleatorioEspera(float aleatorioEspera) {
        this.aleatorioEspera.set(aleatorioEspera);
    }

    public SimpleIntegerProperty tiempoEsperaProperty() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera.set(tiempoEspera);
    }


}
