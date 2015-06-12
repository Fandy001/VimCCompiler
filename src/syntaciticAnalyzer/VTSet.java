package syntaciticAnalyzer;

import java.util.HashSet;

public class VTSet {
	HashSet<String> CVTSet;

	public VTSet(ProduceType CProduceType) {
		this.CVTSet = new HashSet<String>();
		
		/*
		 * ��������ʽ�������ַ���
		 */
		for (SingleProduceType TempSPT:CProduceType.CSingleProduceType){
			//����ʽ�󲿲������ս�������Բ�ɨ��
			for (Produce TempProduce:TempSPT.RightProduce){
				for (String Str:TempProduce.Elems){
					if (Str.charAt(0) != '��'){
						this.CVTSet.add(Str);
					}
				}
			}
		}
	}
	
	public void Show(){
		for (String Str:this.CVTSet){
			System.out.println(Str);
		}
	}
	
}
