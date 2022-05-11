package edd.estructuras;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

public class ArbolBinarioBusqueda<T extends Comparable<T>> extends ArbolBinario<T> {
    static int index;
    /*
     * Clase para contruir al iterador de la clase
     */
    private class Iterador implements Iterator<T>{
        private Pila<Vertice> pila;

        public Iterador(){
            pila = new Pila<Vertice>();
            Vertice p = raiz;
            while (p!= null) {
                pila.push(p);
                p = p.izquierdo;    
            }
        }

        public T next(){
            if(pila.isEmpty()){
                throw new NoSuchElementException("vacio");
            }
            Vertice v = pila.pop();
            if(v.derecho != null){
                Vertice u = v.derecho;
                while (u!=null) {
                    pila.push(u);
                    u=u.izquierdo;
                }
            }

            return v.elemento;
        }

        /* Nos dice si hay un elemento siguiente. */
        public boolean hasNext(){
            return !pila.isEmpty();
        }
    }

    /**Ultimo vértice agregado al arbol BST, se inicializa en add*/
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parametros. 
     */
    public ArbolBinarioBusqueda(){ 
        super(); 
    }

    /**
     * Construye un arbol BST a partir de una colección. El árbol
     * BST tiene los mismos elementos que la colección recibida.
     * 
     * @param coleccion la colección a partir de la cual creamos el árbol AVL 
     */
    public ArbolBinarioBusqueda(Collection<T> coleccion) {
        super(coleccion);
    }

    /**
     * Constructor de la clase ocupa dos métodos auxiliares. 
     * @param lista
     * @param isSorted
     */
    public ArbolBinarioBusqueda(Lista<T> lista, boolean isSorted ){
        if (isSorted) {
            raiz = buildSorted(lista);
            elementos = lista.size();
        }
        else{
            raiz = buildUnsorted(lista);
            elementos = lista.size();
        }
    }

     /**
     * Funcion para construir un arbol apartir de una lista ordenada
     * @param lista
     * @return raiz
     */
    public Vertice buildSorted(Lista<T> lista){ 
        int izq=0; 
        int der=lista.size()-1;
        IteradorLista<T> iter = lista.iteradorLista();
        Vertice raiz = auxbuildSorted(iter,izq,der);
        return raiz;
    }

