package ru.kpfu.itis.lifeTrack.exception.workflow;

public class WorkflowAlreadyExistsException extends Exception{
    public WorkflowAlreadyExistsException(String message) {
        super(message);
    }
}
