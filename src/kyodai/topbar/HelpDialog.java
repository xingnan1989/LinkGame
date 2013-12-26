package kyodai.topbar;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

/**
 * 生成帮助对话框的类
 */

public class HelpDialog extends JDialog implements HyperlinkListener {
	JScrollPane ScrollPane = new JScrollPane();
	JEditorPane HelpPane = new JEditorPane();
	Border border1;
	JPanel Panel1 = new JPanel();
	JButton Close = new JButton();
	Border border2;

	public HelpDialog(JFrame frame) throws HeadlessException {
		super(frame, true);
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//设置运行位置，使对话框居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - 420) / 2,
			(int) (screenSize.height - 360) / 2);
		this.setResizable(false);
	}

	private void jbInit() throws Exception {
		border2 = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(
				Color.lightGray,1),BorderFactory.createEmptyBorder(2,10,2,10));
		this.setSize(new Dimension(420, 360));
		this.setTitle("帮助");
		border1 = BorderFactory.createEmptyBorder();

		URLClassLoader urlLoader = (URLClassLoader)this.getClass().getClassLoader();
		URL url = null;
		//调用帮助文件
		url = urlLoader.findResource("doc/help.htm");
		HelpPane.setPage(url);
		HelpPane.setEditable(false);
		HelpPane.addHyperlinkListener(this);
		ScrollPane.setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ScrollPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ScrollPane.setBorder(border1);
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
		this.getContentPane().add(ScrollPane, BorderLayout.CENTER);
		this.getContentPane().add(Panel1,  BorderLayout.SOUTH);
		ScrollPane.getViewport().add(HelpPane, null);
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