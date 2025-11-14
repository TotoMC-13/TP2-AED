package aed;

public class Estudiante {
    int[] _examen; // Arreglo de tamaño R que almacena las respuestas (o -1 si está enblanco)
    double _puntaje; // El puntaje actual del estudiante
    // Es un puntero a la posición del estudiante en el _puntajes Min-Heap, permitiendo actualizaciones en O(log E).
    // HeapHandle _posicionEnHeap;
    int _id;
    int _fila;
    int _columna;
    boolean _esSospechoso;
    boolean _yaEntrego;

    public Estudiante(int cantRespuestas, int id, int fila, int columna, boolean esSospechoso, boolean yaEntrego) {
        // Creamos la lista de len = cantRespuestas y la llenamos de 2
        // Que seria que no respondio

        _examen = new int[cantRespuestas];
        _id = id;
        _puntaje = (double) 0;
        _fila = fila;
        _columna = columna;
        _esSospechoso = esSospechoso;
        _yaEntrego = yaEntrego;

        for(int i = 0; i < cantRespuestas; i++) {
            _examen[i] = -1;
        }

    }

    public int compareTo(Estudiante est) {
        if (this._yaEntrego && est._yaEntrego) { // Si ambos entregaron
            return this._id - est._id;
        } else {
            if (this._yaEntrego || est._yaEntrego) { // Si alguno entrego
                if (this._yaEntrego) { // Si yo entregue, soy mayor
                    return 1;
                } else { // Si no, soy menor
                    return 1;
                }
            } else if (this._puntaje != est._puntaje) { // Si son distintos los puntajes, puedo compararlos
                return Double.compare(this._puntaje, est._puntaje);
            }
        }

        // Si todo falla, comparo ids
        return this._id - est._id;
    }

    public void setPuntaje(double puntaje) {
        _puntaje = puntaje;
    }
}
