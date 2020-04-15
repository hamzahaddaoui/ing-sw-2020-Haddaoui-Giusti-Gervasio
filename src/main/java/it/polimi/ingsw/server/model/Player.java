package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;

import java.util.*;

import static it.polimi.ingsw.server.model.TurnState.*;

/**
 * Class managing the instance of a certain user, liked to a match.
 */

public class Player{
    private final int ID; //id connessione del giocatore
    private final String nickname;
    private Match match;
    GodCards card;
    private Commands commands;

    private final Set<Worker> workers = new HashSet<>(2);
    private Worker currentWorker;

    private PlayerState playerState;
    private TurnState turnState;

    private boolean selectedCard;
    private boolean placedWorkers;
    private boolean specialFunction;
    private boolean terminateTurnAvailable;         //può essere terminato il turno?
    private boolean specialFunctionAvailable;       //può essere attivata la funzione speciale?

    public Player(int ID,String nickname) {
        this.ID = ID;
        this.nickname = nickname;
        this.playerState = PlayerState.INITIALIZED;
    }

    public int getID() {
        return ID;
    }

    public String getNickname() {
        return nickname;
    }

    public Match getMatch() {
        return match;
    }

    public GodCards getCard(){
        return card;
    }

    public Commands getCommands() {return commands;}

    public Set<Worker> getWorkers() {
        return workers;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public Position getCurrentWorkerPosition() {
        return currentWorker.getPosition();
    }

    public PlayerState getPlayerState(){
        return playerState;
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public boolean hasSelectedCard(){
        return selectedCard;
    }

    public boolean hasPlacedWorkers() {
        return placedWorkers;
    }

    public boolean getSpecialFunction() {
        return specialFunction;
    }

    public boolean hasFinished() {
        return (playerState==PlayerState.IDLE);
    }

    public boolean isTerminateTurnAvailable(){
        return terminateTurnAvailable;
    }

    public boolean isSpecialFunctionAvailable(){
        return specialFunctionAvailable;
    }

    public void setMatch(Match match){
        this.match = match;
    }

    public void setCommands(GodCards card) {
        this.card = card;
        selectedCard = true;
        this.commands = card.apply(new BasicCommands());
        match.removeCard(card);
    }

    public void setWorker(Position position) {
        position.setZ(0);
        workers.add(new Worker(position));
        commands.placeWorker(position, this);
        if (workers.size() == 2){
            turnState = commands.nextState(this);
            placedWorkers = true;
        }

    }

    public void setCurrentWorker(Position position) {
        currentWorker = workers.stream().filter(worker -> worker.getPosition()==position).findAny().get();
    }

    public void setPlayerState(){
        this.playerState = PlayerState.ACTIVE;
        if (match.getCurrentState() == MatchState.RUNNING) {
            specialFunction = false;
            terminateTurnAvailable = false;
            specialFunctionAvailable = false;
            commands.nextState(this);
            setAvailableCells();
        }
    }

    public void resetPlayerState(){
        this.playerState = PlayerState.IDLE;
    }

    public PlayerState nextPlayerState() {
        playerState = playerState.next();
        return playerState;
    }

    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
    }

    public void setUnsetSpecialFunction(boolean specialFunction){
        this.specialFunction = specialFunction;
    }

    public void setHasFinished() {
        playerState = PlayerState.IDLE;
    }

    public void setTerminateTurnAvailable(){
        this.terminateTurnAvailable = true;
    }

    public void setUnsetSpecialFunctionAvailable(boolean specialFunctionAvailable){
        this.specialFunctionAvailable = specialFunctionAvailable;
    }

    public void playerAction(Position position){
        switch (turnState){
            case MOVE:
                commands.moveWorker(position, this);
                break;
            case BUILD:
                commands.build(position, this);
                break;
        }
        turnState = commands.nextState(this);
        setAvailableCells();
        if (commands.winningCondition(this))
            playerState = PlayerState.WIN;
        else if (commands.losingCondition(this))
            playerState = PlayerState.LOST;
    }

    public Set<Position> getPlacingAvailableCells(){
        return commands.computeAvailablePlacing(this);
    }

    public Map<Position,Set<Position>> getWorkersAvailableCells() {
        Map<Position,Set<Position>> positionSetMap = new HashMap<>();
        workers.forEach(worker -> positionSetMap
                .put(worker.getPosition(),worker.getAvailableCells(turnState)));
        return positionSetMap;
    }


    public void setAvailableCells() {
        workers.forEach(worker -> {
            worker.setAvailableCells(MOVE, commands.computeAvailableMovements(this, worker));
            worker.setAvailableCells(BUILD, commands.computeAvailableBuildings(this, worker));
        });
    }
}
