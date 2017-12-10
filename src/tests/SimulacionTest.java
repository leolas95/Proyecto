package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import proyecto.DistribucionProbabilidad;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulacionTest {
    private static DistribucionProbabilidad tablaDemanda;
    private static DistribucionProbabilidad tablaTiempoEntrega;
    private static DistribucionProbabilidad tablaTiempoEspera;

    /**
     * Construye las tablas de ejemplo del proyecto
     */
    @BeforeAll
    static void init() {
        ArrayList<Integer> valoresDemanda = new ArrayList<>();
        valoresDemanda.add(25);
        valoresDemanda.add(26);
        valoresDemanda.add(27);
        valoresDemanda.add(28);
        valoresDemanda.add(29);
        valoresDemanda.add(30);
        valoresDemanda.add(31);
        valoresDemanda.add(35);
        valoresDemanda.add(33);
        valoresDemanda.add(34);

        ArrayList<Float> probabilidadesDemanda = new ArrayList<>();
        probabilidadesDemanda.add(0.02F);
        probabilidadesDemanda.add(0.04F);
        probabilidadesDemanda.add(0.06F);
        probabilidadesDemanda.add(0.12F);
        probabilidadesDemanda.add(0.20F);
        probabilidadesDemanda.add(0.24F);
        probabilidadesDemanda.add(0.15F);
        probabilidadesDemanda.add(0.1F);
        probabilidadesDemanda.add(0.05F);
        probabilidadesDemanda.add(0.02F);

        tablaDemanda = new DistribucionProbabilidad(valoresDemanda, probabilidadesDemanda);

        ArrayList<Integer> valoresEntrega = new ArrayList<>();
        valoresEntrega.add(1);
        valoresEntrega.add(2);
        valoresEntrega.add(3);
        valoresEntrega.add(4);

        ArrayList<Float> probabilidadesEntrega = new ArrayList<>();
        probabilidadesEntrega.add(0.2F);
        probabilidadesEntrega.add(0.3F);
        probabilidadesEntrega.add(0.25F);
        probabilidadesEntrega.add(0.25F);

        tablaTiempoEntrega = new DistribucionProbabilidad(valoresEntrega, probabilidadesEntrega);

        ArrayList<Integer> valoresEspera = new ArrayList<>();
        valoresEspera.add(0);
        valoresEspera.add(1);
        valoresEspera.add(2);
        valoresEspera.add(3);
        valoresEspera.add(4);

        ArrayList<Float> probabilidadesEspera = new ArrayList<>();
        probabilidadesEspera.add(0.4F);
        probabilidadesEspera.add(0.2F);
        probabilidadesEspera.add(0.15F);
        probabilidadesEspera.add(0.15F);
        probabilidadesEspera.add(0.1F);

        tablaTiempoEspera = new DistribucionProbabilidad(valoresEspera, probabilidadesEspera);
    }

    /**
     * Se asegura que la frecuencia acumulada de la tabla de demandas sea 1
     */
    @Test
    void frecuenciaAcumuladaDemandaDeberiaSerUno() {
        float frecuenciaAcumulada = tablaDemanda.frecuenciaAcumulada();
        float epsilon = 0.000001F;
        boolean b = Math.abs(frecuenciaAcumulada - 1.0F) < epsilon;
        float f = Math.abs(frecuenciaAcumulada - 1.0F);
        assertTrue(b, "La frecuencia acumulada deberia ser == 1, pero es " + frecuenciaAcumulada + " " + f);
    }

    /**
     * Se asegura que la frecuencia acumulada de la tabla de tiempos de entrega sea 1
     */
    @Test
    void frecuenciaAcumuladaEntregaDeberiaSerUno() {
        float frecuenciaAcumulada = tablaTiempoEntrega.frecuenciaAcumulada();
        float epsilon = 0.000001F;
        boolean b = Math.abs(frecuenciaAcumulada - 1.0F) < epsilon;
        float f = Math.abs(frecuenciaAcumulada - 1.0F);
        assertTrue(b, "La frecuencia acumulada deberia ser == 1, pero es " + frecuenciaAcumulada + " " + f);
    }

    /**
     * Se asegura que la frecuencia acumulada de la tabla de espera sea 1
     */
    @Test
    void frecuenciaAcumuladaEsperaDeberiaSerUno() {
        float frecuenciaAcumulada = tablaTiempoEspera.frecuenciaAcumulada();
        float epsilon = 0.000001F;
        boolean b = Math.abs(frecuenciaAcumulada - 1.0F) < epsilon;
        float f = Math.abs(frecuenciaAcumulada - 1.0F);
        assertTrue(b, "La frecuencia acumulada deberia ser == 1, pero es " + frecuenciaAcumulada + " " + f);
    }

    /**
     * Se asegura que los valores de demanda de cada numero aleatorio sean iguales a los del ejemplo
     */
    @Test
    void lasDemandasSonLasDelEjemplo() {
        assertEquals(31, tablaDemanda.obtenerValor(69));
        assertEquals(29, tablaDemanda.obtenerValor(37));
        assertEquals(31, tablaDemanda.obtenerValor(75));
        assertEquals(30, tablaDemanda.obtenerValor(60));
        assertEquals(30, tablaDemanda.obtenerValor(54));
        assertEquals(30, tablaDemanda.obtenerValor(47));
        assertEquals(31, tablaDemanda.obtenerValor(79));
        assertEquals(33, tablaDemanda.obtenerValor(96));
        assertEquals(29, tablaDemanda.obtenerValor(42));
        assertEquals(34, tablaDemanda.obtenerValor(98));
        assertEquals(28, tablaDemanda.obtenerValor(15));
        assertEquals(30, tablaDemanda.obtenerValor(59));
        assertEquals(29, tablaDemanda.obtenerValor(37));
        assertEquals(29, tablaDemanda.obtenerValor(25));
        assertEquals(28, tablaDemanda.obtenerValor(14));
    }

    /**
     * Se asegura que los valores de tiempo de entrega de cada numero aleatorio sean iguales a los del ejemplo
     */
    @Test
    void losTiemposDeEntregaSonLosDelEjemplo() {
        assertEquals(2, tablaTiempoEntrega.obtenerValor(22));
        assertEquals(2, tablaTiempoEntrega.obtenerValor(43));
        assertEquals(1, tablaTiempoEntrega.obtenerValor(10));
        assertEquals(2, tablaTiempoEntrega.obtenerValor(29));
        assertEquals(4, tablaTiempoEntrega.obtenerValor(76));
    }

    /**
     * Se asegura que los valores de tiempos de espera de cada numero aleatorio sean iguales a los del ejemplo
     */
    @Test
    void losTiemposDeEsperaSonLosDelEjemplo() {
        assertEquals(2, tablaTiempoEspera.obtenerValor(64));
        assertEquals(0, tablaTiempoEspera.obtenerValor(6));
    }
}