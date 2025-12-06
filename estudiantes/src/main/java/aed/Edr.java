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
     
    public void copiarse(int estudiante) {
        // El/la estudiante se copia del vecino que mas respuestas
        // completadas tenga que el/ella no tenga; se copia solamente la
        // primera de esas respuestas. Desempata por id menor.
        // O(R + log E)

        // Obtenemos el handle del estudiante y el estudiante
        MinHeap<Estudiante>.Handle h = _estudiantes.get(estudiante);
        Estudiante e = h.getElement();

        // Buscamos los vecinos
        ArrayList<Estudiante> vecinos = obtenerVecinos(e);

        // Obtenemos el mejor vecino para copiarse
        Estudiante mejorVecino = buscarMejorVecino(e, vecinos);

        // Si no es null, nos copiamos
        if (mejorVecino != null) {
            copiarRespuesta(e, mejorVecino);

            h.setElemento(e);
        }

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
    }
        
    private ArrayList<Estudiante> obtenerVecinos(Estudiante e) {
        // La funcion es bastante sencilla, hacemos una lista vacia y chequeamos
        // las 3 posibles posiciones, por la forma en la que estan ordenados se puede
        // hacer con 3 cuentas faciles
        ArrayList<Estudiante> vecinos = new ArrayList<>();

        // Derecha
        Estudiante derecha = getEstudianteAula(e.getFila(), e.getColumna() + 2);
        if (derecha != null) {
            vecinos.add(derecha);
        }

        // Izquierda
        Estudiante izquierda = getEstudianteAula(e.getFila(), e.getColumna() - 2);
        if (izquierda != null) {
            vecinos.add(izquierda);
        }

        // Adelate
        Estudiante adelante = getEstudianteAula(e.getFila() - 1, e.getColumna());
        if (adelante != null) {
            vecinos.add(adelante);
        }

        return vecinos;
    }

    private Estudiante buscarMejorVecino(Estudiante e, ArrayList<Estudiante> vecinos) {
        // El mejor vecino seria el que mas respuestas utiles tiene, es decir
        // respuestas que no haya respondido e pero si el vecino
        Estudiante mejorVecino = null;
        int maxRespuestasUtiles = 0;

        // Para cada vecino, contamos las respuestas utiles, el que mas tenga
        // sera el mejor vecino para copiarse
        for (Estudiante vecino: vecinos) {
            int utiles = contarRespuestasUtiles(e, vecino);

            if (utiles > 0) {
                if (utiles > maxRespuestasUtiles) {
                    maxRespuestasUtiles = utiles;
                    mejorVecino = vecino;
                } else if (utiles == maxRespuestasUtiles) {
                    if (mejorVecino == null || vecino.getId() < mejorVecino.getId()) {
                        mejorVecino = vecino;
                    }
                } 
            }
        }

        return mejorVecino;
    }

    private int contarRespuestasUtiles(Estudiante e, Estudiante vecino) {
        // Funcion que cuenta las respuestas que no respondimos y el vecino si
        int respuestasUtiles = 0;

        for (int i = 0; i < _solucionCanonica.length; i++) {
            if (e.getRespuesta(i) == -1 && vecino.getRespuesta(i) != -1) {
                respuestasUtiles++;
            }
        }

        return respuestasUtiles;
    }

    private void copiarRespuesta(Estudiante e, Estudiante vecino) {
        // Buscamos la primera respuesta que nos sirva del vecino y
        // la copiamos

        for (int i = 0; i < _solucionCanonica.length; i++) {
            if (e.getRespuesta(i) == -1 && vecino.getRespuesta(i) != -1) {
                int respuestaCopiada = vecino.getRespuesta(i);

                if (respuestaCopiada == _solucionCanonica[i]) {
                    // Si es correcta la copiada, actualizamos el atributo correctas del estudiante
                    e.setCorrectas(e.getCorrectas() + 1);
                }

                // Seteamos la respuesta copiada y el nuevo puntaje tras copiarnos
                e.setExamen(i, respuestaCopiada);
                e.setPuntaje(calcular_puntaje(e.getCorrectas()));

                return;
            }
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
        h.setElemento(e);
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

        // Establece cual es la maxima opcion de respuesta posible
        int maxOpcion = calcularMaxValorRespuesta();

        // Hacemos una matriz con columnas = preguntas y filas = respuesta
        // En cada lugar va la cantidad de personas que contestaron esa opcion en esa pregunta
        // Sumamos 1 para no salirnos del indice, si el maximo valor es 3 queremos una longitud para 4 respuestas (0,1,2,3)
        int[][] conteoRespuestas = generarFrecuencias(maxOpcion + 1);

        // Definimos el umbral para definir si es sospechoso o no
        double umbral = (double)(cant_estudiantes() - 1) * 0.25;
        ArrayList<Integer> copiones = new ArrayList<>();

        // Recorremos los estudiantes
        for (int id = 0; id < _estudiantes.size(); id++) {
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
        for(int i=0; i < copiones.size(); i++) {
            resultado[i] = copiones.get(i);
        }

        return resultado;
    }

    // Busca la maxima opcion de respuesta posible
    private int calcularMaxValorRespuesta() {
        int max = -1;

        for (int i = 0; i < _estudiantes.size(); i++) {
            Estudiante e = _estudiantes.get(i).getElement();
            
            for (int rta: e.getExamen()) {
                if (rta > max) {
                    max = rta;
                }
            }
        }

        return max;
    }

    // Genera la matriz con las frecuencias de cada respuesta
    private int[][] generarFrecuencias(int cantOpciones) {
        int[][] frecuencias = new int[_solucionCanonica.length][cantOpciones];

        // Recorro los estudiantes
        for (int i = 0; i < _estudiantes.size(); i++) {
            Estudiante e = _estudiantes.get(i).getElement();

            // Recorro las respuestas del estudiante
            for (int preg = 0; preg < _solucionCanonica.length; preg++) {
                int rta = e.getRespuesta(preg);

                // Si esta respondida, sumo uno a esa respuesta a la pregunta en la matriz
                if (rta != -1) {
                    frecuencias[preg][rta]++;
                }
            }
        }

        return frecuencias;
    }

    private boolean esSospechoso(Estudiante e, int[][] frecuencias, double umbral) {
        boolean respondioAlgo = false;

        // Recoremos todas las preguntas para ver las respuestas
        for (int preg = 0; preg < _solucionCanonica.length; preg++) {
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
    }
}
