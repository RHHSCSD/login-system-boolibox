/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package loginsystem;
import java.util.*;
import java.io.*;
import java.security.*;

/**
 *
 * @author Michael Zheng
 */
public class LoginSystem {
    final String DELIMITER = ", ";
    final String USERFILE = "src/loginsystem/UserList.txt";
    ArrayList<User> users = new ArrayList<>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
    public int userExists(User u, boolean checkPassword){
        for(int i = 0; i < users.size(); i++){
            if(users.get(i) == u){
                return i;
            }
            if(users.get(i).getUsername().equals(u.getUsername())){
                if(!checkPassword){
                    return i;
                }
                else if(u.getPassword().equals(users.get(i).getPassword())){
                    return i;
                }
            }
        }
        return -1;
    }
    public void save(){
        PrintWriter pw;
        try{
            pw = new PrintWriter(new FileWriter(USERFILE, false));
            for(int i = 0; i < users.size(); i++){
                pw.print(users.get(i).getUsername() + DELIMITER);
                pw.print(users.get(i).getPassword() + DELIMITER);
                pw.print(users.get(i).getEmail() + DELIMITER);
                pw.print(users.get(i).getPhoneNumber() + DELIMITER);
                pw.print(users.get(i).getAge() + DELIMITER);
                pw.println(users.get(i).getSalt());
            }
            pw.close();
        }
        catch(Exception e){
            
        }
    }
    public boolean login(String username, String password){
        User u = new User(username, password);
        int userLocation = userExists(u, false);
        if(userLocation != -1){
            String salt = users.get(userLocation).getSalt();
            String encryptedPassword = encryptPassword(password, salt);
            if(encryptedPassword.equals(users.get(userLocation).getPassword())){
                return true;
            }
        }
        return false;
    }
    public void loadUsers(){
        try{
            Scanner sc = new Scanner(new File(USERFILE));
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] tokens = line.split(DELIMITER);
                String username = tokens[0];
                String password = tokens[1];
                String email = tokens[2];
                String phoneNumber = tokens[3];
                int age = Integer.parseInt(tokens[4]);
                String salt = tokens[5];
                User u = new User(username, password, email, phoneNumber, age);
                u.setSalt(salt);
                users.add(u);
            }
        }
        catch(Exception e){
            
        }
    }
    public boolean strongPassword(String password){
        if(password.length() < 8){
            return false;
        }
        boolean upperCase = false;
        boolean lowerCase = false;
        boolean number = false;
        boolean symbol = false;
        for(int i = 0; i < password.length(); i++){
            char letter = password.charAt(i);
            if(Character.isLowerCase(letter)){
                lowerCase = true;
            }
            else if(Character.isUpperCase(letter)){
                upperCase = true;
            }
            else if(Character.isDigit(letter)){
                number = true;
            }
            else{
                symbol = true;
            }
        }
        return lowerCase && upperCase && number && symbol;
    }
    public String encryptPassword(String password, String salt){
        //java helper class to perform encryption
        try{
            password += salt;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            //perform the encryption
            byte byteData[] = md.digest();
            //To express the byte data as a hexadecimal number (the normal way)
            String encryptedPassword="";
            for (int i = 0; i < byteData.length; ++i) {
                encryptedPassword += (Integer.toHexString((byteData[i] & 0xFF) |
                0x100).substring(1,3));
            }
            return encryptedPassword;
        }
        catch(Exception e){
            
        }
        return password;
    }
}