package edd.simulador;

import edd.estructuras.*;
import edd.modelo.*;
import edd.colors.*;
import java.util.Random;
import java.util.Scanner;



public class Juego {
     
    Scanner scn = new Scanner(System.in);

    Random random = new Random();

    Tablero tablero;
    int fichaJugador;

    public Juego(){

        this.tablero = setTablero();
        this.fichaJugador = setJugador();
    }

    /**
     * Metodo para inicializar el tablero, puede iniciarse de forma 
     * predeterminada o la puede escoger el jugador.
     * @return tablero - el tablero para iniciar la partida
     */
    public Tablero setTablero(){

        Tablero tablero;

        StringBuilder menu = new StringBuilder();

        menu.append(Colors.HIGH_INTENSITY);
        menu.append("Bienvenido al juego encerrado\n\n");
        menu.append("Escoge tu tablero\n");
        menu.append("1. Juega con tablero inicial\n");
        menu.append("2. Juega con tablero personalizado\n");
        menu.append(Colors.RESTORE);

        System.out.println(menu.toString());

        /*Mensaje del menú*/
        String menu1 = Colors.HIGH_INTENSITY + "Ingresa el numero de la opcion que quieres escoger:" + Colors.RESTORE;
        String error1 = "Por favor ingresa una opcion valida.";

        int opcion = getInt(menu1,error1,1,2);

        if(opcion == 1){
             tablero = new Tablero();
        }else{
            boolean ocupado[] = new boolean[5]; 

            System.out.println("Tienes el siguiente tablero");
            System.out.println("0 ----- 1");
            System.out.println("| \\  / |");
            System.out.println("|  2   |");
            System.out.println("| /  \\ |");
            System.out.println("3 ----- 4");


            String ficha = "Escoge la posicion para la primera ficha Roja";
            String error2 = "Escoge una posicion válida";

            int roja1 = setFicha(ficha, error2,ocupado);
            ocupado[roja1] = true;

            ficha = "Escoge la posicion para la segunda ficha Roja";

            int roja2 = setFicha(ficha, error2,ocupado); 
            ocupado[roja2] = true;

            ficha = "Escoge la posicion para la primera ficha Azul";

            int azul1 = setFicha(ficha, error2,ocupado); 
            ocupado[azul1] = true;

            ficha = "Escoge la posicion para la segunda ficha Azul";

            int azul2 = setFicha(ficha, error2,ocupado); 
            ocupado[azul2] = true;
            
            tablero = new Tablero(roja1,roja2,azul1,azul2);
        }

        return tablero;
    }

    /**
     * Método para elegir el color de la ficha con el jugará el usuario
     * @return 
     */
    public int setJugador(){
        
        Colors.println("Bienvenido al juego encerrado", Colors.HIGH_INTENSITY);
        Colors.println("Escoge el color de tu ficha", Colors.HIGH_INTENSITY);
        Colors.println("1. ROJAS", Colors.HIGH_INTENSITY);
        Colors.println("2. AZULES", Colors.HIGH_INTENSITY);

        String mensaje = "Escribe la opcion que quieres (1 o 2)";
        String error = "Escoge una opción válida";

        int opcion = getInt(mensaje, error, 1, 2);

        return opcion;
    }
    /**
    * Método que devuelve la opción del menu solo si es valida
    * @return int <code>opción</code>
    * @param mensaje -Texto del menu
    * @param error -Mensaje de que la opcion es invalida
    * @param min -La menor opcion valida
    * @param max -La opcion mas grande que podemos poner
    */
    public int getInt(String mensaje, String error, int min, int max) {
        int val;

        while (true) {
            System.out.println(mensaje);
            if (scn.hasNextInt()) {
                val = scn.nextInt();
                // (-infinito, min) || (max, infinito)
                if ((val < min || max < val)) {
                    System.out.println(error);
                }else {
                    return val;
                }
            } else {
                scn.next();
                System.out.println(error);
            }
        }
    }

    /**
    * Método que devuelve la opción del menu solo si es valida
    * @return int <code>opción</code>
    * @param mensaje -Texto del menu
    * @param error -Mensaje de que la opcion es invalida
    * @param min -La menor opcion valida
    * @param max -La opcion mas grande que podemos poner
    */
    public int setFicha(String mensaje, String error, boolean[] ocupado) {
        int val;
        while (true) {
            System.out.println(mensaje);
            if (scn.hasNextInt()) {
                val = scn.nextInt();
                // (-infinito, min) || (max, infinito)
                if ((val < 0 || 4 < val) || ocupado[val]) {
                    System.out.println(error);
                }else {
                    return val;
                }
            } else {
                scn.next();
                System.out.println(error);
            }
        }
    }
    
}
