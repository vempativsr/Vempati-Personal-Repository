package com.springer.pro;

import java.util.Scanner;

public class Drawing {
	
	String gArr[][] = null;
	
	int canWidth = 0;
	int canHeight = 0;
	
	int maxWidth = 0;
	int maxHeight = 0;
	
	public static void main(String args[])
	{
		Drawing dw = new Drawing();
		dw.setMaxWidth(200);
		dw.setMaxHeight(100);
		//dw.displayMainMenu();
		
		dw.displayCanvasMenu();
		dw.displayLineMenu();
		dw.displayRectangleMenu();
		dw.displayBucketMenu();
		System.out.println("Press Q to Quit");		
		System.out.println("Please enter a command of your choice...");
		
		Scanner sc = new Scanner(System.in);
		//int ip = dw.getMainMenuInput(sc);
		//System.out.println("Main menu input from main method: " + ip);
		
		System.out.print ("Enter command: ");
		String ipStr = sc.nextLine();
		
		String canIp = null;
		String lineIp = null;
		String rectIp = null;
		
		String input = null;
		String canValidationResult = null;
		String lineValidationResult = null;
		String rectValidationResult = null;
		String bucketValidationResult = null;
		
		while (!ipStr.equals("Q"))
		{
			if (ipStr.startsWith("C"))
			{
				canValidationResult = dw.validateCanvasInput(ipStr);
				
				if(canValidationResult.equalsIgnoreCase("Invalid"))
				{
					System.out.println("Wrong Input for Canvas...Please try again.");
					dw.displayCanvasMenu();
				}
				else
				{
					String cip[] = ipStr.split(" ");
					
					int width = Integer.valueOf(cip[1]);
					int height = Integer.valueOf(cip[2]);
					
					dw.setCanvasWidth(width);
					dw.setCanvasHeight(height);
					dw.drawCanvas(width, height);
				}
			}
			else if (ipStr.startsWith("L"))
			{
				if (dw.getCanvasWidth() == 0)
				{
					System.out.println("Please draw Canvas first...");
					dw.displayCanvasMenu();
				}
				else
				{
					lineValidationResult = dw.validateLineInput(ipStr);
					
					if(lineValidationResult.equalsIgnoreCase("Invalid"))
					{
						System.out.println("Wrong Input for Line...Please try again.");
						dw.displayLineMenu();
					}
					else
					{
						String lip[] = ipStr.split(" ");
						
						int x1 = Integer.valueOf(lip[1]);
						int y1 = Integer.valueOf(lip[2]);
						int x2 = Integer.valueOf(lip[3]);
						int y2 = Integer.valueOf(lip[4]);
						
						dw.drawLine(x1, y1, x2, y2);
					}
				}				
			}
			
			else if (ipStr.startsWith("R"))
			{
				if (dw.getCanvasWidth() == 0)
				{
					System.out.println("Please draw Canvas first...");
					dw.displayCanvasMenu();
				}
				else
				{
					rectValidationResult = dw.validateRectInput(ipStr);
					
					if(rectValidationResult.equalsIgnoreCase("Invalid"))
					{
						System.out.println("Wrong Input for Rectangle...Please try again.");
						dw.displayRectangleMenu();
					}
					else
					{
						String lip[] = ipStr.split(" ");
						
						int x1 = Integer.valueOf(lip[1]);
						int y1 = Integer.valueOf(lip[2]);
						int x2 = Integer.valueOf(lip[3]);
						int y2 = Integer.valueOf(lip[4]);
						
						dw.drawRect(x1, y1, x2, y2);
					}
				}
			}
			else if (ipStr.startsWith("B"))
			{
				if (dw.getCanvasWidth() == 0)
				{
					System.out.println("Please draw Canvas first...");
					dw.displayCanvasMenu();
				}
				else
				{
					bucketValidationResult = dw.validateBucketInput(ipStr);
					
					if(bucketValidationResult.equalsIgnoreCase("Invalid"))
					{
						System.out.println("Wrong Input for Bucket...Please try again.");
						dw.displayBucketMenu();
					}
					else
					{
						String cip[] = ipStr.split(" ");
						
						int width = Integer.valueOf(cip[1]);
						int height = Integer.valueOf(cip[2]);
						String color = cip[3];
						
						dw.bucketFill(Integer.valueOf(cip[1]), Integer.valueOf(cip[2]), color);
					}
				}
			}
			else
			{
				System.out.println("Wrong Input...Please try again.");
			}
			
			if (dw.gArr != null && dw.gArr.length > 0)
				dw.printObjects();
			
			System.out.print ("Enter command: ");
			ipStr = sc.nextLine();
			
		}

		sc.close();
		System.exit(0);
		
	}
	
	public void displayCanvasMenu()
	{
		//System.out.println("1. Create a new Canvas.");
		System.out.println("Command Format: C w h");
		System.out.println("(Creates a new canvas of width w characters and height h characters. Limits: w: 200 max and h: 150 max)");
	}

