package it.polimi.ingsw.server.model.decorators;


import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;
public class DemeterDecoratorTest {

    Commands commands1,commands2;
    Match match=new Match(2);
    Player player1=new Player(1,"leo",match);
    Player player2=new Player(2,"dario",match);
    Position p;

    @Test
    public void nextState() {
        match.addPlayer(player1);
        match.setPlayersNum(2);
        match.addPlayer(player2);
        match.addCard(GodCards.Apollo);
        match.addCard(GodCards.Artemis);
        player1.setCommands(GodCards.Artemis);
        player2.setCommands(GodCards.Apollo);
        commands1=player1.getCommands();
        commands2=player2.getCommands();
    }

    @Test
    public void build() {
    }

    @Test
    public void computeAvailableBuildings() {
    }

    @Test
    public void computeAvailableSpecialBuildings() {
    }
}