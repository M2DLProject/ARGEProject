import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class RepartiteurHelper {

	public List<WorkerNode> workerNodes = new ArrayList<WorkerNode>();

	public RepartiteurHelper() throws MalformedURLException {

		System.out.println("Repartiteur is started!");
	}

	private Integer count = 0;

	public WorkerNode getFreeWN() {

		return workerNodes.get(count % workerNodes.size());

	}

	public synchronized WorkerNode getWN() {

		// Integer connection = 11;
		WorkerNode freeWN = getFreeWN();
		// while(connection >= 10){

		return freeWN;
	}

	public Integer callMethod(String method, Object[] params) {

		WorkerNode lastVM = getWN();

		System.out.println("Call : " + lastVM.getIp() + " " + lastVM.getPort());
		Integer result = lastVM.callMethod(method, params);

		return result;

	}

	public synchronized void loadWNBase() {

		try {

			File file = new File("dbWN.data");

			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedReader br = new BufferedReader(new FileReader(file));

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] s = sCurrentLine.split(" ");
				System.out.println("IP : " + s[0] + " PORT : " + s[1]);
				WorkerNode w = new WorkerNode(s[0], s[1]);
				workerNodes.add(w);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void updateWNBase() {

		try {

			File file = new File("dbWN.data");

			if (!file.exists()) {
				file.createNewFile();
			}

			String content = "";
			for (WorkerNode w : workerNodes) {
				content += w.getIp() + " " + w.getPort() + "\n";
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(content);
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void addWN(String ip, String port) {
		WorkerNode w = new WorkerNode(ip, port);
		workerNodes.add(w);
		updateWNBase();
		System.out.println("Add Node : " + ip + ":" + port);

	}

	public synchronized void delWN(String ip, String port) {
		Integer idToDelete = null;
		WorkerNode workerToDelete = null;

		for (int i = 0; i < workerNodes.size(); i++) {
			WorkerNode worker = workerNodes.get(i);
			if (worker.getIp().equals(ip) && worker.getPort().equals(port)) {
				idToDelete = i;
				workerToDelete = worker;

			}
		}

		if (idToDelete != null) {

			workerNodes.remove(workerToDelete);

			updateWNBase();
		}

		System.out.println("Removed Node : " + ip + ":" + port);

	}

	public void restart() {
		System.out.print("Restarting... ");
		workerNodes.clear();
		updateWNBase();
		System.out.println("OK ");
	}

	public static Integer getConnexionCount(String ipR, String portR) {

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

		try {
			config.setServerURL(new URL("http://" + ipR + ":" + portR + "/xmlrpc"));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
}
