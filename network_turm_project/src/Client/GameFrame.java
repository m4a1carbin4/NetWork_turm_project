package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main_server.Bullet;
import main_server.GameModel;
import main_server.GameModelList;
import main_server.Player;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GameFrame extends JFrame implements Runnable{

	public DataOutputStream dataoutputstream;
	public DataInputStream datainputstream;
	public JSONParser parser = new JSONParser();
	public Gson gson;
   
   //배경이미지, 배경이미지 가로-세로 길이 [ init() 에서 초기화 ]
	public BufferedImage mapImg;
	public int mapW;
	public int mapH;
   //캐릭터, 회전 된 캐릭터 이미지를 저장할 BufferedImage 객체 [ init() 에서 초기화 ] 
	public BufferedImage character;
//   private BufferedImage rotatedCharacter;
	public int rotatedCharacterW;
	public int rotatedCharacterH;
   
   //캐릭터 회전을 위해 필요한 객체들
	public GraphicsConfiguration gc = getDefaultConfiguration();
	public BufferedImage result;
	public Graphics2D g;
   //캐릭터 회전 축 (원본 캐릭터 이미지 파일을 기준으로 설정해야 함)
	public final int rotateAxisX = 123; // 세로방향 이미지 : 120 , 가로방향 이미지 = 123
	public final int rotateAxisY = 120; // 세로방향 이미지 : 230 , 가로방향 이미지 = 120
   //회전 된 캐릭터를 그릴 새로운 BufferedImage 가로, 세로 (원본 캐릭터 이미지 파일의 가로,세로 보다 훨씬 커야함)
   //이 값을 ratio로 나눈 값이 rotatedCharacter의 가로, 세로 길이가 될 것임
	public final int newBuffImageW = 600;
	public final int newBuffImageH = 600;
   
   //그림 그릴 패널의 가로, 세로 길이 [ 윈도우 창(?) 크기에 따라 다름 ]
	public final int panelW = 1094;
	public final int panelH = 772;
   //캐릭터 그리기 위해 필요한 변수
	public int username;
   
	public int charX=1390, charY=50;
	public int mouseX, mouseY;
   double angle = 0;
   
   //총알 관련
   public BufferedImage bulletImg;
//   private BufferedImage rotatedBullet;
   public int rotatedBulletW;
   public int rotatedBulletH;
   
   private final int bulletRotateAxisX = 50; 
   private final int bulletRotateAxisY = 33; 

   private final int bulletNewBuffImageW = 200;
   private final int bulletNewBuffImageH = 200;
   
   private final int bulletSpeed = 30;
   
   private int bulletOffsetX;
   private int bulletOffsetY;
   private final int bulletRatio = 2;
   
   //서버로 보낼 GameModel
   private Player player;
   private Vector<Bullet> sendBulletList;
   private Vector<Bullet> receiveBulletList;
   private int receiveCurHp;
   private GameModel sendGameModel;
   
   private GameModelList receiveGameModelList;
   private String jsonData;
   
   //캐릭터 그릴 위치 보정을 위한 변수 [ init() 에서 초기화 ]
   private int offsetX;
   private int offsetY;
   //캐릭터 크기 조절을 위한 변수
   private final int ratio = 4;
      
   //캐릭터를 움직이기 위해 필요한 변수
   private final int characterSpeed = 3;
   boolean rightPressed=false, leftPressed=false, upPressed=false, downPressed=false;
   
   //맵-캐릭터 충돌판정을 위한 (보이지 않는) 배경이미지 [ init() 에서 초기화 ]
   private BufferedImage mapCharacterCollision;
   private BufferedImage mapBulletCollision;
   
   public JSONObject Json_parser(String data) {
		
		JSONObject json_data = null;
		
		try {
			json_data = (JSONObject) parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("incoming data error error ");
		}
		
		return json_data;
		
	}
	
	public String Json_maker(String data,String type) {
		
		JSONObject object = new JSONObject();
		
		object.put("Type", type);
		object.put("Data", data);
		
		
		System.out.println("new json data");
		
		return object.toJSONString();
		
	}
   
   public void send_data(String Data) {
	   
	   String str = Json_maker(Data,"Game");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Game_model_msg_send_fail");
		}
	   
   }
   
   public void Game_model_msg(String input) {
		String str = Json_maker(input,"Game");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Game_model_msg_send_fail");
		}
	}
   
   public GameFrame(DataOutputStream w, DataInputStream r, int username) {
      this.username = username;
      this.datainputstream = r;
      this.dataoutputstream = w;
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 1100, 800);
      setLocationRelativeTo(null);
      setResizable(false);   
      
      MyPanel myPanel = new MyPanel();
      myPanel.setFocusable(true);
      myPanel.setLayout(null);
      setContentPane(myPanel);
      
      init();

      //내 캐릭터 좌표 변경하는 메서드 17ms 마다 호출
      ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
      service.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);

      setVisible(true);
      
      //서버로부터 GameModelList 받고 repaint호출 하는 스레드 시작
      Thread t = new Thread(myPanel);
      t.start();
   }
   
   //이미지 파일 불러와서 변수에 넣기 + 초기값 설정
   private void init() {
      try {
         mapImg = ImageIO.read(new File("images/game/map.png"));
         mapW = mapImg.getWidth();
         mapH = mapImg.getHeight();
         
         character = ImageIO.read(new File("images/game/character.png"));
//         rotatedCharacter = rotate(character, angle);
         
         rotatedCharacterW = newBuffImageW/ratio;
         rotatedCharacterH = newBuffImageH/ratio;
         
         offsetX = ( newBuffImageW/2 - ( character.getWidth()/2 - rotateAxisX ) )/ratio;
         offsetY = ( newBuffImageH/2 - ( character.getHeight()/2 - rotateAxisY ) )/ratio;
         
         
         bulletImg = ImageIO.read(new File("images/game/bullet.png"));
//         rotatedBullet = rotateBullet(bulletImg, angle);
         
         rotatedBulletW = bulletNewBuffImageW/bulletRatio;
         rotatedBulletH = bulletNewBuffImageH/bulletRatio;
         
         bulletOffsetX = ( bulletNewBuffImageW/2 - ( bulletImg.getWidth()/2 - bulletRotateAxisX ) )/bulletRatio;
         bulletOffsetY = ( bulletNewBuffImageH/2 - ( bulletImg.getHeight()/2 - bulletRotateAxisY ) )/bulletRatio;
         
         mapCharacterCollision = ImageIO.read(new File("images/game/mapCharacterCollision.png"));
         mapBulletCollision = ImageIO.read(new File("images/game/mapBulletCollision2.png"));
         
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      gson = new Gson();
      
      player = new Player(username, charX, charY, angle, 100);
      sendBulletList = new Vector<Bullet>();
      receiveBulletList = new Vector<Bullet>();
      
      sendGameModel = new GameModel();
      sendGameModel.setPlayer(player);
   }
   
   //내 캐릭터 좌표 변경하는 메서드
   @Override
   public void run() {
      if (rightPressed) {
         if( mapCharacterCollision.getRGB(charX+characterSpeed, charY) >= -1 ) {
            charX += characterSpeed;               
         }
      }
      if (leftPressed) {
         if( mapCharacterCollision.getRGB(charX-characterSpeed, charY) >= -1 ) {
            charX -= characterSpeed;
         }
      }
      if (upPressed) {
         if( mapCharacterCollision.getRGB(charX, charY-characterSpeed) >= -1 ) {
            charY -= characterSpeed;
         }
      }
      if (downPressed) {
         if( mapCharacterCollision.getRGB(charX, charY+characterSpeed) >= -1 ) {
            charY += characterSpeed;
         }
      }
      setAngle(charX, charY, mouseX, mouseY);
      
      player.setX(charX);
      player.setY(charY);
      player.setAngle(angle);
   }
      
   class MyPanel extends JPanel implements Runnable{
      
      public MyPanel() {
         addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
               if (charX<panelW/2) {
                  mouseX = e.getX();
               }else if(charX>=panelW/2 && charX <= (mapW-panelW/2)){
                  mouseX = e.getX() + charX - panelW/2;
               }else {
                  mouseX = e.getX() + mapW - panelW;
               }
               
               if (charY < panelH/2) {
                  mouseY = e.getY();
               }else if(charY>=panelH/2 && charY <= (mapH-panelH/2)){
                  mouseY = e.getY() + charY - panelH/2;
               }else {
                  mouseY = e.getY() + mapH - panelH;
               }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
               if (charX<panelW/2) {
                  mouseX = e.getX();
               }else if(charX>=panelW/2 && charX <= (mapW-panelW/2)){
                  mouseX = e.getX() + charX - panelW/2;
               }else {
                  mouseX = e.getX() + mapW - panelW;
               }
               
               if (charY < panelH/2) {
                  mouseY = e.getY();
               }else if(charY>=panelH/2 && charY <= (mapH-panelH/2)){
                  mouseY = e.getY() + charY - panelH/2;
               }else {
                  mouseY = e.getY() + mapH - panelH;
               }
            }
            
         });
         
         addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               if(e.getButton() == MouseEvent.BUTTON1   ) {
                  if( receiveBulletList.size() + sendBulletList.size() < 5 ) {
                     Bullet bullet = new Bullet(username, charX + 50*Math.cos(angle), charY + 50*Math.sin(angle), angle, 20);
                     sendBulletList.add(bullet);
                  }
               }
            }
         });
         
         addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               switch(e.getKeyCode()){
                  case KeyEvent.VK_D:
                     rightPressed = true;
                     break;
                  case KeyEvent.VK_A:
                     leftPressed = true;
                     break;
                  case KeyEvent.VK_W:
                     upPressed = true;
                     break;
                  case KeyEvent.VK_S:
                     downPressed = true;
                     break;
               }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
               switch(e.getKeyCode()){
                  case KeyEvent.VK_D:
                     rightPressed = false;
                     break;
                  case KeyEvent.VK_A:
                     leftPressed = false;
                     break;
                  case KeyEvent.VK_W:
                     upPressed = false;
                     break;
                  case KeyEvent.VK_S:
                     downPressed = false;
                     break;
               }
            }
            
         });
         
      }
      
      //서버로부터 GameModelList 받고 repaint()호출 하는 스레드
      @Override
      public void run() {
    	 String str= " ";
    	 
         try {
        	 while(true) {
        		 str = datainputstream.readUTF();
 				
     			JSONObject input_data = Json_parser(str);
     				
     			String Type = (String) input_data.get("Type");
     			String Data = (String) input_data.get("Data");
             	 
             	if(!(Type.equals("Game"))) {
             		continue;
             	}
                System.out.println("From Server >> " + Data);

                   receiveGameModelList = gson.fromJson(Data.toString(), GameModelList.class);
                   
                   for (GameModel gm : receiveGameModelList.getGameModelList()) {
                      if( gm.getPlayer().getStz_username() == username ) {
                         receiveCurHp = gm.getPlayer().getCurHp();
                         receiveBulletList = gm.getBulletList();
                         break;
                      }
                   }
                   
                   // 내 캐릭터 체력 0 이하면 GameResultFrame으로 넘어가기
                   if(receiveCurHp <= 0) {
                      //new GameResultFrame(reader, writer, username);
                      dispose();
                      break;
                   }
                   
                    // paintComponent()메서드를 스레드로 호출함.
                   repaint();
                   
                   // 서버로 보낼 임시 BulletList 생성하고 서버에서 받은 값 그대로 복사(깊은복사)
                   Vector<Bullet> tempBulletList = new Vector<Bullet>();
                   for (int i = 0; i < receiveBulletList.size(); i++) {
                      Bullet bullet = new Bullet();
                      bullet.setStz_username( receiveBulletList.get(i).getStz_username() );
                      bullet.setX( receiveBulletList.get(i).getX() );
                      bullet.setY( receiveBulletList.get(i).getY() );
                      bullet.setAngle( receiveBulletList.get(i).getAngle() );
                      bullet.setDmg( receiveBulletList.get(i).getDmg() );
                      
                      tempBulletList.add(bullet);
                   }
                   
                   // 서버에서 받은 receiveBulletList + 내가 가지고 있는 sendBulletList
                   for (Bullet bullet : sendBulletList) {
                      tempBulletList.add(bullet);
                   }
                   sendBulletList.clear();
                   
                   // 두개 합친 bulletList 중에서 벽에 맞은것은 제거하고 서버로 보냄
                   Vector<Integer> removeBullet = new Vector<Integer>();
                   int i = 0;
                   for (Bullet b : tempBulletList) {
                      //총알 데미지가 0이상인가?
                      if(b.getDmg() > 0) {
                         //총알의 다음 위치(dx, dy)
                         double dx = b.getX() + bulletSpeed * Math.cos(b.getAngle());
                         double dy = b.getY() + bulletSpeed * Math.sin(b.getAngle());
                         
                         // 총알 다음 위치가 맵 영역안에 존재하는가?
                         if( dx>0 && dx<mapBulletCollision.getWidth() && dy>0 && dy<mapBulletCollision.getHeight()) {
                            if( mapBulletCollision.getRGB( (int)dx, (int)dy ) >= -1 ) {
                               b.setX(dx);
                               b.setY(dy);
                            }else {
                               removeBullet.add(i);
                            }
                         }else {
                            removeBullet.add(i);
                         }
                         i++;
                      }else {
                         removeBullet.add(i);
                      }
                   }
                   
                   int m = 0;
                   for (int k = 0; k < removeBullet.size(); k++) {
                      int temp = removeBullet.get(k) - m;
                      tempBulletList.remove(temp);
                      m++;
                   }
                   
                   sendGameModel.setBulletList(tempBulletList);
                   
                   Game_model_msg(gson.toJson(sendGameModel));
        	 }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      
      
      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         
         if (charX<panelW/2) {
            if (charY < panelH/2) {
               g.drawImage(mapImg, 0, 0, panelW, panelH, 0, 0, 0+panelW, 0+panelH, null);
//               g.drawImage(rotatedCharacter, charX-offsetX, charY-offsetY, rotatedCharacterW,rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX, (int)b.getY()-bulletOffsetY, rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage( rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX, gm.getPlayer().getY()-offsetY, rotatedCharacterW,rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(gm.getPlayer().getX()-25, gm.getPlayer().getY()-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4, gm.getPlayer().getY()-43);
                  }else {
                     g.drawImage( rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX, gm.getPlayer().getY()-offsetY, rotatedCharacterW,rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25, gm.getPlayer().getY()-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4, gm.getPlayer().getY()-43);
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX, (int)b.getY()-bulletOffsetY, rotatedBulletW, rotatedBulletH, null);                        
                     }
                  }
               } 
               
            }else if(charY>=panelH/2 && charY <= (mapH-panelH/2)){
               g.drawImage(mapImg, 0, 0, panelW, panelH, 0, charY-panelH/2, 0+panelW, charY-panelH/2+panelH, null);
//               g.drawImage(rotatedCharacter, charX-offsetX, panelH/2-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX, (int)b.getY()-bulletOffsetY-(charY-panelH/2), rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX, panelH/2-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(gm.getPlayer().getX()-25, panelH/2-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4, panelH/2-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX, gm.getPlayer().getY()-offsetY-(charY-panelH/2), rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25, gm.getPlayer().getY()-40-(charY-panelH/2), 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4, gm.getPlayer().getY()-43-(charY-panelH/2));
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX, (int)b.getY()-bulletOffsetY-(charY-panelH/2), rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }
               
            }else {
               g.drawImage(mapImg, 0, 0, panelW, panelH, 0, mapH-panelH, 0+panelW, mapH-panelH+panelH, null);
//               g.drawImage(rotatedCharacter, charX-offsetX, panelH-(mapH-charY)-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX, (int)b.getY()-bulletOffsetY-(mapH-panelH), rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX, panelH-(mapH-gm.getPlayer().getY())-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(gm.getPlayer().getX()-25, panelH-(mapH-gm.getPlayer().getY())-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4, panelH-(mapH-gm.getPlayer().getY())-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX, gm.getPlayer().getY()-offsetY-(mapH-panelH), rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25, gm.getPlayer().getY()-40-(mapH-panelH), 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4, gm.getPlayer().getY()-43-(mapH-panelH));
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX, (int)b.getY()-bulletOffsetY-(mapH-panelH), rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }

            }
         }else if(charX>=panelW/2 && charX <= (mapW-panelW/2)){
            if (charY < panelH/2) {
               g.drawImage(mapImg, 0, 0, panelW, panelH, charX-panelW/2, 0, charX-panelW/2+panelW, 0+panelH, null);
//               g.drawImage(rotatedCharacter, panelW/2-offsetX, charY-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(charX-panelW/2), (int)b.getY()-bulletOffsetY, rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), panelW/2-offsetX, gm.getPlayer().getY()-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(panelW/2-25, gm.getPlayer().getY()-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), panelW/2 - Integer.toString(gm.getPlayer().getStz_username()).length()*4, gm.getPlayer().getY()-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX-(charX-panelW/2), gm.getPlayer().getY()-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25-(charX-panelW/2), gm.getPlayer().getY()-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4-(charX-panelW/2), gm.getPlayer().getY()-43);
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(charX-panelW/2), (int)b.getY()-bulletOffsetY, rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }
               
            }else if(charY>=panelH/2 && charY <= (mapH-panelH/2)){
               g.drawImage(mapImg, 0, 0, panelW, panelH, charX-panelW/2, charY-panelH/2, charX-panelW/2+panelW, charY-panelH/2+panelH, null);
//               g.drawImage(rotatedCharacter, panelW/2-offsetX, panelH/2-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(charX-panelW/2), (int)b.getY()-bulletOffsetY-(charY-panelH/2), rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), panelW/2-offsetX, panelH/2-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(panelW/2-25, panelH/2-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), panelW/2 - Integer.toString(gm.getPlayer().getStz_username()).length()*4, panelH/2-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX-(charX-panelW/2), gm.getPlayer().getY()-offsetY-(charY-panelH/2), rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25-(charX-panelW/2), gm.getPlayer().getY()-40-(charY-panelH/2), 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4-(charX-panelW/2), gm.getPlayer().getY()-43-(charY-panelH/2));
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(charX-panelW/2), (int)b.getY()-bulletOffsetY-(charY-panelH/2), rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }
               
            }else {
               g.drawImage(mapImg, 0, 0, panelW, panelH, charX-panelW/2, mapH-panelH, charX-panelW/2+panelW, mapH-panelH+panelH, null);
//               g.drawImage(rotatedCharacter, panelW/2-offsetX, panelH-(mapH-charY)-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(charX-panelW/2), (int)b.getY()-bulletOffsetY-(mapH-panelH), rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), panelW/2-offsetX, panelH-(mapH-gm.getPlayer().getY())-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(panelW/2-25, panelH-(mapH-gm.getPlayer().getY())-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), panelW/2 - Integer.toString(gm.getPlayer().getStz_username()).length()*4, panelH-(mapH-gm.getPlayer().getY())-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX-(charX-panelW/2), gm.getPlayer().getY()-offsetY-(mapH-panelH), rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25-(charX-panelW/2), gm.getPlayer().getY()-40-(mapH-panelH), 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4-(charX-panelW/2), gm.getPlayer().getY()-43-(mapH-panelH));
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(charX-panelW/2), (int)b.getY()-bulletOffsetY-(mapH-panelH), rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }
               
            }
         }else {
            if (charY < panelH/2) {
               g.drawImage(mapImg, 0, 0, panelW, panelH, mapW-panelW, 0, mapW-panelW+panelW, 0+panelH, null);
//               g.drawImage(rotatedCharacter, panelW-(mapW-charX)-offsetX, charY-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(mapW-panelW), (int)b.getY()-bulletOffsetY, rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), panelW-(mapW-gm.getPlayer().getX())-offsetX, gm.getPlayer().getY()-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(panelW-(mapW-gm.getPlayer().getX())-25, gm.getPlayer().getY()-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), panelW-(mapW-gm.getPlayer().getX()) - Integer.toString(gm.getPlayer().getStz_username()).length()*4, gm.getPlayer().getY()-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX-(mapW-panelW), gm.getPlayer().getY()-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25-(mapW-panelW), gm.getPlayer().getY()-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4-(mapW-panelW), gm.getPlayer().getY()-43);
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(mapW-panelW), (int)b.getY()-bulletOffsetY, rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }
               
            }else if(charY>=panelH/2 && charY <= (mapH-panelH/2)){
               g.drawImage(mapImg, 0, 0, panelW, panelH, mapW-panelW, charY-panelH/2, mapW-panelW+panelW, charY-panelH/2+panelH, null);
//               g.drawImage(rotatedCharacter, panelW-(mapW-charX)-offsetX, panelH/2-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(mapW-panelW), (int)b.getY()-bulletOffsetY-(charY-panelH/2), rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), panelW-(mapW-gm.getPlayer().getX())-offsetX, panelH/2-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(panelW-(mapW-gm.getPlayer().getX())-25, panelH/2-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), panelW-(mapW-gm.getPlayer().getX()) - Integer.toString(gm.getPlayer().getStz_username()).length()*4, panelH/2-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX-(mapW-panelW), gm.getPlayer().getY()-offsetY-(charY-panelH/2), rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25-(mapW-panelW), gm.getPlayer().getY()-40-(charY-panelH/2), 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4-(mapW-panelW), gm.getPlayer().getY()-43-(charY-panelH/2));
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(mapW-panelW), (int)b.getY()-bulletOffsetY-(charY-panelH/2), rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }
               
            }else {
               g.drawImage(mapImg, 0, 0, panelW, panelH, mapW-panelW, mapH-panelH, mapW-panelW+panelW, mapH-panelH+panelH, null);
//               g.drawImage(rotatedCharacter, panelW-(mapW-charX)-offsetX, panelH-(mapH-charY)-offsetY, rotatedCharacterW, rotatedCharacterH, null);
//               
//               for (Bullet b : bulletList) {
//                  g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(mapW-panelW), (int)b.getY()-bulletOffsetY-(mapH-panelH), rotatedBulletW, rotatedBulletH, null);
//               }
               
               for (GameModel gm : receiveGameModelList.getGameModelList()) {
                  if(gm.getPlayer().getStz_username() == username) {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), panelW-(mapW-gm.getPlayer().getX())-offsetX, panelH-(mapH-gm.getPlayer().getY())-offsetY, rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(181,230,29));
                     g.fillRect(panelW-(mapW-gm.getPlayer().getX())-25, panelH-(mapH-gm.getPlayer().getY())-40, 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), panelW-(mapW-gm.getPlayer().getX()) - Integer.toString(gm.getPlayer().getStz_username()).length()*4, panelH-(mapH-gm.getPlayer().getY())-43);
                  }else {
                     g.drawImage(rotate(character, gm.getPlayer().getAngle()), gm.getPlayer().getX()-offsetX-(mapW-panelW), gm.getPlayer().getY()-offsetY-(mapH-panelH), rotatedCharacterW, rotatedCharacterH, null);
                     g.setColor(new Color(237,28,36));
                     g.fillRect(gm.getPlayer().getX()-25-(mapW-panelW), gm.getPlayer().getY()-40-(mapH-panelH), 50*gm.getPlayer().getCurHp()/100, 10);
                     g.setColor(Color.black);
                     g.drawString(Integer.toString(gm.getPlayer().getStz_username()), gm.getPlayer().getX() - Integer.toString(gm.getPlayer().getStz_username()).length()*4-(mapW-panelW), gm.getPlayer().getY()-43-(mapH-panelH));
                  }
                  for (Bullet b : gm.getBulletList()) {
                     if (b.getDmg() > 0) {
                        g.drawImage(rotateBullet(bulletImg, b.getAngle()), (int)b.getX()-bulletOffsetX-(mapW-panelW), (int)b.getY()-bulletOffsetY-(mapH-panelH), rotatedBulletW, rotatedBulletH, null);   
                     }
                  }
               }
            }
         }

      }

      
   }
   
   
   private void setAngle(int charX, int charY, int mouseX, int mouseY) {
      int triangleBottom = mouseX - charX;
      int triangleHeight = mouseY - charY;
      
      if (triangleBottom > 0) {
         angle = Math.atan( (double)triangleHeight / triangleBottom ); //+ Math.PI/2;                  
      }else if(triangleBottom < 0) {
         angle = Math.atan( (double)triangleHeight / triangleBottom ) + Math.PI;// + Math.PI/2;
      }else {
         if(triangleHeight > 0) {
            angle = Math.PI/2;// + Math.PI/2;
         }else if(triangleHeight < 0 ) {
            angle = -Math.PI/2;// + Math.PI/2;
         }else {
            angle = 0;// + Math.PI/2;
         }
      }
   }
   
   private BufferedImage rotate(BufferedImage image, double angle) {
       result = gc.createCompatibleImage(newBuffImageW, newBuffImageH, Transparency.TRANSLUCENT);
       g = result.createGraphics();       
       
       g.translate( (newBuffImageW - image.getWidth())/2, (newBuffImageH - image.getHeight())/2 );
       g.rotate(angle, rotateAxisX, rotateAxisY);
       
       g.drawRenderedImage(image, null);
       g.dispose();
       return result;
   }
   
   private BufferedImage rotateBullet(BufferedImage image, double angle) {
       result = gc.createCompatibleImage(bulletNewBuffImageW, bulletNewBuffImageH, Transparency.TRANSLUCENT);
       g = result.createGraphics();       
       
       g.translate( (bulletNewBuffImageW - image.getWidth())/2, (bulletNewBuffImageH - image.getHeight())/2 );
       g.rotate(angle, bulletRotateAxisX, bulletRotateAxisY);
       
       g.drawRenderedImage(image, null);
       g.dispose();
       return result;
   }

   private GraphicsConfiguration getDefaultConfiguration() {
       GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
       GraphicsDevice gd = ge.getDefaultScreenDevice();
       return gd.getDefaultConfiguration();
   }
   
   
   
//   public static void main(String[] args) {
//      new GameFrame(null, null, null);
//   }
   
}