import brugerautorisation.data.Bruger;
import server.IGalgeServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import server.IGalgeServer.SpilStatus;

public class GalgeKlient {

    private static IGalgeServer server;
    private static Scanner scan = new Scanner(System.in);
    private static Bruger bruger = null;

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        System.out.printf("Forbinder til Galgelegserver (%s)\n", IGalgeServer.URL);
        server = (IGalgeServer) Naming.lookup(IGalgeServer.URL);
        System.out.println("Forbundet til server!");

        login();

        startSpil();

        System.out.println("\nAfslutter program");
    }

    static void login(){
        System.out.println("\nLogin for at spille");

        while(true) {
            System.out.print("Brugernavn: ");
            String brugernavn = scan.nextLine();

            System.out.print("Adgangskode: ");
            String adgangskode = scan.nextLine();

            try {
                bruger = server.login(brugernavn, adgangskode);
                if (bruger == null) {
                    System.out.println("\nForket brugernavn og/eller adgangskode. Prøv igen.");
                } else {
                    System.out.println("\nDu er nu logget ind som: " + bruger.fornavn);
                    break;
                }
            } catch (RemoteException e) {
                System.out.println("\nDer skete en fejl hos serveren. Prøv igen.");
            }
        }
    }

    static void startSpil() throws RemoteException {
        System.out.println("\nStarter et nyt server");
        /*server = server.nytSpil(bruger);*/


        String besked = "";
        while(true){
            printSpilStatus();

            if( !besked.equals("") )
                System.out.println("\n" + besked);
            besked = "";

            char gaet = indtastGaet();
            switch(server.gaetBogstav(bruger, gaet)){
                case KORREKT:
                    besked = gaet + " er korrekt!";
                    break;
                case FORKERT:
                    besked = gaet + " er forket!";
                    break;
                case ALLEREDEGAETTET:
                    besked = "Du har allerede gaettet på " + gaet;
                    break;
                case IKKETILLADT:
                    besked = gaet + " er ikke et tilladt gaet";
                    break;
            }

            SpilStatus spilStatus = server.getSpilStatus(bruger);
            if( spilStatus == SpilStatus.VUNDET ){
                System.out.println(besked);
                System.out.println("Du har gaettet det rigtige ord og har vundet!");
                break;
            }
            if( spilStatus == SpilStatus.TABT ){
                System.out.println(besked);
                System.out.println("Du har bruge alle dine forsøg, og har tabt!");
                break;
            }
        }

        System.out.println("\nTak for spillet!");
    }


    private static void printSpilStatus() throws RemoteException {
        System.out.println("\nOrdet: " + server.getSynligtOrd(bruger));
        System.out.println("Forsøg tilbage: " + server.getAntalForsoeg(bruger));

        // Print forkerte bogstaver
        List<String> gaettedeBogstaver = server.getBrugteBogstaver(bruger);
        if( gaettedeBogstaver.size() > 0 ){
            String bogstaver = "";
            for( String bogstav : gaettedeBogstaver ){
                if( !bogstaver.equals("") )
                    bogstaver += ", ";
                bogstaver += bogstav;
            }
            System.out.println("Brugte bogstaver: " + bogstaver);
        }
    }

    private static char indtastGaet(){
        System.out.print("\nIndtast gæt: ");
        while(true){
            String input = scan.nextLine();
            if( input.length() > 0  ) {
                return input.charAt(0);
            }
        }
    }



}
