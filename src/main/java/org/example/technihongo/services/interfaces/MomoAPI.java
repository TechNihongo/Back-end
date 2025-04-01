package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateMomoRequest;
import org.example.technihongo.dto.CreateMomoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "momo", url = "${payment.momo.end-point}")
public interface MomoAPI {
    @PostMapping("${payment.momo.create-url}")
    CreateMomoResponse createMomoQR(@RequestBody CreateMomoRequest createMomoRequest);
}