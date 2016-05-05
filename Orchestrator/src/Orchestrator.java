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

			System.out.print("Repartiteur IP (type 'ok' for 195.220.53.33): ");
			String repartiteurIP = scanner.next();
			System.out.print("Repartiteur PORT (type 'ok' for 8081): ");
			String repartiteurP = scanner.next();
			if (repartiteurIP == "ok") {
				repartiteurIP = "195.220.53.33";
			}
			if (repartiteurP == "ok") {
				repartiteurP = "8081";
			}

			System.out.print("VM IP: ");
			String vmIP = scanner.next();
			System.out.print("VM PORT: ");
			String vmPort = scanner.next();
			update_repartiteur.addWN(repartiteurIP, repartiteurP, vmIP, vmPort);
		}
		if (n == 2) {
			System.out.print("Repartiteur IP (195.220.53.33): ");
			String repartiteurIP = scanner.next();
			System.out.print("Repartiteur PORT (8081): ");
			String repartiteurP = scanner.next();
			if (repartiteurIP == "") {
				repartiteurIP = "195.220.53.33";
			}
			if (repartiteurP == "") {
				repartiteurP = "8081";
			}

			System.out.print("VM IP: ");
			String vmIP = scanner.next();
			System.out.print("VM PORT: ");
			String vmPort = scanner.next();
			update_repartiteur.delWN(repartiteurIP, repartiteurP, vmIP, vmPort);
		}
		if (n == 3) {
			status.getServers();
		}

		if (n == 3) {
			update_repartiteur.fakeAddWN("195.220.53.33", "8081");
		}

	}
}
