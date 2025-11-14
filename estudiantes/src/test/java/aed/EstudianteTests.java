package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EstudianteTests {
    @Test
    void menorNota() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        e2.setPuntaje((double)10);
        assertTrue(e1.compareTo(e2) < 0, "e1 debe ser menor que e2 cuando su puntaje es menor");
    }

    @Test
    void mayorNota() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        e1.setPuntaje((double) 10);
        assertTrue(e1.compareTo(e2) > 0, "e1 debe ser mayor que e2 cuando su puntaje es mayor");
    }

    @Test
    void mismaNotaMenorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, false);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, false);
        assertTrue(e1.compareTo(e2) < 0, "Con misma nota, el menor id debe ser menor");
    }

    @Test
    void mismaNotaMayorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, false);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, false);
        assertTrue(e1.compareTo(e2) > 0, "Con misma nota, el mayor id debe ser mayor");
    }

    @Test
    void yaEntregue() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, false);
        assertTrue(e1.compareTo(e2) > 0, "Quien ya entregó debe ser mayor que quien no entregó");
    }

    @Test
    void noEntregoEsMenor() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, false);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        assertTrue(e1.compareTo(e2) < 0, "Quien no entregó debe ser menor que quien ya entregó");
    }

    @Test
    void ambosEntregaronMenorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        assertTrue(e1.compareTo(e2) < 0, "Si ambos entregaron y mismas condiciones, menor id => menor");
    }

    @Test
    void ambosEntregaronMayorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        assertTrue(e1.compareTo(e2) > 0, "Si ambos entregaron y mismas condiciones, mayor id => mayor");
    }

    @Test
    void compareToEqualsZeroWhenAllEqual() {
        // Mismo id, misma nota y mismo estado de entrega => compareTo == 0
        Estudiante e1 = new Estudiante(10, 42, 2, 2, false, true);
        Estudiante e2 = new Estudiante(10, 42, 2, 3, false, true);
        e1.setPuntaje(5.5);
        e2.setPuntaje(5.5);
        assertEquals(0, e1.compareTo(e2), "Estudiantes idénticos en id, puntaje y entrega deben comparar iguales");
    }

    @Test
    void entregaDominaPuntaje() {
        // e1 no entrego pero tiene mayor puntaje, e2 entregó y tiene menor puntaje.
        // La condición de entrega tiene que ser la mas importante, quien no entregó es menor.
        Estudiante e1 = new Estudiante(10, 1, 2, 2, false, false);
        Estudiante e2 = new Estudiante(10, 2, 2, 2, false, true);
        e1.setPuntaje(100.0);
        e2.setPuntaje(10.0);
        assertTrue(e1.compareTo(e2) < 0, "Quien no entregó debe ser menor aunque tenga mayor puntaje");
        assertTrue(e2.compareTo(e1) > 0, "La comparación debe ser simétrica: si a < b entonces b > a");
    }

    @Test
    void pruebaSimetria() {
        // Comprobamos una simetría simple: si a < b entonces b > a
        Estudiante a = new Estudiante(5, 3, 1, 1, false, true);
        Estudiante b = new Estudiante(5, 4, 1, 1, false, true);
        // mismo puntaje (0), mismo estado de entrega -> compara por id
        assertTrue(a.compareTo(b) < 0, "a debe ser menor que b por id");
        assertTrue(b.compareTo(a) > 0, "b debe ser mayor que a por id (simetría)");
    }
}
