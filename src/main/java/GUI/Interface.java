package GUI;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import VM.VirtualMachine;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public final class Interface extends JFrame {
    private static final Interface INSTANCE = new Interface();
    private JMenuBar menu;
    private JMenu menu_file, menu_edit, menu_help;
    private JMenuItem mitem_new, mitem_open, mitem_save, mitem_saveas, mitem_exit;
    private JMenuItem mitem_undo, mitem_cut, mitem_copy, mitem_paste, mitem_find, mitem_replace;
    private JMenuItem mitem_about;
    public JPanel MainPanel;
    private JLabel StatusLabel;
    private JLabel BtnComp;
    private JLabel BtnVm;
    private JLabel BtnLog;
    private JPanel BottomPanel;
    private JPanel CenterPanel;
    private JPanel VmPanel;
    private JTable table1;
    private JTable table2;
    private JButton stopButton;
    private JButton bttnLoad;
    private JList list1;
    private JList list2;
    private JButton bttnStep;
    private JButton bttnRun;
    private JLabel label2;
    private JLabel label1;
    private TableModInstructions TModI;
    private TableModStack TModS;
    private NewSelectionModel SelectorI, SelectorS;
    private boolean vm_toogle = false;

    //f(x) - functions
    public static Interface getInstance() {
        return INSTANCE;
    }

    //constructor
    public Interface(){
        super("TestLab");
        vm_toogle = false;
        setSize(1280, 720);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//DO_NOTHING_ON_CLOSE);
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

        CenterPanel.setLayout(new BorderLayout());
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        CenterPanel.add(sp,BorderLayout.CENTER);

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"));
            theme.apply(textArea);
        } catch (IOException ioe) { // Never happens
            ioe.printStackTrace();
        }
        //CenterPanel.add(new JLabel("Testando"),BorderLayout.EAST);
        //CenterPanel.add(VmPanel,BorderLayout.EAST);

        BtnVm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(vm_toogle == false){
                    CenterPanel.remove(sp);
                    CenterPanel.add(VmPanel,BorderLayout.CENTER);
                    CenterPanel.add(sp,BorderLayout.WEST);
                    vm_toogle = true;
                }
                else{
                    CenterPanel.remove(VmPanel);
                    CenterPanel.add(sp,BorderLayout.CENTER);
                    vm_toogle = false;
                }
                Interface.getInstance().revalidate();
                super.mouseClicked(e);
            }
        });
        BtnComp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        bttnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    if(VirtualMachine.getInstance().load(selectedFile))
                        for(int i=0;i<VirtualMachine.getInstance().I.size();i++){
                            TModI.addRow(VirtualMachine.getInstance().I.get(i));
                        }
                        updateInstructions();
                }
            }
        });
        bttnStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateInstructions();
                updateStack();
                try {
                    VirtualMachine.getInstance().exec_instruction(VirtualMachine.getInstance().I.get(VirtualMachine.getInstance().i));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        bttnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                VirtualMachine.getInstance().running = true;
                while(VirtualMachine.getInstance().running){
                    updateInstructions();
                    updateStack();
                    try {
                        VirtualMachine.getInstance().exec_instruction(VirtualMachine.getInstance().I.get(VirtualMachine.getInstance().i));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateInstructions(){

        SelectorI.set(VirtualMachine.getInstance().i);
        SelectorI.updateSelection();
        label1.setText(Integer.toString(VirtualMachine.getInstance().i));
    }
    private void updateStack(){
        TModS.setRowCount(0);
        for(int x=0;x<VirtualMachine.getInstance().M.size();x++)
            TModS.addRow(x,VirtualMachine.getInstance().M.get(x));
        label2.setText(Integer.toString(VirtualMachine.getInstance().s));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        table1 = new JTable();
        TModS = new TableModStack();
        table1.setModel(TModS);
        SelectorS = new NewSelectionModel();
        table1.setSelectionModel(SelectorS);

        table2 = new JTable();
        TModI = new TableModInstructions();
        table2.setModel(TModI);
        SelectorI = new NewSelectionModel();
        table2.setSelectionModel(SelectorI);
        table2.getColumnModel().getColumn(0).setPreferredWidth(30);
        table2.getColumnModel().getColumn(1).setPreferredWidth(65);
        table2.getColumnModel().getColumn(2).setPreferredWidth(40);
        table2.getColumnModel().getColumn(3).setPreferredWidth(40);
        table2.getColumnModel().getColumn(4).setPreferredWidth(300);
        this.revalidate();
    }
}

