package edd.simulador;

import edd.estructuras.*;
import edd.modelo.*;
import edd.colors.*;
import java.util.Scanner;


/**
 * Clase para simular el juego de encerrado
 */
public class Juego {
     
    Scanner scn = new Scanner(System.in);

    private Tablero tablero; // Tablero del juego

    private int fichaJugador; // Si es 1 el jugador tiene fichas rojas, si no tiene azules 

    private int fichaMaquina; // El tipo de ficha con la que juega la máquina 

    private int modo;         // Si es 1 es al azar, si no es modo minimax

    private boolean juegoTerminado; // Indica si el juego a terminado 

    public Juego(){

        this.tablero = setTablero();

        this.fichaJugador = setJugador();

        this.modo = setModoDeJuego();

        fichaMaquina = (fichaJugador == 1) ? 2:1; // selecciona el color ocne l juega la maquina 

        while(!juegoTerminado){
            
            //El usuario empieza por que escogio azules o empieza la maquina 

            if(fichaJugador == 2){
                juegaUsuario();
                if(juegoTerminado) break;
                juegaMaquina();
            }else{
                juegaMaquina();
                if(juegoTerminado) break;
                juegaUsuario();
            }
        }
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

             Colors.println("Así se ve tu tablero", Colors.HIGH_INTENSITY);
             System.out.println(tablero.toString());
        }else{
            boolean ocupado[] = new boolean[5]; 

            System.out.println("Tienes el siguiente tablero\n");
            System.out.println("0 ----- 1");
            System.out.println("| \\  / |");
            System.out.println("|  2   |");
            System.out.println("| /  \\ |");
            System.out.println("3       4");


            String ficha = "Escoge la posicion para la primera ficha" + Colors.RED + " Roja" + Colors.RESTORE;
            String error2 = "Escoge una posicion válida";

            int roja1 = setFicha(ficha, error2,ocupado);
            ocupado[roja1] = true;

            ficha = "Escoge la posicion para la segunda ficha" + Colors.RED + " Roja" + Colors.RESTORE;

            int roja2 = setFicha(ficha, error2,ocupado); 
            ocupado[roja2] = true;

            ficha = "Escoge la posicion para la primera ficha " + Colors.BLUE + " Azul" + Colors.RESTORE;

            int azul1 = setFicha(ficha, error2,ocupado); 
            ocupado[azul1] = true;

            ficha = "Escoge la posicion para la segunda ficha" + Colors.BLUE +  " Azul" + Colors.RESTORE;

            int azul2 = setFicha(ficha, error2,ocupado); 
            ocupado[azul2] = true;
            
            tablero = new Tablero(roja1,roja2,azul1,azul2);

            Colors.println("Así se ve tu tablero\n", Colors.HIGH_INTENSITY);
            System.out.println(tablero.toString());
        }

