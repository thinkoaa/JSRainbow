package burp;

import java.awt.Component;
import java.io.PrintWriter;

import javax.swing.SwingUtilities;

public class BurpExtender implements IBurpExtender,IIntruderPayloadProcessor,ITab {
    public final static String extensionName = "JS Rainbow";
	public final static String version ="1.0";
	public static IBurpExtenderCallbacks callbacks;
	public static IExtensionHelpers helpers;
	public static PrintWriter stdout;
	public static PrintWriter stderr;
	public static GUI gui;
	
	@SuppressWarnings("static-access")
	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;
		this.helpers = callbacks.getHelpers();
		this.stdout = new PrintWriter(callbacks.getStdout(),true);
		this.stderr = new PrintWriter(callbacks.getStderr(),true);
		
		callbacks.setExtensionName(extensionName+" "+version);
		callbacks.registerContextMenuFactory(new Menu());
		callbacks.registerIntruderPayloadProcessor(this);

		BurpExtender.this.gui = new GUI();
		SwingUtilities.invokeLater(new Runnable()
	      {
	        public void run()
	        {
	          BurpExtender.this.callbacks.addSuiteTab(BurpExtender.this);
	          stdout.println(Utils.getBanner());
	        }
	      });
		
	}
	
	 
	@Override
	public String getProcessorName() {
		return extensionName;
	}

	@Override
	public byte[] processPayload(byte[] currentPayload, byte[] originalPayload, byte[] baseValue) {
		String payload = new String(currentPayload);
		String newPayload = Utils.sendPayload(payload);
		return helpers.stringToBytes(newPayload);
	}

 
	@Override
	public String getTabCaption() {
		return extensionName;
	}

	@Override
	public Component getUiComponent() {
		return gui.getComponet();
	}
	
	public static void main(String args[]) {
		System.out.println("hello");
	}
}
