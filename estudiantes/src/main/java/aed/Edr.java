package aed;
import java.util.ArrayList;

public class Edr {
    Estudiante[] _estudiantes; // Esto tiene que ser una lista de Handles. ARREGLAR
    MinHeap<Estudiante> _puntajes;
    int[][] _aula;
    int[] _solucionCanonica;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _estudiantes = new Estudiante[Cant_estudiantes];
        _aula = new int[LadoAula][LadoAula];
        _solucionCanonica = ExamenCanonico;
        
        int posFila = 0;
        int posColumna = 0;
        
        for (int id = 1; id <= Cant_estudiantes; id++) {
            if (posFila > _aula.length) {
                posFila = 0;
                posColumna ++;
            }
            
            Estudiante e = new Estudiante(_solucionCanonica.length, id, posFila, posColumna, false, false);
            _aula[posColumna][posFila] = id;
            _estudiantes[id-1] = e;
            
            posFila += 2;
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

    private Estudiante get_estudiante_aula(int columna, int fila) {
        int id_estudiante = _aula[columna][fila];
        return _estudiantes[id_estudiante - 1];
    }
    
    public void copiarse(int estudiante) {
        // El/la estudiante se copia del vecino que mas respuestas
        // completadas tenga que el/ella no tenga; se copia solamente la
        // primera de esas respuestas. Desempata por id menor.
        // O(R + log E)
        Estudiante e = _estudiantes[estudiante];
        
        ArrayList<Estudiante> posibles_estudiantes_copiados = new ArrayList<Estudiante>();
        
        if (e.getFila() + 2 < _aula.length && _aula[e.getColumna()][e.getFila() + 2] != 0) {
            Estudiante ec = get_estudiante_aula(e.getColumna(),e.getFila() + 2);
            posibles_estudiantes_copiados.add(ec);
        }

        if (e.getFila() - 2 >= 0 && _aula[e.getColumna()][e.getFila() - 2] != 0) {
            Estudiante ec = get_estudiante_aula(e.getColumna(),e.getFila() - 2);
            posibles_estudiantes_copiados.add(ec);
        }

        if (e.getColumna() - 1 >= 0 && _aula[e.getColumna() - 1][e.getFila()] != 0) {
            Estudiante ec = get_estudiante_aula(e.getColumna() - 1,e.getFila());
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
        e._examen[NroEjercicio] = res;
        int preguntasCorrectas = 0;

        for (int i = 0; i < e._examen.length; i++) {
            if (e._examen[i] == _solucionCanonica[i]) {
                preguntasCorrectas ++;
            }
        }

        e._puntaje = (preguntasCorrectas / _solucionCanonica.length) * 100;
        // Falta cambiar el tema de los puntajes en el heap. 
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
        throw new UnsupportedOperationException("Sin implementar");
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        // El/la estudiante entrega su examen
        // O(log E) según enunciado actualizado
        // Extraer del heap para que no sea considerado en consultarDarkWeb
        // (alternativa: mantener en heap y filtrar en consultarDarkWeb)
        throw new UnsupportedOperationException("Sin implementar");
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        // Devuelve las notas de los examenes de los estudiantes 
        // que no se hayan copiado ordenada por NotaFinal.nota de forma
        // decreciente. En caso de empate, se desempata 
        // por mayor NotaFinal.id de estudiante
        throw new UnsupportedOperationException("Sin implementar");
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        // Devuelve la lista de los estudiantes
        // sospechosos de haberse copiado ordenada por id de estudiante.
        // Encontrar el valor máximo de respuesta para dimensionar el array
        throw new UnsupportedOperationException("Sin implementar");
    }
}
