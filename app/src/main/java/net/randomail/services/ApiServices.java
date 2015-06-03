package net.randomail.services;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.randomail.RandomailApplication;
import net.randomail.models.Email;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by baptistejamin on 02/06/15.
 */
public class ApiServices {

    static String path = "http://alpha.randomail.net:8080/v1";

    public static void login(String email, String password, JsonHttpResponseHandler callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject query = null;
        StringEntity se = null;
        try {

            query = new JSONObject();
            query.put("email", email);
            query.put("password", password);
            se = new StringEntity(query.toString(), "UTF-8");

            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8"));
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "text/plain;charset=UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(null, path + "/user/login", se, "application/json", callback);
    }

    public static void register(String email, String password, JsonHttpResponseHandler callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject query = null;
        StringEntity se = null;
        try {

            query = new JSONObject();
            query.put("email", email);
            query.put("password", password);
            se = new StringEntity(query.toString(), "UTF-8");

            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8"));
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "text/plain;charset=UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(null, path + "/user/register", se, "application/json", callback);
    }

    public static void getEmails(JsonHttpResponseHandler callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(RandomailApplication.mUserId, RandomailApplication.mAuthToken);
        client.get(path + "/emails", callback);
    }

    public static void updateEmail(Email email, JsonHttpResponseHandler callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(RandomailApplication.mUserId, RandomailApplication.mAuthToken);

        JSONObject query = null;
        StringEntity se = null;
        try {

            query = new JSONObject();
            query.put("paused", email.paused);
            se = new StringEntity(query.toString(), "UTF-8");

            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8"));
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "text/plain;charset=UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.put(null, path + "/email/" + email.email, se, "application/json", callback);
    }

    public static void createEmail(Email email, JsonHttpResponseHandler callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(RandomailApplication.mUserId, RandomailApplication.mAuthToken);

        JSONObject query = null;
        StringEntity se = null;
        try {

            query = new JSONObject();
            query.put("email", email.email);
            if (email.label.length() > 0) {
                query.put("label", email.label);
            }
            se = new StringEntity(query.toString(), "UTF-8");

            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8"));
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "text/plain;charset=UTF-8"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(null, path + "/email/", se, "application/json", callback);
    }

    public static void deleteEmail(Email email, JsonHttpResponseHandler callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(RandomailApplication.mUserId, RandomailApplication.mAuthToken);
        client.delete(path + "/email/" + email.email, callback);
    }

    public static void getUser(JsonHttpResponseHandler callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(RandomailApplication.mUserId, RandomailApplication.mAuthToken);
        client.delete(path + "/user/", callback);
    }
}
