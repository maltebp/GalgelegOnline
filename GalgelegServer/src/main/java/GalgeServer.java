import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class GalgeServer extends UnicastRemoteObject implements GalgeServerI {

    private String spillerNavn = null;

    private GalgeServer() throws RemoteException {}

    // TODO: Create proper try-catch for exception
    public static void main(String[] args) throws RemoteException, MalformedURLException {

        System.out.printf("Starter server som: %s\n", URL);

        LocateRegistry.createRegistry(PORT);
        GalgeServerI server = new GalgeServer();
        Naming.rebind(URL, server );

        System.out.println("Server startet");

        // Server Console
        Scanner scan = new Scanner(System.in);
        String input;
        while(true){
            System.out.print(">");
            input = scan.nextLine();
            if( input.equals("stop") || input.equals("exit") ){
                break;
            }
            if( input.equals("print") ){
                System.out.println("Printer server:\n" + server);
            }
        }
        System.out.println("Stopping server");
    }

    public void setNavn(String name) throws RemoteException {
        spillerNavn = name;
    }

    public String getNavn() throws RemoteException {
        return spillerNavn;
    }

    public void startSpil() throws RemoteException {
    }

    @Override
    public String toString(){
        return String.format("GalgeServer{ spillerNavn: %s }",
                spillerNavn != null ? spillerNavn : "NULL"
        );
    }


}
