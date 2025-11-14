package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;

class EstudianteTests {
    @Test
    void menorNota() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        e2.setPuntaje((double)10);
        assertEquals(e1.compareTo(e2), -1);
    }

    @Test
    void mayorNota() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        e1.setPuntaje((double) 10);
        assertEquals(e1.compareTo(e2), 1);
    }

    @Test
    void mismaNotaMenorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, false);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, false);
        assertEquals(e1.compareTo(e2), -1);
    }

    @Test
    void mismaNotaMayorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, false);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, false);
        assertEquals(e1.compareTo(e2), 1);
    }

    @Test
    void yaEntregue() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, false);
        assertEquals(e1.compareTo(e2), 1);
    }

    @Test
    void yaEntrego() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, false);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        assertEquals(e1.compareTo(e2), 1);
    }

    @Test
    void ambosEntregaronMenorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 10, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        assertEquals(e1.compareTo(e2), -1);
    }

    @Test
    void ambosEntregaronMayorId() {
        // La nota default es 0
        Estudiante e1 = new Estudiante(10, 12, 2, 2, false, true);
        Estudiante e2 = new Estudiante(5, 11, 2, 2, false, true);
        assertEquals(e1.compareTo(e2), 1);
    }
}
