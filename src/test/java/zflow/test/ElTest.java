package zflow.test;

import com.louisz.zflow.util.ELParseUtil;

public class ElTest {
	public static void main(String[] args) {
		String elString = "#{zflow.test.ElTest.test()}";
		System.out.println(ELParseUtil.reassemble(elString));
	}

	public static boolean test() {
		boolean b = true;
		return b;
	}
}
