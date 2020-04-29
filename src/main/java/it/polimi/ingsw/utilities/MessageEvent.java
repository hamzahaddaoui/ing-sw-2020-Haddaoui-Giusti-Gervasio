package it.polimi.ingsw.utilities;

import it.polimi.ingsw.server.ClientHandler;

import java.util.Map;
import java.util.Set;

public class MessageEvent {
    private transient ClientHandler clientHandler;

    private String info;
    //CONTROLLER_TO_CONTROLLER
    //CONTROLLER_TO_VIEW

    private Integer                         matchID;
    private Integer                         playerID;

    //user to controller
    private String                          nickname;

    private Integer                         playersNum;
    private Set<String>                     godCards; //le Carte selezionate in fase di SelectingGodCards del controller

    private String                          godCard; //Carta Selezionata in fase di SelectionSpecialFunction del controller

    private Set<Position>                   initializedPositions;   //le 2 posizioni in cui inizializzo i workers

    private Position                        startPosition;          //il worker che muovo
    private Position                        endPosition;            //la posizione finale del worker che voglio muovere

    private Boolean                         endTurn;                //se decido di terminare il turno
    private Boolean                         specialFunction;        //se decido di attivare la funzione speciale

    private boolean                         exit;                   //SE l'utente esce. mando prima questo

    //controller to view
    private MatchState                      matchState; //-
    private PlayerState                     playerState; //-
    private TurnState                       turnState; //-
    private Boolean                         error; //-

    private Set<String>                     matchCards;             //Carte del gioco //-
    private Set<Position>                   availablePlacingCells; //-

    private Map<Position, Cell>             billboardStatus;  //-

    private Map<Position, Set<Position>>    workersAvailableCells; //-

    private Boolean                         terminateTurnAvailable; //-
    private Map<Position,Boolean>           specialFunctionAvailable;//worker+disponibilità special function -

    private Map<Integer, String>            matchPlayers;           //i giocatori del gioco -
    //private int                           activeMatches;
    //private int                           playersConnected;

    private Integer                         winner;                 //chi è il vincitore se il match è finito

    private boolean                         finished;               //se il match è finito


    public ClientHandler getClientHandler(){
        return clientHandler;
    }

