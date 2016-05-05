import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.OSFactory;

public class Orchestrator {

	public static OSClient os;

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

	}

	public static void manuel() throws Exception {
		System.out.println("# MANUAL");
		System.out.println("1) add VM");
		System.out.println("2) delete VM");
		System.out.println("3) servers list");
		System.out.println("4) Test XMLRPC");
		System.out.println("5) Delete all workernodes");
		System.out.println("What ?");
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();

		if (n == 1) {

			String repartiteurIP = "192.168.0.114";
			String repartiteurP = "8081";
			System.out.println("Repartiteur IP : " + repartiteurIP);
			System.out.println("Repartiteur PORT : " + repartiteurP);

			Map<String, String> params = createVM();

			System.out.println("VM IP: " + params.get("ip"));
			System.out.println("VM PORT: " + params.get("port"));

			update_repartiteur.addWN(repartiteurIP, repartiteurP, params.get("ip"), params.get("port"));
		}

		if (n == 2) {
			String repartiteurIP = "192.168.0.114";
			String repartiteurP = "8081";
			System.out.println("Repartiteur IP : " + repartiteurIP);
			System.out.println("Repartiteur PORT : " + repartiteurP);
			System.out.print("IP to delete? ");
			String ip = scanner.next();
			String port = "8081";
			System.out.println("VM IP: " + ip);
			System.out.println("VM PORT: " + port);

			deleteVM(ip);

			update_repartiteur.delWN(repartiteurIP, repartiteurP, ip, port);
		}
		if (n == 3) {
			status.getServers();
		}

		if (n == 4) {
			update_repartiteur.fakeAddWN("192.168.0.114", "8081");
		}

		if (n == 5) {
			deleteAllWN();
		}

	}

	public static OSClient connnexionCloudMip() {
		System.out.print("Connexion Cloud Mip... ");
		OSClient os = OSFactory.builder().endpoint("http://195.220.53.61:5000/v2.0").credentials("ens25", "GOJF00")
				.tenantName("service").authenticate();

		System.out.println("OK");
		return os;
	}

	public static void deleteVM(String ip) {

		System.out.println("Delete VM... ");
		// List all Servers
		List<? extends Server> servers = os.compute().servers().list();

		boolean isFound = false;
		int it = 0;
		while (!isFound && it < servers.size()) {
			if (servers.get(it).getAddresses().getAddresses().toString().contains(ip)) {
				String wNodeId = servers.get(it).getId();
				os.compute().servers().delete(wNodeId);
				System.out.println("id = " + wNodeId);
				isFound = true;
			}
			it++;
		}
		System.out.println("Finish");
	}

	public static void deleteAllWN() {

		List<? extends Server> servers = os.compute().servers().list();

		int it = 0;
		while (it < servers.size()) {

			Server server = os.compute().servers().get(servers.get(it).getId());

			if (server.getName().contains("doom_WN_")) {
				String ip = getServerIP(server, TypeIP.Private);
				deleteVM(ip);
				System.out.println("Delete WN : " + ip);
			}

			it++;
		}
	}

	public static Map<String, String> createVM() {

		// Create VM
		System.out.print("Create VM...");
		List<String> network = new ArrayList<>();
		network.add("c1445469-4640-4c5a-ad86-9c0cb6650cca");

		ServerCreate serverCreate = Builders.server().name("doom_WN_" + new Date().getTime()).flavor("2")
				.image("545f176d-54f8-4bad-93f2-a285870482f4").networks(network).build();

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
		result.put("port", "8080");
		result.put("ip", ip);

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
