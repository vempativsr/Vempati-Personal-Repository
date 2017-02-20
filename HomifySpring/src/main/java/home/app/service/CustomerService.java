package home.app.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;

import home.app.beans.Customer;
import home.app.beans.CustomerContract;


import java.util.Collection;
import java.util.Map;
import net.spy.memcached.MemcachedClient;

public class CustomerService implements DisposableBean {

	ConcurrentHashMap<String, Object> customerMap = null;
	
	// Define a variable of type MultiMap from Guava or some implementation say contractMap
	HashMultimap<String, Object> contractMap = null;
	
	// Define a variable of type MultiMap from Guava or some implementation say contractTypeMap
	HashMultimap<String, Object> contractTypeMap = null;
	
	int mcPort = 11211;
	MemcachedClient mc = null;
	
	public CustomerService()
	{
		// Start Memcached client
		//startMC();
		customerMap = new ConcurrentHashMap<String, Object>();
		contractMap = HashMultimap.create();
		contractTypeMap = HashMultimap.create();
	}
	
	public Customer getCustomer(int customerId)
	{
		return (Customer) customerMap.get("" + customerId);
	}
	
	public boolean putCustomer(Customer customer)
	{
		boolean res = false;
		System.out.println("Customer Id from customerMap = " + customerMap.get("" + customer.getCustomerId()));
		
		if(customerMap.get("" + customer.getCustomerId()) == null)
		{
			customerMap.put("" + customer.getCustomerId(), customer);
			// Successfully added
			res = true;
			System.out.println("Customer added successfully...");
		}
		
		return res;
	}
	
	public Collection getContarcts(int customerId)
	{
		Collection contracts = null;
		
		// Return the collection from Multimap
		if (contractMap.containsKey("" + customerId))
			contracts =  contractMap.get("" + customerId);
		
		return contracts;
	}
	
	public Collection getContarctsByType(String type)
	{
		Collection contracts = null;
		
		// Return the collection from Multimap
		contracts =  contractTypeMap.get("" + type);
		
		return contracts;
	}
	
	public boolean addContract(CustomerContract customerContract)
	{
		boolean res = false;
		boolean res2 = false;
		
		// Multimap allows multiple values with the same key
		// Add the contract to the contract map by customer
		res = contractMap.put("" + customerContract.getCustomerId(), customerContract);
		System.out.println("Contract added successfully to contract map!!!");
		
		// Multimap allows multiple values with the same key
		// Add the contract to the contract map by type
		res2 = contractTypeMap.put("" + customerContract.getContractType(), customerContract);
		System.out.println("Contract added successfully to contract type map!!!");
		
		return (res & res2);
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
