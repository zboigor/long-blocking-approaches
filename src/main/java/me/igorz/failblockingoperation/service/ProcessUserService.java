package me.igorz.failblockingoperation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessUserService {

    private final ProcessService processService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void processBlocking(Integer id) {
        processService.processBlocking(id);
    }

    public void processAsync(Integer id) {
        applicationEventPublisher.publishEvent(new ProcessEvent(id));
    }

    public int count() {
        return processService.count();
    }

    public void reset() {
        processService.reset();
    }
}
