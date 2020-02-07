import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class GalgeKlient {

    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        System.out.println("Forbinder til server...");
        GalgeServerI server = (GalgeServerI) Naming.lookup(GalgeServerI.URL);
        System.out.println("Forbundet til server!");

        String input;
        while(true){
            System.out.println("\n>");
            input = scan.nextLine();
            if(!input.equals("")){
                server.setNavn(input);
                System.out.println("Sat navn til: "+ server.getNavn());
            }
        }
    }




}
