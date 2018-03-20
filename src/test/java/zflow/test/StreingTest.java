package zflow.test;

import com.louisz.zflow.constant.Result;

public class StreingTest {
	public static void main(String[] args) {
		String regx = "\\d{1,}";
		String strtest = "4";

		System.out.println(strtest.matches(regx));

		System.out.println(Result.SUCCESS.toString());

	}
}
