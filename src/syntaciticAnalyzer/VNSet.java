package syntaciticAnalyzer;

import java.util.HashSet;

public class VNSet {
	HashSet<String> CVNSet;

	public VNSet(ProduceType CProduceType) {
		this.CVNSet = new HashSet<String>();
		
		/*
		 * ��������ʽ�������ַ���
		 */
		for (SingleProduceType TempSPT:CProduceType.CSingleProduceType){
			//ɨ�����ʽ��
			this.CVNSet.add(TempSPT.LeftName);
			for (Produce TempProduce:TempSPT.RightProduce){
				for (String Str:TempProduce.Elems){
					if (Str.charAt(0) == '��'){
						this.CVNSet.add(Str);
					}
				}
			}
		}
	}

	public void Show(){
		for (String Str:this.CVNSet){
			System.out.println(Str);
		}
	}
}
