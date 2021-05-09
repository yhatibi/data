import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * Created by jordi on 26/02/17.
 * Exemple Servidor UDP extret dels apunts IOC i ampliat
 * El seu CLient és DatagramSocketClient
 */
public class DatagramSocketServer {
    DatagramSocket socket;
    NombreSecret nombreSecret;
    //Instàciar el socket
    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
        nombreSecret = new NombreSecret();
        nombreSecret.pensa(10);
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[4];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

        while(true) {
            DatagramPacket packet = new DatagramPacket(receivingData,4);
            socket.receive(packet);
            sendingData = processData(packet.getData(),packet.getLength());
            //Llegim el port i l'adreça del client per on se li ha d'enviar la resposta
            clientIP = packet.getAddress();
            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData,sendingData.length,clientIP,clientPort);
            socket.send(packet);
        }
    }

    //El server retorna al client el mateix missatge que li arriba però en majúscules
    private byte[] processData(byte[] data, int lenght) {
        int nombre = ByteBuffer.wrap(data).getInt(); //data és l'array de bytes
        System.out.println(nombre);
        int comprva = nombreSecret.comprova(nombre);
        System.out.println(comprva);
        byte[] resposta = ByteBuffer.allocate(4).putInt(comprva).array(); //num és un int
        return resposta;
    }

    public static void main(String[] args) {


        DatagramSocketServer server = new DatagramSocketServer();
        try {
            server.init(5555);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}