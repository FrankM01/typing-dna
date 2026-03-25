package com.typingdna.history;

import com.typingdna.dto.response.AuthAttemptResponse;
import com.typingdna.dto.response.StatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/attempts")
    public ResponseEntity<List<AuthAttemptResponse>> getAttempts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int limit) {
        return ResponseEntity.ok(historyService.getAttempts(userDetails.getUsername(), limit));
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(historyService.getStats(userDetails.getUsername()));
    }
}
