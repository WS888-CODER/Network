
package phase1;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    private List<Client> players = new ArrayList<>();
    private final int MAX_PLAYERS = 4;  // Only 4 players allowed in the playing room

    public boolean addPlayer(Client player) {
        if (players.size() < MAX_PLAYERS) {  // Only add player if room isn't full
            players.add(player);
            return true;
        }
        return false;  // Room is full, player can't be added
    }

    public boolean isFull() {
        return players.size() == MAX_PLAYERS;  // Room is full when it has 4 players
    }
    

    public String getPlayersList() {
        StringBuilder playersList = new StringBuilder("Players in the playing room: ");
        for (Client player : players) {
            playersList.append(player.getUsername()).append(", ");
        }
        if (playersList.length() > 0) {
            playersList.setLength(playersList.length() - 2);  // Remove the last comma
        }
        return playersList.toString();
    }

    public List<Client> getPlayers() {
        return players;
    }
}