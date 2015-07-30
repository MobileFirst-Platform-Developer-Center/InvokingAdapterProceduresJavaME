/**
* Copyright 2015 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
