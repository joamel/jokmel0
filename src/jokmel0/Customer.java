package jokmel0;
/**
 * Klassen skapar ett objekt kund som håller information om kunden.
 * @author Joakim Melander, jokmel-0
 */

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Deklarerar instansvariabler 
 */
public class Customer implements Serializable {
	protected ArrayList<Account> accounts = new ArrayList<Account>();
	private String firstName;
	private String surName;
	private String pNo;

	
	/**
	 * Objektet tilldelas instansvariabler 
	 */
	public Customer(String firstName, String surName, String pNo){
	    this.firstName = firstName;
	    this.surName = surName;
	    this.pNo = pNo;
	}
	
	
	/**
	 * @return Objektets förnamn
	 */
	public String getFirstName()
	{
	    return firstName;
	}
	
	
	/**
	 * @return Objektets efternamn
	 */
	public String getSurName()
	{		
	    return surName;
	}
	
	
	/**
	 * @return Objektets personnummer
	 */
	public String getPNo(){
	    return pNo;
	}
	
	
	/**
	 * @return formaterad info om kunden
	 */
	public String getCustomerInfo(){
		return String.format(pNo + " " + firstName + " " + surName);
	}
	
	
	/**
	 * Hämtar info om kundens konton
	 * @return ArrayList<String> med kontoinfo
	 */
	public ArrayList<String> accInfo() 
	{
		ArrayList<String> customerAccounts = new ArrayList<String>();
    	for(Account var: accounts)
    	{
	        customerAccounts.add(String.valueOf(var.accNo));
	        customerAccounts.add(String.valueOf(var.accType));
    		customerAccounts.add(String.valueOf(var.balance));
        }
    	return customerAccounts;
	}

	
	/**
	 * Tar fram formaterat kontoinfo för ett eller alla kontonummer för en kund
	 * @param accNo, kontonummer som ska tas fram. Är null om alla konton berörs.
	 * @return arrayList innehållandes formaterade konton
	 */
	public ArrayList<String> getFormatedAccounts(Integer accNo)
	{
		ArrayList<String> formatedAccounts = new ArrayList<String>();
		if(accNo == null)
		{
			//Metodanrop till getFormatedAccount för alla objekt i Accounts 
	    	for(Account var: accounts)
	    	{
		        formatedAccounts.add(var.getFormatedAccount());
	        }
		}
    	else
    	{
    		int idx = getAccountIndex(accNo);
    		//Om kontonumret inte hör till kunden returneras null
    		if(idx < 0)
		        return null;
    		formatedAccounts.add(accounts.get(idx).getFormatedAccount());
	    }
	    return formatedAccounts;
	}
	
	
	
	/**
	 * Tar fram formaterad lista av transaktioner för ett kontonummer hos en kund
	 * @param accNo, kontonummer som ska tas fram
	 * @return arrayList innehållandes presentation av transaktionerna
	 */
	public ArrayList<String> getFormatedTrans(int accNo)
	{
		int idx = getAccountIndex(accNo);
		//Om kontonumret inte hör till kunden returneras null
		if(idx < 0)
	        return null;
		return accounts.get(idx).getFormatedTrans();
	}
	
	
	/**
	 * @param newFirstName, förnamn som ska ändras för kunden
	 */
	public void SetFirstName(String newFirstNname)
	{
	    firstName = newFirstNname;
	}

	
	/**
	 * @param newSurName, efternamn som ska ändras för kunden
	 */
	public void SetSurName(String newSurName)
	{
	    surName = newSurName;
	}
	
	
	/**
	 * Skapar nytt objekt av klassen Accounts
	 * @param accNo, kontonummer som ska skapas
	 */
	public void addAccount(int accNo,String accType)
	{
		if(accType.equals("Savings account"))
		{
		    Account newAccount = new SavingsAccount(0.0, accNo, accType);
			accounts.add(newAccount);
		}
		else if(accType.equals("Credit account"))
		{	
			//Skapar nytt objekt newAccount av klassen Account 
		    Account newAccount = new CreditAccount(0.0, accNo, accType);
		    accounts.add(newAccount);
		}
	}
	
	
	/**
	 * Kallar på metoden transaction i Accounts om accNo tillhör kunden 
	 * @param accNo, konto som berörs.
	 * @param diff, summan som ska sättas in eller tas ut från kontot
	 * @return boolean om transaktionen lyckades eller ej.
	 */
	public boolean makeTransaction(int accNo, double diff)
	{
		//Undersöker att kontot tillhör kundens konton annars returneras false
		int idx = getAccountIndex(accNo);
	    if(idx<0)
	    	return false;
	    //
	    //Loopar genom kundens konton tills rätt konto hittats och returnerar sedan metodanrop transaction.
	    for(Account var: accounts)
	    {
	        if(var.getAccountNumber() == accNo)
	        	return(var.transaction(diff)); 	
    	}
	    return false; 
	}
	
	
	/**
	 * Stänger ned ett konto 
	 * @param accNo, konto som ska stängas. Är null om alla konton för objektet ska stängas.
	 * @return array av formaterade konton som stängs ned.
	 */
	public ArrayList<String> closeAccount(Integer accNo)
	{
		ArrayList<String> closedAcc = new ArrayList<String>();
		//Om accNo är null stängs alla konton för objektet ned
		if(accNo == null)
		{
			for(Account var: accounts)
			{		
				closedAcc.add(var.getInterest());	
			}
			//Alla kontoobjekt tas bort när accounts deklareras på nytt
			accounts = new ArrayList<Account>();		
		}	
		else
		{
			int idx = getAccountIndex(accNo);
    		//Om kontonumret inte hör till kunden returneras null
		    if(idx < 0)
		        return null;
			closedAcc.add(accounts.get(idx).getInterest());
			//Kontoobjektet tas bort ur listan accounts 
			accounts.remove(idx);
		}
			return closedAcc;
	}

	
	/**
	 * Undersöker om kontonumret tillhör kunden
	 * @param accNo, kontonumret som undersöks
	 * @return index för kontot i accounts alternativt -1 om kontot inte existerar.
	 */
	protected int getAccountIndex(int accNo)
	{
	    int idx = 0;
	    //Loopar genom accounts. Kallar på getAccountNumber för att jämföra objektets kontonummer med param.
	    for(int i = 0; i < accounts.size(); i++)
	    {
	        //Om kontot existerar returneras indexet för objektet i accounts
	    	if(accounts.get(i).getAccountNumber() == accNo)
	    		return idx;
	    	idx += 1;
	    }
	    //Om kontot inte existerar i accounts returneras -1;
	    return -1;
	}
}