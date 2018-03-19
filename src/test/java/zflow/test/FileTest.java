package zflow.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class FileTest {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String path = "D:\\opt\\aaa.xml";
		String content = IOUtils.toString(new FileInputStream(path));

		System.out.println(content);
	}
}
