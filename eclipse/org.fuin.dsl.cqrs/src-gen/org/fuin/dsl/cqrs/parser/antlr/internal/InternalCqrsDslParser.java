package org.fuin.dsl.cqrs.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.fuin.dsl.cqrs.services.CqrsDslGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
@SuppressWarnings("all")
public class InternalCqrsDslParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_DOC", "RULE_ID", "RULE_INT", "RULE_HEX", "RULE_DECIMAL", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "'context'", "'{'", "'}'", "'module'", "'dependency'", "'local'", "'import'", "'hint'", "'type'", "'element'", "'generics'", "'acceptable'", "'detection'", "'resolution'", "'consistency'", "'data-protection'", "'protection'", "'category'", "','", "'subject'", "'purpose'", "'lawful-basis'", "'retention'", "'then'", "'protected-by'", "'constraint'", "'input'", "'|'", "'exception'", "'message'", "'business-rule'", "'requires'", "'||'", "'&&'", "'!'", "'('", "')'", "'.'", "'is-empty'", "'null'", "'annotation'", "'cid'", "'value-object'", "'base'", "'identified-by'", "'entity-id'", "'identifies'", "'aggregate-id'", "'enum'", "'instances'", "'deprecated'", "'event'", "'copies-attributes-of'", "'entity'", "'identifier'", "'root'", "'aggregate'", "'key'", "'attributes'", "'on-collision'", "'display-as'", "'no-key'", "'constructor'", "'fires'", "'operation-context'", "'returns'", "'optional'", "'method'", "'ref'", "'rest-path'", "'slabel'", "'label'", "'tooltip'", "'prompt'", "'examples'", "'<'", "'>'", "'invariants'", "'preconditions'", "'business-rules'", "'own-id'", "'@'", "'service'", "'command'", "'target'", "'sla'", "'command-handler'", "'handles'", "'uses'", "'projection'", "'view'", "'cron-schedule'", "'process-manager'", "'instance-key'", "'process-states'", "'reacts-to'", "'in-state'", "'correlate-by'", "'issues-commands'", "'transition-to'", "'arm-timeout'", "'cancel-timeout'", "':'", "'['", "']'", "'true'", "'false'", "'*'", "'millis'", "'seconds'", "'minutes'", "'hours'", "'days'", "'weeks'", "'months'", "'years'", "'weak'", "'strong'", "'never'", "'manually'", "'automatic'", "'workflow'", "'none'", "'personal'", "'sensitive'", "'consent'", "'explicit_consent'", "'contract'", "'legal_obligation'", "'vital_interests'", "'public_task'", "'legitimate_interests'", "'health'", "'genetic'", "'biometric'", "'racial'", "'political'", "'religious'", "'philosophical'", "'trade_union'", "'sex_life'", "'sexual_orientation'", "'delete'", "'anonymize'", "'pseudonymize'", "'archive'", "'review'", "'=='", "'!='", "'<='", "'>='", "'refuse'", "'overwrite'", "'skip'"
    };
    public static final int T__144=144;
    public static final int T__143=143;
    public static final int T__146=146;
    public static final int RULE_HEX=8;
    public static final int T__50=50;
    public static final int T__145=145;
    public static final int T__140=140;
    public static final int T__142=142;
    public static final int T__141=141;
    public static final int T__59=59;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__137=137;
    public static final int T__52=52;
    public static final int T__136=136;
    public static final int T__53=53;
    public static final int T__139=139;
    public static final int T__54=54;
    public static final int T__138=138;
    public static final int T__133=133;
    public static final int T__132=132;
    public static final int T__60=60;
    public static final int T__135=135;
    public static final int T__61=61;
    public static final int T__134=134;
    public static final int RULE_ID=6;
    public static final int T__131=131;
    public static final int T__130=130;
    public static final int RULE_INT=7;
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=10;
    public static final int T__67=67;
    public static final int T__129=129;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__126=126;
    public static final int T__63=63;
    public static final int T__125=125;
    public static final int T__64=64;
    public static final int T__128=128;
    public static final int T__65=65;
    public static final int T__127=127;
    public static final int T__166=166;
    public static final int T__165=165;
    public static final int T__162=162;
    public static final int T__161=161;
    public static final int T__164=164;
    public static final int T__163=163;
    public static final int T__160=160;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__159=159;
    public static final int T__30=30;
    public static final int T__158=158;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__155=155;
    public static final int T__154=154;
    public static final int T__157=157;
    public static final int T__156=156;
    public static final int T__151=151;
    public static final int T__150=150;
    public static final int T__153=153;
    public static final int T__152=152;
    public static final int RULE_DOC=5;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__148=148;
    public static final int T__41=41;
    public static final int T__147=147;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__149=149;
    public static final int T__91=91;
    public static final int T__100=100;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__102=102;
    public static final int T__94=94;
    public static final int T__101=101;
    public static final int T__90=90;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__99=99;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int RULE_DECIMAL=9;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__122=122;
    public static final int T__70=70;
    public static final int T__121=121;
    public static final int T__71=71;
    public static final int T__124=124;
    public static final int T__72=72;
    public static final int T__123=123;
    public static final int T__120=120;
    public static final int RULE_STRING=4;
    public static final int RULE_SL_COMMENT=11;
    public static final int T__77=77;
    public static final int T__119=119;
    public static final int T__78=78;
    public static final int T__118=118;
    public static final int T__79=79;
    public static final int T__73=73;
    public static final int T__115=115;
    public static final int EOF=-1;
    public static final int T__74=74;
    public static final int T__114=114;
    public static final int T__75=75;
    public static final int T__117=117;
    public static final int T__76=76;
    public static final int T__116=116;
    public static final int T__80=80;
    public static final int T__111=111;
    public static final int T__81=81;
    public static final int T__110=110;
    public static final int T__82=82;
    public static final int T__113=113;
    public static final int T__83=83;
    public static final int T__112=112;
    public static final int RULE_WS=12;
    public static final int T__88=88;
    public static final int T__108=108;
    public static final int T__89=89;
    public static final int T__107=107;
    public static final int T__109=109;
    public static final int T__84=84;
    public static final int T__104=104;
    public static final int T__85=85;
    public static final int T__103=103;
    public static final int T__86=86;
    public static final int T__106=106;
    public static final int T__87=87;
    public static final int T__105=105;

    // delegates
    // delegators


        public InternalCqrsDslParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalCqrsDslParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalCqrsDslParser.tokenNames; }
    public String getGrammarFileName() { return "InternalCqrsDsl.g"; }



     	private CqrsDslGrammarAccess grammarAccess;

        public InternalCqrsDslParser(TokenStream input, CqrsDslGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "DomainModel";
       	}

       	@Override
       	protected CqrsDslGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleDomainModel"
    // InternalCqrsDsl.g:65:1: entryRuleDomainModel returns [EObject current=null] : iv_ruleDomainModel= ruleDomainModel EOF ;
    public final EObject entryRuleDomainModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDomainModel = null;


        try {
            // InternalCqrsDsl.g:65:52: (iv_ruleDomainModel= ruleDomainModel EOF )
            // InternalCqrsDsl.g:66:2: iv_ruleDomainModel= ruleDomainModel EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getDomainModelRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleDomainModel=ruleDomainModel();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleDomainModel; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDomainModel"


    // $ANTLR start "ruleDomainModel"
    // InternalCqrsDsl.g:72:1: ruleDomainModel returns [EObject current=null] : ( (lv_contexts_0_0= ruleContext ) )* ;
    public final EObject ruleDomainModel() throws RecognitionException {
        EObject current = null;

        EObject lv_contexts_0_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:78:2: ( ( (lv_contexts_0_0= ruleContext ) )* )
            // InternalCqrsDsl.g:79:2: ( (lv_contexts_0_0= ruleContext ) )*
            {
            // InternalCqrsDsl.g:79:2: ( (lv_contexts_0_0= ruleContext ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==13) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCqrsDsl.g:80:3: (lv_contexts_0_0= ruleContext )
            	    {
            	    // InternalCqrsDsl.g:80:3: (lv_contexts_0_0= ruleContext )
            	    // InternalCqrsDsl.g:81:4: lv_contexts_0_0= ruleContext
            	    {
            	    if ( state.backtracking==0 ) {

            	      				newCompositeNode(grammarAccess.getDomainModelAccess().getContextsContextParserRuleCall_0());
            	      			
            	    }
            	    pushFollow(FOLLOW_3);
            	    lv_contexts_0_0=ruleContext();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				if (current==null) {
            	      					current = createModelElementForParent(grammarAccess.getDomainModelRule());
            	      				}
            	      				add(
            	      					current,
            	      					"contexts",
            	      					lv_contexts_0_0,
            	      					"org.fuin.dsl.cqrs.CqrsDsl.Context");
            	      				afterParserOrEnumRuleCall();
            	      			
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDomainModel"


    // $ANTLR start "entryRuleContext"
    // InternalCqrsDsl.g:101:1: entryRuleContext returns [EObject current=null] : iv_ruleContext= ruleContext EOF ;
    public final EObject entryRuleContext() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContext = null;


        try {
            // InternalCqrsDsl.g:101:48: (iv_ruleContext= ruleContext EOF )
            // InternalCqrsDsl.g:102:2: iv_ruleContext= ruleContext EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getContextRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleContext=ruleContext();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleContext; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleContext"


    // $ANTLR start "ruleContext"
    // InternalCqrsDsl.g:108:1: ruleContext returns [EObject current=null] : (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_dependencies_3_0= ruleDependency ) )* ( (lv_imports_4_0= ruleImport ) )* ( (lv_hints_5_0= ruleHint ) )* ( (lv_modules_6_0= ruleModule ) )* otherlv_7= '}' ) ;
    public final EObject ruleContext() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_7=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_dependencies_3_0 = null;

        EObject lv_imports_4_0 = null;

        EObject lv_hints_5_0 = null;

        EObject lv_modules_6_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:114:2: ( (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_dependencies_3_0= ruleDependency ) )* ( (lv_imports_4_0= ruleImport ) )* ( (lv_hints_5_0= ruleHint ) )* ( (lv_modules_6_0= ruleModule ) )* otherlv_7= '}' ) )
            // InternalCqrsDsl.g:115:2: (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_dependencies_3_0= ruleDependency ) )* ( (lv_imports_4_0= ruleImport ) )* ( (lv_hints_5_0= ruleHint ) )* ( (lv_modules_6_0= ruleModule ) )* otherlv_7= '}' )
            {
            // InternalCqrsDsl.g:115:2: (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_dependencies_3_0= ruleDependency ) )* ( (lv_imports_4_0= ruleImport ) )* ( (lv_hints_5_0= ruleHint ) )* ( (lv_modules_6_0= ruleModule ) )* otherlv_7= '}' )
            // InternalCqrsDsl.g:116:3: otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_dependencies_3_0= ruleDependency ) )* ( (lv_imports_4_0= ruleImport ) )* ( (lv_hints_5_0= ruleHint ) )* ( (lv_modules_6_0= ruleModule ) )* otherlv_7= '}'
            {
            otherlv_0=(Token)match(input,13,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getContextAccess().getContextKeyword_0());
              		
            }
            // InternalCqrsDsl.g:120:3: ( (lv_name_1_0= ruleFQN ) )
            // InternalCqrsDsl.g:121:4: (lv_name_1_0= ruleFQN )
            {
            // InternalCqrsDsl.g:121:4: (lv_name_1_0= ruleFQN )
            // InternalCqrsDsl.g:122:5: lv_name_1_0= ruleFQN
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getContextAccess().getNameFQNParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_5);
            lv_name_1_0=ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getContextRule());
              					}
              					set(
              						current,
              						"name",
              						lv_name_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.FQN");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_6); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getContextAccess().getLeftCurlyBracketKeyword_2());
              		
            }
            // InternalCqrsDsl.g:143:3: ( (lv_dependencies_3_0= ruleDependency ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==17) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalCqrsDsl.g:144:4: (lv_dependencies_3_0= ruleDependency )
            	    {
            	    // InternalCqrsDsl.g:144:4: (lv_dependencies_3_0= ruleDependency )
            	    // InternalCqrsDsl.g:145:5: lv_dependencies_3_0= ruleDependency
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getContextAccess().getDependenciesDependencyParserRuleCall_3_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_6);
            	    lv_dependencies_3_0=ruleDependency();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getContextRule());
            	      					}
            	      					add(
            	      						current,
            	      						"dependencies",
            	      						lv_dependencies_3_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Dependency");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            // InternalCqrsDsl.g:162:3: ( (lv_imports_4_0= ruleImport ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==19) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCqrsDsl.g:163:4: (lv_imports_4_0= ruleImport )
            	    {
            	    // InternalCqrsDsl.g:163:4: (lv_imports_4_0= ruleImport )
            	    // InternalCqrsDsl.g:164:5: lv_imports_4_0= ruleImport
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getContextAccess().getImportsImportParserRuleCall_4_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_7);
            	    lv_imports_4_0=ruleImport();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getContextRule());
            	      					}
            	      					add(
            	      						current,
            	      						"imports",
            	      						lv_imports_4_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Import");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // InternalCqrsDsl.g:181:3: ( (lv_hints_5_0= ruleHint ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==RULE_DOC||LA4_0==20) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalCqrsDsl.g:182:4: (lv_hints_5_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:182:4: (lv_hints_5_0= ruleHint )
            	    // InternalCqrsDsl.g:183:5: lv_hints_5_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getContextAccess().getHintsHintParserRuleCall_5_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_8);
            	    lv_hints_5_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getContextRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_5_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // InternalCqrsDsl.g:200:3: ( (lv_modules_6_0= ruleModule ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==16) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalCqrsDsl.g:201:4: (lv_modules_6_0= ruleModule )
            	    {
            	    // InternalCqrsDsl.g:201:4: (lv_modules_6_0= ruleModule )
            	    // InternalCqrsDsl.g:202:5: lv_modules_6_0= ruleModule
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getContextAccess().getModulesModuleParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_9);
            	    lv_modules_6_0=ruleModule();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getContextRule());
            	      					}
            	      					add(
            	      						current,
            	      						"modules",
            	      						lv_modules_6_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Module");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            otherlv_7=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getContextAccess().getRightCurlyBracketKeyword_7());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleContext"


    // $ANTLR start "entryRuleModule"
    // InternalCqrsDsl.g:227:1: entryRuleModule returns [EObject current=null] : iv_ruleModule= ruleModule EOF ;
    public final EObject entryRuleModule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModule = null;


        try {
            // InternalCqrsDsl.g:227:47: (iv_ruleModule= ruleModule EOF )
            // InternalCqrsDsl.g:228:2: iv_ruleModule= ruleModule EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getModuleRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleModule=ruleModule();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleModule; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModule"


    // $ANTLR start "ruleModule"
    // InternalCqrsDsl.g:234:1: ruleModule returns [EObject current=null] : (otherlv_0= 'module' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_metaInfo_3_0= ruleTypeMetaInfo ) ) ( (lv_hints_4_0= ruleHint ) )* ( (lv_dependencies_5_0= ruleDependency ) )* ( (lv_imports_6_0= ruleImport ) )* ( (lv_elements_7_0= ruleAbstractElement ) )* otherlv_8= '}' ) ;
    public final EObject ruleModule() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_8=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_metaInfo_3_0 = null;

        EObject lv_hints_4_0 = null;

        EObject lv_dependencies_5_0 = null;

        EObject lv_imports_6_0 = null;

        EObject lv_elements_7_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:240:2: ( (otherlv_0= 'module' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_metaInfo_3_0= ruleTypeMetaInfo ) ) ( (lv_hints_4_0= ruleHint ) )* ( (lv_dependencies_5_0= ruleDependency ) )* ( (lv_imports_6_0= ruleImport ) )* ( (lv_elements_7_0= ruleAbstractElement ) )* otherlv_8= '}' ) )
            // InternalCqrsDsl.g:241:2: (otherlv_0= 'module' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_metaInfo_3_0= ruleTypeMetaInfo ) ) ( (lv_hints_4_0= ruleHint ) )* ( (lv_dependencies_5_0= ruleDependency ) )* ( (lv_imports_6_0= ruleImport ) )* ( (lv_elements_7_0= ruleAbstractElement ) )* otherlv_8= '}' )
            {
            // InternalCqrsDsl.g:241:2: (otherlv_0= 'module' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_metaInfo_3_0= ruleTypeMetaInfo ) ) ( (lv_hints_4_0= ruleHint ) )* ( (lv_dependencies_5_0= ruleDependency ) )* ( (lv_imports_6_0= ruleImport ) )* ( (lv_elements_7_0= ruleAbstractElement ) )* otherlv_8= '}' )
            // InternalCqrsDsl.g:242:3: otherlv_0= 'module' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_metaInfo_3_0= ruleTypeMetaInfo ) ) ( (lv_hints_4_0= ruleHint ) )* ( (lv_dependencies_5_0= ruleDependency ) )* ( (lv_imports_6_0= ruleImport ) )* ( (lv_elements_7_0= ruleAbstractElement ) )* otherlv_8= '}'
            {
            otherlv_0=(Token)match(input,16,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getModuleAccess().getModuleKeyword_0());
              		
            }
            // InternalCqrsDsl.g:246:3: ( (lv_name_1_0= ruleFQN ) )
            // InternalCqrsDsl.g:247:4: (lv_name_1_0= ruleFQN )
            {
            // InternalCqrsDsl.g:247:4: (lv_name_1_0= ruleFQN )
            // InternalCqrsDsl.g:248:5: lv_name_1_0= ruleFQN
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getModuleAccess().getNameFQNParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_5);
            lv_name_1_0=ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getModuleRule());
              					}
              					set(
              						current,
              						"name",
              						lv_name_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.FQN");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_10); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getModuleAccess().getLeftCurlyBracketKeyword_2());
              		
            }
            // InternalCqrsDsl.g:269:3: ( (lv_metaInfo_3_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:270:4: (lv_metaInfo_3_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:270:4: (lv_metaInfo_3_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:271:5: lv_metaInfo_3_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getModuleAccess().getMetaInfoTypeMetaInfoParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_11);
            lv_metaInfo_3_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getModuleRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_3_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:288:3: ( (lv_hints_4_0= ruleHint ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==RULE_DOC) ) {
                    int LA6_2 = input.LA(2);

                    if ( (LA6_2==20) ) {
                        alt6=1;
                    }


                }
                else if ( (LA6_0==20) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalCqrsDsl.g:289:4: (lv_hints_4_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:289:4: (lv_hints_4_0= ruleHint )
            	    // InternalCqrsDsl.g:290:5: lv_hints_4_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getModuleAccess().getHintsHintParserRuleCall_4_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_11);
            	    lv_hints_4_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getModuleRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_4_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            // InternalCqrsDsl.g:307:3: ( (lv_dependencies_5_0= ruleDependency ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==17) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCqrsDsl.g:308:4: (lv_dependencies_5_0= ruleDependency )
            	    {
            	    // InternalCqrsDsl.g:308:4: (lv_dependencies_5_0= ruleDependency )
            	    // InternalCqrsDsl.g:309:5: lv_dependencies_5_0= ruleDependency
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getModuleAccess().getDependenciesDependencyParserRuleCall_5_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_12);
            	    lv_dependencies_5_0=ruleDependency();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getModuleRule());
            	      					}
            	      					add(
            	      						current,
            	      						"dependencies",
            	      						lv_dependencies_5_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Dependency");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            // InternalCqrsDsl.g:326:3: ( (lv_imports_6_0= ruleImport ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==19) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCqrsDsl.g:327:4: (lv_imports_6_0= ruleImport )
            	    {
            	    // InternalCqrsDsl.g:327:4: (lv_imports_6_0= ruleImport )
            	    // InternalCqrsDsl.g:328:5: lv_imports_6_0= ruleImport
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getModuleAccess().getImportsImportParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_13);
            	    lv_imports_6_0=ruleImport();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getModuleRule());
            	      					}
            	      					add(
            	      						current,
            	      						"imports",
            	      						lv_imports_6_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Import");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // InternalCqrsDsl.g:345:3: ( (lv_elements_7_0= ruleAbstractElement ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==RULE_DOC||LA9_0==21||LA9_0==28||LA9_0==38||LA9_0==41||LA9_0==53||LA9_0==55||LA9_0==58||(LA9_0>=60 && LA9_0<=61)||LA9_0==64||LA9_0==66||LA9_0==69||(LA9_0>=94 && LA9_0<=96)||LA9_0==99||(LA9_0>=102 && LA9_0<=103)||LA9_0==105) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCqrsDsl.g:346:4: (lv_elements_7_0= ruleAbstractElement )
            	    {
            	    // InternalCqrsDsl.g:346:4: (lv_elements_7_0= ruleAbstractElement )
            	    // InternalCqrsDsl.g:347:5: lv_elements_7_0= ruleAbstractElement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getModuleAccess().getElementsAbstractElementParserRuleCall_7_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_14);
            	    lv_elements_7_0=ruleAbstractElement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getModuleRule());
            	      					}
            	      					add(
            	      						current,
            	      						"elements",
            	      						lv_elements_7_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.AbstractElement");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            otherlv_8=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_8, grammarAccess.getModuleAccess().getRightCurlyBracketKeyword_8());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModule"


    // $ANTLR start "entryRuleDependency"
    // InternalCqrsDsl.g:372:1: entryRuleDependency returns [EObject current=null] : iv_ruleDependency= ruleDependency EOF ;
    public final EObject entryRuleDependency() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDependency = null;


        try {
            // InternalCqrsDsl.g:372:51: (iv_ruleDependency= ruleDependency EOF )
            // InternalCqrsDsl.g:373:2: iv_ruleDependency= ruleDependency EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getDependencyRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleDependency=ruleDependency();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleDependency; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDependency"


    // $ANTLR start "ruleDependency"
    // InternalCqrsDsl.g:379:1: ruleDependency returns [EObject current=null] : (otherlv_0= 'dependency' ( (lv_coordinate_1_0= RULE_STRING ) ) (otherlv_2= 'local' ( (lv_local_3_0= RULE_STRING ) ) )? ) ;
    public final EObject ruleDependency() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_coordinate_1_0=null;
        Token otherlv_2=null;
        Token lv_local_3_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:385:2: ( (otherlv_0= 'dependency' ( (lv_coordinate_1_0= RULE_STRING ) ) (otherlv_2= 'local' ( (lv_local_3_0= RULE_STRING ) ) )? ) )
            // InternalCqrsDsl.g:386:2: (otherlv_0= 'dependency' ( (lv_coordinate_1_0= RULE_STRING ) ) (otherlv_2= 'local' ( (lv_local_3_0= RULE_STRING ) ) )? )
            {
            // InternalCqrsDsl.g:386:2: (otherlv_0= 'dependency' ( (lv_coordinate_1_0= RULE_STRING ) ) (otherlv_2= 'local' ( (lv_local_3_0= RULE_STRING ) ) )? )
            // InternalCqrsDsl.g:387:3: otherlv_0= 'dependency' ( (lv_coordinate_1_0= RULE_STRING ) ) (otherlv_2= 'local' ( (lv_local_3_0= RULE_STRING ) ) )?
            {
            otherlv_0=(Token)match(input,17,FOLLOW_15); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getDependencyAccess().getDependencyKeyword_0());
              		
            }
            // InternalCqrsDsl.g:391:3: ( (lv_coordinate_1_0= RULE_STRING ) )
            // InternalCqrsDsl.g:392:4: (lv_coordinate_1_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:392:4: (lv_coordinate_1_0= RULE_STRING )
            // InternalCqrsDsl.g:393:5: lv_coordinate_1_0= RULE_STRING
            {
            lv_coordinate_1_0=(Token)match(input,RULE_STRING,FOLLOW_16); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_coordinate_1_0, grammarAccess.getDependencyAccess().getCoordinateSTRINGTerminalRuleCall_1_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getDependencyRule());
              					}
              					setWithLastConsumed(
              						current,
              						"coordinate",
              						lv_coordinate_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.STRING");
              				
            }

            }


            }

            // InternalCqrsDsl.g:409:3: (otherlv_2= 'local' ( (lv_local_3_0= RULE_STRING ) ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==18) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalCqrsDsl.g:410:4: otherlv_2= 'local' ( (lv_local_3_0= RULE_STRING ) )
                    {
                    otherlv_2=(Token)match(input,18,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_2, grammarAccess.getDependencyAccess().getLocalKeyword_2_0());
                      			
                    }
                    // InternalCqrsDsl.g:414:4: ( (lv_local_3_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:415:5: (lv_local_3_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:415:5: (lv_local_3_0= RULE_STRING )
                    // InternalCqrsDsl.g:416:6: lv_local_3_0= RULE_STRING
                    {
                    lv_local_3_0=(Token)match(input,RULE_STRING,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_local_3_0, grammarAccess.getDependencyAccess().getLocalSTRINGTerminalRuleCall_2_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getDependencyRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"local",
                      							lv_local_3_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDependency"


    // $ANTLR start "entryRuleImport"
    // InternalCqrsDsl.g:437:1: entryRuleImport returns [EObject current=null] : iv_ruleImport= ruleImport EOF ;
    public final EObject entryRuleImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImport = null;


        try {
            // InternalCqrsDsl.g:437:47: (iv_ruleImport= ruleImport EOF )
            // InternalCqrsDsl.g:438:2: iv_ruleImport= ruleImport EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getImportRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleImport=ruleImport();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleImport; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleImport"


    // $ANTLR start "ruleImport"
    // InternalCqrsDsl.g:444:1: ruleImport returns [EObject current=null] : (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) ) ;
    public final EObject ruleImport() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_importedNamespace_1_1 = null;

        AntlrDatatypeRuleToken lv_importedNamespace_1_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:450:2: ( (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) ) )
            // InternalCqrsDsl.g:451:2: (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) )
            {
            // InternalCqrsDsl.g:451:2: (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) )
            // InternalCqrsDsl.g:452:3: otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) )
            {
            otherlv_0=(Token)match(input,19,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getImportAccess().getImportKeyword_0());
              		
            }
            // InternalCqrsDsl.g:456:3: ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) )
            // InternalCqrsDsl.g:457:4: ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) )
            {
            // InternalCqrsDsl.g:457:4: ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) )
            // InternalCqrsDsl.g:458:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )
            {
            // InternalCqrsDsl.g:458:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )
            int alt11=2;
            alt11 = dfa11.predict(input);
            switch (alt11) {
                case 1 :
                    // InternalCqrsDsl.g:459:6: lv_importedNamespace_1_1= ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getImportAccess().getImportedNamespaceFQNParserRuleCall_1_0_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_importedNamespace_1_1=ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getImportRule());
                      						}
                      						set(
                      							current,
                      							"importedNamespace",
                      							lv_importedNamespace_1_1,
                      							"org.fuin.dsl.cqrs.CqrsDsl.FQN");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:475:6: lv_importedNamespace_1_2= ruleFQNWithWildcard
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getImportAccess().getImportedNamespaceFQNWithWildcardParserRuleCall_1_0_1());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_importedNamespace_1_2=ruleFQNWithWildcard();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getImportRule());
                      						}
                      						set(
                      							current,
                      							"importedNamespace",
                      							lv_importedNamespace_1_2,
                      							"org.fuin.dsl.cqrs.CqrsDsl.FQNWithWildcard");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }
                    break;

            }


            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleImport"


    // $ANTLR start "entryRuleHint"
    // InternalCqrsDsl.g:497:1: entryRuleHint returns [EObject current=null] : iv_ruleHint= ruleHint EOF ;
    public final EObject entryRuleHint() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleHint = null;


        try {
            // InternalCqrsDsl.g:497:45: (iv_ruleHint= ruleHint EOF )
            // InternalCqrsDsl.g:498:2: iv_ruleHint= ruleHint EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getHintRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleHint=ruleHint();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleHint; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleHint"


    // $ANTLR start "ruleHint"
    // InternalCqrsDsl.g:504:1: ruleHint returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) ) ;
    public final EObject ruleHint() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_2_0 = null;

        EObject lv_json_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:510:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) ) )
            // InternalCqrsDsl.g:511:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) )
            {
            // InternalCqrsDsl.g:511:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) )
            // InternalCqrsDsl.g:512:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) )
            {
            // InternalCqrsDsl.g:512:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==RULE_DOC) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalCqrsDsl.g:513:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:513:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:514:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_17); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getHintAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getHintRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,20,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getHintAccess().getHintKeyword_1());
              		
            }
            // InternalCqrsDsl.g:534:3: ( (lv_name_2_0= ruleFQN ) )
            // InternalCqrsDsl.g:535:4: (lv_name_2_0= ruleFQN )
            {
            // InternalCqrsDsl.g:535:4: (lv_name_2_0= ruleFQN )
            // InternalCqrsDsl.g:536:5: lv_name_2_0= ruleFQN
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getHintAccess().getNameFQNParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_18);
            lv_name_2_0=ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getHintRule());
              					}
              					set(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.FQN");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:553:3: ( (lv_json_3_0= ruleJSON ) )
            // InternalCqrsDsl.g:554:4: (lv_json_3_0= ruleJSON )
            {
            // InternalCqrsDsl.g:554:4: (lv_json_3_0= ruleJSON )
            // InternalCqrsDsl.g:555:5: lv_json_3_0= ruleJSON
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getHintAccess().getJsonJSONParserRuleCall_3_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_json_3_0=ruleJSON();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getHintRule());
              					}
              					set(
              						current,
              						"json",
              						lv_json_3_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.JSON");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleHint"


    // $ANTLR start "entryRuleAbstractElement"
    // InternalCqrsDsl.g:576:1: entryRuleAbstractElement returns [EObject current=null] : iv_ruleAbstractElement= ruleAbstractElement EOF ;
    public final EObject entryRuleAbstractElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractElement = null;


        try {
            // InternalCqrsDsl.g:576:56: (iv_ruleAbstractElement= ruleAbstractElement EOF )
            // InternalCqrsDsl.g:577:2: iv_ruleAbstractElement= ruleAbstractElement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAbstractElementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAbstractElement=ruleAbstractElement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAbstractElement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAbstractElement"


    // $ANTLR start "ruleAbstractElement"
    // InternalCqrsDsl.g:583:1: ruleAbstractElement returns [EObject current=null] : (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_BusinessRule_4= ruleBusinessRule | this_Event_5= ruleEvent | this_Command_6= ruleCommand | this_CommandHandler_7= ruleCommandHandler | this_Projection_8= ruleProjection | this_View_9= ruleView | this_ProcessManager_10= ruleProcessManager | this_DataProtection_11= ruleDataProtection ) ;
    public final EObject ruleAbstractElement() throws RecognitionException {
        EObject current = null;

        EObject this_Constraint_0 = null;

        EObject this_Annotation_1 = null;

        EObject this_Type_2 = null;

        EObject this_Exception_3 = null;

        EObject this_BusinessRule_4 = null;

        EObject this_Event_5 = null;

        EObject this_Command_6 = null;

        EObject this_CommandHandler_7 = null;

        EObject this_Projection_8 = null;

        EObject this_View_9 = null;

        EObject this_ProcessManager_10 = null;

        EObject this_DataProtection_11 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:589:2: ( (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_BusinessRule_4= ruleBusinessRule | this_Event_5= ruleEvent | this_Command_6= ruleCommand | this_CommandHandler_7= ruleCommandHandler | this_Projection_8= ruleProjection | this_View_9= ruleView | this_ProcessManager_10= ruleProcessManager | this_DataProtection_11= ruleDataProtection ) )
            // InternalCqrsDsl.g:590:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_BusinessRule_4= ruleBusinessRule | this_Event_5= ruleEvent | this_Command_6= ruleCommand | this_CommandHandler_7= ruleCommandHandler | this_Projection_8= ruleProjection | this_View_9= ruleView | this_ProcessManager_10= ruleProcessManager | this_DataProtection_11= ruleDataProtection )
            {
            // InternalCqrsDsl.g:590:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_BusinessRule_4= ruleBusinessRule | this_Event_5= ruleEvent | this_Command_6= ruleCommand | this_CommandHandler_7= ruleCommandHandler | this_Projection_8= ruleProjection | this_View_9= ruleView | this_ProcessManager_10= ruleProcessManager | this_DataProtection_11= ruleDataProtection )
            int alt13=12;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // InternalCqrsDsl.g:591:3: this_Constraint_0= ruleConstraint
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getConstraintParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Constraint_0=ruleConstraint();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Constraint_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:600:3: this_Annotation_1= ruleAnnotation
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getAnnotationParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Annotation_1=ruleAnnotation();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Annotation_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:609:3: this_Type_2= ruleType
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getTypeParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Type_2=ruleType();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Type_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:618:3: this_Exception_3= ruleException
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getExceptionParserRuleCall_3());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Exception_3=ruleException();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Exception_3;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:627:3: this_BusinessRule_4= ruleBusinessRule
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getBusinessRuleParserRuleCall_4());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_BusinessRule_4=ruleBusinessRule();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_BusinessRule_4;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:636:3: this_Event_5= ruleEvent
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getEventParserRuleCall_5());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Event_5=ruleEvent();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Event_5;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:645:3: this_Command_6= ruleCommand
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getCommandParserRuleCall_6());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Command_6=ruleCommand();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Command_6;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:654:3: this_CommandHandler_7= ruleCommandHandler
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getCommandHandlerParserRuleCall_7());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_CommandHandler_7=ruleCommandHandler();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_CommandHandler_7;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 9 :
                    // InternalCqrsDsl.g:663:3: this_Projection_8= ruleProjection
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getProjectionParserRuleCall_8());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Projection_8=ruleProjection();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Projection_8;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 10 :
                    // InternalCqrsDsl.g:672:3: this_View_9= ruleView
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getViewParserRuleCall_9());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_View_9=ruleView();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_View_9;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 11 :
                    // InternalCqrsDsl.g:681:3: this_ProcessManager_10= ruleProcessManager
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getProcessManagerParserRuleCall_10());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_ProcessManager_10=ruleProcessManager();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_ProcessManager_10;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 12 :
                    // InternalCqrsDsl.g:690:3: this_DataProtection_11= ruleDataProtection
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractElementAccess().getDataProtectionParserRuleCall_11());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_DataProtection_11=ruleDataProtection();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_DataProtection_11;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAbstractElement"


    // $ANTLR start "entryRuleEntityElement"
    // InternalCqrsDsl.g:702:1: entryRuleEntityElement returns [EObject current=null] : iv_ruleEntityElement= ruleEntityElement EOF ;
    public final EObject entryRuleEntityElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntityElement = null;


        try {
            // InternalCqrsDsl.g:702:54: (iv_ruleEntityElement= ruleEntityElement EOF )
            // InternalCqrsDsl.g:703:2: iv_ruleEntityElement= ruleEntityElement EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getEntityElementRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleEntityElement=ruleEntityElement();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleEntityElement; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEntityElement"


    // $ANTLR start "ruleEntityElement"
    // InternalCqrsDsl.g:709:1: ruleEntityElement returns [EObject current=null] : (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection ) ;
    public final EObject ruleEntityElement() throws RecognitionException {
        EObject current = null;

        EObject this_Constraint_0 = null;

        EObject this_Annotation_1 = null;

        EObject this_Type_2 = null;

        EObject this_Exception_3 = null;

        EObject this_Event_4 = null;

        EObject this_Command_5 = null;

        EObject this_CommandHandler_6 = null;

        EObject this_Projection_7 = null;

        EObject this_View_8 = null;

        EObject this_ProcessManager_9 = null;

        EObject this_DataProtection_10 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:715:2: ( (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection ) )
            // InternalCqrsDsl.g:716:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection )
            {
            // InternalCqrsDsl.g:716:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection )
            int alt14=11;
            alt14 = dfa14.predict(input);
            switch (alt14) {
                case 1 :
                    // InternalCqrsDsl.g:717:3: this_Constraint_0= ruleConstraint
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getConstraintParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Constraint_0=ruleConstraint();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Constraint_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:726:3: this_Annotation_1= ruleAnnotation
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getAnnotationParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Annotation_1=ruleAnnotation();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Annotation_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:735:3: this_Type_2= ruleType
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getTypeParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Type_2=ruleType();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Type_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:744:3: this_Exception_3= ruleException
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getExceptionParserRuleCall_3());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Exception_3=ruleException();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Exception_3;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:753:3: this_Event_4= ruleEvent
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getEventParserRuleCall_4());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Event_4=ruleEvent();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Event_4;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:762:3: this_Command_5= ruleCommand
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getCommandParserRuleCall_5());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Command_5=ruleCommand();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Command_5;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:771:3: this_CommandHandler_6= ruleCommandHandler
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getCommandHandlerParserRuleCall_6());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_CommandHandler_6=ruleCommandHandler();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_CommandHandler_6;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:780:3: this_Projection_7= ruleProjection
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getProjectionParserRuleCall_7());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Projection_7=ruleProjection();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Projection_7;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 9 :
                    // InternalCqrsDsl.g:789:3: this_View_8= ruleView
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getViewParserRuleCall_8());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_View_8=ruleView();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_View_8;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 10 :
                    // InternalCqrsDsl.g:798:3: this_ProcessManager_9= ruleProcessManager
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getProcessManagerParserRuleCall_9());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_ProcessManager_9=ruleProcessManager();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_ProcessManager_9;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 11 :
                    // InternalCqrsDsl.g:807:3: this_DataProtection_10= ruleDataProtection
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getEntityElementAccess().getDataProtectionParserRuleCall_10());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_DataProtection_10=ruleDataProtection();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_DataProtection_10;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEntityElement"


    // $ANTLR start "entryRuleType"
    // InternalCqrsDsl.g:819:1: entryRuleType returns [EObject current=null] : iv_ruleType= ruleType EOF ;
    public final EObject entryRuleType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleType = null;


        try {
            // InternalCqrsDsl.g:819:45: (iv_ruleType= ruleType EOF )
            // InternalCqrsDsl.g:820:2: iv_ruleType= ruleType EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getTypeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleType=ruleType();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleType; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleType"


    // $ANTLR start "ruleType"
    // InternalCqrsDsl.g:826:1: ruleType returns [EObject current=null] : (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService ) ;
    public final EObject ruleType() throws RecognitionException {
        EObject current = null;

        EObject this_ExternalType_0 = null;

        EObject this_InternalType_1 = null;

        EObject this_Service_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:832:2: ( (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService ) )
            // InternalCqrsDsl.g:833:2: (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService )
            {
            // InternalCqrsDsl.g:833:2: (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService )
            int alt15=3;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                switch ( input.LA(2) ) {
                case 55:
                case 58:
                case 60:
                case 61:
                case 66:
                case 69:
                case 94:
                    {
                    alt15=2;
                    }
                    break;
                case 95:
                    {
                    alt15=3;
                    }
                    break;
                case 21:
                    {
                    alt15=1;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }

                }
                break;
            case 21:
                {
                alt15=1;
                }
                break;
            case 55:
            case 58:
            case 60:
            case 61:
            case 66:
            case 69:
            case 94:
                {
                alt15=2;
                }
                break;
            case 95:
                {
                alt15=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // InternalCqrsDsl.g:834:3: this_ExternalType_0= ruleExternalType
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getTypeAccess().getExternalTypeParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_ExternalType_0=ruleExternalType();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_ExternalType_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:843:3: this_InternalType_1= ruleInternalType
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getTypeAccess().getInternalTypeParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_InternalType_1=ruleInternalType();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_InternalType_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:852:3: this_Service_2= ruleService
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getTypeAccess().getServiceParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Service_2=ruleService();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Service_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleType"


    // $ANTLR start "entryRuleInternalType"
    // InternalCqrsDsl.g:864:1: entryRuleInternalType returns [EObject current=null] : iv_ruleInternalType= ruleInternalType EOF ;
    public final EObject entryRuleInternalType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInternalType = null;


        try {
            // InternalCqrsDsl.g:864:53: (iv_ruleInternalType= ruleInternalType EOF )
            // InternalCqrsDsl.g:865:2: iv_ruleInternalType= ruleInternalType EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getInternalTypeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleInternalType=ruleInternalType();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleInternalType; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleInternalType"


    // $ANTLR start "ruleInternalType"
    // InternalCqrsDsl.g:871:1: ruleInternalType returns [EObject current=null] : (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject ) ;
    public final EObject ruleInternalType() throws RecognitionException {
        EObject current = null;

        EObject this_AbstractVO_0 = null;

        EObject this_AbstractEntity_1 = null;

        EObject this_EnumObject_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:877:2: ( (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject ) )
            // InternalCqrsDsl.g:878:2: (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject )
            {
            // InternalCqrsDsl.g:878:2: (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject )
            int alt16=3;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                switch ( input.LA(2) ) {
                case 55:
                case 58:
                case 60:
                case 94:
                    {
                    alt16=1;
                    }
                    break;
                case 66:
                case 69:
                    {
                    alt16=2;
                    }
                    break;
                case 61:
                    {
                    alt16=3;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;
                }

                }
                break;
            case 55:
            case 58:
            case 60:
            case 94:
                {
                alt16=1;
                }
                break;
            case 66:
            case 69:
                {
                alt16=2;
                }
                break;
            case 61:
                {
                alt16=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // InternalCqrsDsl.g:879:3: this_AbstractVO_0= ruleAbstractVO
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getInternalTypeAccess().getAbstractVOParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_AbstractVO_0=ruleAbstractVO();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_AbstractVO_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:888:3: this_AbstractEntity_1= ruleAbstractEntity
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getInternalTypeAccess().getAbstractEntityParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_AbstractEntity_1=ruleAbstractEntity();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_AbstractEntity_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:897:3: this_EnumObject_2= ruleEnumObject
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getInternalTypeAccess().getEnumObjectParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_EnumObject_2=ruleEnumObject();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_EnumObject_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleInternalType"


    // $ANTLR start "entryRuleAbstractVO"
    // InternalCqrsDsl.g:909:1: entryRuleAbstractVO returns [EObject current=null] : iv_ruleAbstractVO= ruleAbstractVO EOF ;
    public final EObject entryRuleAbstractVO() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractVO = null;


        try {
            // InternalCqrsDsl.g:909:51: (iv_ruleAbstractVO= ruleAbstractVO EOF )
            // InternalCqrsDsl.g:910:2: iv_ruleAbstractVO= ruleAbstractVO EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAbstractVORule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAbstractVO=ruleAbstractVO();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAbstractVO; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAbstractVO"


    // $ANTLR start "ruleAbstractVO"
    // InternalCqrsDsl.g:916:1: ruleAbstractVO returns [EObject current=null] : (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId ) ;
    public final EObject ruleAbstractVO() throws RecognitionException {
        EObject current = null;

        EObject this_ValueObject_0 = null;

        EObject this_AbstractEntityId_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:922:2: ( (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId ) )
            // InternalCqrsDsl.g:923:2: (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId )
            {
            // InternalCqrsDsl.g:923:2: (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId )
            int alt17=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==58||LA17_1==60) ) {
                    alt17=2;
                }
                else if ( (LA17_1==55||LA17_1==94) ) {
                    alt17=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;
                }
                }
                break;
            case 55:
            case 94:
                {
                alt17=1;
                }
                break;
            case 58:
            case 60:
                {
                alt17=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // InternalCqrsDsl.g:924:3: this_ValueObject_0= ruleValueObject
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractVOAccess().getValueObjectParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_ValueObject_0=ruleValueObject();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_ValueObject_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:933:3: this_AbstractEntityId_1= ruleAbstractEntityId
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractVOAccess().getAbstractEntityIdParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_AbstractEntityId_1=ruleAbstractEntityId();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_AbstractEntityId_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAbstractVO"


    // $ANTLR start "entryRuleAbstractEntityId"
    // InternalCqrsDsl.g:945:1: entryRuleAbstractEntityId returns [EObject current=null] : iv_ruleAbstractEntityId= ruleAbstractEntityId EOF ;
    public final EObject entryRuleAbstractEntityId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractEntityId = null;


        try {
            // InternalCqrsDsl.g:945:57: (iv_ruleAbstractEntityId= ruleAbstractEntityId EOF )
            // InternalCqrsDsl.g:946:2: iv_ruleAbstractEntityId= ruleAbstractEntityId EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAbstractEntityIdRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAbstractEntityId=ruleAbstractEntityId();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAbstractEntityId; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAbstractEntityId"


    // $ANTLR start "ruleAbstractEntityId"
    // InternalCqrsDsl.g:952:1: ruleAbstractEntityId returns [EObject current=null] : (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId ) ;
    public final EObject ruleAbstractEntityId() throws RecognitionException {
        EObject current = null;

        EObject this_EntityId_0 = null;

        EObject this_AggregateId_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:958:2: ( (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId ) )
            // InternalCqrsDsl.g:959:2: (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId )
            {
            // InternalCqrsDsl.g:959:2: (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId )
            int alt18=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA18_1 = input.LA(2);

                if ( (LA18_1==58) ) {
                    alt18=1;
                }
                else if ( (LA18_1==60) ) {
                    alt18=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;
                }
                }
                break;
            case 58:
                {
                alt18=1;
                }
                break;
            case 60:
                {
                alt18=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // InternalCqrsDsl.g:960:3: this_EntityId_0= ruleEntityId
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractEntityIdAccess().getEntityIdParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_EntityId_0=ruleEntityId();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_EntityId_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:969:3: this_AggregateId_1= ruleAggregateId
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractEntityIdAccess().getAggregateIdParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_AggregateId_1=ruleAggregateId();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_AggregateId_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAbstractEntityId"


    // $ANTLR start "entryRuleAbstractEntity"
    // InternalCqrsDsl.g:981:1: entryRuleAbstractEntity returns [EObject current=null] : iv_ruleAbstractEntity= ruleAbstractEntity EOF ;
    public final EObject entryRuleAbstractEntity() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractEntity = null;


        try {
            // InternalCqrsDsl.g:981:55: (iv_ruleAbstractEntity= ruleAbstractEntity EOF )
            // InternalCqrsDsl.g:982:2: iv_ruleAbstractEntity= ruleAbstractEntity EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAbstractEntityRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAbstractEntity=ruleAbstractEntity();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAbstractEntity; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAbstractEntity"


    // $ANTLR start "ruleAbstractEntity"
    // InternalCqrsDsl.g:988:1: ruleAbstractEntity returns [EObject current=null] : (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate ) ;
    public final EObject ruleAbstractEntity() throws RecognitionException {
        EObject current = null;

        EObject this_Entity_0 = null;

        EObject this_Aggregate_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:994:2: ( (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate ) )
            // InternalCqrsDsl.g:995:2: (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate )
            {
            // InternalCqrsDsl.g:995:2: (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate )
            int alt19=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==66) ) {
                    alt19=1;
                }
                else if ( (LA19_1==69) ) {
                    alt19=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
                }
                break;
            case 66:
                {
                alt19=1;
                }
                break;
            case 69:
                {
                alt19=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // InternalCqrsDsl.g:996:3: this_Entity_0= ruleEntity
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractEntityAccess().getEntityParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Entity_0=ruleEntity();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Entity_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:1005:3: this_Aggregate_1= ruleAggregate
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getAbstractEntityAccess().getAggregateParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_Aggregate_1=ruleAggregate();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_Aggregate_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAbstractEntity"


    // $ANTLR start "entryRuleExternalType"
    // InternalCqrsDsl.g:1017:1: entryRuleExternalType returns [EObject current=null] : iv_ruleExternalType= ruleExternalType EOF ;
    public final EObject entryRuleExternalType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExternalType = null;


        try {
            // InternalCqrsDsl.g:1017:53: (iv_ruleExternalType= ruleExternalType EOF )
            // InternalCqrsDsl.g:1018:2: iv_ruleExternalType= ruleExternalType EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getExternalTypeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleExternalType=ruleExternalType();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleExternalType; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExternalType"


    // $ANTLR start "ruleExternalType"
    // InternalCqrsDsl.g:1024:1: ruleExternalType returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? ) ;
    public final EObject ruleExternalType() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_element_2_0=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token lv_generics_5_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:1030:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? ) )
            // InternalCqrsDsl.g:1031:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? )
            {
            // InternalCqrsDsl.g:1031:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? )
            // InternalCqrsDsl.g:1032:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )?
            {
            // InternalCqrsDsl.g:1032:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==RULE_DOC) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCqrsDsl.g:1033:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1033:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1034:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_19); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getExternalTypeAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getExternalTypeRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,21,FOLLOW_20); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getExternalTypeAccess().getTypeKeyword_1());
              		
            }
            // InternalCqrsDsl.g:1054:3: ( (lv_element_2_0= 'element' ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==22) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCqrsDsl.g:1055:4: (lv_element_2_0= 'element' )
                    {
                    // InternalCqrsDsl.g:1055:4: (lv_element_2_0= 'element' )
                    // InternalCqrsDsl.g:1056:5: lv_element_2_0= 'element'
                    {
                    lv_element_2_0=(Token)match(input,22,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_element_2_0, grammarAccess.getExternalTypeAccess().getElementElementKeyword_2_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getExternalTypeRule());
                      					}
                      					setWithLastConsumed(current, "element", lv_element_2_0, "element");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:1068:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:1069:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1069:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:1070:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_21); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_3_0, grammarAccess.getExternalTypeAccess().getNameIDTerminalRuleCall_3_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getExternalTypeRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_3_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:1086:3: (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==23) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCqrsDsl.g:1087:4: otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) )
                    {
                    otherlv_4=(Token)match(input,23,FOLLOW_22); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getExternalTypeAccess().getGenericsKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:1091:4: ( (lv_generics_5_0= RULE_INT ) )
                    // InternalCqrsDsl.g:1092:5: (lv_generics_5_0= RULE_INT )
                    {
                    // InternalCqrsDsl.g:1092:5: (lv_generics_5_0= RULE_INT )
                    // InternalCqrsDsl.g:1093:6: lv_generics_5_0= RULE_INT
                    {
                    lv_generics_5_0=(Token)match(input,RULE_INT,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_generics_5_0, grammarAccess.getExternalTypeAccess().getGenericsINTTerminalRuleCall_4_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getExternalTypeRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"generics",
                      							lv_generics_5_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.INT");
                      					
                    }

                    }


                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExternalType"


    // $ANTLR start "entryRuleDuration"
    // InternalCqrsDsl.g:1114:1: entryRuleDuration returns [EObject current=null] : iv_ruleDuration= ruleDuration EOF ;
    public final EObject entryRuleDuration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDuration = null;


        try {
            // InternalCqrsDsl.g:1114:49: (iv_ruleDuration= ruleDuration EOF )
            // InternalCqrsDsl.g:1115:2: iv_ruleDuration= ruleDuration EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getDurationRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleDuration=ruleDuration();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleDuration; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDuration"


    // $ANTLR start "ruleDuration"
    // InternalCqrsDsl.g:1121:1: ruleDuration returns [EObject current=null] : ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) ) ;
    public final EObject ruleDuration() throws RecognitionException {
        EObject current = null;

        Token lv_time_0_0=null;
        Enumerator lv_unit_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:1127:2: ( ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) ) )
            // InternalCqrsDsl.g:1128:2: ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) )
            {
            // InternalCqrsDsl.g:1128:2: ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) )
            // InternalCqrsDsl.g:1129:3: ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) )
            {
            // InternalCqrsDsl.g:1129:3: ( (lv_time_0_0= RULE_INT ) )
            // InternalCqrsDsl.g:1130:4: (lv_time_0_0= RULE_INT )
            {
            // InternalCqrsDsl.g:1130:4: (lv_time_0_0= RULE_INT )
            // InternalCqrsDsl.g:1131:5: lv_time_0_0= RULE_INT
            {
            lv_time_0_0=(Token)match(input,RULE_INT,FOLLOW_23); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_time_0_0, grammarAccess.getDurationAccess().getTimeINTTerminalRuleCall_0_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getDurationRule());
              					}
              					setWithLastConsumed(
              						current,
              						"time",
              						lv_time_0_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.INT");
              				
            }

            }


            }

            // InternalCqrsDsl.g:1147:3: ( (lv_unit_1_0= ruleTimeUnit ) )
            // InternalCqrsDsl.g:1148:4: (lv_unit_1_0= ruleTimeUnit )
            {
            // InternalCqrsDsl.g:1148:4: (lv_unit_1_0= ruleTimeUnit )
            // InternalCqrsDsl.g:1149:5: lv_unit_1_0= ruleTimeUnit
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getDurationAccess().getUnitTimeUnitEnumRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_unit_1_0=ruleTimeUnit();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getDurationRule());
              					}
              					set(
              						current,
              						"unit",
              						lv_unit_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TimeUnit");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDuration"


    // $ANTLR start "entryRuleWeakConsistency"
    // InternalCqrsDsl.g:1170:1: entryRuleWeakConsistency returns [EObject current=null] : iv_ruleWeakConsistency= ruleWeakConsistency EOF ;
    public final EObject entryRuleWeakConsistency() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWeakConsistency = null;


        try {
            // InternalCqrsDsl.g:1170:56: (iv_ruleWeakConsistency= ruleWeakConsistency EOF )
            // InternalCqrsDsl.g:1171:2: iv_ruleWeakConsistency= ruleWeakConsistency EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getWeakConsistencyRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleWeakConsistency=ruleWeakConsistency();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleWeakConsistency; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleWeakConsistency"


    // $ANTLR start "ruleWeakConsistency"
    // InternalCqrsDsl.g:1177:1: ruleWeakConsistency returns [EObject current=null] : ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) ) ;
    public final EObject ruleWeakConsistency() throws RecognitionException {
        EObject current = null;

        Token lv_acceptableDoc_0_0=null;
        Token otherlv_1=null;
        Token lv_detectionDoc_3_0=null;
        Token otherlv_4=null;
        Token lv_resolutionDoc_6_0=null;
        Token otherlv_7=null;
        EObject lv_acceptable_2_0 = null;

        Enumerator lv_detection_5_0 = null;

        Enumerator lv_resolution_8_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:1183:2: ( ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) ) )
            // InternalCqrsDsl.g:1184:2: ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) )
            {
            // InternalCqrsDsl.g:1184:2: ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) )
            // InternalCqrsDsl.g:1185:3: ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) )
            {
            // InternalCqrsDsl.g:1185:3: ( (lv_acceptableDoc_0_0= RULE_DOC ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_DOC) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCqrsDsl.g:1186:4: (lv_acceptableDoc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1186:4: (lv_acceptableDoc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1187:5: lv_acceptableDoc_0_0= RULE_DOC
                    {
                    lv_acceptableDoc_0_0=(Token)match(input,RULE_DOC,FOLLOW_24); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_acceptableDoc_0_0, grammarAccess.getWeakConsistencyAccess().getAcceptableDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getWeakConsistencyRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"acceptableDoc",
                      						lv_acceptableDoc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,24,FOLLOW_22); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getWeakConsistencyAccess().getAcceptableKeyword_1());
              		
            }
            // InternalCqrsDsl.g:1207:3: ( (lv_acceptable_2_0= ruleDuration ) )
            // InternalCqrsDsl.g:1208:4: (lv_acceptable_2_0= ruleDuration )
            {
            // InternalCqrsDsl.g:1208:4: (lv_acceptable_2_0= ruleDuration )
            // InternalCqrsDsl.g:1209:5: lv_acceptable_2_0= ruleDuration
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getAcceptableDurationParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_25);
            lv_acceptable_2_0=ruleDuration();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getWeakConsistencyRule());
              					}
              					set(
              						current,
              						"acceptable",
              						lv_acceptable_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.Duration");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:1226:3: ( (lv_detectionDoc_3_0= RULE_DOC ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==RULE_DOC) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCqrsDsl.g:1227:4: (lv_detectionDoc_3_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1227:4: (lv_detectionDoc_3_0= RULE_DOC )
                    // InternalCqrsDsl.g:1228:5: lv_detectionDoc_3_0= RULE_DOC
                    {
                    lv_detectionDoc_3_0=(Token)match(input,RULE_DOC,FOLLOW_26); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_detectionDoc_3_0, grammarAccess.getWeakConsistencyAccess().getDetectionDocDOCTerminalRuleCall_3_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getWeakConsistencyRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"detectionDoc",
                      						lv_detectionDoc_3_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,25,FOLLOW_27); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getWeakConsistencyAccess().getDetectionKeyword_4());
              		
            }
            // InternalCqrsDsl.g:1248:3: ( (lv_detection_5_0= ruleInconsistencyDetection ) )
            // InternalCqrsDsl.g:1249:4: (lv_detection_5_0= ruleInconsistencyDetection )
            {
            // InternalCqrsDsl.g:1249:4: (lv_detection_5_0= ruleInconsistencyDetection )
            // InternalCqrsDsl.g:1250:5: lv_detection_5_0= ruleInconsistencyDetection
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getDetectionInconsistencyDetectionEnumRuleCall_5_0());
              				
            }
            pushFollow(FOLLOW_28);
            lv_detection_5_0=ruleInconsistencyDetection();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getWeakConsistencyRule());
              					}
              					set(
              						current,
              						"detection",
              						lv_detection_5_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.InconsistencyDetection");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:1267:3: ( (lv_resolutionDoc_6_0= RULE_DOC ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==RULE_DOC) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalCqrsDsl.g:1268:4: (lv_resolutionDoc_6_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1268:4: (lv_resolutionDoc_6_0= RULE_DOC )
                    // InternalCqrsDsl.g:1269:5: lv_resolutionDoc_6_0= RULE_DOC
                    {
                    lv_resolutionDoc_6_0=(Token)match(input,RULE_DOC,FOLLOW_29); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_resolutionDoc_6_0, grammarAccess.getWeakConsistencyAccess().getResolutionDocDOCTerminalRuleCall_6_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getWeakConsistencyRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"resolutionDoc",
                      						lv_resolutionDoc_6_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_7=(Token)match(input,26,FOLLOW_30); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getWeakConsistencyAccess().getResolutionKeyword_7());
              		
            }
            // InternalCqrsDsl.g:1289:3: ( (lv_resolution_8_0= ruleInconsistencyResolution ) )
            // InternalCqrsDsl.g:1290:4: (lv_resolution_8_0= ruleInconsistencyResolution )
            {
            // InternalCqrsDsl.g:1290:4: (lv_resolution_8_0= ruleInconsistencyResolution )
            // InternalCqrsDsl.g:1291:5: lv_resolution_8_0= ruleInconsistencyResolution
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getResolutionInconsistencyResolutionEnumRuleCall_8_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_resolution_8_0=ruleInconsistencyResolution();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getWeakConsistencyRule());
              					}
              					set(
              						current,
              						"resolution",
              						lv_resolution_8_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.InconsistencyResolution");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleWeakConsistency"


    // $ANTLR start "entryRuleConsistency"
    // InternalCqrsDsl.g:1312:1: entryRuleConsistency returns [EObject current=null] : iv_ruleConsistency= ruleConsistency EOF ;
    public final EObject entryRuleConsistency() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConsistency = null;


        try {
            // InternalCqrsDsl.g:1312:52: (iv_ruleConsistency= ruleConsistency EOF )
            // InternalCqrsDsl.g:1313:2: iv_ruleConsistency= ruleConsistency EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getConsistencyRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleConsistency=ruleConsistency();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleConsistency; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConsistency"


    // $ANTLR start "ruleConsistency"
    // InternalCqrsDsl.g:1319:1: ruleConsistency returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? ) ;
    public final EObject ruleConsistency() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Enumerator lv_level_2_0 = null;

        EObject lv_weakConsistency_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:1325:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? ) )
            // InternalCqrsDsl.g:1326:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? )
            {
            // InternalCqrsDsl.g:1326:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? )
            // InternalCqrsDsl.g:1327:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )?
            {
            // InternalCqrsDsl.g:1327:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==RULE_DOC) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalCqrsDsl.g:1328:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1328:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1329:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_31); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getConsistencyAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getConsistencyRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,27,FOLLOW_32); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getConsistencyAccess().getConsistencyKeyword_1());
              		
            }
            // InternalCqrsDsl.g:1349:3: ( (lv_level_2_0= ruleConsistencyLevel ) )
            // InternalCqrsDsl.g:1350:4: (lv_level_2_0= ruleConsistencyLevel )
            {
            // InternalCqrsDsl.g:1350:4: (lv_level_2_0= ruleConsistencyLevel )
            // InternalCqrsDsl.g:1351:5: lv_level_2_0= ruleConsistencyLevel
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getConsistencyAccess().getLevelConsistencyLevelEnumRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_33);
            lv_level_2_0=ruleConsistencyLevel();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getConsistencyRule());
              					}
              					set(
              						current,
              						"level",
              						lv_level_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ConsistencyLevel");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:1368:3: (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==14) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // InternalCqrsDsl.g:1369:4: otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_34); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getConsistencyAccess().getLeftCurlyBracketKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:1373:4: ( (lv_weakConsistency_4_0= ruleWeakConsistency ) )
                    // InternalCqrsDsl.g:1374:5: (lv_weakConsistency_4_0= ruleWeakConsistency )
                    {
                    // InternalCqrsDsl.g:1374:5: (lv_weakConsistency_4_0= ruleWeakConsistency )
                    // InternalCqrsDsl.g:1375:6: lv_weakConsistency_4_0= ruleWeakConsistency
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getConsistencyAccess().getWeakConsistencyWeakConsistencyParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_35);
                    lv_weakConsistency_4_0=ruleWeakConsistency();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getConsistencyRule());
                      						}
                      						set(
                      							current,
                      							"weakConsistency",
                      							lv_weakConsistency_4_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.WeakConsistency");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    otherlv_5=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getConsistencyAccess().getRightCurlyBracketKeyword_3_2());
                      			
                    }

                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConsistency"


    // $ANTLR start "entryRuleDataProtection"
    // InternalCqrsDsl.g:1401:1: entryRuleDataProtection returns [EObject current=null] : iv_ruleDataProtection= ruleDataProtection EOF ;
    public final EObject entryRuleDataProtection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataProtection = null;


        try {
            // InternalCqrsDsl.g:1401:55: (iv_ruleDataProtection= ruleDataProtection EOF )
            // InternalCqrsDsl.g:1402:2: iv_ruleDataProtection= ruleDataProtection EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getDataProtectionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleDataProtection=ruleDataProtection();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleDataProtection; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDataProtection"


    // $ANTLR start "ruleDataProtection"
    // InternalCqrsDsl.g:1408:1: ruleDataProtection returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' ) ;
    public final EObject ruleDataProtection() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token lv_levelDoc_4_0=null;
        Token otherlv_5=null;
        Token lv_categoryDoc_7_0=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token lv_subjectDoc_12_0=null;
        Token otherlv_13=null;
        Token lv_subject_14_0=null;
        Token lv_purposeDoc_15_0=null;
        Token otherlv_16=null;
        Token lv_purpose_17_0=null;
        Token lv_basisDoc_18_0=null;
        Token otherlv_19=null;
        Token lv_retentionDoc_21_0=null;
        Token otherlv_22=null;
        Token otherlv_24=null;
        Token otherlv_26=null;
        Enumerator lv_level_6_0 = null;

        Enumerator lv_categories_9_0 = null;

        Enumerator lv_categories_11_0 = null;

        Enumerator lv_lawfulBasis_20_0 = null;

        EObject lv_retention_23_0 = null;

        Enumerator lv_erasure_25_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:1414:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' ) )
            // InternalCqrsDsl.g:1415:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' )
            {
            // InternalCqrsDsl.g:1415:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' )
            // InternalCqrsDsl.g:1416:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}'
            {
            // InternalCqrsDsl.g:1416:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==RULE_DOC) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // InternalCqrsDsl.g:1417:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1417:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1418:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_36); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getDataProtectionAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getDataProtectionRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,28,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getDataProtectionAccess().getDataProtectionKeyword_1());
              		
            }
            // InternalCqrsDsl.g:1438:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1439:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1439:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1440:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getDataProtectionAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getDataProtectionRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_37); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getDataProtectionAccess().getLeftCurlyBracketKeyword_3());
              		
            }
            // InternalCqrsDsl.g:1460:3: ( (lv_levelDoc_4_0= RULE_DOC ) )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==RULE_DOC) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // InternalCqrsDsl.g:1461:4: (lv_levelDoc_4_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1461:4: (lv_levelDoc_4_0= RULE_DOC )
                    // InternalCqrsDsl.g:1462:5: lv_levelDoc_4_0= RULE_DOC
                    {
                    lv_levelDoc_4_0=(Token)match(input,RULE_DOC,FOLLOW_38); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_levelDoc_4_0, grammarAccess.getDataProtectionAccess().getLevelDocDOCTerminalRuleCall_4_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getDataProtectionRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"levelDoc",
                      						lv_levelDoc_4_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_5=(Token)match(input,29,FOLLOW_39); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getDataProtectionAccess().getProtectionKeyword_5());
              		
            }
            // InternalCqrsDsl.g:1482:3: ( (lv_level_6_0= ruleProtectionLevel ) )
            // InternalCqrsDsl.g:1483:4: (lv_level_6_0= ruleProtectionLevel )
            {
            // InternalCqrsDsl.g:1483:4: (lv_level_6_0= ruleProtectionLevel )
            // InternalCqrsDsl.g:1484:5: lv_level_6_0= ruleProtectionLevel
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getDataProtectionAccess().getLevelProtectionLevelEnumRuleCall_6_0());
              				
            }
            pushFollow(FOLLOW_40);
            lv_level_6_0=ruleProtectionLevel();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getDataProtectionRule());
              					}
              					set(
              						current,
              						"level",
              						lv_level_6_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ProtectionLevel");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:1501:3: ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==RULE_DOC) ) {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==30) ) {
                    alt32=1;
                }
            }
            else if ( (LA32_0==30) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // InternalCqrsDsl.g:1502:4: ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )*
                    {
                    // InternalCqrsDsl.g:1502:4: ( (lv_categoryDoc_7_0= RULE_DOC ) )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==RULE_DOC) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // InternalCqrsDsl.g:1503:5: (lv_categoryDoc_7_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1503:5: (lv_categoryDoc_7_0= RULE_DOC )
                            // InternalCqrsDsl.g:1504:6: lv_categoryDoc_7_0= RULE_DOC
                            {
                            lv_categoryDoc_7_0=(Token)match(input,RULE_DOC,FOLLOW_41); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						newLeafNode(lv_categoryDoc_7_0, grammarAccess.getDataProtectionAccess().getCategoryDocDOCTerminalRuleCall_7_0_0());
                              					
                            }
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElement(grammarAccess.getDataProtectionRule());
                              						}
                              						setWithLastConsumed(
                              							current,
                              							"categoryDoc",
                              							lv_categoryDoc_7_0,
                              							"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                              					
                            }

                            }


                            }
                            break;

                    }

                    otherlv_8=(Token)match(input,30,FOLLOW_42); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getDataProtectionAccess().getCategoryKeyword_7_1());
                      			
                    }
                    // InternalCqrsDsl.g:1524:4: ( (lv_categories_9_0= ruleSpecialCategory ) )
                    // InternalCqrsDsl.g:1525:5: (lv_categories_9_0= ruleSpecialCategory )
                    {
                    // InternalCqrsDsl.g:1525:5: (lv_categories_9_0= ruleSpecialCategory )
                    // InternalCqrsDsl.g:1526:6: lv_categories_9_0= ruleSpecialCategory
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getDataProtectionAccess().getCategoriesSpecialCategoryEnumRuleCall_7_2_0());
                      					
                    }
                    pushFollow(FOLLOW_43);
                    lv_categories_9_0=ruleSpecialCategory();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getDataProtectionRule());
                      						}
                      						add(
                      							current,
                      							"categories",
                      							lv_categories_9_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.SpecialCategory");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:1543:4: (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )*
                    loop31:
                    do {
                        int alt31=2;
                        int LA31_0 = input.LA(1);

                        if ( (LA31_0==31) ) {
                            alt31=1;
                        }


                        switch (alt31) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:1544:5: otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) )
                    	    {
                    	    otherlv_10=(Token)match(input,31,FOLLOW_42); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_10, grammarAccess.getDataProtectionAccess().getCommaKeyword_7_3_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:1548:5: ( (lv_categories_11_0= ruleSpecialCategory ) )
                    	    // InternalCqrsDsl.g:1549:6: (lv_categories_11_0= ruleSpecialCategory )
                    	    {
                    	    // InternalCqrsDsl.g:1549:6: (lv_categories_11_0= ruleSpecialCategory )
                    	    // InternalCqrsDsl.g:1550:7: lv_categories_11_0= ruleSpecialCategory
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getDataProtectionAccess().getCategoriesSpecialCategoryEnumRuleCall_7_3_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_43);
                    	    lv_categories_11_0=ruleSpecialCategory();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getDataProtectionRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"categories",
                    	      								lv_categories_11_0,
                    	      								"org.fuin.dsl.cqrs.CqrsDsl.SpecialCategory");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop31;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:1569:3: ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==RULE_DOC) ) {
                int LA34_1 = input.LA(2);

                if ( (LA34_1==32) ) {
                    alt34=1;
                }
            }
            else if ( (LA34_0==32) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // InternalCqrsDsl.g:1570:4: ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) )
                    {
                    // InternalCqrsDsl.g:1570:4: ( (lv_subjectDoc_12_0= RULE_DOC ) )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==RULE_DOC) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // InternalCqrsDsl.g:1571:5: (lv_subjectDoc_12_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1571:5: (lv_subjectDoc_12_0= RULE_DOC )
                            // InternalCqrsDsl.g:1572:6: lv_subjectDoc_12_0= RULE_DOC
                            {
                            lv_subjectDoc_12_0=(Token)match(input,RULE_DOC,FOLLOW_44); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						newLeafNode(lv_subjectDoc_12_0, grammarAccess.getDataProtectionAccess().getSubjectDocDOCTerminalRuleCall_8_0_0());
                              					
                            }
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElement(grammarAccess.getDataProtectionRule());
                              						}
                              						setWithLastConsumed(
                              							current,
                              							"subjectDoc",
                              							lv_subjectDoc_12_0,
                              							"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                              					
                            }

                            }


                            }
                            break;

                    }

                    otherlv_13=(Token)match(input,32,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_13, grammarAccess.getDataProtectionAccess().getSubjectKeyword_8_1());
                      			
                    }
                    // InternalCqrsDsl.g:1592:4: ( (lv_subject_14_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1593:5: (lv_subject_14_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1593:5: (lv_subject_14_0= RULE_STRING )
                    // InternalCqrsDsl.g:1594:6: lv_subject_14_0= RULE_STRING
                    {
                    lv_subject_14_0=(Token)match(input,RULE_STRING,FOLLOW_45); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_subject_14_0, grammarAccess.getDataProtectionAccess().getSubjectSTRINGTerminalRuleCall_8_2_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getDataProtectionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"subject",
                      							lv_subject_14_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:1611:3: ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==RULE_DOC) ) {
                int LA36_1 = input.LA(2);

                if ( (LA36_1==33) ) {
                    alt36=1;
                }
            }
            else if ( (LA36_0==33) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // InternalCqrsDsl.g:1612:4: ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) )
                    {
                    // InternalCqrsDsl.g:1612:4: ( (lv_purposeDoc_15_0= RULE_DOC ) )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==RULE_DOC) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // InternalCqrsDsl.g:1613:5: (lv_purposeDoc_15_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1613:5: (lv_purposeDoc_15_0= RULE_DOC )
                            // InternalCqrsDsl.g:1614:6: lv_purposeDoc_15_0= RULE_DOC
                            {
                            lv_purposeDoc_15_0=(Token)match(input,RULE_DOC,FOLLOW_46); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						newLeafNode(lv_purposeDoc_15_0, grammarAccess.getDataProtectionAccess().getPurposeDocDOCTerminalRuleCall_9_0_0());
                              					
                            }
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElement(grammarAccess.getDataProtectionRule());
                              						}
                              						setWithLastConsumed(
                              							current,
                              							"purposeDoc",
                              							lv_purposeDoc_15_0,
                              							"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                              					
                            }

                            }


                            }
                            break;

                    }

                    otherlv_16=(Token)match(input,33,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_16, grammarAccess.getDataProtectionAccess().getPurposeKeyword_9_1());
                      			
                    }
                    // InternalCqrsDsl.g:1634:4: ( (lv_purpose_17_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1635:5: (lv_purpose_17_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1635:5: (lv_purpose_17_0= RULE_STRING )
                    // InternalCqrsDsl.g:1636:6: lv_purpose_17_0= RULE_STRING
                    {
                    lv_purpose_17_0=(Token)match(input,RULE_STRING,FOLLOW_47); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_purpose_17_0, grammarAccess.getDataProtectionAccess().getPurposeSTRINGTerminalRuleCall_9_2_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getDataProtectionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"purpose",
                      							lv_purpose_17_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:1653:3: ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==RULE_DOC) ) {
                int LA38_1 = input.LA(2);

                if ( (LA38_1==34) ) {
                    alt38=1;
                }
            }
            else if ( (LA38_0==34) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // InternalCqrsDsl.g:1654:4: ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) )
                    {
                    // InternalCqrsDsl.g:1654:4: ( (lv_basisDoc_18_0= RULE_DOC ) )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==RULE_DOC) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // InternalCqrsDsl.g:1655:5: (lv_basisDoc_18_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1655:5: (lv_basisDoc_18_0= RULE_DOC )
                            // InternalCqrsDsl.g:1656:6: lv_basisDoc_18_0= RULE_DOC
                            {
                            lv_basisDoc_18_0=(Token)match(input,RULE_DOC,FOLLOW_48); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						newLeafNode(lv_basisDoc_18_0, grammarAccess.getDataProtectionAccess().getBasisDocDOCTerminalRuleCall_10_0_0());
                              					
                            }
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElement(grammarAccess.getDataProtectionRule());
                              						}
                              						setWithLastConsumed(
                              							current,
                              							"basisDoc",
                              							lv_basisDoc_18_0,
                              							"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                              					
                            }

                            }


                            }
                            break;

                    }

                    otherlv_19=(Token)match(input,34,FOLLOW_49); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_19, grammarAccess.getDataProtectionAccess().getLawfulBasisKeyword_10_1());
                      			
                    }
                    // InternalCqrsDsl.g:1676:4: ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) )
                    // InternalCqrsDsl.g:1677:5: (lv_lawfulBasis_20_0= ruleLawfulBasis )
                    {
                    // InternalCqrsDsl.g:1677:5: (lv_lawfulBasis_20_0= ruleLawfulBasis )
                    // InternalCqrsDsl.g:1678:6: lv_lawfulBasis_20_0= ruleLawfulBasis
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getDataProtectionAccess().getLawfulBasisLawfulBasisEnumRuleCall_10_2_0());
                      					
                    }
                    pushFollow(FOLLOW_50);
                    lv_lawfulBasis_20_0=ruleLawfulBasis();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getDataProtectionRule());
                      						}
                      						set(
                      							current,
                      							"lawfulBasis",
                      							lv_lawfulBasis_20_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.LawfulBasis");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:1696:3: ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==RULE_DOC||LA41_0==35) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // InternalCqrsDsl.g:1697:4: ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )?
                    {
                    // InternalCqrsDsl.g:1697:4: ( (lv_retentionDoc_21_0= RULE_DOC ) )?
                    int alt39=2;
                    int LA39_0 = input.LA(1);

                    if ( (LA39_0==RULE_DOC) ) {
                        alt39=1;
                    }
                    switch (alt39) {
                        case 1 :
                            // InternalCqrsDsl.g:1698:5: (lv_retentionDoc_21_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1698:5: (lv_retentionDoc_21_0= RULE_DOC )
                            // InternalCqrsDsl.g:1699:6: lv_retentionDoc_21_0= RULE_DOC
                            {
                            lv_retentionDoc_21_0=(Token)match(input,RULE_DOC,FOLLOW_51); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              						newLeafNode(lv_retentionDoc_21_0, grammarAccess.getDataProtectionAccess().getRetentionDocDOCTerminalRuleCall_11_0_0());
                              					
                            }
                            if ( state.backtracking==0 ) {

                              						if (current==null) {
                              							current = createModelElement(grammarAccess.getDataProtectionRule());
                              						}
                              						setWithLastConsumed(
                              							current,
                              							"retentionDoc",
                              							lv_retentionDoc_21_0,
                              							"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                              					
                            }

                            }


                            }
                            break;

                    }

                    otherlv_22=(Token)match(input,35,FOLLOW_22); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_22, grammarAccess.getDataProtectionAccess().getRetentionKeyword_11_1());
                      			
                    }
                    // InternalCqrsDsl.g:1719:4: ( (lv_retention_23_0= ruleDuration ) )
                    // InternalCqrsDsl.g:1720:5: (lv_retention_23_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:1720:5: (lv_retention_23_0= ruleDuration )
                    // InternalCqrsDsl.g:1721:6: lv_retention_23_0= ruleDuration
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getDataProtectionAccess().getRetentionDurationParserRuleCall_11_2_0());
                      					
                    }
                    pushFollow(FOLLOW_52);
                    lv_retention_23_0=ruleDuration();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getDataProtectionRule());
                      						}
                      						set(
                      							current,
                      							"retention",
                      							lv_retention_23_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.Duration");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:1738:4: (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )?
                    int alt40=2;
                    int LA40_0 = input.LA(1);

                    if ( (LA40_0==36) ) {
                        alt40=1;
                    }
                    switch (alt40) {
                        case 1 :
                            // InternalCqrsDsl.g:1739:5: otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) )
                            {
                            otherlv_24=(Token)match(input,36,FOLLOW_53); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					newLeafNode(otherlv_24, grammarAccess.getDataProtectionAccess().getThenKeyword_11_3_0());
                              				
                            }
                            // InternalCqrsDsl.g:1743:5: ( (lv_erasure_25_0= ruleErasureStrategy ) )
                            // InternalCqrsDsl.g:1744:6: (lv_erasure_25_0= ruleErasureStrategy )
                            {
                            // InternalCqrsDsl.g:1744:6: (lv_erasure_25_0= ruleErasureStrategy )
                            // InternalCqrsDsl.g:1745:7: lv_erasure_25_0= ruleErasureStrategy
                            {
                            if ( state.backtracking==0 ) {

                              							newCompositeNode(grammarAccess.getDataProtectionAccess().getErasureErasureStrategyEnumRuleCall_11_3_1_0());
                              						
                            }
                            pushFollow(FOLLOW_35);
                            lv_erasure_25_0=ruleErasureStrategy();

                            state._fsp--;
                            if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              							if (current==null) {
                              								current = createModelElementForParent(grammarAccess.getDataProtectionRule());
                              							}
                              							set(
                              								current,
                              								"erasure",
                              								lv_erasure_25_0,
                              								"org.fuin.dsl.cqrs.CqrsDsl.ErasureStrategy");
                              							afterParserOrEnumRuleCall();
                              						
                            }

                            }


                            }


                            }
                            break;

                    }


                    }
                    break;

            }

            otherlv_26=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_26, grammarAccess.getDataProtectionAccess().getRightCurlyBracketKeyword_12());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDataProtection"


    // $ANTLR start "entryRuleDataProtectionInstance"
    // InternalCqrsDsl.g:1772:1: entryRuleDataProtectionInstance returns [EObject current=null] : iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF ;
    public final EObject entryRuleDataProtectionInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataProtectionInstance = null;


        try {
            // InternalCqrsDsl.g:1772:63: (iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF )
            // InternalCqrsDsl.g:1773:2: iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getDataProtectionInstanceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleDataProtectionInstance=ruleDataProtectionInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleDataProtectionInstance; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDataProtectionInstance"


    // $ANTLR start "ruleDataProtectionInstance"
    // InternalCqrsDsl.g:1779:1: ruleDataProtectionInstance returns [EObject current=null] : (otherlv_0= 'protected-by' ( ( ruleFQN ) ) ) ;
    public final EObject ruleDataProtectionInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:1785:2: ( (otherlv_0= 'protected-by' ( ( ruleFQN ) ) ) )
            // InternalCqrsDsl.g:1786:2: (otherlv_0= 'protected-by' ( ( ruleFQN ) ) )
            {
            // InternalCqrsDsl.g:1786:2: (otherlv_0= 'protected-by' ( ( ruleFQN ) ) )
            // InternalCqrsDsl.g:1787:3: otherlv_0= 'protected-by' ( ( ruleFQN ) )
            {
            otherlv_0=(Token)match(input,37,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getDataProtectionInstanceAccess().getProtectedByKeyword_0());
              		
            }
            // InternalCqrsDsl.g:1791:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:1792:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:1792:4: ( ruleFQN )
            // InternalCqrsDsl.g:1793:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getDataProtectionInstanceRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getDataProtectionInstanceAccess().getPolicyDataProtectionCrossReference_1_0());
              				
            }
            pushFollow(FOLLOW_2);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDataProtectionInstance"


    // $ANTLR start "entryRuleConstraint"
    // InternalCqrsDsl.g:1811:1: entryRuleConstraint returns [EObject current=null] : iv_ruleConstraint= ruleConstraint EOF ;
    public final EObject entryRuleConstraint() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstraint = null;


        try {
            // InternalCqrsDsl.g:1811:51: (iv_ruleConstraint= ruleConstraint EOF )
            // InternalCqrsDsl.g:1812:2: iv_ruleConstraint= ruleConstraint EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getConstraintRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleConstraint=ruleConstraint();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleConstraint; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConstraint"


    // $ANTLR start "ruleConstraint"
    // InternalCqrsDsl.g:1818:1: ruleConstraint returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) ;
    public final EObject ruleConstraint() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token lv_message_12_0=null;
        Token otherlv_13=null;
        EObject lv_attributes_10_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:1824:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) )
            // InternalCqrsDsl.g:1825:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:1825:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            // InternalCqrsDsl.g:1826:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}'
            {
            // InternalCqrsDsl.g:1826:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==RULE_DOC) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCqrsDsl.g:1827:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1827:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1828:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_54); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getConstraintAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getConstraintRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,38,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getConstraintAccess().getConstraintKeyword_1());
              		
            }
            // InternalCqrsDsl.g:1848:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1849:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1849:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1850:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_55); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getConstraintAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getConstraintRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:1866:3: (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==39) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // InternalCqrsDsl.g:1867:4: otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )*
                    {
                    otherlv_3=(Token)match(input,39,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getConstraintAccess().getInputKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:1871:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:1872:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:1872:5: ( ruleFQN )
                    // InternalCqrsDsl.g:1873:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getConstraintRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getConstraintAccess().getInputTypeCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_56);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:1887:4: (otherlv_5= '|' ( ( ruleFQN ) ) )*
                    loop43:
                    do {
                        int alt43=2;
                        int LA43_0 = input.LA(1);

                        if ( (LA43_0==40) ) {
                            alt43=1;
                        }


                        switch (alt43) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:1888:5: otherlv_5= '|' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_5=(Token)match(input,40,FOLLOW_4); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_5, grammarAccess.getConstraintAccess().getVerticalLineKeyword_3_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:1892:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:1893:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:1893:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:1894:7: ruleFQN
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElement(grammarAccess.getConstraintRule());
                    	      							}
                    	      						
                    	    }
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getConstraintAccess().getInputTypeCrossReference_3_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_56);
                    	    ruleFQN();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop43;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:1910:3: (otherlv_7= 'exception' ( ( ruleFQN ) ) )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==41) ) {
                alt45=1;
            }
            switch (alt45) {
                case 1 :
                    // InternalCqrsDsl.g:1911:4: otherlv_7= 'exception' ( ( ruleFQN ) )
                    {
                    otherlv_7=(Token)match(input,41,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getConstraintAccess().getExceptionKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:1915:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:1916:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:1916:5: ( ruleFQN )
                    // InternalCqrsDsl.g:1917:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getConstraintRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getConstraintAccess().getExceptionExceptionCrossReference_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_5);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_57); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getConstraintAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalCqrsDsl.g:1936:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( ((LA46_0>=RULE_DOC && LA46_0<=RULE_ID)||LA46_0==79) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCqrsDsl.g:1937:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:1937:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:1938:5: lv_attributes_10_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getConstraintAccess().getAttributesAttributeParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_57);
            	    lv_attributes_10_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getConstraintRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_10_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop46;
                }
            } while (true);

            // InternalCqrsDsl.g:1955:3: (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==42) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // InternalCqrsDsl.g:1956:4: otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) )
                    {
                    otherlv_11=(Token)match(input,42,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getConstraintAccess().getMessageKeyword_7_0());
                      			
                    }
                    // InternalCqrsDsl.g:1960:4: ( (lv_message_12_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1961:5: (lv_message_12_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1961:5: (lv_message_12_0= RULE_STRING )
                    // InternalCqrsDsl.g:1962:6: lv_message_12_0= RULE_STRING
                    {
                    lv_message_12_0=(Token)match(input,RULE_STRING,FOLLOW_35); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_message_12_0, grammarAccess.getConstraintAccess().getMessageSTRINGTerminalRuleCall_7_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getConstraintRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"message",
                      							lv_message_12_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_13=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_13, grammarAccess.getConstraintAccess().getRightCurlyBracketKeyword_8());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConstraint"


    // $ANTLR start "entryRuleBusinessRule"
    // InternalCqrsDsl.g:1987:1: entryRuleBusinessRule returns [EObject current=null] : iv_ruleBusinessRule= ruleBusinessRule EOF ;
    public final EObject entryRuleBusinessRule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRule = null;


        try {
            // InternalCqrsDsl.g:1987:53: (iv_ruleBusinessRule= ruleBusinessRule EOF )
            // InternalCqrsDsl.g:1988:2: iv_ruleBusinessRule= ruleBusinessRule EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getBusinessRuleRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleBusinessRule=ruleBusinessRule();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleBusinessRule; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBusinessRule"


    // $ANTLR start "ruleBusinessRule"
    // InternalCqrsDsl.g:1994:1: ruleBusinessRule returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* ( (lv_consistency_7_0= ruleConsistency ) ) (otherlv_8= 'requires' ( (lv_requires_9_0= ruleRuleExpr ) ) )? otherlv_10= '}' ) ;
    public final EObject ruleBusinessRule() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        EObject lv_attributes_6_0 = null;

        EObject lv_consistency_7_0 = null;

        EObject lv_requires_9_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2000:2: ( ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* ( (lv_consistency_7_0= ruleConsistency ) ) (otherlv_8= 'requires' ( (lv_requires_9_0= ruleRuleExpr ) ) )? otherlv_10= '}' ) )
            // InternalCqrsDsl.g:2001:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* ( (lv_consistency_7_0= ruleConsistency ) ) (otherlv_8= 'requires' ( (lv_requires_9_0= ruleRuleExpr ) ) )? otherlv_10= '}' )
            {
            // InternalCqrsDsl.g:2001:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* ( (lv_consistency_7_0= ruleConsistency ) ) (otherlv_8= 'requires' ( (lv_requires_9_0= ruleRuleExpr ) ) )? otherlv_10= '}' )
            // InternalCqrsDsl.g:2002:3: ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* ( (lv_consistency_7_0= ruleConsistency ) ) (otherlv_8= 'requires' ( (lv_requires_9_0= ruleRuleExpr ) ) )? otherlv_10= '}'
            {
            // InternalCqrsDsl.g:2002:3: ( (lv_doc_0_0= RULE_DOC ) )
            // InternalCqrsDsl.g:2003:4: (lv_doc_0_0= RULE_DOC )
            {
            // InternalCqrsDsl.g:2003:4: (lv_doc_0_0= RULE_DOC )
            // InternalCqrsDsl.g:2004:5: lv_doc_0_0= RULE_DOC
            {
            lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_58); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_doc_0_0, grammarAccess.getBusinessRuleAccess().getDocDOCTerminalRuleCall_0_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getBusinessRuleRule());
              					}
              					setWithLastConsumed(
              						current,
              						"doc",
              						lv_doc_0_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
              				
            }

            }


            }

            otherlv_1=(Token)match(input,43,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getBusinessRuleAccess().getBusinessRuleKeyword_1());
              		
            }
            // InternalCqrsDsl.g:2024:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2025:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2025:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2026:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_59); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getBusinessRuleAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getBusinessRuleRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            otherlv_3=(Token)match(input,41,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getBusinessRuleAccess().getExceptionKeyword_3());
              		
            }
            // InternalCqrsDsl.g:2046:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:2047:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:2047:4: ( ruleFQN )
            // InternalCqrsDsl.g:2048:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getBusinessRuleRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getBusinessRuleAccess().getExceptionExceptionCrossReference_4_0());
              				
            }
            pushFollow(FOLLOW_5);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            otherlv_5=(Token)match(input,14,FOLLOW_60); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getBusinessRuleAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalCqrsDsl.g:2066:3: ( (lv_attributes_6_0= ruleAttribute ) )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==RULE_DOC) ) {
                    int LA48_1 = input.LA(2);

                    if ( (LA48_1==RULE_ID||LA48_1==79) ) {
                        alt48=1;
                    }


                }
                else if ( (LA48_0==RULE_ID||LA48_0==79) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // InternalCqrsDsl.g:2067:4: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2067:4: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2068:5: lv_attributes_6_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getBusinessRuleAccess().getAttributesAttributeParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_60);
            	    lv_attributes_6_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getBusinessRuleRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_6_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);

            // InternalCqrsDsl.g:2085:3: ( (lv_consistency_7_0= ruleConsistency ) )
            // InternalCqrsDsl.g:2086:4: (lv_consistency_7_0= ruleConsistency )
            {
            // InternalCqrsDsl.g:2086:4: (lv_consistency_7_0= ruleConsistency )
            // InternalCqrsDsl.g:2087:5: lv_consistency_7_0= ruleConsistency
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getBusinessRuleAccess().getConsistencyConsistencyParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_61);
            lv_consistency_7_0=ruleConsistency();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getBusinessRuleRule());
              					}
              					set(
              						current,
              						"consistency",
              						lv_consistency_7_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.Consistency");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:2104:3: (otherlv_8= 'requires' ( (lv_requires_9_0= ruleRuleExpr ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==44) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCqrsDsl.g:2105:4: otherlv_8= 'requires' ( (lv_requires_9_0= ruleRuleExpr ) )
                    {
                    otherlv_8=(Token)match(input,44,FOLLOW_62); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getBusinessRuleAccess().getRequiresKeyword_8_0());
                      			
                    }
                    // InternalCqrsDsl.g:2109:4: ( (lv_requires_9_0= ruleRuleExpr ) )
                    // InternalCqrsDsl.g:2110:5: (lv_requires_9_0= ruleRuleExpr )
                    {
                    // InternalCqrsDsl.g:2110:5: (lv_requires_9_0= ruleRuleExpr )
                    // InternalCqrsDsl.g:2111:6: lv_requires_9_0= ruleRuleExpr
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getBusinessRuleAccess().getRequiresRuleExprParserRuleCall_8_1_0());
                      					
                    }
                    pushFollow(FOLLOW_35);
                    lv_requires_9_0=ruleRuleExpr();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getBusinessRuleRule());
                      						}
                      						set(
                      							current,
                      							"requires",
                      							lv_requires_9_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.RuleExpr");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_10=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_10, grammarAccess.getBusinessRuleAccess().getRightCurlyBracketKeyword_9());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBusinessRule"


    // $ANTLR start "entryRuleRuleExpr"
    // InternalCqrsDsl.g:2137:1: entryRuleRuleExpr returns [EObject current=null] : iv_ruleRuleExpr= ruleRuleExpr EOF ;
    public final EObject entryRuleRuleExpr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleExpr = null;


        try {
            // InternalCqrsDsl.g:2137:49: (iv_ruleRuleExpr= ruleRuleExpr EOF )
            // InternalCqrsDsl.g:2138:2: iv_ruleRuleExpr= ruleRuleExpr EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getRuleExprRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleRuleExpr=ruleRuleExpr();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleRuleExpr; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleExpr"


    // $ANTLR start "ruleRuleExpr"
    // InternalCqrsDsl.g:2144:1: ruleRuleExpr returns [EObject current=null] : this_RuleOr_0= ruleRuleOr ;
    public final EObject ruleRuleExpr() throws RecognitionException {
        EObject current = null;

        EObject this_RuleOr_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2150:2: (this_RuleOr_0= ruleRuleOr )
            // InternalCqrsDsl.g:2151:2: this_RuleOr_0= ruleRuleOr
            {
            if ( state.backtracking==0 ) {

              		newCompositeNode(grammarAccess.getRuleExprAccess().getRuleOrParserRuleCall());
              	
            }
            pushFollow(FOLLOW_2);
            this_RuleOr_0=ruleRuleOr();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              		current = this_RuleOr_0;
              		afterParserOrEnumRuleCall();
              	
            }

            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleExpr"


    // $ANTLR start "entryRuleRuleOr"
    // InternalCqrsDsl.g:2162:1: entryRuleRuleOr returns [EObject current=null] : iv_ruleRuleOr= ruleRuleOr EOF ;
    public final EObject entryRuleRuleOr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleOr = null;


        try {
            // InternalCqrsDsl.g:2162:47: (iv_ruleRuleOr= ruleRuleOr EOF )
            // InternalCqrsDsl.g:2163:2: iv_ruleRuleOr= ruleRuleOr EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getRuleOrRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleRuleOr=ruleRuleOr();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleRuleOr; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleOr"


    // $ANTLR start "ruleRuleOr"
    // InternalCqrsDsl.g:2169:1: ruleRuleOr returns [EObject current=null] : (this_RuleAnd_0= ruleRuleAnd ( () otherlv_2= '||' ( (lv_right_3_0= ruleRuleAnd ) ) )* ) ;
    public final EObject ruleRuleOr() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_RuleAnd_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2175:2: ( (this_RuleAnd_0= ruleRuleAnd ( () otherlv_2= '||' ( (lv_right_3_0= ruleRuleAnd ) ) )* ) )
            // InternalCqrsDsl.g:2176:2: (this_RuleAnd_0= ruleRuleAnd ( () otherlv_2= '||' ( (lv_right_3_0= ruleRuleAnd ) ) )* )
            {
            // InternalCqrsDsl.g:2176:2: (this_RuleAnd_0= ruleRuleAnd ( () otherlv_2= '||' ( (lv_right_3_0= ruleRuleAnd ) ) )* )
            // InternalCqrsDsl.g:2177:3: this_RuleAnd_0= ruleRuleAnd ( () otherlv_2= '||' ( (lv_right_3_0= ruleRuleAnd ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getRuleOrAccess().getRuleAndParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_63);
            this_RuleAnd_0=ruleRuleAnd();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_RuleAnd_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalCqrsDsl.g:2185:3: ( () otherlv_2= '||' ( (lv_right_3_0= ruleRuleAnd ) ) )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( (LA50_0==45) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // InternalCqrsDsl.g:2186:4: () otherlv_2= '||' ( (lv_right_3_0= ruleRuleAnd ) )
            	    {
            	    // InternalCqrsDsl.g:2186:4: ()
            	    // InternalCqrsDsl.g:2187:5: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      					current = forceCreateModelElementAndSet(
            	      						grammarAccess.getRuleOrAccess().getRuleOrLeftAction_1_0(),
            	      						current);
            	      				
            	    }

            	    }

            	    otherlv_2=(Token)match(input,45,FOLLOW_62); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_2, grammarAccess.getRuleOrAccess().getVerticalLineVerticalLineKeyword_1_1());
            	      			
            	    }
            	    // InternalCqrsDsl.g:2197:4: ( (lv_right_3_0= ruleRuleAnd ) )
            	    // InternalCqrsDsl.g:2198:5: (lv_right_3_0= ruleRuleAnd )
            	    {
            	    // InternalCqrsDsl.g:2198:5: (lv_right_3_0= ruleRuleAnd )
            	    // InternalCqrsDsl.g:2199:6: lv_right_3_0= ruleRuleAnd
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getRuleOrAccess().getRightRuleAndParserRuleCall_1_2_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_63);
            	    lv_right_3_0=ruleRuleAnd();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getRuleOrRule());
            	      						}
            	      						set(
            	      							current,
            	      							"right",
            	      							lv_right_3_0,
            	      							"org.fuin.dsl.cqrs.CqrsDsl.RuleAnd");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop50;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleOr"


    // $ANTLR start "entryRuleRuleAnd"
    // InternalCqrsDsl.g:2221:1: entryRuleRuleAnd returns [EObject current=null] : iv_ruleRuleAnd= ruleRuleAnd EOF ;
    public final EObject entryRuleRuleAnd() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleAnd = null;


        try {
            // InternalCqrsDsl.g:2221:48: (iv_ruleRuleAnd= ruleRuleAnd EOF )
            // InternalCqrsDsl.g:2222:2: iv_ruleRuleAnd= ruleRuleAnd EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getRuleAndRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleRuleAnd=ruleRuleAnd();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleRuleAnd; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleAnd"


    // $ANTLR start "ruleRuleAnd"
    // InternalCqrsDsl.g:2228:1: ruleRuleAnd returns [EObject current=null] : (this_RuleUnary_0= ruleRuleUnary ( () otherlv_2= '&&' ( (lv_right_3_0= ruleRuleUnary ) ) )* ) ;
    public final EObject ruleRuleAnd() throws RecognitionException {
        EObject current = null;

        Token otherlv_2=null;
        EObject this_RuleUnary_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2234:2: ( (this_RuleUnary_0= ruleRuleUnary ( () otherlv_2= '&&' ( (lv_right_3_0= ruleRuleUnary ) ) )* ) )
            // InternalCqrsDsl.g:2235:2: (this_RuleUnary_0= ruleRuleUnary ( () otherlv_2= '&&' ( (lv_right_3_0= ruleRuleUnary ) ) )* )
            {
            // InternalCqrsDsl.g:2235:2: (this_RuleUnary_0= ruleRuleUnary ( () otherlv_2= '&&' ( (lv_right_3_0= ruleRuleUnary ) ) )* )
            // InternalCqrsDsl.g:2236:3: this_RuleUnary_0= ruleRuleUnary ( () otherlv_2= '&&' ( (lv_right_3_0= ruleRuleUnary ) ) )*
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getRuleAndAccess().getRuleUnaryParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_64);
            this_RuleUnary_0=ruleRuleUnary();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current = this_RuleUnary_0;
              			afterParserOrEnumRuleCall();
              		
            }
            // InternalCqrsDsl.g:2244:3: ( () otherlv_2= '&&' ( (lv_right_3_0= ruleRuleUnary ) ) )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==46) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // InternalCqrsDsl.g:2245:4: () otherlv_2= '&&' ( (lv_right_3_0= ruleRuleUnary ) )
            	    {
            	    // InternalCqrsDsl.g:2245:4: ()
            	    // InternalCqrsDsl.g:2246:5: 
            	    {
            	    if ( state.backtracking==0 ) {

            	      					current = forceCreateModelElementAndSet(
            	      						grammarAccess.getRuleAndAccess().getRuleAndLeftAction_1_0(),
            	      						current);
            	      				
            	    }

            	    }

            	    otherlv_2=(Token)match(input,46,FOLLOW_62); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_2, grammarAccess.getRuleAndAccess().getAmpersandAmpersandKeyword_1_1());
            	      			
            	    }
            	    // InternalCqrsDsl.g:2256:4: ( (lv_right_3_0= ruleRuleUnary ) )
            	    // InternalCqrsDsl.g:2257:5: (lv_right_3_0= ruleRuleUnary )
            	    {
            	    // InternalCqrsDsl.g:2257:5: (lv_right_3_0= ruleRuleUnary )
            	    // InternalCqrsDsl.g:2258:6: lv_right_3_0= ruleRuleUnary
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getRuleAndAccess().getRightRuleUnaryParserRuleCall_1_2_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_64);
            	    lv_right_3_0=ruleRuleUnary();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getRuleAndRule());
            	      						}
            	      						set(
            	      							current,
            	      							"right",
            	      							lv_right_3_0,
            	      							"org.fuin.dsl.cqrs.CqrsDsl.RuleUnary");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleAnd"


    // $ANTLR start "entryRuleRuleUnary"
    // InternalCqrsDsl.g:2280:1: entryRuleRuleUnary returns [EObject current=null] : iv_ruleRuleUnary= ruleRuleUnary EOF ;
    public final EObject entryRuleRuleUnary() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleUnary = null;


        try {
            // InternalCqrsDsl.g:2280:50: (iv_ruleRuleUnary= ruleRuleUnary EOF )
            // InternalCqrsDsl.g:2281:2: iv_ruleRuleUnary= ruleRuleUnary EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getRuleUnaryRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleRuleUnary=ruleRuleUnary();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleRuleUnary; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleUnary"


    // $ANTLR start "ruleRuleUnary"
    // InternalCqrsDsl.g:2287:1: ruleRuleUnary returns [EObject current=null] : ( ( () otherlv_1= '!' ( (lv_expr_2_0= ruleRuleUnary ) ) ) | (otherlv_3= '(' this_RuleOr_4= ruleRuleOr otherlv_5= ')' ) | this_RuleAtom_6= ruleRuleAtom ) ;
    public final EObject ruleRuleUnary() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_expr_2_0 = null;

        EObject this_RuleOr_4 = null;

        EObject this_RuleAtom_6 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2293:2: ( ( ( () otherlv_1= '!' ( (lv_expr_2_0= ruleRuleUnary ) ) ) | (otherlv_3= '(' this_RuleOr_4= ruleRuleOr otherlv_5= ')' ) | this_RuleAtom_6= ruleRuleAtom ) )
            // InternalCqrsDsl.g:2294:2: ( ( () otherlv_1= '!' ( (lv_expr_2_0= ruleRuleUnary ) ) ) | (otherlv_3= '(' this_RuleOr_4= ruleRuleOr otherlv_5= ')' ) | this_RuleAtom_6= ruleRuleAtom )
            {
            // InternalCqrsDsl.g:2294:2: ( ( () otherlv_1= '!' ( (lv_expr_2_0= ruleRuleUnary ) ) ) | (otherlv_3= '(' this_RuleOr_4= ruleRuleOr otherlv_5= ')' ) | this_RuleAtom_6= ruleRuleAtom )
            int alt52=3;
            switch ( input.LA(1) ) {
            case 47:
                {
                alt52=1;
                }
                break;
            case 48:
                {
                alt52=2;
                }
                break;
            case RULE_ID:
                {
                alt52=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 52, 0, input);

                throw nvae;
            }

            switch (alt52) {
                case 1 :
                    // InternalCqrsDsl.g:2295:3: ( () otherlv_1= '!' ( (lv_expr_2_0= ruleRuleUnary ) ) )
                    {
                    // InternalCqrsDsl.g:2295:3: ( () otherlv_1= '!' ( (lv_expr_2_0= ruleRuleUnary ) ) )
                    // InternalCqrsDsl.g:2296:4: () otherlv_1= '!' ( (lv_expr_2_0= ruleRuleUnary ) )
                    {
                    // InternalCqrsDsl.g:2296:4: ()
                    // InternalCqrsDsl.g:2297:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getRuleUnaryAccess().getRuleNotAction_0_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_1=(Token)match(input,47,FOLLOW_62); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_1, grammarAccess.getRuleUnaryAccess().getExclamationMarkKeyword_0_1());
                      			
                    }
                    // InternalCqrsDsl.g:2307:4: ( (lv_expr_2_0= ruleRuleUnary ) )
                    // InternalCqrsDsl.g:2308:5: (lv_expr_2_0= ruleRuleUnary )
                    {
                    // InternalCqrsDsl.g:2308:5: (lv_expr_2_0= ruleRuleUnary )
                    // InternalCqrsDsl.g:2309:6: lv_expr_2_0= ruleRuleUnary
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getRuleUnaryAccess().getExprRuleUnaryParserRuleCall_0_2_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_expr_2_0=ruleRuleUnary();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getRuleUnaryRule());
                      						}
                      						set(
                      							current,
                      							"expr",
                      							lv_expr_2_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.RuleUnary");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:2328:3: (otherlv_3= '(' this_RuleOr_4= ruleRuleOr otherlv_5= ')' )
                    {
                    // InternalCqrsDsl.g:2328:3: (otherlv_3= '(' this_RuleOr_4= ruleRuleOr otherlv_5= ')' )
                    // InternalCqrsDsl.g:2329:4: otherlv_3= '(' this_RuleOr_4= ruleRuleOr otherlv_5= ')'
                    {
                    otherlv_3=(Token)match(input,48,FOLLOW_62); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getRuleUnaryAccess().getLeftParenthesisKeyword_1_0());
                      			
                    }
                    if ( state.backtracking==0 ) {

                      				newCompositeNode(grammarAccess.getRuleUnaryAccess().getRuleOrParserRuleCall_1_1());
                      			
                    }
                    pushFollow(FOLLOW_65);
                    this_RuleOr_4=ruleRuleOr();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = this_RuleOr_4;
                      				afterParserOrEnumRuleCall();
                      			
                    }
                    otherlv_5=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getRuleUnaryAccess().getRightParenthesisKeyword_1_2());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:2347:3: this_RuleAtom_6= ruleRuleAtom
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getRuleUnaryAccess().getRuleAtomParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_RuleAtom_6=ruleRuleAtom();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_RuleAtom_6;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleUnary"


    // $ANTLR start "entryRuleRuleAtom"
    // InternalCqrsDsl.g:2359:1: entryRuleRuleAtom returns [EObject current=null] : iv_ruleRuleAtom= ruleRuleAtom EOF ;
    public final EObject entryRuleRuleAtom() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleAtom = null;


        try {
            // InternalCqrsDsl.g:2359:49: (iv_ruleRuleAtom= ruleRuleAtom EOF )
            // InternalCqrsDsl.g:2360:2: iv_ruleRuleAtom= ruleRuleAtom EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getRuleAtomRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleRuleAtom=ruleRuleAtom();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleRuleAtom; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleAtom"


    // $ANTLR start "ruleRuleAtom"
    // InternalCqrsDsl.g:2366:1: ruleRuleAtom returns [EObject current=null] : ( () ( (otherlv_1= RULE_ID ) ) ( ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) ) | ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' ) )? ) ;
    public final EObject ruleRuleAtom() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Enumerator lv_op_3_0 = null;

        EObject lv_right_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2372:2: ( ( () ( (otherlv_1= RULE_ID ) ) ( ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) ) | ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' ) )? ) )
            // InternalCqrsDsl.g:2373:2: ( () ( (otherlv_1= RULE_ID ) ) ( ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) ) | ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' ) )? )
            {
            // InternalCqrsDsl.g:2373:2: ( () ( (otherlv_1= RULE_ID ) ) ( ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) ) | ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' ) )? )
            // InternalCqrsDsl.g:2374:3: () ( (otherlv_1= RULE_ID ) ) ( ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) ) | ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' ) )?
            {
            // InternalCqrsDsl.g:2374:3: ()
            // InternalCqrsDsl.g:2375:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getRuleAtomAccess().getRuleAttrRefAction_0(),
              					current);
              			
            }

            }

            // InternalCqrsDsl.g:2381:3: ( (otherlv_1= RULE_ID ) )
            // InternalCqrsDsl.g:2382:4: (otherlv_1= RULE_ID )
            {
            // InternalCqrsDsl.g:2382:4: (otherlv_1= RULE_ID )
            // InternalCqrsDsl.g:2383:5: otherlv_1= RULE_ID
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getRuleAtomRule());
              					}
              				
            }
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_66); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(otherlv_1, grammarAccess.getRuleAtomAccess().getAttributeAttributeCrossReference_1_0());
              				
            }

            }


            }

            // InternalCqrsDsl.g:2394:3: ( ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) ) | ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' ) )?
            int alt53=3;
            int LA53_0 = input.LA(1);

            if ( ((LA53_0>=88 && LA53_0<=89)||(LA53_0>=160 && LA53_0<=163)) ) {
                alt53=1;
            }
            else if ( (LA53_0==50) ) {
                alt53=2;
            }
            switch (alt53) {
                case 1 :
                    // InternalCqrsDsl.g:2395:4: ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) )
                    {
                    // InternalCqrsDsl.g:2395:4: ( () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) ) )
                    // InternalCqrsDsl.g:2396:5: () ( (lv_op_3_0= ruleCompareOp ) ) ( (lv_right_4_0= ruleRuleOperand ) )
                    {
                    // InternalCqrsDsl.g:2396:5: ()
                    // InternalCqrsDsl.g:2397:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElementAndSet(
                      							grammarAccess.getRuleAtomAccess().getRuleComparisonLeftAction_2_0_0(),
                      							current);
                      					
                    }

                    }

                    // InternalCqrsDsl.g:2403:5: ( (lv_op_3_0= ruleCompareOp ) )
                    // InternalCqrsDsl.g:2404:6: (lv_op_3_0= ruleCompareOp )
                    {
                    // InternalCqrsDsl.g:2404:6: (lv_op_3_0= ruleCompareOp )
                    // InternalCqrsDsl.g:2405:7: lv_op_3_0= ruleCompareOp
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getRuleAtomAccess().getOpCompareOpEnumRuleCall_2_0_1_0());
                      						
                    }
                    pushFollow(FOLLOW_67);
                    lv_op_3_0=ruleCompareOp();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElementForParent(grammarAccess.getRuleAtomRule());
                      							}
                      							set(
                      								current,
                      								"op",
                      								lv_op_3_0,
                      								"org.fuin.dsl.cqrs.CqrsDsl.CompareOp");
                      							afterParserOrEnumRuleCall();
                      						
                    }

                    }


                    }

                    // InternalCqrsDsl.g:2422:5: ( (lv_right_4_0= ruleRuleOperand ) )
                    // InternalCqrsDsl.g:2423:6: (lv_right_4_0= ruleRuleOperand )
                    {
                    // InternalCqrsDsl.g:2423:6: (lv_right_4_0= ruleRuleOperand )
                    // InternalCqrsDsl.g:2424:7: lv_right_4_0= ruleRuleOperand
                    {
                    if ( state.backtracking==0 ) {

                      							newCompositeNode(grammarAccess.getRuleAtomAccess().getRightRuleOperandParserRuleCall_2_0_2_0());
                      						
                    }
                    pushFollow(FOLLOW_2);
                    lv_right_4_0=ruleRuleOperand();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      							if (current==null) {
                      								current = createModelElementForParent(grammarAccess.getRuleAtomRule());
                      							}
                      							set(
                      								current,
                      								"right",
                      								lv_right_4_0,
                      								"org.fuin.dsl.cqrs.CqrsDsl.RuleOperand");
                      							afterParserOrEnumRuleCall();
                      						
                    }

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:2443:4: ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' )
                    {
                    // InternalCqrsDsl.g:2443:4: ( () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')' )
                    // InternalCqrsDsl.g:2444:5: () otherlv_6= '.' otherlv_7= 'is-empty' otherlv_8= '(' otherlv_9= ')'
                    {
                    // InternalCqrsDsl.g:2444:5: ()
                    // InternalCqrsDsl.g:2445:6: 
                    {
                    if ( state.backtracking==0 ) {

                      						current = forceCreateModelElementAndSet(
                      							grammarAccess.getRuleAtomAccess().getRuleIsEmptyLeftAction_2_1_0(),
                      							current);
                      					
                    }

                    }

                    otherlv_6=(Token)match(input,50,FOLLOW_68); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(otherlv_6, grammarAccess.getRuleAtomAccess().getFullStopKeyword_2_1_1());
                      				
                    }
                    otherlv_7=(Token)match(input,51,FOLLOW_69); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(otherlv_7, grammarAccess.getRuleAtomAccess().getIsEmptyKeyword_2_1_2());
                      				
                    }
                    otherlv_8=(Token)match(input,48,FOLLOW_65); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(otherlv_8, grammarAccess.getRuleAtomAccess().getLeftParenthesisKeyword_2_1_3());
                      				
                    }
                    otherlv_9=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(otherlv_9, grammarAccess.getRuleAtomAccess().getRightParenthesisKeyword_2_1_4());
                      				
                    }

                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleAtom"


    // $ANTLR start "entryRuleRuleOperand"
    // InternalCqrsDsl.g:2473:1: entryRuleRuleOperand returns [EObject current=null] : iv_ruleRuleOperand= ruleRuleOperand EOF ;
    public final EObject entryRuleRuleOperand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleOperand = null;


        try {
            // InternalCqrsDsl.g:2473:52: (iv_ruleRuleOperand= ruleRuleOperand EOF )
            // InternalCqrsDsl.g:2474:2: iv_ruleRuleOperand= ruleRuleOperand EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getRuleOperandRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleRuleOperand=ruleRuleOperand();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleRuleOperand; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleOperand"


    // $ANTLR start "ruleRuleOperand"
    // InternalCqrsDsl.g:2480:1: ruleRuleOperand returns [EObject current=null] : ( ( () ( (otherlv_1= RULE_ID ) ) ) | ( () ( (lv_nullValue_3_0= 'null' ) ) ) ) ;
    public final EObject ruleRuleOperand() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_nullValue_3_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:2486:2: ( ( ( () ( (otherlv_1= RULE_ID ) ) ) | ( () ( (lv_nullValue_3_0= 'null' ) ) ) ) )
            // InternalCqrsDsl.g:2487:2: ( ( () ( (otherlv_1= RULE_ID ) ) ) | ( () ( (lv_nullValue_3_0= 'null' ) ) ) )
            {
            // InternalCqrsDsl.g:2487:2: ( ( () ( (otherlv_1= RULE_ID ) ) ) | ( () ( (lv_nullValue_3_0= 'null' ) ) ) )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==RULE_ID) ) {
                alt54=1;
            }
            else if ( (LA54_0==52) ) {
                alt54=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;
            }
            switch (alt54) {
                case 1 :
                    // InternalCqrsDsl.g:2488:3: ( () ( (otherlv_1= RULE_ID ) ) )
                    {
                    // InternalCqrsDsl.g:2488:3: ( () ( (otherlv_1= RULE_ID ) ) )
                    // InternalCqrsDsl.g:2489:4: () ( (otherlv_1= RULE_ID ) )
                    {
                    // InternalCqrsDsl.g:2489:4: ()
                    // InternalCqrsDsl.g:2490:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getRuleOperandAccess().getRuleRefOperandAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalCqrsDsl.g:2496:4: ( (otherlv_1= RULE_ID ) )
                    // InternalCqrsDsl.g:2497:5: (otherlv_1= RULE_ID )
                    {
                    // InternalCqrsDsl.g:2497:5: (otherlv_1= RULE_ID )
                    // InternalCqrsDsl.g:2498:6: otherlv_1= RULE_ID
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getRuleOperandRule());
                      						}
                      					
                    }
                    otherlv_1=(Token)match(input,RULE_ID,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_1, grammarAccess.getRuleOperandAccess().getTargetEObjectCrossReference_0_1_0());
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:2511:3: ( () ( (lv_nullValue_3_0= 'null' ) ) )
                    {
                    // InternalCqrsDsl.g:2511:3: ( () ( (lv_nullValue_3_0= 'null' ) ) )
                    // InternalCqrsDsl.g:2512:4: () ( (lv_nullValue_3_0= 'null' ) )
                    {
                    // InternalCqrsDsl.g:2512:4: ()
                    // InternalCqrsDsl.g:2513:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getRuleOperandAccess().getRuleNullOperandAction_1_0(),
                      						current);
                      				
                    }

                    }

                    // InternalCqrsDsl.g:2519:4: ( (lv_nullValue_3_0= 'null' ) )
                    // InternalCqrsDsl.g:2520:5: (lv_nullValue_3_0= 'null' )
                    {
                    // InternalCqrsDsl.g:2520:5: (lv_nullValue_3_0= 'null' )
                    // InternalCqrsDsl.g:2521:6: lv_nullValue_3_0= 'null'
                    {
                    lv_nullValue_3_0=(Token)match(input,52,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_nullValue_3_0, grammarAccess.getRuleOperandAccess().getNullValueNullKeyword_1_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getRuleOperandRule());
                      						}
                      						setWithLastConsumed(current, "nullValue", lv_nullValue_3_0, "null");
                      					
                    }

                    }


                    }


                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleOperand"


    // $ANTLR start "entryRuleAnnotation"
    // InternalCqrsDsl.g:2538:1: entryRuleAnnotation returns [EObject current=null] : iv_ruleAnnotation= ruleAnnotation EOF ;
    public final EObject entryRuleAnnotation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnnotation = null;


        try {
            // InternalCqrsDsl.g:2538:51: (iv_ruleAnnotation= ruleAnnotation EOF )
            // InternalCqrsDsl.g:2539:2: iv_ruleAnnotation= ruleAnnotation EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAnnotationRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAnnotation=ruleAnnotation();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAnnotation; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAnnotation"


    // $ANTLR start "ruleAnnotation"
    // InternalCqrsDsl.g:2545:1: ruleAnnotation returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' ) ;
    public final EObject ruleAnnotation() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_attributes_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2551:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' ) )
            // InternalCqrsDsl.g:2552:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:2552:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' )
            // InternalCqrsDsl.g:2553:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}'
            {
            // InternalCqrsDsl.g:2553:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==RULE_DOC) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // InternalCqrsDsl.g:2554:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2554:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2555:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_70); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getAnnotationAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getAnnotationRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,53,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getAnnotationAccess().getAnnotationKeyword_1());
              		
            }
            // InternalCqrsDsl.g:2575:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2576:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2576:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2577:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getAnnotationAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getAnnotationRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_71); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getAnnotationAccess().getLeftCurlyBracketKeyword_3());
              		
            }
            // InternalCqrsDsl.g:2597:3: ( (lv_attributes_4_0= ruleAttribute ) )*
            loop56:
            do {
                int alt56=2;
                int LA56_0 = input.LA(1);

                if ( ((LA56_0>=RULE_DOC && LA56_0<=RULE_ID)||LA56_0==79) ) {
                    alt56=1;
                }


                switch (alt56) {
            	case 1 :
            	    // InternalCqrsDsl.g:2598:4: (lv_attributes_4_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2598:4: (lv_attributes_4_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2599:5: lv_attributes_4_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAnnotationAccess().getAttributesAttributeParserRuleCall_4_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_71);
            	    lv_attributes_4_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAnnotationRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_4_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop56;
                }
            } while (true);

            otherlv_5=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getAnnotationAccess().getRightCurlyBracketKeyword_5());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAnnotation"


    // $ANTLR start "entryRuleException"
    // InternalCqrsDsl.g:2624:1: entryRuleException returns [EObject current=null] : iv_ruleException= ruleException EOF ;
    public final EObject entryRuleException() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleException = null;


        try {
            // InternalCqrsDsl.g:2624:50: (iv_ruleException= ruleException EOF )
            // InternalCqrsDsl.g:2625:2: iv_ruleException= ruleException EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getExceptionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleException=ruleException();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleException; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleException"


    // $ANTLR start "ruleException"
    // InternalCqrsDsl.g:2631:1: ruleException returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' ) ;
    public final EObject ruleException() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token lv_cid_4_0=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token lv_message_8_0=null;
        Token otherlv_9=null;
        EObject lv_attributes_6_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2637:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' ) )
            // InternalCqrsDsl.g:2638:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' )
            {
            // InternalCqrsDsl.g:2638:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' )
            // InternalCqrsDsl.g:2639:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}'
            {
            // InternalCqrsDsl.g:2639:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==RULE_DOC) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // InternalCqrsDsl.g:2640:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2640:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2641:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_59); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getExceptionAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getExceptionRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,41,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getExceptionAccess().getExceptionKeyword_1());
              		
            }
            // InternalCqrsDsl.g:2661:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2662:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2662:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2663:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_72); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getExceptionAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getExceptionRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:2679:3: (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==54) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // InternalCqrsDsl.g:2680:4: otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) )
                    {
                    otherlv_3=(Token)match(input,54,FOLLOW_22); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getExceptionAccess().getCidKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:2684:4: ( (lv_cid_4_0= RULE_INT ) )
                    // InternalCqrsDsl.g:2685:5: (lv_cid_4_0= RULE_INT )
                    {
                    // InternalCqrsDsl.g:2685:5: (lv_cid_4_0= RULE_INT )
                    // InternalCqrsDsl.g:2686:6: lv_cid_4_0= RULE_INT
                    {
                    lv_cid_4_0=(Token)match(input,RULE_INT,FOLLOW_5); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_cid_4_0, grammarAccess.getExceptionAccess().getCidINTTerminalRuleCall_3_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getExceptionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"cid",
                      							lv_cid_4_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.INT");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_5=(Token)match(input,14,FOLLOW_73); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getExceptionAccess().getLeftCurlyBracketKeyword_4());
              		
            }
            // InternalCqrsDsl.g:2707:3: ( (lv_attributes_6_0= ruleAttribute ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( ((LA59_0>=RULE_DOC && LA59_0<=RULE_ID)||LA59_0==79) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalCqrsDsl.g:2708:4: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2708:4: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2709:5: lv_attributes_6_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getExceptionAccess().getAttributesAttributeParserRuleCall_5_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_73);
            	    lv_attributes_6_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getExceptionRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_6_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);

            otherlv_7=(Token)match(input,42,FOLLOW_15); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getExceptionAccess().getMessageKeyword_6());
              		
            }
            // InternalCqrsDsl.g:2730:3: ( (lv_message_8_0= RULE_STRING ) )
            // InternalCqrsDsl.g:2731:4: (lv_message_8_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:2731:4: (lv_message_8_0= RULE_STRING )
            // InternalCqrsDsl.g:2732:5: lv_message_8_0= RULE_STRING
            {
            lv_message_8_0=(Token)match(input,RULE_STRING,FOLLOW_35); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_message_8_0, grammarAccess.getExceptionAccess().getMessageSTRINGTerminalRuleCall_7_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getExceptionRule());
              					}
              					setWithLastConsumed(
              						current,
              						"message",
              						lv_message_8_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.STRING");
              				
            }

            }


            }

            otherlv_9=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getExceptionAccess().getRightCurlyBracketKeyword_8());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleException"


    // $ANTLR start "entryRuleValueObject"
    // InternalCqrsDsl.g:2756:1: entryRuleValueObject returns [EObject current=null] : iv_ruleValueObject= ruleValueObject EOF ;
    public final EObject entryRuleValueObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleValueObject = null;


        try {
            // InternalCqrsDsl.g:2756:52: (iv_ruleValueObject= ruleValueObject EOF )
            // InternalCqrsDsl.g:2757:2: iv_ruleValueObject= ruleValueObject EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getValueObjectRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleValueObject=ruleValueObject();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleValueObject; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleValueObject"


    // $ANTLR start "ruleValueObject"
    // InternalCqrsDsl.g:2763:1: ruleValueObject returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_hints_10_0= ruleHint ) )* ( (lv_attributes_11_0= ruleAttribute ) )* (otherlv_12= 'identified-by' ( (otherlv_13= RULE_ID ) ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* otherlv_16= '}' ) ;
    public final EObject ruleValueObject() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token otherlv_8=null;
        Token otherlv_12=null;
        Token otherlv_13=null;
        Token otherlv_16=null;
        EObject lv_annotations_1_0 = null;

        EObject lv_invariants_6_0 = null;

        EObject lv_dataProtection_7_0 = null;

        EObject lv_metaInfo_9_0 = null;

        EObject lv_hints_10_0 = null;

        EObject lv_attributes_11_0 = null;

        EObject lv_constructors_14_0 = null;

        EObject lv_methods_15_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2769:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_hints_10_0= ruleHint ) )* ( (lv_attributes_11_0= ruleAttribute ) )* (otherlv_12= 'identified-by' ( (otherlv_13= RULE_ID ) ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* otherlv_16= '}' ) )
            // InternalCqrsDsl.g:2770:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_hints_10_0= ruleHint ) )* ( (lv_attributes_11_0= ruleAttribute ) )* (otherlv_12= 'identified-by' ( (otherlv_13= RULE_ID ) ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* otherlv_16= '}' )
            {
            // InternalCqrsDsl.g:2770:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_hints_10_0= ruleHint ) )* ( (lv_attributes_11_0= ruleAttribute ) )* (otherlv_12= 'identified-by' ( (otherlv_13= RULE_ID ) ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* otherlv_16= '}' )
            // InternalCqrsDsl.g:2771:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_hints_10_0= ruleHint ) )* ( (lv_attributes_11_0= ruleAttribute ) )* (otherlv_12= 'identified-by' ( (otherlv_13= RULE_ID ) ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* otherlv_16= '}'
            {
            // InternalCqrsDsl.g:2771:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==RULE_DOC) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // InternalCqrsDsl.g:2772:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2772:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2773:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_74); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getValueObjectAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getValueObjectRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2789:3: ( (lv_annotations_1_0= ruleAnnotationInstance ) )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==94) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // InternalCqrsDsl.g:2790:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    {
            	    // InternalCqrsDsl.g:2790:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    // InternalCqrsDsl.g:2791:5: lv_annotations_1_0= ruleAnnotationInstance
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getValueObjectAccess().getAnnotationsAnnotationInstanceParserRuleCall_1_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_74);
            	    lv_annotations_1_0=ruleAnnotationInstance();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"annotations",
            	      						lv_annotations_1_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.AnnotationInstance");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);

            otherlv_2=(Token)match(input,55,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getValueObjectAccess().getValueObjectKeyword_2());
              		
            }
            // InternalCqrsDsl.g:2812:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:2813:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2813:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:2814:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_75); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_3_0, grammarAccess.getValueObjectAccess().getNameIDTerminalRuleCall_3_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getValueObjectRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_3_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:2830:3: (otherlv_4= 'base' ( ( ruleFQN ) ) )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==56) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // InternalCqrsDsl.g:2831:4: otherlv_4= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_4=(Token)match(input,56,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getValueObjectAccess().getBaseKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:2835:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2836:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2836:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2837:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getValueObjectRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getValueObjectAccess().getBaseExternalTypeCrossReference_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_76);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2852:3: ( (lv_invariants_6_0= ruleInvariants ) )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==90) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // InternalCqrsDsl.g:2853:4: (lv_invariants_6_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2853:4: (lv_invariants_6_0= ruleInvariants )
                    // InternalCqrsDsl.g:2854:5: lv_invariants_6_0= ruleInvariants
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getValueObjectAccess().getInvariantsInvariantsParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_77);
                    lv_invariants_6_0=ruleInvariants();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getValueObjectRule());
                      					}
                      					set(
                      						current,
                      						"invariants",
                      						lv_invariants_6_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Invariants");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2871:3: ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==37) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // InternalCqrsDsl.g:2872:4: (lv_dataProtection_7_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2872:4: (lv_dataProtection_7_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2873:5: lv_dataProtection_7_0= ruleDataProtectionInstance
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getValueObjectAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                      				
                    }
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_7_0=ruleDataProtectionInstance();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getValueObjectRule());
                      					}
                      					set(
                      						current,
                      						"dataProtection",
                      						lv_dataProtection_7_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DataProtectionInstance");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_8=(Token)match(input,14,FOLLOW_78); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_8, grammarAccess.getValueObjectAccess().getLeftCurlyBracketKeyword_7());
              		
            }
            // InternalCqrsDsl.g:2894:3: ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2895:4: (lv_metaInfo_9_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2895:4: (lv_metaInfo_9_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2896:5: lv_metaInfo_9_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getValueObjectAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
              				
            }
            pushFollow(FOLLOW_79);
            lv_metaInfo_9_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getValueObjectRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_9_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:2913:3: ( (lv_hints_10_0= ruleHint ) )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==RULE_DOC) ) {
                    int LA65_1 = input.LA(2);

                    if ( (LA65_1==20) ) {
                        alt65=1;
                    }


                }
                else if ( (LA65_0==20) ) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // InternalCqrsDsl.g:2914:4: (lv_hints_10_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:2914:4: (lv_hints_10_0= ruleHint )
            	    // InternalCqrsDsl.g:2915:5: lv_hints_10_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getValueObjectAccess().getHintsHintParserRuleCall_9_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_79);
            	    lv_hints_10_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_10_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop65;
                }
            } while (true);

            // InternalCqrsDsl.g:2932:3: ( (lv_attributes_11_0= ruleAttribute ) )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==RULE_DOC) ) {
                    int LA66_2 = input.LA(2);

                    if ( (LA66_2==RULE_ID||LA66_2==79) ) {
                        alt66=1;
                    }


                }
                else if ( (LA66_0==RULE_ID||LA66_0==79) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // InternalCqrsDsl.g:2933:4: (lv_attributes_11_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2933:4: (lv_attributes_11_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2934:5: lv_attributes_11_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getValueObjectAccess().getAttributesAttributeParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_80);
            	    lv_attributes_11_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_11_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop66;
                }
            } while (true);

            // InternalCqrsDsl.g:2951:3: (otherlv_12= 'identified-by' ( (otherlv_13= RULE_ID ) ) )?
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( (LA67_0==57) ) {
                alt67=1;
            }
            switch (alt67) {
                case 1 :
                    // InternalCqrsDsl.g:2952:4: otherlv_12= 'identified-by' ( (otherlv_13= RULE_ID ) )
                    {
                    otherlv_12=(Token)match(input,57,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_12, grammarAccess.getValueObjectAccess().getIdentifiedByKeyword_11_0());
                      			
                    }
                    // InternalCqrsDsl.g:2956:4: ( (otherlv_13= RULE_ID ) )
                    // InternalCqrsDsl.g:2957:5: (otherlv_13= RULE_ID )
                    {
                    // InternalCqrsDsl.g:2957:5: (otherlv_13= RULE_ID )
                    // InternalCqrsDsl.g:2958:6: otherlv_13= RULE_ID
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getValueObjectRule());
                      						}
                      					
                    }
                    otherlv_13=(Token)match(input,RULE_ID,FOLLOW_81); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_13, grammarAccess.getValueObjectAccess().getIdentifiedByAttributeCrossReference_11_1_0());
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2970:3: ( (lv_constructors_14_0= ruleConstructor ) )*
            loop68:
            do {
                int alt68=2;
                int LA68_0 = input.LA(1);

                if ( (LA68_0==RULE_DOC) ) {
                    int LA68_1 = input.LA(2);

                    if ( (LA68_1==75) ) {
                        alt68=1;
                    }


                }
                else if ( (LA68_0==75) ) {
                    alt68=1;
                }


                switch (alt68) {
            	case 1 :
            	    // InternalCqrsDsl.g:2971:4: (lv_constructors_14_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:2971:4: (lv_constructors_14_0= ruleConstructor )
            	    // InternalCqrsDsl.g:2972:5: lv_constructors_14_0= ruleConstructor
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getValueObjectAccess().getConstructorsConstructorParserRuleCall_12_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_81);
            	    lv_constructors_14_0=ruleConstructor();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"constructors",
            	      						lv_constructors_14_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop68;
                }
            } while (true);

            // InternalCqrsDsl.g:2989:3: ( (lv_methods_15_0= ruleMethod ) )*
            loop69:
            do {
                int alt69=2;
                int LA69_0 = input.LA(1);

                if ( (LA69_0==RULE_DOC||LA69_0==80) ) {
                    alt69=1;
                }


                switch (alt69) {
            	case 1 :
            	    // InternalCqrsDsl.g:2990:4: (lv_methods_15_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:2990:4: (lv_methods_15_0= ruleMethod )
            	    // InternalCqrsDsl.g:2991:5: lv_methods_15_0= ruleMethod
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getValueObjectAccess().getMethodsMethodParserRuleCall_13_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_82);
            	    lv_methods_15_0=ruleMethod();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"methods",
            	      						lv_methods_15_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop69;
                }
            } while (true);

            otherlv_16=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_16, grammarAccess.getValueObjectAccess().getRightCurlyBracketKeyword_14());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleValueObject"


    // $ANTLR start "entryRuleEntityId"
    // InternalCqrsDsl.g:3016:1: entryRuleEntityId returns [EObject current=null] : iv_ruleEntityId= ruleEntityId EOF ;
    public final EObject entryRuleEntityId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntityId = null;


        try {
            // InternalCqrsDsl.g:3016:49: (iv_ruleEntityId= ruleEntityId EOF )
            // InternalCqrsDsl.g:3017:2: iv_ruleEntityId= ruleEntityId EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getEntityIdRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleEntityId=ruleEntityId();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleEntityId; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEntityId"


    // $ANTLR start "ruleEntityId"
    // InternalCqrsDsl.g:3023:1: ruleEntityId returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' ) ;
    public final EObject ruleEntityId() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_9=null;
        Token otherlv_15=null;
        EObject lv_invariants_7_0 = null;

        EObject lv_dataProtection_8_0 = null;

        EObject lv_metaInfo_10_0 = null;

        EObject lv_hints_11_0 = null;

        EObject lv_attributes_12_0 = null;

        EObject lv_constructors_13_0 = null;

        EObject lv_methods_14_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3029:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' ) )
            // InternalCqrsDsl.g:3030:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' )
            {
            // InternalCqrsDsl.g:3030:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' )
            // InternalCqrsDsl.g:3031:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}'
            {
            // InternalCqrsDsl.g:3031:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==RULE_DOC) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // InternalCqrsDsl.g:3032:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3032:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3033:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_83); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getEntityIdAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getEntityIdRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,58,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getEntityIdAccess().getEntityIdKeyword_1());
              		
            }
            // InternalCqrsDsl.g:3053:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3054:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3054:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3055:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_84); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getEntityIdAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getEntityIdRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:3071:3: (otherlv_3= 'identifies' ( ( ruleFQN ) ) )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==59) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // InternalCqrsDsl.g:3072:4: otherlv_3= 'identifies' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,59,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getEntityIdAccess().getIdentifiesKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:3076:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3077:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3077:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3078:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getEntityIdRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getEntityIdAccess().getEntityEntityCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_75);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3093:3: (otherlv_5= 'base' ( ( ruleFQN ) ) )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==56) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // InternalCqrsDsl.g:3094:4: otherlv_5= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,56,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getEntityIdAccess().getBaseKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:3098:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3099:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3099:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3100:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getEntityIdRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getEntityIdAccess().getBaseExternalTypeCrossReference_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_76);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3115:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==90) ) {
                alt73=1;
            }
            switch (alt73) {
                case 1 :
                    // InternalCqrsDsl.g:3116:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:3116:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:3117:5: lv_invariants_7_0= ruleInvariants
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEntityIdAccess().getInvariantsInvariantsParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_77);
                    lv_invariants_7_0=ruleInvariants();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEntityIdRule());
                      					}
                      					set(
                      						current,
                      						"invariants",
                      						lv_invariants_7_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Invariants");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3134:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt74=2;
            int LA74_0 = input.LA(1);

            if ( (LA74_0==37) ) {
                alt74=1;
            }
            switch (alt74) {
                case 1 :
                    // InternalCqrsDsl.g:3135:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:3135:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:3136:5: lv_dataProtection_8_0= ruleDataProtectionInstance
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEntityIdAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                      				
                    }
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_8_0=ruleDataProtectionInstance();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEntityIdRule());
                      					}
                      					set(
                      						current,
                      						"dataProtection",
                      						lv_dataProtection_8_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DataProtectionInstance");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_85); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getEntityIdAccess().getLeftCurlyBracketKeyword_7());
              		
            }
            // InternalCqrsDsl.g:3157:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:3158:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:3158:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:3159:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getEntityIdAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
              				
            }
            pushFollow(FOLLOW_86);
            lv_metaInfo_10_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getEntityIdRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_10_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:3176:3: ( (lv_hints_11_0= ruleHint ) )*
            loop75:
            do {
                int alt75=2;
                int LA75_0 = input.LA(1);

                if ( (LA75_0==RULE_DOC) ) {
                    int LA75_1 = input.LA(2);

                    if ( (LA75_1==20) ) {
                        alt75=1;
                    }


                }
                else if ( (LA75_0==20) ) {
                    alt75=1;
                }


                switch (alt75) {
            	case 1 :
            	    // InternalCqrsDsl.g:3177:4: (lv_hints_11_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:3177:4: (lv_hints_11_0= ruleHint )
            	    // InternalCqrsDsl.g:3178:5: lv_hints_11_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityIdAccess().getHintsHintParserRuleCall_9_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_86);
            	    lv_hints_11_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_11_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop75;
                }
            } while (true);

            // InternalCqrsDsl.g:3195:3: ( (lv_attributes_12_0= ruleAttribute ) )*
            loop76:
            do {
                int alt76=2;
                int LA76_0 = input.LA(1);

                if ( (LA76_0==RULE_DOC) ) {
                    int LA76_1 = input.LA(2);

                    if ( (LA76_1==RULE_ID||LA76_1==79) ) {
                        alt76=1;
                    }


                }
                else if ( (LA76_0==RULE_ID||LA76_0==79) ) {
                    alt76=1;
                }


                switch (alt76) {
            	case 1 :
            	    // InternalCqrsDsl.g:3196:4: (lv_attributes_12_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3196:4: (lv_attributes_12_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3197:5: lv_attributes_12_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityIdAccess().getAttributesAttributeParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_87);
            	    lv_attributes_12_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_12_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop76;
                }
            } while (true);

            // InternalCqrsDsl.g:3214:3: ( (lv_constructors_13_0= ruleConstructor ) )*
            loop77:
            do {
                int alt77=2;
                int LA77_0 = input.LA(1);

                if ( (LA77_0==RULE_DOC) ) {
                    int LA77_1 = input.LA(2);

                    if ( (LA77_1==75) ) {
                        alt77=1;
                    }


                }
                else if ( (LA77_0==75) ) {
                    alt77=1;
                }


                switch (alt77) {
            	case 1 :
            	    // InternalCqrsDsl.g:3215:4: (lv_constructors_13_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:3215:4: (lv_constructors_13_0= ruleConstructor )
            	    // InternalCqrsDsl.g:3216:5: lv_constructors_13_0= ruleConstructor
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityIdAccess().getConstructorsConstructorParserRuleCall_11_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_81);
            	    lv_constructors_13_0=ruleConstructor();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"constructors",
            	      						lv_constructors_13_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop77;
                }
            } while (true);

            // InternalCqrsDsl.g:3233:3: ( (lv_methods_14_0= ruleMethod ) )*
            loop78:
            do {
                int alt78=2;
                int LA78_0 = input.LA(1);

                if ( (LA78_0==RULE_DOC||LA78_0==80) ) {
                    alt78=1;
                }


                switch (alt78) {
            	case 1 :
            	    // InternalCqrsDsl.g:3234:4: (lv_methods_14_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:3234:4: (lv_methods_14_0= ruleMethod )
            	    // InternalCqrsDsl.g:3235:5: lv_methods_14_0= ruleMethod
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityIdAccess().getMethodsMethodParserRuleCall_12_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_82);
            	    lv_methods_14_0=ruleMethod();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"methods",
            	      						lv_methods_14_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop78;
                }
            } while (true);

            otherlv_15=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_15, grammarAccess.getEntityIdAccess().getRightCurlyBracketKeyword_13());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEntityId"


    // $ANTLR start "entryRuleAggregateId"
    // InternalCqrsDsl.g:3260:1: entryRuleAggregateId returns [EObject current=null] : iv_ruleAggregateId= ruleAggregateId EOF ;
    public final EObject entryRuleAggregateId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregateId = null;


        try {
            // InternalCqrsDsl.g:3260:52: (iv_ruleAggregateId= ruleAggregateId EOF )
            // InternalCqrsDsl.g:3261:2: iv_ruleAggregateId= ruleAggregateId EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAggregateIdRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAggregateId=ruleAggregateId();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAggregateId; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAggregateId"


    // $ANTLR start "ruleAggregateId"
    // InternalCqrsDsl.g:3267:1: ruleAggregateId returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' ) ;
    public final EObject ruleAggregateId() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_9=null;
        Token otherlv_15=null;
        EObject lv_invariants_7_0 = null;

        EObject lv_dataProtection_8_0 = null;

        EObject lv_metaInfo_10_0 = null;

        EObject lv_hints_11_0 = null;

        EObject lv_attributes_12_0 = null;

        EObject lv_constructors_13_0 = null;

        EObject lv_methods_14_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3273:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' ) )
            // InternalCqrsDsl.g:3274:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' )
            {
            // InternalCqrsDsl.g:3274:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}' )
            // InternalCqrsDsl.g:3275:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* otherlv_15= '}'
            {
            // InternalCqrsDsl.g:3275:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( (LA79_0==RULE_DOC) ) {
                alt79=1;
            }
            switch (alt79) {
                case 1 :
                    // InternalCqrsDsl.g:3276:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3276:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3277:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_88); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getAggregateIdAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getAggregateIdRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,60,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getAggregateIdAccess().getAggregateIdKeyword_1());
              		
            }
            // InternalCqrsDsl.g:3297:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3298:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3298:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3299:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_84); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getAggregateIdAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getAggregateIdRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:3315:3: (otherlv_3= 'identifies' ( ( ruleFQN ) ) )?
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( (LA80_0==59) ) {
                alt80=1;
            }
            switch (alt80) {
                case 1 :
                    // InternalCqrsDsl.g:3316:4: otherlv_3= 'identifies' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,59,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getAggregateIdAccess().getIdentifiesKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:3320:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3321:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3321:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3322:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getAggregateIdRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getAggregateIdAccess().getAggregateAggregateCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_75);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3337:3: (otherlv_5= 'base' ( ( ruleFQN ) ) )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( (LA81_0==56) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // InternalCqrsDsl.g:3338:4: otherlv_5= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,56,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getAggregateIdAccess().getBaseKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:3342:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3343:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3343:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3344:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getAggregateIdRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getAggregateIdAccess().getBaseExternalTypeCrossReference_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_76);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3359:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt82=2;
            int LA82_0 = input.LA(1);

            if ( (LA82_0==90) ) {
                alt82=1;
            }
            switch (alt82) {
                case 1 :
                    // InternalCqrsDsl.g:3360:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:3360:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:3361:5: lv_invariants_7_0= ruleInvariants
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAggregateIdAccess().getInvariantsInvariantsParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_77);
                    lv_invariants_7_0=ruleInvariants();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
                      					}
                      					set(
                      						current,
                      						"invariants",
                      						lv_invariants_7_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Invariants");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3378:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==37) ) {
                alt83=1;
            }
            switch (alt83) {
                case 1 :
                    // InternalCqrsDsl.g:3379:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:3379:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:3380:5: lv_dataProtection_8_0= ruleDataProtectionInstance
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAggregateIdAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                      				
                    }
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_8_0=ruleDataProtectionInstance();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
                      					}
                      					set(
                      						current,
                      						"dataProtection",
                      						lv_dataProtection_8_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DataProtectionInstance");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_85); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getAggregateIdAccess().getLeftCurlyBracketKeyword_7());
              		
            }
            // InternalCqrsDsl.g:3401:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:3402:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:3402:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:3403:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getAggregateIdAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
              				
            }
            pushFollow(FOLLOW_86);
            lv_metaInfo_10_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_10_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:3420:3: ( (lv_hints_11_0= ruleHint ) )*
            loop84:
            do {
                int alt84=2;
                int LA84_0 = input.LA(1);

                if ( (LA84_0==RULE_DOC) ) {
                    int LA84_1 = input.LA(2);

                    if ( (LA84_1==20) ) {
                        alt84=1;
                    }


                }
                else if ( (LA84_0==20) ) {
                    alt84=1;
                }


                switch (alt84) {
            	case 1 :
            	    // InternalCqrsDsl.g:3421:4: (lv_hints_11_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:3421:4: (lv_hints_11_0= ruleHint )
            	    // InternalCqrsDsl.g:3422:5: lv_hints_11_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateIdAccess().getHintsHintParserRuleCall_9_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_86);
            	    lv_hints_11_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_11_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop84;
                }
            } while (true);

            // InternalCqrsDsl.g:3439:3: ( (lv_attributes_12_0= ruleAttribute ) )*
            loop85:
            do {
                int alt85=2;
                int LA85_0 = input.LA(1);

                if ( (LA85_0==RULE_DOC) ) {
                    int LA85_1 = input.LA(2);

                    if ( (LA85_1==RULE_ID||LA85_1==79) ) {
                        alt85=1;
                    }


                }
                else if ( (LA85_0==RULE_ID||LA85_0==79) ) {
                    alt85=1;
                }


                switch (alt85) {
            	case 1 :
            	    // InternalCqrsDsl.g:3440:4: (lv_attributes_12_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3440:4: (lv_attributes_12_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3441:5: lv_attributes_12_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateIdAccess().getAttributesAttributeParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_87);
            	    lv_attributes_12_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_12_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop85;
                }
            } while (true);

            // InternalCqrsDsl.g:3458:3: ( (lv_constructors_13_0= ruleConstructor ) )*
            loop86:
            do {
                int alt86=2;
                int LA86_0 = input.LA(1);

                if ( (LA86_0==RULE_DOC) ) {
                    int LA86_1 = input.LA(2);

                    if ( (LA86_1==75) ) {
                        alt86=1;
                    }


                }
                else if ( (LA86_0==75) ) {
                    alt86=1;
                }


                switch (alt86) {
            	case 1 :
            	    // InternalCqrsDsl.g:3459:4: (lv_constructors_13_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:3459:4: (lv_constructors_13_0= ruleConstructor )
            	    // InternalCqrsDsl.g:3460:5: lv_constructors_13_0= ruleConstructor
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateIdAccess().getConstructorsConstructorParserRuleCall_11_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_81);
            	    lv_constructors_13_0=ruleConstructor();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"constructors",
            	      						lv_constructors_13_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop86;
                }
            } while (true);

            // InternalCqrsDsl.g:3477:3: ( (lv_methods_14_0= ruleMethod ) )*
            loop87:
            do {
                int alt87=2;
                int LA87_0 = input.LA(1);

                if ( (LA87_0==RULE_DOC||LA87_0==80) ) {
                    alt87=1;
                }


                switch (alt87) {
            	case 1 :
            	    // InternalCqrsDsl.g:3478:4: (lv_methods_14_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:3478:4: (lv_methods_14_0= ruleMethod )
            	    // InternalCqrsDsl.g:3479:5: lv_methods_14_0= ruleMethod
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateIdAccess().getMethodsMethodParserRuleCall_12_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_82);
            	    lv_methods_14_0=ruleMethod();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
            	      					}
            	      					add(
            	      						current,
            	      						"methods",
            	      						lv_methods_14_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop87;
                }
            } while (true);

            otherlv_15=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_15, grammarAccess.getAggregateIdAccess().getRightCurlyBracketKeyword_13());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAggregateId"


    // $ANTLR start "entryRuleEnumObject"
    // InternalCqrsDsl.g:3504:1: entryRuleEnumObject returns [EObject current=null] : iv_ruleEnumObject= ruleEnumObject EOF ;
    public final EObject entryRuleEnumObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumObject = null;


        try {
            // InternalCqrsDsl.g:3504:51: (iv_ruleEnumObject= ruleEnumObject EOF )
            // InternalCqrsDsl.g:3505:2: iv_ruleEnumObject= ruleEnumObject EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getEnumObjectRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleEnumObject=ruleEnumObject();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleEnumObject; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEnumObject"


    // $ANTLR start "ruleEnumObject"
    // InternalCqrsDsl.g:3511:1: ruleEnumObject returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* otherlv_11= 'instances' otherlv_12= '{' ( (lv_instances_13_0= ruleEnumInstance ) )+ otherlv_14= '}' otherlv_15= '}' ) ;
    public final EObject ruleEnumObject() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_7=null;
        Token otherlv_11=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token otherlv_15=null;
        EObject lv_invariants_5_0 = null;

        EObject lv_dataProtection_6_0 = null;

        EObject lv_metaInfo_8_0 = null;

        EObject lv_hints_9_0 = null;

        EObject lv_attributes_10_0 = null;

        EObject lv_instances_13_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3517:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* otherlv_11= 'instances' otherlv_12= '{' ( (lv_instances_13_0= ruleEnumInstance ) )+ otherlv_14= '}' otherlv_15= '}' ) )
            // InternalCqrsDsl.g:3518:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* otherlv_11= 'instances' otherlv_12= '{' ( (lv_instances_13_0= ruleEnumInstance ) )+ otherlv_14= '}' otherlv_15= '}' )
            {
            // InternalCqrsDsl.g:3518:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* otherlv_11= 'instances' otherlv_12= '{' ( (lv_instances_13_0= ruleEnumInstance ) )+ otherlv_14= '}' otherlv_15= '}' )
            // InternalCqrsDsl.g:3519:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* otherlv_11= 'instances' otherlv_12= '{' ( (lv_instances_13_0= ruleEnumInstance ) )+ otherlv_14= '}' otherlv_15= '}'
            {
            // InternalCqrsDsl.g:3519:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( (LA88_0==RULE_DOC) ) {
                alt88=1;
            }
            switch (alt88) {
                case 1 :
                    // InternalCqrsDsl.g:3520:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3520:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3521:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_89); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getEnumObjectAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getEnumObjectRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,61,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getEnumObjectAccess().getEnumKeyword_1());
              		
            }
            // InternalCqrsDsl.g:3541:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3542:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3542:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3543:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_75); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getEnumObjectAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getEnumObjectRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:3559:3: (otherlv_3= 'base' ( ( ruleFQN ) ) )?
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( (LA89_0==56) ) {
                alt89=1;
            }
            switch (alt89) {
                case 1 :
                    // InternalCqrsDsl.g:3560:4: otherlv_3= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,56,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getEnumObjectAccess().getBaseKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:3564:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3565:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3565:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3566:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getEnumObjectRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getEnumObjectAccess().getBaseExternalTypeCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_76);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3581:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt90=2;
            int LA90_0 = input.LA(1);

            if ( (LA90_0==90) ) {
                alt90=1;
            }
            switch (alt90) {
                case 1 :
                    // InternalCqrsDsl.g:3582:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:3582:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:3583:5: lv_invariants_5_0= ruleInvariants
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEnumObjectAccess().getInvariantsInvariantsParserRuleCall_4_0());
                      				
                    }
                    pushFollow(FOLLOW_77);
                    lv_invariants_5_0=ruleInvariants();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
                      					}
                      					set(
                      						current,
                      						"invariants",
                      						lv_invariants_5_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Invariants");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3600:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( (LA91_0==37) ) {
                alt91=1;
            }
            switch (alt91) {
                case 1 :
                    // InternalCqrsDsl.g:3601:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:3601:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:3602:5: lv_dataProtection_6_0= ruleDataProtectionInstance
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEnumObjectAccess().getDataProtectionDataProtectionInstanceParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_6_0=ruleDataProtectionInstance();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
                      					}
                      					set(
                      						current,
                      						"dataProtection",
                      						lv_dataProtection_6_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DataProtectionInstance");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_7=(Token)match(input,14,FOLLOW_90); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getEnumObjectAccess().getLeftCurlyBracketKeyword_6());
              		
            }
            // InternalCqrsDsl.g:3623:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:3624:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:3624:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:3625:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getEnumObjectAccess().getMetaInfoTypeMetaInfoParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_91);
            lv_metaInfo_8_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_8_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:3642:3: ( (lv_hints_9_0= ruleHint ) )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==RULE_DOC) ) {
                    int LA92_1 = input.LA(2);

                    if ( (LA92_1==20) ) {
                        alt92=1;
                    }


                }
                else if ( (LA92_0==20) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // InternalCqrsDsl.g:3643:4: (lv_hints_9_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:3643:4: (lv_hints_9_0= ruleHint )
            	    // InternalCqrsDsl.g:3644:5: lv_hints_9_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEnumObjectAccess().getHintsHintParserRuleCall_8_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_91);
            	    lv_hints_9_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_9_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop92;
                }
            } while (true);

            // InternalCqrsDsl.g:3661:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop93:
            do {
                int alt93=2;
                int LA93_0 = input.LA(1);

                if ( ((LA93_0>=RULE_DOC && LA93_0<=RULE_ID)||LA93_0==79) ) {
                    alt93=1;
                }


                switch (alt93) {
            	case 1 :
            	    // InternalCqrsDsl.g:3662:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3662:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3663:5: lv_attributes_10_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEnumObjectAccess().getAttributesAttributeParserRuleCall_9_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_92);
            	    lv_attributes_10_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_10_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop93;
                }
            } while (true);

            otherlv_11=(Token)match(input,62,FOLLOW_5); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_11, grammarAccess.getEnumObjectAccess().getInstancesKeyword_10());
              		
            }
            otherlv_12=(Token)match(input,14,FOLLOW_93); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_12, grammarAccess.getEnumObjectAccess().getLeftCurlyBracketKeyword_11());
              		
            }
            // InternalCqrsDsl.g:3688:3: ( (lv_instances_13_0= ruleEnumInstance ) )+
            int cnt94=0;
            loop94:
            do {
                int alt94=2;
                int LA94_0 = input.LA(1);

                if ( ((LA94_0>=RULE_DOC && LA94_0<=RULE_ID)||LA94_0==63) ) {
                    alt94=1;
                }


                switch (alt94) {
            	case 1 :
            	    // InternalCqrsDsl.g:3689:4: (lv_instances_13_0= ruleEnumInstance )
            	    {
            	    // InternalCqrsDsl.g:3689:4: (lv_instances_13_0= ruleEnumInstance )
            	    // InternalCqrsDsl.g:3690:5: lv_instances_13_0= ruleEnumInstance
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEnumObjectAccess().getInstancesEnumInstanceParserRuleCall_12_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_94);
            	    lv_instances_13_0=ruleEnumInstance();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
            	      					}
            	      					add(
            	      						current,
            	      						"instances",
            	      						lv_instances_13_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.EnumInstance");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt94 >= 1 ) break loop94;
            	    if (state.backtracking>0) {state.failed=true; return current;}
                        EarlyExitException eee =
                            new EarlyExitException(94, input);
                        throw eee;
                }
                cnt94++;
            } while (true);

            otherlv_14=(Token)match(input,15,FOLLOW_35); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_14, grammarAccess.getEnumObjectAccess().getRightCurlyBracketKeyword_13());
              		
            }
            otherlv_15=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_15, grammarAccess.getEnumObjectAccess().getRightCurlyBracketKeyword_14());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEnumObject"


    // $ANTLR start "entryRuleEnumInstance"
    // InternalCqrsDsl.g:3719:1: entryRuleEnumInstance returns [EObject current=null] : iv_ruleEnumInstance= ruleEnumInstance EOF ;
    public final EObject entryRuleEnumInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumInstance = null;


        try {
            // InternalCqrsDsl.g:3719:53: (iv_ruleEnumInstance= ruleEnumInstance EOF )
            // InternalCqrsDsl.g:3720:2: iv_ruleEnumInstance= ruleEnumInstance EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getEnumInstanceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleEnumInstance=ruleEnumInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleEnumInstance; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEnumInstance"


    // $ANTLR start "ruleEnumInstance"
    // InternalCqrsDsl.g:3726:1: ruleEnumInstance returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ( (lv_overridden_8_0= ruleOverriddenTypeMetaInfo ) )? ) ;
    public final EObject ruleEnumInstance() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token lv_deprecated_1_0=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        EObject lv_params_4_0 = null;

        EObject lv_params_6_0 = null;

        EObject lv_overridden_8_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3732:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ( (lv_overridden_8_0= ruleOverriddenTypeMetaInfo ) )? ) )
            // InternalCqrsDsl.g:3733:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ( (lv_overridden_8_0= ruleOverriddenTypeMetaInfo ) )? )
            {
            // InternalCqrsDsl.g:3733:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ( (lv_overridden_8_0= ruleOverriddenTypeMetaInfo ) )? )
            // InternalCqrsDsl.g:3734:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ( (lv_overridden_8_0= ruleOverriddenTypeMetaInfo ) )?
            {
            // InternalCqrsDsl.g:3734:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt95=2;
            int LA95_0 = input.LA(1);

            if ( (LA95_0==RULE_DOC) ) {
                alt95=1;
            }
            switch (alt95) {
                case 1 :
                    // InternalCqrsDsl.g:3735:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3735:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3736:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_95); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getEnumInstanceAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getEnumInstanceRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3752:3: ( (lv_deprecated_1_0= 'deprecated' ) )?
            int alt96=2;
            int LA96_0 = input.LA(1);

            if ( (LA96_0==63) ) {
                alt96=1;
            }
            switch (alt96) {
                case 1 :
                    // InternalCqrsDsl.g:3753:4: (lv_deprecated_1_0= 'deprecated' )
                    {
                    // InternalCqrsDsl.g:3753:4: (lv_deprecated_1_0= 'deprecated' )
                    // InternalCqrsDsl.g:3754:5: lv_deprecated_1_0= 'deprecated'
                    {
                    lv_deprecated_1_0=(Token)match(input,63,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_deprecated_1_0, grammarAccess.getEnumInstanceAccess().getDeprecatedDeprecatedKeyword_1_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getEnumInstanceRule());
                      					}
                      					setWithLastConsumed(current, "deprecated", lv_deprecated_1_0, "deprecated");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3766:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3767:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3767:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3768:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_96); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getEnumInstanceAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getEnumInstanceRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:3784:3: (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )?
            int alt98=2;
            int LA98_0 = input.LA(1);

            if ( (LA98_0==48) ) {
                alt98=1;
            }
            switch (alt98) {
                case 1 :
                    // InternalCqrsDsl.g:3785:4: otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')'
                    {
                    otherlv_3=(Token)match(input,48,FOLLOW_97); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getEnumInstanceAccess().getLeftParenthesisKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:3789:4: ( (lv_params_4_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:3790:5: (lv_params_4_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:3790:5: (lv_params_4_0= ruleLiteral )
                    // InternalCqrsDsl.g:3791:6: lv_params_4_0= ruleLiteral
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getEnumInstanceAccess().getParamsLiteralParserRuleCall_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_98);
                    lv_params_4_0=ruleLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getEnumInstanceRule());
                      						}
                      						add(
                      							current,
                      							"params",
                      							lv_params_4_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:3808:4: (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )*
                    loop97:
                    do {
                        int alt97=2;
                        int LA97_0 = input.LA(1);

                        if ( (LA97_0==31) ) {
                            alt97=1;
                        }


                        switch (alt97) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:3809:5: otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) )
                    	    {
                    	    otherlv_5=(Token)match(input,31,FOLLOW_97); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_5, grammarAccess.getEnumInstanceAccess().getCommaKeyword_3_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:3813:5: ( (lv_params_6_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:3814:6: (lv_params_6_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:3814:6: (lv_params_6_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:3815:7: lv_params_6_0= ruleLiteral
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getEnumInstanceAccess().getParamsLiteralParserRuleCall_3_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_98);
                    	    lv_params_6_0=ruleLiteral();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getEnumInstanceRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"params",
                    	      								lv_params_6_0,
                    	      								"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop97;
                        }
                    } while (true);

                    otherlv_7=(Token)match(input,49,FOLLOW_33); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getEnumInstanceAccess().getRightParenthesisKeyword_3_3());
                      			
                    }

                    }
                    break;

            }

            // InternalCqrsDsl.g:3838:3: ( (lv_overridden_8_0= ruleOverriddenTypeMetaInfo ) )?
            int alt99=2;
            int LA99_0 = input.LA(1);

            if ( (LA99_0==14) ) {
                alt99=1;
            }
            switch (alt99) {
                case 1 :
                    // InternalCqrsDsl.g:3839:4: (lv_overridden_8_0= ruleOverriddenTypeMetaInfo )
                    {
                    // InternalCqrsDsl.g:3839:4: (lv_overridden_8_0= ruleOverriddenTypeMetaInfo )
                    // InternalCqrsDsl.g:3840:5: lv_overridden_8_0= ruleOverriddenTypeMetaInfo
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEnumInstanceAccess().getOverriddenOverriddenTypeMetaInfoParserRuleCall_4_0());
                      				
                    }
                    pushFollow(FOLLOW_2);
                    lv_overridden_8_0=ruleOverriddenTypeMetaInfo();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEnumInstanceRule());
                      					}
                      					set(
                      						current,
                      						"overridden",
                      						lv_overridden_8_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.OverriddenTypeMetaInfo");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEnumInstance"


    // $ANTLR start "entryRuleEvent"
    // InternalCqrsDsl.g:3861:1: entryRuleEvent returns [EObject current=null] : iv_ruleEvent= ruleEvent EOF ;
    public final EObject entryRuleEvent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEvent = null;


        try {
            // InternalCqrsDsl.g:3861:46: (iv_ruleEvent= ruleEvent EOF )
            // InternalCqrsDsl.g:3862:2: iv_ruleEvent= ruleEvent EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getEventRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleEvent=ruleEvent();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleEvent; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEvent"


    // $ANTLR start "ruleEvent"
    // InternalCqrsDsl.g:3868:1: ruleEvent returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' ) ;
    public final EObject ruleEvent() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token lv_message_9_0=null;
        Token otherlv_10=null;
        EObject lv_annotations_1_0 = null;

        EObject lv_attributes_7_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3874:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' ) )
            // InternalCqrsDsl.g:3875:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' )
            {
            // InternalCqrsDsl.g:3875:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' )
            // InternalCqrsDsl.g:3876:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}'
            {
            // InternalCqrsDsl.g:3876:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt100=2;
            int LA100_0 = input.LA(1);

            if ( (LA100_0==RULE_DOC) ) {
                alt100=1;
            }
            switch (alt100) {
                case 1 :
                    // InternalCqrsDsl.g:3877:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3877:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3878:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_99); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getEventAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getEventRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3894:3: ( (lv_annotations_1_0= ruleAnnotationInstance ) )*
            loop101:
            do {
                int alt101=2;
                int LA101_0 = input.LA(1);

                if ( (LA101_0==94) ) {
                    alt101=1;
                }


                switch (alt101) {
            	case 1 :
            	    // InternalCqrsDsl.g:3895:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    {
            	    // InternalCqrsDsl.g:3895:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    // InternalCqrsDsl.g:3896:5: lv_annotations_1_0= ruleAnnotationInstance
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEventAccess().getAnnotationsAnnotationInstanceParserRuleCall_1_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_99);
            	    lv_annotations_1_0=ruleAnnotationInstance();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEventRule());
            	      					}
            	      					add(
            	      						current,
            	      						"annotations",
            	      						lv_annotations_1_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.AnnotationInstance");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop101;
                }
            } while (true);

            otherlv_2=(Token)match(input,64,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_2, grammarAccess.getEventAccess().getEventKeyword_2());
              		
            }
            // InternalCqrsDsl.g:3917:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:3918:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3918:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:3919:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_100); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_3_0, grammarAccess.getEventAccess().getNameIDTerminalRuleCall_3_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getEventRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_3_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:3935:3: (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )?
            int alt102=2;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==65) ) {
                alt102=1;
            }
            switch (alt102) {
                case 1 :
                    // InternalCqrsDsl.g:3936:4: otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) )
                    {
                    otherlv_4=(Token)match(input,65,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getEventAccess().getCopiesAttributesOfKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:3940:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3941:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3941:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3942:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getEventRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getEventAccess().getOriginAbstractMethodCrossReference_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_5);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,14,FOLLOW_57); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getEventAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalCqrsDsl.g:3961:3: ( (lv_attributes_7_0= ruleAttribute ) )*
            loop103:
            do {
                int alt103=2;
                int LA103_0 = input.LA(1);

                if ( ((LA103_0>=RULE_DOC && LA103_0<=RULE_ID)||LA103_0==79) ) {
                    alt103=1;
                }


                switch (alt103) {
            	case 1 :
            	    // InternalCqrsDsl.g:3962:4: (lv_attributes_7_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3962:4: (lv_attributes_7_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3963:5: lv_attributes_7_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEventAccess().getAttributesAttributeParserRuleCall_6_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_57);
            	    lv_attributes_7_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEventRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_7_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop103;
                }
            } while (true);

            // InternalCqrsDsl.g:3980:3: (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )?
            int alt104=2;
            int LA104_0 = input.LA(1);

            if ( (LA104_0==42) ) {
                alt104=1;
            }
            switch (alt104) {
                case 1 :
                    // InternalCqrsDsl.g:3981:4: otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) )
                    {
                    otherlv_8=(Token)match(input,42,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getEventAccess().getMessageKeyword_7_0());
                      			
                    }
                    // InternalCqrsDsl.g:3985:4: ( (lv_message_9_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:3986:5: (lv_message_9_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:3986:5: (lv_message_9_0= RULE_STRING )
                    // InternalCqrsDsl.g:3987:6: lv_message_9_0= RULE_STRING
                    {
                    lv_message_9_0=(Token)match(input,RULE_STRING,FOLLOW_35); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_message_9_0, grammarAccess.getEventAccess().getMessageSTRINGTerminalRuleCall_7_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getEventRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"message",
                      							lv_message_9_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_10=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_10, grammarAccess.getEventAccess().getRightCurlyBracketKeyword_8());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEvent"


    // $ANTLR start "entryRuleEntity"
    // InternalCqrsDsl.g:4012:1: entryRuleEntity returns [EObject current=null] : iv_ruleEntity= ruleEntity EOF ;
    public final EObject entryRuleEntity() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntity = null;


        try {
            // InternalCqrsDsl.g:4012:47: (iv_ruleEntity= ruleEntity EOF )
            // InternalCqrsDsl.g:4013:2: iv_ruleEntity= ruleEntity EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getEntityRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleEntity=ruleEntity();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleEntity; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEntity"


    // $ANTLR start "ruleEntity"
    // InternalCqrsDsl.g:4019:1: ruleEntity returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_businessRules_13_0= ruleBusinessRule ) )* ( (lv_keys_14_0= ruleKey ) )* ( (lv_noKey_15_0= ruleNoKey ) )? ( (lv_constructors_16_0= ruleConstructor ) )* ( (lv_methods_17_0= ruleMethod ) )* ( (lv_elements_18_0= ruleEntityElement ) )* otherlv_19= '}' ) ;
    public final EObject ruleEntity() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_9=null;
        Token otherlv_19=null;
        EObject lv_invariants_7_0 = null;

        EObject lv_dataProtection_8_0 = null;

        EObject lv_metaInfo_10_0 = null;

        EObject lv_hints_11_0 = null;

        EObject lv_attributes_12_0 = null;

        EObject lv_businessRules_13_0 = null;

        EObject lv_keys_14_0 = null;

        EObject lv_noKey_15_0 = null;

        EObject lv_constructors_16_0 = null;

        EObject lv_methods_17_0 = null;

        EObject lv_elements_18_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4025:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_businessRules_13_0= ruleBusinessRule ) )* ( (lv_keys_14_0= ruleKey ) )* ( (lv_noKey_15_0= ruleNoKey ) )? ( (lv_constructors_16_0= ruleConstructor ) )* ( (lv_methods_17_0= ruleMethod ) )* ( (lv_elements_18_0= ruleEntityElement ) )* otherlv_19= '}' ) )
            // InternalCqrsDsl.g:4026:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_businessRules_13_0= ruleBusinessRule ) )* ( (lv_keys_14_0= ruleKey ) )* ( (lv_noKey_15_0= ruleNoKey ) )? ( (lv_constructors_16_0= ruleConstructor ) )* ( (lv_methods_17_0= ruleMethod ) )* ( (lv_elements_18_0= ruleEntityElement ) )* otherlv_19= '}' )
            {
            // InternalCqrsDsl.g:4026:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_businessRules_13_0= ruleBusinessRule ) )* ( (lv_keys_14_0= ruleKey ) )* ( (lv_noKey_15_0= ruleNoKey ) )? ( (lv_constructors_16_0= ruleConstructor ) )* ( (lv_methods_17_0= ruleMethod ) )* ( (lv_elements_18_0= ruleEntityElement ) )* otherlv_19= '}' )
            // InternalCqrsDsl.g:4027:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_hints_11_0= ruleHint ) )* ( (lv_attributes_12_0= ruleAttribute ) )* ( (lv_businessRules_13_0= ruleBusinessRule ) )* ( (lv_keys_14_0= ruleKey ) )* ( (lv_noKey_15_0= ruleNoKey ) )? ( (lv_constructors_16_0= ruleConstructor ) )* ( (lv_methods_17_0= ruleMethod ) )* ( (lv_elements_18_0= ruleEntityElement ) )* otherlv_19= '}'
            {
            // InternalCqrsDsl.g:4027:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt105=2;
            int LA105_0 = input.LA(1);

            if ( (LA105_0==RULE_DOC) ) {
                alt105=1;
            }
            switch (alt105) {
                case 1 :
                    // InternalCqrsDsl.g:4028:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4028:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4029:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_101); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getEntityAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getEntityRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,66,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getEntityAccess().getEntityKeyword_1());
              		
            }
            // InternalCqrsDsl.g:4049:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:4050:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4050:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:4051:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_102); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getEntityAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getEntityRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:4067:3: (otherlv_3= 'identifier' ( ( ruleFQN ) ) )?
            int alt106=2;
            int LA106_0 = input.LA(1);

            if ( (LA106_0==67) ) {
                alt106=1;
            }
            switch (alt106) {
                case 1 :
                    // InternalCqrsDsl.g:4068:4: otherlv_3= 'identifier' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,67,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getEntityAccess().getIdentifierKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:4072:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:4073:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4073:5: ( ruleFQN )
                    // InternalCqrsDsl.g:4074:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getEntityRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getEntityAccess().getIdTypeEntityIdCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_103);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4089:3: (otherlv_5= 'root' ( ( ruleFQN ) ) )?
            int alt107=2;
            int LA107_0 = input.LA(1);

            if ( (LA107_0==68) ) {
                alt107=1;
            }
            switch (alt107) {
                case 1 :
                    // InternalCqrsDsl.g:4090:4: otherlv_5= 'root' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,68,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getEntityAccess().getRootKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:4094:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:4095:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4095:5: ( ruleFQN )
                    // InternalCqrsDsl.g:4096:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getEntityRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getEntityAccess().getRootAggregateCrossReference_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_76);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4111:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt108=2;
            int LA108_0 = input.LA(1);

            if ( (LA108_0==90) ) {
                alt108=1;
            }
            switch (alt108) {
                case 1 :
                    // InternalCqrsDsl.g:4112:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:4112:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:4113:5: lv_invariants_7_0= ruleInvariants
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEntityAccess().getInvariantsInvariantsParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_77);
                    lv_invariants_7_0=ruleInvariants();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEntityRule());
                      					}
                      					set(
                      						current,
                      						"invariants",
                      						lv_invariants_7_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Invariants");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4130:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt109=2;
            int LA109_0 = input.LA(1);

            if ( (LA109_0==37) ) {
                alt109=1;
            }
            switch (alt109) {
                case 1 :
                    // InternalCqrsDsl.g:4131:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:4131:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:4132:5: lv_dataProtection_8_0= ruleDataProtectionInstance
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEntityAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                      				
                    }
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_8_0=ruleDataProtectionInstance();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEntityRule());
                      					}
                      					set(
                      						current,
                      						"dataProtection",
                      						lv_dataProtection_8_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DataProtectionInstance");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_104); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getEntityAccess().getLeftCurlyBracketKeyword_7());
              		
            }
            // InternalCqrsDsl.g:4153:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:4154:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:4154:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:4155:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getEntityAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
              				
            }
            pushFollow(FOLLOW_105);
            lv_metaInfo_10_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getEntityRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_10_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:4172:3: ( (lv_hints_11_0= ruleHint ) )*
            loop110:
            do {
                int alt110=2;
                int LA110_0 = input.LA(1);

                if ( (LA110_0==RULE_DOC) ) {
                    int LA110_1 = input.LA(2);

                    if ( (LA110_1==20) ) {
                        alt110=1;
                    }


                }
                else if ( (LA110_0==20) ) {
                    alt110=1;
                }


                switch (alt110) {
            	case 1 :
            	    // InternalCqrsDsl.g:4173:4: (lv_hints_11_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:4173:4: (lv_hints_11_0= ruleHint )
            	    // InternalCqrsDsl.g:4174:5: lv_hints_11_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityAccess().getHintsHintParserRuleCall_9_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_105);
            	    lv_hints_11_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_11_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop110;
                }
            } while (true);

            // InternalCqrsDsl.g:4191:3: ( (lv_attributes_12_0= ruleAttribute ) )*
            loop111:
            do {
                int alt111=2;
                int LA111_0 = input.LA(1);

                if ( (LA111_0==RULE_DOC) ) {
                    int LA111_1 = input.LA(2);

                    if ( (LA111_1==RULE_ID||LA111_1==79) ) {
                        alt111=1;
                    }


                }
                else if ( (LA111_0==RULE_ID||LA111_0==79) ) {
                    alt111=1;
                }


                switch (alt111) {
            	case 1 :
            	    // InternalCqrsDsl.g:4192:4: (lv_attributes_12_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:4192:4: (lv_attributes_12_0= ruleAttribute )
            	    // InternalCqrsDsl.g:4193:5: lv_attributes_12_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityAccess().getAttributesAttributeParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_106);
            	    lv_attributes_12_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_12_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop111;
                }
            } while (true);

            // InternalCqrsDsl.g:4210:3: ( (lv_businessRules_13_0= ruleBusinessRule ) )*
            loop112:
            do {
                int alt112=2;
                int LA112_0 = input.LA(1);

                if ( (LA112_0==RULE_DOC) ) {
                    int LA112_1 = input.LA(2);

                    if ( (LA112_1==43) ) {
                        alt112=1;
                    }


                }


                switch (alt112) {
            	case 1 :
            	    // InternalCqrsDsl.g:4211:4: (lv_businessRules_13_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:4211:4: (lv_businessRules_13_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:4212:5: lv_businessRules_13_0= ruleBusinessRule
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityAccess().getBusinessRulesBusinessRuleParserRuleCall_11_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_107);
            	    lv_businessRules_13_0=ruleBusinessRule();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityRule());
            	      					}
            	      					add(
            	      						current,
            	      						"businessRules",
            	      						lv_businessRules_13_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRule");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop112;
                }
            } while (true);

            // InternalCqrsDsl.g:4229:3: ( (lv_keys_14_0= ruleKey ) )*
            loop113:
            do {
                int alt113=2;
                int LA113_0 = input.LA(1);

                if ( (LA113_0==RULE_DOC) ) {
                    int LA113_1 = input.LA(2);

                    if ( (LA113_1==70) ) {
                        alt113=1;
                    }


                }


                switch (alt113) {
            	case 1 :
            	    // InternalCqrsDsl.g:4230:4: (lv_keys_14_0= ruleKey )
            	    {
            	    // InternalCqrsDsl.g:4230:4: (lv_keys_14_0= ruleKey )
            	    // InternalCqrsDsl.g:4231:5: lv_keys_14_0= ruleKey
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityAccess().getKeysKeyParserRuleCall_12_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_107);
            	    lv_keys_14_0=ruleKey();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityRule());
            	      					}
            	      					add(
            	      						current,
            	      						"keys",
            	      						lv_keys_14_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Key");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop113;
                }
            } while (true);

            // InternalCqrsDsl.g:4248:3: ( (lv_noKey_15_0= ruleNoKey ) )?
            int alt114=2;
            int LA114_0 = input.LA(1);

            if ( (LA114_0==RULE_DOC) ) {
                int LA114_1 = input.LA(2);

                if ( (LA114_1==74) ) {
                    alt114=1;
                }
            }
            switch (alt114) {
                case 1 :
                    // InternalCqrsDsl.g:4249:4: (lv_noKey_15_0= ruleNoKey )
                    {
                    // InternalCqrsDsl.g:4249:4: (lv_noKey_15_0= ruleNoKey )
                    // InternalCqrsDsl.g:4250:5: lv_noKey_15_0= ruleNoKey
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getEntityAccess().getNoKeyNoKeyParserRuleCall_13_0());
                      				
                    }
                    pushFollow(FOLLOW_107);
                    lv_noKey_15_0=ruleNoKey();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getEntityRule());
                      					}
                      					set(
                      						current,
                      						"noKey",
                      						lv_noKey_15_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.NoKey");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4267:3: ( (lv_constructors_16_0= ruleConstructor ) )*
            loop115:
            do {
                int alt115=2;
                int LA115_0 = input.LA(1);

                if ( (LA115_0==RULE_DOC) ) {
                    int LA115_1 = input.LA(2);

                    if ( (LA115_1==75) ) {
                        alt115=1;
                    }


                }
                else if ( (LA115_0==75) ) {
                    alt115=1;
                }


                switch (alt115) {
            	case 1 :
            	    // InternalCqrsDsl.g:4268:4: (lv_constructors_16_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:4268:4: (lv_constructors_16_0= ruleConstructor )
            	    // InternalCqrsDsl.g:4269:5: lv_constructors_16_0= ruleConstructor
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityAccess().getConstructorsConstructorParserRuleCall_14_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_107);
            	    lv_constructors_16_0=ruleConstructor();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityRule());
            	      					}
            	      					add(
            	      						current,
            	      						"constructors",
            	      						lv_constructors_16_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop115;
                }
            } while (true);

            // InternalCqrsDsl.g:4286:3: ( (lv_methods_17_0= ruleMethod ) )*
            loop116:
            do {
                int alt116=2;
                int LA116_0 = input.LA(1);

                if ( (LA116_0==RULE_DOC) ) {
                    int LA116_1 = input.LA(2);

                    if ( (LA116_1==80) ) {
                        alt116=1;
                    }


                }
                else if ( (LA116_0==80) ) {
                    alt116=1;
                }


                switch (alt116) {
            	case 1 :
            	    // InternalCqrsDsl.g:4287:4: (lv_methods_17_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:4287:4: (lv_methods_17_0= ruleMethod )
            	    // InternalCqrsDsl.g:4288:5: lv_methods_17_0= ruleMethod
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityAccess().getMethodsMethodParserRuleCall_15_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_108);
            	    lv_methods_17_0=ruleMethod();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityRule());
            	      					}
            	      					add(
            	      						current,
            	      						"methods",
            	      						lv_methods_17_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop116;
                }
            } while (true);

            // InternalCqrsDsl.g:4305:3: ( (lv_elements_18_0= ruleEntityElement ) )*
            loop117:
            do {
                int alt117=2;
                int LA117_0 = input.LA(1);

                if ( (LA117_0==RULE_DOC||LA117_0==21||LA117_0==28||LA117_0==38||LA117_0==41||LA117_0==53||LA117_0==55||LA117_0==58||(LA117_0>=60 && LA117_0<=61)||LA117_0==64||LA117_0==66||LA117_0==69||(LA117_0>=94 && LA117_0<=96)||LA117_0==99||(LA117_0>=102 && LA117_0<=103)||LA117_0==105) ) {
                    alt117=1;
                }


                switch (alt117) {
            	case 1 :
            	    // InternalCqrsDsl.g:4306:4: (lv_elements_18_0= ruleEntityElement )
            	    {
            	    // InternalCqrsDsl.g:4306:4: (lv_elements_18_0= ruleEntityElement )
            	    // InternalCqrsDsl.g:4307:5: lv_elements_18_0= ruleEntityElement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getEntityAccess().getElementsEntityElementParserRuleCall_16_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_14);
            	    lv_elements_18_0=ruleEntityElement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getEntityRule());
            	      					}
            	      					add(
            	      						current,
            	      						"elements",
            	      						lv_elements_18_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.EntityElement");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop117;
                }
            } while (true);

            otherlv_19=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_19, grammarAccess.getEntityAccess().getRightCurlyBracketKeyword_17());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEntity"


    // $ANTLR start "entryRuleAggregate"
    // InternalCqrsDsl.g:4332:1: entryRuleAggregate returns [EObject current=null] : iv_ruleAggregate= ruleAggregate EOF ;
    public final EObject entryRuleAggregate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregate = null;


        try {
            // InternalCqrsDsl.g:4332:50: (iv_ruleAggregate= ruleAggregate EOF )
            // InternalCqrsDsl.g:4333:2: iv_ruleAggregate= ruleAggregate EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAggregateRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAggregate=ruleAggregate();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAggregate; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAggregate"


    // $ANTLR start "ruleAggregate"
    // InternalCqrsDsl.g:4339:1: ruleAggregate returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_businessRules_11_0= ruleBusinessRule ) )* ( (lv_keys_12_0= ruleKey ) )* ( (lv_noKey_13_0= ruleNoKey ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* ( (lv_elements_16_0= ruleEntityElement ) )* otherlv_17= '}' ) ;
    public final EObject ruleAggregate() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_7=null;
        Token otherlv_17=null;
        EObject lv_invariants_5_0 = null;

        EObject lv_dataProtection_6_0 = null;

        EObject lv_metaInfo_8_0 = null;

        EObject lv_hints_9_0 = null;

        EObject lv_attributes_10_0 = null;

        EObject lv_businessRules_11_0 = null;

        EObject lv_keys_12_0 = null;

        EObject lv_noKey_13_0 = null;

        EObject lv_constructors_14_0 = null;

        EObject lv_methods_15_0 = null;

        EObject lv_elements_16_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4345:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_businessRules_11_0= ruleBusinessRule ) )* ( (lv_keys_12_0= ruleKey ) )* ( (lv_noKey_13_0= ruleNoKey ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* ( (lv_elements_16_0= ruleEntityElement ) )* otherlv_17= '}' ) )
            // InternalCqrsDsl.g:4346:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_businessRules_11_0= ruleBusinessRule ) )* ( (lv_keys_12_0= ruleKey ) )* ( (lv_noKey_13_0= ruleNoKey ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* ( (lv_elements_16_0= ruleEntityElement ) )* otherlv_17= '}' )
            {
            // InternalCqrsDsl.g:4346:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_businessRules_11_0= ruleBusinessRule ) )* ( (lv_keys_12_0= ruleKey ) )* ( (lv_noKey_13_0= ruleNoKey ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* ( (lv_elements_16_0= ruleEntityElement ) )* otherlv_17= '}' )
            // InternalCqrsDsl.g:4347:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_businessRules_11_0= ruleBusinessRule ) )* ( (lv_keys_12_0= ruleKey ) )* ( (lv_noKey_13_0= ruleNoKey ) )? ( (lv_constructors_14_0= ruleConstructor ) )* ( (lv_methods_15_0= ruleMethod ) )* ( (lv_elements_16_0= ruleEntityElement ) )* otherlv_17= '}'
            {
            // InternalCqrsDsl.g:4347:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt118=2;
            int LA118_0 = input.LA(1);

            if ( (LA118_0==RULE_DOC) ) {
                alt118=1;
            }
            switch (alt118) {
                case 1 :
                    // InternalCqrsDsl.g:4348:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4348:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4349:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_109); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getAggregateAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getAggregateRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,69,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getAggregateAccess().getAggregateKeyword_1());
              		
            }
            // InternalCqrsDsl.g:4369:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:4370:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4370:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:4371:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_110); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getAggregateAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getAggregateRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:4387:3: (otherlv_3= 'identifier' ( ( ruleFQN ) ) )?
            int alt119=2;
            int LA119_0 = input.LA(1);

            if ( (LA119_0==67) ) {
                alt119=1;
            }
            switch (alt119) {
                case 1 :
                    // InternalCqrsDsl.g:4388:4: otherlv_3= 'identifier' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,67,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getAggregateAccess().getIdentifierKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:4392:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:4393:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4393:5: ( ruleFQN )
                    // InternalCqrsDsl.g:4394:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getAggregateRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getAggregateAccess().getIdTypeAggregateIdCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_76);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4409:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt120=2;
            int LA120_0 = input.LA(1);

            if ( (LA120_0==90) ) {
                alt120=1;
            }
            switch (alt120) {
                case 1 :
                    // InternalCqrsDsl.g:4410:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:4410:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:4411:5: lv_invariants_5_0= ruleInvariants
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAggregateAccess().getInvariantsInvariantsParserRuleCall_4_0());
                      				
                    }
                    pushFollow(FOLLOW_77);
                    lv_invariants_5_0=ruleInvariants();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAggregateRule());
                      					}
                      					set(
                      						current,
                      						"invariants",
                      						lv_invariants_5_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Invariants");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4428:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt121=2;
            int LA121_0 = input.LA(1);

            if ( (LA121_0==37) ) {
                alt121=1;
            }
            switch (alt121) {
                case 1 :
                    // InternalCqrsDsl.g:4429:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:4429:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:4430:5: lv_dataProtection_6_0= ruleDataProtectionInstance
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAggregateAccess().getDataProtectionDataProtectionInstanceParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_6_0=ruleDataProtectionInstance();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAggregateRule());
                      					}
                      					set(
                      						current,
                      						"dataProtection",
                      						lv_dataProtection_6_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DataProtectionInstance");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_7=(Token)match(input,14,FOLLOW_104); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getAggregateAccess().getLeftCurlyBracketKeyword_6());
              		
            }
            // InternalCqrsDsl.g:4451:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:4452:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:4452:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:4453:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getAggregateAccess().getMetaInfoTypeMetaInfoParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_105);
            lv_metaInfo_8_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getAggregateRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_8_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:4470:3: ( (lv_hints_9_0= ruleHint ) )*
            loop122:
            do {
                int alt122=2;
                int LA122_0 = input.LA(1);

                if ( (LA122_0==RULE_DOC) ) {
                    int LA122_1 = input.LA(2);

                    if ( (LA122_1==20) ) {
                        alt122=1;
                    }


                }
                else if ( (LA122_0==20) ) {
                    alt122=1;
                }


                switch (alt122) {
            	case 1 :
            	    // InternalCqrsDsl.g:4471:4: (lv_hints_9_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:4471:4: (lv_hints_9_0= ruleHint )
            	    // InternalCqrsDsl.g:4472:5: lv_hints_9_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateAccess().getHintsHintParserRuleCall_8_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_105);
            	    lv_hints_9_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_9_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop122;
                }
            } while (true);

            // InternalCqrsDsl.g:4489:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop123:
            do {
                int alt123=2;
                int LA123_0 = input.LA(1);

                if ( (LA123_0==RULE_DOC) ) {
                    int LA123_1 = input.LA(2);

                    if ( (LA123_1==RULE_ID||LA123_1==79) ) {
                        alt123=1;
                    }


                }
                else if ( (LA123_0==RULE_ID||LA123_0==79) ) {
                    alt123=1;
                }


                switch (alt123) {
            	case 1 :
            	    // InternalCqrsDsl.g:4490:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:4490:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:4491:5: lv_attributes_10_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateAccess().getAttributesAttributeParserRuleCall_9_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_106);
            	    lv_attributes_10_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_10_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop123;
                }
            } while (true);

            // InternalCqrsDsl.g:4508:3: ( (lv_businessRules_11_0= ruleBusinessRule ) )*
            loop124:
            do {
                int alt124=2;
                int LA124_0 = input.LA(1);

                if ( (LA124_0==RULE_DOC) ) {
                    int LA124_1 = input.LA(2);

                    if ( (LA124_1==43) ) {
                        alt124=1;
                    }


                }


                switch (alt124) {
            	case 1 :
            	    // InternalCqrsDsl.g:4509:4: (lv_businessRules_11_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:4509:4: (lv_businessRules_11_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:4510:5: lv_businessRules_11_0= ruleBusinessRule
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateAccess().getBusinessRulesBusinessRuleParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_107);
            	    lv_businessRules_11_0=ruleBusinessRule();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	      					}
            	      					add(
            	      						current,
            	      						"businessRules",
            	      						lv_businessRules_11_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRule");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop124;
                }
            } while (true);

            // InternalCqrsDsl.g:4527:3: ( (lv_keys_12_0= ruleKey ) )*
            loop125:
            do {
                int alt125=2;
                int LA125_0 = input.LA(1);

                if ( (LA125_0==RULE_DOC) ) {
                    int LA125_1 = input.LA(2);

                    if ( (LA125_1==70) ) {
                        alt125=1;
                    }


                }


                switch (alt125) {
            	case 1 :
            	    // InternalCqrsDsl.g:4528:4: (lv_keys_12_0= ruleKey )
            	    {
            	    // InternalCqrsDsl.g:4528:4: (lv_keys_12_0= ruleKey )
            	    // InternalCqrsDsl.g:4529:5: lv_keys_12_0= ruleKey
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateAccess().getKeysKeyParserRuleCall_11_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_107);
            	    lv_keys_12_0=ruleKey();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	      					}
            	      					add(
            	      						current,
            	      						"keys",
            	      						lv_keys_12_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Key");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop125;
                }
            } while (true);

            // InternalCqrsDsl.g:4546:3: ( (lv_noKey_13_0= ruleNoKey ) )?
            int alt126=2;
            int LA126_0 = input.LA(1);

            if ( (LA126_0==RULE_DOC) ) {
                int LA126_1 = input.LA(2);

                if ( (LA126_1==74) ) {
                    alt126=1;
                }
            }
            switch (alt126) {
                case 1 :
                    // InternalCqrsDsl.g:4547:4: (lv_noKey_13_0= ruleNoKey )
                    {
                    // InternalCqrsDsl.g:4547:4: (lv_noKey_13_0= ruleNoKey )
                    // InternalCqrsDsl.g:4548:5: lv_noKey_13_0= ruleNoKey
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAggregateAccess().getNoKeyNoKeyParserRuleCall_12_0());
                      				
                    }
                    pushFollow(FOLLOW_107);
                    lv_noKey_13_0=ruleNoKey();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAggregateRule());
                      					}
                      					set(
                      						current,
                      						"noKey",
                      						lv_noKey_13_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.NoKey");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4565:3: ( (lv_constructors_14_0= ruleConstructor ) )*
            loop127:
            do {
                int alt127=2;
                int LA127_0 = input.LA(1);

                if ( (LA127_0==RULE_DOC) ) {
                    int LA127_1 = input.LA(2);

                    if ( (LA127_1==75) ) {
                        alt127=1;
                    }


                }
                else if ( (LA127_0==75) ) {
                    alt127=1;
                }


                switch (alt127) {
            	case 1 :
            	    // InternalCqrsDsl.g:4566:4: (lv_constructors_14_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:4566:4: (lv_constructors_14_0= ruleConstructor )
            	    // InternalCqrsDsl.g:4567:5: lv_constructors_14_0= ruleConstructor
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateAccess().getConstructorsConstructorParserRuleCall_13_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_107);
            	    lv_constructors_14_0=ruleConstructor();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	      					}
            	      					add(
            	      						current,
            	      						"constructors",
            	      						lv_constructors_14_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop127;
                }
            } while (true);

            // InternalCqrsDsl.g:4584:3: ( (lv_methods_15_0= ruleMethod ) )*
            loop128:
            do {
                int alt128=2;
                int LA128_0 = input.LA(1);

                if ( (LA128_0==RULE_DOC) ) {
                    int LA128_1 = input.LA(2);

                    if ( (LA128_1==80) ) {
                        alt128=1;
                    }


                }
                else if ( (LA128_0==80) ) {
                    alt128=1;
                }


                switch (alt128) {
            	case 1 :
            	    // InternalCqrsDsl.g:4585:4: (lv_methods_15_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:4585:4: (lv_methods_15_0= ruleMethod )
            	    // InternalCqrsDsl.g:4586:5: lv_methods_15_0= ruleMethod
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateAccess().getMethodsMethodParserRuleCall_14_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_108);
            	    lv_methods_15_0=ruleMethod();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	      					}
            	      					add(
            	      						current,
            	      						"methods",
            	      						lv_methods_15_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop128;
                }
            } while (true);

            // InternalCqrsDsl.g:4603:3: ( (lv_elements_16_0= ruleEntityElement ) )*
            loop129:
            do {
                int alt129=2;
                int LA129_0 = input.LA(1);

                if ( (LA129_0==RULE_DOC||LA129_0==21||LA129_0==28||LA129_0==38||LA129_0==41||LA129_0==53||LA129_0==55||LA129_0==58||(LA129_0>=60 && LA129_0<=61)||LA129_0==64||LA129_0==66||LA129_0==69||(LA129_0>=94 && LA129_0<=96)||LA129_0==99||(LA129_0>=102 && LA129_0<=103)||LA129_0==105) ) {
                    alt129=1;
                }


                switch (alt129) {
            	case 1 :
            	    // InternalCqrsDsl.g:4604:4: (lv_elements_16_0= ruleEntityElement )
            	    {
            	    // InternalCqrsDsl.g:4604:4: (lv_elements_16_0= ruleEntityElement )
            	    // InternalCqrsDsl.g:4605:5: lv_elements_16_0= ruleEntityElement
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getAggregateAccess().getElementsEntityElementParserRuleCall_15_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_14);
            	    lv_elements_16_0=ruleEntityElement();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	      					}
            	      					add(
            	      						current,
            	      						"elements",
            	      						lv_elements_16_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.EntityElement");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop129;
                }
            } while (true);

            otherlv_17=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_17, grammarAccess.getAggregateAccess().getRightCurlyBracketKeyword_16());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAggregate"


    // $ANTLR start "entryRuleKey"
    // InternalCqrsDsl.g:4630:1: entryRuleKey returns [EObject current=null] : iv_ruleKey= ruleKey EOF ;
    public final EObject entryRuleKey() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleKey = null;


        try {
            // InternalCqrsDsl.g:4630:44: (iv_ruleKey= ruleKey EOF )
            // InternalCqrsDsl.g:4631:2: iv_ruleKey= ruleKey EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getKeyRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleKey=ruleKey();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleKey; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleKey"


    // $ANTLR start "ruleKey"
    // InternalCqrsDsl.g:4637:1: ruleKey returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'key' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'exception' ( ( ruleFQN ) ) )? otherlv_5= '{' otherlv_6= 'attributes' ( (otherlv_7= RULE_ID ) ) (otherlv_8= ',' ( (otherlv_9= RULE_ID ) ) )* otherlv_10= 'on-collision' ( (lv_onCollision_11_0= ruleCollisionStrategy ) ) ( (lv_consistency_12_0= ruleConsistency ) ) (otherlv_13= 'display-as' ( (lv_displayAs_14_0= RULE_STRING ) ) )? otherlv_15= '}' ) ;
    public final EObject ruleKey() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_10=null;
        Token otherlv_13=null;
        Token lv_displayAs_14_0=null;
        Token otherlv_15=null;
        Enumerator lv_onCollision_11_0 = null;

        EObject lv_consistency_12_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4643:2: ( ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'key' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'exception' ( ( ruleFQN ) ) )? otherlv_5= '{' otherlv_6= 'attributes' ( (otherlv_7= RULE_ID ) ) (otherlv_8= ',' ( (otherlv_9= RULE_ID ) ) )* otherlv_10= 'on-collision' ( (lv_onCollision_11_0= ruleCollisionStrategy ) ) ( (lv_consistency_12_0= ruleConsistency ) ) (otherlv_13= 'display-as' ( (lv_displayAs_14_0= RULE_STRING ) ) )? otherlv_15= '}' ) )
            // InternalCqrsDsl.g:4644:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'key' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'exception' ( ( ruleFQN ) ) )? otherlv_5= '{' otherlv_6= 'attributes' ( (otherlv_7= RULE_ID ) ) (otherlv_8= ',' ( (otherlv_9= RULE_ID ) ) )* otherlv_10= 'on-collision' ( (lv_onCollision_11_0= ruleCollisionStrategy ) ) ( (lv_consistency_12_0= ruleConsistency ) ) (otherlv_13= 'display-as' ( (lv_displayAs_14_0= RULE_STRING ) ) )? otherlv_15= '}' )
            {
            // InternalCqrsDsl.g:4644:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'key' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'exception' ( ( ruleFQN ) ) )? otherlv_5= '{' otherlv_6= 'attributes' ( (otherlv_7= RULE_ID ) ) (otherlv_8= ',' ( (otherlv_9= RULE_ID ) ) )* otherlv_10= 'on-collision' ( (lv_onCollision_11_0= ruleCollisionStrategy ) ) ( (lv_consistency_12_0= ruleConsistency ) ) (otherlv_13= 'display-as' ( (lv_displayAs_14_0= RULE_STRING ) ) )? otherlv_15= '}' )
            // InternalCqrsDsl.g:4645:3: ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'key' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'exception' ( ( ruleFQN ) ) )? otherlv_5= '{' otherlv_6= 'attributes' ( (otherlv_7= RULE_ID ) ) (otherlv_8= ',' ( (otherlv_9= RULE_ID ) ) )* otherlv_10= 'on-collision' ( (lv_onCollision_11_0= ruleCollisionStrategy ) ) ( (lv_consistency_12_0= ruleConsistency ) ) (otherlv_13= 'display-as' ( (lv_displayAs_14_0= RULE_STRING ) ) )? otherlv_15= '}'
            {
            // InternalCqrsDsl.g:4645:3: ( (lv_doc_0_0= RULE_DOC ) )
            // InternalCqrsDsl.g:4646:4: (lv_doc_0_0= RULE_DOC )
            {
            // InternalCqrsDsl.g:4646:4: (lv_doc_0_0= RULE_DOC )
            // InternalCqrsDsl.g:4647:5: lv_doc_0_0= RULE_DOC
            {
            lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_111); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_doc_0_0, grammarAccess.getKeyAccess().getDocDOCTerminalRuleCall_0_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getKeyRule());
              					}
              					setWithLastConsumed(
              						current,
              						"doc",
              						lv_doc_0_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
              				
            }

            }


            }

            otherlv_1=(Token)match(input,70,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getKeyAccess().getKeyKeyword_1());
              		
            }
            // InternalCqrsDsl.g:4667:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:4668:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4668:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:4669:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_112); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getKeyAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getKeyRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:4685:3: (otherlv_3= 'exception' ( ( ruleFQN ) ) )?
            int alt130=2;
            int LA130_0 = input.LA(1);

            if ( (LA130_0==41) ) {
                alt130=1;
            }
            switch (alt130) {
                case 1 :
                    // InternalCqrsDsl.g:4686:4: otherlv_3= 'exception' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,41,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getKeyAccess().getExceptionKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:4690:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:4691:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4691:5: ( ruleFQN )
                    // InternalCqrsDsl.g:4692:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getKeyRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getKeyAccess().getExceptionExceptionCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_5);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_5=(Token)match(input,14,FOLLOW_113); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getKeyAccess().getLeftCurlyBracketKeyword_4());
              		
            }
            otherlv_6=(Token)match(input,71,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getKeyAccess().getAttributesKeyword_5());
              		
            }
            // InternalCqrsDsl.g:4715:3: ( (otherlv_7= RULE_ID ) )
            // InternalCqrsDsl.g:4716:4: (otherlv_7= RULE_ID )
            {
            // InternalCqrsDsl.g:4716:4: (otherlv_7= RULE_ID )
            // InternalCqrsDsl.g:4717:5: otherlv_7= RULE_ID
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getKeyRule());
              					}
              				
            }
            otherlv_7=(Token)match(input,RULE_ID,FOLLOW_114); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(otherlv_7, grammarAccess.getKeyAccess().getAttributesAttributeCrossReference_6_0());
              				
            }

            }


            }

            // InternalCqrsDsl.g:4728:3: (otherlv_8= ',' ( (otherlv_9= RULE_ID ) ) )*
            loop131:
            do {
                int alt131=2;
                int LA131_0 = input.LA(1);

                if ( (LA131_0==31) ) {
                    alt131=1;
                }


                switch (alt131) {
            	case 1 :
            	    // InternalCqrsDsl.g:4729:4: otherlv_8= ',' ( (otherlv_9= RULE_ID ) )
            	    {
            	    otherlv_8=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_8, grammarAccess.getKeyAccess().getCommaKeyword_7_0());
            	      			
            	    }
            	    // InternalCqrsDsl.g:4733:4: ( (otherlv_9= RULE_ID ) )
            	    // InternalCqrsDsl.g:4734:5: (otherlv_9= RULE_ID )
            	    {
            	    // InternalCqrsDsl.g:4734:5: (otherlv_9= RULE_ID )
            	    // InternalCqrsDsl.g:4735:6: otherlv_9= RULE_ID
            	    {
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElement(grammarAccess.getKeyRule());
            	      						}
            	      					
            	    }
            	    otherlv_9=(Token)match(input,RULE_ID,FOLLOW_114); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						newLeafNode(otherlv_9, grammarAccess.getKeyAccess().getAttributesAttributeCrossReference_7_1_0());
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop131;
                }
            } while (true);

            otherlv_10=(Token)match(input,72,FOLLOW_115); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_10, grammarAccess.getKeyAccess().getOnCollisionKeyword_8());
              		
            }
            // InternalCqrsDsl.g:4751:3: ( (lv_onCollision_11_0= ruleCollisionStrategy ) )
            // InternalCqrsDsl.g:4752:4: (lv_onCollision_11_0= ruleCollisionStrategy )
            {
            // InternalCqrsDsl.g:4752:4: (lv_onCollision_11_0= ruleCollisionStrategy )
            // InternalCqrsDsl.g:4753:5: lv_onCollision_11_0= ruleCollisionStrategy
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getKeyAccess().getOnCollisionCollisionStrategyEnumRuleCall_9_0());
              				
            }
            pushFollow(FOLLOW_60);
            lv_onCollision_11_0=ruleCollisionStrategy();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getKeyRule());
              					}
              					set(
              						current,
              						"onCollision",
              						lv_onCollision_11_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.CollisionStrategy");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:4770:3: ( (lv_consistency_12_0= ruleConsistency ) )
            // InternalCqrsDsl.g:4771:4: (lv_consistency_12_0= ruleConsistency )
            {
            // InternalCqrsDsl.g:4771:4: (lv_consistency_12_0= ruleConsistency )
            // InternalCqrsDsl.g:4772:5: lv_consistency_12_0= ruleConsistency
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getKeyAccess().getConsistencyConsistencyParserRuleCall_10_0());
              				
            }
            pushFollow(FOLLOW_116);
            lv_consistency_12_0=ruleConsistency();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getKeyRule());
              					}
              					set(
              						current,
              						"consistency",
              						lv_consistency_12_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.Consistency");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:4789:3: (otherlv_13= 'display-as' ( (lv_displayAs_14_0= RULE_STRING ) ) )?
            int alt132=2;
            int LA132_0 = input.LA(1);

            if ( (LA132_0==73) ) {
                alt132=1;
            }
            switch (alt132) {
                case 1 :
                    // InternalCqrsDsl.g:4790:4: otherlv_13= 'display-as' ( (lv_displayAs_14_0= RULE_STRING ) )
                    {
                    otherlv_13=(Token)match(input,73,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_13, grammarAccess.getKeyAccess().getDisplayAsKeyword_11_0());
                      			
                    }
                    // InternalCqrsDsl.g:4794:4: ( (lv_displayAs_14_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4795:5: (lv_displayAs_14_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4795:5: (lv_displayAs_14_0= RULE_STRING )
                    // InternalCqrsDsl.g:4796:6: lv_displayAs_14_0= RULE_STRING
                    {
                    lv_displayAs_14_0=(Token)match(input,RULE_STRING,FOLLOW_35); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_displayAs_14_0, grammarAccess.getKeyAccess().getDisplayAsSTRINGTerminalRuleCall_11_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getKeyRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"displayAs",
                      							lv_displayAs_14_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_15=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_15, grammarAccess.getKeyAccess().getRightCurlyBracketKeyword_12());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleKey"


    // $ANTLR start "entryRuleNoKey"
    // InternalCqrsDsl.g:4821:1: entryRuleNoKey returns [EObject current=null] : iv_ruleNoKey= ruleNoKey EOF ;
    public final EObject entryRuleNoKey() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNoKey = null;


        try {
            // InternalCqrsDsl.g:4821:46: (iv_ruleNoKey= ruleNoKey EOF )
            // InternalCqrsDsl.g:4822:2: iv_ruleNoKey= ruleNoKey EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getNoKeyRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleNoKey=ruleNoKey();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleNoKey; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNoKey"


    // $ANTLR start "ruleNoKey"
    // InternalCqrsDsl.g:4828:1: ruleNoKey returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'no-key' ) ;
    public final EObject ruleNoKey() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:4834:2: ( ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'no-key' ) )
            // InternalCqrsDsl.g:4835:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'no-key' )
            {
            // InternalCqrsDsl.g:4835:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'no-key' )
            // InternalCqrsDsl.g:4836:3: ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'no-key'
            {
            // InternalCqrsDsl.g:4836:3: ( (lv_doc_0_0= RULE_DOC ) )
            // InternalCqrsDsl.g:4837:4: (lv_doc_0_0= RULE_DOC )
            {
            // InternalCqrsDsl.g:4837:4: (lv_doc_0_0= RULE_DOC )
            // InternalCqrsDsl.g:4838:5: lv_doc_0_0= RULE_DOC
            {
            lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_117); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_doc_0_0, grammarAccess.getNoKeyAccess().getDocDOCTerminalRuleCall_0_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getNoKeyRule());
              					}
              					setWithLastConsumed(
              						current,
              						"doc",
              						lv_doc_0_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
              				
            }

            }


            }

            otherlv_1=(Token)match(input,74,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getNoKeyAccess().getNoKeyKeyword_1());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNoKey"


    // $ANTLR start "entryRuleConstructor"
    // InternalCqrsDsl.g:4862:1: entryRuleConstructor returns [EObject current=null] : iv_ruleConstructor= ruleConstructor EOF ;
    public final EObject entryRuleConstructor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstructor = null;


        try {
            // InternalCqrsDsl.g:4862:52: (iv_ruleConstructor= ruleConstructor EOF )
            // InternalCqrsDsl.g:4863:2: iv_ruleConstructor= ruleConstructor EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getConstructorRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleConstructor=ruleConstructor();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleConstructor; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConstructor"


    // $ANTLR start "ruleConstructor"
    // InternalCqrsDsl.g:4869:1: ruleConstructor returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* (otherlv_11= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_services_13_0= ruleService ) )* ( (lv_events_14_0= ruleEvent ) )* otherlv_15= '}' ) ;
    public final EObject ruleConstructor() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_15=null;
        EObject lv_preconditions_3_0 = null;

        EObject lv_businessRules_4_0 = null;

        EObject lv_parameters_10_0 = null;

        EObject lv_services_13_0 = null;

        EObject lv_events_14_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4875:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* (otherlv_11= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_services_13_0= ruleService ) )* ( (lv_events_14_0= ruleEvent ) )* otherlv_15= '}' ) )
            // InternalCqrsDsl.g:4876:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* (otherlv_11= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_services_13_0= ruleService ) )* ( (lv_events_14_0= ruleEvent ) )* otherlv_15= '}' )
            {
            // InternalCqrsDsl.g:4876:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* (otherlv_11= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_services_13_0= ruleService ) )* ( (lv_events_14_0= ruleEvent ) )* otherlv_15= '}' )
            // InternalCqrsDsl.g:4877:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* (otherlv_11= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_services_13_0= ruleService ) )* ( (lv_events_14_0= ruleEvent ) )* otherlv_15= '}'
            {
            // InternalCqrsDsl.g:4877:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt133=2;
            int LA133_0 = input.LA(1);

            if ( (LA133_0==RULE_DOC) ) {
                alt133=1;
            }
            switch (alt133) {
                case 1 :
                    // InternalCqrsDsl.g:4878:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4878:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4879:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_118); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getConstructorAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getConstructorRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,75,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getConstructorAccess().getConstructorKeyword_1());
              		
            }
            // InternalCqrsDsl.g:4899:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:4900:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4900:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:4901:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_119); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getConstructorAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getConstructorRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:4917:3: ( (lv_preconditions_3_0= rulePreconditions ) )?
            int alt134=2;
            int LA134_0 = input.LA(1);

            if ( (LA134_0==91) ) {
                alt134=1;
            }
            switch (alt134) {
                case 1 :
                    // InternalCqrsDsl.g:4918:4: (lv_preconditions_3_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:4918:4: (lv_preconditions_3_0= rulePreconditions )
                    // InternalCqrsDsl.g:4919:5: lv_preconditions_3_0= rulePreconditions
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getConstructorAccess().getPreconditionsPreconditionsParserRuleCall_3_0());
                      				
                    }
                    pushFollow(FOLLOW_120);
                    lv_preconditions_3_0=rulePreconditions();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getConstructorRule());
                      					}
                      					set(
                      						current,
                      						"preconditions",
                      						lv_preconditions_3_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Preconditions");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4936:3: ( (lv_businessRules_4_0= ruleBusinessRules ) )?
            int alt135=2;
            int LA135_0 = input.LA(1);

            if ( (LA135_0==92) ) {
                alt135=1;
            }
            switch (alt135) {
                case 1 :
                    // InternalCqrsDsl.g:4937:4: (lv_businessRules_4_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:4937:4: (lv_businessRules_4_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:4938:5: lv_businessRules_4_0= ruleBusinessRules
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getConstructorAccess().getBusinessRulesBusinessRulesParserRuleCall_4_0());
                      				
                    }
                    pushFollow(FOLLOW_121);
                    lv_businessRules_4_0=ruleBusinessRules();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getConstructorRule());
                      					}
                      					set(
                      						current,
                      						"businessRules",
                      						lv_businessRules_4_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRules");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4955:3: (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )?
            int alt137=2;
            int LA137_0 = input.LA(1);

            if ( (LA137_0==76) ) {
                alt137=1;
            }
            switch (alt137) {
                case 1 :
                    // InternalCqrsDsl.g:4956:4: otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_5=(Token)match(input,76,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getConstructorAccess().getFiresKeyword_5_0());
                      			
                    }
                    // InternalCqrsDsl.g:4960:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:4961:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4961:5: ( ruleFQN )
                    // InternalCqrsDsl.g:4962:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getConstructorRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getConstructorAccess().getFiredEventsEventCrossReference_5_1_0());
                      					
                    }
                    pushFollow(FOLLOW_122);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:4976:4: (otherlv_7= ',' ( ( ruleFQN ) ) )*
                    loop136:
                    do {
                        int alt136=2;
                        int LA136_0 = input.LA(1);

                        if ( (LA136_0==31) ) {
                            alt136=1;
                        }


                        switch (alt136) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:4977:5: otherlv_7= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_7=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_7, grammarAccess.getConstructorAccess().getCommaKeyword_5_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:4981:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:4982:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:4982:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:4983:7: ruleFQN
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElement(grammarAccess.getConstructorRule());
                    	      							}
                    	      						
                    	    }
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getConstructorAccess().getFiredEventsEventCrossReference_5_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_122);
                    	    ruleFQN();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop136;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_123); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_9, grammarAccess.getConstructorAccess().getLeftCurlyBracketKeyword_6());
              		
            }
            // InternalCqrsDsl.g:5003:3: ( (lv_parameters_10_0= ruleParameter ) )*
            loop138:
            do {
                int alt138=2;
                int LA138_0 = input.LA(1);

                if ( (LA138_0==RULE_DOC) ) {
                    int LA138_2 = input.LA(2);

                    if ( (LA138_2==RULE_ID||LA138_2==79) ) {
                        alt138=1;
                    }


                }
                else if ( (LA138_0==RULE_ID||LA138_0==79) ) {
                    alt138=1;
                }


                switch (alt138) {
            	case 1 :
            	    // InternalCqrsDsl.g:5004:4: (lv_parameters_10_0= ruleParameter )
            	    {
            	    // InternalCqrsDsl.g:5004:4: (lv_parameters_10_0= ruleParameter )
            	    // InternalCqrsDsl.g:5005:5: lv_parameters_10_0= ruleParameter
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getConstructorAccess().getParametersParameterParserRuleCall_7_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_123);
            	    lv_parameters_10_0=ruleParameter();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getConstructorRule());
            	      					}
            	      					add(
            	      						current,
            	      						"parameters",
            	      						lv_parameters_10_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Parameter");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop138;
                }
            } while (true);

            // InternalCqrsDsl.g:5022:3: (otherlv_11= 'operation-context' ( ( ruleFQN ) ) )?
            int alt139=2;
            int LA139_0 = input.LA(1);

            if ( (LA139_0==77) ) {
                alt139=1;
            }
            switch (alt139) {
                case 1 :
                    // InternalCqrsDsl.g:5023:4: otherlv_11= 'operation-context' ( ( ruleFQN ) )
                    {
                    otherlv_11=(Token)match(input,77,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getConstructorAccess().getOperationContextKeyword_8_0());
                      			
                    }
                    // InternalCqrsDsl.g:5027:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5028:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5028:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5029:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getConstructorRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getConstructorAccess().getOperationContextServiceCrossReference_8_1_0());
                      					
                    }
                    pushFollow(FOLLOW_124);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5044:3: ( (lv_services_13_0= ruleService ) )*
            loop140:
            do {
                int alt140=2;
                int LA140_0 = input.LA(1);

                if ( (LA140_0==RULE_DOC) ) {
                    int LA140_1 = input.LA(2);

                    if ( (LA140_1==95) ) {
                        alt140=1;
                    }


                }
                else if ( (LA140_0==95) ) {
                    alt140=1;
                }


                switch (alt140) {
            	case 1 :
            	    // InternalCqrsDsl.g:5045:4: (lv_services_13_0= ruleService )
            	    {
            	    // InternalCqrsDsl.g:5045:4: (lv_services_13_0= ruleService )
            	    // InternalCqrsDsl.g:5046:5: lv_services_13_0= ruleService
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getConstructorAccess().getServicesServiceParserRuleCall_9_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_124);
            	    lv_services_13_0=ruleService();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getConstructorRule());
            	      					}
            	      					add(
            	      						current,
            	      						"services",
            	      						lv_services_13_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Service");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop140;
                }
            } while (true);

            // InternalCqrsDsl.g:5063:3: ( (lv_events_14_0= ruleEvent ) )*
            loop141:
            do {
                int alt141=2;
                int LA141_0 = input.LA(1);

                if ( (LA141_0==RULE_DOC||LA141_0==64||LA141_0==94) ) {
                    alt141=1;
                }


                switch (alt141) {
            	case 1 :
            	    // InternalCqrsDsl.g:5064:4: (lv_events_14_0= ruleEvent )
            	    {
            	    // InternalCqrsDsl.g:5064:4: (lv_events_14_0= ruleEvent )
            	    // InternalCqrsDsl.g:5065:5: lv_events_14_0= ruleEvent
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getConstructorAccess().getEventsEventParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_125);
            	    lv_events_14_0=ruleEvent();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getConstructorRule());
            	      					}
            	      					add(
            	      						current,
            	      						"events",
            	      						lv_events_14_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Event");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop141;
                }
            } while (true);

            otherlv_15=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_15, grammarAccess.getConstructorAccess().getRightCurlyBracketKeyword_11());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConstructor"


    // $ANTLR start "entryRuleReturnType"
    // InternalCqrsDsl.g:5090:1: entryRuleReturnType returns [EObject current=null] : iv_ruleReturnType= ruleReturnType EOF ;
    public final EObject entryRuleReturnType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReturnType = null;


        try {
            // InternalCqrsDsl.g:5090:51: (iv_ruleReturnType= ruleReturnType EOF )
            // InternalCqrsDsl.g:5091:2: iv_ruleReturnType= ruleReturnType EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getReturnTypeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleReturnType=ruleReturnType();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleReturnType; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleReturnType"


    // $ANTLR start "ruleReturnType"
    // InternalCqrsDsl.g:5097:1: ruleReturnType returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? ) ;
    public final EObject ruleReturnType() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_optional_2_0=null;
        EObject lv_generics_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5103:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? ) )
            // InternalCqrsDsl.g:5104:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? )
            {
            // InternalCqrsDsl.g:5104:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? )
            // InternalCqrsDsl.g:5105:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )?
            {
            // InternalCqrsDsl.g:5105:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt142=2;
            int LA142_0 = input.LA(1);

            if ( (LA142_0==RULE_DOC) ) {
                alt142=1;
            }
            switch (alt142) {
                case 1 :
                    // InternalCqrsDsl.g:5106:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5106:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5107:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_126); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getReturnTypeAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getReturnTypeRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,78,FOLLOW_127); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getReturnTypeAccess().getReturnsKeyword_1());
              		
            }
            // InternalCqrsDsl.g:5127:3: ( (lv_optional_2_0= 'optional' ) )?
            int alt143=2;
            int LA143_0 = input.LA(1);

            if ( (LA143_0==79) ) {
                alt143=1;
            }
            switch (alt143) {
                case 1 :
                    // InternalCqrsDsl.g:5128:4: (lv_optional_2_0= 'optional' )
                    {
                    // InternalCqrsDsl.g:5128:4: (lv_optional_2_0= 'optional' )
                    // InternalCqrsDsl.g:5129:5: lv_optional_2_0= 'optional'
                    {
                    lv_optional_2_0=(Token)match(input,79,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_optional_2_0, grammarAccess.getReturnTypeAccess().getOptionalOptionalKeyword_2_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getReturnTypeRule());
                      					}
                      					setWithLastConsumed(current, "optional", lv_optional_2_0, "optional");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5141:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5142:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5142:4: ( ruleFQN )
            // InternalCqrsDsl.g:5143:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getReturnTypeRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getReturnTypeAccess().getTypeTypeCrossReference_3_0());
              				
            }
            pushFollow(FOLLOW_128);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:5157:3: ( (lv_generics_4_0= ruleGenericArgs ) )?
            int alt144=2;
            int LA144_0 = input.LA(1);

            if ( (LA144_0==88) ) {
                alt144=1;
            }
            switch (alt144) {
                case 1 :
                    // InternalCqrsDsl.g:5158:4: (lv_generics_4_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:5158:4: (lv_generics_4_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:5159:5: lv_generics_4_0= ruleGenericArgs
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getReturnTypeAccess().getGenericsGenericArgsParserRuleCall_4_0());
                      				
                    }
                    pushFollow(FOLLOW_2);
                    lv_generics_4_0=ruleGenericArgs();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getReturnTypeRule());
                      					}
                      					set(
                      						current,
                      						"generics",
                      						lv_generics_4_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.GenericArgs");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleReturnType"


    // $ANTLR start "entryRuleMethod"
    // InternalCqrsDsl.g:5180:1: entryRuleMethod returns [EObject current=null] : iv_ruleMethod= ruleMethod EOF ;
    public final EObject entryRuleMethod() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMethod = null;


        try {
            // InternalCqrsDsl.g:5180:47: (iv_ruleMethod= ruleMethod EOF )
            // InternalCqrsDsl.g:5181:2: iv_ruleMethod= ruleMethod EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getMethodRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleMethod=ruleMethod();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleMethod; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMethod"


    // $ANTLR start "ruleMethod"
    // InternalCqrsDsl.g:5187:1: ruleMethod returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? (otherlv_11= 'rest-path' ( (lv_restPath_12_0= RULE_STRING ) ) )? otherlv_13= '{' ( (lv_metaInfo_14_0= ruleTypeMetaInfo ) ) ( (lv_hints_15_0= ruleHint ) )* ( (lv_parameters_16_0= ruleParameter ) )* (otherlv_17= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_returnType_19_0= ruleReturnType ) )? ( (lv_services_20_0= ruleService ) )* ( (lv_events_21_0= ruleEvent ) )* otherlv_22= '}' ) ;
    public final EObject ruleMethod() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token lv_restPath_12_0=null;
        Token otherlv_13=null;
        Token otherlv_17=null;
        Token otherlv_22=null;
        EObject lv_preconditions_5_0 = null;

        EObject lv_businessRules_6_0 = null;

        EObject lv_metaInfo_14_0 = null;

        EObject lv_hints_15_0 = null;

        EObject lv_parameters_16_0 = null;

        EObject lv_returnType_19_0 = null;

        EObject lv_services_20_0 = null;

        EObject lv_events_21_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5193:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? (otherlv_11= 'rest-path' ( (lv_restPath_12_0= RULE_STRING ) ) )? otherlv_13= '{' ( (lv_metaInfo_14_0= ruleTypeMetaInfo ) ) ( (lv_hints_15_0= ruleHint ) )* ( (lv_parameters_16_0= ruleParameter ) )* (otherlv_17= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_returnType_19_0= ruleReturnType ) )? ( (lv_services_20_0= ruleService ) )* ( (lv_events_21_0= ruleEvent ) )* otherlv_22= '}' ) )
            // InternalCqrsDsl.g:5194:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? (otherlv_11= 'rest-path' ( (lv_restPath_12_0= RULE_STRING ) ) )? otherlv_13= '{' ( (lv_metaInfo_14_0= ruleTypeMetaInfo ) ) ( (lv_hints_15_0= ruleHint ) )* ( (lv_parameters_16_0= ruleParameter ) )* (otherlv_17= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_returnType_19_0= ruleReturnType ) )? ( (lv_services_20_0= ruleService ) )* ( (lv_events_21_0= ruleEvent ) )* otherlv_22= '}' )
            {
            // InternalCqrsDsl.g:5194:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? (otherlv_11= 'rest-path' ( (lv_restPath_12_0= RULE_STRING ) ) )? otherlv_13= '{' ( (lv_metaInfo_14_0= ruleTypeMetaInfo ) ) ( (lv_hints_15_0= ruleHint ) )* ( (lv_parameters_16_0= ruleParameter ) )* (otherlv_17= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_returnType_19_0= ruleReturnType ) )? ( (lv_services_20_0= ruleService ) )* ( (lv_events_21_0= ruleEvent ) )* otherlv_22= '}' )
            // InternalCqrsDsl.g:5195:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? (otherlv_11= 'rest-path' ( (lv_restPath_12_0= RULE_STRING ) ) )? otherlv_13= '{' ( (lv_metaInfo_14_0= ruleTypeMetaInfo ) ) ( (lv_hints_15_0= ruleHint ) )* ( (lv_parameters_16_0= ruleParameter ) )* (otherlv_17= 'operation-context' ( ( ruleFQN ) ) )? ( (lv_returnType_19_0= ruleReturnType ) )? ( (lv_services_20_0= ruleService ) )* ( (lv_events_21_0= ruleEvent ) )* otherlv_22= '}'
            {
            // InternalCqrsDsl.g:5195:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt145=2;
            int LA145_0 = input.LA(1);

            if ( (LA145_0==RULE_DOC) ) {
                alt145=1;
            }
            switch (alt145) {
                case 1 :
                    // InternalCqrsDsl.g:5196:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5196:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5197:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_129); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getMethodAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getMethodRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,80,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getMethodAccess().getMethodKeyword_1());
              		
            }
            // InternalCqrsDsl.g:5217:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5218:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5218:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5219:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_130); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getMethodAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getMethodRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:5235:3: (otherlv_3= 'ref' ( ( ruleFQN ) ) )?
            int alt146=2;
            int LA146_0 = input.LA(1);

            if ( (LA146_0==81) ) {
                alt146=1;
            }
            switch (alt146) {
                case 1 :
                    // InternalCqrsDsl.g:5236:4: otherlv_3= 'ref' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,81,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getMethodAccess().getRefKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:5240:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5241:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5241:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5242:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getMethodRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getMethodAccess().getRefMethodMethodCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_131);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5257:3: ( (lv_preconditions_5_0= rulePreconditions ) )?
            int alt147=2;
            int LA147_0 = input.LA(1);

            if ( (LA147_0==91) ) {
                alt147=1;
            }
            switch (alt147) {
                case 1 :
                    // InternalCqrsDsl.g:5258:4: (lv_preconditions_5_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:5258:4: (lv_preconditions_5_0= rulePreconditions )
                    // InternalCqrsDsl.g:5259:5: lv_preconditions_5_0= rulePreconditions
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getMethodAccess().getPreconditionsPreconditionsParserRuleCall_4_0());
                      				
                    }
                    pushFollow(FOLLOW_132);
                    lv_preconditions_5_0=rulePreconditions();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getMethodRule());
                      					}
                      					set(
                      						current,
                      						"preconditions",
                      						lv_preconditions_5_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Preconditions");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5276:3: ( (lv_businessRules_6_0= ruleBusinessRules ) )?
            int alt148=2;
            int LA148_0 = input.LA(1);

            if ( (LA148_0==92) ) {
                alt148=1;
            }
            switch (alt148) {
                case 1 :
                    // InternalCqrsDsl.g:5277:4: (lv_businessRules_6_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:5277:4: (lv_businessRules_6_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:5278:5: lv_businessRules_6_0= ruleBusinessRules
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getMethodAccess().getBusinessRulesBusinessRulesParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_133);
                    lv_businessRules_6_0=ruleBusinessRules();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getMethodRule());
                      					}
                      					set(
                      						current,
                      						"businessRules",
                      						lv_businessRules_6_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRules");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5295:3: (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            int alt150=2;
            int LA150_0 = input.LA(1);

            if ( (LA150_0==76) ) {
                alt150=1;
            }
            switch (alt150) {
                case 1 :
                    // InternalCqrsDsl.g:5296:4: otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_7=(Token)match(input,76,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getMethodAccess().getFiresKeyword_6_0());
                      			
                    }
                    // InternalCqrsDsl.g:5300:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5301:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5301:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5302:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getMethodRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getMethodAccess().getFiredEventsEventCrossReference_6_1_0());
                      					
                    }
                    pushFollow(FOLLOW_134);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:5316:4: (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    loop149:
                    do {
                        int alt149=2;
                        int LA149_0 = input.LA(1);

                        if ( (LA149_0==31) ) {
                            alt149=1;
                        }


                        switch (alt149) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5317:5: otherlv_9= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_9=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_9, grammarAccess.getMethodAccess().getCommaKeyword_6_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:5321:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:5322:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:5322:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:5323:7: ruleFQN
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElement(grammarAccess.getMethodRule());
                    	      							}
                    	      						
                    	    }
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getMethodAccess().getFiredEventsEventCrossReference_6_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_134);
                    	    ruleFQN();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop149;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:5339:3: (otherlv_11= 'rest-path' ( (lv_restPath_12_0= RULE_STRING ) ) )?
            int alt151=2;
            int LA151_0 = input.LA(1);

            if ( (LA151_0==82) ) {
                alt151=1;
            }
            switch (alt151) {
                case 1 :
                    // InternalCqrsDsl.g:5340:4: otherlv_11= 'rest-path' ( (lv_restPath_12_0= RULE_STRING ) )
                    {
                    otherlv_11=(Token)match(input,82,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getMethodAccess().getRestPathKeyword_7_0());
                      			
                    }
                    // InternalCqrsDsl.g:5344:4: ( (lv_restPath_12_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:5345:5: (lv_restPath_12_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:5345:5: (lv_restPath_12_0= RULE_STRING )
                    // InternalCqrsDsl.g:5346:6: lv_restPath_12_0= RULE_STRING
                    {
                    lv_restPath_12_0=(Token)match(input,RULE_STRING,FOLLOW_5); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_restPath_12_0, grammarAccess.getMethodAccess().getRestPathSTRINGTerminalRuleCall_7_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getMethodRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"restPath",
                      							lv_restPath_12_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_13=(Token)match(input,14,FOLLOW_135); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_13, grammarAccess.getMethodAccess().getLeftCurlyBracketKeyword_8());
              		
            }
            // InternalCqrsDsl.g:5367:3: ( (lv_metaInfo_14_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:5368:4: (lv_metaInfo_14_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:5368:4: (lv_metaInfo_14_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:5369:5: lv_metaInfo_14_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getMethodAccess().getMetaInfoTypeMetaInfoParserRuleCall_9_0());
              				
            }
            pushFollow(FOLLOW_136);
            lv_metaInfo_14_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getMethodRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_14_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:5386:3: ( (lv_hints_15_0= ruleHint ) )*
            loop152:
            do {
                int alt152=2;
                int LA152_0 = input.LA(1);

                if ( (LA152_0==RULE_DOC) ) {
                    int LA152_1 = input.LA(2);

                    if ( (LA152_1==20) ) {
                        alt152=1;
                    }


                }
                else if ( (LA152_0==20) ) {
                    alt152=1;
                }


                switch (alt152) {
            	case 1 :
            	    // InternalCqrsDsl.g:5387:4: (lv_hints_15_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:5387:4: (lv_hints_15_0= ruleHint )
            	    // InternalCqrsDsl.g:5388:5: lv_hints_15_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getMethodAccess().getHintsHintParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_136);
            	    lv_hints_15_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getMethodRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_15_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop152;
                }
            } while (true);

            // InternalCqrsDsl.g:5405:3: ( (lv_parameters_16_0= ruleParameter ) )*
            loop153:
            do {
                int alt153=2;
                int LA153_0 = input.LA(1);

                if ( (LA153_0==RULE_DOC) ) {
                    int LA153_2 = input.LA(2);

                    if ( (LA153_2==RULE_ID||LA153_2==79) ) {
                        alt153=1;
                    }


                }
                else if ( (LA153_0==RULE_ID||LA153_0==79) ) {
                    alt153=1;
                }


                switch (alt153) {
            	case 1 :
            	    // InternalCqrsDsl.g:5406:4: (lv_parameters_16_0= ruleParameter )
            	    {
            	    // InternalCqrsDsl.g:5406:4: (lv_parameters_16_0= ruleParameter )
            	    // InternalCqrsDsl.g:5407:5: lv_parameters_16_0= ruleParameter
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getMethodAccess().getParametersParameterParserRuleCall_11_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_137);
            	    lv_parameters_16_0=ruleParameter();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getMethodRule());
            	      					}
            	      					add(
            	      						current,
            	      						"parameters",
            	      						lv_parameters_16_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Parameter");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop153;
                }
            } while (true);

            // InternalCqrsDsl.g:5424:3: (otherlv_17= 'operation-context' ( ( ruleFQN ) ) )?
            int alt154=2;
            int LA154_0 = input.LA(1);

            if ( (LA154_0==77) ) {
                alt154=1;
            }
            switch (alt154) {
                case 1 :
                    // InternalCqrsDsl.g:5425:4: otherlv_17= 'operation-context' ( ( ruleFQN ) )
                    {
                    otherlv_17=(Token)match(input,77,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_17, grammarAccess.getMethodAccess().getOperationContextKeyword_12_0());
                      			
                    }
                    // InternalCqrsDsl.g:5429:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5430:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5430:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5431:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getMethodRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getMethodAccess().getOperationContextServiceCrossReference_12_1_0());
                      					
                    }
                    pushFollow(FOLLOW_138);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5446:3: ( (lv_returnType_19_0= ruleReturnType ) )?
            int alt155=2;
            int LA155_0 = input.LA(1);

            if ( (LA155_0==RULE_DOC) ) {
                int LA155_1 = input.LA(2);

                if ( (LA155_1==78) ) {
                    alt155=1;
                }
            }
            else if ( (LA155_0==78) ) {
                alt155=1;
            }
            switch (alt155) {
                case 1 :
                    // InternalCqrsDsl.g:5447:4: (lv_returnType_19_0= ruleReturnType )
                    {
                    // InternalCqrsDsl.g:5447:4: (lv_returnType_19_0= ruleReturnType )
                    // InternalCqrsDsl.g:5448:5: lv_returnType_19_0= ruleReturnType
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getMethodAccess().getReturnTypeReturnTypeParserRuleCall_13_0());
                      				
                    }
                    pushFollow(FOLLOW_124);
                    lv_returnType_19_0=ruleReturnType();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getMethodRule());
                      					}
                      					set(
                      						current,
                      						"returnType",
                      						lv_returnType_19_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.ReturnType");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5465:3: ( (lv_services_20_0= ruleService ) )*
            loop156:
            do {
                int alt156=2;
                int LA156_0 = input.LA(1);

                if ( (LA156_0==RULE_DOC) ) {
                    int LA156_1 = input.LA(2);

                    if ( (LA156_1==95) ) {
                        alt156=1;
                    }


                }
                else if ( (LA156_0==95) ) {
                    alt156=1;
                }


                switch (alt156) {
            	case 1 :
            	    // InternalCqrsDsl.g:5466:4: (lv_services_20_0= ruleService )
            	    {
            	    // InternalCqrsDsl.g:5466:4: (lv_services_20_0= ruleService )
            	    // InternalCqrsDsl.g:5467:5: lv_services_20_0= ruleService
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getMethodAccess().getServicesServiceParserRuleCall_14_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_124);
            	    lv_services_20_0=ruleService();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getMethodRule());
            	      					}
            	      					add(
            	      						current,
            	      						"services",
            	      						lv_services_20_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Service");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop156;
                }
            } while (true);

            // InternalCqrsDsl.g:5484:3: ( (lv_events_21_0= ruleEvent ) )*
            loop157:
            do {
                int alt157=2;
                int LA157_0 = input.LA(1);

                if ( (LA157_0==RULE_DOC||LA157_0==64||LA157_0==94) ) {
                    alt157=1;
                }


                switch (alt157) {
            	case 1 :
            	    // InternalCqrsDsl.g:5485:4: (lv_events_21_0= ruleEvent )
            	    {
            	    // InternalCqrsDsl.g:5485:4: (lv_events_21_0= ruleEvent )
            	    // InternalCqrsDsl.g:5486:5: lv_events_21_0= ruleEvent
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getMethodAccess().getEventsEventParserRuleCall_15_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_125);
            	    lv_events_21_0=ruleEvent();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getMethodRule());
            	      					}
            	      					add(
            	      						current,
            	      						"events",
            	      						lv_events_21_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Event");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop157;
                }
            } while (true);

            otherlv_22=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_22, grammarAccess.getMethodAccess().getRightCurlyBracketKeyword_16());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMethod"


    // $ANTLR start "entryRuleTypeMetaInfo"
    // InternalCqrsDsl.g:5511:1: entryRuleTypeMetaInfo returns [EObject current=null] : iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF ;
    public final EObject entryRuleTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTypeMetaInfo = null;


        try {
            // InternalCqrsDsl.g:5511:53: (iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF )
            // InternalCqrsDsl.g:5512:2: iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getTypeMetaInfoRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleTypeMetaInfo=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleTypeMetaInfo; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTypeMetaInfo"


    // $ANTLR start "ruleTypeMetaInfo"
    // InternalCqrsDsl.g:5518:1: ruleTypeMetaInfo returns [EObject current=null] : ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? ) ;
    public final EObject ruleTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_slabel_2_0=null;
        Token otherlv_3=null;
        Token lv_label_4_0=null;
        Token otherlv_5=null;
        Token lv_tooltip_6_0=null;
        Token otherlv_7=null;
        Token lv_prompt_8_0=null;
        Token otherlv_9=null;
        EObject lv_examples_10_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5524:2: ( ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? ) )
            // InternalCqrsDsl.g:5525:2: ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? )
            {
            // InternalCqrsDsl.g:5525:2: ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? )
            // InternalCqrsDsl.g:5526:3: () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )?
            {
            // InternalCqrsDsl.g:5526:3: ()
            // InternalCqrsDsl.g:5527:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getTypeMetaInfoAccess().getTypeMetaInfoAction_0(),
              					current);
              			
            }

            }

            // InternalCqrsDsl.g:5533:3: (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )?
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( (LA158_0==83) ) {
                alt158=1;
            }
            switch (alt158) {
                case 1 :
                    // InternalCqrsDsl.g:5534:4: otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) )
                    {
                    otherlv_1=(Token)match(input,83,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_1, grammarAccess.getTypeMetaInfoAccess().getSlabelKeyword_1_0());
                      			
                    }
                    // InternalCqrsDsl.g:5538:4: ( (lv_slabel_2_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:5539:5: (lv_slabel_2_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:5539:5: (lv_slabel_2_0= RULE_STRING )
                    // InternalCqrsDsl.g:5540:6: lv_slabel_2_0= RULE_STRING
                    {
                    lv_slabel_2_0=(Token)match(input,RULE_STRING,FOLLOW_139); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_slabel_2_0, grammarAccess.getTypeMetaInfoAccess().getSlabelSTRINGTerminalRuleCall_1_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getTypeMetaInfoRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"slabel",
                      							lv_slabel_2_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5557:3: (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )?
            int alt159=2;
            int LA159_0 = input.LA(1);

            if ( (LA159_0==84) ) {
                alt159=1;
            }
            switch (alt159) {
                case 1 :
                    // InternalCqrsDsl.g:5558:4: otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) )
                    {
                    otherlv_3=(Token)match(input,84,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getTypeMetaInfoAccess().getLabelKeyword_2_0());
                      			
                    }
                    // InternalCqrsDsl.g:5562:4: ( (lv_label_4_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:5563:5: (lv_label_4_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:5563:5: (lv_label_4_0= RULE_STRING )
                    // InternalCqrsDsl.g:5564:6: lv_label_4_0= RULE_STRING
                    {
                    lv_label_4_0=(Token)match(input,RULE_STRING,FOLLOW_140); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_label_4_0, grammarAccess.getTypeMetaInfoAccess().getLabelSTRINGTerminalRuleCall_2_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getTypeMetaInfoRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"label",
                      							lv_label_4_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5581:3: (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )?
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==85) ) {
                alt160=1;
            }
            switch (alt160) {
                case 1 :
                    // InternalCqrsDsl.g:5582:4: otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) )
                    {
                    otherlv_5=(Token)match(input,85,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getTypeMetaInfoAccess().getTooltipKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:5586:4: ( (lv_tooltip_6_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:5587:5: (lv_tooltip_6_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:5587:5: (lv_tooltip_6_0= RULE_STRING )
                    // InternalCqrsDsl.g:5588:6: lv_tooltip_6_0= RULE_STRING
                    {
                    lv_tooltip_6_0=(Token)match(input,RULE_STRING,FOLLOW_141); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_tooltip_6_0, grammarAccess.getTypeMetaInfoAccess().getTooltipSTRINGTerminalRuleCall_3_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getTypeMetaInfoRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"tooltip",
                      							lv_tooltip_6_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5605:3: (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )?
            int alt161=2;
            int LA161_0 = input.LA(1);

            if ( (LA161_0==86) ) {
                alt161=1;
            }
            switch (alt161) {
                case 1 :
                    // InternalCqrsDsl.g:5606:4: otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) )
                    {
                    otherlv_7=(Token)match(input,86,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getTypeMetaInfoAccess().getPromptKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:5610:4: ( (lv_prompt_8_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:5611:5: (lv_prompt_8_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:5611:5: (lv_prompt_8_0= RULE_STRING )
                    // InternalCqrsDsl.g:5612:6: lv_prompt_8_0= RULE_STRING
                    {
                    lv_prompt_8_0=(Token)match(input,RULE_STRING,FOLLOW_142); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_prompt_8_0, grammarAccess.getTypeMetaInfoAccess().getPromptSTRINGTerminalRuleCall_4_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getTypeMetaInfoRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"prompt",
                      							lv_prompt_8_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5629:3: (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )?
            int alt163=2;
            int LA163_0 = input.LA(1);

            if ( (LA163_0==87) ) {
                alt163=1;
            }
            switch (alt163) {
                case 1 :
                    // InternalCqrsDsl.g:5630:4: otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )*
                    {
                    otherlv_9=(Token)match(input,87,FOLLOW_143); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_9, grammarAccess.getTypeMetaInfoAccess().getExamplesKeyword_5_0());
                      			
                    }
                    // InternalCqrsDsl.g:5634:4: ( (lv_examples_10_0= ruleLiteral ) )*
                    loop162:
                    do {
                        int alt162=2;
                        int LA162_0 = input.LA(1);

                        if ( (LA162_0==RULE_STRING||(LA162_0>=RULE_INT && LA162_0<=RULE_DECIMAL)||LA162_0==52||(LA162_0>=118 && LA162_0<=119)) ) {
                            alt162=1;
                        }


                        switch (alt162) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5635:5: (lv_examples_10_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:5635:5: (lv_examples_10_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:5636:6: lv_examples_10_0= ruleLiteral
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      						newCompositeNode(grammarAccess.getTypeMetaInfoAccess().getExamplesLiteralParserRuleCall_5_1_0());
                    	      					
                    	    }
                    	    pushFollow(FOLLOW_143);
                    	    lv_examples_10_0=ruleLiteral();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      						if (current==null) {
                    	      							current = createModelElementForParent(grammarAccess.getTypeMetaInfoRule());
                    	      						}
                    	      						add(
                    	      							current,
                    	      							"examples",
                    	      							lv_examples_10_0,
                    	      							"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                    	      						afterParserOrEnumRuleCall();
                    	      					
                    	    }

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop162;
                        }
                    } while (true);


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTypeMetaInfo"


    // $ANTLR start "entryRuleGenericArgs"
    // InternalCqrsDsl.g:5658:1: entryRuleGenericArgs returns [EObject current=null] : iv_ruleGenericArgs= ruleGenericArgs EOF ;
    public final EObject entryRuleGenericArgs() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGenericArgs = null;


        try {
            // InternalCqrsDsl.g:5658:52: (iv_ruleGenericArgs= ruleGenericArgs EOF )
            // InternalCqrsDsl.g:5659:2: iv_ruleGenericArgs= ruleGenericArgs EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getGenericArgsRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleGenericArgs=ruleGenericArgs();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleGenericArgs; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGenericArgs"


    // $ANTLR start "ruleGenericArgs"
    // InternalCqrsDsl.g:5665:1: ruleGenericArgs returns [EObject current=null] : ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' ) ;
    public final EObject ruleGenericArgs() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5671:2: ( ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' ) )
            // InternalCqrsDsl.g:5672:2: ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' )
            {
            // InternalCqrsDsl.g:5672:2: ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' )
            // InternalCqrsDsl.g:5673:3: (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>'
            {
            // InternalCqrsDsl.g:5673:3: (otherlv_0= '<' )+
            int cnt164=0;
            loop164:
            do {
                int alt164=2;
                int LA164_0 = input.LA(1);

                if ( (LA164_0==88) ) {
                    alt164=1;
                }


                switch (alt164) {
            	case 1 :
            	    // InternalCqrsDsl.g:5674:4: otherlv_0= '<'
            	    {
            	    otherlv_0=(Token)match(input,88,FOLLOW_144); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_0, grammarAccess.getGenericArgsAccess().getLessThanSignKeyword_0());
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    if ( cnt164 >= 1 ) break loop164;
            	    if (state.backtracking>0) {state.failed=true; return current;}
                        EarlyExitException eee =
                            new EarlyExitException(164, input);
                        throw eee;
                }
                cnt164++;
            } while (true);

            // InternalCqrsDsl.g:5679:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5680:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5680:4: ( ruleFQN )
            // InternalCqrsDsl.g:5681:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getGenericArgsRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getGenericArgsAccess().getArgsTypeCrossReference_1_0());
              				
            }
            pushFollow(FOLLOW_145);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:5695:3: (otherlv_2= ',' ( ( ruleFQN ) ) )*
            loop165:
            do {
                int alt165=2;
                int LA165_0 = input.LA(1);

                if ( (LA165_0==31) ) {
                    alt165=1;
                }


                switch (alt165) {
            	case 1 :
            	    // InternalCqrsDsl.g:5696:4: otherlv_2= ',' ( ( ruleFQN ) )
            	    {
            	    otherlv_2=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_2, grammarAccess.getGenericArgsAccess().getCommaKeyword_2_0());
            	      			
            	    }
            	    // InternalCqrsDsl.g:5700:4: ( ( ruleFQN ) )
            	    // InternalCqrsDsl.g:5701:5: ( ruleFQN )
            	    {
            	    // InternalCqrsDsl.g:5701:5: ( ruleFQN )
            	    // InternalCqrsDsl.g:5702:6: ruleFQN
            	    {
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElement(grammarAccess.getGenericArgsRule());
            	      						}
            	      					
            	    }
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getGenericArgsAccess().getArgsTypeCrossReference_2_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_145);
            	    ruleFQN();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop165;
                }
            } while (true);

            otherlv_4=(Token)match(input,89,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_4, grammarAccess.getGenericArgsAccess().getGreaterThanSignKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGenericArgs"


    // $ANTLR start "entryRuleAttribute"
    // InternalCqrsDsl.g:5725:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCqrsDsl.g:5725:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCqrsDsl.g:5726:2: iv_ruleAttribute= ruleAttribute EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAttributeRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAttribute=ruleAttribute();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAttribute; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAttribute"


    // $ANTLR start "ruleAttribute"
    // InternalCqrsDsl.g:5732:1: ruleAttribute returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token lv_optional_1_0=null;
        Token lv_name_4_0=null;
        EObject lv_generics_3_0 = null;

        EObject lv_invariants_5_0 = null;

        EObject lv_dataProtection_6_0 = null;

        EObject lv_overridden_7_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5738:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) )
            // InternalCqrsDsl.g:5739:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            {
            // InternalCqrsDsl.g:5739:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            // InternalCqrsDsl.g:5740:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            {
            // InternalCqrsDsl.g:5740:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt166=2;
            int LA166_0 = input.LA(1);

            if ( (LA166_0==RULE_DOC) ) {
                alt166=1;
            }
            switch (alt166) {
                case 1 :
                    // InternalCqrsDsl.g:5741:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5741:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5742:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_127); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getAttributeAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getAttributeRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5758:3: ( (lv_optional_1_0= 'optional' ) )?
            int alt167=2;
            int LA167_0 = input.LA(1);

            if ( (LA167_0==79) ) {
                alt167=1;
            }
            switch (alt167) {
                case 1 :
                    // InternalCqrsDsl.g:5759:4: (lv_optional_1_0= 'optional' )
                    {
                    // InternalCqrsDsl.g:5759:4: (lv_optional_1_0= 'optional' )
                    // InternalCqrsDsl.g:5760:5: lv_optional_1_0= 'optional'
                    {
                    lv_optional_1_0=(Token)match(input,79,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_optional_1_0, grammarAccess.getAttributeAccess().getOptionalOptionalKeyword_1_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getAttributeRule());
                      					}
                      					setWithLastConsumed(current, "optional", lv_optional_1_0, "optional");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5772:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5773:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5773:4: ( ruleFQN )
            // InternalCqrsDsl.g:5774:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getAttributeRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getAttributeAccess().getTypeTypeCrossReference_2_0());
              				
            }
            pushFollow(FOLLOW_144);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:5788:3: ( (lv_generics_3_0= ruleGenericArgs ) )?
            int alt168=2;
            int LA168_0 = input.LA(1);

            if ( (LA168_0==88) ) {
                alt168=1;
            }
            switch (alt168) {
                case 1 :
                    // InternalCqrsDsl.g:5789:4: (lv_generics_3_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:5789:4: (lv_generics_3_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:5790:5: lv_generics_3_0= ruleGenericArgs
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAttributeAccess().getGenericsGenericArgsParserRuleCall_3_0());
                      				
                    }
                    pushFollow(FOLLOW_4);
                    lv_generics_3_0=ruleGenericArgs();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAttributeRule());
                      					}
                      					set(
                      						current,
                      						"generics",
                      						lv_generics_3_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.GenericArgs");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5807:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCqrsDsl.g:5808:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5808:4: (lv_name_4_0= RULE_ID )
            // InternalCqrsDsl.g:5809:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_146); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_4_0, grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_4_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getAttributeRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_4_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:5825:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt169=2;
            int LA169_0 = input.LA(1);

            if ( (LA169_0==90) ) {
                alt169=1;
            }
            switch (alt169) {
                case 1 :
                    // InternalCqrsDsl.g:5826:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:5826:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:5827:5: lv_invariants_5_0= ruleInvariants
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAttributeAccess().getInvariantsInvariantsParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_147);
                    lv_invariants_5_0=ruleInvariants();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAttributeRule());
                      					}
                      					set(
                      						current,
                      						"invariants",
                      						lv_invariants_5_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Invariants");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5844:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt170=2;
            int LA170_0 = input.LA(1);

            if ( (LA170_0==37) ) {
                alt170=1;
            }
            switch (alt170) {
                case 1 :
                    // InternalCqrsDsl.g:5845:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:5845:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:5846:5: lv_dataProtection_6_0= ruleDataProtectionInstance
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAttributeAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                      				
                    }
                    pushFollow(FOLLOW_33);
                    lv_dataProtection_6_0=ruleDataProtectionInstance();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAttributeRule());
                      					}
                      					set(
                      						current,
                      						"dataProtection",
                      						lv_dataProtection_6_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DataProtectionInstance");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5863:3: ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            int alt171=2;
            int LA171_0 = input.LA(1);

            if ( (LA171_0==14) ) {
                alt171=1;
            }
            switch (alt171) {
                case 1 :
                    // InternalCqrsDsl.g:5864:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    {
                    // InternalCqrsDsl.g:5864:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    // InternalCqrsDsl.g:5865:5: lv_overridden_7_0= ruleOverriddenTypeMetaInfo
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getAttributeAccess().getOverriddenOverriddenTypeMetaInfoParserRuleCall_7_0());
                      				
                    }
                    pushFollow(FOLLOW_2);
                    lv_overridden_7_0=ruleOverriddenTypeMetaInfo();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getAttributeRule());
                      					}
                      					set(
                      						current,
                      						"overridden",
                      						lv_overridden_7_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.OverriddenTypeMetaInfo");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAttribute"


    // $ANTLR start "entryRuleParameter"
    // InternalCqrsDsl.g:5886:1: entryRuleParameter returns [EObject current=null] : iv_ruleParameter= ruleParameter EOF ;
    public final EObject entryRuleParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameter = null;


        try {
            // InternalCqrsDsl.g:5886:50: (iv_ruleParameter= ruleParameter EOF )
            // InternalCqrsDsl.g:5887:2: iv_ruleParameter= ruleParameter EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getParameterRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleParameter=ruleParameter();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleParameter; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameter"


    // $ANTLR start "ruleParameter"
    // InternalCqrsDsl.g:5893:1: ruleParameter returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) ;
    public final EObject ruleParameter() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token lv_optional_1_0=null;
        Token lv_name_4_0=null;
        EObject lv_generics_3_0 = null;

        EObject lv_preconditions_5_0 = null;

        EObject lv_businessRules_6_0 = null;

        EObject lv_overridden_7_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5899:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) )
            // InternalCqrsDsl.g:5900:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            {
            // InternalCqrsDsl.g:5900:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            // InternalCqrsDsl.g:5901:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            {
            // InternalCqrsDsl.g:5901:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt172=2;
            int LA172_0 = input.LA(1);

            if ( (LA172_0==RULE_DOC) ) {
                alt172=1;
            }
            switch (alt172) {
                case 1 :
                    // InternalCqrsDsl.g:5902:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5902:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5903:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_127); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getParameterAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getParameterRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5919:3: ( (lv_optional_1_0= 'optional' ) )?
            int alt173=2;
            int LA173_0 = input.LA(1);

            if ( (LA173_0==79) ) {
                alt173=1;
            }
            switch (alt173) {
                case 1 :
                    // InternalCqrsDsl.g:5920:4: (lv_optional_1_0= 'optional' )
                    {
                    // InternalCqrsDsl.g:5920:4: (lv_optional_1_0= 'optional' )
                    // InternalCqrsDsl.g:5921:5: lv_optional_1_0= 'optional'
                    {
                    lv_optional_1_0=(Token)match(input,79,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_optional_1_0, grammarAccess.getParameterAccess().getOptionalOptionalKeyword_1_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getParameterRule());
                      					}
                      					setWithLastConsumed(current, "optional", lv_optional_1_0, "optional");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5933:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5934:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5934:4: ( ruleFQN )
            // InternalCqrsDsl.g:5935:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getParameterRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getParameterAccess().getTypeTypeCrossReference_2_0());
              				
            }
            pushFollow(FOLLOW_144);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:5949:3: ( (lv_generics_3_0= ruleGenericArgs ) )?
            int alt174=2;
            int LA174_0 = input.LA(1);

            if ( (LA174_0==88) ) {
                alt174=1;
            }
            switch (alt174) {
                case 1 :
                    // InternalCqrsDsl.g:5950:4: (lv_generics_3_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:5950:4: (lv_generics_3_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:5951:5: lv_generics_3_0= ruleGenericArgs
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getParameterAccess().getGenericsGenericArgsParserRuleCall_3_0());
                      				
                    }
                    pushFollow(FOLLOW_4);
                    lv_generics_3_0=ruleGenericArgs();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getParameterRule());
                      					}
                      					set(
                      						current,
                      						"generics",
                      						lv_generics_3_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.GenericArgs");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5968:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCqrsDsl.g:5969:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5969:4: (lv_name_4_0= RULE_ID )
            // InternalCqrsDsl.g:5970:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_148); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_4_0, grammarAccess.getParameterAccess().getNameIDTerminalRuleCall_4_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getParameterRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_4_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:5986:3: ( (lv_preconditions_5_0= rulePreconditions ) )?
            int alt175=2;
            int LA175_0 = input.LA(1);

            if ( (LA175_0==91) ) {
                alt175=1;
            }
            switch (alt175) {
                case 1 :
                    // InternalCqrsDsl.g:5987:4: (lv_preconditions_5_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:5987:4: (lv_preconditions_5_0= rulePreconditions )
                    // InternalCqrsDsl.g:5988:5: lv_preconditions_5_0= rulePreconditions
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getParameterAccess().getPreconditionsPreconditionsParserRuleCall_5_0());
                      				
                    }
                    pushFollow(FOLLOW_149);
                    lv_preconditions_5_0=rulePreconditions();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getParameterRule());
                      					}
                      					set(
                      						current,
                      						"preconditions",
                      						lv_preconditions_5_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.Preconditions");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:6005:3: ( (lv_businessRules_6_0= ruleBusinessRules ) )?
            int alt176=2;
            int LA176_0 = input.LA(1);

            if ( (LA176_0==92) ) {
                alt176=1;
            }
            switch (alt176) {
                case 1 :
                    // InternalCqrsDsl.g:6006:4: (lv_businessRules_6_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:6006:4: (lv_businessRules_6_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:6007:5: lv_businessRules_6_0= ruleBusinessRules
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getParameterAccess().getBusinessRulesBusinessRulesParserRuleCall_6_0());
                      				
                    }
                    pushFollow(FOLLOW_33);
                    lv_businessRules_6_0=ruleBusinessRules();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getParameterRule());
                      					}
                      					set(
                      						current,
                      						"businessRules",
                      						lv_businessRules_6_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRules");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:6024:3: ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            int alt177=2;
            int LA177_0 = input.LA(1);

            if ( (LA177_0==14) ) {
                alt177=1;
            }
            switch (alt177) {
                case 1 :
                    // InternalCqrsDsl.g:6025:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    {
                    // InternalCqrsDsl.g:6025:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    // InternalCqrsDsl.g:6026:5: lv_overridden_7_0= ruleOverriddenTypeMetaInfo
                    {
                    if ( state.backtracking==0 ) {

                      					newCompositeNode(grammarAccess.getParameterAccess().getOverriddenOverriddenTypeMetaInfoParserRuleCall_7_0());
                      				
                    }
                    pushFollow(FOLLOW_2);
                    lv_overridden_7_0=ruleOverriddenTypeMetaInfo();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElementForParent(grammarAccess.getParameterRule());
                      					}
                      					set(
                      						current,
                      						"overridden",
                      						lv_overridden_7_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.OverriddenTypeMetaInfo");
                      					afterParserOrEnumRuleCall();
                      				
                    }

                    }


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameter"


    // $ANTLR start "entryRuleInvariants"
    // InternalCqrsDsl.g:6047:1: entryRuleInvariants returns [EObject current=null] : iv_ruleInvariants= ruleInvariants EOF ;
    public final EObject entryRuleInvariants() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInvariants = null;


        try {
            // InternalCqrsDsl.g:6047:51: (iv_ruleInvariants= ruleInvariants EOF )
            // InternalCqrsDsl.g:6048:2: iv_ruleInvariants= ruleInvariants EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getInvariantsRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleInvariants=ruleInvariants();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleInvariants; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleInvariants"


    // $ANTLR start "ruleInvariants"
    // InternalCqrsDsl.g:6054:1: ruleInvariants returns [EObject current=null] : (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) ;
    public final EObject ruleInvariants() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_constraintInstances_1_0 = null;

        EObject lv_constraintInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6060:2: ( (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) )
            // InternalCqrsDsl.g:6061:2: (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            {
            // InternalCqrsDsl.g:6061:2: (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            // InternalCqrsDsl.g:6062:3: otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            {
            otherlv_0=(Token)match(input,90,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getInvariantsAccess().getInvariantsKeyword_0());
              		
            }
            // InternalCqrsDsl.g:6066:3: ( (lv_constraintInstances_1_0= ruleConstraintInstance ) )
            // InternalCqrsDsl.g:6067:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            {
            // InternalCqrsDsl.g:6067:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            // InternalCqrsDsl.g:6068:5: lv_constraintInstances_1_0= ruleConstraintInstance
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getInvariantsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_150);
            lv_constraintInstances_1_0=ruleConstraintInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getInvariantsRule());
              					}
              					add(
              						current,
              						"constraintInstances",
              						lv_constraintInstances_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ConstraintInstance");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:6085:3: (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            loop178:
            do {
                int alt178=2;
                int LA178_0 = input.LA(1);

                if ( (LA178_0==31) ) {
                    alt178=1;
                }


                switch (alt178) {
            	case 1 :
            	    // InternalCqrsDsl.g:6086:4: otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_2, grammarAccess.getInvariantsAccess().getCommaKeyword_2_0());
            	      			
            	    }
            	    // InternalCqrsDsl.g:6090:4: ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    // InternalCqrsDsl.g:6091:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    {
            	    // InternalCqrsDsl.g:6091:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    // InternalCqrsDsl.g:6092:6: lv_constraintInstances_3_0= ruleConstraintInstance
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getInvariantsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_2_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_150);
            	    lv_constraintInstances_3_0=ruleConstraintInstance();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getInvariantsRule());
            	      						}
            	      						add(
            	      							current,
            	      							"constraintInstances",
            	      							lv_constraintInstances_3_0,
            	      							"org.fuin.dsl.cqrs.CqrsDsl.ConstraintInstance");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop178;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleInvariants"


    // $ANTLR start "entryRulePreconditions"
    // InternalCqrsDsl.g:6114:1: entryRulePreconditions returns [EObject current=null] : iv_rulePreconditions= rulePreconditions EOF ;
    public final EObject entryRulePreconditions() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePreconditions = null;


        try {
            // InternalCqrsDsl.g:6114:54: (iv_rulePreconditions= rulePreconditions EOF )
            // InternalCqrsDsl.g:6115:2: iv_rulePreconditions= rulePreconditions EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getPreconditionsRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_rulePreconditions=rulePreconditions();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_rulePreconditions; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePreconditions"


    // $ANTLR start "rulePreconditions"
    // InternalCqrsDsl.g:6121:1: rulePreconditions returns [EObject current=null] : (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) ;
    public final EObject rulePreconditions() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_constraintInstances_1_0 = null;

        EObject lv_constraintInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6127:2: ( (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) )
            // InternalCqrsDsl.g:6128:2: (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            {
            // InternalCqrsDsl.g:6128:2: (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            // InternalCqrsDsl.g:6129:3: otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            {
            otherlv_0=(Token)match(input,91,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getPreconditionsAccess().getPreconditionsKeyword_0());
              		
            }
            // InternalCqrsDsl.g:6133:3: ( (lv_constraintInstances_1_0= ruleConstraintInstance ) )
            // InternalCqrsDsl.g:6134:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            {
            // InternalCqrsDsl.g:6134:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            // InternalCqrsDsl.g:6135:5: lv_constraintInstances_1_0= ruleConstraintInstance
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getPreconditionsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_150);
            lv_constraintInstances_1_0=ruleConstraintInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getPreconditionsRule());
              					}
              					add(
              						current,
              						"constraintInstances",
              						lv_constraintInstances_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ConstraintInstance");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:6152:3: (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            loop179:
            do {
                int alt179=2;
                int LA179_0 = input.LA(1);

                if ( (LA179_0==31) ) {
                    alt179=1;
                }


                switch (alt179) {
            	case 1 :
            	    // InternalCqrsDsl.g:6153:4: otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_2, grammarAccess.getPreconditionsAccess().getCommaKeyword_2_0());
            	      			
            	    }
            	    // InternalCqrsDsl.g:6157:4: ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    // InternalCqrsDsl.g:6158:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    {
            	    // InternalCqrsDsl.g:6158:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    // InternalCqrsDsl.g:6159:6: lv_constraintInstances_3_0= ruleConstraintInstance
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getPreconditionsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_2_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_150);
            	    lv_constraintInstances_3_0=ruleConstraintInstance();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getPreconditionsRule());
            	      						}
            	      						add(
            	      							current,
            	      							"constraintInstances",
            	      							lv_constraintInstances_3_0,
            	      							"org.fuin.dsl.cqrs.CqrsDsl.ConstraintInstance");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop179;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePreconditions"


    // $ANTLR start "entryRuleBusinessRules"
    // InternalCqrsDsl.g:6181:1: entryRuleBusinessRules returns [EObject current=null] : iv_ruleBusinessRules= ruleBusinessRules EOF ;
    public final EObject entryRuleBusinessRules() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRules = null;


        try {
            // InternalCqrsDsl.g:6181:54: (iv_ruleBusinessRules= ruleBusinessRules EOF )
            // InternalCqrsDsl.g:6182:2: iv_ruleBusinessRules= ruleBusinessRules EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getBusinessRulesRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleBusinessRules=ruleBusinessRules();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleBusinessRules; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBusinessRules"


    // $ANTLR start "ruleBusinessRules"
    // InternalCqrsDsl.g:6188:1: ruleBusinessRules returns [EObject current=null] : (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* ) ;
    public final EObject ruleBusinessRules() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_businessRuleInstances_1_0 = null;

        EObject lv_businessRuleInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6194:2: ( (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* ) )
            // InternalCqrsDsl.g:6195:2: (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* )
            {
            // InternalCqrsDsl.g:6195:2: (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* )
            // InternalCqrsDsl.g:6196:3: otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )*
            {
            otherlv_0=(Token)match(input,92,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getBusinessRulesAccess().getBusinessRulesKeyword_0());
              		
            }
            // InternalCqrsDsl.g:6200:3: ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) )
            // InternalCqrsDsl.g:6201:4: (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance )
            {
            // InternalCqrsDsl.g:6201:4: (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance )
            // InternalCqrsDsl.g:6202:5: lv_businessRuleInstances_1_0= ruleBusinessRuleInstance
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getBusinessRulesAccess().getBusinessRuleInstancesBusinessRuleInstanceParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_150);
            lv_businessRuleInstances_1_0=ruleBusinessRuleInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getBusinessRulesRule());
              					}
              					add(
              						current,
              						"businessRuleInstances",
              						lv_businessRuleInstances_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRuleInstance");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:6219:3: (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )*
            loop180:
            do {
                int alt180=2;
                int LA180_0 = input.LA(1);

                if ( (LA180_0==31) ) {
                    alt180=1;
                }


                switch (alt180) {
            	case 1 :
            	    // InternalCqrsDsl.g:6220:4: otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_2, grammarAccess.getBusinessRulesAccess().getCommaKeyword_2_0());
            	      			
            	    }
            	    // InternalCqrsDsl.g:6224:4: ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) )
            	    // InternalCqrsDsl.g:6225:5: (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance )
            	    {
            	    // InternalCqrsDsl.g:6225:5: (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance )
            	    // InternalCqrsDsl.g:6226:6: lv_businessRuleInstances_3_0= ruleBusinessRuleInstance
            	    {
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getBusinessRulesAccess().getBusinessRuleInstancesBusinessRuleInstanceParserRuleCall_2_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_150);
            	    lv_businessRuleInstances_3_0=ruleBusinessRuleInstance();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElementForParent(grammarAccess.getBusinessRulesRule());
            	      						}
            	      						add(
            	      							current,
            	      							"businessRuleInstances",
            	      							lv_businessRuleInstances_3_0,
            	      							"org.fuin.dsl.cqrs.CqrsDsl.BusinessRuleInstance");
            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop180;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBusinessRules"


    // $ANTLR start "entryRuleOverriddenTypeMetaInfo"
    // InternalCqrsDsl.g:6248:1: entryRuleOverriddenTypeMetaInfo returns [EObject current=null] : iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF ;
    public final EObject entryRuleOverriddenTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOverriddenTypeMetaInfo = null;


        try {
            // InternalCqrsDsl.g:6248:63: (iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF )
            // InternalCqrsDsl.g:6249:2: iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getOverriddenTypeMetaInfoRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleOverriddenTypeMetaInfo=ruleOverriddenTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleOverriddenTypeMetaInfo; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOverriddenTypeMetaInfo"


    // $ANTLR start "ruleOverriddenTypeMetaInfo"
    // InternalCqrsDsl.g:6255:1: ruleOverriddenTypeMetaInfo returns [EObject current=null] : (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) ( (lv_hints_2_0= ruleHint ) )* otherlv_3= '}' ) ;
    public final EObject ruleOverriddenTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_3=null;
        EObject lv_metaInfo_1_0 = null;

        EObject lv_hints_2_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6261:2: ( (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) ( (lv_hints_2_0= ruleHint ) )* otherlv_3= '}' ) )
            // InternalCqrsDsl.g:6262:2: (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) ( (lv_hints_2_0= ruleHint ) )* otherlv_3= '}' )
            {
            // InternalCqrsDsl.g:6262:2: (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) ( (lv_hints_2_0= ruleHint ) )* otherlv_3= '}' )
            // InternalCqrsDsl.g:6263:3: otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) ( (lv_hints_2_0= ruleHint ) )* otherlv_3= '}'
            {
            otherlv_0=(Token)match(input,14,FOLLOW_151); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getOverriddenTypeMetaInfoAccess().getLeftCurlyBracketKeyword_0());
              		
            }
            // InternalCqrsDsl.g:6267:3: ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:6268:4: (lv_metaInfo_1_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:6268:4: (lv_metaInfo_1_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:6269:5: lv_metaInfo_1_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getOverriddenTypeMetaInfoAccess().getMetaInfoTypeMetaInfoParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_152);
            lv_metaInfo_1_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getOverriddenTypeMetaInfoRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:6286:3: ( (lv_hints_2_0= ruleHint ) )*
            loop181:
            do {
                int alt181=2;
                int LA181_0 = input.LA(1);

                if ( (LA181_0==RULE_DOC||LA181_0==20) ) {
                    alt181=1;
                }


                switch (alt181) {
            	case 1 :
            	    // InternalCqrsDsl.g:6287:4: (lv_hints_2_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:6287:4: (lv_hints_2_0= ruleHint )
            	    // InternalCqrsDsl.g:6288:5: lv_hints_2_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getOverriddenTypeMetaInfoAccess().getHintsHintParserRuleCall_2_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_152);
            	    lv_hints_2_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getOverriddenTypeMetaInfoRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_2_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop181;
                }
            } while (true);

            otherlv_3=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getOverriddenTypeMetaInfoAccess().getRightCurlyBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOverriddenTypeMetaInfo"


    // $ANTLR start "entryRuleConstraintInstance"
    // InternalCqrsDsl.g:6313:1: entryRuleConstraintInstance returns [EObject current=null] : iv_ruleConstraintInstance= ruleConstraintInstance EOF ;
    public final EObject entryRuleConstraintInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstraintInstance = null;


        try {
            // InternalCqrsDsl.g:6313:59: (iv_ruleConstraintInstance= ruleConstraintInstance EOF )
            // InternalCqrsDsl.g:6314:2: iv_ruleConstraintInstance= ruleConstraintInstance EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getConstraintInstanceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleConstraintInstance=ruleConstraintInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleConstraintInstance; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConstraintInstance"


    // $ANTLR start "ruleConstraintInstance"
    // InternalCqrsDsl.g:6320:1: ruleConstraintInstance returns [EObject current=null] : ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) ;
    public final EObject ruleConstraintInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_params_2_0 = null;

        EObject lv_params_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6326:2: ( ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) )
            // InternalCqrsDsl.g:6327:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            {
            // InternalCqrsDsl.g:6327:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            // InternalCqrsDsl.g:6328:3: ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            {
            // InternalCqrsDsl.g:6328:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:6329:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:6329:4: ( ruleFQN )
            // InternalCqrsDsl.g:6330:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getConstraintInstanceRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getConstraintInstanceAccess().getConstraintConstraintCrossReference_0_0());
              				
            }
            pushFollow(FOLLOW_153);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:6344:3: (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            int alt183=2;
            int LA183_0 = input.LA(1);

            if ( (LA183_0==48) ) {
                alt183=1;
            }
            switch (alt183) {
                case 1 :
                    // InternalCqrsDsl.g:6345:4: otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')'
                    {
                    otherlv_1=(Token)match(input,48,FOLLOW_97); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_1, grammarAccess.getConstraintInstanceAccess().getLeftParenthesisKeyword_1_0());
                      			
                    }
                    // InternalCqrsDsl.g:6349:4: ( (lv_params_2_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:6350:5: (lv_params_2_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:6350:5: (lv_params_2_0= ruleLiteral )
                    // InternalCqrsDsl.g:6351:6: lv_params_2_0= ruleLiteral
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getConstraintInstanceAccess().getParamsLiteralParserRuleCall_1_1_0());
                      					
                    }
                    pushFollow(FOLLOW_98);
                    lv_params_2_0=ruleLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getConstraintInstanceRule());
                      						}
                      						add(
                      							current,
                      							"params",
                      							lv_params_2_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:6368:4: (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )*
                    loop182:
                    do {
                        int alt182=2;
                        int LA182_0 = input.LA(1);

                        if ( (LA182_0==31) ) {
                            alt182=1;
                        }


                        switch (alt182) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6369:5: otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) )
                    	    {
                    	    otherlv_3=(Token)match(input,31,FOLLOW_97); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getConstraintInstanceAccess().getCommaKeyword_1_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:6373:5: ( (lv_params_4_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:6374:6: (lv_params_4_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:6374:6: (lv_params_4_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:6375:7: lv_params_4_0= ruleLiteral
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getConstraintInstanceAccess().getParamsLiteralParserRuleCall_1_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_98);
                    	    lv_params_4_0=ruleLiteral();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getConstraintInstanceRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"params",
                    	      								lv_params_4_0,
                    	      								"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop182;
                        }
                    } while (true);

                    otherlv_5=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getConstraintInstanceAccess().getRightParenthesisKeyword_1_3());
                      			
                    }

                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConstraintInstance"


    // $ANTLR start "entryRuleBusinessRuleInstance"
    // InternalCqrsDsl.g:6402:1: entryRuleBusinessRuleInstance returns [EObject current=null] : iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF ;
    public final EObject entryRuleBusinessRuleInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRuleInstance = null;


        try {
            // InternalCqrsDsl.g:6402:61: (iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF )
            // InternalCqrsDsl.g:6403:2: iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getBusinessRuleInstanceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleBusinessRuleInstance=ruleBusinessRuleInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleBusinessRuleInstance; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBusinessRuleInstance"


    // $ANTLR start "ruleBusinessRuleInstance"
    // InternalCqrsDsl.g:6409:1: ruleBusinessRuleInstance returns [EObject current=null] : ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleRuleArgument ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )* otherlv_5= ')' )? ) ;
    public final EObject ruleBusinessRuleInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_params_2_0 = null;

        EObject lv_params_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6415:2: ( ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleRuleArgument ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )* otherlv_5= ')' )? ) )
            // InternalCqrsDsl.g:6416:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleRuleArgument ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )* otherlv_5= ')' )? )
            {
            // InternalCqrsDsl.g:6416:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleRuleArgument ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )* otherlv_5= ')' )? )
            // InternalCqrsDsl.g:6417:3: ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleRuleArgument ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )* otherlv_5= ')' )?
            {
            // InternalCqrsDsl.g:6417:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:6418:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:6418:4: ( ruleFQN )
            // InternalCqrsDsl.g:6419:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getBusinessRuleInstanceRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getBusinessRuleBusinessRuleCrossReference_0_0());
              				
            }
            pushFollow(FOLLOW_153);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:6433:3: (otherlv_1= '(' ( (lv_params_2_0= ruleRuleArgument ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )* otherlv_5= ')' )?
            int alt185=2;
            int LA185_0 = input.LA(1);

            if ( (LA185_0==48) ) {
                alt185=1;
            }
            switch (alt185) {
                case 1 :
                    // InternalCqrsDsl.g:6434:4: otherlv_1= '(' ( (lv_params_2_0= ruleRuleArgument ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )* otherlv_5= ')'
                    {
                    otherlv_1=(Token)match(input,48,FOLLOW_154); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_1, grammarAccess.getBusinessRuleInstanceAccess().getLeftParenthesisKeyword_1_0());
                      			
                    }
                    // InternalCqrsDsl.g:6438:4: ( (lv_params_2_0= ruleRuleArgument ) )
                    // InternalCqrsDsl.g:6439:5: (lv_params_2_0= ruleRuleArgument )
                    {
                    // InternalCqrsDsl.g:6439:5: (lv_params_2_0= ruleRuleArgument )
                    // InternalCqrsDsl.g:6440:6: lv_params_2_0= ruleRuleArgument
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getParamsRuleArgumentParserRuleCall_1_1_0());
                      					
                    }
                    pushFollow(FOLLOW_98);
                    lv_params_2_0=ruleRuleArgument();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getBusinessRuleInstanceRule());
                      						}
                      						add(
                      							current,
                      							"params",
                      							lv_params_2_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.RuleArgument");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:6457:4: (otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) ) )*
                    loop184:
                    do {
                        int alt184=2;
                        int LA184_0 = input.LA(1);

                        if ( (LA184_0==31) ) {
                            alt184=1;
                        }


                        switch (alt184) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6458:5: otherlv_3= ',' ( (lv_params_4_0= ruleRuleArgument ) )
                    	    {
                    	    otherlv_3=(Token)match(input,31,FOLLOW_154); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getBusinessRuleInstanceAccess().getCommaKeyword_1_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:6462:5: ( (lv_params_4_0= ruleRuleArgument ) )
                    	    // InternalCqrsDsl.g:6463:6: (lv_params_4_0= ruleRuleArgument )
                    	    {
                    	    // InternalCqrsDsl.g:6463:6: (lv_params_4_0= ruleRuleArgument )
                    	    // InternalCqrsDsl.g:6464:7: lv_params_4_0= ruleRuleArgument
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getParamsRuleArgumentParserRuleCall_1_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_98);
                    	    lv_params_4_0=ruleRuleArgument();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getBusinessRuleInstanceRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"params",
                    	      								lv_params_4_0,
                    	      								"org.fuin.dsl.cqrs.CqrsDsl.RuleArgument");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop184;
                        }
                    } while (true);

                    otherlv_5=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getBusinessRuleInstanceAccess().getRightParenthesisKeyword_1_3());
                      			
                    }

                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBusinessRuleInstance"


    // $ANTLR start "entryRuleRuleArgument"
    // InternalCqrsDsl.g:6491:1: entryRuleRuleArgument returns [EObject current=null] : iv_ruleRuleArgument= ruleRuleArgument EOF ;
    public final EObject entryRuleRuleArgument() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleArgument = null;


        try {
            // InternalCqrsDsl.g:6491:53: (iv_ruleRuleArgument= ruleRuleArgument EOF )
            // InternalCqrsDsl.g:6492:2: iv_ruleRuleArgument= ruleRuleArgument EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getRuleArgumentRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleRuleArgument=ruleRuleArgument();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleRuleArgument; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleArgument"


    // $ANTLR start "ruleRuleArgument"
    // InternalCqrsDsl.g:6498:1: ruleRuleArgument returns [EObject current=null] : ( ( () ( (lv_literal_1_0= ruleLiteral ) ) ) | ( ( ( ( ( ruleFQN ) ) '(' ) )=>this_ServiceCallArgument_2= ruleServiceCallArgument ) | ( () otherlv_4= 'own-id' ) | ( () ( (otherlv_6= RULE_ID ) ) ) ) ;
    public final EObject ruleRuleArgument() throws RecognitionException {
        EObject current = null;

        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_literal_1_0 = null;

        EObject this_ServiceCallArgument_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6504:2: ( ( ( () ( (lv_literal_1_0= ruleLiteral ) ) ) | ( ( ( ( ( ruleFQN ) ) '(' ) )=>this_ServiceCallArgument_2= ruleServiceCallArgument ) | ( () otherlv_4= 'own-id' ) | ( () ( (otherlv_6= RULE_ID ) ) ) ) )
            // InternalCqrsDsl.g:6505:2: ( ( () ( (lv_literal_1_0= ruleLiteral ) ) ) | ( ( ( ( ( ruleFQN ) ) '(' ) )=>this_ServiceCallArgument_2= ruleServiceCallArgument ) | ( () otherlv_4= 'own-id' ) | ( () ( (otherlv_6= RULE_ID ) ) ) )
            {
            // InternalCqrsDsl.g:6505:2: ( ( () ( (lv_literal_1_0= ruleLiteral ) ) ) | ( ( ( ( ( ruleFQN ) ) '(' ) )=>this_ServiceCallArgument_2= ruleServiceCallArgument ) | ( () otherlv_4= 'own-id' ) | ( () ( (otherlv_6= RULE_ID ) ) ) )
            int alt186=4;
            switch ( input.LA(1) ) {
            case RULE_STRING:
            case RULE_INT:
            case RULE_HEX:
            case RULE_DECIMAL:
            case 52:
            case 118:
            case 119:
                {
                alt186=1;
                }
                break;
            case RULE_ID:
                {
                int LA186_2 = input.LA(2);

                if ( (LA186_2==50) && (synpred1_InternalCqrsDsl())) {
                    alt186=2;
                }
                else if ( (LA186_2==48) && (synpred1_InternalCqrsDsl())) {
                    alt186=2;
                }
                else if ( (LA186_2==EOF||LA186_2==31||LA186_2==49) ) {
                    alt186=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return current;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 186, 2, input);

                    throw nvae;
                }
                }
                break;
            case 93:
                {
                alt186=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 186, 0, input);

                throw nvae;
            }

            switch (alt186) {
                case 1 :
                    // InternalCqrsDsl.g:6506:3: ( () ( (lv_literal_1_0= ruleLiteral ) ) )
                    {
                    // InternalCqrsDsl.g:6506:3: ( () ( (lv_literal_1_0= ruleLiteral ) ) )
                    // InternalCqrsDsl.g:6507:4: () ( (lv_literal_1_0= ruleLiteral ) )
                    {
                    // InternalCqrsDsl.g:6507:4: ()
                    // InternalCqrsDsl.g:6508:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getRuleArgumentAccess().getLiteralArgumentAction_0_0(),
                      						current);
                      				
                    }

                    }

                    // InternalCqrsDsl.g:6514:4: ( (lv_literal_1_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:6515:5: (lv_literal_1_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:6515:5: (lv_literal_1_0= ruleLiteral )
                    // InternalCqrsDsl.g:6516:6: lv_literal_1_0= ruleLiteral
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getRuleArgumentAccess().getLiteralLiteralParserRuleCall_0_1_0());
                      					
                    }
                    pushFollow(FOLLOW_2);
                    lv_literal_1_0=ruleLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getRuleArgumentRule());
                      						}
                      						set(
                      							current,
                      							"literal",
                      							lv_literal_1_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6535:3: ( ( ( ( ( ruleFQN ) ) '(' ) )=>this_ServiceCallArgument_2= ruleServiceCallArgument )
                    {
                    // InternalCqrsDsl.g:6535:3: ( ( ( ( ( ruleFQN ) ) '(' ) )=>this_ServiceCallArgument_2= ruleServiceCallArgument )
                    // InternalCqrsDsl.g:6536:4: ( ( ( ( ruleFQN ) ) '(' ) )=>this_ServiceCallArgument_2= ruleServiceCallArgument
                    {
                    if ( state.backtracking==0 ) {

                      				newCompositeNode(grammarAccess.getRuleArgumentAccess().getServiceCallArgumentParserRuleCall_1());
                      			
                    }
                    pushFollow(FOLLOW_2);
                    this_ServiceCallArgument_2=ruleServiceCallArgument();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = this_ServiceCallArgument_2;
                      				afterParserOrEnumRuleCall();
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6555:3: ( () otherlv_4= 'own-id' )
                    {
                    // InternalCqrsDsl.g:6555:3: ( () otherlv_4= 'own-id' )
                    // InternalCqrsDsl.g:6556:4: () otherlv_4= 'own-id'
                    {
                    // InternalCqrsDsl.g:6556:4: ()
                    // InternalCqrsDsl.g:6557:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getRuleArgumentAccess().getIdentityArgumentAction_2_0(),
                      						current);
                      				
                    }

                    }

                    otherlv_4=(Token)match(input,93,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getRuleArgumentAccess().getOwnIdKeyword_2_1());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6569:3: ( () ( (otherlv_6= RULE_ID ) ) )
                    {
                    // InternalCqrsDsl.g:6569:3: ( () ( (otherlv_6= RULE_ID ) ) )
                    // InternalCqrsDsl.g:6570:4: () ( (otherlv_6= RULE_ID ) )
                    {
                    // InternalCqrsDsl.g:6570:4: ()
                    // InternalCqrsDsl.g:6571:5: 
                    {
                    if ( state.backtracking==0 ) {

                      					current = forceCreateModelElement(
                      						grammarAccess.getRuleArgumentAccess().getVariableArgumentAction_3_0(),
                      						current);
                      				
                    }

                    }

                    // InternalCqrsDsl.g:6577:4: ( (otherlv_6= RULE_ID ) )
                    // InternalCqrsDsl.g:6578:5: (otherlv_6= RULE_ID )
                    {
                    // InternalCqrsDsl.g:6578:5: (otherlv_6= RULE_ID )
                    // InternalCqrsDsl.g:6579:6: otherlv_6= RULE_ID
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getRuleArgumentRule());
                      						}
                      					
                    }
                    otherlv_6=(Token)match(input,RULE_ID,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_6, grammarAccess.getRuleArgumentAccess().getVariableVariableCrossReference_3_1_0());
                      					
                    }

                    }


                    }


                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleArgument"


    // $ANTLR start "entryRuleServiceCallArgument"
    // InternalCqrsDsl.g:6595:1: entryRuleServiceCallArgument returns [EObject current=null] : iv_ruleServiceCallArgument= ruleServiceCallArgument EOF ;
    public final EObject entryRuleServiceCallArgument() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleServiceCallArgument = null;


        try {
            // InternalCqrsDsl.g:6595:60: (iv_ruleServiceCallArgument= ruleServiceCallArgument EOF )
            // InternalCqrsDsl.g:6596:2: iv_ruleServiceCallArgument= ruleServiceCallArgument EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getServiceCallArgumentRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleServiceCallArgument=ruleServiceCallArgument();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleServiceCallArgument; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleServiceCallArgument"


    // $ANTLR start "ruleServiceCallArgument"
    // InternalCqrsDsl.g:6602:1: ruleServiceCallArgument returns [EObject current=null] : ( ( ( ( ( ( ruleFQN ) ) '(' ) )=> ( ( ( ruleFQN ) ) otherlv_1= '(' ) ) ( ( (otherlv_2= RULE_ID ) ) (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )* )? otherlv_5= ')' ) ;
    public final EObject ruleServiceCallArgument() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_5=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6608:2: ( ( ( ( ( ( ( ruleFQN ) ) '(' ) )=> ( ( ( ruleFQN ) ) otherlv_1= '(' ) ) ( ( (otherlv_2= RULE_ID ) ) (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )* )? otherlv_5= ')' ) )
            // InternalCqrsDsl.g:6609:2: ( ( ( ( ( ( ruleFQN ) ) '(' ) )=> ( ( ( ruleFQN ) ) otherlv_1= '(' ) ) ( ( (otherlv_2= RULE_ID ) ) (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )* )? otherlv_5= ')' )
            {
            // InternalCqrsDsl.g:6609:2: ( ( ( ( ( ( ruleFQN ) ) '(' ) )=> ( ( ( ruleFQN ) ) otherlv_1= '(' ) ) ( ( (otherlv_2= RULE_ID ) ) (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )* )? otherlv_5= ')' )
            // InternalCqrsDsl.g:6610:3: ( ( ( ( ( ruleFQN ) ) '(' ) )=> ( ( ( ruleFQN ) ) otherlv_1= '(' ) ) ( ( (otherlv_2= RULE_ID ) ) (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )* )? otherlv_5= ')'
            {
            // InternalCqrsDsl.g:6610:3: ( ( ( ( ( ruleFQN ) ) '(' ) )=> ( ( ( ruleFQN ) ) otherlv_1= '(' ) )
            // InternalCqrsDsl.g:6611:4: ( ( ( ( ruleFQN ) ) '(' ) )=> ( ( ( ruleFQN ) ) otherlv_1= '(' )
            {
            // InternalCqrsDsl.g:6620:4: ( ( ( ruleFQN ) ) otherlv_1= '(' )
            // InternalCqrsDsl.g:6621:5: ( ( ruleFQN ) ) otherlv_1= '('
            {
            // InternalCqrsDsl.g:6621:5: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:6622:6: ( ruleFQN )
            {
            // InternalCqrsDsl.g:6622:6: ( ruleFQN )
            // InternalCqrsDsl.g:6623:7: ruleFQN
            {
            if ( state.backtracking==0 ) {

              							if (current==null) {
              								current = createModelElement(grammarAccess.getServiceCallArgumentRule());
              							}
              						
            }
            if ( state.backtracking==0 ) {

              							newCompositeNode(grammarAccess.getServiceCallArgumentAccess().getMethodMethodCrossReference_0_0_0_0());
              						
            }
            pushFollow(FOLLOW_69);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              							afterParserOrEnumRuleCall();
              						
            }

            }


            }

            otherlv_1=(Token)match(input,48,FOLLOW_155); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(otherlv_1, grammarAccess.getServiceCallArgumentAccess().getLeftParenthesisKeyword_0_0_1());
              				
            }

            }


            }

            // InternalCqrsDsl.g:6643:3: ( ( (otherlv_2= RULE_ID ) ) (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )* )?
            int alt188=2;
            int LA188_0 = input.LA(1);

            if ( (LA188_0==RULE_ID) ) {
                alt188=1;
            }
            switch (alt188) {
                case 1 :
                    // InternalCqrsDsl.g:6644:4: ( (otherlv_2= RULE_ID ) ) (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )*
                    {
                    // InternalCqrsDsl.g:6644:4: ( (otherlv_2= RULE_ID ) )
                    // InternalCqrsDsl.g:6645:5: (otherlv_2= RULE_ID )
                    {
                    // InternalCqrsDsl.g:6645:5: (otherlv_2= RULE_ID )
                    // InternalCqrsDsl.g:6646:6: otherlv_2= RULE_ID
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getServiceCallArgumentRule());
                      						}
                      					
                    }
                    otherlv_2=(Token)match(input,RULE_ID,FOLLOW_98); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(otherlv_2, grammarAccess.getServiceCallArgumentAccess().getArgsVariableCrossReference_1_0_0());
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:6657:4: (otherlv_3= ',' ( (otherlv_4= RULE_ID ) ) )*
                    loop187:
                    do {
                        int alt187=2;
                        int LA187_0 = input.LA(1);

                        if ( (LA187_0==31) ) {
                            alt187=1;
                        }


                        switch (alt187) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6658:5: otherlv_3= ',' ( (otherlv_4= RULE_ID ) )
                    	    {
                    	    otherlv_3=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getServiceCallArgumentAccess().getCommaKeyword_1_1_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:6662:5: ( (otherlv_4= RULE_ID ) )
                    	    // InternalCqrsDsl.g:6663:6: (otherlv_4= RULE_ID )
                    	    {
                    	    // InternalCqrsDsl.g:6663:6: (otherlv_4= RULE_ID )
                    	    // InternalCqrsDsl.g:6664:7: otherlv_4= RULE_ID
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElement(grammarAccess.getServiceCallArgumentRule());
                    	      							}
                    	      						
                    	    }
                    	    otherlv_4=(Token)match(input,RULE_ID,FOLLOW_98); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							newLeafNode(otherlv_4, grammarAccess.getServiceCallArgumentAccess().getArgsVariableCrossReference_1_1_1_0());
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop187;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getServiceCallArgumentAccess().getRightParenthesisKeyword_2());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleServiceCallArgument"


    // $ANTLR start "entryRuleAnnotationInstance"
    // InternalCqrsDsl.g:6685:1: entryRuleAnnotationInstance returns [EObject current=null] : iv_ruleAnnotationInstance= ruleAnnotationInstance EOF ;
    public final EObject entryRuleAnnotationInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnnotationInstance = null;


        try {
            // InternalCqrsDsl.g:6685:59: (iv_ruleAnnotationInstance= ruleAnnotationInstance EOF )
            // InternalCqrsDsl.g:6686:2: iv_ruleAnnotationInstance= ruleAnnotationInstance EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getAnnotationInstanceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleAnnotationInstance=ruleAnnotationInstance();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleAnnotationInstance; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAnnotationInstance"


    // $ANTLR start "ruleAnnotationInstance"
    // InternalCqrsDsl.g:6692:1: ruleAnnotationInstance returns [EObject current=null] : (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? ) ;
    public final EObject ruleAnnotationInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_params_3_0 = null;

        EObject lv_params_5_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6698:2: ( (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? ) )
            // InternalCqrsDsl.g:6699:2: (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? )
            {
            // InternalCqrsDsl.g:6699:2: (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? )
            // InternalCqrsDsl.g:6700:3: otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )?
            {
            otherlv_0=(Token)match(input,94,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_0, grammarAccess.getAnnotationInstanceAccess().getCommercialAtKeyword_0());
              		
            }
            // InternalCqrsDsl.g:6704:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:6705:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:6705:4: ( ruleFQN )
            // InternalCqrsDsl.g:6706:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getAnnotationInstanceRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getAnnotationAnnotationCrossReference_1_0());
              				
            }
            pushFollow(FOLLOW_153);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:6720:3: (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )?
            int alt190=2;
            int LA190_0 = input.LA(1);

            if ( (LA190_0==48) ) {
                alt190=1;
            }
            switch (alt190) {
                case 1 :
                    // InternalCqrsDsl.g:6721:4: otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')'
                    {
                    otherlv_2=(Token)match(input,48,FOLLOW_97); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_2, grammarAccess.getAnnotationInstanceAccess().getLeftParenthesisKeyword_2_0());
                      			
                    }
                    // InternalCqrsDsl.g:6725:4: ( (lv_params_3_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:6726:5: (lv_params_3_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:6726:5: (lv_params_3_0= ruleLiteral )
                    // InternalCqrsDsl.g:6727:6: lv_params_3_0= ruleLiteral
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getParamsLiteralParserRuleCall_2_1_0());
                      					
                    }
                    pushFollow(FOLLOW_98);
                    lv_params_3_0=ruleLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getAnnotationInstanceRule());
                      						}
                      						add(
                      							current,
                      							"params",
                      							lv_params_3_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:6744:4: (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )*
                    loop189:
                    do {
                        int alt189=2;
                        int LA189_0 = input.LA(1);

                        if ( (LA189_0==31) ) {
                            alt189=1;
                        }


                        switch (alt189) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6745:5: otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) )
                    	    {
                    	    otherlv_4=(Token)match(input,31,FOLLOW_97); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_4, grammarAccess.getAnnotationInstanceAccess().getCommaKeyword_2_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:6749:5: ( (lv_params_5_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:6750:6: (lv_params_5_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:6750:6: (lv_params_5_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:6751:7: lv_params_5_0= ruleLiteral
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getParamsLiteralParserRuleCall_2_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_98);
                    	    lv_params_5_0=ruleLiteral();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getAnnotationInstanceRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"params",
                    	      								lv_params_5_0,
                    	      								"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop189;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,49,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_6, grammarAccess.getAnnotationInstanceAccess().getRightParenthesisKeyword_2_3());
                      			
                    }

                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAnnotationInstance"


    // $ANTLR start "entryRuleService"
    // InternalCqrsDsl.g:6778:1: entryRuleService returns [EObject current=null] : iv_ruleService= ruleService EOF ;
    public final EObject entryRuleService() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleService = null;


        try {
            // InternalCqrsDsl.g:6778:48: (iv_ruleService= ruleService EOF )
            // InternalCqrsDsl.g:6779:2: iv_ruleService= ruleService EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getServiceRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleService=ruleService();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleService; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleService"


    // $ANTLR start "ruleService"
    // InternalCqrsDsl.g:6785:1: ruleService returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' ) ;
    public final EObject ruleService() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_6=null;
        EObject lv_businessRules_4_0 = null;

        EObject lv_methods_5_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6791:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' ) )
            // InternalCqrsDsl.g:6792:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' )
            {
            // InternalCqrsDsl.g:6792:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' )
            // InternalCqrsDsl.g:6793:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}'
            {
            // InternalCqrsDsl.g:6793:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt191=2;
            int LA191_0 = input.LA(1);

            if ( (LA191_0==RULE_DOC) ) {
                alt191=1;
            }
            switch (alt191) {
                case 1 :
                    // InternalCqrsDsl.g:6794:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:6794:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:6795:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_156); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getServiceAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getServiceRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,95,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getServiceAccess().getServiceKeyword_1());
              		
            }
            // InternalCqrsDsl.g:6815:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:6816:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:6816:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:6817:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getServiceAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getServiceRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_82); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getServiceAccess().getLeftCurlyBracketKeyword_3());
              		
            }
            // InternalCqrsDsl.g:6837:3: ( (lv_businessRules_4_0= ruleBusinessRule ) )*
            loop192:
            do {
                int alt192=2;
                int LA192_0 = input.LA(1);

                if ( (LA192_0==RULE_DOC) ) {
                    int LA192_1 = input.LA(2);

                    if ( (LA192_1==43) ) {
                        alt192=1;
                    }


                }


                switch (alt192) {
            	case 1 :
            	    // InternalCqrsDsl.g:6838:4: (lv_businessRules_4_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:6838:4: (lv_businessRules_4_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:6839:5: lv_businessRules_4_0= ruleBusinessRule
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getServiceAccess().getBusinessRulesBusinessRuleParserRuleCall_4_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_82);
            	    lv_businessRules_4_0=ruleBusinessRule();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getServiceRule());
            	      					}
            	      					add(
            	      						current,
            	      						"businessRules",
            	      						lv_businessRules_4_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRule");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop192;
                }
            } while (true);

            // InternalCqrsDsl.g:6856:3: ( (lv_methods_5_0= ruleMethod ) )*
            loop193:
            do {
                int alt193=2;
                int LA193_0 = input.LA(1);

                if ( (LA193_0==RULE_DOC||LA193_0==80) ) {
                    alt193=1;
                }


                switch (alt193) {
            	case 1 :
            	    // InternalCqrsDsl.g:6857:4: (lv_methods_5_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:6857:4: (lv_methods_5_0= ruleMethod )
            	    // InternalCqrsDsl.g:6858:5: lv_methods_5_0= ruleMethod
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getServiceAccess().getMethodsMethodParserRuleCall_5_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_82);
            	    lv_methods_5_0=ruleMethod();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getServiceRule());
            	      					}
            	      					add(
            	      						current,
            	      						"methods",
            	      						lv_methods_5_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop193;
                }
            } while (true);

            otherlv_6=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_6, grammarAccess.getServiceAccess().getRightCurlyBracketKeyword_6());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleService"


    // $ANTLR start "entryRuleCommand"
    // InternalCqrsDsl.g:6883:1: entryRuleCommand returns [EObject current=null] : iv_ruleCommand= ruleCommand EOF ;
    public final EObject entryRuleCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommand = null;


        try {
            // InternalCqrsDsl.g:6883:48: (iv_ruleCommand= ruleCommand EOF )
            // InternalCqrsDsl.g:6884:2: iv_ruleCommand= ruleCommand EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getCommandRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleCommand=ruleCommand();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleCommand; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCommand"


    // $ANTLR start "ruleCommand"
    // InternalCqrsDsl.g:6890:1: ruleCommand returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) ;
    public final EObject ruleCommand() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_11=null;
        Token lv_message_12_0=null;
        Token otherlv_13=null;
        EObject lv_acceptable_6_0 = null;

        EObject lv_metaInfo_8_0 = null;

        EObject lv_hints_9_0 = null;

        EObject lv_attributes_10_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6896:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) )
            // InternalCqrsDsl.g:6897:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:6897:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            // InternalCqrsDsl.g:6898:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}'
            {
            // InternalCqrsDsl.g:6898:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt194=2;
            int LA194_0 = input.LA(1);

            if ( (LA194_0==RULE_DOC) ) {
                alt194=1;
            }
            switch (alt194) {
                case 1 :
                    // InternalCqrsDsl.g:6899:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:6899:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:6900:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_157); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getCommandAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getCommandRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,96,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getCommandAccess().getCommandKeyword_1());
              		
            }
            // InternalCqrsDsl.g:6920:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:6921:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:6921:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:6922:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_158); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getCommandAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getCommandRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:6938:3: (otherlv_3= 'target' ( ( ruleFQN ) ) )?
            int alt195=2;
            int LA195_0 = input.LA(1);

            if ( (LA195_0==97) ) {
                alt195=1;
            }
            switch (alt195) {
                case 1 :
                    // InternalCqrsDsl.g:6939:4: otherlv_3= 'target' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,97,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getCommandAccess().getTargetKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:6943:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:6944:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:6944:5: ( ruleFQN )
                    // InternalCqrsDsl.g:6945:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getCommandRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getCommandAccess().getTargetAbstractMethodCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_159);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:6960:3: (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )?
            int alt196=2;
            int LA196_0 = input.LA(1);

            if ( (LA196_0==98) ) {
                alt196=1;
            }
            switch (alt196) {
                case 1 :
                    // InternalCqrsDsl.g:6961:4: otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) )
                    {
                    otherlv_5=(Token)match(input,98,FOLLOW_22); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getCommandAccess().getSlaKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:6965:4: ( (lv_acceptable_6_0= ruleDuration ) )
                    // InternalCqrsDsl.g:6966:5: (lv_acceptable_6_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:6966:5: (lv_acceptable_6_0= ruleDuration )
                    // InternalCqrsDsl.g:6967:6: lv_acceptable_6_0= ruleDuration
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getCommandAccess().getAcceptableDurationParserRuleCall_4_1_0());
                      					
                    }
                    pushFollow(FOLLOW_5);
                    lv_acceptable_6_0=ruleDuration();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getCommandRule());
                      						}
                      						set(
                      							current,
                      							"acceptable",
                      							lv_acceptable_6_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.Duration");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_7=(Token)match(input,14,FOLLOW_160); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getCommandAccess().getLeftCurlyBracketKeyword_5());
              		
            }
            // InternalCqrsDsl.g:6989:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:6990:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:6990:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:6991:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getCommandAccess().getMetaInfoTypeMetaInfoParserRuleCall_6_0());
              				
            }
            pushFollow(FOLLOW_161);
            lv_metaInfo_8_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getCommandRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_8_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:7008:3: ( (lv_hints_9_0= ruleHint ) )*
            loop197:
            do {
                int alt197=2;
                int LA197_0 = input.LA(1);

                if ( (LA197_0==RULE_DOC) ) {
                    int LA197_1 = input.LA(2);

                    if ( (LA197_1==20) ) {
                        alt197=1;
                    }


                }
                else if ( (LA197_0==20) ) {
                    alt197=1;
                }


                switch (alt197) {
            	case 1 :
            	    // InternalCqrsDsl.g:7009:4: (lv_hints_9_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:7009:4: (lv_hints_9_0= ruleHint )
            	    // InternalCqrsDsl.g:7010:5: lv_hints_9_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getCommandAccess().getHintsHintParserRuleCall_7_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_161);
            	    lv_hints_9_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getCommandRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_9_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop197;
                }
            } while (true);

            // InternalCqrsDsl.g:7027:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop198:
            do {
                int alt198=2;
                int LA198_0 = input.LA(1);

                if ( ((LA198_0>=RULE_DOC && LA198_0<=RULE_ID)||LA198_0==79) ) {
                    alt198=1;
                }


                switch (alt198) {
            	case 1 :
            	    // InternalCqrsDsl.g:7028:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:7028:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:7029:5: lv_attributes_10_0= ruleAttribute
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getCommandAccess().getAttributesAttributeParserRuleCall_8_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_57);
            	    lv_attributes_10_0=ruleAttribute();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getCommandRule());
            	      					}
            	      					add(
            	      						current,
            	      						"attributes",
            	      						lv_attributes_10_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop198;
                }
            } while (true);

            // InternalCqrsDsl.g:7046:3: (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )?
            int alt199=2;
            int LA199_0 = input.LA(1);

            if ( (LA199_0==42) ) {
                alt199=1;
            }
            switch (alt199) {
                case 1 :
                    // InternalCqrsDsl.g:7047:4: otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) )
                    {
                    otherlv_11=(Token)match(input,42,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getCommandAccess().getMessageKeyword_9_0());
                      			
                    }
                    // InternalCqrsDsl.g:7051:4: ( (lv_message_12_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:7052:5: (lv_message_12_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:7052:5: (lv_message_12_0= RULE_STRING )
                    // InternalCqrsDsl.g:7053:6: lv_message_12_0= RULE_STRING
                    {
                    lv_message_12_0=(Token)match(input,RULE_STRING,FOLLOW_35); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_message_12_0, grammarAccess.getCommandAccess().getMessageSTRINGTerminalRuleCall_9_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getCommandRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"message",
                      							lv_message_12_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_13=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_13, grammarAccess.getCommandAccess().getRightCurlyBracketKeyword_10());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCommand"


    // $ANTLR start "entryRuleCommandHandler"
    // InternalCqrsDsl.g:7078:1: entryRuleCommandHandler returns [EObject current=null] : iv_ruleCommandHandler= ruleCommandHandler EOF ;
    public final EObject entryRuleCommandHandler() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommandHandler = null;


        try {
            // InternalCqrsDsl.g:7078:55: (iv_ruleCommandHandler= ruleCommandHandler EOF )
            // InternalCqrsDsl.g:7079:2: iv_ruleCommandHandler= ruleCommandHandler EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getCommandHandlerRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleCommandHandler=ruleCommandHandler();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleCommandHandler; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCommandHandler"


    // $ANTLR start "ruleCommandHandler"
    // InternalCqrsDsl.g:7085:1: ruleCommandHandler returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? ) ;
    public final EObject ruleCommandHandler() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7091:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? ) )
            // InternalCqrsDsl.g:7092:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? )
            {
            // InternalCqrsDsl.g:7092:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? )
            // InternalCqrsDsl.g:7093:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            {
            // InternalCqrsDsl.g:7093:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt200=2;
            int LA200_0 = input.LA(1);

            if ( (LA200_0==RULE_DOC) ) {
                alt200=1;
            }
            switch (alt200) {
                case 1 :
                    // InternalCqrsDsl.g:7094:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:7094:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:7095:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_162); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getCommandHandlerAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getCommandHandlerRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,99,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getCommandHandlerAccess().getCommandHandlerKeyword_1());
              		
            }
            // InternalCqrsDsl.g:7115:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:7116:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:7116:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:7117:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_163); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getCommandHandlerAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getCommandHandlerRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            otherlv_3=(Token)match(input,100,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getCommandHandlerAccess().getHandlesKeyword_3());
              		
            }
            // InternalCqrsDsl.g:7137:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:7138:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:7138:4: ( ruleFQN )
            // InternalCqrsDsl.g:7139:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getCommandHandlerRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getCommandHandlerAccess().getCommandsCommandCrossReference_4_0());
              				
            }
            pushFollow(FOLLOW_164);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:7153:3: (otherlv_5= ',' ( ( ruleFQN ) ) )*
            loop201:
            do {
                int alt201=2;
                int LA201_0 = input.LA(1);

                if ( (LA201_0==31) ) {
                    alt201=1;
                }


                switch (alt201) {
            	case 1 :
            	    // InternalCqrsDsl.g:7154:4: otherlv_5= ',' ( ( ruleFQN ) )
            	    {
            	    otherlv_5=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(otherlv_5, grammarAccess.getCommandHandlerAccess().getCommaKeyword_5_0());
            	      			
            	    }
            	    // InternalCqrsDsl.g:7158:4: ( ( ruleFQN ) )
            	    // InternalCqrsDsl.g:7159:5: ( ruleFQN )
            	    {
            	    // InternalCqrsDsl.g:7159:5: ( ruleFQN )
            	    // InternalCqrsDsl.g:7160:6: ruleFQN
            	    {
            	    if ( state.backtracking==0 ) {

            	      						if (current==null) {
            	      							current = createModelElement(grammarAccess.getCommandHandlerRule());
            	      						}
            	      					
            	    }
            	    if ( state.backtracking==0 ) {

            	      						newCompositeNode(grammarAccess.getCommandHandlerAccess().getCommandsCommandCrossReference_5_1_0());
            	      					
            	    }
            	    pushFollow(FOLLOW_164);
            	    ruleFQN();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      						afterParserOrEnumRuleCall();
            	      					
            	    }

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop201;
                }
            } while (true);

            // InternalCqrsDsl.g:7175:3: (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            int alt203=2;
            int LA203_0 = input.LA(1);

            if ( (LA203_0==101) ) {
                alt203=1;
            }
            switch (alt203) {
                case 1 :
                    // InternalCqrsDsl.g:7176:4: otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_7=(Token)match(input,101,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_7, grammarAccess.getCommandHandlerAccess().getUsesKeyword_6_0());
                      			
                    }
                    // InternalCqrsDsl.g:7180:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:7181:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:7181:5: ( ruleFQN )
                    // InternalCqrsDsl.g:7182:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getCommandHandlerRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getCommandHandlerAccess().getAggregatesAggregateCrossReference_6_1_0());
                      					
                    }
                    pushFollow(FOLLOW_150);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:7196:4: (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    loop202:
                    do {
                        int alt202=2;
                        int LA202_0 = input.LA(1);

                        if ( (LA202_0==31) ) {
                            alt202=1;
                        }


                        switch (alt202) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:7197:5: otherlv_9= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_9=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_9, grammarAccess.getCommandHandlerAccess().getCommaKeyword_6_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:7201:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:7202:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:7202:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:7203:7: ruleFQN
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElement(grammarAccess.getCommandHandlerRule());
                    	      							}
                    	      						
                    	    }
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getCommandHandlerAccess().getAggregatesAggregateCrossReference_6_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_150);
                    	    ruleFQN();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop202;
                        }
                    } while (true);


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCommandHandler"


    // $ANTLR start "entryRuleProjection"
    // InternalCqrsDsl.g:7223:1: entryRuleProjection returns [EObject current=null] : iv_ruleProjection= ruleProjection EOF ;
    public final EObject entryRuleProjection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProjection = null;


        try {
            // InternalCqrsDsl.g:7223:51: (iv_ruleProjection= ruleProjection EOF )
            // InternalCqrsDsl.g:7224:2: iv_ruleProjection= ruleProjection EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getProjectionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleProjection=ruleProjection();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleProjection; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleProjection"


    // $ANTLR start "ruleProjection"
    // InternalCqrsDsl.g:7230:1: ruleProjection returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? ) ;
    public final EObject ruleProjection() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7236:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? ) )
            // InternalCqrsDsl.g:7237:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? )
            {
            // InternalCqrsDsl.g:7237:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? )
            // InternalCqrsDsl.g:7238:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )?
            {
            // InternalCqrsDsl.g:7238:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt204=2;
            int LA204_0 = input.LA(1);

            if ( (LA204_0==RULE_DOC) ) {
                alt204=1;
            }
            switch (alt204) {
                case 1 :
                    // InternalCqrsDsl.g:7239:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:7239:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:7240:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_165); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getProjectionAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getProjectionRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,102,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getProjectionAccess().getProjectionKeyword_1());
              		
            }
            // InternalCqrsDsl.g:7260:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:7261:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:7261:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:7262:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_166); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getProjectionAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getProjectionRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            // InternalCqrsDsl.g:7278:3: (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )?
            int alt206=2;
            int LA206_0 = input.LA(1);

            if ( (LA206_0==39) ) {
                alt206=1;
            }
            switch (alt206) {
                case 1 :
                    // InternalCqrsDsl.g:7279:4: otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_3=(Token)match(input,39,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getProjectionAccess().getInputKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:7283:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:7284:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:7284:5: ( ruleFQN )
                    // InternalCqrsDsl.g:7285:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getProjectionRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getProjectionAccess().getEventsEventCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_150);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:7299:4: (otherlv_5= ',' ( ( ruleFQN ) ) )*
                    loop205:
                    do {
                        int alt205=2;
                        int LA205_0 = input.LA(1);

                        if ( (LA205_0==31) ) {
                            alt205=1;
                        }


                        switch (alt205) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:7300:5: otherlv_5= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_5=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_5, grammarAccess.getProjectionAccess().getCommaKeyword_3_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:7304:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:7305:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:7305:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:7306:7: ruleFQN
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElement(grammarAccess.getProjectionRule());
                    	      							}
                    	      						
                    	    }
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getProjectionAccess().getEventsEventCrossReference_3_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_150);
                    	    ruleFQN();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop205;
                        }
                    } while (true);


                    }
                    break;

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleProjection"


    // $ANTLR start "entryRuleView"
    // InternalCqrsDsl.g:7326:1: entryRuleView returns [EObject current=null] : iv_ruleView= ruleView EOF ;
    public final EObject entryRuleView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleView = null;


        try {
            // InternalCqrsDsl.g:7326:45: (iv_ruleView= ruleView EOF )
            // InternalCqrsDsl.g:7327:2: iv_ruleView= ruleView EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getViewRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleView=ruleView();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleView; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleView"


    // $ANTLR start "ruleView"
    // InternalCqrsDsl.g:7333:1: ruleView returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) (otherlv_5= 'rest-path' ( (lv_restPath_6_0= RULE_STRING ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* (otherlv_10= 'cron-schedule' ( (lv_cron_11_0= RULE_STRING ) ) )? ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) ;
    public final EObject ruleView() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token lv_restPath_6_0=null;
        Token otherlv_7=null;
        Token otherlv_10=null;
        Token lv_cron_11_0=null;
        Token otherlv_14=null;
        EObject lv_metaInfo_8_0 = null;

        EObject lv_hints_9_0 = null;

        EObject lv_businessRules_12_0 = null;

        EObject lv_methods_13_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:7339:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) (otherlv_5= 'rest-path' ( (lv_restPath_6_0= RULE_STRING ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* (otherlv_10= 'cron-schedule' ( (lv_cron_11_0= RULE_STRING ) ) )? ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:7340:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) (otherlv_5= 'rest-path' ( (lv_restPath_6_0= RULE_STRING ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* (otherlv_10= 'cron-schedule' ( (lv_cron_11_0= RULE_STRING ) ) )? ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:7340:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) (otherlv_5= 'rest-path' ( (lv_restPath_6_0= RULE_STRING ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* (otherlv_10= 'cron-schedule' ( (lv_cron_11_0= RULE_STRING ) ) )? ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:7341:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) (otherlv_5= 'rest-path' ( (lv_restPath_6_0= RULE_STRING ) ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_hints_9_0= ruleHint ) )* (otherlv_10= 'cron-schedule' ( (lv_cron_11_0= RULE_STRING ) ) )? ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:7341:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt207=2;
            int LA207_0 = input.LA(1);

            if ( (LA207_0==RULE_DOC) ) {
                alt207=1;
            }
            switch (alt207) {
                case 1 :
                    // InternalCqrsDsl.g:7342:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:7342:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:7343:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_167); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getViewAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getViewRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,103,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getViewAccess().getViewKeyword_1());
              		
            }
            // InternalCqrsDsl.g:7363:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:7364:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:7364:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:7365:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_168); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getViewAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getViewRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            otherlv_3=(Token)match(input,101,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getViewAccess().getUsesKeyword_3());
              		
            }
            // InternalCqrsDsl.g:7385:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:7386:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:7386:4: ( ruleFQN )
            // InternalCqrsDsl.g:7387:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getViewRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getViewAccess().getProjectionProjectionCrossReference_4_0());
              				
            }
            pushFollow(FOLLOW_169);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:7401:3: (otherlv_5= 'rest-path' ( (lv_restPath_6_0= RULE_STRING ) ) )?
            int alt208=2;
            int LA208_0 = input.LA(1);

            if ( (LA208_0==82) ) {
                alt208=1;
            }
            switch (alt208) {
                case 1 :
                    // InternalCqrsDsl.g:7402:4: otherlv_5= 'rest-path' ( (lv_restPath_6_0= RULE_STRING ) )
                    {
                    otherlv_5=(Token)match(input,82,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_5, grammarAccess.getViewAccess().getRestPathKeyword_5_0());
                      			
                    }
                    // InternalCqrsDsl.g:7406:4: ( (lv_restPath_6_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:7407:5: (lv_restPath_6_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:7407:5: (lv_restPath_6_0= RULE_STRING )
                    // InternalCqrsDsl.g:7408:6: lv_restPath_6_0= RULE_STRING
                    {
                    lv_restPath_6_0=(Token)match(input,RULE_STRING,FOLLOW_5); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_restPath_6_0, grammarAccess.getViewAccess().getRestPathSTRINGTerminalRuleCall_5_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getViewRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"restPath",
                      							lv_restPath_6_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_7=(Token)match(input,14,FOLLOW_170); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_7, grammarAccess.getViewAccess().getLeftCurlyBracketKeyword_6());
              		
            }
            // InternalCqrsDsl.g:7429:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:7430:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:7430:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:7431:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getViewAccess().getMetaInfoTypeMetaInfoParserRuleCall_7_0());
              				
            }
            pushFollow(FOLLOW_171);
            lv_metaInfo_8_0=ruleTypeMetaInfo();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getViewRule());
              					}
              					set(
              						current,
              						"metaInfo",
              						lv_metaInfo_8_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.TypeMetaInfo");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:7448:3: ( (lv_hints_9_0= ruleHint ) )*
            loop209:
            do {
                int alt209=2;
                int LA209_0 = input.LA(1);

                if ( (LA209_0==RULE_DOC) ) {
                    int LA209_2 = input.LA(2);

                    if ( (LA209_2==20) ) {
                        alt209=1;
                    }


                }
                else if ( (LA209_0==20) ) {
                    alt209=1;
                }


                switch (alt209) {
            	case 1 :
            	    // InternalCqrsDsl.g:7449:4: (lv_hints_9_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:7449:4: (lv_hints_9_0= ruleHint )
            	    // InternalCqrsDsl.g:7450:5: lv_hints_9_0= ruleHint
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getViewAccess().getHintsHintParserRuleCall_8_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_171);
            	    lv_hints_9_0=ruleHint();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getViewRule());
            	      					}
            	      					add(
            	      						current,
            	      						"hints",
            	      						lv_hints_9_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop209;
                }
            } while (true);

            // InternalCqrsDsl.g:7467:3: (otherlv_10= 'cron-schedule' ( (lv_cron_11_0= RULE_STRING ) ) )?
            int alt210=2;
            int LA210_0 = input.LA(1);

            if ( (LA210_0==104) ) {
                alt210=1;
            }
            switch (alt210) {
                case 1 :
                    // InternalCqrsDsl.g:7468:4: otherlv_10= 'cron-schedule' ( (lv_cron_11_0= RULE_STRING ) )
                    {
                    otherlv_10=(Token)match(input,104,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_10, grammarAccess.getViewAccess().getCronScheduleKeyword_9_0());
                      			
                    }
                    // InternalCqrsDsl.g:7472:4: ( (lv_cron_11_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:7473:5: (lv_cron_11_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:7473:5: (lv_cron_11_0= RULE_STRING )
                    // InternalCqrsDsl.g:7474:6: lv_cron_11_0= RULE_STRING
                    {
                    lv_cron_11_0=(Token)match(input,RULE_STRING,FOLLOW_82); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_cron_11_0, grammarAccess.getViewAccess().getCronSTRINGTerminalRuleCall_9_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getViewRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"cron",
                      							lv_cron_11_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:7491:3: ( (lv_businessRules_12_0= ruleBusinessRule ) )*
            loop211:
            do {
                int alt211=2;
                int LA211_0 = input.LA(1);

                if ( (LA211_0==RULE_DOC) ) {
                    int LA211_1 = input.LA(2);

                    if ( (LA211_1==43) ) {
                        alt211=1;
                    }


                }


                switch (alt211) {
            	case 1 :
            	    // InternalCqrsDsl.g:7492:4: (lv_businessRules_12_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:7492:4: (lv_businessRules_12_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:7493:5: lv_businessRules_12_0= ruleBusinessRule
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getViewAccess().getBusinessRulesBusinessRuleParserRuleCall_10_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_82);
            	    lv_businessRules_12_0=ruleBusinessRule();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getViewRule());
            	      					}
            	      					add(
            	      						current,
            	      						"businessRules",
            	      						lv_businessRules_12_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRule");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop211;
                }
            } while (true);

            // InternalCqrsDsl.g:7510:3: ( (lv_methods_13_0= ruleMethod ) )*
            loop212:
            do {
                int alt212=2;
                int LA212_0 = input.LA(1);

                if ( (LA212_0==RULE_DOC||LA212_0==80) ) {
                    alt212=1;
                }


                switch (alt212) {
            	case 1 :
            	    // InternalCqrsDsl.g:7511:4: (lv_methods_13_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:7511:4: (lv_methods_13_0= ruleMethod )
            	    // InternalCqrsDsl.g:7512:5: lv_methods_13_0= ruleMethod
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getViewAccess().getMethodsMethodParserRuleCall_11_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_82);
            	    lv_methods_13_0=ruleMethod();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getViewRule());
            	      					}
            	      					add(
            	      						current,
            	      						"methods",
            	      						lv_methods_13_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop212;
                }
            } while (true);

            otherlv_14=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_14, grammarAccess.getViewAccess().getRightCurlyBracketKeyword_12());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleView"


    // $ANTLR start "entryRuleProcessManager"
    // InternalCqrsDsl.g:7537:1: entryRuleProcessManager returns [EObject current=null] : iv_ruleProcessManager= ruleProcessManager EOF ;
    public final EObject entryRuleProcessManager() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProcessManager = null;


        try {
            // InternalCqrsDsl.g:7537:55: (iv_ruleProcessManager= ruleProcessManager EOF )
            // InternalCqrsDsl.g:7538:2: iv_ruleProcessManager= ruleProcessManager EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getProcessManagerRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleProcessManager=ruleProcessManager();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleProcessManager; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleProcessManager"


    // $ANTLR start "ruleProcessManager"
    // InternalCqrsDsl.g:7544:1: ruleProcessManager returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'instance-key' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' ) ;
    public final EObject ruleProcessManager() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token lv_cron_5_0=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_13=null;
        EObject lv_states_10_0 = null;

        EObject lv_reactions_12_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:7550:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'instance-key' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' ) )
            // InternalCqrsDsl.g:7551:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'instance-key' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:7551:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'instance-key' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' )
            // InternalCqrsDsl.g:7552:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'instance-key' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}'
            {
            // InternalCqrsDsl.g:7552:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt213=2;
            int LA213_0 = input.LA(1);

            if ( (LA213_0==RULE_DOC) ) {
                alt213=1;
            }
            switch (alt213) {
                case 1 :
                    // InternalCqrsDsl.g:7553:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:7553:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:7554:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_172); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getProcessManagerAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getProcessManagerRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,105,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getProcessManagerAccess().getProcessManagerKeyword_1());
              		
            }
            // InternalCqrsDsl.g:7574:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:7575:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:7575:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:7576:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_2_0, grammarAccess.getProcessManagerAccess().getNameIDTerminalRuleCall_2_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getProcessManagerRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }

            otherlv_3=(Token)match(input,14,FOLLOW_173); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_3, grammarAccess.getProcessManagerAccess().getLeftCurlyBracketKeyword_3());
              		
            }
            // InternalCqrsDsl.g:7596:3: (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )?
            int alt214=2;
            int LA214_0 = input.LA(1);

            if ( (LA214_0==104) ) {
                alt214=1;
            }
            switch (alt214) {
                case 1 :
                    // InternalCqrsDsl.g:7597:4: otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) )
                    {
                    otherlv_4=(Token)match(input,104,FOLLOW_15); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_4, grammarAccess.getProcessManagerAccess().getCronScheduleKeyword_4_0());
                      			
                    }
                    // InternalCqrsDsl.g:7601:4: ( (lv_cron_5_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:7602:5: (lv_cron_5_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:7602:5: (lv_cron_5_0= RULE_STRING )
                    // InternalCqrsDsl.g:7603:6: lv_cron_5_0= RULE_STRING
                    {
                    lv_cron_5_0=(Token)match(input,RULE_STRING,FOLLOW_174); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_cron_5_0, grammarAccess.getProcessManagerAccess().getCronSTRINGTerminalRuleCall_4_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getProcessManagerRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"cron",
                      							lv_cron_5_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:7620:3: (otherlv_6= 'instance-key' ( ( ruleFQN ) ) )?
            int alt215=2;
            int LA215_0 = input.LA(1);

            if ( (LA215_0==106) ) {
                alt215=1;
            }
            switch (alt215) {
                case 1 :
                    // InternalCqrsDsl.g:7621:4: otherlv_6= 'instance-key' ( ( ruleFQN ) )
                    {
                    otherlv_6=(Token)match(input,106,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_6, grammarAccess.getProcessManagerAccess().getInstanceKeyKeyword_5_0());
                      			
                    }
                    // InternalCqrsDsl.g:7625:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:7626:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:7626:5: ( ruleFQN )
                    // InternalCqrsDsl.g:7627:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getProcessManagerRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getProcessManagerAccess().getInstanceKeyTypeCrossReference_5_1_0());
                      					
                    }
                    pushFollow(FOLLOW_175);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:7642:3: (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )?
            int alt217=2;
            int LA217_0 = input.LA(1);

            if ( (LA217_0==107) ) {
                alt217=1;
            }
            switch (alt217) {
                case 1 :
                    // InternalCqrsDsl.g:7643:4: otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}'
                    {
                    otherlv_8=(Token)match(input,107,FOLLOW_5); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getProcessManagerAccess().getProcessStatesKeyword_6_0());
                      			
                    }
                    otherlv_9=(Token)match(input,14,FOLLOW_176); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_9, grammarAccess.getProcessManagerAccess().getLeftCurlyBracketKeyword_6_1());
                      			
                    }
                    // InternalCqrsDsl.g:7651:4: ( (lv_states_10_0= ruleProcessState ) )+
                    int cnt216=0;
                    loop216:
                    do {
                        int alt216=2;
                        int LA216_0 = input.LA(1);

                        if ( ((LA216_0>=RULE_DOC && LA216_0<=RULE_ID)) ) {
                            alt216=1;
                        }


                        switch (alt216) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:7652:5: (lv_states_10_0= ruleProcessState )
                    	    {
                    	    // InternalCqrsDsl.g:7652:5: (lv_states_10_0= ruleProcessState )
                    	    // InternalCqrsDsl.g:7653:6: lv_states_10_0= ruleProcessState
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      						newCompositeNode(grammarAccess.getProcessManagerAccess().getStatesProcessStateParserRuleCall_6_2_0());
                    	      					
                    	    }
                    	    pushFollow(FOLLOW_177);
                    	    lv_states_10_0=ruleProcessState();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      						if (current==null) {
                    	      							current = createModelElementForParent(grammarAccess.getProcessManagerRule());
                    	      						}
                    	      						add(
                    	      							current,
                    	      							"states",
                    	      							lv_states_10_0,
                    	      							"org.fuin.dsl.cqrs.CqrsDsl.ProcessState");
                    	      						afterParserOrEnumRuleCall();
                    	      					
                    	    }

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt216 >= 1 ) break loop216;
                    	    if (state.backtracking>0) {state.failed=true; return current;}
                                EarlyExitException eee =
                                    new EarlyExitException(216, input);
                                throw eee;
                        }
                        cnt216++;
                    } while (true);

                    otherlv_11=(Token)match(input,15,FOLLOW_178); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_11, grammarAccess.getProcessManagerAccess().getRightCurlyBracketKeyword_6_3());
                      			
                    }

                    }
                    break;

            }

            // InternalCqrsDsl.g:7675:3: ( (lv_reactions_12_0= ruleProcessReaction ) )*
            loop218:
            do {
                int alt218=2;
                int LA218_0 = input.LA(1);

                if ( (LA218_0==RULE_DOC||LA218_0==108) ) {
                    alt218=1;
                }


                switch (alt218) {
            	case 1 :
            	    // InternalCqrsDsl.g:7676:4: (lv_reactions_12_0= ruleProcessReaction )
            	    {
            	    // InternalCqrsDsl.g:7676:4: (lv_reactions_12_0= ruleProcessReaction )
            	    // InternalCqrsDsl.g:7677:5: lv_reactions_12_0= ruleProcessReaction
            	    {
            	    if ( state.backtracking==0 ) {

            	      					newCompositeNode(grammarAccess.getProcessManagerAccess().getReactionsProcessReactionParserRuleCall_7_0());
            	      				
            	    }
            	    pushFollow(FOLLOW_178);
            	    lv_reactions_12_0=ruleProcessReaction();

            	    state._fsp--;
            	    if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      					if (current==null) {
            	      						current = createModelElementForParent(grammarAccess.getProcessManagerRule());
            	      					}
            	      					add(
            	      						current,
            	      						"reactions",
            	      						lv_reactions_12_0,
            	      						"org.fuin.dsl.cqrs.CqrsDsl.ProcessReaction");
            	      					afterParserOrEnumRuleCall();
            	      				
            	    }

            	    }


            	    }
            	    break;

            	default :
            	    break loop218;
                }
            } while (true);

            otherlv_13=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_13, grammarAccess.getProcessManagerAccess().getRightCurlyBracketKeyword_8());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleProcessManager"


    // $ANTLR start "entryRuleProcessState"
    // InternalCqrsDsl.g:7702:1: entryRuleProcessState returns [EObject current=null] : iv_ruleProcessState= ruleProcessState EOF ;
    public final EObject entryRuleProcessState() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProcessState = null;


        try {
            // InternalCqrsDsl.g:7702:53: (iv_ruleProcessState= ruleProcessState EOF )
            // InternalCqrsDsl.g:7703:2: iv_ruleProcessState= ruleProcessState EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getProcessStateRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleProcessState=ruleProcessState();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleProcessState; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleProcessState"


    // $ANTLR start "ruleProcessState"
    // InternalCqrsDsl.g:7709:1: ruleProcessState returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleProcessState() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token lv_name_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7715:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalCqrsDsl.g:7716:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalCqrsDsl.g:7716:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) )
            // InternalCqrsDsl.g:7717:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) )
            {
            // InternalCqrsDsl.g:7717:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt219=2;
            int LA219_0 = input.LA(1);

            if ( (LA219_0==RULE_DOC) ) {
                alt219=1;
            }
            switch (alt219) {
                case 1 :
                    // InternalCqrsDsl.g:7718:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:7718:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:7719:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getProcessStateAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getProcessStateRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:7735:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCqrsDsl.g:7736:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCqrsDsl.g:7736:4: (lv_name_1_0= RULE_ID )
            // InternalCqrsDsl.g:7737:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_name_1_0, grammarAccess.getProcessStateAccess().getNameIDTerminalRuleCall_1_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getProcessStateRule());
              					}
              					setWithLastConsumed(
              						current,
              						"name",
              						lv_name_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.ID");
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleProcessState"


    // $ANTLR start "entryRuleProcessReaction"
    // InternalCqrsDsl.g:7757:1: entryRuleProcessReaction returns [EObject current=null] : iv_ruleProcessReaction= ruleProcessReaction EOF ;
    public final EObject entryRuleProcessReaction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProcessReaction = null;


        try {
            // InternalCqrsDsl.g:7757:56: (iv_ruleProcessReaction= ruleProcessReaction EOF )
            // InternalCqrsDsl.g:7758:2: iv_ruleProcessReaction= ruleProcessReaction EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getProcessReactionRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleProcessReaction=ruleProcessReaction();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleProcessReaction; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleProcessReaction"


    // $ANTLR start "ruleProcessReaction"
    // InternalCqrsDsl.g:7764:1: ruleProcessReaction returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' ) ;
    public final EObject ruleProcessReaction() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token lv_correlationKey_7_0=null;
        Token otherlv_8=null;
        Token otherlv_10=null;
        Token otherlv_12=null;
        Token otherlv_14=null;
        Token lv_cancelTimeout_16_0=null;
        Token otherlv_17=null;
        EObject lv_armTimeout_15_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:7770:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' ) )
            // InternalCqrsDsl.g:7771:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' )
            {
            // InternalCqrsDsl.g:7771:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' )
            // InternalCqrsDsl.g:7772:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}'
            {
            // InternalCqrsDsl.g:7772:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt220=2;
            int LA220_0 = input.LA(1);

            if ( (LA220_0==RULE_DOC) ) {
                alt220=1;
            }
            switch (alt220) {
                case 1 :
                    // InternalCqrsDsl.g:7773:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:7773:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:7774:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_179); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_doc_0_0, grammarAccess.getProcessReactionAccess().getDocDOCTerminalRuleCall_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getProcessReactionRule());
                      					}
                      					setWithLastConsumed(
                      						current,
                      						"doc",
                      						lv_doc_0_0,
                      						"org.fuin.dsl.cqrs.CqrsDsl.DOC");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_1=(Token)match(input,108,FOLLOW_4); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getProcessReactionAccess().getReactsToKeyword_1());
              		
            }
            // InternalCqrsDsl.g:7794:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:7795:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:7795:4: ( ruleFQN )
            // InternalCqrsDsl.g:7796:5: ruleFQN
            {
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getProcessReactionRule());
              					}
              				
            }
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getProcessReactionAccess().getEventEventCrossReference_2_0());
              				
            }
            pushFollow(FOLLOW_180);
            ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					afterParserOrEnumRuleCall();
              				
            }

            }


            }

            // InternalCqrsDsl.g:7810:3: (otherlv_3= 'in-state' ( ( ruleFQN ) ) )?
            int alt221=2;
            int LA221_0 = input.LA(1);

            if ( (LA221_0==109) ) {
                alt221=1;
            }
            switch (alt221) {
                case 1 :
                    // InternalCqrsDsl.g:7811:4: otherlv_3= 'in-state' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,109,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_3, grammarAccess.getProcessReactionAccess().getInStateKeyword_3_0());
                      			
                    }
                    // InternalCqrsDsl.g:7815:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:7816:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:7816:5: ( ruleFQN )
                    // InternalCqrsDsl.g:7817:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getProcessReactionRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getProcessReactionAccess().getFromStateProcessStateCrossReference_3_1_0());
                      					
                    }
                    pushFollow(FOLLOW_5);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            otherlv_5=(Token)match(input,14,FOLLOW_181); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getProcessReactionAccess().getLeftCurlyBracketKeyword_4());
              		
            }
            // InternalCqrsDsl.g:7836:3: (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )?
            int alt222=2;
            int LA222_0 = input.LA(1);

            if ( (LA222_0==110) ) {
                alt222=1;
            }
            switch (alt222) {
                case 1 :
                    // InternalCqrsDsl.g:7837:4: otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) )
                    {
                    otherlv_6=(Token)match(input,110,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_6, grammarAccess.getProcessReactionAccess().getCorrelateByKeyword_5_0());
                      			
                    }
                    // InternalCqrsDsl.g:7841:4: ( (lv_correlationKey_7_0= RULE_ID ) )
                    // InternalCqrsDsl.g:7842:5: (lv_correlationKey_7_0= RULE_ID )
                    {
                    // InternalCqrsDsl.g:7842:5: (lv_correlationKey_7_0= RULE_ID )
                    // InternalCqrsDsl.g:7843:6: lv_correlationKey_7_0= RULE_ID
                    {
                    lv_correlationKey_7_0=(Token)match(input,RULE_ID,FOLLOW_182); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_correlationKey_7_0, grammarAccess.getProcessReactionAccess().getCorrelationKeyIDTerminalRuleCall_5_1_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getProcessReactionRule());
                      						}
                      						setWithLastConsumed(
                      							current,
                      							"correlationKey",
                      							lv_correlationKey_7_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.ID");
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:7860:3: (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )?
            int alt224=2;
            int LA224_0 = input.LA(1);

            if ( (LA224_0==111) ) {
                alt224=1;
            }
            switch (alt224) {
                case 1 :
                    // InternalCqrsDsl.g:7861:4: otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_8=(Token)match(input,111,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_8, grammarAccess.getProcessReactionAccess().getIssuesCommandsKeyword_6_0());
                      			
                    }
                    // InternalCqrsDsl.g:7865:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:7866:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:7866:5: ( ruleFQN )
                    // InternalCqrsDsl.g:7867:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getProcessReactionRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getProcessReactionAccess().getCommandsCommandCrossReference_6_1_0());
                      					
                    }
                    pushFollow(FOLLOW_183);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:7881:4: (otherlv_10= ',' ( ( ruleFQN ) ) )*
                    loop223:
                    do {
                        int alt223=2;
                        int LA223_0 = input.LA(1);

                        if ( (LA223_0==31) ) {
                            alt223=1;
                        }


                        switch (alt223) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:7882:5: otherlv_10= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_10=(Token)match(input,31,FOLLOW_4); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_10, grammarAccess.getProcessReactionAccess().getCommaKeyword_6_2_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:7886:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:7887:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:7887:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:7888:7: ruleFQN
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElement(grammarAccess.getProcessReactionRule());
                    	      							}
                    	      						
                    	    }
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getProcessReactionAccess().getCommandsCommandCrossReference_6_2_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_183);
                    	    ruleFQN();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop223;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:7904:3: (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )?
            int alt225=2;
            int LA225_0 = input.LA(1);

            if ( (LA225_0==112) ) {
                alt225=1;
            }
            switch (alt225) {
                case 1 :
                    // InternalCqrsDsl.g:7905:4: otherlv_12= 'transition-to' ( ( ruleFQN ) )
                    {
                    otherlv_12=(Token)match(input,112,FOLLOW_4); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_12, grammarAccess.getProcessReactionAccess().getTransitionToKeyword_7_0());
                      			
                    }
                    // InternalCqrsDsl.g:7909:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:7910:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:7910:5: ( ruleFQN )
                    // InternalCqrsDsl.g:7911:6: ruleFQN
                    {
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getProcessReactionRule());
                      						}
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getProcessReactionAccess().getToStateProcessStateCrossReference_7_1_0());
                      					
                    }
                    pushFollow(FOLLOW_184);
                    ruleFQN();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:7926:3: (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )?
            int alt226=2;
            int LA226_0 = input.LA(1);

            if ( (LA226_0==113) ) {
                alt226=1;
            }
            switch (alt226) {
                case 1 :
                    // InternalCqrsDsl.g:7927:4: otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) )
                    {
                    otherlv_14=(Token)match(input,113,FOLLOW_22); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				newLeafNode(otherlv_14, grammarAccess.getProcessReactionAccess().getArmTimeoutKeyword_8_0());
                      			
                    }
                    // InternalCqrsDsl.g:7931:4: ( (lv_armTimeout_15_0= ruleDuration ) )
                    // InternalCqrsDsl.g:7932:5: (lv_armTimeout_15_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:7932:5: (lv_armTimeout_15_0= ruleDuration )
                    // InternalCqrsDsl.g:7933:6: lv_armTimeout_15_0= ruleDuration
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getProcessReactionAccess().getArmTimeoutDurationParserRuleCall_8_1_0());
                      					
                    }
                    pushFollow(FOLLOW_185);
                    lv_armTimeout_15_0=ruleDuration();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getProcessReactionRule());
                      						}
                      						set(
                      							current,
                      							"armTimeout",
                      							lv_armTimeout_15_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.Duration");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:7951:3: ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )?
            int alt227=2;
            int LA227_0 = input.LA(1);

            if ( (LA227_0==114) ) {
                alt227=1;
            }
            switch (alt227) {
                case 1 :
                    // InternalCqrsDsl.g:7952:4: (lv_cancelTimeout_16_0= 'cancel-timeout' )
                    {
                    // InternalCqrsDsl.g:7952:4: (lv_cancelTimeout_16_0= 'cancel-timeout' )
                    // InternalCqrsDsl.g:7953:5: lv_cancelTimeout_16_0= 'cancel-timeout'
                    {
                    lv_cancelTimeout_16_0=(Token)match(input,114,FOLLOW_35); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_cancelTimeout_16_0, grammarAccess.getProcessReactionAccess().getCancelTimeoutCancelTimeoutKeyword_9_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getProcessReactionRule());
                      					}
                      					setWithLastConsumed(current, "cancelTimeout", lv_cancelTimeout_16_0 != null, "cancel-timeout");
                      				
                    }

                    }


                    }
                    break;

            }

            otherlv_17=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_17, grammarAccess.getProcessReactionAccess().getRightCurlyBracketKeyword_10());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleProcessReaction"


    // $ANTLR start "entryRuleLiteral"
    // InternalCqrsDsl.g:7973:1: entryRuleLiteral returns [EObject current=null] : iv_ruleLiteral= ruleLiteral EOF ;
    public final EObject entryRuleLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLiteral = null;


        try {
            // InternalCqrsDsl.g:7973:48: (iv_ruleLiteral= ruleLiteral EOF )
            // InternalCqrsDsl.g:7974:2: iv_ruleLiteral= ruleLiteral EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getLiteralRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleLiteral=ruleLiteral();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleLiteral; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleLiteral"


    // $ANTLR start "ruleLiteral"
    // InternalCqrsDsl.g:7980:1: ruleLiteral returns [EObject current=null] : (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral ) ;
    public final EObject ruleLiteral() throws RecognitionException {
        EObject current = null;

        EObject this_NullLiteral_0 = null;

        EObject this_BooleanLiteral_1 = null;

        EObject this_NumberLiteral_2 = null;

        EObject this_StringLiteral_3 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:7986:2: ( (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral ) )
            // InternalCqrsDsl.g:7987:2: (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral )
            {
            // InternalCqrsDsl.g:7987:2: (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral )
            int alt228=4;
            switch ( input.LA(1) ) {
            case 52:
                {
                alt228=1;
                }
                break;
            case 118:
            case 119:
                {
                alt228=2;
                }
                break;
            case RULE_INT:
            case RULE_HEX:
            case RULE_DECIMAL:
                {
                alt228=3;
                }
                break;
            case RULE_STRING:
                {
                alt228=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 228, 0, input);

                throw nvae;
            }

            switch (alt228) {
                case 1 :
                    // InternalCqrsDsl.g:7988:3: this_NullLiteral_0= ruleNullLiteral
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getLiteralAccess().getNullLiteralParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_NullLiteral_0=ruleNullLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_NullLiteral_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7997:3: this_BooleanLiteral_1= ruleBooleanLiteral
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getLiteralAccess().getBooleanLiteralParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_BooleanLiteral_1=ruleBooleanLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_BooleanLiteral_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:8006:3: this_NumberLiteral_2= ruleNumberLiteral
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getLiteralAccess().getNumberLiteralParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_NumberLiteral_2=ruleNumberLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_NumberLiteral_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:8015:3: this_StringLiteral_3= ruleStringLiteral
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getLiteralAccess().getStringLiteralParserRuleCall_3());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_StringLiteral_3=ruleStringLiteral();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_StringLiteral_3;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLiteral"


    // $ANTLR start "entryRuleJSON"
    // InternalCqrsDsl.g:8027:1: entryRuleJSON returns [EObject current=null] : iv_ruleJSON= ruleJSON EOF ;
    public final EObject entryRuleJSON() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJSON = null;


        try {
            // InternalCqrsDsl.g:8027:45: (iv_ruleJSON= ruleJSON EOF )
            // InternalCqrsDsl.g:8028:2: iv_ruleJSON= ruleJSON EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJSONRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJSON=ruleJSON();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJSON; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJSON"


    // $ANTLR start "ruleJSON"
    // InternalCqrsDsl.g:8034:1: ruleJSON returns [EObject current=null] : (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull ) ;
    public final EObject ruleJSON() throws RecognitionException {
        EObject current = null;

        EObject this_JsonObject_0 = null;

        EObject this_JsonArray_1 = null;

        EObject this_JsonString_2 = null;

        EObject this_JsonNumber_3 = null;

        EObject this_JsonBoolean_4 = null;

        EObject this_JsonNull_5 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:8040:2: ( (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull ) )
            // InternalCqrsDsl.g:8041:2: (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull )
            {
            // InternalCqrsDsl.g:8041:2: (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull )
            int alt229=6;
            switch ( input.LA(1) ) {
            case 14:
                {
                alt229=1;
                }
                break;
            case 116:
                {
                alt229=2;
                }
                break;
            case RULE_STRING:
                {
                alt229=3;
                }
                break;
            case RULE_INT:
            case RULE_HEX:
            case RULE_DECIMAL:
                {
                alt229=4;
                }
                break;
            case 118:
            case 119:
                {
                alt229=5;
                }
                break;
            case 52:
                {
                alt229=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 229, 0, input);

                throw nvae;
            }

            switch (alt229) {
                case 1 :
                    // InternalCqrsDsl.g:8042:3: this_JsonObject_0= ruleJsonObject
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJSONAccess().getJsonObjectParserRuleCall_0());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_JsonObject_0=ruleJsonObject();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_JsonObject_0;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8051:3: this_JsonArray_1= ruleJsonArray
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJSONAccess().getJsonArrayParserRuleCall_1());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_JsonArray_1=ruleJsonArray();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_JsonArray_1;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:8060:3: this_JsonString_2= ruleJsonString
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJSONAccess().getJsonStringParserRuleCall_2());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_JsonString_2=ruleJsonString();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_JsonString_2;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:8069:3: this_JsonNumber_3= ruleJsonNumber
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJSONAccess().getJsonNumberParserRuleCall_3());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_JsonNumber_3=ruleJsonNumber();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_JsonNumber_3;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:8078:3: this_JsonBoolean_4= ruleJsonBoolean
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJSONAccess().getJsonBooleanParserRuleCall_4());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_JsonBoolean_4=ruleJsonBoolean();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_JsonBoolean_4;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:8087:3: this_JsonNull_5= ruleJsonNull
                    {
                    if ( state.backtracking==0 ) {

                      			newCompositeNode(grammarAccess.getJSONAccess().getJsonNullParserRuleCall_5());
                      		
                    }
                    pushFollow(FOLLOW_2);
                    this_JsonNull_5=ruleJsonNull();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current = this_JsonNull_5;
                      			afterParserOrEnumRuleCall();
                      		
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJSON"


    // $ANTLR start "entryRuleJsonObject"
    // InternalCqrsDsl.g:8099:1: entryRuleJsonObject returns [EObject current=null] : iv_ruleJsonObject= ruleJsonObject EOF ;
    public final EObject entryRuleJsonObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonObject = null;


        try {
            // InternalCqrsDsl.g:8099:51: (iv_ruleJsonObject= ruleJsonObject EOF )
            // InternalCqrsDsl.g:8100:2: iv_ruleJsonObject= ruleJsonObject EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJsonObjectRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJsonObject=ruleJsonObject();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJsonObject; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJsonObject"


    // $ANTLR start "ruleJsonObject"
    // InternalCqrsDsl.g:8106:1: ruleJsonObject returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleJsonObject() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_members_2_0 = null;

        EObject lv_members_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:8112:2: ( ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' ) )
            // InternalCqrsDsl.g:8113:2: ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:8113:2: ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' )
            // InternalCqrsDsl.g:8114:3: () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}'
            {
            // InternalCqrsDsl.g:8114:3: ()
            // InternalCqrsDsl.g:8115:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getJsonObjectAccess().getJsonObjectAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,14,FOLLOW_186); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getJsonObjectAccess().getLeftCurlyBracketKeyword_1());
              		
            }
            // InternalCqrsDsl.g:8125:3: ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )?
            int alt231=2;
            int LA231_0 = input.LA(1);

            if ( (LA231_0==RULE_STRING) ) {
                alt231=1;
            }
            switch (alt231) {
                case 1 :
                    // InternalCqrsDsl.g:8126:4: ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )*
                    {
                    // InternalCqrsDsl.g:8126:4: ( (lv_members_2_0= ruleJsonMember ) )
                    // InternalCqrsDsl.g:8127:5: (lv_members_2_0= ruleJsonMember )
                    {
                    // InternalCqrsDsl.g:8127:5: (lv_members_2_0= ruleJsonMember )
                    // InternalCqrsDsl.g:8128:6: lv_members_2_0= ruleJsonMember
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getJsonObjectAccess().getMembersJsonMemberParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_187);
                    lv_members_2_0=ruleJsonMember();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getJsonObjectRule());
                      						}
                      						add(
                      							current,
                      							"members",
                      							lv_members_2_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.JsonMember");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:8145:4: (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )*
                    loop230:
                    do {
                        int alt230=2;
                        int LA230_0 = input.LA(1);

                        if ( (LA230_0==31) ) {
                            alt230=1;
                        }


                        switch (alt230) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:8146:5: otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) )
                    	    {
                    	    otherlv_3=(Token)match(input,31,FOLLOW_15); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getJsonObjectAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:8150:5: ( (lv_members_4_0= ruleJsonMember ) )
                    	    // InternalCqrsDsl.g:8151:6: (lv_members_4_0= ruleJsonMember )
                    	    {
                    	    // InternalCqrsDsl.g:8151:6: (lv_members_4_0= ruleJsonMember )
                    	    // InternalCqrsDsl.g:8152:7: lv_members_4_0= ruleJsonMember
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getJsonObjectAccess().getMembersJsonMemberParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_187);
                    	    lv_members_4_0=ruleJsonMember();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getJsonObjectRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"members",
                    	      								lv_members_4_0,
                    	      								"org.fuin.dsl.cqrs.CqrsDsl.JsonMember");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop230;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,15,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getJsonObjectAccess().getRightCurlyBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJsonObject"


    // $ANTLR start "entryRuleJsonMember"
    // InternalCqrsDsl.g:8179:1: entryRuleJsonMember returns [EObject current=null] : iv_ruleJsonMember= ruleJsonMember EOF ;
    public final EObject entryRuleJsonMember() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonMember = null;


        try {
            // InternalCqrsDsl.g:8179:51: (iv_ruleJsonMember= ruleJsonMember EOF )
            // InternalCqrsDsl.g:8180:2: iv_ruleJsonMember= ruleJsonMember EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJsonMemberRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJsonMember=ruleJsonMember();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJsonMember; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJsonMember"


    // $ANTLR start "ruleJsonMember"
    // InternalCqrsDsl.g:8186:1: ruleJsonMember returns [EObject current=null] : ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) ) ;
    public final EObject ruleJsonMember() throws RecognitionException {
        EObject current = null;

        Token lv_key_0_0=null;
        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:8192:2: ( ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) ) )
            // InternalCqrsDsl.g:8193:2: ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) )
            {
            // InternalCqrsDsl.g:8193:2: ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) )
            // InternalCqrsDsl.g:8194:3: ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) )
            {
            // InternalCqrsDsl.g:8194:3: ( (lv_key_0_0= RULE_STRING ) )
            // InternalCqrsDsl.g:8195:4: (lv_key_0_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:8195:4: (lv_key_0_0= RULE_STRING )
            // InternalCqrsDsl.g:8196:5: lv_key_0_0= RULE_STRING
            {
            lv_key_0_0=(Token)match(input,RULE_STRING,FOLLOW_188); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_key_0_0, grammarAccess.getJsonMemberAccess().getKeySTRINGTerminalRuleCall_0_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getJsonMemberRule());
              					}
              					setWithLastConsumed(
              						current,
              						"key",
              						lv_key_0_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.STRING");
              				
            }

            }


            }

            otherlv_1=(Token)match(input,115,FOLLOW_18); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getJsonMemberAccess().getColonKeyword_1());
              		
            }
            // InternalCqrsDsl.g:8216:3: ( (lv_value_2_0= ruleJSON ) )
            // InternalCqrsDsl.g:8217:4: (lv_value_2_0= ruleJSON )
            {
            // InternalCqrsDsl.g:8217:4: (lv_value_2_0= ruleJSON )
            // InternalCqrsDsl.g:8218:5: lv_value_2_0= ruleJSON
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getJsonMemberAccess().getValueJSONParserRuleCall_2_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_value_2_0=ruleJSON();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getJsonMemberRule());
              					}
              					set(
              						current,
              						"value",
              						lv_value_2_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.JSON");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJsonMember"


    // $ANTLR start "entryRuleJsonArray"
    // InternalCqrsDsl.g:8239:1: entryRuleJsonArray returns [EObject current=null] : iv_ruleJsonArray= ruleJsonArray EOF ;
    public final EObject entryRuleJsonArray() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonArray = null;


        try {
            // InternalCqrsDsl.g:8239:50: (iv_ruleJsonArray= ruleJsonArray EOF )
            // InternalCqrsDsl.g:8240:2: iv_ruleJsonArray= ruleJsonArray EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJsonArrayRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJsonArray=ruleJsonArray();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJsonArray; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJsonArray"


    // $ANTLR start "ruleJsonArray"
    // InternalCqrsDsl.g:8246:1: ruleJsonArray returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleJsonArray() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:8252:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' ) )
            // InternalCqrsDsl.g:8253:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' )
            {
            // InternalCqrsDsl.g:8253:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' )
            // InternalCqrsDsl.g:8254:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']'
            {
            // InternalCqrsDsl.g:8254:3: ()
            // InternalCqrsDsl.g:8255:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getJsonArrayAccess().getJsonArrayAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,116,FOLLOW_189); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getJsonArrayAccess().getLeftSquareBracketKeyword_1());
              		
            }
            // InternalCqrsDsl.g:8265:3: ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )?
            int alt233=2;
            int LA233_0 = input.LA(1);

            if ( (LA233_0==RULE_STRING||(LA233_0>=RULE_INT && LA233_0<=RULE_DECIMAL)||LA233_0==14||LA233_0==52||LA233_0==116||(LA233_0>=118 && LA233_0<=119)) ) {
                alt233=1;
            }
            switch (alt233) {
                case 1 :
                    // InternalCqrsDsl.g:8266:4: ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )*
                    {
                    // InternalCqrsDsl.g:8266:4: ( (lv_elements_2_0= ruleJSON ) )
                    // InternalCqrsDsl.g:8267:5: (lv_elements_2_0= ruleJSON )
                    {
                    // InternalCqrsDsl.g:8267:5: (lv_elements_2_0= ruleJSON )
                    // InternalCqrsDsl.g:8268:6: lv_elements_2_0= ruleJSON
                    {
                    if ( state.backtracking==0 ) {

                      						newCompositeNode(grammarAccess.getJsonArrayAccess().getElementsJSONParserRuleCall_2_0_0());
                      					
                    }
                    pushFollow(FOLLOW_190);
                    lv_elements_2_0=ruleJSON();

                    state._fsp--;
                    if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElementForParent(grammarAccess.getJsonArrayRule());
                      						}
                      						add(
                      							current,
                      							"elements",
                      							lv_elements_2_0,
                      							"org.fuin.dsl.cqrs.CqrsDsl.JSON");
                      						afterParserOrEnumRuleCall();
                      					
                    }

                    }


                    }

                    // InternalCqrsDsl.g:8285:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )*
                    loop232:
                    do {
                        int alt232=2;
                        int LA232_0 = input.LA(1);

                        if ( (LA232_0==31) ) {
                            alt232=1;
                        }


                        switch (alt232) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:8286:5: otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) )
                    	    {
                    	    otherlv_3=(Token)match(input,31,FOLLOW_18); if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      					newLeafNode(otherlv_3, grammarAccess.getJsonArrayAccess().getCommaKeyword_2_1_0());
                    	      				
                    	    }
                    	    // InternalCqrsDsl.g:8290:5: ( (lv_elements_4_0= ruleJSON ) )
                    	    // InternalCqrsDsl.g:8291:6: (lv_elements_4_0= ruleJSON )
                    	    {
                    	    // InternalCqrsDsl.g:8291:6: (lv_elements_4_0= ruleJSON )
                    	    // InternalCqrsDsl.g:8292:7: lv_elements_4_0= ruleJSON
                    	    {
                    	    if ( state.backtracking==0 ) {

                    	      							newCompositeNode(grammarAccess.getJsonArrayAccess().getElementsJSONParserRuleCall_2_1_1_0());
                    	      						
                    	    }
                    	    pushFollow(FOLLOW_190);
                    	    lv_elements_4_0=ruleJSON();

                    	    state._fsp--;
                    	    if (state.failed) return current;
                    	    if ( state.backtracking==0 ) {

                    	      							if (current==null) {
                    	      								current = createModelElementForParent(grammarAccess.getJsonArrayRule());
                    	      							}
                    	      							add(
                    	      								current,
                    	      								"elements",
                    	      								lv_elements_4_0,
                    	      								"org.fuin.dsl.cqrs.CqrsDsl.JSON");
                    	      							afterParserOrEnumRuleCall();
                    	      						
                    	    }

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop232;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,117,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_5, grammarAccess.getJsonArrayAccess().getRightSquareBracketKeyword_3());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJsonArray"


    // $ANTLR start "entryRuleJsonString"
    // InternalCqrsDsl.g:8319:1: entryRuleJsonString returns [EObject current=null] : iv_ruleJsonString= ruleJsonString EOF ;
    public final EObject entryRuleJsonString() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonString = null;


        try {
            // InternalCqrsDsl.g:8319:51: (iv_ruleJsonString= ruleJsonString EOF )
            // InternalCqrsDsl.g:8320:2: iv_ruleJsonString= ruleJsonString EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJsonStringRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJsonString=ruleJsonString();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJsonString; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJsonString"


    // $ANTLR start "ruleJsonString"
    // InternalCqrsDsl.g:8326:1: ruleJsonString returns [EObject current=null] : ( (lv_value_0_0= RULE_STRING ) ) ;
    public final EObject ruleJsonString() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8332:2: ( ( (lv_value_0_0= RULE_STRING ) ) )
            // InternalCqrsDsl.g:8333:2: ( (lv_value_0_0= RULE_STRING ) )
            {
            // InternalCqrsDsl.g:8333:2: ( (lv_value_0_0= RULE_STRING ) )
            // InternalCqrsDsl.g:8334:3: (lv_value_0_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:8334:3: (lv_value_0_0= RULE_STRING )
            // InternalCqrsDsl.g:8335:4: lv_value_0_0= RULE_STRING
            {
            lv_value_0_0=(Token)match(input,RULE_STRING,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              				newLeafNode(lv_value_0_0, grammarAccess.getJsonStringAccess().getValueSTRINGTerminalRuleCall_0());
              			
            }
            if ( state.backtracking==0 ) {

              				if (current==null) {
              					current = createModelElement(grammarAccess.getJsonStringRule());
              				}
              				setWithLastConsumed(
              					current,
              					"value",
              					lv_value_0_0,
              					"org.fuin.dsl.cqrs.CqrsDsl.STRING");
              			
            }

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJsonString"


    // $ANTLR start "entryRuleJsonNumber"
    // InternalCqrsDsl.g:8354:1: entryRuleJsonNumber returns [EObject current=null] : iv_ruleJsonNumber= ruleJsonNumber EOF ;
    public final EObject entryRuleJsonNumber() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonNumber = null;


        try {
            // InternalCqrsDsl.g:8354:51: (iv_ruleJsonNumber= ruleJsonNumber EOF )
            // InternalCqrsDsl.g:8355:2: iv_ruleJsonNumber= ruleJsonNumber EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJsonNumberRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJsonNumber=ruleJsonNumber();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJsonNumber; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJsonNumber"


    // $ANTLR start "ruleJsonNumber"
    // InternalCqrsDsl.g:8361:1: ruleJsonNumber returns [EObject current=null] : ( (lv_value_0_0= ruleNumber ) ) ;
    public final EObject ruleJsonNumber() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:8367:2: ( ( (lv_value_0_0= ruleNumber ) ) )
            // InternalCqrsDsl.g:8368:2: ( (lv_value_0_0= ruleNumber ) )
            {
            // InternalCqrsDsl.g:8368:2: ( (lv_value_0_0= ruleNumber ) )
            // InternalCqrsDsl.g:8369:3: (lv_value_0_0= ruleNumber )
            {
            // InternalCqrsDsl.g:8369:3: (lv_value_0_0= ruleNumber )
            // InternalCqrsDsl.g:8370:4: lv_value_0_0= ruleNumber
            {
            if ( state.backtracking==0 ) {

              				newCompositeNode(grammarAccess.getJsonNumberAccess().getValueNumberParserRuleCall_0());
              			
            }
            pushFollow(FOLLOW_2);
            lv_value_0_0=ruleNumber();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              				if (current==null) {
              					current = createModelElementForParent(grammarAccess.getJsonNumberRule());
              				}
              				set(
              					current,
              					"value",
              					lv_value_0_0,
              					"org.fuin.dsl.cqrs.CqrsDsl.Number");
              				afterParserOrEnumRuleCall();
              			
            }

            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJsonNumber"


    // $ANTLR start "entryRuleJsonBoolean"
    // InternalCqrsDsl.g:8390:1: entryRuleJsonBoolean returns [EObject current=null] : iv_ruleJsonBoolean= ruleJsonBoolean EOF ;
    public final EObject entryRuleJsonBoolean() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonBoolean = null;


        try {
            // InternalCqrsDsl.g:8390:52: (iv_ruleJsonBoolean= ruleJsonBoolean EOF )
            // InternalCqrsDsl.g:8391:2: iv_ruleJsonBoolean= ruleJsonBoolean EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJsonBooleanRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJsonBoolean=ruleJsonBoolean();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJsonBoolean; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJsonBoolean"


    // $ANTLR start "ruleJsonBoolean"
    // InternalCqrsDsl.g:8397:1: ruleJsonBoolean returns [EObject current=null] : ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) ) ;
    public final EObject ruleJsonBoolean() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_1=null;
        Token lv_value_0_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8403:2: ( ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) ) )
            // InternalCqrsDsl.g:8404:2: ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) )
            {
            // InternalCqrsDsl.g:8404:2: ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) )
            // InternalCqrsDsl.g:8405:3: ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) )
            {
            // InternalCqrsDsl.g:8405:3: ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) )
            // InternalCqrsDsl.g:8406:4: (lv_value_0_1= 'true' | lv_value_0_2= 'false' )
            {
            // InternalCqrsDsl.g:8406:4: (lv_value_0_1= 'true' | lv_value_0_2= 'false' )
            int alt234=2;
            int LA234_0 = input.LA(1);

            if ( (LA234_0==118) ) {
                alt234=1;
            }
            else if ( (LA234_0==119) ) {
                alt234=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 234, 0, input);

                throw nvae;
            }
            switch (alt234) {
                case 1 :
                    // InternalCqrsDsl.g:8407:5: lv_value_0_1= 'true'
                    {
                    lv_value_0_1=(Token)match(input,118,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_value_0_1, grammarAccess.getJsonBooleanAccess().getValueTrueKeyword_0_0());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getJsonBooleanRule());
                      					}
                      					setWithLastConsumed(current, "value", lv_value_0_1, null);
                      				
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8418:5: lv_value_0_2= 'false'
                    {
                    lv_value_0_2=(Token)match(input,119,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      					newLeafNode(lv_value_0_2, grammarAccess.getJsonBooleanAccess().getValueFalseKeyword_0_1());
                      				
                    }
                    if ( state.backtracking==0 ) {

                      					if (current==null) {
                      						current = createModelElement(grammarAccess.getJsonBooleanRule());
                      					}
                      					setWithLastConsumed(current, "value", lv_value_0_2, null);
                      				
                    }

                    }
                    break;

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJsonBoolean"


    // $ANTLR start "entryRuleJsonNull"
    // InternalCqrsDsl.g:8434:1: entryRuleJsonNull returns [EObject current=null] : iv_ruleJsonNull= ruleJsonNull EOF ;
    public final EObject entryRuleJsonNull() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonNull = null;


        try {
            // InternalCqrsDsl.g:8434:49: (iv_ruleJsonNull= ruleJsonNull EOF )
            // InternalCqrsDsl.g:8435:2: iv_ruleJsonNull= ruleJsonNull EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getJsonNullRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleJsonNull=ruleJsonNull();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleJsonNull; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleJsonNull"


    // $ANTLR start "ruleJsonNull"
    // InternalCqrsDsl.g:8441:1: ruleJsonNull returns [EObject current=null] : ( () otherlv_1= 'null' ) ;
    public final EObject ruleJsonNull() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8447:2: ( ( () otherlv_1= 'null' ) )
            // InternalCqrsDsl.g:8448:2: ( () otherlv_1= 'null' )
            {
            // InternalCqrsDsl.g:8448:2: ( () otherlv_1= 'null' )
            // InternalCqrsDsl.g:8449:3: () otherlv_1= 'null'
            {
            // InternalCqrsDsl.g:8449:3: ()
            // InternalCqrsDsl.g:8450:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getJsonNullAccess().getJsonNullAction_0(),
              					current);
              			
            }

            }

            otherlv_1=(Token)match(input,52,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			newLeafNode(otherlv_1, grammarAccess.getJsonNullAccess().getNullKeyword_1());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleJsonNull"


    // $ANTLR start "entryRuleFQN"
    // InternalCqrsDsl.g:8464:1: entryRuleFQN returns [String current=null] : iv_ruleFQN= ruleFQN EOF ;
    public final String entryRuleFQN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFQN = null;


        try {
            // InternalCqrsDsl.g:8464:43: (iv_ruleFQN= ruleFQN EOF )
            // InternalCqrsDsl.g:8465:2: iv_ruleFQN= ruleFQN EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getFQNRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleFQN=ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleFQN.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFQN"


    // $ANTLR start "ruleFQN"
    // InternalCqrsDsl.g:8471:1: ruleFQN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleFQN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8477:2: ( (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) )
            // InternalCqrsDsl.g:8478:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            {
            // InternalCqrsDsl.g:8478:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            // InternalCqrsDsl.g:8479:3: this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_191); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_ID_0);
              		
            }
            if ( state.backtracking==0 ) {

              			newLeafNode(this_ID_0, grammarAccess.getFQNAccess().getIDTerminalRuleCall_0());
              		
            }
            // InternalCqrsDsl.g:8486:3: (kw= '.' this_ID_2= RULE_ID )*
            loop235:
            do {
                int alt235=2;
                int LA235_0 = input.LA(1);

                if ( (LA235_0==50) ) {
                    int LA235_2 = input.LA(2);

                    if ( (LA235_2==RULE_ID) ) {
                        alt235=1;
                    }


                }


                switch (alt235) {
            	case 1 :
            	    // InternalCqrsDsl.g:8487:4: kw= '.' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,50,FOLLOW_4); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(kw);
            	      				newLeafNode(kw, grammarAccess.getFQNAccess().getFullStopKeyword_1_0());
            	      			
            	    }
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_191); if (state.failed) return current;
            	    if ( state.backtracking==0 ) {

            	      				current.merge(this_ID_2);
            	      			
            	    }
            	    if ( state.backtracking==0 ) {

            	      				newLeafNode(this_ID_2, grammarAccess.getFQNAccess().getIDTerminalRuleCall_1_1());
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    break loop235;
                }
            } while (true);


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFQN"


    // $ANTLR start "entryRuleFQNWithWildcard"
    // InternalCqrsDsl.g:8504:1: entryRuleFQNWithWildcard returns [String current=null] : iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF ;
    public final String entryRuleFQNWithWildcard() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFQNWithWildcard = null;


        try {
            // InternalCqrsDsl.g:8504:55: (iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF )
            // InternalCqrsDsl.g:8505:2: iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getFQNWithWildcardRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleFQNWithWildcard=ruleFQNWithWildcard();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleFQNWithWildcard.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFQNWithWildcard"


    // $ANTLR start "ruleFQNWithWildcard"
    // InternalCqrsDsl.g:8511:1: ruleFQNWithWildcard returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_FQN_0= ruleFQN kw= '.' kw= '*' ) ;
    public final AntlrDatatypeRuleToken ruleFQNWithWildcard() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_FQN_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:8517:2: ( (this_FQN_0= ruleFQN kw= '.' kw= '*' ) )
            // InternalCqrsDsl.g:8518:2: (this_FQN_0= ruleFQN kw= '.' kw= '*' )
            {
            // InternalCqrsDsl.g:8518:2: (this_FQN_0= ruleFQN kw= '.' kw= '*' )
            // InternalCqrsDsl.g:8519:3: this_FQN_0= ruleFQN kw= '.' kw= '*'
            {
            if ( state.backtracking==0 ) {

              			newCompositeNode(grammarAccess.getFQNWithWildcardAccess().getFQNParserRuleCall_0());
              		
            }
            pushFollow(FOLLOW_192);
            this_FQN_0=ruleFQN();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(this_FQN_0);
              		
            }
            if ( state.backtracking==0 ) {

              			afterParserOrEnumRuleCall();
              		
            }
            kw=(Token)match(input,50,FOLLOW_193); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(kw);
              			newLeafNode(kw, grammarAccess.getFQNWithWildcardAccess().getFullStopKeyword_1());
              		
            }
            kw=(Token)match(input,120,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              			current.merge(kw);
              			newLeafNode(kw, grammarAccess.getFQNWithWildcardAccess().getAsteriskKeyword_2());
              		
            }

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFQNWithWildcard"


    // $ANTLR start "entryRuleBooleanLiteral"
    // InternalCqrsDsl.g:8543:1: entryRuleBooleanLiteral returns [EObject current=null] : iv_ruleBooleanLiteral= ruleBooleanLiteral EOF ;
    public final EObject entryRuleBooleanLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBooleanLiteral = null;


        try {
            // InternalCqrsDsl.g:8543:55: (iv_ruleBooleanLiteral= ruleBooleanLiteral EOF )
            // InternalCqrsDsl.g:8544:2: iv_ruleBooleanLiteral= ruleBooleanLiteral EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getBooleanLiteralRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleBooleanLiteral=ruleBooleanLiteral();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleBooleanLiteral; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBooleanLiteral"


    // $ANTLR start "ruleBooleanLiteral"
    // InternalCqrsDsl.g:8550:1: ruleBooleanLiteral returns [EObject current=null] : ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) ) ;
    public final EObject ruleBooleanLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_1=null;
        Token lv_value_1_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8556:2: ( ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) ) )
            // InternalCqrsDsl.g:8557:2: ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) )
            {
            // InternalCqrsDsl.g:8557:2: ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) )
            // InternalCqrsDsl.g:8558:3: () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) )
            {
            // InternalCqrsDsl.g:8558:3: ()
            // InternalCqrsDsl.g:8559:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getBooleanLiteralAccess().getBooleanLiteralAction_0(),
              					current);
              			
            }

            }

            // InternalCqrsDsl.g:8565:3: ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) )
            // InternalCqrsDsl.g:8566:4: ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) )
            {
            // InternalCqrsDsl.g:8566:4: ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) )
            // InternalCqrsDsl.g:8567:5: (lv_value_1_1= 'false' | lv_value_1_2= 'true' )
            {
            // InternalCqrsDsl.g:8567:5: (lv_value_1_1= 'false' | lv_value_1_2= 'true' )
            int alt236=2;
            int LA236_0 = input.LA(1);

            if ( (LA236_0==119) ) {
                alt236=1;
            }
            else if ( (LA236_0==118) ) {
                alt236=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 236, 0, input);

                throw nvae;
            }
            switch (alt236) {
                case 1 :
                    // InternalCqrsDsl.g:8568:6: lv_value_1_1= 'false'
                    {
                    lv_value_1_1=(Token)match(input,119,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_1_1, grammarAccess.getBooleanLiteralAccess().getValueFalseKeyword_1_0_0());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getBooleanLiteralRule());
                      						}
                      						setWithLastConsumed(current, "value", lv_value_1_1, null);
                      					
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8579:6: lv_value_1_2= 'true'
                    {
                    lv_value_1_2=(Token)match(input,118,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      						newLeafNode(lv_value_1_2, grammarAccess.getBooleanLiteralAccess().getValueTrueKeyword_1_0_1());
                      					
                    }
                    if ( state.backtracking==0 ) {

                      						if (current==null) {
                      							current = createModelElement(grammarAccess.getBooleanLiteralRule());
                      						}
                      						setWithLastConsumed(current, "value", lv_value_1_2, null);
                      					
                    }

                    }
                    break;

            }


            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBooleanLiteral"


    // $ANTLR start "entryRuleNullLiteral"
    // InternalCqrsDsl.g:8596:1: entryRuleNullLiteral returns [EObject current=null] : iv_ruleNullLiteral= ruleNullLiteral EOF ;
    public final EObject entryRuleNullLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNullLiteral = null;


        try {
            // InternalCqrsDsl.g:8596:52: (iv_ruleNullLiteral= ruleNullLiteral EOF )
            // InternalCqrsDsl.g:8597:2: iv_ruleNullLiteral= ruleNullLiteral EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getNullLiteralRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleNullLiteral=ruleNullLiteral();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleNullLiteral; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNullLiteral"


    // $ANTLR start "ruleNullLiteral"
    // InternalCqrsDsl.g:8603:1: ruleNullLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= 'null' ) ) ) ;
    public final EObject ruleNullLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8609:2: ( ( () ( (lv_value_1_0= 'null' ) ) ) )
            // InternalCqrsDsl.g:8610:2: ( () ( (lv_value_1_0= 'null' ) ) )
            {
            // InternalCqrsDsl.g:8610:2: ( () ( (lv_value_1_0= 'null' ) ) )
            // InternalCqrsDsl.g:8611:3: () ( (lv_value_1_0= 'null' ) )
            {
            // InternalCqrsDsl.g:8611:3: ()
            // InternalCqrsDsl.g:8612:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getNullLiteralAccess().getNullLiteralAction_0(),
              					current);
              			
            }

            }

            // InternalCqrsDsl.g:8618:3: ( (lv_value_1_0= 'null' ) )
            // InternalCqrsDsl.g:8619:4: (lv_value_1_0= 'null' )
            {
            // InternalCqrsDsl.g:8619:4: (lv_value_1_0= 'null' )
            // InternalCqrsDsl.g:8620:5: lv_value_1_0= 'null'
            {
            lv_value_1_0=(Token)match(input,52,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_value_1_0, grammarAccess.getNullLiteralAccess().getValueNullKeyword_1_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getNullLiteralRule());
              					}
              					setWithLastConsumed(current, "value", lv_value_1_0, "null");
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNullLiteral"


    // $ANTLR start "entryRuleNumberLiteral"
    // InternalCqrsDsl.g:8636:1: entryRuleNumberLiteral returns [EObject current=null] : iv_ruleNumberLiteral= ruleNumberLiteral EOF ;
    public final EObject entryRuleNumberLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNumberLiteral = null;


        try {
            // InternalCqrsDsl.g:8636:54: (iv_ruleNumberLiteral= ruleNumberLiteral EOF )
            // InternalCqrsDsl.g:8637:2: iv_ruleNumberLiteral= ruleNumberLiteral EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getNumberLiteralRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleNumberLiteral=ruleNumberLiteral();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleNumberLiteral; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNumberLiteral"


    // $ANTLR start "ruleNumberLiteral"
    // InternalCqrsDsl.g:8643:1: ruleNumberLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= ruleNumber ) ) ) ;
    public final EObject ruleNumberLiteral() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:8649:2: ( ( () ( (lv_value_1_0= ruleNumber ) ) ) )
            // InternalCqrsDsl.g:8650:2: ( () ( (lv_value_1_0= ruleNumber ) ) )
            {
            // InternalCqrsDsl.g:8650:2: ( () ( (lv_value_1_0= ruleNumber ) ) )
            // InternalCqrsDsl.g:8651:3: () ( (lv_value_1_0= ruleNumber ) )
            {
            // InternalCqrsDsl.g:8651:3: ()
            // InternalCqrsDsl.g:8652:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getNumberLiteralAccess().getNumberLiteralAction_0(),
              					current);
              			
            }

            }

            // InternalCqrsDsl.g:8658:3: ( (lv_value_1_0= ruleNumber ) )
            // InternalCqrsDsl.g:8659:4: (lv_value_1_0= ruleNumber )
            {
            // InternalCqrsDsl.g:8659:4: (lv_value_1_0= ruleNumber )
            // InternalCqrsDsl.g:8660:5: lv_value_1_0= ruleNumber
            {
            if ( state.backtracking==0 ) {

              					newCompositeNode(grammarAccess.getNumberLiteralAccess().getValueNumberParserRuleCall_1_0());
              				
            }
            pushFollow(FOLLOW_2);
            lv_value_1_0=ruleNumber();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElementForParent(grammarAccess.getNumberLiteralRule());
              					}
              					set(
              						current,
              						"value",
              						lv_value_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.Number");
              					afterParserOrEnumRuleCall();
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNumberLiteral"


    // $ANTLR start "entryRuleStringLiteral"
    // InternalCqrsDsl.g:8681:1: entryRuleStringLiteral returns [EObject current=null] : iv_ruleStringLiteral= ruleStringLiteral EOF ;
    public final EObject entryRuleStringLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStringLiteral = null;


        try {
            // InternalCqrsDsl.g:8681:54: (iv_ruleStringLiteral= ruleStringLiteral EOF )
            // InternalCqrsDsl.g:8682:2: iv_ruleStringLiteral= ruleStringLiteral EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getStringLiteralRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleStringLiteral=ruleStringLiteral();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleStringLiteral; 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStringLiteral"


    // $ANTLR start "ruleStringLiteral"
    // InternalCqrsDsl.g:8688:1: ruleStringLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleStringLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8694:2: ( ( () ( (lv_value_1_0= RULE_STRING ) ) ) )
            // InternalCqrsDsl.g:8695:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            {
            // InternalCqrsDsl.g:8695:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            // InternalCqrsDsl.g:8696:3: () ( (lv_value_1_0= RULE_STRING ) )
            {
            // InternalCqrsDsl.g:8696:3: ()
            // InternalCqrsDsl.g:8697:4: 
            {
            if ( state.backtracking==0 ) {

              				current = forceCreateModelElement(
              					grammarAccess.getStringLiteralAccess().getStringLiteralAction_0(),
              					current);
              			
            }

            }

            // InternalCqrsDsl.g:8703:3: ( (lv_value_1_0= RULE_STRING ) )
            // InternalCqrsDsl.g:8704:4: (lv_value_1_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:8704:4: (lv_value_1_0= RULE_STRING )
            // InternalCqrsDsl.g:8705:5: lv_value_1_0= RULE_STRING
            {
            lv_value_1_0=(Token)match(input,RULE_STRING,FOLLOW_2); if (state.failed) return current;
            if ( state.backtracking==0 ) {

              					newLeafNode(lv_value_1_0, grammarAccess.getStringLiteralAccess().getValueSTRINGTerminalRuleCall_1_0());
              				
            }
            if ( state.backtracking==0 ) {

              					if (current==null) {
              						current = createModelElement(grammarAccess.getStringLiteralRule());
              					}
              					setWithLastConsumed(
              						current,
              						"value",
              						lv_value_1_0,
              						"org.fuin.dsl.cqrs.CqrsDsl.STRING");
              				
            }

            }


            }


            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStringLiteral"


    // $ANTLR start "entryRuleNumber"
    // InternalCqrsDsl.g:8725:1: entryRuleNumber returns [String current=null] : iv_ruleNumber= ruleNumber EOF ;
    public final String entryRuleNumber() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleNumber = null;



        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens();

        try {
            // InternalCqrsDsl.g:8727:2: (iv_ruleNumber= ruleNumber EOF )
            // InternalCqrsDsl.g:8728:2: iv_ruleNumber= ruleNumber EOF
            {
            if ( state.backtracking==0 ) {
               newCompositeNode(grammarAccess.getNumberRule()); 
            }
            pushFollow(FOLLOW_1);
            iv_ruleNumber=ruleNumber();

            state._fsp--;
            if (state.failed) return current;
            if ( state.backtracking==0 ) {
               current =iv_ruleNumber.getText(); 
            }
            match(input,EOF,FOLLOW_2); if (state.failed) return current;

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {

            	myHiddenTokenState.restore();

        }
        return current;
    }
    // $ANTLR end "entryRuleNumber"


    // $ANTLR start "ruleNumber"
    // InternalCqrsDsl.g:8737:1: ruleNumber returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) ) ;
    public final AntlrDatatypeRuleToken ruleNumber() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_HEX_0=null;
        Token this_INT_1=null;
        Token this_DECIMAL_2=null;
        Token kw=null;
        Token this_INT_4=null;
        Token this_DECIMAL_5=null;


        	enterRule();
        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens();

        try {
            // InternalCqrsDsl.g:8744:2: ( (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) ) )
            // InternalCqrsDsl.g:8745:2: (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) )
            {
            // InternalCqrsDsl.g:8745:2: (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) )
            int alt240=2;
            int LA240_0 = input.LA(1);

            if ( (LA240_0==RULE_HEX) ) {
                alt240=1;
            }
            else if ( (LA240_0==RULE_INT||LA240_0==RULE_DECIMAL) ) {
                alt240=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 240, 0, input);

                throw nvae;
            }
            switch (alt240) {
                case 1 :
                    // InternalCqrsDsl.g:8746:3: this_HEX_0= RULE_HEX
                    {
                    this_HEX_0=(Token)match(input,RULE_HEX,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      			current.merge(this_HEX_0);
                      		
                    }
                    if ( state.backtracking==0 ) {

                      			newLeafNode(this_HEX_0, grammarAccess.getNumberAccess().getHEXTerminalRuleCall_0());
                      		
                    }

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8754:3: ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? )
                    {
                    // InternalCqrsDsl.g:8754:3: ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? )
                    // InternalCqrsDsl.g:8755:4: (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )?
                    {
                    // InternalCqrsDsl.g:8755:4: (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL )
                    int alt237=2;
                    int LA237_0 = input.LA(1);

                    if ( (LA237_0==RULE_INT) ) {
                        alt237=1;
                    }
                    else if ( (LA237_0==RULE_DECIMAL) ) {
                        alt237=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return current;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 237, 0, input);

                        throw nvae;
                    }
                    switch (alt237) {
                        case 1 :
                            // InternalCqrsDsl.g:8756:5: this_INT_1= RULE_INT
                            {
                            this_INT_1=(Token)match(input,RULE_INT,FOLLOW_191); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					current.merge(this_INT_1);
                              				
                            }
                            if ( state.backtracking==0 ) {

                              					newLeafNode(this_INT_1, grammarAccess.getNumberAccess().getINTTerminalRuleCall_1_0_0());
                              				
                            }

                            }
                            break;
                        case 2 :
                            // InternalCqrsDsl.g:8764:5: this_DECIMAL_2= RULE_DECIMAL
                            {
                            this_DECIMAL_2=(Token)match(input,RULE_DECIMAL,FOLLOW_191); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					current.merge(this_DECIMAL_2);
                              				
                            }
                            if ( state.backtracking==0 ) {

                              					newLeafNode(this_DECIMAL_2, grammarAccess.getNumberAccess().getDECIMALTerminalRuleCall_1_0_1());
                              				
                            }

                            }
                            break;

                    }

                    // InternalCqrsDsl.g:8772:4: (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )?
                    int alt239=2;
                    int LA239_0 = input.LA(1);

                    if ( (LA239_0==50) ) {
                        alt239=1;
                    }
                    switch (alt239) {
                        case 1 :
                            // InternalCqrsDsl.g:8773:5: kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL )
                            {
                            kw=(Token)match(input,50,FOLLOW_194); if (state.failed) return current;
                            if ( state.backtracking==0 ) {

                              					current.merge(kw);
                              					newLeafNode(kw, grammarAccess.getNumberAccess().getFullStopKeyword_1_1_0());
                              				
                            }
                            // InternalCqrsDsl.g:8778:5: (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL )
                            int alt238=2;
                            int LA238_0 = input.LA(1);

                            if ( (LA238_0==RULE_INT) ) {
                                alt238=1;
                            }
                            else if ( (LA238_0==RULE_DECIMAL) ) {
                                alt238=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return current;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 238, 0, input);

                                throw nvae;
                            }
                            switch (alt238) {
                                case 1 :
                                    // InternalCqrsDsl.g:8779:6: this_INT_4= RULE_INT
                                    {
                                    this_INT_4=(Token)match(input,RULE_INT,FOLLOW_2); if (state.failed) return current;
                                    if ( state.backtracking==0 ) {

                                      						current.merge(this_INT_4);
                                      					
                                    }
                                    if ( state.backtracking==0 ) {

                                      						newLeafNode(this_INT_4, grammarAccess.getNumberAccess().getINTTerminalRuleCall_1_1_1_0());
                                      					
                                    }

                                    }
                                    break;
                                case 2 :
                                    // InternalCqrsDsl.g:8787:6: this_DECIMAL_5= RULE_DECIMAL
                                    {
                                    this_DECIMAL_5=(Token)match(input,RULE_DECIMAL,FOLLOW_2); if (state.failed) return current;
                                    if ( state.backtracking==0 ) {

                                      						current.merge(this_DECIMAL_5);
                                      					
                                    }
                                    if ( state.backtracking==0 ) {

                                      						newLeafNode(this_DECIMAL_5, grammarAccess.getNumberAccess().getDECIMALTerminalRuleCall_1_1_1_1());
                                      					
                                    }

                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {

            	myHiddenTokenState.restore();

        }
        return current;
    }
    // $ANTLR end "ruleNumber"


    // $ANTLR start "ruleTimeUnit"
    // InternalCqrsDsl.g:8804:1: ruleTimeUnit returns [Enumerator current=null] : ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) ) ;
    public final Enumerator ruleTimeUnit() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;
        Token enumLiteral_5=null;
        Token enumLiteral_6=null;
        Token enumLiteral_7=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8810:2: ( ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) ) )
            // InternalCqrsDsl.g:8811:2: ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) )
            {
            // InternalCqrsDsl.g:8811:2: ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) )
            int alt241=8;
            switch ( input.LA(1) ) {
            case 121:
                {
                alt241=1;
                }
                break;
            case 122:
                {
                alt241=2;
                }
                break;
            case 123:
                {
                alt241=3;
                }
                break;
            case 124:
                {
                alt241=4;
                }
                break;
            case 125:
                {
                alt241=5;
                }
                break;
            case 126:
                {
                alt241=6;
                }
                break;
            case 127:
                {
                alt241=7;
                }
                break;
            case 128:
                {
                alt241=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 241, 0, input);

                throw nvae;
            }

            switch (alt241) {
                case 1 :
                    // InternalCqrsDsl.g:8812:3: (enumLiteral_0= 'millis' )
                    {
                    // InternalCqrsDsl.g:8812:3: (enumLiteral_0= 'millis' )
                    // InternalCqrsDsl.g:8813:4: enumLiteral_0= 'millis'
                    {
                    enumLiteral_0=(Token)match(input,121,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getMillisEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getTimeUnitAccess().getMillisEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8820:3: (enumLiteral_1= 'seconds' )
                    {
                    // InternalCqrsDsl.g:8820:3: (enumLiteral_1= 'seconds' )
                    // InternalCqrsDsl.g:8821:4: enumLiteral_1= 'seconds'
                    {
                    enumLiteral_1=(Token)match(input,122,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getSecondsEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getTimeUnitAccess().getSecondsEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:8828:3: (enumLiteral_2= 'minutes' )
                    {
                    // InternalCqrsDsl.g:8828:3: (enumLiteral_2= 'minutes' )
                    // InternalCqrsDsl.g:8829:4: enumLiteral_2= 'minutes'
                    {
                    enumLiteral_2=(Token)match(input,123,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getMinutesEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getTimeUnitAccess().getMinutesEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:8836:3: (enumLiteral_3= 'hours' )
                    {
                    // InternalCqrsDsl.g:8836:3: (enumLiteral_3= 'hours' )
                    // InternalCqrsDsl.g:8837:4: enumLiteral_3= 'hours'
                    {
                    enumLiteral_3=(Token)match(input,124,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getHoursEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_3, grammarAccess.getTimeUnitAccess().getHoursEnumLiteralDeclaration_3());
                      			
                    }

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:8844:3: (enumLiteral_4= 'days' )
                    {
                    // InternalCqrsDsl.g:8844:3: (enumLiteral_4= 'days' )
                    // InternalCqrsDsl.g:8845:4: enumLiteral_4= 'days'
                    {
                    enumLiteral_4=(Token)match(input,125,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getDaysEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_4, grammarAccess.getTimeUnitAccess().getDaysEnumLiteralDeclaration_4());
                      			
                    }

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:8852:3: (enumLiteral_5= 'weeks' )
                    {
                    // InternalCqrsDsl.g:8852:3: (enumLiteral_5= 'weeks' )
                    // InternalCqrsDsl.g:8853:4: enumLiteral_5= 'weeks'
                    {
                    enumLiteral_5=(Token)match(input,126,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getWeeksEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_5, grammarAccess.getTimeUnitAccess().getWeeksEnumLiteralDeclaration_5());
                      			
                    }

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:8860:3: (enumLiteral_6= 'months' )
                    {
                    // InternalCqrsDsl.g:8860:3: (enumLiteral_6= 'months' )
                    // InternalCqrsDsl.g:8861:4: enumLiteral_6= 'months'
                    {
                    enumLiteral_6=(Token)match(input,127,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getMonthsEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_6, grammarAccess.getTimeUnitAccess().getMonthsEnumLiteralDeclaration_6());
                      			
                    }

                    }


                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:8868:3: (enumLiteral_7= 'years' )
                    {
                    // InternalCqrsDsl.g:8868:3: (enumLiteral_7= 'years' )
                    // InternalCqrsDsl.g:8869:4: enumLiteral_7= 'years'
                    {
                    enumLiteral_7=(Token)match(input,128,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getTimeUnitAccess().getYearsEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_7, grammarAccess.getTimeUnitAccess().getYearsEnumLiteralDeclaration_7());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTimeUnit"


    // $ANTLR start "ruleConsistencyLevel"
    // InternalCqrsDsl.g:8879:1: ruleConsistencyLevel returns [Enumerator current=null] : ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) ) ;
    public final Enumerator ruleConsistencyLevel() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8885:2: ( ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) ) )
            // InternalCqrsDsl.g:8886:2: ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) )
            {
            // InternalCqrsDsl.g:8886:2: ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) )
            int alt242=2;
            int LA242_0 = input.LA(1);

            if ( (LA242_0==129) ) {
                alt242=1;
            }
            else if ( (LA242_0==130) ) {
                alt242=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 242, 0, input);

                throw nvae;
            }
            switch (alt242) {
                case 1 :
                    // InternalCqrsDsl.g:8887:3: (enumLiteral_0= 'weak' )
                    {
                    // InternalCqrsDsl.g:8887:3: (enumLiteral_0= 'weak' )
                    // InternalCqrsDsl.g:8888:4: enumLiteral_0= 'weak'
                    {
                    enumLiteral_0=(Token)match(input,129,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getConsistencyLevelAccess().getWeakEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getConsistencyLevelAccess().getWeakEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8895:3: (enumLiteral_1= 'strong' )
                    {
                    // InternalCqrsDsl.g:8895:3: (enumLiteral_1= 'strong' )
                    // InternalCqrsDsl.g:8896:4: enumLiteral_1= 'strong'
                    {
                    enumLiteral_1=(Token)match(input,130,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getConsistencyLevelAccess().getStrongEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getConsistencyLevelAccess().getStrongEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConsistencyLevel"


    // $ANTLR start "ruleInconsistencyDetection"
    // InternalCqrsDsl.g:8906:1: ruleInconsistencyDetection returns [Enumerator current=null] : ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) ) ;
    public final Enumerator ruleInconsistencyDetection() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8912:2: ( ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) ) )
            // InternalCqrsDsl.g:8913:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) )
            {
            // InternalCqrsDsl.g:8913:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) )
            int alt243=3;
            switch ( input.LA(1) ) {
            case 131:
                {
                alt243=1;
                }
                break;
            case 132:
                {
                alt243=2;
                }
                break;
            case 133:
                {
                alt243=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 243, 0, input);

                throw nvae;
            }

            switch (alt243) {
                case 1 :
                    // InternalCqrsDsl.g:8914:3: (enumLiteral_0= 'never' )
                    {
                    // InternalCqrsDsl.g:8914:3: (enumLiteral_0= 'never' )
                    // InternalCqrsDsl.g:8915:4: enumLiteral_0= 'never'
                    {
                    enumLiteral_0=(Token)match(input,131,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getInconsistencyDetectionAccess().getNeverEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getInconsistencyDetectionAccess().getNeverEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8922:3: (enumLiteral_1= 'manually' )
                    {
                    // InternalCqrsDsl.g:8922:3: (enumLiteral_1= 'manually' )
                    // InternalCqrsDsl.g:8923:4: enumLiteral_1= 'manually'
                    {
                    enumLiteral_1=(Token)match(input,132,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getInconsistencyDetectionAccess().getManuallyEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getInconsistencyDetectionAccess().getManuallyEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:8930:3: (enumLiteral_2= 'automatic' )
                    {
                    // InternalCqrsDsl.g:8930:3: (enumLiteral_2= 'automatic' )
                    // InternalCqrsDsl.g:8931:4: enumLiteral_2= 'automatic'
                    {
                    enumLiteral_2=(Token)match(input,133,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getInconsistencyDetectionAccess().getAutomaticEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getInconsistencyDetectionAccess().getAutomaticEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleInconsistencyDetection"


    // $ANTLR start "ruleInconsistencyResolution"
    // InternalCqrsDsl.g:8941:1: ruleInconsistencyResolution returns [Enumerator current=null] : ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) ) ;
    public final Enumerator ruleInconsistencyResolution() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8947:2: ( ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) ) )
            // InternalCqrsDsl.g:8948:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) )
            {
            // InternalCqrsDsl.g:8948:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) )
            int alt244=4;
            switch ( input.LA(1) ) {
            case 131:
                {
                alt244=1;
                }
                break;
            case 132:
                {
                alt244=2;
                }
                break;
            case 133:
                {
                alt244=3;
                }
                break;
            case 134:
                {
                alt244=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 244, 0, input);

                throw nvae;
            }

            switch (alt244) {
                case 1 :
                    // InternalCqrsDsl.g:8949:3: (enumLiteral_0= 'never' )
                    {
                    // InternalCqrsDsl.g:8949:3: (enumLiteral_0= 'never' )
                    // InternalCqrsDsl.g:8950:4: enumLiteral_0= 'never'
                    {
                    enumLiteral_0=(Token)match(input,131,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getInconsistencyResolutionAccess().getNeverEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getInconsistencyResolutionAccess().getNeverEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:8957:3: (enumLiteral_1= 'manually' )
                    {
                    // InternalCqrsDsl.g:8957:3: (enumLiteral_1= 'manually' )
                    // InternalCqrsDsl.g:8958:4: enumLiteral_1= 'manually'
                    {
                    enumLiteral_1=(Token)match(input,132,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getInconsistencyResolutionAccess().getManuallyEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getInconsistencyResolutionAccess().getManuallyEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:8965:3: (enumLiteral_2= 'automatic' )
                    {
                    // InternalCqrsDsl.g:8965:3: (enumLiteral_2= 'automatic' )
                    // InternalCqrsDsl.g:8966:4: enumLiteral_2= 'automatic'
                    {
                    enumLiteral_2=(Token)match(input,133,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getInconsistencyResolutionAccess().getAutomaticEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getInconsistencyResolutionAccess().getAutomaticEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:8973:3: (enumLiteral_3= 'workflow' )
                    {
                    // InternalCqrsDsl.g:8973:3: (enumLiteral_3= 'workflow' )
                    // InternalCqrsDsl.g:8974:4: enumLiteral_3= 'workflow'
                    {
                    enumLiteral_3=(Token)match(input,134,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getInconsistencyResolutionAccess().getWorkflowEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_3, grammarAccess.getInconsistencyResolutionAccess().getWorkflowEnumLiteralDeclaration_3());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleInconsistencyResolution"


    // $ANTLR start "ruleProtectionLevel"
    // InternalCqrsDsl.g:8984:1: ruleProtectionLevel returns [Enumerator current=null] : ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) ) ;
    public final Enumerator ruleProtectionLevel() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:8990:2: ( ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) ) )
            // InternalCqrsDsl.g:8991:2: ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) )
            {
            // InternalCqrsDsl.g:8991:2: ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) )
            int alt245=3;
            switch ( input.LA(1) ) {
            case 135:
                {
                alt245=1;
                }
                break;
            case 136:
                {
                alt245=2;
                }
                break;
            case 137:
                {
                alt245=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 245, 0, input);

                throw nvae;
            }

            switch (alt245) {
                case 1 :
                    // InternalCqrsDsl.g:8992:3: (enumLiteral_0= 'none' )
                    {
                    // InternalCqrsDsl.g:8992:3: (enumLiteral_0= 'none' )
                    // InternalCqrsDsl.g:8993:4: enumLiteral_0= 'none'
                    {
                    enumLiteral_0=(Token)match(input,135,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getProtectionLevelAccess().getNoneEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getProtectionLevelAccess().getNoneEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:9000:3: (enumLiteral_1= 'personal' )
                    {
                    // InternalCqrsDsl.g:9000:3: (enumLiteral_1= 'personal' )
                    // InternalCqrsDsl.g:9001:4: enumLiteral_1= 'personal'
                    {
                    enumLiteral_1=(Token)match(input,136,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getProtectionLevelAccess().getPersonalEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getProtectionLevelAccess().getPersonalEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:9008:3: (enumLiteral_2= 'sensitive' )
                    {
                    // InternalCqrsDsl.g:9008:3: (enumLiteral_2= 'sensitive' )
                    // InternalCqrsDsl.g:9009:4: enumLiteral_2= 'sensitive'
                    {
                    enumLiteral_2=(Token)match(input,137,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getProtectionLevelAccess().getSensitiveEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getProtectionLevelAccess().getSensitiveEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleProtectionLevel"


    // $ANTLR start "ruleLawfulBasis"
    // InternalCqrsDsl.g:9019:1: ruleLawfulBasis returns [Enumerator current=null] : ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) ) ;
    public final Enumerator ruleLawfulBasis() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;
        Token enumLiteral_5=null;
        Token enumLiteral_6=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:9025:2: ( ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) ) )
            // InternalCqrsDsl.g:9026:2: ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) )
            {
            // InternalCqrsDsl.g:9026:2: ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) )
            int alt246=7;
            switch ( input.LA(1) ) {
            case 138:
                {
                alt246=1;
                }
                break;
            case 139:
                {
                alt246=2;
                }
                break;
            case 140:
                {
                alt246=3;
                }
                break;
            case 141:
                {
                alt246=4;
                }
                break;
            case 142:
                {
                alt246=5;
                }
                break;
            case 143:
                {
                alt246=6;
                }
                break;
            case 144:
                {
                alt246=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 246, 0, input);

                throw nvae;
            }

            switch (alt246) {
                case 1 :
                    // InternalCqrsDsl.g:9027:3: (enumLiteral_0= 'consent' )
                    {
                    // InternalCqrsDsl.g:9027:3: (enumLiteral_0= 'consent' )
                    // InternalCqrsDsl.g:9028:4: enumLiteral_0= 'consent'
                    {
                    enumLiteral_0=(Token)match(input,138,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getLawfulBasisAccess().getConsentEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getLawfulBasisAccess().getConsentEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:9035:3: (enumLiteral_1= 'explicit_consent' )
                    {
                    // InternalCqrsDsl.g:9035:3: (enumLiteral_1= 'explicit_consent' )
                    // InternalCqrsDsl.g:9036:4: enumLiteral_1= 'explicit_consent'
                    {
                    enumLiteral_1=(Token)match(input,139,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getLawfulBasisAccess().getExplicit_consentEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getLawfulBasisAccess().getExplicit_consentEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:9043:3: (enumLiteral_2= 'contract' )
                    {
                    // InternalCqrsDsl.g:9043:3: (enumLiteral_2= 'contract' )
                    // InternalCqrsDsl.g:9044:4: enumLiteral_2= 'contract'
                    {
                    enumLiteral_2=(Token)match(input,140,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getLawfulBasisAccess().getContractEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getLawfulBasisAccess().getContractEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:9051:3: (enumLiteral_3= 'legal_obligation' )
                    {
                    // InternalCqrsDsl.g:9051:3: (enumLiteral_3= 'legal_obligation' )
                    // InternalCqrsDsl.g:9052:4: enumLiteral_3= 'legal_obligation'
                    {
                    enumLiteral_3=(Token)match(input,141,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getLawfulBasisAccess().getLegal_obligationEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_3, grammarAccess.getLawfulBasisAccess().getLegal_obligationEnumLiteralDeclaration_3());
                      			
                    }

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:9059:3: (enumLiteral_4= 'vital_interests' )
                    {
                    // InternalCqrsDsl.g:9059:3: (enumLiteral_4= 'vital_interests' )
                    // InternalCqrsDsl.g:9060:4: enumLiteral_4= 'vital_interests'
                    {
                    enumLiteral_4=(Token)match(input,142,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getLawfulBasisAccess().getVital_interestsEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_4, grammarAccess.getLawfulBasisAccess().getVital_interestsEnumLiteralDeclaration_4());
                      			
                    }

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:9067:3: (enumLiteral_5= 'public_task' )
                    {
                    // InternalCqrsDsl.g:9067:3: (enumLiteral_5= 'public_task' )
                    // InternalCqrsDsl.g:9068:4: enumLiteral_5= 'public_task'
                    {
                    enumLiteral_5=(Token)match(input,143,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getLawfulBasisAccess().getPublic_taskEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_5, grammarAccess.getLawfulBasisAccess().getPublic_taskEnumLiteralDeclaration_5());
                      			
                    }

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:9075:3: (enumLiteral_6= 'legitimate_interests' )
                    {
                    // InternalCqrsDsl.g:9075:3: (enumLiteral_6= 'legitimate_interests' )
                    // InternalCqrsDsl.g:9076:4: enumLiteral_6= 'legitimate_interests'
                    {
                    enumLiteral_6=(Token)match(input,144,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getLawfulBasisAccess().getLegitimate_interestsEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_6, grammarAccess.getLawfulBasisAccess().getLegitimate_interestsEnumLiteralDeclaration_6());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleLawfulBasis"


    // $ANTLR start "ruleSpecialCategory"
    // InternalCqrsDsl.g:9086:1: ruleSpecialCategory returns [Enumerator current=null] : ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) ) ;
    public final Enumerator ruleSpecialCategory() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;
        Token enumLiteral_5=null;
        Token enumLiteral_6=null;
        Token enumLiteral_7=null;
        Token enumLiteral_8=null;
        Token enumLiteral_9=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:9092:2: ( ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) ) )
            // InternalCqrsDsl.g:9093:2: ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) )
            {
            // InternalCqrsDsl.g:9093:2: ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) )
            int alt247=10;
            switch ( input.LA(1) ) {
            case 145:
                {
                alt247=1;
                }
                break;
            case 146:
                {
                alt247=2;
                }
                break;
            case 147:
                {
                alt247=3;
                }
                break;
            case 148:
                {
                alt247=4;
                }
                break;
            case 149:
                {
                alt247=5;
                }
                break;
            case 150:
                {
                alt247=6;
                }
                break;
            case 151:
                {
                alt247=7;
                }
                break;
            case 152:
                {
                alt247=8;
                }
                break;
            case 153:
                {
                alt247=9;
                }
                break;
            case 154:
                {
                alt247=10;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 247, 0, input);

                throw nvae;
            }

            switch (alt247) {
                case 1 :
                    // InternalCqrsDsl.g:9094:3: (enumLiteral_0= 'health' )
                    {
                    // InternalCqrsDsl.g:9094:3: (enumLiteral_0= 'health' )
                    // InternalCqrsDsl.g:9095:4: enumLiteral_0= 'health'
                    {
                    enumLiteral_0=(Token)match(input,145,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getHealthEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getSpecialCategoryAccess().getHealthEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:9102:3: (enumLiteral_1= 'genetic' )
                    {
                    // InternalCqrsDsl.g:9102:3: (enumLiteral_1= 'genetic' )
                    // InternalCqrsDsl.g:9103:4: enumLiteral_1= 'genetic'
                    {
                    enumLiteral_1=(Token)match(input,146,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getGeneticEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getSpecialCategoryAccess().getGeneticEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:9110:3: (enumLiteral_2= 'biometric' )
                    {
                    // InternalCqrsDsl.g:9110:3: (enumLiteral_2= 'biometric' )
                    // InternalCqrsDsl.g:9111:4: enumLiteral_2= 'biometric'
                    {
                    enumLiteral_2=(Token)match(input,147,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getBiometricEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getSpecialCategoryAccess().getBiometricEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:9118:3: (enumLiteral_3= 'racial' )
                    {
                    // InternalCqrsDsl.g:9118:3: (enumLiteral_3= 'racial' )
                    // InternalCqrsDsl.g:9119:4: enumLiteral_3= 'racial'
                    {
                    enumLiteral_3=(Token)match(input,148,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getRacialEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_3, grammarAccess.getSpecialCategoryAccess().getRacialEnumLiteralDeclaration_3());
                      			
                    }

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:9126:3: (enumLiteral_4= 'political' )
                    {
                    // InternalCqrsDsl.g:9126:3: (enumLiteral_4= 'political' )
                    // InternalCqrsDsl.g:9127:4: enumLiteral_4= 'political'
                    {
                    enumLiteral_4=(Token)match(input,149,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getPoliticalEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_4, grammarAccess.getSpecialCategoryAccess().getPoliticalEnumLiteralDeclaration_4());
                      			
                    }

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:9134:3: (enumLiteral_5= 'religious' )
                    {
                    // InternalCqrsDsl.g:9134:3: (enumLiteral_5= 'religious' )
                    // InternalCqrsDsl.g:9135:4: enumLiteral_5= 'religious'
                    {
                    enumLiteral_5=(Token)match(input,150,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getReligiousEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_5, grammarAccess.getSpecialCategoryAccess().getReligiousEnumLiteralDeclaration_5());
                      			
                    }

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:9142:3: (enumLiteral_6= 'philosophical' )
                    {
                    // InternalCqrsDsl.g:9142:3: (enumLiteral_6= 'philosophical' )
                    // InternalCqrsDsl.g:9143:4: enumLiteral_6= 'philosophical'
                    {
                    enumLiteral_6=(Token)match(input,151,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getPhilosophicalEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_6, grammarAccess.getSpecialCategoryAccess().getPhilosophicalEnumLiteralDeclaration_6());
                      			
                    }

                    }


                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:9150:3: (enumLiteral_7= 'trade_union' )
                    {
                    // InternalCqrsDsl.g:9150:3: (enumLiteral_7= 'trade_union' )
                    // InternalCqrsDsl.g:9151:4: enumLiteral_7= 'trade_union'
                    {
                    enumLiteral_7=(Token)match(input,152,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getTrade_unionEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_7, grammarAccess.getSpecialCategoryAccess().getTrade_unionEnumLiteralDeclaration_7());
                      			
                    }

                    }


                    }
                    break;
                case 9 :
                    // InternalCqrsDsl.g:9158:3: (enumLiteral_8= 'sex_life' )
                    {
                    // InternalCqrsDsl.g:9158:3: (enumLiteral_8= 'sex_life' )
                    // InternalCqrsDsl.g:9159:4: enumLiteral_8= 'sex_life'
                    {
                    enumLiteral_8=(Token)match(input,153,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getSex_lifeEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_8, grammarAccess.getSpecialCategoryAccess().getSex_lifeEnumLiteralDeclaration_8());
                      			
                    }

                    }


                    }
                    break;
                case 10 :
                    // InternalCqrsDsl.g:9166:3: (enumLiteral_9= 'sexual_orientation' )
                    {
                    // InternalCqrsDsl.g:9166:3: (enumLiteral_9= 'sexual_orientation' )
                    // InternalCqrsDsl.g:9167:4: enumLiteral_9= 'sexual_orientation'
                    {
                    enumLiteral_9=(Token)match(input,154,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getSpecialCategoryAccess().getSexual_orientationEnumLiteralDeclaration_9().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_9, grammarAccess.getSpecialCategoryAccess().getSexual_orientationEnumLiteralDeclaration_9());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSpecialCategory"


    // $ANTLR start "ruleErasureStrategy"
    // InternalCqrsDsl.g:9177:1: ruleErasureStrategy returns [Enumerator current=null] : ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) ) ;
    public final Enumerator ruleErasureStrategy() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:9183:2: ( ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) ) )
            // InternalCqrsDsl.g:9184:2: ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) )
            {
            // InternalCqrsDsl.g:9184:2: ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) )
            int alt248=5;
            switch ( input.LA(1) ) {
            case 155:
                {
                alt248=1;
                }
                break;
            case 156:
                {
                alt248=2;
                }
                break;
            case 157:
                {
                alt248=3;
                }
                break;
            case 158:
                {
                alt248=4;
                }
                break;
            case 159:
                {
                alt248=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 248, 0, input);

                throw nvae;
            }

            switch (alt248) {
                case 1 :
                    // InternalCqrsDsl.g:9185:3: (enumLiteral_0= 'delete' )
                    {
                    // InternalCqrsDsl.g:9185:3: (enumLiteral_0= 'delete' )
                    // InternalCqrsDsl.g:9186:4: enumLiteral_0= 'delete'
                    {
                    enumLiteral_0=(Token)match(input,155,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getErasureStrategyAccess().getDeleteEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getErasureStrategyAccess().getDeleteEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:9193:3: (enumLiteral_1= 'anonymize' )
                    {
                    // InternalCqrsDsl.g:9193:3: (enumLiteral_1= 'anonymize' )
                    // InternalCqrsDsl.g:9194:4: enumLiteral_1= 'anonymize'
                    {
                    enumLiteral_1=(Token)match(input,156,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getErasureStrategyAccess().getAnonymizeEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getErasureStrategyAccess().getAnonymizeEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:9201:3: (enumLiteral_2= 'pseudonymize' )
                    {
                    // InternalCqrsDsl.g:9201:3: (enumLiteral_2= 'pseudonymize' )
                    // InternalCqrsDsl.g:9202:4: enumLiteral_2= 'pseudonymize'
                    {
                    enumLiteral_2=(Token)match(input,157,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getErasureStrategyAccess().getPseudonymizeEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getErasureStrategyAccess().getPseudonymizeEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:9209:3: (enumLiteral_3= 'archive' )
                    {
                    // InternalCqrsDsl.g:9209:3: (enumLiteral_3= 'archive' )
                    // InternalCqrsDsl.g:9210:4: enumLiteral_3= 'archive'
                    {
                    enumLiteral_3=(Token)match(input,158,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getErasureStrategyAccess().getArchiveEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_3, grammarAccess.getErasureStrategyAccess().getArchiveEnumLiteralDeclaration_3());
                      			
                    }

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:9217:3: (enumLiteral_4= 'review' )
                    {
                    // InternalCqrsDsl.g:9217:3: (enumLiteral_4= 'review' )
                    // InternalCqrsDsl.g:9218:4: enumLiteral_4= 'review'
                    {
                    enumLiteral_4=(Token)match(input,159,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getErasureStrategyAccess().getReviewEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_4, grammarAccess.getErasureStrategyAccess().getReviewEnumLiteralDeclaration_4());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleErasureStrategy"


    // $ANTLR start "ruleCompareOp"
    // InternalCqrsDsl.g:9228:1: ruleCompareOp returns [Enumerator current=null] : ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '<' ) | (enumLiteral_5= '>' ) ) ;
    public final Enumerator ruleCompareOp() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;
        Token enumLiteral_5=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:9234:2: ( ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '<' ) | (enumLiteral_5= '>' ) ) )
            // InternalCqrsDsl.g:9235:2: ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '<' ) | (enumLiteral_5= '>' ) )
            {
            // InternalCqrsDsl.g:9235:2: ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '<=' ) | (enumLiteral_3= '>=' ) | (enumLiteral_4= '<' ) | (enumLiteral_5= '>' ) )
            int alt249=6;
            switch ( input.LA(1) ) {
            case 160:
                {
                alt249=1;
                }
                break;
            case 161:
                {
                alt249=2;
                }
                break;
            case 162:
                {
                alt249=3;
                }
                break;
            case 163:
                {
                alt249=4;
                }
                break;
            case 88:
                {
                alt249=5;
                }
                break;
            case 89:
                {
                alt249=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 249, 0, input);

                throw nvae;
            }

            switch (alt249) {
                case 1 :
                    // InternalCqrsDsl.g:9236:3: (enumLiteral_0= '==' )
                    {
                    // InternalCqrsDsl.g:9236:3: (enumLiteral_0= '==' )
                    // InternalCqrsDsl.g:9237:4: enumLiteral_0= '=='
                    {
                    enumLiteral_0=(Token)match(input,160,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCompareOpAccess().getEqEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getCompareOpAccess().getEqEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:9244:3: (enumLiteral_1= '!=' )
                    {
                    // InternalCqrsDsl.g:9244:3: (enumLiteral_1= '!=' )
                    // InternalCqrsDsl.g:9245:4: enumLiteral_1= '!='
                    {
                    enumLiteral_1=(Token)match(input,161,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCompareOpAccess().getNeEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getCompareOpAccess().getNeEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:9252:3: (enumLiteral_2= '<=' )
                    {
                    // InternalCqrsDsl.g:9252:3: (enumLiteral_2= '<=' )
                    // InternalCqrsDsl.g:9253:4: enumLiteral_2= '<='
                    {
                    enumLiteral_2=(Token)match(input,162,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCompareOpAccess().getLeEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getCompareOpAccess().getLeEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:9260:3: (enumLiteral_3= '>=' )
                    {
                    // InternalCqrsDsl.g:9260:3: (enumLiteral_3= '>=' )
                    // InternalCqrsDsl.g:9261:4: enumLiteral_3= '>='
                    {
                    enumLiteral_3=(Token)match(input,163,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCompareOpAccess().getGeEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_3, grammarAccess.getCompareOpAccess().getGeEnumLiteralDeclaration_3());
                      			
                    }

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:9268:3: (enumLiteral_4= '<' )
                    {
                    // InternalCqrsDsl.g:9268:3: (enumLiteral_4= '<' )
                    // InternalCqrsDsl.g:9269:4: enumLiteral_4= '<'
                    {
                    enumLiteral_4=(Token)match(input,88,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCompareOpAccess().getLtEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_4, grammarAccess.getCompareOpAccess().getLtEnumLiteralDeclaration_4());
                      			
                    }

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:9276:3: (enumLiteral_5= '>' )
                    {
                    // InternalCqrsDsl.g:9276:3: (enumLiteral_5= '>' )
                    // InternalCqrsDsl.g:9277:4: enumLiteral_5= '>'
                    {
                    enumLiteral_5=(Token)match(input,89,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCompareOpAccess().getGtEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_5, grammarAccess.getCompareOpAccess().getGtEnumLiteralDeclaration_5());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCompareOp"


    // $ANTLR start "ruleCollisionStrategy"
    // InternalCqrsDsl.g:9287:1: ruleCollisionStrategy returns [Enumerator current=null] : ( (enumLiteral_0= 'refuse' ) | (enumLiteral_1= 'overwrite' ) | (enumLiteral_2= 'skip' ) ) ;
    public final Enumerator ruleCollisionStrategy() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:9293:2: ( ( (enumLiteral_0= 'refuse' ) | (enumLiteral_1= 'overwrite' ) | (enumLiteral_2= 'skip' ) ) )
            // InternalCqrsDsl.g:9294:2: ( (enumLiteral_0= 'refuse' ) | (enumLiteral_1= 'overwrite' ) | (enumLiteral_2= 'skip' ) )
            {
            // InternalCqrsDsl.g:9294:2: ( (enumLiteral_0= 'refuse' ) | (enumLiteral_1= 'overwrite' ) | (enumLiteral_2= 'skip' ) )
            int alt250=3;
            switch ( input.LA(1) ) {
            case 164:
                {
                alt250=1;
                }
                break;
            case 165:
                {
                alt250=2;
                }
                break;
            case 166:
                {
                alt250=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return current;}
                NoViableAltException nvae =
                    new NoViableAltException("", 250, 0, input);

                throw nvae;
            }

            switch (alt250) {
                case 1 :
                    // InternalCqrsDsl.g:9295:3: (enumLiteral_0= 'refuse' )
                    {
                    // InternalCqrsDsl.g:9295:3: (enumLiteral_0= 'refuse' )
                    // InternalCqrsDsl.g:9296:4: enumLiteral_0= 'refuse'
                    {
                    enumLiteral_0=(Token)match(input,164,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCollisionStrategyAccess().getRefuseEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_0, grammarAccess.getCollisionStrategyAccess().getRefuseEnumLiteralDeclaration_0());
                      			
                    }

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:9303:3: (enumLiteral_1= 'overwrite' )
                    {
                    // InternalCqrsDsl.g:9303:3: (enumLiteral_1= 'overwrite' )
                    // InternalCqrsDsl.g:9304:4: enumLiteral_1= 'overwrite'
                    {
                    enumLiteral_1=(Token)match(input,165,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCollisionStrategyAccess().getOverwriteEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_1, grammarAccess.getCollisionStrategyAccess().getOverwriteEnumLiteralDeclaration_1());
                      			
                    }

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:9311:3: (enumLiteral_2= 'skip' )
                    {
                    // InternalCqrsDsl.g:9311:3: (enumLiteral_2= 'skip' )
                    // InternalCqrsDsl.g:9312:4: enumLiteral_2= 'skip'
                    {
                    enumLiteral_2=(Token)match(input,166,FOLLOW_2); if (state.failed) return current;
                    if ( state.backtracking==0 ) {

                      				current = grammarAccess.getCollisionStrategyAccess().getSkipEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                      				newLeafNode(enumLiteral_2, grammarAccess.getCollisionStrategyAccess().getSkipEnumLiteralDeclaration_2());
                      			
                    }

                    }


                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

              	leaveRule();

            }
        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCollisionStrategy"

    // $ANTLR start synpred1_InternalCqrsDsl
    public final void synpred1_InternalCqrsDsl_fragment() throws RecognitionException {   
        // InternalCqrsDsl.g:6536:4: ( ( ( ( ruleFQN ) ) '(' ) )
        // InternalCqrsDsl.g:6536:5: ( ( ( ruleFQN ) ) '(' )
        {
        // InternalCqrsDsl.g:6536:5: ( ( ( ruleFQN ) ) '(' )
        // InternalCqrsDsl.g:6537:5: ( ( ruleFQN ) ) '('
        {
        // InternalCqrsDsl.g:6537:5: ( ( ruleFQN ) )
        // InternalCqrsDsl.g:6538:6: ( ruleFQN )
        {
        // InternalCqrsDsl.g:6538:6: ( ruleFQN )
        // InternalCqrsDsl.g:6539:7: ruleFQN
        {
        pushFollow(FOLLOW_69);
        ruleFQN();

        state._fsp--;
        if (state.failed) return ;

        }


        }

        match(input,48,FOLLOW_2); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred1_InternalCqrsDsl

    // Delegated rules

    public final boolean synpred1_InternalCqrsDsl() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_InternalCqrsDsl_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA11 dfa11 = new DFA11(this);
    protected DFA13 dfa13 = new DFA13(this);
    protected DFA14 dfa14 = new DFA14(this);
    static final String dfa_1s = "\6\uffff";
    static final String dfa_2s = "\1\uffff\1\3\2\uffff\1\3\1\uffff";
    static final String dfa_3s = "\1\6\1\5\1\6\1\uffff\1\5\1\uffff";
    static final String dfa_4s = "\1\6\1\151\1\170\1\uffff\1\151\1\uffff";
    static final String dfa_5s = "\3\uffff\1\1\1\uffff\1\2";
    static final String dfa_6s = "\6\uffff}>";
    static final String[] dfa_7s = {
            "\1\1",
            "\1\3\11\uffff\2\3\2\uffff\3\3\6\uffff\1\3\11\uffff\1\3\2\uffff\1\3\10\uffff\1\2\2\uffff\1\3\1\uffff\1\3\2\uffff\1\3\1\uffff\2\3\2\uffff\1\3\1\uffff\1\3\2\uffff\1\3\30\uffff\3\3\2\uffff\1\3\2\uffff\2\3\1\uffff\1\3",
            "\1\4\161\uffff\1\5",
            "",
            "\1\3\11\uffff\2\3\2\uffff\3\3\6\uffff\1\3\11\uffff\1\3\2\uffff\1\3\10\uffff\1\2\2\uffff\1\3\1\uffff\1\3\2\uffff\1\3\1\uffff\2\3\2\uffff\1\3\1\uffff\1\3\2\uffff\1\3\30\uffff\3\3\2\uffff\1\3\2\uffff\2\3\1\uffff\1\3",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "458:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )";
        }
    }
    static final String dfa_8s = "\51\uffff";
    static final String dfa_9s = "\1\5\1\25\3\uffff\1\6\11\uffff\1\60\1\6\1\4\1\60\7\37\1\4\1\67\1\7\11\37\1\7\2\37";
    static final String dfa_10s = "\2\151\3\uffff\1\6\11\uffff\1\136\1\6\1\167\1\136\4\61\2\62\1\61\1\167\1\136\1\11\4\61\2\62\3\61\1\11\2\61";
    static final String dfa_11s = "\2\uffff\1\1\1\2\1\3\1\uffff\1\4\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\5\32\uffff";
    static final String dfa_12s = "\51\uffff}>";
    static final String[] dfa_13s = {
            "\1\1\17\uffff\1\4\6\uffff\1\15\11\uffff\1\2\2\uffff\1\6\13\uffff\1\3\1\uffff\1\4\2\uffff\1\4\1\uffff\2\4\2\uffff\1\7\1\uffff\1\4\2\uffff\1\4\30\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13\1\uffff\1\14",
            "\1\4\6\uffff\1\15\11\uffff\1\2\2\uffff\1\6\1\uffff\1\16\11\uffff\1\3\1\uffff\1\4\2\uffff\1\4\1\uffff\2\4\2\uffff\1\7\1\uffff\1\4\2\uffff\1\4\30\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13\1\uffff\1\14",
            "",
            "",
            "",
            "\1\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\21\1\uffff\1\20\4\uffff\1\4\10\uffff\1\7\35\uffff\1\5",
            "\1\22",
            "\1\31\2\uffff\1\27\1\26\1\30\52\uffff\1\23\101\uffff\1\25\1\24",
            "\1\21\1\uffff\1\20\4\uffff\1\4\10\uffff\1\7\35\uffff\1\5",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33\1\34",
            "\1\32\21\uffff\1\33\1\34",
            "\1\32\21\uffff\1\33",
            "\1\43\2\uffff\1\41\1\40\1\42\52\uffff\1\35\101\uffff\1\37\1\36",
            "\1\4\10\uffff\1\7\35\uffff\1\5",
            "\1\44\1\uffff\1\45",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33\1\46",
            "\1\32\21\uffff\1\33\1\46",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33",
            "\1\47\1\uffff\1\50",
            "\1\32\21\uffff\1\33",
            "\1\32\21\uffff\1\33"
    };

    static final short[] dfa_8 = DFA.unpackEncodedString(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final char[] dfa_10 = DFA.unpackEncodedStringToUnsignedChars(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[] dfa_12 = DFA.unpackEncodedString(dfa_12s);
    static final short[][] dfa_13 = unpackEncodedStringArray(dfa_13s);

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = dfa_8;
            this.eof = dfa_8;
            this.min = dfa_9;
            this.max = dfa_10;
            this.accept = dfa_11;
            this.special = dfa_12;
            this.transition = dfa_13;
        }
        public String getDescription() {
            return "590:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_BusinessRule_4= ruleBusinessRule | this_Event_5= ruleEvent | this_Command_6= ruleCommand | this_CommandHandler_7= ruleCommandHandler | this_Projection_8= ruleProjection | this_View_9= ruleView | this_ProcessManager_10= ruleProcessManager | this_DataProtection_11= ruleDataProtection )";
        }
    }
    static final String dfa_14s = "\50\uffff";
    static final String dfa_15s = "\1\5\1\25\3\uffff\1\6\10\uffff\1\60\1\6\1\4\1\60\7\37\1\4\1\67\1\7\11\37\1\7\2\37";
    static final String dfa_16s = "\2\151\3\uffff\1\6\10\uffff\1\136\1\6\1\167\1\136\4\61\2\62\1\61\1\167\1\136\1\11\4\61\2\62\3\61\1\11\2\61";
    static final String dfa_17s = "\2\uffff\1\1\1\2\1\3\1\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\32\uffff";
    static final String dfa_18s = "\50\uffff}>";
    static final String[] dfa_19s = {
            "\1\1\17\uffff\1\4\6\uffff\1\15\11\uffff\1\2\2\uffff\1\6\13\uffff\1\3\1\uffff\1\4\2\uffff\1\4\1\uffff\2\4\2\uffff\1\7\1\uffff\1\4\2\uffff\1\4\30\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13\1\uffff\1\14",
            "\1\4\6\uffff\1\15\11\uffff\1\2\2\uffff\1\6\13\uffff\1\3\1\uffff\1\4\2\uffff\1\4\1\uffff\2\4\2\uffff\1\7\1\uffff\1\4\2\uffff\1\4\30\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13\1\uffff\1\14",
            "",
            "",
            "",
            "\1\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\20\1\uffff\1\17\4\uffff\1\4\10\uffff\1\7\35\uffff\1\5",
            "\1\21",
            "\1\30\2\uffff\1\26\1\25\1\27\52\uffff\1\22\101\uffff\1\24\1\23",
            "\1\20\1\uffff\1\17\4\uffff\1\4\10\uffff\1\7\35\uffff\1\5",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32\1\33",
            "\1\31\21\uffff\1\32\1\33",
            "\1\31\21\uffff\1\32",
            "\1\42\2\uffff\1\40\1\37\1\41\52\uffff\1\34\101\uffff\1\36\1\35",
            "\1\4\10\uffff\1\7\35\uffff\1\5",
            "\1\43\1\uffff\1\44",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32\1\45",
            "\1\31\21\uffff\1\32\1\45",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32",
            "\1\46\1\uffff\1\47",
            "\1\31\21\uffff\1\32",
            "\1\31\21\uffff\1\32"
    };

    static final short[] dfa_14 = DFA.unpackEncodedString(dfa_14s);
    static final char[] dfa_15 = DFA.unpackEncodedStringToUnsignedChars(dfa_15s);
    static final char[] dfa_16 = DFA.unpackEncodedStringToUnsignedChars(dfa_16s);
    static final short[] dfa_17 = DFA.unpackEncodedString(dfa_17s);
    static final short[] dfa_18 = DFA.unpackEncodedString(dfa_18s);
    static final short[][] dfa_19 = unpackEncodedStringArray(dfa_19s);

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = dfa_14;
            this.eof = dfa_14;
            this.min = dfa_15;
            this.max = dfa_16;
            this.accept = dfa_17;
            this.special = dfa_18;
            this.transition = dfa_19;
        }
        public String getDescription() {
            return "716:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection )";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x00000000001B8020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000198020L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000118020L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000018000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x34A00240103A8020L,0x000002C9C0F80025L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x34A00240103A8020L,0x000002C9C0000025L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x34A00240102A8020L,0x000002C9C0000025L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x34A0024010288020L,0x000002C9C0000025L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x34A0024010208020L,0x000002C9C0000025L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0010000000004390L,0x00D0000000000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000000400040L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000000000000L,0xFE00000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000002000020L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000038L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000004000020L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000078L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000001000020L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000020000020L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000380L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000F40008020L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000007FE0000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000F80008020L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000E00008020L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000C00008020L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x000000000001FC00L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000800008020L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000001000008000L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x00000000F8000000L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000028000004000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000030000004000L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000040000008060L,0x0000000000008000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000000008000060L,0x0000000000008000L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0000100000008000L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0001800000000040L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000200000000002L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0004000000000002L,0x0000000003000000L,0x0000000F00000000L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x0010000000000040L});
    public static final BitSet FOLLOW_68 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_69 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_70 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_71 = new BitSet(new long[]{0x0000000000008060L,0x0000000000008000L});
    public static final BitSet FOLLOW_72 = new BitSet(new long[]{0x0040000000004000L});
    public static final BitSet FOLLOW_73 = new BitSet(new long[]{0x0000040000000060L,0x0000000000008000L});
    public static final BitSet FOLLOW_74 = new BitSet(new long[]{0x0080000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_75 = new BitSet(new long[]{0x0100002000004000L,0x0000000004000000L});
    public static final BitSet FOLLOW_76 = new BitSet(new long[]{0x0000002000004000L,0x0000000004000000L});
    public static final BitSet FOLLOW_77 = new BitSet(new long[]{0x0000002000004000L});
    public static final BitSet FOLLOW_78 = new BitSet(new long[]{0x0200000000108060L,0x0000000000F98800L});
    public static final BitSet FOLLOW_79 = new BitSet(new long[]{0x0200000000108060L,0x0000000000018800L});
    public static final BitSet FOLLOW_80 = new BitSet(new long[]{0x0200000000008060L,0x0000000000018800L});
    public static final BitSet FOLLOW_81 = new BitSet(new long[]{0x0000000000008020L,0x0000000000010800L});
    public static final BitSet FOLLOW_82 = new BitSet(new long[]{0x0000000000008020L,0x0000000000010000L});
    public static final BitSet FOLLOW_83 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_84 = new BitSet(new long[]{0x0900002000004000L,0x0000000004000000L});
    public static final BitSet FOLLOW_85 = new BitSet(new long[]{0x0000000000108060L,0x0000000000F98800L});
    public static final BitSet FOLLOW_86 = new BitSet(new long[]{0x0000000000108060L,0x0000000000018800L});
    public static final BitSet FOLLOW_87 = new BitSet(new long[]{0x0000000000008060L,0x0000000000018800L});
    public static final BitSet FOLLOW_88 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_89 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_90 = new BitSet(new long[]{0x4000000000100060L,0x0000000000F88000L});
    public static final BitSet FOLLOW_91 = new BitSet(new long[]{0x4000000000100060L,0x0000000000008000L});
    public static final BitSet FOLLOW_92 = new BitSet(new long[]{0x4000000000000060L,0x0000000000008000L});
    public static final BitSet FOLLOW_93 = new BitSet(new long[]{0x8000000000000060L});
    public static final BitSet FOLLOW_94 = new BitSet(new long[]{0x8000000000008060L});
    public static final BitSet FOLLOW_95 = new BitSet(new long[]{0x8000000000000040L});
    public static final BitSet FOLLOW_96 = new BitSet(new long[]{0x0001000000004002L});
    public static final BitSet FOLLOW_97 = new BitSet(new long[]{0x0010000000000390L,0x00C0000000000000L});
    public static final BitSet FOLLOW_98 = new BitSet(new long[]{0x0002000080000000L});
    public static final BitSet FOLLOW_99 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000001L});
    public static final BitSet FOLLOW_100 = new BitSet(new long[]{0x0000000000004000L,0x0000000000000002L});
    public static final BitSet FOLLOW_101 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_102 = new BitSet(new long[]{0x0000002000004000L,0x0000000004000018L});
    public static final BitSet FOLLOW_103 = new BitSet(new long[]{0x0000002000004000L,0x0000000004000010L});
    public static final BitSet FOLLOW_104 = new BitSet(new long[]{0x34A0024010308060L,0x000002C9C0F98825L});
    public static final BitSet FOLLOW_105 = new BitSet(new long[]{0x34A0024010308060L,0x000002C9C0018825L});
    public static final BitSet FOLLOW_106 = new BitSet(new long[]{0x34A0024010208060L,0x000002C9C0018825L});
    public static final BitSet FOLLOW_107 = new BitSet(new long[]{0x34A0024010208020L,0x000002C9C0010825L});
    public static final BitSet FOLLOW_108 = new BitSet(new long[]{0x34A0024010208020L,0x000002C9C0010025L});
    public static final BitSet FOLLOW_109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_110 = new BitSet(new long[]{0x0000002000004000L,0x0000000004000008L});
    public static final BitSet FOLLOW_111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_112 = new BitSet(new long[]{0x0000020000004000L});
    public static final BitSet FOLLOW_113 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_114 = new BitSet(new long[]{0x0000000080000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_115 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000007000000000L});
    public static final BitSet FOLLOW_116 = new BitSet(new long[]{0x0000000000008000L,0x0000000000000200L});
    public static final BitSet FOLLOW_117 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_118 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_119 = new BitSet(new long[]{0x0000000000004000L,0x0000000018001000L});
    public static final BitSet FOLLOW_120 = new BitSet(new long[]{0x0000000000004000L,0x0000000010001000L});
    public static final BitSet FOLLOW_121 = new BitSet(new long[]{0x0000000000004000L,0x0000000000001000L});
    public static final BitSet FOLLOW_122 = new BitSet(new long[]{0x0000000080004000L});
    public static final BitSet FOLLOW_123 = new BitSet(new long[]{0x3480000000208060L,0x00000000C000A025L});
    public static final BitSet FOLLOW_124 = new BitSet(new long[]{0x3480000000208020L,0x00000000C0000025L});
    public static final BitSet FOLLOW_125 = new BitSet(new long[]{0x0000000000008020L,0x0000000040000001L});
    public static final BitSet FOLLOW_126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_127 = new BitSet(new long[]{0x0000000000000040L,0x0000000000008000L});
    public static final BitSet FOLLOW_128 = new BitSet(new long[]{0x0000000000000002L,0x0000000001000000L});
    public static final BitSet FOLLOW_129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_130 = new BitSet(new long[]{0x0000000000004000L,0x0000000018061000L});
    public static final BitSet FOLLOW_131 = new BitSet(new long[]{0x0000000000004000L,0x0000000018041000L});
    public static final BitSet FOLLOW_132 = new BitSet(new long[]{0x0000000000004000L,0x0000000010041000L});
    public static final BitSet FOLLOW_133 = new BitSet(new long[]{0x0000000000004000L,0x0000000000041000L});
    public static final BitSet FOLLOW_134 = new BitSet(new long[]{0x0000000080004000L,0x0000000000040000L});
    public static final BitSet FOLLOW_135 = new BitSet(new long[]{0x3480000000308060L,0x00000000C0F8E025L});
    public static final BitSet FOLLOW_136 = new BitSet(new long[]{0x3480000000308060L,0x00000000C000E025L});
    public static final BitSet FOLLOW_137 = new BitSet(new long[]{0x3480000000208060L,0x00000000C000E025L});
    public static final BitSet FOLLOW_138 = new BitSet(new long[]{0x3480000000208020L,0x00000000C0004025L});
    public static final BitSet FOLLOW_139 = new BitSet(new long[]{0x0000000000000002L,0x0000000000F00000L});
    public static final BitSet FOLLOW_140 = new BitSet(new long[]{0x0000000000000002L,0x0000000000E00000L});
    public static final BitSet FOLLOW_141 = new BitSet(new long[]{0x0000000000000002L,0x0000000000C00000L});
    public static final BitSet FOLLOW_142 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_143 = new BitSet(new long[]{0x0010000000000392L,0x00C0000000000000L});
    public static final BitSet FOLLOW_144 = new BitSet(new long[]{0x0000000000000040L,0x0000000001000000L});
    public static final BitSet FOLLOW_145 = new BitSet(new long[]{0x0000000080000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_146 = new BitSet(new long[]{0x0000002000004002L,0x0000000004000000L});
    public static final BitSet FOLLOW_147 = new BitSet(new long[]{0x0000002000004002L});
    public static final BitSet FOLLOW_148 = new BitSet(new long[]{0x0000000000004002L,0x0000000018000000L});
    public static final BitSet FOLLOW_149 = new BitSet(new long[]{0x0000000000004002L,0x0000000010000000L});
    public static final BitSet FOLLOW_150 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_151 = new BitSet(new long[]{0x0000000000108020L,0x0000000000F80000L});
    public static final BitSet FOLLOW_152 = new BitSet(new long[]{0x0000000000108020L});
    public static final BitSet FOLLOW_153 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_154 = new BitSet(new long[]{0x00100000000003D0L,0x00C0000020000000L});
    public static final BitSet FOLLOW_155 = new BitSet(new long[]{0x0002000000000040L});
    public static final BitSet FOLLOW_156 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_157 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_158 = new BitSet(new long[]{0x0000000000004000L,0x0000000600000000L});
    public static final BitSet FOLLOW_159 = new BitSet(new long[]{0x0000000000004000L,0x0000000400000000L});
    public static final BitSet FOLLOW_160 = new BitSet(new long[]{0x0000040000108060L,0x0000000000F88000L});
    public static final BitSet FOLLOW_161 = new BitSet(new long[]{0x0000040000108060L,0x0000000000008000L});
    public static final BitSet FOLLOW_162 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_163 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
    public static final BitSet FOLLOW_164 = new BitSet(new long[]{0x0000000080000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_165 = new BitSet(new long[]{0x0000000000000000L,0x0000004000000000L});
    public static final BitSet FOLLOW_166 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_167 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_168 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_169 = new BitSet(new long[]{0x0000000000004000L,0x0000000000040000L});
    public static final BitSet FOLLOW_170 = new BitSet(new long[]{0x0000000000108020L,0x0000010000F90000L});
    public static final BitSet FOLLOW_171 = new BitSet(new long[]{0x0000000000108020L,0x0000010000010000L});
    public static final BitSet FOLLOW_172 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
    public static final BitSet FOLLOW_173 = new BitSet(new long[]{0x0000000000008020L,0x00001D0000000000L});
    public static final BitSet FOLLOW_174 = new BitSet(new long[]{0x0000000000008020L,0x00001C0000000000L});
    public static final BitSet FOLLOW_175 = new BitSet(new long[]{0x0000000000008020L,0x0000180000000000L});
    public static final BitSet FOLLOW_176 = new BitSet(new long[]{0x0000000000000060L});
    public static final BitSet FOLLOW_177 = new BitSet(new long[]{0x0000000000008060L});
    public static final BitSet FOLLOW_178 = new BitSet(new long[]{0x0000000000008020L,0x0000100000000000L});
    public static final BitSet FOLLOW_179 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_180 = new BitSet(new long[]{0x0000000000004000L,0x0000200000000000L});
    public static final BitSet FOLLOW_181 = new BitSet(new long[]{0x0000000000008000L,0x0007C00000000000L});
    public static final BitSet FOLLOW_182 = new BitSet(new long[]{0x0000000000008000L,0x0007800000000000L});
    public static final BitSet FOLLOW_183 = new BitSet(new long[]{0x0000000080008000L,0x0007000000000000L});
    public static final BitSet FOLLOW_184 = new BitSet(new long[]{0x0000000000008000L,0x0006000000000000L});
    public static final BitSet FOLLOW_185 = new BitSet(new long[]{0x0000000000008000L,0x0004000000000000L});
    public static final BitSet FOLLOW_186 = new BitSet(new long[]{0x0000000000008010L});
    public static final BitSet FOLLOW_187 = new BitSet(new long[]{0x0000000080008000L});
    public static final BitSet FOLLOW_188 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_189 = new BitSet(new long[]{0x0010000000004390L,0x00F0000000000000L});
    public static final BitSet FOLLOW_190 = new BitSet(new long[]{0x0000000080000000L,0x0020000000000000L});
    public static final BitSet FOLLOW_191 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_192 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_193 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
    public static final BitSet FOLLOW_194 = new BitSet(new long[]{0x0000000000000280L});

}