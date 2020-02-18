package brugerautorisation.transport.rmi;

import brugerautorisation.data.Bruger;

public interface Brugeradmin extends java.rmi.Remote {

    Bruger hentBruger(String brugernavn, String adgangskode) throws java.rmi.RemoteException;

}
