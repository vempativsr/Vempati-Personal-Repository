package home.app.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import home.app.beans.Customer;
import home.app.beans.CustomerContract;
import jersey.repackaged.com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;
import net.spy.memcached.MemcachedClient;

public class CustomerService {

	ConcurrentHashMap<String, Object> customerMap = new ConcurrentHashMap<String, Object>();
	// Define a variable of type MultiMap from Guava or some implementation say contractMap
	// Define a variable of type MultiMap from Guava or some implementation say contractTypeMap
	
	int mcPort = 11211;
	MemcachedClient mc = null;
	
	public CustomerService()
	{
		// Start Memcached client
		startMC();
	}
	
	public Customer getCustomer(int customerId)
	{
		return (Customer)customerMap.get("" + customerId);
	}
	
	public boolean putCustomer(Customer customer)
	{
		boolean res = false;
		
		if(customerMap.get("" + customer.getCustomerId()) != null)
		{
			customerMap.put("" + customer.getCustomerId(), customer);
			// Successfully added
			res = true;
		}
		
		return res;
	}
	
	public Collection getContarcts(int customerId)
	{
		Collection contracts = null;
		
		// Return the collection from Multimap
		//contract =  contractMap.get("" + customerId);
		
		return contracts;
	}
	
	public Collection getContarcts(String type)
	{
		Collection contracts = null;
		
		// Return the collection from Multimap
		//contract =  contractMap.get("" + customerId);
		
		return contracts;
	}
	
	public boolean addContract(CustomerContract customerContract)
	{
		boolean res = false;
		
		// Multimap allows multiple values with the same key
		// Add the contract to the contract map by customer
		//res = contractMap.put("" + customerContract.getCustomerId(), customerContract);
		
		// Multimap allows multiple values with the same key
		// Add the contract to the contract map by type
		//res2 = contractTypeMap.put("" + customerContract.getContractType(), customerContract);
		
		return res;
	}
	
	// Start Memcached client
	public void startMC()
	{
		try
		{
			mc = new MemcachedClient(new InetSocketAddress("127.0.0.1", mcPort));
			System.out.println("Connection to Memcached server is Successful.");
		}
		catch(UnknownHostException uhe)
		{
			System.out.println("Unknown Host Exception. Exiting...");
			uhe.printStackTrace();
			System.exit(1);
		}
		catch(IOException ioe)
		{
			System.out.println("Unexpected IO Exception. Exiting...");
			ioe.printStackTrace();
			System.exit(1);
		}
	}
	
	// Terminate Memcached client
	@PreDestroy
	public void destroy()
	{
		mc.shutdown();
		System.out.println("Memcached client has been shutdown !!!");
	}
}
