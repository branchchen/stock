import java.util.ArrayList;
import java.util.List;

/**
 * Build request URL for a stock request
 * 
 * @author Chen Huang
 */

public class urlBuilder {
	
	final String prefix = "http://quote.yahoo.com/d/quotes.csv?s=";
    List<String> stockSymbols = new ArrayList<String>();
	List<String> tags = new ArrayList<String>();
    	
	public urlBuilder() {}
	
	public urlBuilder(List<String> symbols, List<String> tags) {
		this.stockSymbols = symbols;
		this.tags = tags;	
	}
	
	public String buildUrl() {
		StringBuilder ssBuilder = new StringBuilder();
		for (String s : stockSymbols) {
			ssBuilder.append(s);
			ssBuilder.append('+');
		}
		String stockStr = ssBuilder.substring(0, ssBuilder.length() - 1);
		
		StringBuilder tsBuilder = new StringBuilder();
	    for (String s : tags) {
	    	tsBuilder.append(s);
	    }
		String tagString = tsBuilder.toString();
		
		String url = prefix + stockStr + "&f=" + tagString;
		return url;
	}
}
