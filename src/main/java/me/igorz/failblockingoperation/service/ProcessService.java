package me.igorz.failblockingoperation.service;

import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProcessService {

    // one hour
    private final int BLOCK_TIME = 1000 * 60 * 60;

    private final Set<Integer> processed = new HashSet<>();

    @SneakyThrows
    public void processBlocking(Integer id) {
        if (id % 10 == 2 || id % 10 == 3) {
            Thread.sleep(BLOCK_TIME);
        }
        processed.add(id);
    }

    public int count() {
        return processed.size();
    }

    public void reset() {
        processed.clear();
    }

    @Async("processesExecutor")
    @EventListener
    public void processAsync(ProcessEvent event) {
        processBlocking(event.getId());
    }
}
