package home.app.beans;

import org.json.JSONObject;

import pl.zientarski.SchemaMapper;

public class Customer {

	private int customerId;
	private String customerName;
	private String customerEmail;
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
	public String toJson4() {		

		JSONObject jsonObject = new SchemaMapper().toJsonSchema4(this.getClass(), true);
		jsonObject.put("customer_id", this.getCustomerId());
		jsonObject.put("customer_name", this.getCustomerName());
		jsonObject.put("customer_email", this.getCustomerEmail());
		return jsonObject.toString(4);
	}
}
