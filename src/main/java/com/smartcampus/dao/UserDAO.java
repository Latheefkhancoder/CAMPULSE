package com.smartcampus.dao;

import com.smartcampus.model.User;
import com.smartcampus.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User login(String email, String password) throws SQLException {
        if (email == null || password == null) {
            return null;
        }

        String sql = """
                SELECT user_id, name, email, password, role
                FROM users
                WHERE email = ? AND password = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.trim());
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                }
            }
        }

        return null;
    }
}