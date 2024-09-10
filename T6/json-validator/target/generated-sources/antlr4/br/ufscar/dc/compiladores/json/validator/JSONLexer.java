// Generated from java-escape by ANTLR 4.11.1
package br.ufscar.dc.compiladores.json.validator;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JSONLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, NUMBER=2, TRUE=3, FALSE=4, NULL=5, LCURLY=6, RCURLY=7, LSQUARE=8, 
		RSQUARE=9, COMMA=10, COLON=11, WS=12;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"STRING", "NUMBER", "TRUE", "FALSE", "NULL", "LCURLY", "RCURLY", "LSQUARE", 
			"RSQUARE", "COMMA", "COLON", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'true'", "'false'", "'null'", "'{'", "'}'", "'['", 
			"']'", "','", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "STRING", "NUMBER", "TRUE", "FALSE", "NULL", "LCURLY", "RCURLY", 
			"LSQUARE", "RSQUARE", "COMMA", "COLON", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public JSONLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JSON.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\fi\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\"\b\u0000\u0001\u0000"+
		"\u0005\u0000%\b\u0000\n\u0000\f\u0000(\t\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0003\u0001-\b\u0001\u0001\u0001\u0004\u00010\b\u0001\u000b"+
		"\u0001\f\u00011\u0001\u0001\u0001\u0001\u0004\u00016\b\u0001\u000b\u0001"+
		"\f\u00017\u0003\u0001:\b\u0001\u0001\u0001\u0001\u0001\u0003\u0001>\b"+
		"\u0001\u0001\u0001\u0004\u0001A\b\u0001\u000b\u0001\f\u0001B\u0003\u0001"+
		"E\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0004\u000bd\b\u000b\u000b"+
		"\u000b\f\u000be\u0001\u000b\u0001\u000b\u0000\u0000\f\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u000b\u0017\f\u0001\u0000\u0007\t\u0000\"\"\'\'//\\\\"+
		"bbffnnrrtt\u0003\u000009AFaf\u0002\u0000\"\"\\\\\u0001\u000009\u0002\u0000"+
		"EEee\u0002\u0000++--\u0003\u0000\t\n\r\r  s\u0000\u0001\u0001\u0000\u0000"+
		"\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000"+
		"\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000"+
		"\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000"+
		"\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000"+
		"\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000"+
		"\u0017\u0001\u0000\u0000\u0000\u0001\u0019\u0001\u0000\u0000\u0000\u0003"+
		",\u0001\u0000\u0000\u0000\u0005F\u0001\u0000\u0000\u0000\u0007K\u0001"+
		"\u0000\u0000\u0000\tQ\u0001\u0000\u0000\u0000\u000bV\u0001\u0000\u0000"+
		"\u0000\rX\u0001\u0000\u0000\u0000\u000fZ\u0001\u0000\u0000\u0000\u0011"+
		"\\\u0001\u0000\u0000\u0000\u0013^\u0001\u0000\u0000\u0000\u0015`\u0001"+
		"\u0000\u0000\u0000\u0017c\u0001\u0000\u0000\u0000\u0019&\u0005\"\u0000"+
		"\u0000\u001a!\u0005\\\u0000\u0000\u001b\"\u0007\u0000\u0000\u0000\u001c"+
		"\u001d\u0005u\u0000\u0000\u001d\u001e\u0007\u0001\u0000\u0000\u001e\u001f"+
		"\u0007\u0001\u0000\u0000\u001f \u0007\u0001\u0000\u0000 \"\u0007\u0001"+
		"\u0000\u0000!\u001b\u0001\u0000\u0000\u0000!\u001c\u0001\u0000\u0000\u0000"+
		"\"%\u0001\u0000\u0000\u0000#%\b\u0002\u0000\u0000$\u001a\u0001\u0000\u0000"+
		"\u0000$#\u0001\u0000\u0000\u0000%(\u0001\u0000\u0000\u0000&$\u0001\u0000"+
		"\u0000\u0000&\'\u0001\u0000\u0000\u0000\')\u0001\u0000\u0000\u0000(&\u0001"+
		"\u0000\u0000\u0000)*\u0005\"\u0000\u0000*\u0002\u0001\u0000\u0000\u0000"+
		"+-\u0005-\u0000\u0000,+\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000\u0000"+
		"-/\u0001\u0000\u0000\u0000.0\u0007\u0003\u0000\u0000/.\u0001\u0000\u0000"+
		"\u000001\u0001\u0000\u0000\u00001/\u0001\u0000\u0000\u000012\u0001\u0000"+
		"\u0000\u000029\u0001\u0000\u0000\u000035\u0005.\u0000\u000046\u0007\u0003"+
		"\u0000\u000054\u0001\u0000\u0000\u000067\u0001\u0000\u0000\u000075\u0001"+
		"\u0000\u0000\u000078\u0001\u0000\u0000\u00008:\u0001\u0000\u0000\u0000"+
		"93\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:D\u0001\u0000\u0000"+
		"\u0000;=\u0007\u0004\u0000\u0000<>\u0007\u0005\u0000\u0000=<\u0001\u0000"+
		"\u0000\u0000=>\u0001\u0000\u0000\u0000>@\u0001\u0000\u0000\u0000?A\u0007"+
		"\u0003\u0000\u0000@?\u0001\u0000\u0000\u0000AB\u0001\u0000\u0000\u0000"+
		"B@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000CE\u0001\u0000\u0000"+
		"\u0000D;\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000E\u0004\u0001"+
		"\u0000\u0000\u0000FG\u0005t\u0000\u0000GH\u0005r\u0000\u0000HI\u0005u"+
		"\u0000\u0000IJ\u0005e\u0000\u0000J\u0006\u0001\u0000\u0000\u0000KL\u0005"+
		"f\u0000\u0000LM\u0005a\u0000\u0000MN\u0005l\u0000\u0000NO\u0005s\u0000"+
		"\u0000OP\u0005e\u0000\u0000P\b\u0001\u0000\u0000\u0000QR\u0005n\u0000"+
		"\u0000RS\u0005u\u0000\u0000ST\u0005l\u0000\u0000TU\u0005l\u0000\u0000"+
		"U\n\u0001\u0000\u0000\u0000VW\u0005{\u0000\u0000W\f\u0001\u0000\u0000"+
		"\u0000XY\u0005}\u0000\u0000Y\u000e\u0001\u0000\u0000\u0000Z[\u0005[\u0000"+
		"\u0000[\u0010\u0001\u0000\u0000\u0000\\]\u0005]\u0000\u0000]\u0012\u0001"+
		"\u0000\u0000\u0000^_\u0005,\u0000\u0000_\u0014\u0001\u0000\u0000\u0000"+
		"`a\u0005:\u0000\u0000a\u0016\u0001\u0000\u0000\u0000bd\u0007\u0006\u0000"+
		"\u0000cb\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000ec\u0001\u0000"+
		"\u0000\u0000ef\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000gh\u0006"+
		"\u000b\u0000\u0000h\u0018\u0001\u0000\u0000\u0000\f\u0000!$&,179=BDe\u0001"+
		"\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}