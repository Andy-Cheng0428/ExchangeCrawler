import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;

/**
 * ExchangeCrawler
 * Usage :
 * Created By Andy on 2020/12/12 21:10:29
 */
public class Singleton {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private Singleton() {

    }

    private static class InstanceHolder {
        private static Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return InstanceHolder.instance;
    }

    public static Gson getGson() {
        return getInstance().gson;
    }

    public static SimpleDateFormat getDateFormat() {
        return getInstance().dateFormat;
    }
}
