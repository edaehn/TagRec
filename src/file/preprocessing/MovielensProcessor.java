package file.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovielensProcessor {

	public static boolean processFile(String inputFile, String outputFile) {
		try {
			FileReader reader = new FileReader(new File("./data/csv/ml_core/" + inputFile));
			FileWriter writer = new FileWriter(new File("./data/csv/ml_core/" + outputFile + ".txt"));
			BufferedReader br = new BufferedReader(reader);
			BufferedWriter bw = new BufferedWriter(writer);
			String line = null;
			String resID = "", userHash = "", timestamp = "";
			List<String> tags = new ArrayList<String>();
			
			while ((line = br.readLine()) != null) {
				String[] lineParts = line.split("::");
				String tag = lineParts[2];
				//if (!filter || (!tag.contains("no-tag") && !tag.contains("-import"))) {
					if (!resID.isEmpty() && !userHash.isEmpty() && (!resID.equals(lineParts[1]) || !userHash.equals(lineParts[0]))) {
						writeLine(bw, resID, userHash, timestamp, tags);
						tags.clear();
					}
					resID = lineParts[1];
					userHash = lineParts[0];
					timestamp = lineParts[3];
					tags.add(tag);
				//}
			}
			writeLine(bw, resID, userHash, timestamp, tags);
			
			br.close();
			bw.flush();
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean writeLine(BufferedWriter bw, String resID, String userHash, String timestamp, List<String> tags) {
		try {
			String tagString = "";
			for (String tag : tags) {
				tagString += (tag + ",");
			}
			tagString = tagString.length() > 0 ? tagString.substring(0, tagString.length() - 1) : "";
			
			bw.write("\"" + userHash + "\";\"" + resID + "\";\"" + timestamp + "\";\"" + tagString + "\";\"\"\n");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}