package kyodai;

import java.awt.*;
import javax.swing.*;

/**
 * 处理时间的类
 */
public class ClockAnimate extends JPanel
	implements Runnable {

	private volatile Thread thread;
	long startTime = 0l; //开始时间
	long usedTime = 0l; //使用时间

	Color color = new Color(212, 255, 200);
	Font font48 = new Font("serif", Font.PLAIN, 28);
	java.text.DecimalFormat df = new java.text.DecimalFormat("000");
	java.text.DecimalFormat df2 = new java.text.DecimalFormat("0");

	public ClockAnimate() {
		this.setMinimumSize(new Dimension(156, 48));
		this.setPreferredSize(new Dimension(156, 48));
	}

	/**
	 * 时间的绘制
	*/
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Dimension d = getSize();
		g2.setBackground(new Color(111, 146, 212));
		g2.clearRect(0, 0, d.width, d.height);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		g2.setFont(font48);

		g2.drawString("时间:" + getTime(), 16, 40);
	}

	/**
	 * 取得使用时间格式化后的字符串
	*/
	String getTime() {
		int sec, ms;
		long time;
		time = usedTime;
		sec = Math.round(time / 1000);
		time -= sec * 1000;
		ms = Math.round(time / 100);
		return (df.format(sec) + "." + df2.format(ms));
	}

	public void start() {
		startTime = System.currentTimeMillis();
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		Thread currentThread = Thread.currentThread();
		while (thread == currentThread) {
			long time = System.currentTimeMillis();
			usedTime = time - startTime;
			try {
				repaint();
				thread.sleep(100l);
			}
			catch (InterruptedException ex) {
			}
		}
	}

	public void stop() {
		if (thread != null) {
			thread = null;
		}
	}

	/**
	 * 取得用户使用的时间
	*/
	public int getUsedTime() {
		return Math.round(usedTime / 1000);
	}

}