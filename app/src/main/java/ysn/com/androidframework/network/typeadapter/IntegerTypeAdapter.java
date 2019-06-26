package ysn.com.androidframework.network.typeadapter;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import ysn.com.androidframework.util.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName IntegerTypeAdapter
 * @Description 一句话概括作用
 * @Date 2019/6/26
 * @History 2019/6/26 author: description:
 */
public class IntegerTypeAdapter  extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter out, Integer value) {
        try {
            if (value == null){
                value = 0;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer read(JsonReader in) {
        try {
            Integer value;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                Log.e("TypeAdapter", "null is not a number");
                return 0;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                boolean b = in.nextBoolean();
                Log.e("TypeAdapter", b + " is not a number");
                return 0;
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isInt(str)){
                    return Integer.parseInt(str);
                } else {
                    Log.e("TypeAdapter", str + " is not a int number");
                    return 0;
                }
            } else {
                value = in.nextInt();
                return value;
            }
        } catch (Exception e) {
            Log.e("TypeAdapter", "Not a number", e);
        }
        return 0;
    }
}
