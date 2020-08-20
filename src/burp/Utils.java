package burp;


import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class Utils {
    private static Invocable  invocable;
    public static String functionName;
    
     public  static  void initJsEngine(String javaScriptPath) throws Exception{
    	// 获取JS引擎
 		ScriptEngine se = new ScriptEngineManager().getEngineByName("nashorn");
 	 
 		FileReader fr = new FileReader(javaScriptPath);
 		se.eval(fr);
 		fr.close();
 		invocable= (Invocable) se;
 	 
     }
    

	public static String sendPayload(String payload) throws NoSuchMethodException, ScriptException {
		 
			return    invocable.invokeFunction(functionName, payload).toString();
	 
		  
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
