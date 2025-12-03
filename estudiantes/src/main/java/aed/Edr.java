package aed;
import java.util.ArrayList;

public class Edr {
    ArrayList<MinHeap<Estudiante>.Handle> _estudiantes; 
    MinHeap<Estudiante> _puntajes;
    int[] _solucionCanonica;
    int _cantEntregados;
    int _ladoAula;
    

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
      
        _ladoAula = LadoAula;
        _estudiantes = new ArrayList<MinHeap<Estudiante>.Handle>();
        _solucionCanonica = ExamenCanonico.clone();
        _puntajes = new MinHeap<Estudiante>();
        
        // Creamos un array temporal para los estudiantes
        Estudiante[] tempEstudiantes = new Estudiante[Cant_estudiantes];

        // Es importante notar que los estudiantes terminan sentados en pos pares
        // Ej: LadoAula = 4, se sientan en 0 y 2
        int estudiantesPorFila = (_ladoAula + 1) / 2;

        for (int i = 0; i < Cant_estudiantes; i++) {
            int id = i + 1;

            // Calculamos la posicion con su id
            // Capaz es mas facil ver esto en una tabla si no se entiende: https://imagebin.ca/v/93Urkwzo9cI0
            int fila = i / estudiantesPorFila;
            int col = (i % estudiantesPorFila) * 2;

            Estudiante e = new Estudiante(_solucionCanonica.length, id, fila, col, false, false);
            tempEstudiantes[i] = e;
            
            // Esto es para despues poder usar set
            _estudiantes.add(null);
        }

        // Armamos el heap en O(E) con Heapify (esta en nuestro constructor)
        _puntajes = new MinHeap<>(tempEstudiantes);

        // Recuperamos las handles y las asignamos
        ArrayList<MinHeap<Estudiante>.Handle> handles = _puntajes.getHandles();

        // Asignamos cada handle en su lugar correspondiente
        for (MinHeap<Estudiante>.Handle h : handles) {
            int id = h.getElement().getId();
            _estudiantes.set(id - 1, h);
        }

    }

    private int cant_estudiantes(){
        return _estudiantes.size();
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
            res[i] = _estudiantes.get(i).getElement().getPuntaje();
        }

        return res;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    private Integer[] obtener_respuesta_de_otro_estudiante(Estudiante e, ArrayList<Estudiante> posibles_estudiantes_copiados) {
        
        if (posibles_estudiantes_copiados.size() > 0) {
         
            int[] cantidad_de_respuestas_faltantes = new int[posibles_estudiantes_copiados.size()];
            Integer[][] primeras_respuestas_faltantes = new Integer[posibles_estudiantes_copiados.size()][2];

            for (int pregunta = 0; pregunta < e.getExamen().length; pregunta++) {
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
    
    private Estudiante get_estudiante_aula(int fila, int columna) {
        // Chequeamos que no sea invalido
        if (fila < 0 || fila >= _ladoAula || columna < 0 || columna >= _ladoAula) {
            return null;
        }

        // Si la columna es impar no hay nadie aca por como armamos el salon, retornamos null
        if (columna % 2 != 0) {
            return null;
        }
        
        int estudiantesPorFila = (_ladoAula + 1) / 2;

        // Calculamos el indice del estudiante
        int indice = (fila * estudiantesPorFila) + (columna / 2);

        if (indice >= cant_estudiantes()) {
            return null;
        }

        return _estudiantes.get(indice).getElement();
    }

    private boolean hay_estudiante(int fila, int columna) {
        return get_estudiante_aula(fila, columna) != null;
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
                e.setCorrectas(e.getCorrectas() + 1);
            }
            e.setExamen(pregunta_y_respuesta[0], pregunta_y_respuesta[1]);
            e.setPuntaje(calcular_puntaje(e.getCorrectas()));
            _puntajes.ordenar_handle(h);
        }
            
        
    }   


//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        //El/la estudiante resuelve un ejercicio
        // O(log E)
        
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);
        Estudiante e = _estudiantes.get(estudiante).getElement();

        // Si ya entrego no hace nada
        if (e.getYaEntrego()) return;

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
            e.setCorrectas(e.getCorrectas() + 1);
        } else if (estabaCorrecta && !esCorrecta) {
            e.setCorrectas(e.getCorrectas() - 1);
        }
        
        
        // Recalculo el puntaje y lo guardo en O(1)
        double nuevoPuntaje = calcular_puntaje(e.getCorrectas()); 
        e.setPuntaje(nuevoPuntaje);

        // Aca actualizo el Heap, en O(log E)
        _puntajes.ordenar_handle(h); 
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

            e.setCorrectas(correctas);
            e.setPuntaje(calcular_puntaje(e.getCorrectas()));
            
            estudiantesParaReinsertar.add(e);
            kProcesados++;
        }

        for (Estudiante e : estudiantesParaReinsertar) {
            MinHeap<Estudiante>.Handle nuevoHandle = _puntajes.push(e);
            _estudiantes.set(e.getId() - 1, nuevoHandle);
        }
    }

 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);

        Estudiante e = h.getElement();
        if (e.getYaEntrego()) return;

        e.setYaEntrego(true);
        // Actualizo el Heap 
        // Como ahora e._yaEntrego es true, el compareTo lo va a considerar mayory lo va a mandar al fondo 
        // No modificamos su puntaje, así notas() sigue funcionando.
        _puntajes.ordenar_handle(h);
    }

   
