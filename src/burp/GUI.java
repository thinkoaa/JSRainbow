package burp;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    private JLabel lbEngineInfo;

    public GUI() {
        contentPane = new JPanel();
        lbJavaScriptPath = new JLabel("JS文件的绝对路径：");
        lbJavaScriptPath.setFont(lbJavaScriptPath.getFont().deriveFont(20.0f));
        javaScriptPath = new JTextField(10);
        javaScriptPath.setEditable(false);
        javaScriptPath.setBackground(Color.LIGHT_GRAY);

        button = new JButton("选择文件");
        button.setFont(button.getFont().deriveFont(20.0f));
        refresh = new JButton("重新加载");
        refresh.setFont(refresh.getFont().deriveFont(20.0f));

        lbFunctionName = new JLabel("JS方法的调用名称：");
        lbFunctionName.setFont(lbFunctionName.getFont().deriveFont(20.0f));
        functionName = new JTextField(10);

        // 引擎信息标签
        lbEngineInfo = new JLabel("JS引擎: 自动加载");
        lbEngineInfo.setFont(lbEngineInfo.getFont().deriveFont(16.0f));
        lbEngineInfo.setForeground(Color.GRAY);

        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 第一行：JS文件路径
        gbc.gridx = 0; gbc.gridy = 0;
        contentPane.add(lbJavaScriptPath, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        contentPane.add(javaScriptPath, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        contentPane.add(button, gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        contentPane.add(refresh, gbc);

        // 第二行：函数名称
        gbc.gridx = 0; gbc.gridy = 1;
        contentPane.add(lbFunctionName, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        contentPane.add(functionName, gbc);

        // 第三行：引擎信息
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        contentPane.add(lbEngineInfo, gbc);

        functionName.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                Utils.functionName = functionName.getText().trim();
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(button);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filepath = chooser.getSelectedFile().getAbsolutePath();
                    javaScriptPath.setText(filepath);
                    try {
                        Utils.initJsEngine(filepath);
                        lbEngineInfo.setText("JS引擎: " + Utils.getEngineName() + " | 已加载: " + filepath);
                        lbEngineInfo.setForeground(new Color(0, 128, 0));
                    } catch (Exception e1) {
                        lbEngineInfo.setText("JS引擎: 加载失败");
                        lbEngineInfo.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(getComponent(), e1.getMessage(),
                                "请检查以下异常信息", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = javaScriptPath.getText();
                if (path != null && !path.trim().isEmpty()) {
                    try {
                        Utils.initJsEngine(path);
                        lbEngineInfo.setText("JS引擎: " + Utils.getEngineName() + " | 已加载: " + path);
                        lbEngineInfo.setForeground(new Color(0, 128, 0));
                    } catch (Exception e1) {
                        lbEngineInfo.setText("JS引擎: 加载失败");
                        lbEngineInfo.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(getComponent(), e1.getMessage(),
                                "请检查以下异常信息", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(getComponent(), "请先选择JS文件",
                            "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        BurpExtender.callbacks.customizeUiComponent(contentPane);
    }

    public Component getComponent() {
        return contentPane;
    }

    public JTextField getFunctionName() {
        return functionName;
    }

    public JTextField getJavaScriptPath() {
        return javaScriptPath;
    }
}
