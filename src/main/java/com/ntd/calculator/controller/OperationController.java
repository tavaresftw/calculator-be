package com.ntd.calculator.controller;

import com.ntd.calculator.data.OperationRequest;
import com.ntd.calculator.data.RecordsResponse;
import com.ntd.calculator.security.JwtUtil;
import com.ntd.calculator.service.OperationService;
import com.ntd.calculator.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/operation")
public class OperationController {
    private static final Logger log = LoggerFactory.getLogger(OperationController.class);

    private final OperationService operationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public OperationController(OperationService operationService, UserService userService, JwtUtil jwtUtil) {
        this.operationService = operationService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/")
    public ResponseEntity<String> operation(
            @RequestHeader("Authorization") String token,
            @RequestBody OperationRequest operationRequest
    ) {
        String username = userService.getUsernameFromToken(token);
        log.info("User=" + username + " Operation status=executing, Operation request=" + operationRequest.getOperationType());
        if(jwtUtil.validateToken(token, username)){
            String result = operationService.executeOperation(
                username,
                operationRequest.getNum1(),
                operationRequest.getNum2(),
                operationRequest.getOperationType()
            );
            log.info("User=" + username + ", Operation status=success, " + "Operation result=" + result);
            return ResponseEntity.ok(result);
        }
        log.info("User=" + username + ", Operation status=failed, " + "Invalid token");
        return ResponseEntity.badRequest().body("Invalid token");
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getRecordsWithFilter(
            @RequestHeader("Authorization") String token,
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam String search
    ) {
        if(jwtUtil.validateToken(token, username)){
            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            List<RecordsResponse> records = operationService.getRecordsByUserWithFilter(username, page, size, sortBy, sortDirection, search);
            return ResponseEntity.ok(records);
        }
        log.info("User=" + username + ", Get records status=failed, " + "Invalid token");
        return ResponseEntity.badRequest().body("Invalid token");
    }

    @DeleteMapping("/record/{recordId}")
    public ResponseEntity<String> softDeleteRecord(
            @RequestHeader("Authorization") String token,
            @PathVariable Long recordId
    ) {
        String username = userService.getUsernameFromToken(token);
        log.info("User=" + username + ", Soft delete record status=executing, Record id=" + recordId);
        if(jwtUtil.validateToken(token, username)){
            operationService.softDeleteRecord(recordId);
            log.info("User=" + username + ", Delete record status=success, Record id=" + recordId);
            return ResponseEntity.ok("Record deleted");
        }
        log.info("User=" + username + ", Delete record status=failed, " + "Invalid token");
        return ResponseEntity.badRequest().body("Invalid token");
    }
}
