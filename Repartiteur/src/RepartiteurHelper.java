import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
				WorkerNode w = new WorkerNode();
				w.setIp(s[0]);
				w.setPort(s[1]);
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
		WorkerNode w = new WorkerNode();
		w.setIp(ip);
		w.setPort(port);
		workerNodes.add(w);
		updateWNBase();
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
