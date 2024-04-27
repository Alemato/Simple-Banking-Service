package it.univaq.sose.simplebankingsoapservice.repository;

import it.univaq.sose.simplebankingsoapservice.domain.BankAccount;
import it.univaq.sose.simplebankingsoapservice.webservice.InsufficientFundsException;
import it.univaq.sose.simplebankingsoapservice.webservice.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccountRepository {
    private static volatile BankAccountRepository instance;
    private final Map<Long, BankAccount> bankAccounts;
    private long lastIndex;
    private final ReentrantReadWriteLock lock;

    private BankAccountRepository() {
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
        this.bankAccounts = new HashMap<>();
        this.lastIndex = -1;
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

    private long getNextIndex() {
        lastIndex += 1;
        return lastIndex;
    }

    public List<BankAccount> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(bankAccounts.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public BankAccount findById(long id) throws NotFoundException {
        lock.readLock().lock();
        try {
//            return bankAccounts.get(id);
            BankAccount bankAccount = bankAccounts.get(id);
            if (bankAccount == null) {
                throw new NotFoundException("BankAccount with ID " + id + " not found.");
            }
            return bankAccount;
        } finally {
            lock.readLock().unlock();
        }
    }

    public long save(BankAccount bankAccount) {
        lock.writeLock().lock();
        try {
            long id = getNextIndex();
            bankAccount.setIdBankAccount(id);
            bankAccounts.put(id, bankAccount);
            return bankAccount.getIdBankAccount();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(BankAccount bankAccount) {
        lock.writeLock().lock();
        try {
            bankAccounts.remove(bankAccount.getIdBankAccount());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addMoney(long idBankAccount, float amount) {
        lock.writeLock().lock();
        try {
            BankAccount account = bankAccounts.get(idBankAccount);
            if (account != null) {
                float newBalance = account.getMoney() + amount;
                account.setMoney(newBalance);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeMoney(long idBankAccount, float amount) throws NotFoundException, InsufficientFundsException {
        lock.writeLock().lock();
        try {
            BankAccount bankAccount = bankAccounts.get(idBankAccount);
            if (bankAccount == null) {
                throw new NotFoundException("Account with ID " + idBankAccount + " not found.");
            }
            if (bankAccount.getMoney() < amount) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal.");
            }
            if (bankAccount.getMoney() >= amount) {
                float newBalance = bankAccount.getMoney() - amount;
                bankAccount.setMoney(newBalance);
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
            String base = "IT60X0542811101";
            String indexAsString = Long.toString(lastIndex + 2);
            int remainingLength = 12 - indexAsString.length();
            StringBuilder zeros = new StringBuilder();
            for (int i = 0; i < remainingLength; i++) {
                zeros.append("0");
            }
            return base + zeros + indexAsString;

        } finally {
            lock.readLock().unlock();
        }
    }
}
