import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

public class ClientLaucher extends Thread {

	public XmlRpcClient client;

	public void run() {
		// make the a regular call
		Object[] params = new Object[] { new String("add"), new Integer(2), new Integer(3) };
		Integer result = null;
		try {
			result = (Integer) client.execute("Repartiteur.call", params);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("2 + 3 = " + result);
	}
}
