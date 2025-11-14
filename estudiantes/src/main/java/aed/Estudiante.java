package aed;

public class Estudiante {
    int[] _examen; // Arreglo de tamaño R que almacena las respuestas (o -1 si está enblanco)
    double _puntaje; // El puntaje actual del estudiante
    // Es un puntero a la posición del estudiante en el _puntajes Min-Heap, permitiendo actualizaciones en O(log E).
    // HeapHandle _posicionEnHeap; 
    int _fila;
    int _columna;
    boolean _esSospechoso;
    boolean _yaEntrego;

    public Estudiante(int cantRespuestas, int fila, int columna) {
        // Creamos la lista de len = cantRespuestas y la llenamos de 2
        // Que seria que no respondio

        _examen = new int[cantRespuestas];

        for(int i = 0; i < cantRespuestas; i++) {
            _examen[i] = -1;
        }

        _puntaje = 0;
        _fila = 0;
        _columna = 0;
    } 
}
