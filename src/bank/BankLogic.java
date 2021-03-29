package bank;
/**
 * Klassen exekverar olika handlingar i banken.
 * @author Joakim Melander, jokmel-0
 */ 

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Deklarerar instansvariabler 
 */
public class BankLogic implements Serializable { 
	
    private String SAVE_OBJECT_FILE =  "bank_files/test.dat";
	private int accountNumber;
	private int lastAssignedNumber = 1000;
	protected ArrayList<Customer> customers = new ArrayList<Customer>();

	
	/**
	 * Sparar kunder till fil. 
	 */
	public void writeCustToFile() {
		
		if(customers.size() > 0)
		{
			//Försöker skriva till fil
	        try 
	        {
	        	ObjectOutputStream fileOut = new ObjectOutputStream (new
	        			FileOutputStream(SAVE_OBJECT_FILE));
	        	//Skriver senast skapade kontonumret till första raden i filen 
	        	fileOut.writeInt(lastAssignedNumber);
	        	//Skriver sedan alla kundobjekt till filen
	            for(Customer cust: customers)
	            {
					fileOut.writeObject(cust);
	            }
	            //Stänger filen
	            fileOut.close();
	            JOptionPane.showMessageDialog(null, "The customers have been saved.");
	            
	        } 

	        catch (FileNotFoundException e)
	        {
	        	JOptionPane.showMessageDialog(null, "File not found or read-only.");
	        }
	        //Fångar eventuella problem som kan uppstå
	        catch (Exception ex) 
	        {
	        	JOptionPane.showMessageDialog(null, ex);
			}
		}
		else
		{
			//Om inga kunder finns, skrivs inget till filen.
			JOptionPane.showMessageDialog(null, "No customers to save.");
		}
    }

	/**
	 * Läser in senast skapade kontonumret för att det inte ska kunna skapas dubbletter. 
	 */
	public void readAccNoFromFile() {

		//Försöker läsa in från fil
        try 
        {
        	ObjectInputStream fileIn = new ObjectInputStream (new
        
            		FileInputStream(SAVE_OBJECT_FILE));
		
        	//Läser in första raden i filen som vi vet ska vara kontonumret
        	lastAssignedNumber = (int) fileIn.readInt();
        	//Stänger filen
        	fileIn.close();
        //Fångar FileNotFoundException
        } catch (FileNotFoundException e) {
        	JOptionPane.showMessageDialog(null, "FileNotFoundException.");

    	//IOException måste fångas men ger inget fel i detta fall.
        } catch (IOException e) {
	    }    
	}
	
	
	/**
	 * Läser in sparade kunder från fil. 
	 */
	public void readObjectFromFile()
	{
		//Försöker läsa in objektet från fil
        try (ObjectInputStream fileIn = new ObjectInputStream (new
            		FileInputStream(SAVE_OBJECT_FILE));)
    		{
        	
        	lastAssignedNumber = (int) fileIn.readInt();

            while(true)
            {
	            Customer cust = (Customer) fileIn.readObject();
	            
	            int idx = getCustomerIndex(cust.getPNo());
	    		if(idx < 0)
	    			customers.add(cust);
            }
        
        } catch (FileNotFoundException e) {
        	JOptionPane.showMessageDialog(null, "FileNotFoundException.");

        } catch (ClassNotFoundException e) {
        	JOptionPane.showMessageDialog(null, "ClassNotFoundException is caught.");
        
        	//IOException kommer alltid kastas vid första tomma raden i filen.
	    } catch (IOException e) {
	    	//JOptionPane.showMessageDialog(null, "All customers has been loaded.");
	    	
	    }
	}    
	
	
	/**
	 * Sparar kontohändelser till fil. 
	 */
	public void writeEventToFile(String pNo, int accountId) {
		
		//Om personnummer inte hör till något objekt i Customers returneras null 
		int idx = getCustomerIndex(pNo);
		String events = getTransactions(pNo,accountId) +"";
		events = events.replaceAll("[\\[\\]]", "");
		 if(events.length() == 0)
			 JOptionPane.showMessageDialog(null, "No events to print.");
		 else if(idx >= 0)
		 {
			//Försöker skriva till fil
	        try 
	        {
	        	Date date = new Date();
	    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
	    	    String strDate = formatter.format(date); 
	    	    
	    		
	        	OutputStreamWriter fileOut =
	                    new OutputStreamWriter(new FileOutputStream("bank_files/"+String.valueOf(accountId)), StandardCharsets.UTF_8);
	        	
	        	//Skriver senast skapade kontonumret till första raden i filen 
	        	//fileOut.write(strDate + " Events for account: " + String.valueOf(accountId) + "\n\n");
	        	fileOut.write(strDate + " " + customers.get(idx).getFirstName() + " " + customers.get(idx).getSurName() +
    			" " + customers.get(idx).getPNo() + " Account: " + String.valueOf(accountId) + "\n\n");
	        	fileOut.write(events);
	            //Stänger filen
	            fileOut.close();
	            JOptionPane.showMessageDialog(null, "The event log have been saved.");
	            
	        } 
	        //Fångar eventuella problem som kan uppstå
	        catch (Exception ex) 
	        {
	        	JOptionPane.showMessageDialog(null, ex);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "No customer found.");
		}
	}
		
	
	
