package aed;
import java.util.ArrayList;

public class Edr {
    ArrayList<MinHeap<Estudiante>.Handle> _estudiantes; 
    MinHeap<Estudiante> _puntajes;
    int[][] _aula;
    int[] _solucionCanonica;
    boolean[] _yaEntregaron; 
    int _cantEntregados;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
      
        _estudiantes = new ArrayList<MinHeap<Estudiante>.Handle>();
        _aula = new int[LadoAula][LadoAula];
        _solucionCanonica = ExamenCanonico;
        _puntajes = new MinHeap<Estudiante>();
        _yaEntregaron = new boolean[Cant_estudiantes]; 
        _cantEntregados = 0;

        int posFila = 0;
        int posColumna = 0;

        _puntajes = new MinHeap<Estudiante>();

        
        for (int id = 1; id <= Cant_estudiantes; id++) {
            if (posColumna >= _aula.length) {
                posColumna = 0;
                posFila ++;
            }
            
            Estudiante e = new Estudiante(_solucionCanonica.length, id, posFila, posColumna, false, false);
            _aula[posFila][posColumna] = id;
           
            _estudiantes.add( _puntajes.push(e)) ;
            
            posColumna += 2;
        }


    }

    private double calcular_puntaje(int correctas) {
        return Math.floor((double)correctas * 100.0 / _solucionCanonica.length);
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        //Devuelve una secuencia de las notas de todos los estudiantes ordenada por id.
        // O(E)
        double[] res = new double[_estudiantes.size()];

        for (int i = 0; i < _estudiantes.size(); i++) {
            res[i] = _estudiantes.get(i).getElement()._puntaje;
        }

        return res;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    private Integer[] obtener_respuesta_de_otro_estudiante(Estudiante e, ArrayList<Estudiante> posibles_estudiantes_copiados) {
        
        if (posibles_estudiantes_copiados.size() > 0) {
         
            int[] cantidad_de_respuestas_faltantes = new int[posibles_estudiantes_copiados.size()];
            Integer[][] primeras_respuestas_faltantes = new Integer[posibles_estudiantes_copiados.size()][2];

            for (int pregunta = 0; pregunta < e._examen.length; pregunta++) {
                for (int i = 0; i < posibles_estudiantes_copiados.size(); i++) {
                    if (e.getRespuesta(pregunta) == -1 && posibles_estudiantes_copiados.get(i).getRespuesta(pregunta) != -1){
                        cantidad_de_respuestas_faltantes[i] += 1;

                        if(cantidad_de_respuestas_faltantes[i] == 1){
                            primeras_respuestas_faltantes[i][0] = pregunta;
                            primeras_respuestas_faltantes[i][1] = posibles_estudiantes_copiados.get(i).getRespuesta(pregunta);
                        }
                    }   
                }
            }
        
            boolean todos_cero = true;
            for (int cantidad : cantidad_de_respuestas_faltantes){
                if (cantidad > 0){
                    todos_cero = false;
                }
            }
            if (!todos_cero){
        
            
                int res_idx = 0;
                for (int i = 1; i < posibles_estudiantes_copiados.size(); i++){
                    if (cantidad_de_respuestas_faltantes[res_idx] < cantidad_de_respuestas_faltantes[i]){
                        res_idx = i;                
                    }
                }

                return primeras_respuestas_faltantes[res_idx];
            }
        }
        return null;
    }

    private Estudiante get_estudiante_por_id(int id_estudiante) {
        return _estudiantes.get(id_estudiante - 1).getElement();
    }
    
    private Estudiante get_estudiante_aula(int fila, int columna) {
        int id_estudiante = _aula[fila][columna];
        return get_estudiante_por_id(id_estudiante);
    }
    
    private boolean la_posicion_es_valida(int fila, int columna) {
        return (fila >= 0 && fila < _aula.length && columna >= 0 && columna < _aula.length);
    }

    private boolean la_posicion_esta_ocupada(int fila, int columna) {
        return _aula[fila][columna] != 0;
    }

    private boolean hay_estudiante(int fila, int columna) {
        return la_posicion_es_valida(fila, columna) && la_posicion_esta_ocupada(fila, columna);
    }
     
    public void copiarse(int estudiante) {
        // El/la estudiante se copia del vecino que mas respuestas
        // completadas tenga que el/ella no tenga; se copia solamente la
        // primera de esas respuestas. Desempata por id menor.
        // O(R + log E)
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);
        Estudiante e = h.getElement();
        
        ArrayList<Estudiante> posibles_estudiantes_copiados = new ArrayList<Estudiante>();
        
        if (hay_estudiante(e.getFila(), e.getColumna() + 2)) { //miro a la derecha
            Estudiante ec = get_estudiante_aula(e.getFila(), e.getColumna() + 2);
            posibles_estudiantes_copiados.add(ec);
        }

        if (hay_estudiante(e.getFila(), e.getColumna() - 2)) { //miro a la izquierda
            Estudiante ec = get_estudiante_aula(e.getFila(), e.getColumna() - 2);
            posibles_estudiantes_copiados.add(ec);
        }

        if (hay_estudiante(e.getFila() - 1, e.getColumna())) { //miro adelante
            Estudiante ec = get_estudiante_aula(e.getFila() - 1, e.getColumna());
            posibles_estudiantes_copiados.add(ec);
        }

        Integer[] pregunta_y_respuesta = obtener_respuesta_de_otro_estudiante(e, posibles_estudiantes_copiados);
        
        if (pregunta_y_respuesta != null) {
            if (_solucionCanonica[pregunta_y_respuesta[0]] == pregunta_y_respuesta[1]) {
                e._correctas += 1;
            }
            e.setExamen(pregunta_y_respuesta[0], pregunta_y_respuesta[1]);
            e.setPuntaje(calcular_puntaje(e._correctas));
            h.setElemento(e);
        }
            
        
    }   


