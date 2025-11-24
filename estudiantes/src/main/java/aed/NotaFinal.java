package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    public int compareTo(NotaFinal otra){
        int notaComparison = Double.compare(otra._nota, this._nota);

        // Comparar por nota (decreceinte)
        if (notaComparison != 0) {
            return notaComparison;
        }

        // Si las notas son iguales, comparar por ID (decreceinte)
        return Integer.compare(otra._id, this._id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotaFinal other = (NotaFinal) obj;
        return Double.compare(other._nota, _nota) == 0 && _id == other._id;
    }
}
