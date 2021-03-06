package srvcifIC201820;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class MonitorCPU extends Thread{



	private int id; 

	private static ArrayList<Double> cpu = new ArrayList<>(); 
	private static PrintWriter pw = null; 

	public MonitorCPU(int pId)
	{
		id = pId; 
	}


	public synchronized void printCPU(double valor) throws FileNotFoundException
	{

		if(pw==null )
			pw = new PrintWriter(System.getProperty("user.dir") + File.separator + "data" + File.separator+ Coordinador.NUMERO_THREADS + "threads-"+ "usoCPU.csv" ); 



		if(id==0)
		{
			pw.println("sep=,");
		}

		pw.println(valor);

		if(id == Delegado3.numeroCarga - 1)
		{
			pw.close();
		}

		//cpu.add(valor); 
	}


	public void closePW()
	{
		pw.close();
	}



	public ArrayList<Double> getUsoCPU(){
		return cpu; 
	}

	//	public static void getSystemCpuLoad() throws Exception {
	//		
	//		
	//		 double valor = Double.NaN; 
	//		
	//		 MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	//		 ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
	//		 AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });
	//		 if (list.isEmpty()) return;
	//		 Attribute att = (Attribute)list.get(0);
	//		 Double value = (Double)att.getValue();
	//		 // usually takes a couple of seconds before we get real values
	//		 if (value == -1.0) valor =  Double.NaN;
	//		 // returns a percentage value with 1 decimal point precision
	//		  valor =  ((int)(value * 1000) / 10.0);
	//		 }

	@Override
	public void run()
	{


		double valor = Double.NaN; 
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });
			if (list.isEmpty()) return;
			Attribute att = (Attribute)list.get(0);
			Double value = (Double)att.getValue();
			// usually takes a couple of seconds before we get real values
			if (value == -1.0) valor =  Double.NaN;
			// returns a percentage value with 1 decimal point precision
			valor =  ((int)(value * 1000) / 10.0);

			printCPU(valor);
			System.out.println(valor);

		}catch(Exception e )
		{
			System.out.println("ERROR " + e) ;
		}

	}


}
