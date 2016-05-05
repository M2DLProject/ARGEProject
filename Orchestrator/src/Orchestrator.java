import java.util.Scanner;

public class Orchestrator {

	public static void main(String[] args) throws Exception {

		System.out.println("-----------------------------------------");
		System.out.println("ORCHESTRATOR");
		System.out.println("-----------------------------------------");
		System.out.println("");
		System.out.println("1) Manuel");
		System.out.println("2) Auto");
		System.out.println("Mode ?");
		Scanner reader = new Scanner(System.in);
		int n = reader.nextInt();

		if (n == 1) {
			manuel();
		}

	}

	public static void manuel() throws Exception {
		System.out.println("# MANUEL");
		System.out.println("1) add VM");
		System.out.println("2) delete VM");
		System.out.println("3) servers list");
		System.out.println("4) Test XMLRPC");
		System.out.println("What ?");
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();

		if (n == 1) {

			String repartiteurIP = "192.168.0.114";
			String repartiteurP = "8081";

			System.out.print("Repartiteur IP : " + repartiteurIP);
			System.out.print("Repartiteur PORT : " + repartiteurP);

			System.out.print("VM IP: ");
			String vmIP = scanner.next();
			System.out.print("VM PORT: ");
			String vmPort = scanner.next();
			update_repartiteur.addWN(repartiteurIP, repartiteurP, vmIP, vmPort);
		}
		if (n == 2) {
			String repartiteurIP = "192.168.0.114";
			String repartiteurP = "8081";

			System.out.print("Repartiteur IP : " + repartiteurIP);
			System.out.print("Repartiteur PORT : " + repartiteurP);

			System.out.print("VM IP: ");
			String vmIP = scanner.next();
			System.out.print("VM PORT: ");
			String vmPort = scanner.next();
			update_repartiteur.delWN(repartiteurIP, repartiteurP, vmIP, vmPort);
		}
		if (n == 3) {
			status.getServers();
		}

		if (n == 4) {
			update_repartiteur.fakeAddWN("192.168.0.114", "8081");
		}

	}
}
