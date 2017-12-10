package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import proyecto.DistribucionProbabilidad;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DistribucionProbabilidadTest {

    private static DistribucionProbabilidad dp;

    /**
     * Construye una tabla de prueba:
     *  25 |  26 |  27 |  28 (valores)
     * .25 | .25 | .30 | .20 (probabilidades)
     * .25 | .50 | .80 | 1.0 (frecuencias acumuladas)
     */
    @BeforeAll
    static void init() {
        ArrayList<Integer> i = new ArrayList<>();
        i.add(25);
        i.add(26);
        i.add(27);
        i.add(28);

        ArrayList<Float> f = new ArrayList<>();
        f.add(0.25F);
        f.add(0.25F);
        f.add(0.3F);
        f.add(0.2F);

        dp = new DistribucionProbabilidad(i, f);
    }

    @Test
    void frecuenciaAcumuladaDeberiaSerUno() {
        float frecuenciaAcumulada = dp.frecuenciaAcumulada();
        float eps = 0.000001F;
        boolean b = Math.abs(frecuenciaAcumulada - 1.0F) < eps;
        float f = Math.abs(frecuenciaAcumulada - 1.0F);
        assertTrue(b, "La frecuencia acumulada deberia ser == 1, pero es " + frecuenciaAcumulada + " " + f);
    }

    @Test
    void obtieneValorCorrecto() {
        assertEquals(25, dp.obtenerValor(0));
        assertEquals(25, dp.obtenerValor(1));
        assertEquals(25, dp.obtenerValor(15));
        assertEquals(25, dp.obtenerValor(24));

        assertEquals(26, dp.obtenerValor(25));
        assertEquals(26, dp.obtenerValor(30));
        assertEquals(26, dp.obtenerValor(48));
        assertEquals(26, dp.obtenerValor(49));

        assertEquals(27, dp.obtenerValor(50));
        assertEquals(27, dp.obtenerValor(60));
        assertEquals(27, dp.obtenerValor(78));
        assertEquals(27, dp.obtenerValor(79));

        assertEquals(28, dp.obtenerValor(80));
        assertEquals(28, dp.obtenerValor(85));
        assertEquals(28, dp.obtenerValor(90));
        assertEquals(28, dp.obtenerValor(99));
    }
}