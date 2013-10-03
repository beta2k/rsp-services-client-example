package polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.annotations.Expose;

public class Result {
	
	@Expose private ArrayList<Map<String, Variable>> bindings;

	public ArrayList<Map<String, Variable>> getBindings() {
		return bindings;
	}

	public void setBindings(ArrayList<Map<String, Variable>> bindings) {
		this.bindings = bindings;
	}
	
}
