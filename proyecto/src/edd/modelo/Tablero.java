package edd.modelo;

import edd.colors.*;


public class Tablero {

    private int[] posiciones;

    private int posicionLibre;

    private String[] color;

    private int roja1;
    private int roja2;
    private int azul1;
    private int azul2;

    public Tablero(int roja1, int roja2, int azul1, int azul2){

        this.roja1 = roja1;
        this.roja2 = roja2;
        this.azul1 = azul1;
        this.azul2 = azul2;
        
        posiciones = new int[5];

        posiciones[roja1] =  1;
        posiciones[roja2] =  1;
        posiciones[azul1] =  2; 
        posiciones[azul2] =  2;

        posicionLibre = 10-roja1-roja2-azul1-azul2;

        posiciones[posicionLibre] = 0;
    }

    public Tablero (){

        posiciones = new int[5];

        posiciones[0] = 1;
        posiciones[1] = 2;
        posiciones[3] = 1; 
        posiciones[4] = 2;
        
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(Colors.HIGH_INTENSITY);
        sb.append(Colors.RESTORE);

        return sb.toString();
    }
}
