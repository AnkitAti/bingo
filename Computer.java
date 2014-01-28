import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Computer {
	JButton[] button;
	JFrame frame;
	JPanel panel;
	Check status;
	public Computer(int num)
	{
		button = new JButton[25];
		frame = new JFrame("Computer["+(num+1)+"]");
		panel = new JPanel(new GridLayout(5,5));
		status = new Check();
		
		for(int i=0; i<25; i++)
			button[i] = new JButton();
		
		boolean flag = false;
		for(int i=0; i<25; i++)
		{
			int k =(int)(Math.random()*25) + 1;
			for(int j=0; j<i; j++)
			{
				try
				{	
					if(k == Integer.parseInt(button[j].getText()))
					{				
						flag = true;
						break;
					}
				}
				catch(NumberFormatException e)
				{
					flag = false;
				}
			}
			if(flag)
			{
				i--;
				flag = false;
				continue;
			}
			button[i].setText(""+k+"");
		}
		
		for(int i=0; i<25; i++)
			panel.add(button[i]);
		
		frame.add(panel);
		frame.setSize(300,300);
	}
	public void show()
	{
		this.frame.setVisible(true);
	}
	public void hide()
	{
		this.frame.setVisible(false);
	}
	public boolean checkWinner()
	{
		return this.status.check(this.button);
	}
	public void playerMove(int button)
	{
		for(int i=0; i<25; i++)
		{
			if(Integer.parseInt(this.button[i].getText()) == button)
				this.button[i].setEnabled(false);
		}
	}
	public void computerMove(int button)
	{
		for(int i=0; i<25; i++)
		{
			if(Integer.parseInt(this.button[i].getText()) == button)
			{
				this.button[i].setEnabled(false);
				break;
			}
		}
	}
	public int move()
	{
		if(button[12].isEnabled() == true)
			return Integer.parseInt(button[12].getText());
		int max = 0,buttonMove=0;
		for(int i=0; i<25; i+=6)
			if(button[i].isEnabled()==true)
			{
				max++;
				buttonMove = i;
			}
		if(max==1)
			return Integer.parseInt(button[buttonMove].getText());
		max = 0;
		for(int i=4; i<21; i+=4)
			if(button[i].isEnabled()==true)
			{
				max++;
				buttonMove = i;
			}
		if(max==1)
			return Integer.parseInt(button[buttonMove].getText());
		for(int i=0; i<5; i++)
		{	
			max = 0;
			for(int j=i; j<25; j+=5)
			{
				if(button[j].isEnabled()==true)
				{
					max++;
					buttonMove = j;
				}
			}
			if(max==1)
				return Integer.parseInt(button[buttonMove].getText());
			max = 0;
			for(int j=i*5; j<i*5+5; j++)
			{
				if(button[j].isEnabled()==true)
				{
					max++;
					buttonMove = j;
				}
			}
			if(max==1)
					return Integer.parseInt(button[buttonMove].getText());
		}
		max=0;
		buttonMove = 0;
		for(int i=0; i<25; i++)
		{
			if(button[i].isEnabled()==false)
				continue;
			int rowNumber = (i/5)*5;
			int colNumber = i%5;
			int tempMax=0;
			for(int j=rowNumber; j<rowNumber+5; j++)
				if(button[j].isEnabled()==false)
					tempMax++;
			for(int j=colNumber; j<colNumber+21; j+=5)
				if(button[j].isEnabled()==false)
					tempMax++;
			if(i%6 == 0)
			{
				for(int j=0; j<25; j+=6)
					if(button[j].isEnabled()==false)
						tempMax++;
			}
			if(i%4==0)
			{
				for(int j=0; j<21; j+=4)
					if(button[j].isEnabled()==false)
						tempMax++;
			}
			if(tempMax > max)
			{
				max = tempMax;
				buttonMove = i;
			}
		}
		return Integer.parseInt(button[buttonMove].getText());
	}
}
