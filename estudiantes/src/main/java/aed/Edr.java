package aed;
import java.util.ArrayList;

public class Edr {

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        //Devuelve una secuencia de las notas de todos los estudiantes ordenada por id.
        // O(E)
        throw new UnsupportedOperationException("Sin implementar");
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        // El/la estudiante se copia del vecino que mas respuestas
        // completadas tenga que el/ella no tenga; se copia solamente la
        // primera de esas respuestas. Desempata por id menor.
        // O(R + log E)
        throw new UnsupportedOperationException("Sin implementar");
    }


//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        //El/la estudiante resuelve un ejercicio
        // O(log E)
        throw new UnsupportedOperationException("Sin implementar");
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
