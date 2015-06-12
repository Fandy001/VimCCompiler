import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import syntaciticAnalyzer.*;
import lexicalAnalyzer.*;
import macroProcessing.*;

public class Compiler {
	static public void main(String[] args){
		try {
			PrintStream PsFile = new PrintStream("D:/HCCCompiler/Result.txt");  
            System.setOut(PsFile);
			
			String FilePath;
			//�ļ�����·��
			FilePath = "D:/HCCCompiler/test.txt";
			//����΢����������ͷ�ļ����滻define���塢���ע��)��
			FilePath = MacroProcessing.Deal(FilePath);
			
			//���뵥�ʣ���Ϣ����article��
			ArrayList<TermsTable> Article;
			Article = Split.FromStream(FilePath);
			
			File desFile2 = new File("D:/HCCCompiler/mainVariable.txt");
			PrintWriter pw2;
			pw2 = new PrintWriter(desFile2);
	        
	        //��������ı�ʶ��������VariableTable����
	        for (TermsTable TempTable:Article){
	        	if (TempTable.Kind.equals("����")){
	        		if (Terms.IsInArrayList(VariableTable.data, TempTable.Str) == -1){
	        			VariableTable.data.add(TempTable.Str);
	        		}
	        	}
	        }
	        
	        //�����ʶ�����ļ�
	        int printIndex = 0;
	        for (String Str:VariableTable.data){
				pw2.println(Str + "\t" + printIndex);
				printIndex ++;
			}
	        pw2.close();
	        
	        //����ʷ�����������ļ�
	        File desFile3 = new File("D:/HCCCompiler/LexiclaAnalysisResult.txt");
	        PrintWriter pw3 = new PrintWriter(desFile3);
	        for (TermsTable TempTable:Article){
	        	if (!TempTable.Str.trim().isEmpty()){
		        	int VariableIndex;
		        	VariableIndex = Terms.IsInArrayList(VariableTable.data, TempTable.Str);
		        	if (VariableIndex != -1){
		        		pw3.println(TempTable.Str + "\t\t\t\t:" + TempTable.Kind + "\t\t\t\t" + VariableIndex);
		        	}
		        	else{
		        		pw3.println(TempTable.Str + "\t\t\t\t:" + TempTable.Kind);
		        	}
	        	}
	        }
	        pw3.close();
	        
	        //���ִʽ��ת�����﷨����Ҫ�õľ��ӱ���
	        for (TermsTable TempTable:Article){
	        	if (!TempTable.Str.trim().isEmpty()){
		        	if (TempTable.Kind.equals("�ؼ���")){
		        		if (TempTable.Str.equals("for") || TempTable.Str.equals("if") || TempTable.Str.equals("else") || TempTable.Str.equals("return") || TempTable.Str.equals("void")){
		        			LexicalStream.Article.add(TempTable.Str);
		        		}
		        		else{
		        			LexicalStream.Article.add("type");
		        		}
		        	}
		        	else if (TempTable.Kind.equals("����")){
		        		LexicalStream.Article.add("id");
		        	}
		        	else if (TempTable.Kind.equals("�����")){
		        		LexicalStream.Article.add(TempTable.Str);
		        	}
		        	else if (TempTable.Kind.equals("���ֳ���")){
		        		LexicalStream.Article.add("digit");
		        	}
		        	else if (TempTable.Kind.equals("���")){
		        		LexicalStream.Article.add(TempTable.Str);
		        	}
		        	else if (TempTable.Kind.equals("�ַ�������")){
		        		LexicalStream.Article.add("string");
		        	}
	        	}
	        }
	        //LexicalStream.Show();
	        //
	        
	        /*
	         * ��ʼ���﷨������������Ԥ���
	         */
	        CGramClass MainCGramClass = new CGramClass("D:\\HCCCompiler\\grammar.txt");
	        MainCGramClass.Show();
	        if (MainCGramClass.IsLL1()){
	        	System.out.println("�﷨��LL(1)��");
	        }
	        else{
	        	System.out.println("�﷨����LL(1)��");
	        }
	        MainCGramClass.IsLegal(LexicalStream.Article);
	        
	        
        } catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
}
