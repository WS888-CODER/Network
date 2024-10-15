
package newserver;

import java.util.ArrayList;


public class All_Players_Array {
    public static ArrayList<Client> list = new ArrayList<>();
    
    public static void addPlayer(Client player) {
        list.add(player);
    }
}
