package home.app.beans;

import java.util.Date;

import org.json.JSONObject;

import pl.zientarski.SchemaMapper;

public class CustomerContract {

	private int customerId;
	private int contractId;
	private Date startDate;
	private String contractType;
	private Double revenue;

	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getContractId() {
		return contractId;
	}
	public void setContractId(int contractId) {
		this.contractId = contractId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	
	public String toJson4() {		

		JSONObject jsonObject = new SchemaMapper().toJsonSchema4(this.getClass(), true);
		jsonObject.put("customer_id", this.getCustomerId());
		jsonObject.put("customer_id", this.getContractId());
		jsonObject.put("start_date", this.getStartDate());
		jsonObject.put("type", this.getStartDate());
		jsonObject.put("revenue", this.getRevenue());
		return jsonObject.toString(4);
	}
}
