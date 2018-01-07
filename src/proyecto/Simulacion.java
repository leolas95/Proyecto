package proyecto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Simulacion {

    private final int DIAS_EN_UN_ANO = 365;

    private DistribucionProbabilidad tablaDemanda;
    private DistribucionProbabilidad tablaTiempoEntrega;
    private DistribucionProbabilidad tablaTiempoEspera;

    private final int diasSimulacion;

    // Los costos asociados a la simulacion. Son los que se leen como parametros al usuario
    private float costoInventarioSimulacion = 0;
    private float costoOrdenarSimulacion = 0;
    private float costoFaltanteConEsperaSimulacion = 0;
    private float costoFaltanteSinEsperaSimulacion = 0;

    private int inventarioInicial;

    // Los costos minimos, para mostrar como resultado final
    private float costoFaltanteMinimo = 0;
    private float costoDeOrdenMinimo = 0;
    private float costoDeInventarioMinimo = 0;
    private float costoTotalMinimo = Float.MAX_VALUE;

    // Los valores de "q" y "r" asociados al costo total minimo de toda la simulacion
    private float qmin, rmin;

    // Todos estos son usados para los graficos de ContoladorSimulacion.
    private float costoFaltanteSinEspera;
    private float costoFaltanteConEspera;
    private float _costoInventario;
    private float _costoOrdenar;
    private float _costoFaltante;

    private ControladorSimulacion controladorSimulacion;

    Simulacion(DistribucionProbabilidad tablaDemanda, DistribucionProbabilidad tablaTiempoEntrega,
               DistribucionProbabilidad tablaTiempoEspera, int diasSimulacion, int costoInventarioSimulacion,
               int costoOrdenarSimulacion, int costoFaltanteConEsperaSimulacion, int costoFaltanteSinEsperaSimulacion, int inventarioInicial,
               ControladorSimulacion controladorSimulacion) {
        this.tablaDemanda = tablaDemanda;
        this.tablaTiempoEntrega = tablaTiempoEntrega;
        this.tablaTiempoEspera = tablaTiempoEspera;
        this.diasSimulacion = diasSimulacion;
        this.costoInventarioSimulacion = costoInventarioSimulacion;
        this.costoOrdenarSimulacion = costoOrdenarSimulacion;
        this.costoFaltanteConEsperaSimulacion = costoFaltanteConEsperaSimulacion;
        this.costoFaltanteSinEsperaSimulacion = costoFaltanteSinEsperaSimulacion;
        this.inventarioInicial = inventarioInicial;
        this.controladorSimulacion = controladorSimulacion;

        System.out.println("diasSimulacion = " + diasSimulacion);
        System.out.println("costo inventario = " + costoInventarioSimulacion);
        System.out.println("costo ordenar " + costoOrdenarSimulacion);
        System.out.println("costo faltante con espera " + costoFaltanteConEsperaSimulacion);
        System.out.println("costo faltante sin espera " + costoFaltanteSinEsperaSimulacion);
        System.out.println("inventario inicial " + inventarioInicial);
    }

    void ejecutar(int q, int r) {
        // Inicializamos la tabla en la interfaz
        controladorSimulacion.inicializarTablaSimulacion();
        Random random = new Random();

        ArrayList<FaltanteEnEspera> registroFaltantes = new ArrayList<>();

        // Los costos resultantes asociados a una corrida de la simulacion.
        float resultadoCostoFaltante = 0;
        float resultadoCostoDeOrdenar;
        float resultadoCostoDeInventario;
        float resultadoCostoTotal;

        int diaActual;
        float sumaInventarioPromedioDiario = 0;

        // Para no perder el valor original del inventario inicial
        int inventarioInicialCorrida = inventarioInicial;

        // Para que el primer dia, el inventario inicial sea igual
        // al del dia "anterior", que en teoria no existe
        int inventarioFinal = inventarioInicialCorrida;

        boolean hayOrdenesPendiente = false;
        int nroOrden = 0;
        int faltante;

        int nroAleatorioDemanda;
        int demanda = -1;

        int nroAleatorioTiempoEntrega;
        int tiempoEntrega = -1; // -1 significa que no hay tiempo de entrega

        int nroAleatorioTiempoEspera;
        int tiempoEspera;

        float inventarioPromedio;

        for (diaActual = 1; diaActual <= diasSimulacion; diaActual++) {
            inventarioInicialCorrida = inventarioFinal;

            // Si tenemos ordenes pendientes aun sin entregar, ajusta el tiempo que falta
            if (hayOrdenesPendiente && tiempoEntrega >= 0) {
                tiempoEntrega--;

                // Si llega el pedido, actualiza el inventario inicial, e indica que ya no hay ordenes pendiente
                if (tiempoEntrega < 0) {
                    inventarioInicialCorrida += q;
                    hayOrdenesPendiente = false;
                }
            }

            Iterator iterator = registroFaltantes.iterator();

            // Recorre la lista de inventarios pendientes, actualizando cada uno
            while (iterator.hasNext()) {
                FaltanteEnEspera faltanteEnEspera = (FaltanteEnEspera) iterator.next();
                int inventarioFaltante = faltanteEnEspera.getFaltante();
                int diasDeEspera = faltanteEnEspera.getDiasDeEspera();

                // Si tenemos algo de inventario para pagar
                if (inventarioInicialCorrida > 0) {

                    // Si no podemos pagar tod0 el faltante, solo una parte
                    if (inventarioInicialCorrida <= inventarioFaltante) {
                        faltanteEnEspera.setFaltante(inventarioFaltante - inventarioInicialCorrida);
                        inventarioInicialCorrida = 0;
                    }
                    // Podemos pagar tod0 el faltante de una vez
                    else {
                        inventarioInicialCorrida -= inventarioFaltante;
                        faltanteEnEspera.setFaltante(0);
                    }
                }

                // Pagamos tod0 antes de que se acabe el tiempo de espera
                if (inventarioFaltante == 0 && diasDeEspera >= 0) {
                    resultadoCostoFaltante += faltanteEnEspera.calcularCosto(costoFaltanteConEsperaSimulacion, costoFaltanteSinEsperaSimulacion);
                    costoFaltanteConEspera += faltanteEnEspera.calcularCosto(costoFaltanteConEsperaSimulacion, costoFaltanteSinEsperaSimulacion);
                    iterator.remove();
                    continue;
                }

                if (diasDeEspera > 0) {
                    faltanteEnEspera.decrementarDiasDeEspera();
                }
                // Se acabo el tiempo de espera y no pagamos tod0
                else {
                    resultadoCostoFaltante += faltanteEnEspera.calcularCosto(costoFaltanteConEsperaSimulacion, costoFaltanteSinEsperaSimulacion);
                    costoFaltanteSinEspera += faltanteEnEspera.calcularCosto(costoFaltanteConEsperaSimulacion, costoFaltanteSinEsperaSimulacion);
                    iterator.remove();
                }
            }

            nroAleatorioDemanda = random.nextInt(99);

            demanda = tablaDemanda.obtenerValor(nroAleatorioDemanda);
            inventarioFinal = inventarioInicialCorrida - demanda;

            faltante = 0;
            nroAleatorioTiempoEspera = -1;
            tiempoEspera = -1;

            // Si el inventario final es < 0, significa que no satisfacimos la demanda y hubo faltante
            if (inventarioFinal < 0) {
                inventarioFinal = 0;
                faltante = Math.abs(inventarioInicialCorrida - demanda);

                // Obten el tiempo de espera
                nroAleatorioTiempoEspera = random.nextInt(99);
                tiempoEspera = tablaTiempoEspera.obtenerValor(nroAleatorioTiempoEspera);

                // Si el cliente espera, guardamos lo que tenemos pendiente
                if (tiempoEspera > 0) {
                    registroFaltantes.add(new FaltanteEnEspera(faltante, tiempoEspera));
                }
            }
            inventarioPromedio = (inventarioInicialCorrida + inventarioFinal) / 2.0F;

            nroAleatorioTiempoEntrega = -1;

            // Pregunta si hay que hacer un pedido
            if (inventarioFinal < r && !hayOrdenesPendiente) {
                nroOrden++;
                nroAleatorioTiempoEntrega = random.nextInt(99);
                tiempoEntrega = tablaTiempoEntrega.obtenerValor(nroAleatorioTiempoEntrega);
                hayOrdenesPendiente = true;
            }

            // Inserta una nueva fila en la tabla de eventos
            insertarNuevaFila(
                    diaActual, inventarioInicialCorrida, nroAleatorioDemanda, demanda,
                    inventarioFinal, inventarioPromedio, faltante, nroOrden, hayOrdenesPendiente, nroAleatorioTiempoEntrega,
                    tiempoEntrega, nroAleatorioTiempoEspera, tiempoEspera
            );

            // Si hubo faltante y el cliente no espera, es perdida de ventas
            if (faltante > 0 && tiempoEspera == 0) {
                resultadoCostoFaltante += faltante * costoFaltanteSinEsperaSimulacion;
            }
            sumaInventarioPromedioDiario += inventarioPromedio;
        }

        resultadoCostoDeOrdenar = nroOrden * costoOrdenarSimulacion;
        resultadoCostoDeInventario = sumaInventarioPromedioDiario * (costoInventarioSimulacion / DIAS_EN_UN_ANO);
        resultadoCostoTotal = resultadoCostoFaltante + resultadoCostoDeOrdenar + resultadoCostoDeInventario;

        _costoInventario = resultadoCostoDeInventario;
        _costoOrdenar = resultadoCostoDeOrdenar;
        _costoFaltante = resultadoCostoFaltante;

        costoFaltanteSinEspera = resultadoCostoFaltante - costoFaltanteConEspera;

        mostrarCostos(resultadoCostoFaltante, resultadoCostoDeOrdenar, resultadoCostoDeInventario, resultadoCostoTotal);

        // Si estos valores de q y r dan un costo minimo mejor, los guardamos
        if (resultadoCostoTotal < costoTotalMinimo) {
            costoFaltanteMinimo = resultadoCostoFaltante;
            costoDeOrdenMinimo = resultadoCostoDeOrdenar;
            costoDeInventarioMinimo = resultadoCostoDeInventario;
            costoTotalMinimo = resultadoCostoTotal;
            qmin = q;
            rmin = r;
        }
    }

    // Muestra los costos finales de la corrida de simulacion en la pantalla
    private void mostrarCostos(float resultadoCostoFaltante, float resultadoCostoDeOrdenar,
                               float resultadoCostoDeInventario, float resultadoCostoTotal) {
        controladorSimulacion.costoFaltanteText.setText("Costo faltante = " + resultadoCostoFaltante);
        controladorSimulacion.costoOrdenText.setText("Costo de orden = " + resultadoCostoDeOrdenar);
        controladorSimulacion.costoInventarioText.setText("Costo de inventario = " + resultadoCostoDeInventario);
        controladorSimulacion.costoTotalText.setText("Costo Total = " + resultadoCostoTotal);
        controladorSimulacion.qMinimaText.setText("Q minima = " + String.valueOf(getQmin()));
        controladorSimulacion.rMinimaText.setText("R minima = " + String.valueOf(getRmin()));

    }

    // Crea una nueva fila con los argumentos, y la inserta en la tabla de eventos
    private void insertarNuevaFila(int diaActual, int inventarioInicialCorrida, int nroAleatorioDemanda, int demanda,
                                   int inventarioFinal, float inventarioPromedio, int faltante, int nroOrden,
                                   boolean hayOrdenesPendiente, int nroAleatorioTiempoEntrega, int tiempoEntrega,
                                   int nroAleatorioTiempoEspera, int tiempoEspera) {

        // Aqui es donde se muestran los calculos en la interfaz
        Inventario inventario = new Inventario(diaActual, inventarioInicialCorrida, nroAleatorioDemanda, demanda,
                inventarioFinal, inventarioPromedio, faltante, nroOrden, hayOrdenesPendiente, nroAleatorioTiempoEntrega,
                tiempoEntrega, nroAleatorioTiempoEspera, tiempoEspera);
        controladorSimulacion.insertarFila(inventario);
    }

    float getQmin() {
        return qmin;
    }

    float getRmin() {
        return rmin;
    }

    float getCostoTotalMinimo() {
        return costoTotalMinimo;
    }

    public float getCostoFaltanteMinimo() {
        return costoFaltanteMinimo;
    }

    public float getCostoDeOrdenMinimo() {
        return costoDeOrdenMinimo;
    }

    public float getCostoDeInventarioMinimo() {
        return costoDeInventarioMinimo;
    }

    public float getCostoFaltanteSinEspera() {
        return costoFaltanteSinEspera;
    }

    public float getCostoFaltanteConEspera() {
        return costoFaltanteConEspera;
    }

    public float get_costoInventario() {
        return _costoInventario;
    }

    public float get_costoOrdenar() {
        return _costoOrdenar;
    }

    public float get_costoFaltante() {
        return _costoFaltante;
    }
}