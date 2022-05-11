package edd.estructuras;

/**
 * Clase para árboles AVL
 */
public class ArbolAVL<T extends Comparable<T>> extends ArbolBinarioBusqueda<T> {
    
    /**
     * Clase para vértices se extiende de Arbol Binario. Se 
     * implementa el atributo de altura para cada vértice. 
     */
    protected class VerticeAVL extends Vertice {

        /** Altura  del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
                super(elemento);
                altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        public int getAltura() {
			return altura;
        }

        /**
         * Regresa la altura del vértice.
         * 
         * @return la altura del vértice.
         *  
         */
        @Override public int altura() {
            int alturaIzq;
            int alturaDer;
            
            if( izquierdo == null){
                alturaIzq = -1; 
            }else{
                alturaIzq = izquierdo.altura();
            }
        
            if (derecho == null){
                alturaDer =-1; 
            }else{
                alturaDer = derecho.altura();
            }
            return 1 + Math.max(alturaIzq, alturaDer);
        }
        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {

			return String.format("%s %d", elemento.toString(), altura()); 
        }

        /**
         * Compara el vértice con otro objeto de manera recursiva. 
         * 
         * @param objeto objeto que se compara con el vertice
         * @return <code>true</code>  si son de la misma clase, 
         *          tienen al mismo elemento y mismos subarboles recursivamente 
         *          y tienen la misma altura. 
         */
        @Override public boolean equals(Object objeto) {
            
            //Verifica que sea de la misma clase 
            if (objeto == null || getClass() != objeto.getClass()){
                return false;
            }

            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;

            /*Verifica que tengan la misma altura y que tengan el mismo elemento. 
             Los mismos subarboles, con el metodo que se hereda de Arbol Binario*/
            if(altura == vertice.altura && super.equals(objeto)){
                return true;
            }else{
                return false;
            }   
        }

    }

    /**
     * Constructor sin parámetros.
     */
    public ArbolAVL() { 
        super(); 
    }

    /**
     * Construye un arbol AVL a partir de una colección. El árbol
     * AVL tiene los mismos elementos que la colección recibida.
     * 
     * @param coleccion la colección a partir de la cual creamos el árbol AVL 
     */
    public ArbolAVL(Collection<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice AVL con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioBusqueda#add}, y después balancea el árbol girandolo 
     * para balnccear las alturas. 
     * @param elemento el elemento a agregar.
     */
    @Override public void add(T elemento) {
        super.add(elemento);
        VerticeAVL v = (VerticeAVL) ultimoAgregado;
        rebalance((VerticeAVL) v.padre);

    }

    @Override public boolean delete(T elemento){

        Vertice elimina = search(raiz, elemento);
        super.delete(elemento);
        rebalance((VerticeAVL)elimina.padre);

        return true;
    }
    
    /**
     * Verfica que los vertices esten balanceados, haciendo la resta 
     * entre la altura del vertice izquierdo y derecho 
     * @param VerticeAVL vertice a sacar el balance
     * @return balance (alutura hijo izquierdo altura hijo derecho) 
     */
    private int balance(VerticeAVL vertice){


        int alturaIzq = (vertice.izquierdo == null ? -1 : altura((VerticeAVL)vertice.izquierdo));
        int alturaDer = (vertice.derecho == null ? -1 : altura((VerticeAVL)vertice.derecho));

        return alturaIzq-alturaDer;
    }

    /**
     * Método para actualizar recursivamente las alturas desde un vétice
     * @param VerticeAVL vertice desde donde empieza a recalcular la altura 
     * @return altura 
     */
    private int altura(VerticeAVL vertice){
        if (vertice == null){
            return -1;
        }
        int alturaIzq = (vertice.izquierdo == null ? -1 : vertice.izquierdo.altura());
        int alturaDer = (vertice.derecho == null ? -1 : vertice.derecho.altura());
        return Math.max(alturaIzq,alturaDer) + 1;
    }

    /**
     * Funcion para rebalancear los vertices del arbol
     * @param vertice - vertice desde que se empieza a revalancear
     */
    private void rebalance(VerticeAVL vertice) {
		if (vertice == null) {
			return;
		}
        //Actualizamos la altura del vertice 
		altura(vertice);

        //Caso 1: Desvalance a la derecha 
		if (balance(vertice) == -2) {
			VerticeAVL q = (VerticeAVL)vertice.derecho;
            
            //Caso 1.2: Desvalance en zigzag 
			if (balance(q) == 1) {

                //Se gira el sobrino derecho 
				super.giraDerecha(q);
                //Se actualizan las alturas
				altura(q);
				altura((VerticeAVL)q.padre);
			}
            //Se rota al vertice a la izquierda 
			super.giraIzquierda(vertice);
			altura(vertice);
		}
        //Caso 2: Desvalance a la izquierda
		if (balance(vertice) == 2) {
			VerticeAVL p = (VerticeAVL)vertice.izquierdo;
            //Caso 2.2: Desvalnce en zigzag 
			if (balance(p) == -1) {
				super.giraIzquierda(p);
                //Se actualizan las alturas 
				altura(p);
				altura((VerticeAVL)p.padre);
			}
			super.giraDerecha(vertice);
			altura(vertice);
		}

        //Se revalancea hasta la raiz 
		rebalance((VerticeAVL)vertice.padre);
	}


}
