import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.openstack4j.api.OSClient;
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
			Map<String, ? extends Number> diagnostics = os.compute().servers().diagnostics(servers.get(it).getId());
			String wNodeIP = diagnostics.toString();
			String wNodeName = servers.get(it).getName();
			System.out.println(wNodeName + " : " + wNodeIP);
			it++;
		}

	}
}
