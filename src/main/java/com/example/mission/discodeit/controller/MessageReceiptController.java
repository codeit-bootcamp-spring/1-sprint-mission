package com.example.mission.discodeit.controller;

import com.example.mission.discodeit.entity.MessageReceipt;
import com.example.mission.discodeit.service.MessageReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class MessageReceiptController {
    private final MessageReceiptService receiptService;

    @PostMapping
    public MessageReceipt createReceipt(@RequestBody MessageReceipt receipt) {
        return receiptService.createReceipt(receipt);
    }

    @GetMapping("/user/{userId}")
    public List<MessageReceipt> getReceiptsByUser(@PathVariable UUID userId) {
        return receiptService.getReceiptsByUser(userId);
    }
}