        /**
     * Primero sacamos el elemento a la indix de la mitad, 
     * construimos recursivamente todos los arboles a la izquierda,
     * pasamos al sigueinte de la raiz, y contruimos recursivamente todos los
     * elementos a la derecha.
     * Vemos que la complejidad de 
     * O(auxbuildSorted(n)) = O(1) + O(auxbuildSorted(n/2)) + O(auxbuildSorted(n/2))
     * O(auxbuildSorted(n/2)) = 0(1) +  O(auxbuildSorted(n/4)) + O(auxbuildSorted(n/4))
     * ...
     * O(auxbuildSorted(n)) = O(1) + O(n) = O(n)
     * 
     * @param iter - iterador para movernos sobre la cabeza de la lista
     * @param izq - index más a izquierda
     * @param der - index mas a la derecha
     * @return root
     */
    private Vertice auxbuildSorted(IteradorLista<T> iter, int izq, int der){
        int mid;
        if(izq > der){
            return null;
        }
        mid = (izq + der)/2;
        Vertice izquierdo = auxbuildSorted(iter, izq, mid-1);
        Vertice root = nuevoVertice(iter.next());
        root.izquierdo = izquierdo;
        root.derecho = auxbuildSorted(iter, mid+1, der);
        return root;
    }
    /**
     * Metodo para construir un arbol a partir de una lista desordenada
     * @param lista
     * @return
     */
    public Vertice buildUnsorted(Lista<T> lista){
        Lista<T> sorteado = lista.mergeSort(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        });
        int izq=0; 
        int der=sorteado.size()-1;
        IteradorLista<T> iter = sorteado.iteradorLista();
        Vertice raiz = auxbuildSorted(iter,izq,der);
        return raiz;
    }

    /**
     * Función que transforma un BT en un BST pasandolo primeramente a una Lista de elementos T.
     * @param raiz
     */
    public void convertBST(Vertice raiz)
    {
        if (raiz == null){
            return;
        }
        int n = countVertices(raiz);
        Lista<T> arr = new Lista<>();    
        for (int i=0; i<n; i++){
            arr.add(raiz.elemento);
        } 
        ordenar(raiz, arr);
        Lista<T> sorteado = arr.mergeSort(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        });         
        index = 0;
        arrayToBST(sorteado, raiz);
    }
    
    /**
     * Función auxiliar que ayuda a contar los vértices de un árbol
     * @param raiz
     * @return
     */
    private int countVertices(Vertice raiz){
        if (raiz == null)
            return 0;
        return countVertices(raiz.izquierdo) + countVertices(raiz.derecho) + 1;
    }

    /**
     * Función auxiliar que permite ordenar de manera ordenada el BT. 
     * Esta función es uxiliar para convertBST.
     * @param node
     * @param inorder
     */
    private void ordenar(Vertice verti,Lista<T> inorder){
        if (verti == null){
            return;
        }
        ordenar(verti.izquierdo, inorder);
        inorder.insert(index, verti.get());
        index++;
        ordenar(verti.derecho, inorder);
    }

    /**
     * Función que copia el contenido de una Lista<T> a un BT.
     * @param arr
     * @param raiz
     */
    private void arrayToBST(Lista<T> arr, Vertice raiz)
    {
        if (raiz == null)
            return;
        arrayToBST(arr, raiz.izquierdo);
        raiz.elemento = arr.get(index);
        index++;
        arrayToBST(arr, raiz.derecho);
    }

    /**
     * Función para encontrar un elemento de tipo T en nuestro BST, en caso de que el elemento se encuentre
     * regresa el Vertice, de lo contrario regresa null.
     * @param elemento
     * @return
     */
    public Vertice search(Vertice raiz, T contenido){
        if (raiz==null || raiz.elemento==contenido)
            return raiz;
        if (raiz.elemento.compareTo(contenido) < 0){

            return search(raiz.derecho, contenido);
        }else{
            return search(raiz.izquierdo, contenido);
        }
    }

    /**
     * Función add que funciona al igual que extend
     */
    public void add(T elemento){
        elementos++;
        Vertice nuevo = nuevoVertice(elemento);

        if (raiz == null) { 
            raiz = nuevo;
            raiz.padre = null; 
            ultimoAgregado = raiz;
            return; 
        } 

        insert(raiz, nuevo); 
        ultimoAgregado = nuevo;
    }
   
    /**
     * Función que permite insertar en el lugar apropiado del árbol un elemento T
     * @param raiz
     * @param elemento
     * @return
     */
    protected void insert(Vertice actual, Vertice nuevo) { 

        if (nuevo.elemento.compareTo(actual.elemento) < 0){
            if (actual.izquierdo == null) {
				actual.izquierdo = nuevo;
				nuevo.padre = actual;
				return;
			}
            insert(actual.izquierdo,nuevo); 
        }
        else if (nuevo.elemento.compareTo(actual.elemento) >= 0){
            if (actual.derecho == null) {
				actual.derecho = nuevo;
				nuevo.padre = actual;
				return;
			}
            insert(actual.derecho, nuevo); 
        }
    } 

    /**
     * Función que elimina un elemento del árbol. Si el nodo tiene un hijo,
     * se sube el hijo al lugar del padre. si el nodo tiene más de un hijo, deberás 
     * encontrar al sucesor inOrder y reemplazarlo en el árbol. 
     * El cual sera el mínimo de el subárbol derecho del nodo a eliminar.
     */
    public boolean delete(T elemento){

		Vertice eliminar = search(raiz,elemento);

        if (eliminar == null){
            return false;
        }else{
            /*Si tiene dos hijos se intercambian los elementos con el vertice minimo 
            del subarbol derecho*/
            if(eliminar.hayDerecho() && eliminar.hayIzquierdo()){
                eliminar = intercambia(eliminar);
            }
            deleteRecursive(eliminar);
        }

        elementos--;

        return true;
    }
 
    /**
     * Función recursiva para eliminar un vertice con a lo más un hijo. 
     * Se sube al hijo al lugar del padre. 
     * Si el vertice es una hoja se actualiza la referencia de su padre a null. 
     * @param raiz
     * @param elemento
     * @return
     */
    private void deleteRecursive(Vertice eliminar)
    {   
        Vertice hijo = null;

        if(eliminar.hayIzquierdo()){
            hijo = eliminar.izquierdo;
        }else if(eliminar.hayDerecho()){
            hijo = eliminar.derecho;
        }

        //Verifica que el vertice a eliminar no sea la raiz
        if(eliminar.padre != null){

            if(eliminar.padre.izquierdo == eliminar){
                //Se sube al hijo (hijo puede ser null cuando se elimina una hoja)
                eliminar.padre.izquierdo = hijo;
            }else{
                eliminar.padre.derecho = hijo;
            }
        }else{
            raiz = hijo;
        }

        if(hijo != null){
            hijo.padre = eliminar.padre; 
        }

    }
    
    /**
     * Método para intercambiar el valor un vertice con el 
     * minimo de su subárbol derecho.
     * @param vertice - vertice a intercabiar 
     * @return Vertice - vertice ya intercambiado
     */
    private Vertice intercambia(Vertice vertice){
        Vertice minv = valorMinimo(vertice.derecho);
        Vertice aux = vertice; 
        vertice.elemento = minv.elemento;
        minv.elemento = aux.elemento;
        return minv;
    }
    /**
     * Metodo para encontrar el elemento minimo en el arbol
     * @param raiz
     * @return elemento minimo 
     */
    private Vertice valorMinimo(Vertice raiz)
    {
        if(raiz.izquierdo == null){
            return raiz;
        }
        return valorMinimo(raiz.izquierdo);
    }

    /**
     * Metodo para balancear un arbol BTS, como agregar a la lista es lineal y buildSorted lineal.
     * La complejidad de balance tambien es lineal. 
     * @param root
     */
    public void balance(Vertice root){
        Lista<T> elems = new Lista<T>();
        for( T i : this){
            elems.add(i);
        }
        raiz = buildSorted(elems);
    }

    /**
     * Al girar a la derecha el vértice, su hijo izquierdo se vuelve su padre 
     * y el subárbol derecho del que antes era su hijo, se vuelve si subarbol 
     * izquierdo 
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
		Vertice v = vertice(vertice);
		if (v.izquierdo == null) {
			return;
		}
        //hi es hijo izquierdo 
		Vertice hi = v.izquierdo;
		if (v.padre == null) {
			raiz = hi;
		} else if (v.padre.izquierdo == vertice) {
			v.padre.izquierdo = hi;
		} else {
			v.padre.derecho = hi;
		}
        //Se intercambian los padres 
		hi.padre = v.padre;
		v.padre = hi;
		if (hi.derecho != null) {
			hi.derecho.padre = v;
		}
        //Se intercambian los subarboles derechos 
		v.izquierdo = hi.derecho;
		hi.derecho = v;
    }

    /**
     * Al girar a la izquierda un vértice, su hijo derecho se vuleve 
     * su padre y el subárbol izquierdo del que antes era su hijo, 
     * se vuelve su subárbol derecho
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
		Vertice v = vertice(vertice);
		if (v.derecho == null) {
			return;
		}
        // hd es HIjo derecho 
		Vertice hd = v.derecho;
		if (v.padre == null) {
			raiz = hd;
		} else if (v.padre.izquierdo == vertice) {
			v.padre.izquierdo = hd;
		} else {
			v.padre.derecho = hd;
		}
        //Se intercambian los padres 
		hd.padre = v.padre;
		v.padre = hd;
		if (hd.izquierdo != null) {
			hd.izquierdo.padre = v;
		}
        //Se intercambian los subarboles izquierdos 
		v.derecho = hd.izquierdo;
		hd.izquierdo = v;
    }

    //Regresa el toString en DFS

    // @Override public String toString(){
    //     String s=""; 
    //     for(T elemento: this){
    //         s += elemento;
    //     }
    //     return s + "\n";
    // }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * 
     * @return un iterador para iterar el árbol.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterador();
    }
}
