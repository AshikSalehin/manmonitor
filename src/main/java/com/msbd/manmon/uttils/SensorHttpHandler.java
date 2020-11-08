
package com.msbd.manmon.uttils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class SensorHttpHandler {

    public static String fetchDataFromControllerOnHttpGet(String getEndPoint) throws Exception{

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.2.105:8097/"+getEndPoint))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response.body();
    }
    
    public static String fetchNoiseInfoFromControllerOnHttpGet(String getEndPoint) throws Exception{

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.2.105:8097/"+getEndPoint))
                .build();

        HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        
        return response.body();
    }
   
}
