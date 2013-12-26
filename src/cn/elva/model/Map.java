package cn.elva.model;

import java.util.Random;

import cn.elva.Settings;

/**
 * @author AXL 数据部分,一个内部数组来表示页面上的具体情况
 */
public class Map
{
	public static int LEFTCOUNT = Settings.ROWS * Settings.COLUMNS;

	// 让其最外层的数据不显示,可以解决边框消除不掉的情况
	private int[][] map = new int[Settings.ROWS + 2][Settings.COLUMNS + 2];

	// private ArrayPoint prePoint;

	// private ArrayPoint currPoint;

	// 出现的不同图片个数
	private int maxKinds = 4;

	public Map()
	{
		init();
	}

	public int[][] getMap()
	{
		return map;
	}

	/**
	 * 实现了二维数组的产生
	 */
	private void init()
	{
		int[] tempArr = new int[Settings.ROWS * Settings.COLUMNS];
		int len = tempArr.length;

		// 根据图片的种类数来确定数组大小,如有64张图片,每四个为一样的,则需要图片数为64/4=16
		for (int i = 0; i < len / maxKinds; i++)
		{
			tempArr[i * 4] = i + 1;
			tempArr[i * 4 + 1] = i + 1;
			tempArr[i * 4 + 2] = i + 1;
			tempArr[i * 4 + 3] = i + 1;
		}

		// 打乱一维数组内数据的排列
		random(tempArr);

		// 填充到二维数组中
		for (int i = 1; i < Settings.ROWS + 1; i++)
		{
			for (int j = 1; j < Settings.COLUMNS + 1; j++)
			{
				this.map[i][j] = tempArr[(i - 1) * Settings.COLUMNS
						+ (j - 1)];
			}
		}
	}

	/**
	 * 将数组进行重排,使其成无序状态
	 * 
	 * @param array
	 *              要重排的数组
	 */
	private void random(int[] array)
	{
		Random random = new Random();
		int len = array.length;
		for (int i = len; i > 0; i--)
		{
			int j = random.nextInt(i);
			int temp = array[i - 1];
			array[i - 1] = array[j];
			array[j] = temp;
		}
	}

