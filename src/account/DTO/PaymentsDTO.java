package account.DTO;

import account.database.Payments;

import java.time.format.DateTimeFormatter;

public class PaymentsDTO {
    private String  name;
    private String lastname;
    private String period;
    private String    salary;

    public PaymentsDTO(String name, String last_name, String period, String salary) {
        this.name = name;
        this.lastname = last_name;
        this.period = period;
        this.salary = salary;
    }

    public PaymentsDTO(Payments payments)
    {
        this(
            payments.getUser().getName(),
            payments.getUser().getLastname(),
            payments.getPeriod().format(DateTimeFormatter.ofPattern("MMMM-yyyy")),
            String.format("%d dollar(s) %d cent(s)", payments.getSalary() / 100, payments.getSalary() % 100)
        );
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
