package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.Arrays;

class MinHeapTests {

    @Test
    void crear_heap_vacio() {
        Integer[] arr = {};
        MinHeap<Integer> heap = new MinHeap<>(arr);
        assertNotNull(heap);
    }

    @Test
    void crear_heap_con_tres_elementos() {
       Integer[] arr = {1,2,3};

        MinHeap<Integer> h = new MinHeap<>(arr);
       
        assertEquals(1, h.desencolar().getElement());
        assertEquals(2, h.desencolar().getElement());
        assertEquals(3, h.desencolar().getElement());
    }

    @Test
    void crear_heap_con_un_estudiante() {
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);
        Estudiante[] arr = {e1};

        e1.setPuntaje(50.0);
        
        MinHeap<Estudiante> heap = new MinHeap<>(arr);
        MinHeap<Estudiante>.Handle h = heap.desencolar();
        assertEquals(e1, h.getElement());
    }

    @Test
    void crear_heap_con_varios_estudiantes_y_extraer_minimo() {
        
        Estudiante e1 = new Estudiante(10, 1, 0, 0, false, false);
        Estudiante e2 = new Estudiante(10, 2, 0, 1, false, false);
        Estudiante e3 = new Estudiante(10, 3, 1, 0, false, false);

        Estudiante[] arr = {e1,e2,e3};

        e1.setPuntaje(30.0);
        e2.setPuntaje(10.0);  
        e3.setPuntaje(50.0);

        
        MinHeap<Estudiante> heap = new MinHeap<>(arr);
        MinHeap<Estudiante>.Handle h = heap.desencolar();
        
        assertEquals(e2, h.getElement());
        assertEquals(10.0, h.getElement().getPuntaje());
    }

    @Test
    void desencolar_mantiene_orden() {
        Integer[] arr = {2,5,8,7,9,3};
        
        MinHeap<Integer> heap = new MinHeap<>(arr);
        
        assertEquals(2, heap.desencolar().getElement());
        assertEquals(3, heap.desencolar().getElement());
        assertEquals(5, heap.desencolar().getElement());
        assertEquals(7, heap.desencolar().getElement());
        assertEquals(8, heap.desencolar().getElement());
        assertEquals(9, heap.desencolar().getElement());
    }

    @Test
    void desencolar_nada() {
        Integer[] arr = {};
        
        MinHeap<Integer> heap = new MinHeap<>(arr);
        
        assertEquals(null, heap.desencolar());
        
    }
    
    @Test
    void desencolar_100_en_orden() {
        Integer[] arr = new Integer[100];

        for (int i = 0; i <100; i++) {
            arr[i] = i;
            
            
            
        }
        MinHeap<Integer> heap = new MinHeap<>(arr);
            
        for (int i = 0; i <100; i++) {
            assertEquals(i, heap.desencolar().getElement());
        }
    }

    @Test
    void desencolar_100_al_reves() {
        Integer[] arr = new Integer[101];

        for (int i = 100; i >= 0; i--) {
            arr[i] = i;           
        }
        MinHeap<Integer> heap = new MinHeap<>(arr);
            
        for (int i = 0; i <= 100; i++) {
            assertEquals(i, heap.desencolar().getElement());
        }
    }
}
