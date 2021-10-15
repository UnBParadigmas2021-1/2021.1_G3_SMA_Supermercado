package dados;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import  java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Api {
	
	private static HttpURLConnection connection;
	
	public Api() {}
	
	public void get() throws ProtocolException {
		try {
			
			URL url = new URL("https://api.instabuy.com.br/apiv3/layout?subdomain=bigboxdelivery");
			
			BufferedReader reader;
			String line;
			StringBuffer response = new StringBuffer();
			
			connection = (HttpURLConnection) url.openConnection();
			
			// Request Setup
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			
			System.out.println("Status: " + status);
			
			if(status == 200) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
				
			//System.out.println(response.toString());
			parse(response.toString());
			
			
			
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			connection.disconnect();
		}

	}
	
	
	public static String parse(String responseBody) {
		
		JSONObject responseParsed = new JSONObject(responseBody);
		
		
		JSONObject data = responseParsed.getJSONObject("data");
		
		JSONArray promo = data.getJSONArray("promo");
		
		for(int i = 0; i < promo.length(); i++) {
			JSONObject product = promo.getJSONObject(i);
			
			// Nome
			String name = product.getString("name");
			
			// Imagem
			JSONArray images = product.getJSONArray("images");
			String image = images.getString(0);
			
			// Preço
			JSONArray prices = product.getJSONArray("prices");
			JSONObject info = prices.getJSONObject(0);
			float price = info.getFloat("price");
			
			System.out.println("Nome: " + name + "Preço: " + price + "Image: " +  image);
		}
		
		
		return null;
	}

}
