package proyecto;

import java.util.ArrayList;

public class Simulacion {
    private DistribucionProbabilidad tablaDemanda;
    private DistribucionProbabilidad tablaTiempoEntrega;
    private DistribucionProbabilidad tablaTiempoEspera;

    private int diasSimulacion;

    private int costoInventario = 52;
    private int costoOrdenar = 100;
    private int costoFaltanteConEspera = 20;
    private int costoFaltanteSinEspera = 50;
    private int inventarioInicial = 50;

    // Para usarlos como prueba con los datos del ejemplo
    ArrayList<Integer> aleatoriosDemanda = new ArrayList<>();
    ArrayList<Integer> aleatoriosEntrega = new ArrayList<>();
    ArrayList<Integer> aleatoriosEspera = new ArrayList<>();

    Controller controller;

    public Simulacion() {
        init();
    }

    public Simulacion(DistribucionProbabilidad tablaDemanda, DistribucionProbabilidad tablaTiempoEntrega,
                      DistribucionProbabilidad tablaTiempoEspera, int diasSimulacion, int costoInventario,
                      int costoOrdenar, int costoFaltanteConEspera, int costoFaltanteSinEspera, int inventarioInicial,
                      Controller controller) {
        this.tablaDemanda = tablaDemanda;
        this.tablaTiempoEntrega = tablaTiempoEntrega;
        this.tablaTiempoEspera = tablaTiempoEspera;
        this.diasSimulacion = diasSimulacion;
        this.costoInventario = costoInventario;
        this.costoOrdenar = costoOrdenar;
        this.costoFaltanteConEspera = costoFaltanteConEspera;
        this.costoFaltanteSinEspera = costoFaltanteSinEspera;
        this.inventarioInicial = inventarioInicial;
        this.controller = controller;

        init();

        System.out.println("diasSimulacion = " + diasSimulacion);
        System.out.println("costo inventario = " + costoInventario);
        System.out.println("costo ordenar " + costoOrdenar);
        System.out.println("costo faltante con espera " + costoFaltanteConEspera);
        System.out.println("costo faltante sin espera " + costoFaltanteSinEspera);
        System.out.println("inventario inicial " + inventarioInicial);
    }

    public void setTablaDemanda(DistribucionProbabilidad tablaDemanda) {
        this.tablaDemanda = tablaDemanda;
    }

    public void setTablaTiempoEntrega(DistribucionProbabilidad tablaTiempoEntrega) {
        this.tablaTiempoEntrega = tablaTiempoEntrega;
    }

    public void setTablaTiempoEspera(DistribucionProbabilidad tablaTiempoEspera) {
        this.tablaTiempoEspera = tablaTiempoEspera;
    }

    public void setCostoInventario(int costoInventario) {
        this.costoInventario = costoInventario;
    }

    public void setCostoOrdenar(int costoOrdenar) {
        this.costoOrdenar = costoOrdenar;
    }

    public void setCostoFaltanteConEspera(int costoFaltanteConEspera) {
        this.costoFaltanteConEspera = costoFaltanteConEspera;
    }

    public void setCostoFaltanteSinEspera(int costoFaltanteSinEspera) {
        this.costoFaltanteSinEspera = costoFaltanteSinEspera;
    }

    public void setInventarioInicial(int inventarioInicial) {
        this.inventarioInicial = inventarioInicial;
    }

