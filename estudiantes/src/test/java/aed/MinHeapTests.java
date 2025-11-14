package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.Arrays;

class MinHeapTests {

    @Test
    void crear_heap_vacio() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        assertNotNull(heap);
    }

    @Test
    void crear_heap_con_un_elemento() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);
        e1.setPuntaje(50.0);
        lista.add(e1);
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        MinHeap<Estudiante>.Handle h = heap.desencolar();
        assertEquals(e1, h.getElement());
    }

    @Test
    void crear_heap_con_varios_elementos_y_extraer_minimo() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);
        Estudiante e2 = new Estudiante(10, 2, 0, 1, false, false);
        Estudiante e3 = new Estudiante(10, 3, 1, 0, false, false);
        
        e1.setPuntaje(30.0);
        e2.setPuntaje(10.0);  // Este es el minimo
        e3.setPuntaje(50.0);
        
        lista.add(e1);
        lista.add(e2);
        lista.add(e3);
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        MinHeap<Estudiante>.Handle h = heap.desencolar();
        
        assertEquals(e2, h.getElement());
        assertEquals(10.0, h.getElement()._puntaje);
    }

    @Test
    void desencolar_mantiene_orden() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);
        Estudiante e2 = new Estudiante(10, 2, 0, 1, false, false);
        Estudiante e3 = new Estudiante(10, 3, 1, 0, false, false);
        Estudiante e4 = new Estudiante(10, 4, 1, 1, false, false);
        
        e1.setPuntaje(40.0);
        e2.setPuntaje(10.0);
        e3.setPuntaje(30.0);
        e4.setPuntaje(20.0);
        
        lista.add(e1);
        lista.add(e2);
        lista.add(e3);
        lista.add(e4);
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        assertEquals(10.0, heap.desencolar().getElement()._puntaje);
        assertEquals(20.0, heap.desencolar().getElement()._puntaje);
        assertEquals(30.0, heap.desencolar().getElement()._puntaje);
        assertEquals(40.0, heap.desencolar().getElement()._puntaje);
    }

    @Test
    void heap_respeta_compareTo_con_yaEntrego() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);  // No entrego
        Estudiante e2 = new Estudiante(10, 2, 0, 1, false, true);   // Ya entrego
        
        e1.setPuntaje(50.0);  // Mayor puntaje pero no entrego
        e2.setPuntaje(10.0);  // Menor puntaje pero ya entrego
        
        lista.add(e1);
        lista.add(e2);
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        // El que no entrego debe salir primero (tiene menor valor en compareTo)
        assertEquals(e1, heap.desencolar().getElement());
        assertEquals(e2, heap.desencolar().getElement());
    }

    @Test
    void actualizar_elemento_via_handle() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);
        Estudiante e2 = new Estudiante(10, 2, 0, 1, false, false);
        Estudiante e3 = new Estudiante(10, 3, 1, 0, false, false);
        
        e1.setPuntaje(30.0);
        e2.setPuntaje(20.0);
        e3.setPuntaje(10.0);
        
        lista.add(e1);
        lista.add(e2);
        lista.add(e3);
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        // e3 debería estar en el tope (puntaje 10)
        MinHeap<Estudiante>.Handle h = heap.desencolar();
        assertEquals(e3, h.getElement());
        
        // Cambiamos e3 para que tenga puntaje mayor
        e3.setPuntaje(50.0);
        h.setElemento(e3);
        
        // Ahora e2 debería estar en el tope
        MinHeap<Estudiante>.Handle h2 = heap.desencolar();
        assertEquals(e2, h2.getElement());
    }

    @Test
    void heap_con_mismos_puntajes_ordena_por_id() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Estudiante e1 = new Estudiante(10, 5, 0, 0, false, false);
        Estudiante e2 = new Estudiante(10, 2, 0, 1, false, false);
        Estudiante e3 = new Estudiante(10, 8, 1, 0, false, false);
        
        e1.setPuntaje(20.0);
        e2.setPuntaje(20.0);
        e3.setPuntaje(20.0);
        
        lista.add(e1);
        lista.add(e2);
        lista.add(e3);
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        // Debe salir primero el de menor ID
        assertEquals(2, heap.desencolar().getElement()._id);
        assertEquals(5, heap.desencolar().getElement()._id);
        assertEquals(8, heap.desencolar().getElement()._id);
    }

    @Test
    void heap_con_muchos_elementos() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        
        // Agregamos 20 estudiantes con puntajes aleatorios
        for (int i = 0; i < 20; i++) {
            Estudiante e = new Estudiante(10, i, i / 5, i % 5, false, false);
            e.setPuntaje((double)(20 - i) * 5);
            lista.add(e);
        }
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        // Verificamos que salen en orden ascendente de puntaje
        double puntajeAnterior = -1.0;
        for (int i = 0; i < 20; i++) {
            MinHeap<Estudiante>.Handle h = heap.desencolar();
            double puntajeActual = h.getElement()._puntaje;
            assertTrue(puntajeActual >= puntajeAnterior);
            puntajeAnterior = puntajeActual;
        }
    }

    @Test
    void heap_con_elementos_negativos() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);
        Estudiante e2 = new Estudiante(10, 2, 0, 1, false, false);
        Estudiante e3 = new Estudiante(10, 3, 1, 0, false, false);
        
        e1.setPuntaje(-10.0);
        e2.setPuntaje(0.0);
        e3.setPuntaje(10.0);
        
        lista.add(e3);
        lista.add(e1);
        lista.add(e2);
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        assertEquals(-10.0, heap.desencolar().getElement()._puntaje);
        assertEquals(0.0, heap.desencolar().getElement()._puntaje);
        assertEquals(10.0, heap.desencolar().getElement()._puntaje);
    }

    @Test
    void heap_inserciones_en_orden_ascendente() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        
        for (int i = 1; i <= 10; i++) {
            Estudiante e = new Estudiante(10, i, 0, 0, false, false);
            e.setPuntaje((double)i);
            lista.add(e);
        }
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        for (int i = 1; i <= 10; i++) {
            assertEquals((double)i, heap.desencolar().getElement()._puntaje);
        }
    }

    @Test
    void heap_inserciones_en_orden_descendente() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        
        for (int i = 10; i >= 1; i--) {
            Estudiante e = new Estudiante(10, i, 0, 0, false, false);
            e.setPuntaje((double)i);
            lista.add(e);
        }
        
        MinHeap<Estudiante> heap = new MinHeap<>(lista);
        
        for (int i = 1; i <= 10; i++) {
            assertEquals((double)i, heap.desencolar().getElement()._puntaje);
        }
    }
}
