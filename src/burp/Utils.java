package burp;


import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class Utils {
    private static Invocable  invocable;
    public static String functionName;
    
     public  static  void initJsEngine(String javaScriptPath){
    	// 获取JS引擎
 		ScriptEngine se = new ScriptEngineManager().getEngineByName("javascript");
 		try {
 		FileReader fr = new FileReader(javaScriptPath);
 		se.eval(fr);
 		fr.close();
 		invocable= (Invocable) se;
 		BurpExtender.stdout.println("**The js file was imported successfully**");
 		}catch (Exception e) {
 			BurpExtender.stderr.println(BurpExtender.extensionName+":JS file has syntax error, please test successfully in the local first, in addition, replace [let] with [var]");
 			BurpExtender.stderr.println(e.getMessage());
 		}
     }
    

	public static String sendPayload(String payload) {
		try {
			return  (String) invocable.invokeFunction(functionName, payload);
		}catch (Exception e) {
			BurpExtender.stderr.println(BurpExtender.extensionName+":Syntax error occurred when calling js method to handle payload. Please test the js method in the local file");
 			BurpExtender.stderr.println(e.getMessage());
 			return e.getMessage();
		} 
		  
	}

    public static String getBanner(){
    
        String hello =
                  "==	" + BurpExtender.extensionName +"-"+BurpExtender.version+ "\n"
                + "==	hello\n"
                + "==	anthor: thinkoaa\n"
                + "==	email: 990448338@qq.com\n"
        		+ "==	github: https://github.com/thinkoaa/JSRainbow\n";
        
        
        return hello;
    }

 
}
