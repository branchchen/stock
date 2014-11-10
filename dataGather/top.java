import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class top {
	private static long timeout = 10;
	private static TimeUnit unit = TimeUnit.MINUTES;
	private static int maxThreadCount = 50;
	    	
    public final static void main(String[] args) throws Exception {
    	String [] file = {"nasdaq.csv", 
    			          "amex.csv", 
    			          "nyse.csv"};

    	long startTime = System.currentTimeMillis();
    	ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadCount);
    	for (String marketStr : file) {
    		threadPool.submit(new market(marketStr));
    	}
    	
    	threadPool.shutdown();
    	threadPool.awaitTermination(timeout, unit);
    	long endTime = System.currentTimeMillis();
    	System.out.println("All done, total time(ms): " + (endTime - startTime)); 
    }
}