    public void setClientHandler(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    public String getInfo(){
        return info;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public Integer getMatchID(){
        return matchID;
    }

    public void setMatchID(Integer matchID){
        this.matchID = matchID;
    }

    public Integer getPlayerID(){
        return playerID;
    }

    public void setPlayerID(Integer playerID){
        this.playerID = playerID;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public Integer getPlayersNum(){
        return playersNum;
    }

    public void setPlayersNum(Integer playersNum){
        this.playersNum = playersNum;
    }

    public String getGodCard(){
        return godCard;
    }

    public void setGodCard(String godCard){
        this.godCard = godCard;
    }

    public Set<String> getGodCards(){
        return godCards;
    }

    public void setGodCards(Set<String> godCards){
        this.godCards = godCards;
    }

    public Set<Position> getInitializedPositions(){
        return initializedPositions;
    }

    public void setInitializedPositions(Set<Position> initializedPositions){
        this.initializedPositions = initializedPositions;
    }

    public Position getStartPosition(){
        return startPosition;
    }

    public void setStartPosition(Position startPosition){
        this.startPosition = startPosition;
    }

    public Position getEndPosition(){
        return endPosition;
    }

    public void setEndPosition(Position endPosition){
        this.endPosition = endPosition;
    }

    public Boolean getEndTurn(){
        return endTurn;
    }

    public void setEndTurn(Boolean endTurn){
        this.endTurn = endTurn;
    }

    public Boolean getSpecialFunction(){
        return specialFunction;
    }

    public void setSpecialFunction(Boolean specialFunction){
        this.specialFunction = specialFunction;
    }

    public MatchState getMatchState(){
        return matchState;
    }

    public void setMatchState(MatchState matchState){
        this.matchState = matchState;
    }

    public PlayerState getPlayerState(){
        return playerState;
    }

    public void setPlayerState(PlayerState playerState){
        this.playerState = playerState;
    }

    public TurnState getTurnState(){
        return turnState;
    }

    public void setTurnState(TurnState turnState){
        this.turnState = turnState;
    }

    public Boolean getError(){
        return error;
    }

    public void setError(Boolean error){
        this.error = error;
    }

    public Map<Position, Cell> getBillboardStatus(){
        return billboardStatus;
    }

    public void setBillboardStatus(Map<Position, Cell> billboardStatus){
        this.billboardStatus = billboardStatus;
    }

    public Set<String> getMatchCards(){
        return matchCards;
    }

    public void setMatchCards(Set<String> matchCards){
        this.matchCards = matchCards;
    }

    public Map<Position, Set<Position>> getWorkersAvailableCells(){
        return workersAvailableCells;
    }

    public void setWorkersAvailableCells(Map<Position, Set<Position>> workersAvailableCells){
        this.workersAvailableCells = workersAvailableCells;
    }

    public Set<Position> getAvailablePlacingCells(){
        return availablePlacingCells;
    }

    public void setAvailablePlacingCells(Set<Position> availablePlacingCells){
        this.availablePlacingCells = availablePlacingCells;
    }

    public Boolean getTerminateTurnAvailable(){
        return terminateTurnAvailable;
    }

    public void setTerminateTurnAvailable(Boolean terminateTurnAvailable){
        this.terminateTurnAvailable = terminateTurnAvailable;
    }

    public Map<Position, Boolean> getSpecialFunctionAvailable(){
        return specialFunctionAvailable;
    }

    public void setSpecialFunctionAvailable(Map<Position, Boolean>  specialFunctionAvailable){
        this.specialFunctionAvailable = specialFunctionAvailable;
    }

    public Map<Integer, String> getMatchPlayers(){
        return matchPlayers;
    }

    public void setMatchPlayers(Map<Integer, String> matchPlayers){
        this.matchPlayers = matchPlayers;
    }

    /*public int getActiveMatches(){
        return activeMatches;
    }

    public void setActiveMatches(int activeMatches){
        this.activeMatches = activeMatches;
    }

    public int getPlayersConnected(){
        return playersConnected;
    }

    public void setPlayersConnected(int playersConnected){
        this.playersConnected = playersConnected;
    }*/

    public Integer getWinner(){
        return winner;
    }

    public void setWinner(Integer winner){
        this.winner = winner;
    }

    public boolean isExit(){
        return exit;
    }

    public void setExit(boolean exit){
        this.exit = exit;
    }

    public boolean isFinished(){
        return finished;
    }

    public void setFinished(boolean finished){
        this.finished = finished;
    }


    @Override
    public String toString(){
        return "MessageEvent{" +
               "info='" + info + '\'' +
               ", matchID=" + matchID +
               ", playerID=" + playerID +
               ", nickname='" + nickname + '\'' +
               ", playersNum=" + playersNum +
               ", godCards=" + godCards +
               ", godCard='" + godCard + '\'' +
               ", initializedPositions=" + initializedPositions +
               ", startPosition=" + startPosition +
               ", endPosition=" + endPosition +
               ", endTurn=" + endTurn +
               ", specialFunction=" + specialFunction +
               ", exit=" + exit +
               ", matchState=" + matchState +
               ", playerState=" + playerState +
               ", turnState=" + turnState +
               ", error=" + error +
               ", matchCards=" + matchCards +
               ", availablePlacingCells=" + availablePlacingCells +
               ", billboardStatus=" + billboardStatus +
               ", workersAvailableCells=" + workersAvailableCells +
               ", terminateTurnAvailable=" + terminateTurnAvailable +
               ", specialFunctionAvailable=" + specialFunctionAvailable +
               ", matchPlayers=" + matchPlayers +
               ", winner=" + winner +
               ", finished=" + finished +
               '}';
    }
}
