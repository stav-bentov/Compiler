   
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			String result = "";

			try {
				/***********************/
				/* [4] Read next token */
				/***********************/
				s = l.next_token();

				/********************************/
				/* [5] Main reading tokens loop */
				/********************************/
				while (s.sym != TokenNames.EOF)
				{
					result += getFormat(s, l);

					/***********************/
					/* [6] Read next token */
					/***********************/
					s = l.next_token();

					if (s.sym != TokenNames.EOF) {
						result += "\n";
					}
				}

				/*********************/
				/* [7] Print to file */
				/*********************/
				file_writer.write(result);
			}

			catch (Error e) {
				file_writer.write("ERROR");
			}
			
			/******************************/
			/* [8] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [9] Close output file */
			/**************************/
			file_writer.close();
    	}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static String getFormat(Symbol s, Lexer l) {
		String location = String.format("[%d,%d]", l.getLine(), l.getTokenStartPosition());
		switch (s.sym) {
			case (TokenNames.INT):
				return String.format("INT(%d)%s", s.value, location);
			case (TokenNames.STRING):
				return String.format("STRING(%s)%s", s.value, location);
			case (TokenNames.ID):
				return String.format("ID(%s)%s", s.value, location);
			case (TokenNames.EOF):
				return String.format("EOF%s", location);
			case (TokenNames.LPAREN):
				return String.format("LPAREN%s", location);
			case (TokenNames.RPAREN):
				return String.format("RPAREN%s", location);
			case (TokenNames.LBRACK):
				return String.format("LBRACK%s", location);
			case (TokenNames.RBRACK):
				return String.format("RBRACK%s", location);
			case (TokenNames.LBRACE):
				return String.format("LBRACE%s", location);
			case (TokenNames.RBRACE):
				return String.format("RBRACE%s", location);
			case (TokenNames.NIL):
				return String.format("NIL%s", location);
			case (TokenNames.PLUS):
				return String.format("PLUS%s", location);
			case (TokenNames.MINUS):
				return String.format("MINUS%s", location);
			case (TokenNames.TIMES):
				return String.format("TIMES%s", location);
			case (TokenNames.DIVIDE):
				return String.format("DIVIDE%s", location);
			case (TokenNames.COMMA):
				return String.format("COMMA%s", location);
			case (TokenNames.DOT):
				return String.format("DOT%s", location);
			case (TokenNames.SEMICOLON):
				return String.format("SEMICOLON%s", location);
			case (TokenNames.TYPE_INT):
				return String.format("TYPE_INT%s", location);
			case (TokenNames.TYPE_STRING):
				return String.format("TYPE_STRING%s", location);
			case (TokenNames.TYPE_VOID):
				return String.format("TYPE_VOID%s", location);
			case (TokenNames.ASSIGN):
				return String.format("ASSIGN%s", location);
			case (TokenNames.EQ):
				return String.format("EQ%s", location);
			case (TokenNames.LT):
				return String.format("LT%s", location);
			case (TokenNames.GT):
				return String.format("GT%s", location);
			case (TokenNames.ARRAY):
				return String.format("ARRAY%s", location);
			case (TokenNames.CLASS):
				return String.format("CLASS%s", location);
			case (TokenNames.EXTENDS):
				return String.format("EXTENDS%s", location);
			case (TokenNames.RETURN):
				return String.format("RETURN%s", location);
			case (TokenNames.WHILE):
				return String.format("WHILE%s", location);
			case (TokenNames.IF):
				return String.format("IF%s", location);
			case (TokenNames.NEW):
				return String.format("NEW%s", location);
		}
		throw new Error("Error: not a token");
	}
}


