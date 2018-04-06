import com.twilio.http.HttpClient;
import com.twilio.http.NetworkHttpClient;
import com.twilio.http.TwilioRestClient;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


public class ProxiedTwilioClientCreator {
    private String username;
    private String password;
    private String proxyHost;
    private int proxyPort;
    private HttpClient httpClient;

    public ProxiedTwilioClientCreator(String username, String password, String proxyHost, int proxyPort) {
        this.username = username;
        this.password = password;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    private void createHttpClient() {
        // Default config
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(30500)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(10);
        connectionManager.setMaxTotal(10*2);

        HttpHost proxy = new HttpHost(proxyHost, proxyPort);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder
                .setConnectionManager(connectionManager)
                .setProxy(proxy)
                .setDefaultRequestConfig(config);

        this.httpClient = new NetworkHttpClient(clientBuilder);
    }

    public TwilioRestClient getClient() {
        if (this.httpClient == null) {
            this.createHttpClient();
        }

        TwilioRestClient.Builder builder = new TwilioRestClient.Builder(username, password);
        return builder.httpClient(this.httpClient).build();
    }
}
