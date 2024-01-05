package org.zhangfish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.StringJoiner;

public class MainFrame extends JFrame {
    JButton start = new JButton("开始转换");
    JTextArea textArea = new JTextArea();
    StringJoiner text = new StringJoiner("\n");

    /**
     * 窗口初始化
     */
    public MainFrame(ActionListener startListener) {
        JFrame frame = new JFrame("pdf2png");
        frame.setBounds(700, 300, 600, 600);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("pdf2png", JLabel.CENTER);
        title.setFont(new Font("黑体", Font.PLAIN, 50));

        textArea.setFont(new Font("等线", Font.PLAIN, 20));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        start.addActionListener(startListener);
        start.setPreferredSize(new Dimension(100, 100));
        start.setFont(new Font("等线", Font.PLAIN, 40));

        frame.add(title, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(start, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 在文本框中新增一行
     *
     * @param text 新增的文本
     */
    public void addText(String text) {
        this.text.add(text);
        textArea.setText(String.valueOf(this.text));
        textArea.updateUI();
    }
}
