package me.igorz.failblockingoperation;

import me.igorz.failblockingoperation.service.ProcessUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RestTemplateTimeoutConfig.class)
class FailBlockingOperationApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProcessUserService processUserService;

    @Test
    @Timeout(2)
    void test8ItemsAreProcessedFromNextServiceAsync() {
        processUserService.reset();
        for (int i = 10; i < 20; i++) {
            processUserService.processAsync(i);
        }
        await().atMost(Duration.ofSeconds(3)).until(() -> processUserService.count() == 8);
    }

    @Test
    void test8ItemsAreProcessedFromRest() {
        processUserService.reset();
        for (int i = 0; i < 10; i++) {
            try {
                this.restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/process/blocking/" + i, null, Void.class);
            } catch (ResourceAccessException ignored) {
            }
        }
        ResponseEntity<Integer> response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/api/count/blocking", Integer.class);
        assertEquals(8, response.getBody());
    }

    @Test
    @Timeout(2)
    void test8ItemsAreProcessedFromNextServiceBlocking() {
        processUserService.reset();
        for (int i = 0; i < 10; i++) {
            processUserService.processBlocking(i);
        }
        assertEquals(8, processUserService.count());
    }

}