        return tablero;
    }

    /**
     * Método para elegir el color de la ficha con el jugará el usuario
     * @return 
     */
    public int setJugador(){
        
        Colors.println("Escoge el color de tu ficha\n", Colors.HIGH_INTENSITY);
        Colors.println("Recuerda que empiezan las azules\n", Colors.ITALICS);
        Colors.println("1. ROJAS", Colors.HIGH_INTENSITY + Colors.RED);
        Colors.println("2. AZULES", Colors.HIGH_INTENSITY + Colors.BLUE);

        String mensaje = "Escribe la opcion que quieres (1 o 2)";
        String error = "Escoge una opción válida";

        int opcion = getInt(mensaje, error, 1, 2);

        return opcion;
    }
    
    /**
     * Método para escoger el modo de juego, encualquier momento se puede cambiar 
     * presionando -1 
     * @return
     */
    public int  setModoDeJuego(){
        Colors.println("Escoge el modo de juego\n", Colors.HIGH_INTENSITY);
        Colors.println("1. RANDOM", Colors.HIGH_INTENSITY + Colors.MAGENTA);
        Colors.println("2. MINIMAX", Colors.HIGH_INTENSITY + Colors.YELLOW);

        String mensaje = "Escribe la opcion que quieres (1 o 2)";
        String error = "Escoge una opción válida";

        int opcion  = getInt(mensaje, error, 1, 2);

        return opcion;
    }

    /**
     * Método para que el jugador pueda escoger que ficha mover en su turno 
     */
    public void juegaUsuario(){

        //Checa si se acabo el juego 
        if(seAcabo()){
            return;
        }

        String ficha;
        
        ficha = (fichaJugador == 1)  ? Colors.RED + "rojo" + Colors.RESTORE :
                                       Colors.BLUE + "azules" + Colors.RESTORE;

        Colors.println("Turno del usuario", Colors.HIGH_INTENSITY);
        System.out.println("Juegan las fichas " + ficha);
        System.out.println(tablero.toString());

        Colors.println("Escoge la poscion ficha que quieres mover", Colors.HIGH_INTENSITY);

        String mensaje = "Recuerda que las posciones van de 0 a 4";
        String error = "Esa no es una posicion valida para que tu muevas";

        int fichaMover = escogeFicha(mensaje, error);
        
        Colors.println("Bien escogiste mover la ficha " + fichaMover, Colors.HIGH_INTENSITY);
        
        System.out.println("Así ha quedado el tablero");
        System.out.println(tablero.toString());

        if(seAcabo()){
            return;
        }
        //Pregunta si se quiere cambiar el modo de juego 

        Colors.println("Para cambiar el modo de juego presiona 1 sino 0", Colors.CYAN);
        if(getInt("Escribe 0 o 1", error, 0, 1) == 1){
            modo = setModoDeJuego();
        }
    }

    /**
     * Checa si el juego ha terminado
     */
    public boolean seAcabo(){   
        if(tablero.esJuegoTerminado() == fichaJugador){
            Colors.println("Perdiste, lo siento", Colors.HIGH_INTENSITY + Colors.RED);
            juegoTerminado =true;
            return true;
        }else if(tablero.esJuegoTerminado() == fichaMaquina){
            Colors.println("Ganaste, FELICIDADES", Colors.HIGH_INTENSITY + Colors.GREEN);
            juegoTerminado =true;
            return true;
        }

        return false;
    }
    /**
     * Método para que juegue la máquina dependiendo del modo en el que se juega 
     */
    public void juegaMaquina(){

        //Checa si se acabo el juego 
        if(seAcabo()){
            return;
        }

        String ficha;
        
        ficha = (fichaMaquina == 1)  ? Colors.RED + "rojo" + Colors.RESTORE :
                                       Colors.BLUE + "azules" + Colors.RESTORE;

        Colors.println("Turno de la máquina", Colors.HIGH_INTENSITY);
        System.out.println("Juegan las fichas " + ficha);
        System.out.println(tablero.toString());

        if(modo == 1 ){
            tablero.mueveFichaAleatorio(fichaMaquina);
        }else{
            juegoMinimax();
        }

        System.out.println("Así ha quedado el tablero");
        System.out.println(tablero.toString());
    }

    //FALTA
    public void juegoMinimax(){

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
    * Método que verifica que la opcion para escoger un ficha en el tablero sea válida
    * @return int <code> posicion valida</code>
    * @param mensaje - POsibles opciones a elegir 
    * @param error -Mensaje de que la opcion es invalida
    * @param ocupado - Arreglo para ver si la posicon esta ocupada o no
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
    
    /**
    * Método que devuelve la ficha a mover solo si es un movimiento válido 
    * @return int <code>ficha</code>
    * @param mensaje - Escoger la ficha 
    * @param error -Mensaje de que la opcion es invalida
    */
    public int escogeFicha(String mensaje, String error) {
        int val;
        while (true) {
            System.out.println(mensaje);
            if (scn.hasNextInt()) {
                val = scn.nextInt();
                // (-infinito, min) || (max, infinito)
                if ((val < 0 || 4 < val)) {
                    System.out.println(error);
                }else {
                    if(tablero.mueveFicha(val, fichaJugador)){
                        return val;
                    }else{
                        System.out.println(error);
                    }
                }
            } else {
                scn.next();
                System.out.println(error);
            }
        }
    }
}
