package polimi.deib.rsp_service4csparql_client_example.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class Utilities {

	private static Logger logger = LoggerFactory.getLogger(Utilities.class.getName()); 
	
	public static void writeJsonToFile(String filePath, Gson gson, Object obj){

		try {
			File file = new File(filePath);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			gson.toJson(obj, bw);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			logger.error("IO Exception occurred", e);
		}

	}

}
