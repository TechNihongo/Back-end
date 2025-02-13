package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.UpdatePaymentMethodRequestDTO;
import org.example.technihongo.entities.PaymentMethod;
import org.example.technihongo.enums.PaymentMethodType;
import org.example.technihongo.repositories.PaymentMethodRepository;
import org.example.technihongo.services.interfaces.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Override
    public PaymentMethod updatePaymentMethod(Integer methodId, UpdatePaymentMethodRequestDTO request) {
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(methodId);
        if (optionalPaymentMethod.isEmpty()) {
            throw new RuntimeException("Payment method not found with Id: " + methodId);
        }
        PaymentMethod paymentMethod = optionalPaymentMethod.get();

        if (request.getName() != null) {
            paymentMethod.setName(request.getName());
        }
        paymentMethod.setActive(request.isActive());

        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public List<PaymentMethod> paymentMethodList() {
        return paymentMethodRepository.findAll();
    }

}
