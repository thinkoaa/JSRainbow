package burp;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUI {
	private JPanel contentPane;
	private JLabel lbJavaScriptPath;
	private JTextField javaScriptPath;
	private JLabel lbFunctionName;
	private JTextField functionName;
	private JButton button;
	private JButton refresh;

	public GUI() {

		contentPane = new JPanel();
		lbJavaScriptPath = new JLabel("JS文件的绝对路径：");
		lbJavaScriptPath.setFont(lbJavaScriptPath.getFont().deriveFont(20.0f));
		javaScriptPath = new JTextField(10);
		javaScriptPath.setEditable(false);
		javaScriptPath.setBackground(Color.LIGHT_GRAY);
		button=new JButton("选择文件");
		button.setFont(button.getFont().deriveFont(20.0f));
		refresh=new JButton("重新获取");
		refresh.setFont(refresh.getFont().deriveFont(20.0f));
		
		lbFunctionName = new JLabel("JS方法的调用名称：");
		lbFunctionName.setFont(lbFunctionName.getFont().deriveFont(20.0f));
		functionName = new JTextField(10);
		contentPane.add(lbJavaScriptPath);
		contentPane.add(javaScriptPath);
		contentPane.add(lbFunctionName);
		contentPane.add(functionName);
		contentPane.add(button);
		contentPane.add(refresh);
		contentPane.setLayout(null);
		
		lbJavaScriptPath.setBounds(240, 100, 300, 50);
		javaScriptPath.setBounds(480, 100, 500, 35);
		button.setBounds(1000, 100, 150, 35);
		refresh.setBounds(1250, 100, 150, 35);
		
		lbFunctionName.setBounds(240, 180, 300, 50);
		functionName.setBounds(480, 180, 500, 35);
		
		
		functionName.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				Utils.functionName=functionName.getText().trim();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
 
		
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 按钮点击事件
				JFileChooser chooser = new JFileChooser(); // 设置选择器
				int returnVal = chooser.showOpenDialog(button); // 是否打开文件选择框
				if (returnVal == JFileChooser.APPROVE_OPTION) { // 如果符合文件类型
					String filepath = chooser.getSelectedFile().getAbsolutePath(); // 获取绝对路径
					javaScriptPath.setText(filepath);
                    Utils.initJsEngine(filepath);
				}
			}
		});
		
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 按钮点击事件
				 if(javaScriptPath.getText()!=null) {
					 Utils.initJsEngine(javaScriptPath.getText());
				 }
			}
		});
		 

		BurpExtender.callbacks.customizeUiComponent(contentPane);
	}

	public Component getComponet() {
		return contentPane;
	}

	public JTextField getFunctionName() {
		return functionName;
	}
	public JTextField getJavaScriptPath() {
		return javaScriptPath;
	}
}
