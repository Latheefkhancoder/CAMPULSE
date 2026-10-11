package com.smartcampus.dao;

import com.smartcampus.model.Equipment;
import com.smartcampus.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    // ====================================
    // GET ALL EQUIPMENT
    // ====================================

    public List<Equipment> getAllEquipment() {

        List<Equipment> equipmentList =
                new ArrayList<>();

        String sql = """
                SELECT equipment_id,
                       equipment_name,
                       category,
                       quantity,
                       available_quantity,
                       condition_status
                FROM equipment
                ORDER BY equipment_id
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Equipment equipment =
                        new Equipment(
                                resultSet.getInt(
                                        "equipment_id"
                                ),

                                resultSet.getString(
                                        "equipment_name"
                                ),

                                resultSet.getString(
                                        "category"
                                ),

                                resultSet.getInt(
                                        "quantity"
                                ),

                                resultSet.getInt(
                                        "available_quantity"
                                ),

                                resultSet.getString(
                                        "condition_status"
                                )
                        );

                equipmentList.add(equipment);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return equipmentList;
    }

    // ====================================
    // ADD EQUIPMENT
    // ====================================

    public boolean addEquipment(
            Equipment equipment
    ) {

        String sql = """
                INSERT INTO equipment
                (
                    equipment_name,
                    category,
                    quantity,
                    available_quantity,
                    condition_status
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    equipment.getEquipmentName()
            );

            statement.setString(
                    2,
                    equipment.getCategory()
            );

            statement.setInt(
                    3,
                    equipment.getQuantity()
            );

            statement.setInt(
                    4,
                    equipment.getAvailableQuantity()
            );

            statement.setString(
                    5,
                    equipment.getConditionStatus()
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // ====================================
    // UPDATE EQUIPMENT
    // ====================================

    public boolean updateEquipment(
            Equipment equipment
    ) {

        String sql = """
                UPDATE equipment
                SET equipment_name = ?,
                    category = ?,
                    quantity = ?,
                    available_quantity = ?,
                    condition_status = ?
                WHERE equipment_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    equipment.getEquipmentName()
            );

            statement.setString(
                    2,
                    equipment.getCategory()
            );

            statement.setInt(
                    3,
                    equipment.getQuantity()
            );

            statement.setInt(
                    4,
                    equipment.getAvailableQuantity()
            );

            statement.setString(
                    5,
                    equipment.getConditionStatus()
            );

            statement.setInt(
                    6,
                    equipment.getEquipmentId()
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // ====================================
    // DELETE EQUIPMENT
    // ====================================

    public boolean deleteEquipment(
            int equipmentId
    ) {

        String sql = """
                DELETE FROM equipment
                WHERE equipment_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    equipmentId
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}