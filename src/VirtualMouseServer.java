/**
 * A Server for VirtualMouse, a touch screen application 
 * for J2ME devices to control PC mouse from mobile device
 * over a network.
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class VirtualMouseServer
{
	public static void getIPAddress() throws IOException
	{
		URL whatsMyIP;
		String ipAddr="Can't fetch you IP";
        HttpURLConnection con;
        BufferedReader in=null;
        String r;
        System.out.println("Fetching your IP Address....");
        try {
            whatsMyIP = new URL("http://automation.whatismyip.com/n09230945.asp");
            con=(HttpURLConnection)whatsMyIP.openConnection();
            con.setUseCaches(false);
            con.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0");
            in=new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((r=in.readLine())!=null)
                ipAddr=r;
            System.out.println("Your IP Address is : "+ipAddr);
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        catch(UnknownHostException ex)
        {
            System.out.println("Internet Connection not found!!!");
            System.exit(0);
        }
        
        catch (IOException ex) {
            ex.printStackTrace();
        } 
        finally{
			if(in!=null)
			in.close();
		}
    }
	
	public static void main(String[] args)  throws IOException
	{
		Socket s=null;
		ServerSocket ss=null;
		try
		{
			getIPAddress();
			ss=new ServerSocket(54232);
			s=(Socket)ss.accept();
			InputStream inStream=s.getInputStream();
			OutputStream outStream=s.getOutputStream();
			Robot robo=new Robot();
			outStream.write("Connected".getBytes());
			outStream.flush();
			while(true)
			{
				byte[] data=new byte[512];
				do
				{
					inStream.read(data);
				}
				while(inStream.available()>0);
				int l=0;
				for(int i=0;i<data.length;i++)
				{
					if(data[i]!=0)
					l++;
					else if(data[i]==0)
					break;
				}
				String r=new String(data,0,l);
				System.out.println();
				outStream.write("got".getBytes());
				outStream.flush();
				int index=r.indexOf("+");
				int index1=r.lastIndexOf("+");
				String x=r.substring(0,index);
				String y=r.substring(index+1,index1);
				String command=r.substring(index1+1);
				if(command.equals("d"))
					robo.mouseMove(Integer.parseInt(x)*4,Integer.parseInt(y)*2);

				else
				{	
					robo.mousePress(InputEvent.BUTTON1_MASK);
					robo.mouseRelease(InputEvent.BUTTON1_MASK);

				}
				}
			}
				catch(AWTException e)
				{
					e.printStackTrace();
					}
					catch(IOException e){
						e.printStackTrace();
						}
						finally{
							if(s!=null)
							s.close();
							if(ss!=null)
							ss.close();
						}
	}
}
						
