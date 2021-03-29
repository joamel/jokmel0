package bank;

/**
 * Subklassen ärver från Account och skapar ett objekt kreditkonto och håller information detta konto.
 * @author Joakim Melander, jokmel-0
 */


/**
 * Deklarerar instansvariabler 
 */
public class CreditAccount extends Account {
	private static double DEBT_RATE = 7.0;
	private static double CREDIT_LIMIT = 5000;

	
	/**
	 * Konstruktorn åberopar basklassens konstruktor
	 */
	public CreditAccount(double balance, int accNo, String accType)
	{
		super(balance,accNo,accType);
	}
	
	
	/**
	 * @return double räntesatsen som är satt till 0.5; 
	 */
	public double interestRate() 
	{
		return 0.5;
	}
	
	
	/**
	 * Utför transaktion på kontot 
	 * @param diff, summan som ska sättas in eller tas ut från kontot
	 * @return boolean om transaktionen lyckades eller ej.
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
			//Utförs bara om balansen efter uttaget är större än eller lika med kreditgränsen
			
			if (balance+diff >= -CREDIT_LIMIT)
			{
				balance += diff;
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
		if(balance >= 0) 
		{
			return String.format(accNo + " " + balance +" kr"+ " " + accType + " " + interestRate() + " %%");
		}
		else
		{
			return String.format(accNo + " " + balance +" kr"+ " " + accType + " " + DEBT_RATE + " %%");
		}
	}
	
	
	/**
	 * Räknar ut räntan på kontot på det stängs
	 * @return String, formaterat kontoinfo
	 */
	@Override
	public String getInterest()
	{
		if(balance >= 0) 
		{
			interest = balance * interestRate() / 100;	
		}
		else if(balance < 0)
		{
			interest = balance * DEBT_RATE / 100;
		}
		return "\nAccount: " + accNo + ", Balance: " + balance +" SEK" + ", Account type: "+ accType +", Interest: "+ interest +" SEK";
	}

	
	/**
	 * Metoden ärvs från account men används ej i denna klass.
	 * @return 0
	 */
	@Override
	public int getNoWd() {
		// TODO Auto-generated method stub
		return 0;
	}
}