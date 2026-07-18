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

@SuppressWarnings("all")
public class InternalCqrsDslParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_DOC", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_HEX", "RULE_DECIMAL", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "'project'", "'{'", "'}'", "'context'", "'namespace'", "'import'", "'hint'", "'type'", "'element'", "'generics'", "'acceptable'", "'detection'", "'resolution'", "'consistency'", "'data-protection'", "'protection'", "'category'", "','", "'subject'", "'purpose'", "'lawful-basis'", "'retention'", "'then'", "'protected-by'", "'constraint'", "'input'", "'|'", "'exception'", "'message'", "'business-rule'", "'annotation'", "'cid'", "'value-object'", "'base'", "'entity-id'", "'identifies'", "'aggregate-id'", "'enum'", "'instances'", "'deprecated'", "'('", "')'", "'event'", "'copies-attributes-of'", "'entity'", "'identifier'", "'root'", "'aggregate'", "'constructor'", "'fires'", "'returns'", "'optional'", "'method'", "'ref'", "'slabel'", "'label'", "'tooltip'", "'prompt'", "'examples'", "'<'", "'>'", "'invariants'", "'preconditions'", "'business-rules'", "'@'", "'service'", "'command'", "'target'", "'sla'", "'command-handler'", "'handles'", "'uses'", "'projection'", "'view'", "'rest-path'", "'cron-schedule'", "'process-manager'", "'correlation-id'", "'process-states'", "'reacts-to'", "'in-state'", "'correlate-by'", "'issues-commands'", "'transition-to'", "'arm-timeout'", "'cancel-timeout'", "':'", "'['", "']'", "'true'", "'false'", "'null'", "'.'", "'*'", "'millis'", "'seconds'", "'minutes'", "'hours'", "'days'", "'weeks'", "'months'", "'years'", "'weak'", "'strong'", "'never'", "'manually'", "'automatic'", "'workflow'", "'none'", "'personal'", "'sensitive'", "'consent'", "'explicit_consent'", "'contract'", "'legal_obligation'", "'vital_interests'", "'public_task'", "'legitimate_interests'", "'health'", "'genetic'", "'biometric'", "'racial'", "'political'", "'religious'", "'philosophical'", "'trade_union'", "'sex_life'", "'sexual_orientation'", "'delete'", "'anonymize'", "'pseudonymize'", "'archive'", "'review'"
    };
    public static final int T__144=144;
    public static final int T__143=143;
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
    public static final int RULE_ID=5;
    public static final int T__131=131;
    public static final int T__130=130;
    public static final int RULE_INT=6;
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
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_DOC=4;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;
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
    public static final int RULE_STRING=7;
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
             newCompositeNode(grammarAccess.getDomainModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDomainModel=ruleDomainModel();

            state._fsp--;

             current =iv_ruleDomainModel; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:72:1: ruleDomainModel returns [EObject current=null] : ( (lv_projects_0_0= ruleProject ) )* ;
    public final EObject ruleDomainModel() throws RecognitionException {
        EObject current = null;

        EObject lv_projects_0_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:78:2: ( ( (lv_projects_0_0= ruleProject ) )* )
            // InternalCqrsDsl.g:79:2: ( (lv_projects_0_0= ruleProject ) )*
            {
            // InternalCqrsDsl.g:79:2: ( (lv_projects_0_0= ruleProject ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==13) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCqrsDsl.g:80:3: (lv_projects_0_0= ruleProject )
            	    {
            	    // InternalCqrsDsl.g:80:3: (lv_projects_0_0= ruleProject )
            	    // InternalCqrsDsl.g:81:4: lv_projects_0_0= ruleProject
            	    {

            	    				newCompositeNode(grammarAccess.getDomainModelAccess().getProjectsProjectParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_projects_0_0=ruleProject();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getDomainModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"projects",
            	    					lv_projects_0_0,
            	    					"org.fuin.dsl.cqrs.CqrsDsl.Project");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            	leaveRule();

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


    // $ANTLR start "entryRuleProject"
    // InternalCqrsDsl.g:101:1: entryRuleProject returns [EObject current=null] : iv_ruleProject= ruleProject EOF ;
    public final EObject entryRuleProject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProject = null;


        try {
            // InternalCqrsDsl.g:101:48: (iv_ruleProject= ruleProject EOF )
            // InternalCqrsDsl.g:102:2: iv_ruleProject= ruleProject EOF
            {
             newCompositeNode(grammarAccess.getProjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleProject=ruleProject();

            state._fsp--;

             current =iv_ruleProject; 
            match(input,EOF,FOLLOW_2); 

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
    // $ANTLR end "entryRuleProject"


    // $ANTLR start "ruleProject"
    // InternalCqrsDsl.g:108:1: ruleProject returns [EObject current=null] : (otherlv_0= 'project' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_hints_3_0= ruleHint ) )* ( (lv_contexts_4_0= ruleContext ) )* otherlv_5= '}' ) ;
    public final EObject ruleProject() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_5=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_hints_3_0 = null;

        EObject lv_contexts_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:114:2: ( (otherlv_0= 'project' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_hints_3_0= ruleHint ) )* ( (lv_contexts_4_0= ruleContext ) )* otherlv_5= '}' ) )
            // InternalCqrsDsl.g:115:2: (otherlv_0= 'project' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_hints_3_0= ruleHint ) )* ( (lv_contexts_4_0= ruleContext ) )* otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:115:2: (otherlv_0= 'project' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_hints_3_0= ruleHint ) )* ( (lv_contexts_4_0= ruleContext ) )* otherlv_5= '}' )
            // InternalCqrsDsl.g:116:3: otherlv_0= 'project' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_hints_3_0= ruleHint ) )* ( (lv_contexts_4_0= ruleContext ) )* otherlv_5= '}'
            {
            otherlv_0=(Token)match(input,13,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getProjectAccess().getProjectKeyword_0());
            		
            // InternalCqrsDsl.g:120:3: ( (lv_name_1_0= ruleFQN ) )
            // InternalCqrsDsl.g:121:4: (lv_name_1_0= ruleFQN )
            {
            // InternalCqrsDsl.g:121:4: (lv_name_1_0= ruleFQN )
            // InternalCqrsDsl.g:122:5: lv_name_1_0= ruleFQN
            {

            					newCompositeNode(grammarAccess.getProjectAccess().getNameFQNParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_5);
            lv_name_1_0=ruleFQN();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getProjectRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.fuin.dsl.cqrs.CqrsDsl.FQN");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_6); 

            			newLeafNode(otherlv_2, grammarAccess.getProjectAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalCqrsDsl.g:143:3: ( (lv_hints_3_0= ruleHint ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==RULE_DOC||LA2_0==19) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalCqrsDsl.g:144:4: (lv_hints_3_0= ruleHint )
            	    {
            	    // InternalCqrsDsl.g:144:4: (lv_hints_3_0= ruleHint )
            	    // InternalCqrsDsl.g:145:5: lv_hints_3_0= ruleHint
            	    {

            	    					newCompositeNode(grammarAccess.getProjectAccess().getHintsHintParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_6);
            	    lv_hints_3_0=ruleHint();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getProjectRule());
            	    					}
            	    					add(
            	    						current,
            	    						"hints",
            	    						lv_hints_3_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Hint");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            // InternalCqrsDsl.g:162:3: ( (lv_contexts_4_0= ruleContext ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==16) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCqrsDsl.g:163:4: (lv_contexts_4_0= ruleContext )
            	    {
            	    // InternalCqrsDsl.g:163:4: (lv_contexts_4_0= ruleContext )
            	    // InternalCqrsDsl.g:164:5: lv_contexts_4_0= ruleContext
            	    {

            	    					newCompositeNode(grammarAccess.getProjectAccess().getContextsContextParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_7);
            	    lv_contexts_4_0=ruleContext();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getProjectRule());
            	    					}
            	    					add(
            	    						current,
            	    						"contexts",
            	    						lv_contexts_4_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Context");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            otherlv_5=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getProjectAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleProject"


    // $ANTLR start "entryRuleContext"
    // InternalCqrsDsl.g:189:1: entryRuleContext returns [EObject current=null] : iv_ruleContext= ruleContext EOF ;
    public final EObject entryRuleContext() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContext = null;


        try {
            // InternalCqrsDsl.g:189:48: (iv_ruleContext= ruleContext EOF )
            // InternalCqrsDsl.g:190:2: iv_ruleContext= ruleContext EOF
            {
             newCompositeNode(grammarAccess.getContextRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleContext=ruleContext();

            state._fsp--;

             current =iv_ruleContext; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:196:1: ruleContext returns [EObject current=null] : (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( ( (lv_namespaces_3_0= ruleNamespace ) )* | ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* ) ) otherlv_6= '}' ) ;
    public final EObject ruleContext() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_6=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_namespaces_3_0 = null;

        EObject lv_imports_4_0 = null;

        EObject lv_elements_5_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:202:2: ( (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( ( (lv_namespaces_3_0= ruleNamespace ) )* | ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* ) ) otherlv_6= '}' ) )
            // InternalCqrsDsl.g:203:2: (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( ( (lv_namespaces_3_0= ruleNamespace ) )* | ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* ) ) otherlv_6= '}' )
            {
            // InternalCqrsDsl.g:203:2: (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( ( (lv_namespaces_3_0= ruleNamespace ) )* | ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* ) ) otherlv_6= '}' )
            // InternalCqrsDsl.g:204:3: otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( ( (lv_namespaces_3_0= ruleNamespace ) )* | ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* ) ) otherlv_6= '}'
            {
            otherlv_0=(Token)match(input,16,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getContextAccess().getContextKeyword_0());
            		
            // InternalCqrsDsl.g:208:3: ( (lv_name_1_0= ruleFQN ) )
            // InternalCqrsDsl.g:209:4: (lv_name_1_0= ruleFQN )
            {
            // InternalCqrsDsl.g:209:4: (lv_name_1_0= ruleFQN )
            // InternalCqrsDsl.g:210:5: lv_name_1_0= ruleFQN
            {

            					newCompositeNode(grammarAccess.getContextAccess().getNameFQNParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_5);
            lv_name_1_0=ruleFQN();

            state._fsp--;


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

            otherlv_2=(Token)match(input,14,FOLLOW_8); 

            			newLeafNode(otherlv_2, grammarAccess.getContextAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalCqrsDsl.g:231:3: ( ( (lv_namespaces_3_0= ruleNamespace ) )* | ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* ) )
            int alt7=2;
            switch ( input.LA(1) ) {
            case 17:
                {
                alt7=1;
                }
                break;
            case 15:
                {
                alt7=1;
                }
                break;
            case RULE_DOC:
            case 18:
            case 20:
            case 27:
            case 37:
            case 40:
            case 43:
            case 45:
            case 47:
            case 49:
            case 50:
            case 55:
            case 57:
            case 60:
            case 77:
            case 78:
            case 79:
            case 82:
            case 85:
            case 86:
            case 89:
                {
                alt7=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // InternalCqrsDsl.g:232:4: ( (lv_namespaces_3_0= ruleNamespace ) )*
                    {
                    // InternalCqrsDsl.g:232:4: ( (lv_namespaces_3_0= ruleNamespace ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==17) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:233:5: (lv_namespaces_3_0= ruleNamespace )
                    	    {
                    	    // InternalCqrsDsl.g:233:5: (lv_namespaces_3_0= ruleNamespace )
                    	    // InternalCqrsDsl.g:234:6: lv_namespaces_3_0= ruleNamespace
                    	    {

                    	    						newCompositeNode(grammarAccess.getContextAccess().getNamespacesNamespaceParserRuleCall_3_0_0());
                    	    					
                    	    pushFollow(FOLLOW_9);
                    	    lv_namespaces_3_0=ruleNamespace();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getContextRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"namespaces",
                    	    							lv_namespaces_3_0,
                    	    							"org.fuin.dsl.cqrs.CqrsDsl.Namespace");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:252:4: ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* )
                    {
                    // InternalCqrsDsl.g:252:4: ( ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )* )
                    // InternalCqrsDsl.g:253:5: ( (lv_imports_4_0= ruleImport ) )* ( (lv_elements_5_0= ruleAbstractElement ) )*
                    {
                    // InternalCqrsDsl.g:253:5: ( (lv_imports_4_0= ruleImport ) )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==18) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:254:6: (lv_imports_4_0= ruleImport )
                    	    {
                    	    // InternalCqrsDsl.g:254:6: (lv_imports_4_0= ruleImport )
                    	    // InternalCqrsDsl.g:255:7: lv_imports_4_0= ruleImport
                    	    {

                    	    							newCompositeNode(grammarAccess.getContextAccess().getImportsImportParserRuleCall_3_1_0_0());
                    	    						
                    	    pushFollow(FOLLOW_10);
                    	    lv_imports_4_0=ruleImport();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    // InternalCqrsDsl.g:272:5: ( (lv_elements_5_0= ruleAbstractElement ) )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==RULE_DOC||LA6_0==20||LA6_0==27||LA6_0==37||LA6_0==40||LA6_0==43||LA6_0==45||LA6_0==47||(LA6_0>=49 && LA6_0<=50)||LA6_0==55||LA6_0==57||LA6_0==60||(LA6_0>=77 && LA6_0<=79)||LA6_0==82||(LA6_0>=85 && LA6_0<=86)||LA6_0==89) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:273:6: (lv_elements_5_0= ruleAbstractElement )
                    	    {
                    	    // InternalCqrsDsl.g:273:6: (lv_elements_5_0= ruleAbstractElement )
                    	    // InternalCqrsDsl.g:274:7: lv_elements_5_0= ruleAbstractElement
                    	    {

                    	    							newCompositeNode(grammarAccess.getContextAccess().getElementsAbstractElementParserRuleCall_3_1_1_0());
                    	    						
                    	    pushFollow(FOLLOW_11);
                    	    lv_elements_5_0=ruleAbstractElement();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getContextRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"elements",
                    	    								lv_elements_5_0,
                    	    								"org.fuin.dsl.cqrs.CqrsDsl.AbstractElement");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getContextAccess().getRightCurlyBracketKeyword_4());
            		

            }


            }


            	leaveRule();

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


    // $ANTLR start "entryRuleNamespace"
    // InternalCqrsDsl.g:301:1: entryRuleNamespace returns [EObject current=null] : iv_ruleNamespace= ruleNamespace EOF ;
    public final EObject entryRuleNamespace() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNamespace = null;


        try {
            // InternalCqrsDsl.g:301:50: (iv_ruleNamespace= ruleNamespace EOF )
            // InternalCqrsDsl.g:302:2: iv_ruleNamespace= ruleNamespace EOF
            {
             newCompositeNode(grammarAccess.getNamespaceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNamespace=ruleNamespace();

            state._fsp--;

             current =iv_ruleNamespace; 
            match(input,EOF,FOLLOW_2); 

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
    // $ANTLR end "entryRuleNamespace"


    // $ANTLR start "ruleNamespace"
    // InternalCqrsDsl.g:308:1: ruleNamespace returns [EObject current=null] : (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' ) ;
    public final EObject ruleNamespace() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_5=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_imports_3_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:314:2: ( (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' ) )
            // InternalCqrsDsl.g:315:2: (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:315:2: (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' )
            // InternalCqrsDsl.g:316:3: otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}'
            {
            otherlv_0=(Token)match(input,17,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getNamespaceAccess().getNamespaceKeyword_0());
            		
            // InternalCqrsDsl.g:320:3: ( (lv_name_1_0= ruleFQN ) )
            // InternalCqrsDsl.g:321:4: (lv_name_1_0= ruleFQN )
            {
            // InternalCqrsDsl.g:321:4: (lv_name_1_0= ruleFQN )
            // InternalCqrsDsl.g:322:5: lv_name_1_0= ruleFQN
            {

            					newCompositeNode(grammarAccess.getNamespaceAccess().getNameFQNParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_5);
            lv_name_1_0=ruleFQN();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getNamespaceRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.fuin.dsl.cqrs.CqrsDsl.FQN");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,14,FOLLOW_10); 

            			newLeafNode(otherlv_2, grammarAccess.getNamespaceAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalCqrsDsl.g:343:3: ( (lv_imports_3_0= ruleImport ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==18) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalCqrsDsl.g:344:4: (lv_imports_3_0= ruleImport )
            	    {
            	    // InternalCqrsDsl.g:344:4: (lv_imports_3_0= ruleImport )
            	    // InternalCqrsDsl.g:345:5: lv_imports_3_0= ruleImport
            	    {

            	    					newCompositeNode(grammarAccess.getNamespaceAccess().getImportsImportParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_10);
            	    lv_imports_3_0=ruleImport();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getNamespaceRule());
            	    					}
            	    					add(
            	    						current,
            	    						"imports",
            	    						lv_imports_3_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Import");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // InternalCqrsDsl.g:362:3: ( (lv_elements_4_0= ruleAbstractElement ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==RULE_DOC||LA9_0==20||LA9_0==27||LA9_0==37||LA9_0==40||LA9_0==43||LA9_0==45||LA9_0==47||(LA9_0>=49 && LA9_0<=50)||LA9_0==55||LA9_0==57||LA9_0==60||(LA9_0>=77 && LA9_0<=79)||LA9_0==82||(LA9_0>=85 && LA9_0<=86)||LA9_0==89) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCqrsDsl.g:363:4: (lv_elements_4_0= ruleAbstractElement )
            	    {
            	    // InternalCqrsDsl.g:363:4: (lv_elements_4_0= ruleAbstractElement )
            	    // InternalCqrsDsl.g:364:5: lv_elements_4_0= ruleAbstractElement
            	    {

            	    					newCompositeNode(grammarAccess.getNamespaceAccess().getElementsAbstractElementParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_11);
            	    lv_elements_4_0=ruleAbstractElement();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getNamespaceRule());
            	    					}
            	    					add(
            	    						current,
            	    						"elements",
            	    						lv_elements_4_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.AbstractElement");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            otherlv_5=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getNamespaceAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNamespace"


    // $ANTLR start "entryRuleImport"
    // InternalCqrsDsl.g:389:1: entryRuleImport returns [EObject current=null] : iv_ruleImport= ruleImport EOF ;
    public final EObject entryRuleImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImport = null;


        try {
            // InternalCqrsDsl.g:389:47: (iv_ruleImport= ruleImport EOF )
            // InternalCqrsDsl.g:390:2: iv_ruleImport= ruleImport EOF
            {
             newCompositeNode(grammarAccess.getImportRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleImport=ruleImport();

            state._fsp--;

             current =iv_ruleImport; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:396:1: ruleImport returns [EObject current=null] : (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) ) ;
    public final EObject ruleImport() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_importedNamespace_1_1 = null;

        AntlrDatatypeRuleToken lv_importedNamespace_1_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:402:2: ( (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) ) )
            // InternalCqrsDsl.g:403:2: (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) )
            {
            // InternalCqrsDsl.g:403:2: (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) )
            // InternalCqrsDsl.g:404:3: otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) )
            {
            otherlv_0=(Token)match(input,18,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getImportAccess().getImportKeyword_0());
            		
            // InternalCqrsDsl.g:408:3: ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) )
            // InternalCqrsDsl.g:409:4: ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) )
            {
            // InternalCqrsDsl.g:409:4: ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) )
            // InternalCqrsDsl.g:410:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )
            {
            // InternalCqrsDsl.g:410:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )
            int alt10=2;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // InternalCqrsDsl.g:411:6: lv_importedNamespace_1_1= ruleFQN
                    {

                    						newCompositeNode(grammarAccess.getImportAccess().getImportedNamespaceFQNParserRuleCall_1_0_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_importedNamespace_1_1=ruleFQN();

                    state._fsp--;


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
                    break;
                case 2 :
                    // InternalCqrsDsl.g:427:6: lv_importedNamespace_1_2= ruleFQNWithWildcard
                    {

                    						newCompositeNode(grammarAccess.getImportAccess().getImportedNamespaceFQNWithWildcardParserRuleCall_1_0_1());
                    					
                    pushFollow(FOLLOW_2);
                    lv_importedNamespace_1_2=ruleFQNWithWildcard();

                    state._fsp--;


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
                    break;

            }


            }


            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:449:1: entryRuleHint returns [EObject current=null] : iv_ruleHint= ruleHint EOF ;
    public final EObject entryRuleHint() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleHint = null;


        try {
            // InternalCqrsDsl.g:449:45: (iv_ruleHint= ruleHint EOF )
            // InternalCqrsDsl.g:450:2: iv_ruleHint= ruleHint EOF
            {
             newCompositeNode(grammarAccess.getHintRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleHint=ruleHint();

            state._fsp--;

             current =iv_ruleHint; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:456:1: ruleHint returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) ) ;
    public final EObject ruleHint() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_2_0 = null;

        EObject lv_json_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:462:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) ) )
            // InternalCqrsDsl.g:463:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) )
            {
            // InternalCqrsDsl.g:463:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) ) )
            // InternalCqrsDsl.g:464:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'hint' ( (lv_name_2_0= ruleFQN ) ) ( (lv_json_3_0= ruleJSON ) )
            {
            // InternalCqrsDsl.g:464:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==RULE_DOC) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalCqrsDsl.g:465:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:465:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:466:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_12); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getHintAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,19,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getHintAccess().getHintKeyword_1());
            		
            // InternalCqrsDsl.g:486:3: ( (lv_name_2_0= ruleFQN ) )
            // InternalCqrsDsl.g:487:4: (lv_name_2_0= ruleFQN )
            {
            // InternalCqrsDsl.g:487:4: (lv_name_2_0= ruleFQN )
            // InternalCqrsDsl.g:488:5: lv_name_2_0= ruleFQN
            {

            					newCompositeNode(grammarAccess.getHintAccess().getNameFQNParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_13);
            lv_name_2_0=ruleFQN();

            state._fsp--;


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

            // InternalCqrsDsl.g:505:3: ( (lv_json_3_0= ruleJSON ) )
            // InternalCqrsDsl.g:506:4: (lv_json_3_0= ruleJSON )
            {
            // InternalCqrsDsl.g:506:4: (lv_json_3_0= ruleJSON )
            // InternalCqrsDsl.g:507:5: lv_json_3_0= ruleJSON
            {

            					newCompositeNode(grammarAccess.getHintAccess().getJsonJSONParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_json_3_0=ruleJSON();

            state._fsp--;


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


            	leaveRule();

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
    // InternalCqrsDsl.g:528:1: entryRuleAbstractElement returns [EObject current=null] : iv_ruleAbstractElement= ruleAbstractElement EOF ;
    public final EObject entryRuleAbstractElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractElement = null;


        try {
            // InternalCqrsDsl.g:528:56: (iv_ruleAbstractElement= ruleAbstractElement EOF )
            // InternalCqrsDsl.g:529:2: iv_ruleAbstractElement= ruleAbstractElement EOF
            {
             newCompositeNode(grammarAccess.getAbstractElementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAbstractElement=ruleAbstractElement();

            state._fsp--;

             current =iv_ruleAbstractElement; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:535:1: ruleAbstractElement returns [EObject current=null] : (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection ) ;
    public final EObject ruleAbstractElement() throws RecognitionException {
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
            // InternalCqrsDsl.g:541:2: ( (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection ) )
            // InternalCqrsDsl.g:542:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection )
            {
            // InternalCqrsDsl.g:542:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection )
            int alt12=11;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // InternalCqrsDsl.g:543:3: this_Constraint_0= ruleConstraint
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getConstraintParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Constraint_0=ruleConstraint();

                    state._fsp--;


                    			current = this_Constraint_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:552:3: this_Annotation_1= ruleAnnotation
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getAnnotationParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Annotation_1=ruleAnnotation();

                    state._fsp--;


                    			current = this_Annotation_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:561:3: this_Type_2= ruleType
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getTypeParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Type_2=ruleType();

                    state._fsp--;


                    			current = this_Type_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:570:3: this_Exception_3= ruleException
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getExceptionParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_Exception_3=ruleException();

                    state._fsp--;


                    			current = this_Exception_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:579:3: this_Event_4= ruleEvent
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getEventParserRuleCall_4());
                    		
                    pushFollow(FOLLOW_2);
                    this_Event_4=ruleEvent();

                    state._fsp--;


                    			current = this_Event_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:588:3: this_Command_5= ruleCommand
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getCommandParserRuleCall_5());
                    		
                    pushFollow(FOLLOW_2);
                    this_Command_5=ruleCommand();

                    state._fsp--;


                    			current = this_Command_5;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:597:3: this_CommandHandler_6= ruleCommandHandler
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getCommandHandlerParserRuleCall_6());
                    		
                    pushFollow(FOLLOW_2);
                    this_CommandHandler_6=ruleCommandHandler();

                    state._fsp--;


                    			current = this_CommandHandler_6;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:606:3: this_Projection_7= ruleProjection
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getProjectionParserRuleCall_7());
                    		
                    pushFollow(FOLLOW_2);
                    this_Projection_7=ruleProjection();

                    state._fsp--;


                    			current = this_Projection_7;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 9 :
                    // InternalCqrsDsl.g:615:3: this_View_8= ruleView
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getViewParserRuleCall_8());
                    		
                    pushFollow(FOLLOW_2);
                    this_View_8=ruleView();

                    state._fsp--;


                    			current = this_View_8;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 10 :
                    // InternalCqrsDsl.g:624:3: this_ProcessManager_9= ruleProcessManager
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getProcessManagerParserRuleCall_9());
                    		
                    pushFollow(FOLLOW_2);
                    this_ProcessManager_9=ruleProcessManager();

                    state._fsp--;


                    			current = this_ProcessManager_9;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 11 :
                    // InternalCqrsDsl.g:633:3: this_DataProtection_10= ruleDataProtection
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getDataProtectionParserRuleCall_10());
                    		
                    pushFollow(FOLLOW_2);
                    this_DataProtection_10=ruleDataProtection();

                    state._fsp--;


                    			current = this_DataProtection_10;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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


    // $ANTLR start "entryRuleType"
    // InternalCqrsDsl.g:645:1: entryRuleType returns [EObject current=null] : iv_ruleType= ruleType EOF ;
    public final EObject entryRuleType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleType = null;


        try {
            // InternalCqrsDsl.g:645:45: (iv_ruleType= ruleType EOF )
            // InternalCqrsDsl.g:646:2: iv_ruleType= ruleType EOF
            {
             newCompositeNode(grammarAccess.getTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleType=ruleType();

            state._fsp--;

             current =iv_ruleType; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:652:1: ruleType returns [EObject current=null] : (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService ) ;
    public final EObject ruleType() throws RecognitionException {
        EObject current = null;

        EObject this_ExternalType_0 = null;

        EObject this_InternalType_1 = null;

        EObject this_Service_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:658:2: ( (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService ) )
            // InternalCqrsDsl.g:659:2: (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService )
            {
            // InternalCqrsDsl.g:659:2: (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService )
            int alt13=3;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                switch ( input.LA(2) ) {
                case 45:
                case 47:
                case 49:
                case 50:
                case 57:
                case 60:
                case 77:
                    {
                    alt13=2;
                    }
                    break;
                case 20:
                    {
                    alt13=1;
                    }
                    break;
                case 78:
                    {
                    alt13=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }

                }
                break;
            case 20:
                {
                alt13=1;
                }
                break;
            case 45:
            case 47:
            case 49:
            case 50:
            case 57:
            case 60:
            case 77:
                {
                alt13=2;
                }
                break;
            case 78:
                {
                alt13=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // InternalCqrsDsl.g:660:3: this_ExternalType_0= ruleExternalType
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getExternalTypeParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_ExternalType_0=ruleExternalType();

                    state._fsp--;


                    			current = this_ExternalType_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:669:3: this_InternalType_1= ruleInternalType
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getInternalTypeParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_InternalType_1=ruleInternalType();

                    state._fsp--;


                    			current = this_InternalType_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:678:3: this_Service_2= ruleService
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getServiceParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Service_2=ruleService();

                    state._fsp--;


                    			current = this_Service_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:690:1: entryRuleInternalType returns [EObject current=null] : iv_ruleInternalType= ruleInternalType EOF ;
    public final EObject entryRuleInternalType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInternalType = null;


        try {
            // InternalCqrsDsl.g:690:53: (iv_ruleInternalType= ruleInternalType EOF )
            // InternalCqrsDsl.g:691:2: iv_ruleInternalType= ruleInternalType EOF
            {
             newCompositeNode(grammarAccess.getInternalTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInternalType=ruleInternalType();

            state._fsp--;

             current =iv_ruleInternalType; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:697:1: ruleInternalType returns [EObject current=null] : (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject ) ;
    public final EObject ruleInternalType() throws RecognitionException {
        EObject current = null;

        EObject this_AbstractVO_0 = null;

        EObject this_AbstractEntity_1 = null;

        EObject this_EnumObject_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:703:2: ( (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject ) )
            // InternalCqrsDsl.g:704:2: (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject )
            {
            // InternalCqrsDsl.g:704:2: (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject )
            int alt14=3;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                switch ( input.LA(2) ) {
                case 50:
                    {
                    alt14=3;
                    }
                    break;
                case 45:
                case 47:
                case 49:
                case 77:
                    {
                    alt14=1;
                    }
                    break;
                case 57:
                case 60:
                    {
                    alt14=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 1, input);

                    throw nvae;
                }

                }
                break;
            case 45:
            case 47:
            case 49:
            case 77:
                {
                alt14=1;
                }
                break;
            case 57:
            case 60:
                {
                alt14=2;
                }
                break;
            case 50:
                {
                alt14=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // InternalCqrsDsl.g:705:3: this_AbstractVO_0= ruleAbstractVO
                    {

                    			newCompositeNode(grammarAccess.getInternalTypeAccess().getAbstractVOParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_AbstractVO_0=ruleAbstractVO();

                    state._fsp--;


                    			current = this_AbstractVO_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:714:3: this_AbstractEntity_1= ruleAbstractEntity
                    {

                    			newCompositeNode(grammarAccess.getInternalTypeAccess().getAbstractEntityParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_AbstractEntity_1=ruleAbstractEntity();

                    state._fsp--;


                    			current = this_AbstractEntity_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:723:3: this_EnumObject_2= ruleEnumObject
                    {

                    			newCompositeNode(grammarAccess.getInternalTypeAccess().getEnumObjectParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_EnumObject_2=ruleEnumObject();

                    state._fsp--;


                    			current = this_EnumObject_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:735:1: entryRuleAbstractVO returns [EObject current=null] : iv_ruleAbstractVO= ruleAbstractVO EOF ;
    public final EObject entryRuleAbstractVO() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractVO = null;


        try {
            // InternalCqrsDsl.g:735:51: (iv_ruleAbstractVO= ruleAbstractVO EOF )
            // InternalCqrsDsl.g:736:2: iv_ruleAbstractVO= ruleAbstractVO EOF
            {
             newCompositeNode(grammarAccess.getAbstractVORule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAbstractVO=ruleAbstractVO();

            state._fsp--;

             current =iv_ruleAbstractVO; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:742:1: ruleAbstractVO returns [EObject current=null] : (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId ) ;
    public final EObject ruleAbstractVO() throws RecognitionException {
        EObject current = null;

        EObject this_ValueObject_0 = null;

        EObject this_AbstractEntityId_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:748:2: ( (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId ) )
            // InternalCqrsDsl.g:749:2: (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId )
            {
            // InternalCqrsDsl.g:749:2: (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId )
            int alt15=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA15_1 = input.LA(2);

                if ( (LA15_1==47||LA15_1==49) ) {
                    alt15=2;
                }
                else if ( (LA15_1==45||LA15_1==77) ) {
                    alt15=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 1, input);

                    throw nvae;
                }
                }
                break;
            case 45:
            case 77:
                {
                alt15=1;
                }
                break;
            case 47:
            case 49:
                {
                alt15=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // InternalCqrsDsl.g:750:3: this_ValueObject_0= ruleValueObject
                    {

                    			newCompositeNode(grammarAccess.getAbstractVOAccess().getValueObjectParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_ValueObject_0=ruleValueObject();

                    state._fsp--;


                    			current = this_ValueObject_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:759:3: this_AbstractEntityId_1= ruleAbstractEntityId
                    {

                    			newCompositeNode(grammarAccess.getAbstractVOAccess().getAbstractEntityIdParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_AbstractEntityId_1=ruleAbstractEntityId();

                    state._fsp--;


                    			current = this_AbstractEntityId_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:771:1: entryRuleAbstractEntityId returns [EObject current=null] : iv_ruleAbstractEntityId= ruleAbstractEntityId EOF ;
    public final EObject entryRuleAbstractEntityId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractEntityId = null;


        try {
            // InternalCqrsDsl.g:771:57: (iv_ruleAbstractEntityId= ruleAbstractEntityId EOF )
            // InternalCqrsDsl.g:772:2: iv_ruleAbstractEntityId= ruleAbstractEntityId EOF
            {
             newCompositeNode(grammarAccess.getAbstractEntityIdRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAbstractEntityId=ruleAbstractEntityId();

            state._fsp--;

             current =iv_ruleAbstractEntityId; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:778:1: ruleAbstractEntityId returns [EObject current=null] : (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId ) ;
    public final EObject ruleAbstractEntityId() throws RecognitionException {
        EObject current = null;

        EObject this_EntityId_0 = null;

        EObject this_AggregateId_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:784:2: ( (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId ) )
            // InternalCqrsDsl.g:785:2: (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId )
            {
            // InternalCqrsDsl.g:785:2: (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId )
            int alt16=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==49) ) {
                    alt16=2;
                }
                else if ( (LA16_1==47) ) {
                    alt16=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;
                }
                }
                break;
            case 47:
                {
                alt16=1;
                }
                break;
            case 49:
                {
                alt16=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // InternalCqrsDsl.g:786:3: this_EntityId_0= ruleEntityId
                    {

                    			newCompositeNode(grammarAccess.getAbstractEntityIdAccess().getEntityIdParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_EntityId_0=ruleEntityId();

                    state._fsp--;


                    			current = this_EntityId_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:795:3: this_AggregateId_1= ruleAggregateId
                    {

                    			newCompositeNode(grammarAccess.getAbstractEntityIdAccess().getAggregateIdParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_AggregateId_1=ruleAggregateId();

                    state._fsp--;


                    			current = this_AggregateId_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:807:1: entryRuleAbstractEntity returns [EObject current=null] : iv_ruleAbstractEntity= ruleAbstractEntity EOF ;
    public final EObject entryRuleAbstractEntity() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractEntity = null;


        try {
            // InternalCqrsDsl.g:807:55: (iv_ruleAbstractEntity= ruleAbstractEntity EOF )
            // InternalCqrsDsl.g:808:2: iv_ruleAbstractEntity= ruleAbstractEntity EOF
            {
             newCompositeNode(grammarAccess.getAbstractEntityRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAbstractEntity=ruleAbstractEntity();

            state._fsp--;

             current =iv_ruleAbstractEntity; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:814:1: ruleAbstractEntity returns [EObject current=null] : (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate ) ;
    public final EObject ruleAbstractEntity() throws RecognitionException {
        EObject current = null;

        EObject this_Entity_0 = null;

        EObject this_Aggregate_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:820:2: ( (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate ) )
            // InternalCqrsDsl.g:821:2: (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate )
            {
            // InternalCqrsDsl.g:821:2: (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate )
            int alt17=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==57) ) {
                    alt17=1;
                }
                else if ( (LA17_1==60) ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;
                }
                }
                break;
            case 57:
                {
                alt17=1;
                }
                break;
            case 60:
                {
                alt17=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }

            switch (alt17) {
                case 1 :
                    // InternalCqrsDsl.g:822:3: this_Entity_0= ruleEntity
                    {

                    			newCompositeNode(grammarAccess.getAbstractEntityAccess().getEntityParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Entity_0=ruleEntity();

                    state._fsp--;


                    			current = this_Entity_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:831:3: this_Aggregate_1= ruleAggregate
                    {

                    			newCompositeNode(grammarAccess.getAbstractEntityAccess().getAggregateParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Aggregate_1=ruleAggregate();

                    state._fsp--;


                    			current = this_Aggregate_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:843:1: entryRuleExternalType returns [EObject current=null] : iv_ruleExternalType= ruleExternalType EOF ;
    public final EObject entryRuleExternalType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExternalType = null;


        try {
            // InternalCqrsDsl.g:843:53: (iv_ruleExternalType= ruleExternalType EOF )
            // InternalCqrsDsl.g:844:2: iv_ruleExternalType= ruleExternalType EOF
            {
             newCompositeNode(grammarAccess.getExternalTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExternalType=ruleExternalType();

            state._fsp--;

             current =iv_ruleExternalType; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:850:1: ruleExternalType returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? ) ;
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
            // InternalCqrsDsl.g:856:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? ) )
            // InternalCqrsDsl.g:857:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? )
            {
            // InternalCqrsDsl.g:857:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? )
            // InternalCqrsDsl.g:858:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )?
            {
            // InternalCqrsDsl.g:858:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==RULE_DOC) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // InternalCqrsDsl.g:859:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:859:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:860:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_14); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getExternalTypeAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,20,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getExternalTypeAccess().getTypeKeyword_1());
            		
            // InternalCqrsDsl.g:880:3: ( (lv_element_2_0= 'element' ) )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==21) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCqrsDsl.g:881:4: (lv_element_2_0= 'element' )
                    {
                    // InternalCqrsDsl.g:881:4: (lv_element_2_0= 'element' )
                    // InternalCqrsDsl.g:882:5: lv_element_2_0= 'element'
                    {
                    lv_element_2_0=(Token)match(input,21,FOLLOW_4); 

                    					newLeafNode(lv_element_2_0, grammarAccess.getExternalTypeAccess().getElementElementKeyword_2_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getExternalTypeRule());
                    					}
                    					setWithLastConsumed(current, "element", lv_element_2_0, "element");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:894:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:895:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:895:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:896:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_16); 

            					newLeafNode(lv_name_3_0, grammarAccess.getExternalTypeAccess().getNameIDTerminalRuleCall_3_0());
            				

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

            // InternalCqrsDsl.g:912:3: (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==22) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCqrsDsl.g:913:4: otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) )
                    {
                    otherlv_4=(Token)match(input,22,FOLLOW_17); 

                    				newLeafNode(otherlv_4, grammarAccess.getExternalTypeAccess().getGenericsKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:917:4: ( (lv_generics_5_0= RULE_INT ) )
                    // InternalCqrsDsl.g:918:5: (lv_generics_5_0= RULE_INT )
                    {
                    // InternalCqrsDsl.g:918:5: (lv_generics_5_0= RULE_INT )
                    // InternalCqrsDsl.g:919:6: lv_generics_5_0= RULE_INT
                    {
                    lv_generics_5_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_generics_5_0, grammarAccess.getExternalTypeAccess().getGenericsINTTerminalRuleCall_4_1_0());
                    					

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
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:940:1: entryRuleDuration returns [EObject current=null] : iv_ruleDuration= ruleDuration EOF ;
    public final EObject entryRuleDuration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDuration = null;


        try {
            // InternalCqrsDsl.g:940:49: (iv_ruleDuration= ruleDuration EOF )
            // InternalCqrsDsl.g:941:2: iv_ruleDuration= ruleDuration EOF
            {
             newCompositeNode(grammarAccess.getDurationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDuration=ruleDuration();

            state._fsp--;

             current =iv_ruleDuration; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:947:1: ruleDuration returns [EObject current=null] : ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) ) ;
    public final EObject ruleDuration() throws RecognitionException {
        EObject current = null;

        Token lv_time_0_0=null;
        Enumerator lv_unit_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:953:2: ( ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) ) )
            // InternalCqrsDsl.g:954:2: ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) )
            {
            // InternalCqrsDsl.g:954:2: ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) )
            // InternalCqrsDsl.g:955:3: ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) )
            {
            // InternalCqrsDsl.g:955:3: ( (lv_time_0_0= RULE_INT ) )
            // InternalCqrsDsl.g:956:4: (lv_time_0_0= RULE_INT )
            {
            // InternalCqrsDsl.g:956:4: (lv_time_0_0= RULE_INT )
            // InternalCqrsDsl.g:957:5: lv_time_0_0= RULE_INT
            {
            lv_time_0_0=(Token)match(input,RULE_INT,FOLLOW_18); 

            					newLeafNode(lv_time_0_0, grammarAccess.getDurationAccess().getTimeINTTerminalRuleCall_0_0());
            				

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

            // InternalCqrsDsl.g:973:3: ( (lv_unit_1_0= ruleTimeUnit ) )
            // InternalCqrsDsl.g:974:4: (lv_unit_1_0= ruleTimeUnit )
            {
            // InternalCqrsDsl.g:974:4: (lv_unit_1_0= ruleTimeUnit )
            // InternalCqrsDsl.g:975:5: lv_unit_1_0= ruleTimeUnit
            {

            					newCompositeNode(grammarAccess.getDurationAccess().getUnitTimeUnitEnumRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_unit_1_0=ruleTimeUnit();

            state._fsp--;


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


            	leaveRule();

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
    // InternalCqrsDsl.g:996:1: entryRuleWeakConsistency returns [EObject current=null] : iv_ruleWeakConsistency= ruleWeakConsistency EOF ;
    public final EObject entryRuleWeakConsistency() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWeakConsistency = null;


        try {
            // InternalCqrsDsl.g:996:56: (iv_ruleWeakConsistency= ruleWeakConsistency EOF )
            // InternalCqrsDsl.g:997:2: iv_ruleWeakConsistency= ruleWeakConsistency EOF
            {
             newCompositeNode(grammarAccess.getWeakConsistencyRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleWeakConsistency=ruleWeakConsistency();

            state._fsp--;

             current =iv_ruleWeakConsistency; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:1003:1: ruleWeakConsistency returns [EObject current=null] : ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) ) ;
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
            // InternalCqrsDsl.g:1009:2: ( ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) ) )
            // InternalCqrsDsl.g:1010:2: ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) )
            {
            // InternalCqrsDsl.g:1010:2: ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) )
            // InternalCqrsDsl.g:1011:3: ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) )
            {
            // InternalCqrsDsl.g:1011:3: ( (lv_acceptableDoc_0_0= RULE_DOC ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==RULE_DOC) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCqrsDsl.g:1012:4: (lv_acceptableDoc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1012:4: (lv_acceptableDoc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1013:5: lv_acceptableDoc_0_0= RULE_DOC
                    {
                    lv_acceptableDoc_0_0=(Token)match(input,RULE_DOC,FOLLOW_19); 

                    					newLeafNode(lv_acceptableDoc_0_0, grammarAccess.getWeakConsistencyAccess().getAcceptableDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,23,FOLLOW_17); 

            			newLeafNode(otherlv_1, grammarAccess.getWeakConsistencyAccess().getAcceptableKeyword_1());
            		
            // InternalCqrsDsl.g:1033:3: ( (lv_acceptable_2_0= ruleDuration ) )
            // InternalCqrsDsl.g:1034:4: (lv_acceptable_2_0= ruleDuration )
            {
            // InternalCqrsDsl.g:1034:4: (lv_acceptable_2_0= ruleDuration )
            // InternalCqrsDsl.g:1035:5: lv_acceptable_2_0= ruleDuration
            {

            					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getAcceptableDurationParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_20);
            lv_acceptable_2_0=ruleDuration();

            state._fsp--;


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

            // InternalCqrsDsl.g:1052:3: ( (lv_detectionDoc_3_0= RULE_DOC ) )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==RULE_DOC) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // InternalCqrsDsl.g:1053:4: (lv_detectionDoc_3_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1053:4: (lv_detectionDoc_3_0= RULE_DOC )
                    // InternalCqrsDsl.g:1054:5: lv_detectionDoc_3_0= RULE_DOC
                    {
                    lv_detectionDoc_3_0=(Token)match(input,RULE_DOC,FOLLOW_21); 

                    					newLeafNode(lv_detectionDoc_3_0, grammarAccess.getWeakConsistencyAccess().getDetectionDocDOCTerminalRuleCall_3_0());
                    				

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
                    break;

            }

            otherlv_4=(Token)match(input,24,FOLLOW_22); 

            			newLeafNode(otherlv_4, grammarAccess.getWeakConsistencyAccess().getDetectionKeyword_4());
            		
            // InternalCqrsDsl.g:1074:3: ( (lv_detection_5_0= ruleInconsistencyDetection ) )
            // InternalCqrsDsl.g:1075:4: (lv_detection_5_0= ruleInconsistencyDetection )
            {
            // InternalCqrsDsl.g:1075:4: (lv_detection_5_0= ruleInconsistencyDetection )
            // InternalCqrsDsl.g:1076:5: lv_detection_5_0= ruleInconsistencyDetection
            {

            					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getDetectionInconsistencyDetectionEnumRuleCall_5_0());
            				
            pushFollow(FOLLOW_23);
            lv_detection_5_0=ruleInconsistencyDetection();

            state._fsp--;


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

            // InternalCqrsDsl.g:1093:3: ( (lv_resolutionDoc_6_0= RULE_DOC ) )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==RULE_DOC) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // InternalCqrsDsl.g:1094:4: (lv_resolutionDoc_6_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1094:4: (lv_resolutionDoc_6_0= RULE_DOC )
                    // InternalCqrsDsl.g:1095:5: lv_resolutionDoc_6_0= RULE_DOC
                    {
                    lv_resolutionDoc_6_0=(Token)match(input,RULE_DOC,FOLLOW_24); 

                    					newLeafNode(lv_resolutionDoc_6_0, grammarAccess.getWeakConsistencyAccess().getResolutionDocDOCTerminalRuleCall_6_0());
                    				

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
                    break;

            }

            otherlv_7=(Token)match(input,25,FOLLOW_25); 

            			newLeafNode(otherlv_7, grammarAccess.getWeakConsistencyAccess().getResolutionKeyword_7());
            		
            // InternalCqrsDsl.g:1115:3: ( (lv_resolution_8_0= ruleInconsistencyResolution ) )
            // InternalCqrsDsl.g:1116:4: (lv_resolution_8_0= ruleInconsistencyResolution )
            {
            // InternalCqrsDsl.g:1116:4: (lv_resolution_8_0= ruleInconsistencyResolution )
            // InternalCqrsDsl.g:1117:5: lv_resolution_8_0= ruleInconsistencyResolution
            {

            					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getResolutionInconsistencyResolutionEnumRuleCall_8_0());
            				
            pushFollow(FOLLOW_2);
            lv_resolution_8_0=ruleInconsistencyResolution();

            state._fsp--;


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


            	leaveRule();

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
    // InternalCqrsDsl.g:1138:1: entryRuleConsistency returns [EObject current=null] : iv_ruleConsistency= ruleConsistency EOF ;
    public final EObject entryRuleConsistency() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConsistency = null;


        try {
            // InternalCqrsDsl.g:1138:52: (iv_ruleConsistency= ruleConsistency EOF )
            // InternalCqrsDsl.g:1139:2: iv_ruleConsistency= ruleConsistency EOF
            {
             newCompositeNode(grammarAccess.getConsistencyRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConsistency=ruleConsistency();

            state._fsp--;

             current =iv_ruleConsistency; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:1145:1: ruleConsistency returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? ) ;
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
            // InternalCqrsDsl.g:1151:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? ) )
            // InternalCqrsDsl.g:1152:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? )
            {
            // InternalCqrsDsl.g:1152:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? )
            // InternalCqrsDsl.g:1153:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )?
            {
            // InternalCqrsDsl.g:1153:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==RULE_DOC) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCqrsDsl.g:1154:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1154:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1155:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_26); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getConsistencyAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,26,FOLLOW_27); 

            			newLeafNode(otherlv_1, grammarAccess.getConsistencyAccess().getConsistencyKeyword_1());
            		
            // InternalCqrsDsl.g:1175:3: ( (lv_level_2_0= ruleConsistencyLevel ) )
            // InternalCqrsDsl.g:1176:4: (lv_level_2_0= ruleConsistencyLevel )
            {
            // InternalCqrsDsl.g:1176:4: (lv_level_2_0= ruleConsistencyLevel )
            // InternalCqrsDsl.g:1177:5: lv_level_2_0= ruleConsistencyLevel
            {

            					newCompositeNode(grammarAccess.getConsistencyAccess().getLevelConsistencyLevelEnumRuleCall_2_0());
            				
            pushFollow(FOLLOW_28);
            lv_level_2_0=ruleConsistencyLevel();

            state._fsp--;


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

            // InternalCqrsDsl.g:1194:3: (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==14) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalCqrsDsl.g:1195:4: otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_29); 

                    				newLeafNode(otherlv_3, grammarAccess.getConsistencyAccess().getLeftCurlyBracketKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:1199:4: ( (lv_weakConsistency_4_0= ruleWeakConsistency ) )
                    // InternalCqrsDsl.g:1200:5: (lv_weakConsistency_4_0= ruleWeakConsistency )
                    {
                    // InternalCqrsDsl.g:1200:5: (lv_weakConsistency_4_0= ruleWeakConsistency )
                    // InternalCqrsDsl.g:1201:6: lv_weakConsistency_4_0= ruleWeakConsistency
                    {

                    						newCompositeNode(grammarAccess.getConsistencyAccess().getWeakConsistencyWeakConsistencyParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_30);
                    lv_weakConsistency_4_0=ruleWeakConsistency();

                    state._fsp--;


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

                    otherlv_5=(Token)match(input,15,FOLLOW_2); 

                    				newLeafNode(otherlv_5, grammarAccess.getConsistencyAccess().getRightCurlyBracketKeyword_3_2());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:1227:1: entryRuleDataProtection returns [EObject current=null] : iv_ruleDataProtection= ruleDataProtection EOF ;
    public final EObject entryRuleDataProtection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataProtection = null;


        try {
            // InternalCqrsDsl.g:1227:55: (iv_ruleDataProtection= ruleDataProtection EOF )
            // InternalCqrsDsl.g:1228:2: iv_ruleDataProtection= ruleDataProtection EOF
            {
             newCompositeNode(grammarAccess.getDataProtectionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDataProtection=ruleDataProtection();

            state._fsp--;

             current =iv_ruleDataProtection; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:1234:1: ruleDataProtection returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' ) ;
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
            // InternalCqrsDsl.g:1240:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' ) )
            // InternalCqrsDsl.g:1241:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' )
            {
            // InternalCqrsDsl.g:1241:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' )
            // InternalCqrsDsl.g:1242:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}'
            {
            // InternalCqrsDsl.g:1242:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==RULE_DOC) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalCqrsDsl.g:1243:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1243:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1244:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_31); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getDataProtectionAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,27,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getDataProtectionAccess().getDataProtectionKeyword_1());
            		
            // InternalCqrsDsl.g:1264:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1265:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1265:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1266:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_2_0, grammarAccess.getDataProtectionAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            otherlv_3=(Token)match(input,14,FOLLOW_32); 

            			newLeafNode(otherlv_3, grammarAccess.getDataProtectionAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalCqrsDsl.g:1286:3: ( (lv_levelDoc_4_0= RULE_DOC ) )?
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==RULE_DOC) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // InternalCqrsDsl.g:1287:4: (lv_levelDoc_4_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1287:4: (lv_levelDoc_4_0= RULE_DOC )
                    // InternalCqrsDsl.g:1288:5: lv_levelDoc_4_0= RULE_DOC
                    {
                    lv_levelDoc_4_0=(Token)match(input,RULE_DOC,FOLLOW_33); 

                    					newLeafNode(lv_levelDoc_4_0, grammarAccess.getDataProtectionAccess().getLevelDocDOCTerminalRuleCall_4_0());
                    				

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
                    break;

            }

            otherlv_5=(Token)match(input,28,FOLLOW_34); 

            			newLeafNode(otherlv_5, grammarAccess.getDataProtectionAccess().getProtectionKeyword_5());
            		
            // InternalCqrsDsl.g:1308:3: ( (lv_level_6_0= ruleProtectionLevel ) )
            // InternalCqrsDsl.g:1309:4: (lv_level_6_0= ruleProtectionLevel )
            {
            // InternalCqrsDsl.g:1309:4: (lv_level_6_0= ruleProtectionLevel )
            // InternalCqrsDsl.g:1310:5: lv_level_6_0= ruleProtectionLevel
            {

            					newCompositeNode(grammarAccess.getDataProtectionAccess().getLevelProtectionLevelEnumRuleCall_6_0());
            				
            pushFollow(FOLLOW_35);
            lv_level_6_0=ruleProtectionLevel();

            state._fsp--;


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

            // InternalCqrsDsl.g:1327:3: ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==RULE_DOC) ) {
                int LA30_1 = input.LA(2);

                if ( (LA30_1==29) ) {
                    alt30=1;
                }
            }
            else if ( (LA30_0==29) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // InternalCqrsDsl.g:1328:4: ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )*
                    {
                    // InternalCqrsDsl.g:1328:4: ( (lv_categoryDoc_7_0= RULE_DOC ) )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==RULE_DOC) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // InternalCqrsDsl.g:1329:5: (lv_categoryDoc_7_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1329:5: (lv_categoryDoc_7_0= RULE_DOC )
                            // InternalCqrsDsl.g:1330:6: lv_categoryDoc_7_0= RULE_DOC
                            {
                            lv_categoryDoc_7_0=(Token)match(input,RULE_DOC,FOLLOW_36); 

                            						newLeafNode(lv_categoryDoc_7_0, grammarAccess.getDataProtectionAccess().getCategoryDocDOCTerminalRuleCall_7_0_0());
                            					

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
                            break;

                    }

                    otherlv_8=(Token)match(input,29,FOLLOW_37); 

                    				newLeafNode(otherlv_8, grammarAccess.getDataProtectionAccess().getCategoryKeyword_7_1());
                    			
                    // InternalCqrsDsl.g:1350:4: ( (lv_categories_9_0= ruleSpecialCategory ) )
                    // InternalCqrsDsl.g:1351:5: (lv_categories_9_0= ruleSpecialCategory )
                    {
                    // InternalCqrsDsl.g:1351:5: (lv_categories_9_0= ruleSpecialCategory )
                    // InternalCqrsDsl.g:1352:6: lv_categories_9_0= ruleSpecialCategory
                    {

                    						newCompositeNode(grammarAccess.getDataProtectionAccess().getCategoriesSpecialCategoryEnumRuleCall_7_2_0());
                    					
                    pushFollow(FOLLOW_38);
                    lv_categories_9_0=ruleSpecialCategory();

                    state._fsp--;


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

                    // InternalCqrsDsl.g:1369:4: (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )*
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( (LA29_0==30) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:1370:5: otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) )
                    	    {
                    	    otherlv_10=(Token)match(input,30,FOLLOW_37); 

                    	    					newLeafNode(otherlv_10, grammarAccess.getDataProtectionAccess().getCommaKeyword_7_3_0());
                    	    				
                    	    // InternalCqrsDsl.g:1374:5: ( (lv_categories_11_0= ruleSpecialCategory ) )
                    	    // InternalCqrsDsl.g:1375:6: (lv_categories_11_0= ruleSpecialCategory )
                    	    {
                    	    // InternalCqrsDsl.g:1375:6: (lv_categories_11_0= ruleSpecialCategory )
                    	    // InternalCqrsDsl.g:1376:7: lv_categories_11_0= ruleSpecialCategory
                    	    {

                    	    							newCompositeNode(grammarAccess.getDataProtectionAccess().getCategoriesSpecialCategoryEnumRuleCall_7_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_38);
                    	    lv_categories_11_0=ruleSpecialCategory();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop29;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:1395:3: ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==RULE_DOC) ) {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==31) ) {
                    alt32=1;
                }
            }
            else if ( (LA32_0==31) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // InternalCqrsDsl.g:1396:4: ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) )
                    {
                    // InternalCqrsDsl.g:1396:4: ( (lv_subjectDoc_12_0= RULE_DOC ) )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==RULE_DOC) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // InternalCqrsDsl.g:1397:5: (lv_subjectDoc_12_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1397:5: (lv_subjectDoc_12_0= RULE_DOC )
                            // InternalCqrsDsl.g:1398:6: lv_subjectDoc_12_0= RULE_DOC
                            {
                            lv_subjectDoc_12_0=(Token)match(input,RULE_DOC,FOLLOW_39); 

                            						newLeafNode(lv_subjectDoc_12_0, grammarAccess.getDataProtectionAccess().getSubjectDocDOCTerminalRuleCall_8_0_0());
                            					

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
                            break;

                    }

                    otherlv_13=(Token)match(input,31,FOLLOW_40); 

                    				newLeafNode(otherlv_13, grammarAccess.getDataProtectionAccess().getSubjectKeyword_8_1());
                    			
                    // InternalCqrsDsl.g:1418:4: ( (lv_subject_14_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1419:5: (lv_subject_14_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1419:5: (lv_subject_14_0= RULE_STRING )
                    // InternalCqrsDsl.g:1420:6: lv_subject_14_0= RULE_STRING
                    {
                    lv_subject_14_0=(Token)match(input,RULE_STRING,FOLLOW_41); 

                    						newLeafNode(lv_subject_14_0, grammarAccess.getDataProtectionAccess().getSubjectSTRINGTerminalRuleCall_8_2_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:1437:3: ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )?
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
                    // InternalCqrsDsl.g:1438:4: ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) )
                    {
                    // InternalCqrsDsl.g:1438:4: ( (lv_purposeDoc_15_0= RULE_DOC ) )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==RULE_DOC) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // InternalCqrsDsl.g:1439:5: (lv_purposeDoc_15_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1439:5: (lv_purposeDoc_15_0= RULE_DOC )
                            // InternalCqrsDsl.g:1440:6: lv_purposeDoc_15_0= RULE_DOC
                            {
                            lv_purposeDoc_15_0=(Token)match(input,RULE_DOC,FOLLOW_42); 

                            						newLeafNode(lv_purposeDoc_15_0, grammarAccess.getDataProtectionAccess().getPurposeDocDOCTerminalRuleCall_9_0_0());
                            					

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
                            break;

                    }

                    otherlv_16=(Token)match(input,32,FOLLOW_40); 

                    				newLeafNode(otherlv_16, grammarAccess.getDataProtectionAccess().getPurposeKeyword_9_1());
                    			
                    // InternalCqrsDsl.g:1460:4: ( (lv_purpose_17_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1461:5: (lv_purpose_17_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1461:5: (lv_purpose_17_0= RULE_STRING )
                    // InternalCqrsDsl.g:1462:6: lv_purpose_17_0= RULE_STRING
                    {
                    lv_purpose_17_0=(Token)match(input,RULE_STRING,FOLLOW_43); 

                    						newLeafNode(lv_purpose_17_0, grammarAccess.getDataProtectionAccess().getPurposeSTRINGTerminalRuleCall_9_2_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:1479:3: ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )?
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
                    // InternalCqrsDsl.g:1480:4: ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) )
                    {
                    // InternalCqrsDsl.g:1480:4: ( (lv_basisDoc_18_0= RULE_DOC ) )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==RULE_DOC) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // InternalCqrsDsl.g:1481:5: (lv_basisDoc_18_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1481:5: (lv_basisDoc_18_0= RULE_DOC )
                            // InternalCqrsDsl.g:1482:6: lv_basisDoc_18_0= RULE_DOC
                            {
                            lv_basisDoc_18_0=(Token)match(input,RULE_DOC,FOLLOW_44); 

                            						newLeafNode(lv_basisDoc_18_0, grammarAccess.getDataProtectionAccess().getBasisDocDOCTerminalRuleCall_10_0_0());
                            					

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
                            break;

                    }

                    otherlv_19=(Token)match(input,33,FOLLOW_45); 

                    				newLeafNode(otherlv_19, grammarAccess.getDataProtectionAccess().getLawfulBasisKeyword_10_1());
                    			
                    // InternalCqrsDsl.g:1502:4: ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) )
                    // InternalCqrsDsl.g:1503:5: (lv_lawfulBasis_20_0= ruleLawfulBasis )
                    {
                    // InternalCqrsDsl.g:1503:5: (lv_lawfulBasis_20_0= ruleLawfulBasis )
                    // InternalCqrsDsl.g:1504:6: lv_lawfulBasis_20_0= ruleLawfulBasis
                    {

                    						newCompositeNode(grammarAccess.getDataProtectionAccess().getLawfulBasisLawfulBasisEnumRuleCall_10_2_0());
                    					
                    pushFollow(FOLLOW_46);
                    lv_lawfulBasis_20_0=ruleLawfulBasis();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:1522:3: ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==RULE_DOC||LA39_0==34) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // InternalCqrsDsl.g:1523:4: ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )?
                    {
                    // InternalCqrsDsl.g:1523:4: ( (lv_retentionDoc_21_0= RULE_DOC ) )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==RULE_DOC) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // InternalCqrsDsl.g:1524:5: (lv_retentionDoc_21_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1524:5: (lv_retentionDoc_21_0= RULE_DOC )
                            // InternalCqrsDsl.g:1525:6: lv_retentionDoc_21_0= RULE_DOC
                            {
                            lv_retentionDoc_21_0=(Token)match(input,RULE_DOC,FOLLOW_47); 

                            						newLeafNode(lv_retentionDoc_21_0, grammarAccess.getDataProtectionAccess().getRetentionDocDOCTerminalRuleCall_11_0_0());
                            					

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
                            break;

                    }

                    otherlv_22=(Token)match(input,34,FOLLOW_17); 

                    				newLeafNode(otherlv_22, grammarAccess.getDataProtectionAccess().getRetentionKeyword_11_1());
                    			
                    // InternalCqrsDsl.g:1545:4: ( (lv_retention_23_0= ruleDuration ) )
                    // InternalCqrsDsl.g:1546:5: (lv_retention_23_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:1546:5: (lv_retention_23_0= ruleDuration )
                    // InternalCqrsDsl.g:1547:6: lv_retention_23_0= ruleDuration
                    {

                    						newCompositeNode(grammarAccess.getDataProtectionAccess().getRetentionDurationParserRuleCall_11_2_0());
                    					
                    pushFollow(FOLLOW_48);
                    lv_retention_23_0=ruleDuration();

                    state._fsp--;


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

                    // InternalCqrsDsl.g:1564:4: (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )?
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( (LA38_0==35) ) {
                        alt38=1;
                    }
                    switch (alt38) {
                        case 1 :
                            // InternalCqrsDsl.g:1565:5: otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) )
                            {
                            otherlv_24=(Token)match(input,35,FOLLOW_49); 

                            					newLeafNode(otherlv_24, grammarAccess.getDataProtectionAccess().getThenKeyword_11_3_0());
                            				
                            // InternalCqrsDsl.g:1569:5: ( (lv_erasure_25_0= ruleErasureStrategy ) )
                            // InternalCqrsDsl.g:1570:6: (lv_erasure_25_0= ruleErasureStrategy )
                            {
                            // InternalCqrsDsl.g:1570:6: (lv_erasure_25_0= ruleErasureStrategy )
                            // InternalCqrsDsl.g:1571:7: lv_erasure_25_0= ruleErasureStrategy
                            {

                            							newCompositeNode(grammarAccess.getDataProtectionAccess().getErasureErasureStrategyEnumRuleCall_11_3_1_0());
                            						
                            pushFollow(FOLLOW_30);
                            lv_erasure_25_0=ruleErasureStrategy();

                            state._fsp--;


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
                            break;

                    }


                    }
                    break;

            }

            otherlv_26=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_26, grammarAccess.getDataProtectionAccess().getRightCurlyBracketKeyword_12());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:1598:1: entryRuleDataProtectionInstance returns [EObject current=null] : iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF ;
    public final EObject entryRuleDataProtectionInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataProtectionInstance = null;


        try {
            // InternalCqrsDsl.g:1598:63: (iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF )
            // InternalCqrsDsl.g:1599:2: iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF
            {
             newCompositeNode(grammarAccess.getDataProtectionInstanceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDataProtectionInstance=ruleDataProtectionInstance();

            state._fsp--;

             current =iv_ruleDataProtectionInstance; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:1605:1: ruleDataProtectionInstance returns [EObject current=null] : (otherlv_0= 'protected-by' ( ( ruleFQN ) ) ) ;
    public final EObject ruleDataProtectionInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:1611:2: ( (otherlv_0= 'protected-by' ( ( ruleFQN ) ) ) )
            // InternalCqrsDsl.g:1612:2: (otherlv_0= 'protected-by' ( ( ruleFQN ) ) )
            {
            // InternalCqrsDsl.g:1612:2: (otherlv_0= 'protected-by' ( ( ruleFQN ) ) )
            // InternalCqrsDsl.g:1613:3: otherlv_0= 'protected-by' ( ( ruleFQN ) )
            {
            otherlv_0=(Token)match(input,36,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getDataProtectionInstanceAccess().getProtectedByKeyword_0());
            		
            // InternalCqrsDsl.g:1617:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:1618:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:1618:4: ( ruleFQN )
            // InternalCqrsDsl.g:1619:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDataProtectionInstanceRule());
            					}
            				

            					newCompositeNode(grammarAccess.getDataProtectionInstanceAccess().getPolicyDataProtectionCrossReference_1_0());
            				
            pushFollow(FOLLOW_2);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:1637:1: entryRuleConstraint returns [EObject current=null] : iv_ruleConstraint= ruleConstraint EOF ;
    public final EObject entryRuleConstraint() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstraint = null;


        try {
            // InternalCqrsDsl.g:1637:51: (iv_ruleConstraint= ruleConstraint EOF )
            // InternalCqrsDsl.g:1638:2: iv_ruleConstraint= ruleConstraint EOF
            {
             newCompositeNode(grammarAccess.getConstraintRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConstraint=ruleConstraint();

            state._fsp--;

             current =iv_ruleConstraint; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:1644:1: ruleConstraint returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) ;
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
            // InternalCqrsDsl.g:1650:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) )
            // InternalCqrsDsl.g:1651:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:1651:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            // InternalCqrsDsl.g:1652:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}'
            {
            // InternalCqrsDsl.g:1652:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==RULE_DOC) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalCqrsDsl.g:1653:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1653:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1654:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_50); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getConstraintAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,37,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getConstraintAccess().getConstraintKeyword_1());
            		
            // InternalCqrsDsl.g:1674:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1675:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1675:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1676:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_51); 

            					newLeafNode(lv_name_2_0, grammarAccess.getConstraintAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:1692:3: (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==38) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCqrsDsl.g:1693:4: otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )*
                    {
                    otherlv_3=(Token)match(input,38,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getConstraintAccess().getInputKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:1697:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:1698:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:1698:5: ( ruleFQN )
                    // InternalCqrsDsl.g:1699:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConstraintRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getConstraintAccess().getInputTypeCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_52);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:1713:4: (otherlv_5= '|' ( ( ruleFQN ) ) )*
                    loop41:
                    do {
                        int alt41=2;
                        int LA41_0 = input.LA(1);

                        if ( (LA41_0==39) ) {
                            alt41=1;
                        }


                        switch (alt41) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:1714:5: otherlv_5= '|' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_5=(Token)match(input,39,FOLLOW_4); 

                    	    					newLeafNode(otherlv_5, grammarAccess.getConstraintAccess().getVerticalLineKeyword_3_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:1718:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:1719:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:1719:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:1720:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getConstraintRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getConstraintAccess().getInputTypeCrossReference_3_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_52);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop41;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:1736:3: (otherlv_7= 'exception' ( ( ruleFQN ) ) )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==40) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // InternalCqrsDsl.g:1737:4: otherlv_7= 'exception' ( ( ruleFQN ) )
                    {
                    otherlv_7=(Token)match(input,40,FOLLOW_4); 

                    				newLeafNode(otherlv_7, grammarAccess.getConstraintAccess().getExceptionKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:1741:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:1742:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:1742:5: ( ruleFQN )
                    // InternalCqrsDsl.g:1743:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConstraintRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getConstraintAccess().getExceptionExceptionCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_5);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_53); 

            			newLeafNode(otherlv_9, grammarAccess.getConstraintAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:1762:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( ((LA44_0>=RULE_DOC && LA44_0<=RULE_ID)||LA44_0==64) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCqrsDsl.g:1763:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:1763:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:1764:5: lv_attributes_10_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getConstraintAccess().getAttributesAttributeParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_53);
            	    lv_attributes_10_0=ruleAttribute();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop44;
                }
            } while (true);

            // InternalCqrsDsl.g:1781:3: (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==41) ) {
                alt45=1;
            }
            switch (alt45) {
                case 1 :
                    // InternalCqrsDsl.g:1782:4: otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) )
                    {
                    otherlv_11=(Token)match(input,41,FOLLOW_40); 

                    				newLeafNode(otherlv_11, grammarAccess.getConstraintAccess().getMessageKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:1786:4: ( (lv_message_12_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1787:5: (lv_message_12_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1787:5: (lv_message_12_0= RULE_STRING )
                    // InternalCqrsDsl.g:1788:6: lv_message_12_0= RULE_STRING
                    {
                    lv_message_12_0=(Token)match(input,RULE_STRING,FOLLOW_30); 

                    						newLeafNode(lv_message_12_0, grammarAccess.getConstraintAccess().getMessageSTRINGTerminalRuleCall_7_1_0());
                    					

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
                    break;

            }

            otherlv_13=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_13, grammarAccess.getConstraintAccess().getRightCurlyBracketKeyword_8());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:1813:1: entryRuleBusinessRule returns [EObject current=null] : iv_ruleBusinessRule= ruleBusinessRule EOF ;
    public final EObject entryRuleBusinessRule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRule = null;


        try {
            // InternalCqrsDsl.g:1813:53: (iv_ruleBusinessRule= ruleBusinessRule EOF )
            // InternalCqrsDsl.g:1814:2: iv_ruleBusinessRule= ruleBusinessRule EOF
            {
             newCompositeNode(grammarAccess.getBusinessRuleRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBusinessRule=ruleBusinessRule();

            state._fsp--;

             current =iv_ruleBusinessRule; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:1820:1: ruleBusinessRule returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' ) ;
    public final EObject ruleBusinessRule() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        EObject lv_consistency_6_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:1826:2: ( ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' ) )
            // InternalCqrsDsl.g:1827:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' )
            {
            // InternalCqrsDsl.g:1827:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' )
            // InternalCqrsDsl.g:1828:3: ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}'
            {
            // InternalCqrsDsl.g:1828:3: ( (lv_doc_0_0= RULE_DOC ) )
            // InternalCqrsDsl.g:1829:4: (lv_doc_0_0= RULE_DOC )
            {
            // InternalCqrsDsl.g:1829:4: (lv_doc_0_0= RULE_DOC )
            // InternalCqrsDsl.g:1830:5: lv_doc_0_0= RULE_DOC
            {
            lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_54); 

            					newLeafNode(lv_doc_0_0, grammarAccess.getBusinessRuleAccess().getDocDOCTerminalRuleCall_0_0());
            				

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

            otherlv_1=(Token)match(input,42,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getBusinessRuleAccess().getBusinessRuleKeyword_1());
            		
            // InternalCqrsDsl.g:1850:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1851:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1851:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1852:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_55); 

            					newLeafNode(lv_name_2_0, grammarAccess.getBusinessRuleAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            otherlv_3=(Token)match(input,40,FOLLOW_4); 

            			newLeafNode(otherlv_3, grammarAccess.getBusinessRuleAccess().getExceptionKeyword_3());
            		
            // InternalCqrsDsl.g:1872:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:1873:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:1873:4: ( ruleFQN )
            // InternalCqrsDsl.g:1874:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBusinessRuleRule());
            					}
            				

            					newCompositeNode(grammarAccess.getBusinessRuleAccess().getExceptionExceptionCrossReference_4_0());
            				
            pushFollow(FOLLOW_5);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_5=(Token)match(input,14,FOLLOW_56); 

            			newLeafNode(otherlv_5, grammarAccess.getBusinessRuleAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:1892:3: ( (lv_consistency_6_0= ruleConsistency ) )
            // InternalCqrsDsl.g:1893:4: (lv_consistency_6_0= ruleConsistency )
            {
            // InternalCqrsDsl.g:1893:4: (lv_consistency_6_0= ruleConsistency )
            // InternalCqrsDsl.g:1894:5: lv_consistency_6_0= ruleConsistency
            {

            					newCompositeNode(grammarAccess.getBusinessRuleAccess().getConsistencyConsistencyParserRuleCall_6_0());
            				
            pushFollow(FOLLOW_30);
            lv_consistency_6_0=ruleConsistency();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getBusinessRuleRule());
            					}
            					set(
            						current,
            						"consistency",
            						lv_consistency_6_0,
            						"org.fuin.dsl.cqrs.CqrsDsl.Consistency");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_7=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_7, grammarAccess.getBusinessRuleAccess().getRightCurlyBracketKeyword_7());
            		

            }


            }


            	leaveRule();

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


    // $ANTLR start "entryRuleAnnotation"
    // InternalCqrsDsl.g:1919:1: entryRuleAnnotation returns [EObject current=null] : iv_ruleAnnotation= ruleAnnotation EOF ;
    public final EObject entryRuleAnnotation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnnotation = null;


        try {
            // InternalCqrsDsl.g:1919:51: (iv_ruleAnnotation= ruleAnnotation EOF )
            // InternalCqrsDsl.g:1920:2: iv_ruleAnnotation= ruleAnnotation EOF
            {
             newCompositeNode(grammarAccess.getAnnotationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAnnotation=ruleAnnotation();

            state._fsp--;

             current =iv_ruleAnnotation; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:1926:1: ruleAnnotation returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' ) ;
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
            // InternalCqrsDsl.g:1932:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' ) )
            // InternalCqrsDsl.g:1933:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:1933:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' )
            // InternalCqrsDsl.g:1934:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}'
            {
            // InternalCqrsDsl.g:1934:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==RULE_DOC) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalCqrsDsl.g:1935:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1935:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1936:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_57); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getAnnotationAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,43,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getAnnotationAccess().getAnnotationKeyword_1());
            		
            // InternalCqrsDsl.g:1956:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1957:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1957:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1958:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_2_0, grammarAccess.getAnnotationAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            otherlv_3=(Token)match(input,14,FOLLOW_58); 

            			newLeafNode(otherlv_3, grammarAccess.getAnnotationAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalCqrsDsl.g:1978:3: ( (lv_attributes_4_0= ruleAttribute ) )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( ((LA47_0>=RULE_DOC && LA47_0<=RULE_ID)||LA47_0==64) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // InternalCqrsDsl.g:1979:4: (lv_attributes_4_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:1979:4: (lv_attributes_4_0= ruleAttribute )
            	    // InternalCqrsDsl.g:1980:5: lv_attributes_4_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAnnotationAccess().getAttributesAttributeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_58);
            	    lv_attributes_4_0=ruleAttribute();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);

            otherlv_5=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getAnnotationAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:2005:1: entryRuleException returns [EObject current=null] : iv_ruleException= ruleException EOF ;
    public final EObject entryRuleException() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleException = null;


        try {
            // InternalCqrsDsl.g:2005:50: (iv_ruleException= ruleException EOF )
            // InternalCqrsDsl.g:2006:2: iv_ruleException= ruleException EOF
            {
             newCompositeNode(grammarAccess.getExceptionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleException=ruleException();

            state._fsp--;

             current =iv_ruleException; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:2012:1: ruleException returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' ) ;
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
            // InternalCqrsDsl.g:2018:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' ) )
            // InternalCqrsDsl.g:2019:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' )
            {
            // InternalCqrsDsl.g:2019:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' )
            // InternalCqrsDsl.g:2020:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}'
            {
            // InternalCqrsDsl.g:2020:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==RULE_DOC) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // InternalCqrsDsl.g:2021:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2021:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2022:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_55); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getExceptionAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,40,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getExceptionAccess().getExceptionKeyword_1());
            		
            // InternalCqrsDsl.g:2042:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2043:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2043:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2044:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_59); 

            					newLeafNode(lv_name_2_0, grammarAccess.getExceptionAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:2060:3: (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==44) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCqrsDsl.g:2061:4: otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) )
                    {
                    otherlv_3=(Token)match(input,44,FOLLOW_17); 

                    				newLeafNode(otherlv_3, grammarAccess.getExceptionAccess().getCidKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2065:4: ( (lv_cid_4_0= RULE_INT ) )
                    // InternalCqrsDsl.g:2066:5: (lv_cid_4_0= RULE_INT )
                    {
                    // InternalCqrsDsl.g:2066:5: (lv_cid_4_0= RULE_INT )
                    // InternalCqrsDsl.g:2067:6: lv_cid_4_0= RULE_INT
                    {
                    lv_cid_4_0=(Token)match(input,RULE_INT,FOLLOW_5); 

                    						newLeafNode(lv_cid_4_0, grammarAccess.getExceptionAccess().getCidINTTerminalRuleCall_3_1_0());
                    					

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
                    break;

            }

            otherlv_5=(Token)match(input,14,FOLLOW_60); 

            			newLeafNode(otherlv_5, grammarAccess.getExceptionAccess().getLeftCurlyBracketKeyword_4());
            		
            // InternalCqrsDsl.g:2088:3: ( (lv_attributes_6_0= ruleAttribute ) )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( ((LA50_0>=RULE_DOC && LA50_0<=RULE_ID)||LA50_0==64) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // InternalCqrsDsl.g:2089:4: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2089:4: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2090:5: lv_attributes_6_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getExceptionAccess().getAttributesAttributeParserRuleCall_5_0());
            	    				
            	    pushFollow(FOLLOW_60);
            	    lv_attributes_6_0=ruleAttribute();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop50;
                }
            } while (true);

            otherlv_7=(Token)match(input,41,FOLLOW_40); 

            			newLeafNode(otherlv_7, grammarAccess.getExceptionAccess().getMessageKeyword_6());
            		
            // InternalCqrsDsl.g:2111:3: ( (lv_message_8_0= RULE_STRING ) )
            // InternalCqrsDsl.g:2112:4: (lv_message_8_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:2112:4: (lv_message_8_0= RULE_STRING )
            // InternalCqrsDsl.g:2113:5: lv_message_8_0= RULE_STRING
            {
            lv_message_8_0=(Token)match(input,RULE_STRING,FOLLOW_30); 

            					newLeafNode(lv_message_8_0, grammarAccess.getExceptionAccess().getMessageSTRINGTerminalRuleCall_7_0());
            				

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

            otherlv_9=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_9, grammarAccess.getExceptionAccess().getRightCurlyBracketKeyword_8());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:2137:1: entryRuleValueObject returns [EObject current=null] : iv_ruleValueObject= ruleValueObject EOF ;
    public final EObject entryRuleValueObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleValueObject = null;


        try {
            // InternalCqrsDsl.g:2137:52: (iv_ruleValueObject= ruleValueObject EOF )
            // InternalCqrsDsl.g:2138:2: iv_ruleValueObject= ruleValueObject EOF
            {
             newCompositeNode(grammarAccess.getValueObjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleValueObject=ruleValueObject();

            state._fsp--;

             current =iv_ruleValueObject; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:2144:1: ruleValueObject returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' ) ;
    public final EObject ruleValueObject() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_2=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token otherlv_8=null;
        Token otherlv_13=null;
        EObject lv_annotations_1_0 = null;

        EObject lv_invariants_6_0 = null;

        EObject lv_dataProtection_7_0 = null;

        EObject lv_metaInfo_9_0 = null;

        EObject lv_attributes_10_0 = null;

        EObject lv_constructors_11_0 = null;

        EObject lv_methods_12_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2150:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' ) )
            // InternalCqrsDsl.g:2151:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:2151:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' )
            // InternalCqrsDsl.g:2152:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}'
            {
            // InternalCqrsDsl.g:2152:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==RULE_DOC) ) {
                alt51=1;
            }
            switch (alt51) {
                case 1 :
                    // InternalCqrsDsl.g:2153:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2153:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2154:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_61); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getValueObjectAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            // InternalCqrsDsl.g:2170:3: ( (lv_annotations_1_0= ruleAnnotationInstance ) )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==77) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // InternalCqrsDsl.g:2171:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    {
            	    // InternalCqrsDsl.g:2171:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    // InternalCqrsDsl.g:2172:5: lv_annotations_1_0= ruleAnnotationInstance
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getAnnotationsAnnotationInstanceParserRuleCall_1_0());
            	    				
            	    pushFollow(FOLLOW_61);
            	    lv_annotations_1_0=ruleAnnotationInstance();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);

            otherlv_2=(Token)match(input,45,FOLLOW_4); 

            			newLeafNode(otherlv_2, grammarAccess.getValueObjectAccess().getValueObjectKeyword_2());
            		
            // InternalCqrsDsl.g:2193:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:2194:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2194:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:2195:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_62); 

            					newLeafNode(lv_name_3_0, grammarAccess.getValueObjectAccess().getNameIDTerminalRuleCall_3_0());
            				

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

            // InternalCqrsDsl.g:2211:3: (otherlv_4= 'base' ( ( ruleFQN ) ) )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==46) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCqrsDsl.g:2212:4: otherlv_4= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_4=(Token)match(input,46,FOLLOW_4); 

                    				newLeafNode(otherlv_4, grammarAccess.getValueObjectAccess().getBaseKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:2216:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2217:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2217:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2218:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getValueObjectRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getValueObjectAccess().getBaseExternalTypeCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_63);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2233:3: ( (lv_invariants_6_0= ruleInvariants ) )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==74) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // InternalCqrsDsl.g:2234:4: (lv_invariants_6_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2234:4: (lv_invariants_6_0= ruleInvariants )
                    // InternalCqrsDsl.g:2235:5: lv_invariants_6_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getValueObjectAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_64);
                    lv_invariants_6_0=ruleInvariants();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:2252:3: ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==36) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // InternalCqrsDsl.g:2253:4: (lv_dataProtection_7_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2253:4: (lv_dataProtection_7_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2254:5: lv_dataProtection_7_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getValueObjectAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_7_0=ruleDataProtectionInstance();

                    state._fsp--;


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
                    break;

            }

            otherlv_8=(Token)match(input,14,FOLLOW_65); 

            			newLeafNode(otherlv_8, grammarAccess.getValueObjectAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:2275:3: ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2276:4: (lv_metaInfo_9_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2276:4: (lv_metaInfo_9_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2277:5: lv_metaInfo_9_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getValueObjectAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_66);
            lv_metaInfo_9_0=ruleTypeMetaInfo();

            state._fsp--;


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

            // InternalCqrsDsl.g:2294:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop56:
            do {
                int alt56=2;
                int LA56_0 = input.LA(1);

                if ( (LA56_0==RULE_DOC) ) {
                    int LA56_1 = input.LA(2);

                    if ( (LA56_1==RULE_ID||LA56_1==64) ) {
                        alt56=1;
                    }


                }
                else if ( (LA56_0==RULE_ID||LA56_0==64) ) {
                    alt56=1;
                }


                switch (alt56) {
            	case 1 :
            	    // InternalCqrsDsl.g:2295:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2295:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2296:5: lv_attributes_10_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_66);
            	    lv_attributes_10_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_10_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop56;
                }
            } while (true);

            // InternalCqrsDsl.g:2313:3: ( (lv_constructors_11_0= ruleConstructor ) )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==RULE_DOC) ) {
                    int LA57_1 = input.LA(2);

                    if ( (LA57_1==61) ) {
                        alt57=1;
                    }


                }
                else if ( (LA57_0==61) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // InternalCqrsDsl.g:2314:4: (lv_constructors_11_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:2314:4: (lv_constructors_11_0= ruleConstructor )
            	    // InternalCqrsDsl.g:2315:5: lv_constructors_11_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_67);
            	    lv_constructors_11_0=ruleConstructor();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	    					}
            	    					add(
            	    						current,
            	    						"constructors",
            	    						lv_constructors_11_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);

            // InternalCqrsDsl.g:2332:3: ( (lv_methods_12_0= ruleMethod ) )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==RULE_DOC||LA58_0==65) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // InternalCqrsDsl.g:2333:4: (lv_methods_12_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:2333:4: (lv_methods_12_0= ruleMethod )
            	    // InternalCqrsDsl.g:2334:5: lv_methods_12_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_68);
            	    lv_methods_12_0=ruleMethod();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getValueObjectRule());
            	    					}
            	    					add(
            	    						current,
            	    						"methods",
            	    						lv_methods_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop58;
                }
            } while (true);

            otherlv_13=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_13, grammarAccess.getValueObjectAccess().getRightCurlyBracketKeyword_12());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:2359:1: entryRuleEntityId returns [EObject current=null] : iv_ruleEntityId= ruleEntityId EOF ;
    public final EObject entryRuleEntityId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntityId = null;


        try {
            // InternalCqrsDsl.g:2359:49: (iv_ruleEntityId= ruleEntityId EOF )
            // InternalCqrsDsl.g:2360:2: iv_ruleEntityId= ruleEntityId EOF
            {
             newCompositeNode(grammarAccess.getEntityIdRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEntityId=ruleEntityId();

            state._fsp--;

             current =iv_ruleEntityId; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:2366:1: ruleEntityId returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) ;
    public final EObject ruleEntityId() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_9=null;
        Token otherlv_14=null;
        EObject lv_invariants_7_0 = null;

        EObject lv_dataProtection_8_0 = null;

        EObject lv_metaInfo_10_0 = null;

        EObject lv_attributes_11_0 = null;

        EObject lv_constructors_12_0 = null;

        EObject lv_methods_13_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2372:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:2373:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:2373:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:2374:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:2374:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( (LA59_0==RULE_DOC) ) {
                alt59=1;
            }
            switch (alt59) {
                case 1 :
                    // InternalCqrsDsl.g:2375:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2375:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2376:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_69); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getEntityIdAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,47,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getEntityIdAccess().getEntityIdKeyword_1());
            		
            // InternalCqrsDsl.g:2396:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2397:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2397:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2398:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_70); 

            					newLeafNode(lv_name_2_0, grammarAccess.getEntityIdAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:2414:3: (otherlv_3= 'identifies' ( ( ruleFQN ) ) )?
            int alt60=2;
            int LA60_0 = input.LA(1);

            if ( (LA60_0==48) ) {
                alt60=1;
            }
            switch (alt60) {
                case 1 :
                    // InternalCqrsDsl.g:2415:4: otherlv_3= 'identifies' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,48,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getEntityIdAccess().getIdentifiesKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2419:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2420:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2420:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2421:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityIdAccess().getEntityEntityCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_62);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2436:3: (otherlv_5= 'base' ( ( ruleFQN ) ) )?
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==46) ) {
                alt61=1;
            }
            switch (alt61) {
                case 1 :
                    // InternalCqrsDsl.g:2437:4: otherlv_5= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,46,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getEntityIdAccess().getBaseKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:2441:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2442:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2442:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2443:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityIdAccess().getBaseExternalTypeCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_63);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2458:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==74) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // InternalCqrsDsl.g:2459:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2459:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:2460:5: lv_invariants_7_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getEntityIdAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_64);
                    lv_invariants_7_0=ruleInvariants();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:2477:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==36) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // InternalCqrsDsl.g:2478:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2478:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2479:5: lv_dataProtection_8_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getEntityIdAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_8_0=ruleDataProtectionInstance();

                    state._fsp--;


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
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_65); 

            			newLeafNode(otherlv_9, grammarAccess.getEntityIdAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:2500:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2501:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2501:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2502:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getEntityIdAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_66);
            lv_metaInfo_10_0=ruleTypeMetaInfo();

            state._fsp--;


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

            // InternalCqrsDsl.g:2519:3: ( (lv_attributes_11_0= ruleAttribute ) )*
            loop64:
            do {
                int alt64=2;
                int LA64_0 = input.LA(1);

                if ( (LA64_0==RULE_DOC) ) {
                    int LA64_1 = input.LA(2);

                    if ( (LA64_1==RULE_ID||LA64_1==64) ) {
                        alt64=1;
                    }


                }
                else if ( (LA64_0==RULE_ID||LA64_0==64) ) {
                    alt64=1;
                }


                switch (alt64) {
            	case 1 :
            	    // InternalCqrsDsl.g:2520:4: (lv_attributes_11_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2520:4: (lv_attributes_11_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2521:5: lv_attributes_11_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEntityIdAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_66);
            	    lv_attributes_11_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityIdRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_11_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);

            // InternalCqrsDsl.g:2538:3: ( (lv_constructors_12_0= ruleConstructor ) )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==RULE_DOC) ) {
                    int LA65_1 = input.LA(2);

                    if ( (LA65_1==61) ) {
                        alt65=1;
                    }


                }
                else if ( (LA65_0==61) ) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // InternalCqrsDsl.g:2539:4: (lv_constructors_12_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:2539:4: (lv_constructors_12_0= ruleConstructor )
            	    // InternalCqrsDsl.g:2540:5: lv_constructors_12_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getEntityIdAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_67);
            	    lv_constructors_12_0=ruleConstructor();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityIdRule());
            	    					}
            	    					add(
            	    						current,
            	    						"constructors",
            	    						lv_constructors_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop65;
                }
            } while (true);

            // InternalCqrsDsl.g:2557:3: ( (lv_methods_13_0= ruleMethod ) )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==RULE_DOC||LA66_0==65) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // InternalCqrsDsl.g:2558:4: (lv_methods_13_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:2558:4: (lv_methods_13_0= ruleMethod )
            	    // InternalCqrsDsl.g:2559:5: lv_methods_13_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getEntityIdAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_68);
            	    lv_methods_13_0=ruleMethod();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityIdRule());
            	    					}
            	    					add(
            	    						current,
            	    						"methods",
            	    						lv_methods_13_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop66;
                }
            } while (true);

            otherlv_14=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_14, grammarAccess.getEntityIdAccess().getRightCurlyBracketKeyword_12());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:2584:1: entryRuleAggregateId returns [EObject current=null] : iv_ruleAggregateId= ruleAggregateId EOF ;
    public final EObject entryRuleAggregateId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregateId = null;


        try {
            // InternalCqrsDsl.g:2584:52: (iv_ruleAggregateId= ruleAggregateId EOF )
            // InternalCqrsDsl.g:2585:2: iv_ruleAggregateId= ruleAggregateId EOF
            {
             newCompositeNode(grammarAccess.getAggregateIdRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAggregateId=ruleAggregateId();

            state._fsp--;

             current =iv_ruleAggregateId; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:2591:1: ruleAggregateId returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) ;
    public final EObject ruleAggregateId() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_9=null;
        Token otherlv_14=null;
        EObject lv_invariants_7_0 = null;

        EObject lv_dataProtection_8_0 = null;

        EObject lv_metaInfo_10_0 = null;

        EObject lv_attributes_11_0 = null;

        EObject lv_constructors_12_0 = null;

        EObject lv_methods_13_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2597:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:2598:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:2598:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:2599:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:2599:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt67=2;
            int LA67_0 = input.LA(1);

            if ( (LA67_0==RULE_DOC) ) {
                alt67=1;
            }
            switch (alt67) {
                case 1 :
                    // InternalCqrsDsl.g:2600:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2600:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2601:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_71); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getAggregateIdAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,49,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregateIdAccess().getAggregateIdKeyword_1());
            		
            // InternalCqrsDsl.g:2621:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2622:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2622:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2623:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_70); 

            					newLeafNode(lv_name_2_0, grammarAccess.getAggregateIdAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:2639:3: (otherlv_3= 'identifies' ( ( ruleFQN ) ) )?
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==48) ) {
                alt68=1;
            }
            switch (alt68) {
                case 1 :
                    // InternalCqrsDsl.g:2640:4: otherlv_3= 'identifies' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,48,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getAggregateIdAccess().getIdentifiesKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2644:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2645:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2645:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2646:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregateIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getAggregateIdAccess().getAggregateAggregateCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_62);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2661:3: (otherlv_5= 'base' ( ( ruleFQN ) ) )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==46) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // InternalCqrsDsl.g:2662:4: otherlv_5= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,46,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getAggregateIdAccess().getBaseKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:2666:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2667:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2667:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2668:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregateIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getAggregateIdAccess().getBaseExternalTypeCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_63);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2683:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==74) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // InternalCqrsDsl.g:2684:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2684:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:2685:5: lv_invariants_7_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getAggregateIdAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_64);
                    lv_invariants_7_0=ruleInvariants();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:2702:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==36) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // InternalCqrsDsl.g:2703:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2703:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2704:5: lv_dataProtection_8_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getAggregateIdAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_8_0=ruleDataProtectionInstance();

                    state._fsp--;


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
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_65); 

            			newLeafNode(otherlv_9, grammarAccess.getAggregateIdAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:2725:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2726:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2726:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2727:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getAggregateIdAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_66);
            lv_metaInfo_10_0=ruleTypeMetaInfo();

            state._fsp--;


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

            // InternalCqrsDsl.g:2744:3: ( (lv_attributes_11_0= ruleAttribute ) )*
            loop72:
            do {
                int alt72=2;
                int LA72_0 = input.LA(1);

                if ( (LA72_0==RULE_DOC) ) {
                    int LA72_1 = input.LA(2);

                    if ( (LA72_1==RULE_ID||LA72_1==64) ) {
                        alt72=1;
                    }


                }
                else if ( (LA72_0==RULE_ID||LA72_0==64) ) {
                    alt72=1;
                }


                switch (alt72) {
            	case 1 :
            	    // InternalCqrsDsl.g:2745:4: (lv_attributes_11_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2745:4: (lv_attributes_11_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2746:5: lv_attributes_11_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateIdAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_66);
            	    lv_attributes_11_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_11_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop72;
                }
            } while (true);

            // InternalCqrsDsl.g:2763:3: ( (lv_constructors_12_0= ruleConstructor ) )*
            loop73:
            do {
                int alt73=2;
                int LA73_0 = input.LA(1);

                if ( (LA73_0==RULE_DOC) ) {
                    int LA73_1 = input.LA(2);

                    if ( (LA73_1==61) ) {
                        alt73=1;
                    }


                }
                else if ( (LA73_0==61) ) {
                    alt73=1;
                }


                switch (alt73) {
            	case 1 :
            	    // InternalCqrsDsl.g:2764:4: (lv_constructors_12_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:2764:4: (lv_constructors_12_0= ruleConstructor )
            	    // InternalCqrsDsl.g:2765:5: lv_constructors_12_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateIdAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_67);
            	    lv_constructors_12_0=ruleConstructor();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
            	    					}
            	    					add(
            	    						current,
            	    						"constructors",
            	    						lv_constructors_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop73;
                }
            } while (true);

            // InternalCqrsDsl.g:2782:3: ( (lv_methods_13_0= ruleMethod ) )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==RULE_DOC||LA74_0==65) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // InternalCqrsDsl.g:2783:4: (lv_methods_13_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:2783:4: (lv_methods_13_0= ruleMethod )
            	    // InternalCqrsDsl.g:2784:5: lv_methods_13_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateIdAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_68);
            	    lv_methods_13_0=ruleMethod();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateIdRule());
            	    					}
            	    					add(
            	    						current,
            	    						"methods",
            	    						lv_methods_13_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop74;
                }
            } while (true);

            otherlv_14=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_14, grammarAccess.getAggregateIdAccess().getRightCurlyBracketKeyword_12());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:2809:1: entryRuleEnumObject returns [EObject current=null] : iv_ruleEnumObject= ruleEnumObject EOF ;
    public final EObject entryRuleEnumObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumObject = null;


        try {
            // InternalCqrsDsl.g:2809:51: (iv_ruleEnumObject= ruleEnumObject EOF )
            // InternalCqrsDsl.g:2810:2: iv_ruleEnumObject= ruleEnumObject EOF
            {
             newCompositeNode(grammarAccess.getEnumObjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEnumObject=ruleEnumObject();

            state._fsp--;

             current =iv_ruleEnumObject; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:2816:1: ruleEnumObject returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' ) ;
    public final EObject ruleEnumObject() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_7=null;
        Token otherlv_10=null;
        Token otherlv_11=null;
        Token otherlv_13=null;
        Token otherlv_14=null;
        EObject lv_invariants_5_0 = null;

        EObject lv_dataProtection_6_0 = null;

        EObject lv_metaInfo_8_0 = null;

        EObject lv_attributes_9_0 = null;

        EObject lv_instances_12_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:2822:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' ) )
            // InternalCqrsDsl.g:2823:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:2823:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' )
            // InternalCqrsDsl.g:2824:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}'
            {
            // InternalCqrsDsl.g:2824:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt75=2;
            int LA75_0 = input.LA(1);

            if ( (LA75_0==RULE_DOC) ) {
                alt75=1;
            }
            switch (alt75) {
                case 1 :
                    // InternalCqrsDsl.g:2825:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2825:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2826:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_72); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getEnumObjectAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,50,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getEnumObjectAccess().getEnumKeyword_1());
            		
            // InternalCqrsDsl.g:2846:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2847:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2847:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2848:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_62); 

            					newLeafNode(lv_name_2_0, grammarAccess.getEnumObjectAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:2864:3: (otherlv_3= 'base' ( ( ruleFQN ) ) )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==46) ) {
                alt76=1;
            }
            switch (alt76) {
                case 1 :
                    // InternalCqrsDsl.g:2865:4: otherlv_3= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,46,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getEnumObjectAccess().getBaseKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2869:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2870:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2870:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2871:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEnumObjectRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEnumObjectAccess().getBaseExternalTypeCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_63);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2886:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt77=2;
            int LA77_0 = input.LA(1);

            if ( (LA77_0==74) ) {
                alt77=1;
            }
            switch (alt77) {
                case 1 :
                    // InternalCqrsDsl.g:2887:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2887:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:2888:5: lv_invariants_5_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getEnumObjectAccess().getInvariantsInvariantsParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_64);
                    lv_invariants_5_0=ruleInvariants();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:2905:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( (LA78_0==36) ) {
                alt78=1;
            }
            switch (alt78) {
                case 1 :
                    // InternalCqrsDsl.g:2906:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2906:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2907:5: lv_dataProtection_6_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getEnumObjectAccess().getDataProtectionDataProtectionInstanceParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_6_0=ruleDataProtectionInstance();

                    state._fsp--;


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
                    break;

            }

            otherlv_7=(Token)match(input,14,FOLLOW_73); 

            			newLeafNode(otherlv_7, grammarAccess.getEnumObjectAccess().getLeftCurlyBracketKeyword_6());
            		
            // InternalCqrsDsl.g:2928:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2929:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2929:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2930:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getEnumObjectAccess().getMetaInfoTypeMetaInfoParserRuleCall_7_0());
            				
            pushFollow(FOLLOW_74);
            lv_metaInfo_8_0=ruleTypeMetaInfo();

            state._fsp--;


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

            // InternalCqrsDsl.g:2947:3: ( (lv_attributes_9_0= ruleAttribute ) )*
            loop79:
            do {
                int alt79=2;
                int LA79_0 = input.LA(1);

                if ( ((LA79_0>=RULE_DOC && LA79_0<=RULE_ID)||LA79_0==64) ) {
                    alt79=1;
                }


                switch (alt79) {
            	case 1 :
            	    // InternalCqrsDsl.g:2948:4: (lv_attributes_9_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2948:4: (lv_attributes_9_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2949:5: lv_attributes_9_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEnumObjectAccess().getAttributesAttributeParserRuleCall_8_0());
            	    				
            	    pushFollow(FOLLOW_74);
            	    lv_attributes_9_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_9_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop79;
                }
            } while (true);

            otherlv_10=(Token)match(input,51,FOLLOW_5); 

            			newLeafNode(otherlv_10, grammarAccess.getEnumObjectAccess().getInstancesKeyword_9());
            		
            otherlv_11=(Token)match(input,14,FOLLOW_75); 

            			newLeafNode(otherlv_11, grammarAccess.getEnumObjectAccess().getLeftCurlyBracketKeyword_10());
            		
            // InternalCqrsDsl.g:2974:3: ( (lv_instances_12_0= ruleEnumInstance ) )+
            int cnt80=0;
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( ((LA80_0>=RULE_DOC && LA80_0<=RULE_ID)||LA80_0==52) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // InternalCqrsDsl.g:2975:4: (lv_instances_12_0= ruleEnumInstance )
            	    {
            	    // InternalCqrsDsl.g:2975:4: (lv_instances_12_0= ruleEnumInstance )
            	    // InternalCqrsDsl.g:2976:5: lv_instances_12_0= ruleEnumInstance
            	    {

            	    					newCompositeNode(grammarAccess.getEnumObjectAccess().getInstancesEnumInstanceParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_76);
            	    lv_instances_12_0=ruleEnumInstance();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEnumObjectRule());
            	    					}
            	    					add(
            	    						current,
            	    						"instances",
            	    						lv_instances_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.EnumInstance");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt80 >= 1 ) break loop80;
                        EarlyExitException eee =
                            new EarlyExitException(80, input);
                        throw eee;
                }
                cnt80++;
            } while (true);

            otherlv_13=(Token)match(input,15,FOLLOW_30); 

            			newLeafNode(otherlv_13, grammarAccess.getEnumObjectAccess().getRightCurlyBracketKeyword_12());
            		
            otherlv_14=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_14, grammarAccess.getEnumObjectAccess().getRightCurlyBracketKeyword_13());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:3005:1: entryRuleEnumInstance returns [EObject current=null] : iv_ruleEnumInstance= ruleEnumInstance EOF ;
    public final EObject entryRuleEnumInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumInstance = null;


        try {
            // InternalCqrsDsl.g:3005:53: (iv_ruleEnumInstance= ruleEnumInstance EOF )
            // InternalCqrsDsl.g:3006:2: iv_ruleEnumInstance= ruleEnumInstance EOF
            {
             newCompositeNode(grammarAccess.getEnumInstanceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEnumInstance=ruleEnumInstance();

            state._fsp--;

             current =iv_ruleEnumInstance; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:3012:1: ruleEnumInstance returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ) ;
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



        	enterRule();

        try {
            // InternalCqrsDsl.g:3018:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ) )
            // InternalCqrsDsl.g:3019:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? )
            {
            // InternalCqrsDsl.g:3019:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? )
            // InternalCqrsDsl.g:3020:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )?
            {
            // InternalCqrsDsl.g:3020:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( (LA81_0==RULE_DOC) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // InternalCqrsDsl.g:3021:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3021:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3022:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_77); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getEnumInstanceAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            // InternalCqrsDsl.g:3038:3: ( (lv_deprecated_1_0= 'deprecated' ) )?
            int alt82=2;
            int LA82_0 = input.LA(1);

            if ( (LA82_0==52) ) {
                alt82=1;
            }
            switch (alt82) {
                case 1 :
                    // InternalCqrsDsl.g:3039:4: (lv_deprecated_1_0= 'deprecated' )
                    {
                    // InternalCqrsDsl.g:3039:4: (lv_deprecated_1_0= 'deprecated' )
                    // InternalCqrsDsl.g:3040:5: lv_deprecated_1_0= 'deprecated'
                    {
                    lv_deprecated_1_0=(Token)match(input,52,FOLLOW_4); 

                    					newLeafNode(lv_deprecated_1_0, grammarAccess.getEnumInstanceAccess().getDeprecatedDeprecatedKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getEnumInstanceRule());
                    					}
                    					setWithLastConsumed(current, "deprecated", lv_deprecated_1_0, "deprecated");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3052:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3053:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3053:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3054:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_78); 

            					newLeafNode(lv_name_2_0, grammarAccess.getEnumInstanceAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:3070:3: (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )?
            int alt84=2;
            int LA84_0 = input.LA(1);

            if ( (LA84_0==53) ) {
                alt84=1;
            }
            switch (alt84) {
                case 1 :
                    // InternalCqrsDsl.g:3071:4: otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')'
                    {
                    otherlv_3=(Token)match(input,53,FOLLOW_79); 

                    				newLeafNode(otherlv_3, grammarAccess.getEnumInstanceAccess().getLeftParenthesisKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:3075:4: ( (lv_params_4_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:3076:5: (lv_params_4_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:3076:5: (lv_params_4_0= ruleLiteral )
                    // InternalCqrsDsl.g:3077:6: lv_params_4_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getEnumInstanceAccess().getParamsLiteralParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_80);
                    lv_params_4_0=ruleLiteral();

                    state._fsp--;


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

                    // InternalCqrsDsl.g:3094:4: (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )*
                    loop83:
                    do {
                        int alt83=2;
                        int LA83_0 = input.LA(1);

                        if ( (LA83_0==30) ) {
                            alt83=1;
                        }


                        switch (alt83) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:3095:5: otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) )
                    	    {
                    	    otherlv_5=(Token)match(input,30,FOLLOW_79); 

                    	    					newLeafNode(otherlv_5, grammarAccess.getEnumInstanceAccess().getCommaKeyword_3_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:3099:5: ( (lv_params_6_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:3100:6: (lv_params_6_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:3100:6: (lv_params_6_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:3101:7: lv_params_6_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getEnumInstanceAccess().getParamsLiteralParserRuleCall_3_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_80);
                    	    lv_params_6_0=ruleLiteral();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop83;
                        }
                    } while (true);

                    otherlv_7=(Token)match(input,54,FOLLOW_2); 

                    				newLeafNode(otherlv_7, grammarAccess.getEnumInstanceAccess().getRightParenthesisKeyword_3_3());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:3128:1: entryRuleEvent returns [EObject current=null] : iv_ruleEvent= ruleEvent EOF ;
    public final EObject entryRuleEvent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEvent = null;


        try {
            // InternalCqrsDsl.g:3128:46: (iv_ruleEvent= ruleEvent EOF )
            // InternalCqrsDsl.g:3129:2: iv_ruleEvent= ruleEvent EOF
            {
             newCompositeNode(grammarAccess.getEventRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEvent=ruleEvent();

            state._fsp--;

             current =iv_ruleEvent; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:3135:1: ruleEvent returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' ) ;
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
            // InternalCqrsDsl.g:3141:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' ) )
            // InternalCqrsDsl.g:3142:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' )
            {
            // InternalCqrsDsl.g:3142:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' )
            // InternalCqrsDsl.g:3143:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}'
            {
            // InternalCqrsDsl.g:3143:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( (LA85_0==RULE_DOC) ) {
                alt85=1;
            }
            switch (alt85) {
                case 1 :
                    // InternalCqrsDsl.g:3144:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3144:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3145:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_81); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getEventAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            // InternalCqrsDsl.g:3161:3: ( (lv_annotations_1_0= ruleAnnotationInstance ) )*
            loop86:
            do {
                int alt86=2;
                int LA86_0 = input.LA(1);

                if ( (LA86_0==77) ) {
                    alt86=1;
                }


                switch (alt86) {
            	case 1 :
            	    // InternalCqrsDsl.g:3162:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    {
            	    // InternalCqrsDsl.g:3162:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    // InternalCqrsDsl.g:3163:5: lv_annotations_1_0= ruleAnnotationInstance
            	    {

            	    					newCompositeNode(grammarAccess.getEventAccess().getAnnotationsAnnotationInstanceParserRuleCall_1_0());
            	    				
            	    pushFollow(FOLLOW_81);
            	    lv_annotations_1_0=ruleAnnotationInstance();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop86;
                }
            } while (true);

            otherlv_2=(Token)match(input,55,FOLLOW_4); 

            			newLeafNode(otherlv_2, grammarAccess.getEventAccess().getEventKeyword_2());
            		
            // InternalCqrsDsl.g:3184:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:3185:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3185:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:3186:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_82); 

            					newLeafNode(lv_name_3_0, grammarAccess.getEventAccess().getNameIDTerminalRuleCall_3_0());
            				

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

            // InternalCqrsDsl.g:3202:3: (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )?
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( (LA87_0==56) ) {
                alt87=1;
            }
            switch (alt87) {
                case 1 :
                    // InternalCqrsDsl.g:3203:4: otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) )
                    {
                    otherlv_4=(Token)match(input,56,FOLLOW_4); 

                    				newLeafNode(otherlv_4, grammarAccess.getEventAccess().getCopiesAttributesOfKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:3207:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3208:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3208:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3209:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEventRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEventAccess().getOriginAbstractMethodCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_5);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,14,FOLLOW_53); 

            			newLeafNode(otherlv_6, grammarAccess.getEventAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:3228:3: ( (lv_attributes_7_0= ruleAttribute ) )*
            loop88:
            do {
                int alt88=2;
                int LA88_0 = input.LA(1);

                if ( ((LA88_0>=RULE_DOC && LA88_0<=RULE_ID)||LA88_0==64) ) {
                    alt88=1;
                }


                switch (alt88) {
            	case 1 :
            	    // InternalCqrsDsl.g:3229:4: (lv_attributes_7_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3229:4: (lv_attributes_7_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3230:5: lv_attributes_7_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEventAccess().getAttributesAttributeParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_53);
            	    lv_attributes_7_0=ruleAttribute();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop88;
                }
            } while (true);

            // InternalCqrsDsl.g:3247:3: (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )?
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( (LA89_0==41) ) {
                alt89=1;
            }
            switch (alt89) {
                case 1 :
                    // InternalCqrsDsl.g:3248:4: otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) )
                    {
                    otherlv_8=(Token)match(input,41,FOLLOW_40); 

                    				newLeafNode(otherlv_8, grammarAccess.getEventAccess().getMessageKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:3252:4: ( (lv_message_9_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:3253:5: (lv_message_9_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:3253:5: (lv_message_9_0= RULE_STRING )
                    // InternalCqrsDsl.g:3254:6: lv_message_9_0= RULE_STRING
                    {
                    lv_message_9_0=(Token)match(input,RULE_STRING,FOLLOW_30); 

                    						newLeafNode(lv_message_9_0, grammarAccess.getEventAccess().getMessageSTRINGTerminalRuleCall_7_1_0());
                    					

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
                    break;

            }

            otherlv_10=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_10, grammarAccess.getEventAccess().getRightCurlyBracketKeyword_8());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:3279:1: entryRuleEntity returns [EObject current=null] : iv_ruleEntity= ruleEntity EOF ;
    public final EObject entryRuleEntity() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntity = null;


        try {
            // InternalCqrsDsl.g:3279:47: (iv_ruleEntity= ruleEntity EOF )
            // InternalCqrsDsl.g:3280:2: iv_ruleEntity= ruleEntity EOF
            {
             newCompositeNode(grammarAccess.getEntityRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEntity=ruleEntity();

            state._fsp--;

             current =iv_ruleEntity; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:3286:1: ruleEntity returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' ) ;
    public final EObject ruleEntity() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_9=null;
        Token otherlv_16=null;
        EObject lv_invariants_7_0 = null;

        EObject lv_dataProtection_8_0 = null;

        EObject lv_metaInfo_10_0 = null;

        EObject lv_attributes_11_0 = null;

        EObject lv_businessRules_12_0 = null;

        EObject lv_constructors_13_0 = null;

        EObject lv_methods_14_0 = null;

        EObject lv_elements_15_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3292:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' ) )
            // InternalCqrsDsl.g:3293:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' )
            {
            // InternalCqrsDsl.g:3293:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' )
            // InternalCqrsDsl.g:3294:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}'
            {
            // InternalCqrsDsl.g:3294:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt90=2;
            int LA90_0 = input.LA(1);

            if ( (LA90_0==RULE_DOC) ) {
                alt90=1;
            }
            switch (alt90) {
                case 1 :
                    // InternalCqrsDsl.g:3295:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3295:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3296:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_83); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getEntityAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,57,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getEntityAccess().getEntityKeyword_1());
            		
            // InternalCqrsDsl.g:3316:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3317:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3317:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3318:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_84); 

            					newLeafNode(lv_name_2_0, grammarAccess.getEntityAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:3334:3: (otherlv_3= 'identifier' ( ( ruleFQN ) ) )?
            int alt91=2;
            int LA91_0 = input.LA(1);

            if ( (LA91_0==58) ) {
                alt91=1;
            }
            switch (alt91) {
                case 1 :
                    // InternalCqrsDsl.g:3335:4: otherlv_3= 'identifier' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,58,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getEntityAccess().getIdentifierKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:3339:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3340:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3340:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3341:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityAccess().getIdTypeEntityIdCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_85);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3356:3: (otherlv_5= 'root' ( ( ruleFQN ) ) )?
            int alt92=2;
            int LA92_0 = input.LA(1);

            if ( (LA92_0==59) ) {
                alt92=1;
            }
            switch (alt92) {
                case 1 :
                    // InternalCqrsDsl.g:3357:4: otherlv_5= 'root' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,59,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getEntityAccess().getRootKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:3361:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3362:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3362:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3363:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityAccess().getRootAggregateCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_63);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3378:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt93=2;
            int LA93_0 = input.LA(1);

            if ( (LA93_0==74) ) {
                alt93=1;
            }
            switch (alt93) {
                case 1 :
                    // InternalCqrsDsl.g:3379:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:3379:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:3380:5: lv_invariants_7_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getEntityAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_64);
                    lv_invariants_7_0=ruleInvariants();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:3397:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt94=2;
            int LA94_0 = input.LA(1);

            if ( (LA94_0==36) ) {
                alt94=1;
            }
            switch (alt94) {
                case 1 :
                    // InternalCqrsDsl.g:3398:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:3398:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:3399:5: lv_dataProtection_8_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getEntityAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_8_0=ruleDataProtectionInstance();

                    state._fsp--;


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
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_86); 

            			newLeafNode(otherlv_9, grammarAccess.getEntityAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:3420:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:3421:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:3421:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:3422:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getEntityAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_87);
            lv_metaInfo_10_0=ruleTypeMetaInfo();

            state._fsp--;


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

            // InternalCqrsDsl.g:3439:3: ( (lv_attributes_11_0= ruleAttribute ) )*
            loop95:
            do {
                int alt95=2;
                int LA95_0 = input.LA(1);

                if ( (LA95_0==RULE_DOC) ) {
                    int LA95_1 = input.LA(2);

                    if ( (LA95_1==RULE_ID||LA95_1==64) ) {
                        alt95=1;
                    }


                }
                else if ( (LA95_0==RULE_ID||LA95_0==64) ) {
                    alt95=1;
                }


                switch (alt95) {
            	case 1 :
            	    // InternalCqrsDsl.g:3440:4: (lv_attributes_11_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3440:4: (lv_attributes_11_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3441:5: lv_attributes_11_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_87);
            	    lv_attributes_11_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_11_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop95;
                }
            } while (true);

            // InternalCqrsDsl.g:3458:3: ( (lv_businessRules_12_0= ruleBusinessRule ) )*
            loop96:
            do {
                int alt96=2;
                int LA96_0 = input.LA(1);

                if ( (LA96_0==RULE_DOC) ) {
                    int LA96_1 = input.LA(2);

                    if ( (LA96_1==42) ) {
                        alt96=1;
                    }


                }


                switch (alt96) {
            	case 1 :
            	    // InternalCqrsDsl.g:3459:4: (lv_businessRules_12_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:3459:4: (lv_businessRules_12_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:3460:5: lv_businessRules_12_0= ruleBusinessRule
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getBusinessRulesBusinessRuleParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_88);
            	    lv_businessRules_12_0=ruleBusinessRule();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityRule());
            	    					}
            	    					add(
            	    						current,
            	    						"businessRules",
            	    						lv_businessRules_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRule");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop96;
                }
            } while (true);

            // InternalCqrsDsl.g:3477:3: ( (lv_constructors_13_0= ruleConstructor ) )*
            loop97:
            do {
                int alt97=2;
                int LA97_0 = input.LA(1);

                if ( (LA97_0==RULE_DOC) ) {
                    int LA97_1 = input.LA(2);

                    if ( (LA97_1==61) ) {
                        alt97=1;
                    }


                }
                else if ( (LA97_0==61) ) {
                    alt97=1;
                }


                switch (alt97) {
            	case 1 :
            	    // InternalCqrsDsl.g:3478:4: (lv_constructors_13_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:3478:4: (lv_constructors_13_0= ruleConstructor )
            	    // InternalCqrsDsl.g:3479:5: lv_constructors_13_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getConstructorsConstructorParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_88);
            	    lv_constructors_13_0=ruleConstructor();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityRule());
            	    					}
            	    					add(
            	    						current,
            	    						"constructors",
            	    						lv_constructors_13_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop97;
                }
            } while (true);

            // InternalCqrsDsl.g:3496:3: ( (lv_methods_14_0= ruleMethod ) )*
            loop98:
            do {
                int alt98=2;
                int LA98_0 = input.LA(1);

                if ( (LA98_0==RULE_DOC) ) {
                    int LA98_1 = input.LA(2);

                    if ( (LA98_1==65) ) {
                        alt98=1;
                    }


                }
                else if ( (LA98_0==65) ) {
                    alt98=1;
                }


                switch (alt98) {
            	case 1 :
            	    // InternalCqrsDsl.g:3497:4: (lv_methods_14_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:3497:4: (lv_methods_14_0= ruleMethod )
            	    // InternalCqrsDsl.g:3498:5: lv_methods_14_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getMethodsMethodParserRuleCall_12_0());
            	    				
            	    pushFollow(FOLLOW_89);
            	    lv_methods_14_0=ruleMethod();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityRule());
            	    					}
            	    					add(
            	    						current,
            	    						"methods",
            	    						lv_methods_14_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop98;
                }
            } while (true);

            // InternalCqrsDsl.g:3515:3: ( (lv_elements_15_0= ruleAbstractElement ) )*
            loop99:
            do {
                int alt99=2;
                int LA99_0 = input.LA(1);

                if ( (LA99_0==RULE_DOC||LA99_0==20||LA99_0==27||LA99_0==37||LA99_0==40||LA99_0==43||LA99_0==45||LA99_0==47||(LA99_0>=49 && LA99_0<=50)||LA99_0==55||LA99_0==57||LA99_0==60||(LA99_0>=77 && LA99_0<=79)||LA99_0==82||(LA99_0>=85 && LA99_0<=86)||LA99_0==89) ) {
                    alt99=1;
                }


                switch (alt99) {
            	case 1 :
            	    // InternalCqrsDsl.g:3516:4: (lv_elements_15_0= ruleAbstractElement )
            	    {
            	    // InternalCqrsDsl.g:3516:4: (lv_elements_15_0= ruleAbstractElement )
            	    // InternalCqrsDsl.g:3517:5: lv_elements_15_0= ruleAbstractElement
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getElementsAbstractElementParserRuleCall_13_0());
            	    				
            	    pushFollow(FOLLOW_11);
            	    lv_elements_15_0=ruleAbstractElement();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityRule());
            	    					}
            	    					add(
            	    						current,
            	    						"elements",
            	    						lv_elements_15_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.AbstractElement");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop99;
                }
            } while (true);

            otherlv_16=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_16, grammarAccess.getEntityAccess().getRightCurlyBracketKeyword_14());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:3542:1: entryRuleAggregate returns [EObject current=null] : iv_ruleAggregate= ruleAggregate EOF ;
    public final EObject entryRuleAggregate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregate = null;


        try {
            // InternalCqrsDsl.g:3542:50: (iv_ruleAggregate= ruleAggregate EOF )
            // InternalCqrsDsl.g:3543:2: iv_ruleAggregate= ruleAggregate EOF
            {
             newCompositeNode(grammarAccess.getAggregateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAggregate=ruleAggregate();

            state._fsp--;

             current =iv_ruleAggregate; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:3549:1: ruleAggregate returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' ) ;
    public final EObject ruleAggregate() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_7=null;
        Token otherlv_14=null;
        EObject lv_invariants_5_0 = null;

        EObject lv_dataProtection_6_0 = null;

        EObject lv_metaInfo_8_0 = null;

        EObject lv_attributes_9_0 = null;

        EObject lv_businessRules_10_0 = null;

        EObject lv_constructors_11_0 = null;

        EObject lv_methods_12_0 = null;

        EObject lv_elements_13_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3555:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:3556:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:3556:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:3557:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:3557:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt100=2;
            int LA100_0 = input.LA(1);

            if ( (LA100_0==RULE_DOC) ) {
                alt100=1;
            }
            switch (alt100) {
                case 1 :
                    // InternalCqrsDsl.g:3558:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3558:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3559:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_90); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getAggregateAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,60,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregateAccess().getAggregateKeyword_1());
            		
            // InternalCqrsDsl.g:3579:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3580:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3580:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3581:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_91); 

            					newLeafNode(lv_name_2_0, grammarAccess.getAggregateAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:3597:3: (otherlv_3= 'identifier' ( ( ruleFQN ) ) )?
            int alt101=2;
            int LA101_0 = input.LA(1);

            if ( (LA101_0==58) ) {
                alt101=1;
            }
            switch (alt101) {
                case 1 :
                    // InternalCqrsDsl.g:3598:4: otherlv_3= 'identifier' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,58,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getAggregateAccess().getIdentifierKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:3602:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3603:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3603:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3604:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregateRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getAggregateAccess().getIdTypeAggregateIdCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_63);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3619:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt102=2;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==74) ) {
                alt102=1;
            }
            switch (alt102) {
                case 1 :
                    // InternalCqrsDsl.g:3620:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:3620:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:3621:5: lv_invariants_5_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getAggregateAccess().getInvariantsInvariantsParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_64);
                    lv_invariants_5_0=ruleInvariants();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:3638:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt103=2;
            int LA103_0 = input.LA(1);

            if ( (LA103_0==36) ) {
                alt103=1;
            }
            switch (alt103) {
                case 1 :
                    // InternalCqrsDsl.g:3639:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:3639:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:3640:5: lv_dataProtection_6_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getAggregateAccess().getDataProtectionDataProtectionInstanceParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_5);
                    lv_dataProtection_6_0=ruleDataProtectionInstance();

                    state._fsp--;


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
                    break;

            }

            otherlv_7=(Token)match(input,14,FOLLOW_86); 

            			newLeafNode(otherlv_7, grammarAccess.getAggregateAccess().getLeftCurlyBracketKeyword_6());
            		
            // InternalCqrsDsl.g:3661:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:3662:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:3662:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:3663:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getAggregateAccess().getMetaInfoTypeMetaInfoParserRuleCall_7_0());
            				
            pushFollow(FOLLOW_87);
            lv_metaInfo_8_0=ruleTypeMetaInfo();

            state._fsp--;


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

            // InternalCqrsDsl.g:3680:3: ( (lv_attributes_9_0= ruleAttribute ) )*
            loop104:
            do {
                int alt104=2;
                int LA104_0 = input.LA(1);

                if ( (LA104_0==RULE_DOC) ) {
                    int LA104_1 = input.LA(2);

                    if ( (LA104_1==RULE_ID||LA104_1==64) ) {
                        alt104=1;
                    }


                }
                else if ( (LA104_0==RULE_ID||LA104_0==64) ) {
                    alt104=1;
                }


                switch (alt104) {
            	case 1 :
            	    // InternalCqrsDsl.g:3681:4: (lv_attributes_9_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3681:4: (lv_attributes_9_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3682:5: lv_attributes_9_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getAttributesAttributeParserRuleCall_8_0());
            	    				
            	    pushFollow(FOLLOW_87);
            	    lv_attributes_9_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_9_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop104;
                }
            } while (true);

            // InternalCqrsDsl.g:3699:3: ( (lv_businessRules_10_0= ruleBusinessRule ) )*
            loop105:
            do {
                int alt105=2;
                int LA105_0 = input.LA(1);

                if ( (LA105_0==RULE_DOC) ) {
                    int LA105_1 = input.LA(2);

                    if ( (LA105_1==42) ) {
                        alt105=1;
                    }


                }


                switch (alt105) {
            	case 1 :
            	    // InternalCqrsDsl.g:3700:4: (lv_businessRules_10_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:3700:4: (lv_businessRules_10_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:3701:5: lv_businessRules_10_0= ruleBusinessRule
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getBusinessRulesBusinessRuleParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_88);
            	    lv_businessRules_10_0=ruleBusinessRule();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	    					}
            	    					add(
            	    						current,
            	    						"businessRules",
            	    						lv_businessRules_10_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRule");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop105;
                }
            } while (true);

            // InternalCqrsDsl.g:3718:3: ( (lv_constructors_11_0= ruleConstructor ) )*
            loop106:
            do {
                int alt106=2;
                int LA106_0 = input.LA(1);

                if ( (LA106_0==RULE_DOC) ) {
                    int LA106_1 = input.LA(2);

                    if ( (LA106_1==61) ) {
                        alt106=1;
                    }


                }
                else if ( (LA106_0==61) ) {
                    alt106=1;
                }


                switch (alt106) {
            	case 1 :
            	    // InternalCqrsDsl.g:3719:4: (lv_constructors_11_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:3719:4: (lv_constructors_11_0= ruleConstructor )
            	    // InternalCqrsDsl.g:3720:5: lv_constructors_11_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_88);
            	    lv_constructors_11_0=ruleConstructor();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	    					}
            	    					add(
            	    						current,
            	    						"constructors",
            	    						lv_constructors_11_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Constructor");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop106;
                }
            } while (true);

            // InternalCqrsDsl.g:3737:3: ( (lv_methods_12_0= ruleMethod ) )*
            loop107:
            do {
                int alt107=2;
                int LA107_0 = input.LA(1);

                if ( (LA107_0==RULE_DOC) ) {
                    int LA107_1 = input.LA(2);

                    if ( (LA107_1==65) ) {
                        alt107=1;
                    }


                }
                else if ( (LA107_0==65) ) {
                    alt107=1;
                }


                switch (alt107) {
            	case 1 :
            	    // InternalCqrsDsl.g:3738:4: (lv_methods_12_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:3738:4: (lv_methods_12_0= ruleMethod )
            	    // InternalCqrsDsl.g:3739:5: lv_methods_12_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_89);
            	    lv_methods_12_0=ruleMethod();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	    					}
            	    					add(
            	    						current,
            	    						"methods",
            	    						lv_methods_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop107;
                }
            } while (true);

            // InternalCqrsDsl.g:3756:3: ( (lv_elements_13_0= ruleAbstractElement ) )*
            loop108:
            do {
                int alt108=2;
                int LA108_0 = input.LA(1);

                if ( (LA108_0==RULE_DOC||LA108_0==20||LA108_0==27||LA108_0==37||LA108_0==40||LA108_0==43||LA108_0==45||LA108_0==47||(LA108_0>=49 && LA108_0<=50)||LA108_0==55||LA108_0==57||LA108_0==60||(LA108_0>=77 && LA108_0<=79)||LA108_0==82||(LA108_0>=85 && LA108_0<=86)||LA108_0==89) ) {
                    alt108=1;
                }


                switch (alt108) {
            	case 1 :
            	    // InternalCqrsDsl.g:3757:4: (lv_elements_13_0= ruleAbstractElement )
            	    {
            	    // InternalCqrsDsl.g:3757:4: (lv_elements_13_0= ruleAbstractElement )
            	    // InternalCqrsDsl.g:3758:5: lv_elements_13_0= ruleAbstractElement
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getElementsAbstractElementParserRuleCall_12_0());
            	    				
            	    pushFollow(FOLLOW_11);
            	    lv_elements_13_0=ruleAbstractElement();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAggregateRule());
            	    					}
            	    					add(
            	    						current,
            	    						"elements",
            	    						lv_elements_13_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.AbstractElement");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop108;
                }
            } while (true);

            otherlv_14=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_14, grammarAccess.getAggregateAccess().getRightCurlyBracketKeyword_13());
            		

            }


            }


            	leaveRule();

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


    // $ANTLR start "entryRuleConstructor"
    // InternalCqrsDsl.g:3783:1: entryRuleConstructor returns [EObject current=null] : iv_ruleConstructor= ruleConstructor EOF ;
    public final EObject entryRuleConstructor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstructor = null;


        try {
            // InternalCqrsDsl.g:3783:52: (iv_ruleConstructor= ruleConstructor EOF )
            // InternalCqrsDsl.g:3784:2: iv_ruleConstructor= ruleConstructor EOF
            {
             newCompositeNode(grammarAccess.getConstructorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConstructor=ruleConstructor();

            state._fsp--;

             current =iv_ruleConstructor; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:3790:1: ruleConstructor returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' ) ;
    public final EObject ruleConstructor() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_14=null;
        EObject lv_preconditions_3_0 = null;

        EObject lv_businessRules_4_0 = null;

        EObject lv_parameters_10_0 = null;

        EObject lv_services_12_0 = null;

        EObject lv_events_13_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:3796:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:3797:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:3797:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:3798:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:3798:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt109=2;
            int LA109_0 = input.LA(1);

            if ( (LA109_0==RULE_DOC) ) {
                alt109=1;
            }
            switch (alt109) {
                case 1 :
                    // InternalCqrsDsl.g:3799:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3799:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3800:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_92); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getConstructorAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,61,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getConstructorAccess().getConstructorKeyword_1());
            		
            // InternalCqrsDsl.g:3820:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3821:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3821:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3822:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_93); 

            					newLeafNode(lv_name_2_0, grammarAccess.getConstructorAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:3838:3: ( (lv_preconditions_3_0= rulePreconditions ) )?
            int alt110=2;
            int LA110_0 = input.LA(1);

            if ( (LA110_0==75) ) {
                alt110=1;
            }
            switch (alt110) {
                case 1 :
                    // InternalCqrsDsl.g:3839:4: (lv_preconditions_3_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:3839:4: (lv_preconditions_3_0= rulePreconditions )
                    // InternalCqrsDsl.g:3840:5: lv_preconditions_3_0= rulePreconditions
                    {

                    					newCompositeNode(grammarAccess.getConstructorAccess().getPreconditionsPreconditionsParserRuleCall_3_0());
                    				
                    pushFollow(FOLLOW_94);
                    lv_preconditions_3_0=rulePreconditions();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:3857:3: ( (lv_businessRules_4_0= ruleBusinessRules ) )?
            int alt111=2;
            int LA111_0 = input.LA(1);

            if ( (LA111_0==76) ) {
                alt111=1;
            }
            switch (alt111) {
                case 1 :
                    // InternalCqrsDsl.g:3858:4: (lv_businessRules_4_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:3858:4: (lv_businessRules_4_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:3859:5: lv_businessRules_4_0= ruleBusinessRules
                    {

                    					newCompositeNode(grammarAccess.getConstructorAccess().getBusinessRulesBusinessRulesParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_95);
                    lv_businessRules_4_0=ruleBusinessRules();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:3876:3: (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )?
            int alt113=2;
            int LA113_0 = input.LA(1);

            if ( (LA113_0==62) ) {
                alt113=1;
            }
            switch (alt113) {
                case 1 :
                    // InternalCqrsDsl.g:3877:4: otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_5=(Token)match(input,62,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getConstructorAccess().getFiresKeyword_5_0());
                    			
                    // InternalCqrsDsl.g:3881:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3882:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3882:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3883:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConstructorRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getConstructorAccess().getFiredEventsEventCrossReference_5_1_0());
                    					
                    pushFollow(FOLLOW_96);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:3897:4: (otherlv_7= ',' ( ( ruleFQN ) ) )*
                    loop112:
                    do {
                        int alt112=2;
                        int LA112_0 = input.LA(1);

                        if ( (LA112_0==30) ) {
                            alt112=1;
                        }


                        switch (alt112) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:3898:5: otherlv_7= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_7=(Token)match(input,30,FOLLOW_4); 

                    	    					newLeafNode(otherlv_7, grammarAccess.getConstructorAccess().getCommaKeyword_5_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:3902:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:3903:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:3903:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:3904:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getConstructorRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getConstructorAccess().getFiredEventsEventCrossReference_5_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_96);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop112;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_97); 

            			newLeafNode(otherlv_9, grammarAccess.getConstructorAccess().getLeftCurlyBracketKeyword_6());
            		
            // InternalCqrsDsl.g:3924:3: ( (lv_parameters_10_0= ruleParameter ) )*
            loop114:
            do {
                int alt114=2;
                alt114 = dfa114.predict(input);
                switch (alt114) {
            	case 1 :
            	    // InternalCqrsDsl.g:3925:4: (lv_parameters_10_0= ruleParameter )
            	    {
            	    // InternalCqrsDsl.g:3925:4: (lv_parameters_10_0= ruleParameter )
            	    // InternalCqrsDsl.g:3926:5: lv_parameters_10_0= ruleParameter
            	    {

            	    					newCompositeNode(grammarAccess.getConstructorAccess().getParametersParameterParserRuleCall_7_0());
            	    				
            	    pushFollow(FOLLOW_97);
            	    lv_parameters_10_0=ruleParameter();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop114;
                }
            } while (true);

            // InternalCqrsDsl.g:3943:3: ( ( ruleFQN ) )?
            int alt115=2;
            int LA115_0 = input.LA(1);

            if ( (LA115_0==RULE_ID) ) {
                alt115=1;
            }
            switch (alt115) {
                case 1 :
                    // InternalCqrsDsl.g:3944:4: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3944:4: ( ruleFQN )
                    // InternalCqrsDsl.g:3945:5: ruleFQN
                    {

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getConstructorRule());
                    					}
                    				

                    					newCompositeNode(grammarAccess.getConstructorAccess().getServiceServiceCrossReference_8_0());
                    				
                    pushFollow(FOLLOW_98);
                    ruleFQN();

                    state._fsp--;


                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3959:3: ( (lv_services_12_0= ruleService ) )*
            loop116:
            do {
                int alt116=2;
                int LA116_0 = input.LA(1);

                if ( (LA116_0==RULE_DOC) ) {
                    int LA116_1 = input.LA(2);

                    if ( (LA116_1==78) ) {
                        alt116=1;
                    }


                }
                else if ( (LA116_0==78) ) {
                    alt116=1;
                }


                switch (alt116) {
            	case 1 :
            	    // InternalCqrsDsl.g:3960:4: (lv_services_12_0= ruleService )
            	    {
            	    // InternalCqrsDsl.g:3960:4: (lv_services_12_0= ruleService )
            	    // InternalCqrsDsl.g:3961:5: lv_services_12_0= ruleService
            	    {

            	    					newCompositeNode(grammarAccess.getConstructorAccess().getServicesServiceParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_98);
            	    lv_services_12_0=ruleService();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getConstructorRule());
            	    					}
            	    					add(
            	    						current,
            	    						"services",
            	    						lv_services_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Service");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop116;
                }
            } while (true);

            // InternalCqrsDsl.g:3978:3: ( (lv_events_13_0= ruleEvent ) )*
            loop117:
            do {
                int alt117=2;
                int LA117_0 = input.LA(1);

                if ( (LA117_0==RULE_DOC||LA117_0==55||LA117_0==77) ) {
                    alt117=1;
                }


                switch (alt117) {
            	case 1 :
            	    // InternalCqrsDsl.g:3979:4: (lv_events_13_0= ruleEvent )
            	    {
            	    // InternalCqrsDsl.g:3979:4: (lv_events_13_0= ruleEvent )
            	    // InternalCqrsDsl.g:3980:5: lv_events_13_0= ruleEvent
            	    {

            	    					newCompositeNode(grammarAccess.getConstructorAccess().getEventsEventParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_99);
            	    lv_events_13_0=ruleEvent();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getConstructorRule());
            	    					}
            	    					add(
            	    						current,
            	    						"events",
            	    						lv_events_13_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Event");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop117;
                }
            } while (true);

            otherlv_14=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_14, grammarAccess.getConstructorAccess().getRightCurlyBracketKeyword_11());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4005:1: entryRuleReturnType returns [EObject current=null] : iv_ruleReturnType= ruleReturnType EOF ;
    public final EObject entryRuleReturnType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReturnType = null;


        try {
            // InternalCqrsDsl.g:4005:51: (iv_ruleReturnType= ruleReturnType EOF )
            // InternalCqrsDsl.g:4006:2: iv_ruleReturnType= ruleReturnType EOF
            {
             newCompositeNode(grammarAccess.getReturnTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleReturnType=ruleReturnType();

            state._fsp--;

             current =iv_ruleReturnType; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4012:1: ruleReturnType returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? ) ;
    public final EObject ruleReturnType() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_optional_2_0=null;
        EObject lv_generics_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4018:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? ) )
            // InternalCqrsDsl.g:4019:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? )
            {
            // InternalCqrsDsl.g:4019:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )? )
            // InternalCqrsDsl.g:4020:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( (lv_optional_2_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_4_0= ruleGenericArgs ) )?
            {
            // InternalCqrsDsl.g:4020:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt118=2;
            int LA118_0 = input.LA(1);

            if ( (LA118_0==RULE_DOC) ) {
                alt118=1;
            }
            switch (alt118) {
                case 1 :
                    // InternalCqrsDsl.g:4021:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4021:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4022:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_100); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getReturnTypeAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,63,FOLLOW_101); 

            			newLeafNode(otherlv_1, grammarAccess.getReturnTypeAccess().getReturnsKeyword_1());
            		
            // InternalCqrsDsl.g:4042:3: ( (lv_optional_2_0= 'optional' ) )?
            int alt119=2;
            int LA119_0 = input.LA(1);

            if ( (LA119_0==64) ) {
                alt119=1;
            }
            switch (alt119) {
                case 1 :
                    // InternalCqrsDsl.g:4043:4: (lv_optional_2_0= 'optional' )
                    {
                    // InternalCqrsDsl.g:4043:4: (lv_optional_2_0= 'optional' )
                    // InternalCqrsDsl.g:4044:5: lv_optional_2_0= 'optional'
                    {
                    lv_optional_2_0=(Token)match(input,64,FOLLOW_4); 

                    					newLeafNode(lv_optional_2_0, grammarAccess.getReturnTypeAccess().getOptionalOptionalKeyword_2_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getReturnTypeRule());
                    					}
                    					setWithLastConsumed(current, "optional", lv_optional_2_0, "optional");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4056:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4057:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4057:4: ( ruleFQN )
            // InternalCqrsDsl.g:4058:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getReturnTypeRule());
            					}
            				

            					newCompositeNode(grammarAccess.getReturnTypeAccess().getTypeTypeCrossReference_3_0());
            				
            pushFollow(FOLLOW_102);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4072:3: ( (lv_generics_4_0= ruleGenericArgs ) )?
            int alt120=2;
            int LA120_0 = input.LA(1);

            if ( (LA120_0==72) ) {
                alt120=1;
            }
            switch (alt120) {
                case 1 :
                    // InternalCqrsDsl.g:4073:4: (lv_generics_4_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:4073:4: (lv_generics_4_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:4074:5: lv_generics_4_0= ruleGenericArgs
                    {

                    					newCompositeNode(grammarAccess.getReturnTypeAccess().getGenericsGenericArgsParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_generics_4_0=ruleGenericArgs();

                    state._fsp--;


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
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4095:1: entryRuleMethod returns [EObject current=null] : iv_ruleMethod= ruleMethod EOF ;
    public final EObject entryRuleMethod() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMethod = null;


        try {
            // InternalCqrsDsl.g:4095:47: (iv_ruleMethod= ruleMethod EOF )
            // InternalCqrsDsl.g:4096:2: iv_ruleMethod= ruleMethod EOF
            {
             newCompositeNode(grammarAccess.getMethodRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMethod=ruleMethod();

            state._fsp--;

             current =iv_ruleMethod; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4102:1: ruleMethod returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' ) ;
    public final EObject ruleMethod() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token otherlv_11=null;
        Token otherlv_17=null;
        EObject lv_preconditions_5_0 = null;

        EObject lv_businessRules_6_0 = null;

        EObject lv_parameters_12_0 = null;

        EObject lv_returnType_14_0 = null;

        EObject lv_services_15_0 = null;

        EObject lv_events_16_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4108:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' ) )
            // InternalCqrsDsl.g:4109:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' )
            {
            // InternalCqrsDsl.g:4109:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' )
            // InternalCqrsDsl.g:4110:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}'
            {
            // InternalCqrsDsl.g:4110:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt121=2;
            int LA121_0 = input.LA(1);

            if ( (LA121_0==RULE_DOC) ) {
                alt121=1;
            }
            switch (alt121) {
                case 1 :
                    // InternalCqrsDsl.g:4111:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4111:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4112:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_103); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getMethodAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,65,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getMethodAccess().getMethodKeyword_1());
            		
            // InternalCqrsDsl.g:4132:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:4133:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4133:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:4134:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_104); 

            					newLeafNode(lv_name_2_0, grammarAccess.getMethodAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:4150:3: (otherlv_3= 'ref' ( ( ruleFQN ) ) )?
            int alt122=2;
            int LA122_0 = input.LA(1);

            if ( (LA122_0==66) ) {
                alt122=1;
            }
            switch (alt122) {
                case 1 :
                    // InternalCqrsDsl.g:4151:4: otherlv_3= 'ref' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,66,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getMethodAccess().getRefKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:4155:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:4156:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4156:5: ( ruleFQN )
                    // InternalCqrsDsl.g:4157:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getMethodRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getMethodAccess().getRefMethodMethodCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_93);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4172:3: ( (lv_preconditions_5_0= rulePreconditions ) )?
            int alt123=2;
            int LA123_0 = input.LA(1);

            if ( (LA123_0==75) ) {
                alt123=1;
            }
            switch (alt123) {
                case 1 :
                    // InternalCqrsDsl.g:4173:4: (lv_preconditions_5_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:4173:4: (lv_preconditions_5_0= rulePreconditions )
                    // InternalCqrsDsl.g:4174:5: lv_preconditions_5_0= rulePreconditions
                    {

                    					newCompositeNode(grammarAccess.getMethodAccess().getPreconditionsPreconditionsParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_94);
                    lv_preconditions_5_0=rulePreconditions();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4191:3: ( (lv_businessRules_6_0= ruleBusinessRules ) )?
            int alt124=2;
            int LA124_0 = input.LA(1);

            if ( (LA124_0==76) ) {
                alt124=1;
            }
            switch (alt124) {
                case 1 :
                    // InternalCqrsDsl.g:4192:4: (lv_businessRules_6_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:4192:4: (lv_businessRules_6_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:4193:5: lv_businessRules_6_0= ruleBusinessRules
                    {

                    					newCompositeNode(grammarAccess.getMethodAccess().getBusinessRulesBusinessRulesParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_95);
                    lv_businessRules_6_0=ruleBusinessRules();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4210:3: (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            int alt126=2;
            int LA126_0 = input.LA(1);

            if ( (LA126_0==62) ) {
                alt126=1;
            }
            switch (alt126) {
                case 1 :
                    // InternalCqrsDsl.g:4211:4: otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_7=(Token)match(input,62,FOLLOW_4); 

                    				newLeafNode(otherlv_7, grammarAccess.getMethodAccess().getFiresKeyword_6_0());
                    			
                    // InternalCqrsDsl.g:4215:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:4216:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4216:5: ( ruleFQN )
                    // InternalCqrsDsl.g:4217:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getMethodRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getMethodAccess().getFiredEventsEventCrossReference_6_1_0());
                    					
                    pushFollow(FOLLOW_96);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:4231:4: (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    loop125:
                    do {
                        int alt125=2;
                        int LA125_0 = input.LA(1);

                        if ( (LA125_0==30) ) {
                            alt125=1;
                        }


                        switch (alt125) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:4232:5: otherlv_9= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_9=(Token)match(input,30,FOLLOW_4); 

                    	    					newLeafNode(otherlv_9, grammarAccess.getMethodAccess().getCommaKeyword_6_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:4236:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:4237:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:4237:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:4238:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getMethodRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getMethodAccess().getFiredEventsEventCrossReference_6_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_96);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop125;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_11=(Token)match(input,14,FOLLOW_105); 

            			newLeafNode(otherlv_11, grammarAccess.getMethodAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:4258:3: ( (lv_parameters_12_0= ruleParameter ) )*
            loop127:
            do {
                int alt127=2;
                alt127 = dfa127.predict(input);
                switch (alt127) {
            	case 1 :
            	    // InternalCqrsDsl.g:4259:4: (lv_parameters_12_0= ruleParameter )
            	    {
            	    // InternalCqrsDsl.g:4259:4: (lv_parameters_12_0= ruleParameter )
            	    // InternalCqrsDsl.g:4260:5: lv_parameters_12_0= ruleParameter
            	    {

            	    					newCompositeNode(grammarAccess.getMethodAccess().getParametersParameterParserRuleCall_8_0());
            	    				
            	    pushFollow(FOLLOW_105);
            	    lv_parameters_12_0=ruleParameter();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getMethodRule());
            	    					}
            	    					add(
            	    						current,
            	    						"parameters",
            	    						lv_parameters_12_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Parameter");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop127;
                }
            } while (true);

            // InternalCqrsDsl.g:4277:3: ( ( ruleFQN ) )?
            int alt128=2;
            int LA128_0 = input.LA(1);

            if ( (LA128_0==RULE_ID) ) {
                alt128=1;
            }
            switch (alt128) {
                case 1 :
                    // InternalCqrsDsl.g:4278:4: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4278:4: ( ruleFQN )
                    // InternalCqrsDsl.g:4279:5: ruleFQN
                    {

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getMethodRule());
                    					}
                    				

                    					newCompositeNode(grammarAccess.getMethodAccess().getServiceServiceCrossReference_9_0());
                    				
                    pushFollow(FOLLOW_106);
                    ruleFQN();

                    state._fsp--;


                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4293:3: ( (lv_returnType_14_0= ruleReturnType ) )?
            int alt129=2;
            int LA129_0 = input.LA(1);

            if ( (LA129_0==RULE_DOC) ) {
                int LA129_1 = input.LA(2);

                if ( (LA129_1==63) ) {
                    alt129=1;
                }
            }
            else if ( (LA129_0==63) ) {
                alt129=1;
            }
            switch (alt129) {
                case 1 :
                    // InternalCqrsDsl.g:4294:4: (lv_returnType_14_0= ruleReturnType )
                    {
                    // InternalCqrsDsl.g:4294:4: (lv_returnType_14_0= ruleReturnType )
                    // InternalCqrsDsl.g:4295:5: lv_returnType_14_0= ruleReturnType
                    {

                    					newCompositeNode(grammarAccess.getMethodAccess().getReturnTypeReturnTypeParserRuleCall_10_0());
                    				
                    pushFollow(FOLLOW_98);
                    lv_returnType_14_0=ruleReturnType();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getMethodRule());
                    					}
                    					set(
                    						current,
                    						"returnType",
                    						lv_returnType_14_0,
                    						"org.fuin.dsl.cqrs.CqrsDsl.ReturnType");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4312:3: ( (lv_services_15_0= ruleService ) )*
            loop130:
            do {
                int alt130=2;
                int LA130_0 = input.LA(1);

                if ( (LA130_0==RULE_DOC) ) {
                    int LA130_1 = input.LA(2);

                    if ( (LA130_1==78) ) {
                        alt130=1;
                    }


                }
                else if ( (LA130_0==78) ) {
                    alt130=1;
                }


                switch (alt130) {
            	case 1 :
            	    // InternalCqrsDsl.g:4313:4: (lv_services_15_0= ruleService )
            	    {
            	    // InternalCqrsDsl.g:4313:4: (lv_services_15_0= ruleService )
            	    // InternalCqrsDsl.g:4314:5: lv_services_15_0= ruleService
            	    {

            	    					newCompositeNode(grammarAccess.getMethodAccess().getServicesServiceParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_98);
            	    lv_services_15_0=ruleService();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getMethodRule());
            	    					}
            	    					add(
            	    						current,
            	    						"services",
            	    						lv_services_15_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Service");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop130;
                }
            } while (true);

            // InternalCqrsDsl.g:4331:3: ( (lv_events_16_0= ruleEvent ) )*
            loop131:
            do {
                int alt131=2;
                int LA131_0 = input.LA(1);

                if ( (LA131_0==RULE_DOC||LA131_0==55||LA131_0==77) ) {
                    alt131=1;
                }


                switch (alt131) {
            	case 1 :
            	    // InternalCqrsDsl.g:4332:4: (lv_events_16_0= ruleEvent )
            	    {
            	    // InternalCqrsDsl.g:4332:4: (lv_events_16_0= ruleEvent )
            	    // InternalCqrsDsl.g:4333:5: lv_events_16_0= ruleEvent
            	    {

            	    					newCompositeNode(grammarAccess.getMethodAccess().getEventsEventParserRuleCall_12_0());
            	    				
            	    pushFollow(FOLLOW_99);
            	    lv_events_16_0=ruleEvent();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getMethodRule());
            	    					}
            	    					add(
            	    						current,
            	    						"events",
            	    						lv_events_16_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Event");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop131;
                }
            } while (true);

            otherlv_17=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_17, grammarAccess.getMethodAccess().getRightCurlyBracketKeyword_13());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4358:1: entryRuleTypeMetaInfo returns [EObject current=null] : iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF ;
    public final EObject entryRuleTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTypeMetaInfo = null;


        try {
            // InternalCqrsDsl.g:4358:53: (iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF )
            // InternalCqrsDsl.g:4359:2: iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF
            {
             newCompositeNode(grammarAccess.getTypeMetaInfoRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTypeMetaInfo=ruleTypeMetaInfo();

            state._fsp--;

             current =iv_ruleTypeMetaInfo; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4365:1: ruleTypeMetaInfo returns [EObject current=null] : ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? ) ;
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
            // InternalCqrsDsl.g:4371:2: ( ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? ) )
            // InternalCqrsDsl.g:4372:2: ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? )
            {
            // InternalCqrsDsl.g:4372:2: ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? )
            // InternalCqrsDsl.g:4373:3: () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )?
            {
            // InternalCqrsDsl.g:4373:3: ()
            // InternalCqrsDsl.g:4374:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTypeMetaInfoAccess().getTypeMetaInfoAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:4380:3: (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )?
            int alt132=2;
            int LA132_0 = input.LA(1);

            if ( (LA132_0==67) ) {
                alt132=1;
            }
            switch (alt132) {
                case 1 :
                    // InternalCqrsDsl.g:4381:4: otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) )
                    {
                    otherlv_1=(Token)match(input,67,FOLLOW_40); 

                    				newLeafNode(otherlv_1, grammarAccess.getTypeMetaInfoAccess().getSlabelKeyword_1_0());
                    			
                    // InternalCqrsDsl.g:4385:4: ( (lv_slabel_2_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4386:5: (lv_slabel_2_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4386:5: (lv_slabel_2_0= RULE_STRING )
                    // InternalCqrsDsl.g:4387:6: lv_slabel_2_0= RULE_STRING
                    {
                    lv_slabel_2_0=(Token)match(input,RULE_STRING,FOLLOW_107); 

                    						newLeafNode(lv_slabel_2_0, grammarAccess.getTypeMetaInfoAccess().getSlabelSTRINGTerminalRuleCall_1_1_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:4404:3: (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )?
            int alt133=2;
            int LA133_0 = input.LA(1);

            if ( (LA133_0==68) ) {
                alt133=1;
            }
            switch (alt133) {
                case 1 :
                    // InternalCqrsDsl.g:4405:4: otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) )
                    {
                    otherlv_3=(Token)match(input,68,FOLLOW_40); 

                    				newLeafNode(otherlv_3, grammarAccess.getTypeMetaInfoAccess().getLabelKeyword_2_0());
                    			
                    // InternalCqrsDsl.g:4409:4: ( (lv_label_4_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4410:5: (lv_label_4_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4410:5: (lv_label_4_0= RULE_STRING )
                    // InternalCqrsDsl.g:4411:6: lv_label_4_0= RULE_STRING
                    {
                    lv_label_4_0=(Token)match(input,RULE_STRING,FOLLOW_108); 

                    						newLeafNode(lv_label_4_0, grammarAccess.getTypeMetaInfoAccess().getLabelSTRINGTerminalRuleCall_2_1_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:4428:3: (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )?
            int alt134=2;
            int LA134_0 = input.LA(1);

            if ( (LA134_0==69) ) {
                alt134=1;
            }
            switch (alt134) {
                case 1 :
                    // InternalCqrsDsl.g:4429:4: otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) )
                    {
                    otherlv_5=(Token)match(input,69,FOLLOW_40); 

                    				newLeafNode(otherlv_5, grammarAccess.getTypeMetaInfoAccess().getTooltipKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:4433:4: ( (lv_tooltip_6_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4434:5: (lv_tooltip_6_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4434:5: (lv_tooltip_6_0= RULE_STRING )
                    // InternalCqrsDsl.g:4435:6: lv_tooltip_6_0= RULE_STRING
                    {
                    lv_tooltip_6_0=(Token)match(input,RULE_STRING,FOLLOW_109); 

                    						newLeafNode(lv_tooltip_6_0, grammarAccess.getTypeMetaInfoAccess().getTooltipSTRINGTerminalRuleCall_3_1_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:4452:3: (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )?
            int alt135=2;
            int LA135_0 = input.LA(1);

            if ( (LA135_0==70) ) {
                alt135=1;
            }
            switch (alt135) {
                case 1 :
                    // InternalCqrsDsl.g:4453:4: otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) )
                    {
                    otherlv_7=(Token)match(input,70,FOLLOW_40); 

                    				newLeafNode(otherlv_7, grammarAccess.getTypeMetaInfoAccess().getPromptKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:4457:4: ( (lv_prompt_8_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4458:5: (lv_prompt_8_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4458:5: (lv_prompt_8_0= RULE_STRING )
                    // InternalCqrsDsl.g:4459:6: lv_prompt_8_0= RULE_STRING
                    {
                    lv_prompt_8_0=(Token)match(input,RULE_STRING,FOLLOW_110); 

                    						newLeafNode(lv_prompt_8_0, grammarAccess.getTypeMetaInfoAccess().getPromptSTRINGTerminalRuleCall_4_1_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:4476:3: (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )?
            int alt137=2;
            int LA137_0 = input.LA(1);

            if ( (LA137_0==71) ) {
                alt137=1;
            }
            switch (alt137) {
                case 1 :
                    // InternalCqrsDsl.g:4477:4: otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )*
                    {
                    otherlv_9=(Token)match(input,71,FOLLOW_111); 

                    				newLeafNode(otherlv_9, grammarAccess.getTypeMetaInfoAccess().getExamplesKeyword_5_0());
                    			
                    // InternalCqrsDsl.g:4481:4: ( (lv_examples_10_0= ruleLiteral ) )*
                    loop136:
                    do {
                        int alt136=2;
                        int LA136_0 = input.LA(1);

                        if ( ((LA136_0>=RULE_INT && LA136_0<=RULE_DECIMAL)||(LA136_0>=102 && LA136_0<=104)) ) {
                            alt136=1;
                        }


                        switch (alt136) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:4482:5: (lv_examples_10_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:4482:5: (lv_examples_10_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:4483:6: lv_examples_10_0= ruleLiteral
                    	    {

                    	    						newCompositeNode(grammarAccess.getTypeMetaInfoAccess().getExamplesLiteralParserRuleCall_5_1_0());
                    	    					
                    	    pushFollow(FOLLOW_111);
                    	    lv_examples_10_0=ruleLiteral();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop136;
                        }
                    } while (true);


                    }
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4505:1: entryRuleGenericArgs returns [EObject current=null] : iv_ruleGenericArgs= ruleGenericArgs EOF ;
    public final EObject entryRuleGenericArgs() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGenericArgs = null;


        try {
            // InternalCqrsDsl.g:4505:52: (iv_ruleGenericArgs= ruleGenericArgs EOF )
            // InternalCqrsDsl.g:4506:2: iv_ruleGenericArgs= ruleGenericArgs EOF
            {
             newCompositeNode(grammarAccess.getGenericArgsRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGenericArgs=ruleGenericArgs();

            state._fsp--;

             current =iv_ruleGenericArgs; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4512:1: ruleGenericArgs returns [EObject current=null] : ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' ) ;
    public final EObject ruleGenericArgs() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:4518:2: ( ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' ) )
            // InternalCqrsDsl.g:4519:2: ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' )
            {
            // InternalCqrsDsl.g:4519:2: ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' )
            // InternalCqrsDsl.g:4520:3: (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>'
            {
            // InternalCqrsDsl.g:4520:3: (otherlv_0= '<' )+
            int cnt138=0;
            loop138:
            do {
                int alt138=2;
                int LA138_0 = input.LA(1);

                if ( (LA138_0==72) ) {
                    alt138=1;
                }


                switch (alt138) {
            	case 1 :
            	    // InternalCqrsDsl.g:4521:4: otherlv_0= '<'
            	    {
            	    otherlv_0=(Token)match(input,72,FOLLOW_112); 

            	    				newLeafNode(otherlv_0, grammarAccess.getGenericArgsAccess().getLessThanSignKeyword_0());
            	    			

            	    }
            	    break;

            	default :
            	    if ( cnt138 >= 1 ) break loop138;
                        EarlyExitException eee =
                            new EarlyExitException(138, input);
                        throw eee;
                }
                cnt138++;
            } while (true);

            // InternalCqrsDsl.g:4526:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4527:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4527:4: ( ruleFQN )
            // InternalCqrsDsl.g:4528:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getGenericArgsRule());
            					}
            				

            					newCompositeNode(grammarAccess.getGenericArgsAccess().getArgsTypeCrossReference_1_0());
            				
            pushFollow(FOLLOW_113);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4542:3: (otherlv_2= ',' ( ( ruleFQN ) ) )*
            loop139:
            do {
                int alt139=2;
                int LA139_0 = input.LA(1);

                if ( (LA139_0==30) ) {
                    alt139=1;
                }


                switch (alt139) {
            	case 1 :
            	    // InternalCqrsDsl.g:4543:4: otherlv_2= ',' ( ( ruleFQN ) )
            	    {
            	    otherlv_2=(Token)match(input,30,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getGenericArgsAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:4547:4: ( ( ruleFQN ) )
            	    // InternalCqrsDsl.g:4548:5: ( ruleFQN )
            	    {
            	    // InternalCqrsDsl.g:4548:5: ( ruleFQN )
            	    // InternalCqrsDsl.g:4549:6: ruleFQN
            	    {

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getGenericArgsRule());
            	    						}
            	    					

            	    						newCompositeNode(grammarAccess.getGenericArgsAccess().getArgsTypeCrossReference_2_1_0());
            	    					
            	    pushFollow(FOLLOW_113);
            	    ruleFQN();

            	    state._fsp--;


            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop139;
                }
            } while (true);

            otherlv_4=(Token)match(input,73,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getGenericArgsAccess().getGreaterThanSignKeyword_3());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4572:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCqrsDsl.g:4572:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCqrsDsl.g:4573:2: iv_ruleAttribute= ruleAttribute EOF
            {
             newCompositeNode(grammarAccess.getAttributeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAttribute=ruleAttribute();

            state._fsp--;

             current =iv_ruleAttribute; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4579:1: ruleAttribute returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) ;
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
            // InternalCqrsDsl.g:4585:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) )
            // InternalCqrsDsl.g:4586:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            {
            // InternalCqrsDsl.g:4586:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            // InternalCqrsDsl.g:4587:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            {
            // InternalCqrsDsl.g:4587:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt140=2;
            int LA140_0 = input.LA(1);

            if ( (LA140_0==RULE_DOC) ) {
                alt140=1;
            }
            switch (alt140) {
                case 1 :
                    // InternalCqrsDsl.g:4588:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4588:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4589:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_101); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getAttributeAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            // InternalCqrsDsl.g:4605:3: ( (lv_optional_1_0= 'optional' ) )?
            int alt141=2;
            int LA141_0 = input.LA(1);

            if ( (LA141_0==64) ) {
                alt141=1;
            }
            switch (alt141) {
                case 1 :
                    // InternalCqrsDsl.g:4606:4: (lv_optional_1_0= 'optional' )
                    {
                    // InternalCqrsDsl.g:4606:4: (lv_optional_1_0= 'optional' )
                    // InternalCqrsDsl.g:4607:5: lv_optional_1_0= 'optional'
                    {
                    lv_optional_1_0=(Token)match(input,64,FOLLOW_4); 

                    					newLeafNode(lv_optional_1_0, grammarAccess.getAttributeAccess().getOptionalOptionalKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getAttributeRule());
                    					}
                    					setWithLastConsumed(current, "optional", lv_optional_1_0, "optional");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4619:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4620:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4620:4: ( ruleFQN )
            // InternalCqrsDsl.g:4621:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeRule());
            					}
            				

            					newCompositeNode(grammarAccess.getAttributeAccess().getTypeTypeCrossReference_2_0());
            				
            pushFollow(FOLLOW_112);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4635:3: ( (lv_generics_3_0= ruleGenericArgs ) )?
            int alt142=2;
            int LA142_0 = input.LA(1);

            if ( (LA142_0==72) ) {
                alt142=1;
            }
            switch (alt142) {
                case 1 :
                    // InternalCqrsDsl.g:4636:4: (lv_generics_3_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:4636:4: (lv_generics_3_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:4637:5: lv_generics_3_0= ruleGenericArgs
                    {

                    					newCompositeNode(grammarAccess.getAttributeAccess().getGenericsGenericArgsParserRuleCall_3_0());
                    				
                    pushFollow(FOLLOW_4);
                    lv_generics_3_0=ruleGenericArgs();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4654:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCqrsDsl.g:4655:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4655:4: (lv_name_4_0= RULE_ID )
            // InternalCqrsDsl.g:4656:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_114); 

            					newLeafNode(lv_name_4_0, grammarAccess.getAttributeAccess().getNameIDTerminalRuleCall_4_0());
            				

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

            // InternalCqrsDsl.g:4672:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt143=2;
            int LA143_0 = input.LA(1);

            if ( (LA143_0==74) ) {
                alt143=1;
            }
            switch (alt143) {
                case 1 :
                    // InternalCqrsDsl.g:4673:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:4673:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:4674:5: lv_invariants_5_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getAttributeAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_115);
                    lv_invariants_5_0=ruleInvariants();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4691:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt144=2;
            int LA144_0 = input.LA(1);

            if ( (LA144_0==36) ) {
                alt144=1;
            }
            switch (alt144) {
                case 1 :
                    // InternalCqrsDsl.g:4692:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:4692:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:4693:5: lv_dataProtection_6_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getAttributeAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_28);
                    lv_dataProtection_6_0=ruleDataProtectionInstance();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4710:3: ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            int alt145=2;
            int LA145_0 = input.LA(1);

            if ( (LA145_0==14) ) {
                alt145=1;
            }
            switch (alt145) {
                case 1 :
                    // InternalCqrsDsl.g:4711:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    {
                    // InternalCqrsDsl.g:4711:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    // InternalCqrsDsl.g:4712:5: lv_overridden_7_0= ruleOverriddenTypeMetaInfo
                    {

                    					newCompositeNode(grammarAccess.getAttributeAccess().getOverriddenOverriddenTypeMetaInfoParserRuleCall_7_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_overridden_7_0=ruleOverriddenTypeMetaInfo();

                    state._fsp--;


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
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4733:1: entryRuleParameter returns [EObject current=null] : iv_ruleParameter= ruleParameter EOF ;
    public final EObject entryRuleParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameter = null;


        try {
            // InternalCqrsDsl.g:4733:50: (iv_ruleParameter= ruleParameter EOF )
            // InternalCqrsDsl.g:4734:2: iv_ruleParameter= ruleParameter EOF
            {
             newCompositeNode(grammarAccess.getParameterRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleParameter=ruleParameter();

            state._fsp--;

             current =iv_ruleParameter; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4740:1: ruleParameter returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) ;
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
            // InternalCqrsDsl.g:4746:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) )
            // InternalCqrsDsl.g:4747:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            {
            // InternalCqrsDsl.g:4747:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            // InternalCqrsDsl.g:4748:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_optional_1_0= 'optional' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            {
            // InternalCqrsDsl.g:4748:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt146=2;
            int LA146_0 = input.LA(1);

            if ( (LA146_0==RULE_DOC) ) {
                alt146=1;
            }
            switch (alt146) {
                case 1 :
                    // InternalCqrsDsl.g:4749:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4749:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4750:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_101); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getParameterAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            // InternalCqrsDsl.g:4766:3: ( (lv_optional_1_0= 'optional' ) )?
            int alt147=2;
            int LA147_0 = input.LA(1);

            if ( (LA147_0==64) ) {
                alt147=1;
            }
            switch (alt147) {
                case 1 :
                    // InternalCqrsDsl.g:4767:4: (lv_optional_1_0= 'optional' )
                    {
                    // InternalCqrsDsl.g:4767:4: (lv_optional_1_0= 'optional' )
                    // InternalCqrsDsl.g:4768:5: lv_optional_1_0= 'optional'
                    {
                    lv_optional_1_0=(Token)match(input,64,FOLLOW_4); 

                    					newLeafNode(lv_optional_1_0, grammarAccess.getParameterAccess().getOptionalOptionalKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getParameterRule());
                    					}
                    					setWithLastConsumed(current, "optional", lv_optional_1_0, "optional");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4780:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4781:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4781:4: ( ruleFQN )
            // InternalCqrsDsl.g:4782:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getParameterRule());
            					}
            				

            					newCompositeNode(grammarAccess.getParameterAccess().getTypeTypeCrossReference_2_0());
            				
            pushFollow(FOLLOW_112);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4796:3: ( (lv_generics_3_0= ruleGenericArgs ) )?
            int alt148=2;
            int LA148_0 = input.LA(1);

            if ( (LA148_0==72) ) {
                alt148=1;
            }
            switch (alt148) {
                case 1 :
                    // InternalCqrsDsl.g:4797:4: (lv_generics_3_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:4797:4: (lv_generics_3_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:4798:5: lv_generics_3_0= ruleGenericArgs
                    {

                    					newCompositeNode(grammarAccess.getParameterAccess().getGenericsGenericArgsParserRuleCall_3_0());
                    				
                    pushFollow(FOLLOW_4);
                    lv_generics_3_0=ruleGenericArgs();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4815:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCqrsDsl.g:4816:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4816:4: (lv_name_4_0= RULE_ID )
            // InternalCqrsDsl.g:4817:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_116); 

            					newLeafNode(lv_name_4_0, grammarAccess.getParameterAccess().getNameIDTerminalRuleCall_4_0());
            				

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

            // InternalCqrsDsl.g:4833:3: ( (lv_preconditions_5_0= rulePreconditions ) )?
            int alt149=2;
            int LA149_0 = input.LA(1);

            if ( (LA149_0==75) ) {
                alt149=1;
            }
            switch (alt149) {
                case 1 :
                    // InternalCqrsDsl.g:4834:4: (lv_preconditions_5_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:4834:4: (lv_preconditions_5_0= rulePreconditions )
                    // InternalCqrsDsl.g:4835:5: lv_preconditions_5_0= rulePreconditions
                    {

                    					newCompositeNode(grammarAccess.getParameterAccess().getPreconditionsPreconditionsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_117);
                    lv_preconditions_5_0=rulePreconditions();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4852:3: ( (lv_businessRules_6_0= ruleBusinessRules ) )?
            int alt150=2;
            int LA150_0 = input.LA(1);

            if ( (LA150_0==76) ) {
                alt150=1;
            }
            switch (alt150) {
                case 1 :
                    // InternalCqrsDsl.g:4853:4: (lv_businessRules_6_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:4853:4: (lv_businessRules_6_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:4854:5: lv_businessRules_6_0= ruleBusinessRules
                    {

                    					newCompositeNode(grammarAccess.getParameterAccess().getBusinessRulesBusinessRulesParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_28);
                    lv_businessRules_6_0=ruleBusinessRules();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:4871:3: ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            int alt151=2;
            int LA151_0 = input.LA(1);

            if ( (LA151_0==14) ) {
                alt151=1;
            }
            switch (alt151) {
                case 1 :
                    // InternalCqrsDsl.g:4872:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    {
                    // InternalCqrsDsl.g:4872:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    // InternalCqrsDsl.g:4873:5: lv_overridden_7_0= ruleOverriddenTypeMetaInfo
                    {

                    					newCompositeNode(grammarAccess.getParameterAccess().getOverriddenOverriddenTypeMetaInfoParserRuleCall_7_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_overridden_7_0=ruleOverriddenTypeMetaInfo();

                    state._fsp--;


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
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4894:1: entryRuleInvariants returns [EObject current=null] : iv_ruleInvariants= ruleInvariants EOF ;
    public final EObject entryRuleInvariants() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInvariants = null;


        try {
            // InternalCqrsDsl.g:4894:51: (iv_ruleInvariants= ruleInvariants EOF )
            // InternalCqrsDsl.g:4895:2: iv_ruleInvariants= ruleInvariants EOF
            {
             newCompositeNode(grammarAccess.getInvariantsRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInvariants=ruleInvariants();

            state._fsp--;

             current =iv_ruleInvariants; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4901:1: ruleInvariants returns [EObject current=null] : (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) ;
    public final EObject ruleInvariants() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_constraintInstances_1_0 = null;

        EObject lv_constraintInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4907:2: ( (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) )
            // InternalCqrsDsl.g:4908:2: (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            {
            // InternalCqrsDsl.g:4908:2: (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            // InternalCqrsDsl.g:4909:3: otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            {
            otherlv_0=(Token)match(input,74,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getInvariantsAccess().getInvariantsKeyword_0());
            		
            // InternalCqrsDsl.g:4913:3: ( (lv_constraintInstances_1_0= ruleConstraintInstance ) )
            // InternalCqrsDsl.g:4914:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            {
            // InternalCqrsDsl.g:4914:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            // InternalCqrsDsl.g:4915:5: lv_constraintInstances_1_0= ruleConstraintInstance
            {

            					newCompositeNode(grammarAccess.getInvariantsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_118);
            lv_constraintInstances_1_0=ruleConstraintInstance();

            state._fsp--;


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

            // InternalCqrsDsl.g:4932:3: (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            loop152:
            do {
                int alt152=2;
                int LA152_0 = input.LA(1);

                if ( (LA152_0==30) ) {
                    alt152=1;
                }


                switch (alt152) {
            	case 1 :
            	    // InternalCqrsDsl.g:4933:4: otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,30,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getInvariantsAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:4937:4: ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    // InternalCqrsDsl.g:4938:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    {
            	    // InternalCqrsDsl.g:4938:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    // InternalCqrsDsl.g:4939:6: lv_constraintInstances_3_0= ruleConstraintInstance
            	    {

            	    						newCompositeNode(grammarAccess.getInvariantsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_118);
            	    lv_constraintInstances_3_0=ruleConstraintInstance();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop152;
                }
            } while (true);


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:4961:1: entryRulePreconditions returns [EObject current=null] : iv_rulePreconditions= rulePreconditions EOF ;
    public final EObject entryRulePreconditions() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePreconditions = null;


        try {
            // InternalCqrsDsl.g:4961:54: (iv_rulePreconditions= rulePreconditions EOF )
            // InternalCqrsDsl.g:4962:2: iv_rulePreconditions= rulePreconditions EOF
            {
             newCompositeNode(grammarAccess.getPreconditionsRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePreconditions=rulePreconditions();

            state._fsp--;

             current =iv_rulePreconditions; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4968:1: rulePreconditions returns [EObject current=null] : (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) ;
    public final EObject rulePreconditions() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_constraintInstances_1_0 = null;

        EObject lv_constraintInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4974:2: ( (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) )
            // InternalCqrsDsl.g:4975:2: (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            {
            // InternalCqrsDsl.g:4975:2: (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            // InternalCqrsDsl.g:4976:3: otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            {
            otherlv_0=(Token)match(input,75,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getPreconditionsAccess().getPreconditionsKeyword_0());
            		
            // InternalCqrsDsl.g:4980:3: ( (lv_constraintInstances_1_0= ruleConstraintInstance ) )
            // InternalCqrsDsl.g:4981:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            {
            // InternalCqrsDsl.g:4981:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            // InternalCqrsDsl.g:4982:5: lv_constraintInstances_1_0= ruleConstraintInstance
            {

            					newCompositeNode(grammarAccess.getPreconditionsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_118);
            lv_constraintInstances_1_0=ruleConstraintInstance();

            state._fsp--;


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

            // InternalCqrsDsl.g:4999:3: (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            loop153:
            do {
                int alt153=2;
                int LA153_0 = input.LA(1);

                if ( (LA153_0==30) ) {
                    alt153=1;
                }


                switch (alt153) {
            	case 1 :
            	    // InternalCqrsDsl.g:5000:4: otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,30,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getPreconditionsAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:5004:4: ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    // InternalCqrsDsl.g:5005:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    {
            	    // InternalCqrsDsl.g:5005:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    // InternalCqrsDsl.g:5006:6: lv_constraintInstances_3_0= ruleConstraintInstance
            	    {

            	    						newCompositeNode(grammarAccess.getPreconditionsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_118);
            	    lv_constraintInstances_3_0=ruleConstraintInstance();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop153;
                }
            } while (true);


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5028:1: entryRuleBusinessRules returns [EObject current=null] : iv_ruleBusinessRules= ruleBusinessRules EOF ;
    public final EObject entryRuleBusinessRules() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRules = null;


        try {
            // InternalCqrsDsl.g:5028:54: (iv_ruleBusinessRules= ruleBusinessRules EOF )
            // InternalCqrsDsl.g:5029:2: iv_ruleBusinessRules= ruleBusinessRules EOF
            {
             newCompositeNode(grammarAccess.getBusinessRulesRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBusinessRules=ruleBusinessRules();

            state._fsp--;

             current =iv_ruleBusinessRules; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5035:1: ruleBusinessRules returns [EObject current=null] : (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* ) ;
    public final EObject ruleBusinessRules() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_businessRuleInstances_1_0 = null;

        EObject lv_businessRuleInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5041:2: ( (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* ) )
            // InternalCqrsDsl.g:5042:2: (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* )
            {
            // InternalCqrsDsl.g:5042:2: (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* )
            // InternalCqrsDsl.g:5043:3: otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )*
            {
            otherlv_0=(Token)match(input,76,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getBusinessRulesAccess().getBusinessRulesKeyword_0());
            		
            // InternalCqrsDsl.g:5047:3: ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) )
            // InternalCqrsDsl.g:5048:4: (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance )
            {
            // InternalCqrsDsl.g:5048:4: (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance )
            // InternalCqrsDsl.g:5049:5: lv_businessRuleInstances_1_0= ruleBusinessRuleInstance
            {

            					newCompositeNode(grammarAccess.getBusinessRulesAccess().getBusinessRuleInstancesBusinessRuleInstanceParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_118);
            lv_businessRuleInstances_1_0=ruleBusinessRuleInstance();

            state._fsp--;


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

            // InternalCqrsDsl.g:5066:3: (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )*
            loop154:
            do {
                int alt154=2;
                int LA154_0 = input.LA(1);

                if ( (LA154_0==30) ) {
                    alt154=1;
                }


                switch (alt154) {
            	case 1 :
            	    // InternalCqrsDsl.g:5067:4: otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,30,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getBusinessRulesAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:5071:4: ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) )
            	    // InternalCqrsDsl.g:5072:5: (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance )
            	    {
            	    // InternalCqrsDsl.g:5072:5: (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance )
            	    // InternalCqrsDsl.g:5073:6: lv_businessRuleInstances_3_0= ruleBusinessRuleInstance
            	    {

            	    						newCompositeNode(grammarAccess.getBusinessRulesAccess().getBusinessRuleInstancesBusinessRuleInstanceParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_118);
            	    lv_businessRuleInstances_3_0=ruleBusinessRuleInstance();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop154;
                }
            } while (true);


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5095:1: entryRuleOverriddenTypeMetaInfo returns [EObject current=null] : iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF ;
    public final EObject entryRuleOverriddenTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOverriddenTypeMetaInfo = null;


        try {
            // InternalCqrsDsl.g:5095:63: (iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF )
            // InternalCqrsDsl.g:5096:2: iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF
            {
             newCompositeNode(grammarAccess.getOverriddenTypeMetaInfoRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOverriddenTypeMetaInfo=ruleOverriddenTypeMetaInfo();

            state._fsp--;

             current =iv_ruleOverriddenTypeMetaInfo; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5102:1: ruleOverriddenTypeMetaInfo returns [EObject current=null] : (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' ) ;
    public final EObject ruleOverriddenTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_metaInfo_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5108:2: ( (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' ) )
            // InternalCqrsDsl.g:5109:2: (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' )
            {
            // InternalCqrsDsl.g:5109:2: (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' )
            // InternalCqrsDsl.g:5110:3: otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}'
            {
            otherlv_0=(Token)match(input,14,FOLLOW_119); 

            			newLeafNode(otherlv_0, grammarAccess.getOverriddenTypeMetaInfoAccess().getLeftCurlyBracketKeyword_0());
            		
            // InternalCqrsDsl.g:5114:3: ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:5115:4: (lv_metaInfo_1_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:5115:4: (lv_metaInfo_1_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:5116:5: lv_metaInfo_1_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getOverriddenTypeMetaInfoAccess().getMetaInfoTypeMetaInfoParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_30);
            lv_metaInfo_1_0=ruleTypeMetaInfo();

            state._fsp--;


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

            otherlv_2=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getOverriddenTypeMetaInfoAccess().getRightCurlyBracketKeyword_2());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5141:1: entryRuleConstraintInstance returns [EObject current=null] : iv_ruleConstraintInstance= ruleConstraintInstance EOF ;
    public final EObject entryRuleConstraintInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstraintInstance = null;


        try {
            // InternalCqrsDsl.g:5141:59: (iv_ruleConstraintInstance= ruleConstraintInstance EOF )
            // InternalCqrsDsl.g:5142:2: iv_ruleConstraintInstance= ruleConstraintInstance EOF
            {
             newCompositeNode(grammarAccess.getConstraintInstanceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConstraintInstance=ruleConstraintInstance();

            state._fsp--;

             current =iv_ruleConstraintInstance; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5148:1: ruleConstraintInstance returns [EObject current=null] : ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) ;
    public final EObject ruleConstraintInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_params_2_0 = null;

        EObject lv_params_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5154:2: ( ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) )
            // InternalCqrsDsl.g:5155:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            {
            // InternalCqrsDsl.g:5155:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            // InternalCqrsDsl.g:5156:3: ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            {
            // InternalCqrsDsl.g:5156:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5157:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5157:4: ( ruleFQN )
            // InternalCqrsDsl.g:5158:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getConstraintInstanceRule());
            					}
            				

            					newCompositeNode(grammarAccess.getConstraintInstanceAccess().getConstraintConstraintCrossReference_0_0());
            				
            pushFollow(FOLLOW_78);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:5172:3: (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            int alt156=2;
            int LA156_0 = input.LA(1);

            if ( (LA156_0==53) ) {
                alt156=1;
            }
            switch (alt156) {
                case 1 :
                    // InternalCqrsDsl.g:5173:4: otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')'
                    {
                    otherlv_1=(Token)match(input,53,FOLLOW_79); 

                    				newLeafNode(otherlv_1, grammarAccess.getConstraintInstanceAccess().getLeftParenthesisKeyword_1_0());
                    			
                    // InternalCqrsDsl.g:5177:4: ( (lv_params_2_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:5178:5: (lv_params_2_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:5178:5: (lv_params_2_0= ruleLiteral )
                    // InternalCqrsDsl.g:5179:6: lv_params_2_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getConstraintInstanceAccess().getParamsLiteralParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_80);
                    lv_params_2_0=ruleLiteral();

                    state._fsp--;


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

                    // InternalCqrsDsl.g:5196:4: (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )*
                    loop155:
                    do {
                        int alt155=2;
                        int LA155_0 = input.LA(1);

                        if ( (LA155_0==30) ) {
                            alt155=1;
                        }


                        switch (alt155) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5197:5: otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) )
                    	    {
                    	    otherlv_3=(Token)match(input,30,FOLLOW_79); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getConstraintInstanceAccess().getCommaKeyword_1_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5201:5: ( (lv_params_4_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:5202:6: (lv_params_4_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:5202:6: (lv_params_4_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:5203:7: lv_params_4_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getConstraintInstanceAccess().getParamsLiteralParserRuleCall_1_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_80);
                    	    lv_params_4_0=ruleLiteral();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop155;
                        }
                    } while (true);

                    otherlv_5=(Token)match(input,54,FOLLOW_2); 

                    				newLeafNode(otherlv_5, grammarAccess.getConstraintInstanceAccess().getRightParenthesisKeyword_1_3());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5230:1: entryRuleBusinessRuleInstance returns [EObject current=null] : iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF ;
    public final EObject entryRuleBusinessRuleInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRuleInstance = null;


        try {
            // InternalCqrsDsl.g:5230:61: (iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF )
            // InternalCqrsDsl.g:5231:2: iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF
            {
             newCompositeNode(grammarAccess.getBusinessRuleInstanceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBusinessRuleInstance=ruleBusinessRuleInstance();

            state._fsp--;

             current =iv_ruleBusinessRuleInstance; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5237:1: ruleBusinessRuleInstance returns [EObject current=null] : ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) ;
    public final EObject ruleBusinessRuleInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_params_2_0 = null;

        EObject lv_params_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5243:2: ( ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) )
            // InternalCqrsDsl.g:5244:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            {
            // InternalCqrsDsl.g:5244:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            // InternalCqrsDsl.g:5245:3: ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            {
            // InternalCqrsDsl.g:5245:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5246:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5246:4: ( ruleFQN )
            // InternalCqrsDsl.g:5247:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBusinessRuleInstanceRule());
            					}
            				

            					newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getBusinessRuleBusinessRuleCrossReference_0_0());
            				
            pushFollow(FOLLOW_78);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:5261:3: (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( (LA158_0==53) ) {
                alt158=1;
            }
            switch (alt158) {
                case 1 :
                    // InternalCqrsDsl.g:5262:4: otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')'
                    {
                    otherlv_1=(Token)match(input,53,FOLLOW_79); 

                    				newLeafNode(otherlv_1, grammarAccess.getBusinessRuleInstanceAccess().getLeftParenthesisKeyword_1_0());
                    			
                    // InternalCqrsDsl.g:5266:4: ( (lv_params_2_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:5267:5: (lv_params_2_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:5267:5: (lv_params_2_0= ruleLiteral )
                    // InternalCqrsDsl.g:5268:6: lv_params_2_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getParamsLiteralParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_80);
                    lv_params_2_0=ruleLiteral();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getBusinessRuleInstanceRule());
                    						}
                    						add(
                    							current,
                    							"params",
                    							lv_params_2_0,
                    							"org.fuin.dsl.cqrs.CqrsDsl.Literal");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:5285:4: (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )*
                    loop157:
                    do {
                        int alt157=2;
                        int LA157_0 = input.LA(1);

                        if ( (LA157_0==30) ) {
                            alt157=1;
                        }


                        switch (alt157) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5286:5: otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) )
                    	    {
                    	    otherlv_3=(Token)match(input,30,FOLLOW_79); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getBusinessRuleInstanceAccess().getCommaKeyword_1_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5290:5: ( (lv_params_4_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:5291:6: (lv_params_4_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:5291:6: (lv_params_4_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:5292:7: lv_params_4_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getParamsLiteralParserRuleCall_1_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_80);
                    	    lv_params_4_0=ruleLiteral();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getBusinessRuleInstanceRule());
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
                    	    break;

                    	default :
                    	    break loop157;
                        }
                    } while (true);

                    otherlv_5=(Token)match(input,54,FOLLOW_2); 

                    				newLeafNode(otherlv_5, grammarAccess.getBusinessRuleInstanceAccess().getRightParenthesisKeyword_1_3());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

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


    // $ANTLR start "entryRuleAnnotationInstance"
    // InternalCqrsDsl.g:5319:1: entryRuleAnnotationInstance returns [EObject current=null] : iv_ruleAnnotationInstance= ruleAnnotationInstance EOF ;
    public final EObject entryRuleAnnotationInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnnotationInstance = null;


        try {
            // InternalCqrsDsl.g:5319:59: (iv_ruleAnnotationInstance= ruleAnnotationInstance EOF )
            // InternalCqrsDsl.g:5320:2: iv_ruleAnnotationInstance= ruleAnnotationInstance EOF
            {
             newCompositeNode(grammarAccess.getAnnotationInstanceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAnnotationInstance=ruleAnnotationInstance();

            state._fsp--;

             current =iv_ruleAnnotationInstance; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5326:1: ruleAnnotationInstance returns [EObject current=null] : (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? ) ;
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
            // InternalCqrsDsl.g:5332:2: ( (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? ) )
            // InternalCqrsDsl.g:5333:2: (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? )
            {
            // InternalCqrsDsl.g:5333:2: (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? )
            // InternalCqrsDsl.g:5334:3: otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )?
            {
            otherlv_0=(Token)match(input,77,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getAnnotationInstanceAccess().getCommercialAtKeyword_0());
            		
            // InternalCqrsDsl.g:5338:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5339:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5339:4: ( ruleFQN )
            // InternalCqrsDsl.g:5340:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAnnotationInstanceRule());
            					}
            				

            					newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getAnnotationAnnotationCrossReference_1_0());
            				
            pushFollow(FOLLOW_78);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:5354:3: (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )?
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==53) ) {
                alt160=1;
            }
            switch (alt160) {
                case 1 :
                    // InternalCqrsDsl.g:5355:4: otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')'
                    {
                    otherlv_2=(Token)match(input,53,FOLLOW_79); 

                    				newLeafNode(otherlv_2, grammarAccess.getAnnotationInstanceAccess().getLeftParenthesisKeyword_2_0());
                    			
                    // InternalCqrsDsl.g:5359:4: ( (lv_params_3_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:5360:5: (lv_params_3_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:5360:5: (lv_params_3_0= ruleLiteral )
                    // InternalCqrsDsl.g:5361:6: lv_params_3_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getParamsLiteralParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_80);
                    lv_params_3_0=ruleLiteral();

                    state._fsp--;


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

                    // InternalCqrsDsl.g:5378:4: (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )*
                    loop159:
                    do {
                        int alt159=2;
                        int LA159_0 = input.LA(1);

                        if ( (LA159_0==30) ) {
                            alt159=1;
                        }


                        switch (alt159) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5379:5: otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) )
                    	    {
                    	    otherlv_4=(Token)match(input,30,FOLLOW_79); 

                    	    					newLeafNode(otherlv_4, grammarAccess.getAnnotationInstanceAccess().getCommaKeyword_2_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5383:5: ( (lv_params_5_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:5384:6: (lv_params_5_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:5384:6: (lv_params_5_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:5385:7: lv_params_5_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getParamsLiteralParserRuleCall_2_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_80);
                    	    lv_params_5_0=ruleLiteral();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop159;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,54,FOLLOW_2); 

                    				newLeafNode(otherlv_6, grammarAccess.getAnnotationInstanceAccess().getRightParenthesisKeyword_2_3());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5412:1: entryRuleService returns [EObject current=null] : iv_ruleService= ruleService EOF ;
    public final EObject entryRuleService() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleService = null;


        try {
            // InternalCqrsDsl.g:5412:48: (iv_ruleService= ruleService EOF )
            // InternalCqrsDsl.g:5413:2: iv_ruleService= ruleService EOF
            {
             newCompositeNode(grammarAccess.getServiceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleService=ruleService();

            state._fsp--;

             current =iv_ruleService; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5419:1: ruleService returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' ) ;
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
            // InternalCqrsDsl.g:5425:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' ) )
            // InternalCqrsDsl.g:5426:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' )
            {
            // InternalCqrsDsl.g:5426:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' )
            // InternalCqrsDsl.g:5427:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}'
            {
            // InternalCqrsDsl.g:5427:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt161=2;
            int LA161_0 = input.LA(1);

            if ( (LA161_0==RULE_DOC) ) {
                alt161=1;
            }
            switch (alt161) {
                case 1 :
                    // InternalCqrsDsl.g:5428:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5428:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5429:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_120); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getServiceAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,78,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getServiceAccess().getServiceKeyword_1());
            		
            // InternalCqrsDsl.g:5449:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5450:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5450:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5451:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_2_0, grammarAccess.getServiceAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            otherlv_3=(Token)match(input,14,FOLLOW_68); 

            			newLeafNode(otherlv_3, grammarAccess.getServiceAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalCqrsDsl.g:5471:3: ( (lv_businessRules_4_0= ruleBusinessRule ) )*
            loop162:
            do {
                int alt162=2;
                int LA162_0 = input.LA(1);

                if ( (LA162_0==RULE_DOC) ) {
                    int LA162_1 = input.LA(2);

                    if ( (LA162_1==42) ) {
                        alt162=1;
                    }


                }


                switch (alt162) {
            	case 1 :
            	    // InternalCqrsDsl.g:5472:4: (lv_businessRules_4_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:5472:4: (lv_businessRules_4_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:5473:5: lv_businessRules_4_0= ruleBusinessRule
            	    {

            	    					newCompositeNode(grammarAccess.getServiceAccess().getBusinessRulesBusinessRuleParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_68);
            	    lv_businessRules_4_0=ruleBusinessRule();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop162;
                }
            } while (true);

            // InternalCqrsDsl.g:5490:3: ( (lv_methods_5_0= ruleMethod ) )*
            loop163:
            do {
                int alt163=2;
                int LA163_0 = input.LA(1);

                if ( (LA163_0==RULE_DOC||LA163_0==65) ) {
                    alt163=1;
                }


                switch (alt163) {
            	case 1 :
            	    // InternalCqrsDsl.g:5491:4: (lv_methods_5_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:5491:4: (lv_methods_5_0= ruleMethod )
            	    // InternalCqrsDsl.g:5492:5: lv_methods_5_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getServiceAccess().getMethodsMethodParserRuleCall_5_0());
            	    				
            	    pushFollow(FOLLOW_68);
            	    lv_methods_5_0=ruleMethod();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop163;
                }
            } while (true);

            otherlv_6=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getServiceAccess().getRightCurlyBracketKeyword_6());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5517:1: entryRuleCommand returns [EObject current=null] : iv_ruleCommand= ruleCommand EOF ;
    public final EObject entryRuleCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommand = null;


        try {
            // InternalCqrsDsl.g:5517:48: (iv_ruleCommand= ruleCommand EOF )
            // InternalCqrsDsl.g:5518:2: iv_ruleCommand= ruleCommand EOF
            {
             newCompositeNode(grammarAccess.getCommandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCommand=ruleCommand();

            state._fsp--;

             current =iv_ruleCommand; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5524:1: ruleCommand returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' ) ;
    public final EObject ruleCommand() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        Token lv_message_10_0=null;
        Token otherlv_11=null;
        EObject lv_acceptable_6_0 = null;

        EObject lv_attributes_8_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5530:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' ) )
            // InternalCqrsDsl.g:5531:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' )
            {
            // InternalCqrsDsl.g:5531:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' )
            // InternalCqrsDsl.g:5532:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}'
            {
            // InternalCqrsDsl.g:5532:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt164=2;
            int LA164_0 = input.LA(1);

            if ( (LA164_0==RULE_DOC) ) {
                alt164=1;
            }
            switch (alt164) {
                case 1 :
                    // InternalCqrsDsl.g:5533:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5533:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5534:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_121); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getCommandAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,79,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getCommandAccess().getCommandKeyword_1());
            		
            // InternalCqrsDsl.g:5554:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5555:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5555:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5556:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_122); 

            					newLeafNode(lv_name_2_0, grammarAccess.getCommandAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:5572:3: (otherlv_3= 'target' ( ( ruleFQN ) ) )?
            int alt165=2;
            int LA165_0 = input.LA(1);

            if ( (LA165_0==80) ) {
                alt165=1;
            }
            switch (alt165) {
                case 1 :
                    // InternalCqrsDsl.g:5573:4: otherlv_3= 'target' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,80,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getCommandAccess().getTargetKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:5577:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5578:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5578:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5579:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCommandRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getCommandAccess().getTargetAbstractMethodCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_123);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5594:3: (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )?
            int alt166=2;
            int LA166_0 = input.LA(1);

            if ( (LA166_0==81) ) {
                alt166=1;
            }
            switch (alt166) {
                case 1 :
                    // InternalCqrsDsl.g:5595:4: otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) )
                    {
                    otherlv_5=(Token)match(input,81,FOLLOW_17); 

                    				newLeafNode(otherlv_5, grammarAccess.getCommandAccess().getSlaKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:5599:4: ( (lv_acceptable_6_0= ruleDuration ) )
                    // InternalCqrsDsl.g:5600:5: (lv_acceptable_6_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:5600:5: (lv_acceptable_6_0= ruleDuration )
                    // InternalCqrsDsl.g:5601:6: lv_acceptable_6_0= ruleDuration
                    {

                    						newCompositeNode(grammarAccess.getCommandAccess().getAcceptableDurationParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_5);
                    lv_acceptable_6_0=ruleDuration();

                    state._fsp--;


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
                    break;

            }

            otherlv_7=(Token)match(input,14,FOLLOW_53); 

            			newLeafNode(otherlv_7, grammarAccess.getCommandAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:5623:3: ( (lv_attributes_8_0= ruleAttribute ) )*
            loop167:
            do {
                int alt167=2;
                int LA167_0 = input.LA(1);

                if ( ((LA167_0>=RULE_DOC && LA167_0<=RULE_ID)||LA167_0==64) ) {
                    alt167=1;
                }


                switch (alt167) {
            	case 1 :
            	    // InternalCqrsDsl.g:5624:4: (lv_attributes_8_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:5624:4: (lv_attributes_8_0= ruleAttribute )
            	    // InternalCqrsDsl.g:5625:5: lv_attributes_8_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getCommandAccess().getAttributesAttributeParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_53);
            	    lv_attributes_8_0=ruleAttribute();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getCommandRule());
            	    					}
            	    					add(
            	    						current,
            	    						"attributes",
            	    						lv_attributes_8_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Attribute");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop167;
                }
            } while (true);

            // InternalCqrsDsl.g:5642:3: (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )?
            int alt168=2;
            int LA168_0 = input.LA(1);

            if ( (LA168_0==41) ) {
                alt168=1;
            }
            switch (alt168) {
                case 1 :
                    // InternalCqrsDsl.g:5643:4: otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) )
                    {
                    otherlv_9=(Token)match(input,41,FOLLOW_40); 

                    				newLeafNode(otherlv_9, grammarAccess.getCommandAccess().getMessageKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:5647:4: ( (lv_message_10_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:5648:5: (lv_message_10_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:5648:5: (lv_message_10_0= RULE_STRING )
                    // InternalCqrsDsl.g:5649:6: lv_message_10_0= RULE_STRING
                    {
                    lv_message_10_0=(Token)match(input,RULE_STRING,FOLLOW_30); 

                    						newLeafNode(lv_message_10_0, grammarAccess.getCommandAccess().getMessageSTRINGTerminalRuleCall_7_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCommandRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"message",
                    							lv_message_10_0,
                    							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_11=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_11, grammarAccess.getCommandAccess().getRightCurlyBracketKeyword_8());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5674:1: entryRuleCommandHandler returns [EObject current=null] : iv_ruleCommandHandler= ruleCommandHandler EOF ;
    public final EObject entryRuleCommandHandler() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommandHandler = null;


        try {
            // InternalCqrsDsl.g:5674:55: (iv_ruleCommandHandler= ruleCommandHandler EOF )
            // InternalCqrsDsl.g:5675:2: iv_ruleCommandHandler= ruleCommandHandler EOF
            {
             newCompositeNode(grammarAccess.getCommandHandlerRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCommandHandler=ruleCommandHandler();

            state._fsp--;

             current =iv_ruleCommandHandler; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5681:1: ruleCommandHandler returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? ) ;
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
            // InternalCqrsDsl.g:5687:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? ) )
            // InternalCqrsDsl.g:5688:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? )
            {
            // InternalCqrsDsl.g:5688:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? )
            // InternalCqrsDsl.g:5689:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            {
            // InternalCqrsDsl.g:5689:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt169=2;
            int LA169_0 = input.LA(1);

            if ( (LA169_0==RULE_DOC) ) {
                alt169=1;
            }
            switch (alt169) {
                case 1 :
                    // InternalCqrsDsl.g:5690:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5690:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5691:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_124); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getCommandHandlerAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,82,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getCommandHandlerAccess().getCommandHandlerKeyword_1());
            		
            // InternalCqrsDsl.g:5711:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5712:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5712:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5713:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_125); 

            					newLeafNode(lv_name_2_0, grammarAccess.getCommandHandlerAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            otherlv_3=(Token)match(input,83,FOLLOW_4); 

            			newLeafNode(otherlv_3, grammarAccess.getCommandHandlerAccess().getHandlesKeyword_3());
            		
            // InternalCqrsDsl.g:5733:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5734:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5734:4: ( ruleFQN )
            // InternalCqrsDsl.g:5735:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCommandHandlerRule());
            					}
            				

            					newCompositeNode(grammarAccess.getCommandHandlerAccess().getCommandsCommandCrossReference_4_0());
            				
            pushFollow(FOLLOW_126);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:5749:3: (otherlv_5= ',' ( ( ruleFQN ) ) )*
            loop170:
            do {
                int alt170=2;
                int LA170_0 = input.LA(1);

                if ( (LA170_0==30) ) {
                    alt170=1;
                }


                switch (alt170) {
            	case 1 :
            	    // InternalCqrsDsl.g:5750:4: otherlv_5= ',' ( ( ruleFQN ) )
            	    {
            	    otherlv_5=(Token)match(input,30,FOLLOW_4); 

            	    				newLeafNode(otherlv_5, grammarAccess.getCommandHandlerAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCqrsDsl.g:5754:4: ( ( ruleFQN ) )
            	    // InternalCqrsDsl.g:5755:5: ( ruleFQN )
            	    {
            	    // InternalCqrsDsl.g:5755:5: ( ruleFQN )
            	    // InternalCqrsDsl.g:5756:6: ruleFQN
            	    {

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getCommandHandlerRule());
            	    						}
            	    					

            	    						newCompositeNode(grammarAccess.getCommandHandlerAccess().getCommandsCommandCrossReference_5_1_0());
            	    					
            	    pushFollow(FOLLOW_126);
            	    ruleFQN();

            	    state._fsp--;


            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop170;
                }
            } while (true);

            // InternalCqrsDsl.g:5771:3: (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            int alt172=2;
            int LA172_0 = input.LA(1);

            if ( (LA172_0==84) ) {
                alt172=1;
            }
            switch (alt172) {
                case 1 :
                    // InternalCqrsDsl.g:5772:4: otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_7=(Token)match(input,84,FOLLOW_4); 

                    				newLeafNode(otherlv_7, grammarAccess.getCommandHandlerAccess().getUsesKeyword_6_0());
                    			
                    // InternalCqrsDsl.g:5776:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5777:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5777:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5778:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCommandHandlerRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getCommandHandlerAccess().getAggregatesAggregateCrossReference_6_1_0());
                    					
                    pushFollow(FOLLOW_118);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:5792:4: (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    loop171:
                    do {
                        int alt171=2;
                        int LA171_0 = input.LA(1);

                        if ( (LA171_0==30) ) {
                            alt171=1;
                        }


                        switch (alt171) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5793:5: otherlv_9= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_9=(Token)match(input,30,FOLLOW_4); 

                    	    					newLeafNode(otherlv_9, grammarAccess.getCommandHandlerAccess().getCommaKeyword_6_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5797:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:5798:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:5798:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:5799:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getCommandHandlerRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getCommandHandlerAccess().getAggregatesAggregateCrossReference_6_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_118);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop171;
                        }
                    } while (true);


                    }
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5819:1: entryRuleProjection returns [EObject current=null] : iv_ruleProjection= ruleProjection EOF ;
    public final EObject entryRuleProjection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProjection = null;


        try {
            // InternalCqrsDsl.g:5819:51: (iv_ruleProjection= ruleProjection EOF )
            // InternalCqrsDsl.g:5820:2: iv_ruleProjection= ruleProjection EOF
            {
             newCompositeNode(grammarAccess.getProjectionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleProjection=ruleProjection();

            state._fsp--;

             current =iv_ruleProjection; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5826:1: ruleProjection returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? ) ;
    public final EObject ruleProjection() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5832:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? ) )
            // InternalCqrsDsl.g:5833:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? )
            {
            // InternalCqrsDsl.g:5833:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? )
            // InternalCqrsDsl.g:5834:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )?
            {
            // InternalCqrsDsl.g:5834:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt173=2;
            int LA173_0 = input.LA(1);

            if ( (LA173_0==RULE_DOC) ) {
                alt173=1;
            }
            switch (alt173) {
                case 1 :
                    // InternalCqrsDsl.g:5835:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5835:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5836:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_127); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getProjectionAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,85,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getProjectionAccess().getProjectionKeyword_1());
            		
            // InternalCqrsDsl.g:5856:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5857:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5857:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5858:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_128); 

            					newLeafNode(lv_name_2_0, grammarAccess.getProjectionAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            // InternalCqrsDsl.g:5874:3: (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )?
            int alt175=2;
            int LA175_0 = input.LA(1);

            if ( (LA175_0==38) ) {
                alt175=1;
            }
            switch (alt175) {
                case 1 :
                    // InternalCqrsDsl.g:5875:4: otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_3=(Token)match(input,38,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getProjectionAccess().getInputKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:5879:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5880:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5880:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5881:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getProjectionRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getProjectionAccess().getEventsEventCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_118);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:5895:4: (otherlv_5= ',' ( ( ruleFQN ) ) )*
                    loop174:
                    do {
                        int alt174=2;
                        int LA174_0 = input.LA(1);

                        if ( (LA174_0==30) ) {
                            alt174=1;
                        }


                        switch (alt174) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5896:5: otherlv_5= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_5=(Token)match(input,30,FOLLOW_4); 

                    	    					newLeafNode(otherlv_5, grammarAccess.getProjectionAccess().getCommaKeyword_3_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5900:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:5901:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:5901:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:5902:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getProjectionRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getProjectionAccess().getEventsEventCrossReference_3_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_118);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop174;
                        }
                    } while (true);


                    }
                    break;

            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:5922:1: entryRuleView returns [EObject current=null] : iv_ruleView= ruleView EOF ;
    public final EObject entryRuleView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleView = null;


        try {
            // InternalCqrsDsl.g:5922:45: (iv_ruleView= ruleView EOF )
            // InternalCqrsDsl.g:5923:2: iv_ruleView= ruleView EOF
            {
             newCompositeNode(grammarAccess.getViewRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleView=ruleView();

            state._fsp--;

             current =iv_ruleView; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5929:1: ruleView returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) otherlv_5= '{' (otherlv_6= 'rest-path' ( (lv_restPath_7_0= RULE_STRING ) ) )? (otherlv_8= 'cron-schedule' ( (lv_cron_9_0= RULE_STRING ) ) )? ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_methods_11_0= ruleMethod ) )* otherlv_12= '}' ) ;
    public final EObject ruleView() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token lv_restPath_7_0=null;
        Token otherlv_8=null;
        Token lv_cron_9_0=null;
        Token otherlv_12=null;
        EObject lv_businessRules_10_0 = null;

        EObject lv_methods_11_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5935:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) otherlv_5= '{' (otherlv_6= 'rest-path' ( (lv_restPath_7_0= RULE_STRING ) ) )? (otherlv_8= 'cron-schedule' ( (lv_cron_9_0= RULE_STRING ) ) )? ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_methods_11_0= ruleMethod ) )* otherlv_12= '}' ) )
            // InternalCqrsDsl.g:5936:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) otherlv_5= '{' (otherlv_6= 'rest-path' ( (lv_restPath_7_0= RULE_STRING ) ) )? (otherlv_8= 'cron-schedule' ( (lv_cron_9_0= RULE_STRING ) ) )? ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_methods_11_0= ruleMethod ) )* otherlv_12= '}' )
            {
            // InternalCqrsDsl.g:5936:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) otherlv_5= '{' (otherlv_6= 'rest-path' ( (lv_restPath_7_0= RULE_STRING ) ) )? (otherlv_8= 'cron-schedule' ( (lv_cron_9_0= RULE_STRING ) ) )? ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_methods_11_0= ruleMethod ) )* otherlv_12= '}' )
            // InternalCqrsDsl.g:5937:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( ( ruleFQN ) ) otherlv_5= '{' (otherlv_6= 'rest-path' ( (lv_restPath_7_0= RULE_STRING ) ) )? (otherlv_8= 'cron-schedule' ( (lv_cron_9_0= RULE_STRING ) ) )? ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_methods_11_0= ruleMethod ) )* otherlv_12= '}'
            {
            // InternalCqrsDsl.g:5937:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt176=2;
            int LA176_0 = input.LA(1);

            if ( (LA176_0==RULE_DOC) ) {
                alt176=1;
            }
            switch (alt176) {
                case 1 :
                    // InternalCqrsDsl.g:5938:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5938:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5939:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_129); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getViewAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,86,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getViewAccess().getViewKeyword_1());
            		
            // InternalCqrsDsl.g:5959:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5960:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5960:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5961:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_130); 

            					newLeafNode(lv_name_2_0, grammarAccess.getViewAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            otherlv_3=(Token)match(input,84,FOLLOW_4); 

            			newLeafNode(otherlv_3, grammarAccess.getViewAccess().getUsesKeyword_3());
            		
            // InternalCqrsDsl.g:5981:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5982:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5982:4: ( ruleFQN )
            // InternalCqrsDsl.g:5983:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getViewRule());
            					}
            				

            					newCompositeNode(grammarAccess.getViewAccess().getProjectionProjectionCrossReference_4_0());
            				
            pushFollow(FOLLOW_5);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_5=(Token)match(input,14,FOLLOW_131); 

            			newLeafNode(otherlv_5, grammarAccess.getViewAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:6001:3: (otherlv_6= 'rest-path' ( (lv_restPath_7_0= RULE_STRING ) ) )?
            int alt177=2;
            int LA177_0 = input.LA(1);

            if ( (LA177_0==87) ) {
                alt177=1;
            }
            switch (alt177) {
                case 1 :
                    // InternalCqrsDsl.g:6002:4: otherlv_6= 'rest-path' ( (lv_restPath_7_0= RULE_STRING ) )
                    {
                    otherlv_6=(Token)match(input,87,FOLLOW_40); 

                    				newLeafNode(otherlv_6, grammarAccess.getViewAccess().getRestPathKeyword_6_0());
                    			
                    // InternalCqrsDsl.g:6006:4: ( (lv_restPath_7_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:6007:5: (lv_restPath_7_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:6007:5: (lv_restPath_7_0= RULE_STRING )
                    // InternalCqrsDsl.g:6008:6: lv_restPath_7_0= RULE_STRING
                    {
                    lv_restPath_7_0=(Token)match(input,RULE_STRING,FOLLOW_132); 

                    						newLeafNode(lv_restPath_7_0, grammarAccess.getViewAccess().getRestPathSTRINGTerminalRuleCall_6_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getViewRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"restPath",
                    							lv_restPath_7_0,
                    							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:6025:3: (otherlv_8= 'cron-schedule' ( (lv_cron_9_0= RULE_STRING ) ) )?
            int alt178=2;
            int LA178_0 = input.LA(1);

            if ( (LA178_0==88) ) {
                alt178=1;
            }
            switch (alt178) {
                case 1 :
                    // InternalCqrsDsl.g:6026:4: otherlv_8= 'cron-schedule' ( (lv_cron_9_0= RULE_STRING ) )
                    {
                    otherlv_8=(Token)match(input,88,FOLLOW_40); 

                    				newLeafNode(otherlv_8, grammarAccess.getViewAccess().getCronScheduleKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:6030:4: ( (lv_cron_9_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:6031:5: (lv_cron_9_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:6031:5: (lv_cron_9_0= RULE_STRING )
                    // InternalCqrsDsl.g:6032:6: lv_cron_9_0= RULE_STRING
                    {
                    lv_cron_9_0=(Token)match(input,RULE_STRING,FOLLOW_68); 

                    						newLeafNode(lv_cron_9_0, grammarAccess.getViewAccess().getCronSTRINGTerminalRuleCall_7_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getViewRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"cron",
                    							lv_cron_9_0,
                    							"org.fuin.dsl.cqrs.CqrsDsl.STRING");
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:6049:3: ( (lv_businessRules_10_0= ruleBusinessRule ) )*
            loop179:
            do {
                int alt179=2;
                int LA179_0 = input.LA(1);

                if ( (LA179_0==RULE_DOC) ) {
                    int LA179_1 = input.LA(2);

                    if ( (LA179_1==42) ) {
                        alt179=1;
                    }


                }


                switch (alt179) {
            	case 1 :
            	    // InternalCqrsDsl.g:6050:4: (lv_businessRules_10_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:6050:4: (lv_businessRules_10_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:6051:5: lv_businessRules_10_0= ruleBusinessRule
            	    {

            	    					newCompositeNode(grammarAccess.getViewAccess().getBusinessRulesBusinessRuleParserRuleCall_8_0());
            	    				
            	    pushFollow(FOLLOW_68);
            	    lv_businessRules_10_0=ruleBusinessRule();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getViewRule());
            	    					}
            	    					add(
            	    						current,
            	    						"businessRules",
            	    						lv_businessRules_10_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.BusinessRule");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop179;
                }
            } while (true);

            // InternalCqrsDsl.g:6068:3: ( (lv_methods_11_0= ruleMethod ) )*
            loop180:
            do {
                int alt180=2;
                int LA180_0 = input.LA(1);

                if ( (LA180_0==RULE_DOC||LA180_0==65) ) {
                    alt180=1;
                }


                switch (alt180) {
            	case 1 :
            	    // InternalCqrsDsl.g:6069:4: (lv_methods_11_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:6069:4: (lv_methods_11_0= ruleMethod )
            	    // InternalCqrsDsl.g:6070:5: lv_methods_11_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getViewAccess().getMethodsMethodParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_68);
            	    lv_methods_11_0=ruleMethod();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getViewRule());
            	    					}
            	    					add(
            	    						current,
            	    						"methods",
            	    						lv_methods_11_0,
            	    						"org.fuin.dsl.cqrs.CqrsDsl.Method");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop180;
                }
            } while (true);

            otherlv_12=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_12, grammarAccess.getViewAccess().getRightCurlyBracketKeyword_10());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6095:1: entryRuleProcessManager returns [EObject current=null] : iv_ruleProcessManager= ruleProcessManager EOF ;
    public final EObject entryRuleProcessManager() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProcessManager = null;


        try {
            // InternalCqrsDsl.g:6095:55: (iv_ruleProcessManager= ruleProcessManager EOF )
            // InternalCqrsDsl.g:6096:2: iv_ruleProcessManager= ruleProcessManager EOF
            {
             newCompositeNode(grammarAccess.getProcessManagerRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleProcessManager=ruleProcessManager();

            state._fsp--;

             current =iv_ruleProcessManager; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6102:1: ruleProcessManager returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'correlation-id' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' ) ;
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
            // InternalCqrsDsl.g:6108:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'correlation-id' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' ) )
            // InternalCqrsDsl.g:6109:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'correlation-id' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:6109:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'correlation-id' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}' )
            // InternalCqrsDsl.g:6110:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'process-manager' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )? (otherlv_6= 'correlation-id' ( ( ruleFQN ) ) )? (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )? ( (lv_reactions_12_0= ruleProcessReaction ) )* otherlv_13= '}'
            {
            // InternalCqrsDsl.g:6110:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt181=2;
            int LA181_0 = input.LA(1);

            if ( (LA181_0==RULE_DOC) ) {
                alt181=1;
            }
            switch (alt181) {
                case 1 :
                    // InternalCqrsDsl.g:6111:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:6111:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:6112:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_133); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getProcessManagerAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,89,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getProcessManagerAccess().getProcessManagerKeyword_1());
            		
            // InternalCqrsDsl.g:6132:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:6133:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:6133:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:6134:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_2_0, grammarAccess.getProcessManagerAccess().getNameIDTerminalRuleCall_2_0());
            				

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

            otherlv_3=(Token)match(input,14,FOLLOW_134); 

            			newLeafNode(otherlv_3, grammarAccess.getProcessManagerAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalCqrsDsl.g:6154:3: (otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) ) )?
            int alt182=2;
            int LA182_0 = input.LA(1);

            if ( (LA182_0==88) ) {
                alt182=1;
            }
            switch (alt182) {
                case 1 :
                    // InternalCqrsDsl.g:6155:4: otherlv_4= 'cron-schedule' ( (lv_cron_5_0= RULE_STRING ) )
                    {
                    otherlv_4=(Token)match(input,88,FOLLOW_40); 

                    				newLeafNode(otherlv_4, grammarAccess.getProcessManagerAccess().getCronScheduleKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:6159:4: ( (lv_cron_5_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:6160:5: (lv_cron_5_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:6160:5: (lv_cron_5_0= RULE_STRING )
                    // InternalCqrsDsl.g:6161:6: lv_cron_5_0= RULE_STRING
                    {
                    lv_cron_5_0=(Token)match(input,RULE_STRING,FOLLOW_135); 

                    						newLeafNode(lv_cron_5_0, grammarAccess.getProcessManagerAccess().getCronSTRINGTerminalRuleCall_4_1_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:6178:3: (otherlv_6= 'correlation-id' ( ( ruleFQN ) ) )?
            int alt183=2;
            int LA183_0 = input.LA(1);

            if ( (LA183_0==90) ) {
                alt183=1;
            }
            switch (alt183) {
                case 1 :
                    // InternalCqrsDsl.g:6179:4: otherlv_6= 'correlation-id' ( ( ruleFQN ) )
                    {
                    otherlv_6=(Token)match(input,90,FOLLOW_4); 

                    				newLeafNode(otherlv_6, grammarAccess.getProcessManagerAccess().getCorrelationIdKeyword_5_0());
                    			
                    // InternalCqrsDsl.g:6183:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:6184:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:6184:5: ( ruleFQN )
                    // InternalCqrsDsl.g:6185:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getProcessManagerRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getProcessManagerAccess().getCorrelationIdTypeCrossReference_5_1_0());
                    					
                    pushFollow(FOLLOW_136);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:6200:3: (otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}' )?
            int alt185=2;
            int LA185_0 = input.LA(1);

            if ( (LA185_0==91) ) {
                alt185=1;
            }
            switch (alt185) {
                case 1 :
                    // InternalCqrsDsl.g:6201:4: otherlv_8= 'process-states' otherlv_9= '{' ( (lv_states_10_0= ruleProcessState ) )+ otherlv_11= '}'
                    {
                    otherlv_8=(Token)match(input,91,FOLLOW_5); 

                    				newLeafNode(otherlv_8, grammarAccess.getProcessManagerAccess().getProcessStatesKeyword_6_0());
                    			
                    otherlv_9=(Token)match(input,14,FOLLOW_137); 

                    				newLeafNode(otherlv_9, grammarAccess.getProcessManagerAccess().getLeftCurlyBracketKeyword_6_1());
                    			
                    // InternalCqrsDsl.g:6209:4: ( (lv_states_10_0= ruleProcessState ) )+
                    int cnt184=0;
                    loop184:
                    do {
                        int alt184=2;
                        int LA184_0 = input.LA(1);

                        if ( ((LA184_0>=RULE_DOC && LA184_0<=RULE_ID)) ) {
                            alt184=1;
                        }


                        switch (alt184) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6210:5: (lv_states_10_0= ruleProcessState )
                    	    {
                    	    // InternalCqrsDsl.g:6210:5: (lv_states_10_0= ruleProcessState )
                    	    // InternalCqrsDsl.g:6211:6: lv_states_10_0= ruleProcessState
                    	    {

                    	    						newCompositeNode(grammarAccess.getProcessManagerAccess().getStatesProcessStateParserRuleCall_6_2_0());
                    	    					
                    	    pushFollow(FOLLOW_138);
                    	    lv_states_10_0=ruleProcessState();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    if ( cnt184 >= 1 ) break loop184;
                                EarlyExitException eee =
                                    new EarlyExitException(184, input);
                                throw eee;
                        }
                        cnt184++;
                    } while (true);

                    otherlv_11=(Token)match(input,15,FOLLOW_139); 

                    				newLeafNode(otherlv_11, grammarAccess.getProcessManagerAccess().getRightCurlyBracketKeyword_6_3());
                    			

                    }
                    break;

            }

            // InternalCqrsDsl.g:6233:3: ( (lv_reactions_12_0= ruleProcessReaction ) )*
            loop186:
            do {
                int alt186=2;
                int LA186_0 = input.LA(1);

                if ( (LA186_0==RULE_DOC||LA186_0==92) ) {
                    alt186=1;
                }


                switch (alt186) {
            	case 1 :
            	    // InternalCqrsDsl.g:6234:4: (lv_reactions_12_0= ruleProcessReaction )
            	    {
            	    // InternalCqrsDsl.g:6234:4: (lv_reactions_12_0= ruleProcessReaction )
            	    // InternalCqrsDsl.g:6235:5: lv_reactions_12_0= ruleProcessReaction
            	    {

            	    					newCompositeNode(grammarAccess.getProcessManagerAccess().getReactionsProcessReactionParserRuleCall_7_0());
            	    				
            	    pushFollow(FOLLOW_139);
            	    lv_reactions_12_0=ruleProcessReaction();

            	    state._fsp--;


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
            	    break;

            	default :
            	    break loop186;
                }
            } while (true);

            otherlv_13=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_13, grammarAccess.getProcessManagerAccess().getRightCurlyBracketKeyword_8());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6260:1: entryRuleProcessState returns [EObject current=null] : iv_ruleProcessState= ruleProcessState EOF ;
    public final EObject entryRuleProcessState() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProcessState = null;


        try {
            // InternalCqrsDsl.g:6260:53: (iv_ruleProcessState= ruleProcessState EOF )
            // InternalCqrsDsl.g:6261:2: iv_ruleProcessState= ruleProcessState EOF
            {
             newCompositeNode(grammarAccess.getProcessStateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleProcessState=ruleProcessState();

            state._fsp--;

             current =iv_ruleProcessState; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6267:1: ruleProcessState returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleProcessState() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token lv_name_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6273:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalCqrsDsl.g:6274:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalCqrsDsl.g:6274:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) ) )
            // InternalCqrsDsl.g:6275:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_name_1_0= RULE_ID ) )
            {
            // InternalCqrsDsl.g:6275:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt187=2;
            int LA187_0 = input.LA(1);

            if ( (LA187_0==RULE_DOC) ) {
                alt187=1;
            }
            switch (alt187) {
                case 1 :
                    // InternalCqrsDsl.g:6276:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:6276:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:6277:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_4); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getProcessStateAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            // InternalCqrsDsl.g:6293:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalCqrsDsl.g:6294:4: (lv_name_1_0= RULE_ID )
            {
            // InternalCqrsDsl.g:6294:4: (lv_name_1_0= RULE_ID )
            // InternalCqrsDsl.g:6295:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_1_0, grammarAccess.getProcessStateAccess().getNameIDTerminalRuleCall_1_0());
            				

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


            	leaveRule();

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
    // InternalCqrsDsl.g:6315:1: entryRuleProcessReaction returns [EObject current=null] : iv_ruleProcessReaction= ruleProcessReaction EOF ;
    public final EObject entryRuleProcessReaction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProcessReaction = null;


        try {
            // InternalCqrsDsl.g:6315:56: (iv_ruleProcessReaction= ruleProcessReaction EOF )
            // InternalCqrsDsl.g:6316:2: iv_ruleProcessReaction= ruleProcessReaction EOF
            {
             newCompositeNode(grammarAccess.getProcessReactionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleProcessReaction=ruleProcessReaction();

            state._fsp--;

             current =iv_ruleProcessReaction; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6322:1: ruleProcessReaction returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' ) ;
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
            // InternalCqrsDsl.g:6328:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' ) )
            // InternalCqrsDsl.g:6329:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' )
            {
            // InternalCqrsDsl.g:6329:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}' )
            // InternalCqrsDsl.g:6330:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'reacts-to' ( ( ruleFQN ) ) (otherlv_3= 'in-state' ( ( ruleFQN ) ) )? otherlv_5= '{' (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )? (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )? (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )? (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )? ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )? otherlv_17= '}'
            {
            // InternalCqrsDsl.g:6330:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt188=2;
            int LA188_0 = input.LA(1);

            if ( (LA188_0==RULE_DOC) ) {
                alt188=1;
            }
            switch (alt188) {
                case 1 :
                    // InternalCqrsDsl.g:6331:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:6331:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:6332:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_140); 

                    					newLeafNode(lv_doc_0_0, grammarAccess.getProcessReactionAccess().getDocDOCTerminalRuleCall_0_0());
                    				

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
                    break;

            }

            otherlv_1=(Token)match(input,92,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getProcessReactionAccess().getReactsToKeyword_1());
            		
            // InternalCqrsDsl.g:6352:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:6353:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:6353:4: ( ruleFQN )
            // InternalCqrsDsl.g:6354:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getProcessReactionRule());
            					}
            				

            					newCompositeNode(grammarAccess.getProcessReactionAccess().getEventEventCrossReference_2_0());
            				
            pushFollow(FOLLOW_141);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:6368:3: (otherlv_3= 'in-state' ( ( ruleFQN ) ) )?
            int alt189=2;
            int LA189_0 = input.LA(1);

            if ( (LA189_0==93) ) {
                alt189=1;
            }
            switch (alt189) {
                case 1 :
                    // InternalCqrsDsl.g:6369:4: otherlv_3= 'in-state' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,93,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getProcessReactionAccess().getInStateKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:6373:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:6374:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:6374:5: ( ruleFQN )
                    // InternalCqrsDsl.g:6375:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getProcessReactionRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getProcessReactionAccess().getFromStateProcessStateCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_5);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_5=(Token)match(input,14,FOLLOW_142); 

            			newLeafNode(otherlv_5, grammarAccess.getProcessReactionAccess().getLeftCurlyBracketKeyword_4());
            		
            // InternalCqrsDsl.g:6394:3: (otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) ) )?
            int alt190=2;
            int LA190_0 = input.LA(1);

            if ( (LA190_0==94) ) {
                alt190=1;
            }
            switch (alt190) {
                case 1 :
                    // InternalCqrsDsl.g:6395:4: otherlv_6= 'correlate-by' ( (lv_correlationKey_7_0= RULE_ID ) )
                    {
                    otherlv_6=(Token)match(input,94,FOLLOW_4); 

                    				newLeafNode(otherlv_6, grammarAccess.getProcessReactionAccess().getCorrelateByKeyword_5_0());
                    			
                    // InternalCqrsDsl.g:6399:4: ( (lv_correlationKey_7_0= RULE_ID ) )
                    // InternalCqrsDsl.g:6400:5: (lv_correlationKey_7_0= RULE_ID )
                    {
                    // InternalCqrsDsl.g:6400:5: (lv_correlationKey_7_0= RULE_ID )
                    // InternalCqrsDsl.g:6401:6: lv_correlationKey_7_0= RULE_ID
                    {
                    lv_correlationKey_7_0=(Token)match(input,RULE_ID,FOLLOW_143); 

                    						newLeafNode(lv_correlationKey_7_0, grammarAccess.getProcessReactionAccess().getCorrelationKeyIDTerminalRuleCall_5_1_0());
                    					

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
                    break;

            }

            // InternalCqrsDsl.g:6418:3: (otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )* )?
            int alt192=2;
            int LA192_0 = input.LA(1);

            if ( (LA192_0==95) ) {
                alt192=1;
            }
            switch (alt192) {
                case 1 :
                    // InternalCqrsDsl.g:6419:4: otherlv_8= 'issues-commands' ( ( ruleFQN ) ) (otherlv_10= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_8=(Token)match(input,95,FOLLOW_4); 

                    				newLeafNode(otherlv_8, grammarAccess.getProcessReactionAccess().getIssuesCommandsKeyword_6_0());
                    			
                    // InternalCqrsDsl.g:6423:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:6424:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:6424:5: ( ruleFQN )
                    // InternalCqrsDsl.g:6425:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getProcessReactionRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getProcessReactionAccess().getCommandsCommandCrossReference_6_1_0());
                    					
                    pushFollow(FOLLOW_144);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:6439:4: (otherlv_10= ',' ( ( ruleFQN ) ) )*
                    loop191:
                    do {
                        int alt191=2;
                        int LA191_0 = input.LA(1);

                        if ( (LA191_0==30) ) {
                            alt191=1;
                        }


                        switch (alt191) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6440:5: otherlv_10= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_10=(Token)match(input,30,FOLLOW_4); 

                    	    					newLeafNode(otherlv_10, grammarAccess.getProcessReactionAccess().getCommaKeyword_6_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:6444:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:6445:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:6445:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:6446:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getProcessReactionRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getProcessReactionAccess().getCommandsCommandCrossReference_6_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_144);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop191;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:6462:3: (otherlv_12= 'transition-to' ( ( ruleFQN ) ) )?
            int alt193=2;
            int LA193_0 = input.LA(1);

            if ( (LA193_0==96) ) {
                alt193=1;
            }
            switch (alt193) {
                case 1 :
                    // InternalCqrsDsl.g:6463:4: otherlv_12= 'transition-to' ( ( ruleFQN ) )
                    {
                    otherlv_12=(Token)match(input,96,FOLLOW_4); 

                    				newLeafNode(otherlv_12, grammarAccess.getProcessReactionAccess().getTransitionToKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:6467:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:6468:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:6468:5: ( ruleFQN )
                    // InternalCqrsDsl.g:6469:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getProcessReactionRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getProcessReactionAccess().getToStateProcessStateCrossReference_7_1_0());
                    					
                    pushFollow(FOLLOW_145);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:6484:3: (otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) ) )?
            int alt194=2;
            int LA194_0 = input.LA(1);

            if ( (LA194_0==97) ) {
                alt194=1;
            }
            switch (alt194) {
                case 1 :
                    // InternalCqrsDsl.g:6485:4: otherlv_14= 'arm-timeout' ( (lv_armTimeout_15_0= ruleDuration ) )
                    {
                    otherlv_14=(Token)match(input,97,FOLLOW_17); 

                    				newLeafNode(otherlv_14, grammarAccess.getProcessReactionAccess().getArmTimeoutKeyword_8_0());
                    			
                    // InternalCqrsDsl.g:6489:4: ( (lv_armTimeout_15_0= ruleDuration ) )
                    // InternalCqrsDsl.g:6490:5: (lv_armTimeout_15_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:6490:5: (lv_armTimeout_15_0= ruleDuration )
                    // InternalCqrsDsl.g:6491:6: lv_armTimeout_15_0= ruleDuration
                    {

                    						newCompositeNode(grammarAccess.getProcessReactionAccess().getArmTimeoutDurationParserRuleCall_8_1_0());
                    					
                    pushFollow(FOLLOW_146);
                    lv_armTimeout_15_0=ruleDuration();

                    state._fsp--;


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
                    break;

            }

            // InternalCqrsDsl.g:6509:3: ( (lv_cancelTimeout_16_0= 'cancel-timeout' ) )?
            int alt195=2;
            int LA195_0 = input.LA(1);

            if ( (LA195_0==98) ) {
                alt195=1;
            }
            switch (alt195) {
                case 1 :
                    // InternalCqrsDsl.g:6510:4: (lv_cancelTimeout_16_0= 'cancel-timeout' )
                    {
                    // InternalCqrsDsl.g:6510:4: (lv_cancelTimeout_16_0= 'cancel-timeout' )
                    // InternalCqrsDsl.g:6511:5: lv_cancelTimeout_16_0= 'cancel-timeout'
                    {
                    lv_cancelTimeout_16_0=(Token)match(input,98,FOLLOW_30); 

                    					newLeafNode(lv_cancelTimeout_16_0, grammarAccess.getProcessReactionAccess().getCancelTimeoutCancelTimeoutKeyword_9_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getProcessReactionRule());
                    					}
                    					setWithLastConsumed(current, "cancelTimeout", lv_cancelTimeout_16_0 != null, "cancel-timeout");
                    				

                    }


                    }
                    break;

            }

            otherlv_17=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_17, grammarAccess.getProcessReactionAccess().getRightCurlyBracketKeyword_10());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6531:1: entryRuleLiteral returns [EObject current=null] : iv_ruleLiteral= ruleLiteral EOF ;
    public final EObject entryRuleLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLiteral = null;


        try {
            // InternalCqrsDsl.g:6531:48: (iv_ruleLiteral= ruleLiteral EOF )
            // InternalCqrsDsl.g:6532:2: iv_ruleLiteral= ruleLiteral EOF
            {
             newCompositeNode(grammarAccess.getLiteralRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleLiteral=ruleLiteral();

            state._fsp--;

             current =iv_ruleLiteral; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6538:1: ruleLiteral returns [EObject current=null] : (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral ) ;
    public final EObject ruleLiteral() throws RecognitionException {
        EObject current = null;

        EObject this_NullLiteral_0 = null;

        EObject this_BooleanLiteral_1 = null;

        EObject this_NumberLiteral_2 = null;

        EObject this_StringLiteral_3 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6544:2: ( (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral ) )
            // InternalCqrsDsl.g:6545:2: (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral )
            {
            // InternalCqrsDsl.g:6545:2: (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral )
            int alt196=4;
            switch ( input.LA(1) ) {
            case 104:
                {
                alt196=1;
                }
                break;
            case 102:
            case 103:
                {
                alt196=2;
                }
                break;
            case RULE_INT:
            case RULE_HEX:
            case RULE_DECIMAL:
                {
                alt196=3;
                }
                break;
            case RULE_STRING:
                {
                alt196=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 196, 0, input);

                throw nvae;
            }

            switch (alt196) {
                case 1 :
                    // InternalCqrsDsl.g:6546:3: this_NullLiteral_0= ruleNullLiteral
                    {

                    			newCompositeNode(grammarAccess.getLiteralAccess().getNullLiteralParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_NullLiteral_0=ruleNullLiteral();

                    state._fsp--;


                    			current = this_NullLiteral_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6555:3: this_BooleanLiteral_1= ruleBooleanLiteral
                    {

                    			newCompositeNode(grammarAccess.getLiteralAccess().getBooleanLiteralParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_BooleanLiteral_1=ruleBooleanLiteral();

                    state._fsp--;


                    			current = this_BooleanLiteral_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6564:3: this_NumberLiteral_2= ruleNumberLiteral
                    {

                    			newCompositeNode(grammarAccess.getLiteralAccess().getNumberLiteralParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_NumberLiteral_2=ruleNumberLiteral();

                    state._fsp--;


                    			current = this_NumberLiteral_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6573:3: this_StringLiteral_3= ruleStringLiteral
                    {

                    			newCompositeNode(grammarAccess.getLiteralAccess().getStringLiteralParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_StringLiteral_3=ruleStringLiteral();

                    state._fsp--;


                    			current = this_StringLiteral_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6585:1: entryRuleJSON returns [EObject current=null] : iv_ruleJSON= ruleJSON EOF ;
    public final EObject entryRuleJSON() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJSON = null;


        try {
            // InternalCqrsDsl.g:6585:45: (iv_ruleJSON= ruleJSON EOF )
            // InternalCqrsDsl.g:6586:2: iv_ruleJSON= ruleJSON EOF
            {
             newCompositeNode(grammarAccess.getJSONRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJSON=ruleJSON();

            state._fsp--;

             current =iv_ruleJSON; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6592:1: ruleJSON returns [EObject current=null] : (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull ) ;
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
            // InternalCqrsDsl.g:6598:2: ( (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull ) )
            // InternalCqrsDsl.g:6599:2: (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull )
            {
            // InternalCqrsDsl.g:6599:2: (this_JsonObject_0= ruleJsonObject | this_JsonArray_1= ruleJsonArray | this_JsonString_2= ruleJsonString | this_JsonNumber_3= ruleJsonNumber | this_JsonBoolean_4= ruleJsonBoolean | this_JsonNull_5= ruleJsonNull )
            int alt197=6;
            switch ( input.LA(1) ) {
            case 14:
                {
                alt197=1;
                }
                break;
            case 100:
                {
                alt197=2;
                }
                break;
            case RULE_STRING:
                {
                alt197=3;
                }
                break;
            case RULE_INT:
            case RULE_HEX:
            case RULE_DECIMAL:
                {
                alt197=4;
                }
                break;
            case 102:
            case 103:
                {
                alt197=5;
                }
                break;
            case 104:
                {
                alt197=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 197, 0, input);

                throw nvae;
            }

            switch (alt197) {
                case 1 :
                    // InternalCqrsDsl.g:6600:3: this_JsonObject_0= ruleJsonObject
                    {

                    			newCompositeNode(grammarAccess.getJSONAccess().getJsonObjectParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_JsonObject_0=ruleJsonObject();

                    state._fsp--;


                    			current = this_JsonObject_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6609:3: this_JsonArray_1= ruleJsonArray
                    {

                    			newCompositeNode(grammarAccess.getJSONAccess().getJsonArrayParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_JsonArray_1=ruleJsonArray();

                    state._fsp--;


                    			current = this_JsonArray_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6618:3: this_JsonString_2= ruleJsonString
                    {

                    			newCompositeNode(grammarAccess.getJSONAccess().getJsonStringParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_JsonString_2=ruleJsonString();

                    state._fsp--;


                    			current = this_JsonString_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6627:3: this_JsonNumber_3= ruleJsonNumber
                    {

                    			newCompositeNode(grammarAccess.getJSONAccess().getJsonNumberParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_JsonNumber_3=ruleJsonNumber();

                    state._fsp--;


                    			current = this_JsonNumber_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:6636:3: this_JsonBoolean_4= ruleJsonBoolean
                    {

                    			newCompositeNode(grammarAccess.getJSONAccess().getJsonBooleanParserRuleCall_4());
                    		
                    pushFollow(FOLLOW_2);
                    this_JsonBoolean_4=ruleJsonBoolean();

                    state._fsp--;


                    			current = this_JsonBoolean_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:6645:3: this_JsonNull_5= ruleJsonNull
                    {

                    			newCompositeNode(grammarAccess.getJSONAccess().getJsonNullParserRuleCall_5());
                    		
                    pushFollow(FOLLOW_2);
                    this_JsonNull_5=ruleJsonNull();

                    state._fsp--;


                    			current = this_JsonNull_5;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6657:1: entryRuleJsonObject returns [EObject current=null] : iv_ruleJsonObject= ruleJsonObject EOF ;
    public final EObject entryRuleJsonObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonObject = null;


        try {
            // InternalCqrsDsl.g:6657:51: (iv_ruleJsonObject= ruleJsonObject EOF )
            // InternalCqrsDsl.g:6658:2: iv_ruleJsonObject= ruleJsonObject EOF
            {
             newCompositeNode(grammarAccess.getJsonObjectRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJsonObject=ruleJsonObject();

            state._fsp--;

             current =iv_ruleJsonObject; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6664:1: ruleJsonObject returns [EObject current=null] : ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' ) ;
    public final EObject ruleJsonObject() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_members_2_0 = null;

        EObject lv_members_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6670:2: ( ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' ) )
            // InternalCqrsDsl.g:6671:2: ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:6671:2: ( () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}' )
            // InternalCqrsDsl.g:6672:3: () otherlv_1= '{' ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )? otherlv_5= '}'
            {
            // InternalCqrsDsl.g:6672:3: ()
            // InternalCqrsDsl.g:6673:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getJsonObjectAccess().getJsonObjectAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,14,FOLLOW_147); 

            			newLeafNode(otherlv_1, grammarAccess.getJsonObjectAccess().getLeftCurlyBracketKeyword_1());
            		
            // InternalCqrsDsl.g:6683:3: ( ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )* )?
            int alt199=2;
            int LA199_0 = input.LA(1);

            if ( (LA199_0==RULE_STRING) ) {
                alt199=1;
            }
            switch (alt199) {
                case 1 :
                    // InternalCqrsDsl.g:6684:4: ( (lv_members_2_0= ruleJsonMember ) ) (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )*
                    {
                    // InternalCqrsDsl.g:6684:4: ( (lv_members_2_0= ruleJsonMember ) )
                    // InternalCqrsDsl.g:6685:5: (lv_members_2_0= ruleJsonMember )
                    {
                    // InternalCqrsDsl.g:6685:5: (lv_members_2_0= ruleJsonMember )
                    // InternalCqrsDsl.g:6686:6: lv_members_2_0= ruleJsonMember
                    {

                    						newCompositeNode(grammarAccess.getJsonObjectAccess().getMembersJsonMemberParserRuleCall_2_0_0());
                    					
                    pushFollow(FOLLOW_148);
                    lv_members_2_0=ruleJsonMember();

                    state._fsp--;


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

                    // InternalCqrsDsl.g:6703:4: (otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) ) )*
                    loop198:
                    do {
                        int alt198=2;
                        int LA198_0 = input.LA(1);

                        if ( (LA198_0==30) ) {
                            alt198=1;
                        }


                        switch (alt198) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6704:5: otherlv_3= ',' ( (lv_members_4_0= ruleJsonMember ) )
                    	    {
                    	    otherlv_3=(Token)match(input,30,FOLLOW_40); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getJsonObjectAccess().getCommaKeyword_2_1_0());
                    	    				
                    	    // InternalCqrsDsl.g:6708:5: ( (lv_members_4_0= ruleJsonMember ) )
                    	    // InternalCqrsDsl.g:6709:6: (lv_members_4_0= ruleJsonMember )
                    	    {
                    	    // InternalCqrsDsl.g:6709:6: (lv_members_4_0= ruleJsonMember )
                    	    // InternalCqrsDsl.g:6710:7: lv_members_4_0= ruleJsonMember
                    	    {

                    	    							newCompositeNode(grammarAccess.getJsonObjectAccess().getMembersJsonMemberParserRuleCall_2_1_1_0());
                    	    						
                    	    pushFollow(FOLLOW_148);
                    	    lv_members_4_0=ruleJsonMember();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop198;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getJsonObjectAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6737:1: entryRuleJsonMember returns [EObject current=null] : iv_ruleJsonMember= ruleJsonMember EOF ;
    public final EObject entryRuleJsonMember() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonMember = null;


        try {
            // InternalCqrsDsl.g:6737:51: (iv_ruleJsonMember= ruleJsonMember EOF )
            // InternalCqrsDsl.g:6738:2: iv_ruleJsonMember= ruleJsonMember EOF
            {
             newCompositeNode(grammarAccess.getJsonMemberRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJsonMember=ruleJsonMember();

            state._fsp--;

             current =iv_ruleJsonMember; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6744:1: ruleJsonMember returns [EObject current=null] : ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) ) ;
    public final EObject ruleJsonMember() throws RecognitionException {
        EObject current = null;

        Token lv_key_0_0=null;
        Token otherlv_1=null;
        EObject lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6750:2: ( ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) ) )
            // InternalCqrsDsl.g:6751:2: ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) )
            {
            // InternalCqrsDsl.g:6751:2: ( ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) ) )
            // InternalCqrsDsl.g:6752:3: ( (lv_key_0_0= RULE_STRING ) ) otherlv_1= ':' ( (lv_value_2_0= ruleJSON ) )
            {
            // InternalCqrsDsl.g:6752:3: ( (lv_key_0_0= RULE_STRING ) )
            // InternalCqrsDsl.g:6753:4: (lv_key_0_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:6753:4: (lv_key_0_0= RULE_STRING )
            // InternalCqrsDsl.g:6754:5: lv_key_0_0= RULE_STRING
            {
            lv_key_0_0=(Token)match(input,RULE_STRING,FOLLOW_149); 

            					newLeafNode(lv_key_0_0, grammarAccess.getJsonMemberAccess().getKeySTRINGTerminalRuleCall_0_0());
            				

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

            otherlv_1=(Token)match(input,99,FOLLOW_13); 

            			newLeafNode(otherlv_1, grammarAccess.getJsonMemberAccess().getColonKeyword_1());
            		
            // InternalCqrsDsl.g:6774:3: ( (lv_value_2_0= ruleJSON ) )
            // InternalCqrsDsl.g:6775:4: (lv_value_2_0= ruleJSON )
            {
            // InternalCqrsDsl.g:6775:4: (lv_value_2_0= ruleJSON )
            // InternalCqrsDsl.g:6776:5: lv_value_2_0= ruleJSON
            {

            					newCompositeNode(grammarAccess.getJsonMemberAccess().getValueJSONParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_2_0=ruleJSON();

            state._fsp--;


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


            	leaveRule();

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
    // InternalCqrsDsl.g:6797:1: entryRuleJsonArray returns [EObject current=null] : iv_ruleJsonArray= ruleJsonArray EOF ;
    public final EObject entryRuleJsonArray() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonArray = null;


        try {
            // InternalCqrsDsl.g:6797:50: (iv_ruleJsonArray= ruleJsonArray EOF )
            // InternalCqrsDsl.g:6798:2: iv_ruleJsonArray= ruleJsonArray EOF
            {
             newCompositeNode(grammarAccess.getJsonArrayRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJsonArray=ruleJsonArray();

            state._fsp--;

             current =iv_ruleJsonArray; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6804:1: ruleJsonArray returns [EObject current=null] : ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' ) ;
    public final EObject ruleJsonArray() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_elements_2_0 = null;

        EObject lv_elements_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6810:2: ( ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' ) )
            // InternalCqrsDsl.g:6811:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' )
            {
            // InternalCqrsDsl.g:6811:2: ( () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']' )
            // InternalCqrsDsl.g:6812:3: () otherlv_1= '[' ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )? otherlv_5= ']'
            {
            // InternalCqrsDsl.g:6812:3: ()
            // InternalCqrsDsl.g:6813:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getJsonArrayAccess().getJsonArrayAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,100,FOLLOW_150); 

            			newLeafNode(otherlv_1, grammarAccess.getJsonArrayAccess().getLeftSquareBracketKeyword_1());
            		
            // InternalCqrsDsl.g:6823:3: ( ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )* )?
            int alt201=2;
            int LA201_0 = input.LA(1);

            if ( ((LA201_0>=RULE_INT && LA201_0<=RULE_DECIMAL)||LA201_0==14||LA201_0==100||(LA201_0>=102 && LA201_0<=104)) ) {
                alt201=1;
            }
            switch (alt201) {
                case 1 :
                    // InternalCqrsDsl.g:6824:4: ( (lv_elements_2_0= ruleJSON ) ) (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )*
                    {
                    // InternalCqrsDsl.g:6824:4: ( (lv_elements_2_0= ruleJSON ) )
                    // InternalCqrsDsl.g:6825:5: (lv_elements_2_0= ruleJSON )
                    {
                    // InternalCqrsDsl.g:6825:5: (lv_elements_2_0= ruleJSON )
                    // InternalCqrsDsl.g:6826:6: lv_elements_2_0= ruleJSON
                    {

                    						newCompositeNode(grammarAccess.getJsonArrayAccess().getElementsJSONParserRuleCall_2_0_0());
                    					
                    pushFollow(FOLLOW_151);
                    lv_elements_2_0=ruleJSON();

                    state._fsp--;


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

                    // InternalCqrsDsl.g:6843:4: (otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) ) )*
                    loop200:
                    do {
                        int alt200=2;
                        int LA200_0 = input.LA(1);

                        if ( (LA200_0==30) ) {
                            alt200=1;
                        }


                        switch (alt200) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:6844:5: otherlv_3= ',' ( (lv_elements_4_0= ruleJSON ) )
                    	    {
                    	    otherlv_3=(Token)match(input,30,FOLLOW_13); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getJsonArrayAccess().getCommaKeyword_2_1_0());
                    	    				
                    	    // InternalCqrsDsl.g:6848:5: ( (lv_elements_4_0= ruleJSON ) )
                    	    // InternalCqrsDsl.g:6849:6: (lv_elements_4_0= ruleJSON )
                    	    {
                    	    // InternalCqrsDsl.g:6849:6: (lv_elements_4_0= ruleJSON )
                    	    // InternalCqrsDsl.g:6850:7: lv_elements_4_0= ruleJSON
                    	    {

                    	    							newCompositeNode(grammarAccess.getJsonArrayAccess().getElementsJSONParserRuleCall_2_1_1_0());
                    	    						
                    	    pushFollow(FOLLOW_151);
                    	    lv_elements_4_0=ruleJSON();

                    	    state._fsp--;


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
                    	    break;

                    	default :
                    	    break loop200;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_5=(Token)match(input,101,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getJsonArrayAccess().getRightSquareBracketKeyword_3());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6877:1: entryRuleJsonString returns [EObject current=null] : iv_ruleJsonString= ruleJsonString EOF ;
    public final EObject entryRuleJsonString() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonString = null;


        try {
            // InternalCqrsDsl.g:6877:51: (iv_ruleJsonString= ruleJsonString EOF )
            // InternalCqrsDsl.g:6878:2: iv_ruleJsonString= ruleJsonString EOF
            {
             newCompositeNode(grammarAccess.getJsonStringRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJsonString=ruleJsonString();

            state._fsp--;

             current =iv_ruleJsonString; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6884:1: ruleJsonString returns [EObject current=null] : ( (lv_value_0_0= RULE_STRING ) ) ;
    public final EObject ruleJsonString() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6890:2: ( ( (lv_value_0_0= RULE_STRING ) ) )
            // InternalCqrsDsl.g:6891:2: ( (lv_value_0_0= RULE_STRING ) )
            {
            // InternalCqrsDsl.g:6891:2: ( (lv_value_0_0= RULE_STRING ) )
            // InternalCqrsDsl.g:6892:3: (lv_value_0_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:6892:3: (lv_value_0_0= RULE_STRING )
            // InternalCqrsDsl.g:6893:4: lv_value_0_0= RULE_STRING
            {
            lv_value_0_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            				newLeafNode(lv_value_0_0, grammarAccess.getJsonStringAccess().getValueSTRINGTerminalRuleCall_0());
            			

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


            	leaveRule();

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
    // InternalCqrsDsl.g:6912:1: entryRuleJsonNumber returns [EObject current=null] : iv_ruleJsonNumber= ruleJsonNumber EOF ;
    public final EObject entryRuleJsonNumber() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonNumber = null;


        try {
            // InternalCqrsDsl.g:6912:51: (iv_ruleJsonNumber= ruleJsonNumber EOF )
            // InternalCqrsDsl.g:6913:2: iv_ruleJsonNumber= ruleJsonNumber EOF
            {
             newCompositeNode(grammarAccess.getJsonNumberRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJsonNumber=ruleJsonNumber();

            state._fsp--;

             current =iv_ruleJsonNumber; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6919:1: ruleJsonNumber returns [EObject current=null] : ( (lv_value_0_0= ruleNumber ) ) ;
    public final EObject ruleJsonNumber() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_value_0_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:6925:2: ( ( (lv_value_0_0= ruleNumber ) ) )
            // InternalCqrsDsl.g:6926:2: ( (lv_value_0_0= ruleNumber ) )
            {
            // InternalCqrsDsl.g:6926:2: ( (lv_value_0_0= ruleNumber ) )
            // InternalCqrsDsl.g:6927:3: (lv_value_0_0= ruleNumber )
            {
            // InternalCqrsDsl.g:6927:3: (lv_value_0_0= ruleNumber )
            // InternalCqrsDsl.g:6928:4: lv_value_0_0= ruleNumber
            {

            				newCompositeNode(grammarAccess.getJsonNumberAccess().getValueNumberParserRuleCall_0());
            			
            pushFollow(FOLLOW_2);
            lv_value_0_0=ruleNumber();

            state._fsp--;


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


            	leaveRule();

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
    // InternalCqrsDsl.g:6948:1: entryRuleJsonBoolean returns [EObject current=null] : iv_ruleJsonBoolean= ruleJsonBoolean EOF ;
    public final EObject entryRuleJsonBoolean() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonBoolean = null;


        try {
            // InternalCqrsDsl.g:6948:52: (iv_ruleJsonBoolean= ruleJsonBoolean EOF )
            // InternalCqrsDsl.g:6949:2: iv_ruleJsonBoolean= ruleJsonBoolean EOF
            {
             newCompositeNode(grammarAccess.getJsonBooleanRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJsonBoolean=ruleJsonBoolean();

            state._fsp--;

             current =iv_ruleJsonBoolean; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6955:1: ruleJsonBoolean returns [EObject current=null] : ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) ) ;
    public final EObject ruleJsonBoolean() throws RecognitionException {
        EObject current = null;

        Token lv_value_0_1=null;
        Token lv_value_0_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6961:2: ( ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) ) )
            // InternalCqrsDsl.g:6962:2: ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) )
            {
            // InternalCqrsDsl.g:6962:2: ( ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) ) )
            // InternalCqrsDsl.g:6963:3: ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) )
            {
            // InternalCqrsDsl.g:6963:3: ( (lv_value_0_1= 'true' | lv_value_0_2= 'false' ) )
            // InternalCqrsDsl.g:6964:4: (lv_value_0_1= 'true' | lv_value_0_2= 'false' )
            {
            // InternalCqrsDsl.g:6964:4: (lv_value_0_1= 'true' | lv_value_0_2= 'false' )
            int alt202=2;
            int LA202_0 = input.LA(1);

            if ( (LA202_0==102) ) {
                alt202=1;
            }
            else if ( (LA202_0==103) ) {
                alt202=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 202, 0, input);

                throw nvae;
            }
            switch (alt202) {
                case 1 :
                    // InternalCqrsDsl.g:6965:5: lv_value_0_1= 'true'
                    {
                    lv_value_0_1=(Token)match(input,102,FOLLOW_2); 

                    					newLeafNode(lv_value_0_1, grammarAccess.getJsonBooleanAccess().getValueTrueKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getJsonBooleanRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_1, null);
                    				

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6976:5: lv_value_0_2= 'false'
                    {
                    lv_value_0_2=(Token)match(input,103,FOLLOW_2); 

                    					newLeafNode(lv_value_0_2, grammarAccess.getJsonBooleanAccess().getValueFalseKeyword_0_1());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getJsonBooleanRule());
                    					}
                    					setWithLastConsumed(current, "value", lv_value_0_2, null);
                    				

                    }
                    break;

            }


            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:6992:1: entryRuleJsonNull returns [EObject current=null] : iv_ruleJsonNull= ruleJsonNull EOF ;
    public final EObject entryRuleJsonNull() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJsonNull = null;


        try {
            // InternalCqrsDsl.g:6992:49: (iv_ruleJsonNull= ruleJsonNull EOF )
            // InternalCqrsDsl.g:6993:2: iv_ruleJsonNull= ruleJsonNull EOF
            {
             newCompositeNode(grammarAccess.getJsonNullRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleJsonNull=ruleJsonNull();

            state._fsp--;

             current =iv_ruleJsonNull; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6999:1: ruleJsonNull returns [EObject current=null] : ( () otherlv_1= 'null' ) ;
    public final EObject ruleJsonNull() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7005:2: ( ( () otherlv_1= 'null' ) )
            // InternalCqrsDsl.g:7006:2: ( () otherlv_1= 'null' )
            {
            // InternalCqrsDsl.g:7006:2: ( () otherlv_1= 'null' )
            // InternalCqrsDsl.g:7007:3: () otherlv_1= 'null'
            {
            // InternalCqrsDsl.g:7007:3: ()
            // InternalCqrsDsl.g:7008:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getJsonNullAccess().getJsonNullAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,104,FOLLOW_2); 

            			newLeafNode(otherlv_1, grammarAccess.getJsonNullAccess().getNullKeyword_1());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7022:1: entryRuleFQN returns [String current=null] : iv_ruleFQN= ruleFQN EOF ;
    public final String entryRuleFQN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFQN = null;


        try {
            // InternalCqrsDsl.g:7022:43: (iv_ruleFQN= ruleFQN EOF )
            // InternalCqrsDsl.g:7023:2: iv_ruleFQN= ruleFQN EOF
            {
             newCompositeNode(grammarAccess.getFQNRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFQN=ruleFQN();

            state._fsp--;

             current =iv_ruleFQN.getText(); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:7029:1: ruleFQN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleFQN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7035:2: ( (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) )
            // InternalCqrsDsl.g:7036:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            {
            // InternalCqrsDsl.g:7036:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            // InternalCqrsDsl.g:7037:3: this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_152); 

            			current.merge(this_ID_0);
            		

            			newLeafNode(this_ID_0, grammarAccess.getFQNAccess().getIDTerminalRuleCall_0());
            		
            // InternalCqrsDsl.g:7044:3: (kw= '.' this_ID_2= RULE_ID )*
            loop203:
            do {
                int alt203=2;
                int LA203_0 = input.LA(1);

                if ( (LA203_0==105) ) {
                    int LA203_2 = input.LA(2);

                    if ( (LA203_2==RULE_ID) ) {
                        alt203=1;
                    }


                }


                switch (alt203) {
            	case 1 :
            	    // InternalCqrsDsl.g:7045:4: kw= '.' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,105,FOLLOW_4); 

            	    				current.merge(kw);
            	    				newLeafNode(kw, grammarAccess.getFQNAccess().getFullStopKeyword_1_0());
            	    			
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_152); 

            	    				current.merge(this_ID_2);
            	    			

            	    				newLeafNode(this_ID_2, grammarAccess.getFQNAccess().getIDTerminalRuleCall_1_1());
            	    			

            	    }
            	    break;

            	default :
            	    break loop203;
                }
            } while (true);


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7062:1: entryRuleFQNWithWildcard returns [String current=null] : iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF ;
    public final String entryRuleFQNWithWildcard() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFQNWithWildcard = null;


        try {
            // InternalCqrsDsl.g:7062:55: (iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF )
            // InternalCqrsDsl.g:7063:2: iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF
            {
             newCompositeNode(grammarAccess.getFQNWithWildcardRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFQNWithWildcard=ruleFQNWithWildcard();

            state._fsp--;

             current =iv_ruleFQNWithWildcard.getText(); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:7069:1: ruleFQNWithWildcard returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_FQN_0= ruleFQN kw= '.' kw= '*' ) ;
    public final AntlrDatatypeRuleToken ruleFQNWithWildcard() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_FQN_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:7075:2: ( (this_FQN_0= ruleFQN kw= '.' kw= '*' ) )
            // InternalCqrsDsl.g:7076:2: (this_FQN_0= ruleFQN kw= '.' kw= '*' )
            {
            // InternalCqrsDsl.g:7076:2: (this_FQN_0= ruleFQN kw= '.' kw= '*' )
            // InternalCqrsDsl.g:7077:3: this_FQN_0= ruleFQN kw= '.' kw= '*'
            {

            			newCompositeNode(grammarAccess.getFQNWithWildcardAccess().getFQNParserRuleCall_0());
            		
            pushFollow(FOLLOW_153);
            this_FQN_0=ruleFQN();

            state._fsp--;


            			current.merge(this_FQN_0);
            		

            			afterParserOrEnumRuleCall();
            		
            kw=(Token)match(input,105,FOLLOW_154); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getFQNWithWildcardAccess().getFullStopKeyword_1());
            		
            kw=(Token)match(input,106,FOLLOW_2); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getFQNWithWildcardAccess().getAsteriskKeyword_2());
            		

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7101:1: entryRuleBooleanLiteral returns [EObject current=null] : iv_ruleBooleanLiteral= ruleBooleanLiteral EOF ;
    public final EObject entryRuleBooleanLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBooleanLiteral = null;


        try {
            // InternalCqrsDsl.g:7101:55: (iv_ruleBooleanLiteral= ruleBooleanLiteral EOF )
            // InternalCqrsDsl.g:7102:2: iv_ruleBooleanLiteral= ruleBooleanLiteral EOF
            {
             newCompositeNode(grammarAccess.getBooleanLiteralRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBooleanLiteral=ruleBooleanLiteral();

            state._fsp--;

             current =iv_ruleBooleanLiteral; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:7108:1: ruleBooleanLiteral returns [EObject current=null] : ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) ) ;
    public final EObject ruleBooleanLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_1=null;
        Token lv_value_1_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7114:2: ( ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) ) )
            // InternalCqrsDsl.g:7115:2: ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) )
            {
            // InternalCqrsDsl.g:7115:2: ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) )
            // InternalCqrsDsl.g:7116:3: () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) )
            {
            // InternalCqrsDsl.g:7116:3: ()
            // InternalCqrsDsl.g:7117:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getBooleanLiteralAccess().getBooleanLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:7123:3: ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) )
            // InternalCqrsDsl.g:7124:4: ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) )
            {
            // InternalCqrsDsl.g:7124:4: ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) )
            // InternalCqrsDsl.g:7125:5: (lv_value_1_1= 'false' | lv_value_1_2= 'true' )
            {
            // InternalCqrsDsl.g:7125:5: (lv_value_1_1= 'false' | lv_value_1_2= 'true' )
            int alt204=2;
            int LA204_0 = input.LA(1);

            if ( (LA204_0==103) ) {
                alt204=1;
            }
            else if ( (LA204_0==102) ) {
                alt204=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 204, 0, input);

                throw nvae;
            }
            switch (alt204) {
                case 1 :
                    // InternalCqrsDsl.g:7126:6: lv_value_1_1= 'false'
                    {
                    lv_value_1_1=(Token)match(input,103,FOLLOW_2); 

                    						newLeafNode(lv_value_1_1, grammarAccess.getBooleanLiteralAccess().getValueFalseKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getBooleanLiteralRule());
                    						}
                    						setWithLastConsumed(current, "value", lv_value_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7137:6: lv_value_1_2= 'true'
                    {
                    lv_value_1_2=(Token)match(input,102,FOLLOW_2); 

                    						newLeafNode(lv_value_1_2, grammarAccess.getBooleanLiteralAccess().getValueTrueKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getBooleanLiteralRule());
                    						}
                    						setWithLastConsumed(current, "value", lv_value_1_2, null);
                    					

                    }
                    break;

            }


            }


            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7154:1: entryRuleNullLiteral returns [EObject current=null] : iv_ruleNullLiteral= ruleNullLiteral EOF ;
    public final EObject entryRuleNullLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNullLiteral = null;


        try {
            // InternalCqrsDsl.g:7154:52: (iv_ruleNullLiteral= ruleNullLiteral EOF )
            // InternalCqrsDsl.g:7155:2: iv_ruleNullLiteral= ruleNullLiteral EOF
            {
             newCompositeNode(grammarAccess.getNullLiteralRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNullLiteral=ruleNullLiteral();

            state._fsp--;

             current =iv_ruleNullLiteral; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:7161:1: ruleNullLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= 'null' ) ) ) ;
    public final EObject ruleNullLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7167:2: ( ( () ( (lv_value_1_0= 'null' ) ) ) )
            // InternalCqrsDsl.g:7168:2: ( () ( (lv_value_1_0= 'null' ) ) )
            {
            // InternalCqrsDsl.g:7168:2: ( () ( (lv_value_1_0= 'null' ) ) )
            // InternalCqrsDsl.g:7169:3: () ( (lv_value_1_0= 'null' ) )
            {
            // InternalCqrsDsl.g:7169:3: ()
            // InternalCqrsDsl.g:7170:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNullLiteralAccess().getNullLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:7176:3: ( (lv_value_1_0= 'null' ) )
            // InternalCqrsDsl.g:7177:4: (lv_value_1_0= 'null' )
            {
            // InternalCqrsDsl.g:7177:4: (lv_value_1_0= 'null' )
            // InternalCqrsDsl.g:7178:5: lv_value_1_0= 'null'
            {
            lv_value_1_0=(Token)match(input,104,FOLLOW_2); 

            					newLeafNode(lv_value_1_0, grammarAccess.getNullLiteralAccess().getValueNullKeyword_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getNullLiteralRule());
            					}
            					setWithLastConsumed(current, "value", lv_value_1_0, "null");
            				

            }


            }


            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7194:1: entryRuleNumberLiteral returns [EObject current=null] : iv_ruleNumberLiteral= ruleNumberLiteral EOF ;
    public final EObject entryRuleNumberLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNumberLiteral = null;


        try {
            // InternalCqrsDsl.g:7194:54: (iv_ruleNumberLiteral= ruleNumberLiteral EOF )
            // InternalCqrsDsl.g:7195:2: iv_ruleNumberLiteral= ruleNumberLiteral EOF
            {
             newCompositeNode(grammarAccess.getNumberLiteralRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNumberLiteral=ruleNumberLiteral();

            state._fsp--;

             current =iv_ruleNumberLiteral; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:7201:1: ruleNumberLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= ruleNumber ) ) ) ;
    public final EObject ruleNumberLiteral() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:7207:2: ( ( () ( (lv_value_1_0= ruleNumber ) ) ) )
            // InternalCqrsDsl.g:7208:2: ( () ( (lv_value_1_0= ruleNumber ) ) )
            {
            // InternalCqrsDsl.g:7208:2: ( () ( (lv_value_1_0= ruleNumber ) ) )
            // InternalCqrsDsl.g:7209:3: () ( (lv_value_1_0= ruleNumber ) )
            {
            // InternalCqrsDsl.g:7209:3: ()
            // InternalCqrsDsl.g:7210:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNumberLiteralAccess().getNumberLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:7216:3: ( (lv_value_1_0= ruleNumber ) )
            // InternalCqrsDsl.g:7217:4: (lv_value_1_0= ruleNumber )
            {
            // InternalCqrsDsl.g:7217:4: (lv_value_1_0= ruleNumber )
            // InternalCqrsDsl.g:7218:5: lv_value_1_0= ruleNumber
            {

            					newCompositeNode(grammarAccess.getNumberLiteralAccess().getValueNumberParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_1_0=ruleNumber();

            state._fsp--;


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


            	leaveRule();

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
    // InternalCqrsDsl.g:7239:1: entryRuleStringLiteral returns [EObject current=null] : iv_ruleStringLiteral= ruleStringLiteral EOF ;
    public final EObject entryRuleStringLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStringLiteral = null;


        try {
            // InternalCqrsDsl.g:7239:54: (iv_ruleStringLiteral= ruleStringLiteral EOF )
            // InternalCqrsDsl.g:7240:2: iv_ruleStringLiteral= ruleStringLiteral EOF
            {
             newCompositeNode(grammarAccess.getStringLiteralRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStringLiteral=ruleStringLiteral();

            state._fsp--;

             current =iv_ruleStringLiteral; 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:7246:1: ruleStringLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleStringLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7252:2: ( ( () ( (lv_value_1_0= RULE_STRING ) ) ) )
            // InternalCqrsDsl.g:7253:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            {
            // InternalCqrsDsl.g:7253:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            // InternalCqrsDsl.g:7254:3: () ( (lv_value_1_0= RULE_STRING ) )
            {
            // InternalCqrsDsl.g:7254:3: ()
            // InternalCqrsDsl.g:7255:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStringLiteralAccess().getStringLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:7261:3: ( (lv_value_1_0= RULE_STRING ) )
            // InternalCqrsDsl.g:7262:4: (lv_value_1_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:7262:4: (lv_value_1_0= RULE_STRING )
            // InternalCqrsDsl.g:7263:5: lv_value_1_0= RULE_STRING
            {
            lv_value_1_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            					newLeafNode(lv_value_1_0, grammarAccess.getStringLiteralAccess().getValueSTRINGTerminalRuleCall_1_0());
            				

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


            	leaveRule();

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
    // InternalCqrsDsl.g:7283:1: entryRuleNumber returns [String current=null] : iv_ruleNumber= ruleNumber EOF ;
    public final String entryRuleNumber() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleNumber = null;



        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens();

        try {
            // InternalCqrsDsl.g:7285:2: (iv_ruleNumber= ruleNumber EOF )
            // InternalCqrsDsl.g:7286:2: iv_ruleNumber= ruleNumber EOF
            {
             newCompositeNode(grammarAccess.getNumberRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNumber=ruleNumber();

            state._fsp--;

             current =iv_ruleNumber.getText(); 
            match(input,EOF,FOLLOW_2); 

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
    // InternalCqrsDsl.g:7295:1: ruleNumber returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) ) ;
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
            // InternalCqrsDsl.g:7302:2: ( (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) ) )
            // InternalCqrsDsl.g:7303:2: (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) )
            {
            // InternalCqrsDsl.g:7303:2: (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) )
            int alt208=2;
            int LA208_0 = input.LA(1);

            if ( (LA208_0==RULE_HEX) ) {
                alt208=1;
            }
            else if ( (LA208_0==RULE_INT||LA208_0==RULE_DECIMAL) ) {
                alt208=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 208, 0, input);

                throw nvae;
            }
            switch (alt208) {
                case 1 :
                    // InternalCqrsDsl.g:7304:3: this_HEX_0= RULE_HEX
                    {
                    this_HEX_0=(Token)match(input,RULE_HEX,FOLLOW_2); 

                    			current.merge(this_HEX_0);
                    		

                    			newLeafNode(this_HEX_0, grammarAccess.getNumberAccess().getHEXTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7312:3: ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? )
                    {
                    // InternalCqrsDsl.g:7312:3: ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? )
                    // InternalCqrsDsl.g:7313:4: (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )?
                    {
                    // InternalCqrsDsl.g:7313:4: (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL )
                    int alt205=2;
                    int LA205_0 = input.LA(1);

                    if ( (LA205_0==RULE_INT) ) {
                        alt205=1;
                    }
                    else if ( (LA205_0==RULE_DECIMAL) ) {
                        alt205=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 205, 0, input);

                        throw nvae;
                    }
                    switch (alt205) {
                        case 1 :
                            // InternalCqrsDsl.g:7314:5: this_INT_1= RULE_INT
                            {
                            this_INT_1=(Token)match(input,RULE_INT,FOLLOW_152); 

                            					current.merge(this_INT_1);
                            				

                            					newLeafNode(this_INT_1, grammarAccess.getNumberAccess().getINTTerminalRuleCall_1_0_0());
                            				

                            }
                            break;
                        case 2 :
                            // InternalCqrsDsl.g:7322:5: this_DECIMAL_2= RULE_DECIMAL
                            {
                            this_DECIMAL_2=(Token)match(input,RULE_DECIMAL,FOLLOW_152); 

                            					current.merge(this_DECIMAL_2);
                            				

                            					newLeafNode(this_DECIMAL_2, grammarAccess.getNumberAccess().getDECIMALTerminalRuleCall_1_0_1());
                            				

                            }
                            break;

                    }

                    // InternalCqrsDsl.g:7330:4: (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )?
                    int alt207=2;
                    int LA207_0 = input.LA(1);

                    if ( (LA207_0==105) ) {
                        alt207=1;
                    }
                    switch (alt207) {
                        case 1 :
                            // InternalCqrsDsl.g:7331:5: kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL )
                            {
                            kw=(Token)match(input,105,FOLLOW_155); 

                            					current.merge(kw);
                            					newLeafNode(kw, grammarAccess.getNumberAccess().getFullStopKeyword_1_1_0());
                            				
                            // InternalCqrsDsl.g:7336:5: (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL )
                            int alt206=2;
                            int LA206_0 = input.LA(1);

                            if ( (LA206_0==RULE_INT) ) {
                                alt206=1;
                            }
                            else if ( (LA206_0==RULE_DECIMAL) ) {
                                alt206=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 206, 0, input);

                                throw nvae;
                            }
                            switch (alt206) {
                                case 1 :
                                    // InternalCqrsDsl.g:7337:6: this_INT_4= RULE_INT
                                    {
                                    this_INT_4=(Token)match(input,RULE_INT,FOLLOW_2); 

                                    						current.merge(this_INT_4);
                                    					

                                    						newLeafNode(this_INT_4, grammarAccess.getNumberAccess().getINTTerminalRuleCall_1_1_1_0());
                                    					

                                    }
                                    break;
                                case 2 :
                                    // InternalCqrsDsl.g:7345:6: this_DECIMAL_5= RULE_DECIMAL
                                    {
                                    this_DECIMAL_5=(Token)match(input,RULE_DECIMAL,FOLLOW_2); 

                                    						current.merge(this_DECIMAL_5);
                                    					

                                    						newLeafNode(this_DECIMAL_5, grammarAccess.getNumberAccess().getDECIMALTerminalRuleCall_1_1_1_1());
                                    					

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


            	leaveRule();

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
    // InternalCqrsDsl.g:7362:1: ruleTimeUnit returns [Enumerator current=null] : ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) ) ;
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
            // InternalCqrsDsl.g:7368:2: ( ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) ) )
            // InternalCqrsDsl.g:7369:2: ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) )
            {
            // InternalCqrsDsl.g:7369:2: ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) )
            int alt209=8;
            switch ( input.LA(1) ) {
            case 107:
                {
                alt209=1;
                }
                break;
            case 108:
                {
                alt209=2;
                }
                break;
            case 109:
                {
                alt209=3;
                }
                break;
            case 110:
                {
                alt209=4;
                }
                break;
            case 111:
                {
                alt209=5;
                }
                break;
            case 112:
                {
                alt209=6;
                }
                break;
            case 113:
                {
                alt209=7;
                }
                break;
            case 114:
                {
                alt209=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 209, 0, input);

                throw nvae;
            }

            switch (alt209) {
                case 1 :
                    // InternalCqrsDsl.g:7370:3: (enumLiteral_0= 'millis' )
                    {
                    // InternalCqrsDsl.g:7370:3: (enumLiteral_0= 'millis' )
                    // InternalCqrsDsl.g:7371:4: enumLiteral_0= 'millis'
                    {
                    enumLiteral_0=(Token)match(input,107,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getMillisEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getTimeUnitAccess().getMillisEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7378:3: (enumLiteral_1= 'seconds' )
                    {
                    // InternalCqrsDsl.g:7378:3: (enumLiteral_1= 'seconds' )
                    // InternalCqrsDsl.g:7379:4: enumLiteral_1= 'seconds'
                    {
                    enumLiteral_1=(Token)match(input,108,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getSecondsEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getTimeUnitAccess().getSecondsEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:7386:3: (enumLiteral_2= 'minutes' )
                    {
                    // InternalCqrsDsl.g:7386:3: (enumLiteral_2= 'minutes' )
                    // InternalCqrsDsl.g:7387:4: enumLiteral_2= 'minutes'
                    {
                    enumLiteral_2=(Token)match(input,109,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getMinutesEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getTimeUnitAccess().getMinutesEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:7394:3: (enumLiteral_3= 'hours' )
                    {
                    // InternalCqrsDsl.g:7394:3: (enumLiteral_3= 'hours' )
                    // InternalCqrsDsl.g:7395:4: enumLiteral_3= 'hours'
                    {
                    enumLiteral_3=(Token)match(input,110,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getHoursEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getTimeUnitAccess().getHoursEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:7402:3: (enumLiteral_4= 'days' )
                    {
                    // InternalCqrsDsl.g:7402:3: (enumLiteral_4= 'days' )
                    // InternalCqrsDsl.g:7403:4: enumLiteral_4= 'days'
                    {
                    enumLiteral_4=(Token)match(input,111,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getDaysEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getTimeUnitAccess().getDaysEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:7410:3: (enumLiteral_5= 'weeks' )
                    {
                    // InternalCqrsDsl.g:7410:3: (enumLiteral_5= 'weeks' )
                    // InternalCqrsDsl.g:7411:4: enumLiteral_5= 'weeks'
                    {
                    enumLiteral_5=(Token)match(input,112,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getWeeksEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getTimeUnitAccess().getWeeksEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:7418:3: (enumLiteral_6= 'months' )
                    {
                    // InternalCqrsDsl.g:7418:3: (enumLiteral_6= 'months' )
                    // InternalCqrsDsl.g:7419:4: enumLiteral_6= 'months'
                    {
                    enumLiteral_6=(Token)match(input,113,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getMonthsEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getTimeUnitAccess().getMonthsEnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:7426:3: (enumLiteral_7= 'years' )
                    {
                    // InternalCqrsDsl.g:7426:3: (enumLiteral_7= 'years' )
                    // InternalCqrsDsl.g:7427:4: enumLiteral_7= 'years'
                    {
                    enumLiteral_7=(Token)match(input,114,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getYearsEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_7, grammarAccess.getTimeUnitAccess().getYearsEnumLiteralDeclaration_7());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7437:1: ruleConsistencyLevel returns [Enumerator current=null] : ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) ) ;
    public final Enumerator ruleConsistencyLevel() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7443:2: ( ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) ) )
            // InternalCqrsDsl.g:7444:2: ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) )
            {
            // InternalCqrsDsl.g:7444:2: ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) )
            int alt210=2;
            int LA210_0 = input.LA(1);

            if ( (LA210_0==115) ) {
                alt210=1;
            }
            else if ( (LA210_0==116) ) {
                alt210=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 210, 0, input);

                throw nvae;
            }
            switch (alt210) {
                case 1 :
                    // InternalCqrsDsl.g:7445:3: (enumLiteral_0= 'weak' )
                    {
                    // InternalCqrsDsl.g:7445:3: (enumLiteral_0= 'weak' )
                    // InternalCqrsDsl.g:7446:4: enumLiteral_0= 'weak'
                    {
                    enumLiteral_0=(Token)match(input,115,FOLLOW_2); 

                    				current = grammarAccess.getConsistencyLevelAccess().getWeakEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getConsistencyLevelAccess().getWeakEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7453:3: (enumLiteral_1= 'strong' )
                    {
                    // InternalCqrsDsl.g:7453:3: (enumLiteral_1= 'strong' )
                    // InternalCqrsDsl.g:7454:4: enumLiteral_1= 'strong'
                    {
                    enumLiteral_1=(Token)match(input,116,FOLLOW_2); 

                    				current = grammarAccess.getConsistencyLevelAccess().getStrongEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getConsistencyLevelAccess().getStrongEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7464:1: ruleInconsistencyDetection returns [Enumerator current=null] : ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) ) ;
    public final Enumerator ruleInconsistencyDetection() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7470:2: ( ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) ) )
            // InternalCqrsDsl.g:7471:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) )
            {
            // InternalCqrsDsl.g:7471:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) )
            int alt211=3;
            switch ( input.LA(1) ) {
            case 117:
                {
                alt211=1;
                }
                break;
            case 118:
                {
                alt211=2;
                }
                break;
            case 119:
                {
                alt211=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 211, 0, input);

                throw nvae;
            }

            switch (alt211) {
                case 1 :
                    // InternalCqrsDsl.g:7472:3: (enumLiteral_0= 'never' )
                    {
                    // InternalCqrsDsl.g:7472:3: (enumLiteral_0= 'never' )
                    // InternalCqrsDsl.g:7473:4: enumLiteral_0= 'never'
                    {
                    enumLiteral_0=(Token)match(input,117,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyDetectionAccess().getNeverEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getInconsistencyDetectionAccess().getNeverEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7480:3: (enumLiteral_1= 'manually' )
                    {
                    // InternalCqrsDsl.g:7480:3: (enumLiteral_1= 'manually' )
                    // InternalCqrsDsl.g:7481:4: enumLiteral_1= 'manually'
                    {
                    enumLiteral_1=(Token)match(input,118,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyDetectionAccess().getManuallyEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getInconsistencyDetectionAccess().getManuallyEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:7488:3: (enumLiteral_2= 'automatic' )
                    {
                    // InternalCqrsDsl.g:7488:3: (enumLiteral_2= 'automatic' )
                    // InternalCqrsDsl.g:7489:4: enumLiteral_2= 'automatic'
                    {
                    enumLiteral_2=(Token)match(input,119,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyDetectionAccess().getAutomaticEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getInconsistencyDetectionAccess().getAutomaticEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7499:1: ruleInconsistencyResolution returns [Enumerator current=null] : ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) ) ;
    public final Enumerator ruleInconsistencyResolution() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7505:2: ( ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) ) )
            // InternalCqrsDsl.g:7506:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) )
            {
            // InternalCqrsDsl.g:7506:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) )
            int alt212=4;
            switch ( input.LA(1) ) {
            case 117:
                {
                alt212=1;
                }
                break;
            case 118:
                {
                alt212=2;
                }
                break;
            case 119:
                {
                alt212=3;
                }
                break;
            case 120:
                {
                alt212=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 212, 0, input);

                throw nvae;
            }

            switch (alt212) {
                case 1 :
                    // InternalCqrsDsl.g:7507:3: (enumLiteral_0= 'never' )
                    {
                    // InternalCqrsDsl.g:7507:3: (enumLiteral_0= 'never' )
                    // InternalCqrsDsl.g:7508:4: enumLiteral_0= 'never'
                    {
                    enumLiteral_0=(Token)match(input,117,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyResolutionAccess().getNeverEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getInconsistencyResolutionAccess().getNeverEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7515:3: (enumLiteral_1= 'manually' )
                    {
                    // InternalCqrsDsl.g:7515:3: (enumLiteral_1= 'manually' )
                    // InternalCqrsDsl.g:7516:4: enumLiteral_1= 'manually'
                    {
                    enumLiteral_1=(Token)match(input,118,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyResolutionAccess().getManuallyEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getInconsistencyResolutionAccess().getManuallyEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:7523:3: (enumLiteral_2= 'automatic' )
                    {
                    // InternalCqrsDsl.g:7523:3: (enumLiteral_2= 'automatic' )
                    // InternalCqrsDsl.g:7524:4: enumLiteral_2= 'automatic'
                    {
                    enumLiteral_2=(Token)match(input,119,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyResolutionAccess().getAutomaticEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getInconsistencyResolutionAccess().getAutomaticEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:7531:3: (enumLiteral_3= 'workflow' )
                    {
                    // InternalCqrsDsl.g:7531:3: (enumLiteral_3= 'workflow' )
                    // InternalCqrsDsl.g:7532:4: enumLiteral_3= 'workflow'
                    {
                    enumLiteral_3=(Token)match(input,120,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyResolutionAccess().getWorkflowEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getInconsistencyResolutionAccess().getWorkflowEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7542:1: ruleProtectionLevel returns [Enumerator current=null] : ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) ) ;
    public final Enumerator ruleProtectionLevel() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7548:2: ( ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) ) )
            // InternalCqrsDsl.g:7549:2: ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) )
            {
            // InternalCqrsDsl.g:7549:2: ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) )
            int alt213=3;
            switch ( input.LA(1) ) {
            case 121:
                {
                alt213=1;
                }
                break;
            case 122:
                {
                alt213=2;
                }
                break;
            case 123:
                {
                alt213=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 213, 0, input);

                throw nvae;
            }

            switch (alt213) {
                case 1 :
                    // InternalCqrsDsl.g:7550:3: (enumLiteral_0= 'none' )
                    {
                    // InternalCqrsDsl.g:7550:3: (enumLiteral_0= 'none' )
                    // InternalCqrsDsl.g:7551:4: enumLiteral_0= 'none'
                    {
                    enumLiteral_0=(Token)match(input,121,FOLLOW_2); 

                    				current = grammarAccess.getProtectionLevelAccess().getNoneEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getProtectionLevelAccess().getNoneEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7558:3: (enumLiteral_1= 'personal' )
                    {
                    // InternalCqrsDsl.g:7558:3: (enumLiteral_1= 'personal' )
                    // InternalCqrsDsl.g:7559:4: enumLiteral_1= 'personal'
                    {
                    enumLiteral_1=(Token)match(input,122,FOLLOW_2); 

                    				current = grammarAccess.getProtectionLevelAccess().getPersonalEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getProtectionLevelAccess().getPersonalEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:7566:3: (enumLiteral_2= 'sensitive' )
                    {
                    // InternalCqrsDsl.g:7566:3: (enumLiteral_2= 'sensitive' )
                    // InternalCqrsDsl.g:7567:4: enumLiteral_2= 'sensitive'
                    {
                    enumLiteral_2=(Token)match(input,123,FOLLOW_2); 

                    				current = grammarAccess.getProtectionLevelAccess().getSensitiveEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getProtectionLevelAccess().getSensitiveEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7577:1: ruleLawfulBasis returns [Enumerator current=null] : ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) ) ;
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
            // InternalCqrsDsl.g:7583:2: ( ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) ) )
            // InternalCqrsDsl.g:7584:2: ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) )
            {
            // InternalCqrsDsl.g:7584:2: ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) )
            int alt214=7;
            switch ( input.LA(1) ) {
            case 124:
                {
                alt214=1;
                }
                break;
            case 125:
                {
                alt214=2;
                }
                break;
            case 126:
                {
                alt214=3;
                }
                break;
            case 127:
                {
                alt214=4;
                }
                break;
            case 128:
                {
                alt214=5;
                }
                break;
            case 129:
                {
                alt214=6;
                }
                break;
            case 130:
                {
                alt214=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 214, 0, input);

                throw nvae;
            }

            switch (alt214) {
                case 1 :
                    // InternalCqrsDsl.g:7585:3: (enumLiteral_0= 'consent' )
                    {
                    // InternalCqrsDsl.g:7585:3: (enumLiteral_0= 'consent' )
                    // InternalCqrsDsl.g:7586:4: enumLiteral_0= 'consent'
                    {
                    enumLiteral_0=(Token)match(input,124,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getConsentEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getLawfulBasisAccess().getConsentEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7593:3: (enumLiteral_1= 'explicit_consent' )
                    {
                    // InternalCqrsDsl.g:7593:3: (enumLiteral_1= 'explicit_consent' )
                    // InternalCqrsDsl.g:7594:4: enumLiteral_1= 'explicit_consent'
                    {
                    enumLiteral_1=(Token)match(input,125,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getExplicit_consentEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getLawfulBasisAccess().getExplicit_consentEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:7601:3: (enumLiteral_2= 'contract' )
                    {
                    // InternalCqrsDsl.g:7601:3: (enumLiteral_2= 'contract' )
                    // InternalCqrsDsl.g:7602:4: enumLiteral_2= 'contract'
                    {
                    enumLiteral_2=(Token)match(input,126,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getContractEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getLawfulBasisAccess().getContractEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:7609:3: (enumLiteral_3= 'legal_obligation' )
                    {
                    // InternalCqrsDsl.g:7609:3: (enumLiteral_3= 'legal_obligation' )
                    // InternalCqrsDsl.g:7610:4: enumLiteral_3= 'legal_obligation'
                    {
                    enumLiteral_3=(Token)match(input,127,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getLegal_obligationEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getLawfulBasisAccess().getLegal_obligationEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:7617:3: (enumLiteral_4= 'vital_interests' )
                    {
                    // InternalCqrsDsl.g:7617:3: (enumLiteral_4= 'vital_interests' )
                    // InternalCqrsDsl.g:7618:4: enumLiteral_4= 'vital_interests'
                    {
                    enumLiteral_4=(Token)match(input,128,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getVital_interestsEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getLawfulBasisAccess().getVital_interestsEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:7625:3: (enumLiteral_5= 'public_task' )
                    {
                    // InternalCqrsDsl.g:7625:3: (enumLiteral_5= 'public_task' )
                    // InternalCqrsDsl.g:7626:4: enumLiteral_5= 'public_task'
                    {
                    enumLiteral_5=(Token)match(input,129,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getPublic_taskEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getLawfulBasisAccess().getPublic_taskEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:7633:3: (enumLiteral_6= 'legitimate_interests' )
                    {
                    // InternalCqrsDsl.g:7633:3: (enumLiteral_6= 'legitimate_interests' )
                    // InternalCqrsDsl.g:7634:4: enumLiteral_6= 'legitimate_interests'
                    {
                    enumLiteral_6=(Token)match(input,130,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getLegitimate_interestsEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getLawfulBasisAccess().getLegitimate_interestsEnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7644:1: ruleSpecialCategory returns [Enumerator current=null] : ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) ) ;
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
            // InternalCqrsDsl.g:7650:2: ( ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) ) )
            // InternalCqrsDsl.g:7651:2: ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) )
            {
            // InternalCqrsDsl.g:7651:2: ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) )
            int alt215=10;
            switch ( input.LA(1) ) {
            case 131:
                {
                alt215=1;
                }
                break;
            case 132:
                {
                alt215=2;
                }
                break;
            case 133:
                {
                alt215=3;
                }
                break;
            case 134:
                {
                alt215=4;
                }
                break;
            case 135:
                {
                alt215=5;
                }
                break;
            case 136:
                {
                alt215=6;
                }
                break;
            case 137:
                {
                alt215=7;
                }
                break;
            case 138:
                {
                alt215=8;
                }
                break;
            case 139:
                {
                alt215=9;
                }
                break;
            case 140:
                {
                alt215=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 215, 0, input);

                throw nvae;
            }

            switch (alt215) {
                case 1 :
                    // InternalCqrsDsl.g:7652:3: (enumLiteral_0= 'health' )
                    {
                    // InternalCqrsDsl.g:7652:3: (enumLiteral_0= 'health' )
                    // InternalCqrsDsl.g:7653:4: enumLiteral_0= 'health'
                    {
                    enumLiteral_0=(Token)match(input,131,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getHealthEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getSpecialCategoryAccess().getHealthEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7660:3: (enumLiteral_1= 'genetic' )
                    {
                    // InternalCqrsDsl.g:7660:3: (enumLiteral_1= 'genetic' )
                    // InternalCqrsDsl.g:7661:4: enumLiteral_1= 'genetic'
                    {
                    enumLiteral_1=(Token)match(input,132,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getGeneticEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getSpecialCategoryAccess().getGeneticEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:7668:3: (enumLiteral_2= 'biometric' )
                    {
                    // InternalCqrsDsl.g:7668:3: (enumLiteral_2= 'biometric' )
                    // InternalCqrsDsl.g:7669:4: enumLiteral_2= 'biometric'
                    {
                    enumLiteral_2=(Token)match(input,133,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getBiometricEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getSpecialCategoryAccess().getBiometricEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:7676:3: (enumLiteral_3= 'racial' )
                    {
                    // InternalCqrsDsl.g:7676:3: (enumLiteral_3= 'racial' )
                    // InternalCqrsDsl.g:7677:4: enumLiteral_3= 'racial'
                    {
                    enumLiteral_3=(Token)match(input,134,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getRacialEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getSpecialCategoryAccess().getRacialEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:7684:3: (enumLiteral_4= 'political' )
                    {
                    // InternalCqrsDsl.g:7684:3: (enumLiteral_4= 'political' )
                    // InternalCqrsDsl.g:7685:4: enumLiteral_4= 'political'
                    {
                    enumLiteral_4=(Token)match(input,135,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getPoliticalEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getSpecialCategoryAccess().getPoliticalEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:7692:3: (enumLiteral_5= 'religious' )
                    {
                    // InternalCqrsDsl.g:7692:3: (enumLiteral_5= 'religious' )
                    // InternalCqrsDsl.g:7693:4: enumLiteral_5= 'religious'
                    {
                    enumLiteral_5=(Token)match(input,136,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getReligiousEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getSpecialCategoryAccess().getReligiousEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:7700:3: (enumLiteral_6= 'philosophical' )
                    {
                    // InternalCqrsDsl.g:7700:3: (enumLiteral_6= 'philosophical' )
                    // InternalCqrsDsl.g:7701:4: enumLiteral_6= 'philosophical'
                    {
                    enumLiteral_6=(Token)match(input,137,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getPhilosophicalEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getSpecialCategoryAccess().getPhilosophicalEnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:7708:3: (enumLiteral_7= 'trade_union' )
                    {
                    // InternalCqrsDsl.g:7708:3: (enumLiteral_7= 'trade_union' )
                    // InternalCqrsDsl.g:7709:4: enumLiteral_7= 'trade_union'
                    {
                    enumLiteral_7=(Token)match(input,138,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getTrade_unionEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_7, grammarAccess.getSpecialCategoryAccess().getTrade_unionEnumLiteralDeclaration_7());
                    			

                    }


                    }
                    break;
                case 9 :
                    // InternalCqrsDsl.g:7716:3: (enumLiteral_8= 'sex_life' )
                    {
                    // InternalCqrsDsl.g:7716:3: (enumLiteral_8= 'sex_life' )
                    // InternalCqrsDsl.g:7717:4: enumLiteral_8= 'sex_life'
                    {
                    enumLiteral_8=(Token)match(input,139,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getSex_lifeEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_8, grammarAccess.getSpecialCategoryAccess().getSex_lifeEnumLiteralDeclaration_8());
                    			

                    }


                    }
                    break;
                case 10 :
                    // InternalCqrsDsl.g:7724:3: (enumLiteral_9= 'sexual_orientation' )
                    {
                    // InternalCqrsDsl.g:7724:3: (enumLiteral_9= 'sexual_orientation' )
                    // InternalCqrsDsl.g:7725:4: enumLiteral_9= 'sexual_orientation'
                    {
                    enumLiteral_9=(Token)match(input,140,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getSexual_orientationEnumLiteralDeclaration_9().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_9, grammarAccess.getSpecialCategoryAccess().getSexual_orientationEnumLiteralDeclaration_9());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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
    // InternalCqrsDsl.g:7735:1: ruleErasureStrategy returns [Enumerator current=null] : ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) ) ;
    public final Enumerator ruleErasureStrategy() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:7741:2: ( ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) ) )
            // InternalCqrsDsl.g:7742:2: ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) )
            {
            // InternalCqrsDsl.g:7742:2: ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) )
            int alt216=5;
            switch ( input.LA(1) ) {
            case 141:
                {
                alt216=1;
                }
                break;
            case 142:
                {
                alt216=2;
                }
                break;
            case 143:
                {
                alt216=3;
                }
                break;
            case 144:
                {
                alt216=4;
                }
                break;
            case 145:
                {
                alt216=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 216, 0, input);

                throw nvae;
            }

            switch (alt216) {
                case 1 :
                    // InternalCqrsDsl.g:7743:3: (enumLiteral_0= 'delete' )
                    {
                    // InternalCqrsDsl.g:7743:3: (enumLiteral_0= 'delete' )
                    // InternalCqrsDsl.g:7744:4: enumLiteral_0= 'delete'
                    {
                    enumLiteral_0=(Token)match(input,141,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getDeleteEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getErasureStrategyAccess().getDeleteEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:7751:3: (enumLiteral_1= 'anonymize' )
                    {
                    // InternalCqrsDsl.g:7751:3: (enumLiteral_1= 'anonymize' )
                    // InternalCqrsDsl.g:7752:4: enumLiteral_1= 'anonymize'
                    {
                    enumLiteral_1=(Token)match(input,142,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getAnonymizeEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getErasureStrategyAccess().getAnonymizeEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:7759:3: (enumLiteral_2= 'pseudonymize' )
                    {
                    // InternalCqrsDsl.g:7759:3: (enumLiteral_2= 'pseudonymize' )
                    // InternalCqrsDsl.g:7760:4: enumLiteral_2= 'pseudonymize'
                    {
                    enumLiteral_2=(Token)match(input,143,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getPseudonymizeEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getErasureStrategyAccess().getPseudonymizeEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:7767:3: (enumLiteral_3= 'archive' )
                    {
                    // InternalCqrsDsl.g:7767:3: (enumLiteral_3= 'archive' )
                    // InternalCqrsDsl.g:7768:4: enumLiteral_3= 'archive'
                    {
                    enumLiteral_3=(Token)match(input,144,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getArchiveEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getErasureStrategyAccess().getArchiveEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:7775:3: (enumLiteral_4= 'review' )
                    {
                    // InternalCqrsDsl.g:7775:3: (enumLiteral_4= 'review' )
                    // InternalCqrsDsl.g:7776:4: enumLiteral_4= 'review'
                    {
                    enumLiteral_4=(Token)match(input,145,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getReviewEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getErasureStrategyAccess().getReviewEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

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

    // Delegated rules


    protected DFA10 dfa10 = new DFA10(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA114 dfa114 = new DFA114(this);
    protected DFA127 dfa127 = new DFA127(this);
    static final String dfa_1s = "\6\uffff";
    static final String dfa_2s = "\1\uffff\1\3\2\uffff\1\3\1\uffff";
    static final String dfa_3s = "\1\5\1\4\1\5\1\uffff\1\4\1\uffff";
    static final String dfa_4s = "\1\5\1\151\1\152\1\uffff\1\151\1\uffff";
    static final String dfa_5s = "\3\uffff\1\1\1\uffff\1\2";
    static final String dfa_6s = "\6\uffff}>";
    static final String[] dfa_7s = {
            "\1\1",
            "\1\3\12\uffff\1\3\2\uffff\1\3\1\uffff\1\3\6\uffff\1\3\11\uffff\1\3\2\uffff\1\3\2\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\2\3\4\uffff\1\3\1\uffff\1\3\2\uffff\1\3\20\uffff\3\3\2\uffff\1\3\2\uffff\2\3\2\uffff\1\3\17\uffff\1\2",
            "\1\4\144\uffff\1\5",
            "",
            "\1\3\12\uffff\1\3\2\uffff\1\3\1\uffff\1\3\6\uffff\1\3\11\uffff\1\3\2\uffff\1\3\2\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\2\3\4\uffff\1\3\1\uffff\1\3\2\uffff\1\3\20\uffff\3\3\2\uffff\1\3\2\uffff\2\3\2\uffff\1\3\17\uffff\1\2",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "410:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )";
        }
    }
    static final String dfa_8s = "\50\uffff";
    static final String dfa_9s = "\1\4\1\24\3\uffff\1\5\10\uffff\1\55\1\5\1\6\1\55\7\36\1\6\1\55\1\6\11\36\1\6\2\36";
    static final String dfa_10s = "\2\131\3\uffff\1\5\10\uffff\1\151\1\5\1\150\1\151\4\66\2\151\1\66\1\150\1\115\1\11\4\66\2\151\3\66\1\11\2\66";
    static final String dfa_11s = "\2\uffff\1\1\1\2\1\3\1\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\32\uffff";
    static final String dfa_12s = "\50\uffff}>";
    static final String[] dfa_13s = {
            "\1\1\17\uffff\1\4\6\uffff\1\15\11\uffff\1\2\2\uffff\1\6\2\uffff\1\3\1\uffff\1\4\1\uffff\1\4\1\uffff\2\4\4\uffff\1\7\1\uffff\1\4\2\uffff\1\4\20\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13\2\uffff\1\14",
            "\1\4\6\uffff\1\15\11\uffff\1\2\2\uffff\1\6\2\uffff\1\3\1\uffff\1\4\1\uffff\1\4\1\uffff\2\4\4\uffff\1\7\1\uffff\1\4\2\uffff\1\4\20\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13\2\uffff\1\14",
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
            "\1\4\7\uffff\1\20\1\uffff\1\7\25\uffff\1\5\33\uffff\1\17",
            "\1\21",
            "\1\26\1\30\1\25\1\27\134\uffff\1\24\1\23\1\22",
            "\1\4\7\uffff\1\20\1\uffff\1\7\25\uffff\1\5\33\uffff\1\17",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32\62\uffff\1\33",
            "\1\31\27\uffff\1\32\62\uffff\1\33",
            "\1\31\27\uffff\1\32",
            "\1\40\1\42\1\37\1\41\134\uffff\1\36\1\35\1\34",
            "\1\4\11\uffff\1\7\25\uffff\1\5",
            "\1\43\2\uffff\1\44",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32\62\uffff\1\45",
            "\1\31\27\uffff\1\32\62\uffff\1\45",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32",
            "\1\46\2\uffff\1\47",
            "\1\31\27\uffff\1\32",
            "\1\31\27\uffff\1\32"
    };

    static final short[] dfa_8 = DFA.unpackEncodedString(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final char[] dfa_10 = DFA.unpackEncodedStringToUnsignedChars(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[] dfa_12 = DFA.unpackEncodedString(dfa_12s);
    static final short[][] dfa_13 = unpackEncodedStringArray(dfa_13s);

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = dfa_8;
            this.eof = dfa_8;
            this.min = dfa_9;
            this.max = dfa_10;
            this.accept = dfa_11;
            this.special = dfa_12;
            this.transition = dfa_13;
        }
        public String getDescription() {
            return "542:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_ProcessManager_9= ruleProcessManager | this_DataProtection_10= ruleDataProtection )";
        }
    }
    static final String dfa_14s = "\7\uffff";
    static final String dfa_15s = "\2\4\1\5\2\uffff\1\5\1\4";
    static final String dfa_16s = "\1\116\1\151\1\116\2\uffff\1\5\1\151";
    static final String dfa_17s = "\3\uffff\1\2\1\1\2\uffff";
    static final String dfa_18s = "\7\uffff}>";
    static final String[] dfa_19s = {
            "\1\2\1\1\11\uffff\1\3\47\uffff\1\3\10\uffff\1\4\14\uffff\2\3",
            "\1\3\1\4\11\uffff\1\3\47\uffff\1\3\20\uffff\1\4\4\uffff\2\3\32\uffff\1\5",
            "\1\4\61\uffff\1\3\10\uffff\1\4\14\uffff\2\3",
            "",
            "",
            "\1\6",
            "\1\3\1\4\11\uffff\1\3\47\uffff\1\3\20\uffff\1\4\4\uffff\2\3\32\uffff\1\5"
    };

    static final short[] dfa_14 = DFA.unpackEncodedString(dfa_14s);
    static final char[] dfa_15 = DFA.unpackEncodedStringToUnsignedChars(dfa_15s);
    static final char[] dfa_16 = DFA.unpackEncodedStringToUnsignedChars(dfa_16s);
    static final short[] dfa_17 = DFA.unpackEncodedString(dfa_17s);
    static final short[] dfa_18 = DFA.unpackEncodedString(dfa_18s);
    static final short[][] dfa_19 = unpackEncodedStringArray(dfa_19s);

    class DFA114 extends DFA {

        public DFA114(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 114;
            this.eot = dfa_14;
            this.eof = dfa_14;
            this.min = dfa_15;
            this.max = dfa_16;
            this.accept = dfa_17;
            this.special = dfa_18;
            this.transition = dfa_19;
        }
        public String getDescription() {
            return "()* loopback of 3924:3: ( (lv_parameters_10_0= ruleParameter ) )*";
        }
    }
    static final String[] dfa_20s = {
            "\1\2\1\1\11\uffff\1\3\47\uffff\1\3\7\uffff\1\3\1\4\14\uffff\2\3",
            "\1\3\1\4\11\uffff\1\3\47\uffff\1\3\7\uffff\1\3\10\uffff\1\4\4\uffff\2\3\32\uffff\1\5",
            "\1\4\61\uffff\1\3\7\uffff\1\3\1\4\14\uffff\2\3",
            "",
            "",
            "\1\6",
            "\1\3\1\4\11\uffff\1\3\47\uffff\1\3\7\uffff\1\3\10\uffff\1\4\4\uffff\2\3\32\uffff\1\5"
    };
    static final short[][] dfa_20 = unpackEncodedStringArray(dfa_20s);

    class DFA127 extends DFA {

        public DFA127(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 127;
            this.eot = dfa_14;
            this.eof = dfa_14;
            this.min = dfa_15;
            this.max = dfa_16;
            this.accept = dfa_17;
            this.special = dfa_18;
            this.transition = dfa_20;
        }
        public String getDescription() {
            return "()* loopback of 4258:3: ( (lv_parameters_12_0= ruleParameter ) )*";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000098010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000018000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x1286A92008168010L,0x000000000264E000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000028000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x1286A92008148010L,0x000000000264E000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x1286A92008108010L,0x000000000264E000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x00000000000043C0L,0x000001D000000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000200020L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000000000L,0x0007F80000000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000001000010L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000000L,0x00E0000000000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000002000010L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000000000L,0x01E0000000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000000000000L,0x0018000000000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000010000010L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000000000000L,0x0E00000000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x00000007A0008010L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000001FF8L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x00000007C0008010L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000700008010L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000600008010L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000000000000L,0xF000000000000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000000400008010L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000800008000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x000000000003E000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000014000004000L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000018000004000L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000020000008030L,0x0000000000000001L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000000008030L,0x0000000000000001L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000100000004000L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x0000020000000030L,0x0000000000000001L});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x0000200000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x0000401000004000L,0x0000000000000400L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x0000001000004000L,0x0000000000000400L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000001000004000L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x2000000000008030L,0x00000000000000FBL});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x2000000000008030L,0x0000000000000003L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x2000000000008010L,0x0000000000000002L});
    public static final BitSet FOLLOW_68 = new BitSet(new long[]{0x0000000000008010L,0x0000000000000002L});
    public static final BitSet FOLLOW_69 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_70 = new BitSet(new long[]{0x0001401000004000L,0x0000000000000400L});
    public static final BitSet FOLLOW_71 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_72 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_73 = new BitSet(new long[]{0x0008000000000030L,0x00000000000000F9L});
    public static final BitSet FOLLOW_74 = new BitSet(new long[]{0x0008000000000030L,0x0000000000000001L});
    public static final BitSet FOLLOW_75 = new BitSet(new long[]{0x0010000000000030L});
    public static final BitSet FOLLOW_76 = new BitSet(new long[]{0x0010000000008030L});
    public static final BitSet FOLLOW_77 = new BitSet(new long[]{0x0010000000000020L});
    public static final BitSet FOLLOW_78 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_79 = new BitSet(new long[]{0x00000000000003C0L,0x000001C000000000L});
    public static final BitSet FOLLOW_80 = new BitSet(new long[]{0x0040000040000000L});
    public static final BitSet FOLLOW_81 = new BitSet(new long[]{0x0080000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_82 = new BitSet(new long[]{0x0100000000004000L});
    public static final BitSet FOLLOW_83 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_84 = new BitSet(new long[]{0x0C00001000004000L,0x0000000000000400L});
    public static final BitSet FOLLOW_85 = new BitSet(new long[]{0x0800001000004000L,0x0000000000000400L});
    public static final BitSet FOLLOW_86 = new BitSet(new long[]{0x3286A92008108030L,0x000000000264E0FBL});
    public static final BitSet FOLLOW_87 = new BitSet(new long[]{0x3286A92008108030L,0x000000000264E003L});
    public static final BitSet FOLLOW_88 = new BitSet(new long[]{0x3286A92008108010L,0x000000000264E002L});
    public static final BitSet FOLLOW_89 = new BitSet(new long[]{0x1286A92008108010L,0x000000000264E002L});
    public static final BitSet FOLLOW_90 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_91 = new BitSet(new long[]{0x0400001000004000L,0x0000000000000400L});
    public static final BitSet FOLLOW_92 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_93 = new BitSet(new long[]{0x4000000000004000L,0x0000000000001800L});
    public static final BitSet FOLLOW_94 = new BitSet(new long[]{0x4000000000004000L,0x0000000000001000L});
    public static final BitSet FOLLOW_95 = new BitSet(new long[]{0x4000000000004000L});
    public static final BitSet FOLLOW_96 = new BitSet(new long[]{0x0000000040004000L});
    public static final BitSet FOLLOW_97 = new BitSet(new long[]{0x1286A00000108030L,0x0000000000006001L});
    public static final BitSet FOLLOW_98 = new BitSet(new long[]{0x1286A00000108010L,0x0000000000006000L});
    public static final BitSet FOLLOW_99 = new BitSet(new long[]{0x0080000000008010L,0x0000000000002000L});
    public static final BitSet FOLLOW_100 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_101 = new BitSet(new long[]{0x0000000000000020L,0x0000000000000001L});
    public static final BitSet FOLLOW_102 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_103 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_104 = new BitSet(new long[]{0x4000000000004000L,0x0000000000001804L});
    public static final BitSet FOLLOW_105 = new BitSet(new long[]{0x9286A00000108030L,0x0000000000006001L});
    public static final BitSet FOLLOW_106 = new BitSet(new long[]{0x9286A00000108010L,0x0000000000006000L});
    public static final BitSet FOLLOW_107 = new BitSet(new long[]{0x0000000000000002L,0x00000000000000F0L});
    public static final BitSet FOLLOW_108 = new BitSet(new long[]{0x0000000000000002L,0x00000000000000E0L});
    public static final BitSet FOLLOW_109 = new BitSet(new long[]{0x0000000000000002L,0x00000000000000C0L});
    public static final BitSet FOLLOW_110 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_111 = new BitSet(new long[]{0x00000000000003C2L,0x000001C000000000L});
    public static final BitSet FOLLOW_112 = new BitSet(new long[]{0x0000000000000020L,0x0000000000000100L});
    public static final BitSet FOLLOW_113 = new BitSet(new long[]{0x0000000040000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_114 = new BitSet(new long[]{0x0000001000004002L,0x0000000000000400L});
    public static final BitSet FOLLOW_115 = new BitSet(new long[]{0x0000001000004002L});
    public static final BitSet FOLLOW_116 = new BitSet(new long[]{0x0000000000004002L,0x0000000000001800L});
    public static final BitSet FOLLOW_117 = new BitSet(new long[]{0x0000000000004002L,0x0000000000001000L});
    public static final BitSet FOLLOW_118 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_119 = new BitSet(new long[]{0x0000000000008000L,0x00000000000000F8L});
    public static final BitSet FOLLOW_120 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_121 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_122 = new BitSet(new long[]{0x0000000000004000L,0x0000000000030000L});
    public static final BitSet FOLLOW_123 = new BitSet(new long[]{0x0000000000004000L,0x0000000000020000L});
    public static final BitSet FOLLOW_124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_125 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_126 = new BitSet(new long[]{0x0000000040000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_127 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_128 = new BitSet(new long[]{0x0000004000000002L});
    public static final BitSet FOLLOW_129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_130 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_131 = new BitSet(new long[]{0x0000000000008010L,0x0000000001800002L});
    public static final BitSet FOLLOW_132 = new BitSet(new long[]{0x0000000000008010L,0x0000000001000002L});
    public static final BitSet FOLLOW_133 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_134 = new BitSet(new long[]{0x0000000000008010L,0x000000001D000000L});
    public static final BitSet FOLLOW_135 = new BitSet(new long[]{0x0000000000008010L,0x000000001C000000L});
    public static final BitSet FOLLOW_136 = new BitSet(new long[]{0x0000000000008010L,0x0000000018000000L});
    public static final BitSet FOLLOW_137 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_138 = new BitSet(new long[]{0x0000000000008030L});
    public static final BitSet FOLLOW_139 = new BitSet(new long[]{0x0000000000008010L,0x0000000010000000L});
    public static final BitSet FOLLOW_140 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_141 = new BitSet(new long[]{0x0000000000004000L,0x0000000020000000L});
    public static final BitSet FOLLOW_142 = new BitSet(new long[]{0x0000000000008000L,0x00000007C0000000L});
    public static final BitSet FOLLOW_143 = new BitSet(new long[]{0x0000000000008000L,0x0000000780000000L});
    public static final BitSet FOLLOW_144 = new BitSet(new long[]{0x0000000040008000L,0x0000000700000000L});
    public static final BitSet FOLLOW_145 = new BitSet(new long[]{0x0000000000008000L,0x0000000600000000L});
    public static final BitSet FOLLOW_146 = new BitSet(new long[]{0x0000000000008000L,0x0000000400000000L});
    public static final BitSet FOLLOW_147 = new BitSet(new long[]{0x0000000000008080L});
    public static final BitSet FOLLOW_148 = new BitSet(new long[]{0x0000000040008000L});
    public static final BitSet FOLLOW_149 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_150 = new BitSet(new long[]{0x00000000000043C0L,0x000001F000000000L});
    public static final BitSet FOLLOW_151 = new BitSet(new long[]{0x0000000040000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_152 = new BitSet(new long[]{0x0000000000000002L,0x0000020000000000L});
    public static final BitSet FOLLOW_153 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
    public static final BitSet FOLLOW_154 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
    public static final BitSet FOLLOW_155 = new BitSet(new long[]{0x0000000000000240L});

}