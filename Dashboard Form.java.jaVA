import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public DashboardForm() {
        super();
        setTitle("DASHBOARD");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(700, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        boolean hasRegisteredUsers = connectToDatabase();

        if (hasRegisteredUsers) {
            LoginForm loginform = new LoginForm(null);
            User user = loginform.user;
            if (user != null) {
                lbAdmin.setText("User :" + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                dispose();
            }
        } else {
            RegistrationForm registrationform = new RegistrationForm(this);
            User user = registrationform.user;
            if (user != null) {
                lbAdmin.setText("User :" + user.name);
                setLocationRelativeTo(null);
                setVisible(true);

            } else {
                dispose();
            }
        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationform =new  RegistrationForm(DashboardForm.this);
                User user= registrationform.user;
                if(user !=null){
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New user" +user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUsers=true;
        final String MYSQL_SERVER_URL="jdbc:mysql://localhost/";
        final String DB_URL="jdbc:mysql://localhost/mystore?serverTimezone=UTC";
        final String USERNAME="root";
        final String PASSWORD="";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //connected successfully
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mystore");
            statement.close();
            conn.close();

             conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            statement=conn.createStatement();
            String sql="CREATE TABLE IF NOT EXISTS users (" +"id INT (10) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    +" name VARCHAR (200) NOT NULL," +"email VARCHAR (200) NOT NULL UNIQUE," +"phone VARCHAR(200),"
                   +" address VARCHAR (200)," + "password VARCHAR (200) NOT NULL)" +")";
            statement.executeUpdate(sql);

            statement = conn.createStatement();
            ResultSet resultset=statement.executeQuery("SELECT COUNT(*) FROM users");

            if(resultset.next()){
                int numUsers=resultset.getInt(1);

                if(numUsers >0){
                    hasRegisteredUsers=true;
                }
            }
            statement.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }
 public static void main(String args[]){
        DashboardForm myform = new DashboardForm();
 }

}