//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        //El/la estudiante resuelve un ejercicio
        // O(log E)
        
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);
        Estudiante e = _estudiantes.get(estudiante).getElement();

        // Si ya entrego no hace nada
        if (e._yaEntrego) return;

        int respuestaAnterior = e.getRespuesta(NroEjercicio);
        
        int respuestaCorrecta = _solucionCanonica[NroEjercicio];
      
        // Actualizo respuesta en O(1)
        e.setExamen(NroEjercicio, res);

        // Actualizo cantidad de correctas en O(1) 
        // (Si estaba bien + ahora mal -> resta)
        // (Si estaba mal/no contestada + ahora bien -> suma
        boolean estabaCorrecta = (respuestaAnterior == respuestaCorrecta);
        boolean esCorrecta = (res == respuestaCorrecta);        
        if (!estabaCorrecta && esCorrecta) {
            e._correctas++;
        } else if (estabaCorrecta && !esCorrecta) {
            e._correctas--;
        }
        
        
        // Recalculo el puntaje y lo guardo en O(1)
        double nuevoPuntaje = calcular_puntaje(e._correctas); 
        e.setPuntaje(nuevoPuntaje);

        // Aca actualizo el Heap, en O(log E)
        h.setElemento(e); 
    }



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        // Los/as k estudiantes que tengan el
        // peor puntaje (hasta el momento) reemplazan completamente su 
        // examen por el examenDW. Nota: en caso de empate en el puntaje,
        // se desempata por menor id de estudiante.
        // O(k * (R + log E)) - peor caso O(E * (R + log E))        
        // IMPORTANTE: Solo extraer y procesar, no reinsertar los que ya entregaron
        // Si ya entregó, simplemente no lo reinsertamos
        // (queda fuera del heap permanentemente)

        ArrayList<Estudiante> estudiantesParaReinsertar = new ArrayList<>();
        int kProcesados = 0;
        
        while (kProcesados < n && !_puntajes.estaVacio()) {
            // Al sacar del heap, obtenemos el menor.
            // Como los que entregaron tienen _yaEntrego=true, son mayores así que están al fondo
            MinHeap<Estudiante>.Handle h = _puntajes.desencolar();

            Estudiante e = h.getElement();
            
            for (int j = 0; j < _solucionCanonica.length; j++) {
                e.setExamen(j, examenDW[j]);
            }
            
            int correctas = 0;
            for (int j = 0; j < _solucionCanonica.length; j++) {
                if (e.getRespuesta(j) == _solucionCanonica[j]) {
                    correctas++;
                }
            }
            e._correctas = correctas;
            e.setPuntaje(calcular_puntaje(e._correctas));
            
            estudiantesParaReinsertar.add(e);
            kProcesados++;
        }

        for (Estudiante e : estudiantesParaReinsertar) {
            MinHeap<Estudiante>.Handle nuevoHandle = _puntajes.push(e);
            _estudiantes.set(e._id - 1, nuevoHandle);
        }
    }

 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        if (_yaEntregaron[estudiante]) return;

        _yaEntregaron[estudiante] = true;
        _cantEntregados++; 
        
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);
        Estudiante e = h.getElement();
        e._yaEntrego = true; 

        // Actualizo el Heap 
        // Como ahora e._yaEntrego es true, el compareTo lo va a considerar mayory lo va a mandar al fondo 
        // No modificamos su puntaje, así notas() sigue funcionando.
        h.setElemento(e);
    }

   
