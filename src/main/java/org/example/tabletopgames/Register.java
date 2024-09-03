package org.example.tabletopgames;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.regex.Pattern;

public class Register {

    @FXML
    private TextField nameField = new TextField();
    @FXML
    private PasswordField passwdField = new PasswordField();
    @FXML
    private PasswordField passwdField2 = new PasswordField();

    // 显示弹窗的辅助方法
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 检查密码有效性的方法
    private boolean isPasswordValid(String password) {
        // 密码长度在 8 到 20 位之间，且至少包含一个字母和一个数字
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$";
        return Pattern.matches(passwordPattern, password);
    }

    @FXML
    protected void onRegisterButtonClick() {
        String username = nameField.getText().trim(); // 去除前后空格
        String password = passwdField.getText().trim();
        String password2 = passwdField2.getText().trim();

        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            showAlert("错误", "用户名和密码不能为空", Alert.AlertType.ERROR);
            return; // 结束方法执行
        }
        // 检查密码长度和内容
        if (!isPasswordValid(password)) {
            showAlert("错误", "密码必须在 8 到 20 位之间，且至少包含一个字母和一个数字", Alert.AlertType.ERROR);
            return; // 结束方法执行
        }

        try {
            // 加载MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立数据库连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://47.93.160.179:3306/user", "user", "123456");

            // 创建Statement对象
            Statement statement = connection.createStatement();

            // 执行SELECT查询
            String query = "SELECT * FROM userinfo"; // 替换为你的查询语句
            ResultSet resultSet = statement.executeQuery(query);

            boolean userExists = false;

            // 处理结果集
            while (resultSet.next()) {
                String name = resultSet.getString("username");
                if (name.equals(username)) {
                    userExists = true; // 用户名已存在
                    break; // 找到已存在的用户名后退出循环
                }
            }

            // 如果用户名不存在，插入新用户
            if (!userExists) {
                // 确保密码一致
                if (!password.equals(password2)) {
                    showAlert("错误", "两次密码输入不一致", Alert.AlertType.ERROR);
                } else {
                    // 使用PreparedStatement来防止SQL注入
                    String insertQuery = "INSERT INTO userinfo (username, password) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    preparedStatement.executeUpdate();

                    showAlert("成功", "用户注册成功", Alert.AlertType.INFORMATION);
                    // 注册成功后，可以清空输入框或进行其他操作
                }
            } else {
                showAlert("错误", "用户名已存在", Alert.AlertType.ERROR);
            }

            // 关闭资源
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            showAlert("错误", "数据库驱动未找到", Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (SQLException e) {
            showAlert("错误", "数据库操作失败", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        System.out.println("Register button clicked");
    }

    @FXML
    protected void onResetButtonClick() {
        nameField.setText("");
        passwdField.setText("");
        passwdField2.setText("");
        showAlert("成功", "已重置", Alert.AlertType.INFORMATION);
        System.out.println("Reset button clicked");
    }
}
