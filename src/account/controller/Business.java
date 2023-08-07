package account.controller;

import account.DTO.PaymentsDTO;
import account.DTO.StatusMessage;
import account.database.Payments;
import account.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@Validated
public class Business {

    BusinessService businessService;

    Business(@Autowired BusinessService businessService)
    {
        this.businessService = businessService;
    }


    @GetMapping("/api/empl/payment")
    public List<PaymentsDTO> getPayment(@RequestParam(value="period", required = false) YearMonth period, @AuthenticationPrincipal UserDetails userDetails) {
        return businessService.getPayment(period, userDetails);
    }

    @PostMapping("/api/acct/payments")
    public StatusMessage postPayments(@RequestBody List<Payments> payments) {
        return businessService.postPayments(payments);
    }


    @PutMapping("/api/acct/payments")
    public StatusMessage   putPayments(@RequestBody Payments payment){
        return businessService.putPayments(payment);
    }
}
