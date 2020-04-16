package com.example.demo;

import net.plan99.nodejs.java.NodeJS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootApplication
public class DemoApplication
implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		LOG.info("EXECUTING : starting");

		String runtimePath = resolvePath("runtime.js");
		String middlewarePath = resolvePath("middleware.cjs");

		LOG.info("EXECUTING : runtimePath {} and middlewarePath {}", runtimePath, middlewarePath);

		String html = read("sample.html");

		for (int i = 0; i < 1000; i++) {
			execute(html, runtimePath, middlewarePath);
			Thread.sleep(5000); // to avoid killing my MAC
		}

		LOG.info("EXECUTING : finished");
	}

	static ResultHolder execute(String input, String runtimePath, String middlewarePath) {

		ResultHolder onResolve = new ResultHolder.Success();
		ResultHolder onReject = new ResultHolder.Failure();

		return NodeJS.runJS(() -> {
			// TODO maybe NodeJS could work with org.graalvm.polyglot.Source instead of String
			NodeJS.eval(
			"(async function(html, runtime, midd) {  \n"
					+ " const middleware_runtime = require(runtime); \n"
					+ " const middleware = require(midd); \n"
					+ " let value = await middleware_runtime(html, middleware); \n"
					+ " if (value) { \n"
					+ "   return JSON.stringify(value); \n"
					+ " } else { \n"
					+ "   return value; \n"
					+ " } \n"
					+ "})"
			).execute(input, runtimePath, middlewarePath)
					.invokeMember("then", onResolve)
					.invokeMember("catch", onReject);

			return onResolve.isEmpty() ? onReject : onResolve;
		});
	}

	static String resolvePath(String fileName) {
		return Paths.get("src/test/resources/" + fileName).toAbsolutePath().toFile().getAbsolutePath();
	}

	static String read(String fileName) throws Exception {
		Path path = Paths.get("src/test/resources/" + fileName);
		return String.join(" ", Files.readAllLines(path));
	}

}
