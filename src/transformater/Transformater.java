package transformater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import syntaciticAnalyzer.GramTreeNode;

public class Transformater {

	public Transformater(GramTree MainGramTree, SourceStream MainSourceStream) {
		try {
			File ObjFile = new File("D:/HCCCompiler/BBasicObj.txt");
			PrintWriter ObjWriter = new PrintWriter(ObjFile);
			ObjWriter.println("CCompiler Ver 1.0");
			ObjWriter.println("Author: HelloClyde");
			ObjWriter.println(";======MainProg======");
			ObjWriter.println("call fint_vint_main");
			ObjWriter.println("exit");
			ObjWriter.println(";======FuncProg======");
			
			//�������к���,���еǼ�
			ArrayList<GramTreeNode> FunctionNode = MainGramTree.SearchNode("������������");
			for (GramTreeNode TempNode:FunctionNode){
				//System.out.println(FindFunctionName(TempNode).Words.Value);
				
			}
			ObjWriter.close();
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
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
}
