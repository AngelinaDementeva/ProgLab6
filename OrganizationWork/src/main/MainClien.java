package main;

import data.OrganizationWork;
import serviceClient.ClientSocket;


/**
 * Тестовый класс, содержащий точку входа в программу
 * @author Admin
 */
public class MainClien {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ClientSocket clientSocket = new ClientSocket(5000, 2500);
        OrganizationWork organizationWork1 = new OrganizationWork(clientSocket);
        organizationWork1.run();
        
    }
    
}
