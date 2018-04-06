// Install the Java helper library from twilio.com/docs/java/install
import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;


public class Example {
    public static void main(String args[]) {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .load();
        String ACCOUNT_SID = dotenv.get("ACCOUNT_SID");
        String AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
        String PROXY_HOST = dotenv.get("PROXY_HOST");
        int PROXY_PORT = Integer.parseInt(dotenv.get("PROXY_PORT"));

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        ProxiedTwilioClientCreator clientCreator = new ProxiedTwilioClientCreator(
                ACCOUNT_SID, AUTH_TOKEN, PROXY_HOST, PROXY_PORT);
        TwilioRestClient twilioRestClient = clientCreator.getClient();
        Twilio.setRestClient(twilioRestClient);

        Message message = Message.creator(new PhoneNumber("+15558675310"),
                new PhoneNumber("+15017122661"), "Hey there!").create();

        System.out.println(message.getSid());
    }
}
