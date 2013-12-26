package kyodai.map;

import java.awt.Point;

/**
 * 定义直线的类
*/
public class Line{

	public Point a;
	public Point b;
	public int direct;

	public Line(){
	}

	/**
	 * 通过两点和方向构造直线
	*/
	public Line(int direct, Point a, Point b){
		this.direct = direct;
		this.a = a;
		this.b = b;
	}
}
