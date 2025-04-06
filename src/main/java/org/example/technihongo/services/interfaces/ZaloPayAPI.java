package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateZaloRequest;
import org.example.technihongo.dto.CreateZaloResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "zalopay", url = "${payment.zaloPay.endpoint}")
public interface ZaloPayAPI {
    @PostMapping("${payment.zaloPay.create-url}")
    CreateZaloResponse createZaloOrder(@RequestBody CreateZaloRequest createZaloRequest);
}