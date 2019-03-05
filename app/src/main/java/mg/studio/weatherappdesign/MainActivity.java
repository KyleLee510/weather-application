package mg.studio.weatherappdesign;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.attribute.FileAttribute;
import java.lang.String;
import java.lang.*;
import java.util.Calendar;
import java.util.List;

class Coord {
    double lat;
    double lon;
}

class City {
    int id;
    String name;
    Coord coord;
    String country;
}

class mmain {
    double temp;
    double temp_min;
    double temp_max;
    double pressure;
    double sea_level;
    double grnd_level;
    int humidity;
    double temp_kf;
}

class Clouds {
    int all;
}

class Wind {
    double speed;
    double deg;
}

class Rain {

}

class Sys {
    String pod;
}

class Weather {
    int id;
    String main;
    String description;
    String icon;
}


class LList {
    int dt;
    mmain main;
    List<Weather> weather ;
    Clouds clouds;
    Wind wind;
    Rain rain;
    Sys sys;
    String dt_txt;
}
class Allweather {
    String cod;
    double message;
    int cnt;
    List<LList> list;
    City city;
}


public class MainActivity extends AppCompatActivity {

    public void upDateWeatherIcon(ImageView iconImageView, String theState) {
        switch (theState) {
            case "Clouds":
                iconImageView.setImageResource(R.drawable.partly_sunny_small); //多云
                break;
            case "clear":
                iconImageView.setImageResource(R.drawable.sunny_small);//晴天
                break;
            case "Rain":
                iconImageView.setImageResource(R.drawable.rainy_small);//雨
                break;
            case "Drizzle":
                iconImageView.setImageResource(R.drawable.rainy_small);//雨
                break;
            case "Atmosphere":
                iconImageView.setImageResource(R.drawable.windy_small);//多风
                break;
            default:
                iconImageView.setImageResource(R.drawable.partly_sunny_small); //多云
                break;
        }
    }

    public int getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        return i;
    }

    public String Theday(int i) {
        switch (i) {
            case 1:
                return "SUN";
            case 2:
                return "MON";
            case 3:
                return "TUE";
            case 4:
                return "WED";
            case 5:
                return "THU";
            case 6:
                return "FRI";
            case 7:
                return "SAT";
            default:
                return "";
        }
    }

    public String Refreshing() {
        String stringUrl = "http://api.openweathermap.org/data/2.5/forecast?id=1814906&APPID=a07580e104ea2416e3396574521eb4e7&units=metric";
        HttpURLConnection urlConnection = null;
        BufferedReader reader;

        try {
            URL url = new URL(stringUrl);

            // Create the request to get the information from the server, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Json 的读取
            GsonBuilder gsonBuilder = new GsonBuilder(); //声明Gson
            Gson gson = gsonBuilder.create();
            Allweather allweather = gson.fromJson(reader, Allweather.class);

            //所在地 0
            String information = allweather.city.name;
            information += "\n";

            //当前日期 1
            String date = allweather.list.get(0).dt_txt.substring(0,9);
            //date = date.replace("-","/");
            information += date;
            information += "\n";

            //当前温度 2
            int temp1 = (int) allweather.list.get(0).main.temp;
            information += String.valueOf(temp1);
            information += "\n";


            //第二天温度 3
            int temp2 = (int) allweather.list.get(8).main.temp;
            information += String.valueOf(temp2);
            information += "\n";

            //第三天温度 4
            int temp3 = (int) allweather.list.get(16).main.temp;
            information += String.valueOf(temp3);
            information += "\n";

            //第四天温度 5
            int temp4 = (int) allweather.list.get(24).main.temp;
            information += String.valueOf(temp4);
            information += "\n";

            //第五天温度 6
            int temp5 = (int) allweather.list.get(32).main.temp;
            information += String.valueOf(temp5);
            information += "\n";

            //今日周几 7
            int i = getWeek();
            String today = Theday(i);
            information += today;
            information += "\n";

            //后面四天周几 8 9 10 11
            int j = 1;
            for(j = 1; j < 5; j++) {
                i = i + 1;
                String next = Theday(i);
                information += next;
                information += "\n";
            }

            //今日天气图标更新
            information += allweather.list.get(0).weather.get(0).main;
            information += "\n";
            //后续四天天气图标更新
            information += allweather.list.get(8).weather.get(0).main;
            information += "\n";

            information += allweather.list.get(16).weather.get(0).main;
            information += "\n";

            information += allweather.list.get(24).weather.get(0).main;
            information += "\n";

            information += allweather.list.get(32).weather.get(0).main;
            information += "\n";


            return information;



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void Refreshed(String information) {
        String info[] = information.split("\n");
        ((TextView) findViewById(R.id.tv_location)).setText(info[0]); //所在地
        ((TextView) findViewById(R.id.tv_date)).setText(info[1]); //当前日期
        ((TextView) findViewById(R.id.temperature_of_the_day)).setText(info[2]); //当前温度
        ((TextView) findViewById(R.id.Sec_temp)).setText(info[3]); //明日温度
        ((TextView) findViewById(R.id.Third_temp)).setText(info[4]); //第三日温度
        ((TextView) findViewById(R.id.Fourth_temp)).setText(info[5]); //第四日温度
        ((TextView) findViewById(R.id.Fifth_temp)).setText(info[6]); //第五日温度
        ((TextView) findViewById(R.id.Today)).setText(info[7]); //今日周几
        ((TextView) findViewById(R.id.sec_day)).setText(info[8]); //后续四天周几
        ((TextView) findViewById(R.id.Third_day)).setText(info[9]);
        ((TextView) findViewById(R.id.Fourth_day)).setText(info[10]);
        ((TextView) findViewById(R.id.Fifth_day)).setText(info[11]);
        upDateWeatherIcon( (ImageView) findViewById(R.id.img_weather_condition),info[12]); //更新今日天气状况图标
        upDateWeatherIcon( (ImageView) findViewById(R.id.sec_wea),info[13]); //更新第二日天气状况图标
        upDateWeatherIcon( (ImageView) findViewById(R.id.Third_wea),info[14]); //更新第三日天气状况图标
        upDateWeatherIcon( (ImageView) findViewById(R.id.Fourth_wea),info[15]); //更新第四日天气状况图标
        upDateWeatherIcon( (ImageView) findViewById(R.id.Fifth_wea),info[16]); //更新第五日天气状况图标

        Toast.makeText(this, "已经更新", Toast.LENGTH_SHORT).show();
    }

    public boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            //Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            //Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownloadUpdate().execute();
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String information = null;
            if (isConnectIsNomarl() == true ) {

                information = Refreshing();
                return information;
            }
            else {
                return information;
            }
        }

        @Override
        protected void onPostExecute(String information) {
            //Update the temperature displayed
            if (information == null) {
                return;
            }
            else {
                Refreshed(information);
                String info[] = information.split("\n");
            }
        }
    }
}
