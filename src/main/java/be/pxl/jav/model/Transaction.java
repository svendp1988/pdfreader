package be.pxl.jav.model;

import java.time.LocalDate;
import java.time.Month;

public class Transaction {

    private LocalDate date;
    private String name;
    private double amount;
    private String detail;
    private String category;

	public Transaction(LocalDate date, double amount, String category, String name, String detail) {
        this.date = date;
        this.amount = amount;
        this.name = name;
        this.detail = detail;
        this.category = category;
    }

	public String getCategory() {
		return category;
	}

	public boolean isIncome() {
    	return amount > 0;
    }

    public boolean isExpense() {
    	return amount < 0;
    }

	public String getName() {
		return name;
	}

    public boolean isMonthAndYear(Month month, int year) {
		return month.equals(date.getMonth()) && year == date.getYear();
    }

	public LocalDate getDate() {
		return date;
	}

	public void setAmount(float amount) {
        this.amount = amount;
    }

	public double getAmount() {
		return amount;
	}

	public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

	@Override
	public String toString() {
		return "Transaction{" +
				"date=" + date +
				", name='" + name + '\'' +
				", amount=" + amount +
				", detail='" + detail + '\'' +
				'}';
	}
}