	/**
	 * Tar fram information om alla kunder och deras konton.
	 * @return ArrayList<String> som innehåller informationen om kunderna inklusive deras konton.
	 */
	public ArrayList<String> getAllCustomers()
	{
		ArrayList<String> allCustomers = new ArrayList<>();
	    for(int i = 0; i < customers.size(); i++)
	    {
	    	//Metodanrop till getCustomerInfo som läggs till i arrayList 
	    	allCustomers.add(customers.get(i).getCustomerInfo());
	    }
	    return allCustomers;
	}
	
	
	/**
	 * Tar fram information om alla kunders personnummer.
	 * @return ArrayList<String> som innehåller alla kunders personnummer.
	 */
	public ArrayList<String> getAllpNo()
	{
		ArrayList<String> allPNo = new ArrayList<>();
	    for(int i = 0; i < customers.size(); i++)
	    {
	    	//Metodanrop till getCustomerInfo som läggs till i arrayList 
	    	allPNo.add(customers.get(i).getPNo());
	    }
	    return allPNo;
	}
	
	
	/**
	 * Skapar nytt objekt av klassen Customer
	 * @param name, nya kundens förnamn
	 * @param surname, nya kundens efternamn
	 * @param pNo, nya kundens personnummer
	 * @return true eller false om kunden skapats eller ej
	 */
	public boolean createCustomer(String name, String surname, String pNo)
	{	
		//Om personnumret redan tillhör ett objekt returneras false
		if(getCustomerIndex(pNo) >= 0)
		{
			return false;
	    }
		//Skapar nytt objekt kund av klassen Customer 
		customers.add(new Customer(name, surname, pNo));
		return true;
	}
	
	
	/**
	 * Tar fram information om en kund.
	 * @param pNo, sökta kundens personnummer
	 * @return String searchedCustomer som innehåller informationen om kunden.
	 */
	public String getCustomer(String pNo)
	{
		int idx = getCustomerIndex(pNo);
		if(idx < 0)
			return null; 
		StringBuilder strBul = new StringBuilder();
		strBul.append(customers.get(idx).getCustomerInfo());
		String searchedCustomer = strBul.toString();
	    return searchedCustomer;
	}
	
	
	/**
	 * Tar fram info om sökta kontot
	 * @param pNo, sökta kundens personnummer
	 * @return ArrayList<String> som innehåller informationen om 
	 */
	public ArrayList<String> getAccInfo(String pNo)
	{
		int idx = getCustomerIndex(pNo);
		if(idx < 0)
			return null; 
		ArrayList<String> searchedAccInfo = new ArrayList<>();
		for (String var : customers.get(idx).accInfo())
		{
			searchedAccInfo.add(var);
	    }
	    return searchedAccInfo;
	}
	
	
	/**
	 * Byta namn på en kund
	 * @param name, sökta kundens förnamn
	 * @param surname, sökta kundens efternamn
	 * @param pNo, sökta kundens personnummer
	 * @return true eller false om namnbytet gick igenom eller ej
	 */
	public boolean changeCustomerName(String name, String surname, String pNo)
	{
		//Om personnummer inte hör till något objekt i Customers eller
		//om för- eller efternamn är tomma returneras false
		int idx = getCustomerIndex(pNo);
	    if(idx < 0 || name.equals("") || surname.equals(""))
	        return false;
	    //Metodanrop för att byta för- och/eller efternamn
		customers.get(idx).SetFirstName(name);
		customers.get(idx).SetSurName(surname);
	    return true;
	}
	
	
	/**
	 * Genererar nytt kontonummer för att sedan kalla på nytt konto
	 * @param pNo, personummer för berörd kund
	 * @return nyskapade kontonumret alternativt -1 om inget konto skapas
	 */
	public int createSavingsAccount(String pNo)
	{

		//Om personnummer inte hör till något objekt i Customers returneras -1
		int idx = getCustomerIndex(pNo);
	    if(idx < 0)
	        return -1;
	    //Genererar ett nytt kontonummer
	    int accNo = getNewAccountNo();
	    //Metodanrop till addAccount med nya kontonumret  
	    customers.get(idx).addAccount(accNo,"Savings account");
	    return accNo;
	}
	
	
	/**Skapar ett kreditkonto till kund med personnummer pNr, returnerar 
	 * kontonumret som det skapade kontot fick alternativt returneras -1 om inget konto skapades.
	 */
	public int createCreditAccount(String pNo)
	{
		//Om personnummer inte hör till något objekt i Customers returneras -1
		int idx = getCustomerIndex(pNo);
	    if(idx < 0)
	        return -1;
	    //Genererar ett nytt kontonummer
	    int accNo = getNewAccountNo();
	    //Metodanrop till addAccount med nya kontonumret  
	    customers.get(idx).addAccount(accNo,"Credit account");
	    return accNo;
	}
	
	
	/**
	 * Hjälpmetod till createSavingAccount för att generera nytt kontonummer 
	 * @return nytt genererat kontonummer
	 */
	private int getNewAccountNo()
	{
		lastAssignedNumber++;
		accountNumber = lastAssignedNumber; 
		return accountNumber;
	}

	
	/**
	 * Tar fram info om ett sökt konton
	 * @param pNo, personnummer för berörd kund
	 * @param accountId, kontonummmer som söks
	 * @return en String som innehåller presentation av kontot: kontonummer saldo, kontotyp räntesats
	 */
	public String getAccount(String pNo, int accountId)
	{
		//Om personnummer inte hör till något objekt i Customers returneras null
		int idx = getCustomerIndex(pNo);
		if(idx < 0){
			return null;
		}
		//Använder en StringBuilder för att göra om returnerad array från getFormatedAccount till en String.
		StringBuilder strBul = new StringBuilder();
		strBul.append(customers.get(idx).getFormatedAccounts(accountId));
		String str = strBul.toString();
		return str;	
	}
	
	
	/**
	 * Gör en insättning till en kunds konto
	 * @param pNo, personnummer för berörd kund
	 * @param accountId, kontonummmer som berörs
	 * @param amount, summa som ska sättas in på kontot
	 * @return True eller false om insättningen lyckades eller ej
	 */
	public boolean deposit(String pNo, int accountId, double amount)
	{
		int idx = getCustomerIndex(pNo);
		//Om kunden inte finns eller insättningen är negativ returneras false
		if(idx < 0 || amount <= 0)
			return false; 
		//Returnerar metodanropet för makeTransaction som är true eller false
		return(customers.get(idx).makeTransaction(accountId, Math.round(amount * 100.0) / 100.0));    
	}
	
	
	/**
	 * Gör ett uttag från en kunds konto
	 * @param pNo, personnummer för berörd kund
	 * @param accountId, kontonummmer som berörs
	 * @param amount, summa som ska dras från kontot
	 * @return True eller false om uttaget lyckades eller ej
	 */
	public boolean withdraw(String pNo, int accountId, double amount)
	{	
		int idx = getCustomerIndex(pNo);
		//Om kunden inte finns eller insättningen är negativ returneras false
		if(idx < 0 || amount <= 0 )
			return false; 
		//Returnerar metodanropet för makeTransaction som är true eller false,
		//amount sätts till negativt för att göra ett uttag
		return(customers.get(idx).makeTransaction(accountId, -Math.round(amount * 100.0) / 100.0));	
	}
	
	
	/**
	 * Hämtar en lista som innehåller presentation av alla transaktioner som gjorts på kontot
	 * @param pNo, personnummer för berörd kund
	 * @param accountId, berört kontonummer
	 * @return ArrayList med transaktionerna. Null om fel
	 */
	public ArrayList<String> getTransactions(String pNo, int accountId)
	{
		//Om personnummer inte hör till något objekt i Customers returneras null
		int idx = getCustomerIndex(pNo);
		if(idx < 0)
		{
			return null;
		}
		ArrayList<String> searchedTrans = new ArrayList<>();
		//Om inga transaktioner skett returneras null
		if(customers.get(idx).getFormatedTrans(accountId) == null)
			return null;
		//Varje transaktion på kundens konto läggs in i listan searchedTrans
		for (String var : customers.get(idx).getFormatedTrans(accountId))
		{
			searchedTrans.add(var);
		}
		return searchedTrans; 	
	}
	
	
	/**
	 * Avslutar en kunds konto och returnerar info om det avslutade kontot
	 * @param pNo, personnummer vars konto ska avslutas
	 * @param accountId, kontonummmer som ska avslutas
	 * @return String med info om det avslutade kontot. Null om inget konto avslutas
	 */
	public String closeAccount(String pNo, int accountId)
	{	
		//Om personnummer inte hör till något objekt i Customers returneras null 
		int idx = getCustomerIndex(pNo);
		if(idx < 0)
		{
			return null;
		}
		//Använder en StringBuilder för att göra om returnerad array från closeAccount till en String.
		StringBuilder strBul = new StringBuilder();
		strBul.append(customers.get(idx).closeAccount(accountId));
		String str = strBul.toString();
		str = str.replaceAll("[\\[\\]]", "");
		return str;
	}
	
	
	/**
	 * Tar bort en kund med personnummer pNo ur banken,
	 * alla kundens eventuella konton tas också bort och resultatet returneras.
	 * @param pNo, personnummer som ska raderas
	 * @return ArrayList med info om borttagna kunden och avslutade konton. Null om ingen kund tas bort.
	 */
	public ArrayList<String> deleteCustomer(String pNo)
	{
		ArrayList<String> deletedCustomer = new ArrayList<String>();
		//Om personnummer inte hör till något objekt i Customers returneras null 
		int idx = getCustomerIndex(pNo);
	    if(idx < 0)
	        return null;	
	    //Kallar på closeAccount och lägger till alla de avslutade kontona i listan deletedCustomer 
	    deletedCustomer.addAll(customers.get(idx).closeAccount(null));
	    customers.remove(idx);
	    return deletedCustomer;
	}
	
	
	/**
	 * Undersöker om kundens personnummer matchar personnummer i Customers 
	 * @param pNo, personnummer som undersöks
	 * @return index för kunden i Customers alternativt -1 om kontot inte existerar.
	 */
	protected int getCustomerIndex(String pNo)
	{
	    int idx = 0;
	    for(int i = 0; i < customers.size(); i++)
	    {
	        //Om kundens personnummer existerar returneras index för objektet i Customers
	    	if(customers.get(i).getPNo().equals(pNo))
	    	{
	    		return idx;
	    	}
	    	idx += 1;
	    }
	    //Om ingen kund hittas returneras -1
	    return -1;
	}
}

