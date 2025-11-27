package aed;

public class Estudiante implements Comparable<Estudiante>{
    private int[] _examen; // Arreglo de tamaño R que almacena las respuestas (o -1 si está enblanco)
    private double _puntaje; // El puntaje actual del estudiante
   
    private int _correctas; // Agregado para O(1) en resolver
    private int _id;
    private int _fila;
    private int _columna;
    private boolean _esSospechoso;
    private boolean _yaEntrego;

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
    
    // Getters
    
    public int getRespuesta(int pregunta) {
        return _examen[pregunta]; 
    }
    
    public int[] getExamen() {
        return _examen;
    }
    
    public double getPuntaje() {
        return _puntaje;
    }
    
    public int getCorrectas() {
        return _correctas;
    }
    
    public int getId() {
        return _id;
    }

    public int getFila(){
        return _fila;
    }

    public int getColumna(){
        return _columna;
    }

    public boolean getEsSospechoso() {
        return _esSospechoso;
    }

    public boolean getYaEntrego() {
        return _yaEntrego;
    }
    
    // Setters

    public void setExamen(int pregunta, int respuesta) {
        _examen[pregunta] = respuesta; 
    }
    
    public void setPuntaje(double puntaje) {
        _puntaje = puntaje;
    }

    public void setCorrectas(int correctas) {
        _correctas = correctas;
    }

    public void setEsSospechoso(boolean esSospechoso) {
        _esSospechoso = esSospechoso;
    }
    
    public void setYaEntrego(boolean yaEntrego) {
        _yaEntrego = yaEntrego;
    }

}
