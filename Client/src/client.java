
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class client {

	public static XmlRpcClient client;
	public static AsyncCallback async;
	private static String myIp = "192.168.0.184";
	public static Integer calls = 0;

	public static void main(String[] args) throws Exception {

		calls = Integer.parseInt(args[0]);
		String ip = args[1];
		String port = args[2];

		// create configuration
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://" + ip + ":" + port + "/xmlrpc"));
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);

		async = new AsyncCallback() {

			public void handleResult(XmlRpcRequest arg0, Object arg1) {
				// TODO Auto-generated method stub
				// System.out.println("[" + calls + "] 2 +3 = " + arg1);
				return;
			}

			@Override
			public void handleError(XmlRpcRequest arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				System.out.println("Erreur lors de l'appel xmlrpc async du client");

			}

		};

		System.out.println("=======================");
		System.out.println("Client call " + calls + " : " + ip + " " + port);

		InputStreamReader fileInputStream = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(fileInputStream);

		String inputR = "";
		while (true) {

			for (int i = 0; i < calls; i++) {
				call();
			}

			// update calls
			if (bufferedReader.ready()) {
				inputR = bufferedReader.readLine();
				if (inputR.equals("a")) {
					calls = 1000;
				}
				if (inputR.equals("z")) {
					calls = 10000;
				}
				if (inputR.equals("e")) {
					calls = 30000;
				}
				if (inputR.equals("r")) {
					calls = 50000;
				}
				if (inputR.equals("t")) {
					calls = 80000;
				}
				System.out.println("Update calls to " + calls);
			}

			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}

		}
	}

	public static void call() {

		// make the a regular call
		Object[] params = new Object[] { new String("add"), new Integer(2), new Integer(3) };

		try {
			client.executeAsync("Repartiteur.call", params, async);

		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("2 + 3 = " + result);
	}

}