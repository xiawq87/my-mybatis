package com.xwq.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtil {
    private static DataSource dataSource;

    static {
        try {
            InputStream inputStream = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void execute(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            release(connection, statement);
        }
    }

    public static <T> T select(String sql, Class<T> entityClz) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();

            T t = entityClz.newInstance();

            Field[] fields = entityClz.getDeclaredFields();
            for(Field field : fields) {
                field.setAccessible(true);

                String fieldName = field.getName();
                Class<?> type = field.getType();

                if(Long.class.getName().equals(type.getName())) {
                    long fieldValue = resultSet.getLong(fieldName);
                    field.set(t, fieldValue);
                } else if(String.class.getName().equals(type.getName())) {
                    String fieldValue = resultSet.getString(fieldName);
                    field.set(t, fieldValue);
                }
            }

            return t;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            release(connection, statement);
        }
        return null;
    }

    private static void release(Connection connection, Statement statement) {
        try {
            if(statement != null) statement.close();
            if(connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
