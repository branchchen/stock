import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class dbWriter {
	private static String url = "jdbc:mysql://localhost:3306/stock";
	private static String user = "root";
	private static String password = "cdbr8485";
	private static Connection con = null;
	
	static void writeIntoDB(Map<String, stockInfo> stocks) {
		try {
			con = DriverManager.getConnection(url, user, password);
			storeRecords(con, stocks);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void storeRecords(Connection con, Map<String, stockInfo> stocks) throws SQLException {
    	String insertStr = "INSERT INTO stockinfo "
    			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
    	
    	PreparedStatement pst = con.prepareStatement(insertStr);
    	int i = 0;
    	for (stockInfo info : stocks.values()) {
			if (info.symbol != null)           pst.setString(1, info.symbol);
			if (info.date != null)             pst.setDate(2, new java.sql.Date(info.date.getTime()));
			if (info.price != null)            pst.setDouble(3, info.price);             else pst.setNull(3, java.sql.Types.DOUBLE);
			if (info.change != null)           pst.setDouble(4, info.change);            else pst.setNull(4, java.sql.Types.DOUBLE);
			if (info.vol != null)              pst.setDouble(5, info.vol);               else pst.setNull(5, java.sql.Types.DOUBLE);
			if (info.avgVol != null)           pst.setDouble(6, info.avgVol);            else pst.setNull(6, java.sql.Types.DOUBLE);
			if (info.dayLow != null)           pst.setDouble(7, info.dayLow);            else pst.setNull(7, java.sql.Types.DOUBLE);
			if (info.dayHigh != null)          pst.setDouble(8, info.dayHigh);           else pst.setNull(8, java.sql.Types.DOUBLE);
			if (info.divPerShare != null)      pst.setDouble(9, info.divPerShare);       else pst.setNull(9, java.sql.Types.DOUBLE);
			if (info.earnPerShare != null)     pst.setDouble(10, info.earnPerShare);     else pst.setNull(10, java.sql.Types.DOUBLE);
			if (info.earnPerShareCur != null)  pst.setDouble(11, info.earnPerShareCur);  else pst.setNull(11, java.sql.Types.DOUBLE);
			if (info.earnPerShareNext != null) pst.setDouble(12, info.earnPerShareNext); else pst.setNull(12, java.sql.Types.DOUBLE);
			if (info.low52 != null)            pst.setDouble(13, info.low52);            else pst.setNull(13, java.sql.Types.DOUBLE);
			if (info.high52 != null)           pst.setDouble(14, info.high52);           else pst.setNull(14, java.sql.Types.DOUBLE);
			if (info.marketCap != null)        pst.setDouble(15, info.marketCap);        else pst.setNull(15, java.sql.Types.DOUBLE);
			if (info.ebitda != null)           pst.setDouble(16, info.ebitda);           else pst.setNull(16, java.sql.Types.DOUBLE);
			if (info.moving50 != null)         pst.setDouble(17, info.moving50);         else pst.setNull(17, java.sql.Types.DOUBLE);
			if (info.moving200 != null)        pst.setDouble(18, info.moving200);        else pst.setNull(18, java.sql.Types.DOUBLE);
			if (info.pricePerSale != null)     pst.setDouble(19, info.pricePerSale);     else pst.setNull(19, java.sql.Types.DOUBLE);
			if (info.pricePerBook != null)     pst.setDouble(20, info.pricePerBook);     else pst.setNull(20, java.sql.Types.DOUBLE);
			if (info.pe != null)               pst.setDouble(21, info.pe);               else pst.setNull(21, java.sql.Types.DOUBLE);
			if (info.peg != null)              pst.setDouble(22, info.peg);              else pst.setNull(22, java.sql.Types.DOUBLE);
			if (info.shortRatio != null)       pst.setDouble(23, info.shortRatio);       else pst.setNull(23, java.sql.Types.DOUBLE);
			if (info.targetPrice != null)      pst.setDouble(24, info.targetPrice);      else pst.setNull(24, java.sql.Types.DOUBLE);
			
			pst.addBatch();
			++i;
            if ((i + 1) % 1000 == 0) {
            	batchInsert(pst);
            }
		}
    	batchInsert(pst);
    }
	
	private static void batchInsert(PreparedStatement pst) {
		try {
	        pst.executeBatch(); // Execute every 1000 items.
	        System.out.println("batch insert done.");  
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	@SuppressWarnings("deprecation")
	public static void backfill() throws IOException {
		String dir = "F:\\dropbox\\Dropbox\\Finance\\stock_project\\data\\";
    	File[] files = new File(dir).listFiles();	
    	Map<String, stockInfo> stocks = new HashMap<String, stockInfo>();
    	
    	for (File file : files) {
    		String fileStr = dir + file.getName();
    		BufferedReader fileBuffer = new BufferedReader(new FileReader(fileStr));
    		String l;
    		
    		while ((l = fileBuffer.readLine()) != null) {
    			stockInfo stock = new stockInfo(l);
    			String key = stock.symbol + stock.date.toGMTString();
    			stocks.put(key, stock);
    		}
    	}
    	System.out.println("Total record size: " + stocks.size());
    	
    	writeIntoDB(stocks);
    }
}
