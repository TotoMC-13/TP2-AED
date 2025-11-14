package aed;

import java.util.ArrayList;

// a.CompateTo(b) > 0 a mayor --  a.CompateTo(b) < 0 b menor
public class MinHeap<T extends Comparable> {
	private ArrayList<Handle> elementos;

	public MinHeap(ArrayList<T> objetos) {
		elementos = new ArrayList<Handle>();

		for (T o : objetos) {
			add(o);
		}
	}

	public class Handle {
		private T elemento;
		private int posicion;

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

		public void setElemento(T elemento) {
			this.elemento = elemento;
			ordenar_handle(this);
		}
	}

	private void cambiar_posicion_handle(Handle h, int posicion) {
		elementos.add(posicion, h);
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

	private Handle getHijos(Handle h, int numero_hijo) {
		int posicion_hijo = 2 * h.posicion + numero_hijo;
		return elementos.get(posicion_hijo);
	}

	private void bajar(Handle h) {
		if (h.posicion == 0)
			return;

		Handle hijo1 = getHijos(h, 1);

		if (hijo1.compareTo(h) < 0) {
			intercambiar_handles(hijo1, h);
			subir(h);
			return;
		}

		Handle hijo2 = getHijos(h, 1);

		if (hijo2.compareTo(h) < 0) {
			intercambiar_handles(hijo2, h);
			subir(h);
			return;
		}
	}

	private void subir(Handle h) {
		if (h.posicion == 0)
			return;

		Handle padre = getPadre(h);

		if (padre.compareTo(h) > 0) {
			intercambiar_handles(padre, h);
			subir(h);
		}
	}

	private void add(T o) {
		Handle h = new Handle(o, elementos.size());
		elementos.add(h);
		subir(h);
	}

	private void ordenar_handle(Handle h) {
		subir(h);
		bajar(h);
	}

	public Handle desencolar() {
		Handle res = elementos.get(0);
		Handle h2 = elementos.get(elementos.size() - 1);
		intercambiar_handles(res, h2);
		elementos.remove(elementos.size() - 1);
		ordenar_handle(h2);
		return res;
	}
}