package zflow.test;

public class StreingTest {
	public static void main(String[] args) {
		String regx = ",";
		String strtest = "4,f,744,4,,3";

		String[] strings = strtest.split(regx);
		for (String string : strings) {
			System.out.println(string);
		}

	}
}
