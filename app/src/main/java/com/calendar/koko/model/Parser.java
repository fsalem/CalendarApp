package com.calendar.koko.model;

import com.calendar.koko.model.objects.ConfirmObject;
import com.calendar.koko.model.objects.CreateEventObject;
import com.calendar.koko.model.objects.EventSearchObject;
import com.calendar.koko.model.objects.EventSearchPeriodObject;
import com.calendar.koko.model.objects.NameValuePair;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by farouk on 11/5/15.
 */
public class Parser {
    private final static HashMap<String, String> URI_MAP = new HashMap<String, String>();
    private final static String SERVER_URL = "http://130.233.42.98:8080";
    //private final static String SERVER_URL = "http://192.168.0.101:8080";
    static {
        URI_MAP.put("login", "/api/users/login/");
        URI_MAP.put("CRUDEvent", "/api/events/");
        URI_MAP.put("getPeriodEvents", "/api/events/search/period/");
    }

    private Parser() {

    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private static String getResponseContent(InputStream content) throws IOException {
        BufferedReader bReader = new BufferedReader(new InputStreamReader(content));
        StringBuilder stringBuilder = new StringBuilder("");
        String line;
        while ((line = bReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private static String getURLContent(String uri, String method, List<NameValuePair> params) {
        try {
            //System.out.println("getURLContent: "+uri);
            URL url = new URL(SERVER_URL + uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            if (params != null) {
                connection.setDoOutput(true);
                //System.out.println(params.toString());
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
            }
            connection.connect();
            int statusCode = connection.getResponseCode();
            //System.out.println("getURLContent: statusCode = "+statusCode);
            if (statusCode == 200) {
                String content = getResponseContent((InputStream) connection.getContent());

                return content;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static ConfirmObject login(String email, String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("email", email));
        params.add(new NameValuePair("password", password));
        String content = getURLContent(URI_MAP.get("login"), "POST", params);
        ConfirmObject confirmObject = new Gson().fromJson(content, ConfirmObject.class);
        //System.out.println("success object = "+confirmObject + " & error = "+confirmObject.getError());
        return confirmObject;
    }

    public static ConfirmObject createEvent(CreateEventObject eventObject) throws IllegalAccessException, InvocationTargetException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Method[] methods = eventObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                Object object = method.invoke(eventObject);
                if (object != null) {
                    params.add(new NameValuePair(Character.toLowerCase(method.getName().charAt(3))+method.getName().substring(4), object.toString()));
                }
            }
        }
        String content = getURLContent(URI_MAP.get("CRUDEvent"), "POST", params);
        ConfirmObject confirmObject = new Gson().fromJson(content, ConfirmObject.class);
        return confirmObject;
    }

    public static ConfirmObject updateEvent(CreateEventObject eventObject,String _id) throws IllegalAccessException, InvocationTargetException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Method[] methods = eventObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                Object object = method.invoke(eventObject);
                if (object != null) {
                    params.add(new NameValuePair(Character.toLowerCase(method.getName().charAt(3))+method.getName().substring(4), object.toString()));
                }
            }
        }
        String content = getURLContent(URI_MAP.get("CRUDEvent")+_id+"/", "PUT", params);
        //System.out.println("in updateEvent = "+content);
        ConfirmObject confirmObject = new Gson().fromJson(content, ConfirmObject.class);
        return confirmObject;
    }

    public static ConfirmObject deleteEvent(String email, String password,String _id) throws IllegalAccessException, InvocationTargetException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("email",email));
        params.add(new NameValuePair("password",password));
        String content = getURLContent(URI_MAP.get("CRUDEvent")+_id+"/", "DELETE", params);
        //System.out.println("in deleteEvent = "+content);
        ConfirmObject confirmObject = new Gson().fromJson(content, ConfirmObject.class);
        return confirmObject;
    }

    public static EventSearchObject retreiveAllEvents(String email, String password) throws IllegalAccessException, InvocationTargetException {
        String content = getURLContent(URI_MAP.get("CRUDEvent")+email+"/"+password, "GET", null);
        EventSearchObject eventsObject = new Gson().fromJson(content, EventSearchObject.class);
        //System.out.println("Result of retrieveAll eventObject = "+eventsObject);
        return eventsObject;
    }

    public static EventSearchPeriodObject retreiveEventsWithinPeriod(Long startDate, Long endDate, String email, String password) throws IllegalAccessException, InvocationTargetException {
        String content = getURLContent(URI_MAP.get("getPeriodEvents")+startDate+"/"+endDate+"/"+email+"/"+password, "GET", null);
        EventSearchPeriodObject eventsObject = new Gson().fromJson(content, EventSearchPeriodObject.class);
        return eventsObject;
    }
}