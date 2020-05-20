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
              + "==	\u63d2\u4ef6\u52a0\u8f7d\u6210\u529f\n"
              + "==	\u8bf7\u5728 "+BurpExtender.extensionName+" \u4e2d\u8bbe\u7f6e\u004a\u0053\u6587\u4ef6\u8def\u5f84\u4ee5\u53ca\u8981\u8c03\u7528\u7684\u65b9\u6cd5\u540d \n"
              + "==	anthor: tinkmore\n"
              + "==	email: 990448338@qq.com\n";
        
        
        return hello;
    }

 
}
