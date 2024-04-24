package it.univaq.sose.simplebankingsoapservice.repository;

import it.univaq.sose.simplebankingsoapservice.domain.Account;
import it.univaq.sose.simplebankingsoapservice.webservice.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountRepository {
    private static volatile AccountRepository instance;

    private final Map<Long, Account> accounts;
    private final ReentrantReadWriteLock lock;

    private AccountRepository() {
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
        this.accounts = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
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
//            return accounts.get(id);
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
            return accounts.size() - 1;
        } finally {
            lock.readLock().unlock();
        }
    }

    public long save(Account account) {
        lock.writeLock().lock();
        try {
            accounts.put(account.getIdAccount(), account);
            return account.getIdAccount();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(long id) {
        lock.writeLock().lock();
        try {
            accounts.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
