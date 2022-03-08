package com.dbc.repository;

import com.dbc.entities.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<Integer, User> {

    @Override
    public Integer getNextId(Connection connection) throws SQLException {
        String sql = "SELECT DONATOR_PROJECT.users_seq.nextval mysequence FROM DUAL";

        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery(sql);

        if (res.next()) {
            return res.getInt("mysequence");
        }

        return null;
    }

    @Override
    public User add(User user) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionDB.getConnection();

            Integer nextId = this.getNextId(conn);
            user.setIdUser(nextId);

            String sql = "INSERT  INTO DONATOR_PROJECT.USERS (id_user, name, email, password, type, document)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, user.getIdUser());
            statement.setString(2, user.getName());
            statement.setString(4, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(5, user.getType());
            statement.setString(6, user.getDocument());


            int res = statement.executeUpdate();
            System.out.println("adduser.res=" + res);
            return user;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean remove(Integer id) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionDB.getConnection();

            String sql = "DELETE FROM USERS WHERE id_user = ?";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, id);

            int res = statement.executeUpdate();
            System.out.println("removeUserId.res=" + res);

            return res > 0;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean update(Integer id, User user) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionDB.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE USERS SET ");
            sql.append(" name = ?,");
            sql.append(" email = ?,");
            sql.append(" password = ? ");
            sql.append(" WHERE id_user = ?");

            PreparedStatement stmt = conn.prepareStatement(sql.toString());

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());

            int res = stmt.executeUpdate();
            System.out.println("updateuser.res=" + res);

            return res > 0;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> list() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ConnectionDB.getConnection();

            Statement stmt = conn.createStatement();

            String sql = "SELECT * USERS";

            ResultSet res = stmt.executeQuery(sql);

            // TODO - arrumar isso, mas depende dos metódos das outras classes
            while (res.next()) {
                User user = new User();
                user.setIdUser(res.getInt("id_user"));
                user.setName(res.getString("name"));
                user.setEmail(res.getString("email"));
                user.setPassword(res.getString("password"));
                user.setType(res.getInt("type"));
                user.setDocument(res.getString("document"));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }
}