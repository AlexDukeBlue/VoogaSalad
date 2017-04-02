import util.net.VoogaServer;

import java.io.IOException;

/**
 * @author Created by th174 on 3/30/2017.
 */
public class VoogaServerMain {
    public static void main(String[] args) throws Exception {
        VoogaServer<NetworkingTest> voogaServer = new VoogaServer<>(new NetworkingTest(NetworkingTest.INITIAL_STATE), NetworkingTest::toString);
        voogaServer.listenForClients(10023);
    }
}
