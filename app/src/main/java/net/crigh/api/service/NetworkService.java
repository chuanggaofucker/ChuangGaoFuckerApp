package net.crigh.api.service;

import net.crigh.api.encrypt.AES;
import net.crigh.api.encrypt.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;

import static net.crigh.api.encrypt.ChingoEncrypt.cgapiEnrypt;


public class NetworkService {
    private static final String PROVINCE_CODE = "42";
    private static final String USER_AGENT = "cgapp/2.5.9 (Linux; Android 7.1.2; Xiaomi Build/NJH47F)";
    private static final String VERSION = "2.5.9";
    private static final String APP_KEY = "azk3t4jrcfm5772t";
    private static final String APP_SECRET = "262b6c001ea05beceb9d560be1dbf14f";
    private static final String LOGIN_URL = "https://petzjk.hust.edu.cn/cgapp-server/api/f/v6/login";
    private static final String PREJUDGMENT_URL = "https://petzjk.hust.edu.cn/cgapp-server/api/l/v6/prejudgment";
    private static final String SAVE_SPORTS_URL = "https://petzjk.hust.edu.cn/cgapp-server/api/l/v6/savesports";

    public static JSONObject login(String username, String password) throws Exception {
        TreeMap<String, String> form = new TreeMap<>();

        form.put("string", "string");
        form.get("string");

        form.put("password", AES.Encrypt(password));
        form.put("provinceCode", PROVINCE_CODE);
        form.put("randomCode", "19");
        form.put("userAgent", USER_AGENT);
        form.put("username", username);
        form.put("version", VERSION);

        String timestamp = String.valueOf(System.currentTimeMillis());


        String path = LOGIN_URL.substring(LOGIN_URL.indexOf("/api"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(APP_SECRET);
        stringBuilder.append(path);
        for (Entry entry : form.entrySet()) {
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append((String) entry.getValue());
        }
        stringBuilder.append(timestamp);
        stringBuilder.append(" ");
        stringBuilder.append(APP_SECRET);

        String sign = MD5.getMd5(stringBuilder.toString());

        form.put("sign", sign);
        form.put("app_key", APP_KEY);
        form.put("timestamp", timestamp);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Entry entry : form.entrySet()) {
            formBodyBuilder.add((String) entry.getKey(), (String) entry.getValue());
        }

        Request request = new Request
                .Builder()
                .url(LOGIN_URL)
                .header("timestamp", timestamp)
                .header("sign", sign)
                .header("app-key", APP_KEY)
                .header("content-type", "application/x-www-form-urlencoded")
                .header("user-agent", "okhttp/3.10.0")
                .post(formBodyBuilder.build())
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body != null) {
            JSONObject loginResponse = new JSONObject(body.string());
//                Log.d("Login", body.string());
            Integer code = loginResponse.getInt("code");
            String message = loginResponse.getString("message");
            if (code != 200) {
                throw new Exception("Server said: \"" + message + "\"");
            }
            return loginResponse;
        } else {
            throw new Exception("Unknown error!");
        }
    }

