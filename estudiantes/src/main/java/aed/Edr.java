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
        _solucionCanonica = ExamenCanonico.clone(); // O(R)
        _puntajes = new MinHeap<Estudiante>();
        
        // Creamos un array temporal para los estudiantes
        Estudiante[] tempEstudiantes = new Estudiante[Cant_estudiantes];

        // Es importante notar que los estudiantes terminan sentados en pos pares
        // Ej: LadoAula = 4, se sientan en 0 y 2
        int estudiantesPorFila = (_ladoAula + 1) / 2;

        for (int i = 0; i < Cant_estudiantes; i++) { // O(E)

            // Calculamos la posicion con su id
            // Capaz es mas facil ver esto en una tabla si no se entiende: https://imagebin.ca/v/93Urkwzo9cI0
            int fila = i / estudiantesPorFila;
            int col = (i % estudiantesPorFila) * 2;

            Estudiante e = new Estudiante(_solucionCanonica.length, i, fila, col, false, false);
            tempEstudiantes[i] = e;
            
            // Esto es para despues poder usar set
            _estudiantes.add(null); // O(1)
        }

        // Armamos el heap en O(E) con Heapify (esta en nuestro constructor)
        _puntajes = new MinHeap<>(tempEstudiantes); // O(E)

        // Recuperamos las handles y las asignamos
        ArrayList<MinHeap<Estudiante>.Handle> handles = _puntajes.getHandles();

        // Asignamos cada handle en su lugar correspondiente
        for (MinHeap<Estudiante>.Handle h : handles) { // O(E)
            int id = h.getElement().getId();
            _estudiantes.set(id, h);
        }

        // Final: O(E * R)
    }

    private int cant_estudiantes(){
        return _estudiantes.size(); // O(1)
    }
    private double calcular_puntaje(int correctas) {
        return Math.floor((double)correctas * 100.0 / _solucionCanonica.length); // O(1)
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        //Devuelve una secuencia de las notas de todos los estudiantes ordenada por id.
        // O(E)
        double[] res = new double[_estudiantes.size()];

        for (int i = 0; i < _estudiantes.size(); i++) { // O(E)
            res[i] = _estudiantes.get(i).getElement().getPuntaje(); // O(1)
        }

        return res;

        // Final: O(E)
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    private void sumarPreguntaFaltante(int pregunta, Estudiante e, Estudiante[] posibles_estudiantes_copiados, int[] cantidad_de_respuestas_faltantes,Integer[][] primeras_preguntas_y_respuestas_faltantes){
        for (int i = 0; i < posibles_estudiantes_copiados.length; i++) { // O(1) pues siempre son 3 vecinos como maximo

            Estudiante posible_estudiante_copiado = posibles_estudiantes_copiados[i];

            // Si no tenemos la pregunta y el vecino si
            if (e.getRespuesta(pregunta) == -1 && posible_estudiante_copiado != null && posible_estudiante_copiado.getRespuesta(pregunta) != -1){
                cantidad_de_respuestas_faltantes[i] += 1;
                
                // Si no la habiamos encontrado antes, la guardamos
                if(cantidad_de_respuestas_faltantes[i] == 1){
                    primeras_preguntas_y_respuestas_faltantes[i][0] = pregunta;
                    primeras_preguntas_y_respuestas_faltantes[i][1] = posible_estudiante_copiado.getRespuesta(pregunta);
                }
            }   
        }

        // Final: O(1)
    }
    
    private boolean hayAlgunaRespuestaFaltante(int[] cantidad_de_respuestas_faltantes){
       // Chequea si algun vecino tiene algo util para copiarnos
        for (int cantidad : cantidad_de_respuestas_faltantes){ // O(1) pues siempre son 3 vecinos como maximo
            if (cantidad > 0){
                return true;
            }
        }

        return false;

        // Final: O(1)
    }

    private int indiceDelEstudianteConMasRespuestasFaltantes(int[] cantidad_de_respuestas_faltantes){
        int res_idx = 0;

        // Busca entre los vecinos cual tiene mas respuestas que nos falten
        for (int i = 1; i < cantidad_de_respuestas_faltantes.length; i++){ // O(1) pues siempre son 3 vecinos como maximo
            if (cantidad_de_respuestas_faltantes[res_idx] < cantidad_de_respuestas_faltantes[i]){
                res_idx = i;                
            }
        }

        return res_idx;

        // Final: O(1)
    }

    private Integer[] obtenerRespuestaDeOtroEstudiante(Estudiante e, Estudiante[] posibles_estudiantes_copiados) {
        
        // Chequeamos si nos podemos copiar de alguien
        if (posibles_estudiantes_copiados.length > 0) {
            
            int[] cantidad_de_respuestas_faltantes = new int[posibles_estudiantes_copiados.length];
            Integer[][] primeras_preguntas_y_respuestas_faltantes = new Integer[posibles_estudiantes_copiados.length][2];
            
            // Recorremos todas las preguntas del examen para ver cuales nos faltan
            for (int pregunta = 0; pregunta < e.getExamen().length; pregunta++) { // O(R)
                sumarPreguntaFaltante(pregunta, e, posibles_estudiantes_copiados, cantidad_de_respuestas_faltantes, primeras_preguntas_y_respuestas_faltantes);
            }
            
            // Vemos si hay alguna respuesta faltante
            boolean hay_respuesta = hayAlgunaRespuestaFaltante(cantidad_de_respuestas_faltantes);
            
            // Si hay una respuesta util, llamamos a indiceDelEstudianteConMasRespuestasFaltantes para
            // obtener el indice del vecino del cual nos vamos a copiar
            if (hay_respuesta){

                int res_idx = indiceDelEstudianteConMasRespuestasFaltantes(cantidad_de_respuestas_faltantes);
                return primeras_preguntas_y_respuestas_faltantes[res_idx];
            }
        }

        return null;

        // Final: O(R)
    }

    private Estudiante getEstudianteAula(int fila, int columna) {
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

        // Final: O(1), son solo calculos matematicos
    }

    private boolean hayEstudiante(int fila, int columna) {
        return getEstudianteAula(fila, columna) != null;

        // Final: O(1)
    }

    private  Estudiante[] obtenerVecinos(Estudiante e) {
        // Como es facil ubicar a los vecinos, los buscamos directamente con unas cuentas
        // para ver cuales estan para poder copiarnos
        Estudiante[] posiblesEstudiantesCopiados = new Estudiante[3];
        
        if (hayEstudiante(e.getFila(), e.getColumna() + 2)) { //miro a la derecha
            Estudiante ec = getEstudianteAula(e.getFila(), e.getColumna() + 2);
            if(!ec.getYaEntrego()){
                posiblesEstudiantesCopiados[0] = ec;
            }
        }

        if (hayEstudiante(e.getFila(), e.getColumna() - 2)) { //miro a la izquierda
            Estudiante ec = getEstudianteAula(e.getFila(), e.getColumna() - 2);
            if(!ec.getYaEntrego()){
                posiblesEstudiantesCopiados[1] = ec;
            }
        }

        if (hayEstudiante(e.getFila() - 1, e.getColumna())) { //miro adelante
            Estudiante ec = getEstudianteAula(e.getFila() - 1, e.getColumna());
            if(!ec.getYaEntrego()){
                posiblesEstudiantesCopiados[2] = ec;
            }
        }

        return posiblesEstudiantesCopiados;

        // Final: O(1) pues son 3 cuentas simples
    }
     
    public void copiarse(int estudiante) {
        // El/la estudiante se copia del vecino que mas respuestas
        // completadas tenga que el/ella no tenga; se copia solamente la
        // primera de esas respuestas. Desempata por id menor.
        // O(R + log E)
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante); // O(1)
        Estudiante e = h.getElement(); // O(1)
        
        Estudiante[] posibles_estudiantes_copiados = obtenerVecinos(e); // O(1)

        Integer[] pregunta_y_respuesta = obtenerRespuestaDeOtroEstudiante(e, posibles_estudiantes_copiados); // O(R)
        
        if (pregunta_y_respuesta != null) {

            if (_solucionCanonica[pregunta_y_respuesta[0]] == pregunta_y_respuesta[1]) {
                e.setCorrectas(e.getCorrectas() + 1); // O(1)
            }
            
            e.setExamen(pregunta_y_respuesta[0], pregunta_y_respuesta[1]); // O(1)
            e.setPuntaje(calcular_puntaje(e.getCorrectas())); // O(1)
            h.setElemento(e); // O(log E)
        }   
        
        // Final: O(R + log E)
    }   


