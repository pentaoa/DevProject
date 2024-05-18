package edu.sustech.students.ura.devproject.io;

import edu.sustech.students.ura.devproject.util.SerializationUtil;

public class SerializeTest {
    public static void main(String[] args) {
        Player player = new Player("exampleUsername", "examplePassword");

        // 序列化到文件
        SerializationUtil.serializePlayer(player, "playerData.plck");

        // 从文件反序列化
        Player deserializedPlayer = SerializationUtil.deserializePlayer("playerData.plck");
    }
}
