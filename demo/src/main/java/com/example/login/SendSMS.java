package com.example.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {

    // Method to send SMS with a custom message
    public String sendSms(String message, String phoneNumber) {
        try {
            // Construct data
            String apiKey = "apikey=" + "NzUzNjMzMzI0ZTM3NjI1OTQ5NWE2NzMwNmI1MzMzNGY=";  // Use your own API key
            String sender = "&sender=" + "phandang"; // Use your sender ID
            String numbers = "&numbers=" + phoneNumber;

            // URL encode the message to handle special characters
            String encodedMessage = "&message=" + URLEncoder.encode(message, "UTF-8");

            // Send data using HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + encodedMessage + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));

            // Send the request
            conn.getOutputStream().write(data.getBytes("UTF-8"));

            // Read the response from the API
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            // Check if the response contains an error message (e.g., failed to send)
            String response = stringBuffer.toString();
            if (response.contains("error") || response.contains("failed")) {
                System.out.println("Error sending SMS: " + response);
                return "Error sending SMS: " + response;
            } else {
                return response; // Return the successful response
            }

        } catch (Exception e) {
            // Handle and print any exceptions that occur during the process
            System.out.println("Exception occurred while sending SMS: " + e.getMessage());
            e.printStackTrace(); // Optionally print the stack trace for debugging
            return "Exception occurred: " + e.getMessage();
        }
    }
}
