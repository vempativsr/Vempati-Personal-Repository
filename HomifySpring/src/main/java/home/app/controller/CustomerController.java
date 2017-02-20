package home.app.controller;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import home.app.service.CustomerService;
import home.app.beans.Customer;
import home.app.beans.CustomerContract;

@RestController
@RequestMapping("/customerservice")
public class CustomerController {
	
	CustomerService customerService = new CustomerService();
	
	// Returns Customer and all contracts of the given customer
	@RequestMapping(method = RequestMethod.GET, value = "/customer/{customer_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCustomerContracts(@PathVariable("customer_id") Integer customer_id)
	{
		String custContractsJson = "";
		
		System.out.println("Customer ID = " + customer_id);
		
		try
		{
			String customerJson = "";
			String contractsJson = "";
			
			Customer customer = customerService.getCustomer(customer_id);
			
			if(customer != null)
			{
				System.out.println("Customer ID from DB = " + customer.getCustomerId());
	
				if (customerService.getContarcts(customer_id) != null)
				{
					Object custContractsObj[] = customerService.getContarcts(customer_id).toArray();
	
					try
					{
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						
						for(int i = 0; i < custContractsObj.length; i++)
						{
							contractsJson = contractsJson + ((CustomerContract)custContractsObj[i]).toJson4();
						}
							
						customerJson = ow.writeValueAsString(customer);
						System.out.println("customerJson: " + customerJson + contractsJson);
						
						custContractsJson = customerJson + contractsJson;
					}
					catch(JsonProcessingException jpe)
					{
						return "Exception occured during Json processing...";
					}
				}
				else
					custContractsJson = "No Contracts found for the customer: " + customer_id;
			}
			else
				custContractsJson = "Customer does not exist !!!";
		}
		catch(Exception e)
		{
			System.out.println("Exception while fetching Customer and Contracts...");
			e.printStackTrace();
		}
		
		return custContractsJson;
	}
	
	// Creates a new customer entry
	@RequestMapping(method = RequestMethod.POST, value = "/customer/{customer_id}")
	@Produces(MediaType.APPLICATION_JSON)
	//@Consumes("text/plain")
	@Consumes(MediaType.APPLICATION_JSON)
	//public String putCustomer(Map<String, Object> customerInput)
	public String putCustomer(@RequestBody Map<String, Object> customerInput)
	{
		String msg = "";
		
		try
		{
			LinkedHashMap custMap = (LinkedHashMap) customerInput.get("customer");
		
			if (custMap != null)
			{
				System.out.println("Key = " + custMap.keySet().iterator().next().toString());
				
				// Check if the specified customer exists
				Integer customer_id = ((Integer)custMap.get("customerId"));
				System.out.println("Customer ID = " + customer_id);
				Customer inCustomer = customerService.getCustomer(customer_id);
				
				// If customer does not exist, try to add
				if (inCustomer == null)
				{
					Customer customer = new Customer();
					
					customer.setCustomerId((Integer)custMap.get("customerId"));
					customer.setCustomerName(custMap.get("customerName").toString());
					customer.setCustomerEmail(custMap.get("customerEmail").toString());
		
					System.out.println("Customer ID = " + customer.getCustomerId());
					System.out.println("Customer Name = " + customer.getCustomerName());
					System.out.println("Customer Email = " + customer.getCustomerEmail());
					
					boolean res = customerService.putCustomer(customer);

					if (res)
					{
						msg = "Successfully added !!!";
					}
				}
				else
					msg = "Customer exists ...";
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception while adding customer ...");
			e.printStackTrace();
		}
		
		return msg;

	}

	// Creates a new contract entry
	@RequestMapping(method = RequestMethod.POST, value = "/contract/{contract_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addContract(@RequestBody Map<String, Object> contractInput)
	{
		String msg = "";
		
		try
		{
			LinkedHashMap custContractMap = (LinkedHashMap) contractInput.get("customercontract");
			
			if (custContractMap != null)
			{
				System.out.println("Key = " + custContractMap.keySet().iterator().next().toString());
				
				// Check if the specified customer exists
				Integer customer_id = ((Integer)custContractMap.get("customerId"));
				System.out.println("Customer ID = " + customer_id);
				Customer customer = customerService.getCustomer(customer_id);
				
				// If the customer exists, try to add contract
				if (customer != null)
				{
					CustomerContract customerContract = new CustomerContract();
					
					customerContract.setCustomerId((Integer)custContractMap.get("customerId"));
					customerContract.setContractId((Integer)custContractMap.get("contractId"));
					customerContract.setStartDate(new Date(custContractMap.get("startDate").toString()));
					customerContract.setContractType(custContractMap.get("contractType").toString());
					customerContract.setRevenue((Double)custContractMap.get("revenue"));
	
					System.out.println("Customer ID = " + customerContract.getCustomerId());
					System.out.println("Contract ID = " + customerContract.getContractId());
					System.out.println("Start Date = " + customerContract.getStartDate());
					System.out.println("Contract Type = " + customerContract.getContractType());
								
					boolean res = customerService.addContract(customerContract);
	
					//boolean res = false;
					if (res)
					{
						msg = "Contract added Successfully !!!";
					}
					else
						msg = "Contract exists ...";
				}
				else
					msg = "Customer does not exist ...";
	
			}
			else
				msg = "Error in Input ...";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return msg;
		
	}
	
	// Returns sum of revenues of all contracts of the customer
	@RequestMapping(method = RequestMethod.GET, value = "/custcontractsum/{customer_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCustomerRevenue(@PathVariable("customer_id") Integer customer_id)
	{
		String customerRevenueJson = "";
		System.out.println("Customer ID = " + customer_id);
		
		try
		{
			double totRevenue = 0.0;
			String customerJson = "";
		
			Customer customer = customerService.getCustomer(customer_id);
			
			if (customer != null)
			{
				System.out.println("Customer ID from DB = " + customer.getCustomerId());
				
				if (customerService.getContarcts(customer_id) != null)
				{
					Object custContractsObj[] = customerService.getContarcts(customer_id).toArray();
					
					if (custContractsObj != null)
					{
						for (int i = 0; i < custContractsObj.length; i++){
							if (i == 0)
								totRevenue = ((CustomerContract)custContractsObj[i]).getRevenue().doubleValue();
							else
								totRevenue = totRevenue + ((CustomerContract)custContractsObj[i]).getRevenue().doubleValue();
						}
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
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception while fetching revenue...");
			e.printStackTrace();
		}
		
		return customerRevenueJson;
	}
	
	// Returns sum of revenues of all contracts of the specified type
	@RequestMapping(method = RequestMethod.GET, value = "/contracttypesum/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRevenueTypeSum(@PathVariable("type") String type)
	{

		String totRevTypeJson = "";
		System.out.println("Contract Type = " + type);
		
		try
		{
			if (type != null)
			{
				double totRevenue = 0.0;
				
				if (customerService.getContarctsByType(type) != null)
				{
					Object custContractsObj[] = customerService.getContarctsByType(type).toArray();
					
					if (custContractsObj != null)
					{
						for (int i = 0; i < custContractsObj.length; i++){
							if (i == 0)
								totRevenue = ((CustomerContract)custContractsObj[i]).getRevenue().doubleValue();
							else
								totRevenue = totRevenue + ((CustomerContract)custContractsObj[i]).getRevenue().doubleValue();
						}
					}
					
					totRevTypeJson = "{\n   ContractType:" + type + ",\n"
									+ "   Total Revenue:" + totRevenue + "\n}";

				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception while fetching revenue...");
			e.printStackTrace();
		}
		
		return totRevTypeJson;
	}
}
