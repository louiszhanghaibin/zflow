package zflow.test;

import org.springframework.scheduling.annotation.Async;

public class AsyncFunc {
	@Async
	public static void asyncFunc(int n) {
		if (0 < n) {
			System.out.println("!!!!!!!!!" + n);
			asyncFunc(--n);
		}
		// return;
	}
}
