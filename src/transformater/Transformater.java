package transformater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import syntaciticAnalyzer.GramTreeNode;

public class Transformater {
	String FunctionName;
	ArrayList<String> TotalVarTable;
	ArrayList<VarTable> VarTableList;
	int LabelIndex = 0;

	public Transformater(GramTree MainGramTree, SourceStream MainSourceStream) {
		try {
			File ObjFile = new File("D:/HCCCompiler/BBasicObj.txt");
			PrintWriter ObjWriter = new PrintWriter(ObjFile);
			ObjWriter.println(";CCompiler Ver 1.0");
			ObjWriter.println(";Author: HelloClyde");
			ObjWriter.println(";======MainProg======");
			ObjWriter.println("call fint_vint_main");
			ObjWriter.println("exit");
			
			ObjWriter.println(";======FuncProg======");
			//�������к���,���еǼ�
			ArrayList<GramTreeNode> FunctionNode = MainGramTree.SearchNode("������������");
			//������
			this.TotalVarTable = new ArrayList<String>();
			for (GramTreeNode FunNode:FunctionNode){
				//��ȡ������
				this.FunctionName = String.valueOf(FindFunctionName(FunNode).Words.Value);
				//�����������������,���ڷ���ֵ
				TotalVarTable.add("vint_" + FunctionName);
				//��ȡ��������
				this.VarTableList = this.GetVarTable(this.FindFunVarDeclare(FunNode));
				//�����ջ״̬�����Զ�ջ��ʼ��
				ObjWriter.println("fint_vint_" + FunctionName + ":");
				ObjWriter.println("push rb");
				ObjWriter.println("ld int rb,rs");
				ObjWriter.println("cal int add rs,-4");
				//��ȡ������
				GramTreeNode FunBlock = this.FindFunBlock(FunNode);
				GramTreeNode FunVarDelare = FunBlock.Child;
				GramTreeNode FunDoBlock = FunBlock.Child.next;
				//������д���������
				ArrayList<VarTable> TempVarTableList = this.GetVarTable(FunVarDelare);
				for (VarTable TempVarTable:TempVarTableList){
					TotalVarTable.add("vint_" + FunctionName + "_" + TempVarTable.VarName);
				}
				//������հ�����
				FunDo(ObjWriter,FunDoBlock,FunctionName);
				//�����˳�
				ObjWriter.println("vint_" + FunctionName + "_exit:");
				//�ָ���ջ״̬
				ObjWriter.println("ld int rs,rb");
				ObjWriter.println("pop rb");
				ObjWriter.println("ret");
			}
			//д������
			ObjWriter.println(";======BinData======");
			for (String Str:TotalVarTable){
				ObjWriter.println("data " + Str + " int 0");
			}
			ObjWriter.close();
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	//�������顿
	void FunBlock(PrintWriter DesPWriter,GramTreeNode FunBlockBlock){
		//������䲻����
		//������հ�
		this.FunDo(DesPWriter, FunBlockBlock.Child.next, this.FunctionName);
	}
	
	void FunDo(PrintWriter DesPWriter,GramTreeNode FunDoBlock,String FunName){
		if (FunDoBlock.Child == null){
			return;
		}
		else{
			if (FunDoBlock.Child.Value.equals("����ֵ������")){
				VarTo(DesPWriter,FunDoBlock.Child);
			}
			else if (FunDoBlock.Child.Value.equals("��forѭ����")){
				ForDo(DesPWriter,FunDoBlock.Child);
			}
			else if (FunDoBlock.Child.Value.equals("��������䡿")){
				IfDo(DesPWriter,FunDoBlock.Child);
			}
			else if (FunDoBlock.Child.Value.equals("���������ء�")){
				ReturnDo(DesPWriter,FunDoBlock.Child,FunName);
			}
			FunDo(DesPWriter,FunDoBlock.Child.next,FunName);
		}
	}
	
	//���ʽ
	void CalDo(PrintWriter DesWriter,GramTreeNode CalDoBlock){
		CalA(DesWriter,CalDoBlock.Child);
		CalB(DesWriter,CalDoBlock.Child.next);
	}
	
	//��
	void CalB(PrintWriter DesWriter,GramTreeNode CalBBlock){
		if (CalBBlock.Child != null){
			//����r0��r1
			DesWriter.println("ld int r1,r0");
			CalA(DesWriter,CalBBlock.Child.next);
			if (CalBBlock.Child.Value.equals("+")){
				DesWriter.println("cal int add r1,r0");
			}
			else if (CalBBlock.Child.Value.equals("-")){
				DesWriter.println("cal int sub r1,r0");
			}
			DesWriter.println("ld int r0,r1");
			CalB(DesWriter,CalBBlock.Child.next.next);
		}
	}
	
	//����
	void CalA(PrintWriter DesWriter,GramTreeNode CalABlock){
		CalS(DesWriter,CalABlock.Child);
		CalSa(DesWriter,CalABlock.Child.next);
	}
	
	//��ʽ�ݹ�
	void CalSa(PrintWriter DesWriter,GramTreeNode CalSaBlock){
		if (CalSaBlock.Child != null){
			//����r0��r1
			DesWriter.println("ld int r1,r0");
			//������һ����ʽ,����r0
			CalS(DesWriter,CalSaBlock.Child.next);
			if (CalSaBlock.Child.Value.equals("*")){
				DesWriter.println("cal int mul r1,r0");
			}
			else if (CalSaBlock.Child.Value.equals("/")){
				DesWriter.println("cal int div r1,r0");
			}
			else if (CalSaBlock.Child.Value.equals("%")){
				DesWriter.println("cal int mod r1,r0");
			}
			//����r0
			DesWriter.println("ld int r0,r1");
		}
	}
	
	void VarOrFun(PrintWriter DesWriter,GramTreeNode VarOrFunBlock){
		if (VarOrFunBlock.Child.next.Child == null){
			//����
			DesWriter.println("ld int r0," + VarOrFunBlock.Child.Child.Child.Words.Value);
		}
		else{
			//��������
			//ѹ����
			PushVar(DesWriter,VarOrFunBlock.Child.next.Child.next);
			//Callָ��
			DesWriter.println("call fint_vint_" + VarOrFunBlock.Child.Child.Child.Words.Value);
		}
	}
	
	/*
	 * �����б�,ѹ������ջ
	 */
	void PushVar(PrintWriter DesWriter,GramTreeNode VarList){
		ArrayList<GramTreeNode> GramTreeNodeArray = new ArrayList<GramTreeNode>();
		VarList.Search("��������",GramTreeNodeArray);
		for (GramTreeNode TempTreeNode:GramTreeNodeArray){
			if (TempTreeNode.Child.Value.equals("����־����")){
				String VarName = TempTreeNode.Child.Child.Words.Value;
				int VarIndex = this.IsInVarTableList(VarName);
				if (VarIndex == -1){
					DesWriter.println("push " + "[vint_" + this.FunctionName + "_" + VarName + "]");
				}
				else{
					//����ջ
					DesWriter.println("ld int r0,rb");
					DesWriter.println("cal int add r0," + (VarIndex - 1));
					//ѹջ
					DesWriter.println("push " + "[r0]");
				}
			}
			else if (TempTreeNode.Child.Value.equals("�����֡�")){
				DesWriter.println("push " + TempTreeNode.Child.Words.Value);
			}
			//�ַ�����������ȷ
			else if (TempTreeNode.Child.Value.equals("���ַ�����")){
				DesWriter.println("push " + TempTreeNode.Child.Child.Words.Value);
			}
		}
	}
	
	int IsInVarTableList(String Str){
		for (VarTable TempVarTable:this.VarTableList){
			if (TempVarTable.VarName.equals(Str)){
				return TempVarTable.Index;
			}
		}
		return -1;
	}
	
	//��ʽ
	void CalS(PrintWriter DesWriter,GramTreeNode CalSBlock){
		GramTreeNode CalNode = CalSBlock;
		if (CalNode.Child.Value.equals("(")){
			//������ʽ
			CalDo(DesWriter,CalNode.Child.next);
		}
		else if (CalNode.Child.Value.equals("�������������á�")){
			VarOrFun(DesWriter,CalNode.Child);
		}
		else if (CalNode.Child.Value.equals("�����֡�")){
			DesWriter.println("ld int r0," + CalNode.Child.Child.Words.Value);
		}
	}
	
	void ReturnDo(PrintWriter DesWriter,GramTreeNode ReturnDoBlock,String FunName){
		//������ʽ
		//������Ľ����������r0��
		CalS(DesWriter,ReturnDoBlock.Child.next);
		DesWriter.println("ld int vint_" + FunName + ",r0");
		DesWriter.println("jmp " + "vint_" + FunName + "_exit");
	}
	
	//�������߼����ʽ��
	void MulLogicExpression(PrintWriter DesWriter,GramTreeNode MulLogicExpressionBlock){
		this.LogicExpression(DesWriter, MulLogicExpressionBlock.Child);
		this.MulLogicExpressionRecursive(DesWriter, MulLogicExpressionBlock.Child.next);
	}
	
	//���߼����ʽ��
	void LogicExpression(PrintWriter DesWriter,GramTreeNode LogicExpressionBlock){
		//���ʽ
		this.CalDo(DesWriter, LogicExpressionBlock.Child);
		this.LogicExpressionSuffix(DesWriter, LogicExpressionBlock.Child.next);
	}
	
	//���߼����ʽ��׺��
	void LogicExpressionSuffix(PrintWriter DesWriter,GramTreeNode LogicExpressionSuffixBlock){
		if (LogicExpressionSuffixBlock.Child != null){
			//����r0��r1
			DesWriter.println("ld int r1,r0");
			this.CalDo(DesWriter, LogicExpressionSuffixBlock.Child.next);
			DesWriter.println("cmp int r0,r1");
			if (LogicExpressionSuffixBlock.Child.Child.Value.equals("<")){
				DesWriter.println("ld int r0,be");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals(">")){
				DesWriter.println("ld int r0,ae");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals("==")){
				DesWriter.println("ld int r0,z");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals("!=")){
				DesWriter.println("ld int r0,nz");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals(">=")){
				DesWriter.println("ld int r0,b");
			}
			else if (LogicExpressionSuffixBlock.Child.Child.Value.equals("<=")){
				DesWriter.println("ld int r0,a");
			}
		}
	}
	
	//�������߼����ʽ�ݹ顿
	void MulLogicExpressionRecursive(PrintWriter DesWriter,GramTreeNode MulLogicExpressionRecursiveBlock){
		if (MulLogicExpressionRecursiveBlock.Child != null){
			//����r0��r1
			DesWriter.println("ld int r1,r0");
			this.LogicExpression(DesWriter, MulLogicExpressionRecursiveBlock.Child.next);
			if (MulLogicExpressionRecursiveBlock.Child.Child.Value.equals("&&")){
				DesWriter.println("cal int mul r0,r1");
			}
			else if (MulLogicExpressionRecursiveBlock.Child.Child.Value.equals("||")){
				DesWriter.println("cal int add r0,r1");
			}
			this.MulLogicExpressionRecursive(DesWriter, MulLogicExpressionRecursiveBlock.Child.next.next);
		}
	}
	
	//��������䡿
	void IfDo(PrintWriter DesWriter,GramTreeNode IfDoBlock){
		//�������߼����ʽ��,�����r0
		this.MulLogicExpression(DesWriter, IfDoBlock.Child.next.next);
		DesWriter.println("cmp int r0,1");
		//��ת��else
		DesWriter.println("jpc nz label" + this.LabelIndex + "_else");
		//���к�����
		this.FunBlock(DesWriter, IfDoBlock.Child.next.next.next.next.next);
		DesWriter.println("jmp label" + this.LabelIndex + "_end");
		this.ElseDo(DesWriter, IfDoBlock.Child.next.next.next.next.next.next.next);
		//������ǩ
		DesWriter.println("label" + this.LabelIndex + "_end:");
		this.LabelIndex ++;
	}
	
	//��������䡿
	void ElseDo(PrintWriter DesWriter,GramTreeNode ElseDoBlock){
		//д��ǩ
		DesWriter.println("label" + this.LabelIndex + "_else:");
		if (ElseDoBlock.Child != null){
			this.FunBlock(DesWriter, ElseDoBlock.Child.next.next);
		}
	}
	
	//��forѭ����
	void ForDo(PrintWriter DesWriter,GramTreeNode ForDoBlock){
		this.VarTo(DesWriter,ForDoBlock.Child.next.next);
		DesWriter.println("for" + this.LabelIndex + "_start:");
		//����ѭ������
		this.LogicExpression(DesWriter, ForDoBlock.Child.next.next.next);
		DesWriter.println("cmp int r0,1");
		DesWriter.println("jpc nz for" + this.LabelIndex + "_end");
		this.FunBlock(DesWriter, ForDoBlock.Child.next.next.next.next.next.next.next.next);
		this.SuffixExpression(DesWriter, ForDoBlock.Child.next.next.next.next.next);
		DesWriter.println("jmp for" + this.LabelIndex + "_start:");
		DesWriter.println("for" + this.LabelIndex + "_end:");
		this.LabelIndex ++;
	}
	
	//����׺���ʽ��
	void SuffixExpression(PrintWriter DesWriter,GramTreeNode SuffixExpressionBlock){
		if (SuffixExpressionBlock.Child.next.Child.Words.Value.equals("++")){
			DesWriter.println("cal int add [vint_" + this.FunctionName + "_" + SuffixExpressionBlock.Child.Child.Child.Words.Value + "],1");
		}
		else if (SuffixExpressionBlock.Child.next.Child.Words.Value.equals("--")){
			DesWriter.println("cal int sub [vint_" + this.FunctionName + "_" + SuffixExpressionBlock.Child.Child.Child.Words.Value + "],1");
		}
	}
	
	//����ֵ������
	void VarTo(PrintWriter DesWriter,GramTreeNode VarToBlock){
		if (VarToBlock.Child.next.Child.Value.equals("=")){
			//��ֵ
			CalDo(DesWriter,VarToBlock.Child.next.Child.next.Child);
			//�ж��Ƿ���ջ��
			int VarIndex = this.IsInVarTableList(VarToBlock.Child.Child.Child.Value);
			if (VarIndex == -1){
				DesWriter.println("ld int [vint_" + this.FunctionName + "_" + VarToBlock.Child.Child.Child.Value + "],r0");
			}
			else{
				//����r0��r1
				DesWriter.println("ld int r1,r0");
				//����ջ
				DesWriter.println("ld int r0,rb");
				DesWriter.println("cal int add r0," + (VarIndex - 1));
				DesWriter.println("ld int [r0],r1");
			}
		}
		else if (VarToBlock.Child.next.Child.Value.equals("(")){
			//��������
			//�ؼ���vasm����ֱ�ӵ�����
			if (VarToBlock.Child.Child.Child.Words.Value.toLowerCase().equals("vasm")){
				ArrayList<GramTreeNode> TempArrayList = new ArrayList<GramTreeNode>();
				VarToBlock.Child.next.Child.next.Search("string", TempArrayList);
				String TempStr = TempArrayList.get(0).Words.Value;
				DesWriter.println(TempStr.substring(1,TempStr.length() - 1).toLowerCase());
			}
			else{
				//ѹ����
				PushVar(DesWriter,VarToBlock.Child.next.Child.next);
				//Callָ��
				DesWriter.println("call fint_vint_" + VarToBlock.Child.Child.Child.Words.Value);
			}
		}
		
	}
	
	/*
	 * ����������node
	 */
	GramTreeNode FindFunctionName(GramTreeNode DesNode){
		GramTreeNode TempGramTreeNode = DesNode.Child;
		while (TempGramTreeNode != null){
			if (TempGramTreeNode.Value.equals("��������")){
				return TempGramTreeNode.Child.Child;
			}
			TempGramTreeNode = TempGramTreeNode.next;
		}
		return null;
	}
	
	/*
	 * ������������
	 */
	GramTreeNode FindFunVarDeclare(GramTreeNode DesNode){
		GramTreeNode TempGramTreeNode = DesNode.Child;
		while (TempGramTreeNode != null){
			if (TempGramTreeNode.Value.equals("������������")){
				return TempGramTreeNode;
			}
			TempGramTreeNode = TempGramTreeNode.next;
		}
		return null;
	}
	
	/*
	 * ����������
	 */
	GramTreeNode FindFunBlock(GramTreeNode DesNode){
		GramTreeNode TempGramTreeNode = DesNode.Child;
		while (TempGramTreeNode != null){
			if (TempGramTreeNode.Value.equals("�������顿")){
				return TempGramTreeNode;
			}
			TempGramTreeNode = TempGramTreeNode.next;
		}
		return null;
	}
	
	/*
	 * ��ȡ���в�����������з���
	 */
	ArrayList<VarTable> GetVarTable(GramTreeNode DesNode){
		ArrayList<VarTable> VarTableArray = new ArrayList<VarTable>();
		ArrayList<GramTreeNode> GramTreeNodeArray = new ArrayList<GramTreeNode>();
		DesNode.Search("��������", GramTreeNodeArray);
		int VarIndex = 0;
		for (GramTreeNode TempGramTreeNode:GramTreeNodeArray){
			VarTable TempVarTable = new VarTable();
			TempVarTable.VarName = TempGramTreeNode.Child.next.next.Child.Child.Words.Value;
			//System.out.println("Var:" + TempVarTable.VarName);
			TempVarTable.Index = VarIndex;
			VarTableArray.add(TempVarTable);
			VarIndex ++;
		}
		return VarTableArray;
	}
}
