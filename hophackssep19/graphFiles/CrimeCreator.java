package graphFiles;

/**
 * CrimeCreator.java
 * Script to parse the data from Open Baltimore
 * HopHacks 2019
 * Jason Kurlander, Keilani Carcuso, Emily Zeng, David Skaff
 */

import java.util.Scanner;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

public class CrimeCreator {
    private List<Crime> crimesList;

    public CrimeCreator(String crimeData) {
        crimesList = new LinkedList<Crime>();
        createCrimeList(crimeData);
    }

    public List<Crime> getCrimesList() {
        return crimesList;
    }

    public List<Crime> createCrimeList(String crimeData) {
        Scanner s = new Scanner(crimeData).useDelimiter("}");
        //For each line that represents a crime
        while (s.hasNext()) {
            String crimeString;
            crimeString = s.next();
            Scanner cs = new Scanner(crimeString.substring(1));
            try {
                //Parse data
                String year = cs.findInLine("[0-9]{4}");
                String month = cs.findInLine("[0-9]{2}");
                String day = cs.findInLine("[0-9]{2}");
                String code = cs.findInLine("([0-9])([A-Z])");
                cs.findInLine("location\":\"");
                String location = cs.findInLine("[A-Za-z][^\"]*");
                cs.findInLine("description\":\"");
                String description = cs.findInLine("[A-Za-z][^\"]*");
                cs.findInLine("inside_outside\":\"");
                String outside = cs.findInLine("[A-Za-z][^\"]*");
                cs.findInLine("longitude\":\"");
                String longitude = cs.findInLine("[-]?[0-9][^\"]*");
                cs.findInLine("latitude\":\"");
                String latitude = cs.findInLine("[-]?[0-9][^\"]*");
                cs.findInLine("total_incidents\":\"");
                String incidents = cs.findInLine("\\d");

                Crime crime = new Crime(new Date(),//Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)),
                                    code,
                                    location,
                                    description,
                                    !(outside.charAt(0) == ('I')),
                                    Integer.parseInt(incidents),
                                    Double.parseDouble(longitude),
                                    Double.parseDouble(latitude));
                crimesList.add(crime);
                /** If the data is missing values for location, it will throw
                  * a NullPointerException or NumberFormatException, however,
                  * if it has no location, it is irelevant to this software,
                  * therefore we ignore it.
                  */
            } catch (NullPointerException e) {
                continue;
            } catch (NumberFormatException e) {
                continue;
            }
        }
        s.close();
        return crimesList;
    }
}
