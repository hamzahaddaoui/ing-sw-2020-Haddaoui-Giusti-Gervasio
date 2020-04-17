package it.polimi.ingsw.client;

public class ViewControllerEvent {

    private String messageSubject;
    private String messageArgument;

    ViewControllerEvent(InputState messageSubject,String messageArgument) {
        this.messageSubject = messageSubject.getSubject();
        this.messageArgument = messageArgument;
    }

    public String getMessageArgument() {
        return messageArgument;
    }

    public String getMessageSubject() {
        return messageSubject;
    }
}
