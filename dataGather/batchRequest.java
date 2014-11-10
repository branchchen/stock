import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class batchRequest implements Runnable {
	String url;
	Map<String, stockInfo> infos;
	int id;
	String rawContent;
	
	public batchRequest (ArrayList<String> stocks, ArrayList<String> tags, int index) {
		urlBuilder urlB = new urlBuilder(stocks, tags);
    	url = urlB.buildUrl();
    	infos = new TreeMap<String, stockInfo>();
    	id = index;
	}
	
	@Override
	public void run() {		
		// Http client
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody;
    
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
			rawContent = responseBody;
			String[] infoStrs = responseBody.split("[\n]");
	        for (int i = 0; i < infoStrs.length; ++i) {
	    	    stockInfo cur = new stockInfo(infoStrs[i]);
	    	    infos.put(cur.symbol, cur);
	        }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public Map<String, stockInfo> getResult() {
		return infos;
	}
}