//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        //El/la estudiante resuelve un ejercicio
        // O(log E)
        
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);
        Estudiante e = _estudiantes.get(estudiante).getElement();

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
        h.setElemento(e); 

        // Final: O(log E)
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
            MinHeap<Estudiante>.Handle h = _puntajes.desencolar(); // O(log E)

            Estudiante e = h.getElement();
            
            for (int j = 0; j < _solucionCanonica.length; j++) { // O(R)
                e.setExamen(j, examenDW[j]);
            }
            
            int correctas = 0;
            
            for (int j = 0; j < _solucionCanonica.length; j++) { // O(R)
                if (e.getRespuesta(j) == _solucionCanonica[j]) {
                    correctas++;
                }
            }

            e.setCorrectas(correctas);
            e.setPuntaje(calcular_puntaje(e.getCorrectas()));
            
            estudiantesParaReinsertar.add(e);
            kProcesados++;
        }

        for (Estudiante e : estudiantesParaReinsertar) { // O(k * log E)
            MinHeap<Estudiante>.Handle nuevoHandle = _puntajes.push(e);
            _estudiantes.set(e.getId(), nuevoHandle);
        }

        // Final: O(k * (R + log E))
    }

 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);

        Estudiante e = h.getElement();

        e.setYaEntrego(true);
        // Actualizo el Heap 
        // Como ahora e._yaEntrego es true, el compareTo lo va a considerar mayory lo va a mandar al fondo 
        // No modificamos su puntaje, así notas() sigue funcionando.
        h.setElemento(e); // O(log E)

        // Final: O(log E)
    }

   
