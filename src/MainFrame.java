

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import cn.elva.Settings;
import cn.elva.model.Map;

public class MainFrame extends JFrame
{

	private static final long serialVersionUID = 1L;

	//炸弹的次数
	private int bombCount = Settings.BOMBCOUNT;
	
	private JPanel jContentPane = null;

	private JMenuBar menuBar = null;

	private JMenu fileMenu = null;

	private JMenuItem reloadItem = null;

	private JMenuItem startItem = null;
//炸弹
	private JMenuItem bombItem = null;
	
	private JMenuItem exitItem = null;

	private MapUI mapUI = null;
	// 游戏开始时间
	private long startTime;

	// 结束时间
	private long endTime;

	private Timer timer = null;

	// private JMenuItem ti
	private JMenuBar initMenuBar()
	{
		if (menuBar == null)
		{
			menuBar = new JMenuBar();
			fileMenu = new JMenu("文件");

			startItem = new JMenuItem("开始游戏");
			startItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					reload();
				}
			});
			reloadItem = new JMenuItem("重来一次");
			reloadItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					reload();
				}
			});

			bombItem = new JMenuItem("炸弹");
			bombItem.addActionListener(new ActionListener(){
				public void actionPerformed( ActionEvent e )
				{
					if( bombCount==0 )
					{
						JOptionPane.showMessageDialog(MainFrame.this,"你已经没有炸弹可用了!!!" );
						bombItem.setEnabled(false);
						return;
					}
					mapUI.bomb();
					bombCount--;
				}
			});
			exitItem = new JMenuItem("退出");
			exitItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});

			fileMenu.add(startItem);
			fileMenu.add(reloadItem);
			fileMenu.add( bombItem );
			fileMenu.add(exitItem);
			menuBar.add(fileMenu);
		}

		return menuBar;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// PAN_TODO 自动生成方法存根
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				MainFrame thisClass = new MainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public MainFrame()
	{
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(900, 900);
		this.setTitle("llk");
		this.setJMenuBar(initMenuBar());
		// this.setContentPane(getJContentPane());
		this.setTitle("连连看");
	}

	private void reload()
	{
		mapUI = new MapUI();
		startTime = System.currentTimeMillis() / 1000;

		endTime = startTime + Settings.PERTIME;

		// jContentPane.setVisible(true);
		jContentPane = new JPanel();
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(mapUI);

		this.setContentPane(jContentPane);
		this.validate();
		Map.LEFTCOUNT = Settings.ROWS * Settings.COLUMNS;
		initTimer();
		
		bombItem.setEnabled(true);
		bombCount=Settings.BOMBCOUNT;
	}

	private void initTimer()
	{
		ActionListener actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startTime = System.currentTimeMillis() / 1000;

				if (startTime == endTime)
				{
					JOptionPane.showMessageDialog(MainFrame.this, "时间到了!!");
					int result = JOptionPane.showConfirmDialog(MainFrame.this,
							"重玩一次?", "Again", JOptionPane.YES_NO_CANCEL_OPTION);
					if (result == JOptionPane.YES_OPTION)
					{
						reload();
					}
					else
					{
						jContentPane.setVisible(false);
						validate();
					}
				}
			}
		};
		timer = new javax.swing.Timer(1000, actionListener);
		timer.start();
	}
}