//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {//TODO: hacer con el heap
        ArrayList<NotaFinal> notasParaOrdenar = new ArrayList<>();
        
        for (int i = 0; i < _estudiantes.size(); i++) {
            if (_yaEntregaron[i] && !_estudiantes.get(i).getElement()._esSospechoso) {
                notasParaOrdenar.add(new NotaFinal(_estudiantes.get(i).getElement()._puntaje, i));
            }
        }
        
        notasParaOrdenar.sort(null); 
        return notasParaOrdenar.toArray(new NotaFinal[0]);
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        if (_cantEntregados == 0) return new int[0];

        int maxOpcion = -1;
        for (int i = 0; i < _estudiantes.size(); i++) {
            Estudiante e = _estudiantes.get(i).getElement();
            if (!e._yaEntrego) continue;
            for (int rta : e._examen) {
                if (rta > maxOpcion) maxOpcion = rta;
            }
        }
        int cantOpciones = (maxOpcion == -1) ? 1 : maxOpcion + 1; 

        int[][] conteoRespuestas = new int[_solucionCanonica.length][cantOpciones];
        
        for (int i = 0; i < _estudiantes.size(); i++) {
            Estudiante e = _estudiantes.get(i).getElement();
            if (!e._yaEntrego) continue;

            for (int preg = 0; preg < _solucionCanonica.length; preg++) {
                int rta = e.getRespuesta(preg);
                if (rta != -1) {
                    conteoRespuestas[preg][rta]++;
                }
            }
        }

        double umbral = (double)(_cantEntregados - 1) * 0.25;
        ArrayList<Integer> copiones = new ArrayList<>();

        for (int id = 0; id < _estudiantes.size(); id++) {
            MinHeap<Estudiante>.Handle h = _estudiantes.get(id);
            Estudiante e = h.getElement();

            if (!e._yaEntrego) continue; 
            
            boolean esSosp = true;      
            boolean respondioAlgo = false; 
            
            for (int preg = 0; preg < _solucionCanonica.length; preg++) {
                int rta = e.getRespuesta(preg);
                if (rta != -1) { 
                    respondioAlgo = true;
                    int coincidenciasExternas = conteoRespuestas[preg][rta] - 1; 

                    if (coincidenciasExternas < umbral) {
                        esSosp = false; 
                        break;
                    }
                }
            }
            
            if (esSosp && respondioAlgo) {
                copiones.add(id);
                e._esSospechoso = true;
            } else {
                e._esSospechoso = false;
            }
            h.setElemento(e);
        }
        
        int[] resultado = new int[copiones.size()];
        for(int i=0; i < copiones.size(); i++) {
            resultado[i] = copiones.get(i);
        }
        return resultado;
    }
}
