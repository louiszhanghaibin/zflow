package zflow.test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();

		map.put("1", "1");
		map.put("3", "3");
		System.out.println(map.toString());
		try {
			map.remove("2");
			map.remove("3");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(map.toString());
	}
}
