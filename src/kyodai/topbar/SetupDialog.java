package kyodai.topbar;

import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import kyodai.*;

/**
 * 生成设置对话框的类
 */
public class SetupDialog extends JDialog {
	Border border1;
	JPanel PanelClose = new JPanel();
	JButton Close = new JButton();
	Border border2;
	JPanel PanelCenter = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JPanel PanelMusic = new JPanel();
	JPanel PanelEffect = new JPanel();
	JPanel PanelLevel = new JPanel();
	JPanel PanelAnimate = new JPanel();
	JRadioButton MusicOn = new JRadioButton();
	JRadioButton MusicOff = new JRadioButton();
	JLabel Music = new JLabel();
	JPanel PanelLeft = new JPanel();
	JPanel PanelRight = new JPanel();
	JLabel Effect = new JLabel();
	JRadioButton EffectOn = new JRadioButton();
	JRadioButton EffectOff = new JRadioButton();
	JLabel Level = new JLabel();
	JRadioButton Level1 = new JRadioButton();
	JRadioButton Level2 = new JRadioButton();
	JRadioButton Level3 = new JRadioButton();
	JRadioButton Level4 = new JRadioButton();
	JRadioButton Level5 = new JRadioButton();
	FlowLayout flowLayout1 = new FlowLayout();
	FlowLayout flowLayout2 = new FlowLayout();
	FlowLayout flowLayout3 = new FlowLayout();
	JLabel AnimateSpeed = new JLabel();
	FlowLayout flowLayout4 = new FlowLayout();
	JRadioButton Speed1 = new JRadioButton();
	JRadioButton Speed8 = new JRadioButton();
	JRadioButton Speed2 = new JRadioButton();
	JRadioButton Speed4 = new JRadioButton();
	JPanel PanelTop = new JPanel();
	ButtonGroup MusicGroup = new ButtonGroup();
	ButtonGroup EffectGroup = new ButtonGroup();
	ButtonGroup LevelGroup = new ButtonGroup();
	ButtonGroup SpeedGroup = new ButtonGroup();

