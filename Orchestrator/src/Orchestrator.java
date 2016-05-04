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
		System.out.println("What ?");
		Scanner reader = new Scanner(System.in);
		int n = reader.nextInt();

		if (n == 1) {
			update_repartiteur.addWN("127.0.0.1 8081", "8081", "127.0.0.1", "2012");
		}
		if (n == 2) {
			update_repartiteur.delWN("127.0.0.1 8081", "8081", "127.0.0.1", "2012");
		}
		if (n == 3) {
			status.getServers();
		}

	}
}
