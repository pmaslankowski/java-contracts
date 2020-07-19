// Generated from Pointcut.g4 by ANTLR 4.7.2

package pl.coas.compiler.instrumentation.antlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PointcutParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, WHITESPACE=21, IDENTIFIER=22;
	public static final int
		RULE_pointcutRule = 0, RULE_alternatives = 1, RULE_conjunctions = 2, RULE_atom = 3, 
		RULE_atomicRule = 4, RULE_methodRule = 5, RULE_targetRule = 6, RULE_argsRule = 7, 
		RULE_annotationRule = 8, RULE_annotatedTypeRule = 9, RULE_annotatedArgsRule = 10, 
		RULE_method = 11, RULE_kind = 12, RULE_modifiers = 13, RULE_modifier = 14, 
		RULE_returnType = 15, RULE_className = 16, RULE_methodName = 17, RULE_args = 18, 
		RULE_argWildcard = 19, RULE_arg = 20, RULE_exceptions = 21, RULE_exception = 22, 
		RULE_typeNames = 23, RULE_typeName = 24;
	private static String[] makeRuleNames() {
		return new String[] {
			"pointcutRule", "alternatives", "conjunctions", "atom", "atomicRule", 
			"methodRule", "targetRule", "argsRule", "annotationRule", "annotatedTypeRule", 
			"annotatedArgsRule", "method", "kind", "modifiers", "modifier", "returnType", 
			"className", "methodName", "args", "argWildcard", "arg", "exceptions", 
			"exception", "typeNames", "typeName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'||'", "'&&'", "'('", "')'", "'method('", "'target('", "'args('", 
			"'annotation('", "'annotatedType('", "'annotatedArgs('", "'.'", "'throwing'", 
			"'static'", "'non-static'", "'public'", "'private'", "'protected'", "'package-private'", 
			"','", "'..'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "WHITESPACE", "IDENTIFIER"
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

	@Override
	public String getGrammarFileName() { return "Pointcut.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PointcutParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class PointcutRuleContext extends ParserRuleContext {
		public AlternativesContext alternatives() {
			return getRuleContext(AlternativesContext.class,0);
		}
		public PointcutRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointcutRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterPointcutRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitPointcutRule(this);
		}
	}

	public final PointcutRuleContext pointcutRule() throws RecognitionException {
		PointcutRuleContext _localctx = new PointcutRuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_pointcutRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			alternatives();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AlternativesContext extends ParserRuleContext {
		public ConjunctionsContext conjunctions() {
			return getRuleContext(ConjunctionsContext.class,0);
		}
		public AlternativesContext alternatives() {
			return getRuleContext(AlternativesContext.class,0);
		}
		public AlternativesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alternatives; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterAlternatives(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitAlternatives(this);
		}
	}

	public final AlternativesContext alternatives() throws RecognitionException {
		AlternativesContext _localctx = new AlternativesContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_alternatives);
		try {
			setState(57);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				conjunctions();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				conjunctions();
				setState(54);
				match(T__0);
				setState(55);
				alternatives();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConjunctionsContext extends ParserRuleContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public ConjunctionsContext conjunctions() {
			return getRuleContext(ConjunctionsContext.class,0);
		}
		public ConjunctionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjunctions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterConjunctions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitConjunctions(this);
		}
	}

	public final ConjunctionsContext conjunctions() throws RecognitionException {
		ConjunctionsContext _localctx = new ConjunctionsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_conjunctions);
		try {
			setState(64);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(59);
				atom();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(60);
				atom();
				setState(61);
				match(T__1);
				setState(62);
				conjunctions();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public AtomicRuleContext atomicRule() {
			return getRuleContext(AtomicRuleContext.class,0);
		}
		public PointcutRuleContext pointcutRule() {
			return getRuleContext(PointcutRuleContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitAtom(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_atom);
		try {
			setState(71);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(66);
				atomicRule();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(67);
				match(T__2);
				setState(68);
				pointcutRule();
				setState(69);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomicRuleContext extends ParserRuleContext {
		public MethodRuleContext methodRule() {
			return getRuleContext(MethodRuleContext.class,0);
		}
		public TargetRuleContext targetRule() {
			return getRuleContext(TargetRuleContext.class,0);
		}
		public ArgsRuleContext argsRule() {
			return getRuleContext(ArgsRuleContext.class,0);
		}
		public AnnotationRuleContext annotationRule() {
			return getRuleContext(AnnotationRuleContext.class,0);
		}
		public AnnotatedTypeRuleContext annotatedTypeRule() {
			return getRuleContext(AnnotatedTypeRuleContext.class,0);
		}
		public AnnotatedArgsRuleContext annotatedArgsRule() {
			return getRuleContext(AnnotatedArgsRuleContext.class,0);
		}
		public AtomicRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomicRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterAtomicRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitAtomicRule(this);
		}
	}

	public final AtomicRuleContext atomicRule() throws RecognitionException {
		AtomicRuleContext _localctx = new AtomicRuleContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_atomicRule);
		try {
			setState(97);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(73);
				match(T__4);
				setState(74);
				methodRule();
				setState(75);
				match(T__3);
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				match(T__5);
				setState(78);
				targetRule();
				setState(79);
				match(T__3);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(81);
				match(T__6);
				setState(82);
				argsRule();
				setState(83);
				match(T__3);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 4);
				{
				setState(85);
				match(T__7);
				setState(86);
				annotationRule();
				setState(87);
				match(T__3);
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(89);
				match(T__8);
				setState(90);
				annotatedTypeRule();
				setState(91);
				match(T__3);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 6);
				{
				setState(93);
				match(T__9);
				setState(94);
				annotatedArgsRule();
				setState(95);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodRuleContext extends ParserRuleContext {
		public MethodContext method() {
			return getRuleContext(MethodContext.class,0);
		}
		public MethodRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterMethodRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitMethodRule(this);
		}
	}

	public final MethodRuleContext methodRule() throws RecognitionException {
		MethodRuleContext _localctx = new MethodRuleContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_methodRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			method();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TargetRuleContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public TargetRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_targetRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterTargetRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitTargetRule(this);
		}
	}

	public final TargetRuleContext targetRule() throws RecognitionException {
		TargetRuleContext _localctx = new TargetRuleContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_targetRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			className();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgsRuleContext extends ParserRuleContext {
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public ArgsRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argsRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterArgsRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitArgsRule(this);
		}
	}

	public final ArgsRuleContext argsRule() throws RecognitionException {
		ArgsRuleContext _localctx = new ArgsRuleContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_argsRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			args();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationRuleContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public AnnotationRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterAnnotationRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitAnnotationRule(this);
		}
	}

	public final AnnotationRuleContext annotationRule() throws RecognitionException {
		AnnotationRuleContext _localctx = new AnnotationRuleContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_annotationRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			className();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotatedTypeRuleContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public AnnotatedTypeRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotatedTypeRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterAnnotatedTypeRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitAnnotatedTypeRule(this);
		}
	}

	public final AnnotatedTypeRuleContext annotatedTypeRule() throws RecognitionException {
		AnnotatedTypeRuleContext _localctx = new AnnotatedTypeRuleContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_annotatedTypeRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			className();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotatedArgsRuleContext extends ParserRuleContext {
		public TypeNamesContext typeNames() {
			return getRuleContext(TypeNamesContext.class,0);
		}
		public AnnotatedArgsRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotatedArgsRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterAnnotatedArgsRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitAnnotatedArgsRule(this);
		}
	}

	public final AnnotatedArgsRuleContext annotatedArgsRule() throws RecognitionException {
		AnnotatedArgsRuleContext _localctx = new AnnotatedArgsRuleContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_annotatedArgsRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			typeNames();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodContext extends ParserRuleContext {
		public KindContext kind() {
			return getRuleContext(KindContext.class,0);
		}
		public ModifiersContext modifiers() {
			return getRuleContext(ModifiersContext.class,0);
		}
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public MethodNameContext methodName() {
			return getRuleContext(MethodNameContext.class,0);
		}
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public ExceptionsContext exceptions() {
			return getRuleContext(ExceptionsContext.class,0);
		}
		public MethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_method; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitMethod(this);
		}
	}

	public final MethodContext method() throws RecognitionException {
		MethodContext _localctx = new MethodContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_method);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			kind();
			setState(112);
			modifiers();
			setState(113);
			returnType();
			setState(114);
			className();
			setState(115);
			match(T__10);
			setState(116);
			methodName();
			setState(117);
			match(T__2);
			setState(118);
			args();
			setState(119);
			match(T__3);
			setState(122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__11) {
				{
				setState(120);
				match(T__11);
				setState(121);
				exceptions();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KindContext extends ParserRuleContext {
		public KindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kind; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterKind(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitKind(this);
		}
	}

	public final KindContext kind() throws RecognitionException {
		KindContext _localctx = new KindContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_kind);
		try {
			setState(127);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__12:
				enterOuterAlt(_localctx, 1);
				{
				setState(124);
				match(T__12);
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 2);
				{
				setState(125);
				match(T__13);
				}
				break;
			case T__14:
			case T__15:
			case T__16:
			case T__17:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifiersContext extends ParserRuleContext {
		public ModifierContext modifier() {
			return getRuleContext(ModifierContext.class,0);
		}
		public ModifiersContext modifiers() {
			return getRuleContext(ModifiersContext.class,0);
		}
		public ModifiersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifiers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterModifiers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitModifiers(this);
		}
	}

	public final ModifiersContext modifiers() throws RecognitionException {
		ModifiersContext _localctx = new ModifiersContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_modifiers);
		try {
			setState(133);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__14:
			case T__15:
			case T__16:
			case T__17:
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				modifier();
				setState(130);
				modifiers();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifierContext extends ParserRuleContext {
		public ModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitModifier(this);
		}
	}

	public final ModifierContext modifier() throws RecognitionException {
		ModifierContext _localctx = new ModifierContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_modifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnTypeContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitReturnType(this);
		}
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_returnType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			typeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassNameContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ClassNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_className; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterClassName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitClassName(this);
		}
	}

	public final ClassNameContext className() throws RecognitionException {
		ClassNameContext _localctx = new ClassNameContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_className);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			typeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PointcutParser.IDENTIFIER, 0); }
		public MethodNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterMethodName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitMethodName(this);
		}
	}

	public final MethodNameContext methodName() throws RecognitionException {
		MethodNameContext _localctx = new MethodNameContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_methodName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgsContext extends ParserRuleContext {
		public ArgWildcardContext argWildcard() {
			return getRuleContext(ArgWildcardContext.class,0);
		}
		public ArgContext arg() {
			return getRuleContext(ArgContext.class,0);
		}
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public ArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_args; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitArgs(this);
		}
	}

	public final ArgsContext args() throws RecognitionException {
		ArgsContext _localctx = new ArgsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_args);
		try {
			setState(150);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(143);
				argWildcard();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(144);
				arg();
				setState(145);
				match(T__18);
				setState(146);
				args();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(148);
				arg();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgWildcardContext extends ParserRuleContext {
		public ArgWildcardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argWildcard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterArgWildcard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitArgWildcard(this);
		}
	}

	public final ArgWildcardContext argWildcard() throws RecognitionException {
		ArgWildcardContext _localctx = new ArgWildcardContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_argWildcard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(T__19);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitArg(this);
		}
	}

	public final ArgContext arg() throws RecognitionException {
		ArgContext _localctx = new ArgContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_arg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			typeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExceptionsContext extends ParserRuleContext {
		public ExceptionContext exception() {
			return getRuleContext(ExceptionContext.class,0);
		}
		public ExceptionsContext exceptions() {
			return getRuleContext(ExceptionsContext.class,0);
		}
		public ExceptionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exceptions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterExceptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitExceptions(this);
		}
	}

	public final ExceptionsContext exceptions() throws RecognitionException {
		ExceptionsContext _localctx = new ExceptionsContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_exceptions);
		try {
			setState(161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				exception();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				exception();
				setState(158);
				match(T__18);
				setState(159);
				exceptions();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExceptionContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ExceptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exception; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterException(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitException(this);
		}
	}

	public final ExceptionContext exception() throws RecognitionException {
		ExceptionContext _localctx = new ExceptionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_exception);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			typeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeNamesContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TypeNamesContext typeNames() {
			return getRuleContext(TypeNamesContext.class,0);
		}
		public TypeNamesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeNames; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterTypeNames(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitTypeNames(this);
		}
	}

	public final TypeNamesContext typeNames() throws RecognitionException {
		TypeNamesContext _localctx = new TypeNamesContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_typeNames);
		try {
			setState(170);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(165);
				typeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(166);
				typeName();
				setState(167);
				match(T__18);
				setState(168);
				typeNames();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PointcutParser.IDENTIFIER, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).enterTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PointcutListener ) ((PointcutListener)listener).exitTypeName(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_typeName);
		try {
			setState(176);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(172);
				match(IDENTIFIER);
				setState(173);
				match(T__10);
				setState(174);
				typeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(175);
				match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\30\u00b5\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3<\n\3\3\4\3\4\3\4\3\4\3\4\5"+
		"\4C\n\4\3\5\3\5\3\5\3\5\3\5\5\5J\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6d\n"+
		"\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r}\n\r\3\16\3\16\3\16\5\16\u0082\n\16\3\17"+
		"\3\17\3\17\3\17\5\17\u0088\n\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u0099\n\24\3\25\3\25\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\3\27\5\27\u00a4\n\27\3\30\3\30\3\31\3\31\3\31\3\31"+
		"\3\31\5\31\u00ad\n\31\3\32\3\32\3\32\3\32\5\32\u00b3\n\32\3\32\2\2\33"+
		"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\2\3\3\2\21\24\2"+
		"\u00ad\2\64\3\2\2\2\4;\3\2\2\2\6B\3\2\2\2\bI\3\2\2\2\nc\3\2\2\2\fe\3\2"+
		"\2\2\16g\3\2\2\2\20i\3\2\2\2\22k\3\2\2\2\24m\3\2\2\2\26o\3\2\2\2\30q\3"+
		"\2\2\2\32\u0081\3\2\2\2\34\u0087\3\2\2\2\36\u0089\3\2\2\2 \u008b\3\2\2"+
		"\2\"\u008d\3\2\2\2$\u008f\3\2\2\2&\u0098\3\2\2\2(\u009a\3\2\2\2*\u009c"+
		"\3\2\2\2,\u00a3\3\2\2\2.\u00a5\3\2\2\2\60\u00ac\3\2\2\2\62\u00b2\3\2\2"+
		"\2\64\65\5\4\3\2\65\3\3\2\2\2\66<\5\6\4\2\678\5\6\4\289\7\3\2\29:\5\4"+
		"\3\2:<\3\2\2\2;\66\3\2\2\2;\67\3\2\2\2<\5\3\2\2\2=C\5\b\5\2>?\5\b\5\2"+
		"?@\7\4\2\2@A\5\6\4\2AC\3\2\2\2B=\3\2\2\2B>\3\2\2\2C\7\3\2\2\2DJ\5\n\6"+
		"\2EF\7\5\2\2FG\5\2\2\2GH\7\6\2\2HJ\3\2\2\2ID\3\2\2\2IE\3\2\2\2J\t\3\2"+
		"\2\2KL\7\7\2\2LM\5\f\7\2MN\7\6\2\2Nd\3\2\2\2OP\7\b\2\2PQ\5\16\b\2QR\7"+
		"\6\2\2Rd\3\2\2\2ST\7\t\2\2TU\5\20\t\2UV\7\6\2\2Vd\3\2\2\2WX\7\n\2\2XY"+
		"\5\22\n\2YZ\7\6\2\2Zd\3\2\2\2[\\\7\13\2\2\\]\5\24\13\2]^\7\6\2\2^d\3\2"+
		"\2\2_`\7\f\2\2`a\5\26\f\2ab\7\6\2\2bd\3\2\2\2cK\3\2\2\2cO\3\2\2\2cS\3"+
		"\2\2\2cW\3\2\2\2c[\3\2\2\2c_\3\2\2\2d\13\3\2\2\2ef\5\30\r\2f\r\3\2\2\2"+
		"gh\5\"\22\2h\17\3\2\2\2ij\5&\24\2j\21\3\2\2\2kl\5\"\22\2l\23\3\2\2\2m"+
		"n\5\"\22\2n\25\3\2\2\2op\5\60\31\2p\27\3\2\2\2qr\5\32\16\2rs\5\34\17\2"+
		"st\5 \21\2tu\5\"\22\2uv\7\r\2\2vw\5$\23\2wx\7\5\2\2xy\5&\24\2y|\7\6\2"+
		"\2z{\7\16\2\2{}\5,\27\2|z\3\2\2\2|}\3\2\2\2}\31\3\2\2\2~\u0082\7\17\2"+
		"\2\177\u0082\7\20\2\2\u0080\u0082\3\2\2\2\u0081~\3\2\2\2\u0081\177\3\2"+
		"\2\2\u0081\u0080\3\2\2\2\u0082\33\3\2\2\2\u0083\u0084\5\36\20\2\u0084"+
		"\u0085\5\34\17\2\u0085\u0088\3\2\2\2\u0086\u0088\3\2\2\2\u0087\u0083\3"+
		"\2\2\2\u0087\u0086\3\2\2\2\u0088\35\3\2\2\2\u0089\u008a\t\2\2\2\u008a"+
		"\37\3\2\2\2\u008b\u008c\5\62\32\2\u008c!\3\2\2\2\u008d\u008e\5\62\32\2"+
		"\u008e#\3\2\2\2\u008f\u0090\7\30\2\2\u0090%\3\2\2\2\u0091\u0099\5(\25"+
		"\2\u0092\u0093\5*\26\2\u0093\u0094\7\25\2\2\u0094\u0095\5&\24\2\u0095"+
		"\u0099\3\2\2\2\u0096\u0099\5*\26\2\u0097\u0099\3\2\2\2\u0098\u0091\3\2"+
		"\2\2\u0098\u0092\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0097\3\2\2\2\u0099"+
		"\'\3\2\2\2\u009a\u009b\7\26\2\2\u009b)\3\2\2\2\u009c\u009d\5\62\32\2\u009d"+
		"+\3\2\2\2\u009e\u00a4\5.\30\2\u009f\u00a0\5.\30\2\u00a0\u00a1\7\25\2\2"+
		"\u00a1\u00a2\5,\27\2\u00a2\u00a4\3\2\2\2\u00a3\u009e\3\2\2\2\u00a3\u009f"+
		"\3\2\2\2\u00a4-\3\2\2\2\u00a5\u00a6\5\62\32\2\u00a6/\3\2\2\2\u00a7\u00ad"+
		"\5\62\32\2\u00a8\u00a9\5\62\32\2\u00a9\u00aa\7\25\2\2\u00aa\u00ab\5\60"+
		"\31\2\u00ab\u00ad\3\2\2\2\u00ac\u00a7\3\2\2\2\u00ac\u00a8\3\2\2\2\u00ad"+
		"\61\3\2\2\2\u00ae\u00af\7\30\2\2\u00af\u00b0\7\r\2\2\u00b0\u00b3\5\62"+
		"\32\2\u00b1\u00b3\7\30\2\2\u00b2\u00ae\3\2\2\2\u00b2\u00b1\3\2\2\2\u00b3"+
		"\63\3\2\2\2\r;BIc|\u0081\u0087\u0098\u00a3\u00ac\u00b2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}