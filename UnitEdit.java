import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.FlowLayout;

class UnitEdit extends JPanel
{
	UnitPic pic = new UnitPic();

	JLabel info1 = new JLabel("拖放圖片至此");

	JPanel picData = new JPanel();	
	JLabel info2 = new JLabel("點擊圖片設定要透明化的部份");
	
	JPanel messege1 = new JPanel();

	JLabel Width = new JLabel("寬：切");
	JTextField width_split = new JTextField(5);
	JLabel split1 = new JLabel("等份");

	JLabel Height = new JLabel("高：切");
	JTextField height_split = new JTextField(5);
	JLabel split2 = new JLabel("等份");

	JCheckBox gif = new JCheckBox("製作GIF");

	JPanel messege2 = new JPanel();
	JRadioButton left_to_right = new JRadioButton("由左至右", true);
	JRadioButton up_to_down = new JRadioButton("由上至下");
	
	JCheckBox trans = new JCheckBox("原圖已去背");

	JButton start = new JButton("開始並儲存");

	int default_Width = 120;
	int default_Height = 500;

	UnitEdit()
	{
		Build();
		editMode(false);
		Action();
	}

	void Build()
	{
		setLayout(new FlowLayout(FlowLayout.CENTER));
		add(pic);
		add(info1);
		messege1.add(Width);
		messege1.add(width_split);
		messege1.add(split1);
	
		messege1.add(Height);
		messege1.add(height_split);
		messege1.add(split2);

		messege1.add(gif);
		add(messege1);

		picData.add(info2);
		add(picData);

		messege2.add(new JLabel("命名順序(包含GIF製作的動作方向)："));
		messege2.add(left_to_right);
		messege2.add(up_to_down);
		add(messege2);

		ButtonGroup group = new ButtonGroup();
		group.add(left_to_right);
		group.add(up_to_down);

		add(trans);
		add(start);

		setPreferredSize(new Dimension(450, 500));
	}

	void editMode(boolean edit)
	{
		pic.setVisible(edit);

		info1.setVisible(!edit);
		
		picData.setVisible(edit);
		messege1.setVisible(edit);
		messege2.setVisible(edit);
		
		trans.setVisible(edit);
		start.setVisible(edit);
	}

	void Action()
	{
		start.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int width_split_data = Integer.parseInt(width_split.getText());
				int height_split_data = Integer.parseInt(height_split.getText());

				pic.ImageCrop(width_split_data, height_split_data);
				
				if(gif.isSelected())
				{
					pic.GIFexport(width_split_data, height_split_data, left_to_right.isSelected());
				}
				
				pic.saveImage(width_split_data, height_split_data, left_to_right.isSelected(), trans.isSelected());
			}
		});
	}

	void ImportImage(BufferedImage temp)
	{
		if(temp != null)
		{
			editMode(true);
			pic.setImage(temp);
		}
	}
}

