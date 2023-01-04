package cz.simt.tabule.service;

import cz.simt.tabule.data.PlayersList;
import cz.simt.tabule.repository.PlayersListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayersListService {
    private final PlayersListRepository playersListRepository;

    @Autowired
    public PlayersListService(PlayersListRepository playersListRepository) {
        this.playersListRepository = playersListRepository;
    }

    public void insertPlayerInDatabase(String nick) {
        Optional<PlayersList> player = playersListRepository.findFirstByNick(nick);
        if (player.isEmpty()) {
            playersListRepository.save(new PlayersList(nick));
        }
    }

    public Iterable<PlayersList> getPlayersList() {
        return playersListRepository.findAll();
    }
}
