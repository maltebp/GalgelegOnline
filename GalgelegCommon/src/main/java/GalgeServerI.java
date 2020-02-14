import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GalgeServerI extends Remote {

    // Server settings
    int     PORT = 1099;
    String  PATH = "galgeleg";
    String  DOMAIN = "dist.saluton.dk";
    String  URL  = String.format("rmi://%s:%d/%s", DOMAIN, PORT, PATH);

    void setNavn(String name)      throws RemoteException;
    String  getNavn()      throws RemoteException;
    void startSpil()    throws RemoteException;
}
