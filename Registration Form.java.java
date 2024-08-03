import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JTextField pfPassword;
    private JTextField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public  RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Create a New Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registeruser();
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

    private void registeruser() {
        String name=tfName.getText();
        String email= tfEmail.getText();
        String phone= tfPhone.getText();
        String address= tfAddress.getText();
        String password= String.valueOf(pfPassword.getText());
        String confirmPassword=String.valueOf(pfConfirmPassword.getText());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() ){
           JOptionPane.showMessageDialog(this,
                   "Please Enter Field","Try Again",
                   JOptionPane.ERROR_MESSAGE);
           return;
        }
        if( !password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match","Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
       user= addUserToDatabase(name, email,phone,address,password);
        if(user != null){
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user","Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    public static User user;
    private User addUserToDatabase (String name,String email,String phone,String address,String password){
        User user=null;
        final String DB_URL="jdbc:mysql://localhost/mystore?serverTimezone=UTC";
        final String USERNAME="root";
        final String PASSWORD="";

        try{
            Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            //connected successfully

            Statement stmt = conn.createStatement();
            String sql="INSERT INTO users(name,email,phone,address,password)" +"VALUES (?,?,?,?,?)";
            PreparedStatement preparedstatement=conn.prepareStatement(sql);
            preparedstatement.setString(1,name);
            preparedstatement.setString(2,email);
            preparedstatement.setString(3,phone);
            preparedstatement.setString(4,address);
            preparedstatement.setString(5,password);
            int addedRows =  preparedstatement.executeUpdate();
            if(addedRows >0){
                user=new User();
                user.name=name;
                user.email=email;
                user.phone=phone;
                user.address=address;
                user.password=password;

            }
            stmt.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String args[]){
        RegistrationForm myform =new  RegistrationForm(null);
        if(user !=null){
            System.out.println("Successfull registration of :" + user.name);
        }
        else{
            System.out.println("Registration cancel");
        }
    }
}
