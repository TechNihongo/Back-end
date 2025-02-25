package org.example.technihongo.repositories;

import org.example.technihongo.entities.PaymentMethod;
import org.example.technihongo.enums.PaymentMethodCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    PaymentMethod findByCode(PaymentMethodCode code);

}
