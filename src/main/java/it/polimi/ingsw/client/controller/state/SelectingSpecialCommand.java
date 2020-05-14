package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class SelectingSpecialCommand extends ControlState {

    //DataBase dataBase = View.getDataBase();

    MessageEvent messageEvent = new MessageEvent();

    @Override
    public MessageEvent computeInput(String input) {
        messageEvent = new MessageEvent();
        if(DataBase.getSelectedGodCards().size() <= 0)
            throw new IllegalArgumentException("Selected god cards empty");
        //corretto
        if(DataBase.getSelectedGodCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))){
            String card = DataBase
                    .getSelectedGodCards()
                    .stream()
                    .filter(card1 -> card1.toUpperCase().equals(input.toUpperCase()))
                    .findAny()
                    .get();
            DataBase.getSelectedGodCards().remove(card);
            DataBase.setGodCard(card);
            messageEvent.setGodCard(DataBase.getGodCard());
            messageEvent.setGodCards(DataBase.getSelectedGodCards());
            DataBase.setMessageReady(true);
            DataBase.setPlayerState(PlayerState.IDLE);
            return  messageEvent;
        }
        else{
            //sbagliato
            View.setError(true);
            View.print();
            DataBase.setMessageReady(false);
            DataBase.setActiveInput(true);
        }
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        if(message.getMatchCards().size() == message.getMatchPlayers().size() && DataBase.getPlayerState() != PlayerState.ACTIVE
                && !message.getMatchPlayers().get(message.getMatchPlayers().keySet()
                .stream()
                .min((Integer i1,Integer i2 ) -> i1.compareTo(i2))
                .get()).equals(DataBase.getNickname())){
            System.out.println("For this match the Gods are "+ message.getMatchCards());
        }

        if(!message.getInfo().equals("Match data update") && DataBase.getPlayer() != message.getCurrentPlayer() ){
            System.out.println(message.getInfo());
        }

        DataBase.setPlayer(message.getCurrentPlayer());

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            DataBase.setControlState(new NotInitialized());
            DataBase.setPlayerState(null);
            DataBase.setActiveInput(true);
            View.setRefresh(true);
            View.print();
            return;
        }


        //caso SELECTING_SPECIAL_COMMAND
        if( message.getMatchCards() != null){
            DataBase.setSelectedGodCards(message.getMatchCards());
        }

        //caso ACTIVE
        if(DataBase.getPlayerState() == PlayerState.ACTIVE){
            DataBase.setActiveInput(true);
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
        if(PlayerState.ACTIVE == DataBase.getPlayerState()){
            if(View.getError()){
                string.append("Please select your card from [ ");
                DataBase.getSelectedGodCards().forEach(card -> string.append(card).append(", "));
                string.deleteCharAt(string.length()-2);
                string.append("]");
            }
            else if(View.getRefresh()){
                if(DataBase.getSelectedGodCards().size()==1){
                    string.append("The last God is [ ");
                    DataBase.getSelectedGodCards().forEach(string::append);
                    string.append(" ] . Please select it.");
                }
                else {
                    string.append("Select your God Card from [ ");
                    DataBase.getSelectedGodCards().forEach(card -> string.append(card).append(", "));
                    string.deleteCharAt(string.length()-2);
                    string.append("]");
                }
            }
            return string.toString();}
        else{
            return DataBase.getMatchPlayers().get(DataBase.getPlayer())  + " is selecting his God for the match ";
        }
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        DataBase.setGodCard(null);
        DataBase.setActiveInput(true);
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