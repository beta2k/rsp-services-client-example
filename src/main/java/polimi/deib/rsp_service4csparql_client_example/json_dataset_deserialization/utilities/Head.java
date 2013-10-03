package polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Head {
	
	@Expose private ArrayList<String> vars;

	public ArrayList<String> getVars() {
		return vars;
	}

	public void setVars(ArrayList<String> vars) {
		this.vars = vars;
	}
	

}
