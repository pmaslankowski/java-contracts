// Generated from MethodPointcut.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MethodPointcutLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, WHITESPACE=13, IDENTIFIER=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "DIGIT", "ALPHA", "SPECIAL", "WHITESPACE", 
			"IDENTIFIER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'('", "')'", "'throwing'", "'static'", "'non-static'", 
			"'public'", "'private'", "'protected'", "'package-private'", "','", "'..'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "WHITESPACE", "IDENTIFIER"
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


	public MethodPointcutLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MethodPointcut.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20\u0085\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3"+
		"\17\3\17\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\6\22\u0082\n\22"+
		"\r\22\16\22\u0083\2\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\2\35\2\37\2!\17#\20\3\2\6\3\2\62;\4\2C\\c|\t\2&&,,//\61"+
		"\61AA]]_a\4\2\13\13\"\"\2\u0084\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3"+
		"\2\2\2\5\'\3\2\2\2\7)\3\2\2\2\t+\3\2\2\2\13\64\3\2\2\2\r;\3\2\2\2\17F"+
		"\3\2\2\2\21M\3\2\2\2\23U\3\2\2\2\25_\3\2\2\2\27o\3\2\2\2\31q\3\2\2\2\33"+
		"t\3\2\2\2\35v\3\2\2\2\37x\3\2\2\2!z\3\2\2\2#\u0081\3\2\2\2%&\7\60\2\2"+
		"&\4\3\2\2\2\'(\7*\2\2(\6\3\2\2\2)*\7+\2\2*\b\3\2\2\2+,\7v\2\2,-\7j\2\2"+
		"-.\7t\2\2./\7q\2\2/\60\7y\2\2\60\61\7k\2\2\61\62\7p\2\2\62\63\7i\2\2\63"+
		"\n\3\2\2\2\64\65\7u\2\2\65\66\7v\2\2\66\67\7c\2\2\678\7v\2\289\7k\2\2"+
		"9:\7e\2\2:\f\3\2\2\2;<\7p\2\2<=\7q\2\2=>\7p\2\2>?\7/\2\2?@\7u\2\2@A\7"+
		"v\2\2AB\7c\2\2BC\7v\2\2CD\7k\2\2DE\7e\2\2E\16\3\2\2\2FG\7r\2\2GH\7w\2"+
		"\2HI\7d\2\2IJ\7n\2\2JK\7k\2\2KL\7e\2\2L\20\3\2\2\2MN\7r\2\2NO\7t\2\2O"+
		"P\7k\2\2PQ\7x\2\2QR\7c\2\2RS\7v\2\2ST\7g\2\2T\22\3\2\2\2UV\7r\2\2VW\7"+
		"t\2\2WX\7q\2\2XY\7v\2\2YZ\7g\2\2Z[\7e\2\2[\\\7v\2\2\\]\7g\2\2]^\7f\2\2"+
		"^\24\3\2\2\2_`\7r\2\2`a\7c\2\2ab\7e\2\2bc\7m\2\2cd\7c\2\2de\7i\2\2ef\7"+
		"g\2\2fg\7/\2\2gh\7r\2\2hi\7t\2\2ij\7k\2\2jk\7x\2\2kl\7c\2\2lm\7v\2\2m"+
		"n\7g\2\2n\26\3\2\2\2op\7.\2\2p\30\3\2\2\2qr\7\60\2\2rs\7\60\2\2s\32\3"+
		"\2\2\2tu\t\2\2\2u\34\3\2\2\2vw\t\3\2\2w\36\3\2\2\2xy\t\4\2\2y \3\2\2\2"+
		"z{\t\5\2\2{|\3\2\2\2|}\b\21\2\2}\"\3\2\2\2~\u0082\5\35\17\2\177\u0082"+
		"\5\33\16\2\u0080\u0082\5\37\20\2\u0081~\3\2\2\2\u0081\177\3\2\2\2\u0081"+
		"\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2"+
		"\2\2\u0084$\3\2\2\2\5\2\u0081\u0083\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}