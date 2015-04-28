/**
 * This is the implementation of Creo, software used by
 * receptionists, salesmen and directors to organise
 * sales in a company
 *
 * @author  Derek O Keeffe
 * @version 1.0
 * @since   01/03/2014 
 */

package myCreo;

import java.awt.EventQueue; 

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

import java.awt.CardLayout;

import javax.swing.JPanel;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.StyleConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JTable;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.UIManager;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JYearChooser;


public class CreoApp{

	// String arrays to hold information for JComboBoxes
	String [] columnNames = { "Job No","Rep", "Name","Surname", "Address","Landline","Mobile","E-Mail","County","Customer Type","Product Type",
			"Referral", "Source", "Status","Passed To", "Rep", "Reason for closing", "Comments", "Time Accepted"};

	String [] customerTypes = {"Customer Type", "Company", "Residential"};

	String [] positions = {"Receptionist", "Salesman"};

	String [] sources = {"Source", "Email", "Showroom", "Telephone", "Builder", "Call in", "Fax", "Golden pages", "Grant",
			"Planning report", "Post", "Tel & E-Mail", "Tel & Plans", "Van", "Website", "Other"};

	String [] counties = new String[32];

	String [] months = {"Month", "January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	private int yearBeginning = 2000;
	private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

	//ArrayLists to hold product and referral lists
	private ArrayList<String> products = new ArrayList<String>();
	private ArrayList<String> referrals = new ArrayList<String>();
	
	//ArrayLists to hold the salesman selling figures
	private ArrayList<Integer> salesmanSales = new ArrayList<Integer>();
	private ArrayList<Integer> salesmanCommission = new ArrayList<Integer>();

	// Panels and frames
	private JFrame frmCreo;
	private JFrame listOfJobs;
	private JPanel cards;
	private JPanel login;
	private JPanel receptionist;
	private JPanel salesman;
	private JPanel createAccount;
	private JPanel admin;
	private JPanel server;
	private JPanel copyright;
	private JComboBox<String> countyBox;
	
	private String taken;
	
	//Login panel
	private JLabel usernameLogin;
	private JLabel passwordLogin;
	private JTextField usernameEntry;
	private JTextField passwordEntry;
	private JLabel amplioLogo;

	private JTextField landlineEntryRecep;
	private JTextField mobileEntryRecep;
	private JTextField addressEntryRecep;
	private JTextField nameEntryRecep;
	private JButton recepLogout;
	private JComboBox<String> createPositionBox;
	private JButton logoutAccountButton;
	private JButton createAccButton;
	private JTextArea commentsField;
	private JLabel creoLogo;
	private JLabel creoLogoSales;
	private JLabel dateTimeSales;
	private JLabel descriptionLabel;
	private JLabel loggedIn;
	private JLabel lblDatetime;
	private JLabel lblTodaysDate;
	private JComboBox<String> cusTypeBox;
	private JComboBox<String> productBox;
	private JComboBox<String> referralBox;
	private JComboBox<String> sourceBox;
	private JComboBox<String> passedToBox;
	private JScrollPane scrollPane;

	//String ID's for each panel
	final private String LOGIN = "Login Panel";
	final private String RECEPTIONIST = "Receptionist Panel";
	final private String SALESMAN = "Salesman Panel";
	final private String CREATEACC = "Create accoutnt panel";
	final private String ADMIN = "Admin panel";
	final private String SALESEDIT = "Sales edit panel";
	final private String DIRECTOR = "Director panel";
	final private String SERVER = "Server panel";
	final private String CHANGEPASS = "Change password panel";

	//Create user panel
	private JLabel createNameLabel;
	private JLabel createPasswordLabel;
	private JLabel createPositionLabel;
	private JTextField createNameField;
	private JTextField createPasswordField;

	//ArrayLists for users and jobs stored in the database
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Job> jobs = new ArrayList<Job>();
	private JTextField emailEntryRecep;
	private JLabel creoLogoLogin;
	private JButton retrieveRecep;

	//Timestamp
	String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	
	//Files to store settings and information
	File jobsFile = new File("JobsDatabase.dat");
	File userFile = new File("UserDatabase.dat");
	File serverFile = new File ("ServerSettings.dat");
	
	//Sales panel
	private JLabel dateSales;
	private JLabel nameDeptSales;
	private JTable table;
	private JButton logoutSales;
	private JLabel creoAdmin;
	private JLabel dateAdmin;
	private JLabel timeDateAdmin;
	private JLabel nameDeptAdmin;
	private JButton createAccAdmin;
	private JLabel creoCreateAcc;
	private JLabel dateCreateAcc;
	private JLabel timeDateCreateAcc;
	private JLabel nameDeptCreateAcc;
	private JButton salesAdmin;
	private JButton receptionAdmin;
	private JButton logoutAdmin;
	private JButton adminSalesButton;
	private JButton adminCreate;
	private JButton recepAdmin;
	private JButton button;
	private JDateChooser dateChooserSales;

	// Director components
	private JLabel creoLogoDirector;
	private JLabel todaysDateDirector;
	private JLabel dateAndTimeDirector;
	private JLabel nameDeptdirector;
	private JComboBox<String> monthBoxDirector;
	private JComboBox<String> yearBoxDirector;
	private JButton jobsDirector;

	// Server components
	private JLabel serverIP;
	private JLabel serverPassword;
	private JLabel serverUsername;
	private JLabel creoLogoServer;
	private JLabel todaysDateServer;
	private JLabel dateTimeServer;
	private JLabel nameDeptServer;
	private JLabel saveServerSettings;
	private JTextField serverIPEntry;
	private JTextField serverUsernameEntry;
	private JTextField serverPasswordEntry;
	String [] serverSettings = new String[3];

	// Change password components
	private JLabel creoLogoPassword;
	private JLabel passwordLabel;
	private JLabel usernamePassword;
	private JTextField usernameEntryPassword;
	private JTextField newPassEntryPassword;
	private JTextField oldPassEntryPassword;

	private boolean jobExists = false;
	private String duplicateJobId;
	private String jobTakenBy;
	String [] tempArr = new String [6];

	//Setting up the table model
	final DefaultTableModel model = new DefaultTableModel(jobs.size(),columnNames.length);
	private JTextField surnameEntryRecep;

	private JPanel salesEdit;
	private JTextField salesEditName;

	//Connection con;// = DriverManager.getConnection("jdbc:mysql://31.220.17.2:3306/ampliogl_creoDB", "ampliogl_creo", "avs123");
	
	//Salesman panel
	private JTextField salesEditAddress;
	private JTextField salesEditSurname;
	private JTextField salesEditLandline;
	private JTextField salesEditMobile;
	private JTextField salesEditEmail;
	private JComboBox<String> salesEditCounty;
	private JComboBox<String> salesEditCusType;
	private JComboBox<String> salesEditProdType;
	private JComboBox<String> salesEditReferral;
	private JComboBox<String> salesEditSource;
	private JTextArea salesEditComments;
	private JButton salesEditCancel;
	private JLabel creoLogoSalesEdit;
	private JLabel dateSalesEdit;
	private JLabel dateTimeSalesEdit;
	private JLabel nameDeptSalesEdit;
	private String userSaving;
	private JButton salesEditClose;

	//Get desktop
	private Desktop fDesktop = Desktop.getDesktop();

	//Chart panels
	private int chartMonthSelected = 15;
	private String chartYearSelected;
	private JLabel lblGreen;
	private JLabel lblOpen;
	private JLabel lblOrange;
	private JLabel lblPassedToBut;
	private JLabel lblNewLabel;
	private JLabel lblUnassigned;
	private JPanel director;
	private JButton referralsDirector;
	private JButton repsDirector;
	private JButton productsDirector;
	private JButton countyDirector;
	private JButton sourceDirector;
	private JButton customerDirector;
	private JTextField salesEditPassedTo;
	private JLabel amplioPrintRecep;
	private JLabel amplioSaveRecep;
	private JLabel amplioSaveSalesEdit;
	private JTextField jobNumberEntrySales;
	private JButton searchByJobRecep;
	private JTextField searchByJobNumberEntryRecep;
	private JPanel changePassword;
	private JLabel newPassword;
	private JLabel confirmPassChange;
	private JLabel changePassLogin;
	private JButton salesDirector;
	private JButton directorCustomerInfo;
	private JButton btnNewButton;
	private JTextField createPermissionField;

	private JLabel todaysDateLogin;
	private JLabel dateTimeLogin;
	private JButton btnNewButton_1;
	private JLabel lblNewLabel_2;
	private JButton btnBack;
	private JButton btnDirectorsReport;
	private JTextField saleAmount;
	private JTextField commission;
	private JLabel weeklySales;
	private JLabel weeklyCommission;
	private JTextField weekSales;
	private JTextField weekCommission;
	private JTextField jobNumber;
	private JTextField jobNoRecep;
	private JButton btnSales;

	public static void main(String[] args) throws Exception
	{
		
		//Accessing driver from JAR file
		Class.forName("com.mysql.jdbc.Driver").newInstance();


		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					CreoApp window = new CreoApp();
					window.frmCreo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public CreoApp() throws Exception {
		//Check if the server file is present
		if (serverFile.exists())
		{
			//Load stored server settings
			serverSettings = loadServerSettings();
			//Call methods to fill all arrayLists
			fillUsers();
			addProducts();
			addReferrals();
		}
		else{
			JOptionPane.showMessageDialog(null,"Problem with server,"
					+ "\n" + "  please contact admin","Server not found",1);
		}
		lblDatetime = new JLabel();
		dateTimeSales = new JLabel();
		dateTimeSalesEdit = new JLabel();
		timeDateAdmin = new JLabel();
		timeDateCreateAcc = new JLabel();
		final DateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		// Clock timer
		ActionListener timerListener = new ActionListener()   
		{   
			public void actionPerformed(ActionEvent e)   
			{   
				Date date = new Date();   
				String time = timeFormat.format(date);   
				lblDatetime.setText(time);  
				dateTimeSales.setText(time);
				timeDateAdmin.setText(time);
				timeDateCreateAcc.setText(time);
				dateTimeSalesEdit.setText(time);
				dateAndTimeDirector.setText(time);
				dateTimeLogin.setText(time);
				//addJobsToTable();
			}  
		};   
		Timer timer = new Timer(1000, timerListener);   
		// to make sure it doesn't wait one second at the start   
		timer.setInitialDelay(0);   
		timer.start();
		// Update table timer
		ActionListener tableListener = new ActionListener()   
		{   
			public void actionPerformed(ActionEvent e)   
			{  
				//Get the reps name from the login and fill the table as needed
				String rep = loggedIn.getText();
				String[] arr = rep.split(" ", 2);
				String name = arr[0];
				addJobsToTable(name);
			}  
		};   
		Timer tableTimer = new Timer(20000, tableListener);   
		// to make sure it doesn't wait one second at the start   
		tableTimer.setInitialDelay(0);   
		tableTimer.start();
		addCounties();
		if (jobsFile.exists())
		{
			jobs = loadJobData();
		}
		initialize();

	}

	/**
	 * Initialise the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frmCreo = new JFrame();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frmCreo.getContentPane().setBackground(Color.WHITE);
		frmCreo.setTitle("CREO V1.95");
		frmCreo.setBounds(100, 100, 664, dim.height);
		frmCreo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCreo.setExtendedState(Frame.MAXIMIZED_BOTH); 
		frmCreo.setLocation(dim.width/2-frmCreo.getSize().width/2, dim.height/2-frmCreo.getSize().height/2);



		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{24, 660, 13, 0};
		gridBagLayout.rowHeights = new int[]{80, 519, 31, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		frmCreo.getContentPane().setLayout(gridBagLayout);

		/*Border raisedBevel, loweredBevel, compound;
		raisedBevel = BorderFactory.createRaisedBevelBorder();
		loweredBevel = BorderFactory.createLoweredBevelBorder();
		compound = BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);*/


		cards = new JPanel();
		GridBagConstraints gbc_cardPanel = new GridBagConstraints();
		gbc_cardPanel.insets = new Insets(0, 0, 5, 5);
		gbc_cardPanel.fill = GridBagConstraints.BOTH;
		gbc_cardPanel.gridx = 1;
		gbc_cardPanel.gridy = 1;
		frmCreo.getContentPane().add(cards,gbc_cardPanel);
		cards.setLayout(new CardLayout(0, 0));

		//Set up the login screen and all the components//////////////////////////////////////////////////////////////
		login = new JPanel();
		login.setBackground(Color.WHITE);
		cards.add(login, LOGIN);
		login.setLayout(null);

		creoLogoLogin = new JLabel("");
		creoLogoLogin.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoLogoLogin.setHorizontalAlignment(SwingConstants.CENTER);
		creoLogoLogin.setBounds(0, 0, 643, 95);
		login.add(creoLogoLogin);

		usernameLogin = new JLabel("Username :");
		usernameLogin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		usernameLogin.setBounds(229, 134, 84, 14);
		login.add(usernameLogin);

		passwordLogin = new JLabel("Password :");
		passwordLogin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		passwordLogin.setBounds(229, 167, 84, 14);
		login.add(passwordLogin);

		button = new JButton("New button");
		login.add(button);

		usernameEntry = new JTextField();
		usernameEntry.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (checkForCaps()){
					//Warn that caps is on for login
					ToolTipManager.sharedInstance().registerComponent(usernameEntry);
					ToolTipManager.sharedInstance().setInitialDelay(0) ;
					usernameEntry.setToolTipText("Caps is on");
				}
				else{
					usernameEntry.setToolTipText(null);
				}
			}
		});
		usernameEntry.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		usernameEntry.setBounds(323, 131, 121, 20);
		login.add(usernameEntry);
		usernameEntry.setColumns(10);

		passwordEntry = new JPasswordField();
		passwordEntry.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (checkForCaps()){
					//Warn that caps is on for login
					ToolTipManager.sharedInstance().registerComponent(passwordEntry);
					ToolTipManager.sharedInstance().setInitialDelay(0) ;
					passwordEntry.setToolTipText("Caps is on");
				}
				else{
					passwordEntry.setToolTipText(null);
				}
			}
		});
		passwordEntry.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		passwordEntry.setBounds(323, 164, 121, 20);
		login.add(passwordEntry);
		passwordEntry.setColumns(10);

		amplioLogo = new JLabel("");
		amplioLogo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				boolean found = false;
				//Check if both textfields contain an entry
				//warn the user if not
				if (usernameEntry.getText().length() < 1 || passwordEntry.getText().length() < 1){
					JOptionPane.showMessageDialog(null,"Please enter all fields!.","Missing info",2);
				}
				//Admin login details for running first time
				else if (usernameEntry.getText().equalsIgnoreCase("Admin")){
					if (passwordEntry.getText().equalsIgnoreCase("ampliovirtus1")){
						found = true;
						CardLayout cl = (CardLayout)(cards.getLayout());
						cl.show(cards, ADMIN);
						nameDeptAdmin.setText(usernameEntry.getText());
						nameDeptCreateAcc.setText(usernameEntry.getText());
						nameDeptSales.setText(usernameEntry.getText());
						loggedIn.setText(usernameEntry.getText());
						usernameEntry.setText("");
						passwordEntry.setText("");
						if (nameDeptSales.getText().equalsIgnoreCase("Admin")){
							adminSalesButton.setVisible(true);
						}
						if (loggedIn.getText().equalsIgnoreCase("Admin")){
							recepAdmin.setVisible(true);
						}
					}
				}
				//Director login
				else if (usernameEntry.getText().equalsIgnoreCase("Director"))
				{
					if (passwordEntry.getText().equals("director"))
					{
						found = true;
						loggedIn.setText(usernameEntry.getText());
						CardLayout cl = (CardLayout)(cards.getLayout());
						cl.show(cards, DIRECTOR);
					}
					if (loggedIn.getText().equalsIgnoreCase("Director")){
						btnBack.setVisible(true);
						btnDirectorsReport.setVisible(true);
					}
				}
				//If not the admin check all other users
				else if (!usernameEntry.getText().equals("Admin")){
					for (int i = 0; i < users.size(); i ++)
					{
						//If username matches check password
						if (usernameEntry.getText().equalsIgnoreCase(users.get(i).getEmployeeName()))
						{
							//If password matches allow access to specified screen
							if (passwordEntry.getText().equals((users.get(i).getEmployeePassword())))
							{
								found = true;
								loggedIn.setText(usernameEntry.getText()+ " " + users.get(i).getEmployeePosition());
								if (users.get(i).getEmployeePosition().equalsIgnoreCase("Salesman")){
									//If salesman has more privileges allow him/her to choose what to see
									if (!users.get(i).getEmployeePermission().equalsIgnoreCase("Salesman") && 
											!users.get(i).getEmployeePermission().equalsIgnoreCase("none")){
										String[] permissions = new String[2];
										permissions[0] = users.get(i).getEmployeePosition();
										permissions[1] = users.get(i).getEmployeePermission();
										String choice = (String)JOptionPane.showInputDialog (null,"Select portal to view",
												"Permissions",JOptionPane.INFORMATION_MESSAGE, null, permissions, "");
										//Show salesman screen
										if (choice.equalsIgnoreCase("Salesman"))
										{
											CardLayout cl = (CardLayout)(cards.getLayout());
											cl.show(cards, SALESMAN);
											nameDeptSales.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
											nameDeptSalesEdit.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
										}
										//Show Director screen
										if (choice.equalsIgnoreCase("Director"))
										{
											CardLayout cl = (CardLayout)(cards.getLayout());
											cl.show(cards, DIRECTOR);
											nameDeptdirector.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
											nameDeptdirector.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
										}

									}
									//Show salesman screen
									else{
										CardLayout cl = (CardLayout)(cards.getLayout());
										cl.show(cards, SALESMAN);
										nameDeptSales.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
										nameDeptSalesEdit.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
										int sale = weeklySales();
										int comm = weeklyComm();
										weekSales.setText("€ " + sale);
										weekCommission.setText("€ " + comm);
										btnSales.setVisible(true);
									}
								}
								//Check if user is a receptionist and if so
								//check if more privileges are granted
								else if (users.get(i).getEmployeePosition().equalsIgnoreCase("Receptionist")){
									if (!users.get(i).getEmployeePermission().equalsIgnoreCase("Receptionist") &&
											!users.get(i).getEmployeePermission().equalsIgnoreCase("none")){
										String[] permissions = new String[2];
										permissions[0] = users.get(i).getEmployeePosition();
										permissions[1] = users.get(i).getEmployeePermission();
										String choice = (String)JOptionPane.showInputDialog (null,"Select portal to view",
												"Permissions",JOptionPane.INFORMATION_MESSAGE, null, permissions, "");
										//Show Receptionist screen
										if (choice.equalsIgnoreCase("Receptionist"))
										{
											CardLayout cl = (CardLayout)(cards.getLayout());
											cl.show(cards, RECEPTIONIST);
											nameDeptSales.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
											nameDeptSalesEdit.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
										}
										//Show Director screen
										if (choice.equalsIgnoreCase("Director"))
										{
											CardLayout cl = (CardLayout)(cards.getLayout());
											cl.show(cards, DIRECTOR);
											nameDeptdirector.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
											nameDeptdirector.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
										}

									}
									//Show receptionist screen
									else{
										CardLayout cl = (CardLayout)(cards.getLayout());
										cl.show(cards, RECEPTIONIST);
										nameDeptSales.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
										nameDeptSalesEdit.setText(usernameEntry.getText() + " " + users.get(i).getEmployeePosition());
									}
								}
								break;
							}
						}
					}
				}
				if (found == false){
					JOptionPane.showMessageDialog(null,"Incorrect information .","Missing info",2);
				}
				usernameEntry.setText("");
				passwordEntry.setText("");
			}
		});
		amplioLogo.setBounds(294, 193, 75, 67);
		login.add(amplioLogo);
		amplioLogo.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newAvs.JPG")));

		changePassLogin = new JLabel("Change password");
		changePassLogin.setForeground(Color.BLUE);
		changePassLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, CHANGEPASS);
			}
		});
		changePassLogin.setHorizontalAlignment(SwingConstants.CENTER);
		changePassLogin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		changePassLogin.setBounds(257, 271, 148, 36);
		login.add(changePassLogin);

		todaysDateLogin = new JLabel("Todays Date");
		todaysDateLogin.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		todaysDateLogin.setBounds(197, 106, 134, 14);
		login.add(todaysDateLogin);

		dateTimeLogin = new JLabel("");
		dateTimeLogin.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateTimeLogin.setBounds(317, 106, 168, 14);
		login.add(dateTimeLogin);

		//Set up the receptionist screen and all the components//////////////////////////////////////////////////////
		receptionist = new JPanel();
		receptionist.setBackground(Color.WHITE);
		cards.add(receptionist, RECEPTIONIST);
		//receptionist.setLayout(new BorderLayout());
		receptionist.setLayout(null);
		//panel.setLayout(new BorderLayout(0, 0));

		countyBox = new JComboBox<String>();
		countyBox.addItem("County");
		//Loop through counties and add to combobox
		for (int i = 0; i < counties.length; i++){
			countyBox.addItem("" + counties[i]);
		}
		countyBox.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		countyBox.setForeground(Color.BLACK);
		countyBox.setBackground(Color.WHITE);
		countyBox.addItem("Waterford");
		countyBox.setBounds(23, 216, 168, 20);
		receptionist.add(countyBox);

		//For each textfield on the reception panel the color will be checked
		//if the mouse is clicked inside the field. If it is gray (not filled)
		//it will be cleared and the color changed to white
		landlineEntryRecep = new JTextField();
		landlineEntryRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		landlineEntryRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (landlineEntryRecep.getBackground() == (Color.LIGHT_GRAY)){
					landlineEntryRecep.setText("");
					landlineEntryRecep.setBackground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (landlineEntryRecep.getText().length() < 1){
					landlineEntryRecep.setBackground(Color.LIGHT_GRAY);
					landlineEntryRecep.setText("Landline");
				}
			}
		});
		landlineEntryRecep.setText("Landline");
		landlineEntryRecep.setBackground(Color.LIGHT_GRAY);
		landlineEntryRecep.setBounds(213, 186, 168, 20);
		receptionist.add(landlineEntryRecep);
		landlineEntryRecep.setColumns(10);

		mobileEntryRecep = new JTextField();
		mobileEntryRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		mobileEntryRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (mobileEntryRecep.getBackground() == (Color.LIGHT_GRAY)){
					mobileEntryRecep.setText("");
					mobileEntryRecep.setBackground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (mobileEntryRecep.getText().length() < 1){
					mobileEntryRecep.setBackground(Color.LIGHT_GRAY);
					mobileEntryRecep.setText("Mobile");
				}
			}
		});
		mobileEntryRecep.setText("Mobile");
		mobileEntryRecep.setBackground(Color.LIGHT_GRAY);
		mobileEntryRecep.setBounds(23, 186, 168, 20);
		receptionist.add(mobileEntryRecep);
		mobileEntryRecep.setColumns(10);

		addressEntryRecep = new JTextField();
		addressEntryRecep.setHorizontalAlignment(SwingConstants.LEFT);
		addressEntryRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		addressEntryRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (addressEntryRecep.getBackground() == (Color.LIGHT_GRAY)){
					addressEntryRecep.setText("");
					addressEntryRecep.setBackground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (addressEntryRecep.getText().length() < 1){
					addressEntryRecep.setBackground(Color.LIGHT_GRAY);
					addressEntryRecep.setText("Address");
				}
			}
		});
		addressEntryRecep.setText("Address");
		addressEntryRecep.setBackground(Color.LIGHT_GRAY);
		addressEntryRecep.setBounds(391, 141, 229, 20);
		receptionist.add(addressEntryRecep);
		addressEntryRecep.setColumns(10);

		nameEntryRecep = new JTextField();
		nameEntryRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		nameEntryRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (nameEntryRecep.getBackground() == (Color.LIGHT_GRAY)){
					nameEntryRecep.setText("");
					nameEntryRecep.setBackground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				if (nameEntryRecep.getText().length() < 1){
					nameEntryRecep.setBackground(Color.LIGHT_GRAY);
					nameEntryRecep.setText("First name");
				}
			}
		});
		nameEntryRecep.setBackground(Color.LIGHT_GRAY);
		nameEntryRecep.setText("First name ");
		nameEntryRecep.setBounds(23, 141, 168, 20);
		receptionist.add(nameEntryRecep);
		nameEntryRecep.setColumns(10);

		recepLogout = new JButton("Logout");
		recepLogout.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		recepLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, LOGIN);
				clearReceptionistFields();
			}
		});
		recepLogout.setBounds(23, 497, 89, 23);
		receptionist.add(recepLogout);

		commentsField = new JTextArea();
		//Add scrollpane to reception panel
		JScrollPane scrollRecep = new JScrollPane(commentsField);
		scrollRecep.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollRecep.setBounds(209, 236, 416, 160);
		//Disable delete key in comments section
		disableKeys(commentsField.getInputMap());
		commentsField.setFont(new Font("Trajan Pro", Font.PLAIN, 13));
		//Create border
		Border border = BorderFactory.createLineBorder(Color.GRAY);
		commentsField.setBorder(BorderFactory.createCompoundBorder(border, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		commentsField.setWrapStyleWord(true);
		commentsField.setLineWrap(true);
		commentsField.setBounds(213, 236, 407, 162);
		receptionist.add(scrollRecep);

		descriptionLabel = new JLabel("Comments");
		descriptionLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descriptionLabel.setBounds(213, 217, 407, 14);
		receptionist.add(descriptionLabel);

		emailEntryRecep = new JTextField();
		emailEntryRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		emailEntryRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (emailEntryRecep.getBackground() == (Color.LIGHT_GRAY)){
					emailEntryRecep.setText("");
					emailEntryRecep.setBackground(Color.WHITE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (emailEntryRecep.getText().length() < 1){
					emailEntryRecep.setBackground(Color.LIGHT_GRAY);
					emailEntryRecep.setText("E-Mail");
				}
			}
		});
		emailEntryRecep.setText("E-Mail");
		emailEntryRecep.setBackground(Color.LIGHT_GRAY);
		emailEntryRecep.setBounds(391, 186, 229, 20);
		receptionist.add(emailEntryRecep);
		emailEntryRecep.setColumns(10);

		creoLogo = new JLabel("");
		creoLogo.setHorizontalAlignment(SwingConstants.CENTER);
		creoLogo.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoLogo.setBounds(0, 0, 643, 89);
		receptionist.add(creoLogo);

		//Loop through the appropriate arrays and fill the comboboxes with the correct info
		cusTypeBox = new JComboBox<String>();
		cusTypeBox.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		cusTypeBox.setBackground(Color.WHITE);
		cusTypeBox.setForeground(Color.BLACK);
		for (int i = 0; i < customerTypes.length; i ++){
			cusTypeBox.addItem(customerTypes[i]);
		}
		cusTypeBox.setBounds(23, 247, 168, 20);
		receptionist.add(cusTypeBox);

		productBox = new JComboBox<String>();
		productBox.addItem("Product");
		productBox.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		for (int i = 0; i < products.size(); i++){
			productBox.addItem(products.get(i).toString());
		}
		productBox.setBounds(23, 279, 168, 20);
		receptionist.add(productBox);

		referralBox = new JComboBox<String>();
		referralBox.addItem("Origin");
		referralBox.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		for(int i = 0; i < referrals.size(); i++){
			referralBox.addItem(referrals.get(i).toString());
		}
		referralBox.setBounds(23, 310, 168, 20);
		receptionist.add(referralBox);

		sourceBox = new JComboBox<String>();
		sourceBox.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		for (int i = 0; i < sources.length; i ++){
			sourceBox.addItem(sources[i]);
		}
		sourceBox.setBounds(23, 341, 168, 20);
		receptionist.add(sourceBox);

		passedToBox = new JComboBox<String>();
		passedToBox.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		passedToBox.addItem("Passed-To");
		for (int i = 0; i < users.size(); i ++){
			if (users.get(i).getEmployeePosition().equalsIgnoreCase("salesman")){
					passedToBox.addItem(users.get(i).getEmployeeName());
				}
		}
		passedToBox.addItem("Not Assigned");
		passedToBox.setBounds(23, 372, 168, 20);
		receptionist.add(passedToBox);

		retrieveRecep = new JButton("Find job");
		retrieveRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		retrieveRecep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchJobs();
			}
		});
		retrieveRecep.setBounds(23, 463, 181, 23);
		receptionist.add(retrieveRecep);

		loggedIn = new JLabel("");
		loggedIn.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		loggedIn.setHorizontalAlignment(SwingConstants.CENTER);
		loggedIn.setBounds(186, 110, 288, 14);
		receptionist.add(loggedIn);

		lblDatetime = new JLabel("Date/Time");
		lblDatetime.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		lblDatetime.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatetime.setText(timeStamp);
		lblDatetime.setBounds(306, 90, 168, 14);
		receptionist.add(lblDatetime);

		lblTodaysDate = new JLabel("Todays Date");
		lblTodaysDate.setHorizontalAlignment(SwingConstants.CENTER);
		lblTodaysDate.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		lblTodaysDate.setBounds(186, 90, 134, 14);
		receptionist.add(lblTodaysDate);

		recepAdmin = new JButton("Admin");
		recepAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, ADMIN);
			}
		});
		recepAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		recepAdmin.setBounds(364, 471, 110, 23);
		receptionist.add(recepAdmin);

		surnameEntryRecep = new JTextField();
		surnameEntryRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (surnameEntryRecep.getBackground() == (Color.LIGHT_GRAY)){
					surnameEntryRecep.setText("");
				}
				surnameEntryRecep.setBackground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				if (surnameEntryRecep.getText().length() < 1){
					surnameEntryRecep.setBackground(Color.LIGHT_GRAY);
					surnameEntryRecep.setText("Surname");
				}
			}
		});
		surnameEntryRecep.setBackground(Color.LIGHT_GRAY);
		surnameEntryRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		surnameEntryRecep.setText("Surname");
		surnameEntryRecep.setBounds(213, 141, 168, 20);
		receptionist.add(surnameEntryRecep);
		surnameEntryRecep.setColumns(10);

		JButton recepCancel = new JButton("Cancel");
		recepCancel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		recepCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearReceptionistFields();
			}
		});
		recepCancel.setBounds(115, 497, 89, 23);
		receptionist.add(recepCancel);

		amplioPrintRecep = new JLabel("");
		amplioPrintRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				print();
			}
		});
		amplioPrintRecep.setIcon(new ImageIcon(CreoApp.class.getResource("/images/rsz_avlogo-print.png")));
		amplioPrintRecep.setBounds(479, 443, 75, 77);
		receptionist.add(amplioPrintRecep);

		amplioSaveRecep = new JLabel("");
		amplioSaveRecep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				userSaving = "recep";
				String name = ((nameEntryRecep.getText()));
				String surname = (replaceQuote((surnameEntryRecep.getText())));
				String address = ((addressEntryRecep.getText()));
				String landline = ((landlineEntryRecep.getText()));
				String mobile = ((mobileEntryRecep.getText()));
				String email = ((emailEntryRecep.getText()));
				if (nameEntryRecep.getText().equalsIgnoreCase("name"))
				{
					name = "(Left Blank)";
				}
				if (surnameEntryRecep.getText().equalsIgnoreCase("surname"))
				{
					surname = "(Left Blank)";
				}
				if (addressEntryRecep.getText().equalsIgnoreCase("address"))
				{
					address = "(Left Blank)";
				}
				if (landlineEntryRecep.getText().equalsIgnoreCase("landline"))
				{
					landline = "(Left Blank)";
				}
				if (mobileEntryRecep.getText().equalsIgnoreCase("mobile"))
				{
					mobile = "(Left Blank)";
				}
				if (emailEntryRecep.getText().equalsIgnoreCase("E-mail"))
				{
					email = "(Left Blank)";
				}
				//Create a new "Job" from the info provided and send to the saveJob method
				//to be saved to the database
				Job job = new Job(name,surname,address, landline,
						mobile, email, countyBox.getSelectedItem().toString(),
						cusTypeBox.getSelectedItem().toString(), productBox.getSelectedItem().toString(),
						referralBox.getSelectedItem().toString(),sourceBox.getSelectedItem().toString(),
						"Not taken",passedToBox.getSelectedItem().toString(),
						commentsField.getText()+ " " + getCurrentTimeStamp() + " :" + loggedIn.getText(), timeStamp, "Open","Not closed","None", "0","0",
						loggedIn.getText());
				if (passedToBox.getSelectedItem().toString().equals("Not Assigned") 
						|| passedToBox.getSelectedItem().equals("Passed-To")){
					job.setJobPassedTo("Unassigned");
				}
				saveJob(job);
			}
		});
		amplioSaveRecep.setIcon(new ImageIcon(CreoApp.class.getResource("/images/rsz_avlogo-save.png")));
		amplioSaveRecep.setBounds(279, 443, 75, 77);
		receptionist.add(amplioSaveRecep);

		//SDearch for a job by the jobNumber
		searchByJobRecep = new JButton("Search by job number");
		searchByJobRecep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String jobNumber = searchByJobNumberEntryRecep.getText();
				Connection con = null;
				PreparedStatement selectStatement = null;
				ResultSet result = null;
				try {
					//Connect to the database and retrieve the job
					//fill the fields in the reception panel with the info
					con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
							":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
					selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `Job Number` = ('"+jobNumber+"')");
					result = selectStatement.executeQuery();
					while (result.next())
					{
						nameEntryRecep.setText(result.getString(2));
						nameEntryRecep.setBackground(Color.WHITE);
						surnameEntryRecep.setText(result.getString(3));
						surnameEntryRecep.setBackground(Color.WHITE);
						addressEntryRecep.setText(result.getString(4));
						addressEntryRecep.setBackground(Color.WHITE);
						landlineEntryRecep.setText((result.getString(5)));
						landlineEntryRecep.setBackground(Color.WHITE);
						mobileEntryRecep.setText((result.getString(6)));
						mobileEntryRecep.setBackground(Color.WHITE);
						emailEntryRecep.setText(result.getString(7));
						emailEntryRecep.setBackground(Color.WHITE);

						//Set information in the comboboxes by using a counter to decide the position of the value needed
						int index = 1;
						for (int j = 0; j < counties.length; j++){
							if  (!result.getString(8).equals(counties[j])){
								index ++;
							}
							else{
								countyBox.setSelectedIndex(index);
								index = 1;
								break;
							}
						}
						int type = 1;
						for (int k = 0; k < customerTypes.length; k ++){
							if (!result.getString(9).equals(customerTypes[k])){
								type ++;
							}
							else{
								cusTypeBox.setSelectedIndex(type);
								type = 1;
								break;
							}
						}
						int product = 0;
						for (int x = 0; x < products.size(); x ++){
							if (!result.getString(10).equals(products.get(x))){
								product ++;
							}
							else{
								productBox.setSelectedIndex(product);
								product = 0;
								break;
							}
						}
						int referral = 0;
						for (int y = 0; y < referrals.size(); y ++){
							if (!result.getString(11).equals(referrals.get(y))){
								referral ++;
							}
							else{
								referralBox.setSelectedIndex(referral);
								referral = 0;
								break;
							}
						}
						int source = 0;
						for (int z = 0; z < sources.length; z ++){
							if (!result.getString(12).equals(sources[z])){
								source ++;
							}
							else{
								sourceBox.setSelectedIndex(source);
								source = 0;
								break;
							}
						}
						int passed = 1;
						for (int h = 0; h < users.size(); h ++){
							if (!result.getString(14).equals(users.get(h).getEmployeeName())){
								passed ++;
							}
							else{
								passedToBox.setSelectedIndex(passed);
								passed = 1;
								break;
							}
						}
						commentsField.setText(result.getString(17));
					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null,"Cannot search by job number.","Missing info",2);
					ex.printStackTrace();
				}
				finally{
					try {
						result.close();
					} catch (SQLException e1) {}
					try{
						selectStatement.close();
					}catch (SQLException e1) {}
					try{
						con.close();
					}catch (SQLException e1) {}
				}
			}
		});
		searchByJobRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		searchByJobRecep.setBounds(259, 411, 200, 23);
		receptionist.add(searchByJobRecep);

		searchByJobNumberEntryRecep = new JTextField();
		searchByJobNumberEntryRecep.setBounds(483, 410, 89, 23);
		receptionist.add(searchByJobNumberEntryRecep);
		searchByJobNumberEntryRecep.setColumns(10);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, DIRECTOR);
			}
		});
		btnBack.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnBack.setBounds(71, 531, 89, 23);
		receptionist.add(btnBack);
		
		JLabel lblFirstName = new JLabel("First name");
		lblFirstName.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirstName.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblFirstName.setBounds(23, 129, 168, 10);
		receptionist.add(lblFirstName);
		
		JLabel lblSurname = new JLabel("Surname");
		lblSurname.setHorizontalAlignment(SwingConstants.CENTER);
		lblSurname.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblSurname.setBounds(213, 129, 168, 10);
		receptionist.add(lblSurname);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddress.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblAddress.setBounds(391, 129, 229, 10);
		receptionist.add(lblAddress);
		
		JLabel lblMobile = new JLabel("Mobile");
		lblMobile.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblMobile.setHorizontalAlignment(SwingConstants.CENTER);
		lblMobile.setBounds(23, 174, 168, 10);
		receptionist.add(lblMobile);
		
		JLabel lblLandline = new JLabel("Landline");
		lblLandline.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandline.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblLandline.setBounds(213, 174, 168, 10);
		receptionist.add(lblLandline);
		
		JLabel lblEmail = new JLabel("E-Mail");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblEmail.setBounds(391, 174, 229, 10);
		receptionist.add(lblEmail);
		
		jobNoRecep = new JTextField();
		jobNoRecep.setHorizontalAlignment(SwingConstants.CENTER);
		jobNoRecep.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		jobNoRecep.setBounds(23, 428, 181, 22);
		receptionist.add(jobNoRecep);
		jobNoRecep.setColumns(10);
		
		JLabel jobNoRecepLabel = new JLabel("Job Number");
		jobNoRecepLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jobNoRecepLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		jobNoRecepLabel.setBounds(23, 405, 181, 16);
		receptionist.add(jobNoRecepLabel);
		
		btnSales = new JButton("Sales");
		btnSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, SALESMAN);
			}
		});
		btnSales.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnSales.setBounds(364, 507, 110, 25);
		receptionist.add(btnSales);
		btnSales.setVisible(false);
		recepAdmin.setVisible(false);
		btnBack.setVisible(false);;

		//Set up salesman screen and all components////////////////////////////////////////////////////////////////
		salesman = new JPanel();
		salesman.setBackground(Color.WHITE);
		cards.add(salesman, SALESMAN);
		salesman.setLayout(null);

		creoLogoSales = new JLabel("");
		creoLogoSales.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoLogoSales.setHorizontalAlignment(SwingConstants.CENTER);
		creoLogoSales.setBounds(10, 0, 633, 95);
		salesman.add(creoLogoSales);

		dateTimeSales = new JLabel("Date and Time");
		dateTimeSales.setHorizontalAlignment(SwingConstants.CENTER);
		dateTimeSales.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateTimeSales.setBounds(322, 106, 168, 14);
		salesman.add(dateTimeSales);

		dateSales = new JLabel("Todays Date");
		dateSales.setHorizontalAlignment(SwingConstants.CENTER);
		dateSales.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateSales.setBounds(202, 106, 134, 14);
		salesman.add(dateSales);

		nameDeptSales = new JLabel("");
		nameDeptSales.setHorizontalAlignment(SwingConstants.CENTER);
		nameDeptSales.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		nameDeptSales.setBounds(188, 119, 288, 14);
		salesman.add(nameDeptSales);

		scrollPane = new JScrollPane();
		scrollPane.setToolTipText("Double click job to show details");
		scrollPane.setBounds(10, 168, 633, 234);
		salesman.add(scrollPane);

		table = new JTable(model)
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int data, int columns)
			{
				return false;
			}
			//Change the color of the rows in the table 
			//depending on what state the job is (unnassigned, not taken, etc)
			public Component prepareRenderer(TableCellRenderer r, int data, int columns)
			{
				Component c = super.prepareRenderer(r, data, columns);
				//If live
				if (table.getValueAt(data,14).toString().equalsIgnoreCase("unassigned"))
				{
					c.setBackground(new Color(255, 0, 0));
				}
				else if (table.getValueAt(data,13).toString().equalsIgnoreCase("not taken"))
				{
					c.setBackground(new Color(255, 140, 0));
				}
				else
				{
					c.setBackground(new Color(50, 205, 50));
				}
				if (isCellSelected(data, columns))
					c.setBackground(SystemColor.inactiveCaption);
				return c;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("Job No");
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		for (int k = 1; k <columnNames.length; k++)
		{
			table.getTableHeader().getColumnModel().getColumn(k).setHeaderValue(columnNames[k]);
			table.getColumnModel().getColumn(k).setPreferredWidth(94);
		}
		for (int j = 8; j < columnNames.length; j ++)
		{
			table.getColumnModel().getColumn(j).setPreferredWidth(0);
			table.getColumnModel().getColumn(j).setMinWidth(0);
			table.getColumnModel().getColumn(j).setMaxWidth(0);
		}
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				//If row double clicked get information and fill the necessary fields from the information retrieved
				if (e.getClickCount()== 2){
					JTable target = (JTable)e.getSource();
					int rowSelected = target.getSelectedRow();
					if (table.getSelectedRow() > -1){
						CardLayout cl = (CardLayout) (cards.getLayout());
						cl.show(cards, SALESEDIT);
						jobNumber.setText(table.getValueAt(rowSelected, 0).toString());
						salesEditName.setText(table.getValueAt(rowSelected, 2).toString());
						salesEditSurname.setText(table.getValueAt(rowSelected, 3).toString());
						salesEditAddress.setText(table.getValueAt(rowSelected, 4).toString());
						salesEditLandline.setText(table.getValueAt(rowSelected, 5).toString());
						salesEditMobile.setText(table.getValueAt(rowSelected, 6).toString());
						salesEditEmail.setText(table.getValueAt(rowSelected, 7).toString());
						salesEditPassedTo.setText(table.getValueAt(rowSelected, 14).toString());
						String county = table.getValueAt(rowSelected, 8).toString();
						int index = 1;
						for (int j = 0; j < counties.length; j++){
							if  (!county.equalsIgnoreCase(counties[j])){
								index ++;
							}
							else{
								salesEditCounty.setSelectedIndex(index);
								index = 1;
								break;
							}
						}
						String cusType = table.getValueAt(rowSelected, 9).toString();
						int type = 0;
						for (int k = 0; k < customerTypes.length; k ++){
							if (!cusType.equalsIgnoreCase(customerTypes[k])){
								type ++;
							}
							else{
								salesEditCusType.setSelectedIndex(type);
								type = 0;
								break;
							}
						}
						String prodType = table.getValueAt(rowSelected, 10).toString();
						int product = 0;
						for (int x = 0; x < products.size(); x ++){
							if (!prodType.equalsIgnoreCase(products.get(x))){
								product ++;
							}
							else{
								salesEditProdType.setSelectedIndex(product);
								product = 0;
								break;
							}
						}
						String referral = table.getValueAt(rowSelected, 11).toString();
						int ref = 0;
						for (int y = 0; y < referrals.size(); y ++){
							if (!referral.equalsIgnoreCase(referrals.get(y))){
								ref ++;
							}
							else{
								salesEditReferral.setSelectedIndex(ref);
								ref = 0;
								break;
							}
						}
						String source = table.getValueAt(rowSelected, 12).toString();
						int src = 0;
						for (int z = 0; z < sources.length; z ++){
							if (!source.equalsIgnoreCase(sources[z])){
								src ++;
							}
							else{
								salesEditSource.setSelectedIndex(src);
								src = 0;
								break;
							}
						}
						salesEditComments.setText(table.getValueAt(rowSelected, 17).toString());
					}
					else{
						JOptionPane.showMessageDialog(null, "Please select a job to edit");
					}
				}
			}
		});
		table.getColumnModel().getColumn(columnNames.length - 2).setPreferredWidth(500);
		scrollPane.setViewportView(table);

		logoutSales = new JButton("Logout");
		logoutSales.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		logoutSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				btnSales.setVisible(false);
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, LOGIN);
			}
		});
		logoutSales.setBounds(410, 456, 89, 23);
		salesman.add(logoutSales);

		adminSalesButton = new JButton("Admin");
		adminSalesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, ADMIN);
			}
		});
		adminSalesButton.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		adminSalesButton.setBounds(74, 481, 89, 23);
		salesman.add(adminSalesButton);

		JButton salesEmailButton = new JButton("E-Mail ");
		salesEmailButton.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEmailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Get info to be used in email 
				int selectedRow = table.getSelectedRow();
				String emailAddress = (table.getValueAt(selectedRow, 6).toString());
				String order = (table.getValueAt(selectedRow, 9).toString().replace(" ", ""));
				//Pass to addEmail method
				addEmail(emailAddress, order);

			}
		});
		salesEmailButton.setBounds(188, 456, 89, 23);
		salesman.add(salesEmailButton);

		lblGreen = new JLabel("");
		lblGreen.setOpaque(true);
		lblGreen.setForeground(new Color(34, 139, 34));
		lblGreen.setBackground(new Color(50, 205, 50));
		lblGreen.setBounds(94, 143, 38, 14);
		salesman.add(lblGreen);

		lblOpen = new JLabel("Open");
		lblOpen.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblOpen.setBounds(137, 143, 46, 14);
		salesman.add(lblOpen);

		lblOrange = new JLabel("");
		lblOrange.setBackground(new Color(255, 140, 0));
		lblOrange.setOpaque(true);
		lblOrange.setBounds(359, 143, 38, 14);
		salesman.add(lblOrange);

		lblPassedToBut = new JLabel("Passed to/not active");
		lblPassedToBut.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblPassedToBut.setBounds(407, 143, 175, 14);
		salesman.add(lblPassedToBut);

		lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(new Color(255, 0, 0));
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBounds(212, 143, 38, 14);
		salesman.add(lblNewLabel);

		lblUnassigned = new JLabel("Unassigned");
		lblUnassigned.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblUnassigned.setBounds(260, 143, 89, 14);
		salesman.add(lblUnassigned);

		JLabel amplioPrintSales = new JLabel("");
		amplioPrintSales.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					//Print the table
					table.print();
				} catch (PrinterException e) {
					JOptionPane.showMessageDialog(null, "Printer Unavailable");
					e.printStackTrace();
				}
			}
		});
		amplioPrintSales.setIcon(new ImageIcon(CreoApp.class.getResource("/images/rsz_avlogo-print.png")));
		amplioPrintSales.setBounds(306, 435, 75, 69);
		salesman.add(amplioPrintSales);

		JButton searchByJobSales = new JButton("Search by job number");
		searchByJobSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String jobNumber = jobNumberEntrySales.getText();
				Connection aCon = null;
				PreparedStatement selectStatement = null;
				ResultSet result = null;
				try {
					aCon = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
							":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
					selectStatement = aCon.prepareStatement("SELECT * FROM `jobs` WHERE `Job Number` = ('"+jobNumber+"')");
					result = selectStatement.executeQuery();
					while (result.next()){
						salesEditName.setText((result.getString(2)));
						salesEditSurname.setText((result.getString(3)));
						salesEditAddress.setText((result.getString(4)));
						salesEditLandline.setText(((result.getString(5))));
						salesEditMobile.setText(((result.getString(6))));
						salesEditEmail.setText ((result.getString(7)));
						salesEditPassedTo.setText(result.getString(14));
						String county = result.getString(8);
						int index = 1;
						for (int j = 0; j < counties.length; j++){
							if  (!county.equalsIgnoreCase(counties[j])){
								index ++;
							}
							else{
								salesEditCounty.setSelectedIndex(index);
								index = 1;
								break;
							}
						}
						String cusType = result.getString(9);
						int type = 0;
						for (int k = 0; k < customerTypes.length; k ++){
							if (!cusType.equalsIgnoreCase(customerTypes[k])){
								type ++;
							}
							else{
								salesEditCusType.setSelectedIndex(type);
								type = 0;
								break;
							}
						}
						String prodType = result.getString(10);
						int product = 0;
						for (int x = 0; x < products.size(); x ++){
							if (!prodType.equalsIgnoreCase(products.get(x))){
								product ++;
							}
							else{
								salesEditProdType.setSelectedIndex(product);
								product = 0;
								break;
							}
						}
						String referral = result.getString(11);
						int ref = 0;
						for (int y = 0; y < referrals.size(); y ++){
							if (!referral.equalsIgnoreCase(referrals.get(y))){
								ref ++;
							}
							else{
								salesEditReferral.setSelectedIndex(ref);
								ref = 0;
								break;
							}
						}
						String source = result.getString(12);
						int src = 0;
						for (int z = 0; z < sources.length; z ++){
							if (!source.equalsIgnoreCase(sources[z])){
								src ++;
							}
							else{
								salesEditSource.setSelectedIndex(src);
								src = 0;
								break;
							}
						}
						salesEditComments.setText(result.getString(17));
					}
				} 
				catch (SQLException e) {
					JOptionPane.showMessageDialog(null,"Problem with job number search.","Missing info",2);
					e.printStackTrace();
				}
				finally{
					try {
						result.close();
					} catch (SQLException e) {}
					try{
						selectStatement.close();
					} catch(SQLException e){}
					try{
						aCon.close();
					} catch (SQLException e){}
				}
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, SALESEDIT);
			}
		});
		searchByJobSales.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		searchByJobSales.setBounds(301, 407, 203, 23);
		salesman.add(searchByJobSales);

		jobNumberEntrySales = new JTextField();
		jobNumberEntrySales.setBounds(523, 407, 89, 23);
		salesman.add(jobNumberEntrySales);
		jobNumberEntrySales.setColumns(10);

		btnNewButton_1 = new JButton("Search jobs");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, RECEPTIONIST);
				String name = loggedIn.getText();
				String[] parts = name.split(" ");
				String part2 = parts[1];
				if (part2.equalsIgnoreCase("Salesman"))
				{
					amplioSaveRecep.setVisible(false);
				}
			}
		});
		btnNewButton_1.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnNewButton_1.setBounds(74, 407, 146, 23);
		salesman.add(btnNewButton_1);
		
		weeklySales = new JLabel("Weekly sales");
		weeklySales.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		weeklySales.setBounds(322, 548, 161, 16);
		salesman.add(weeklySales);
		
		weeklyCommission = new JLabel("Weekly Commission");
		weeklyCommission.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		weeklyCommission.setBounds(322, 577, 154, 16);
		salesman.add(weeklyCommission);
		
		weekSales = new JTextField();
		weekSales.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		weekSales.setEditable(false);
		weekSales.setText("");
		weekSales.setBounds(496, 544, 116, 22);
		salesman.add(weekSales);
		weekSales.setColumns(10);
		
		weekCommission = new JTextField();
		weekCommission.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		weekCommission.setEditable(false);
		weekCommission.setText("");
		weekCommission.setBounds(496, 573, 116, 22);
		salesman.add(weekCommission);
		weekCommission.setColumns(10);
		
		btnDirectorsReport = new JButton("Directors report");
		btnDirectorsReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, DIRECTOR);
			}
		});
		btnDirectorsReport.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnDirectorsReport.setBounds(86, 548, 175, 25);
		salesman.add(btnDirectorsReport);
		
		JButton btnAddRecord = new JButton("Add Record");
		btnAddRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, RECEPTIONIST);
			}
		});
		btnAddRecord.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnAddRecord.setBounds(496, 506, 116, 25);
		salesman.add(btnAddRecord);
		adminSalesButton.setVisible(false);
		btnDirectorsReport.setVisible(false);


		//Set up create account screen and all components//////////////////////////////////////////////////////////
		createAccount = new JPanel();
		createAccount.setBackground(Color.WHITE);
		cards.add(createAccount, CREATEACC);
		createAccount.setLayout(null);

		createNameLabel = new JLabel("Name :");
		createNameLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createNameLabel.setBounds(147, 184, 104, 14);
		createAccount.add(createNameLabel);

		createPasswordLabel = new JLabel("Password :");
		createPasswordLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createPasswordLabel.setBounds(147, 224, 104, 14);
		createAccount.add(createPasswordLabel);

		createPositionLabel = new JLabel("Position :");
		createPositionLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createPositionLabel.setBounds(147, 265, 104, 14);
		createAccount.add(createPositionLabel);

		createNameField = new JTextField();
		createNameField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createNameField.setBackground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (createNameField.getText().length() < 1){
					createNameField.setBackground(Color.LIGHT_GRAY);
				}
			}
		});
		createNameField.setBackground(Color.LIGHT_GRAY);
		createNameField.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createNameField.setBounds(254, 181, 248, 20);
		createAccount.add(createNameField);
		createNameField.setColumns(10);

		createPasswordField = new JTextField();
		createPasswordField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createPasswordField.setBackground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (createPasswordField.getText().length() < 1){
					createPasswordField.setBackground(Color.LIGHT_GRAY);
				}
			}
		});
		createPasswordField.setBackground(Color.LIGHT_GRAY);
		createPasswordField.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createPasswordField.setBounds(254, 221, 248, 20);
		createAccount.add(createPasswordField);
		createPasswordField.setColumns(10);

		createPositionBox = new JComboBox<String>();
		createPositionBox.setBackground(Color.WHITE);
		for (int i = 0; i < positions.length; i ++){
			createPositionBox.addItem(positions[i]);
		}
		createPositionBox.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createPositionBox.setBounds(254, 262, 248, 20);
		createAccount.add(createPositionBox);

		createAccButton = new JButton("Create");
		createAccButton.addActionListener(new ActionListener() {
			//Create a new user
			public void actionPerformed(ActionEvent e) {
				//Setup connection and Statement
				Connection aCon = null;
				Statement insert = null ;
				if (createNameField.getText().length() > 0 && createPasswordField.getText().length() > 0
						&& createPermissionField.getText().length() > 0)
				{
					try {
						//Assign the driver and Statement
						aCon = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
								":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
						insert = aCon.createStatement();
						String name = createNameField.getText();
						String password = ((createPasswordField.getText()));
						String position = createPositionBox.getSelectedItem().toString();
						String permission = createPermissionField.getText();
						//Send new user to database
						insert.executeUpdate("INSERT INTO `ampliogl_creoDB`.`userinfo` (`Username`, `Password`, `Position`, `Permission`) "
								+ "VALUES (('"+name+"'), ('"+password+"'), ('"+position+"'), ('"+permission+"'));");

					} 
					catch (SQLException ex) {
						JOptionPane.showMessageDialog(null,"Database unavailable.","Missing info",2);
						ex.printStackTrace();
					}
					finally{
						try{
							//Close the Statement
							insert.close();
						} catch(SQLException e1){}
						try{
							//Close the connection
							aCon.close();
						} catch (SQLException e1){}
					}
					fillUsers();
					
					createNameField.setText("");
					createNameField.setBackground(Color.LIGHT_GRAY);
					createPasswordField.setText("");
					createPasswordField.setBackground(Color.LIGHT_GRAY);
					createPositionBox.setSelectedIndex(0);
					createPermissionField.setText("");
					createPermissionField.setBackground(Color.LIGHT_GRAY);
				}
				else{
					JOptionPane.showMessageDialog(null,"Please fill in all fields.","Missing info",2);
				}
				try {
					insert.close();
					aCon.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		createAccButton.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createAccButton.setBounds(382, 327, 120, 23);
		createAccount.add(createAccButton);

		logoutAccountButton = new JButton("Logout");
		logoutAccountButton.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		logoutAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, LOGIN);
				recepAdmin.setVisible(false);
				btnBack.setVisible(false);
				btnDirectorsReport.setVisible(false);
				adminSalesButton.setVisible(false);
			}
		});
		logoutAccountButton.setBounds(254, 327, 120, 23);
		createAccount.add(logoutAccountButton);

		creoCreateAcc = new JLabel("");
		creoCreateAcc.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoCreateAcc.setHorizontalAlignment(SwingConstants.CENTER);
		creoCreateAcc.setBounds(0, 0, 643, 95);
		createAccount.add(creoCreateAcc);

		dateCreateAcc = new JLabel("Todays Date");
		dateCreateAcc.setHorizontalAlignment(SwingConstants.CENTER);
		dateCreateAcc.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateCreateAcc.setBounds(187, 106, 134, 14);
		createAccount.add(dateCreateAcc);

		timeDateCreateAcc = new JLabel("25/02/2014 12:37:33");
		timeDateCreateAcc.setHorizontalAlignment(SwingConstants.CENTER);
		timeDateCreateAcc.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		timeDateCreateAcc.setBounds(307, 106, 168, 14);
		createAccount.add(timeDateCreateAcc);

		nameDeptCreateAcc = new JLabel("");
		nameDeptCreateAcc.setHorizontalAlignment(SwingConstants.CENTER);
		nameDeptCreateAcc.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		nameDeptCreateAcc.setBounds(147, 126, 288, 14);
		createAccount.add(nameDeptCreateAcc);

		adminCreate = new JButton("Admin");
		adminCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, ADMIN);
			}
		});
		adminCreate.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		adminCreate.setBounds(254, 361, 248, 23);
		createAccount.add(adminCreate);

		JLabel createPermissionLabel = new JLabel("Permission :");
		createPermissionLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createPermissionLabel.setBounds(147, 302, 104, 14);
		createAccount.add(createPermissionLabel);

		createPermissionField = new JTextField();
		createPermissionField.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createPermissionField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createPermissionField.setBackground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (createPermissionField.getText().length() < 1){
					createPermissionField.setBackground(Color.LIGHT_GRAY);
				}
			}
		});
		createPermissionField.setBackground(Color.LIGHT_GRAY);
		createPermissionField.setBounds(254, 298, 248, 20);
		createAccount.add(createPermissionField);
		createPermissionField.setColumns(10);
		adminCreate.setVisible(true);

		//Set up the admin screen and all components
		admin = new JPanel();
		admin.setBackground(Color.WHITE);
		cards.add(admin, ADMIN);
		admin.setLayout(null);

		creoAdmin = new JLabel("");
		creoAdmin.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		creoAdmin.setBounds(0, 0, 643, 95);
		admin.add(creoAdmin);

		dateAdmin = new JLabel("Todays Date");
		dateAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		dateAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateAdmin.setBounds(190, 106, 134, 14);
		admin.add(dateAdmin);

		timeDateAdmin = new JLabel("25/02/2014 12:30:20");
		timeDateAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		timeDateAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		timeDateAdmin.setBounds(310, 106, 168, 14);
		admin.add(timeDateAdmin);

		nameDeptAdmin = new JLabel("");
		nameDeptAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		nameDeptAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		nameDeptAdmin.setBounds(147, 126, 288, 14);
		admin.add(nameDeptAdmin);

		createAccAdmin = new JButton("Create Account");
		createAccAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, CREATEACC);
			}
		});
		createAccAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		createAccAdmin.setBounds(117, 152, 168, 23);
		admin.add(createAccAdmin);

		salesAdmin = new JButton("Sales");
		salesAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, SALESMAN);
			}
		});
		salesAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesAdmin.setBounds(368, 151, 168, 23);
		admin.add(salesAdmin);

		receptionAdmin = new JButton("Reception");
		receptionAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, RECEPTIONIST);
			}
		});
		receptionAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		receptionAdmin.setBounds(117, 214, 168, 23);
		admin.add(receptionAdmin);

		logoutAdmin = new JButton("Logout");
		logoutAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, LOGIN);
				recepAdmin.setVisible(false);
				btnBack.setVisible(false);
				btnDirectorsReport.setVisible(false);
				adminSalesButton.setVisible(false);
			}
		});
		logoutAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		logoutAdmin.setBounds(283, 323, 105, 23);
		admin.add(logoutAdmin);

		JButton serverAdmin = new JButton("Manage server settings");
		serverAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards,  SERVER);
			}
		});
		serverAdmin.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		serverAdmin.setBounds(212, 265, 231, 23);
		admin.add(serverAdmin);

		JButton btnUserInfo = new JButton("User info");
		btnUserInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Connection, statement and resultSet
				Connection con = null;
				PreparedStatement selectStatement = null;
				ResultSet result = null;
				//Temporary list to hold user info
				List<String> templist = new ArrayList<String>();
				try {
					con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
							":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
					selectStatement = con.prepareStatement("SELECT * FROM `userinfo`");
					result = selectStatement.executeQuery();
					while(result.next())
					{
						templist.add("Username: " + result.getString(1)+ ", " + "Password: " + (result.getString(2))+ "    ");
					}
					Object[] options = templist.toArray();
					JList<Object> list = new JList<Object>(options);
					//Add user info to panel
					JPanel panel = new JPanel();
					panel.add(list);
					JOptionPane.showMessageDialog(null,panel,"User Information",JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,null,"Cannot load user info",JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}
				finally{
					try{
						result.close();
					}catch(SQLException e){}
					try{
						selectStatement.close();
					}catch(SQLException e){}
					try{
						con.close();
					}catch(SQLException e){}
				}

			}
		});
		btnUserInfo.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnUserInfo.setBounds(368, 213, 168, 23);
		admin.add(btnUserInfo);

		//Set up the salesEdit screen and all components
		salesEdit = new JPanel();
		salesEdit.setBackground(Color.WHITE);
		cards.add(salesEdit, SALESEDIT);
		salesEdit.setLayout(null);

		salesEditName = new JTextField();
		salesEditName.setBackground(Color.WHITE);
		salesEditName.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditName.setHorizontalAlignment(SwingConstants.LEFT);
		salesEditName.setText("Name");
		salesEditName.setBounds(24, 151, 145, 20);
		salesEdit.add(salesEditName);
		salesEditName.setColumns(10);

		salesEditAddress = new JTextField();
		salesEditAddress.setBackground(Color.WHITE);
		salesEditAddress.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditAddress.setHorizontalAlignment(SwingConstants.LEFT);
		salesEditAddress.setText("Address");
		salesEditAddress.setBounds(382, 151, 243, 20);
		salesEdit.add(salesEditAddress);
		salesEditAddress.setColumns(10);

		salesEditSurname = new JTextField();
		salesEditSurname.setBackground(Color.WHITE);
		salesEditSurname.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditSurname.setHorizontalAlignment(SwingConstants.LEFT);
		salesEditSurname.setText("Surname");
		salesEditSurname.setBounds(188, 151, 168, 20);
		salesEdit.add(salesEditSurname);
		salesEditSurname.setColumns(10);

		salesEditLandline = new JTextField();
		salesEditLandline.setBackground(Color.WHITE);
		salesEditLandline.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditLandline.setHorizontalAlignment(SwingConstants.LEFT);
		salesEditLandline.setText("Landline");
		salesEditLandline.setBounds(24, 193, 145, 20);
		salesEdit.add(salesEditLandline);
		salesEditLandline.setColumns(10);

		salesEditMobile = new JTextField();
		salesEditMobile.setBackground(Color.WHITE);
		salesEditMobile.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditMobile.setHorizontalAlignment(SwingConstants.LEFT);
		salesEditMobile.setText("Mobile");
		salesEditMobile.setBounds(188, 193, 168, 20);
		salesEdit.add(salesEditMobile);
		salesEditMobile.setColumns(10);

		salesEditEmail = new JTextField();
		salesEditEmail.setBackground(Color.WHITE);
		salesEditEmail.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditEmail.setHorizontalAlignment(SwingConstants.LEFT);
		salesEditEmail.setText("E-Mail");
		salesEditEmail.setBounds(382, 193, 243, 20);
		salesEdit.add(salesEditEmail);
		salesEditEmail.setColumns(10);

		salesEditCounty = new JComboBox<String>();
		salesEditCounty.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		salesEditCounty.addItem("County");
		for (int i = 0; i < counties.length; i ++){
			salesEditCounty.addItem(counties[i]);
		}
		salesEditCounty.setBounds(24, 236, 145, 20);
		salesEdit.add(salesEditCounty);

		salesEditCusType = new JComboBox<String>();
		for (int i = 0; i < customerTypes.length; i ++){
			salesEditCusType.addItem(customerTypes[i]);
		}
		salesEditCusType.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		salesEditCusType.setBounds(24, 271, 145, 20);
		salesEdit.add(salesEditCusType);

		salesEditProdType = new JComboBox<String>();
		for (int i = 0; i < products.size(); i ++){
			salesEditProdType.addItem(products.get(i));
		}
		salesEditProdType.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		salesEditProdType.setBounds(24, 306, 145, 20);
		salesEdit.add(salesEditProdType);

		salesEditReferral = new JComboBox<String>();
		for (int i = 0; i < referrals.size(); i ++){
			salesEditReferral.addItem(referrals.get(i));
		}
		salesEditReferral.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		salesEditReferral.setBounds(24, 341, 145, 20);
		salesEdit.add(salesEditReferral);

		salesEditSource = new JComboBox<String>();
		for (int i = 0; i < sources.length; i ++){
			salesEditSource.addItem(sources[i]);
		}
		salesEditSource.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		salesEditSource.setBounds(24, 376, 145, 20);
		salesEdit.add(salesEditSource);

		salesEditComments = new JTextArea();
		disableKeys(salesEditComments.getInputMap());
		JScrollPane scroll = new JScrollPane(salesEditComments);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(188, 236, 437, 160);
		salesEditComments.setLineWrap(true);
		salesEditComments.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		Border border2 = BorderFactory.createLineBorder(Color.GRAY);
		salesEditComments.setBorder(BorderFactory.createCompoundBorder(border2, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		salesEdit.add(scroll);

		JLabel lblComments = new JLabel("Comments");
		lblComments.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblComments.setHorizontalAlignment(SwingConstants.CENTER);
		lblComments.setBounds(188, 224, 437, 14);
		salesEdit.add(lblComments);

		salesEditCancel = new JButton("Cancel");
		salesEditCancel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, SALESMAN);
			}
		});
		salesEditCancel.setBounds(346, 433, 134, 23);
		salesEdit.add(salesEditCancel);

		creoLogoSalesEdit = new JLabel("");
		creoLogoSalesEdit.setHorizontalAlignment(SwingConstants.CENTER);
		creoLogoSalesEdit.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoLogoSalesEdit.setBounds(0, 0, 643, 95);
		salesEdit.add(creoLogoSalesEdit);

		dateSalesEdit = new JLabel("Todays Date");
		dateSalesEdit.setHorizontalAlignment(SwingConstants.CENTER);
		dateSalesEdit.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateSalesEdit.setBounds(188, 101, 134, 14);
		salesEdit.add(dateSalesEdit);

		dateTimeSalesEdit = new JLabel("New label");
		dateTimeSalesEdit.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateTimeSalesEdit.setHorizontalAlignment(SwingConstants.CENTER);
		dateTimeSalesEdit.setBounds(308, 101, 168, 14);
		salesEdit.add(dateTimeSalesEdit);

		nameDeptSalesEdit = new JLabel("New label");
		nameDeptSalesEdit.setHorizontalAlignment(SwingConstants.CENTER);
		nameDeptSalesEdit.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		nameDeptSalesEdit.setBounds(188, 114, 288, 14);
		salesEdit.add(nameDeptSalesEdit);

		salesEditClose = new JButton("Close Job");
		salesEditClose.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditClose.addActionListener(new ActionListener() {
			//Close a job
			public void actionPerformed(ActionEvent arg0) {
				//Connection, statement, resultSet
				Connection con = null;
				PreparedStatement selectStatement = null;
				ResultSet result = null;
				try {
					String name = ((salesEditName.getText()));
					String surname = (replaceQuote((salesEditSurname.getText())));
					String address = ((salesEditAddress.getText()));
					String landline = ((salesEditLandline.getText()));
					String mobile = ((salesEditMobile.getText()));
					String email = ((salesEditEmail.getText()));
					//Assign the connection, statement and resultSet
					con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
							":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
					selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `name` = ('"+name+"') AND `surname` = "
							+ "('"+surname+"') AND `address` = ('"+address+"')" + "AND `landline` = ('"+landline+"') AND `mobile` = "
							+ "('"+mobile+"') AND `e-mail` = ('"+email+"') LIMIT 0 , 30");
					result = selectStatement.executeQuery();
					if(result.next())
					{
						//Array with reasons for closing a job
						String[] choices = { "Gone to unkown competition", "Bonmahon Joinery", "Call nxt wk", "Will call back", "Awaiting Grant",
								"Awaiting Credit Union", "Sold", "Harris windows", "Awaiting Insurance", "Windows Direct", "Quotes",
								"Too expensive", "Munster Joinery", "Senator", "Jimmy Maher", "Acorn","D&B", "Pat Kerley", "Moloney"};
						//Present user with reasons for closing a job
						String choiceList = (String) JOptionPane.showInputDialog(null, "Choose now...",
								"Chose reason for closing", JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
						String reason = choiceList;
						int choice = JOptionPane.showConfirmDialog(null, "Are you sure you wish to close this job?" + "\n" 
								+ "This action cannot be undone!", "Edit Job", JOptionPane.OK_CANCEL_OPTION);
						//Update job to closed and save reason
						if (choice == JOptionPane.OK_OPTION)
						{
							String jobNumber = result.getString(1);
							Statement update = con.createStatement();
							update.executeUpdate("UPDATE `jobs` SET `Status` = 'closed', `Reason for closing` = ('"+reason+"') WHERE `Job Number` = ('"+jobNumber+"')");
							CardLayout cl = (CardLayout) (cards.getLayout());
							cl.show(cards, SALESMAN);
						}
						else if (choice == JOptionPane.CANCEL_OPTION)
						{
							CardLayout cl = (CardLayout) (cards.getLayout());
							cl.show(cards, SALESMAN);
						}
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Job doesn't exist");
					e.printStackTrace();
				}
				finally{
					try{
						result.close();
					}catch(SQLException e1){}
					try{
						selectStatement.close();
					}catch(SQLException e1){}
					try{
						con.close();
					}catch(SQLException e1){}
				}
			}
		});
		salesEditClose.setBounds(448, 497, 145, 23);
		salesEdit.add(salesEditClose);

		JButton takeJob = new JButton("Take Job");
		takeJob.addActionListener(new ActionListener() {
			//Allow a salesman to take a job
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null, "You are about to take this job" + "\n" +"Would you lke to continue?", "Take Job", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION)
				{
					String str = nameDeptSalesEdit.getText();
					String[] arr = str.split(" ", 2);
					jobTakenBy = arr[0];
					userSaving = "sales";
					String name = ((salesEditName.getText()));
					String surname = (replaceQuote((salesEditSurname.getText())));
					String address = ((salesEditAddress.getText()));
					String landline = ((salesEditLandline.getText()));
					String mobile = ((salesEditMobile.getText()));
					String email = ((salesEditEmail.getText()));
					String recep = null;
					try {
						recep = getRecepName(name, surname, address, landline, mobile, email);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Job job = new Job(name,surname,address, landline, mobile, email,
							salesEditCounty.getSelectedItem().toString(),
							salesEditCusType.getSelectedItem().toString(), salesEditProdType.getSelectedItem().toString(),
							salesEditReferral.getSelectedItem().toString(),salesEditSource.getSelectedItem().toString(),
							"Live",jobTakenBy,//salesEditPassedTo.getSelectedItem().toString(),
							salesEditComments.getText()+ " " + timeStamp + " :" + nameDeptSalesEdit.getText(), timeStamp, "Open", "Open",nameDeptSalesEdit.getText().substring(0,
							nameDeptSalesEdit.getText().indexOf(' ')),saleAmount.getText(),commission.getText(), recep);
					saveJob(job);
				}
			}
		});
		takeJob.setIcon(null);
		takeJob.setOpaque(false);
		takeJob.setBackground(UIManager.getColor("Button.background"));
		takeJob.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		takeJob.setBounds(219, 497, 145, 23);
		salesEdit.add(takeJob);

		salesEditPassedTo = new JTextField();
		salesEditPassedTo.setText("Passed To");
		salesEditPassedTo.setHorizontalAlignment(SwingConstants.LEFT);
		salesEditPassedTo.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesEditPassedTo.setColumns(10);
		salesEditPassedTo.setBackground(Color.LIGHT_GRAY);
		salesEditPassedTo.setBounds(24, 515, 145, 20);
		salesEdit.add(salesEditPassedTo);

		JLabel lblNewLabel_1 = new JLabel("Passed To");
		lblNewLabel_1.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(50, 494, 84, 20);
		salesEdit.add(lblNewLabel_1);

		amplioSaveSalesEdit = new JLabel("");
		amplioSaveSalesEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (saleAmount.getText().length() < 1 || commission.getText().length() < 1){
					JOptionPane.showMessageDialog(null, "Please enter the sale amount " + "\n" + "and the commission recieved");
				}
				else{
					int result = JOptionPane.showConfirmDialog(null, "Are you sure you wish to edit this job?", "Edit Job", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION)
					{
						String quotedBy = null;
						userSaving = "sales";
						String name = ((salesEditName.getText()));
						String surname = (replaceQuote((salesEditSurname.getText())));
						String address = ((salesEditAddress.getText()));
						String landline = ((salesEditLandline.getText()));
						String mobile = ((salesEditMobile.getText()));
						String email = ((salesEditEmail.getText()));
						String recep = null;
						try {
							recep = getRecepName(name, surname, address, landline, mobile, email);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (dateChooserSales.getDate() != null){
							String dateString = new SimpleDateFormat("dd-MM-yyyy").format(dateChooserSales.getDate());
							quotedBy = ("Quoted by, "+ (nameDeptSalesEdit.getText().substring(0, nameDeptSalesEdit.getText().indexOf(' '))) + " on " + dateString);
						}
						else{
							quotedBy = "";
						}
						Job job = new Job(name,surname,address, landline, mobile, email,
								salesEditCounty.getSelectedItem().toString(),
								salesEditCusType.getSelectedItem().toString(), salesEditProdType.getSelectedItem().toString(),
								salesEditReferral.getSelectedItem().toString(),salesEditSource.getSelectedItem().toString(),
								"Live",jobTakenBy, salesEditComments.getText()+ " " + timeStamp + " :" + nameDeptSalesEdit.getText().substring(0, nameDeptSalesEdit.getText().indexOf(' '))
								+ "\n" + quotedBy, timeStamp,
								"Open", "Open",nameDeptSalesEdit.getText().substring(0, nameDeptSalesEdit.getText().indexOf(' ')),saleAmount.getText(),
								commission.getText(), recep);
						saveJob(job);
						CardLayout cl = (CardLayout) (cards.getLayout());
						cl.show(cards, SALESMAN);
					}
				}
			}
		});
		amplioSaveSalesEdit.setIcon(new ImageIcon(CreoApp.class.getResource("/images/rsz_avlogo-save.png")));
		amplioSaveSalesEdit.setBounds(242, 407, 75, 75);
		salesEdit.add(amplioSaveSalesEdit);

		lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				print();
			}
		});
		lblNewLabel_2.setIcon(new ImageIcon(CreoApp.class.getResource("/images/rsz_avlogo-print.png")));
		lblNewLabel_2.setBounds(504, 407, 75, 75);
		salesEdit.add(lblNewLabel_2);

		dateChooserSales = new JDateChooser();
		dateChooserSales.setDateFormatString("dd-MM-yyyy");
		dateChooserSales.setBounds(24, 468, 145, 20);
		salesEdit.add(dateChooserSales);

		JLabel lblQuotedOn = new JLabel("Quoted on");
		lblQuotedOn.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblQuotedOn.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuotedOn.setBounds(24, 449, 145, 14);
		salesEdit.add(lblQuotedOn);
		
		JLabel label = new JLabel("First name");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		label.setBounds(23, 139, 145, 10);
		salesEdit.add(label);
		
		JLabel label_1 = new JLabel("Surname");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		label_1.setBounds(190, 139, 166, 10);
		salesEdit.add(label_1);
		
		JLabel label_2 = new JLabel("Address");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		label_2.setBounds(382, 139, 243, 10);
		salesEdit.add(label_2);
		
		JLabel label_3 = new JLabel("E-Mail");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		label_3.setBounds(380, 184, 245, 10);
		salesEdit.add(label_3);
		
		JLabel lblMobile_1 = new JLabel("Mobile");
		lblMobile_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMobile_1.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblMobile_1.setBounds(190, 184, 168, 10);
		salesEdit.add(lblMobile_1);
		
		JLabel lblLandline_1 = new JLabel("Landline");
		lblLandline_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandline_1.setFont(new Font("Trajan Pro", Font.PLAIN, 9));
		lblLandline_1.setBounds(24, 184, 144, 10);
		salesEdit.add(lblLandline_1);
		
		JLabel lblEnterSaleAmount = new JLabel("Enter sale amount");
		lblEnterSaleAmount.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblEnterSaleAmount.setBounds(275, 547, 145, 16);
		salesEdit.add(lblEnterSaleAmount);
		
		saleAmount = new JTextField();
		saleAmount.setBounds(432, 543, 116, 22);
		salesEdit.add(saleAmount);
		saleAmount.setColumns(10);
		
		JLabel lblEnterCommission = new JLabel("Enter commission");
		lblEnterCommission.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		lblEnterCommission.setBounds(275, 585, 145, 16);
		salesEdit.add(lblEnterCommission);
		
		commission = new JTextField();
		commission.setBounds(432, 581, 116, 22);
		salesEdit.add(commission);
		commission.setColumns(10);
		
		jobNumber = new JTextField();
		jobNumber.setHorizontalAlignment(SwingConstants.CENTER);
		jobNumber.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		jobNumber.setBounds(24, 424, 145, 22);
		salesEdit.add(jobNumber);
		jobNumber.setColumns(10);
		
		JLabel jobNoLabel = new JLabel("Job Number");
		jobNoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jobNoLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		jobNoLabel.setBounds(24, 402, 145, 16);
		salesEdit.add(jobNoLabel);

		director = new JPanel();
		director.setBackground(Color.WHITE);
		cards.add(director, DIRECTOR);
		director.setLayout(null);

		creoLogoDirector = new JLabel("");
		creoLogoDirector.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoLogoDirector.setHorizontalAlignment(SwingConstants.CENTER);
		creoLogoDirector.setBounds(0, 0, 643, 95);
		director.add(creoLogoDirector);

		todaysDateDirector = new JLabel("Todays Date");
		todaysDateDirector.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		todaysDateDirector.setHorizontalAlignment(SwingConstants.CENTER);
		todaysDateDirector.setBounds(187, 106, 134, 14);
		director.add(todaysDateDirector);

		dateAndTimeDirector = new JLabel("");
		dateAndTimeDirector.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		dateAndTimeDirector.setHorizontalAlignment(SwingConstants.CENTER);
		dateAndTimeDirector.setBounds(307, 106, 168, 14);
		director.add(dateAndTimeDirector);

		nameDeptdirector = new JLabel("Director");
		nameDeptdirector.setHorizontalAlignment(SwingConstants.CENTER);
		nameDeptdirector.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		nameDeptdirector.setBounds(187, 126, 288, 14);
		director.add(nameDeptdirector);

		monthBoxDirector = new JComboBox<String>();
		for (int i = 0; i < months.length; i ++){
			monthBoxDirector.addItem(months[i]);
		}
		monthBoxDirector.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		monthBoxDirector.setBounds(305, 151, 134, 20);
		director.add(monthBoxDirector);

		yearBoxDirector = new JComboBox<String>();
		int years = currentYear;
		while (years >= yearBeginning){
			yearBoxDirector.addItem(Integer.toString(years));;
			years --;
		}
		yearBoxDirector.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
		yearBoxDirector.setBounds(449, 151, 75, 20);
		director.add(yearBoxDirector);

		jobsDirector = new JButton("Jobs");
		jobsDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0){
					try {
						//Directors chart for jobs
						JobsChart jobChart = new JobsChart(chartMonthSelected, chartYearSelected);
						jobChart.draw();
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null,"Unable to display charts","Missing info",2);
						e1.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		jobsDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		jobsDirector.setBounds(100, 189, 89, 23);
		director.add(jobsDirector);

		referralsDirector = new JButton("Referrals");
		referralsDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						//Directors chart for referrals
						ReferralsChart refChart = new ReferralsChart(chartMonthSelected, chartYearSelected);
						refChart.draw();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null,"Cannot display referrals chart","Missing info",2);
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		referralsDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		referralsDirector.setBounds(206, 188, 110, 23);
		director.add(referralsDirector);

		repsDirector = new JButton("Reps");
		repsDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						String name = null;
						ArrayList<String> reps = new ArrayList<String>();
						for (int i = 0; i < users.size(); i ++){
							//add all reps to array
							if (users.get(i).getEmployeePosition().equalsIgnoreCase("salesman"))
							{
								reps.add(users.get(i).getEmployeeName());
							}
						}
						//Set selection values for user to pick
						Object[] selectionValues = reps.toArray();
						String initialSelection = "Rep name";
						Object selection = JOptionPane.showInputDialog(null, "Select the rep",
								"Rep", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
						if (selection != null){
							name = selection.toString();
							//Directors chart for reps performance
							RepsChart chart = new RepsChart(chartMonthSelected, chartYearSelected, name);
							chart.draw();
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null,"Cannot display reps chart","Missing info",2);
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		repsDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		repsDirector.setBounds(100, 235, 89, 23);
		director.add(repsDirector);

		productsDirector = new JButton("Products");
		productsDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						String county = null;
						//Set selection values for user to pick
						Object[] selectionValues = new Object[32];
						for (int i = 0; i < counties.length; i ++){
							selectionValues[i] = counties[i];
						}
						String initialSelection = "County";
						Object selection = JOptionPane.showInputDialog(null, "Select the county",
								"County", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
						if (selection != null){
							county = selection.toString();
							ProductsChart pChart = new ProductsChart(chartMonthSelected, chartYearSelected, county);
							pChart.draw();
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null,"Unable to display charts","Missing info",2);
						e1.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		productsDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		productsDirector.setBounds(206, 235, 110, 23);
		director.add(productsDirector);

		countyDirector = new JButton("County");
		countyDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						String county = null;
						//Set selection values for user to pick
						Object[] selectionValues = new Object[32]; 
						for (int i = 0; i < counties.length; i ++){
							selectionValues[i] = counties[i];
						}
						String initialSelection = "County";
						Object selection = JOptionPane.showInputDialog(null, "Select the county",
								"County", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
						if (selection != null){
							//Show county chart
							county = selection.toString();
							CountyChart chart = new CountyChart(chartMonthSelected, chartYearSelected, county);
							chart.draw();
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null,"Unable to display charts","Missing info",2);
						e1.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		countyDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		countyDirector.setBounds(331, 188, 89, 23);
		director.add(countyDirector);

		sourceDirector = new JButton("Source");
		sourceDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						String county = null;
						Object[] selectionValues = new Object[32]; 
						for (int i = 0; i < counties.length; i ++){
							selectionValues[i] = counties[i];
						}
						String initialSelection = "County";
						Object selection = JOptionPane.showInputDialog(null, "Select the county",
								"County", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
						if (selection != null){
							county = selection.toString();
							SourceChart chart = new SourceChart(chartMonthSelected, chartYearSelected, county);
							chart.draw();
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null,"Unable to display charts","Missing info",2);
						e1.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		sourceDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		sourceDirector.setBounds(331, 235, 89, 23);
		director.add(sourceDirector);

		customerDirector = new JButton("Customer");
		customerDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						String county = null;
						Object[] selectionValues = new Object[32]; 
						for (int i = 0; i < counties.length; i ++){
							selectionValues[i] = counties[i];
						}
						String initialSelection = "County";
						Object selection = JOptionPane.showInputDialog(null, "Select the county",
								"County", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
						if (selection != null){
							county = selection.toString();
							CustomerChart chart = new CustomerChart(chartMonthSelected, chartYearSelected, county);
							chart.draw();
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null,"Unable to display charts","Missing info",2);
						e1.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		customerDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		customerDirector.setBounds(437, 188, 110, 23);
		director.add(customerDirector);

		JLabel selectMonthYearDirector = new JLabel("Select month/year");
		selectMonthYearDirector.setHorizontalAlignment(SwingConstants.CENTER);
		selectMonthYearDirector.setFont(new Font("Trajan Pro", Font.BOLD, 13));
		selectMonthYearDirector.setBounds(82, 153, 237, 14);
		director.add(selectMonthYearDirector);

		JButton logoutDirector = new JButton("Logout");
		logoutDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, LOGIN);
				btnBack.setVisible(false);
				btnDirectorsReport.setVisible(false);
			}
		});
		logoutDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		logoutDirector.setBounds(281, 411, 110, 23);
		director.add(logoutDirector);

		salesDirector = new JButton("Sales");
		salesDirector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, SALESMAN);
			}
		});
		salesDirector.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesDirector.setBounds(437, 235, 110, 23);
		director.add(salesDirector);

		directorCustomerInfo = new JButton("Customer Info");
		directorCustomerInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, RECEPTIONIST);
			}
		});
		directorCustomerInfo.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		directorCustomerInfo.setBounds(248, 282, 153, 23);
		director.add(directorCustomerInfo);

		btnNewButton = new JButton("Create user");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"Please contact Amplio Virtus." + "\n"+ "Tel: 051-591976"
						+ "\n"+ "Mob: 083-3500509"+ "\n"+ "E-Mail: info@ampliovirtus.com","Missing info",2);
			}
		});
		btnNewButton.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnNewButton.setBounds(413, 282, 134, 23);
		director.add(btnNewButton);
		
		JButton btnTest = new JButton("C.I.L Report");
		btnTest.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						String name = null;
						Object[] selectionValues = new Object[users.size()]; 
						for (int i = 0; i < users.size(); i ++){
							selectionValues[i] = users.get(i).getEmployeeName();
						}
						String initialSelection = "Employee";
						Object selection = JOptionPane.showInputDialog(null, "Select the employee",
								"Employee", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
						if (selection != null){
							name = selection.toString();
							CusInfoLoss cil = new CusInfoLoss(chartMonthSelected, chartYearSelected, name);
							cil.draw();
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null,"Unable to display charts","Missing info",2);
						e1.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		btnTest.setBounds(100, 282, 138, 23);
		director.add(btnTest);
		
		JButton btnFailedSales = new JButton("Failed Sales");
		btnFailedSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				try {
					NotSoldChart chart = new NotSoldChart(chartMonthSelected, chartYearSelected);
					chart.draw();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFailedSales.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnFailedSales.setBounds(100, 329, 138, 25);
		director.add(btnFailedSales);
		
		JButton salesComm = new JButton("Sales/Comm");
		salesComm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						String name = null;
						ArrayList<String> reps = new ArrayList<String>();
						for (int i = 0; i < users.size(); i ++)
						{
							if (users.get(i).getEmployeePosition().equalsIgnoreCase("salesman"))
							{
								reps.add(users.get(i).getEmployeeName());
							}
						}
						Object[] selectionValues = reps.toArray();
						String initialSelection = "Rep name";
						Object selection = JOptionPane.showInputDialog(null, "Select the rep",
								"Rep", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
						if (selection != null)
						{
							name = selection.toString();
							SalesCommChart chart = new SalesCommChart(chartMonthSelected, chartYearSelected, name);
							chart.draw();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		salesComm.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		salesComm.setBounds(248, 328, 153, 25);
		director.add(salesComm);
		
		JButton grantButton = new JButton("Grant");
		grantButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						GrantChart chart = new GrantChart(chartMonthSelected, chartYearSelected);
						chart.draw();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		grantButton.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		grantButton.setBounds(413, 328, 134, 25);
		director.add(grantButton);
		
		JButton jobStatusBtn = new JButton("Job Status");
		jobStatusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						JobStatus js = new JobStatus(chartMonthSelected, chartYearSelected);
						js.draw();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		jobStatusBtn.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		jobStatusBtn.setBounds(248, 366, 153, 25);
		director.add(jobStatusBtn);
		
		JButton jobStatusRepBtn = new JButton("Job Status Reps");
		jobStatusRepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChartMonthSelected(monthBoxDirector.getSelectedIndex());
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				if (chartMonthSelected > 0)
				{
					try {
						JComboBox<String> rep = new JComboBox<String>();
						for (int i = 0; i < users.size(); i ++){
							if (users.get(i).getEmployeePosition().equalsIgnoreCase("salesman"))
							{
								rep.addItem(users.get(i).getEmployeeName());
							}
						}
						JComboBox<String> jobType = new JComboBox<String>();
						String [] jobTypes = {"Live", "Closed", "All"};
						for (int i = 0; i < jobTypes.length; i ++){
							jobType.addItem(jobTypes[i]);
						}
						/*JComboBox<String> period = new JComboBox<String>();
						period.addItem("Weekly");
						period.addItem("Fortnightly");*/
						Object[] message = {"Rep", rep,
								"Job Type", jobType,};
						int option = JOptionPane.showConfirmDialog(null, message, "Select parameters", JOptionPane.OK_CANCEL_OPTION);
						if (option == JOptionPane.YES_OPTION){
							String name = rep.getSelectedItem().toString();
							String job = jobType.getSelectedItem().toString();
							JobStatusRep jsr = new JobStatusRep(chartMonthSelected, chartYearSelected, name, job);
							jsr.draw();
						}
						else{
							
						}
						/*

					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null,"Cannot display reps chart","Missing info",2);
						e1.printStackTrace();*/
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please select a month to view","Missing info",2);
				}
			}
		});
		jobStatusRepBtn.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		jobStatusRepBtn.setBounds(414, 366, 133, 25);
		director.add(jobStatusRepBtn);
		
		JButton btnRepConv = new JButton("Rep Conv");
		btnRepConv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChartYearSelected(yearBoxDirector.getSelectedItem().toString());
				try {
					String name = null;
					ArrayList<String> reps = new ArrayList<String>();
					for (int i = 0; i < users.size(); i ++)
					{
						if (users.get(i).getEmployeePosition().equalsIgnoreCase("salesman"))
						{
							reps.add(users.get(i).getEmployeeName());
						}
					}
					Object[] selectionValues = reps.toArray();
					String initialSelection = "Rep name";
					Object selection = JOptionPane.showInputDialog(null, "Select the rep",
							"Rep", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
					if (selection != null)
					{
						name = selection.toString();
						RepReport rr = new RepReport(chartYearSelected, name);
						rr.draw();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRepConv.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnRepConv.setBounds(100, 365, 138, 25);
		director.add(btnRepConv);

		server = new JPanel();
		server.setBackground(Color.WHITE);
		cards.add(server, SERVER);
		server.setLayout(null);

		serverIP = new JLabel("IP Address :");
		serverIP.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		serverIP.setBounds(196, 177, 105, 21);
		server.add(serverIP);

		serverPassword = new JLabel("Password :");
		serverPassword.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		serverPassword.setBounds(196, 239, 105, 14);
		server.add(serverPassword);

		serverUsername = new JLabel("Username :");
		serverUsername.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		serverUsername.setBounds(196, 209, 105, 14);
		server.add(serverUsername);

		serverIPEntry = new JTextField();
		serverIPEntry.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		serverIPEntry.setBounds(325, 177, 118, 20);
		server.add(serverIPEntry);
		serverIPEntry.setColumns(10);

		serverUsernameEntry = new JTextField();
		serverUsernameEntry.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		serverUsernameEntry.setBounds(325, 205, 118, 20);
		server.add(serverUsernameEntry);
		serverUsernameEntry.setColumns(10);

		serverPasswordEntry = new JTextField();
		serverPasswordEntry.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		serverPasswordEntry.setBounds(325, 235, 118, 20);
		server.add(serverPasswordEntry);
		serverPasswordEntry.setColumns(10);

		creoLogoServer = new JLabel("");
		creoLogoServer.setHorizontalAlignment(SwingConstants.CENTER);
		creoLogoServer.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoLogoServer.setBounds(0, 0, 643, 95);
		server.add(creoLogoServer);

		todaysDateServer = new JLabel("");
		todaysDateServer.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		todaysDateServer.setHorizontalAlignment(SwingConstants.CENTER);
		todaysDateServer.setBounds(147, 106, 134, 14);
		server.add(todaysDateServer);

		dateTimeServer = new JLabel("");
		dateTimeServer.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		dateTimeServer.setHorizontalAlignment(SwingConstants.CENTER);
		dateTimeServer.setBounds(267, 106, 168, 14);
		server.add(dateTimeServer);

		nameDeptServer = new JLabel("");
		nameDeptServer.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		nameDeptServer.setHorizontalAlignment(SwingConstants.CENTER);
		nameDeptServer.setBounds(147, 126, 288, 14);
		server.add(nameDeptServer);

		saveServerSettings = new JLabel("");
		saveServerSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (serverIPEntry.getText().length() > 1 && serverUsernameEntry.getText().length() > 1
						&& serverPasswordEntry.getText().length() > 1)
				{
					serverSettings[0] = serverIPEntry.getText();
					serverSettings[1] = serverUsernameEntry.getText();
					serverSettings[2] = serverPasswordEntry.getText();
					saveServerSettings(serverSettings);
					int result = JOptionPane.showConfirmDialog(null, "Please restart program to apply settings", "Restart program", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION)
					{
						try {
							System.exit(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Please enter the missing fields.","Missing info",2);
				}
			}
		});
		saveServerSettings.setIcon(new ImageIcon(CreoApp.class.getResource("/images/rsz_avlogo-save.png")));
		saveServerSettings.setHorizontalAlignment(SwingConstants.CENTER);
		saveServerSettings.setBounds(300, 276, 75, 77);
		server.add(saveServerSettings);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards,  ADMIN);
			}
		});
		btnCancel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		btnCancel.setBounds(286, 364, 99, 23);
		server.add(btnCancel);

		// Set up change password panel and all components
		changePassword = new JPanel();
		changePassword.setBackground(Color.WHITE);
		cards.add(changePassword, CHANGEPASS);
		changePassword.setLayout(null);

		creoLogoPassword = new JLabel("");
		creoLogoPassword.setHorizontalAlignment(SwingConstants.CENTER);
		creoLogoPassword.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newCreoLogo.JPG")));
		creoLogoPassword.setBounds(0, 0, 643, 95);
		changePassword.add(creoLogoPassword);

		usernamePassword = new JLabel("Username :");
		usernamePassword.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		usernamePassword.setBounds(210, 137, 114, 14);
		changePassword.add(usernamePassword);

		passwordLabel = new JLabel("Old password :");
		passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
		passwordLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		passwordLabel.setBounds(210, 170, 114, 14);
		changePassword.add(passwordLabel);

		newPassword = new JLabel("New Password :");
		newPassword.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		newPassword.setBounds(210, 202, 114, 14);
		changePassword.add(newPassword);

		usernameEntryPassword = new JTextField();
		usernameEntryPassword.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		usernameEntryPassword.setBounds(334, 134, 121, 20);
		changePassword.add(usernameEntryPassword);
		usernameEntryPassword.setColumns(10);

		oldPassEntryPassword = new JTextField();
		oldPassEntryPassword.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		oldPassEntryPassword.setBounds(334, 167, 121, 20);
		changePassword.add(oldPassEntryPassword);
		oldPassEntryPassword.setColumns(10);

		newPassEntryPassword = new JTextField();
		newPassEntryPassword.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		newPassEntryPassword.setBounds(334, 199, 121, 20);
		changePassword.add(newPassEntryPassword);
		newPassEntryPassword.setColumns(10);

		confirmPassChange = new JLabel("");
		confirmPassChange.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Connection con = null;
				Statement update = null;
				if (usernameEntryPassword.getText().length() > 0 && oldPassEntryPassword.getText().length() > 0
						&& newPassEntryPassword.getText().length()> 0)
				{
					String username = usernameEntryPassword.getText();
					String oldPass = (oldPassEntryPassword.getText());
					String newPass = (newPassEntryPassword.getText());
					try {
						con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
								":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
						update = con.createStatement();
						update.executeUpdate("UPDATE `userInfo` SET `Password`= ('"+newPass+"') WHERE `Username`= ('"+username+"') AND `Password` = ('"+oldPass+"')");
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null,"Cannot change password.","Missing info",2);
						ex.printStackTrace();
					}
					finally{
						try{
							update.close();
						}catch(SQLException e1){}
						try{
							con.close();
						}catch(SQLException e1){}
					}
					JOptionPane.showMessageDialog(null, "Password changed","Success", 1);
					usernameEntryPassword.setText("");
					oldPassEntryPassword.setText("");
					newPassEntryPassword.setText("");
					CardLayout cl = (CardLayout)(cards.getLayout());
					cl.show(cards, LOGIN);
					users.clear();
					fillUsers();
					
				}
				else{
					JOptionPane.showMessageDialog(null,"Please fill in all fields.","Missing info",2);
				}
			}
		});
		confirmPassChange.setIcon(new ImageIcon(CreoApp.class.getResource("/images/newAvs.JPG")));
		confirmPassChange.setBounds(302, 244, 75, 67);
		changePassword.add(confirmPassChange);

		JButton cancelChangePass = new JButton("Cancel");
		cancelChangePass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, LOGIN);
				usernameEntryPassword.setText("");
				oldPassEntryPassword.setText("");
				newPassEntryPassword.setText("");
			}
		});
		cancelChangePass.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		cancelChangePass.setBounds(289, 322, 99, 23);
		changePassword.add(cancelChangePass);

		copyright = new JPanel();
		copyright.setBackground(Color.WHITE);
		copyright.setLayout(null);
		GridBagConstraints gbc_copyright = new GridBagConstraints();
		gbc_copyright.insets = new Insets(0, 0, 0, 5);
		gbc_copyright.fill = GridBagConstraints.BOTH;
		gbc_copyright.gridx = 1;
		gbc_copyright.gridy = 2;
		frmCreo.getContentPane().add(copyright, gbc_copyright);

		JTextField copyrightLabel = new JTextField();//("Copyright \u00A9 2013 Amplio Virtus Limited. Registered in the" + "\n" + "Republic of Ireland. Company Number: 505716. All rights reserved.");
		copyrightLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		copyrightLabel.setBackground(Color.WHITE);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		copyrightLabel.setText("Copyright \u00A9" + year + " Amplio Virtus Limited. V1.95");
		copyrightLabel.setAlignmentX(StyleConstants.ALIGN_CENTER);
		copyrightLabel.setEditable(false);
		copyrightLabel.setFont(new Font("Trajan Pro", Font.BOLD, 11));
		copyrightLabel.setBounds(10, 0, 633, 32);
		copyright.add(copyrightLabel);

	}
	//Automatically sets up the counties list from a .txt file
	public void addCounties() throws Exception 
	{
		java.net.URL file = this.getClass().getResource("counties.txt");
		InputStream in = file.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));       
		for (int i = 0; i < 32; i++){
			counties[i] = br.readLine();
		}
		in.close(); 

	}
	// Sets up the products
	public void addProducts() throws Exception 
	{
		java.net.URL file = this.getClass().getResource("products.txt");
		InputStream in = file.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for (int i = 0; i < 16; i++){
			products.add(br.readLine());
		}
		in.close();

	}
	// Sets up the referrals
	public void addReferrals() throws Exception 
	{
		java.net.URL file = this.getClass().getResource("referrals.txt");
		InputStream in = file.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for (int i = 0; i < 13; i++){
			referrals.add(br.readLine());
		}
		in.close();

	}
	//Save the contents of the users array to a file called UserDatabase////////////////////////////////////////////
	public void saveUserData(ArrayList<User> users)
	{
		File aFile = new File("UserDatabase.dat");
		try {
			FileOutputStream fos = new FileOutputStream(aFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(users);
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("File doesn't exist");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Save the contents of the serverSettings array to a file called ServerSettings////////////////////////////////////////////
	public void saveServerSettings(String[] serverDetails)
	{
		File aFile = new File("ServerSettings.dat");
		try {
			FileOutputStream fos = new FileOutputStream(aFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(serverDetails);
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("File doesn't exist");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Loads the data from a jobs file.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Job> loadJobData()
	{
		ArrayList <Job> job;

		try 
		{
			FileInputStream inByteStream = new FileInputStream(jobsFile);
			ObjectInputStream OIStream = new ObjectInputStream(inByteStream);
			job = (ArrayList<Job>) OIStream.readObject();
			OIStream.close();
			return job;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * Loads the data from a user file.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<User> loadUserData()
	{
		ArrayList <User> user;

		try 
		{
			FileInputStream inByteStream = new FileInputStream(userFile);
			ObjectInputStream OIStream = new ObjectInputStream(inByteStream);
			user = (ArrayList<User>) OIStream.readObject();
			OIStream.close();
			return user;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * Loads the data from a serverSettings file.
	 */
	public String [] loadServerSettings()
	{
		String [] serverSettings;

		try 
		{
			FileInputStream inByteStream = new FileInputStream(serverFile);
			ObjectInputStream OIStream = new ObjectInputStream(inByteStream);
			serverSettings = (String[]) OIStream.readObject();
			OIStream.close();
			return serverSettings;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @return the users
	 */
	public ArrayList<User> getUsers() {
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	/**
	 * @return the jobs
	 */
	public ArrayList<Job> getJobs() {
		return jobs;
	}
	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
	public void addJobsToTable(String name)
	{
		/*int count = 0;
		try {
			PreparedStatement countRows = con.prepareStatement("SELECT COUNT(*) FROM `jobs`");
			ResultSet rows = countRows.executeQuery();
			while(rows.next()){
				count = rows.getInt(1);
		       }
		} catch (SQLException e1) {
			e1.printStackTrace();
		}*/
		//if (count > table.getRowCount()){
		
		model.setRowCount(0);
		Connection con = null;
		PreparedStatement selectStatement = null;
		ResultSet result = null;
		try {
			// change serverSettings[1] to ampliogl_creoTab when hosted
			con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + ":3306/ampliogl_creoDB", "ampliogl_creoTab", serverSettings[2]);
			selectStatement = con.prepareStatement("SELECT * FROM `jobs`WHERE `Status` != 'closed' AND `Passed to` = ('"+name+"') order by `Job Number` desc;");//WHERE `Status` != 'closed'
			result = selectStatement.executeQuery();
			int i = 0;
			while (result.next()){
				model.insertRow(i,new Object[]{result.getString(1),(result.getString(14)),(result.getString(2)),(result.getString(3)),(result.getString(4)), ((result.getString(5))),
						((result.getString(6))),((result.getString(7))),result.getString(8),result.getString(9),
						result.getString(10), result.getString(11), result.getString(12),result.getString(13),
						result.getString(14), result.getString(15), result.getString(16).toString(), result.getString(17).toString()});
				i ++;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Problem filling table.","Missing info",2);
			e.printStackTrace();
		}
		finally{
			try{
				result.close();
			}catch(SQLException e){}
			try{
				selectStatement.close();
			}catch(SQLException e){}
			try{
				con.close();
			}catch(SQLException e){}
		}
	}
	//}
	public String receptionistToString()
	{
		return "Job Name = " + nameEntryRecep.getText() + "Job Surname = " + surnameEntryRecep.getText()
				+ ", Address = " + addressEntryRecep.getText()+ ", Landline = " + landlineEntryRecep.getText()
				+ ", Mobile = " + mobileEntryRecep.getText() + ", Email = " + emailEntryRecep.getText() 
				+ ", County = " + countyBox.getSelectedItem().toString() + ", CustomerType = " + cusTypeBox.getSelectedItem().toString()
				+ "Product = " + productBox.getSelectedItem().toString() + ", Referral = " + referralBox.getSelectedItem().toString()
				+ ", Source = " + sourceBox.getSelectedItem().toString() + ", PassedTo = " + passedToBox.getSelectedItem().toString()
				+ ", Comments = " + commentsField.getText() + " " + timeStamp ;
	}
	public String tableToString(int i)
	{
		return "Job Name = " + table.getValueAt(i, 0) + ", Job Surname = " + table.getValueAt(i, 1)
				+ ", Address = " + table.getValueAt(i, 2) + ", Landline = " + table.getValueAt(i, 3)
				+ ", Mobile = " + table.getValueAt(i, 4) + ", Email = " + table.getValueAt(i, 5)
				+ ", County = " + table.getValueAt(i, 6)+ ", CustomerType = " + table.getValueAt(i, 7)
				+ ", Product = " + table.getValueAt(i, 8) + ", Referral = " + table.getValueAt(i, 9)
				+ ", Source = " + table.getValueAt(i, 10) + ", Status = " + table.getValueAt(i, 11) 
				+ ", PassedTo = " + table.getValueAt(i, 12)+ ", Comments = " + table.getValueAt(i, 13) + " " + timeStamp + " \n";
	}
	public String getRecepName(String name, String surname, String address, String landline, String mobile, String email) throws SQLException
	{
		String recep = null; 
		Connection connect = null;
		PreparedStatement selectStatement= null;
		ResultSet result = null;
		try{
			connect = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + ":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
			selectStatement = connect.prepareStatement ("SELECT * FROM `jobs` WHERE `name` = ('"+name+"') AND `surname` = "
					+ "('"+surname+"') AND `address` = ('"+address+"')"
					+ "AND `landline` = ('"+landline+"') AND `mobile` = "
					+ "('"+mobile+"') AND `e-mail` = ('"+email+"') LIMIT 0 , 30");
			result = selectStatement.executeQuery();
			if (result.next()){
				recep = result.getString(21);
			}
		}
		finally{
			try{
				result.close();
			}catch(SQLException e){}
			try{
				selectStatement.close();
			}catch(SQLException e){}
			try{
				connect.close();
			}catch(SQLException e){}
		}
		return recep;
	}
	public void checkDuplicateJob()
	{
		Connection connect = null;
		PreparedStatement selectStatement= null;
		ResultSet result = null;
		try {
			if (userSaving.equalsIgnoreCase("recep"))
			{
				String name = ((nameEntryRecep.getText()));
				String surname = (replaceQuote((surnameEntryRecep.getText())));
				String address = ((addressEntryRecep.getText()));
				String landline = ((landlineEntryRecep.getText()));
				String mobile = ((mobileEntryRecep.getText()));
				String email = ((emailEntryRecep.getText()));
				connect = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + ":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = connect.prepareStatement ("SELECT * FROM `jobs` WHERE `name` = ('"+name+"') AND `surname` = "
						+ "('"+surname+"') AND `address` = ('"+address+"')"
						+ "AND `landline` = ('"+landline+"') AND `mobile` = "
						+ "('"+mobile+"') AND `e-mail` = ('"+email+"') LIMIT 0 , 30");
				result = selectStatement.executeQuery();
				if (result.next())
				{
					taken = result.getString(13);
					jobExists = true;
					duplicateJobId = (result.getString(1));
				}
				else{
					jobExists = false;
				}
				userSaving = "";
				selectStatement.close();
				result.close();
				connect.close();
			}
			else if (userSaving.equalsIgnoreCase("sales"))
			{
				String name = ((salesEditName.getText()));
				String surname = (replaceQuote((salesEditSurname.getText())));
				String address = ((salesEditAddress.getText()));
				String landline = ((salesEditLandline.getText()));
				String mobile = ((salesEditMobile.getText()));
				String email = ((salesEditEmail.getText()));
				connect = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + ":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = connect.prepareStatement ("SELECT * FROM `jobs` WHERE `name` = ('"+name+"') AND `surname` = "
						+ "('"+surname+"') AND `address` = ('"+address+"')"
						+ "AND `landline` = ('"+landline+"') AND `mobile` = "
						+ "('"+mobile+"') AND `e-mail` = ('"+email+"') LIMIT 0 , 30");
				result = selectStatement.executeQuery();
				if (result.next())
				{
					jobExists = true;
					duplicateJobId = (result.getString(1));
				}
				else{
					jobExists = false;
				}
				userSaving = "";
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Problem with check duplicate job.","Missing info",2);
			e.printStackTrace();
		}
		finally{
			try{
				result.close();
			}catch(SQLException e){}
			try{
				selectStatement.close();
			}catch(SQLException e){}
			try{
				connect.close();
			}catch(SQLException e){}
		}
	}
	public void clearReceptionistFields()
	{
		nameEntryRecep.setText("First Name");
		nameEntryRecep.setBackground(Color.LIGHT_GRAY);
		surnameEntryRecep.setText("Surname");
		surnameEntryRecep.setBackground(Color.LIGHT_GRAY);
		addressEntryRecep.setText("Address");
		addressEntryRecep.setBackground(Color.LIGHT_GRAY);
		landlineEntryRecep.setText("Landline");
		landlineEntryRecep.setBackground(Color.LIGHT_GRAY);
		mobileEntryRecep.setText("Mobile");
		mobileEntryRecep.setBackground(Color.LIGHT_GRAY);
		emailEntryRecep.setText("E-Mail");
		emailEntryRecep.setBackground(Color.LIGHT_GRAY);
		countyBox.setSelectedIndex(0);
		cusTypeBox.setSelectedIndex(0);
		productBox.setSelectedIndex(0);
		referralBox.setSelectedIndex(0);
		sourceBox.setSelectedIndex(0);
		passedToBox.setSelectedIndex(0);
		commentsField.setText("");
	}
	public void repopulateReceptionFields()
	{
		Connection con = null;
		PreparedStatement selectStatement= null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
					":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
			selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `name` = ('"+(tempArr[0])+"') AND `surname` = "
					+ "('"+(tempArr[1])+"') LIMIT 0 , 30");
			result = selectStatement.executeQuery();
			while (result.next())
			{
				jobNoRecep.setText(result.getString(1));
				nameEntryRecep.setText((result.getString(2)));
				nameEntryRecep.setBackground(Color.WHITE);
				surnameEntryRecep.setText((result.getString(3)));
				surnameEntryRecep.setBackground(Color.WHITE);
				addressEntryRecep.setText((result.getString(4)));
				addressEntryRecep.setBackground(Color.WHITE);
				landlineEntryRecep.setText(((result.getString(5))));
				landlineEntryRecep.setBackground(Color.WHITE);
				mobileEntryRecep.setText(((result.getString(6))));
				mobileEntryRecep.setBackground(Color.WHITE);
				emailEntryRecep.setText((result.getString(7)));
				emailEntryRecep.setBackground(Color.WHITE);

				int index = 1;
				for (int j = 0; j < counties.length; j++){
					if  (!result.getString(8).equalsIgnoreCase(counties[j])){
						index ++;
					}
					else{
						countyBox.setSelectedIndex(index);
						index = 1;
						break;
					}
				}
				int type = 0;
				for (int k = 0; k < customerTypes.length; k ++){
					if (!result.getString(9).equalsIgnoreCase(customerTypes[k])){
						type ++;
					}
					else{
						cusTypeBox.setSelectedIndex(type);
						type = 1;
						break;
					}
				}
				int product = 1;
				for (int x = 0; x < products.size(); x ++){
					if (!result.getString(10).equalsIgnoreCase(products.get(x))){
						product ++;
					}
					else{
						productBox.setSelectedIndex(product);
						product = 1;
						break;
					}
				}
				int referral = 1;
				for (int y = 0; y < referrals.size(); y ++){
					if (!result.getString(11).equalsIgnoreCase(referrals.get(y))){
						referral ++;
					}
					else{
						referralBox.setSelectedIndex(referral);
						referral = 1;
						break;
					}
				}
				int source = 0;
				for (int z = 0; z < sources.length; z ++){
					if (!result.getString(12).equalsIgnoreCase(sources[z])){
						source ++;
					}
					else{
						sourceBox.setSelectedIndex(source);
						source = 0;
						break;
					}
				}
				int passed = 0;
				for (int h = 0; h < passedToBox.getItemCount(); h ++){
					if (!result.getString(14).equalsIgnoreCase(passedToBox.getItemAt(h))){
						passed ++;
					}
					else{
						passedToBox.setSelectedIndex(passed);
						passed = 0;
						break;
					}
				}
				commentsField.setText(result.getString(17));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Cannot repopulate reception fields.","Missing info",2);
			e.printStackTrace();
		}
		finally{
			try{
				result.close();
			}catch(SQLException e){}
			try{
				selectStatement.close();
			}catch(SQLException e){}
			try{
				con.close();
			}catch(SQLException e){}
		}
	}
	private void saveJob(Job job)
	{
		Connection con = null;
		Statement insert = null;
		Statement update = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
					":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
			insert = con.createStatement();
			checkDuplicateJob();
			if (jobExists){
				update = con.createStatement();
				update.executeUpdate("UPDATE `jobs` SET `Name`= ('"+job.getJobName()+"'),`Surname`=('"+replaceQuote(job.getJobSurname())+"') "
						+", `Address` = ('"+job.getJobAddress()+"'), `Landline` =  ('"+job.getJobLandline()+"'), `Mobile` = ('"+job.getJobMobile()+"') "
						+", `E-Mail` = ('"+job.getJobEmail()+"'), `County` = ('"+job.getJobCounty()+"'), `Customer Type` = ('"+job.getJobCustomerType()+"')"
						+", `Product Type`= ('"+job.getJobProductType()+"'), `Referral` = ('"+job.getJobReferral()+"'), `Source` = ('"+job.getJobSource()+"') "
						+", `Status` = ('"+taken+"'), `Passed To` = ('"+job.getJobPassedTo()+"'),`Rep` = ('"+job.getRep()+"'), `Comments` = ('"+job.getJobComments()+"')"
						+", `Reason for closing` = ('"+job.getClosingReason()+"'), `Primary Record` = ('"+getCurrentTimeStamp()+"'), `Sale` = ('"+job.getSale()+"'), `Commission` = ('"+job.getCommission()+"')"+ "WHERE `Job Number`=('"+duplicateJobId+"')"); 
				clearReceptionistFields();
			}
			else{
				insert.executeUpdate("INSERT INTO `ampliogl_creoDB`.`jobs` (`Job Number`, `Name`, `Surname`, `Address`, `Landline`, `Mobile`, `E-Mail`, `County`, `Customer Type`, `Product Type`, `Referral`, `Source`, `Status`, `Passed to`, `Rep`, `Reason for closing`, `Comments`, `Primary Record`, `Sale`, `Commission`, `Receptionist`) "
						+ "VALUES (NULL, ('"+job.getJobName()+"'), ('"+replaceQuote(job.getJobSurname())+"'), ('"+job.getJobAddress()+"'), ('"+job.getJobLandline()+"'), ('"+job.getJobMobile()+"'), ('"+job.getJobEmail()+"'), ('"+job.getJobCounty()+"'), ('"+job.getJobCustomerType()+"'),('"+job.getJobProductType()+"'), ('"+job.getJobReferral()+"'), ('"+job.getJobSource()+"'), ('"+job.getJobLiveClosed()+"'), ('"+job.getJobPassedTo()+"'),('"+job.getRep()+"'),('"+job.getClosingReason()+"'),('"+job.getJobComments()+"'),('"+getCurrentTimeStamp()+"'), ('"+job.getSale()+"'), ('"+job.getCommission()+"'), ('"+job.getReceptionist()+"'))");
				clearReceptionistFields();
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Database unavailable.","Missing info",2);
			e.printStackTrace();
		}
		finally{
			try{
				insert.close();
			}catch(SQLException e){}
			try{
				con.close();
			}catch(SQLException e){}
		}
	}
	private void disableKeys(InputMap inputMap) {
		String[] keys = {"DELETE", "BACK_SPACE", "LEFT", "RIGHT"};
		for (String key : keys) {
			inputMap.put(KeyStroke.getKeyStroke(key), "none");
		}    
	}
	private static java.sql.Timestamp getCurrentTimeStamp() 
	{
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}
	private void addEmail(String reciepient, String order)
	{
		try {
			if (reciepient.length() > 0){
				String url = "mailTo:" + reciepient + "?subject=" + order;
				URI mailTo = new URI(url);
				fDesktop.mail(mailTo);
			}
			else{
				JOptionPane.showMessageDialog(null, "No E-Mail address for client. " + "\n" + 
						"Please edit job to add E-Mail.");
			}
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Cannot launch mail client");
		}
		catch (URISyntaxException ex) {
			JOptionPane.showMessageDialog(null, "Mail address issues.");
		}
	}

	public void print()
	{
		PrinterJob pj = PrinterJob.getPrinterJob();
		PageFormat pf = pj.pageDialog(pj.defaultPage());
		pj.setPrintable(new MyPrintable(), pf);
		if (pj.printDialog()) {
			try {
				pj.print();
			} catch (PrinterException pe) {
				pe.printStackTrace(System.err);
			}
		}
	}
	class MyPrintable implements Printable {
		public int print(Graphics g, PageFormat pf, int i) throws PrinterException {
			if (i > 0) {
				return NO_SUCH_PAGE;
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			// get the bounds of the component
			Dimension dim = frmCreo.getSize();
			double cHeight = dim.getHeight();
			double cWidth = dim.getWidth();

			// get the bounds of the printable area
			double pHeight = pf.getImageableHeight();
			double pWidth = pf.getImageableWidth();

			double xRatio = pWidth / cWidth;
			double yRatio = pHeight / cHeight;

			Paper A4 = new Paper();
			A4.setSize(595, 842);

			g2d.scale(xRatio, yRatio);

			frmCreo.printAll(g);
			return Printable.PAGE_EXISTS;
		}
	}
	/*public boolean hostAvailabilityCheck() { 
		try (Socket s = new Socket(serverSettings[0], 3306)) 
		{
			System.out.println("IN");
			return true;
		} 
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null,"No communication with server,"
					+ "\n" + "  Please contact admin","Server not found",1);
		}
		return false;
	}*/
	/*public String encrypt(String str)
	{
		StringBuffer sb = new StringBuffer (str);

		int lenStr = str.length();
		int lenKey = key.length();
		for ( int i = 0, j = 0; i < lenStr; i++, j++ ) 
		{
			if ( j >= lenKey ) j = 0;
			sb.setCharAt(i, (char)(str.charAt(i) ^ key.charAt(j))); 
		}
		return sb.toString();
	}
	public String decrypt(String str)
	{
		return encrypt(str);
	}
	public String capitalize(String word)
	{
		String newWord = (word.substring(0, 1).toUpperCase() + word.substring(1));
		return newWord;
	}
	public String decapitalize(String word)
	{
		String newWord = (word.substring(0, 1).toLowerCase() + word.substring(1));
		return newWord;
	}
	public String addLetterToString(String word)
	{
		String newWord = ("P"+word);
		return newWord;
	}
	public String removeLetterFromString(String word)
	{
		String newWord = word.substring(1);
		return newWord;
	}*/
	/**
	 * Fill the user array with all users in database
	 */
	public void fillUsers()
	{
		Connection con = null;
		PreparedStatement selectStatement = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
					":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
			//con = DriverManager.getConnection("jdbc:mysql://31.220.17.2:3306/ampliogl_creoDB", "ampliogl_creo","avs123");
			selectStatement = con.prepareStatement("SELECT * FROM `userinfo`");
			result = selectStatement.executeQuery();
			while (result.next())
			{
				User user = new User(result.getString(1),(result.getString(2)), result.getString(3), result.getString(4));
				users.add(user);
				
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Cannot fill users in CreoApp.","Missing info",2);
			e.printStackTrace();
		}
		finally{
			try{
				result.close();
			}catch(SQLException e){}
			try{
				selectStatement.close();
			}catch(SQLException e){}
			try{
				con.close();
			}catch(SQLException e){}
		}
	} 
	/**
	 * Check if caps is on 
	 * @return boolean
	 */
	public boolean checkForCaps()
	{
		if(Toolkit.getDefaultToolkit().getLockingKeyState (KeyEvent.VK_CAPS_LOCK ))
		{
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Search through all jobs in database
	 */
	private void searchJobs()
	{
		ArrayList<String> templist = new ArrayList<String>();
		boolean found = false;
		boolean noResult = false;
		Connection con = null;
		PreparedStatement selectStatement = null;
		ResultSet result = null;
		if (nameEntryRecep.getBackground()== (Color.LIGHT_GRAY) && surnameEntryRecep.getBackground() == (Color.LIGHT_GRAY) 
				&& addressEntryRecep.getBackground()== (Color.LIGHT_GRAY) && landlineEntryRecep.getBackground() == (Color.LIGHT_GRAY)
				&& mobileEntryRecep.getBackground()== (Color.LIGHT_GRAY) && emailEntryRecep.getBackground()== (Color.LIGHT_GRAY)){

			try {
				con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
						":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `Primary Record` > current_date -64 LIMIT 0 , 30");
				result = selectStatement.executeQuery();
				while (result.next())
				{
					templist.add((result.getString(2)) + ", " + (result.getString(3)) + ", " + (result.getString(4))
							+ ", " +((result.getString(5))) + ", " + ((result.getString(6))) + ", " + (result.getString(7)));
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Database results empty.","Missing info",2);
				e.printStackTrace();
			}
			finally{
				try{
					result.close();
				}catch(SQLException e){}
				try{
					selectStatement.close();
				}catch(SQLException e){}
				try{
					con.close();
				}catch(SQLException e){}
			}
		}
		else if (nameEntryRecep.getBackground()== (Color.WHITE) && found == false){
			String name = ((nameEntryRecep.getText()));
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
						":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `Name` = ('"+name+"')");
				result = selectStatement.executeQuery();
				/*if (!result.next()){
					JOptionPane.showMessageDialog(null,"Name not found.","Missing info",2);
					noResult = true;
				}*/
				while (result.next())
				{
					templist.add((result.getString(2)) + ", " + (result.getString(3)) + ", " + (result.getString(4))
							+ ", " +((result.getString(5))) + ", " + ((result.getString(6))) + ", " + (result.getString(7)));
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Name not found.","Missing info",2);
				e.printStackTrace();
			}
			finally{
				try{
					result.close();
				}catch(SQLException e){}
				try{
					selectStatement.close();
				}catch(SQLException e){}
				try{
					con.close();
				}catch(SQLException e){}
			}
		}
		else if (surnameEntryRecep.getBackground()== (Color.WHITE) && found == false){
			String surname = (replaceQuote(surnameEntryRecep.getText()));
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
						":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `Surname` = ('"+surname+"')");
				result = selectStatement.executeQuery();
				while (result.next())
				{
					templist.add((result.getString(2)) + ", " + (result.getString(3)) + ", " + (result.getString(4))
							+ ", " +((result.getString(5))) + ", " + ((result.getString(6))) + ", " + (result.getString(7)));
				}
				/*if{
					JOptionPane.showMessageDialog(null,"Surname not found.","Missing info",2);
					noResult = true;
				}*/
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Surname not found.","Missing info",2);
				e.printStackTrace();
			}
			finally{
				try{
					result.close();
				}catch(SQLException e){}
				try{
					selectStatement.close();
				}catch(SQLException e){}
				try{
					con.close();
				}catch(SQLException e){}
			}
		}
		else if (addressEntryRecep.getBackground()== (Color.WHITE)&& found == false){
			String address = ((addressEntryRecep.getText()));
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
						":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `Address` = ('"+address+"')");
				result = selectStatement.executeQuery();
				while (result.next())
				{
					templist.add((result.getString(2)) + ", " + (result.getString(3)) + ", " + (result.getString(4))
							+ ", " +((result.getString(5))) + ", " + ((result.getString(6))) + ", " + (result.getString(7)));
				}
				/*else{
					JOptionPane.showMessageDialog(null,"Address not found.","Missing info",2);
					noResult = true;
				}*/
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Address not found.","Missing info",2);
				e.printStackTrace();
			}
			finally{
				try{
					result.close();
				}catch(SQLException e){}
				try{
					selectStatement.close();
				}catch(SQLException e){}
				try{
					con.close();
				}catch(SQLException e){}
			}
		}
		else if (landlineEntryRecep.getBackground()== (Color.WHITE)&& found == false){
			String landline = ((landlineEntryRecep.getText()));
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
						":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `Landline` = ('"+landline+"')");
				result = selectStatement.executeQuery();
				while (result.next())
				{
					templist.add((result.getString(2)) + ", " + (result.getString(3)) + ", " + (result.getString(4))
							+ ", " +((result.getString(5))) + ", " + ((result.getString(6))) + ", " + (result.getString(7)));
				}
				/*else{
					JOptionPane.showMessageDialog(null,"Landline not found.","Missing info",2);
					noResult = true;
				}*/
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Landline not found.","Missing info",2);
				e.printStackTrace();
			}
			finally{
				try{
					result.close();
				}catch(SQLException e){}
				try{
					selectStatement.close();
				}catch(SQLException e){}
				try{
					con.close();
				}catch(SQLException e){}
			}
		}
		else if (mobileEntryRecep.getBackground()== (Color.WHITE)&& found == false){
			String mobile = ((mobileEntryRecep.getText()));
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
						":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `Mobile` = ('"+mobile+"')");
				result = selectStatement.executeQuery();
				while (result.next())
				{
					templist.add((result.getString(2)) + ", " + (result.getString(3)) + ", " + (result.getString(4))
							+ ", " +((result.getString(5))) + ", " + ((result.getString(6))) + ", " + (result.getString(7)));
				}
				/*if{
					JOptionPane.showMessageDialog(null,"Mobile not found.","Missing info",2);
					noResult = true;
				}*/
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"Mobile not found.","Missing info",2);
				e.printStackTrace();
			}
			finally{
				try{
					result.close();
				}catch(SQLException e){}
				try{
					selectStatement.close();
				}catch(SQLException e){}
				try{
					con.close();
				}catch(SQLException e){}
			}
		}
		else if (emailEntryRecep.getBackground()== (Color.WHITE)&& found == false){
			String email = ((emailEntryRecep.getText()));
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
						":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
				selectStatement = con.prepareStatement("SELECT * FROM `jobs` WHERE `E-Mail` = ('"+email+"')");
				result = selectStatement.executeQuery();
				while (result.next())
				{
					templist.add((result.getString(2)) + ", " + (result.getString(3)) + ", " + (result.getString(4))
							+ ", " +((result.getString(5))) + ", " + ((result.getString(6))) + ", " + (result.getString(7)));
				}
				/*else{
					JOptionPane.showMessageDialog(null,"Email not found.","Missing info",2);
					noResult = true;
				}*/
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,"E-Mail not found.","Missing info",2);
				e.printStackTrace();
			}
			finally{
				try{
					result.close();
				}catch(SQLException e){}
				try{
					selectStatement.close();
				}catch(SQLException e){}
				try{
					con.close();
				}catch(SQLException e){}
			}
		}
		String input = null;
		if (!noResult){
			input = (String) JOptionPane.showInputDialog (listOfJobs,"Please select a job",
					"Saved jobs",JOptionPane.INFORMATION_MESSAGE, null, templist.toArray(), "");
		}
		if ((input == null)) {
			clearReceptionistFields();
		}
		else{
			String chosenJob = input;
			for (int i = 0; i < tempArr.length; i ++){
				tempArr = chosenJob.split(", ");
			}
			repopulateReceptionFields();
		}
	}
	/**
	 * @return weekly sales of salesman
	 */
	public int weeklySales()
	{
		getWeeklySales();
		int sum = 0;
		for (int i = 0; i < salesmanSales.size(); i++) {
			sum = sum +(salesmanSales.get(i));
		}
		return sum;
		
	}
	public int weeklyComm()
	{
		getWeeklyCommission();
		int sum = 0;
		for (int i = 0; i < salesmanCommission.size(); i++) {
			sum = sum + (salesmanCommission.get(i));
		}
		return sum;
		
	}
	public void getWeeklySales()
	{
		String name = loggedIn.getText();
		String[] parts = name.split(" ");
		String rep = parts[0];
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
					":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
			statement = con.prepareStatement("SELECT sale FROM `jobs` WHERE WEEKOFYEAR(`Primary Record`) = WEEKOFYEAR(NOW()) AND YEAR (`Primary Record`) = ('"+currentYear+"') AND `Rep` = ('"+rep+"')");
			result = statement.executeQuery();
			while (result.next())
			{
				if (result.getString(1).equalsIgnoreCase(""))
				{
					salesmanSales.add(0);
				}
				else{
					salesmanSales.add(Integer.parseInt(result.getString(1)));
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"E-Mail not found.","Missing info",2);
			e.printStackTrace();
		}
		finally{
			try{
				result.close();
			}catch(SQLException e){}
			try{
				statement.close();
			}catch(SQLException e){}
			try{
				con.close();
			}catch(SQLException e){}
		}
	}
	public void getWeeklyCommission()
	{
		String name = loggedIn.getText();
		String[] parts = name.split(" ");
		String rep = parts[0];
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + serverSettings[0] + 
					":3306/ampliogl_creoDB", serverSettings[1], serverSettings[2]);
			statement = con.prepareStatement("SELECT commission FROM `jobs` WHERE WEEKOFYEAR(`Primary Record`) = WEEKOFYEAR(NOW()) AND YEAR (`Primary Record`) = ('"+currentYear+"') AND `Rep` = ('"+rep+"')");
			result = statement.executeQuery();
			while (result.next())
			{
				if (result.getString(1).equalsIgnoreCase(""))
				{
					salesmanCommission.add(0);
				}
				else{
					salesmanCommission.add(Integer.parseInt(result.getString(1)));
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"E-Mail not found.","Missing info",2);
			e.printStackTrace();
		}
		finally{
			try{
				result.close();
			}catch(SQLException e){}
			try{
				statement.close();
			}catch(SQLException e){}
			try{
				con.close();
			}catch(SQLException e){}
		}
	}
	/*public String encrypt(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}*/
	public String replaceQuote(String word)
	{
		String str = word.replace("'", "");
		return str;
	}
	/**
	 * @return the chartMonthSelected
	 */
	public int getChartMonthSelected() {
		return chartMonthSelected;
	}
	/**
	 * @param chartMonthSelected the chartMonthSelected to set
	 */
	public void setChartMonthSelected(int chartMonthSelected) {
		this.chartMonthSelected = chartMonthSelected;
	}
	/**
	 * @return the chartYearSelected
	 */
	public String getChartYearSelected() {
		return chartYearSelected;
	}
	/**
	 * @param chartYearSelected the chartYearSelected to set
	 */
	public void setChartYearSelected(String chartYearSelected) {
		this.chartYearSelected = chartYearSelected;
	}
	/**
	 * @return the dateChooserSales
	 */
	public JDateChooser getDateChooserSales() {
		return dateChooserSales;
	}
	/**
	 * @param dateChooserSales the dateChooserSales to set
	 */
	public void setDateChooserSales(JDateChooser dateChooserSales) {
		this.dateChooserSales = dateChooserSales;
	}
	/**
	 * @return the scrollPane
	 */
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	/**
	 * @param scrollPane the scrollPane to set
	 */
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	/**
	 * @return Salesman array
	 */
	public ArrayList<Integer> getSalesmanSales() {
		return salesmanSales;
	}
	/**
	 * @param salesmanSales
	 */
	public void setSalesmanSales(ArrayList<Integer> salesmanSales) {
		this.salesmanSales = salesmanSales;
	}
	/**
	 * @return Salesman commission
	 */
	public ArrayList<Integer> getSalesmanCommission() {
		return salesmanCommission;
	}
	/**
	 * @param salesmanCommission
	 */
	public void setSalesmanCommission(ArrayList<Integer> salesmanCommission) {
		this.salesmanCommission = salesmanCommission;
	}
	/**
	 * @return Taken
	 */
	public String getTaken() {
		return taken;
	}
	/**
	 * @param taken
	 */
	public void setTaken(String taken) {
		this.taken = taken;
	}
}
