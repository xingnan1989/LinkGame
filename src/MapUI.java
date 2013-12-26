import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.elva.Settings;
import cn.elva.model.ArrayPoint;
import cn.elva.model.Map;

public class MapUI extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;

	//private int bombCount = Settings.BOMBCOUNT;
	// 棋子数组,用按钮来表示
	private ChessButton[] chesses = null;

	// 数据模型
	private Map map = new Map();

	// 判断当前点击的棋子是否是第二次选中的
	private boolean two = false;

	// 前面点的那个棋子
	private ArrayPoint priviousPoint;

	// 第二次选中的棋子
	private ArrayPoint currPoint;

	/**
	 * 构造函数
	 */
	public MapUI()
	{
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 * 
	 * @return void
	 */
	private void initialize()
	{
		initChesses();
		GridLayout gridLayout = new GridLayout(Settings.ROWS + 2,
				Settings.COLUMNS + 2);
		gridLayout.setHgap(2);
		gridLayout.setVgap(2);
		this.setLayout(gridLayout);
		this.setSize(300, 200);
		// 放置按钮,按行
		for (int row = 0; row < Settings.ROWS + 2; row++)
		{
			for (int col = 0; col < Settings.COLUMNS + 2; col++)
			{
				add(chesses[row * (Settings.COLUMNS + 2) + col]);
			}
		}
	}

	private void initChesses()
	{
		int[][] values = map.getMap();
		// 初始化棋子,和数据模型里保持一样
		this.chesses = new ChessButton[(Settings.ROWS + 2)
				* (Settings.COLUMNS + 2)];

		for (int row = 0; row < 10; row++)
		{
			for (int col = 0; col < 10; col++)
			{
				// 通过二维的数据模型坐标得到一维的棋子坐标
				int index = row * (Settings.COLUMNS + 2) + col;
				// 对棋子的数据模型,即ArrayPoint对象进行设置,指定此棋子具体的位置和值
				chesses[index] = new ChessButton(row, col, values[row][col]);
				// 添加监听器
				chesses[index].addActionListener(this);
				// 将外围的一圈设为不可见,行,列为0 和为最大值的情况
				if (row == 0 || row == (Settings.ROWS + 2 - 1) || col == 0
						|| col == (Settings.COLUMNS + 2 - 1))
				{
					chesses[index].setVisible(false);
				}
			}
		}
	}

	public void clearCheese(ArrayPoint priviousPoint, ArrayPoint currPoint)
	{
		// 处理匹配,看两点中否联通
		
			// 获得数据模型中的数组
			int[][] values = map.getMap();
			// 将模型中对应的棋子设为0
			values[priviousPoint.getI()][priviousPoint.getJ()] = 0;
			values[currPoint.getI()][currPoint.getJ()] = 0;

			// 使两个已经消除的按钮不可见
			int index1 = priviousPoint.getI() * (Settings.COLUMNS + 2)
					+ priviousPoint.getJ();
			int index2 = currPoint.getI() * (Settings.COLUMNS + 2)
					+ currPoint.getJ();
			chesses[index1].setVisible(false);
			chesses[index2].setVisible(false);
			

			// 如果棋子总数已为0,则程序结束
			if (map.LEFTCOUNT == 0)
			{
				
				JOptionPane.showMessageDialog(this, "恭喜您通过!!");
			}
	}

	/**
	 * 事件监听器处理函数,也是处理棋子消除的地方
	 */
	public void actionPerformed(ActionEvent e)
	{
		// 获得当前的柜子
		ChessButton button = (ChessButton) e.getSource();
		// 获得当前棋子的数据结构
		ArrayPoint p = button.getPoint();

		// 如果已有两个棋子选中, 则进行判断操作
		if (two)
		{
			currPoint = p;
			if( map.match(this.priviousPoint, this.currPoint))
			{
				clearCheese(this.priviousPoint, this.currPoint);
			}
			
			// 设置为没有两个按钮的选中的状态
			two = false;
		}
		else
		{
			// 将当前点击的棋子赋给变量priviousPoint
			this.priviousPoint = p;
			// 标志位设为TRUE,用于点击下个棋子的时候使用
			two = true;
		}

	}

	/**
	 * 炸弹的功能
	 * 
	 */
	public void bomb()
	{
		int[][] values = map.getMap();
		ArrayPoint p1 = null;
		ArrayPoint p2 = null;

		for (int row = 1; row < Settings.ROWS + 1; row++)
		{
			for (int col = 1; col < Settings.COLUMNS + 1; col++)
			{
				if (values[row][col] != 0)
				{
					p1 = new ArrayPoint(row, col, values[row][col]);
					
					for (int i = 1; i < Settings.ROWS + 1; i++)
					{
						for (int j = 1; j < Settings.COLUMNS + 1; j++)
						{
							if (values[i][j] != 0)
							{
								p2 = new ArrayPoint(i, j, values[i][j]);
							}
							else
							{
								continue;
							}

							//System.out.println(p1 + "|" + p2);

							if (map.match(p1, p2))
							{
								clearCheese(p1, p2);
								return;
							}
						}
					}
				}
			}
		}	
	}
}
