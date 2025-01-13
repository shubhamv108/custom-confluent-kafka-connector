package code.shubham.commons.utils;

import com.google.gson.Gson;

public class JsonUtils {

    private static final Gson GSON = new Gson();

    public static <R> R of(String json, Class<R> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <O> String as(O src) {
        return GSON.toJson(src);
    }

}
