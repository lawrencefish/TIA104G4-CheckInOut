package com.chat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class JedisUtil {
    private static JedisPool jedisPool;

    static {
        //讀取配置文件
        InputStream is = JedisPool.class.getClassLoader().getResourceAsStream("application.properties");
        //創建Properties對象
        Properties pro = new Properties();
        try {
            pro.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //獲取數據，設置到JedisPoolConfig中
        JedisPoolConfig config = new JedisPoolConfig();
        

        //初始化JedisPool
        jedisPool = new JedisPool(config, pro.getProperty("host"), Integer.parseInt(pro.getProperty("port")));
    }

    /**
     * 獲取連接方法
     */
    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 關閉Jedis
     */
    public static void close(Jedis jedisPool) {
        if (jedisPool != null) {
        	jedisPool.close();
        }
    }

}