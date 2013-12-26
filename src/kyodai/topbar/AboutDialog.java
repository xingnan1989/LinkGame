package kyodai.topbar;

import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.text.html.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;


/**
 * 生成关于对话框的类
 */

public class AboutDialog extends JDialog implements HyperlinkListener {
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JTextArea jTextArea = new JTextArea();
	BorderLayout borderLayout1 = new BorderLayout();
	Border border1;
	BorderLayout borderLayout2 = new BorderLayout();
	JLabel MyImage = new JLabel();
	JPanel Panel1 = new JPanel();
	JButton Close = new JButton();
	Border border2;

	public AboutDialog(JFrame frame) throws HeadlessException {
		super(frame, true);
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//设置运行位置，使对话框居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - 416) / 2,
						(int) (screenSize.height - 310) / 2);
		this.setResizable(false);
	}

	private void jbInit() throws Exception {
		border1 = BorderFactory.createEmptyBorder(20,25,50,40);
		border2 = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(
				Color.lightGray,1),BorderFactory.createEmptyBorder(2,10,2,10));
		this.setSize(new Dimension(416, 310));
		this.setTitle("关于");
		this.getContentPane().setBackground(Color.white);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jPanel2.setBackground(Color.white);
		jPanel2.setMinimumSize(new Dimension(260, 28));
		jPanel2.setPreferredSize(new Dimension(260, 28));
		jPanel2.setLayout(borderLayout1);
		jPanel1.setBackground(Color.white);
		jPanel1.setMinimumSize(new Dimension(160, 10));
		jPanel1.setPreferredSize(new Dimension(160, 10));
		jPanel1.setLayout(borderLayout2);
		jTextArea.setBorder(border1);
		jTextArea.setEditable(false);
		jTextArea.setText("宝石连连看\n\n"
						+"本程序仿照QQ游戏中的连连看而做，"
						+"代码原作者为张剑，"
						+"编者在张剑的连连看代码基础上做了适当的修改。\n\n"
						+"如果你需要，请联系原作者：\n张剑  leftmoon@163.com");
		jTextArea.setLineWrap(true);
		jTextArea.setForeground(new Color(55, 77, 118));
		this.getContentPane().add(jPanel1, BorderLayout.WEST);
		jPanel1.add(MyImage,  BorderLayout.EAST);
		this.getContentPane().add(jPanel2, BorderLayout.CENTER);
		jPanel2.add(jTextArea, BorderLayout.CENTER);

		URLClassLoader urlLoader = (URLClassLoader)this.getClass().getClassLoader();
		URL url;
		url = urlLoader.findResource("images/me.gif");
		ImageIcon icon = new ImageIcon(url);
		MyImage.setIcon(icon);

		Close.setBackground(Color.white);
		Close.setBorder(border2);
		Close.setActionCommand("jButton1");
		Close.setText("关闭");
		Close.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			}
		);

		Panel1.setBackground(Color.white);
		this.getContentPane().add(Panel1,  BorderLayout.SOUTH);
		Panel1.add(Close, null);
	}

	/**
	 * 当超文本链接更新时调用
	 * 负责更新的事件处理函数
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			JEditorPane pane = (JEditorPane) e.getSource();
			if (e instanceof HTMLFrameHyperlinkEvent) {
				HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
				HTMLDocument doc = (HTMLDocument) pane.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			}
			else {
				try {
					pane.setPage(e.getURL());
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}
}