package syntaciticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;

public class FirstSet {
	
	ArrayList<SingleFirstSet> CSingleFirstSet = new ArrayList<SingleFirstSet>();
	
	public FirstSet(CGramClass SrcGramClass) {
		/*
		 * First���ϳ�ʼ����VN��VT
		 */
		for (String Str:SrcGramClass.CVNSet.CVNSet){
			SingleFirstSet NewFirstSet = new SingleFirstSet();
			NewFirstSet.LeftName = Str;
			//���3:VN���Ϸ������Ƴ�$��First���϶���$
			if (SrcGramClass.CFinalTable.IsProduce$(Str)){
				NewFirstSet.Set.add("$");
			}
			this.CSingleFirstSet.add(NewFirstSet);
		}
		for (String Str:SrcGramClass.CVTSet.CVTSet){
			SingleFirstSet NewFirstSet = new SingleFirstSet();
			NewFirstSet.LeftName = Str;
			//���1��VT���ϵ�FIRST���Ͼ���������
			NewFirstSet.Set.add(Str);
			this.CSingleFirstSet.add(NewFirstSet);
		}
		//this.Show();
		/*
		 * ���4:��X,Y1,Y2,...Yn������VN�����в���ʽX->Y1Y2...Yn����Y1,Y2,...Yi-1�����Ƴ�$ʱ����First(Y1)-{$},first(Y2)-{$},...,First(Yi-1)-{$},First(Yi)��������First(X)��
		 * ���5����4�����е�Yi�����Ƴ�$��(i=1,2,...,n)����FIRST(X)=FIRST(Y1)��FIRST(Y2)��...��FIRST(Yn)��{$}
		 */
		boolean IsModify = true;
		while (IsModify){
			IsModify = false;
			for (SingleFirstSet TempSFS:this.CSingleFirstSet){
				//System.out.println(TempSFS.LeftName);
				if (SrcGramClass.CProduceType.GetProduces(TempSFS.LeftName) == null){
					continue;
				}
				for (Produce TempProduce:SrcGramClass.CProduceType.GetProduces(TempSFS.LeftName)){
					
					int CanProduceNum;
					CanProduceNum = 0;
					//�ж����Ƴ�$�ĸ���
					for (int i = 0;i < TempProduce.Elems.size();i ++){
						if (SrcGramClass.CFinalTable.IsProduce$(TempProduce.Elems.get(i))){
							CanProduceNum ++;
						}
						else{
							break;
						}
					}
					//System.out.println(TempSFS.LeftName + "-------" + CanProduceNum);
					if (CanProduceNum == TempProduce.Elems.size()){
						int OldSize = TempSFS.Set.size();
						for (int i = 0;i < CanProduceNum;i ++){
							String TempStr = TempProduce.Elems.get(i);
							TempSFS.Set.addAll(this.GetSet(TempStr));
						}
						this.GetSet(TempSFS.LeftName).add("$");
						if (TempSFS.Set.size() != OldSize){
							IsModify = true;
						}
					}
					else{
						int OldSize = TempSFS.Set.size();
						for (int i = 0;i < CanProduceNum;i ++){
							HashSet<String> TempHashSet = new HashSet<String>();
							String TempStr = TempProduce.Elems.get(i);
							TempHashSet.addAll(this.GetSet(TempStr));
							TempHashSet.remove("$");
							TempSFS.Set.addAll(TempHashSet);
						}
						TempSFS.Set.addAll(this.GetSet(TempProduce.Elems.get(CanProduceNum)));
						
						if (TempSFS.Set.size() != OldSize){
							IsModify = true;
						}
					}
				}
			}
		}
		
	}
	
	public boolean AddSet(String Name,String Str){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			if (TempSFS.LeftName.equals(Name)){
				int OldSet = TempSFS.Set.size();
				TempSFS.Set.add(Str);
				if (TempSFS.Set.size() == OldSet){
					return false;
				}
				else{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param Name
	 * @return
	 */
	public HashSet<String> GetSet(String Name){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			if (TempSFS.LeftName.equals(Name)){
				return TempSFS.Set;
			}
		}
		return null;
	}
	
	public void Show(){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			TempSFS.Show();
		}
	}
	
	public SingleFirstSet GetSFS(String Name){
		for (SingleFirstSet TempSFS:this.CSingleFirstSet){
			if (TempSFS.LeftName.equals(Name)){
				return TempSFS;
			}
		}
		return null;
	}
}
