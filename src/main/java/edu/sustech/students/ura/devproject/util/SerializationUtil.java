package edu.sustech.students.ura.devproject.util;

import java.io.*;

public class SerializationUtil {

    public static <T> byte[] serialize(T obj) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(obj);
            return byteStream.toByteArray();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> cls) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            return cls.cast(objectStream.readObject());
        }
    }
}