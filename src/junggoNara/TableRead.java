package junggoNara;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class TableRead extends JFrame implements ActionListener, MouseListener{
   private int labelx = 40;
   private int inputx = 100;
   private int titley = 0;
   private int contexty = 30;
   private int labelsizex = 100;
   private int labelsizey = 100;
   private int inputsizex = 350;
   private int inputsizey = 25;
   
   private int imagex = 460;
   private int ButtonY = 480;
   
   private int postlit = 30;
   private int conlit = 650;
   private int comlit = 50;
   
   private String id;   // '��񿡼� �о��' �ۼ��� �Խù� ������ ���̵�
   private String connectid;// '��񿡼� �о��' ���� ���ӵ��ִ� ���̵�
   private String manager;   // ���̵� '�Ŵ���'�� ������ ���̵� 
   private int post_id;
   private JTextField postinput; //����
   private JTextArea contextinput; //����
  
   
   private JPanel p;
   private JPanel jp;
   private JPanel cp;
   
   private JButton toList;
   private JButton correct;
   private JButton Delete;
   private JButton Cancel;
   
   private String postTemp;
   private String contTemp;
   private JTextArea comarea;
   private JFileChooser chooser = new JFileChooser();
   String header[] = {"�ۼ���","����","��¥"};
   String contents[][] = { };
   DefaultTableModel tbmodel = new DefaultTableModel(contents,header){
      public boolean isCellEditable(int i, int c) {
         return false;
      }
   };
   JTable table = null;
   
   private Image image;
   int imagewidth;
   int imageheight;
   double ratio;
    int w;
    int h;

    int newwidth = 300;
    int newheight = 400;
    String mainPosition = "W";
    String imgformat = "jpg";
    private JLabel img = new JLabel();
    
   
    
   JScrollPane scrollPane;
    ImageIcon icon;
    JPanel background;
   private JButton check;
   private JButton pc;
   private JButton upCom;
   
   public TableRead(int post_id){
      setTitle("�Խñ� ����");
      setSize(850, 920);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      
      connectid = Login1.login_id; //������ id ����
     
      this.post_id = post_id; //�Խñ� ��ȣ ����
      
      jp = new JPanel();
      jp.setLayout(null);
      jp.setBounds(imagex + 60, titley, 300, 400);
   
      icon = new ImageIcon("./img/Tread.png");
      background = new JPanel() {
         public void paintComponent(Graphics g) {
                
            g.drawImage(icon.getImage(), -1015, 0, null);
               
            setOpaque(false); 
            super.paintComponent(g);
         }
      };
      background.setLayout(null);
      scrollPane = new JScrollPane(background);
        setContentPane(scrollPane);
        
      setpost();   // �ۼ��ڰ� �ۼ��� ���� ���� �޼ҵ�
      setcontext();   // �ۼ��ڰ� �ۼ��� ���� ���� �޼ҵ�
      PostDAO dao = new PostDAO();
      PostDTO vMem = dao.getPostDTO(post_id);
      viewPost(vMem);
      setupload();   // ���� ��ư ���� �޼ҵ�
      setcom();   //   ��� �ۼ� â ���� �޼ҵ�
      settab();   // ��� ���� â ���� �޼ҵ�
      
      
      background.add(jp.add(img));
      
      
   
      setResizable(false);
      
      setVisible(true);
      
   }
   
   
   private ImageIcon resize(String filepath,double newwidth) {
      ImageIcon imic = null;
      try {
         image = ImageIO.read(new File(filepath));
         imagewidth = image.getWidth(null);
         imageheight = image.getHeight(null);
         
         
            ratio = newwidth /(double)imagewidth;
            w = (int)(imagewidth * ratio);
            h = (int)(imageheight * ratio);
         
         
         Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
         BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
         Graphics g = newImage.getGraphics();
         g.drawImage(resizeImage, 0, 0, null);
         g.dispose();
         
         imic = new ImageIcon(newImage);
         
         
      } catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
      
      return imic;         
   }
   private void settab() {
      
      
      JPanel tableup = new JPanel();      // tableup = ��� ���̺� ũ��,��ġ �Ҵ� �г�
      
      tableup.setBounds(65, 545, 729, 204);
      table = new JTable(tbmodel);      // table = ��� ���
      table.getTableHeader().setReorderingAllowed(false);
      table.getTableHeader().setResizingAllowed(false);
      
      table.getColumn("�ۼ���").setPreferredWidth(30);
      table.getColumn("����").setPreferredWidth(450);
      table.getColumn("��¥").setPreferredWidth(50);
      
      table.addMouseListener(this);
      
      JScrollPane tsp = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      tsp.setPreferredSize(new java.awt.Dimension(730,200));
      
      background.add(tableup);   // background = ��� ������ ���Ե� �г�
      tableup.add(tsp);
   
   }
   private void setcom() {
      comarea = new JTextArea();   // ��� ���� �ؽ�Ʈ ���̸���
      comarea.setDocument(new JTextFieldLimit(comlit));   //setdocument(jtextfieldlimit(int)) ��� �ִ� �� ���� 
      comarea.setLineWrap(true);                     //�ڵ� �ٳѱ�
      JScrollPane csp = new JScrollPane
            (comarea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                  ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      
      csp.setBounds(65, 760, 625, 75);
      
      upCom = new JButton();   //'���' ��ư
      upCom.setBounds(700, 760, 100, 76);
      upCom.setIcon(resize("./img/Commenton.png", 100));
      upCom.setPressedIcon(resize("./img/Commentoff.png", 100));
      upCom.setRolloverIcon(resize("./img/Commentpress.png", 100));
      upCom.addActionListener(this);
      
      background.add(csp);
      background.add(upCom);
      
   }
   
   private void setupload() {
      toList = new JButton();      // ��� ��ư
      toList.setBounds(700,ButtonY,60,30);
      toList.setIcon(resize("./img/liston.png", 58));
      toList.setPressedIcon(resize("./img/listoff.png", 58));
      toList.setRolloverIcon(resize("./img/listpress.png", 58));
      
      toList.addActionListener(this);
      
      correct = new JButton();   // ���� ��ư
      correct.setBounds(625,ButtonY,60,30);
      correct.setIcon(resize("./img/correctin.png", 58));
      correct.setPressedIcon(resize("./img/correctoff.png", 58));
      correct.setRolloverIcon(resize("./img/correctpress.png", 58));
      correct.addActionListener(this);
      
      Cancel = new JButton();      // ��� ��ư
      Cancel.setBounds(625,ButtonY,60,30);
      Cancel.setIcon(resize("./img/Cancellon.png", 58));
      Cancel.setPressedIcon(resize("./img/Cancelloff.png", 58));
      Cancel.setRolloverIcon(resize("./img/Cancellpress.png", 58));
      Cancel.addActionListener(this);
      
      
      Delete = new JButton();      // ���� ��ư   
      Delete.setBounds(550,ButtonY,60,30);
      Delete.setIcon(resize("./img/Deleteon.png", 60));
      Delete.setPressedIcon(resize("./img/Deleteoff.png", 60));
      Delete.setRolloverIcon(resize("./img/Deletepress.png", 60));
      Delete.addActionListener(this);
      
      
      check = new JButton();   // Ȯ�� ��ư
      check.setBounds(550,ButtonY,60,30);
      check.setIcon(resize("./img/checkon.png", 58));
      check.setPressedIcon(resize("./img/checkoff.png", 58));
      check.setRolloverIcon(resize("./img/checkpress.png", 58));
      check.addActionListener(this);
      
      pc = new JButton();      //���� ÷�� ��ư
      pc.setBounds(700,ButtonY,113,30);
      pc.setIcon(resize("./img/pcon.png", 113));
      pc.setPressedIcon(resize("./img/pcoff.png", 113));
      pc.setRolloverIcon(resize("./img/pcpress.png", 113));
      pc.addActionListener(this);
      
      background.add(correct);
      background.add(toList);
      background.add(Delete);
      background.add(Cancel);
      background.add(check);
      background.add(pc);
      
      check.setVisible(false);
      Cancel.setVisible(false);
      pc.setVisible(false);
      System.out.println(id + ", "+ connectid);
      if(id == connectid || connectid == "manager") {         // ��񿡼� ���� ���̵�� ����� ���̵� ���ؼ� ��ư�� ������ ����
         Delete.setVisible(true);
         System.out.println("����1");
      } else {
         Delete.setVisible(false); 
         System.out.println("����2");
      }
      if(id == connectid) {         // ��񿡼� ���� ���̵�� ����� ���̵� ���ؼ� ��ư�� ������ ����
         correct.setVisible(true);
         System.out.println("����3");
      } else {
         correct.setVisible(false);    
         System.out.println("����4");
      }
   }
   private void setcontext() {
      
      
      contextinput = new JTextArea();      // �ۼ� �� ���� �ؽ�Ʈ ���̸���
      contextinput.setDocument(new JTextFieldLimit(conlit));
      contextinput.setLineWrap(true);
      contextinput.setEditable(false);
      contextinput.setText("������ ������");
      
      JScrollPane cjp = new JScrollPane(contextinput,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      cjp.setBounds(inputx, contexty+40, inputsizex, 400);
      
      background.add(cjp);
      
   }
   private void setpost() {
      
      postinput = new JTextField();      // �ۼ� ���� ���� �ؽ�Ʈ ���̸���
      postinput.setDocument((new JTextFieldLimit(30)));
      
      postinput.setBounds(inputx, titley+40, inputsizex, inputsizey);
      postinput.setLocation(inputx, titley+40);
      postinput.setEditable(false);
      postinput.setText("������");
      
      background.add(postinput);   
      

      }
   private void insertTwit(){
       
       //ȭ�鿡�� ����ڰ� �Է��� ������ ��´�.
      TwitDTO dto = getViewData2();
       TwitDAO dao = new TwitDAO();       
       boolean ok = dao.insertTwit(dto);
      
       if(ok){
          
           JOptionPane.showMessageDialog(this, "��۵���� �Ϸ�Ǿ����ϴ�.");
          // mList.jTableRefresh();
           dispose();
          
       }else{
          
           JOptionPane.showMessageDialog(this, "��۵���� ���������� ó������ �ʾҽ��ϴ�.");
       }
	}
       public TwitDTO getViewData2(){
           
           //ȭ�鿡�� ����ڰ� �Է��� ������ ��´�.
           TwitDTO dto = new TwitDTO();
           
            
          
           String twit_content =comarea.getText(); //�۳���
         
           
       
           //dto�� ��´�.
           
           dto.setTwit_content(twit_content);
         
           
          
           return dto;
       }
   @Override
   public void actionPerformed(ActionEvent e) {
      Object obj = e.getSource();
      if(obj == upCom ) {         // obj == ��� ���
         if(comarea.getText().equals(""))
         {
            JOptionPane.showMessageDialog(null, "������ �Է����ּ���");
            return;
         }
         
         Date today = new Date();
          
          SimpleDateFormat date = new SimpleDateFormat("yy.MM.dd");
          SimpleDateFormat time = new SimpleDateFormat("HH:mm ");
         
         tbmodel = (DefaultTableModel) table.getModel();
         String [] add = {connectid,comarea.getText(),date.format(today)+". "+time.format(today)};
         // add ù��° �Ű����� ���Ƿ� ������ ���̵�
         
         comarea.setText("");      // ��� �ۼ�â �ʱ�ȭ
         tbmodel.addRow(add);      // ��� ���â�� �� �߰�
      }
      
      if(obj == Delete) {         // obj == ����
         
         
         int selected = JOptionPane.showConfirmDialog(null, "�ۼ��� ���� �����մϴ�.", 
               "���", JOptionPane.YES_NO_OPTION, 
               JOptionPane.WARNING_MESSAGE);
         if (selected == JOptionPane.YES_OPTION) {
        	 PostDAO dao =  new PostDAO();
        	 dao.deletePost(post_id);
            JOptionPane.showMessageDialog(null, "���� ���� �Ǿ����ϴ�.");
            
            dispose();
         } else {
            return;
         }
      
         
      }
      if(obj == toList) {      // obj == ���
         int result = JOptionPane.showConfirmDialog(null, "�Խñ� ������� �̵��մϱ�?",
        		 "confirm", JOptionPane.YES_NO_OPTION);
         if(result == JOptionPane.YES_OPTION) {
        	 this.dispose();
         }
      }
      
      
      if(obj == correct) {   // obj == ����
         
         
         postTemp = postinput.getText();
         contTemp = contextinput.getText();
         
         postinput.setEditable(true);      // ���� �ؽ�Ʈ ���̸��� ���� ����
         contextinput.setEditable(true);      // ���� �ؽ�Ʈ ���̸��� ���� ����
         UpdatePost();
         Delete.setVisible(false);
         check.setVisible(true);
         correct.setVisible(false);
         Cancel.setVisible(true);
         toList.setVisible(false);
         pc.setVisible(true);
         } 
      
      if(obj == check) {      // obj == Ȯ��
         
            if(postinput.getText().equals(""))
            {
               JOptionPane.showMessageDialog(null, "������ �Էµ��� �ʾҽ��ϴ�.");
            } 
            else if (contextinput.getText().equals(""))
            {
               JOptionPane.showMessageDialog(null, "������ �Էµ��� �ʾҽ��ϴ�.");
            } else {
               JOptionPane.showMessageDialog(null, "�Խù� ���� �Ϸ�");
               PostDTO dto = getViewData(); 
               PostDAO dao = new PostDAO();
               dao.updatePost(dto);
               postinput.setEditable(false);
               contextinput.setEditable(false);
               Delete.setVisible(true);
               correct.setVisible(true);
               pc.setVisible(false);
               toList.setVisible(true);
               toList.setBounds(700,ButtonY,60,30);
            }   
      }
      if(obj == Cancel){      // obj == ���

         int selected = JOptionPane.showConfirmDialog(null, "������ ����մϴ�.", 
               "���", JOptionPane.YES_NO_OPTION, 
               JOptionPane.WARNING_MESSAGE);
         if (selected == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "������ ��ҵǾ����ϴ�.");
            postinput.setEditable(false);
            contextinput.setEditable(false);
            Delete.setVisible(true);
            correct.setVisible(true);
            toList.setVisible(true);
            
            pc.setVisible(false);
            check.setVisible(false);
            Cancel.setVisible(false);
            
            toList.setBounds(700,ButtonY,60,30);
         } else {
            return;
         }
      
         postinput.setText(postTemp);
         contextinput.setText(contTemp);
         
         postinput.setEditable(false);
         contextinput.setEditable(false);

      }
      if(obj  == pc) {   // ��񿡼� ���� ��� �ҷ��������� ������ ũ�⿡ �����ϴ� ��Ŀ� ������ ������ �� 
         // obj == ���� ÷��
         FileNameExtensionFilter filter = new FileNameExtensionFilter
               ("JPG & GIF Images", "jpg","gif","png");
         chooser.setFileFilter(filter);
         
         int ret = chooser.showOpenDialog(null);
         if(ret != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, " ����","���",JOptionPane.WARNING_MESSAGE);
            return;
         }   //���Ƿ� ������ ���� ��� Ž���� ���ε� ���
         
         String filePath = chooser.getSelectedFile().getPath();
         try {
            image = ImageIO.read(new File(filePath));
            imagewidth = image.getWidth(null);
            imageheight = image.getHeight(null);
            
            
               ratio = (double)newwidth/(double)imagewidth;
               w = (int)(imagewidth * ratio);
               h = (int)(imageheight * ratio);
            
            
            Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics g = newImage.getGraphics();
            g.drawImage(resizeImage, 0, 0, null);
            g.dispose();
            
            ImageIcon imic = new ImageIcon(newImage);
            img.setIcon(imic);
            img.setBounds(imagex + 50, titley+70, 300, 400);
            }catch (IOException e1) {
               e1.printStackTrace();
            }                                    //�̹��� ���ε� Ȯ��
         
         }
   }
   @Override
   public void mouseClicked(MouseEvent e) {
      int row = table.getSelectedRow();
      int col = table.getSelectedColumn();
      Object value = table.getValueAt(row, 0);
      
      String userCheck = (String)value;      // ��� ���â���� �о�� ���̵� ��
      
      tbmodel = (DefaultTableModel) table.getModel();
      if(userCheck == connectid || connectid == "manager") {      //���� ������ ���̵� ��
      int selected = JOptionPane.showConfirmDialog(null, "�ۼ��� ����� �����մϴ�.", 
            "���", JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
      if (selected == JOptionPane.YES_OPTION) {
         tbmodel.removeRow(row);
      } else {
         return;
         }
      } 
      else {
    	 JOptionPane.showMessageDialog(null, "�ۼ��ڰ� �ƴմϴ�. ������ �� �����ϴ�.");
      }
   }
   @Override
   public void mousePressed(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }
   @Override
   public void mouseReleased(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }
   @Override
   public void mouseEntered(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }
   @Override
   public void mouseExited(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }
   private void UpdatePost() {
       
       PostDTO dto = getViewData(); 
       PostDAO dao = new PostDAO();
       boolean ok = dao.updatePost(dto);
   }
   public PostDTO getViewData(){
       
       //ȭ�鿡�� ����ڰ� �Է��� ������ ��´�.
       PostDTO dto = new PostDTO();
       
       String title = postinput.getText();
       String content =contextinput.getText();
       
       dto.setContent(content);
       dto.setTitle(title);
       dto.setPost_id(post_id);
      
       return dto;
   }
   //ȭ�鿡 Db���� ������ ������ �ҷ��� ȭ�鿡 �Է��Ѵ�.
   private void viewPost(PostDTO vMem){
       
       String id = vMem.getId();
       String title = vMem.getTitle();
       String content = vMem.getContent();
       
       
       postinput.setText(title);
       contextinput.setText(content);
       this.id = id;
       
       System.out.println( 30 + id + ", " + connectid);
   }

}