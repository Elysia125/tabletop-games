package org.example.tabletopgames;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Login {
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;

    @FXML
    protected void onGameStartButtonClick() throws SQLException, ClassNotFoundException {
        String username = nameField.getText().trim(); // 去除前后空格
        String password = passwordField.getText().trim();

        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "用户名和密码不能为空！");
            return;
        }

        // 加载MySQL JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 建立数据库连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://47.93.160.179:3306/user", "user", "123456");

        // 使用PreparedStatement避免SQL注入
        String query = "SELECT * FROM userinfo WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        // 执行查询
        ResultSet resultSet = preparedStatement.executeQuery();

        // 判断是否有匹配的用户
        if (resultSet.next()) {
            // 用户名和密码匹配
            System.out.println("Login successful!");
            // 进行游戏准备或其他业务逻辑
        } else {
            // 用户名或密码不匹配
            showAlert("Error", "用户名不存在或密码错误！");
        }

        // 关闭连接和其他资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }

    // 显示弹窗提示
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onRegisterButtonClick() throws IOException {
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close(); // Close the login window
        // 创建新的Stage用于注册页面
        Stage registerStage = new Stage();

        // 加载注册页面的FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml")); // 确保路径正确
        Parent root = loader.load();

        // 设置场景并显示注册页面
        Scene scene = new Scene(root);
        registerStage.setScene(scene);
        registerStage.setTitle("注册"); // 设置窗口标题
        registerStage.show();

    }

}
