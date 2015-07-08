/*
 *
    COPYRIGHT LICENSE: This information contains sample code provided in source code form. You may copy, modify, and distribute
    these sample programs in any form without payment to IBM® for the purposes of developing, using, marketing or distributing
    application programs conforming to the application programming interface for the operating platform for which the sample code is written.
    Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES,
    EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY,
    FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT,
    INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR OPERATION OF THE SAMPLE SOURCE CODE.
    IBM HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE SOURCE CODE.

 */

package com.worklight.invokingadapterproceduresjavame;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLProcedureInvocationData;
import com.worklight.wlclient.api.WLRequestOptions;

public class MainMidlet extends MIDlet implements CommandListener,ItemCommandListener{
	
	private Display  display;
	private Form form;	
    private StringItem connect;
    private StringItem invoke;
    private static StringItem result;
	private Command exit;
	private WLClient client;
	

	public MainMidlet() {
		client = WLClient.createInstance(this);
		this.display = Display.getDisplay(this);
		form = new Form("InvokeAdapterProcedure Sample For JavaME");
		connect = new StringItem("", "1.Connect", Item.BUTTON);
		connect.addCommand(new Command("OK", Command.OK, 1));
		connect.setItemCommandListener(this);
		invoke = new StringItem("","2.Invoke Procedure",Item.BUTTON);
		invoke.addCommand(new Command("OK", Command.OK, 1));
		invoke.setItemCommandListener(this);
		result = new StringItem("", "");
		exit = new Command("Exit", Command.EXIT, 0);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		notifyDestroyed();

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		form.append(connect);
		form.append(invoke);
		form.append(result);
		form.addCommand(exit);
		form.setCommandListener(this);
		display.setCurrent(form);

	}
	public void commandAction(Command command, Item item) {
		StringItem itemName = (StringItem)item;
		if(itemName.getText().equals("1.Connect")) {
			updateTextView("\nConnecting...");
			client.connect(new MyConnectListener());
		}else if(itemName.getText().equals("2.Invoke Procedure")) {
			updateTextView("\nInvoking procedure...");
			String adapterName = "RSSReader";
			String procedureName = "getFeedsFiltered";
			
			WLProcedureInvocationData invocationData = new WLProcedureInvocationData(adapterName, procedureName);
			
			Object[] parameters = new Object[] {};
			invocationData.setParameters(parameters);
			
			WLRequestOptions options = new WLRequestOptions();	
			client.invokeProcedure(invocationData, new MyInvokeListener(), options);
			  
		  }
	}
	public void commandAction(Command command, Displayable displayable) {
		  String label = command.getLabel();
		  if(label.equals("Exit")) {
			  try{
				  destroyApp(true);
			  }catch(Exception e){
				  
			  }
		  }		
	}
	public static void updateTextView(final String response){
		result.setText(response);		
	}
}
