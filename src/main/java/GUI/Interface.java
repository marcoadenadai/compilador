package GUI;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import java.awt.*;

import COMP.Compilador;
import VM.VirtualMachine;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


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
    private JList<Integer> list1;
    private JList<Integer> list2;
    /*DefaultListModel listModel1;
    DefaultListModel listModel2;*/
    DefaultListModel listModel2;
    private JButton bttnStep;
    private JButton bttnRun;
    private JLabel label2;
    private JLabel label1;
    private JButton bttnDbg;
    private JButton bttnBkpt;
    private TableModInstructions TModI;
    private TableModStack TModS;
    private NewSelectionModel SelectorI, SelectorS;
    private boolean vm_toogle = false;
    private boolean unchanged = true;

    private File selectedFile;
    private JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    final private RSyntaxTextArea textArea;
    final private RTextScrollPane sp;
    JTextArea consoleTextArea;
    JMenuItem mitem_clear_log;
    Panel consolePanel;
    JMenuItem mitem_add_bkpt;
    JMenuItem mitem_clear_bkpt;
    JPopupMenu pop_bkpt;


    //f(x) - functions
    public static Interface getInstance() {
        return INSTANCE;
    }

    //boolean showDescartarDialog();
    //constructor
    public Interface(){
        super("TestLab - novo arquivo");
        vm_toogle = false;
        setSize(1280, 720);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //EXIT_ON_CLOSE);
        menu = new JMenuBar();
        menu_file = new JMenu("File");
        menu_help = new JMenu("Help");
        mitem_new = new JMenuItem("New");
        mitem_open = new JMenuItem("Open");
        mitem_save = new JMenuItem("Save");
        mitem_saveas = new JMenuItem("Save As");
        mitem_exit = new JMenuItem("Exit");
        mitem_about = new JMenuItem("About");
        menu_file.add(mitem_new);
        menu_file.add(mitem_open);
        menu_file.add(mitem_save);
        menu_file.add(mitem_saveas);
        menu_file.add(mitem_exit);
        menu_help.add(mitem_about);
        menu.add(menu_file);
        menu.add(menu_help);
        setJMenuBar(menu);
        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        selectedFile = null;

        CenterPanel.setLayout(new BorderLayout());
        textArea = new RSyntaxTextArea(20, 60);
        //textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        sp = new RTextScrollPane(textArea);
        CenterPanel.add(sp,BorderLayout.CENTER);

        pop_bkpt = new JPopupMenu();
        mitem_add_bkpt = new JMenuItem("Add Breakpoint");
        mitem_clear_bkpt = new JMenuItem("Limpar Tudo");
        pop_bkpt.add(mitem_add_bkpt);
        pop_bkpt.add(mitem_clear_bkpt);

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"));
            theme.apply(textArea);
        } catch (IOException ioe) { // Never happens
            ioe.printStackTrace();
        }
        //CenterPanel.add(new JLabel("Testando"),BorderLayout.EAST);
        //CenterPanel.add(VmPanel,BorderLayout.EAST);
        this.setContentPane(MainPanel);
        //consoleTextArea!!!
        consoleTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        JPopupMenu popmenu = new JPopupMenu();
        mitem_clear_log = new JMenuItem("Limpar Console");
        popmenu.add(mitem_clear_log);
        consoleTextArea.setComponentPopupMenu(popmenu);
        consoleTextArea.setEditable(false);
        consoleTextArea.setText("");
        consolePanel = new Panel(new BorderLayout());
        consolePanel.setBackground(new Color(30,32,34));
        consolePanel.setSize(640,100);
        consolePanel.add(scrollPane, BorderLayout.CENTER);
        Panel x = new Panel(new BorderLayout());
        JLabel consoleTxt = new JLabel("<html><font color='#ABBFDE'>CONSOLE LOG:</font></html>");
        consoleTxt.setFont(new Font(consoleTxt.getFont().getName(), Font.PLAIN, 9));
        x.add(consoleTxt, BorderLayout.LINE_START);
        consolePanel.add(x,BorderLayout.PAGE_START);
        consolePanel.setLocation(this.getSize().width/3 - 120,this.getSize().height-150);
        consolePanel.setVisible(false);

        this.getLayeredPane().add(consolePanel, JLayeredPane.POPUP_LAYER);
        estado0();

        //-- BUTTONS E LISTENERS ---------------------------------------------------------------------------

        mitem_clear_log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                consoleTextArea.setText("");
            }
        });

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent){
                if(unchanged == false){
                    if(showDescartarDialog() == true){
                        System.exit(0);
                    }
                }
                else{
                    System.exit(0);
                }
            }
        });

        mitem_clear_bkpt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((DefaultListModel)list2.getModel()).clear();
            }
        });

        mitem_add_bkpt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int x = Integer.parseInt(JOptionPane.showInputDialog(null, "Add (VM) breakpoint:"));
                //
                listModel2.addElement(x);
            }
        });

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {

            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {

            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                if(unchanged == true){
                    Interface.getInstance().setTitle(Interface.getInstance().getTitle() + " *");
                    unchanged = false;
                }
            }
        });

        textArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent caretEvent) {
                int ln=1, col=1;
                int caretpos = textArea.getCaretPosition();
                try {
                    ln=textArea.getLineOfOffset(caretpos);
                    col=caretpos - textArea.getLineStartOffset(ln);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                ln++;
                StatusLabel.setText("Line "+ln+", Col "+col+"    ");
            }
        });

        mitem_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //se texto não está em branco,
                if(textArea.getText().length() > 0){
                    if(showDescartarDialog() == true){
                        textArea.setText("");
                        unchanged = true;
                        Interface.getInstance().setTitle("TestLab - novo arquivo");
                        selectedFile = null;
                    }
                }else{
                    textArea.setText("");
                    unchanged = true;
                    Interface.getInstance().setTitle("TestLab - novo arquivo");
                    selectedFile = null;
                }
            }
        });

        mitem_open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = jfc.getSelectedFile();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                textArea.setText(Files.readString(Path.of(selectedFile.getPath())));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            unchanged = true;
                            Interface.getInstance().setTitle("TestLab - " + selectedFile.toString());
                        }
                    });
                }
            }
        });

        mitem_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(selectedFile != null){
                    Interface.getInstance().setTitle("TestLab - " + selectedFile.toString());
                    //salvar simples
                    try {//salvamento
                        BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                        writer.write(textArea.getText());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    unchanged = true;
                } else{
                    //salvar como
                    JFrame parentFrame = new JFrame();
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Salvar: ");

                    int userSelection = fileChooser.showSaveDialog(parentFrame);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile();
                        //salvamento
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                            writer.write(textArea.getText());
                            writer.close();
                            unchanged = true;
                            Interface.getInstance().setTitle("TestLab - " + selectedFile.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        mitem_saveas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame parentFrame = new JFrame();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Salvar como: ");

                int userSelection = fileChooser.showSaveDialog(parentFrame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    //salvamento
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                        writer.write(textArea.getText());
                        writer.close();
                        unchanged = true;
                        Interface.getInstance().setTitle("TestLab - " + selectedFile.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mitem_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(unchanged == false){
                    if(showDescartarDialog() == true){
                        System.exit(0);
                    }
                }
                else{
                    System.exit(0);
                }
            }
        });

        mitem_about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "TestLab é uma IDE para compilação e " +
                        " para a linguagem LPD, " +
                        "este programa faz parte de uma disciplina do curso de computação da PUC-CAMPINAS e foi " +
                        "desenvolvido pelo aluno Marco Antônio De Nadai Filho. 2020/2.");
            }
        });

        BtnLog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toogleConsole();
                super.mouseClicked(e);
            }
        });
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
                if(Interface.getInstance().selectedFile != null) {
                    if(unchanged == false){
                        if(showSalvarECompilarDialog() == true){
                            Interface.getInstance().setTitle("TestLab - " + selectedFile.toString());
                            try {//salvamento
                                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                                writer.write(textArea.getText());
                                writer.close();
                            } catch (IOException ee) {
                                ee.printStackTrace();
                            }
                            unchanged = true;
                            Compilador.getInstance().executa(Interface.getInstance().selectedFile);
                        }
                    }
                    else{
                        Compilador.getInstance().executa(Interface.getInstance().selectedFile);
                    }

                }
                else{
                    JOptionPane.showMessageDialog(null,"Carregue um arquivo antes de compilar.");
                }
                super.mouseClicked(e);
            }
        });
        //------------bttns VM
        bttnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    clearOUTPUT();
                    VirtualMachine.getInstance().inicializa();
                    updateInstruction();
                    updateStack();
                    File selectedFile = jfc.getSelectedFile();
                    if(VirtualMachine.getInstance().load(selectedFile)) {
                        estado1();
                        for (int i = 0; i < VirtualMachine.getInstance().I.size(); i++) {
                            TModI.addRow(VirtualMachine.getInstance().I.get(i));
                        }
                        updateRunInstruction();
                        updateStack();
                    }
                }
            }
        });
        bttnStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateRunInstruction();
                updateStack();
                try {
                    VirtualMachine.getInstance().exec_instruction(VirtualMachine.getInstance().I.get(VirtualMachine.getInstance().i));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if(!VirtualMachine.getInstance().running)
                    estado3();
            }
        });
        bttnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                estado3();
                VirtualMachine.getInstance().running = true;
                while(VirtualMachine.getInstance().running){
                    updateRunInstruction();
                    updateStack();
                    try {
                        VirtualMachine.getInstance().exec_instruction(VirtualMachine.getInstance().I.get(VirtualMachine.getInstance().i));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        bttnDbg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //run com breakpoints
                estado2();
            }
        });
        bttnBkpt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                VirtualMachine.getInstance().running = true;
                while(VirtualMachine.getInstance().running && !bkpt_contido(VirtualMachine.getInstance().i)){
                    updateRunInstruction();
                    updateStack();
                    try {
                        VirtualMachine.getInstance().exec_instruction(VirtualMachine.getInstance().I.get(VirtualMachine.getInstance().i));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                if(!VirtualMachine.getInstance().running)
                    estado3();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //todo e volta pro estado 0
                clearOUTPUT();
                VirtualMachine.getInstance().inicializa();
                updateInstruction();
                updateStack();
                estado0();
            }
        });

        list2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    pop_bkpt.show(list2, e.getPoint().x, e.getPoint().y);
                }
                super.mousePressed(e);
            }
        });
    }
    void estado0(){
        bttnRun.setEnabled(false);
        bttnDbg.setEnabled(false);
        bttnLoad.setEnabled(true);
        stopButton.setEnabled(false);
        bttnStep.setEnabled(false);
        bttnBkpt.setEnabled(false);
    }

    void estado1(){
        bttnRun.setEnabled(true);
        bttnDbg.setEnabled(true);
        bttnLoad.setEnabled(true);
        stopButton.setEnabled(false);
        bttnStep.setEnabled(false);
        bttnBkpt.setEnabled(false);
    }

    void estado2(){ //estado debug
        bttnRun.setEnabled(false);
        bttnDbg.setEnabled(false);
        bttnLoad.setEnabled(false);
        stopButton.setEnabled(true);
        bttnStep.setEnabled(true);
        bttnBkpt.setEnabled(true);
    }

    void estado3(){ //estado execucao normal
        bttnRun.setEnabled(false);
        bttnDbg.setEnabled(false);
        bttnLoad.setEnabled(false);
        stopButton.setEnabled(true);
        bttnStep.setEnabled(false);
        bttnBkpt.setEnabled(false);
    }

    public void clearOUTPUT(){
        ((DefaultListModel)list1.getModel()).clear();
    }
    public void addOUTPUT(int val){
        try{
            ((DefaultListModel)list1.getModel()).addElement(val);
        }catch (NullPointerException e){
            System.out.println(e);
        }

    }

    boolean bkpt_contido(int line){
        for(int i=0;i<listModel2.getSize();i++){
            if((Integer)listModel2.getElementAt(i) == line)
                return true;
        }
        return false;
    }

    //my-functions-------------
    public void printConsole(String input){
        consoleTextArea.setText(consoleTextArea.getText() + input + "\n");
    }
    void toogleConsole(){
        if(consolePanel.isVisible()){
            consolePanel.setVisible(false);
        }
        else
            consolePanel.setVisible(true);
    }
    boolean showDescartarDialog(){
        Object[] options = { "Sim", "Cancelar" };
        int x = JOptionPane.showOptionDialog(null, "Deseja descartar o trabalho não salvo?", "Você tem certeza?",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(x==0)
            return true;
        return false;
    }
    boolean showSalvarECompilarDialog(){
        Object[] options = { "Salvar e continuar", "Cancelar" };
        int x = JOptionPane.showOptionDialog(null, "Seu código possui mudanças não salvas.", "Como prosseguir?",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        if(x==0)
            return true;
        return false;
    }
    //-------------------------
    private void updateRunInstruction(){
        SelectorI.set(VirtualMachine.getInstance().i);
        SelectorI.updateSelection();
        label1.setText("i:"+Integer.toString(VirtualMachine.getInstance().i));
    }
    private void updateStack(){
        TModS.setRowCount(0);
        for(int x=0;x<VirtualMachine.getInstance().M.size();x++)
            TModS.addRow(x,VirtualMachine.getInstance().M.get(x));
        label2.setText("s:"+Integer.toString(VirtualMachine.getInstance().s));
    }
    private void updateInstruction(){
        TModI.clear();
        TModI.setRowCount(0);
        for (int i = 0; i < VirtualMachine.getInstance().I.size(); i++) {
            TModI.addRow(VirtualMachine.getInstance().I.get(i));
        }
        label1.setText("i:"+Integer.toString(VirtualMachine.getInstance().i));
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        listModel2 = new DefaultListModel();
        list2 = new JList(listModel2);


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

