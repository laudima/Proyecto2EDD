
package edd;

import java.util.Scanner;
import edd.colors.Colors;
import edd.modelo.*;
import edd.simulador.*;

/**
 * Clase principal, incluye a la interfaz 
 * @author Laura Itzel Rodríguez Dimayuga
 * @author Anshar Dominguez
 * @version Apr 2022
 */
public class Prueba {
    /**
    * Método que devuelve la opción del menu solo si es valida
    * @return int <code>opción</code>
    * @param mensaje -Texto del menu
    * @param error -Mensaje de que la opcion es invalida
    * @param min -La menor opcion valida
    * @param max -La opcion mas grande que podemos poner
    */
    public static int getInt(String mensaje, String error, int min, int max) {
        int val;
        Scanner scn = new Scanner(System.in);

        while (true) {
            System.out.println(mensaje);
            if (scn.hasNextInt()) {
                val = scn.nextInt();
                // (-infinito, min) || (max, infinito)
                if ((val < min || max < val) && val!=-1) {
                    System.out.println(error);
                }else if(val == -1){
                    System.out.println("Ahhhh así que has decidido ser un aguafiestas, adíos...");
                    System.exit(0);
                }
                else {
                    return val;
                }
            } else {
                scn.next();
                System.out.println(error);
            }
        }
    }

    public static void main(String args[]) {

    }
}
