package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class SelectingSpecialCommand extends ControlState {

    DataBase dataBase = View.getDataBase();

    MessageEvent messageEvent = new MessageEvent();

    @Override
    public MessageEvent computeInput(String input) {
        messageEvent = new MessageEvent();
        if(dataBase.getSelectedGodCards().size() <= 0)
            throw new IllegalArgumentException("Selected god cards empty");
        //corretto
        if(dataBase.getSelectedGodCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))){
            String card = dataBase
                    .getSelectedGodCards()
                    .stream()
                    .filter(card1 -> card1.toUpperCase().equals(input.toUpperCase()))
                    .findAny()
                    .get();
            dataBase.getSelectedGodCards().remove(card);
            dataBase.setGodCard(card);
            messageEvent.setGodCard(dataBase.getGodCard());
            messageEvent.setGodCards(dataBase.getSelectedGodCards());
            Controller.setMessageReady(true);
            dataBase.setPlayerState(PlayerState.IDLE);
            return  messageEvent;
        }
        else{
            //sbagliato
            View.setError(true);
            View.print();
            Controller.setMessageReady(false);
            Controller.setActiveInput(true);
        }
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        if(message.getMatchCards().size() == message.getMatchPlayers().size() && dataBase.getPlayerState() != PlayerState.ACTIVE
                && !message.getMatchPlayers().get(message.getMatchPlayers().keySet()
                .stream()
                .min((Integer i1,Integer i2 ) -> i1.compareTo(i2))
                .get()).equals(dataBase.getNickname())){
            System.out.println("For this match the Gods are "+ message.getMatchCards());
        }

        if(!message.getInfo().equals("Match data update") && dataBase.getPlayer() != message.getCurrentPlayer() ){
            System.out.println(message.getInfo());
        }

        dataBase.setPlayer(message.getCurrentPlayer());

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            dataBase.setControlState(new NotInitialized());
            dataBase.setPlayerState(null);
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
            return;
        }


        //caso SELECTING_SPECIAL_COMMAND
        if( message.getMatchCards() != null){
            dataBase.setSelectedGodCards(message.getMatchCards());
        }

        //caso ACTIVE
        if(dataBase.getPlayerState() == PlayerState.ACTIVE){
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
        }
        else{
            System.out.println(computeView());
        }

    }

    @Override
    public String computeView() {
        StringBuilder string = new StringBuilder();
        if(PlayerState.ACTIVE == dataBase.getPlayerState()){
            if(View.getError()){
                string.append("Please select your card from [ ");
                dataBase.getSelectedGodCards().forEach(card -> string.append(card).append(" ,"));
                string.deleteCharAt(string.length()-1);
                string.append(" ]");
            }
            else if(View.getRefresh()){
                if(dataBase.getSelectedGodCards().size()==1){
                    string.append("The last God is [ ");
                    dataBase.getSelectedGodCards().forEach(string::append);
                    string.append(" ] . Please select it.");
                }
                else {
                    string.append("Select your God Card from [ ");
                    dataBase.getSelectedGodCards().forEach(card -> string.append(card).append(" ,"));
                    string.deleteCharAt(string.length()-1);
                    string.append(" ]");
                }
            }
            return string.toString();}
        else{
            return dataBase.getMatchPlayers().get(dataBase.getPlayer())  + " is selecting his God for the match ";
        }
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        dataBase.setGodCard(null);
        Controller.setActiveInput(true);
        System.out.println(computeView());
    }

}

/*    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        if (super.checkMessage(viewObject)) {

            GameBoard gameBoard = View.getGameBoard();

            if (gameBoard.getSelectedGodCards().contains(viewObject)) {
                Controller.getMessage().setGodCard(viewObject);
                return true;
            } else System.out.println("CARD NOT AVAILABLE");
        }
        return false;
    }
}
*/