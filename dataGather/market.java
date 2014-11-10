import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class market implements Runnable {
	private static final String dir = "F:\\dropbox\\Dropbox\\Finance\\stock_project\\";
	private static final String outPrefix = "F:\\dropbox\\Dropbox\\Finance\\stock_project\\data\\";
	
	private static long timeout = 10;
	private static TimeUnit unit = TimeUnit.MINUTES;
	private static int maxThreadCount = 50;
	
	private String fStr;
	private String marketPrefix;
	
	public market(String file) {
		fStr = dir + file;
		int pos = file.indexOf('.');
		marketPrefix = file.substring(0, pos);
	}
	
	public void run() {
		// Step 1: Get stock id and tag batch
		symbolReader sReader = new symbolReader(fStr);
    	sReader.execute();
    	ArrayList<ArrayList<String> > stockBatches = sReader.getBatches();
    	ArrayList<String> tags = symbolReader.tags;
    	
    	// Step 2: Making requests
    	ArrayList<batchRequest> requests = new ArrayList<batchRequest>();
    	ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadCount);
    	
    	for (int i = 0; i < stockBatches.size(); ++i) {
    		if (stockBatches.get(i).isEmpty()) {
    			continue;
    		}
    		
    		batchRequest req = new batchRequest(stockBatches.get(i), tags, i);
    		requests.add(req);
    		threadPool.submit(req);
    	}
    	System.out.println(marketPrefix + " http request sent");
    	
    	threadPool.shutdown();
    	try {
			threadPool.awaitTermination(timeout, unit);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	System.out.println(marketPrefix + " http request done");
    	
    	// Step 3: request finish, now gather results
    	Map<String, stockInfo> result = new TreeMap<String, stockInfo>();
    	String content = "";
    	for (batchRequest req : requests) {
    		result.putAll(req.getResult());
    		content += req.rawContent;
    	}
    	System.out.println(marketPrefix + " all finish: " + result.size());
    	
    	// Step 4:  output to a file
    	Date today = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat("_yyyy_M_d");
    	String outFile = outPrefix + marketPrefix + formatter.format(today) + ".txt";
    	    	
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
    		out.write(content);
    		out.close();
    	} catch (IOException e) { 
    		System.out.println("Exception ");
    	}
    	
    	// Step 5: inject into DB
    	dbWriter.writeIntoDB(result);
	}

}
