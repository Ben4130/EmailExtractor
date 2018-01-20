/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author benphillips
 */
class UserConfiguration {
    String userName, hostName, protocol;
    Integer port;
    
    public UserConfiguration(){
    }
    
    public UserConfiguration(String pUserName,
                            String pHostName, 
                            String pProtocol, 
                            Integer pPort){
        userName = pUserName;
        hostName = pHostName;
        protocol = pProtocol;
        port = pPort;
    }
    
    
}
