
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class client {

	public static XmlRpcClient client;
	public static AsyncCallback async;

	public static Integer calls = 100;
	public static String ipRepartitor = "";

	public synchronized static void loadWNBase() {

		try {

			File file = new File("db.data");

			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedReader br = new BufferedReader(new FileReader(file));

			ipRepartitor = br.readLine();

			System.out.println("Load DB...");
			System.out.println("Repartitor = " + ipRepartitor);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized static void updateWNBase() {

		try {

			File file = new File("db.data");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(ipRepartitor + "\n");
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		// calls = Integer.parseInt(args[0]);
		// String ip = args[1];
		// String port = args[2];
		loadWNBase();

		String port = "8081";

		System.out.println("What repartitor ip ? ( Type 'd' to use '" + ipRepartitor + "' )");
		Scanner reader = new Scanner(System.in);
		String n = reader.next();

		if (!n.equals("d")) {
			ipRepartitor = n;
			updateWNBase();
		}
		System.out.println("Repartitor ip = " + ipRepartitor);

		// create configuration
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://" + ipRepartitor + ":" + port + "/xmlrpc"));
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
				System.out.println("[" + calls + "] 2 +3 = " + arg1);
				return;
			}

			@Override
			public void handleError(XmlRpcRequest arg0, Throwable arg1) {
				// TODO Auto-generated method stub
				System.out.println("Erreur lors de l'appel xmlrpc async du client");

			}

		};

		System.out.println("=======================");

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
					calls = 100;
				}
				if (inputR.equals("z")) {
					calls = 1000;
				}
				if (inputR.equals("e")) {
					calls = 2000;
				}
				if (inputR.equals("r")) {
					calls = 3000;
				}
				if (inputR.equals("t")) {
					calls = 4000;
				}
				if (inputR.equals("y")) {
					calls = 6000;
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