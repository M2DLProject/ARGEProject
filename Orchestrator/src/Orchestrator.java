import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.OSFactory;

public class Orchestrator {

	public static OSClient os;

	public static List<String> workerNodes = new ArrayList<String>();

	public static void main(String[] args) throws Exception {

		System.out.println("-----------------------------------------");
		System.out.println("ORCHESTRATOR");
		System.out.println("-----------------------------------------");
		os = connnexionCloudMip();
		System.out.println("");
		System.out.println("1) Manual");
		System.out.println("2) Auto");
		System.out.println("Mode ?");
		Scanner reader = new Scanner(System.in);
		int n = reader.nextInt();

		if (n == 1) {
			manuel();
		}
		if(n == 2){
			auto();
		}

		if (n == 2) {
			auto();
		}

	}

	public static Integer getConnexionCount(String ipR, String portR) throws Exception {

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

		Object[] params = new Object[] {};
		Integer result = null;
		try {
			result = (Integer) client.execute("Calculator.getConnexionCount", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void auto() throws Exception {

		System.out.println("#AUTO");
		getAllWN();

		while (true) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			int total = 0;
			int nbConnexionMax = 5;
			System.out.println("Scan...");
			for (String ip : workerNodes) {

				Integer count = getConnexionCount(ip, "8080");
				System.out.println(ip + " : " + count);
				total = total + count;
			}
			if (total/workerNodes.size() > nbConnexionMax ){
				String repartiteurIP = "192.168.0.180";
				String repartiteurP = "8081";
				System.out.println("Mise à jour Ajout d'une machine");
				Map<String, String> params = createVM();
				System.out.println("VM IP: " + params.get("ip"));
				System.out.println("VM PORT: " + params.get("port"));
				update_repartiteur.addWN(repartiteurIP, repartiteurP, params.get("ip"), params.get("port"));
			} else {
				//Supprimé VM
			}

		}

	}

	public static void manuel() throws Exception {
		System.out.println("# MANUAL");
		System.out.println("1) add VM");
		System.out.println("2) delete VM");
		System.out.println("3) servers list");
		System.out.println("4) Restart Repartiteur (XMLRPC)");
		System.out.println("5) Delete all workernodes");
		System.out.print("What ? ");
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();

		if (n == 1) {

			String repartiteurIP = "192.168.0.180";
			String repartiteurP = "8081";
			System.out.println("Repartiteur IP : " + repartiteurIP);
			System.out.println("Repartiteur PORT : " + repartiteurP);

			Map<String, String> params = createVM();

			System.out.println("VM IP: " + params.get("ip"));
			System.out.println("VM PORT: " + params.get("port"));

			update_repartiteur.addWN(repartiteurIP, repartiteurP, params.get("ip"), params.get("port"));
		}

		if (n == 2) {
			String repartiteurIP = "192.168.0.180";
			String repartiteurP = "8081";
			System.out.println("Repartiteur IP : " + repartiteurIP);
			System.out.println("Repartiteur PORT : " + repartiteurP);
			System.out.print("IP to delete? ");
			String ip = scanner.next();
			String port = "8080";
			System.out.println("VM IP: " + ip);
			System.out.println("VM PORT: " + port);

			deleteVM(ip);

			update_repartiteur.delWN(repartiteurIP, repartiteurP, ip, port);
		}
		if (n == 3) {
			getServerList();
		}

		if (n == 4) {
			update_repartiteur.restartRepartiteur("192.168.0.180", "8081");
		}

		if (n == 5) {
			deleteAllWN();
		}

		manuel();

	}

	public static OSClient connnexionCloudMip() {
		System.out.print("Connexion Cloud Mip... ");
		OSClient os = OSFactory.builder().endpoint("http://195.220.53.61:5000/v2.0").credentials("ens25", "GOJF00")
				.tenantName("service").authenticate();

		System.out.println("OK");
		return os;
	}

	public static void deleteVM(String ip) {

		// List all Servers
		List<? extends Server> servers = os.compute().servers().list();

		boolean isFound = false;
		int it = 0;
		while (!isFound && it < servers.size()) {
			if (servers.get(it).getAddresses().getAddresses().toString().contains(ip)) {
				String wNodeId = servers.get(it).getId();
				String name = servers.get(it).getName();
				os.compute().servers().delete(wNodeId);
				System.out.println("Delete VM : " + name);
				isFound = true;
			}
			it++;
		}

		workerNodes.remove(ip);

	}
	
	public static void getAllWN() {

		List<? extends Server> servers = os.compute().servers().list();

		int it = 0;
		while (it < servers.size()) {

			Server server = os.compute().servers().get(servers.get(it).getId());

			if (server.getName().contains("doom_WN_")) {
				String ip = getServerIP(server, TypeIP.Private);
				workerNodes.add(ip);
			}

			it++;
		}

	}

	public static void deleteAllWN() {

		List<? extends Server> servers = os.compute().servers().list();

		int it = 0;
		while (it < servers.size()) {

			Server server = os.compute().servers().get(servers.get(it).getId());

			if (server.getName().contains("doom_WN_")) {
				String ip = getServerIP(server, TypeIP.Private);
				deleteVM(ip);
			}

			it++;
		}

	}

	public static Boolean checkWNisReady(String ip, String port) throws MalformedURLException {

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://" + ip + ":" + port + "/xmlrpc"));
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(1000);

		XmlRpcClient client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		client.setConfig(config);

		Integer result = 0;
		Object[] params = new Object[] {};
		System.out.print("...");
		try {
			result = (Integer) client.execute("Calculator.status", params);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return result == 1;

	}

	public static Map<String, String> createVM() throws MalformedURLException {

		// Create VM
		System.out.print("Create VM...");
		List<String> network = new ArrayList<>();
		network.add("c1445469-4640-4c5a-ad86-9c0cb6650cca");

		// ServerCreate serverCreate = Builders.server().name("doom_WN_" + new
		// Date().getTime()).flavor("2")
		// .image("545f176d-54f8-4bad-93f2-a285870482f4").networks(network).build();

		ServerCreate serverCreate = Builders.server().name("doom_WN_" + new Date().getTime()).flavor("2")
				.image("6cfb3c4c-54f6-44c5-8c69-14c63799b376").networks(network).build();

		System.out.println("OK");

		System.out.print("Boot VM...");
		Server server = os.compute().servers().boot(serverCreate);
		while (!os.compute().servers().get(server.getId()).getStatus().equals(Status.ACTIVE)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("OK");

		server = os.compute().servers().get(server.getId());

		String ip = getServerIP(server, TypeIP.Private);

		Map<String, String> result = new HashMap<String, String>();
		String port = "8080";
		result.put("port", port);
		result.put("ip", ip);

		System.out.print("Boot Worker Node...");
		try {
			while (!checkWNisReady(ip, port)) {

				Thread.sleep(2000);

			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		System.out.println("OK");

		workerNodes.add(ip);

		return result;

	}

	public enum TypeIP {
		Private(0), Public(1);

		private Integer value = 0;

		// Constructeur
		TypeIP(Integer value) {
			this.value = value;
		}

		public Integer toValue() {
			return value;
		}
	}

	private static String getServerIP(Server server, TypeIP typeIP) {
		Map<String, List<? extends Address>> adrMap = server.getAddresses().getAddresses();

		return adrMap.get("private").get(typeIP.toValue()).getAddr().toString();
	}

	public static void getServerList() throws MalformedURLException {

		System.out.println("Server List");

		// List all Servers
		List<? extends Server> servers = os.compute().servers().list();

		int it = 0;
		while (it < servers.size()) {

			Server server = os.compute().servers().get(servers.get(it).getId());
			String wNodeIP = "";
			String wNodeName = server.getName();

			Map<String, List<? extends Address>> adrMap = server.getAddresses().getAddresses();

			for (Address a : adrMap.get("private")) {
				wNodeIP += a.getAddr() + "; ";
			}
			if (wNodeName.contains("doom")) {
				System.out.println(" - " + wNodeName + " : " + wNodeIP);
			}

			it++;
		}

	}

	/*
	 * FloatingIP floatingip = null; for (FloatingIP ipi :
	 * os.compute().floatingIps().list()) { if (ipi.getFixedIpAddress() == null)
	 * { floatingip = ipi; } }
	 * 
	 * NetFloatingIP netFloatingIP =
	 * os.networking().floatingip().get(floatingip.getId());
	 * 
	 * System.out.println("neutron floatingip-create public = " +
	 * netFloatingIP.getFloatingIpAddress());
	 * os.compute().floatingIps().addFloatingIP(server,
	 * netFloatingIP.getFloatingIpAddress());
	 * 
	 * System.out.println("Waiting the server...");
	 * 
	 * System.out.println("Associate VM to ip"); System.out.println("[" +
	 * server.getId() + "]" + netFloatingIP.getFloatingIpAddress());
	 */
}
