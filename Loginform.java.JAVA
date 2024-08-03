import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOk;
    private JButton btnCancel;
    private JPanel LoginPanel;



    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email=tfEmail.getText();
                String password=String.valueOf(pfPassword.getPassword());

                user=getAuthenticatedUser(email,password);
                if(user != null){
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email and Password invalid","Try Again",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
    public static User user;
    public User getAuthenticatedUser(String email, String password) {
        User user=null;
        final String DB_URL="jdbc:mysql://localhost/mystore?serverTimezone=UTC";
        final String USERNAME="root";
        final String PASSWORD="";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //connected successfully
            Statement stmt = conn.createStatement();
            String sql="SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedstatement=conn.prepareStatement(sql);
            preparedstatement.setString(1,email);
            preparedstatement.setString(2,password);

            ResultSet resultset=preparedstatement.executeQuery();

            if(resultset.next()){
                user=new User();
                user.name=resultset.getString("name");
                user.email=resultset.getString("email");
                user.phone=resultset.getString("phone");
                user.address=resultset.getString("address");
                user.password=resultset.getString("password");
            }
            stmt.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;

    }

    public static void main(String args[])
    {
        LoginForm loginform = new LoginForm(null);
        User user=loginform.user;
        if(user !=null){
            System.out.println("Successfull Authentication of" +""+user.name);
            System.out.println("       Name :" +user.name);
            System.out.println("       Email :" +user.email);
            System.out.println("       Phone :" +user.phone);
            System.out.println("       Address :"+user.address);
            System.out.println("       password :" +user.password);

        }
        else{
            System.out.println("Authentication cancelled");
        }
    }
}