    public static void run(JSONObject loginResponse) throws Exception {
        // Every Rhino VM begins with the enter()
        // This Context is not Android's Context
        Context rhino = Context.enter();
        JSONObject json = null;

        // Turn off optimization to make Rhino Android compatible
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();
            // Note the forth argument is 1, which means the JavaScript source has
            // been compressed to only one line using something like YUI
            String JavaScriptCode = "function padZero(e){return e<10?\"0\"+~~e:\"\"+~~e}function generateCoordinate(e,a){return{a:+e,o:+a,v:1,s:0,t:0,ac:0}}function generateData(){for(var e=(3+Math.random()/3).toFixed(2),a=15+10*Math.random(),t=\"00:\"+padZero(a)+\":\"+padZero(a%1*60),o=+(320/30*a).toFixed(1),r=e/a*60,n=r.toFixed(2),d=(r+.5+.7*Math.random()).toFixed(2),i=Date.now(),m=new Date(i-6e4*a-6e4*(new Date).getTimezoneOffset()).toISOString().split(\".\")[0].replace(\"T\",\" \"),p=new Date(i-6e4*(new Date).getTimezoneOffset()).toISOString().split(\".\")[0].replace(\"T\",\" \"),h=~~(3e3+1e3*Math.random()),M=a/e,s=[{km:\"1\",t:padZero(M-1.5*Math.random())+\"'\"+padZero(60*Math.random())+\"''\"},{km:\"2\",t:padZero(M)+\"'\"+padZero(60*Math.random())+\"''\"},{km:\"3\",t:padZero(M+1.5*Math.random())+\"'\"+padZero(60*Math.random())+\"''\"}],u=[],g=1;g<=a;g++)u.push({min:g,v:(r+Math.random()*(g<a/2?1:-1)).toFixed(2)});var x=(r-.5-.7*Math.random()).toFixed(2),Z=(d-.4*Math.random()).toFixed(2),c=padZero(M)+\"'\"+padZero(60*Math.random())+\"''\",F=\"00:\"+padZero(M+1.5*Math.random())+\":\"+padZero(60*Math.random()),f=(h/a).toFixed(2),l=(30.508+Math.random()/1e3).toFixed(6),S=(114.408+Math.random()/1e3).toFixed(6),v=l+\"|\"+S,T=(30.514+Math.random()/1e3).toFixed(6),D=(114.432+Math.random()/1e3).toFixed(6),O=T+\"|\"+D,w=[\"30.509, 114.408\",\"30.511, 114.408\",\"30.511, 114.410\",\"30.510, 114.416\",\"30.510, 114.420\",\"30.513, 114.421\",\"30.515, 114.421\",\"30.514, 114.427\",\"30.514, 114.430\"],C=[];C.push(generateCoordinate(l,S));for(g=0;g<w.length;g++){var P=w[g].split(\", \"),k=+P[0]+Math.random()/1e3,b=+P[1]+Math.random()/1e3;C.push(generateCoordinate(k,b))}return C.push(generateCoordinate(T,D)),{calorie:o,odometer:e,avgSpeed:n,activeTime:t,beginTime:m,endTime:p,isValid:1,isValidReason:\"\",stepCount:h,pace:s,minuteSpeed:u,minSpeedPerHour:x,maxSpeedPerHour:Z,avgPace:c,lastOdometerTime:F,stepMinute:f,beganPoint:v,endPoint:O,coordinate:C}}function main(){return JSON.stringify(generateData())}";
            rhino.evaluateString(scope, JavaScriptCode, "JavaScript", 1, null);

            // Get the functionName defined in JavaScriptCode
            Object main = scope.get("main", scope);

            if (main instanceof Function) {
                Function jsFunction = (Function) main;
                // Call the function with params
                Object result = jsFunction.call(rhino, scope, scope, new Object[]{});
                // Parse the jsResult object to a String
                json = new JSONObject(Context.toString(result));
                // Log.d("Rhino", result);
            }
        } catch (JSONException e) {
            json = null;
        } finally {
            Context.exit();
        }

//        return {
//                calorie: calorie,
//                odometer: odometer,
//                avgSpeed: avgSpeed,
//                activeTime: activeTime,
//                beginTime: beginTime,
//                endTime: endTime,
//                isValid: isValid,
//                isValidReason: isValidReason,
//                stepCount: stepCount,
//                pace: pace,
//                minuteSpeed: minuteSpeed,
//                minSpeedPerHour: minSpeedPerHour,
//                maxSpeedPerHour: maxSpeedPerHour,
//                avgPace: avgPace,
//                lastOdometerTime: lastOdometerTime,
//                stepMinute: stepMinute,
//                beganPoint: beganPoint,
//                endPoint: endPoint,
//                coordinate: coordinate
//        };

