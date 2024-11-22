package com.projecttest;

import org.json.JSONObject;
import java.io.FileReader;
import java.util.Random;
import java.security.MessageDigest;

public class Main {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java -jar <jar_file_name> <roll_number> <path_to_json_file>");
			return;
		}

		// Read inputs
		String rollNumber = args[0].toLowerCase().replaceAll("\\s", ""); // Ensure roll number is lowercase with no spaces
		String filePath = args[1];

		Main app = new Main(); // Create an instance of Main
		String destinationValue;

		try {
			// Step 1: Read and Parse the JSON file
			FileReader reader = new FileReader(filePath);
			StringBuilder jsonContent = new StringBuilder();
			int c;
			while ((c = reader.read()) != -1) {
				jsonContent.append((char) c);
			}
			reader.close();

			// Parse JSON
			JSONObject jsonObject = new JSONObject(jsonContent.toString());

			// Step 2: Traverse JSON to find "destination" key
			destinationValue = app.findKey(jsonObject, "destination");
			if (destinationValue == null) {
				System.out.println("Key 'destination' not found in the JSON file.");
				return;
			}

			// Step 3: Generate a random 8-character alphanumeric string
			String randomString = app.generateRandomString(8);

			// Step 4: Concatenate Roll Number, Destination Value, and Random String
			String toHash = rollNumber + destinationValue + randomString;

			// Step 5: Generate MD5 Hash
			String hashedValue = app.generateMD5Hash(toHash);

			// Step 6: Output the result
			System.out.println(hashedValue + ";" + randomString);

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// Method to find a key in a JSON object recursively
	private String findKey(JSONObject jsonObject, String key) {
		for (String currentKey : jsonObject.keySet()) {
			Object value = jsonObject.get(currentKey);
			if (currentKey.equals(key)) {
				return value.toString();
			} else if (value instanceof JSONObject) {
				String result = findKey((JSONObject) value, key);
				if (result != null) return result;
			}
		}
		return null;
	}

	// Method to generate a random alphanumeric string
	private String generateRandomString(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	// Method to generate an MD5 hash
	private String generateMD5Hash(String input) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(input.getBytes());
		StringBuilder hexString = new StringBuilder();
		for (byte b : digest) {
			hexString.append(String.format("%02x", b));
		}
		return hexString.toString();
	}
}
