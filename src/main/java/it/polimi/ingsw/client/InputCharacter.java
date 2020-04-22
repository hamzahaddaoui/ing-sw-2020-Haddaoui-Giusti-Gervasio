package it.polimi.ingsw.client;

public enum InputCharacter {
    A       ("A"),
    D       ("D"),
    E       ("E"),
    ENTER   ("ENTER"),
    F       ("F"),
    W       ("W"),
    Q       ("Q"),
    S       ("S");

    private final String subject;

    InputCharacter(String subject) {
        this.subject=subject;
    }

    public String getSubject() {
        return subject;
    }
}
