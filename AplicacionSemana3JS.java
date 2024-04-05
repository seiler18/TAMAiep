//Esta línea importa la clase Scanner del paquete java.util. Scanner es una clase que permite leer datos de entrada desde diferentes fuentes, como el teclado en este caso.
import java.util.Scanner;

//Esto define una clase llamada AplicacionSemana3JS. 
//La palabra clave public significa que esta clase es accesible desde cualquier otra clase. 
//Una clase en Java es un bloque de código que encapsula datos (en forma de variables) y comportamiento (en forma de métodos)
public class AplicacionSemana3JS {

	//Esta línea define el método main que es el punto de entrada principal para la ejecución del programa
	//Este método debe tener exactamente esta firma y se invoca cuando se ejecuta el programa
	//Los parámetros de este método, String[] args son una matriz de cadenas que contienen argumentos pasados al programa desde la línea de comandos
    public static void main(String[] args) {

	//Crea una instancia de la clase Scanner llamada scanner que está vinculada a la entrada estándar del sistema (System.in)
	//Esto permite leer datos ingresados por el usuario desde la consola.
        Scanner scanner = new Scanner(System.in);

	//Imprime un mensaje en la consola solicitando al usuario que ingrese su nombre, apellido y luego su edad
	System.out.println("Ingrese su nombre");
	String nombre = scanner.nextLine();

	System.out.println("Ingrese su apellido");
	String apellido = scanner.nextLine();

	System.out.println("Ingrese su edad");
	int edad = scanner.nextInt();
	scanner.nextLine(); // Consumir el carácter de nueva línea
        
	//Imprime un mensaje en la consola solicitando al usuario que ingrese la marca
        System.out.println("Ingrese la marca:");
	
	//Lee la entrada del usuario (en este caso, la marca) utilizando el objeto scanner y la almacena en la variable marca.
        String marca = scanner.nextLine();
        
        System.out.println("Ingrese el modelo:");
        String modelo = scanner.nextLine();
        
        System.out.println("Ingrese la cilindrada:");
        String cilindrada = scanner.nextLine();
        
        System.out.println("Ingrese el tipo de combustible:");
        String tipoCombustible = scanner.nextLine();
        
        System.out.println("Ingrese la capacidad en pasajeros:");
        int capacidadPasajeros = scanner.nextInt();
        
	//Las líneas restantes son similares y siguen el mismo patrón
	//imprimen un mensaje solicitando una entrada específica y luego leen esa entrada del usuario utilizando el objeto scanner
	//Finalmente, se imprimen los valores ingresados por el usuario : 
       	System.out.println("La marca que ha ingresado es: " + marca);
	System.out.println("El modelo que ha ingresado es: " + modelo);
	System.out.println("La cilindrada que ha ingresado es: " + cilindrada);
	System.out.println("El tipo de combustible es: " + tipoCombustible);
	System.out.println("Tiene una capacidad de " + capacidadPasajeros + " pasajeros.");
	//el \n sirve para dar un salto de linea
	System.out.println("Gracias por ocupar esta aplicación, " + nombre + " " + apellido + ".\n" + "Es algo sencilla pero servirá para seguir practicando. Saludos, Jesús Seiler");
        

	//cierra el objeto Scanner, liberando los recursos asociados con él después de su uso.
        scanner.close();
    }
}
