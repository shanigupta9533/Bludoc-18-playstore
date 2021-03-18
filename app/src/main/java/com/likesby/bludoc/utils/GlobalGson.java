package com.likesby.bludoc.utils;

/**
 * Created by RajivLL on 09-Apr-18.
 */



import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class GlobalGson {

    private Gson gson;

    private static final GlobalGson globalGson = new GlobalGson();

    public static GlobalGson get() {
        return globalGson;
    }


    private GlobalGson() {
    }

    public Gson getGson() {
        if (gson == null) {
            //gson = new GsonBuilder().create();//new Gson();
            gson = new GsonBuilder().serializeNulls().create();//new Gson();
            //gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();//new Gson();
            //gson = new GsonBuilder().registerTypeAdapter(Visitor.class, new GenericAdapter()).create();//new Gson();

        }
        return gson;
    }

    public <T> String toJson(@NonNull final T t) {
        return getGson().toJson(t);

    }

    public <T> JsonObject getAsJsonObject(@NonNull final T t) {
        return getGson().toJsonTree(t).getAsJsonObject();
    }

    public <T> T fromJson(@NonNull final String o, @NonNull Class<T> tClass) {
        return getGson().fromJson(o, tClass);
    }

    public <T> T fromJson(@NonNull final JsonElement o, @NonNull Class<T> tClass) {
        return getGson().fromJson(o, tClass);
    }

    public <E> ArrayList<E> listFromJson(@NonNull final String o, @NonNull final Type listType) {
        return getGson().fromJson(o, listType);
    }

    public <E> ArrayList<E> listFromJson(@NonNull final JsonElement o, @NonNull final Type listType) {
        return getGson().fromJson(o, listType);
    }

    public <E> ArrayList<E> listFromJson(@NonNull final String o, @NonNull final Class<E> eClass) {
        return getGson().fromJson(o, new ListType<>(eClass));
    }

    public <E> ArrayList<E> listFromJson(@NonNull final JsonElement o, @NonNull final Class<E> eClass) {
        return getGson().fromJson(o, new ListType<>(eClass));
    }

    private static class ListType<E> implements ParameterizedType {

        private Class<?> wrapped;

        private ListType(Class<E> wrapped) {
            this.wrapped = wrapped;
        }

        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        public Type getRawType() {
            return ArrayList.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

}