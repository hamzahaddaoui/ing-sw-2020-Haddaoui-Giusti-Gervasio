package it.polimi.ingsw.client;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.ArrayList;
import java.util.Map;

public class Player {

    private static String ip;
    private static Integer matchID;
    private static Integer playerID;
    private static String nickname;
    private static PlayerState playerState;
    private static MatchState matchState;
    private static TurnState turnState;

    private static ArrayList<Integer> coloredPlayersNum;
    private static Integer playersNum;

    private static Map<Integer, String> matchPlayers;
    private static boolean terminateTurnAvailable;
    private static Map<Position, Boolean> specialFunctionAvailable;

    private static boolean error = false;

    public static void setError(boolean error) {
        Player.error = error;
    }

    public static void setMatchID(Integer matchID) {
        Player.matchID = matchID;
    }

    public static void setPlayerID(Integer playerID) {
        Player.playerID = playerID;
    }

    public static void setPlayerState(PlayerState playerState) {
        Player.playerState = playerState;
    }

    public static void setMatchState(MatchState matchState) {
        Player.matchState = matchState;
    }

    public static TurnState getTurnState() {
        return turnState;
    }

    public static void setTurnState(TurnState turnState) {
        Player.turnState = turnState;
    }

    public static void setMatchPlayers(Map<Integer, String> matchPlayers) {
        Player.matchPlayers = matchPlayers;
    }

    public static void setTerminateTurnAvailable(boolean terminateTurnAvailable) {
        Player.terminateTurnAvailable = terminateTurnAvailable;
    }

    public static Integer getMatchID() {
        return matchID;
    }

    public static Integer getPlayerID() {
        return playerID;
    }

    public static ArrayList<Integer> getColoredPlayersNum() {
        return coloredPlayersNum;
    }

    public static void setColoredPlayersNum(ArrayList<Integer> coloredPlayersNum) {
        Player.coloredPlayersNum = coloredPlayersNum;
    }

    public static PlayerState getPlayerState() {
        return playerState;
    }

    public static MatchState getMatchState() {
        return matchState;
    }

    public static Integer getPlayersNum() {
        return playersNum;
    }

    public static boolean isTerminateTurnAvailable() {
        return terminateTurnAvailable;
    }

    public static boolean isSpecialFunctionAvailable(Position position) {
        return specialFunctionAvailable.get(position);
    }

    public static Map<Position, Boolean> getSpecialFunctionAvailable(){
        return specialFunctionAvailable;
    }

    public static void setPlayersNum (int selectedPlayersNum) {
        playersNum = selectedPlayersNum;
    }

    public static void setSpecialFunctionAvailable(Map<Position,Boolean> modelMap) {
        specialFunctionAvailable = modelMap;
    }

    public static void setNickname(String nickname) {
        Player.nickname = nickname;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Player.ip = ip;
    }

    public static String getNickname() {
        return nickname;
    }

    public static Map<Integer, String> getMatchPlayers() {
        return matchPlayers;
    }
}
