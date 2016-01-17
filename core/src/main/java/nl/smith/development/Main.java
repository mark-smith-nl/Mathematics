package nl.smith.development;

import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		Properties properties = new Properties();

		properties.setProperty("myName", "Mark Smith");

		// properties = new Properties(properties);

		properties.setProperty("mijnName", "Mark Smith");

		// System.out.println(properties.contains("myName"));
		System.out.println(properties.contains("Mark Smith"));

		System.out.println(properties.containsKey("myName"));
		System.out.println(properties.containsKey("mijnName"));

		properties.remove("myName");

		System.out.println(properties.containsKey("myName"));
		System.out.println(properties.containsKey("mijnName"));

	}

}