    public void setDiasSimulacion(int diasSimulacion) {
        this.diasSimulacion = diasSimulacion;
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

    /**
     * Calcula Q* (la cantidad optima por pedido, segun los costos y demanda dada)
     *             ______________
     *           /   2KD(h+s)
     * Q* = \  / ---------------
     *      \/         hs
     * @param k costo por pedido
     * @param d demanda
     * @param h costo de inventario
     * @param s costo de escasez (faltante)
     * @return Q*
     */
    int calcularQ(int k, int d, int h, int s) {
        return (int) Math.sqrt((2*k*d*(h+s)) / (h*s));
    }

    void ejecutar() {
        // Inicializamos la tabla en la interfaz
        controller.inicializarTablaPersonas();

        int dmin = tablaDemanda.obtenerValorMinimo();
        int dmax = tablaDemanda.obtenerValorMaximo();

        // Calcular qmin y qmax...

        int q = 100;
        int puntoDeReorden = 75;

        // Los costos resultantes mostrados al final de la simulacion. Esto es lo que en verdad le importa a la profe.
        float costoFaltante = 0;
        float costoDeOrden = 0;
        float costoDeInventario = 0;
        float costoTotal = 0;

        int diaActual;
        float sumaInventarioPromedioDiario = 0;

        // Para que el primer dia, el inventario inicial sea igual
        // al del dia "anterior", que en teoria no existe
        int inventarioFinal = inventarioInicial;

        boolean hayOrdenesPendiente = false;
        int nroOrden = 0;
        int faltante;

        int nroAleatorioDemanda;
        int demanda;

        int nroAleatorioTiempoEntrega;
        int tiempoEntrega = -1;
        int indiceEntrega = 0;

        int nroAleatorioTiempoEspera;
        int tiempoEspera;
        int indiceEspera = 0;


        // El inventario pendiente acumulado (lo que se acumula cuando el cliente espera)
        int pendiente = 0;

        float inventarioPromedio;

        for (diaActual = 1; diaActual <= diasSimulacion; diaActual++) {
            inventarioInicial = inventarioFinal;

            if (tiempoEntrega >= 0) {
                tiempoEntrega--;

                // Si llega el pedido, actualiza el inventario inicial, e indica que ya no hay ordenes pendiente
                if (tiempoEntrega < 0) {
                    inventarioInicial += q - pendiente;
                    hayOrdenesPendiente = false;
                    if (inventarioInicial >= 0)
                        pendiente = 0;
                }
            }

            // Esto luego se reemplaza por numeros aleatorios "de verdad"
            nroAleatorioDemanda = aleatoriosDemanda.get(diaActual-1);
            demanda = tablaDemanda.obtenerValor(nroAleatorioDemanda);
            inventarioFinal = inventarioInicial - demanda;

            faltante = 0;
            if (inventarioFinal < 0) {
                inventarioFinal = 0;
                faltante = Math.abs(inventarioInicial - demanda);
            }
            inventarioPromedio = (inventarioInicial + inventarioFinal) / 2.0F;

            nroAleatorioTiempoEntrega = -1;
            if (inventarioFinal < puntoDeReorden && !hayOrdenesPendiente) {
                nroOrden++;
                nroAleatorioTiempoEntrega = aleatoriosEntrega.get(indiceEntrega++);
                tiempoEntrega = tablaTiempoEntrega.obtenerValor(nroAleatorioTiempoEntrega);
                hayOrdenesPendiente = true;
            }

            nroAleatorioTiempoEspera = -1;
            tiempoEspera = -1;
            if (inventarioFinal == 0) {
                if (indiceEspera < aleatoriosEspera.size())
                    nroAleatorioTiempoEspera = aleatoriosEspera.get(indiceEspera++);
                tiempoEspera = tablaTiempoEspera.obtenerValor(nroAleatorioTiempoEspera);
                if (tiempoEspera > 0) {
                    pendiente += faltante;
                }
            }

            /*System.out.println("Dia: " + diaActual);
            System.out.println("Inventario inicial: " + inventarioInicial);
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


            // Aqui es donde se muestran los calculos en la interfaz
            Inventario inventario = new Inventario();
            inventario.setDia(diaActual);
            inventario.setInvInicio(inventarioInicial);
            inventario.setAleatorioDemanda(nroAleatorioDemanda);
            inventario.setDemanda(demanda);
            inventario.setInvFinal(inventarioFinal);
            inventario.setInvProm(inventarioPromedio);
            inventario.setFaltante(faltante);
            inventario.setNoOrden(nroOrden);
            inventario.setAleatorioEntrega(nroAleatorioTiempoEntrega);
            inventario.setTiempoEntrega(tiempoEntrega);
            inventario.setAleatorioEspera(nroAleatorioTiempoEspera);
            inventario.setTiempoEspera(tiempoEspera);
            controller.inventarios.add(inventario);


            // Suma el costo faltante
            if (faltante > 0) {
                if (tiempoEspera > 0)
                    costoFaltante += faltante * costoFaltanteConEspera;
                else
                    costoFaltante += faltante * costoFaltanteSinEspera;
            }
            sumaInventarioPromedioDiario += inventarioPromedio;
        }

        costoDeOrden = nroOrden*costoOrdenar;
        costoDeInventario = sumaInventarioPromedioDiario*(costoInventario/365.0F);
        costoTotal = costoFaltante + costoDeOrden + costoDeInventario;
        System.out.println("Los resultados de la simulacion son:");
        System.out.println("Costo faltante = "+ costoFaltante);
        System.out.println("Costo de Orden = "+ costoDeOrden);
        System.out.println("Costo de inventario = "+ costoDeInventario);
        System.out.println("costoTotal = "+ costoTotal);
        controller.costoFaltanteText.setText("Costo faltante = "+ costoFaltante);
        controller.costoOrdenText.setText("Costo de Orden = "+ costoDeOrden);
        controller.costoInventarioText.setText("Costo de inventario = "+ costoDeInventario);
        controller.costoTotalText.setText("Costo Total = "+ costoTotal);
    }
}