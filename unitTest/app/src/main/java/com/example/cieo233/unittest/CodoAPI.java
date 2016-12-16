package com.example.cieo233.unittest;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Cieo233 on 12/10/2016.
 */

public class CodoAPI {

    public static void userLogin(User user, final Handler handler) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username", user.getUsername())
                .add("password", user.getPassword())
                .add("expiry_time", String.valueOf(Integer.MAX_VALUE))
                .build();
        Request request = new Request.Builder()
                .url("http://api.sysu.space/api/user/login")
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("ret") == StateCode.OK) {
                        CurrentUser.getInstance().setUser(new Gson().fromJson(jsonObject.getString("user"), User.class));
                    }
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void userRegister(User user, final Handler handler) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username", user.getUsername())
                .add("password", user.getPassword())
                .build();
        Request request = new Request.Builder()
                .url("http://api.sysu.space/api/user/register")
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getReminders(final Handler handler) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.sysu.space/api/reminder").newBuilder();
        urlBuilder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("ret") == StateCode.OK) {
                        CurrentUser.getInstance().setReminders((List<Reminder>) new Gson().fromJson(jsonObject.getString("reminders"), new TypeToken<List<Reminder>>() {
                        }.getType()));
                    }
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void createReminder(Reminder reminder, final Handler handler) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.sysu.space/api/reminder").newBuilder();
        urlBuilder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add(Reminder.TITLE, reminder.getTitle());
        bodyBuilder.add(Reminder.PRIORITY, String.valueOf(reminder.getPriority()));
        bodyBuilder.add(Reminder.TYPE, String.valueOf(reminder.getType()));
        bodyBuilder.build();
        if (reminder.getChannel() != null){
            bodyBuilder.add(Reminder.CHANNEL_ID, String.valueOf(reminder.getChannel().getId()));
        }
        if (reminder.getContent() != null){
            bodyBuilder.add(Reminder.CONTENT, reminder.getContent());
        }
        if (reminder.getDue() != null){
            bodyBuilder.add(Reminder.DUE, reminder.getDue());
        }
        RequestBody formBody = bodyBuilder.build();
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void updateReminder(Reminder reminder, final Handler handler) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.sysu.space/api/reminder/" + reminder.getId()).newBuilder();
        urlBuilder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add(Reminder.TITLE, reminder.getTitle());
        bodyBuilder.add(Reminder.PRIORITY, String.valueOf(reminder.getPriority()));
        bodyBuilder.build();
        if (reminder.getContent() != null){
            bodyBuilder.add(Reminder.CONTENT, reminder.getContent());
        } else {
            bodyBuilder.add(Reminder.CONTENT, "");
        }
        if (reminder.getDue() != null){
            bodyBuilder.add(Reminder.DUE, reminder.getDue());
        } else {
            bodyBuilder.add(Reminder.DUE, "");
        }
        if (reminder.getRemark() != null){
            bodyBuilder.add(Reminder.REMARK, reminder.getRemark());
        } else {
            bodyBuilder.add(Reminder.REMARK, "");
        }
        RequestBody formBody = bodyBuilder.build();
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void deleteReminder(Reminder reminder, final Handler handler) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.sysu.space/api/reminder/" + reminder.getId()).newBuilder();
        urlBuilder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .delete()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getChannels(final Handler handler) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        urlBuilder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        urlBuilder.addEncodedQueryParameter("type", String.valueOf(Channel.UNSUBSCRIBE));
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("ret") == StateCode.OK) {
                        CurrentUser.getInstance().setUnsubscribeChannels((List<Channel>) new Gson().fromJson(jsonObject.getString("channels"), new TypeToken<List<Channel>>() {
                        }.getType()));
                    }
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        urlBuilder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        urlBuilder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        urlBuilder.addEncodedQueryParameter("type", String.valueOf(Channel.SUBSCRIBE));
        request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("ret") == StateCode.OK) {
                        CurrentUser.getInstance().setSubscribeChannels((List<Channel>) new Gson().fromJson(jsonObject.getString("channels"), new TypeToken<List<Channel>>() {
                        }.getType()));
                    }
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        urlBuilder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        urlBuilder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        urlBuilder.addEncodedQueryParameter("type", String.valueOf(Channel.CREATOR));
        request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("ret") == StateCode.OK) {
                        CurrentUser.getInstance().setCreatorChannels((List<Channel>) new Gson().fromJson(jsonObject.getString("channels"), new TypeToken<List<Channel>>() {
                        }.getType()));
                    }
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void createChannel(Channel channel, final Handler handler){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        url_builder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        RequestBody formBody = new FormBody.Builder()
                .add(Channel.NAME, channel.getName())
                .build();
        Request request = new Request.Builder()
                .url(url_builder.build())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public static void joinChannel(Channel channel, final Handler handler){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel/" + String.valueOf(channel.getId())).newBuilder();
        url_builder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        RequestBody formBody = new FormBody.Builder()
                .add(Channel.ACTION, String.valueOf(Channel.JOIN))
                .build();
        Request request = new Request.Builder()
                .url(url_builder.build())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void exitChannel(Channel channel, final Handler handler){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel/" + String.valueOf(channel.getId())).newBuilder();
        url_builder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        RequestBody formBody = new FormBody.Builder()
                .add(Channel.ACTION, String.valueOf(Channel.EXIT))
                .build();
        Request request = new Request.Builder()
                .url(url_builder.build())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    handler.sendEmptyMessage(jsonObject.getInt("ret"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}