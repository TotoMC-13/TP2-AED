package aed;

import java.util.ArrayList;

// a.CompateTo(b) > 0 a mayor -- a.CompateTo(b) < 0 b mayor
public class MinHeap<T extends Comparable> {
	private ArrayList<Handle> elementos;

	// Constructor vacío 	para poder insertar manualmente desde Edr
    public MinHeap() {
        elementos = new ArrayList<Handle>();
    }

	// Complejidad O(n)
	public MinHeap(T[] objetos) {
		elementos = new ArrayList<Handle>();

		// Primero agrego todos asi nomas a elementos
		for (int i = 0; i < objetos.length; i++) {
			Handle h = new Handle(objetos[i], i);
			elementos.add(h);
		}

		// Algoritmo de Floyd
		// Empezamos desde el ultimo nodo con hijos y vamos subiendo hasta la raiz
		// O(n)
		for (int i = (elementos.size() / 2) - 1; i >= 0; i--) {
			bajar(elementos.get(i));
		}
	}

	public boolean estaVacio() {
        return elementos.isEmpty();
    }

	public class Handle {
		private T elemento;
		private int posicion;
		private boolean adentro_heap = true;
		
		Handle(T elemento, int posicion) {
			this.elemento = elemento;
			this.posicion = posicion;

		}

		public T getElement() {
			return elemento;
		}

		private int compareTo(Handle h) {
			return elemento.compareTo(h.elemento);
		}

		private void setPosicion(int posicion) {
			this.posicion = posicion;
		}

		public void setElemento(T elemento) { // AUXIOIo cambiar erstudiante desde el heap (?
			this.elemento = elemento;

			if (adentro_heap) {
				ordenar_handle(this);
			}
		}
	}

	// Renombrado de add a push y hecho público para devolver el Handle
	public Handle push(T elemento) {
		Handle h = new Handle(elemento, elementos.size());
		elementos.add(h);
		subir(h);
        return h;
	}

	private void cambiar_posicion_handle(Handle h, int posicion) {
		elementos.set(posicion, h);
		h.setPosicion(posicion);
	}		

	private void intercambiar_handles(Handle h1, Handle h2) {
		int p1 = h1.posicion;
		int p2 = h2.posicion;
		cambiar_posicion_handle(h2, p1);
		cambiar_posicion_handle(h1, p2);
	}

	private Handle getPadre(Handle h) {
		int posicion_padre = (int) Math.floor((h.posicion - 1) / 2);
		return elementos.get(posicion_padre);
	}

	private void bajar(Handle h) { // Esto se va a usar despues de borrar algo
		int hijoIzquierdo = 2 * h.posicion + 1;
		int hijoDerecho = 2 * h.posicion + 2;
		int masChico = h.posicion;

		if (hijoIzquierdo < elementos.size() && elementos.get(hijoIzquierdo).compareTo(elementos.get(masChico)) < 0) {
			masChico = hijoIzquierdo;
		}

		if (hijoDerecho < elementos.size() && elementos.get(hijoDerecho).compareTo(elementos.get(masChico)) < 0) {
			masChico = hijoDerecho;
		}

		if (masChico != h.posicion) {
			intercambiar_handles(h, elementos.get(masChico));
			bajar(h);
		}
	}
	
	private void subir(Handle h) { // Esto se usa despues de agregar algo
		while (h.posicion > 0) {
			Handle padre = getPadre(h);

			if (h.compareTo(padre) >= 0) { // Esto significa que ya esta en su lugar
				break; 
			}

			intercambiar_handles(h, padre);
		}
	}

	private void ordenar_handle(Handle h) {
		subir(h);
		bajar(h);
	}

	public Handle desencolar() {
		if (elementos.size() == 0) {
			return null;
		}

		Handle res = elementos.get(0);
		Handle h2 = elementos.get(elementos.size() - 1);
		intercambiar_handles(res, h2);
		elementos.remove(elementos.size() - 1);
		ordenar_handle(h2);

		res.adentro_heap = false;
		return res;
	}

	public ArrayList<Handle> getHandles() {
		return elementos;
	}
}
