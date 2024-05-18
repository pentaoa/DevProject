package edu.sustech.students.ura.devproject.io;

import edu.sustech.students.ura.devproject.util.SerializationUtil;

/**
 * SerializeTest
 * 序列化测试
 * @version 1.0
 * 序列化测试用于测试序列化和反序列化
 */

public class SerializeTest {
    public static void main(String[] args) {
        Player player = new Player("exampleUsername", "examplePassword");

        // 序列化到文件
        SerializationUtil.serializePlayer(player, "playerData.plck");

        // 从文件反序列化
        Player deserializedPlayer = SerializationUtil.deserializePlayer("playerData.plck");
    }
}
