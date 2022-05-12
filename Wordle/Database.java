import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Database {

    public void post(String user, int tries, int seconds, String word) throws IOException {

        // URL for the cloud Database

        URL url = new URL("https://javaprojectspring2022-abf43-default-rtdb.firebaseio.com/database/" + user + ".json");

        // Creates a new connection for PUT request
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");


        // Create the payload as a string
        String data = "{ " +
                "\"Word\":\"" + word + "\"," +
                "\"Tries\":" + tries + "," +
                "\"Seconds\":" + seconds +
                "}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
        // Disconnect after sending the request and displaying the response message on the console
    }

    public JSONObject read() throws IOException {
        // URL for the cloud Database
        URL url = new URL("https://javaprojectspring2022-abf43-default-rtdb.firebaseio.com/database.json");

        // Creates a new connection for GET request
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());

        // Read the output line by line to a String
        StringBuilder out = new StringBuilder();
        BufferedReader br = null;
        if (http.getResponseCode() == 200) {
            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                out.append(strCurrentLine);
            }
        } else {
            System.out.println("Error while parsing JSON");
        }

        // Parse the String into a JSON object for retrieving data
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(out.toString());
            return obj;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        http.disconnect();
        return null;
    }

}

