import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.network.NetFloatingIP;
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

	public static void addWN(String ipR, String portR, String ip, String port)
			throws IOException, AuthenticationException, NoSuchAlgorithmException {
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

		System.out.println("Init");

		// Création vm
		Config c = Config.newConfig().withConnectionTimeout(10);

		OSClient os = OSFactory.builder().endpoint("http://195.220.53.61:5000/v2.0").credentials("ens25", "GOJF00")
				.tenantName("service").authenticate();

		System.out.println("Connexion Cloud Mip");

		List<String> network = new ArrayList<>();
		network.add("c1445469-4640-4c5a-ad86-9c0cb6650cca");

		ServerCreate serverCreate = Builders.server().name("doomWN2").flavor("2")
				.image("545f176d-54f8-4bad-93f2-a285870482f4").networks(network).build();

		System.out.println("Create VM");

		Server server = os.compute().servers().boot(serverCreate);

		FloatingIP floatingip = null;
		for (FloatingIP ipi : os.compute().floatingIps().list()) {
			if (ipi.getFixedIpAddress() == null) {
				floatingip = ipi;
			}
		}

		NetFloatingIP netFloatingIP = os.networking().floatingip().get(floatingip.getId());
		os.compute().floatingIps().addFloatingIP(server, netFloatingIP.getFloatingIpAddress());

		System.out.println("Associate VM to ip");

		// Boot the Server

		// Object[] params = new Object[] { new String(ip), new String(port) };
		// Integer result = null;
		// try {
		// result = (Integer) client.execute("Repartiteur.addWN", params);
		// } catch (XmlRpcException e) {
		// e.printStackTrace();
		// }
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