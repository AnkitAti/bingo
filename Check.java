import javax.swing.JButton;

public class Check {
	public boolean check(JButton[] button)
	{
		int points = 0;
		boolean flag = true;
		for(int i=0; i<5; i++)
		{
			for(int j=5*i; j<5*i+5; j++)
			{
				if(button[j].isEnabled() == true)
				{
					flag = false;
					break;
				}
			}
			if(flag)
				points++;
			else
				flag = true;
		}
		for(int i=0; i<5; i++)
		{
			for(int j=i; j<25; j+=5)
			{
				if(button[j].isEnabled() == true)
				{
					flag = false;
					break;
				}
			}
			if(flag)
				points++;
			else
				flag = true;
		}
		for(int j=0; j<25; j+=6)
		{
			if(button[j].isEnabled() == true)
			{
				flag = false;
				break;
			}
		}
		if(flag)
			points++;
		else
			flag = true;
		for(int j = 4; j<21; j+=4)
		{
			if(button[j].isEnabled() == true)
			{
				flag = false;
				break;
			}
		}
		if(flag)
			points++;
		else
			flag = true;
		if(points>=5)
			return true;
		else
			return false;
	}
}
