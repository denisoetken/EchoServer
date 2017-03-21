import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by Denis on 21.03.2017.
 */
public class EchoServer {
    // Der geforderte Logger für alles
    private static final Logger LOG = Logger.getLogger("EchoServer");

    public static void main(String[] args) {
        String Server = "localhost";
        int port = 7;
//        Um die Gesamtzahl der übertragenen Bytes zu zählen
        int counter = 0;
//        Aufbau des "servers"
        try (
                ServerSocket server = new ServerSocket(port)) {
            while (true) {
//                Der Server baut mit dem Client einen Socket (Kombination aus In- und OutPutStream je auf Client und Server) auf)
//                Dies wird protokolliert
                Socket zumClient = server.accept();
                LOG.info(String.format("Der Server hat einen Stream zu %s erstellt.", zumClient.getInetAddress()));

//              Der Outputstream soll den gerade empfangenen Input vom Client gleich wieder schreiben (als Bytes)
                OutputStream os = zumClient.getOutputStream();
//                Abbruch bei -1, weil Bytestreams mit -1 enden
                while (zumClient.getInputStream().read() != -1) {
                    os.write(zumClient.getInputStream().read());
//                    Jedes Byte wird geloggt und der Counter hochgezählt
                    LOG.info("1 Byte übertragen");
                    counter++;
                }
//                Nach beenden der Verbindung (Schließen des Clients/ von PuTTY) wird noch einmal die Gesamtzahl
//                der übertragenen Bytes und die entsprechende Clientadresse geloggt
                LOG.info(String.format("Insgesamt wurden %s Bytes mit %s übertragen.", counter, zumClient.getInetAddress()));
//              Der Outputstream muss geschlossen werden (der Inputstream des Servers wird automatisch mit geschlossen) --> Log
                os.close();
                LOG.info("Der Stream des Servers wurde geschlossen .");
//              Der Socket muss geschlossen werden  --> Log
//                Damit ist der Server wieder verfügbar
                zumClient.close();
                LOG.info("Der Socket des Servers wurde geschlossen .");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}