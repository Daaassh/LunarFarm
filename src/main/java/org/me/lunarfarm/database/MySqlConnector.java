package org.me.lunarfarm.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.configs.CustomFileConfiguration;


import java.io.IOException;
import java.net.URLDecoder;
import java.sql.*;

public class MySqlConnector {
    static FileConfiguration mysql = Main.getPlugin(Main.class).getConfig();

    public MySqlConnector() throws SQLException {
        connect();
    }

    static Integer porta = mysql.getInt("mysql.port");
    static String usuario = mysql.getString("mysql.user");
    static String senhaCodificada = mysql.getString("mysql.password");
    static String nomeBanco = mysql.getString("mysql.database");
    static String host = mysql.getString("mysql.host");

    static String senhaDecodificada = null;
    static Connection connection;
    static String url = "jdbc:mysql://" + host + ":" + porta + "/" + nomeBanco;

    public static void connect() {
        try {
            senhaDecodificada = URLDecoder.decode(senhaCodificada, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, usuario, senhaDecodificada);
            if (!(tableExists("LunarFarm"))) {
                try {
                    createTable();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[ Lunar Farm ] " + ChatColor.GREEN +" Tabelas criadas com sucesso");
                }catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ MySQL ] Erro ao criar as tabelas");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getDatabase() throws SQLException {

        return DriverManager.getConnection(url, usuario, senhaDecodificada);
    }

    private static boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);
        return resultSet.next();
    }

    private static void createTable() throws SQLException {
        String createTableQuery = "CREATE TABLE LunarFarm (UUID VARCHAR(255), blocks_break INT, seeds LONG, level INT,fortuna INT, bonus INT,multiplicador INT)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        }
    }

}
