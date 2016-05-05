import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.openstack4j.api.exceptions.AuthenticationException;

public class update_repartiteur {

	// ./update_repartiteur localhost 2000 add localhost 2012
	public static void main(String[] args) throws Exception {
		String ipR = args[0];
		String portR = args[1];
		String method = args[2];
		String ip = args[3];
		String port = args[4];

		if (method.equals("add")) {
			addWN(ipR, portR, ip, port);
		} else if (method.equals("del")) {
			delWN(ipR, portR, ip, port);
		} else {
			System.out.println("Erreur de syntaxe");
		}
	}

	public static void fakeAddWN(String ipR, String portR)
			throws IOException, AuthenticationException, NoSuchAlgorithmException {
		// Connect to repartiteur
		System.out.println("Call XMLRPC...");
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		System.out.println("http://" + ipR + ":" + portR + "/xmlrpc");
		config.setServerURL(new URL("http://" + ipR + ":" + portR + "/xmlrpc"));
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		XmlRpcClient client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);

		Object[] params = new Object[] { new String(""), new String("") };
		Integer result = null;
		try {
			result = (Integer) client.execute("Repartiteur.addWN", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}

	}

	public static void addWN(String ipR, String portR, String ip, String port)
			throws IOException, AuthenticationException, NoSuchAlgorithmException {

		System.out.println("./update_repartiteur " + ipR + " " + portR + " add " + ip + " " + port + "");

		// Connect to repartiteur
		System.out.print("Call XMLRPC...");
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		System.out.println("http://" + ipR + ":" + portR + "/xmlrpc");
		config.setServerURL(new URL("http://" + ipR + ":" + portR + "/xmlrpc"));
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		XmlRpcClient client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);

		Object[] params = new Object[] { new String(ip), new String(port) };
		Integer result = null;
		try {
			result = (Integer) client.execute("Repartiteur.addWN", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	}

	public static void delWN(String ipR, String portR, String ip, String port) throws MalformedURLException {
		System.out.println("./update_repartiteur " + ipR + " " + portR + " del " + ip + " " + port + "");

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://" + ipR + ":" + portR + "/xmlrpc"));
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		XmlRpcClient client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);

		Object[] params = new Object[] { new String(ip), new String(port) };
		Integer result = null;
		try {
			result = (Integer) client.execute("Repartiteur.delWN", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}

	}
}