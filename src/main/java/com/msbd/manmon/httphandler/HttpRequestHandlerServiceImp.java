
package com.msbd.manmon.httphandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequestHandlerServiceImp implements HttpRequestHandlerService{

    @Override
    public String sendHttpGetToSensorController(String getEndPoint) throws Exception{
	HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.2.105:8097/"+getEndPoint))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response.body();
    }
    
}
