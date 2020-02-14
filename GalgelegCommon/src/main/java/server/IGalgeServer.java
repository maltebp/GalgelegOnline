package server;

import brugerautorisation.data.Bruger;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGalgeServer extends Remote {

    // Server settings
    int     PORT = 13337;
    String  PATH = "galgeleg";
    String  DOMAIN = "dist.saluton.dk";
    String  URL  = String.format("rmi://%s:%d/%s", DOMAIN, PORT, PATH);

    Bruger login(String brugernavn, String kode)     throws RemoteException, ServerException;


    GaetResultat gaetBogstav(Bruger spiller, char bogstav) throws RemoteException;
    String getSynligtOrd(Bruger spiller) throws RemoteException;
    SpilStatus getSpilStatus(Bruger spiller) throws RemoteException;
    int getAntalForsoeg(Bruger spiller) throws RemoteException;
    List<String> getBrugteBogstaver(Bruger spiller) throws RemoteException;

    enum SpilStatus{
        IGANG,
        VUNDET,
        TABT
    }

    enum GaetResultat{
        KORREKT,
        FORKERT,
        ALLEREDEGAETTET,
        IKKETILLADT
    }
}
