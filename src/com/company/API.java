package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.*;

import java.sql.Date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


public class API {
    private static HttpURLConnection connection;

    public static void getConnect(String country, int days) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime now = LocalDateTime.now();
        String currentDate = dtf.format(now);

        LocalDateTime startDate = now.minusDays(days);
        String startDateString = dtf.format(startDate);

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+startDateString+"&endtime="+currentDate+"");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if (status > 299) {
                reader = new BufferedReader((new InputStreamReader(connection.getErrorStream())));
                System.out.println("No Earthquakes were recorded past " +days+ "days");
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
            List<Earthquake> list = parse(responseContent.toString(),country, days);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Earthquake> parse(String responseBody,String country, int days) {

        JSONObject earthquakes = new JSONObject(responseBody);
        JSONArray jsonArray = earthquakes.getJSONArray("features");

        List<Earthquake> listEarthquake = new ArrayList<>();

        Earthquake earthquake;

        for (int i = 0; i < jsonArray.length(); i++) {
            Object place = jsonArray.getJSONObject(i).getJSONObject("properties").get("place");
            Object magnitude = jsonArray.getJSONObject(i).getJSONObject("properties").get("mag");
            Object startTime = jsonArray.getJSONObject(i).getJSONObject("properties").get("time");
            Object endTime = jsonArray.getJSONObject(i).getJSONObject("properties").get("updated");

            Date startDate = new Date(Long.parseLong(String.valueOf(startTime)));


            if (!place.equals(null) && !magnitude.equals(null) && !startTime.equals(null) && !endTime.equals(null)  ) {
                earthquake = new Earthquake();
                earthquake.setPlace((String) place);
                earthquake.setCountry(((String) place).substring(((String) place).indexOf(",") +2));
                earthquake.setMagnitude((Double.parseDouble(String.valueOf(magnitude))));
                earthquake.setStartTime(startDate);
                if (earthquake.getCountry().equals(country))
                listEarthquake.add(earthquake);
            }

        }

        listEarthquake.forEach(listOfEarthquakes -> System.out.println("Place: " + listOfEarthquakes.getPlace()+" -- " +
                "Magnitude: " +  listOfEarthquakes.getMagnitude()+
                " -- " +  "Country: " + listOfEarthquakes.getCountry()+
                " -- " + "Time: " + listOfEarthquakes.getStartTime()));

        return listEarthquake;
    }

}

