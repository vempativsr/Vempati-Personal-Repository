package home.app.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import home.app.service.CustomerService;
import home.app.beans.Customer;
import home.app.beans.CustomerContract;

@Path("/customerservice")
public class CustomerController {
	
	CustomerService customerService = new CustomerService();
	
	// Returns all contracts of the given customer
	@GET
	@Path("/customer/{customer_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCustomerContracts(@QueryParam("$customer_id") int customer_id)
	{
		String customerJson = "";
		String contractsJson = "";
		String custContractsJson = "";
		
		Customer customer = customerService.getCustomer(customer_id);
		CustomerContract custContracts [] = null;
		
		custContracts = (CustomerContract[]) customerService.getContarcts(customer_id).toArray();
		
		if (custContracts != null)
		{
			try
			{
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				
				for (int i = 0; i < custContracts.length; i++)
					contractsJson = contractsJson + custContracts[i].toJson4();
					
				customerJson = ow.writeValueAsString(customer);
				System.out.println("customerJson: " + customerJson + contractsJson);
				
				custContractsJson = customerJson + contractsJson;
			}
			catch(JsonProcessingException jpe)
			{
				return "Exception occured during Json processing...";
			}
		}
		
		return custContractsJson;
	}
	
	// Creates a new customer entry
	@POST
	@Path("/customer/{customer_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String putCustomer(Customer customer)
	{
		boolean res = customerService.putCustomer(customer);
		
		if (res)
		{
			return "Successfully added !!!";
		}
		else
			return "Customer exists ...";
	}

	// Creates a new contract entry
	@POST
	@Path("/contract/{contract_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addContract(CustomerContract customerContract)
	{
		boolean res = customerService.addContract(customerContract);
		
		if (res)
			return "Successfully added !!!";
		else
			return "Customer Contract combination exists !!!";
	}
	
	// Returns sum of revenues of all contracts of the customer
	@GET
	@Path("/contract/{customer_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCustomerRevenue(@QueryParam("$customer_id") int customer_id)
	{
		Double totRevenue = null;
		String customerJson = "";
		String customerRevenueJson = "";
		
		CustomerContract custContracts [] = null;
		
		Customer customer = customerService.getCustomer(customer_id);
		
		if (customer != null)
		{
			custContracts = (CustomerContract[]) customerService.getContarcts(customer_id).toArray();
			
			if (custContracts != null)
			{
				for (int i = 0; i < custContracts.length; i++)
						totRevenue = totRevenue + custContracts[i].getRevenue();
			}
			
			try
			{
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				customerJson = ow.writeValueAsString(customer);
				customerRevenueJson = customerJson + "\n" + "Total revenue = " + totRevenue;
				System.out.println("Json Object: " + customerJson);
			}
			catch(JsonProcessingException jpe)
			{
				return "Exception occured during Json processing...";
			}
		}
		
		return customerRevenueJson;
	}
	
	// Returns sum of revenues of all contracts of the specified type
	@GET
	@Path("/contract/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRevenueTypeSum(@QueryParam("$type") String type)
	{
		Double totRevenue = null;
		String totRevTypeJson = "";
		
		CustomerContract custContracts [] = null;
		
		custContracts = (CustomerContract[]) customerService.getContarcts(type).toArray();
			
			if (custContracts != null)
			{
				for (int i = 0; i < custContracts.length; i++)
						totRevenue = totRevenue + custContracts[i].getRevenue();
			}

			totRevTypeJson = "{ Contract Type : " + type + ",\n" 
								+ "Total Revenue = " + totRevenue + "\n" + "}";
		
		return totRevTypeJson;
	}
}
