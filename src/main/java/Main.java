//import com.google.gson.Gson;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Main {

    public static void main(String args[]) throws IOException {
        JDBC();
        //MyGETRequest();
        //MyPOSTRequest();

    }

    public static void JDBC() throws IOException{
        try {
            System.out.println("Hi");

            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

            System.out.println("Connecting to DB");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","c##rohan","123");

            PreparedStatement ps = con.prepareStatement("select * from EMPLOYEES where first_name= ? or first_name = ?");
            ps.setString(1, "Ivy");
            ps.setString(2, "Emily");
            //ps.setString(2, "Burns");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String result = rs.getString("first_name") + " " + rs.getString("last_name");
                System.out.println(result);
            }

            rs.close();
            con.close();


        }
        catch(Exception e){
            System.out.println("Not working");
            e.printStackTrace();
        }
    }

    public static void MyGETRequest() throws IOException{

        int threadCount = 1;
        String threadName = "Thread" + threadCount;
        System.out.println(threadName+" \n");

        URL urlForGetRequest = new URL("https://jsonplaceholder.typicode.com/posts/1");
        String readLine = null;
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("userId","a1bcdef");
        int responseCode = connection.getResponseCode();

        Gson gson = new Gson();

        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while((readLine = in.readLine()) != null){
                response.append(readLine + "\n");
            } in.close();
            //String output = gson.fromJson(response,String.class);
            System.out.println("JSON String Result" + response.toString());


            //System.out.println(output);
        }
        else{
            System.out.println("GET DID NOT WORK");
        }
    }

    public static void MyPOSTRequest() throws IOException{
        final String POST_PARAMS = "{\n" + "\"userId\": 101,\r\n" +
               " \"id\": 101, \r\n" +
               " \"title\": \"test Title\",\r\n" +
               " \"body\": \"Test Body\"" + "\n}";
        System.out.println(POST_PARAMS);
        URL obj = new URL("https://jsonplaceholder.typicode.com/posts");
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("userId","a1bcdef");
        postConnection.setRequestProperty("Content-Type","application/json");

        postConnection.setDoOutput(true);
        OutputStream os = postConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();

        int responseCode = postConnection.getResponseCode();
        System.out.println("POST Response Code : " + responseCode);
        System.out.println("POST Response Message : " + postConnection.getResponseMessage());

        if(responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
        }
        else{
            System.out.println("POST DID NOT WORK");
        }
    }
}
