
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class Client {

	public static void main(String[] args) throws Exception {

		String ip = "127.0.0.1";
		String port = "8081";
		Integer calls = 10;

		// create configuration
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://" + ip + ":" + port + "/xmlrpc"));
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		XmlRpcClient client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);

		System.out.println("=======================");
		System.out.println("Client");

		for (int i = 0; i < calls; i++) {
			ClientLaucher clientLaucher = new ClientLaucher();
			clientLaucher.client = client;
			clientLaucher.start();
		}

		System.out.println("Finish.");

	}
}