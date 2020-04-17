package it.polimi.ingsw.client;

public enum InputState {
    Position("POSITION"),
    EndTurn("ENDTURN"),
    GodCard("GODCARD"),
    Nickname("NICKNAME"),
    Num_Of_Players("NUM_OF_PLAYERS"),
    Special_Function("SPECIAL_FUNCTION");

    private final String subject;

    InputState(String subject) {
        this.subject=subject;
    }

    public String getSubject() {
        return subject;
    }
}
