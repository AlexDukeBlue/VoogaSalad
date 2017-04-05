package chat_client;

import util.net.ObservableClient;
import util.net.ObservableHostBase;

import java.time.Duration;
import java.util.Scanner;

/**
 * @author Created by th174 on 4/1/2017.
 */
public class ChatClientMain {
    //    public static final String HOSTNAME = "25.4.129.184";
    public static final String HOSTNAME = ObservableHostBase.LOCALHOST;
    public static final int PORT = 10023;
    public static final String CLEARSCREEN = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

    public static void main(String[] args) throws Exception {
        ObservableClient<ChatLog> voogaClient = new ObservableClient<>(
                HOSTNAME,
                PORT,
                ChatLog.CHAT_LOG_TEST_SERIALIZER,
                ChatLog.CHAT_LOG_TEST_UNSERIALIZER,
                Duration.ofSeconds(10));
        System.out.println("Client started successfully...");
        Scanner stdin = new Scanner(System.in);
        voogaClient.addListener(client -> System.out.print(CLEARSCREEN + client.getLast() + "\n\n>>  "));
        new Thread(voogaClient).start();
        while (voogaClient.isActive()) {
            String input = stdin.nextLine();
            String user = System.getProperty("user.name");
            voogaClient.send(state -> state.appendMessage(input, user));
        }
    }
}
