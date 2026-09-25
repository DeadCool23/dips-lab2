package com.nisuev.dips.bonusservice.controllers;

import com.nisuev.dips.bonusservice.dto.*;
import com.nisuev.dips.bonusservice.service.PrivilegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/privilege")
@RequiredArgsConstructor
@Tag(name = "Bonus API")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @GetMapping
    @Operation(summary = "Информация о бонусном счёте")
    public ResponseEntity<PrivilegeInfoResponse> getInfo(@RequestHeader("X-User-Name") String username) {
        return ResponseEntity.ok(privilegeService.getInfo(username));
    }

    @PostMapping
    @Operation(summary = "Начисление или списание бонусов при покупке")
    public ResponseEntity<PrivilegeApplyResponse> apply(@RequestHeader("X-User-Name") String username,
                                                        @RequestBody PrivilegeApplyRequest request) {
        return ResponseEntity.ok(privilegeService.apply(username, request));
    }

    @DeleteMapping
    @Operation(summary = "Откат бонусной операции по билету")
    @ApiResponse(responseCode = "204", description = "Откат выполнен")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> rollback(@RequestHeader("X-User-Name") String username,
                                         @RequestParam UUID ticketUid) {
        privilegeService.rollback(username, ticketUid);
        return ResponseEntity.noContent().build();
    }
}
