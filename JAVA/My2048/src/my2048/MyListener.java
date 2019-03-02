package my2048;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

import javax.swing.*;



public class MyListener extends KeyAdapter implements ActionListener {
	private Game UI;
	private int Numbers[][];
	private JLabel lb;
	private JButton start, back , about;
	private int BackUp[][] = new int[4][4];
	private int BackUp2[][] = new int[4][4];
	private Random rand = new Random();
	private boolean isWin = false;
	private JCheckBox isSoundBox;
	private int score = 0;
	private int tempscore,tempscore2;
	private boolean hasBack = false, relive = false, isSound = true;
	
	
	public MyListener(Game UI, JButton start, JButton back, JButton about, JLabel lb, JCheckBox isSoundBox, int Numbers[][]) {
		this.UI = UI;
		this.about = about;
		this.back = back;
		this.lb = lb;
		this.start = start;
		this.isSoundBox = isSoundBox;		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==start) {
			isWin = false;
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					Numbers[i][j]=0;
				}				
			}
			score = 0;
			lb.setText("当前分数： " + score);
			int c1 = rand.nextInt(4);
			int c2 = rand.nextInt(4);
			int r1 = rand.nextInt(4);
			int r2 = rand.nextInt(4);
			
			while (r1 == r2 && c1 == c2) {
				r2 = rand.nextInt(4);
				c2 = rand.nextInt(4);
			}
			
			int value1 = rand.nextInt(2) *2 +2;
			int value2 = rand.nextInt(2) *2 +2;
			
			Numbers[r1][c1] = value1;
			Numbers[r2][c2] = value2;
			UI.paint(UI.getGraphics());
			
		}else if(e.getSource() == about) {
			JOptionPane.showMessageDialog(UI,"游戏玩法\n"); //第一个参数是控制弹出对话框相对的中心位置，如果是null，则是在屏幕中间，如果是其它组件参数，则会在其它组件的中心弹出。

		}else if(e.getSource() == back && hasBack == false){
			hasBack = true;
			if(relive = false) {
				score = tempscore;
				lb.setText("当前分数： " + score);
				for (int i = 0; i < BackUp.length; i++) {
					Numbers[i] = Arrays.copyOf(BackUp[i], BackUp.length);
				} 	
				
			}else {
				score = tempscore2;
				lb.setText("当前分数： " + score);
				for(int i = 0; i < BackUp2.length; i++) {
					Numbers[i] = Arrays.copyOf(BackUp2[i], BackUp2.length);
				}
				relive = false;
			}
			UI.paint(UI.getGraphics());	
		}		
	}
	
	
	public void keyPressed(KeyEvent event) {
		int Counter = 0;
		int NumNearCounter = 0;
		int NumCounter = 0;
		
		hasBack = false;

		if (BackUp != null || BackUp.length != 0) {
			tempscore2 = tempscore;
			// 退两步
			for (int i = 0; i < BackUp.length; i++) {
				BackUp2[i] = Arrays.copyOf(BackUp[i], BackUp[i].length);
			}
		}

		tempscore = score;
		// 退一步
		for (int i = 0; i < Numbers.length; i++) {
			BackUp[i] = Arrays.copyOf(Numbers[i], Numbers[i].length);
		}
		
		if(isWin == false) {
			switch (event.getKeyCode()) {
			case 37: //left
				for (int i = 0; i < 4; i++) {//左移
					for (int j = 0; j < 4; j++) {
						if(Numbers[i][j] != 0) {}
						int temp = Numbers[i][j];
						int pre = j-1;
						while(pre>=0 && Numbers[i][pre] ==0) {
							Numbers[i][pre] = temp;
							Numbers[i][pre + 1] = 0;
							pre--;
							Counter++;
						}				
					}			
				}
				
				for (int i = 0; i < 4; i++) {//合并
					for (int j = 0; j < 4; j++) {
						if(j+1 <4 && (Numbers[i][j] == Numbers[i][j+1])&& (Numbers[i][j] != 0 || Numbers[i][j+1] != 0) ) {
							Numbers[i][j] = Numbers[i][j] + Numbers[i][j+1];
							Numbers[i][j+1] = 0;
							score+=Numbers[i][j];
							Counter++;
							if(Numbers[i][j]==2048) {
								isWin=true;
							}
						}
					}
				}
				
				break;
				
			case 39: //right
				for (int i = 3; i >= 0; i--) {
					for (int j = 3; j >= 0; j--) {
						if(Numbers[i][j] != 0) {}
						int temp = Numbers[i][j];
						int pre = j+1;
						while(pre<=3 && Numbers[i][pre] ==0) {
							Numbers[i][pre] = temp;
							Numbers[i][pre - 1] = 0;
							pre++;
							Counter++;
						}				
					}			
				}
				
				for (int i = 3; i >= 0; i--) {
					for (int j = 3; j >= 0; j--) {
						if(j+1 <4 && (Numbers[i][j] == Numbers[i][j+1])&& (Numbers[i][j] != 0 || Numbers[i][j+1] != 0) ) {
							Numbers[i][j+1] = Numbers[i][j] + Numbers[i][j+1];
							Numbers[i][j] = 0;
							score+=Numbers[i][j+1];
							Counter++;
							if(Numbers[i][j+1]==2048) {
								isWin=true;
							}
						}
					}
				}
				
				break;
				
			case 38: //up
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						if(Numbers[i][j] != 0) {}
						int temp = Numbers[i][j];
						int pre = i-1;
						while(pre>=0 && Numbers[i][pre] ==0) {
							Numbers[pre][j] = temp;
							Numbers[pre+1][j] = 0;
							pre--;
							Counter++;
						}				
					}			
				}
				
				for (int i = 0; i < 4; i++) {//合并
					for (int j = 0; j < 4; j++) {
						if(j+1 <4 && (Numbers[i][j] == Numbers[i+1][j])&& (Numbers[i][j] != 0 || Numbers[i+1][j] != 0) ) {
							Numbers[i][j] = Numbers[i][j] + Numbers[i+1][j];
							Numbers[i+1][j] = 0;
							score+=Numbers[i][j];
							Counter++;
							if(Numbers[i][j]==2048) {
								isWin=true;
							}
						}
					}
				}
				
				break;
				

			case 40: //down
				for (int i = 3; i >= 0; i--) {
					for (int j = 3; j >= 0; j--) {
						if(Numbers[i][j] != 0) {}
						int temp = Numbers[i][j];
						int pre = i+1;
						while(pre<=3 && Numbers[i][pre] ==0) {
							Numbers[pre][j] = temp;
							Numbers[pre-1][j] = 0;
							pre++;
							Counter++;
						}				
					}			
				}
				
				for (int i = 3; i >= 0; i--) {
					for (int j = 3; j >= 0; j--) {
						if(j+1 <4 && (Numbers[i][j] == Numbers[i-1][j])&& (Numbers[i][j] != 0 || Numbers[i-1][j] != 0) ) {
							Numbers[i-1][j] = Numbers[i][j] + Numbers[i-1][j];
							Numbers[i][j] = 0;
							score+=Numbers[i-1][j];
							Counter++;
							if(Numbers[i-1][j]==2048) {
								isWin=true;
							}
						}
					}
				}
				
				break;
				
			}
		
		
		
		
		for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	        if (Numbers[i][j] == Numbers[i][j + 1]&& Numbers[i][j] != 0) {
	                NumNearCounter++;
	        }
	        if (Numbers[i][j] == Numbers[i + 1][j]&& Numbers[i][j] != 0) {
	               NumNearCounter++;
	        }
	        if (Numbers[3][j] == Numbers[3][j + 1]&& Numbers[3][j] != 0) {
	               NumNearCounter++;
	        }
	        if (Numbers[i][3] == Numbers[i + 1][3]&& Numbers[i][3] != 0) {
	               NumNearCounter++;
	            }
	        }
	        
		}
		
	       for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						if (Numbers[i][j] != 0) {
							NumCounter++;
						}
					}
	       }
						
			if (Counter > 0) {

							lb.setText("当前分数" + score);
							int r1 = rand.nextInt(4);
							int c1 = rand.nextInt(4);
							while (Numbers[r1][c1] != 0) {
								r1 = rand.nextInt(4);
								c1 = rand.nextInt(4);
							}
							int value1 = rand.nextInt(2) * 2 + 2;
							Numbers[r1][c1] = value1;
						}
			if (isWin == true){
				UI.paint(UI.getGraphics());
				JOptionPane.showMessageDialog(UI, "恭喜你，你赢了" + score);
			}
	
		if(NumNearCounter ==0 && NumCounter == 16) {
			relive = true;
			JOptionPane.showMessageDialog(UI, "Game Over");
		}
		UI.paint(UI.getGraphics());
		
		}
	

	}
	}

