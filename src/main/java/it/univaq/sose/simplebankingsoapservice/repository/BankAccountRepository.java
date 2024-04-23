package it.univaq.sose.simplebankingsoapservice.repository;

import it.univaq.sose.simplebankingsoapservice.domain.BankAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccountRepository {
    private static volatile BankAccountRepository instance;
    private final Map<Long, BankAccount> bankAccounts;
    private final ReentrantReadWriteLock lock;

    private BankAccountRepository() {
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
        this.bankAccounts = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public static BankAccountRepository getInstance() {
        BankAccountRepository result = instance;
        if (result == null) {
            synchronized (BankAccountRepository.class) {
                result = instance;
                if (result == null) {
                    result = new BankAccountRepository();
                    instance = result;
                }
            }
        }
        return result;
    }

    public List<BankAccount> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(bankAccounts.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public BankAccount findById(long id) {
        lock.readLock().lock();
        try {
            return bankAccounts.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public long save(BankAccount bankAccount) {
        lock.writeLock().lock();
        try {
            bankAccounts.put(bankAccount.getIdBankAccount(), bankAccount);
            return bankAccount.getIdBankAccount();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(long id) {
        lock.writeLock().lock();
        try {
            bankAccounts.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addMoney(long id, float amount) {
        lock.writeLock().lock();
        try {
            BankAccount account = bankAccounts.get(id);
            if (account != null) {
                float newBalance = account.getMoney() + amount;
                account.setMoney(newBalance);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeMoney(long id, float amount) {
        lock.writeLock().lock();
        try {
            BankAccount account = bankAccounts.get(id);
            if (account != null && account.getMoney() >= amount) {
                float newBalance = account.getMoney() - amount;
                account.setMoney(newBalance);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }


    public long lastIdSave() {
        lock.readLock().lock();
        try {
            return bankAccounts.size() - 1;
        } finally {
            lock.readLock().unlock();
        }
    }

    public String generateNewBankAccountNumber() {
        lock.readLock().lock();
        try {
            if (!bankAccounts.isEmpty()) {
                String iban = bankAccounts.get((long) bankAccounts.size() - 1).getBankAccountNumber();
                int length = iban.length();
                String numericPart = iban.substring(length - 12, length);
                long number;

                try {
                    number = Long.parseLong(numericPart);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("L'IBAN fornito non è valido.");
                }

                number++; // incrementa il numero
                String newNumericPart = String.format("%012d", number); // mantiene gli zeri iniziali se necessario
                return iban.substring(0, length - 12) + newNumericPart;
            } else return "IT60X0542811101000000000001";
        } finally {
            lock.readLock().unlock();
        }
    }
}
