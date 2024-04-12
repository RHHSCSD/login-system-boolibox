/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;

/**
 *
 * @author micha
 */
public class RegistrationSystem {
    LoginSystem loginSystem;
    public RegistrationSystem(LoginSystem loginSystem){
        this.loginSystem = loginSystem;
    }
    public int register(User u){
        if(loginSystem.userExists(u, false) != -1){
            return 1;
        }
        else if(loginSystem.strongPassword(u.getPassword()) == false){
            return 2;
        }
        else{
            String salt = (int)(Math.random() * 100000) + "";
            u.setSalt(salt);
            u.setPassword(loginSystem.encryptPassword(u.getPassword(), salt));
            loginSystem.users.add(u);
            System.out.println(loginSystem.users);
            loginSystem.save();
            return 0;
        }
    }
}