package graphFiles;

/**
 * Crime.java
 * Class to store crime reports from Open Baltimore
 * HopHacks 2019
 * Jason Kurlander, Keilani Carcuso, Emily Zeng, David Skaff
 */

import java.util.Date;

public class Crime {
    private Date date;
    private String crimeCode;
    private String location;
    private String description;
    private boolean outdoors;
    private int incidents;
    private double longitude;
    private double latitude;

    public Crime(Date date, String crimeCode, String location,
                String description, boolean oudoors, int incidents,
                double longitude, double latitude) {
        this.date = date;
        this.crimeCode = crimeCode;
        this.location = location;
        this.description = description;
        this.outdoors = outdoors;
        this.incidents = incidents;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    //return latitude
    public double getLat() {
        return this.latitude;
    }

    //return longitude
    public double getLon() {
        return this.longitude;
    }

    //return approximate address
    public String getLocation() {
        return this.location;
    }

    //return UCR crime code
    public String getCrimeCode() {
        return this.crimeCode;
    }

}