	public SetupDialog(JFrame frame) {
		super(frame, true);
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//设置运行位置，使对话框居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - 400) / 2,
						(int) (screenSize.height - 320) / 2);
		this.setResizable(false);
	}

	private void jbInit() throws Exception {
		border2 = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(
				Color.lightGray, 1), BorderFactory.createEmptyBorder(2, 10, 2, 10));
		this.setSize(new Dimension(400, 320));
		this.setTitle("设置");
		border1 = BorderFactory.createEmptyBorder();

		PanelClose.setBackground(Color.white);
		Close.setBackground(Color.white);
		Close.setBorder(border2);
		Close.setText("保存");
		//事件处理
		Close.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//背景音乐的修改
					if (MusicOn.isSelected()) {
						Setting.Music = 1;
					}
					else {
						Setting.Music = 0;
					}
					//音乐特效的修改
					if (EffectOn.isSelected()) {
						Setting.Sound = 1;
					}
					else {
						Setting.Sound = 0;
					}
					//困难等级的修改
					if (Level1.isSelected()) {
						Setting.LevelIndex = 0;
					}
					if (Level2.isSelected()) {
						Setting.LevelIndex = 1;
					}
					if (Level3.isSelected()) {
						Setting.LevelIndex = 2;
					}
					if (Level4.isSelected()) {
						Setting.LevelIndex = 3;
					}
					if (Level5.isSelected()) {
						Setting.LevelIndex = 4;
					}
					//消除方块速度的修改
					if (Speed1.isSelected()) {
						Setting.Animate = 1;
					}
					if (Speed2.isSelected()) {
						Setting.Animate = 2;
					}
					if (Speed4.isSelected()) {
						Setting.Animate = 4;
					}
					if (Speed8.isSelected()) {
						Setting.Animate = 8;
					}

					dispose();
				}
			}
		);
		//生成图形用户界面
		PanelCenter.setBackground(Color.white);
		PanelCenter.setLayout(gridLayout1);
		gridLayout1.setColumns(1);
		gridLayout1.setRows(6);
		PanelMusic.setBackground(Color.white);
		PanelMusic.setLayout(flowLayout3);
		PanelEffect.setBackground(Color.white);
		PanelEffect.setLayout(flowLayout2);
		PanelLevel.setBackground(Color.white);
		PanelLevel.setLayout(flowLayout1);
		PanelAnimate.setBackground(Color.white);
		PanelAnimate.setLayout(flowLayout4);
		MusicOn.setBackground(Color.white);
		MusicOn.setSelected(true);
		MusicOn.setText("开(默认)");
		MusicOff.setBackground(Color.white);
		MusicOff.setText("关");
		Music.setText("背景音乐");
		PanelLeft.setBackground(Color.white);
		PanelLeft.setMinimumSize(new Dimension(30, 10));
		PanelLeft.setOpaque(true);
		PanelLeft.setPreferredSize(new Dimension(30, 10));
		PanelRight.setBackground(Color.white);
		PanelRight.setFont(new java.awt.Font("Dialog", 0, 11));
		PanelRight.setMinimumSize(new Dimension(30, 10));
		PanelRight.setPreferredSize(new Dimension(30, 10));
		Effect.setText("音乐特效");
		EffectOn.setBackground(Color.white);
		EffectOn.setSelected(true);
		EffectOn.setText("开(默认)");
		EffectOff.setBackground(Color.white);
		EffectOff.setText("关");
		Level.setText("困难等级");
		Level1.setBackground(Color.white);
		Level1.setText("1");
		Level2.setBackground(Color.white);
		Level2.setText("2");
		Level3.setBackground(Color.white);
		Level3.setSelected(true);
		Level3.setText("3(默认)");
		Level4.setBackground(Color.white);
		Level4.setText("4");
		Level5.setBackground(Color.white);
		Level5.setText("5");
		flowLayout1.setAlignment(FlowLayout.LEFT);
		flowLayout2.setAlignment(FlowLayout.LEFT);
		flowLayout3.setAlignment(FlowLayout.LEFT);
		AnimateSpeed.setText("消除速度");
		flowLayout4.setAlignment(FlowLayout.LEFT);
		Speed1.setBackground(Color.white);
		Speed1.setText("1x");
		Speed8.setBackground(Color.white);
		Speed8.setText("8x");
		Speed2.setBackground(Color.white);
		Speed2.setSelected(true);
		Speed2.setText("2x(默认)");
		Speed4.setBackground(Color.white);
		Speed4.setText("4x");
		PanelTop.setBackground(Color.white);
		PanelTop.setMinimumSize(new Dimension(10, 10));
		this.getContentPane().add(PanelClose, BorderLayout.SOUTH);
		PanelClose.add(Close, null);
		this.getContentPane().add(PanelCenter, BorderLayout.CENTER);
		PanelMusic.add(Music, null);
		PanelMusic.add(MusicOn, null);
		PanelMusic.add(MusicOff, null);
		PanelCenter.add(PanelTop, null);
		PanelCenter.add(PanelMusic, null);
		PanelCenter.add(PanelEffect, null);
		PanelCenter.add(PanelLevel, null);
		PanelCenter.add(PanelAnimate, null);
		PanelAnimate.add(AnimateSpeed, null);
		PanelAnimate.add(Speed1, null);
		PanelAnimate.add(Speed2, null);
		PanelAnimate.add(Speed4, null);
		PanelAnimate.add(Speed8, null);
		this.getContentPane().add(PanelLeft, BorderLayout.WEST);
		this.getContentPane().add(PanelRight, BorderLayout.EAST);
		PanelEffect.add(Effect, null);
		PanelEffect.add(EffectOn, null);
		PanelEffect.add(EffectOff, null);
		PanelLevel.add(Level, null);
		PanelLevel.add(Level1, null);
		PanelLevel.add(Level2, null);
		PanelLevel.add(Level3, null);
		PanelLevel.add(Level4, null);
		PanelLevel.add(Level5, null);
		SpeedGroup.add(Speed1);
		SpeedGroup.add(Speed2);
		SpeedGroup.add(Speed4);
		SpeedGroup.add(Speed8);
		EffectGroup.add(EffectOn);
		EffectGroup.add(EffectOff);
		LevelGroup.add(Level1);
		LevelGroup.add(Level2);
		LevelGroup.add(Level3);
		LevelGroup.add(Level4);
		LevelGroup.add(Level5);
		MusicGroup.add(MusicOn);
		MusicGroup.add(MusicOff);
		load();
	}

	/**
	 * 装载和保存现有的设置属性
	 * 下一次调用设置对话框时,
	 * 根据此时的设置生成界面.
	 */
	private void load() {
		if (Setting.Music == 1) {
			MusicOn.setSelected(true);
		}
		else {
			MusicOff.setSelected(true);
		}

		if (Setting.Sound == 1) {
			EffectOn.setSelected(true);
		}
		else {
			EffectOff.setSelected(true);
		}

		if (Setting.LevelIndex == 0) {
			Level1.setSelected(true);
		}
		else if (Setting.LevelIndex == 1) {
			Level2.setSelected(true);
		}
		else if (Setting.LevelIndex == 3) {
			Level4.setSelected(true);
		}
		else if (Setting.LevelIndex == 4) {
			Level5.setSelected(true);
		}
		else {
			Level3.setSelected(true);
		}

		if (Setting.Animate == 1) {
			Speed1.setSelected(true);
		}
		else if (Setting.Animate == 4) {
			Speed4.setSelected(true);
		}
		else if (Setting.Animate == 8) {
			Speed8.setSelected(true);
		}
		else {
			Speed2.setSelected(true);
		}
	}
}