package bank;

import java.util.ArrayList;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Basklassen skapar ett objekt konto och håller information om kundens konto.
 * @author Joakim Melander, jokmel-0
 */


/**
 * Deklarerar instansvariabler 
 */
public abstract class Account implements Serializable
{
	protected double balance;
	protected int accNo;
	protected String accType;
	protected double interest;
	protected double diffBalance;
	protected ArrayList<String> transList = new ArrayList<String>();
	
	
	/**
	 * Objektet tilldelas instansvariabler 
	 */
	public Account(double balance, int accNo, String accType)
	{
		this.balance = balance;
		this.accNo = accNo;
		this.accType = accType;
	}
	
	/**
	 * abstract klass för att få subklassens ränta
	 */
	public abstract double interestRate();
	
	
	/**
	 * Utför transaktion på kontot 
	 * @param diff, summan som ska sättas in eller tas ut från kontot
	 * @return boolean om transaktionen lyckades eller ej.
	 */
	public abstract boolean transaction(double diff);
	
	
	/**
	* Lägger till presentation av alla transaktioner som gjorts på kontot i listan transList
	* @param diff, summan som sätts in eller tas ut från kontot
	*/
	public void dateTrans(double diff)
	{	
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String strDate = formatter.format(date);  
	    transList.add("\n" + strDate + " " + diff + " SEK" + " Balance: " + balance + " SEK");
	}
	
	
	/**
	 * @return objektets kontonummer
	 */
	public int getAccountNumber()
	{
	    return accNo;
	}
	
	
	/**
	 * abstract klass för att räkna ut subklassens ränta när kontot stängs
	 */
	public abstract String getInterest();

	
	/**
	 * @return formaterade kontotransaktioner
	 */
	public ArrayList<String> getFormatedTrans()
	{
		return transList;
	}
	
	
	/**
	 * @return formaterat kontoinfo
	 */
	public abstract String getFormatedAccount();
	

	public abstract int getNoWd();
		
	
}