import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Server;
import org.openstack4j.openstack.OSFactory;

public class status {

	public static void getServers() throws MalformedURLException {

		OSClient os = OSFactory.builder().endpoint("http://195.220.53.61:5000/v2.0").credentials("ens25", "GOJF00")
				.tenantName("service").authenticate();

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
			System.out.println(wNodeName + " : " + wNodeIP);
			it++;
		}

	}
}
