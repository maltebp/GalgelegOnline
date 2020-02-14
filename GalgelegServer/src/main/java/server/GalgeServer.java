package server;

import spil.ISpil;
import brugerautorisation.transport.rmi.Brugeradmin;
import brugerautorisation.data.Bruger;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class GalgeServer extends UnicastRemoteObject implements IGalgeServer {

    // Map over Galgelogik (spillet) for spillernavne
    private HashMap<String, Galgelogik> logikMap = new HashMap<String, Galgelogik>();

    private GalgeServer() throws RemoteException {}

    // TODO: Create proper try-catch for exception
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {

        System.out.printf("\nStarter server som: %s\n", URL);

        System.setProperty("java.rmi.server.hostname", IGalgeServer.DOMAIN);
        LocateRegistry.createRegistry(PORT);
        IGalgeServer server = new GalgeServer();
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
            if( input.equals("spil") ){
                HashMap<String, Galgelogik> logikMap = ((GalgeServer) server).logikMap;
                if( logikMap.size() == 0 )
                    System.out.println("Ingen spil i gang");
                else {
                    System.out.printf("Spil (%d):\n", logikMap.size());
                    for ( String spillerNavn : logikMap.keySet() ) {
                        System.out.printf("%s\n", spillerNavn, logikMap.get(spillerNavn));
                        /*System.out.printf("%s:  %s", spiller.fornavn, spilLogik.get(spiller));*/
                    }
                }
            }
        }
        System.out.println("Stopping server");
    }


    @Override
    public Bruger login(String brugernavn, String kode) throws RemoteException, ServerException {
        Brugeradmin ba;
        try {
            ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
            logikMap.remove(brugernavn);
            logikMap.put(brugernavn, new Galgelogik());
            return ba.hentBruger(brugernavn, kode);// ba.hentBruger(brugernavn, kode);
        } catch (RemoteException e) {
            System.out.println("Fejl ved login (RemoteException): " + e.getMessage());
            throw new RemoteException("Kunne ikke oprette forbindelse til autorisationsserver");
        } catch (NotBoundException e) {
            System.out.println("Fejl ved login (NotBoundException): " + e.getMessage());
            throw new RemoteException("Kunne ikke oprette forbindelse til autorisationsserver");
        } catch (MalformedURLException e) {
            System.out.println("Fejl ved login (MalformedURLException): " + e.getMessage());
            throw new RemoteException("Kunne ikke oprette forbindelse til autorisationsserver");
        } catch (Exception e){
            return null;
        }
    }

    /*@Override
    public void nytSpil(Bruger spiller) {
        spilLogik.put(this, new Galgelogik());
    }
*//*

    @Override
    public void nytSpil(Bruger spiller) {
    }*/

    @Override
    public GaetResultat gaetBogstav(Bruger spiller, char bogstav) {
        Galgelogik galgelogik = logikMap.get(spiller.brugernavn);
        String bogstavStr = Character.toString(bogstav);
        if( galgelogik.getBrugteBogstaver().contains(bogstavStr))
            return GaetResultat.ALLEREDEGAETTET;
        if( !((bogstav >= 65 && bogstav <= 90) || (bogstav >= 97 && bogstav <= 122) || bogstav == 'æ' || bogstav == 'ø' || bogstav == 'å') )
            return GaetResultat.IKKETILLADT;
        galgelogik.gætBogstav(bogstavStr);
        return galgelogik.erSidsteBogstavKorrekt() ? GaetResultat.KORREKT : GaetResultat.FORKERT;
    }

    @Override
    public String getSynligtOrd(Bruger spiller) {
        Galgelogik galgelogik = logikMap.get(spiller.brugernavn);
        return galgelogik.getSynligtOrd();
    }

    @Override
    public SpilStatus getSpilStatus(Bruger spiller) {
        Galgelogik galgelogik = logikMap.get(spiller.brugernavn);
        if( galgelogik.erSpilletTabt())
            return SpilStatus.TABT;
        if( galgelogik.erSpilletVundet())
            return SpilStatus.VUNDET;
        return SpilStatus.IGANG;
    }

    @Override
    public int getAntalForsoeg(Bruger spiller) {
        Galgelogik galgelogik = logikMap.get(spiller.brugernavn);
        return 7 - galgelogik.getAntalForkerteBogstaver();
    }

    @Override
    public List<String> getBrugteBogstaver(Bruger spiller) {
        Galgelogik galgelogik = logikMap.get(spiller.brugernavn);
        return galgelogik.getBrugteBogstaver();
    }

}
