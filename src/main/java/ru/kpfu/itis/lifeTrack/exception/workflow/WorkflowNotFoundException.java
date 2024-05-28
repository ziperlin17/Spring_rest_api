package ru.kpfu.itis.lifeTrack.exception.workflow;

import ru.kpfu.itis.lifeTrack.exception.NotFoundException;

public class WorkflowNotFoundException extends NotFoundException {
    public WorkflowNotFoundException(String message) {
        super(message);
    }
}
