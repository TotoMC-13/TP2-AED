package aed;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NotaFinalTests {
     @Test
    void comparar_notas() {
        NotaFinal nota_menor = new NotaFinal(10.0, 1);
        NotaFinal nota_mayor = new NotaFinal(11.0, 1);
        assertTrue(nota_mayor.compareTo(nota_menor) > 0);
    }

    @Test
    void comparar_notas_iguales_distinto_id() {
        NotaFinal nota_menor = new NotaFinal(10.0, 2);
        NotaFinal nota_mayor = new NotaFinal(10.0, 3);
        assertTrue(nota_mayor.compareTo(nota_menor) > 0);
    }

}
