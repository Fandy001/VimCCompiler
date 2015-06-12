package syntaciticAnalyzer;

import java.util.ArrayList;

public class PreciateTable {
	ArrayList<SinglePreciateTable> CSinglePreciateTable = new ArrayList<SinglePreciateTable>();
	
	public PreciateTable(CGramClass SrcGramClass) {
		for (String StrVN:SrcGramClass.CVNSet.CVNSet){
			for (String StrVT:SrcGramClass.CVTSet.CVTSet){
				SinglePreciateTable TempSPT = new SinglePreciateTable();
				TempSPT.VNName = StrVN;
				TempSPT.VTName = StrVT;
				/*
				 * ����Select��
				 */
				TempSPT.DesProduce = SrcGramClass.CSelectSet.GetProduce(StrVN, StrVT);
				this.CSinglePreciateTable.add(TempSPT);
			}
			//#��Ϊ������ս��Ҳ�ü���
			SinglePreciateTable TempSPT = new SinglePreciateTable();
			TempSPT.VNName = StrVN;
			TempSPT.VTName = "#";
			/*
			 * ����Select��
			 */
			TempSPT.DesProduce = SrcGramClass.CSelectSet.GetProduce(StrVN, "#");
			this.CSinglePreciateTable.add(TempSPT);
		}
	}
	
	public Produce GetProduce(String VNName,String VTName){
		for (SinglePreciateTable TempSPT:this.CSinglePreciateTable){
			if (TempSPT.VNName.equals(VNName) && TempSPT.VTName.equals(VTName)){
				return TempSPT.DesProduce;
			}
		}
		return null;
	}
	
	public void Show(){
		for (SinglePreciateTable TempSPT:this.CSinglePreciateTable){
			TempSPT.Show();
		}
	}
}
