import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcreteBank implements Bank {
// TODO
	HashMap<String, Integer> accountMap = new HashMap<String, Integer>();
	HashMap<String, ReentrantLock> LockMap = new HashMap<String, ReentrantLock>();
	
	@Override
	public boolean addAccount(String accountID, Integer initBalance) {
		if(accountMap.containsKey(accountID)) {return false;}
		accountMap.put(accountID, initBalance);
		LockMap.put(accountID, new ReentrantLock());
		return true;
	}

	@Override
	public boolean deposit(String accountID, Integer amount) {
		ReentrantLock lock = LockMap.get(accountID);
		synchronized(lock) {
			if(!accountMap.containsKey(accountID)) {return false;}
			accountMap.put(accountID, accountMap.get(accountID)+amount);
			lock.notifyAll();
			return true;
		}
	}

	@Override
	public boolean withdraw(String accountID, Integer amount, long timeoutMillis) {
		if(!accountMap.containsKey(accountID)) {return false;}
		ReentrantLock lock = LockMap.get(accountID);
		synchronized(lock) {
			int balance = accountMap.get(accountID);
			if(balance<amount) {
				try {lock.wait(timeoutMillis);}
				catch (InterruptedException e) {e.printStackTrace();}
				balance = accountMap.get(accountID);
			}
			if(balance>=amount) {
				accountMap.put(accountID, balance-amount);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean transfer(String srcAccount, String dstAccount, Integer amount, long timeoutMillis) {
		if(withdraw(srcAccount,amount,timeoutMillis)) {
			if(deposit(dstAccount,amount)) {return true;}
			else {deposit(srcAccount,amount);}
		}
		return false;
	}

	@Override
	public Integer getBalance(String accountID) {
		return (accountMap.containsKey(accountID))?accountMap.get(accountID):0;
	}

	@Override
	public void doLottery(ArrayList<String> accounts, Miner miner) {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for(String account:accounts) {
			Thread t = new Thread(()->{deposit(account, miner.mine(account));});
			threads.add(t);
			t.start();
		}
		for(Thread t:threads) {
			try {t.join();}
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
