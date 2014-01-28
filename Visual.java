import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class Visual {
	private JButton[] playerButton;
	private JFrame playerFrame, chooseOption;
	private JPanel main;
	private JPanel playerMain;
	private JPanel buttons,game;
	private JRadioButton server,socket;
	private JButton showComp,newGameHuman,newGameComputer;
	private JMenuBar menuBar;
	private JMenu options,about;
	private JMenuItem playAgainstComputer,playAgainstHuman,endGame,newGame;
	private JMenuItem help, developers;
	private Computer[] computers;
	private Network net;
	private JLabel noOfMoves;
	private int noOfMove = 0,noOfPlayers;
	private boolean status,networkMatch = false;
	int check = 0;
	
	public void init()
	{
		playerButton = new JButton[25];
		playerFrame = new JFrame("BINGO - Player");
		playerMain = new JPanel(new GridLayout(5,5));
		showComp = new JButton("Show Computer");
		buttons = new JPanel(new GridLayout(1,2));
		main = new JPanel();
		noOfMoves = new JLabel("    No. of moves: 0");
		game = new JPanel(new GridLayout(1,2));
		newGameHuman = new JButton("Play Against Human");
		newGameComputer = new JButton("Play Against Computer");
		newGameHuman.setEnabled(false);
		
		for(int i=0; i<25; i++)
			playerButton[i] = new JButton();
		ApplyRandom.applyRandom(playerButton);
		
		showComp.addActionListener(new Show());
		newGameHuman.addActionListener(new AgainstHuman());
		newGameComputer.addActionListener(new AgainstComputer());
		
		for(int i=0; i<25; i++)
		{
			playerMain.add(playerButton[i]);
			playerButton[i].setEnabled(false);
		}
		
		showComp.setEnabled(false);
		
		addComponents();
	}
	
	private void addComponents()
	{
		buttons.add(showComp);
		buttons.add(noOfMoves);
		game.add(newGameHuman);
		game.add(newGameComputer);
		main.add(playerMain);
		main.add(buttons);
		playerFrame.add(BorderLayout.NORTH,game);
		playerFrame.add(BorderLayout.CENTER,playerMain);
		playerFrame.add(BorderLayout.SOUTH,buttons);
		playerFrame.setJMenuBar(getMenuBar());
		
		for(int i=0; i<25; i++)
		{
			playerButton[i].addActionListener(new PlayerListen());
		}
		
		playerFrame.setSize(350,350);
		playerFrame.setResizable(false);
		playerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		playerFrame.setVisible(true);
	}
	
	private JMenuBar getMenuBar()
	{
		menuBar = new JMenuBar();
		
		options = new JMenu("Options");
		about = new JMenu("About");
		
		playAgainstHuman = new JMenuItem("New Game Against Human");
		playAgainstComputer = new JMenuItem("New Game Against Computer");
		endGame = new JMenuItem("End Game");
		newGame = new JMenuItem("New Game");
		help = new JMenuItem("Help");
		developers = new JMenuItem("Developers");
		
		newGame.addActionListener(new NewGame());
		playAgainstComputer.addActionListener(new AgainstComputer());
		playAgainstHuman.addActionListener(new AgainstHuman());
		endGame.addActionListener(new EndGame());
		endGame.setEnabled(false);
		help.addActionListener(new Help());
		developers.addActionListener(new Developers());
		
		options.add(newGame);
		options.add(playAgainstHuman);
		options.add(playAgainstComputer);
		options.add(endGame);
		about.add(help);
		about.add(developers);
		menuBar.add(options);
		menuBar.add(about);
		
		return menuBar;
	}
	
	private void disable(int num)
	{
		for(int i=0; i<25; i++)
			if(Integer.parseInt(playerButton[i].getText()) == num)
			{
				playerButton[i].setEnabled(false);
				break;
			}
	}
	
	public class PlayerListen implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			boolean win = false;
			int networkMove;
			Check c = new Check();
			JButton but = (JButton)evt.getSource();
			int k = Integer.parseInt(but.getText());
			if(networkMatch)
			{
				if(noOfMoves.getText().equals("Your turn..."))
				{	
					but.setEnabled(false);
					noOfMoves.setText("Wait");
					System.out.println(noOfMoves.getText());
					net.playerMove(k);
					for(int i=0; i<noOfPlayers; i++)
					{
						System.out.println(i);
						networkMove = net.play(i);
						disable(networkMove);
						for(int j = i; j<noOfPlayers; j++)
						{
							win = net.winner(j);
							if(win)
							{
								JOptionPane.showMessageDialog(null,"PLayer");
							}
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "It's not your turn...!\nPlease wait for your turn!");
				}
				System.out.println("Done");
			}
			else
			{
				but.setEnabled(false);
				for(int i=0; i<noOfPlayers; i++)
					computers[i].playerMove(k);
				noOfMove++;
				noOfMoves.setText("    No. of moves: "+noOfMove);
				win = c.check(playerButton);
				if(win && status)
				{
					status = false;
					endGame.setEnabled(false);
					JOptionPane.showMessageDialog(null,"Player Wins!");
				}
				for(int i=0; i<noOfPlayers && status; i++)
				{
					win = computers[i].checkWinner();
					if(win && status)
					{
						status = false;
						endGame.setEnabled(false);
						JOptionPane.showMessageDialog(null,"Computer["+(i+1)+"] Wins");
						break;
					}
				}
				for(int i=0; i<noOfPlayers && status; i++)
				{	
					int move = computers[i].move();
					for(int j=0; j<25; j++)
					{
						if(move == Integer.parseInt(playerButton[j].getText()))
						{
							playerButton[j].setEnabled(false);
							break;
						}
					}
					for(int j=0; j<noOfPlayers; j++)
					{
						computers[j].computerMove(move);
					}
					for(int j=i; j<noOfPlayers;j++)
					{
						win = computers[i].checkWinner();
						if(win && status)
						{
							status = false;
							endGame.setEnabled(false);
							JOptionPane.showMessageDialog(null,"Computer["+(i+1)+"] wins!");
						}
					}
					win = c.check(playerButton);
					if(win && status)
					{
						status = false;
						endGame.setEnabled(false);
						JOptionPane.showMessageDialog(null,"Player Wins!");
					}
					for(int j=0; j<i; j++)
					{
						win = computers[i].checkWinner();
						if(win && status)
						{
							status = false;
							endGame.setEnabled(false);
							JOptionPane.showMessageDialog(null,"Computer["+(i+1)+"] wins!");
						}
					}
				}
			}
		}
	}
	
	public class Show implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JButton bt = (JButton)evt.getSource();
			if(bt.getText().equals("Hide Computer"))
			{
				for(int i=0; i<noOfPlayers; i++)
					computers[i].hide();
				bt.setText("Show Computer");
			}
			else
			{
				for(int i=0; i<noOfPlayers; i++)
					computers[i].show();
				bt.setText("Hide Computer");
			}
		}
	}
	public class AgainstComputer implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			boolean flag = true;
			String ans;
			if((endGame.isEnabled() == true) && status)
				endGame.doClick();
			if(check==1)
			{
				check = 2;
				return;
			}
			do
			{
				try
				{
					ans = JOptionPane.showInputDialog("Enter the number of computer players");
					noOfPlayers = Integer.parseInt(ans);
					if(noOfPlayers == 0)
					{
						noOfPlayers = 0;
						for(int i=0 ; i<25; i++)
							playerButton[i].setEnabled(false);
						showComp.setEnabled(false);
						flag = false;
					}
					flag = false;
				}
				catch(NumberFormatException e)
				{
					JOptionPane.showMessageDialog(null,"Please enter a valid number or 0 to exit!");
				}
			}while(flag);
			if(noOfPlayers>0)
			{
				status = true;
				endGame.setEnabled(true);
				ApplyRandom.applyRandom(playerButton);
				showComp.setText("Show Computer");
				computers = new Computer[noOfPlayers];
				for(int i=0; i<noOfPlayers; i++)
					computers[i] = new Computer(i);
				for(int i=0; i<25; i++)
				{
					playerButton[i].setEnabled(true);
				}
				showComp.setEnabled(true);
				noOfMoves.setText("    No. of moves: ");
			}
		}
	}
	public class AgainstHuman implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			chooseOption = new JFrame("Choose Your Position");
			JButton ok = new JButton("OK");
			ok.addActionListener(new Ok());
			JPanel pan = new JPanel();
			JLabel labe = new JLabel("Choose your position for the game");
			JLabel lab = new JLabel("NOTE: There can only be a single server for a game.");
			server = new JRadioButton("Act as a server for the game");
			socket = new JRadioButton("Connect to an existing server");
			socket.setSelected(true);
			
			ButtonGroup group = new ButtonGroup();
			group.add(server);
			group.add(socket);
			
			pan.add(labe);
			pan.add(server);
			pan.add(socket);
			pan.add(lab);
			pan.add(ok);
			
			chooseOption.add(pan);
			chooseOption.setSize(350,180);
			chooseOption.setVisible(true);
			chooseOption.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
	public class Ok implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			chooseOption.dispose();
			boolean flag;
			String s = "";
			if(server.isSelected())
			{	
				try
				{
					s = JOptionPane.showInputDialog(null, "Enter the number of players");
					noOfPlayers = Integer.parseInt(s);
					net = new Network(noOfPlayers);
					flag = net.connect();
					if(flag)
					{
						networkMatch = true;
						noOfMoves.setText("Your turn...");
						for(int i=0; i<25; i++)
							playerButton[i].setEnabled(true);
						showComp.setEnabled(false);
					}
				}
				catch(Exception e)
				{
					if( s.equals("") ||s.equals(Integer.toString(JOptionPane.YES_OPTION)))
						JOptionPane.showMessageDialog(null,"Please enter a valid numer!");
					return;
				}
			}
			else
			{
				Player player = new Player();
				flag = player.connect();
				if(flag)
					player.play();
			}
		}
	}
	public class EndGame implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			int option;
			if(status == true)
				option = JOptionPane.showConfirmDialog(null,"Are you sure you want to end this game?");
			else
				option = JOptionPane.YES_OPTION;
			if(option == JOptionPane.YES_OPTION)
			{
					for(int i=0; i<noOfPlayers; i++)
						computers[i].frame.dispose();
					endGame.setEnabled(false);
					for(int i=0; i<25; i++)
						playerButton[i].setEnabled(false);
					showComp.setEnabled(false);
			}
			else
				check = 1;
		}
	}
	public class NewGame implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			playAgainstComputer.doClick();
		}
		
	}
	public class Help implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JFrame helpFrame = new JFrame("Help For Bingo");
			JTextArea helpArea = new JTextArea(20,20);
			helpArea.setEditable(false);
			helpArea.setLineWrap(true);
			try
			{
				String s = "",temp;
				InputStream stream = getClass().getResourceAsStream("/Help.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				while(true)
				{
					temp = br.readLine();
					if(temp == null)
						break;
					s+=temp;
					s+="\n";
				}
				
				helpArea.setText(s);
				helpFrame.add(helpArea);
				helpFrame.setSize(300,310);
				helpFrame.setVisible(true);
				br.close();
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null, "Unable to get help! Please try later.");
			}
		}
	}
	public class Developers implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			URL url = this.getClass().getResource("Photo0059.jpg");
			ImageIcon c = new ImageIcon(url);
			String s = "Developed by: ANKIT\nConcept and Design: ANKIT\nCoded in: JAVA\nEmail ID: ankmonuati@gmail.com\nReport the bugs and suggestions to the above mentioned email-id.";
			s+= "\nI'll be extremely greateful for your invaluable suggestions and critics.";
			JOptionPane.showMessageDialog(null,s,"Developers",0, (Icon)c);
		}
	}
}
