import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

class UnitDatas extends JPanel
{
	JPanel NamePanel = new JPanel();
	JTextField UnitName; 
	JTextField UnitNum;

	JPanel picData = new JPanel();
	JLabel width = new JLabel("角色圖寬：0");
	JLabel height = new JLabel("圖高：0");

	JPanel AttributePanel = new JPanel();
	JTextField Str;
	JTextField Dex;
	JTextField Int;

	JPanel AttributeIncPanel = new JPanel();
	JTextField StrInc;
	JTextField DexInc;
	JTextField IntInc;

	JPanel ArmorPanel = new JPanel();
	JTextField Armor;
	JTextField MagicArmor;

	JPanel AtkPanel1 = new JPanel();
	JTextField BaseAtk;
	JTextField BulletNum;

	JPanel AtkPanel2 = new JPanel();
	JTextField AtkRange;
	JTextField DetectRange;

	JPanel CreepsPanel1 = new JPanel();
	JTextField HP;
	JTextField HPrate;

	JPanel CreepsPanel2 = new JPanel();
	JTextField MP;
	JTextField MPrate;

	JPanel CreepsPanel3 = new JPanel();
	JTextField AtkRate;

	JPanel CreepsPanel4 = new JPanel();
	JTextField Exp;
	JTextField Award;

	JPanel Button = new JPanel();
	JButton switchUnit = new JButton("編輯英雄單位");
	JButton save = new JButton("存檔");
	JButton clear = new JButton("編輯新檔");

	JPanel Warning = new JPanel();

	int UnitWidth = 0;
	int UnitHeight = 0;

	String[] HeroDatas = {"num", "Nodata", "name", "Nodata", "width", "Nodata"
						, "height", "Nodata", "speed", "Nodata", "baseAtk", "Nodata"
						, "bulletnum", "Nodata", "mainAttribute", "Nodata", "attribute", "Nodata"
						, "attributeInc", "Nodata", "manaArmor", "Nodata", "armor", "Nodata"
						, "skill", "Nodata", "range", "Nodata", "detectRange", "Nodata"};

	String[] CreepDatas = {"num", "Nodata", "name", "Nodata", "width", "Nodata"
						, "height", "Nodata", "speed", "Nodata", "baseAtk", "Nodata"
						, "bulletnum", "Nodata", "hp", "Nodata", "hpRate", "Nodata"
						, "atkRate", "Nodata", "mp", "Nodata", "mpRate", "Nodata"
						, "manaArmor", "Nodata", "armor", "Nodata", "skill", "Nodata"
						, "range", "Nodata", "detectRange", "Nodata", "exp", "Nodata"
						, "award", "Nodata"};

	UnitDatas()
	{
		Build();
		SwitchMode(CreepEditor.HeroMode);
		Action();
	}

