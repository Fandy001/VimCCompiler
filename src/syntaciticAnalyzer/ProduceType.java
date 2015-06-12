package syntaciticAnalyzer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ProduceType {
	/**
	 * ������
	 * @param args
	 */
	static public void main(String[] args){
		ProduceType TestProduceType = new ProduceType("D:\\HCCCompiler\\grammar.txt");
		TestProduceType.Show();
	}
	ArrayList<SingleProduceType> CSingleProduceType = new ArrayList<SingleProduceType>();

	public ProduceType(String FilePath) {
		Scanner MyScanner;
		try {
			MyScanner = new Scanner(new FileReader(FilePath));
		
			while (MyScanner.hasNextLine()){
				/*
				 * ʹ�ÿո�������ַ�������
				 */
				String[] TempStrArray = MyScanner.nextLine().split(" +");
				//��ò���ʽ������
				String GramElemName = TempStrArray[0];
				//���
				this.AddSingleProduceType(GramElemName, TempStrArray);
			}
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param LeftName
	 * @param SrcStrArray
	 */
	public void AddSingleProduceType(String LeftName,String[] SrcStrArray){
		/*
		 * debug
		 */
		//System.out.println(LeftName + "," + SrcStrArray);
		SingleProduceType TempSingleProduceType = null;
		for (SingleProduceType TempSPY:CSingleProduceType){
			if (TempSPY.LeftName.equals(LeftName)){
				TempSingleProduceType = TempSPY;
				break;
			}
		}
		/*
		 * δ�ҵ��½�
		 */
		if (TempSingleProduceType == null){
			TempSingleProduceType = new SingleProduceType(LeftName);
			this.CSingleProduceType.add(TempSingleProduceType);
		}
		String[] TempStrArray = new String[SrcStrArray.length - 2];
		/*
		 * ����String����,��2��ʼ
		 */
		for (int i = 0;i < TempStrArray.length;i ++){
			TempStrArray[i] = SrcStrArray[i + 2];
		}
		TempSingleProduceType.AddRightProduce(TempStrArray);
		
	}
	
	public void Show(){
		for (SingleProduceType TempSPT:this.CSingleProduceType){
			TempSPT.Show();
		}
	}
	
	/**
	 * 
	 * @param SrcLeftName
	 * @return �������null��ʾû��������ŵĲ���ʽ
	 */
	public ArrayList<Produce> GetProduces(String SrcLeftName){
		for (SingleProduceType TempSPT:this.CSingleProduceType){
			if (TempSPT.LeftName.equals(SrcLeftName)){
				return TempSPT.RightProduce;
			}
		}
		return null;
	}

}
