package aed;

import aed.MinHeap.Handle;

public class Estudiante implements Comparable<Estudiante>{
    int[] _examen; // Arreglo de tamaño R que almacena las respuestas (o -1 si está enblanco)
    double _puntaje; // El puntaje actual del estudiante
    // Es un puntero a la posición del estudiante en el _puntajes Min-Heap, permitiendo actualizaciones en O(log E).
    // HeapHandle _posicionEnHeap;
    int _correctas; // Agregado para O(1) en resolver
    Handle _handle; // Agregado para O(log E) en resolver
    int _id;
    int _fila;
    int _columna;
    boolean _esSospechoso;
    boolean _yaEntrego;

    public Estudiante(int cantRespuestas, int id, int fila, int columna, boolean esSospechoso, boolean yaEntrego) {
        // Creamos la lista de len = cantRespuestas y la llenamos de -1
        // Que seria que no respondio

        _examen = new int[cantRespuestas];
        _id = id;
        _puntaje = (double) 0.0;
        _fila = fila;
        _columna = columna;
        _esSospechoso = esSospechoso;
        _yaEntrego = yaEntrego;

        for(int i = 0; i < cantRespuestas; i++) {
            _examen[i] = -1;
        }

    }

    public int getFila(){return _fila;}
    public int getColumna(){return _columna;}

    // Set y Get necesarios para la interactuar con Edr
    public void setHandle(Handle h) { _handle = h; }
    public Handle getHandle() { return _handle; }

    @Override
    public int compareTo(Estudiante est) {
        // Si alguno entrego y el otro todavia no
        if (this._yaEntrego != est._yaEntrego) {
            // El que no entrego es menor
            // Para cuando lean esto, funciona asi: (condicion) ? (expresion si es true) : (expresion si es false)
            return this._yaEntrego ? 1 : -1; 
        }
        
        // Si ambos tienen el mismo estado de entrega, comparamos con puntajes
        if (this._puntaje != est._puntaje) {
            return Double.compare(this._puntaje, est._puntaje);
        }
        
        // Si empatan en puntaje, comparamos por ID
        return Integer.compare(this._id, est._id);
    }
    public void setExamen(int pregunta, int respuesta) {
        _examen[pregunta] = respuesta; 
    }


    public int getRespuesta(int pregunta) {
        return _examen[pregunta]; 
    }

    public void setPuntaje(double puntaje) {
        _puntaje = puntaje;
    }
}
