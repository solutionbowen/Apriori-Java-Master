/**
*作者: Bo-Wen Duan (Paul)
*聯絡方式: bowenduan618@gmail.com
*修改日期: 2016 / 1 / 10
*/
/*
1.基本概念：
關聯規則 A->B 的支持度 support=P(AB)，指的是事件 A和事件 B同時發生的概率。
信賴度confidence=P(B|A)=P(AB)/P(A), 指的是發生事件 A的基礎上發生事件 B的概率。
同時滿足最小支持度門檻值和最小信賴度門檻值的規則稱為強規則 。
如果事件 A中包含 k個元素(項目)，那麼稱這個事件 A為 k-項目集， 並且事件 A滿足最小支持度門檻值的事件稱為高頻k-項集 。 
2.探勘過程：
第一，找出所有的高頻項目集；
第二，由高頻項目集產生強規則。
Apriori演算法採用"合併步驟"和"計算支持度之前判斷本身的子集合是否高頻"兩種方式來找出所有的高頻項目集。
*/
package AprioriMain;
import java.io.*;
import java.util.*;
public class GenerateIargeItemSet {
	private final static ArrayList<String> transactions = new ArrayList<>(); //將所有交易用ArrayList來存
	private final static int support = 2000; //支持個數設定
	private final static double confidence = 0.7; //信賴度設定
	private final static String InputFileName = "retail.txt";
	private final static String OutputFileName = "MiningResult.txt";
	public static void main(String[] args) throws IOException{    //主函式
		 long begin = System.currentTimeMillis();
		 FileReader fr = new FileReader(InputFileName); 
		 FileWriter fw = new FileWriter(OutputFileName);
         BufferedReader br = new BufferedReader(fr);
         BufferedWriter bw = new BufferedWriter(fw);
         String line;
         int transactionscount = 0;
         while((line = br.readLine())!=null){        //將原始交易資料逐行讀進來
        	 transactions.add(line);
        	 transactionscount++;                    //讀檔同時每一行記數一次(統計交易筆數)
         }
         bw.write("支持個數為:"); bw.write(String.format("%d",support));  bw.newLine();
         bw.write("總共有"); bw.write(String.format("%d",transactionscount)); bw.write("筆交易"); bw.newLine();
         HashMap<String,Integer> frequentMap = getfrequentItemSet();
         List<Map.Entry<String, Integer>> list_Tran =
                 new ArrayList<Map.Entry<String, Integer>>(frequentMap.entrySet());
         Collections.sort(list_Tran, new Comparator<Map.Entry<String, Integer>>(){
             public int compare(Map.Entry<String, Integer> entry1,Map.Entry<String, Integer> entry2){
                 int entry1len = entry1.getKey().length();
                 int entry2len = entry2.getKey().length();
            	 if(entry1len < entry2len)
            		 return -1;
            	 if(entry1len > entry2len)
            		 return 1;
            	 return entry1.getKey().compareTo(entry2.getKey());
             }
         });
         bw.write("---------------- 高頻項目集 "+"------------------");
         bw.newLine();
         for (Map.Entry<String, Integer> entry:list_Tran) {
             bw.write("項目集:" + entry.getKey() + "支持個數" + entry.getValue());
             bw.newLine();
         }
	  long over = System.currentTimeMillis();
	  long millisecond = (over - begin) % 1000;
	  int second = (int)(over - begin)/1000 % 60;
	  int minute = (int)(over - begin)/1000 / 60;
	  bw.write("執行時間為： "+ minute + "分" + second + "秒" + millisecond + "毫秒" );
	  br.close();
	  bw.close();
	  sop("Mining結束，所探勘出來的結果已寫入至"+ OutputFileName +"裡");
	 }
	/*
	Function 名稱: getfrequent1Item()
	Function 輸出: 回傳高頻1-項目集。
	Function 執行過程: 1.將已經存入transactions裡面所有的交易，一個一個(也就是一行一行的交易)，宣告一個String型態的trans來接收。
	                2.再來將trans(一行交易)進行切割(切成單一的項目)。
	                3.切割完之後宣告一個sfrequent1ItemMap，HashMap的實體將單一項目當成key，value當作這個項目的支持個數。
	                4.利用宣告一個Integer的count(value)來作為判斷，若為null，count設為1，否則所取出的count再加1。
	                5.形成最後的sfrequent1ItemMap(相異的單一項目,項目的支持個數)
	                6.宣告Set<String>型態的keySet將sfrequent1ItemMap的所有key取出來。
	                7.利用for-each迴圈宣告一個String型態key將keySet取進去。
	                8.利用count&sfrequent1ItemMap.get(key)以及support，if迴圈來判斷是否高頻，同時列出所有候選1-項目集。
	                9.若是高頻將此(key,value)put進另外宣告的HashMap實體rfrequent1ItemMap。
	               10.宣告一個Set<String>型態的keySet1將rfrequent1ItemMap的所有key取出來。
	               11.最後利用for迴圈依序印出所有高頻1-項目集
	*/
	public static HashMap<String,Integer> getfrequent1Item(){    
		 HashMap<String,Integer> sfrequent1ItemMap = new HashMap<>(); //候選1-項目集
		 HashMap<String,Integer> rfrequent1ItemMap = new HashMap<>(); //高頻1-項目集
		 for(int i = 0 ; i < transactions.size() ; i++){
			 String trans  = transactions.get(i);
			 String[] items = trans.split(" ");
               for(int j = 0 ; j < items.length ; j++){
                  String item = items[j];
                  Integer count = sfrequent1ItemMap.get(item + " ");
                    if(count==null){
                    	sfrequent1ItemMap.put(item + " ", 1);
                    }else{
                    	sfrequent1ItemMap.put(item + " ", count+1);
                    }
               }
         }
         Set<String> keySet = sfrequent1ItemMap.keySet();
         for(String key:keySet){
              Integer count = sfrequent1ItemMap.get(key);
              if(count >= support){
            	  rfrequent1ItemMap.put(key, count);
              }
          }
        return rfrequent1ItemMap;
     }
	/*
	Function 名稱: getfrequentItemSet()
	Function 輸出: 回傳所有高頻k-項目集(k >= 2)
	Function 執行過程:
	*/
	public static HashMap<String,Integer> getfrequentItemSet(){
	     HashMap<String,Integer> frequentSetMap = new HashMap<>();  //所有的高頻項目集
	     HashMap<String,Integer> frequentkitemMap = new HashMap<>();
	     frequentSetMap.putAll(getfrequent1Item());
	     frequentkitemMap.putAll(getfrequent1Item());
	     while(frequentkitemMap != null && frequentkitemMap.size() != 0){
	    	HashMap<String,Integer> candidateSet = getCandidateItemSet(frequentkitemMap);
	        Set<String> csKeySet = candidateSet.keySet();
	        //對候選目集項進行count累加
	        for(String trans:transactions){
	        	String[] items = trans.split(" ");
	        	List<String> list = Arrays.asList(items);
	        	for(String candidate:csKeySet){
	                boolean flag=true;  //用來判斷交易中是否出現該候選項目集，如果出現，count加 1
	                String[] candidateItems = candidate.split(" ");
	                for(String candidateItem:candidateItems){
	                      if(list.indexOf(candidateItem) == -1){
	                           flag = false;
	                           break;
	                       }
	                }
	                if(flag){
	                  Integer count = candidateSet.get(candidate);
	                  candidateSet.put(candidate, count+1);
	                }
	          }
	        }
	       //從候選項目集中找到符合支持度的高頻項目集
	       frequentkitemMap.clear();
	       for(String candidate:csKeySet){
	           Integer count = candidateSet.get(candidate);
	           if(count >= support){
	        	   frequentkitemMap.put(candidate, count);
	           }
	        }
	        //合併所有高頻項目集
	       frequentSetMap.putAll(frequentkitemMap);
	    }
	    return frequentSetMap;
	 }
	/*
	Function 名稱: getCandidateItemSet(Map<String,Integer> frequentkitemMap)
	Function 輸出: 回傳所有候選k-項目集(k >= 2)
	Function 執行過程: 
	*/
	private static HashMap<String,Integer> getCandidateItemSet(Map<String,Integer> frequentkitemMap){
		 HashMap<String,Integer> candidateSet = new HashMap<String,Integer>();
         Set<String> kitemSet1 = frequentkitemMap.keySet();
         Set<String> kitemSet2 = frequentkitemMap.keySet();
         for(String kitem1:kitemSet1){
           for(String kitem2:kitemSet2){
                //合併步驟
                String[] tmp1 = kitem1.split(" ");   //切出來會是一個個1-高頻項目集(String型態)
                String[] tmp2 = kitem2.split(" ");   //切出來會是一個個1-高頻項目集(String型態)
                String c = "";
                   if(tmp1.length == 1){             //矩陣中只有單一個項目集(矩陣tmp1會跟矩陣tmp2長度一樣)
                          if(tmp1[0].compareTo(tmp2[0]) < 0){       //字串比對(依照數字和英文字母排序比對)
                                c = tmp1[0] + " " + tmp2[0] +" ";
                          }
                   }else{
                          boolean flag = true;
                          for(int i = 0 ; i < tmp1.length-1 ; i++){  //tmp1和temp2矩陣的index從0~length-2分別比對是否一樣
                             if(!tmp1[i].equals(tmp2[i])){           //中間出現不同時flag改為false，跳出迴圈
                                  flag = false;
                                  break;
                             }
                          }
                          if(flag && (tmp1[tmp1.length-1].compareTo(tmp2[tmp2.length-1]) < 0)){ //比tmp1和temp2矩陣各自最後一個的元素(String型態)
                            c = kitem1 + tmp2[tmp2.length-1]+" ";
                          }
                    }
                //計算支持度之前判斷本身的子集合是否高頻
                boolean hasInfrequentSubSet = false;
                   if (!c.equals("")) {
                       String[] tmpC = c.split(" ");        //對String型態c(候選項目集)進行切割
                       for (int i = 0 ; i < tmpC.length ; i++) {
                           String SubSetC = "";
                           for (int j = 0 ; j < tmpC.length ; j++) {
                               if (i != j) {
                                 SubSetC = SubSetC + tmpC[j] + " ";
                               }
                           }
                           if (frequentkitemMap.get(SubSetC) == null) {   //代表候選項目集的子項目集出現非高頻的情況
                               hasInfrequentSubSet = true;
                               break;
                           }
                       }
                   }else{
                       hasInfrequentSubSet=true;
                   }
                   if(!hasInfrequentSubSet){
                       candidateSet.put(c, 0);
                   }
           }
         }
        return candidateSet;
    }
	public static void sop(Object obj){  //列印的function
	    	System.out.println(obj);
	 }
}
