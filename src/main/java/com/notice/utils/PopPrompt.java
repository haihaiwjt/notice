package src.main.java.com.notice.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
// 弹出弹窗提示
public class PopPrompt implements Runnable {
    private final String message;

    public PopPrompt(String message) {
        this.message = message;
    }

    // 图形化弹窗展示
    public void run() {
        // 创建一个JFrame实例
        JFrame parentFrame = new JFrame();
        parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建一个JDialog实例
        JDialog dialog = new JDialog(parentFrame, "阿米娅闹钟提示博士", true); // 设置为模态对话框
        dialog.setAlwaysOnTop(true); // 设置始终置顶
        dialog.setSize(450, 400); // 设置对话框大小

        // 创建一个JPanel来容纳图片和文本
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // 使用BorderLayout布局

        // 加载图片
        ImageIcon imageIcon = new ImageIcon(".\\images\\amiya1.jpg"); // 替换为你的图片路径
        JLabel imageLabel = new JLabel(imageIcon);
        panel.add(imageLabel, BorderLayout.CENTER); // 将图片添加到面板的中心

        // 创建文本标签
        JLabel textLabel = new JLabel(message, SwingConstants.CENTER);
        textLabel.setBorder(new EmptyBorder(10, 20, 20, 10));
        panel.add(textLabel, BorderLayout.SOUTH); // 将文本添加到面板的南部

        // 将面板添加到对话框
        dialog.add(panel);

        // 显示对话框
        dialog.setVisible(true);
    }
}
