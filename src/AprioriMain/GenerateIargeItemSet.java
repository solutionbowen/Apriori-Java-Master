/*
1.�򥻷����G
���p�W�h A->B ������� support=P(AB)�A�����O�ƥ� A�M�ƥ� B�P�ɵo�ͪ����v�C
�H���confidence=P(B|A)=P(AB)/P(A), �����O�o�ͨƥ� A����¦�W�o�ͨƥ� B�����v�C
�P�ɺ����̤p����ת��e�ȩM�̤p�H��ת��e�Ȫ��W�h�٬��j�W�h �C
�p�G�ƥ� A���]�t k�Ӥ���(����)�A����ٳo�Өƥ� A�� k-���ض��A �åB�ƥ� A�����̤p����ת��e�Ȫ��ƥ�٬����Wk-���� �C 
2.���ɹL�{�G
�Ĥ@�A��X�Ҧ������W���ض��F
�ĤG�A�Ѱ��W���ض����ͱj�W�h�C
Apriori�t��k�ĥ�"�X�֨B�J"�M"�p�����פ��e�P�_�������l���X�O�_���W"��ؤ覡�ӧ�X�Ҧ������W���ض��C
*/
package AprioriMain;
import java.io.*;
import java.util.*;
public class GenerateIargeItemSet {
	private final static ArrayList<String> transactions = new ArrayList<>(); //�N�Ҧ������ArrayList�Ӧs
	private final static int support = 2000; //����ӼƳ]�w
	private final static double confidence = 0.7; //�H��׳]�w
	private final static String InputFileName = "retail.txt";
	private final static String OutputFileName = "MiningResult.txt";
	public static void main(String[] args) throws IOException{    //�D�禡
		 long begin = System.currentTimeMillis();
		 FileReader fr = new FileReader(InputFileName); 
		 FileWriter fw = new FileWriter(OutputFileName);
         BufferedReader br = new BufferedReader(fr);
         BufferedWriter bw = new BufferedWriter(fw);
         String line;
         int transactionscount = 0;
         while((line = br.readLine())!=null){        //�N��l�����Ƴv��Ū�i��
        	 transactions.add(line);
        	 transactionscount++;                    //Ū�ɦP�ɨC�@��O�Ƥ@��(�έp�������)
         }
         bw.write("����ӼƬ�:"); bw.write(String.format("%d",support));  bw.newLine();
         bw.write("�`�@��"); bw.write(String.format("%d",transactionscount)); bw.write("�����"); bw.newLine();
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
         bw.write("---------------- ���W���ض� "+"------------------");
         bw.newLine();
         for (Map.Entry<String, Integer> entry:list_Tran) {
             bw.write("���ض�:" + entry.getKey() + "����Ӽ�" + entry.getValue());
             bw.newLine();
         }
	  long over = System.currentTimeMillis();
	  long millisecond = (over - begin) % 1000;
	  int second = (int)(over - begin)/1000 % 60;
	  int minute = (int)(over - begin)/1000 / 60;
	  bw.write("����ɶ����G "+ minute + "��" + second + "��" + millisecond + "�@��" );
	  br.close();
	  bw.close();
	  sop("Mining�����A�ұ��ɥX�Ӫ����G�w�g�J��"+ OutputFileName +"��");
	 }
	/*
	Function �W��: getfrequent1Item()
	Function ��X: �^�ǰ��W1-���ض��C
	Function ����L�{: 1.�N�w�g�s�Jtransactions�̭��Ҧ�������A�@�Ӥ@��(�]�N�O�@��@�檺���)�A�ŧi�@��String���A��trans�ӱ����C
	                2.�A�ӱNtrans(�@����)�i�����(������@������)�C
	                3.���Χ�����ŧi�@��sfrequent1ItemMap�AHashMap������N��@���ط�key�Avalue��@�o�Ӷ��ت�����ӼơC
	                4.�Q�Ϋŧi�@��Integer��count(value)�ӧ@���P�_�A�Y��null�Acount�]��1�A�_�h�Ҩ��X��count�A�[1�C
	                5.�Φ��̫᪺sfrequent1ItemMap(�۲�����@����,���ت�����Ӽ�)
	                6.�ŧiSet<String>���A��keySet�Nsfrequent1ItemMap���Ҧ�key���X�ӡC
	                7.�Q��for-each�j��ŧi�@��String���Akey�NkeySet���i�h�C
	                8.�Q��count&sfrequent1ItemMap.get(key)�H��support�Aif�j��ӧP�_�O�_���W�A�P�ɦC�X�Ҧ��Կ�1-���ض��C
	                9.�Y�O���W�N��(key,value)put�i�t�~�ŧi��HashMap����rfrequent1ItemMap�C
	               10.�ŧi�@��Set<String>���A��keySet1�Nrfrequent1ItemMap���Ҧ�key���X�ӡC
	               11.�̫�Q��for�j��̧ǦL�X�Ҧ����W1-���ض�
	*/
	public static HashMap<String,Integer> getfrequent1Item(){    
		 HashMap<String,Integer> sfrequent1ItemMap = new HashMap<>(); //�Կ�1-���ض�
		 HashMap<String,Integer> rfrequent1ItemMap = new HashMap<>(); //���W1-���ض�
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
	Function �W��: getfrequentItemSet()
	Function ��X: �^�ǩҦ����Wk-���ض�(k >= 2)
	Function ����L�{:
	*/
	public static HashMap<String,Integer> getfrequentItemSet(){
	     HashMap<String,Integer> frequentSetMap = new HashMap<>();  //�Ҧ������W���ض�
	     HashMap<String,Integer> frequentkitemMap = new HashMap<>();
	     frequentSetMap.putAll(getfrequent1Item());
	     frequentkitemMap.putAll(getfrequent1Item());
	     while(frequentkitemMap != null && frequentkitemMap.size() != 0){
	    	HashMap<String,Integer> candidateSet = getCandidateItemSet(frequentkitemMap);
	        Set<String> csKeySet = candidateSet.keySet();
	        //��Կ�ض����i��count�֥[
	        for(String trans:transactions){
	        	String[] items = trans.split(" ");
	        	List<String> list = Arrays.asList(items);
	        	for(String candidate:csKeySet){
	                boolean flag=true;  //�ΨӧP�_������O�_�X�{�ӭԿﶵ�ض��A�p�G�X�{�Acount�[ 1
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
	       //�q�Կﶵ�ض������ŦX����ת����W���ض�
	       frequentkitemMap.clear();
	       for(String candidate:csKeySet){
	           Integer count = candidateSet.get(candidate);
	           if(count >= support){
	        	   frequentkitemMap.put(candidate, count);
	           }
	        }
	        //�X�֩Ҧ����W���ض�
	       frequentSetMap.putAll(frequentkitemMap);
	    }
	    return frequentSetMap;
	 }
	/*
	Function �W��: getCandidateItemSet(Map<String,Integer> frequentkitemMap)
	Function ��X: �^�ǩҦ��Կ�k-���ض�(k >= 2)
	Function ����L�{: 
	*/
	private static HashMap<String,Integer> getCandidateItemSet(Map<String,Integer> frequentkitemMap){
		 HashMap<String,Integer> candidateSet = new HashMap<String,Integer>();
         Set<String> kitemSet1 = frequentkitemMap.keySet();
         Set<String> kitemSet2 = frequentkitemMap.keySet();
         for(String kitem1:kitemSet1){
           for(String kitem2:kitemSet2){
                //�X�֨B�J
                String[] tmp1 = kitem1.split(" ");   //���X�ӷ|�O�@�ӭ�1-���W���ض�(String���A)
                String[] tmp2 = kitem2.split(" ");   //���X�ӷ|�O�@�ӭ�1-���W���ض�(String���A)
                String c = "";
                   if(tmp1.length == 1){             //�x�}���u����@�Ӷ��ض�(�x�}tmp1�|��x�}tmp2���פ@��)
                          if(tmp1[0].compareTo(tmp2[0]) < 0){       //�r����(�̷ӼƦr�M�^��r���ƧǤ��)
                                c = tmp1[0] + " " + tmp2[0] +" ";
                          }
                   }else{
                          boolean flag = true;
                          for(int i = 0 ; i < tmp1.length-1 ; i++){  //tmp1�Mtemp2�x�}��index�q0~length-2���O���O�_�@��
                             if(!tmp1[i].equals(tmp2[i])){           //�����X�{���P��flag�אּfalse�A���X�j��
                                  flag = false;
                                  break;
                             }
                          }
                          if(flag && (tmp1[tmp1.length-1].compareTo(tmp2[tmp2.length-1]) < 0)){ //��tmp1�Mtemp2�x�}�U�۳̫�@�Ӫ�����(String���A)
                            c = kitem1 + tmp2[tmp2.length-1]+" ";
                          }
                    }
                //�p�����פ��e�P�_�������l���X�O�_���W
                boolean hasInfrequentSubSet = false;
                   if (!c.equals("")) {
                       String[] tmpC = c.split(" ");        //��String���Ac(�Կﶵ�ض�)�i�����
                       for (int i = 0 ; i < tmpC.length ; i++) {
                           String SubSetC = "";
                           for (int j = 0 ; j < tmpC.length ; j++) {
                               if (i != j) {
                                 SubSetC = SubSetC + tmpC[j] + " ";
                               }
                           }
                           if (frequentkitemMap.get(SubSetC) == null) {   //�N��Կﶵ�ض����l���ض��X�{�D���W�����p
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
	public static void sop(Object obj){  //�C�L��function
	    	System.out.println(obj);
	 }
}
