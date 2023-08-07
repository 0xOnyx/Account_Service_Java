package account.database;

import account.deserializer.YearMonthDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.YearMonth;


@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(columnNames = {"employee", "period"}, name = "pk_employee"))
@JsonIgnoreProperties({"id", "user"})
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long    id;

    @JsonDeserialize(using = YearMonthDeserializer.class)
    private YearMonth period;

    @Min(value = 0, message = "Salary must be positive")
    private long    salary;

    @NotEmpty
    @Email
    @Pattern(regexp = "\\w+(@acme.com)$")
    private String  employee;

    @ManyToOne
    @JoinColumn(name = "employee", referencedColumnName = "email", insertable = false, updatable = false)
    @JsonIgnore
    private User user;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
