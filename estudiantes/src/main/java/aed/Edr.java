package aed;
import java.util.ArrayList;

public class Edr {
    Estudiante[] _estudiantes; // Esto tiene que ser una lista de Handles. ARREGLAR
    MinHeap<Estudiante> _puntajes;
    int[][] _aula;
    int[] _solucionCanonica;
    boolean[] _yaEntregaron; 
    int _cantEntregados;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _estudiantes = new Estudiante[Cant_estudiantes];
        _aula = new int[LadoAula][LadoAula];
        _solucionCanonica = ExamenCanonico;
        _puntajes = new MinHeap<Estudiante>();
        _yaEntregaron = new boolean[Cant_estudiantes]; 
        _cantEntregados = 0;

        int posFila = 0;
        int posColumna = 0;
        
        for (int id = 1; id <= Cant_estudiantes; id++) {
            if (posColumna >= _aula.length) {
                posColumna = 0;
                posFila ++;
            }
            
            Estudiante e = new Estudiante(_solucionCanonica.length, id, posFila, posColumna, false, false);
            _aula[posFila][posColumna] = id;
            _estudiantes[id-1] = e;

            MinHeap<Estudiante>.Handle h = _puntajes.push(e);
            e.setHandle(h);
            
            posColumna += 2;
        }

        _puntajes = new MinHeap<Estudiante>(_estudiantes);
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        //Devuelve una secuencia de las notas de todos los estudiantes ordenada por id.
        // O(E)
        double[] res = new double[_estudiantes.length];

        for (int i = 0; i < _estudiantes.length; i++) {
            res[i] = _estudiantes[i]._puntaje;
        }

        return res;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    private Integer[] respuesta_examen_cercano(Estudiante e, ArrayList<Estudiante> posibles_estudiantes_copiados) {
        
        int[] cantidad_de_respuestas_faltantes = new int[posibles_estudiantes_copiados.size()];
        Integer[][] primeras_respuestas_faltantes = new Integer[posibles_estudiantes_copiados.size()][2];

        for (int pregunta = 0; pregunta < e._examen.length; pregunta++) {
            for (int i = 0; i < posibles_estudiantes_copiados.size(); i++) {
                if (e.getRespuesta(pregunta) == -1 && posibles_estudiantes_copiados.get(i).getRespuesta(pregunta) != -1){
                    cantidad_de_respuestas_faltantes[i] += 1;

                    if(cantidad_de_respuestas_faltantes[i] == 1){
                        primeras_respuestas_faltantes[i][0] = pregunta;
                        primeras_respuestas_faltantes[i][1] = posibles_estudiantes_copiados.get(i).getRespuesta(pregunta);
                        
                        if(posibles_estudiantes_copiados.size() == 1){
                            break;
                        }
                    }
                }   
            }
        }
        
        int res_idx = 0;
        for (int i = 1; i < posibles_estudiantes_copiados.size(); i++){
            if (cantidad_de_respuestas_faltantes[res_idx] < cantidad_de_respuestas_faltantes[i]){
                res_idx = i;                
            }
        }

        return primeras_respuestas_faltantes[res_idx];
    }

    private Estudiante get_estudiante_por_id(int id_estudiante) {
        return _estudiantes[id_estudiante - 1];
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
        Estudiante e = _estudiantes[estudiante];
        
        ArrayList<Estudiante> posibles_estudiantes_copiados = new ArrayList<Estudiante>();
        
        if (hay_estudiante(e.getFila(), e.getColumna() + 2)) {
            Estudiante ec = get_estudiante_aula(e.getFila(), e.getColumna() + 2);
            posibles_estudiantes_copiados.add(ec);
        }

        if (hay_estudiante(e.getFila(), e.getColumna() - 2)) {
            Estudiante ec = get_estudiante_aula(e.getFila(), e.getColumna() - 2);
            posibles_estudiantes_copiados.add(ec);
        }

        if (hay_estudiante(e.getFila() - 1, e.getColumna())) {
            Estudiante ec = get_estudiante_aula(e.getFila() - 1, e.getColumna());
            posibles_estudiantes_copiados.add(ec);
        }

        Integer[] pregunta_respuesta = respuesta_examen_cercano(e, posibles_estudiantes_copiados);

        e.setExamen(pregunta_respuesta[0], pregunta_respuesta[1]);

    }   


//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        //El/la estudiante resuelve un ejercicio
        // O(log E)

