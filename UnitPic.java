import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageFilter;
import java.awt.image.CropImageFilter;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.util.ArrayList;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Transparency;

class UnitPic extends JLabel
{
	BufferedImage image = null;
	Shape shape = null;
	ImageFilter cropFilter = null;
	ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();

	int SizeX = 50;
	int SizeY = 50;

	int directoryCount = 1;
	int GIFdirectoryCount = 1;
	
	boolean Clicked = false;
		
	Color defaultBackground = Color.BLACK;
	BufferedImage[][] bi;

	UnitPic()
	{
		Action();
	}
	
	void Action()
	{
		addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if(image != null)
				{
					defaultBackground = new Color(image.getRGB(e.getX(), e.getY()));
					CreepEditor.ue.info2.setText("背景資訊：R(" + defaultBackground.getRed() + "), G(" + defaultBackground.getGreen() + "), B(" + defaultBackground.getBlue() + ")");
					Clicked = true;
				}
			}
		});
	}

	void setImage(BufferedImage temp)
	{
		image = temp;

		if(temp != null)
		{
			SizeX = temp.getWidth();
			SizeY = temp.getHeight();
		}
			
		Clicked = false;

		defaultBackground = Color.BLACK;
		CreepEditor.ue.info2.setText("點擊圖片設定要透明化的部份");
		setIcon(new ImageIcon(image));
	}

	void ImageCrop(int column, int row)
	{
		SizeX = (int)(image.getWidth() / column);
		SizeY = (int)(image.getHeight() / row);

		CreepEditor.ud.width.setText("角色圖寬：" + SizeX);
		CreepEditor.ud.height.setText("圖高：" + SizeY);

		for(int startY = 0; startY < image.getHeight(); startY += SizeY)
		{
			for(int startX = 0; startX < image.getWidth(); startX += SizeX)
			{
				cropFilter = new CropImageFilter(startX, startY, SizeX, SizeY);
				Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));

				imageList.add(ImgToBuffered(img, SizeX, SizeY));
			}
		}

		ListoArray(column, row);
	}

	BufferedImage ImgToBuffered(Image temp, int width, int height)
	{
		BufferedImage buf;

		if(CreepEditor.HeroMode)
		{
			buf = new BufferedImage(50, 50, BufferedImage.TYPE_4BYTE_ABGR_PRE);
		}

		else
		{
			buf = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
		}

		Graphics2D g2d = buf.createGraphics();
		g2d.getDeviceConfiguration().createCompatibleImage(buf.getWidth(), buf.getHeight(), Transparency.TRANSLUCENT);

		if(!(CreepEditor.ue.trans.isSelected()))
		{
			g2d.setColor(defaultBackground);
			g2d.fillRect(0, 0, buf.getWidth(), buf.getHeight());
		}
		g2d.drawImage(temp, (int)((buf.getWidth() - width) / 2), (int)((buf.getHeight() - height) / 2), null);
		g2d.dispose();

		return buf;
	}

	void ListoArray(int c, int r)
	{
		bi = new BufferedImage[c][r];

		int il = 0;
		for(int y = 0; y < r; y++)
		{
			for(int x = 0; x < c; x++)
			{
				bi[x][y] = imageList.get(il);
				il++;
			}
		}

		imageList.clear();
	}

	void GIFexport(int column, int row, boolean left)
	{
		AnimatedGifEncoder age = new AnimatedGifEncoder();

		int gifCount = 1;
		String directory = "";

		if(CreepEditor.HeroMode)
		{
			directory += "./Move/Hero/" + GIFdirectoryCount;
		}
		
		else
		{
			directory += "./Move/Creep/" + GIFdirectoryCount;
		}

		try
		{
			File createDirecty = new File(directory);
			if(!createDirecty.exists())
			{
				createDirecty.mkdirs();
			}
		}

		catch(Exception ex){ex.printStackTrace();}


		if(left)
		{
			for(int y = 0; y < row; y++)
			{
				age.start(directory + "/" + gifCount + ".gif");
				age.setQuality(15);
				age.setRepeat(0);
				age.setDelay(250);
				age.setTransparent(defaultBackground);
				
				for(int x = 0; x < column; x++)
				{
					age.addFrame(bi[x][y]);
				}

				for(int back_x = (column - 2); back_x > 0; back_x--)
				{
					age.addFrame(bi[back_x][y]);
				}

				age.finish();
				gifCount++;
			}
		}
		
		else
		{
			for(int x = 0; x < column; x++)
			{
				age.start(directory + "/" + gifCount + ".gif");
				age.setQuality(15);
				age.setRepeat(0);
				age.setDelay(250);
				age.setTransparent(defaultBackground);
				
				for(int y = 0; y < row; y++)
				{
					age.addFrame(bi[x][y]);
				}

				for(int back_y = (row - 2); back_y > 0; back_y--)
				{
					age.addFrame(bi[x][back_y]);
				}

				age.finish();
				gifCount++;
			}
		}

		GIFdirectoryCount++;
	}

	void saveImage(int column, int row, boolean left, boolean hadTrans)
	{
		AnimatedGifEncoder age = new AnimatedGifEncoder();

		int pngCount = 1;
		String directory = "";

		if(CreepEditor.HeroMode)
		{
			directory += "./Stay/Hero/" + directoryCount;
		}
		
		else
		{
			directory += "./Stay/Creep/" + directoryCount;
		}

		try
		{
			File createDirecty = new File(directory);
			if(!createDirecty.exists())
			{
				createDirecty.mkdirs();
			}
		}

		catch(Exception ex){ex.printStackTrace();}

		if(left)
		{
			for(int y = 0; y < row; y++)
			{
				for(int x = 0; x < column; x++)
				{
					try
					{
						age.start(directory + "/" + pngCount + ".gif");
						age.setQuality(15);
						age.setTransparent(defaultBackground);
						age.addFrame(bi[x][y]);
						age.finish();

						File gifile = new File(directory + "/" + pngCount + ".gif");
						BufferedImage tempImg = ImageIO.read(gifile);
					}

					catch(Exception e){e.printStackTrace();}
					pngCount++;
				}
			}
		}
		
		else
		{
			for(int x = 0; x < column; x++)
			{
				for(int y = 0; y < row; y++)
				{
					try
					{
						age.start(directory + "/" + pngCount + ".gif");
						age.setQuality(15);
						age.setTransparent(defaultBackground);
						age.addFrame(bi[x][y]);
						age.finish();

						File gifile = new File(directory + "/" + pngCount + ".gif");
						BufferedImage tempImg = ImageIO.read(gifile);
					}

					catch(Exception e){e.printStackTrace();}
					pngCount++;
				}
			}
		}

		directoryCount++;
	}

	//固定JLabel的形狀
	public boolean contains(int x, int y)
	{
		if(shape == null || !(shape.getBounds().equals(getBounds())))
		{
			shape = new Rectangle2D.Double(0, 0, SizeX, SizeY);
		}

		return shape.contains(x, y);
	}
}

