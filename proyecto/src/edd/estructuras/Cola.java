package edd.estructuras;


public class Cola<T> extends PushPop<T>{

    // Agregar al inicio.Se agrega un elemento al final de la cola
    public void push(T elemento){
        if(elemento == null){
            throw new IllegalArgumentException("");
        }
        Nodo aux = new Nodo(elemento);
        if(isEmpty()){
            this.cabeza=ultimo=aux;
            longi++;
            return ;
        }
        ultimo.siguiente = aux;
        ultimo = aux;
        longi ++;
    }

    /**
     * Regresa un clon de la estructura.
     * Como modificamos el push, el clon es el mismo que el de pila
     * @return un clon de la estructura.
     */
     public Cola<T> clone(){

            Cola<T> nueva = new Cola<T>();
            Cola<T> aux = new Cola<T>();
            if (this.isEmpty()) {
                return nueva;
            }
            Nodo aux2 = this.cabeza;
            while(aux2 != null){
                aux.push(aux2.elemento);
                aux2 = aux2.siguiente;
            }
            while (!aux.isEmpty()) {
                nueva.push(aux.pop());
            }
            return nueva;
    }

    /*Como los elementos se van agregando al final el to String es el mismo*/
    public String toString(){
        if (this.isEmpty()) {
            return "";
        }
        String regreso = this.cabeza.elemento.toString();
        Nodo n = this.cabeza;
        while (n.siguiente != null) {
            regreso += ", " + n.siguiente.elemento.toString();
            n = n.siguiente;
        }
        return regreso;
    }
}