package kyodai;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.border.*;

import kyodai.map.*;
import kyodai.topbar.*;

/**
 * 连连看主类
 */
public class Kyodai extends JFrame
	implements ActionListener {

	public static Color DarkColor = new Color(55, 77, 118); //暗色
	public static Color LightColor = new Color(111, 146, 212); //亮色
	public static ImageIcon[] BlocksIcon = new ImageIcon[39]; //游戏中方块的图标
	public static ImageIcon GuideIcon; //连线的图标
	public static Border unSelected = BorderFactory.createLineBorder(DarkColor, 1); //未选中时的边框
	public static Border Selected = BorderFactory.createLineBorder(Color.white, 1); //选中后的边框
	public static Border Hint = BorderFactory.createLineBorder(Color.green, 1); //提示的边框

	Dimension faceSize = new Dimension(780, 500);
	Image icon;
	private int counter = 0;

	JPanel toolBar = new JPanel(); //工具栏
	JPanel actionPanel = new JPanel(); //用户操作栏
	JPanel contentPanel = new JPanel(); //容器
	JPanel statusPanel = new JPanel(); //状态栏
	Border emptyBorder = BorderFactory.createEmptyBorder(); //未选中时的边框
	JButton startButton = new JButton(); //"开始"
	JButton refreshButton = new JButton(); //"刷新"
	JButton hintButton = new JButton(); //"提示"
	JButton bombButton = new JButton(); //"炸弹"
	JButton demoButton = new JButton(); //"演示"

	JButton setupButton = new JButton(); //设置
	JButton helpButton = new JButton(); //帮助
	JButton aboutButton = new JButton(); //关于
	JButton goTop10 = new JButton("Go top 10");
	HelpDialog helpDialog; //帮助对话框
	AboutDialog aboutDialog; //关于对话框

	public static JTextField statusField = new JTextField(
		"欢迎使用宝石连连看");
	ImageIcon imgStart, imgHint, imgRefresh, imgBomb, imgDemo;
	ImageIcon imgSetup, imgHelp, imgAbout;

	JButton[] dots = new JButton[Setting.ROW * Setting.COLUMN];
	Setting setting = new Setting();

	MapUI ui;
	Map map;
	ClockAnimate clock = new ClockAnimate(); //时钟
	ScoreAnimate score = new ScoreAnimate(); //分数
	AnimateDelete animateDelete = new AnimateDelete(dots);
	Music music = new Music();

	public Kyodai() {
		initResource();//初始化系统所需要的资源
		map = new Map();
		ui = new MapUI(map, dots);
		initUI();//初始化用户界面
		ui.setScore(score);
		ui.setClock(clock);
		ui.setTop10Button(goTop10);
		animateDelete.setSpeed(setting.Animate);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(faceSize);
		//设置运行时窗口的位置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - faceSize.getWidth()) / 2,
						 (int) (screenSize.height - faceSize.getHeight()) / 2);
		this.setResizable(false);
		this.setTitle("宝石连连看"); //设置标题
		this.setIconImage(icon); //设置程序图标

		//设置动画光标
		URLClassLoader urlLoader = (URLClassLoader)this.getClass().getClassLoader();
		URL url = urlLoader.findResource("images/cursor.gif");
		Image animateImage = new ImageIcon(url).getImage();
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
			animateImage, new Point(0, 0), "cursor");
		this.setCursor(cursor);
		this.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					setting.save();
				}
			}
		);

		if (setting.Music == 1) {
			music.play();
		}
	}

	/**
	 * 初始化系统所需要的资源
	*/
	public void initResource() {
		URLClassLoader urlLoader = (URLClassLoader)this.getClass().getClassLoader();
		URL url;

		//程序图标
		icon = getImage("images/kyodai16.gif");

		for (int i = 0; i < BlocksIcon.length; i++) {
			BlocksIcon[i] = new ImageIcon(getImage("images/" + (i + 1) + ".gif"));
		}

		imgStart = new ImageIcon(getImage("images/start.gif"));
		imgRefresh = new ImageIcon(getImage("images/refresh.gif"));
		imgHint = new ImageIcon(getImage("images/hint.gif"));
		imgBomb = new ImageIcon(getImage("images/bomb.gif"));
		imgDemo = new ImageIcon(getImage("images/demo.gif"));

		imgSetup = new ImageIcon(getImage("images/setup.gif"));
		imgHelp = new ImageIcon(getImage("images/help.gif"));
		imgAbout = new ImageIcon(getImage("images/about.gif"));

		GuideIcon = new ImageIcon(getImage("images/dots.gif"));

		//初始化方块
		for (int i = 0; i < dots.length; i++) {
			dots[i] = new JButton();
			dots[i].setActionCommand("" + i);
			dots[i].setBorder(unSelected);
			dots[i].setBackground(DarkColor);
		}

		//读取用户设置
		setting.load();

		//初始化对话框
		helpDialog = new HelpDialog(this); //帮助对话框
		aboutDialog = new AboutDialog(this); //关于对话框
	}

	/**
	 * 初始化用户界面
	*/
	public void initUI() {
		//界面整体布局
		Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
			new Color(45, 92, 162),
			new Color(43, 66, 97),
			new Color(45, 92, 162),
			new Color(84, 123, 200));
		BorderLayout borderLayout = new BorderLayout();
		toolBar.setBackground(DarkColor);
		toolBar.setBorder(border);
		toolBar.setPreferredSize(new Dimension(780, 48));
		toolBar.setMinimumSize(new Dimension(780, 48));
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		actionPanel.setBackground(LightColor);
		actionPanel.setBorder(border);
		actionPanel.setPreferredSize(new Dimension(160, 380));
		actionPanel.setMinimumSize(new Dimension(160, 380));
		contentPanel.setBackground(DarkColor);
		contentPanel.setBorder(border);
		contentPanel.setPreferredSize(new Dimension(620, 380));
		contentPanel.setMinimumSize(new Dimension(620, 380));

		statusPanel.setBackground(DarkColor);
		statusPanel.setBorder(border);
		statusPanel.setPreferredSize(new Dimension(620, 24));
		statusPanel.setMinimumSize(new Dimension(620, 24));
		statusPanel.setLayout(new BorderLayout());

		this.getContentPane().setLayout(borderLayout);
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(actionPanel, BorderLayout.EAST);
		this.getContentPane().add(contentPanel, BorderLayout.CENTER);
		this.getContentPane().add(statusPanel, BorderLayout.SOUTH);

		//加入地图
		contentPanel.add(ui);

		//加入计分
		actionPanel.add(score);

		//加入开始按钮
		actionPanel.add(startButton);
		startButton.setBorder(emptyBorder);
		startButton.setIcon(imgStart);
		startButton.addActionListener(this);

		//加入刷新按钮
		actionPanel.add(refreshButton);
		refreshButton.setBorder(emptyBorder);
		refreshButton.setIcon(imgRefresh);
		refreshButton.addActionListener(this);

		//加入提示按钮
		actionPanel.add(hintButton);
		hintButton.setBorder(emptyBorder);
		hintButton.setIcon(imgHint);
		hintButton.addActionListener(this);

		//加入炸弹按钮
		actionPanel.add(bombButton);
		bombButton.setBorder(emptyBorder);
		bombButton.setIcon(imgBomb);
		bombButton.addActionListener(this);

		//加入自动演示
		actionPanel.add(demoButton);
		demoButton.setBorder(emptyBorder);
		demoButton.setIcon(imgDemo);
		demoButton.addActionListener(this);

		//加入设置
		toolBar.add(setupButton);
		setupButton.setBorder(emptyBorder);
		setupButton.setIcon(imgSetup);
		setupButton.addActionListener(this);

		//加入帮助
		toolBar.add(helpButton);
		helpButton.setBorder(emptyBorder);
		helpButton.setIcon(imgHelp);
		helpButton.addActionListener(this);

		//加入关于
		toolBar.add(aboutButton);
		aboutButton.setBorder(emptyBorder);
		aboutButton.setIcon(imgAbout);
		aboutButton.addActionListener(this);

		//加入时钟
		actionPanel.add(clock);

		//加入状态栏
		statusPanel.add(statusField, BorderLayout.CENTER);
		statusField.setBorder(unSelected);
		statusField.setEditable(false);
		statusField.setForeground(Color.white);
		statusField.setBackground(DarkColor);

		//加入发送按钮
		statusPanel.add(goTop10, BorderLayout.EAST);
		goTop10.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		goTop10.setForeground(Color.white);
		goTop10.setBackground(DarkColor);
		goTop10.setFont(new Font("Serif", Font.PLAIN, 11));
		goTop10.addActionListener(this);
		goTop10.setEnabled(false);
	}

	public static void showHint(String str) {
		statusField.setText(str);
	}

	/**
	 * 通过给定的文件名获得图像
	*/
	Image getImage(String filename) {
		URLClassLoader urlLoader = (URLClassLoader)this.getClass().
			getClassLoader();
		URL url = null;
		Image image = null;
		url = urlLoader.findResource(filename);
		image = Toolkit.getDefaultToolkit().getImage(url);
		MediaTracker mediatracker = new MediaTracker(this);
		try {
			mediatracker.addImage(image, 0);
			mediatracker.waitForID(0);
		}
		catch (InterruptedException _ex) {
			image = null;
		}
		if (mediatracker.isErrorID(0)) {
			image = null;
		}

		return image;
	}

	/**
	 * 事件处理
	*/
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == startButton) { //开始
			map = new Map(Setting.Level[setting.LevelIndex]);
			ui.setMap(map);
			ui.start();
			clock.start();
			score.setScore( -1, 0);
		}
		else if (obj == refreshButton) { //刷新
			ui.refresh();
		}
		else if (obj == hintButton) { //提示
			ui.findNext(new Point( -1, -1));
		}
		else if (obj == bombButton) { //炸弹
			ui.bomb(new Point( -1, -1), true);
		}
		else if (obj == demoButton) { //自动演示
			ui.autoPlay();
		}
		else if (obj == aboutButton) { //关于
		  aboutDialog.show();
		}
		else if (obj == helpButton) { //帮助
			//new Help();
			helpDialog.show();
		}
		else if (obj == setupButton) { //设置
			SetupDialog setupDialog = new SetupDialog(this); //设置对话框
			setupDialog.show();

			if (setting.Music == 1) {
				music.play();
			}
			else {
				music.stop();
			}

			animateDelete.setSpeed(setting.Animate);
		}
		else if (obj == goTop10) { //排名
			String name = JOptionPane.showInputDialog(this, "请留下大名:", "过眼云烟");
			if (!"".equals(name.trim())) { //如果留了名字
				System.out.println("ui.encode()="+ui.encode());
				new Top10(this, "nickname=" + name + "&" + ui.encode());
				goTop10.setEnabled(false);
			}
		}
	}

	public static void main(String args[]) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		Kyodai kyodai = new Kyodai();
		kyodai.show();
	}
}
