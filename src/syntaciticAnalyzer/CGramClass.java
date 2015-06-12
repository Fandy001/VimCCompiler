package syntaciticAnalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class CGramClass {
	/**
	 * ������
	 * @param args
	 */
	static public void main(String[] args){
		CGramClass TestCGramClass = new CGramClass("D:\\HCCCompiler\\grammar.txt");
		TestCGramClass.Show();
	}
	/*
	 * �������ʽ
	 */
	ProduceType CProduceType;
	/*
	 * ����VT�ս����
	 */
	VTSet CVTSet;
	/*
	 * ����VN���ս����
	 */
	VNSet CVNSet;
	/*
	 * �����ܷ��Ƴ�$�ı�
	 */
	FinalTable CFinalTable;
	/*
	 * ����First����
	 */
	FirstSet CFirstSet;
	/*
	 * ����Follow����
	 */
	FollowSet CFollowSet;
	/*
	 * ����Select����
	 */
	SelectSet CSelectSet;
	/*
	 * ����Ԥ�������
	 */
	PreciateTable CPreciateTable;
	public CGramClass(String FilePath){
		this.CProduceType = new ProduceType(FilePath);
		this.CVTSet = new VTSet(this.CProduceType);
		this.CVNSet = new VNSet(this.CProduceType);
		this.CFinalTable = new FinalTable(this);
		this.CFirstSet = new FirstSet(this);
		this.CFollowSet = new FollowSet(this);
		this.CSelectSet = new SelectSet(this);
		this.CPreciateTable = new PreciateTable(this);
	}
	
	public void Show(){
		System.out.println("\n�﷨��");
		this.CProduceType.Show();
		System.out.println("\n�ս����");
		this.CVTSet.Show();
		System.out.println("\n���ս����");
		this.CVNSet.Show();
		System.out.println("\n$���Ƶ���");
		this.CFinalTable.Show();
		System.out.println("\nFirst����");
		this.CFirstSet.Show();
		System.out.println("\nFollow����");
		this.CFollowSet.Show();
		System.out.println("\nSelect����");
		this.CSelectSet.Show();
		System.out.println("\nԤ�������");
		this.CPreciateTable.Show();
	}

	public boolean IsLegal(ArrayList<String> Article) {
		Stack<String> AnlyStack = new Stack<String>();
		Stack<String> StrStack = new Stack<String>();
		//ѹ��S
		AnlyStack.push("#");
		AnlyStack.push("���������塿");
		//ѹ��Article
		StrStack.push("#");
		for (int i = Article.size() - 1;i >= 0;i --){
			StrStack.push(Article.get(i));
		}
		//���������
		int StepsCounts;
		StepsCounts = 1;
		while (!(AnlyStack.isEmpty() && StrStack.isEmpty())){
			if (AnlyStack.isEmpty() || StrStack.isEmpty()){
				System.out.println("Ԥ������������󡣷���ջ����ʣ�മΪ��");
				return false;
			}
			else{
				System.out.print(StepsCounts + "\t");
				//�������ջ
				for (String TempStr:AnlyStack){
					System.out.print(TempStr + " ");
				}
				System.out.print("\t");
				//���ʣ�മ
				for (String TempStr:StrStack){
					System.out.print(TempStr + " ");
				}
				System.out.print("\t");
				String AnlyStr = AnlyStack.pop();
				String StrStr = StrStack.peek();
				if (AnlyStr.equals(StrStr)){
					System.out.println(StrStr + "ƥ��");
					StrStack.pop();
				}
				else{
					Produce TempProduce;
					TempProduce = this.CPreciateTable.GetProduce(AnlyStr, StrStr);
					if (TempProduce == null){
						System.out.println(StrStr + "ƥ��ʧ��");
						return false;
					}
					else{
						TempProduce.Show();
						System.out.println();
						for (int i = TempProduce.Elems.size() - 1; i >= 0;i --){
							String TempStr = TempProduce.Elems.get(i);
							if (!TempStr.equals("$")){
								AnlyStack.push(TempProduce.Elems.get(i));
							}
						}
					}
				}
				StepsCounts ++;
			}
		}
		System.out.println("ƥ��ɹ�!");
		return true;
	}
	
	
	public boolean IsLL1(){
		for (SingleProduceType TempSPT:this.CProduceType.CSingleProduceType){
			for (Produce TempProduce:TempSPT.RightProduce){
				for (Produce TempProduceOthers:TempSPT.RightProduce){
					if (TempProduce != TempProduceOthers){
						HashSet<String> TempHashSet = new HashSet<String>();
						TempHashSet.addAll(this.CSelectSet.GetSet(TempProduce));
						TempHashSet.retainAll(this.CSelectSet.GetSet(TempProduceOthers));
						if (!TempHashSet.isEmpty()){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
