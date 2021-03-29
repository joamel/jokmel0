package jokmel0;


/**
 * GUI användargränssnittet till programmet.
 * @author Joakim Melander, jokmel-0
 */
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

public class GUI extends JFrame implements ActionListener
{
	private static final int FRAME_WIDTH = 700;
	private static final int FRAME_HEIGHT = 300;
	
	private BankLogic bl;
	private String currPNo;
	private String currAcc;
	private String currAccType;
	private String currBalance;
	private int currPos;


	//Komponenterna till menuPanel
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu newStuff;
	private JMenuItem newCust;
	private JMenuItem newSavAcc;
	private JMenuItem newCredAcc;
	private JMenuItem exitMenu;
	private JMenuItem backMenu;
	private JMenuItem loadItem;
	private JMenuItem saveItem;
	private JMenuItem nameItem;
	private JMenuItem delCustItem;
	private JMenuItem delAccItem;

	
	//Komponenterna till startPanel
	private JPanel startPanel;

	//Komponenterna till newCustomerPanel
	private JPanel newCustomerPanel;
	private JTextField nameField;
	private JTextField surNameField;
	private JTextField pNoField;
	
	//Komponenterna till customerPanel
	private JPanel customerPanel;
	private JList customerList;

	private JLabel customerTextLabel = new JLabel();
	
	//Komponenterna till accountPanel
	private JPanel accountPanel;
	JComboBox cb = new JComboBox<>();
	private JTextField accTypeField = new JTextField();
	private JTextField balanceField = new JTextField();
	private JTextField customerField = new JTextField();
	private JTextField amountField;
	//Deklarerar objekt av JRadioButton 
    JRadioButton depRadButton; 
    JRadioButton withRadButton; 
    //Deklarerar objekt av ButtonGroup 
    ButtonGroup radGroup; 
    
    private JList accEventList;
	

	public static void main(String[] args)
	{
		GUI frame = new GUI();
		frame.setVisible(true);	
	}

