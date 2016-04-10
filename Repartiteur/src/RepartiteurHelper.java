import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RepartiteurHelper {

	private Integer count = 0;

	public static RepartiteurHelper instance = null;

	public List<WorkerNode> workerNodes = new ArrayList<WorkerNode>();

	public static RepartiteurHelper getInstance() throws MalformedURLException {
		if (instance == null) {

			instance = new RepartiteurHelper();

		}
		return instance;
	}

	public RepartiteurHelper() throws MalformedURLException {

		System.out.println("Repartiteur is started!");

	}

	public Boolean needANewVM() {
		return true;
	}

	public Integer callMethod(String method, Object[] params) {

		if (needANewVM()) {
			WorkerNode workerNode = new WorkerNode();
			workerNodes.add(workerNode);
		}

		WorkerNode lastVM = workerNodes.get(workerNodes.size() - 1);
		// wait until worker is ready
		return lastVM.callMethod(method, params);

	}

}
