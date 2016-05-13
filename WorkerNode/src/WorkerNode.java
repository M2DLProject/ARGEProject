
//  import org.apache.xmlrpc.demo.webserver.proxy.impls.AdderImpl;
import java.util.Random;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class WorkerNode {

	private static Sigar sigar = new Sigar();

	private static final int port = 8080;

	public static Integer connexionCount = 0;

	public static synchronized void addConnexion() {
		connexionCount++;
	}

	public static synchronized void removeConnexion() {
		connexionCount--;
	}

	public static int random = 1000;

	public static void main(String[] args) throws Exception {

		System.out.println("Worker starting...");

		Random rand = new Random(); // constructeur
		random = rand.nextInt(10);
		if (random == 0) {
			random = 1;
		}

		WebServer webServer = new WebServer(port);

		XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

		PropertyHandlerMapping phm = new PropertyHandlerMapping();

		phm.addHandler("Calculator", WorkerNode.class);

		xmlRpcServer.setHandlerMapping(phm);

		XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
		serverConfig.setEnabledForExtensions(true);
		serverConfig.setContentLengthOptional(false);

		webServer.start();
		System.out.println("Worker is started!");

	}

	public int status() {
		return 1;
	}

	public int getConnexionCount() {
		return connexionCount;
	}

	public static Double getSystemStatistics() {

		CpuPerc cpuperc = null;
		try {
			cpuperc = sigar.getCpuPerc();
		} catch (SigarException se) {
			se.printStackTrace();
		}

		return (cpuperc.getCombined() * 100);
	}

	public int add(int i1, int i2) {

		addConnexion();
		try {

			Thread.sleep(random * 1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		removeConnexion();
		return i1 + i2 + 10 + random * 1000;
	}
}