package proyecto;

import java.util.ArrayList;

public class Simulacion {
    private DistribucionProbabilidad tablaDemanda;
    private DistribucionProbabilidad tablaTiempoEntrega;
    private DistribucionProbabilidad tablaTiempoEspera;

    private float Costo_faltante = 0;
    private float Costo_de_Orden = 0;
    private float Costo_de_inventario = 0;
    private float Costo_total = 0;

    // Para usarlos como prueba con los datos del ejemplo
    ArrayList<Integer> aleatoriosDemanda = new ArrayList<>();
    ArrayList<Integer> aleatoriosEntrega = new ArrayList<>();
    ArrayList<Integer> aleatoriosEspera = new ArrayList<>();

    public Simulacion() {
        init();
    }

    public DistribucionProbabilidad getTablaDemanda() {
        return tablaDemanda;
    }

    public void setTablaDemanda(DistribucionProbabilidad tablaDemanda) {
        this.tablaDemanda = tablaDemanda;
    }

    public DistribucionProbabilidad getTablaTiempoEntrega() {
        return tablaTiempoEntrega;
    }

    public void setTablaTiempoEntrega(DistribucionProbabilidad tablaTiempoEntrega) {
        this.tablaTiempoEntrega = tablaTiempoEntrega;
    }

    public DistribucionProbabilidad getTablaTiempoEspera() {
        return tablaTiempoEspera;
    }

    public void setTablaTiempoEspera(DistribucionProbabilidad tablaTiempoEspera) {
        this.tablaTiempoEspera = tablaTiempoEspera;
    }

    /**
     * Esto es solo para usar los numeros aleatorios del ejemplo y verifiar que de el mismo resultado
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

    void ejecutar(int dias) {
        int diaActual;
        int costoInventario = 52;
        int costoOrdenar = 100;
        int costoFaltanteConEspera = 20;
        int costoFaltanteSinEspera = 50;
        int inventarioInicial = 50;
        int q = 100;
        int puntoDeReorden = 75;
        int sum_invPromdiario = 0;

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

        for (diaActual = 1; diaActual <= dias; diaActual++) {
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
            tiempoEspera = 0;
            if (inventarioFinal == 0) {
                if (indiceEspera < aleatoriosEspera.size())
                    nroAleatorioTiempoEspera = aleatoriosEspera.get(indiceEspera++);
                tiempoEspera = tablaTiempoEspera.obtenerValor(nroAleatorioTiempoEspera);
                if (tiempoEspera > 0) {
                    pendiente = pendiente + faltante;
                }
            }

            System.out.println("Dia: " + diaActual);
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
            System.out.println();

            /*suma el costo faltante*/
            if(faltante > 0){
                if(tiempoEspera > 0)
                    Costo_faltante += faltante * costoFaltanteConEspera;
                else
                    Costo_faltante += faltante * costoFaltanteSinEspera;
            }

            sum_invPromdiario += inventarioPromedio;

        }

        Costo_de_Orden = nroOrden*costoOrdenar;
        Costo_de_inventario = sum_invPromdiario*costoInventario/365;
        Costo_total = Costo_faltante + Costo_de_Orden + Costo_de_inventario;
        System.out.println("Los resultados de la simulacion son:");
        System.out.println("Costo faltante = "+Costo_faltante);
        System.out.println("Costo de Orden = "+Costo_de_Orden);
        System.out.println("Costo de inventario = "+Costo_de_inventario);
        System.out.println("Costo_total = "+Costo_total);
    }
}
