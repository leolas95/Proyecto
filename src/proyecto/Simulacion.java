package proyecto;

import java.util.ArrayList;

public class Simulacion {
    private DistribucionProbabilidad tablaDemanda;
    private DistribucionProbabilidad tablaTiempoEntrega;
    private DistribucionProbabilidad tablaTiempoEspera;

    private int diasSimulacion;

    // Los costos asociados a la simulacion. Son los que se leen como parametros al usuario
    private int costoInventarioSimulacion;
    private int costoOrdenarSimulacion;
    private int costoFaltanteConEsperaSimulacion;
    private int costoFaltanteSinEsperaSimulacion;

    private int inventarioInicial;

    // Los costos minimos, para mostrar como resultado final
    private float costoFaltanteMinimo;
    private float costoDeOrdenMinimo;
    private float costoDeInventarioMinimo;
    private float costoTotalMinimo = Float.MAX_VALUE;

    // Los valores de "q" y "r" asociados al costo total minimo de toda la simulacion
    private float qmin, rmin;

    // Numeros aleatorios para usarlos como prueba con los datos del ejemplo. Borrar al final
    private ArrayList<Integer> aleatoriosDemanda = new ArrayList<>();
    private ArrayList<Integer> aleatoriosEntrega = new ArrayList<>();
    private ArrayList<Integer> aleatoriosEspera = new ArrayList<>();

    private Controller controller;

    Simulacion(DistribucionProbabilidad tablaDemanda, DistribucionProbabilidad tablaTiempoEntrega,
               DistribucionProbabilidad tablaTiempoEspera, int diasSimulacion, int costoInventarioSimulacion,
               int costoOrdenarSimulacion, int costoFaltanteConEsperaSimulacion, int costoFaltanteSinEsperaSimulacion, int inventarioInicial,
               Controller controller) {
        this.tablaDemanda = tablaDemanda;
        this.tablaTiempoEntrega = tablaTiempoEntrega;
        this.tablaTiempoEspera = tablaTiempoEspera;
        this.diasSimulacion = diasSimulacion;
        this.costoInventarioSimulacion = costoInventarioSimulacion;
        this.costoOrdenarSimulacion = costoOrdenarSimulacion;
        this.costoFaltanteConEsperaSimulacion = costoFaltanteConEsperaSimulacion;
        this.costoFaltanteSinEsperaSimulacion = costoFaltanteSinEsperaSimulacion;
        this.inventarioInicial = inventarioInicial;
        this.controller = controller;

        init();

        System.out.println("diasSimulacion = " + diasSimulacion);
        System.out.println("costo inventario = " + costoInventarioSimulacion);
        System.out.println("costo ordenar " + costoOrdenarSimulacion);
        System.out.println("costo faltante con espera " + costoFaltanteConEsperaSimulacion);
        System.out.println("costo faltante sin espera " + costoFaltanteSinEsperaSimulacion);
        System.out.println("inventario inicial " + inventarioInicial);
    }


    /**
     * Esto es solo para usar los numeros aleatorios del ejemplo y verificar que de el mismo resultado.
     */
    private void init() {
        aleatoriosDemanda.add(69);
        aleatoriosDemanda.add(37);
        aleatoriosDemanda.add(75);
        aleatoriosDemanda.add(60);
        aleatoriosDemanda.add(54);
        aleatoriosDemanda.add(47);
        aleatoriosDemanda.add(79);
        aleatoriosDemanda.add(96);
        aleatoriosDemanda.add(42);
        aleatoriosDemanda.add(98);
        aleatoriosDemanda.add(15);
        aleatoriosDemanda.add(59);
        aleatoriosDemanda.add(37);
        aleatoriosDemanda.add(25);
        aleatoriosDemanda.add(14);
        aleatoriosEntrega.add(22);
        aleatoriosEntrega.add(43);
        aleatoriosEntrega.add(10);
        aleatoriosEntrega.add(29);
        aleatoriosEntrega.add(76);
        aleatoriosEspera.add(64);
        aleatoriosEspera.add(6);
    }

