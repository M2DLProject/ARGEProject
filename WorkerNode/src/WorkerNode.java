
//  import org.apache.xmlrpc.demo.webserver.proxy.impls.AdderImpl;
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

	public static void main(String[] args) throws Exception {

		System.out.println("Worker starting...");

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

	public double getSystemCPU() {

		CpuPerc cpuperc = null;
		try {
			cpuperc = sigar.getCpuPerc();
		} catch (SigarException se) {
			se.printStackTrace();
		}

		return (cpuperc.getCombined() * 100);
	}

	public int add(int i1, int i2) {

		try {

			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		return i1 + i2 + 10;
	}
}