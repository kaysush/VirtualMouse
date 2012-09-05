/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.midlet.*;
import javax.microedition.io.*; 

/**
 * @author sushil
 */
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;



public class VirtualMouse extends MIDlet implements CommandListener
{
private TouchEvents te=null;
private Display display=null;
private String ip;
private Form getIp;
private TextField tfIp;
private Command exit,ok;

public void init()
{
    display=Display.getDisplay(this);
    getIp=new Form("Enter your IP address");
    tfIp=new TextField("IP Address","",20,TextField.ANY);
    exit=new Command("Exit",1,Command.EXIT);
    ok=new Command("Start",2,Command.OK);
    getIp.append(tfIp);
    getIp.addCommand(exit);
    getIp.addCommand(ok);
    getIp.setCommandListener(this);
}


public void startApp()
{
    init();
    display.setCurrent(getIp);
}

public void pauseApp()
{
}

public void destroyApp(boolean u)
{

}

    public void commandAction(Command c, Displayable d) {
        if(c==(Command)exit)
        {
            notifyDestroyed();
            destroyApp(false);
        }
        else if(c==(Command)ok)
        {
            ip=tfIp.getString();
            
            if(ip.length()==0)
            {
                Alert a=new Alert("Error","Not a Valid IP",null,AlertType.ERROR);
                a.setTimeout(Alert.FOREVER);
                display.setCurrent(a);
            }
            else
            {
                te=new TouchEvents(ip,display);
                te.init();
                display.setCurrent(te);
            }
            
        }
    }
}
class TouchEvents extends Canvas
{
private int x,y;
private String command;
private String ip;
private Display display;
private boolean dragged=false;
public TouchEvents(String ip,Display display)
{
    this.command="d";
    this.ip=ip;
    this.display=display;
    x=10;
    y=20;

}

public void init()
{
    new Thread()
{
    public void run()
    {
        byte[] data=new byte[512];
        
        
        
       StringBuffer str=new StringBuffer();
       char ch;
       SocketConnection sc;
                try {
                    sc = (SocketConnection) Connector.open("socket://"+TouchEvents.this.ip+":54232");
                    OutputStream out=sc.openOutputStream();
                    InputStream in=sc.openInputStream();
                    do
                    {
                        in.read(data);
                    }
                    while(in.available()>0);
    
                while(true)
                {
                    
                    out.write((x+"+"+y+"+"+command).getBytes());
                    out.flush();
                    if(command.equals("c"))
                        command="d";
                   
                    do
                    {
                        in.read(data);
                    }
                    while(in.available()>0);
                    
                }
                } catch (IOException ex) {
                   Alert a=new Alert("Error","Can't connect to your PC",null,AlertType.ERROR);
                   a.setTimeout(Alert.FOREVER);
                   display.setCurrent(a);
                   
                   
                           
                }
    }
}.start();
}


public void paint(Graphics g)
{
int w=this.getWidth();
int h=this.getHeight();
g.setColor(255,255,255);
g.fillRect(0, 0, w+1,h+1);
g.setColor(0,0,0);
g.drawString("X : "+x+" Y: "+y,x+4,y+4,Graphics.TOP | Graphics.LEFT);
}
public void pointerPressed(int x, int y)
{
        dragged=false;
        //System.out.println("Pointer Pressed");
	this.x=x;
	this.y=y;
	repaint();
}
protected void pointerDragged(int x, int y)
{
    dragged=true;
    //System.out.println("Pointer Dragged");
    this.x = x;
    this.y = y;
    repaint();
	
}
protected void pointerReleased(int x ,int y)
{
    if(!dragged)
    {
        command="c";
        System.out.println("Clicked");
    }
}
public String getString()
{
return (Integer.toString(x)+"+"+Integer.toString(y)+"+");
}
}


