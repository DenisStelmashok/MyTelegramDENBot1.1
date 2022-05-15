package telegramBot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    //de015373a6769187ab6cc36edb8de6e8

    public static String getWeather(String city, Model model) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=9f5856f5d6be0a83ce02c05f02821811");
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }
        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + " C " + "\n" +
                "Humidity: " + model.getHumidity() + " % " + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + ".png";
    }
}
