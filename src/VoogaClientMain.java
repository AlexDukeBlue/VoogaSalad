import util.net.Client;

import java.util.Scanner;

/**
 * @author Created by th174 on 4/1/2017.
 */
public class VoogaClientMain {
    public static final String HOSTNAME = "25.4.129.184";
//  public static final String HOSTNAME = Client.LOCALHOST;
    public static final int PORT = 10023;
    public static final String CLEARSCREEN = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

    public static void main(String[] args) throws Exception {
        Client<SimpleChatLogTest> voogaClient = new Client<>(
                HOSTNAME,
                PORT,
                SimpleChatLogTest.CHAT_LOG_TEST_SERIALIZER,
                SimpleChatLogTest.CHAT_LOG_TEST_UNSERIALIZER);
        Scanner stdin = new Scanner(System.in);
        voogaClient.addListener(state -> System.out.print(CLEARSCREEN + state.getLast() + "\n\n>>  "));
        voogaClient.start();
        while (voogaClient.isActive()) {
            String input = stdin.nextLine();
            String user = System.getProperty("user.name");
            voogaClient.send(state -> state.appendMessage(input, user));
        }
    }
}
