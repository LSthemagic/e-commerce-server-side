package com.railansantana.e_commerce.services.payment;

import com.railansantana.e_commerce.domain.Order;
import com.railansantana.e_commerce.dtos.auth.ResponseFullDataDTO;
import com.railansantana.e_commerce.services.user.DataUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
    private final DataUserService userService;

    public void payment(String userId){
        ResponseFullDataDTO user =  userService.findById(userId);
        double sum = 0.0;
        for (Order x : user.orders()) {
            double v = x.getTotalPrice();
            sum += v;
        }
        System.err.println("sum -> "+sum);
    }
}
