
package com.msbd.manmon.schedulers;

import com.google.gson.Gson;
import com.msbd.manmon.frontendmodels.NoiseValue;
import com.msbd.manmon.httphandler.HttpRequestHandlerServiceImp;
import com.msbd.manmon.uttils.SensorHttpHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoiseInfoFetchScheduler {
    
//    @Autowired
//    HttpRequestHandlerServiceImp httpRequestHandlerServiceImp;
    
    public static double tenSecondNoiseValue[] = {10.00,11.32,10.00,9.87,10.00};
    public static int i = 0;
    
    //@Scheduled(fixedRate = 500)
    public void fetchNoiseSensorData() throws Exception {
	String noiseInfo = SensorHttpHandler.fetchDataFromControllerOnHttpGet("noiseinfo");
	NoiseValue noiseValue = new NoiseValue();
	Gson gson = new Gson();
	noiseValue = gson.fromJson(noiseInfo, NoiseValue.class);
	tenSecondNoiseValue[i] = noiseValue.getNoiseValue();
	i++;
    }
    
    public static double[] noiseValueList(){
	double noiseValueList[] = tenSecondNoiseValue;
	i = 0;
	return noiseValueList;
    }
}
