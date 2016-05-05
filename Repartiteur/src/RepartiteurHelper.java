import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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

		count++;

		WorkerNode freeWN = getFreeWN();

		return freeWN;
	}

	public Integer callMethod(String method, Object[] params) {

		WorkerNode lastVM = getWN();
		// wait until worker is ready
		// System.out.println("vm " + workerNodes.indexOf(lastVM));
		Integer res = lastVM.callMethod(method, params);
		// lastVM.removeCharge();
		return res;

	}

	public synchronized void addWN(String ip, String port) {
		WorkerNode w = new WorkerNode();
		w.setIp(ip);
		w.setPort(port);
		workerNodes.add(w);
		System.out.println("Add Node : " + ip + ":" + port);

	}

	public synchronized void delWN(String ip, String port) {
		Integer idToDelete = null;
		for (int i = 0; i < workerNodes.size(); i++) {
			WorkerNode worker = workerNodes.get(i);
			if (worker.getIp().equals(ip) && worker.getPort().equals(port)) {
				idToDelete = i;

			}
		}
		if (idToDelete != null) {
			workerNodes.remove(idToDelete);
			System.out.println("workernode deleted" + idToDelete);

		}
		System.out.println("Remove Node : " + ip + ":" + port);

	}

}
