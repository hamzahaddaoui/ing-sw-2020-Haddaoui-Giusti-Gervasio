package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.Position;

import java.util.*;


public class EnterCommand implements CommandCharacter {

    /**
     * Method that confirms the choice of the number of the players' game
     *
     * @return
     */
    @Override
    public boolean executeNumberStatus() {
        Player player = View.getPlayer();
        if(player.getPlayersNum() == null)
            throw new IllegalArgumentException("Players num is empty.");
        Controller.getMessage().setPlayersNum(player.getPlayersNum());
        System.out.println("Number selected: " + player.getPlayersNum());
        View.doUpdate();
        return true;
    }

    /**
     * Method that confirms the position selected for the Worker.
     *
     * the position selected is deleted from PlacingAvailableCells and added to WorkersPositions
     *
     * @return  true if the player have placed his 2 worker, else false
     */
    @Override
    public boolean executePlacingWorkerStatus() {
        GameBoard gameBoard = View.getGameBoard();
        Position coloredPosition = gameBoard.getColoredPosition();
        Set<Position> workersPositions = gameBoard.getWorkersPositions();
        Set<Position> placingAvailableCells = gameBoard.getPlacingAvailableCells();

        if(workersPositions == null){
            gameBoard.setWorkersAvailableCells(new HashMap());
            workersPositions = gameBoard.getWorkersPositions();
        }

        if(placingAvailableCells == null)
            throw new IllegalArgumentException(" placingAvailableCells is empty.");
        if(coloredPosition == null)
            throw new IllegalArgumentException(" coloredPosition is empty.");
        if(workersPositions.contains(coloredPosition))
            throw new IllegalArgumentException(" Posizione già inserita. Non disponibile! ");
        if(!placingAvailableCells.contains(coloredPosition))
            throw new IllegalArgumentException(" Posizione non disponibile! ");

        if( placingAvailableCells.contains(coloredPosition) && !workersPositions.contains(coloredPosition)){
            placingAvailableCells.remove(coloredPosition);
            gameBoard.getWorkersAvailableCells().put(coloredPosition, new HashSet<>());
            gameBoard.setColoredPosition(placingAvailableCells.stream().findFirst().get());
            System.out.println("( "+coloredPosition.getX() + " , " +coloredPosition.getY() +" )  has been insert");
            if(workersPositions.size() == 2){
                System.out.println("Placing done");
                Controller.getMessage().setInitializedPositions(workersPositions);
                View.doUpdate();
                return true;
            }
            if(workersPositions.size() == 1)
                System.out.println("Place the other worker");
            View.doUpdate();
            return true;
        }
        View.doUpdate();
        return false;
    }

    /**
     * Method that confirms the View's colored position.
     * <p>
     * If the player hasn't chosen the worker yet, the method sets the colored position as the View's starting position.
     * Else checks if the colored position is contained in the worker's available cells and, if so, sets the message's start position and end position.
     *
     * @return              true if you can send the message, false otherwise
     */
    @Override
    public boolean executeRunningStatus() {

        if (View.getGameBoard().getStartingPosition()==null) {
            View.getGameBoard().setStartingPosition(View.getGameBoard().getColoredPosition());
            return false;
        }
        else if (View.getGameBoard()
                        .getWorkersAvailableCells(View.getGameBoard().getStartingPosition())
                        .contains(View.getGameBoard().getColoredPosition())) {
            Controller.getMessage().setStartPosition(View.getGameBoard().getStartingPosition());
            Controller.getMessage().setEndPosition(View.getGameBoard().getColoredPosition());
            View.getGameBoard().setStartingPosition(null);
            View.getGameBoard().setColoredPosition(null);
            return true;
        }
        else return false;
    }

    /**
     * Method that confirms the View's colored god card.
     * <p>
     *
     * @return  true if the colored god card is contained in View's set of god cards, false otherwise
     */
    @Override
    public boolean executeSpecialCommandsStatus() {
        String coloredGodCard = View.getGameBoard().getColoredGodCard();

        if (View.getGameBoard().getSelectedGodCards().contains(coloredGodCard)) {
            Controller.getMessage().setGodCard(coloredGodCard);
            return true;
        }
        else return false;
    }

    /**
     * Method that confirms the GodCard's selection.
     *
     * the selectedPosition is deleted from selectedGodCards and is added to GodCards
     *
     * @return true if the selectedGodCard's size is 2, else false
     */
    @Override
    public boolean executeSelectingGodCardsStatus() {
        GameBoard gameBoard = View.getGameBoard();
        ArrayList<String> godCards = gameBoard.getMatchCards();
        Integer playersNum = View.getPlayer().getPlayersNum();
        String coloredGodCard = gameBoard.getColoredGodCard();
        ArrayList<String> selectedGodCards = gameBoard.getSelectedGodCards();


        if(selectedGodCards == null) gameBoard.setSelectedGodCards(new HashSet<>());

        if(godCards == null)
            throw new IllegalArgumentException(" GodCards is empty");
        if(playersNum == null)
            throw new IllegalArgumentException(" PlayersNum is empty");
        if(coloredGodCard == null)
            throw new IllegalArgumentException(" Colored GodCard is empty");
        if(!godCards.contains(coloredGodCard))
            throw new IllegalArgumentException(" This card is not in the GodCards Deck ");

        if(selectedGodCards.size() < playersNum){
            selectedGodCards.add(coloredGodCard);
            godCards.remove(godCards.indexOf(coloredGodCard));
            System.out.println(coloredGodCard + " has been insert");
            if(selectedGodCards.size() < playersNum){
                gameBoard.setColoredGodCard(godCards.get(0));
                System.out.println("Select next card");
                System.out.println("MatchCards "+godCards);
                View.doUpdate();
                return false;
            }
            else if(playersNum == selectedGodCards.size()){
                Controller.getMessage().setGodCards(new HashSet<>(selectedGodCards));
                System.out.println("Cards Selected.");
                System.out.println("Your cards are "+ selectedGodCards);
                gameBoard.setColoredGodCard(null);
                View.doUpdate();
                return true;
            }
        }
        System.out.println("Colored Godcard is " + gameBoard.getColoredGodCard());
        View.doUpdate();
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }

}
