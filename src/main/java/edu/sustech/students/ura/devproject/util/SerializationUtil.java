package edu.sustech.students.ura.devproject.util;

import edu.sustech.students.ura.devproject.io.Player;

import java.io.*;

public class SerializationUtil {

    public static void serializePlayer(Player player, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Player deserializePlayer(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Player) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}