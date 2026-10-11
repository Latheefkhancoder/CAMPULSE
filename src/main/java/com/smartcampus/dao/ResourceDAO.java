package com.smartcampus.dao;

import com.smartcampus.model.Resource;
import com.smartcampus.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    // ====================================
    // GET ALL RESOURCES
    // ====================================

    public List<Resource> getAllResources() {

        List<Resource> resources =
                new ArrayList<>();

        String sql = """
                SELECT resource_id,
                       resource_name,
                       resource_type,
                       location,
                       capacity,
                       availability_status
                FROM resources
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

                Resource resource =
                        new Resource(
                                resultSet.getInt(
                                        "resource_id"
                                ),
                                resultSet.getString(
                                        "resource_name"
                                ),
                                resultSet.getString(
                                        "resource_type"
                                ),
                                resultSet.getString(
                                        "location"
                                ),
                                resultSet.getInt(
                                        "capacity"
                                ),
                                resultSet.getString(
                                        "availability_status"
                                )
                        );

                resources.add(resource);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return resources;
    }

    // ====================================
    // ADD RESOURCE
    // ====================================

    public boolean addResource(
            Resource resource
    ) {

        String sql = """
                INSERT INTO resources
                (
                    resource_name,
                    resource_type,
                    location,
                    capacity,
                    availability_status
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
                    resource.getResourceName()
            );

            statement.setString(
                    2,
                    resource.getResourceType()
            );

            statement.setString(
                    3,
                    resource.getLocation()
            );

            statement.setInt(
                    4,
                    resource.getCapacity()
            );

            statement.setString(
                    5,
                    resource.getAvailabilityStatus()
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // ====================================
    // UPDATE RESOURCE
    // ====================================

    public boolean updateResource(
            Resource resource
    ) {

        String sql = """
                UPDATE resources
                SET resource_name = ?,
                    resource_type = ?,
                    location = ?,
                    capacity = ?,
                    availability_status = ?
                WHERE resource_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    resource.getResourceName()
            );

            statement.setString(
                    2,
                    resource.getResourceType()
            );

            statement.setString(
                    3,
                    resource.getLocation()
            );

            statement.setInt(
                    4,
                    resource.getCapacity()
            );

            statement.setString(
                    5,
                    resource.getAvailabilityStatus()
            );

            statement.setInt(
                    6,
                    resource.getResourceId()
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // ====================================
    // DELETE RESOURCE
    // ====================================

    public boolean deleteResource(
            int resourceId
    ) {

        String sql = """
                DELETE FROM resources
                WHERE resource_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    resourceId
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}