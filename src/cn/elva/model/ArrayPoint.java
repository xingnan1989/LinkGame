package cn.elva.model;

public class ArrayPoint
{
	//表示行
	protected int i;
	//表示列
	protected int j;
	//表示值
	protected int value;
	
	public ArrayPoint( int i,int j,int value )
	{
		this.i =i;
		this.j =j;
		this.value = value;
	}

	public String toString( )
	{
		return i+","+j+","+value;
	}
	/**
	 * @return i
	 */
	public int getI()
	{
		return i;
	}

	/**
	 * @param i 要设置的 i
	 */
	public void setI(int i)
	{
		this.i = i;
	}

	/**
	 * @return j
	 */
	public int getJ()
	{
		return j;
	}

	/**
	 * @param j 要设置的 j
	 */
	public void setJ(int j)
	{
		this.j = j;
	}

	/**
	 * @return value
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * @param value 要设置的 value
	 */
	public void setValue(int value)
	{
		this.value = value;
	}
}
