package gui;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.LayoutStyle.ComponentPlacement;

import businessLogic.BlFacade;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class LoginRegisterGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField textUsernameReg;
	private JPasswordField textPasswordReg;
	JButton buttonLogin = new JButton("LOGIN");
	JButton buttonRegister = new JButton("REGISTER");
	private JButton closeBtn = new JButton(ResourceBundle.getBundle("Etiquetas").
			getString("Close"));
	private BlFacade businessLogic;

	public void setBusinessLogic(BlFacade bl) {
		businessLogic = bl;
	}
	
	public LoginRegisterGUI(BlFacade bl)
	{
		businessLogic = bl;
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit() throws Exception
	{	
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(400, 300));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginRegister"));
		
		closeBtn.setBounds(new Rectangle(130, 220, 130, 30));

		closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jButton_actionPerformed(e);
			}
		});
		
		this.getContentPane().add(closeBtn, null);
		
		JLabel labelRegister = new JLabel("Register/Login");
		labelRegister.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel labelUsernameReg = new JLabel("Username:");
		
		JLabel labelPasswordReg = new JLabel("Password:");
		
		textUsernameReg = new JTextField();
		textUsernameReg.setColumns(10);
		
		textPasswordReg = new JPasswordField();
		
		
		JTextArea validText = new JTextArea();
		validText.setEditable(false);
		
		this.buttonLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String username = textUsernameReg.getText();
				String password = textPasswordReg.getText();
				
				if (businessLogic.validateLogin(username, password))
					validText.setText("Successful login!");
				else
					validText.setText("Error. Try again.");
			}
		});
		
		this.buttonRegister.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String username = textUsernameReg.getText();
				String password = textPasswordReg.getText();
				
				if (businessLogic.registerUser(username, password))
					validText.setText("Registered successfully!");
				else
					validText.setText("Error. Try again.");
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(labelPasswordReg)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textPasswordReg))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(labelUsernameReg)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textUsernameReg, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(96)
							.addComponent(buttonRegister, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(buttonLogin, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(labelRegister, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addContainerGap(20, Short.MAX_VALUE)
							.addComponent(validText, GroupLayout.PREFERRED_SIZE, 348, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addContainerGap(63, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(labelRegister)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelUsernameReg)
						.addComponent(textUsernameReg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textPasswordReg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelPasswordReg))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonLogin, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonRegister, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addGap(50)
					.addComponent(validText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(49, Short.MAX_VALUE))
		);
		this.getContentPane().setLayout(groupLayout);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void jButton_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
