package polimi.deib.rsp_service4csparql_client_example.json_dataset_deserialization.utilities;

import com.google.gson.annotations.Expose;

public class Variable {
	
	@Expose private String datatype;
	@Expose private String type;
	@Expose private String value;
	
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
