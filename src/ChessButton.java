

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import cn.elva.Settings;
import cn.elva.model.ArrayPoint;

public class ChessButton extends JButton
{
	// 按钮所对应的数组中的值和位置,用ArrayPoint结构来表示
	protected ArrayPoint point = null;

	/**
	 * 构造函数,指定按钮所代表的值和位置
	 * 
	 * @param row
	 *              所在行号
	 * @param col
	 *              所在列号
	 * @param value
	 *              代表的值
	 */
	public ChessButton(int row, int col, int value)
	{
		this(new ArrayPoint(row, col, value));
	}

	/**
	 * 构造函数
	 * 
	 * @param point
	 *              值和位置的数据结构
	 */
	public ChessButton(ArrayPoint point)
	{
		this.point = point;
		String name ="Resource/"+point.getValue() + Settings.RELEX;
		URL url = ChessButton.class.getResource(name);
//		System.out.println(url);
		ImageIcon icon = new ImageIcon( url );
		this.setIcon(icon);
	}

	/**
	 * 构造函数,使用默认值
	 */
	public ChessButton()
	{
		this(new ArrayPoint(0, 0, 0));
	}

	/**
	 * 返回当前按钮代表的位置和值
	 * 
	 * @return point
	 */
	public ArrayPoint getPoint()
	{
		return point;
	}

	/**
	 * 设置此按钮所代表的位置和值
	 * 
	 * @param point
	 *              要设置的 point
	 */
	public void setPoint(ArrayPoint point)
	{
		this.point = point;
		
	}
}

