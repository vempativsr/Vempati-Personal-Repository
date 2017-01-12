package com.goeuro.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import java.util.HashMap;
import java.util.Scanner;

import com.goeuro.beans.DirectRoute;

import net.spy.memcached.MemcachedClient;

public class BusRouteService {
	
	int busRoutes = 0;
	
	int mcPort = 11211;
	MemcachedClient mc = null;

	int routeIDPos = 0;
	HashMap<String, String> routeIDs = new HashMap<String, String>();
//	ConcurrentHashMap<String, String> routeMap = new ConcurrentHashMap<String, String>();

	public BusRouteService()
	{
//		String busRouteFile = args[0];
		String busRouteFile = "assets\\BusRouteFile.txt";
		
		if (busRouteFile.isEmpty())
		{
			System.out.println("Bus route file not supplied. Exiting...");
			System.exit(1);
		}
		
		try
		{
			this.readBusRouteFile(busRouteFile);
		}
		catch (FileNotFoundException fne)
		{
			System.out.println("Bus Route File not found. Exiting...");
			fne.printStackTrace();
			System.exit(1);
		}
		catch (IOException ioe)
		{
			System.out.println("Unexpected IO Exception. Exiting...");
			ioe.printStackTrace();
			System.exit(1);
		}
	}
	
	private void readBusRouteFile(String busRouteFile) throws IOException, FileNotFoundException
	{
		Scanner lineReader = new Scanner(new File(busRouteFile));
		int nRoutes = Integer.parseInt(lineReader.nextLine());
		System.out.println("Number of Routes = " + nRoutes);
		setBusRoutes(nRoutes);
		
		// Start Memcached client
		startMC();
		
		while (lineReader.hasNextLine())
		{
			loadBusRouteLine(lineReader.nextLine());
		}
		
		lineReader.close();		
	}
	
	private void setBusRoutes(int nRoutes)
	{
		this.busRoutes = nRoutes;
	}
	
	private void loadBusRouteLine(String busRouteLine) throws IOException
	{
		System.out.println("\nRoute Line = " + busRouteLine);
		
		String arrRouteLine[] = busRouteLine.split(" ");
		
		String routeID = arrRouteLine[0];
		routeIDPos++;
		routeIDs.put("" + routeIDPos, arrRouteLine[0]);
		System.out.println("Bus Route ID = " + routeID + " has been added");
		
		for (int i = 1; i < (arrRouteLine.length - 1); i++)
		{
			String source = "";
			String dest = "";
			
			source = arrRouteLine[i];
			
			System.out.println("Source = " + source);
			
			for (int j = i + 1; j < arrRouteLine.length; j++)
			{
				dest = arrRouteLine[j];
				
				// Key format: RouteID_SourceID_DestinationID
				addBusRoute(""	+ routeID 
								+ "_" + source
								+ "_" + dest,
								source + "_" + dest);
				
				System.out.println("\n*** Route Start ***");
				System.out.println("Bus Route ID = " + routeID);
				System.out.println("Source = " + source);
				System.out.println("Destination = " + dest);
				System.out.println("*** Route End ***\n");
			}
			
		}
	}
	
	public void addBusRoute(String routeKey, String directRoute)
	{
//		routeMap.put(routeKey, directRoute);

		mc.set(routeKey, 600, directRoute);
		System.out.println("Value from Memcached Client =  " + mc.get(routeKey));
	}
	
	public DirectRoute getDirectRoute(int source, int dest)
	{
		/*
		if (routeMap.containsValue(source + "_" + dest))
		{
			return (new DirectRoute(source, dest));
		}
		else
			return null;
		*/
		
		// Print route line IDs
		/*
		for (int j = 1; j <= routeIDPos; j++)
		{
			System.out.println("key = " + j);
			System.out.println("value = " + routeIDs.get("" + j));
		}
		*/
		
		boolean dr = false;
		
		for (int i = 1; i <= routeIDPos; i++)
		{
			if (mc.get(routeIDs.get("" + i) + "_" + source + "_" + dest) != null)
			{
				dr = true;
				break;
			}
		}
		
		if (dr)
		{
			System.out.println("Direct route exists from " + source + " to " + dest);
			return (new DirectRoute(source, dest));
		}
		else 
			return null;
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

