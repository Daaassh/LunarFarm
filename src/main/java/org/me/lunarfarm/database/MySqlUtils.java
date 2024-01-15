package org.me.lunarfarm.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.me.lunarfarm.Main;
import org.me.lunarfarm.managers.PlayerManager;

import java.sql.*;

public class MySqlUtils {
    static FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    static Connection connection;

    static {
        try {
            connection = MySqlConnector.getDatabase();
            if (connection == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ MySQL ] Erro ao conectar ao banco de dados!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePlayer(Player p) throws SQLException {
        String query = "INSERT INTO LunarFarm (uuid,blocks_break,seeds,level,fortuna,multiplicador,bonus) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement collection = connection.prepareStatement(query);
            collection.setString(1, String.valueOf(p.getUniqueId()));
            collection.setInt(2, 1);
            collection.setLong(3, 1L);
            collection.setInt(4, 1);
            collection.setInt(5, 1);
            collection.setInt(6, 1);
            collection.setInt(7, 1);

            collection.executeUpdate();
            collection.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ MySQL ] Erro ao salvar o jogador " + p.getName() + " no banco de dados!");
            throw new RuntimeException(e);
        }
    }

    public static void updatePlayer(PlayerManager manager, Player p) {
        String query = "UPDATE LunarFarm SET blocks_break = ?,seeds = ?,level = ? ,fortuna = ? ,multiplicador = ?,bonus = ? WHERE uuid = ?";
        try {
            PreparedStatement collection = connection.prepareStatement(query);
            collection.setInt(1, manager.getBlocks_break());
            collection.setLong(2, manager.getSeeds());
            collection.setInt(3, manager.getLevel());
            collection.setInt(4, manager.getFortuna());
            collection.setInt(5, manager.getMultiplicador());
            collection.setInt(6, manager.getBonus());
            collection.setString(7, String.valueOf(manager.getPlayer().getUniqueId()));
            collection.executeUpdate();
            collection.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ MySQL ] Erro ao salvar o jogador " + p.getName() + " no banco de dados!");
            throw new RuntimeException(e);
        }

    }

    public static PlayerManager getPlayer(Player player) throws SQLException {
        String query = "SELECT * FROM LunarFarm WHERE uuid = ?";
        PlayerManager manager = null;
        try (Connection connect = MySqlConnector.getDatabase()) {
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, String.valueOf(player.getUniqueId()));
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    int blocks_break = results.getInt("blocks_break");
                    long seeds = results.getLong("seeds");
                    int level = results.getInt("level");
                    int fortuna = results.getInt("fortuna");
                    int multiplicador = results.getInt("multiplicador");
                    int bonus = results.getInt("bonus");
                    manager = new PlayerManager(blocks_break,seeds, level, fortuna, multiplicador, bonus, player);
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[ MySQL ] " + ChatColor.RED + "Nenhuma informação encontrada para o jogador " + player.getName());
                }
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ MySQL ] Erro ao pegar as informações do jogador " + player.getName());
                throw new RuntimeException(e);
            }
        }

        return manager;
    }


    public static void existPlayer(Player p) {
        String query = "SELECT * FROM LunarFarm WHERE uuid = ?";
        try (Connection connections = MySqlConnector.getDatabase()) {
            try (PreparedStatement preparedStatement = connections.prepareStatement(query)) {
                preparedStatement.setString(1, String.valueOf(p.getUniqueId()));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!(resultSet.next())) {
                        savePlayer(p);
                    } else {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[ MySQL ] O jogador " + p.getName() + " ja existe no banco de dados!");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}




