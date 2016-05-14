import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;

public class ClientLaucher extends Thread {

	public XmlRpcClient client;

	public void run() {
		// make the a regular call
		Object[] params = new Object[] { new String("add"), new Integer(2), new Integer(3) };
		Integer result = null;
		try {
			AsyncCallback async = new AsyncCallback() {
				
				public void handleResult(XmlRpcRequest arg0, Object arg1) {
					// TODO Auto-generated method stub
					System.out.println("2 +3 = " +arg1);
					return;
					
				}
				
				@Override
				public void handleError(XmlRpcRequest arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					System.out.println("Erreur lors de l'appel xmlrpc async du client");
					
				}
			};
			
			
			
			client.executeAsync("Repartiteur.call", params, async);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("2 + 3 = " + result);
	}
}
