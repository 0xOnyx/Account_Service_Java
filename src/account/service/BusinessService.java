package account.service;

import account.DTO.PaymentsDTO;
import account.DTO.StatusMessage;
import account.database.Payments;
import account.exception.UserException;
import account.repository.PaymentsRepository;
import account.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BusinessService {

    UserRepository      userRepository;
    PaymentsRepository  paymentsRepository;

    public BusinessService(@Autowired UserRepository userRepository, @Autowired PaymentsRepository paymentsRepository) {
        this.userRepository = userRepository;
        this.paymentsRepository = paymentsRepository;
    }

    public List<PaymentsDTO> getPayment(YearMonth period, UserDetails userDetails) {
        List<Payments>  payments;

        if (period != null)
            payments = paymentsRepository.findAllByEmployeeAndPeriodOrderByPeriodDesc(userDetails.getUsername(), period);
        else
            payments = paymentsRepository.findAllByEmployeeOrderByPeriodDesc(userDetails.getUsername());
        return payments.stream().map(PaymentsDTO::new).toList();
    }

    @Transactional
    public StatusMessage postPayments(List<Payments> payments) {
//        for (Payments payment : payments) {
//            payment.setUser(userRepository.findByEmailIgnoreCase(payment.getEmployee()));
//        }
        payments.forEach(payment ->
                System.out.println(payment.getEmployee() + " " + payment.getPeriod() + " " + payment.getSalary()));
        paymentsRepository.saveAll(payments);
        return new StatusMessage("Added successfully!");
    }

    @Transactional
    public StatusMessage putPayments(Payments payment) {
//        User user = userRepository.findByEmailIgnoreCase(payment.getEmployee());
//        if (user == null) {
//            return new StatusMessage("No such user!");
//        }
//        payment.setUser(user);
        Payments paymentsDataBase = paymentsRepository.findByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod());
        if (paymentsDataBase == null) {
            throw new UserException("No such payment!");
        }
        paymentsDataBase.setSalary(payment.getSalary());
        paymentsRepository.save(paymentsDataBase);
        return new StatusMessage("Updated successfully!");
    }
}
