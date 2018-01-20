/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.emailtextextractor;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
/**
 *
 * @author benphillips
 */
class MailServerAccess {
    
    public static void getFolderList(UserConfiguration userConfig, String pw){
        Properties properties = new Properties();
        
        properties.put("mail." + userConfig.protocol + ".host",userConfig.hostName);
        properties.put("mail." + userConfig.protocol + ".port",userConfig.port);
        properties.put("mail." + userConfig.protocol + ".starttls.enable","true");
        
        Session emailSession = null;
        
        try{
        emailSession = Session.getDefaultInstance(properties);
        }catch (Exception e){
            System.out.println(e);
        }
        
        try {
            Store store = emailSession.getStore(userConfig.protocol + "s");
            store.connect(userConfig.hostName, userConfig.userName, pw);
            
            //Folder emailFolder = store.getFolder("INBOX");
            //emailFolder.open(Folder.READ_ONLY);
            
            //Message[] messages = emailFolder.getMessages();
            
            //System.out.println("Message no: " + messages.length);
            Folder[] f = store.getDefaultFolder().list("*");
            for(Folder fd:f){
                System.out.println(">> "+fd.getName());
            }
            
            //emailFolder.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(MailServerAccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MailServerAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
