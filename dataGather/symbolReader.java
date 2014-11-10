import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class symbolReader {
	ArrayList<ArrayList<String> > stockBatches = new ArrayList<ArrayList<String> >();
	private static final int batchSize = 50;
	String fileStr;
	
	// tags
	static public ArrayList<String> tags = new ArrayList<String>();
	static {
		// price volumn info: 8
		tags.add("s");
		tags.add("l1");
		tags.add("d1");
		tags.add("c1");
		tags.add("v");
		tags.add("a2");
		tags.add("g");
		tags.add("h");
		
		// FA: 16
		tags.add("d");
		tags.add("e");
		tags.add("e7");
		tags.add("e8");
		tags.add("j");
		tags.add("k");
		tags.add("j1");
		tags.add("j4");
		tags.add("m3");
		tags.add("m4");
		tags.add("p5");
		tags.add("p6");
		tags.add("r");
		tags.add("r5");
		tags.add("s7");
		tags.add("t8");
	}
	
	boolean validSymbol(String a) {
		for (int i = 0; i < a.length(); ++i) {
			char cur = a.charAt(i);
			if ((cur >= 'a' && cur <= 'z') || (cur >= 'A' && cur <= 'Z')) {
				continue;
			}
			return false;
		}
		return true;
	}	
	
	public symbolReader(String f) {
		fileStr = f;
	}
	
	public void execute () {	
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(fileStr));
			file.readLine();  // remove first one
			String l;
			int count = 0;
			stockBatches.add(new ArrayList<String>());		
			while ((l = file.readLine()) != null) {
				String [] list = l.split("[,]");
				String temp = list[0];
				String s = temp.replaceAll("[\" ]", ""); // trim "
				if (!validSymbol(s)) {
					continue;
				}
			    			
				if (count == batchSize) {
					stockBatches.add(new ArrayList<String>());
					count = 0;
				} else {
					stockBatches.get(stockBatches.size() - 1).add(s);
					++count;
				}
	   		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 		
			 if (file != null) {
	             try {
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
		}
	}
	
	ArrayList<ArrayList<String> > getBatches() {
		return stockBatches;
	}
}
