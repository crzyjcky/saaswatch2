package edu.sjsu.comp295b.probe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.dto.ConnectionDTO;

public class ConnectionProbe {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectionProbe.class);
	
	private HttpClient httpClient = new DefaultHttpClient();
	
	public List<ConnectionDTO> probe(List<String> connectionURLs) {
		
		List<ConnectionDTO> connectionDTOs = new ArrayList<ConnectionDTO>();
		for (String connectionURL : connectionURLs) {
			
			ConnectionDTO connectionDTO = new ConnectionDTO();
			connectionDTO.url = connectionURL;
			connectionDTO.isUp = false;
			connectionDTO.timestamp = System.currentTimeMillis();
			
			HttpGet httpGet = new HttpGet(connectionURL);
			
			try {
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				// as long as status code is not 5xx, it is consider up
				if ((httpResponse.getStatusLine().getStatusCode() / 100) != 5) {
					
					connectionDTO.isUp = true;
				}
				EntityUtils.consume(httpEntity);
			} catch (ClientProtocolException e) {
	
				logger.error("probe", e);
			} catch (IOException e) {

				// consume this error as this mean isUp = false;
				// logger.error("probe", e);
			}
			
			connectionDTOs.add(connectionDTO);
		}
		
		return connectionDTOs;
	}
	
	

}
