package edd.modelo.Estructuras;
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
    if (raiz.elemento.compareTo(contenido) < 0)
       return search(raiz.derecho, contenido);
    return search(raiz.izquierdo, contenido);
    }

    /**
     * Función add que funciona al igual que extend
     */
    public void add(T elemento){
        insert(elemento);
    }

    /**
     * Función que nos permite hacer la inserción de un elemento T en el árbol
     * @param elemento
     */
    public void insert(T elemento)  { 
        raiz = insert_Recursive(raiz, elemento); 
    } 
   
    /**
     * Función que permite insertar en el lugar apropiado del árbol un elemento T
     * @param raiz
     * @param elemento
     * @return
     */
    protected Vertice insert_Recursive(Vertice raiz, T elemento) { 
        if (raiz == null) { 
            raiz = nuevoVertice(elemento); 
            return raiz; 
        } 
        if (elemento.compareTo(raiz.elemento) < 0)
        raiz.izquierdo = insert_Recursive(raiz.izquierdo, elemento); 
        else if (elemento.compareTo(raiz.elemento) > 0){
            raiz.derecho = insert_Recursive(raiz.derecho, elemento); 
        }
        return raiz; 
    } 

    /**
     * Función que elimina un elemento del árbol. Si el nodo tiene un hijo,
     * se sube el hijo al lugar del padre. si el nodo tiene más de un hijo, deberás 
     * encontrar al sucesor inOrder y reemplazarlo en el árbol. 
     * El cual sera el mínimo de el subárbol derecho del nodo a eliminar.
     */
    public boolean delete(T elemento){
        if (elemento == null){
            return false;
        }else{
            raiz = deleteRecursive(raiz, elemento);
            return true;
        }
    }
 
    /**
     * Función recursiva para eliminar un elemento
     * @param raiz
     * @param elemento
     * @return
     */
    private Vertice deleteRecursive(Vertice raiz, T elemento)
    {
        if (raiz == null){
            return raiz;
        }
        if (elemento.compareTo(raiz.elemento) < 0){
            raiz.izquierdo = deleteRecursive(raiz.izquierdo, elemento);
        }else if (elemento.compareTo(raiz.elemento) > 0){
            raiz.derecho = deleteRecursive(raiz.derecho, elemento);
        }else{
            if (raiz.izquierdo == null){
                return raiz.derecho;
            }   
            else if (raiz.derecho == null){
                return raiz.izquierdo;
            }
            raiz.elemento = valorMinimo(raiz.derecho);
            raiz.derecho = deleteRecursive(raiz.derecho, raiz.elemento);
        }
 
        return raiz;
    }
    
    /**
     * Metodo para encontrar el elemento minimo en el arbol
     * @param raiz
     * @return elemento minimo 
     */
    private T valorMinimo(Vertice raiz)
    {
        T minv = raiz.elemento;
        while (raiz.izquierdo != null)
        {
            minv = raiz.izquierdo.elemento;
            raiz = raiz.izquierdo;
        }
        return minv;
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
        root = buildSorted(elems);
    }

    /**
     * Regresa una representación en cadena del árbol.
     *
     * @return una representación en cadena del árbol.
     */
    @Override
    public String toString() {
        String string = "";
        for (T i : this) {
            string += i + " ";
        }
        return string + "\n";
    }

    /**
     * Método auxiliar para dibujar arboles.
     * @return el numero de espacios necesarios en la String
     */
    private String dibujaEspacios(int l, int[] a, int n) {
        String s = "";
        for (int i = 0; i < l; i++) {
            if (a[i] == 1) {
                s = s + "│  ";
            } else {
                s = s + "   ";
            }
        }
        return s;
    }

    /**
     * Metodo auxiliar de toString para dibujar arboles
     * @return una representacion en String de nuestro arbol.
     */
    private String toStringArboles(Vertice v, int l, int[] m) {
        String s = v.toString() + "\n";
        m[l] = 1;

        if (v.izquierdo != null && v.derecho != null) {
            s = s + dibujaEspacios(l, m, m.length);
            s = s + "├─›";
            s = s + toStringArboles(v.izquierdo, l + 1, m);
            s = s + dibujaEspacios(l, m, m.length);
            s = s + "└─»";
            m[l] = 0;
            s = s + toStringArboles(v.derecho, l + 1, m);
        } else if (v.izquierdo != null) {
            s = s + dibujaEspacios(l, m, m.length);
            s = s + "└─›";
            m[l] = 0;
            s = s + toStringArboles(v.izquierdo, l + 1, m);
        } else if (v.derecho != null) {
            s = s + dibujaEspacios(l, m, m.length);
            s = s + "└─»";
            m[l] = 0;
            s = s + toStringArboles(v.derecho, l + 1, m);
        }
        return s;
    }

    /**
     * Regresa una representación en cadena del arbol.
     * @return una representación en cadena del arbol.
     */
    public String toStringArboles() {
        if (this.raiz == null) {
            return "";
        }
        int[] a = new int[this.altura() + 1];
        for (int i = 0; i < a.length; i++) {
            a[i] = 0;
        }
        return toStringArboles(this.raiz, 0, a);
    }

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
