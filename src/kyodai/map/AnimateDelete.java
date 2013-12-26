package kyodai.map;

import java.awt.*;
import javax.swing.*;

import kyodai.*;

/**
 * 消除连连看方块的类
 */
public class AnimateDelete implements Runnable {
	static JButton[] dots;
	static long delay = 20l;
	int[] array = new int[44]; //最大距离只可能为2行1列
	private int count = 0;
	private volatile Thread thread;

	public AnimateDelete(JButton[] dots) {
		this.dots = dots;
		array = new int[0];
	}

	/**
	 * 初始化
	*/
	public AnimateDelete(int direct, Point a, Point b) {
		initArray();
		calcTwoPoint(direct, a, b);
		start();
	}

	/**
	 * direct 方向
	 * 1表示a, b在同一直线上，b, c在同一竖线上；
	 * 0表示a, b在同一竖线上，b, c在同一直线上
	 */
	public AnimateDelete(int direct, Point a, Point b, Point c) {
		initArray();

		if (direct == 1) { //先横后竖
			calcTwoPoint(1, a, b);
			count--;
			calcTwoPoint(0, b, c);
		}
		else {
			calcTwoPoint(0, a, b);
			count--;
			calcTwoPoint(1, b, c);
		}
		start();
	}

	/**
	 * direct 方向
	 * 1表示a, b为横线，b, c为竖线, c, d为横线
	 * 0表示a, b为竖线，b, c为横线，c, d为竖线
	*/
	public AnimateDelete(int direct, Point a, Point b, Point c, Point d) {
		initArray();

		if (direct == 1) { //横、竖、横方式处理
			calcTwoPoint(1, a, b);
			count--;
			calcTwoPoint(0, b, c);
			count--;
			calcTwoPoint(1, c, d);
		}
		else { //竖、横、竖方式处理
			calcTwoPoint(0, a, b);
			count--;
			calcTwoPoint(1, b, c);
			count--;
			calcTwoPoint(0, c, d);
		}
		start();
	}

	/**
	 * 计算消除的两点
	*/
	private void calcTwoPoint(int direct, Point a, Point b) {
		int offset = 0;
		if (direct == 1) { //横向连接
			if (a.y > b.y) { //a点向b点是从右向左在水平线上消除
				for (int y = a.y; y >= b.y; y--) {
					offset = a.x * Setting.COLUMN + y;
					array[count] = offset;
					count++;
				}
			}
			else { //a点向b点是从左向右在水平线上消除
				for (int y = a.y; y <= b.y; y++) {
					offset = a.x * Setting.COLUMN + y;
					array[count] = offset;
					count++;
				}
			}
		}
		else { //竖向连接
			if (a.x > b.x) { //a点向b点是从下向上垂直消除
				for (int x = a.x; x >= b.x; x--) {
					offset = x * Setting.COLUMN + a.y;
					array[count] = offset;
					count++;
				}
			}
			else { //a点向b点是从上向下垂直消除
				for (int x = a.x; x <= b.x; x++) {
					offset = x * Setting.COLUMN + a.y;
					array[count] = offset;
					count++;
				}
			}
		}
	}

	/**
	 * 设置动画速度
	*/
	public void setSpeed(int speed) {
		delay = speed * 10;
	}

	private void initArray() {
		if (array == null || array.length == 0) {
			return;
		}
		for (int i = 0; i < array.length; i++) {
			array[i] = -1;
		}
	}

	public void test() {
		if (array == null || array.length == 0) {
		return;
		}

		for (int i = 0; i < array.length; i++) {
			if (array[i] != -1) {
				message("[" + array[i] + "]  ");
			}
		}
		System.out.println();
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		if (count < 2) {
			return;
		}

		Thread currentThread = Thread.currentThread();
		boolean animate = true;
		while (thread == currentThread && animate) {
			for (int i = 1; i < count - 1; i++) {
				dots[array[i]].setEnabled(true);
				dots[array[i]].setIcon(Kyodai.GuideIcon);
				try {
					thread.sleep(delay);
				}
				catch (InterruptedException ex) {
				}
			}

			for (int i = 1; i < count - 1; i++) {
				dots[array[i]].setIcon(null);
				dots[array[i]].setEnabled(false);
				try {
					thread.sleep(delay);
				}
				catch (InterruptedException ex) {
				}
			}

			dots[array[0]].setIcon(null);
			dots[array[0]].setEnabled(false);
			dots[array[count - 1]].setIcon(null);
			dots[array[count - 1]].setEnabled(false);

			animate = false;
		}

		stop();
	}

	public void stop() {
		if (thread != null) {
			thread = null;
		}
	}

	void message(String str) {
		System.out.println(str);
	}
}