package me.igorz.failblockingoperation.controller;

import lombok.RequiredArgsConstructor;
import me.igorz.failblockingoperation.service.ProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    @PostMapping("/process/blocking/{id}")
    public ResponseEntity<Void> processBlocking(@PathVariable Integer id) {
        processService.processBlocking(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/blocking")
    public ResponseEntity<Integer> countBlocking() {
        return ResponseEntity.ok(processService.count());
    }

}