	void Build()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));

		//單位名稱&編號
		NamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		NamePanel.add(new JLabel("名稱："));
		NamePanel.add(UnitName = new JTextField(12));
		NamePanel.add(new JLabel("編號："));
		NamePanel.add(UnitNum = new JTextField(3));
		add(NamePanel);

		//圖片大小
		picData.setLayout(new FlowLayout(FlowLayout.LEFT));
		picData.add(width);
		picData.add(new JLabel("/"));
		picData.add(height);
		add(picData);

		//黃十字初始值
		AttributePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		AttributePanel.add(new JLabel("初始黃十字(力/敏/智)："));
		AttributePanel.add(Str = new JTextField(3));
		AttributePanel.add(new JLabel("/"));
		AttributePanel.add(Dex = new JTextField(3));
		AttributePanel.add(new JLabel("/"));
		AttributePanel.add(Int = new JTextField(3));
		add(AttributePanel);

		//升級的增輻
		AttributeIncPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		AttributeIncPanel.add(new JLabel("黃十字每級增加(力/敏/智)："));
		AttributeIncPanel.add(StrInc = new JTextField(3));
		AttributeIncPanel.add(new JLabel("/"));
		AttributeIncPanel.add(DexInc = new JTextField(3));
		AttributeIncPanel.add(new JLabel("/"));
		AttributeIncPanel.add(IntInc = new JTextField(3));
		add(AttributeIncPanel);

		//(魔)防
		ArmorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		ArmorPanel.add(new JLabel("防禦："));
		ArmorPanel.add(Armor = new JTextField(3));
		ArmorPanel.add(new JLabel("魔防："));
		ArmorPanel.add(MagicArmor = new JTextField(3));
		add(ArmorPanel);

		//攻擊資料
		AtkPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		AtkPanel1.add(new JLabel("基礎攻擊力："));
		AtkPanel1.add(BaseAtk = new JTextField(5));
		AtkPanel1.add(new JLabel("使用的子彈編號："));
		AtkPanel1.add(BulletNum = new JTextField(5));
		add(AtkPanel1);

		//範圍偵測
		AtkPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		AtkPanel2.add(new JLabel("攻擊範圍："));
		AtkPanel2.add(AtkRange = new JTextField(5));
		AtkPanel2.add(new JLabel("偵測範圍："));
		AtkPanel2.add(DetectRange = new JTextField(5));
		add(AtkPanel2);

		CreepsPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));	
		CreepsPanel1.add(new JLabel("HP："));
		CreepsPanel1.add(HP = new JTextField(5));
		CreepsPanel1.add(new JLabel("每秒回復(0~1)："));
		CreepsPanel1.add(HPrate = new JTextField(5));
		add(CreepsPanel1);

		CreepsPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		CreepsPanel2.add(new JLabel("MP："));
		CreepsPanel2.add(MP = new JTextField(5));
		CreepsPanel2.add(new JLabel("每秒回復(0~1)："));
		CreepsPanel2.add(MPrate = new JTextField(5));
		add(CreepsPanel2);

		CreepsPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
		CreepsPanel3.add(new JLabel("攻擊速度(1~10)："));
		CreepsPanel3.add(AtkRate = new JTextField(8));
		add(CreepsPanel3);

		CreepsPanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
		CreepsPanel4.add(new JLabel("打倒後的經驗值："));
		CreepsPanel4.add(Exp = new JTextField(5));
		CreepsPanel4.add(new JLabel("獎勵金："));
		CreepsPanel4.add(Award = new JTextField(5));
		add(CreepsPanel4);

		Button.setLayout(new FlowLayout(FlowLayout.LEFT));
		Button.add(switchUnit);
		Button.add(save);
		Button.add(clear);
		add(Button);

	//	Warning.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("注意：                                                  "));
		add(new JLabel("切換至編輯英雄模式時                                    "));
		add(new JLabel("儲存的圖階會被強制縮放成50*50                           "));
		add(new JLabel("單純裁圖或其他非英雄單位時                              "));
		add(new JLabel("請小心避免悲劇發生                                      "));
		//add(Warning);

		setPreferredSize(new Dimension(350, 400));
	}

	void SwitchMode(boolean mode)
	{
		AttributePanel.setVisible(mode);
		AttributeIncPanel.setVisible(mode);

		CreepsPanel1.setVisible(!mode);
		CreepsPanel2.setVisible(!mode);
		CreepsPanel3.setVisible(!mode);
		CreepsPanel4.setVisible(!mode);
		
		if(mode)
		{
			switchUnit.setText("編輯其他單位");
			CreepEditor.jf.setTitle("編輯英雄");
		}

		else
		{
			switchUnit.setText("編輯英雄單位");
			CreepEditor.jf.setTitle("編輯其他單位");
		}
	}

	void saveDataCollect()
	{
		if(CreepEditor.HeroMode)
		{
			HeroDatas[1] = UnitNum.getText();
			HeroDatas[3] = UnitName.getText();
			HeroDatas[5] = "50";
			HeroDatas[7] = "50";
			HeroDatas[9] = "5";
			HeroDatas[11] = BaseAtk.getText();
			HeroDatas[13] = BulletNum.getText();

			int[] temp = {Integer.parseInt(Str.getText()), Integer.parseInt(Dex.getText()), Integer.parseInt(Int.getText())};

			int i = 1;
			int large = 0;
			for(int x = 0; x < 3; x++)
			{
				if(large < temp[x])
				{
					large = temp[x];
					i++;
				}
			}

			HeroDatas[15] = "" + i;
			HeroDatas[17] = Str.getText() + " " + Dex.getText() + " " + Int.getText();
			HeroDatas[19] = StrInc.getText() + " " + DexInc.getText() + " " + IntInc.getText();
			HeroDatas[21] = MagicArmor.getText();
			HeroDatas[23] = Armor.getText();
			HeroDatas[25] = "1 2 3 4";
			HeroDatas[27] = AtkRange.getText();
			HeroDatas[29] = DetectRange.getText();
		}

		else
		{
			CreepDatas[1] = UnitNum.getText();
			CreepDatas[3] = UnitName.getText();
			CreepDatas[5] = "" + CreepEditor.ue.pic.SizeX;
			CreepDatas[7] = "" + CreepEditor.ue.pic.SizeY;
			CreepDatas[9] = "5";
			CreepDatas[11] = BaseAtk.getText();
			CreepDatas[13] = BulletNum.getText();
			CreepDatas[15] = HP.getText();
			CreepDatas[17] = HPrate.getText();
			CreepDatas[19] = AtkRate.getText();
			CreepDatas[21] = MP.getText();
			CreepDatas[23] = MPrate.getText();
			CreepDatas[25] = MagicArmor.getText();
			CreepDatas[27] = Armor.getText();
			CreepDatas[29] = "0 0 0 0";
			CreepDatas[31] = AtkRange.getText();
			CreepDatas[33] = DetectRange.getText();
			CreepDatas[35] = Exp.getText();
			CreepDatas[37] = Award.getText();
		}
	}

	void Action()
	{
		switchUnit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				CreepEditor.HeroMode = !CreepEditor.HeroMode;
				SwitchMode(CreepEditor.HeroMode);
			}
		});

		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveDataCollect();
				try
				{
					File data;

					if(CreepEditor.HeroMode)
					{
						data = new File("./SaveData/Hero/" + UnitNum.getText() + ".txt");
					}
					
					else
					{	
						data = new File("./SaveData/Creeps/" + UnitNum.getText() + ".txt");
					}

					if(data.exists())
					{
						data.delete();
					}

					data.createNewFile();
					BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(data), "UTF-8"));
					
					if(CreepEditor.HeroMode)
					{
						for(String temp : HeroDatas)
						{
							w.write(temp);
							w.newLine();
							w.flush();
						}
					}	
					
					else
					{
						for(String temp : CreepDatas)
						{
							w.write(temp);
							w.newLine();
							w.flush();
						}
					}

					w.close();
				}
				
				catch(Exception ex){}
			}
		});

		clear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				HeroDatas[2] = HeroDatas[4] = HeroDatas[6] = HeroDatas[8]
			   	= HeroDatas[10] = HeroDatas[12] = HeroDatas[14] = HeroDatas[16]
			   	= HeroDatas[18] = HeroDatas[20] = HeroDatas[22] = HeroDatas[24]
			   	= HeroDatas[26] = HeroDatas[28] = HeroDatas[30] = "Nodata";

				CreepDatas[2] = CreepDatas[4] = CreepDatas[6] = CreepDatas[8]
			   	= CreepDatas[10] = CreepDatas[12] = CreepDatas[14] = CreepDatas[16]
			   	= CreepDatas[18] = CreepDatas[20] = CreepDatas[22] = CreepDatas[24]
			   	= CreepDatas[26] = CreepDatas[28] = CreepDatas[30] = CreepDatas[32]
			   	= CreepDatas[34] = CreepDatas[36] = CreepDatas[38] = "Nodata";
			
				UnitName.setText("");
				UnitNum.setText("" + Integer.parseInt(UnitNum.getText()));
				Str.setText("");
				Dex.setText("");
				Int.setText("");
				StrInc.setText("");
				DexInc.setText("");
				IntInc.setText("");
				Armor.setText("");
				MagicArmor.setText("");
				BaseAtk.setText("");
				BulletNum.setText("");
				AtkRange.setText("");
				DetectRange.setText("");
				HP.setText("");
				HPrate.setText("");
				MP.setText("");
				MPrate.setText("");
				AtkRate.setText("");
				Exp.setText("");
				Award.setText("");
			}
		});
	}
}

