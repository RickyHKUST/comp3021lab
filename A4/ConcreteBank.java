import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcreteBank implements Bank {
// TODO
	HashMap<String, Integer> accountMap = new HashMap<String, Integer>();
	HashMap<String, ReentrantLock> lockMap = new HashMap<String, ReentrantLock>();
	
	@Override
	public boolean addAccount(String accountID, Integer initBalance) {
		if(accountMap.containsKey(accountID)) {return false;}
		accountMap.put(accountID, initBalance);
		lockMap.put(accountID, new ReentrantLock());
		return true;
	}

	@Override
	public boolean deposit(String accountID, Integer amount) {
		if(!accountMap.containsKey(accountID)) {return false;}
		ReentrantLock lock = lockMap.get(accountID);
		synchronized(lock) {
			accountMap.put(accountID, accountMap.get(accountID)+amount);
			lock.notifyAll();
			return true;
		}
	}

	@Override
	public boolean withdraw(String accountID, Integer amount, long timeoutMillis) {
		if(!accountMap.containsKey(accountID)) {return false;}
		ReentrantLock lock = lockMap.get(accountID);
		synchronized(lock) {
			if(accountMap.get(accountID)<amount) {
				try {lock.wait(timeoutMillis);}
				catch (InterruptedException e) {e.printStackTrace();}
			}
			if(accountMap.get(accountID)>=amount) {
				accountMap.put(accountID, accountMap.get(accountID)-amount);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean transfer(String srcAccount, String dstAccount, Integer amount, long timeoutMillis) {
		return accountMap.containsKey(dstAccount) && withdraw(srcAccount,amount,timeoutMillis) && deposit(dstAccount,amount);
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