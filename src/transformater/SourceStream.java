package transformater;

import java.util.ArrayList;

import syntaciticAnalyzer.LexicalStream;
import lexicalAnalyzer.Terms;
import lexicalAnalyzer.TermsTable;
import lexicalAnalyzer.VariableTable;

public class SourceStream {
	public ArrayList<Cwords> CSourceStream = new ArrayList<Cwords>();
	
	public SourceStream(ArrayList<TermsTable> SrcArticle){
		for (TermsTable TempTable:SrcArticle){
			if (!TempTable.Str.trim().isEmpty()){
				Cwords TempCwords = new Cwords();
				TempCwords.Kind = String.valueOf(TempTable.Kind);
				TempCwords.Value = String.valueOf(TempTable.Str);
	        	if (!TempTable.Str.trim().isEmpty()){
		        	if (TempTable.Kind.equals("�ؼ���")){
		        		if (TempTable.Str.equals("for") || TempTable.Str.equals("if") || TempTable.Str.equals("else") || TempTable.Str.equals("return") || TempTable.Str.equals("void")){
		        			TempCwords.GramKind = String.valueOf(TempTable.Str);
		        		}
		        		else{
		        			TempCwords.GramKind = "type";
		        		}
		        	}
		        	else if (TempTable.Kind.equals("����")){
		        		TempCwords.GramKind = "id";
		        	}
		        	else if (TempTable.Kind.equals("�����")){
		        		TempCwords.GramKind = String.valueOf(TempTable.Str);
		        	}
		        	else if (TempTable.Kind.equals("���ֳ���")){
		        		TempCwords.GramKind = "digit";
		        	}
		        	else if (TempTable.Kind.equals("���")){
		        		TempCwords.GramKind = String.valueOf(TempTable.Str);
		        	}
		        	else if (TempTable.Kind.equals("�ַ�������")){
		        		TempCwords.GramKind = "string";
		        	}
	        	}
	        	this.CSourceStream.add(TempCwords);
			}
        }
	}
}
