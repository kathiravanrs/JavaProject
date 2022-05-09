import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Database {

    public void post(String user, int tries, int seconds, String word) throws IOException {
        URL url = new URL("https://javaprojectspring2022-abf43-default-rtdb.firebaseio.com/database/" + user + ".json");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

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
    }

    public JSONObject read() throws IOException {

        URL url = new URL("https://javaprojectspring2022-abf43-default-rtdb.firebaseio.com/database.json");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
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

