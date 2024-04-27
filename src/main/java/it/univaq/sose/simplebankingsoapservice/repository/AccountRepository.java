package it.univaq.sose.simplebankingsoapservice.repository;

import it.univaq.sose.simplebankingsoapservice.domain.Account;
import it.univaq.sose.simplebankingsoapservice.domain.Role;
import it.univaq.sose.simplebankingsoapservice.webservice.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountRepository {
    private static final Logger LOG =
            LoggerFactory.getLogger(AccountRepository.class);
    private static volatile AccountRepository instance;

    private final Map<Long, Account> accounts;
    private long lastIndex;
    private final ReentrantReadWriteLock lock;

    private AccountRepository() {
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
        this.accounts = new HashMap<>();
        this.lastIndex = -1;
        this.lock = new ReentrantReadWriteLock();
        initializeAccounts();
    }

    public static AccountRepository getInstance() {
        AccountRepository result = instance;
        if (result == null) {
            synchronized (AccountRepository.class) {
                result = instance;
                if (result == null) {
                    result = new AccountRepository();
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

    private void initializeAccounts() {
        save(new Account(0, "Antonio", "Rossi", "admin", "123456", Role.ADMIN));
        save(new Account(1, "Michela", "Bianchi", "banker", "123456", Role.BANKER));
    }

    public Account findByUsername(String u) throws NotFoundException {
        lock.readLock().lock();
        try {
            return accounts.values().stream()
                    .filter(a -> a.getUsername().equals(u))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Username not found: " + u));
        } finally {
            lock.readLock().unlock();
        }
    }


    public List<Account> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(accounts.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Account findById(long id) throws NotFoundException {
        lock.readLock().lock();
        try {
            Account account = accounts.get(id);
            if (account == null) {
                throw new NotFoundException("Account with ID " + id + " not found.");
            }
            return account;
        } finally {
            lock.readLock().unlock();
        }
    }

    public long lastIdSave() {
        lock.readLock().lock();
        try {
            return lastIndex;
        } finally {
            lock.readLock().unlock();
        }
    }

    public long save(Account account) {
        lock.writeLock().lock();
        try {
            long id = getNextIndex();
            account.setIdAccount(id);
            accounts.put(id, account);
            return account.getIdAccount();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public long updateIdBankAccount(Account account) {
        lock.writeLock().lock();
        try {
            accounts.get(account.getIdAccount()).setIdBankAccount(account.getIdBankAccount());
            return account.getIdAccount();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(Account account) {
        lock.writeLock().lock();
        try {
            accounts.remove(account.getIdAccount());
        } finally {
            lock.writeLock().unlock();
        }
    }
}