//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        // Heap Auxiliar para ordenar las cosas
        MinHeap<NotaFinal> heapNotas = new MinHeap<>();
        int cantidadAprobados = 0;

        for (int i = 0; i < _estudiantes.size(); i++) { // O(E)
            Estudiante e = _estudiantes.get(i).getElement();

            // Si entrego y NO es sospechoso, lo metemos en el heap
            if (!e.getEsSospechoso()) {
                heapNotas.push(new NotaFinal(e.getPuntaje(), i)); // O(log E)
                cantidadAprobados++;
            }
        }

        // Los sacamos del heap, salen en orden asi que los metemos en el array
        NotaFinal[] resultado = new NotaFinal[cantidadAprobados];

        // Los insertamos en el orden correcto
        for (int i = cantidadAprobados-1; i >=0 ; i--) { // O(E)
            resultado[i] = heapNotas.desencolar().getElement(); // O(log E)
        }

        return resultado;

        // Final: O(E log E)
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {

        // Establece cual es la maxima opcion de respuesta posible
        int maxOpcion = calcularMaxValorRespuesta(); // O(E * R)

        // Hacemos una matriz con columnas = preguntas y filas = respuesta
        // En cada lugar va la cantidad de personas que contestaron esa opcion en esa pregunta
        // Sumamos 1 para no salirnos del indice, si el maximo valor es 3 queremos una longitud para 4 respuestas (0,1,2,3)
        int[][] conteoRespuestas = generarFrecuencias(maxOpcion + 1); // O(E * R)

        // Definimos el umbral para definir si es sospechoso o no
        double umbral = (double)(cant_estudiantes() - 1) * 0.25;
        ArrayList<Integer> copiones = new ArrayList<>();

        // Recorremos los estudiantes
        for (int id = 0; id < _estudiantes.size(); id++) { // O(E)
            // Agarramos el handle del array de handles por su id
            MinHeap<Estudiante>.Handle h = _estudiantes.get(id);
            Estudiante e = h.getElement();

            // Si es sospechoso, lo agegamos a la lista de copiones y lo seteamos en su atributo
            if (esSospechoso(e, conteoRespuestas, umbral)) {
                copiones.add(id);
                e.setEsSospechoso(true);
            } else {
                e.setEsSospechoso(false);
            }
        }

        // Lo pasamos a un array normal para devolver los copiones        
        int[] resultado = new int[copiones.size()];
        for(int i=0; i < copiones.size(); i++) { // O(E) si todos son copiones
            resultado[i] = copiones.get(i);
        }

        return resultado;

        // Final: O(E * R)
    }

    // Busca la maxima opcion de respuesta posible
    private int calcularMaxValorRespuesta() {
        int max = -1;

        for (int i = 0; i < _estudiantes.size(); i++) { // O(E)
            Estudiante e = _estudiantes.get(i).getElement();
            
            for (int rta: e.getExamen()) { // O(R)
                if (rta > max) {
                    max = rta;
                }
            }
        }

        return max;

        // Final: O(E * R)
    }

    // Genera la matriz con las frecuencias de cada respuesta
    private int[][] generarFrecuencias(int cantOpciones) {
        int[][] frecuencias = new int[_solucionCanonica.length][cantOpciones];

        // Recorro los estudiantes
        for (int i = 0; i < _estudiantes.size(); i++) { // O(E)
            Estudiante e = _estudiantes.get(i).getElement();

            // Recorro las respuestas del estudiante
            for (int preg = 0; preg < _solucionCanonica.length; preg++) { // O(R)
                int rta = e.getRespuesta(preg);

                // Si esta respondida, sumo uno a esa respuesta a la pregunta en la matriz
                if (rta != -1) {
                    frecuencias[preg][rta]++;
                }
            }
        }

        return frecuencias;

        // Final: O(E * R)
    }

    private boolean esSospechoso(Estudiante e, int[][] frecuencias, double umbral) {
        boolean respondioAlgo = false;

        // Recoremos todas las preguntas para ver las respuestas
        for (int preg = 0; preg < _solucionCanonica.length; preg++) { // O(R)
            int rta = e.getRespuesta(preg);

            // Verificamos si tiene respondida esta pregunta
            if (rta != -1) {
                respondioAlgo = true;

                // Restamos 1 para no contar la respuesta del estudiante actual
                int coincidenciasExternas = frecuencias[preg][rta] - 1;

                // Chequeamos si esta dentro del umbral, si no lo esta se libra de ser sospechoso
                if (coincidenciasExternas < umbral || umbral == 0) {
                    return false;
                }
            }
        }

        // Es sospechoso si respondio algo y todas sus respuestas estan dentro del umbral
        return respondioAlgo;

        // Final: O(R) 
    }
}
