package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public final class Home extends JFrame implements ActionListener {
    private static final Home INSTANCE = new Home();
    // declaracao de variaveis
    private JMenuBar menu;
    private JMenu menu_file, menu_edit, menu_help;
    private JMenuItem mitem_new, mitem_open, mitem_save, mitem_saveas, mitem_exit;
    private JMenuItem mitem_undo, mitem_cut, mitem_copy, mitem_paste, mitem_find, mitem_replace;
    private JMenuItem mitem_about;
    private JPanel panel_bot;
    private BufferedImage img_btn_compile, img_btn_vm, img_btn_log;
    private JLabel btn_compile, btn_vm, btn_log;
    private JLabel status;
    public int ln, col;

    // -----------------------
    public static Home getInstance() {
        return INSTANCE;
    }
    private void drawBottomBar(){
        int frame_x = this.getSize().width;
        int frame_y = this.getSize().height;

        panel_bot = new JPanel(null);
        panel_bot.setBackground(new Color(30,32,34));
        add(panel_bot,BorderLayout.SOUTH);
        //panel_bot.setBounds(0,frame_y-73,frame_x,20);

        /*try {
            img_btn_compile = ImageIO.read(this.getClass().getResource("/bot/compile.png"));
            img_btn_vm = ImageIO.read(this.getClass().getResource("/bot/vm.png"));
            img_btn_log = ImageIO.read(this.getClass().getResource("/bot/log.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        btn_compile = new JLabel(new ImageIcon(img_btn_compile.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btn_vm = new JLabel(new ImageIcon(img_btn_vm.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btn_log = new JLabel(new ImageIcon(img_btn_log.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        int x = btn_compile.getSize().width + btn_vm.getSize().width + btn_log.getSize().width + 32;
        x = (panel_bot.getSize().width / 2) - (x/2);

        panel_bot.add(btn_compile);
        panel_bot.add(btn_vm);
        panel_bot.add(btn_log);

        btn_compile.setBounds(x,0,btn_compile.getPreferredSize().width,20);
        btn_vm.setBounds(x+btn_compile.getPreferredSize().width+16,0,btn_vm.getPreferredSize().width,20);
        btn_log.setBounds(x+btn_compile.getPreferredSize().width+32+btn_vm.getPreferredSize().width,0,btn_log.getPreferredSize().width,20);

        //status = new JLabel("Line 123, Col 22    ");
*/

    }

    //--------------------------------------------------------
    public Home() {
        super("TestLab");   //setTitle("NewTitle");
        setSize(1280, 720);
        //setUndecorated(true);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//DO_NOTHING_ON_CLOSE);

        //RSyntaxTextArea       https://mvnrepository.com/artifact/com.fifesoft/rsyntaxtextarea
        setLayout(new BorderLayout());

        //****[MENU]*************************************************************************
        menu = new JMenuBar();
        menu_file = new JMenu("File");
        menu_edit = new JMenu("Edit");
        menu_help = new JMenu("Help");
        mitem_new = new JMenuItem("New");
        mitem_open = new JMenuItem("Open");
        mitem_save = new JMenuItem("Save");
        mitem_saveas = new JMenuItem("Save As");
        mitem_exit = new JMenuItem("Exit");
        mitem_undo = new JMenuItem("Undo");
        mitem_cut = new JMenuItem("Cut");
        mitem_copy = new JMenuItem("Copy");
        mitem_paste = new JMenuItem("Paste");
        mitem_find = new JMenuItem("Find");
        mitem_replace = new JMenuItem("Replace");
        mitem_about = new JMenuItem("About");
        menu_file.add(mitem_new);
        menu_file.add(mitem_open);
        menu_file.add(mitem_save);
        menu_file.add(mitem_saveas);
        menu_file.add(mitem_exit);
        menu_edit.add(mitem_undo);
        menu_edit.add(mitem_cut);
        menu_edit.add(mitem_copy);
        menu_edit.add(mitem_paste);
        menu_edit.add(mitem_find);
        menu_edit.add(mitem_replace);
        menu_help.add(mitem_about);
        menu.add(menu_file);
        menu.add(menu_edit);
        menu.add(menu_help);
        setJMenuBar(menu);

        drawBottomBar();
    }
    //--------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //if actionEvent.getSource() ==
    }
}
