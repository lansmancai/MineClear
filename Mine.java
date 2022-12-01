package MineClear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Deque;
import java.util.LinkedList;
/**
 * @author jinniu
 * #Description MineClear
 * #Date: 1/12/2022 11:00
 */
public class Mine {
    //定义一个二维数组做为布置地雷的雷池
    private String[][] board;
	//定义一个二维数组做为随着用户一步步扫雷而逐步探雷的雷池状况并展示
	private String[][] showboard;
	//在用户扫雷时需要对board数组进行计算并篡改防止死循环，故用copyboard保存一个布置好地雷后未受破坏的雷池
	private String[][] copyboard;
	//定义棋盘宽度，长度将是宽的2倍
    private static int BOARD_SIZE = 10;
	//定义地雷数量
	private static int MINE_NUMBER = 30;
    public void initBoard(){
                board= new String[BOARD_SIZE][BOARD_SIZE*2];
				showboard = new String[BOARD_SIZE][BOARD_SIZE*2];
				copyboard = new String[BOARD_SIZE][BOARD_SIZE*2];
				
		//把每个元素赋为"0"，用于在控制台画出棋盘
        for(int i = 0;i< BOARD_SIZE; i++){
			for( int j=0;j< BOARD_SIZE*2;j++){
                board[i] [j] = "0";
				showboard[i][j] = "+";
           }
        }
	}
	public void minerandom(){	
		//随机选择位置生成MINE_NUMBER个雷，标记为*
		for(int k = 0; k < MINE_NUMBER; k++) {
			int m = (int)(BOARD_SIZE * Math.random());
			int n = (int)(BOARD_SIZE * 2 * Math.random());
			//System.out.println(m);
			//System.out.println(n);
			board[m][n] = "*";
		}
	}
	
	public void hint() {
		//添加上雷的提示，既计算出周边8个格子地雷的数量并填写
		for (int i = 0;i < BOARD_SIZE;i++){
            for (int j= 0;j< BOARD_SIZE*2 ;j++){
				if (board[i][j] == "*") continue;
				int sum = 0;
				int[] lx = {-1, -1, -1, 0, 0, 1, 1, 1};
			    int[] ly = {-1, 0, 1, -1, 1, -1, 0, 1};
			    for(int k = 0; k < 8; k++){
				    int nextx = i + lx[k];
				    int nexty = j + ly[k];
				    if ((-1 < nextx )&&(nextx < BOARD_SIZE)&&(-1 < nexty)&& (nexty < BOARD_SIZE * 2)) {
						if(board[nextx][nexty] == "*") {
							sum++;
						}
					}
				}
				//sum转为字符串类型
				board[i][j] = sum + "";
		    }
        }
    }
	public void copyb() {
		//将布置好地雷和地雷数提示的board进行拷贝保存
		for (int i = 0;i < BOARD_SIZE;i++){
            for (int j= 0;j< BOARD_SIZE*2 ;j++){
				copyboard[i][j] = board[i][j];
			}
		}
	}
		
    public void pb(){
		//调试时用来展示雷池全景
        for (int i = 0;i < BOARD_SIZE;i++){
            for (int j= 0;j< BOARD_SIZE*2 ;j++){
				//打印数组元素后不换行
                System.out.print(board[i][j]);
            }
				//每打印完一行数组元素后输出一个换行符
                System.out.print ("\n");
        }
    }
	
	public void show(){
		//展示showboard
		for (int i = 0;i < BOARD_SIZE;i++){
			for (int j= 0;j< BOARD_SIZE*2 ;j++){
				//打印数组元素后不换行
                System.out.print(showboard[i][j]);
            }
				//每打印完一行数组元素后输出一个换行符
                System.out.print ("\n");
		}
	} 
	
	public void boom(int x,int y) {
		//实现点击到提示为0既周边8个格子都没有地雷时会有爆炸效果
		Deque<Integer> stackx = new LinkedList<Integer>();
        Deque<Integer> stacky = new LinkedList<Integer>();
        stackx.push(x);
        stacky.push(y);
		while (!stackx.isEmpty()) {
			//用栈进行遍历进而实现爆炸效果
			x = stackx.pop();
			y = stacky.pop();
			if (Integer.parseInt(board[x][y]) == 0){
		    showboard[x][y] = board[x][y];
			board[x][y] = "-1";
			int[] lx = {-1, -1, -1, 0, 0, 1, 1, 1};
			int[] ly = {-1, 0, 1, -1, 1, -1, 0, 1};
			for(int k = 0; k < 8; k++){
				int nextx = x + lx[k];
				int nexty = y + ly[k];
				if ((-1 < nextx )&&(nextx < BOARD_SIZE)&&(-1 < nexty)&& (nexty < BOARD_SIZE * 2)) {
						if(Integer.parseInt(board[nextx][nexty])!= -1) {
							showboard[nextx][nexty] = board[nextx][nexty];
						}
						if(Integer.parseInt(board[nextx][nexty])== 0) {
							stackx.push(nextx);
							stacky.push(nexty);
						}
				}
		    }
		    }
		}
	}
	
		  
	public void count(int x, int y) {
		//踩到雷则打印出全部雷池数据并告知游戏结束
		if (board[x][y] == "*") {
			for(int i = 0;i< BOARD_SIZE; i++){
				for( int j=0;j< BOARD_SIZE*2;j++){
				showboard[i][j] = copyboard[i][j];
				}
			}
			System.out.println("Game over!\n");
			show();
			System.exit(0);
		}
		if (Integer.parseInt(board[x][y]) > 0) {
			//点开是提示则打开提示即可
			showboard[x][y] = board[x][y];
			return;
		}
		if (Integer.parseInt(board[x][y]) == 0) {
			//如果为0说明周边2-8个格子都没有雷，若点击后周边的格子还有0，将会有爆炸效果
			boom(x, y );
			
		}
	}
	
	
		  
    public static void main(String[] args) throws Exception 
	{
		Mine mn = new Mine ();
		//初始化
        mn.initBoard();
		//随机生成地雷
		mn.minerandom();
		//根据随机生成的地雷填好提示
		mn.hint();
		//保存好填好地雷和地雷的雷池数据一份
		mn.copyb();
		//调试时可用mn.pb()打开雷池数据全景
		//mn.pb();
		//逐步展示用户扫描全景
		mn.show();
		System.out.println("请输入你要扫雷的坐标，x,y的方式按回车");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputStr = null;
        //br.readLine（）∶ 每当在键盘上输入一行内容按回车键，刚输入的内容将被br 读取
        while ((inputStr = br.readLine()) != null){
            //将用户输入的字符串以逗号（，）作为分隔符，分隔成两个字符串
            String[] posStrArr= inputStr.split(",");
		//将两个字符串转换成用户下棋的坐标
        int xPos = Integer.parseInt(posStrArr[0]);
        int yPos = Integer.parseInt(posStrArr[1]);
		//System.out.println(xPos);
		//System.out.println(yPos);
		//Windows下Java清空控制台的方法未找到，暂时用换行30的方法替代。
		for (int i = 0; i < 30; ++i) System.out.println(); 
		mn.count(xPos , yPos);
		mn.show();
		System.out.println("请输入你要扫雷的坐标，x,y的方式按回车");
        }
    }
}