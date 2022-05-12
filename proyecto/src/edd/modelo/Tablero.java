package edd.modelo;

import edd.colors.*;
import java.util.Random;
import edd.estructuras.*;

public class Tablero {

    private class Fichas {
       
        int color;// color 1 = roja y color 2 = azul 

        String colors; // "color para la ficha"
        int ficha;     // ficha 1 0 2
        int posicion;  // posicion en el tablero

        int casillaVecinas[];  // casilla a las que se podria mover una ficha 
        int nVecinos;          // cantidad de posiciones a las que se podria mover una ficha 
    

        public Fichas(int color,int ficha, int posicion){
            this.color = color;
            this.ficha = ficha; 
            this.posicion = posicion;

            setPosicion(posicion);
        }

        /**
         * Método para cambiar la posicion de una ficha. Tambien 
         * pone la cantidad de casillas vecinas a las uqe se puede mover una ficha 
         * @param posicion
         */
        public void setPosicion(int posicion){
            
            this.posicion = posicion;

            switch(posicion){
                case 0: nVecinos = 3; break;
                case 1: nVecinos = 3; break;
                case 2: nVecinos = 4; break;
                case 3: nVecinos = 2; break;
                case 4: nVecinos = 2; break;
            }
    
            casillaVecinas = new int[nVecinos];

            switch(posicion){
                case 0: 
                    casillaVecinas[0] = 1;
                    casillaVecinas[1] = 2;
                    casillaVecinas[2] = 3;
                    break;
                case 1:
                    casillaVecinas[0] = 0;
                    casillaVecinas[1] = 2;
                    casillaVecinas[2] = 4;
                    break;  
                case 2:
                    casillaVecinas[0] = 0;
                    casillaVecinas[1] = 1;
                    casillaVecinas[2] = 3;
                    casillaVecinas[3] = 4;
                    break;
                case 3:
                    casillaVecinas[0] = 0;
                    casillaVecinas[1] = 2;
                    break;  

                case 4:
                    casillaVecinas[0] = 1;
                    casillaVecinas[1] = 2;
                    break;  
            }
        }
        
        public int getPosicion(){
            return posicion;
        }

        public int getColor(){
            return color;
        }

        /**
         * Verifica que la casilla se una casilla valida a las que se puede mover la ficha
         * es decir que sea una casilla vecina a la que se encuentra 
         * @param casilla   casilla a verficar si es vecina o no 
         * @return true si es vecina / false si no lo es 
         */
        public boolean esVecino(int casilla){

            boolean esta=false;

            for(int i =0; i < nVecinos ; i++){
                if(casilla == casillaVecinas[i]){
                    esta =true;
                }
            }
            return esta;
        }

        @Override 
        public String toString(){
            if(color == 1){
                colors = Colors.RED;
            }else if(color == 2){
                colors = Colors.BLUE;
            }else{
                colors = Colors.WHITE;
            }

            return Colors.HIGH_INTENSITY + colors + ficha + Colors.RESTORE;
        }
    }


    /**
     * Arreglo de fichas en el tablero
     */
    private Fichas[] fichas;  

    /**
     * Posicion libre en el tablero
     */
    private int posicionLibre; 

    /**
     * Constructor del tablero poscicionando las fichas a nuestra pereferencia
     * @param roja1 posicion de la ficha roja 1 
     * @param roja2 posicion de la ficha roja 2 
     * @param azul1 posicion de la ficha azul 1
     * @param azul2 posicion de la ficha azul 2
     */
    public Tablero(int roja1, int roja2, int azul1, int azul2){

        posicionLibre = 10-roja1-roja2-azul1-azul2;
        
        fichas = new Fichas[5];

        fichas[roja1] = new Fichas(1,1,roja1);
        fichas[roja2] = new Fichas(1,2,roja2);
        fichas[azul1] = new Fichas(2,1,azul1);
        fichas[azul2] = new Fichas(2,2,azul2);
        fichas[posicionLibre] = new Fichas(0,0,posicionLibre);
        
    }

    /**
     * Constructor para el tablero predeterniminado
     */
    public Tablero (){

        fichas = new Fichas[5];

        fichas[0] = new Fichas(1,1,0);
        fichas[1] = new Fichas(2,1,1);
        fichas[2] = new Fichas(0,0,2);
        fichas[3] = new Fichas(2,2,3);
        fichas[4] = new Fichas(1,2,4);

        posicionLibre = 2; 
    }

    /**
     * Método para avisarnos si el juego termino. Si no termina regresa 0, si pierde rojas regresa 1
     * si pierde azules regresa 2.
     * @return  Regresa 0,1,2 si nadie pierde, pierde rojas o pierde azules respectivamente. 
     */
    public int esJuegoTerminado(){

        //Estas son las unicas posciones donde alguien puede perder 
        if(fichas[0].getColor() == 1 && fichas[3].getColor()== 1 && fichas[1].getColor() == 2 && fichas[2].getColor() == 2){
            return 1;
        }else if(fichas[0].getColor() == 2 && fichas[3].getColor()== 2 && fichas[1].getColor() == 1 && fichas[2].getColor() == 1){
            return 2;
        }else if(fichas[1].getColor() == 1 && fichas[4].getColor() == 1 && fichas[0].getColor() == 2 && fichas[2].getColor() == 2 ){
            return 1;
        }else if(fichas[1].getColor() == 2 && fichas[4].getColor() == 2 && fichas[0].getColor() == 1 && fichas[2].getColor() == 1 ){
            return 2;
        }

        return 0;
    }

    /**
     * Metodo para mover una ficha en el trablero 
     * @param poscionFicha posicion a donde se le quiere mover 
     * @param colorJugador color del jugador que va a mover la ficha 
     * @return true si se puede mover / false si no se puede mover 
     */
    public boolean mueveFicha(int poscionFicha, int colorJugador){
        
        Fichas ficha = fichas[poscionFicha];
        boolean sePudo =false;
        if( ficha.esVecino(posicionLibre) && ficha.getColor() == colorJugador){
            sePudo =true;
            int anterior = ficha.getPosicion();
            ficha.setPosicion(posicionLibre);
            
            fichas[anterior] = new Fichas(0, 0, anterior);
            fichas[posicionLibre] = ficha;
            posicionLibre = anterior;
        }
        return sePudo;
    }

    /**
     * Método para mover una ficha de manera aleatoria en el tablero. 
     * da un número aleatorio entre 0 y 4 e intenta moverlo si es un 
     * movimeitno válido. 
     * @param colorMaquina
     */
    public void mueveFichaAleatorio(int colorMaquina){
        
        Random random = new Random();

        int fichaA;

        boolean sePudo = false;

        while(!sePudo){
            fichaA = random.nextInt(5);
            if(mueveFicha(fichaA,colorMaquina)){
                sePudo =true;
            }
        }
    }

    /**
     * Regresa el color de alguna ficha en el tablero
     * @param posicion
     * @return
     */
    public int fichaColor(int posicion){
        return fichas[posicion].getColor();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(fichas[0] + " ----- " + fichas[1] + "\n");
        sb.append("| \\  / |" + "\n");
        sb.append("|  " + fichas[2] + "   |" + "\n");
        sb.append("| /  \\ |" + "\n");
        sb.append(fichas[3] + "       " + fichas[4] + "\n");

        sb.append(Colors.RESTORE);

        return sb.toString();
    }
}
