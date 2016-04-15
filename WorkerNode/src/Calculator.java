public class Calculator {
	public int add(int i1, int i2) {

		try {
			Thread.sleep(5000); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		return i1 + i2 + 10;
	}

	public int subtract(int i1, int i2) {
		return i1 - i2;
	}
}
