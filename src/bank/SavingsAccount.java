package bank;

/**
 * Subklassen ärver från Account och skapar ett objekt sparkonto och håller information detta konto.
 * @author Joakim Melander, jokmel-0
 */
 

/**
 * Deklarerar instansvariabler 
 */
public class SavingsAccount extends Account {
	private int noWd = 0;
	private static double WD_RATE = 2.0;
	
	
	/**
	 * Konstruktorn åberopar basklassens konstruktor
	 */
	public SavingsAccount(double balance, int accNo, String accType)
	{
		super(balance, accNo, accType);	
	}
	
	
	/**
	 * @return double räntesatsen som är satt till 1.0; 
	 */
	public double interestRate() 
	{
		return 1.0;
	}

	/**
	 * @return double räntesatsen som är satt till 1.0; 
	 */
	public int getNoWd() 
	{
		return noWd;
	}

	
	/**
	 * Utför transaktion på kontot 
	 * @param diff, summan som ska sättas in eller tas ut från kontot
	 * @return boolean, om transaktionen lyckades eller ej.
	 */
	public boolean transaction(double diff)
	{
	    //En positiv diff betyder insättning
		if(diff > 0)
		{
			balance += diff;
			dateTrans(diff);
			return true;
		}
		//En negativ diff innebär ett uttag
		if(diff < 0)
		{
			//Utförs bara om uttaget är mindre eller lika med saldot på kontot
			if (Math.abs(diff) <= balance)
			{
				if(noWd < 1)
					balance += diff;
				else
				{
					if(balance + (diff + diff * WD_RATE / 100) < 0)
						return false;
					else
						diff = diff + diff * WD_RATE / 100;
						balance += diff;
				}
				noWd ++;
				dateTrans(diff);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return formaterat kontoinfo
	 */
	public String getFormatedAccount()
	{
		return String.format(accNo + " " + balance +" kr"+ " " + accType + " " + interestRate() + " %%");
	}
	
	/**
	 * Räknar ut räntan på kontot när det stängs
	 * @return String, formaterat kontoinfo
	 */
	@Override
	public String getInterest()
	{
		interest = balance * interestRate() / 100;
	    return String.format("Account: " + accNo + ", Balance: " + balance +" SEK"+", Account type: "+ accType +", Interest: "+ interest +" SEK");
	}
}