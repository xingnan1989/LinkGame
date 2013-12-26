package kyodai.map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import kyodai.*;

/**
 * 生成图形用户界面
 */

public class MapUI extends JPanel
	implements ActionListener, Runnable {

	private Map map;
	private JButton[] dots;

	private Point lastPoint = new Point(0, 0); //上一个点的坐标
	private boolean isSelected = false; //是否已经选择了一个点

	private int score = 0; //记录用户的得分
	private ClockAnimate clockAnimate; //同步显示时钟
	//AnimateDelete animate; //动画
	JButton goTop10;

	private ScoreAnimate scoreAnimate;
	int stepScore = 0; //计算距离的分
	int limitTime = 0; //限定寻找的时间(秒)

	private boolean isPlaying = false; //当前是否正在游戏中

	/**
	 * 构造函数
	 */
	public MapUI(Map map, JButton[] dots) {
		this.map = map;
		this.dots = dots;

		GridLayout gridLayout = new GridLayout();
		this.setLayout(gridLayout);
		gridLayout.setRows(Setting.ROW);
		gridLayout.setColumns(Setting.COLUMN);
		gridLayout.setHgap(2);
		gridLayout.setVgap(2);
		this.setLayout(gridLayout);
		this.setBackground(Kyodai.DarkColor);

		for (int row = 0; row < Setting.ROW; row++) {
			for (int col = 0; col < Setting.COLUMN; col++) {
				int index = row * Setting.COLUMN + col;
				dots[index].addActionListener(this);
				this.add(dots[index]);
			}
		}
	}
	
	/**
	 * 设置地图
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * 获取主界面上的goTop10按钮，以便操作
	*/
	public void setTop10Button(JButton goTop10) {
		this.goTop10 = goTop10;
	}

	/**
	 * 根据数组来绘置画面
	*/
	private void paint() {
		for (int row = 0; row < Setting.ROW; row++) {
			for (int col = 0; col < Setting.COLUMN; col++) {
				int index = row * Setting.COLUMN + col;
				if (map.getMap()[row][col] > 0) {
					dots[index].setIcon(Kyodai.BlocksIcon[map.getMap()[row][col] - 1]);
					dots[index].setEnabled(true);
				}
				else {
					dots[index].setIcon(null);
					dots[index].setEnabled(false);
				}
			}
		}
	}

	public void repaint(Graphics g) {
		paint();
	}

	/**
	 * 判断当前是否已经没有可消除的方块
	*/
	private boolean validMap(Point a) {
		if (map.getCount() == 0) {
			return true;
		}

		Line line = new Line(0, new Point(), new Point());
		map.setTest(true); //只测试
		line = map.findNext(a);
		int offset = 0;
		if (line.direct == 1) { //找到了可消除的
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 更新当前显示的分数
	*/
	private void showScore(int l, int c) {
		if (scoreAnimate == null) {
			return;
		}
		scoreAnimate.setScore(l, c);
	}

	/**
	 * 刷新当前的排列方式
	*/
	public void refresh() {
		if (!isPlaying) { //不在游戏中,返回
			return;
		}
		if (map.getCount() == 0) {
			Kyodai.showHint("还刷，都没了！");
		}
		if (Setting.Sound == 1) {
			new Sound(Sound.REFRESH);
		}

		if (validMap(new Point( -1, -1))) {
			score -= Setting.freshScore;
			showScore(score - 1, score);
		}
		else {
			showScore(score, score + Setting.freshScore);
			score += Setting.freshScore;
		}

		score -= Setting.freshScore;
		showScore(score - 1, score);

		map.refresh();
		paint();
	}

	/**
	 * 消除两个点
	*/
	void earse(Point a, Point b) {
		//paint();

		int offset;
		offset = a.x * Setting.COLUMN + a.y;
		dots[offset].setIcon(null);
		dots[offset].setEnabled(false);

		offset = b.x * Setting.COLUMN + b.y;
		dots[offset].setIcon(null);
		dots[offset].setEnabled(false);

		//如果地图清除完成，关闭
		if (map.getCount() == 0) {
			int remainTime = limitTime - clockAnimate.getUsedTime();
			message("剩余时间 = " + remainTime);
			if (remainTime > 0) {
				showScore(score, score + remainTime * Setting.timeScore);
				score += remainTime * Setting.timeScore;
			}
			isPlaying = false;
			stop();
			Kyodai.showHint("时间+ " + remainTime * Setting.timeScore + "，想看看你的排名吗？");
			goTop10.setEnabled(true);
		}
		else {
			//test1(map.getDeleteArray());
		}
	}

	/**
	 * 自动寻找最佳答案
	*/
	public void findNext(Point a) {
		if (!isPlaying) { //不在游戏中,返回
			return;
		}
		if (map.getCount() == 0) {
			Kyodai.showHint("你找昏了头吧，没了！");
			return;
		}
		Line line = new Line(0, new Point(), new Point());
		map.setTest(true); //告诉map当前只是测试，并不需要进行删除动画
		line = map.findNext(a);
		int offset = 0;
		if (line.direct == 1) { //找到了可消除的
			if (Setting.Sound == 1) {
				new Sound(Sound.HINT);
			}
			offset = line.a.x * Setting.COLUMN + line.a.y;
			dots[offset].setBorder(Kyodai.Hint);
			offset = line.b.x * Setting.COLUMN + line.b.y;
			dots[offset].setBorder(Kyodai.Hint);
			score -= Setting.hintScore;
			showScore(score - 1, score);
		}
		else {
			Kyodai.showHint("找不到，请刷新");
		}
	}

	/**
	 * 自动找出并消除地图上的两个点
	*/
	public boolean bomb(Point a, boolean showMessage) {
		if (!isPlaying) { //不在游戏中,返回
			return false;
		}
		if (map.getCount() == 0) {
			Kyodai.showHint("你炸昏了头吧，没了！");
			return false;
		}
		Line line = new Line(0, new Point(), new Point());
		map.setTest(false);
		line = map.findNext(a);
		int offset = 0;
		if (line.direct == 1) { //找到了可消除的
			if (Setting.Sound == 1) {
				new Sound(Sound.BOMB);
			}
			offset = line.a.x * Setting.COLUMN + line.a.y;
			dots[offset].setBorder(Kyodai.unSelected);
			offset = line.b.x * Setting.COLUMN + line.b.y;
			dots[offset].setBorder(Kyodai.unSelected);
			map.earse(line.a, line.b);
			earse(line.a, line.b);

			score -= Setting.bombScore;
			showScore(score - 1, score);
			return true;
		}
		else {
			if (showMessage) {
				Kyodai.showHint("炸弹用不了，请刷新！");
			}
			return false;
		}
	}

	private void message(String str) {
		Kyodai.showHint(str);
	}

	/**
	 * 自动游戏
	*/
	public void autoPlay() {
		if (!isPlaying) { //不在游戏中,返回
			return;
		}
		//如果使用该功能，不计时间分
		limitTime = 0;

		while (map.getCount() > 0) {
			if (bomb(new Point( -1, -1), false)) {
				message("炸弹使用成功！");
			}
			else {
				message("找不到可用点，刷新……");
				refresh();
			}
		}
	}

	/**
	 * 获取系统的计分板
	*/
	public void setScore(ScoreAnimate score) {
		this.scoreAnimate = score;
	}

	/**
	 * 获取系统的计时板
	 */
	public void setClock(ClockAnimate clock) {
		this.clockAnimate = clock;
	}

	/**
	 * 事件处理
	 */
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		int offset = Integer.parseInt(button.getActionCommand());
		int row, col;
		row = Math.round(offset / Setting.COLUMN);
		col = offset - row * Setting.COLUMN;
		//如果上面没有图片
		if (map.getMap()[row][col] < 1) {
			return;
		}
		//选择时的声音
		if (Setting.Sound == 1) {
			new Sound(Sound.SELECT);
		}

		if (isSelected) {
			message("上次已经选择了一个点");
			message("上次选择点的坐标为： " + lastPoint.x + ", " + lastPoint.y +
					" 值为： " + map.getMap()[lastPoint.x][lastPoint.y] +
					" 位移为： " + (lastPoint.x * Setting.COLUMN + lastPoint.y));

			//是上次选择的点
			if (lastPoint.x == row && lastPoint.y == col) { 
				message("这次选择的点和上次的是同一点，取消选择状态");
				button.setBorder(Kyodai.unSelected);
				isSelected = false;
			}
			else {
				//判断是否可以消除
				message("这次选择的点和上次的点并不相同");
				Point current = new Point(row, col);
				message("这次选择的点的坐标为： " + row + ", " + col + " 值为： " +
						map.getMap()[row][col] +
						" 位移为： " + (row * Setting.COLUMN + col));
				map.setTest(false);
				if (map.test(lastPoint, current)) {
					message("两点可以消除，执行消除");
					//消除前先取消当前选择点的边框，因为有可能是提示
					dots[row * Setting.COLUMN + col].setBorder(Kyodai.unSelected);

					map.earse(current, lastPoint);
					earse(current, lastPoint);
					dots[lastPoint.x * Setting.COLUMN +
						lastPoint.y].setBorder(Kyodai.unSelected);
					lastPoint = new Point(0, 0);
					isSelected = false;

					showScore(score, score + Setting.correctScore + stepScore);
					score += Setting.correctScore + stepScore;

					if (Setting.Sound == 1) {
						new Sound(Sound.EARSE);
					}
				}
				else {
					message("这次选择的点与上次选择的点无解，改变选择为当前点");
					dots[lastPoint.x * Setting.COLUMN +
						lastPoint.y].setBorder(Kyodai.unSelected);
					button.setBorder(Kyodai.Selected);
					lastPoint.x = row;
					lastPoint.y = col;
					isSelected = true;

					score -= Setting.wrongScore;
					showScore(score - 1, score);
				}
			}
		}
		else {
			message("上次并未选择的点，置当前点为选择点");
			message("当前点坐标为： " + row + ", " + col + " 值为： "
					+map.getMap()[row][col]
					+" 位移为： " + (row * Setting.COLUMN + col));
			button.setBorder(Kyodai.Selected);
			lastPoint.x = row;
			lastPoint.y = col;
			isSelected = true;
		}
	}

	/**
	 * 加密当前的分数
	*/
	public String encode() {
		if (score < 0) {
			Kyodai.showHint("负分还想有排名？下次努力吧！");
		}

		int level, f1, f2;
		level = Setting.LevelIndex;
		f1 = (score - 102) * (score - 102);
		f2 = (1978 - score) * (1978 - score);
		return "s=" + score + "&l=" + level + "&f1=" + f1 + "&f2=" + f2;
	}

	public void start() {
		goTop10.setEnabled(false);

		isPlaying = true;
		limitTime = map.getCount() * Setting.limitScore;
		message("时限 = " + limitTime);
		paint();
	}

	public void run() {

	}

	public void stop() {
		clockAnimate.stop();
	}

}