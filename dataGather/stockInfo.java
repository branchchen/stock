import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Convert a String into a Stock object
 * 
 * @author Chen Huang
 *
 */
public class stockInfo implements Comparable<stockInfo> {

	static final int numOfField = 24;
	static final String naStr = "-999999.0";
	static final double na = -999999.0;
	
	// basic info
	public String symbol;
	public Double price;
	public Date date = new Date();
	public Double change;
	public Double vol;
	public Double avgVol;
	public Double dayLow;
	public Double dayHigh;
	
	// FA
	public Double divPerShare;
	public Double earnPerShare;
	public Double earnPerShareCur;
	public Double earnPerShareNext;
	public Double low52;
	public Double high52;
	public Double marketCap;
	public Double ebitda;
	public Double moving50;
	public Double moving200;
	public Double pricePerSale;
	public Double pricePerBook;
	public Double pe;
	public Double peg;
	public Double shortRatio;
	public Double targetPrice;
	
	public boolean naFound = false;
	
	private Double parseCapStr(String a) {
		if (a.compareTo("0") == 0) {
			return 0.0;
		}
		if (a.isEmpty()) {
			return null;			
		}
		String num = a.substring(0, a.length() - 1);
		char scale = a.charAt(a.length() - 1);
		
		double ret = Double.parseDouble(num);
	   
		if (scale == 'K') {
	      ret *= 1000.0;	
		} else if (scale == 'M') {
			ret *= 1000000.0;
		} else if (scale == 'B') {
			ret *= 1000000000.0;
		} else if (scale == 'T') {
			ret *= 1000000000000.0;
		} else {
			System.out.println("Wrong data: ");
			System.out.println(a);
			return 0.0;
		}
	    return ret;
	}
	
	// constructor
	public stockInfo(String a) {
		String t1 = a.replaceAll("[\"]", ""); // trim "
	//	String temp = t1.replace("N/A", "");    // "N/A -> naStr"
	//	naFound = temp.compareTo(t1) != 0;
		
		String delim = "[,]";
		String[] tokens = t1.split(delim);
		
		if (tokens.length != numOfField) {
			System.out.println("field number dismatch");
			return;
		}
		symbol = tokens[0];
		price = Double.parseDouble(tokens[1]);
		
		SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy");
		try {
			if (tokens[2].equals("N/A")) {
				date = new Date();
			} else {
				date = format.parse(tokens[2]);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!tokens[3].equals("N/A"))
		    change = Double.parseDouble(tokens[3]);
		if (!tokens[4].equals("N/A"))
		    vol = Double.parseDouble(tokens[4]);
		if (!tokens[5].equals("N/A"))
		    avgVol = Double.parseDouble(tokens[5]);
		if (!tokens[6].equals("N/A"))
		    dayLow = Double.parseDouble(tokens[6]);
		if (!tokens[7].equals("N/A"))
		    dayHigh = Double.parseDouble(tokens[7]);
		
		// FA
		if (!tokens[8].equals("N/A"))
		    divPerShare = Double.parseDouble(tokens[8]);
		if (!tokens[9].equals("N/A"))
		    earnPerShare = Double.parseDouble(tokens[9]);
		if (!tokens[10].equals("N/A"))
		    earnPerShareCur = Double.parseDouble(tokens[10]);
		if (!tokens[11].equals("N/A"))
		    earnPerShareNext = Double.parseDouble(tokens[11]);
		if (!tokens[12].equals("N/A"))
		    low52 = Double.parseDouble(tokens[12]);
		if (!tokens[13].equals("N/A"))
		    high52 = Double.parseDouble(tokens[13]);
		if (!tokens[14].equals("N/A"))
		    marketCap = parseCapStr(tokens[14]);
		if (!tokens[15].equals("N/A"))
		    ebitda = parseCapStr(tokens[15]);
		if (!tokens[16].equals("N/A"))
		    moving50 = Double.parseDouble(tokens[16]);
		if (!tokens[17].equals("N/A"))
		    moving200 = Double.parseDouble(tokens[17]);
		if (!tokens[18].equals("N/A"))
		    pricePerSale = Double.parseDouble(tokens[18]);
		if (!tokens[19].equals("N/A"))
		    pricePerBook = Double.parseDouble(tokens[19]);
		if (!tokens[20].equals("N/A"))
		    pe = Double.parseDouble(tokens[20]);
		if (!tokens[21].equals("N/A"))
		    peg = Double.parseDouble(tokens[21]);
		if (!tokens[22].equals("N/A"))
		    shortRatio = Double.parseDouble(tokens[22]);
		if (!tokens[23].contains("N/A"))
		    targetPrice = Double.parseDouble(tokens[23]);
	}

	@Override
	public int compareTo(stockInfo o) {
		return this.symbol.compareTo(((stockInfo) o).symbol);
	}

}