	public String validateCanvasInput(String ipStr)
	{
		System.out.println("Canvas Input = " + ipStr);
		
		String[] ip = ipStr.split(" ");
		
		if (ip.length != 3)
		{
			System.out.println("gci 1");
			return "Invalid";
		}
		
		int width = 0;
		int height = 0;
		
		try
		{
			width = Integer.valueOf(ip[1]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for Width. Width should be an Integer");
			return "Invalid";
		}
		
		try
		{
			height = Integer.valueOf(ip[2]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for Height. Height should be an Integer");
			return "Invalid";
		}
		
		if (width < 0)
		{
			System.out.println("Width should be a positive Integer");
			return "Invalid";
		}
		
		if (width > maxWidth)
		{
			System.out.println("Width must be less than or equal to " + maxWidth + "...Please try again.");
			return "Invalid";
		}
		
		if (height < 0)
		{
			System.out.println("Height should be a positive Integer");
			return "Invalid";
		}
		
		if (height > maxHeight)
		{
			System.out.println("Height must be less than or equal to " + maxHeight + "...Please try again.");
			return "Invalid";
		}
				
		return ipStr;
	}
	
	public void setCanvasWidth(int width)
	{
		this.canWidth = width;
	}
	
	public void setCanvasHeight(int height)
	{
		this.canHeight = height;
	}
	
	public int getCanvasWidth()
	{
		return this.canWidth;
	}
	
	public int getCanvasHeight()
	{
		return this.canHeight;
	}
	
	public void setMaxWidth(int maxWidth)
	{
		this.maxWidth = maxWidth;
	}
	
	public void setMaxHeight(int maxHeight)
	{
		this.maxHeight = maxHeight;
	}
	
	public int getMaxWidth()
	{
		return this.maxWidth;
	}
	
	public int getMaxHeight()
	{
		return this.maxHeight;
	}
	
	
	public void drawCanvas(int width, int height)
	{
		System.out.println("Entered drawCanvas");
		
		gArr = new String[width + 2][height + 2];
		
		for (int i = 0; i <= width + 1; i++)
			gArr[i][0] = "-";
		
		System.out.println();
		
		for (int j = 1; j < height + 1; j++)
		{
			gArr[0][j] = "|";
			
			for (int k = 1; k < width + 1; k++)
				gArr[k][j] = " ";
			
			gArr[width + 1][j] = "|";
		}
		
		for (int l = 0; l <= width + 1; l++)
			gArr[l][height + 1] = "-";
		
	}
	
	public void displayLineMenu()
	{
		//System.out.println("2. Start drawing on the Canvas");
		System.out.println("Command Format:....");
		System.out.println("L x1 y1 x2 y2 to draw a line from (x1, y1) to (x2, y2)");
	}

	public String validateLineInput(String ipStr)
	{
		String[] ip = ipStr.split(" ");
		
		if (ip.length != 5)
		{
			return "Invalid";
		}
		
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		
		try
		{
			x1 = Integer.valueOf(ip[1]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for x1. x1 should be an Integer");
			return "Invalid";
		}
		
		try
		{
			y1 = Integer.valueOf(ip[2]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for y1. y1 should be an Integer");
			return "Invalid";
		}

		try
		{
			x2 = Integer.valueOf(ip[3]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for x2. x2 should be an Integer");
			return "Invalid";
		}
		
		try
		{
			y2 = Integer.valueOf(ip[4]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for y2. y2 should be an Integer");
			return "Invalid";
		}
		
		String lineType = "";
		
		if (y1 == y2)
			lineType = "HL";
		else if (x1 == x2)
			lineType = "VL";
		
		if (lineType.isEmpty())
		{
			System.out.println("Only horizontal and vertical lines are supported. Please change your input...");
			return "Invalid";
		}
		
		if ( x1 > getCanvasWidth()
			|| x2 > getCanvasWidth()
			|| (x2 - x1) > getCanvasWidth())
		{
			System.out.println("Line length can not exceed canvas width..." + getCanvasWidth());
			return "Invalid";
		}
		if ( y1 > getCanvasHeight()
				|| y2 > getCanvasHeight()
				|| (y2 - y1) > getCanvasHeight())
		{
			System.out.println("Line length can not exceed canvas height..." + getCanvasHeight());
			return "Invalid";
		}
		
		return ipStr;
	}
	
	public void drawLine (int x1, int y1, int x2, int y2)
	{
		System.out.println("Entered drawLine...");
		
		String lineType = null;
		
		if (y1 == y2)
			lineType = "HL";
		else if (x1 == x2)
			lineType = "VL";
		
		if (lineType.equals("HL"))
		{
			System.out.println("Entered HL...x1 = " + x1 + " x2 = " + x2);
			for (int i = x1; i <= x2; i++)
				gArr[i][y1] = "X";
		}
		
		if (lineType.equals("VL"))
		{
			System.out.println("Entered VL...y1 = " + y1 + " y2 = " + y2);
			for (int i = y1; i <= y2; i++)
				gArr[x1][i] = "X";
		}

	}
	
	public void displayRectangleMenu()
	{
		System.out.println("R x1 y1 x2 y2 to draw a rectangle with upper left corner as (x1, y1) and lower right corner as (x2, y2)");
	}
	
	public String validateRectInput(String ipStr)
	{
		System.out.println("Rectangle Input = " + ipStr);
		
		String[] ip = ipStr.split(" ");
		
		if (ip.length != 5)
		{
			return "Invalid";
		}
		
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		
		try
		{
			x1 = Integer.valueOf(ip[1]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for x1. x1 should be an Integer");
			return "Invalid";
		}
		
		try
		{
			y1 = Integer.valueOf(ip[2]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for y1. y1 should be an Integer");
			return "Invalid";
		}

		try
		{
			x2 = Integer.valueOf(ip[1]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for x2. x2 should be an Integer");
			return "Invalid";
		}
		
		try
		{
			y2 = Integer.valueOf(ip[2]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for y2. y2 should be an Integer");
			return "Invalid";
		}
		
		int width = x2 - x1;
		int height = y2 - y1;
		
		if (width < 0)
		{
			System.out.println("Width should be a positive Integer");
			return "Invalid";
		}
		
		if (x1 > getCanvasWidth()
			|| x2 > getCanvasWidth()
			|| width > getCanvasWidth()
				)
		{
			System.out.println("Width must be less than or equal to " + getCanvasWidth() + "...Please try again.");
			return "Invalid";
		}
		
		if (height < 0)
		{
			System.out.println("Height should be a positive Integer");
			return "Invalid";
		}
		
		if (y1 > getCanvasHeight()
				|| y2 > getCanvasHeight()
				|| height > getCanvasHeight())
		{
			System.out.println("Height must be less than or equal to " + getCanvasHeight() + "...Please try again.");
			return "Invalid";
		}
		
		return ipStr;
	}
	
	public void drawRect (int x1, int y1, int x2, int y2)
	{
		System.out.println("Entered drawRect...");
		
		drawLine(x1, y1, x2, y1);
		drawLine(x2, y1, x2, y2);
		drawLine(x1, y2, x2, y2);
		drawLine(x1, y1, x1, y2);		
	}
	
	public void displayBucketMenu()
	{
		//System.out.println("4. Bucket Fill.");
		System.out.println("Command Format: B x y c");
		System.out.println("(Bucket fill the area connected to (x, y). Limits: w: 200 max and h: 100 max)");
	}
	
	public String validateBucketInput(String ipStr)
	{
		System.out.println("Bucket Input = " + ipStr);
		
		String[] ip = ipStr.split(" ");
		
		if (ip.length != 4)
		{
			System.out.println("gci 3");
			return "Invalid";
		}
		
		int width = 0;
		int height = 0;
		
		try
		{
			width = Integer.valueOf(ip[1]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for Width. Width should be an Integer");
			return "Invalid";
		}
		
		try
		{
			height = Integer.valueOf(ip[2]);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Wrong input for Height. Height should be an Integer");
			return "Invalid";
		}
		
		if (width < 0)
		{
			System.out.println("Width should be a positive Integer");
			return "Invalid";
		}
		
		if (width > getCanvasWidth())
		{
			System.out.println("Width must be less than or equal to " + getCanvasWidth() + "...Please try again.");
			return "Invalid";
		}
		
		if (height < 0)
		{
			System.out.println("Height should be a positive Integer");
			return "Invalid";
		}
		
		if (height > getCanvasHeight())
		{
			System.out.println("Height must be less than or equal to " + getCanvasHeight() + "...Please try again.");
			return "Invalid";
		}
				
		return ipStr;
	}
	
	public void bucketFill(int x, int y, String color)
	{
		System.out.println("Entered bucketFill...");
		
		for (int j = 0; j < getCanvasHeight() + 1; j++)
		{
			for (int i = x; i > 0; i--)
			{
				if (!gArr[i][j].equals(" "))
					break;
				
				if (gArr[i][j].equals(" "))
					gArr[i][j] = color;
			}
			
			for (int i = x + 1; i < getCanvasWidth() + 1; i++)
			{
				if (!gArr[i][j].equals(" "))
					break;
				
				if (gArr[i][j].equals(" "))
					gArr[i][j] = color;
			}
		
		}
	}
	
	public void displayMainMenu()
	{
		System.out.println("1. Create a new Canvas.");
		System.out.println("2. Start drawing on the Canvas");
		System.out.println("3. Press Q to Quit");
		
		System.out.println("Please enter your choice...: ");
		
	}
	
	public int getMainMenuInput(Scanner sc)
	{
		String ipStr = sc.nextLine();
		
		int ipInt = 0;
		
		System.out.println("Main menu input: " + ipStr);
		
		if (ipStr.equals("1"))
			ipInt = 1;
		else if (ipStr.equals("2"))
			ipInt = 2;
		else if (ipStr.equals("Q"))
			ipInt = 3;
		
		return ipInt;
	}
	
	public void printObjects()
	{
		for (int j = 0; j <= getCanvasHeight() + 1; j++)
		{
			for (int i = 0; i <= getCanvasWidth() + 1; i++)
				System.out.print(gArr[i][j]);
			
			System.out.println();
		}		
	}
}