	public GUI()
	{
		createComponents();
		setTitle("Bank");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createComponents()
	{
		bl = new BankLogic();
		bl.readAccNoFromFile();
		
		/*//Lägger till testcustomerer och testkonton för att förenkla testning
		bl.createCustomer("Stina","Nilsson","901021-4322");
		bl.createCustomer("Pelle", "Persson", "710223-4577");
		bl.createCustomer("Frans", "Fransson", "890129-0513");
		bl.createSavingsAccount("901021-4322");
		bl.createCreditAccount("901021-4322");
		bl.createSavingsAccount("710223-4577");
		bl.createCreditAccount("890129-0513");
		bl.deposit("901021-4322", 1001, 1000);
		bl.deposit("710223-4577", 1002, 500);*/
		 
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//Skapar olika paneler med layoutmanager BorderLayout och sätter dess storlekar
		startPanel = new JPanel(new BorderLayout());
		startPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		newCustomerPanel = new JPanel(new BorderLayout());
		newCustomerPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		customerPanel = new JPanel(new BorderLayout());
		customerPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		accountPanel = new JPanel(new BorderLayout());
		accountPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
		
		//Skapar en panel så att switchandet mellan paneler ska fungera
		JPanel framePanel = new JPanel();
		
		//Skapar upp menypanelen som ligger upptill i framePanel
		createMenuPanel();
				
		//Skapa upp panelerna och lägg till dem till framePanel
		createStartPanel();
		framePanel.add(startPanel);
		createNewCustomerPanel();
		framePanel.add(newCustomerPanel);
		createCustomerPanel();
		framePanel.add(customerPanel);		
		createAccountPanel();
		framePanel.add(accountPanel);
		
		//Lägger till framePanel till fönstret
		add(framePanel);
		
		//Anropas för att fönstret ska få rätt storlek uifrån de önskade storlekar och layouts
		pack();
		
		//Ser till så att testcustomererna syns i vår JList
		customerList.setListData(bl.getAllCustomers().toArray());
		
	}

	/**
	 * Skapar upp menuPanel 
	 */
	private void createMenuPanel()
	{
		//Skapar upp två menyval och lägger til dem till menuBar
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		editMenu.setEnabled(false);
		
		//Skapar upp newStuff som läggs till fileMenu
		newStuff = new JMenu("New");
		fileMenu.add(newStuff);
		
		//Skapar sedan upp tre JMenuItems med varsina Listeners som läggs till newStuff
		newCust = new JMenuItem("New customer");
		ActionListener custListener = new ClickMainMenuListener();
		newCust.addActionListener(custListener);
		newStuff.add(newCust);
		newSavAcc = new JMenuItem("New savings account");
		ActionListener savAccListener = new ClickAccountListener();
		newSavAcc.addActionListener(savAccListener);
		newStuff.add(newSavAcc);
		newSavAcc.setEnabled(false);
		newCredAcc = new JMenuItem("New credit account");
		ActionListener credAccListener = new ClickAccountListener();
		newCredAcc.addActionListener(credAccListener);
		newStuff.add(newCredAcc);
		newCredAcc.setEnabled(false);

		//Skapar upp ytterligare JMenuItems med Listeners och lägger till dem
		loadItem = new JMenuItem("Load customers");
		ActionListener loadListener = new ClickCustomerListener();
		loadItem.addActionListener(loadListener);
		fileMenu.add(loadItem);
		loadItem.setVisible(false);
		
		saveItem = new JMenuItem("Save customers");
		ActionListener saveListener = new ClickCustomerListener();
		saveItem.addActionListener(saveListener);
		fileMenu.add(saveItem);
		saveItem.setVisible(false);
		
		backMenu = new JMenuItem("Back");
		ActionListener backListener = new ClickNewCustomerListener();
		backMenu.addActionListener(backListener);
		//Visas inte vid initieringen
		backMenu.setVisible(false);
		fileMenu.add(backMenu);
		
		exitMenu = new JMenuItem("Exit");
		ActionListener exitListener = new ClickMainMenuListener();
		exitMenu.addActionListener(exitListener);
		fileMenu.add(exitMenu);
		
		delCustItem = new JMenuItem("Delete customer");
		ActionListener delListener = new ClickCustomerListener();
		delCustItem.addActionListener(delListener);
		editMenu.add(delCustItem);

		delAccItem = new JMenuItem("Close account");
		ActionListener delAccListener = new ClickAccountListener();
		delAccItem.addActionListener(delAccListener);
		editMenu.add(delAccItem);
				
		nameItem = new JMenuItem("Change name");
		ActionListener nameListener = new ClickAccountListener();
		nameItem.addActionListener(nameListener);
		editMenu.add(nameItem);
		nameItem.setVisible(false);
	}
	
	
	/**
	 * Skapar upp den första panelen
	 */
	private void createStartPanel()
	{
		ActionListener listener = new ClickMainMenuListener();

		//Skapar upp topPanel med 3 rader och 1 kolumn
		JPanel topPanel = new JPanel(new GridLayout(3,1));

		//Skapar och lägger till knappar
		JButton addButton = new JButton("New customer");
		addButton.addActionListener(listener);
		topPanel.add(addButton);
		
		JButton showButton = new JButton("Already customer");
		showButton.addActionListener(listener);
		topPanel.add(showButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(listener);
		topPanel.add(exitButton);
		
		//Lägger till topPanel till mainMenuPanel
		startPanel.add(topPanel);
		
		//Denna panel ska visas när programmet startar
		startPanel.setVisible(true);
	}
	
	
	/**
	 * Skapar upp den andra panelen (newCustomer)
	 */
	private void createNewCustomerPanel()
	{
		ActionListener listener = new ClickNewCustomerListener();
		
		//Skapar upp paneler med x rader och y kolumn
		JPanel topPanel = new JPanel(new GridLayout(1,2));
		JPanel leftPanel = new JPanel(new GridLayout(6,1));
		JPanel rightPanel = new JPanel(new GridLayout(2,1));	
		
		//Skapar och lägger till textfälten
		nameField = new JTextField(10);
		nameField.setBorder(BorderFactory.createTitledBorder("First Name"));
		leftPanel.add(nameField);
		
		surNameField = new JTextField(10);
		surNameField.setBorder(BorderFactory.createTitledBorder("Surname"));
		leftPanel.add(surNameField);
		
		pNoField = new JTextField(10);
		pNoField.setBorder(BorderFactory.createTitledBorder("Personal Identity Number"));
		leftPanel.add(pNoField);
		
		//Skapar och lägger till knappar
		JButton addButton = new JButton("Add");
		addButton.addActionListener(listener);
		rightPanel.add(addButton);
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(listener);
		rightPanel.add(backButton);
		
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
		//Lägger till topPanel till newCustomerPanel
		newCustomerPanel.add(topPanel);
		
		//Denna panel ska inte visas när programmet startar
		newCustomerPanel.setVisible(false);
	}
	
	
	/**
	 * Skapar upp den tredje panelen (customer)
	 */
	private void createCustomerPanel()
	{
		ActionListener listener = new ClickCustomerListener();
		
		//Skapar upp topPanel med 1 rad och två kolumner
		JPanel topPanel = new JPanel(new GridLayout(1,2));
		//Skapar upp leftPanel med 2 rader och 1 kolumn
		JPanel rightPanel = new JPanel(new GridLayout(2,1));	

		//Skapar och lägger till JList customerList		
		customerList = new JList();
		customerList.setBorder(BorderFactory.createTitledBorder("Customer"));
		
		//Anonym lyssnare som hanterar vad som händer när vi klickar på en customer i listan
		customerList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent evt) 
			{
				//Sätter vald kunds personnummer till gällande
				setcurrentPNo();
			}
		});
		
