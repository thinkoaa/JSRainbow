package burp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;


public class Utils {
    private static Invocable invocable;
    public static volatile String functionName;
    private static String engineName = "自动加载";
    private static String lastError = null;

    /**
     * 初始化JS引擎，自动检测可用引擎
     * 优先级: Nashorn独立包(直接实例化) > JDK内置Nashorn > ScriptEngineManager发现
     */
    public static void initJsEngine(String javaScriptPath) throws Exception {
        ScriptEngine se = null;
        lastError = null;
        StringBuilder errors = new StringBuilder();

        // 1. 优先尝试直接实例化Nashorn独立包（fat jar内包含nashorn-core）
        try {
            Class<?> factoryClass = Class.forName("org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory");
            Object factory = factoryClass.getDeclaredConstructor().newInstance();
            se = (ScriptEngine) factoryClass.getMethod("getScriptEngine").invoke(factory);
            if (se != null) {
                engineName = "Nashorn (Standalone)";
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Throwable cause = e.getCause();
            while (cause != null && cause.getMessage() != null) {
                msg = cause.getMessage();
                cause = cause.getCause();
            }
            errors.append("Nashorn Standalone: ").append(e.getClass().getSimpleName()).append(" - ").append(msg).append("\n");
        }

        // 2. 尝试JDK内置Nashorn（JDK 8-14）
        if (se == null) {
            try {
                ScriptEngineManager manager = new ScriptEngineManager();
                se = manager.getEngineByName("nashorn");
                if (se != null) {
                    engineName = "Nashorn (JDK Built-in)";
                }
            } catch (Exception e) {
                errors.append("Nashorn JDK Built-in: ").append(e.getClass().getSimpleName()).append(" - ").append(e.getMessage()).append("\n");
            }
        }

        // 3. 尝试ScriptEngineManager发现任何可用JS引擎（如GraalVM JS等）
        if (se == null) {
            try {
                ScriptEngineManager manager = new ScriptEngineManager();
                for (ScriptEngineFactory factory : manager.getEngineFactories()) {
                    for (String name : factory.getNames()) {
                        if (name.equalsIgnoreCase("js") || name.equalsIgnoreCase("javascript")
                                || name.equalsIgnoreCase("graal.js") || name.equalsIgnoreCase("ecmascript")) {
                            se = factory.getScriptEngine();
                            engineName = factory.getEngineName();
                            break;
                        }
                    }
                    if (se != null) break;
                }
            } catch (Exception e) {
                errors.append("Other JS Engine: ").append(e.getClass().getSimpleName()).append(" - ").append(e.getMessage()).append("\n");
            }
        }

        if (se == null) {
            lastError = errors.toString();
            throw new IllegalStateException(
                "未找到可用的JS引擎！\n\n" +
                "尝试详情:\n" + errors.toString() + "\n" +
                "JDK 15+ 已移除内置Nashorn，请确保使用包含nashorn-core的fat jar。\n" +
                "下载地址: https://github.com/thinkoaa/JSRainbow/releases"
            );
        }

        String scriptContent = new String(Files.readAllBytes(Paths.get(javaScriptPath)), StandardCharsets.UTF_8);
        se.eval(scriptContent);
        invocable = (Invocable) se;
    }

    public static String getLastError() {
        return lastError;
    }

    /**
     * 调用JS函数处理payload
     */
    public static String sendPayload(String payload) throws NoSuchMethodException, ScriptException {
        if (invocable == null) {
            throw new IllegalStateException("JS引擎未初始化，请先加载JS文件");
        }
        String fn = functionName;
        if (fn == null || fn.trim().isEmpty()) {
            throw new IllegalStateException("请先设置JS方法名称");
        }
        Object result = invocable.invokeFunction(fn.trim(), payload);
        if (result == null) {
            return "";
        }
        return result.toString();
    }

    public static String getEngineName() {
        return engineName;
    }

    public static String getBanner() {
        String hello =
                  "==\t" + BurpExtender.extensionName + "-" + BurpExtender.version + "\n"
                + "==\tJS Engine: " + engineName + "\n"
                + "==\tauthor: thinkoaa\n"
                + "==\temail: 990448338@qq.com\n"
                + "==\tgithub: https://github.com/thinkoaa/JSRainbow\n";
        return hello;
    }
}
