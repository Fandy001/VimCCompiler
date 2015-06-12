package syntaciticAnalyzer;

import java.util.ArrayList;

public class FinalTable {
	ArrayList<FinalSet> CFinalTable;
	
	public void AddFinalElem(FinalSet SrcFinalSet){
		
	}

	public FinalTable(CGramClass SrcGramClass) {
		this.CFinalTable = new ArrayList<FinalSet>();
		/*
		 * FinalSet��ȫ��Ԫ��ΪVN��VT
		 */
		/*
		 * ���VT,VT�������Ƴ�$
		 */
		for (String TempStr:SrcGramClass.CVTSet.CVTSet){
			FinalSet NewFinalSet = new FinalSet(TempStr);
			NewFinalSet.IsProduce$ = 0;
			this.CFinalTable.add(NewFinalSet);
		}
		/*
		 * ���VN��VN����ȷ��
		 */
		for (String TempStr:SrcGramClass.CVNSet.CVNSet){
			FinalSet NewFinalSet = new FinalSet(TempStr);
			NewFinalSet.IsProduce$ = -1;
			this.CFinalTable.add(NewFinalSet);
		}
		
		boolean IsModify = true;
		while (IsModify){
			IsModify = false;
			/*
			 * ���������ս����һ������ʽ��ȫ��Ԫ�ض����Ƴ�$��������ս�����Ƴ�$
			 */
			for (int index = 0;index < this.CFinalTable.size();index ++){
				FinalSet SrcFinalSet = this.CFinalTable.get(index);
				if (SrcFinalSet.IsProduce$ == -1){
					for (Produce TempProduce:SrcGramClass.CProduceType.GetProduces(SrcFinalSet.ElemName)){
						boolean IsAllProduce = true;
						for (String Str:TempProduce.Elems){
							if (!this.IsProduce$(Str)){
								IsAllProduce = false;
								break;
							}
						}
						if (IsAllProduce){
							SrcFinalSet.IsProduce$ = 1;
							IsModify = true;
							break;
						}
					}
				}
			}
		}
		
		/*
		 * ʣ��Ķ��ǲ��ܵ���$��
		 */
		for (FinalSet TempFinalSet:this.CFinalTable){
			if (TempFinalSet.IsProduce$ == -1){
				TempFinalSet.IsProduce$ = 0;
			}
		}
	}
	
	public void SetValue(String Name,int Value){
		for (FinalSet TempFinalSet:this.CFinalTable){
			if (TempFinalSet.ElemName.equals(Name)){
				TempFinalSet.IsProduce$ = Value;
			}
		}
	}
	
	public boolean IsProduce$(String Name){
		if (Name.equals("$")){
			return true;
		}
		for (FinalSet TempFinalSet:this.CFinalTable){
			if (TempFinalSet.ElemName.equals(Name)){
				if (TempFinalSet.IsProduce$ == 1){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}

	public void Show(){
		for (FinalSet TempFinalSet:this.CFinalTable){
			System.out.println(TempFinalSet.ElemName + (TempFinalSet.IsProduce$ == 1 ? "�ܵ���$" : "���ܵ���$"));
		}
	}
	
}