		//Lägger till JList customerList till topPanel
		topPanel.add(new JScrollPane(customerList));	
		
		//Skapar och lägger till knappar
		JButton enterButton = new JButton("Enter");
		enterButton.addActionListener(listener);
		rightPanel.add(enterButton);
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(listener);
		rightPanel.add(backButton);
		
		//Lägger till rightPanel till topPanel
		topPanel.add(rightPanel);
		
		//Lägger till topPanel till customerPanel
		customerPanel.add(topPanel);
	
		//Denna panel ska inte visas när programmet startar
		customerPanel.setVisible(false);
	}
	
	
	/**
	 *Skapar upp den fjärde panelen (accounts) 
	 */
	private void createAccountPanel()
	{
		ActionListener listener = new ClickAccountListener();
		
		//Användaren får inte ändra i detta fält
		customerField.setEditable(false); 
		//Centrerar texten i fältet
		customerField.setHorizontalAlignment(JTextField.CENTER); 	
		
		//Skapar upp paneler med olika antal rader och kolumner
		JPanel topPanel = new JPanel(new GridLayout(1,2));
		JPanel leftPanel = new JPanel(new GridLayout(7,1));	
		JPanel rightPanel = new JPanel(new GridLayout(3,1));

		//Skapar en comboBox som läggs till leftPanel
	    cb.setBorder(BorderFactory.createTitledBorder("Account Number"));
		leftPanel.add(cb);
		//Lägger till en Listerner för val i comboboxen
		cb.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//Index till kund som markerats i customerList
				int position = cb.getSelectedIndex();
				//Endast om man valt ett konto
				if(position > -1)
				//Uppdaterar gällande kontoinfo och användargränssnitt
				setAccountInfo(position);
		    }
		});
		
		//Skapar och lägger till textfälten
		accTypeField.setEditable(false);
		accTypeField.setBorder(BorderFactory.createTitledBorder("Account Type"));
		leftPanel.add(accTypeField);
		balanceField.setEditable(false);
		balanceField.setBorder(BorderFactory.createTitledBorder("Balance"));
		leftPanel.add(balanceField);
		
		//Skapar och lägger till knappar
		JRadioButton depRadButton = new JRadioButton("Deposit");
		depRadButton.setSelected(true);
		depRadButton.addActionListener(listener);
		leftPanel.add(depRadButton);

		//Skapar och lägger till knappar
		JRadioButton withRadButton = new JRadioButton("Withdraw");
		withRadButton.addActionListener(listener);
		leftPanel.add(withRadButton);
		  
		//Skapar en ButtonGroup
        radGroup = new ButtonGroup();

        //Skapar upp två radiobuttons för insättning och uttag och lägger till leftPanel
        depRadButton.setText("Deposit");
        depRadButton.setActionCommand("Deposit");
        withRadButton.setText("Withdraw");
        withRadButton.setActionCommand("Withdraw");
        leftPanel.add(depRadButton); 
        leftPanel.add(withRadButton); 
        //Lägger till dem i ButtonGroup för att bara en ska kunna markeras åt gången.
        radGroup.add(depRadButton); 
        radGroup.add(withRadButton); 
		
		//Skapar och lägger till knappar
		amountField = new JTextField(10);
		amountField.setBorder(BorderFactory.createTitledBorder("Amount SEK"));
		leftPanel.add(amountField);
		
		//Skapar upp transButton och lägger till den till leftPanel
		JButton transButton = new JButton("OK");
		transButton.addActionListener(listener);
		leftPanel.add(transButton);
  
		//Skapar och lägger till JList accEventList till rightPanel		
		accEventList = new JList();
		accEventList.setBorder(BorderFactory.createTitledBorder("Events"));
		rightPanel.add(new JScrollPane(accEventList));
		
		JButton printButton = new JButton("Print events");
		printButton.addActionListener(listener);
		rightPanel.add(printButton);
		
		//Skapar upp backButton och lägger till den till rightPanel
		JButton backButton = new JButton("Back");
		backButton.addActionListener(listener);
		rightPanel.add(backButton);

		//Lägger till left och right panels till topPanel
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
			
		//Lägger listan med två kolumner till accountPanel
		accountPanel.add(topPanel);
		
		//Lägger customerField högst upp i accountPanel
		accountPanel.add(customerField, BorderLayout.NORTH);
		
		//Denna panel ska inte visas när programmet startar
		accountPanel.setVisible(false);
	}
	
	
	/**
	 * Hanterar klick som görs i den huvudmeny-panelen (mainMenu)
	 */
	public class ClickMainMenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String actionText = event.getActionCommand();
			
			if(actionText.equals("New customer"))
			{
				//Byter ut så att customerPanel blir osynlig och accountPanel synlig
				startPanel.setVisible(false);
				customerPanel.setVisible(false);
				newCustomerPanel.setVisible(true);
				//Uppdaterar menyvalen
				newStuff.setVisible(false);
				exitMenu.setVisible(false);
				editMenu.setEnabled(false);
				backMenu.setVisible(true);
				loadItem.setVisible(false);
				saveItem.setVisible(false);
			}
			
			if(actionText.equals("Already customer"))
			{
				//Byter ut så att mainMenuPanel blir osynlig och customerPanel synlig
				startPanel.setVisible(false);
				customerPanel.setVisible(true);
				//Uppdaterar menyvalen
				exitMenu.setVisible(false);
				editMenu.setEnabled(true);
				delAccItem.setVisible(false);
				loadItem.setVisible(true);
				saveItem.setVisible(true);
			}
			//Avslutar programmet
			if(actionText.equals("Exit"))
			{
				System.exit(0);
			}
		}
	}

	
	/**
	 * Hanterar klick som görs i den customer-panelen (customer)
	 */
	public class ClickNewCustomerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String buttonText = event.getActionCommand();
			if(buttonText.equals("Add"))
			{
				add();
			}
			if(buttonText.equals("Back"))
			{
				//Byter ut så att newCustomerPanel blir osynlig och startPanel synlig.
				newCustomerPanel.setVisible(false);
				startPanel.setVisible(true);
				newStuff.setVisible(true);
				backMenu.setVisible(false);
				exitMenu.setVisible(true);
			}
		}
	}
	

	/**
	 * Hanterar klick som görs i den första panelen (customer)
	 */
	public class ClickCustomerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String buttonText = event.getActionCommand();
		
			if(buttonText.equals("Enter"))
			{	
				//Om en kund är markerad i listan 
				if(getSelected() >= 0)
				{
					//Ser till så att kundens comboBox med konton blir uppdaterad med det nya kontot.
					updateAccList();
					try
				    {
						//Provar att uppdaterar kontoinfo och användargränssnitt med första kontot i listan,
				    	setAccountInfo(0);
				    }
				    //annars har kunden inget aktivt konto.
				    catch (IndexOutOfBoundsException ex )
				    {
				    	JOptionPane.showMessageDialog(null, "Customer has no active accounts.");
				    	amountField.setEditable(false);
				    	delAccItem.setEnabled(false);
				    }
					//Byter ut så att customerPanel blir osynlig och accountPanel synlig.
					customerPanel.setVisible(false);
					accountPanel.setVisible(true);
					//Uppdaterar menyvalen
					newCust.setEnabled(false);
					loadItem.setVisible(false);
					saveItem.setVisible(false);
					newSavAcc.setEnabled(true);
					newCredAcc.setEnabled(true);
					exitMenu.setVisible(false);
					delAccItem.setVisible(true);
					delAccItem.setEnabled(true);
					delCustItem.setVisible(false);
					nameItem.setVisible(true);
				}
			}
			
			if(buttonText.equals("Load customers"))
			{	
				bl.readObjectFromFile();
				updateCustPanel();	
			}
			
			if(buttonText.equals("Save customers"))
			{
				bl.writeCustToFile();
			}
			
			if(buttonText.equals("Delete customer"))
			{
				if(getSelected() >= 0)
				{	
					String strInfo = "Customer " + bl.getCustomer(currPNo) + " has been deleted. \n" + bl.deleteCustomer(currPNo);
					//Tar bort överflödiga paranteser
					strInfo = strInfo.replaceAll("[\\[\\]]", "");
					JOptionPane.showMessageDialog(null, strInfo);
					//Uppdaterar customList
					updateCustPanel();
				}
			}
			
			if(buttonText.equals("Back"))
			{
				//Byter ut så att customerPanel blir osynlig och startPanel synlig.
				customerPanel.setVisible(false);
				startPanel.setVisible(true);
				//Sätter editMenu till inaktiv
				editMenu.setEnabled(false);
			}
		}
	}
	
	
	/**
	 * Hanterar klick som görs i account-panelen 
	 */
	public class ClickAccountListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String buttonText = event.getActionCommand();
			if(buttonText.equals("New savings account"))
			{
				addAccount("savings");
			}
			
			if(buttonText.equals("New credit account"))
				{
					addAccount("credit");
				}
			if(buttonText.equals("Change name"))
			{
				//Tar fram index för kunden i kundlistan
				int idxC = bl.getCustomerIndex(currPNo);	
				//Inputs för kundens för- och efternamn som sätts till strängar.
				String name = JOptionPane.showInputDialog(null, "First name", bl.customers.get(idxC).getFirstName());
				String surname = JOptionPane.showInputDialog(null, "Surname", bl.customers.get(idxC).getSurName());
				if( name == null)
				{
					if( surname != null)
					{	
						//Förnamn som innan, efternamn ändras
						bl.changeCustomerName(bl.customers.get(idxC).getFirstName(), surname, currPNo);
					}
				}
				else
				{
					if( surname == null)
						//Efternamn som innan, förnamn ändras
						bl.changeCustomerName(name, bl.customers.get(idxC).getSurName(), currPNo);
					else
					{
						//Både för- och efternamn ändras
						bl.changeCustomerName(name, surname, currPNo);
					}
				}
				//customerList uppdateras med nya ändringar
				customerList.setListData(bl.getAllCustomers().toArray());
				//Uppdaterar gällande kontoinfo och användargränssnitt
				setAccountInfo(currPos);
			}
			
			if(buttonText.equals("OK"))
			{
				//Sätt en sträng trans till vald radiobutton i ButtonGroup
				String trans = radGroup.getSelection().getActionCommand();
				try
				{
					//Om inskrivet värde i amountField är negativt visas felmeddelande
					if(Double.parseDouble(amountField.getText()) <= 0 )
						JOptionPane.showMessageDialog(null, "Amount cannot be zero or negative.");	
					
					else if(trans.equals("Deposit"))
					{
						//Om metoden deposit returnerar true utförs transaktionen
						if(bl.deposit(currPNo, Integer.parseInt(currAcc), Double.parseDouble(amountField.getText())))
							{}	
					}
					else if(trans.equals("Withdraw"))
					{
						//Om metoden withdraw returnerar true utförs transaktionen
						if(bl.withdraw(currPNo, Integer.parseInt(currAcc), Double.parseDouble(amountField.getText())))
							{}
						else
						{
							JOptionPane.showMessageDialog(null, "Amount exceeds your limit.");
						}	
					}
					//Uppdaterar gällande kontoinfo och användargränssnitt
					setAccountInfo(currPos);
				}
				//annars fångas felet upp,
				catch(NumberFormatException ex)
				{
					//med felmeddelande om felinmatning
					JOptionPane.showMessageDialog(null, "Incorrect entry.");
				}
			}
			
			if(buttonText.equals("Print events"))
			{
				bl.writeEventToFile(currPNo, (Integer.parseInt(currAcc)));
			}
			
			
			if(buttonText.equals("Close account"))
			{
				int idxC = bl.getCustomerIndex(currPNo);
				//kontrollerar om valt konto inte existerar bland kundens konton,
				if(bl.customers.get(idxC).getAccountIndex(Integer.parseInt(currAcc)) < 0)
					JOptionPane.showMessageDialog(null,"No account was found. Please create a new one.");
				//annars stängs valt konto ned.
				else
				{
					JOptionPane.showMessageDialog(null, "Account was closed.\n" + bl.closeAccount(currPNo, Integer.parseInt(currAcc)));
					//Ser till så att kundens comboBox med konton blir uppdaterad med det nya kontot
					updateAccList();
					//Uppdaterar gällande kontoinfo och användargränssnitt
					setAccountInfo(0);
				}
			}
			
			
			if(buttonText.equals("Back"))
			{
				updateCustPanel();

			}
		}
	}
	
	
	/**
	 * Lägger till en kund
	 */
	private void add()
	{
		//Om namn-, efternamn eller personnummerfältet är tomt,
		if (nameField.getText().equals("") || surNameField.getText().equals("") || pNoField.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Fields cannot be empty.");
		}
		//annars, om true, går via logic för att skapa upp en kund
		else if(bl.createCustomer(nameField.getText().toString(), surNameField.getText().toString(), pNoField.getText().toString()))
		{
			JOptionPane.showMessageDialog(null, "Customer has been added.");
			//Ser till så att vår JList customerList blir uppdaterad med den nya kunden
			customerList.setListData(bl.getAllCustomers().toArray());
			//Tömmer textfälten
			clear();
		}
		//annars om kunden redan finns
		else
		{
			JOptionPane.showMessageDialog(null, "Customer already exists.");	
		}	
	}
	
	/**
	 * Lägger till ett konto
	 */
	private void addAccount(String type)
	{		
		if(type.equals("savings"))
			//Går via logic för att skapa upp ett konto
			bl.createSavingsAccount(currPNo);
			
		else if(type.equals("credit"))
			//Går via logic för att skapa upp ett konto
			bl.createCreditAccount(currPNo);
		
		//Uppdaterar gällande konto
		setAccountInfo(currPos);
		//Ser till så att kundens comboBox med konton blir uppdaterad med det nya kontot
		updateAccList();
		amountField.setEditable(true);
		delAccItem.setEnabled(true);
	}
	
	/**
	 * Uppdaterar kundens combobox med dennes konton.
	 */
	private void updateAccList()
	{
		ArrayList<String> accountList = new ArrayList<>();
		int k = 0;
		for(int j = 0; j < bl.getAccInfo(currPNo).size(); j++)
		{
			try{
			//säkerställa att inte IndexOutOfBounds
			//if(k < bl.getAccInfo(currPNo).size())
				//var 3e element i accountList är kontonumret
				accountList.add(bl.getAccInfo(currPNo).get(j*3));
			}
			
			catch(IndexOutOfBoundsException e)
			{}
			
			//k=k+3;
		}
		//Uppdaterar comboboxen med alla konton  
	    cb.setModel(new DefaultComboBoxModel(accountList.toArray()));
	}
	
	
	/**
	 * Tar fram index för vald kund
	 * @return position i customerlist
	 */
	private int getSelected()
	{
		//Index till kunden som markerats i customerList
		int position = customerList.getSelectedIndex();
		//Endast om man valt en kund
		if(position < 0)
		{
			JOptionPane.showMessageDialog(null, "A customer must be selected.");
		}
		return position;
	}
	
	/**
	 * Tömmer textfält i användargränssnittet
	 */
	private void clear()
	{
		nameField.setText("");
		surNameField.setText("");
		pNoField.setText("");	
		accTypeField.setText("");
		balanceField.setText("");
		customerTextLabel.setText("");
	}
	
	/**
	 * Sätter vald kund till gällande personnummer 
	 */
	private void setcurrentPNo()
	{
		//Index till kund som markerats i customerList
		int position = customerList.getSelectedIndex();
		//Endast om man valt en kund
		if(position > -1)
		{
			currPNo = bl.getAllpNo().get(position);
		}
	}
	
	/**
	 * Uppdaterar gällande kontovariabler och användargränssnittet.
	 */
	private void updateCustPanel()
	{
		customerList.setListData(bl.getAllCustomers().toArray());
		//Byter ut så att accountPanel blir osynlig och customerPanel synlig.
		accountPanel.setVisible(false);
		customerPanel.setVisible(true);
		//Uppdaterar menyvalen
		exitMenu.setVisible(false);
		editMenu.setEnabled(true);
		newCust.setVisible(true);
		newCust.setEnabled(true);
		loadItem.setVisible(true);
		saveItem.setVisible(true);
		delCustItem.setVisible(true);
		delAccItem.setVisible(false);
		newSavAcc.setEnabled(false);
		newCredAcc.setEnabled(false);
		nameItem.setVisible(false);
		
	}
	
	
	/**
	 * Uppdaterar gällande kontovariabler och användargränssnittet.
	 * @param position, vilket index i kontolistan som är valt
	 */
	private void setAccountInfo(int position)
	{
		{
			//testar att kunden har några aktiva konton
			try
			{
				//uppdaterar gällande kontovariabler för valda kontot
				currAcc = bl.getAccInfo(currPNo).get(position*3);
				currAccType = bl.getAccInfo(currPNo).get(position*3+1);
				currBalance = bl.getAccInfo(currPNo).get(position*3+2);
				currPos = position;
				//Uppdaterar användargränssnittet med gällande kontoinfo
				accTypeField.setText(currAccType);
				balanceField.setText(currBalance);
				customerField.setText(bl.getCustomer(currPNo));
				amountField.setEditable(true);
				accEventList.setListData(bl.getTransactions(currPNo,Integer.parseInt(currAcc)).toArray());
			}
			//fångar felet om inget konto funnits
			catch(IndexOutOfBoundsException e)
			{
				//Varningsmeddelande och nollställer användargränssnittet
		    	JOptionPane.showMessageDialog(null, "Customer has no active accounts.");
				accTypeField.setText("");
				balanceField.setText("");
				customerField.setText(bl.getCustomer(currPNo));
				amountField.setText("");
				amountField.setEditable(false);
				delAccItem.setEnabled(false);
				ArrayList<String> emptyList = new ArrayList<>();
				accEventList.setListData(emptyList.toArray());
			}
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO Auto-generated method stub
		
	}
}