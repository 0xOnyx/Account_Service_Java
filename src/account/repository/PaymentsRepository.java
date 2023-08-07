package account.repository;

import account.database.Payments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Repository
public interface PaymentsRepository extends CrudRepository<Payments, Long> {

    List<Payments> findAllByEmployeeAndPeriodOrderByPeriod(String username, YearMonth period);

    Payments findByEmployeeAndPeriod(String employee, YearMonth period);

    List<Payments> findAllByEmployeeOrderByPeriod(String username);

    List<Payments> findAllByEmployeeOrderByPeriodDesc(String username);

    List<Payments> findAllByEmployeeAndPeriodOrderByPeriodDesc(String username, YearMonth period);
}
