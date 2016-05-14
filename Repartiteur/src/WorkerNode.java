import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class WorkerNode {

	private XmlRpcClient client;

	private String ip = "0";

	private String port = "0";

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	private XmlRpcClientConfigImpl config;

	private Integer charge = 0;

	public WorkerNode(String ip, String port) {

		this.setIp(ip);
		this.setPort(port);
		config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL("http://" + ip + ":" + port + "/xmlrpc"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		config.setEnabledForExtensions(true);
		config.setConnectionTimeout(60 * 1000);
		config.setReplyTimeout(60 * 1000);

		client = new XmlRpcClient();

		// use Commons HttpClient as transport
		client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
		// set configuration
		client.setConfig(config);

		System.out.println("WorkerNode is started!");

	}

	public Integer callMethod(String method, Object[] params) {

		Integer result = 0;
		try {
			result = (Integer) client.execute("Calculator." + method, params);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public Boolean isCPUisOverload() {
		return charge > 2;
	}

}