        Estudiante e = _estudiantes[estudiante];

        // Si ya entrego no hace nada
        if (e._yaEntrego) return;
        int respuestaAnterior = e.getRespuesta(NroEjercicio);
        e._examen[NroEjercicio] = res;
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
        double nuevoPuntaje = Math.floor((double)e._correctas * 100.0 / _solucionCanonica.length); 
        e.setPuntaje(nuevoPuntaje);

        // Aca actualizo el Heap, en O(log E)
        e.getHandle().setElemento(e); 
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
            if (h == null) break;

            Estudiante e = h.getElement();
            
            // Si nos encontramos con alguien que ya entregó, significa que se acabaron los estudiantes activos en el heap (porque están ordenados).
                        if (e._yaEntrego) {
                // Lo volvemos a meter porque no lo procesamos 
                // Aunque técnicamente ya salio 
                // break porque no hay más activos.
                break; 
            }
            
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
            e._puntaje = Math.floor((double)e._correctas * 100.0 / _solucionCanonica.length);
            
            estudiantesParaReinsertar.add(e);
            kProcesados++;
        }

        for (Estudiante e : estudiantesParaReinsertar) {
            MinHeap<Estudiante>.Handle nuevoHandle = _puntajes.push(e);
            e.setHandle(nuevoHandle);
        }
    }

 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        if (_yaEntregaron[estudiante]) return;

        _yaEntregaron[estudiante] = true;
        _cantEntregados++; 
        
        Estudiante e = _estudiantes[estudiante];
        
        e._yaEntrego = true; 

        // Actualizo el Heap 
        // Como ahora e._yaEntrego es true, el compareTo lo va a considerar mayory lo va a mandar al fondo 
        // No modificamos su puntaje, así notas() sigue funcionando.
        e.getHandle().setElemento(e);
    }

   
//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        ArrayList<NotaFinal> notasParaOrdenar = new ArrayList<>();
        
        for (int i = 0; i < _estudiantes.length; i++) {
            if (_yaEntregaron[i] && !_estudiantes[i]._esSospechoso) {
                notasParaOrdenar.add(new NotaFinal(_estudiantes[i]._puntaje, i));
            }
        }
        
        notasParaOrdenar.sort(null); 
        return notasParaOrdenar.toArray(new NotaFinal[0]);
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        if (_cantEntregados == 0) return new int[0];

        int maxOpcion = -1;
        for (int i = 0; i < _estudiantes.length; i++) {
            if (!_yaEntregaron[i]) continue;
            for (int rta : _estudiantes[i]._examen) {
                if (rta > maxOpcion) maxOpcion = rta;
            }
        }
        int cantOpciones = (maxOpcion == -1) ? 1 : maxOpcion + 1; 

        int[][] conteoRespuestas = new int[_solucionCanonica.length][cantOpciones];
        
        for (int i = 0; i < _estudiantes.length; i++) {
            if (!_yaEntregaron[i]) continue; 

            for (int preg = 0; preg < _solucionCanonica.length; preg++) {
                int rta = _estudiantes[i].getRespuesta(preg);
                if (rta != -1) {
                    conteoRespuestas[preg][rta]++;
                }
            }
        }

        double umbral = (double)(_cantEntregados - 1) * 0.25;
        ArrayList<Integer> copiones = new ArrayList<>();

        for (int id = 0; id < _estudiantes.length; id++) {
            if (!_yaEntregaron[id]) continue; 
            
            boolean esSosp = true;      
            boolean respondioAlgo = false; 
            
            for (int preg = 0; preg < _solucionCanonica.length; preg++) {
                int rta = _estudiantes[id].getRespuesta(preg);
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
                _estudiantes[id]._esSospechoso = true;
            } else {
                _estudiantes[id]._esSospechoso = false;
            }
        }
        
        int[] resultado = new int[copiones.size()];
        for(int i=0; i < copiones.size(); i++) {
            resultado[i] = copiones.get(i);
        }
        return resultado;
    }
}
