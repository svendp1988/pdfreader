package be.pxl.jav.model;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Account {

    private String iban;
    private String firstName;
    private String name;
    private List<Transaction> transactions = new ArrayList<>();

	public Account(String iban, String firstName, String name) {
		this.iban = iban;
		this.name = name;
		this.firstName = firstName;
	}

	public String getFullName() {
		return firstName + " " + name;
	}

	public List<Transaction> getIncome(Month month, int year) {
		return transactions.stream()
				.filter(transaction -> month.equals(transaction.getDate().getMonth()) && year == transaction.getDate().getYear())
				.collect(toList());
	}

	public List<Transaction> getExpenses(Month month, int year) {
		return transactions.stream()
				.filter(Transaction::isExpense)
				.collect(toList());
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void addTransaction(Transaction transaction) {
    	transactions.add(transaction);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Account account = (Account) o;
		return Objects.equals(iban, account.iban) && Objects.equals(firstName, account.firstName) && Objects.equals(name, account.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(iban, firstName, name);
	}

	@Override
	public String toString() {
		return "Account{" +
				"iban='" + iban + '\'' +
				", firstName='" + firstName + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