    void ejecutar(int q, int r) {
        // Inicializamos la tabla en la interfaz
        controller.inicializarTablaSimulacion();

        // Esto se debe pasar como parametro al metodo
        /*int q = 100;
        int puntoDeReorden = 75;*/

        int puntoDeReorden = r;

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
        int demanda;

        int nroAleatorioTiempoEntrega;
        int tiempoEntrega = -1; // -1 significa que no hay tiempo de entrega
        int indiceEntrega = 0;

        int nroAleatorioTiempoEspera;
        int tiempoEspera;
        int indiceEspera = 0;


        // El inventario pendiente acumulado (lo que se acumula cuando el cliente espera)
        int pendiente = 0;

        float inventarioPromedio;
        for (diaActual = 1; diaActual <= diasSimulacion; diaActual++) {
            inventarioInicialCorrida = inventarioFinal;

            // Si tenemos ordenes pendientes aun sin entregar, ajusta el tiempo que falta
            if (hayOrdenesPendiente && tiempoEntrega >= 0) {
                tiempoEntrega--;

                // Si llega el pedido, actualiza el inventario inicial, e indica que ya no hay ordenes pendiente
                if (tiempoEntrega < 0) {
                    /*inventarioInicial += q - pendiente;
                    hayOrdenesPendiente = false;
                    if (inventarioInicial >= 0)
                        pendiente = 0;*/

                    inventarioInicialCorrida += q - pendiente;
                    if (inventarioInicialCorrida < 0) {
                        inventarioInicialCorrida = 0;
                        pendiente = Math.abs(q - pendiente);
                    } else {
                        pendiente = 0;
                    }
                    hayOrdenesPendiente = false;
                }
            }

            // Esto luego se reemplaza por numeros aleatorios "de verdad"
            nroAleatorioDemanda = aleatoriosDemanda.get(diaActual - 1);
            demanda = tablaDemanda.obtenerValor(nroAleatorioDemanda);
            inventarioFinal = inventarioInicialCorrida - demanda;

            faltante = 0;
            nroAleatorioTiempoEspera = -1;
            tiempoEspera = -1;
            // Si el inventario final es < 0, significa que no satisfacimos la demanda y hubo faltante
            if (inventarioFinal < 0) {
                inventarioFinal = 0;
                faltante = Math.abs(inventarioInicialCorrida - demanda);

                // Cuando eliminemos aleatoriosEspera, el if es innecesario, solo nos quedamos con el cuerpo
                // Obten el tiempo de espera
                if (indiceEspera < aleatoriosEspera.size())
                    nroAleatorioTiempoEspera = aleatoriosEspera.get(indiceEspera++);
                tiempoEspera = tablaTiempoEspera.obtenerValor(nroAleatorioTiempoEspera);

                // Si el cliente espera, guardamos lo que tenemos pendiente
                if (tiempoEspera > 0) {
                    pendiente += faltante;
                }
            }
            inventarioPromedio = (inventarioInicialCorrida + inventarioFinal) / 2.0F;

            nroAleatorioTiempoEntrega = -1;
            // Pregunta si hay que hacer un pedido
            if (inventarioFinal < puntoDeReorden && !hayOrdenesPendiente) {
                nroOrden++;
                nroAleatorioTiempoEntrega = aleatoriosEntrega.get(indiceEntrega++);
                tiempoEntrega = tablaTiempoEntrega.obtenerValor(nroAleatorioTiempoEntrega);
                hayOrdenesPendiente = true;
            }


            /*System.out.println("Dia: " + diaActual);
            System.out.println("Inventario inicial: " + inventarioInicialCorrida);
            System.out.println("Nro aleatorio demanda: " + nroAleatorioDemanda);
            System.out.println("Demanda: " + demanda);
            System.out.println("Inventario final: " + inventarioFinal);
            System.out.println("Inventario promedio: " + inventarioPromedio);
            System.out.println("Faltante: " + faltante);
            System.out.println("Nro orden: " + nroOrden);
            System.out.println("Nro aleatorio entrega: " + nroAleatorioTiempoEntrega);
            System.out.println("Tiempo entrega: " + tiempoEntrega);
            System.out.println("Nro aleatorio espera: " + nroAleatorioTiempoEspera);
            System.out.println("Tiempo espera: " + tiempoEspera);
            System.out.println("Pendiente: " + pendiente);
            System.out.println();*/

            insertarNuevaFila(
                    diaActual, inventarioInicialCorrida, nroAleatorioDemanda, demanda,
                    inventarioFinal, inventarioPromedio, faltante, nroOrden, hayOrdenesPendiente, nroAleatorioTiempoEntrega,
                    tiempoEntrega, nroAleatorioTiempoEspera, tiempoEspera
            );

            // Suma el costo faltante
            if (faltante > 0) {
                if (tiempoEspera > 0) {
                    resultadoCostoFaltante += faltante * costoFaltanteConEsperaSimulacion;
                } else {
                    resultadoCostoFaltante += faltante * costoFaltanteSinEsperaSimulacion;
                }
            }
            sumaInventarioPromedioDiario += inventarioPromedio;
        }

        resultadoCostoDeOrdenar = nroOrden * costoOrdenarSimulacion;
        resultadoCostoDeInventario = sumaInventarioPromedioDiario * (costoInventarioSimulacion / 365.0F);
        resultadoCostoTotal = resultadoCostoFaltante + resultadoCostoDeOrdenar + resultadoCostoDeInventario;
        /*System.out.println("Los resultados de la simulacion son:");
        System.out.println("Costo faltante = " + resultadoCostoFaltante);
        System.out.println("Costo de Orden = " + resultadoCostoDeOrdenar);
        System.out.println("Costo de inventario = " + resultadoCostoDeInventario);
        System.out.println("resultadoCostoTotal = " + resultadoCostoTotal);
        System.out.println();*/

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
        controller.costoFaltanteText.setText("Costo faltante = " + resultadoCostoFaltante);
        controller.costoOrdenText.setText("Costo de Orden = " + resultadoCostoDeOrdenar);
        controller.costoInventarioText.setText("Costo de inventario = " + resultadoCostoDeInventario);
        controller.costoTotalText.setText("Costo Total = " + resultadoCostoTotal);
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
        controller.insertarFila(inventario);
    }

    float getQmin() {
        return qmin;
    }

    float getRmin() {
        return rmin;
    }
}