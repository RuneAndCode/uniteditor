import javax.swing.JFrame;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DnDConstants;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class CreepEditor
{
	static JFrame jf = new JFrame();
	static DropImage Imagedrop = new DropImage();
	static UnitDatas ud = new UnitDatas();
	static UnitEdit ue = new UnitEdit();	

	static boolean HeroMode = false;

	public static void main (String [] args)
	{
		ue.setDropTarget(new DropTarget(ue, DnDConstants.ACTION_COPY_OR_MOVE, Imagedrop, true));
		
		jf.add(ue, BorderLayout.CENTER);
		jf.add(ud, BorderLayout.EAST);
		
		jf.setSize(800, 550);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	static void UpdateUI()
	{

	}
}

