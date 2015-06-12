package syntaciticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;

public class SelectSet {
	ArrayList<SingleSelectSet> CSingleSelectSet = new ArrayList<SingleSelectSet>();
	
	public SelectSet(CGramClass SrcGramClass) {
		/*
		 * ��ʼֵΪ���в���ʽ
		 */
		for (SingleProduceType TempSPT:SrcGramClass.CProduceType.CSingleProduceType){
			for (Produce TempProduce:TempSPT.RightProduce){
				SingleSelectSet NewSingleSelectSet = new SingleSelectSet();
				NewSingleSelectSet.CProduce = TempProduce;
				NewSingleSelectSet.LeftName = TempSPT.LeftName;
				this.CSingleSelectSet.add(NewSingleSelectSet);
			}
		}
		for (SingleSelectSet TempSSS:this.CSingleSelectSet){
			int CanProduceNum;
			CanProduceNum = 0;
			//�ж����Ƴ�$�ĸ���
			for (int i = 0;i < TempSSS.CProduce.Elems.size();i ++){
				if (SrcGramClass.CFinalTable.IsProduce$(TempSSS.CProduce.Elems.get(i))){
					CanProduceNum ++;
				}
				else{
					break;
				}
			}
			//��First(A)
			HashSet<String> TempHashSet = new HashSet<String>();
			for (int i = 0;i < TempSSS.CProduce.Elems.size();i ++){
				TempHashSet.addAll(SrcGramClass.CFirstSet.GetSet(TempSSS.CProduce.Elems.get(i)));
				if (!SrcGramClass.CFinalTable.IsProduce$(TempSSS.CProduce.Elems.get(i))){
					TempHashSet.remove("$");
					break;
				}
			}
			if (CanProduceNum == TempSSS.CProduce.Elems.size()){
				TempHashSet.remove("$");
				TempSSS.Set.addAll(TempHashSet);
				TempSSS.Set.addAll(SrcGramClass.CFollowSet.GetSet(TempSSS.LeftName));
			}
			else{
				TempSSS.Set.addAll(TempHashSet);
			}
		}
	}
	
	public Produce GetProduce(String VNName,String VTName){
		for (SingleSelectSet TempSSS:this.CSingleSelectSet){
			if (TempSSS.LeftName.equals(VNName)){
				/*
				 * �ж�VTName�Ƿ������SET��
				 */
				if (TempSSS.Set.contains(VTName)){
					return TempSSS.CProduce;
				}
			}
		}
		return null;
	}
	
	public HashSet<String> GetSet(Produce SrcProduce){
		for (SingleSelectSet TempSSS:this.CSingleSelectSet){
			if (TempSSS.CProduce == SrcProduce){
				return TempSSS.Set;
			}
		}
		return null;
	}
	
	public void Show(){
		for (SingleSelectSet TempSSS:this.CSingleSelectSet){
			TempSSS.Show();
		}
	}

}
