package zflow.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("zflow")
public class AsyncTest {

	public static void main(String[] args) {
		SpringApplication.run(AsyncTest.class, args);
		int n = 3;
		AsyncFunc.asyncFunc(n);
	}
}
