package graphFiles;
/**
 * CrimeAPI.java
 * Script to access crime statistics from Open Baltimore
 * HopHacks 2019
 * Jason Kurlander, Keilani Carcuso, Emily Zeng, David Skaff
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileWriter;

public class CrimeAPI {

    //String that stores all the data imported from Open Baltimore
    private String crimeData;

    public CrimeAPI() {
        try {
            //url to import data from
            String url = "https://data.baltimorecity.gov/resource/wsfq-mvij.json";

            //import the data
            crimeData = doHttpUrlConnectionAction(url);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // return Open Baltimore crime data
    public String getCrimeData() {
        return crimeData;
    }


  /**
   * Method that sends a get request to the designated url and returns the
   * data retrieved.
   * @param desiredUrl the url to submite get request to
   * @return the data retrieved
   * @throws Exception
   */
   private String doHttpUrlConnectionAction(String desiredUrl) throws Exception {
       URL url = null;
       BufferedReader reader = null;
       StringBuilder stringBuilder;

       try {
           // create the HttpURLConnection
           url = new URL(desiredUrl);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();

           // HTTP get request
           connection.setRequestMethod("GET");

           // give it 15 seconds to respond
           connection.setReadTimeout(15*1000);
           connection.connect();

           // read the output from the server
           reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           stringBuilder = new StringBuilder();

           String line = null;
           while ((line = reader.readLine()) != null) {
               stringBuilder.append(line + "\n");
           }
           return stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                // close the reader
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
}
