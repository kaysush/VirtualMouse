/**
 * A Server for VirtualMouse, a touch screen application 
 * for J2ME devices to control PC mouse from mobile device
 * over a network.
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;

public class VirtualMouseServer
{
	public static void main(String[] args)
	{
		try
		{
			ServerSocket ss=new ServerSocket(54232);
			Socket s=(Socket)ss.accept();
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
				System.out.println("length of bytes : "+l);
				String r=new String(data,0,l);
				System.out.println();
				outStream.write("got".getBytes());
				outStream.flush();
				int index=r.indexOf("+");
				System.out.println("length of response : "+r.length());
				String x=r.substring(0,index);
				String y=r.substring(index+1);
				System.out.println("X"+x+"Y"+y);
				robo.mouseMove(Integer.parseInt(x)*4,Integer.parseInt(y)*2);
				}
			}
				catch(AWTException e)
				{
					e.printStackTrace();
					}
					catch(IOException e){
						e.printStackTrace();
						}
	}
}
						
