import java.net.MalformedURLException;

import org.apache.xmlrpc.XmlRpcException;

public class Repartiteur {

	public int call(String method, int i1, int i2) throws MalformedURLException, XmlRpcException {

		System.out.println("Repartiteur is call!");
		RepartiteurHelper repartiteurHelper = RepartiteurHelper.getInstance();

		// make the a regular call
		Object[] params = new Object[] { new Integer(i1), new Integer(i2) };
		return repartiteurHelper.callMethod(method, params);

		// make a call using dynamic proxy
		/*
		 * ClientFactory factory = new ClientFactory(client); Adder adder =
		 * (Adder) factory.newInstance(Adder.class); int sum = adder.add(2, 4);
		 * System.out.println("2 + 4 = " + sum);
		 */
	}
}