//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        // Heap Auxiliar para ordenar las cosas
        MinHeap<NotaFinal> heapNotas = new MinHeap<>();
        int cantidadAprobados = 0;

        for (int i = 0; i < _estudiantes.size(); i++) {
            Estudiante e = _estudiantes.get(i).getElement();

            // Si entrego y NO es sospechoso, lo metemos en el heap
            if (!e.getEsSospechoso()) {
                heapNotas.push(new NotaFinal(e.getPuntaje(), i));
                cantidadAprobados++;
            }
        }

        // Los sacamos del heap, salen en orden asi que los metemos en el array
        NotaFinal[] resultado = new NotaFinal[cantidadAprobados];

        // Los insertamos en el orden correcto
        for (int i = cantidadAprobados-1; i >=0 ; i--) {
            resultado[i] = heapNotas.desencolar().getElement();
        }

        return resultado;
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {

        int maxOpcion = -1;
        for (int i = 0; i < _estudiantes.size(); i++) {
            Estudiante e = _estudiantes.get(i).getElement();

            for (int rta : e.getExamen()) {
                if (rta > maxOpcion) maxOpcion = rta;
            }
        }
        int cantOpciones = (maxOpcion == -1) ? 1 : maxOpcion + 1; 

        int[][] conteoRespuestas = new int[_solucionCanonica.length][cantOpciones];
        
        for (int i = 0; i < _estudiantes.size(); i++) {
            Estudiante e = _estudiantes.get(i).getElement();

            
            for (int preg = 0; preg < _solucionCanonica.length; preg++) {
                int rta = e.getRespuesta(preg);
                if (rta != -1) {
                    conteoRespuestas[preg][rta]++;
                }
            }
        }

        double umbral = (double)(cant_estudiantes() - 1) * 0.25;
        ArrayList<Integer> copiones = new ArrayList<>();

        for (int id = 0; id < _estudiantes.size(); id++) {
            MinHeap<Estudiante>.Handle h = _estudiantes.get(id);
            Estudiante e = h.getElement();

            
            boolean esSosp = true;      
            boolean respondioAlgo = false; 
            
            for (int preg = 0; preg < _solucionCanonica.length; preg++) {
                int rta = e.getRespuesta(preg);
                if (rta != -1) { 
                    respondioAlgo = true;
                    int coincidenciasExternas = conteoRespuestas[preg][rta] - 1; 

                    if (coincidenciasExternas < umbral || umbral == 0) {
                        esSosp = false; 
                        break;
                    }
                }
            }
            
            if (esSosp && respondioAlgo) {
                copiones.add(id);
                e.setEsSospechoso(true);
            } else {
                e.setEsSospechoso(false);
            }
            // _puntajes.ordenar_handle(h); Como esSospecoso no cambia el orden, podemos no actualizar el heap
        }
        
        int[] resultado = new int[copiones.size()];
        for(int i=0; i < copiones.size(); i++) {
            resultado[i] = copiones.get(i);
        }
        return resultado;
    }
}
