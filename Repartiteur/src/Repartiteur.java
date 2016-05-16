
//  import org.apache.xmlrpc.demo.webserver.proxy.impls.AdderImpl;
import java.net.MalformedURLException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class Repartiteur {
	private static final int port = 8081;

	private static RepartiteurHelper repartiteurHelper;

	public static void main(String[] args) throws Exception {

		System.out.println("Repartiteur staring....");

		WebServer webServer = new WebServer(port);

		XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

		PropertyHandlerMapping phm = new PropertyHandlerMapping();

		repartiteurHelper = new RepartiteurHelper();
		System.out.println("Loading DB...");
		repartiteurHelper.loadWNBase();

		phm.addHandler("Repartiteur", Repartiteur.class);

		xmlRpcServer.setHandlerMapping(phm);

		XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
		serverConfig.setEnabledForExtensions(true);
		serverConfig.setContentLengthOptional(false);

		webServer.start();

	}

	public int call(String method, int i1, int i2) throws MalformedURLException, XmlRpcException {

		// make the a regular call
		Object[] params = new Object[] { new Integer(i1), new Integer(i2) };
		return repartiteurHelper.callMethod(method, params);

	}

	public int addWN(String ip, String port) {
		repartiteurHelper.addWN(ip, port);

		return 1;
	}

	public int delWN(String ip, String port) {
		repartiteurHelper.delWN(ip, port);
		return 1;
	}

	public int restart() {
		repartiteurHelper.restart();
		return 1;
	}

}