	/**
	 * 判断是否在一条直线上,这里不去比较两者值是否相等,主要用于后面两个拐点的情况
	 * 
	 * @param p1
	 *              之前的点
	 * @param p2
	 *              当前所点的点
	 * @return true 相通,false 不通
	 */
	public boolean oneLineWithoutValue(ArrayPoint p1, ArrayPoint p2)
	{
		if (horizonMatch(p1, p2))
		{
			return true;
		}
		else if (verticalMatch(p1, p2))
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断是否在一条直线上,其中包括了垂直和水平两种情况
	 * 
	 * @param p1
	 *              之前的点
	 * @param p2
	 *              当前所点的点
	 * @return true 相通,false 不通
	 */
	public boolean oneLine(ArrayPoint p1, ArrayPoint p2)
	{
		
		if (p1.value != p2.value)
		{
			return false;
		}
		if (oneLineWithoutValue(p1, p2))
		{
			return true;
		}
		return false;
	}

	/**
	 * 竖线上的判断
	 * 
	 * @param p1
	 *              之前的点
	 * @param p2
	 *              当前所点的点
	 * @return true 相通,false 不通
	 */
	public boolean verticalMatch(ArrayPoint p1, ArrayPoint p2)
	{
		if (p1.j != p2.j)
		{
			return false;
		}
		if (p1.i > p2.i)
		{
			ArrayPoint temp = null;
			temp = p1;
			p1 = p2;
			p2 = temp;
		}
		// 之间的相隔的棋子数
		int spaceCount = p2.i - p1.i;

		// 如果相邻,直接消除
		if (spaceCount == 1 && p1.value == p2.value)
		{
			return true;
		}

		for (int i = p1.i + 1; i < p2.i; i++)
		{
			ArrayPoint point = new ArrayPoint(i, p1.j, map[i][p1.j]);
			// 只要其值为0就表示没有棋子
			if (point.value != 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 水平方向的判断
	 * 
	 * @param p1
	 *              之前所选的点
	 * @param p2
	 *              当前选中的点
	 * @return true 相通,false 不通
	 */
	public boolean horizonMatch(ArrayPoint p1, ArrayPoint p2)
	{
		// 如果行行号不等的话,刚不是水平方向的结果
		if (p1.i != p2.i)
		{
			return false;
		}
		if (p1.j > p2.j)
		{
			ArrayPoint temp = null;
			temp = p1;
			p1 = p2;
			p2 = temp;
		}
		// 水平相隔棋子数
		int spaceCount = p2.j - p1.j;
		// 如果相邻,直接消除
		if (spaceCount == 1 && p1.value == p2.value)
		{
			return true;
		}
		for (int i = p1.j + 1; i < p2.j; i++)
		{
			ArrayPoint p = new ArrayPoint(p1.i, i, map[p1.i][i]);
			if (p.value != 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断前后点击的是不是同一个点,i,j相同就是一个点
	 * 
	 * @param p1
	 *              之前所选的点
	 * @param p2
	 *              当前选中的点
	 * @return true 同一个点,false 不同点
	 */
	private boolean isSameOne(ArrayPoint p1, ArrayPoint p2)
	{
		if (p1.i == p2.i && p1.j == p2.j)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 带一个拐点的情况,但不比较值的大小是否相等
	 * 
	 * @param p1
	 *              初始点
	 * @param p2
	 *              结束点
	 * @return true 相通,false 不通
	 */
	public boolean oneConnerWithoutValue(ArrayPoint p1, ArrayPoint p2)
	{
		// 获取P1水平方向的拐点
		ArrayPoint p1H = new ArrayPoint(p1.i, p2.j, map[p1.i][p2.j]);
		// 获取P1垂直方向的拐点
		ArrayPoint p1V = new ArrayPoint(p2.i, p1.j, map[p2.i][p1.j]);

		if (horizonMatch(p1, p1H) && (p1H.value == 0))
		{
			if (this.verticalMatch(p1H, p2))
			{
				return true;
			}
		}

		if (verticalMatch(p1, p1V) && (p1V.value == 0))
		{
			if (horizonMatch(p1V, p2))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 带一个拐点的情况
	 * 
	 * @param p1
	 *              初始点
	 * @param p2
	 *              结束点
	 * @return true 相通,false 不通
	 */
	public boolean oneConner(ArrayPoint p1, ArrayPoint p2)
	{
		// 获取P1水平方向的拐点
		ArrayPoint p1H = new ArrayPoint(p1.i, p2.j, map[p1.i][p2.j]);
		// 获取P1垂直方向的拐点
		ArrayPoint p1V = new ArrayPoint(p2.i, p1.j, map[p2.i][p1.j]);

		if (p1.value != p2.value)
		{
			return false;
		}

		if (oneConnerWithoutValue(p1, p2))
		{
			return true;
		}
		return false;
	}

	/**
	 * 带两个拐点的情况,分四个方向走,上下左右,只要一个方向通过,即通过
	 * 
	 * @param p1
	 *              初始点
	 * @param p2
	 *              结束点
	 * @return true 相通,false 不通
	 */
	public boolean twoConner(ArrayPoint p1, ArrayPoint p2)
	{
		if (p1.value != p2.value)
		{
			return false;
		}

		int count = 0;
		ArrayPoint temp = null;

		// 左
		for (int col = p1.j - 1; col >= 0; col--)
		{
			temp = new ArrayPoint(p1.i, col, map[p1.i][col]);
			if ((temp.value == 0) && this.oneLineWithoutValue(p1, temp))
			{
				if (this.oneConnerWithoutValue(temp, p2))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		} // 右

		count = Settings.COLUMNS + 2 - p1.j;
		for (int col = p1.j; col < Settings.COLUMNS + 2; col++)
		{
			temp = new ArrayPoint(p1.i, col, map[p1.i][col]);
			if ((temp.value == 0) && oneLineWithoutValue(p1, temp))
			{
				if (oneConnerWithoutValue(temp, p2))
				{
					return true;
				}
			}
		}
		// 上
		count = p1.i;
		for (int row = count - 1; row >= 0; row--)
		{
			temp = new ArrayPoint(row, p1.j, map[row][p1.j]);
			if ((temp.value == 0) && oneLineWithoutValue(p1, temp))
			{
				if (oneConnerWithoutValue(temp, p2))
				{
					return true;
				}
			}
		}
		// 下
		for (int row = p1.i + 1; row < Settings.ROWS + 2; row++)
		{
			temp = new ArrayPoint(row, p1.j, map[row][p1.j]);
			if ((temp.value == 0) && oneLineWithoutValue(p1, temp))
			{
				if (oneConnerWithoutValue(temp, p2))
				{
					return true;
				}
			}
		}
		//
		return false;
	}

	/**
	 * 判断两点是否匹配,包括直线,一个拐点,两个拐点三种情况
	 * 
	 * @param p1
	 *              初始点
	 * @param p2
	 *              结束点
	 * @return true 相通,false 不通
	 */
	public boolean match(ArrayPoint p1, ArrayPoint p2)
	{
		if (this.isSameOne(p1, p2))
		{
			return false;
		}

		if (oneLine(p1, p2))
		{
			map[p1.i][p1.j] = 0;
			map[p2.i][p2.j] = 0;
			LEFTCOUNT -= 2;
			return true;
		}
		if (oneConner(p1, p2))
		{
			map[p1.i][p1.j] = 0;
			map[p2.i][p2.j] = 0;
			LEFTCOUNT -= 2;
			return true;
		}
		if (twoConner(p1, p2))
		{
			map[p1.i][p1.j] = 0;
			map[p2.i][p2.j] = 0;
			LEFTCOUNT -= 2;
			return true;
		}
		return false;
	}
	
	
}
