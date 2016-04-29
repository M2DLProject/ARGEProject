import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.OSFactory;

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

	public static void addWN(String ipR, String portR, String ip, String port) throws IOException {
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

		// Création vm
		OSClient os = OSFactory.builder().endpoint("http://cloudmip.univ-tlse3.fr:5000/v2.0")
				.credentials("ens25", "GOJF00").tenantName("service").authenticate();

		// Create a Server Model Object
		ServerCreate sc = Builders.server().name("ubuntuDoom1").flavor("m1.small").image("doomVM")
				.addSecurityGroup("default").n
						.addPersonality("/etc/motd", "Welcome to the new VM! Restricted access only").build();

		// Boot the Server
		Server server = os.compute().servers().boot(sc);

		Object[] params = new Object[] { new String(ip), new String(port) };
		Integer result = null;
		try {
			result = (Integer) client.execute("Repartiteur.addWN", params);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void delWN(String ipR, String portR, String ip, String port) throws MalformedURLException {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}