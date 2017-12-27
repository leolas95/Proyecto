package proyecto;

/**
 * Esta clase sirve para representar el momento en que un dia no se pudo satisfacer la demanda, hubo faltante y el
 * cliente puede esperar (tiempEspera > 0) a que se le pueda pagar lo que se le debe otro dia.
 */
public class FaltanteEnEspera {
    // La cantidad de articulos que se le deben al cliente
    private int faltante;

    // El tiempo de espera del cliente
    private int diasDeEspera;

    // Para guardar la cantidad original de articulos faltante. Sirve para hacer los calculos en calcularCosto()
    private final int faltanteOriginal;

    public FaltanteEnEspera(int faltante, int diasDeEspera) {
        this.faltante = faltante;
        this.diasDeEspera = diasDeEspera;
        this.faltanteOriginal = this.faltante;
    }

    public void setFaltante(int faltante) {
        this.faltante = faltante;
    }

    public int getFaltante() {
        return faltante;
    }

    public int getDiasDeEspera() {
        return diasDeEspera;
    }


    /**
     * Calcula el costo asociado a cuando un cliente espera por su inventario.
     * Si al cliente se le pago lo que se le debia, entonces:
     *   <code>costo = ((faltanteOriginal - faltante) * costoConEspera) + (0 * costoSinEspera)</code><br/>
     *   <code>costo = (faltanteOriginal - faltante) * costoConEspera</code>
     * @param costoConEspera el costo de escasez <b>con</b> espera del cliente. Asociado a la simulacion
     * @param costoSinEspera el costo de escasez <b>sin</b> espera del cliente. Asociado a la simulacion
     * @return el costo asociado a la espera del inventario por un cliente
     */
    public float calcularCosto(float costoConEspera, float costoSinEspera) {
        return ((faltanteOriginal - faltante) * costoConEspera) + (faltante * costoSinEspera);
    }

    public void decrementarDiasDeEspera() {
        this.diasDeEspera--;
    }

}
