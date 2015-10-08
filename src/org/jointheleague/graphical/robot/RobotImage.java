package org.jointheleague.graphical.robot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

public class RobotImage {
	private static final int IMG_WIDTH = 100;
	private static final int IMG_HEIGHT = 100;
	private static final int MINI_SCALE = 4;
	private static final int MINI_WIDTH = IMG_WIDTH / MINI_SCALE;
	private static final int MINI_HEIGHT = IMG_HEIGHT / MINI_SCALE;
	
	private boolean isMini;
	
	private Pixel[] pix;
	private Pixel[] miniPix;
	
	public int x;
	public int y;
	
	public RobotImage(int ix, int iy, String fileName)
	{
		isMini = false;
		
		x = ix;
		y = iy;
		pix = new Pixel[IMG_WIDTH * IMG_HEIGHT];
		miniPix = new Pixel[IMG_WIDTH * IMG_HEIGHT];
		
		int ctr = 0;
		for(int i = 0; i < IMG_WIDTH; i++)
		{
			for(int j = 0; j < IMG_HEIGHT; j++)
			{
				pix[ctr] = new Pixel(i, j);
				miniPix[ctr++] = new Pixel(i , j);
			}
		}
		
		loadDefaultRobot(fileName);
		loadMiniBot();
	}
	
	public void draw(Graphics2D g)
	{
		for(int i = 0; i < pix.length; i++)
		{
			int dx = ((int)pix[i].x + x) - (IMG_WIDTH / 2);
			int dy = ((int)pix[i].y + y) - (IMG_HEIGHT / 2);		
			
			if(!isMini)
			{
				g.setColor(pix[i].getColor());
			}
			else
			{
				g.setColor(miniPix[i].getColor());
			}
			
			g.drawRect(dx, dy, 1, 1);
		}
	}
	
	public void rotate(int angle)
	{
		for(int i = 0; i < pix.length; i++)
		{
			pix[i].rotateAroundPoint(angle, IMG_WIDTH / 2, IMG_HEIGHT / 2);
		}
	}
	
	public void rotateAroundPoint(int angle, int px, int py)
	{
		for(int i = 0; i < pix.length; i++)
		{
			pix[i].rotateAroundPoint(angle, px, py);
		}
	}
	
	public void setPixel(int index, int x, int y, Color c)
	{
		pix[index] = new Pixel(x, y, c);
	}
	
	private void loadDefaultRobot()
	{
		loadDefaultRobot("rob");
	}
	
	void loadDefaultRobot(String s)
	{
		s += ".robi";
		
		try
		{
			InputStream is = this.getClass().getResourceAsStream(s);
			int fileSize = Util.getFileSize(is);
			is.close();
			
			is = this.getClass().getResourceAsStream(s);
			byte[] buffer = Util.readToBuffer(is, fileSize);
			is.close();
			
			loadPixels(buffer);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("RobotImage: loadDefaultRobot");
			loadDefaultRobot();
		}
	}
	
	public void changeRobot(String fileName)
	{
		fileName += ".robi";
		try 
		{
			FileInputStream fis =  new FileInputStream(fileName);
			int fileSize = Util.getFileSize(fis);
			fis.close();
			
			fis = new FileInputStream(fileName);
			byte[] buffer = Util.readToBuffer(fis, fileSize);
			fis.close();
			
			loadPixels(buffer);
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("RobotImage: changeRobot");
			loadDefaultRobot();
		}
		
		loadMiniBot();
	}
	
	private void loadPixels(byte[] buf)
	{
		try
		{
			int ctr = 0;
			
			for(int i = 0; i < buf.length;)
			{
				byte[] num = new byte[4];
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int x = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int y = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int r = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int g = ByteBuffer.wrap(num).getInt();
				
				num[0] = buf[i++];
				num[1] = buf[i++];
				num[2] = buf[i++];
				num[3] = buf[i++];
				
				int b = ByteBuffer.wrap(num).getInt();
				
				Color c;
				
				if(r == 220 &&
				   g == 220 &&
				   b == 220)
				{
					c = new Color (0, 0, 0, 0);
				}
				else
				{
					c = new Color(r, g, b);
				}
				
				setPixel(ctr++, x, y, c);			
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "There was an error loading your file.");
			System.out.println("RobotImage: loadPixels");
		}
	}
	
	public void miniturize()
	{
		isMini = true;
	}
	
	public void expand()
	{
		isMini = false;
	}
	
	private void loadMiniBot()
	{
		Color[][] colorList = new Color[IMG_WIDTH][IMG_HEIGHT];
		Color[][] miniColor = new Color[MINI_WIDTH][MINI_HEIGHT];
		Color[] colorAvg = new Color[MINI_SCALE * MINI_SCALE];
		
		for(int i = 0; i < pix.length; i++)
		{
			colorList[pix[i].getX()][pix[i].getY()] = pix[i].getColor();
		}
		
		int ctr = 0;
		
		for(int i = 0; i < IMG_WIDTH; i += MINI_SCALE)
		{
			for(int j = 0; j < IMG_HEIGHT; j += MINI_SCALE)
			{
				for(int row = 0; row < MINI_SCALE; row++)
				{
					for(int col = 0; col < MINI_SCALE; col++)
					{
						colorAvg[ctr++] = colorList[i + row][j + col];
					}
				}
				
				Color c = Util.getAverageColor(colorAvg);
				miniColor[i / MINI_SCALE][j / MINI_SCALE] = c;
				ctr = 0;
			}
		}
		
		ctr = 0;
		for(int i = 0; i < miniPix.length; i++)
		{
			if(pix[i].getX() > 38 && pix[i].getX() < 63 &&
			   pix[i].getY() > 38 && pix[i].getY() < 63)
			{
				miniPix[i] = new Pixel(pix[i].getX(), pix[i].getY(), miniColor[pix[i].getX() - 38][pix[i].getY() - 38]);
			}
			else
			{
				miniPix[i] = new Pixel((int)pix[i].getX(), (int)pix[i].getY(), new Color(0, 0, 0, 0));
			}
			
		}
	}
}
