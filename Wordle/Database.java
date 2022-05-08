import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Database {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://javaprojectspring2022-abf43-default-rtdb.firebaseio.com/database/.json");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{ " +
                "\"Id\": 12345," +
                "\"Customer\": \"John Smith\"," +
                "\"Quantity\": 100," +
                "\"Price\": 10.00" +
                "}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();

        url = new URL("https://javaprojectspring2022-abf43-default-rtdb.firebaseio.com/database.json");
        http = (HttpURLConnection) url.openConnection();
        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());

        BufferedReader br = null;
        if (http.getResponseCode() == 200) {
            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                System.out.println(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                System.out.println(strCurrentLine);
            }
        }
        http.disconnect();
    }
}