        if (json != null) {
            JSONObject data = loginResponse.getJSONObject("data");
            JSONObject info = data.getJSONObject("info");
            String token = data.getString("token");
            String[] tokenSplit = token.split("\\.");
            String cgAuthorization = tokenSplit[0] + "." + tokenSplit[1] + "." + tokenSplit[2];

            {
                JSONObject prejudgmentJSON = new JSONObject();

                prejudgmentJSON.put("activeTime", json.getString("activeTime"));
                prejudgmentJSON.put("alreadyPassPoint", "");
                prejudgmentJSON.put("beginTime", json.getString("beginTime"));
                prejudgmentJSON.put("endTime", json.getString("endTime"));
                prejudgmentJSON.put("odometer", json.getString("odometer"));
                prejudgmentJSON.put("routeId", "7");
                prejudgmentJSON.put("stepMinute", json.getString("stepMinute"));
                prejudgmentJSON.put("xh", info.getString("xh"));



                String prejudgementString = AES.Encrypt(prejudgmentJSON.toString());

                String JNIParam2 = "/api/l/v6/prejudgment?jsonsports=" + prejudgementString;
                String[] JNIEncrypt = cgapiEnrypt(tokenSplit[3], JNIParam2).split("\\|");

                String timestamp = JNIEncrypt[1];
                String sign = JNIEncrypt[3];


                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add("jsonsports", prejudgementString);


                Request request = new Request
                        .Builder()
                        .url(PREJUDGMENT_URL)
                        .header("timestamp", timestamp)
                        .header("sign", sign)
                        .header("cgAuthorization", cgAuthorization)
                        .header("content-type", "application/x-www-form-urlencoded")
                        .header("user-agent", "okhttp/3.10.0")
                        .post(formBodyBuilder.build())
                        .build();


                OkHttpClient client = new OkHttpClient();
                client.newCall(request).execute();
            }
            {


                JSONObject saveSportsJSON = new JSONObject(json.toString());


                saveSportsJSON.put("saveSportsJSON", "Redmi Note 3,25,7.1.2|2.5.9");
                saveSportsJSON.put("planRouteName", "跑步");
                saveSportsJSON.put("alreadyPassPointResult", "");
                saveSportsJSON.put("alreadyPassPoint", "");
                saveSportsJSON.put("routeId", "7");
                saveSportsJSON.put("roundCount", 0);
                saveSportsJSON.put("modementMode", "3");
                saveSportsJSON.put("sportId", UUID.randomUUID().toString());

                saveSportsJSON.put("name", info.getString("xm"));
                saveSportsJSON.put("xh", info.getString("xh"));


                String saveSportsString = AES.Encrypt(saveSportsJSON.toString());

                String JNIParam2 = "/api/l/v6/savesports?jsonsports=" + saveSportsString;
                String[] JNIEncrypt = cgapiEnrypt(tokenSplit[3], JNIParam2).split("\\|");

                String timestamp = JNIEncrypt[1];
                String sign = JNIEncrypt[3];


                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add("jsonsports", saveSportsString);


                Request request = new Request
                        .Builder()
                        .url(SAVE_SPORTS_URL)
                        .header("timestamp", timestamp)
                        .header("sign", sign)
                        .header("cgAuthorization", cgAuthorization)
                        .header("content-type", "application/x-www-form-urlencoded")
                        .header("user-agent", "okhttp/3.10.0")
                        .post(formBodyBuilder.build())
                        .build();


                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                if (body != null) {
                    JSONObject res = new JSONObject(body.string());
                    if (res.getInt("code") != 200) {
                        throw new Exception("Server said: \"" + res.getString("message") + "\"");
                    }
                    return;
                }
                throw new Exception("Unknown error!");
            }
        } else {
            throw new Exception("Rhino JavaScript Engine failed!");
        }
    }
}
