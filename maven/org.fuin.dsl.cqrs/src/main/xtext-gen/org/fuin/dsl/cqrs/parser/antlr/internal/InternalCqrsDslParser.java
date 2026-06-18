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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_DOC", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_HEX", "RULE_DECIMAL", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "'context'", "'{'", "'}'", "'namespace'", "'import'", "'type'", "'element'", "'generics'", "'acceptable'", "'detection'", "'resolution'", "'consistency'", "'data-protection'", "'protection'", "'category'", "','", "'subject'", "'purpose'", "'lawful-basis'", "'retention'", "'then'", "'protected-by'", "'constraint'", "'input'", "'|'", "'exception'", "'message'", "'business-rule'", "'annotation'", "'cid'", "'value-object'", "'base'", "'entity-id'", "'identifies'", "'aggregate-id'", "'enum'", "'instances'", "'deprecated'", "'('", "')'", "'event'", "'copies-attributes-of'", "'entity'", "'identifier'", "'root'", "'aggregate'", "'constructor'", "'fires'", "'returns'", "'method'", "'ref'", "'slabel'", "'label'", "'tooltip'", "'prompt'", "'examples'", "'<'", "'>'", "'nullable'", "'invariants'", "'preconditions'", "'business-rules'", "'@'", "'service'", "'command'", "'target'", "'sla'", "'command-handler'", "'handles'", "'uses'", "'projection'", "'view'", "'.'", "'*'", "'false'", "'true'", "'null'", "'millis'", "'seconds'", "'minutes'", "'hours'", "'days'", "'weeks'", "'months'", "'years'", "'weak'", "'strong'", "'never'", "'manually'", "'automatic'", "'workflow'", "'none'", "'personal'", "'sensitive'", "'consent'", "'explicit_consent'", "'contract'", "'legal_obligation'", "'vital_interests'", "'public_task'", "'legitimate_interests'", "'health'", "'genetic'", "'biometric'", "'racial'", "'political'", "'religious'", "'philosophical'", "'trade_union'", "'sex_life'", "'sexual_orientation'", "'delete'", "'anonymize'", "'pseudonymize'", "'archive'", "'review'"
    };
    public static final int RULE_HEX=8;
    public static final int T__50=50;
    public static final int T__59=59;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int RULE_ID=5;
    public static final int RULE_INT=6;
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=10;
    public static final int T__67=67;
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

            	    				newCompositeNode(grammarAccess.getDomainModelAccess().getContextsContextParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_contexts_0_0=ruleContext();

            	    state._fsp--;


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


    // $ANTLR start "entryRuleContext"
    // InternalCqrsDsl.g:101:1: entryRuleContext returns [EObject current=null] : iv_ruleContext= ruleContext EOF ;
    public final EObject entryRuleContext() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContext = null;


        try {
            // InternalCqrsDsl.g:101:48: (iv_ruleContext= ruleContext EOF )
            // InternalCqrsDsl.g:102:2: iv_ruleContext= ruleContext EOF
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
    // InternalCqrsDsl.g:108:1: ruleContext returns [EObject current=null] : (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_namespaces_3_0= ruleNamespace ) )* otherlv_4= '}' ) ;
    public final EObject ruleContext() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        AntlrDatatypeRuleToken lv_name_1_0 = null;

        EObject lv_namespaces_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:114:2: ( (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_namespaces_3_0= ruleNamespace ) )* otherlv_4= '}' ) )
            // InternalCqrsDsl.g:115:2: (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_namespaces_3_0= ruleNamespace ) )* otherlv_4= '}' )
            {
            // InternalCqrsDsl.g:115:2: (otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_namespaces_3_0= ruleNamespace ) )* otherlv_4= '}' )
            // InternalCqrsDsl.g:116:3: otherlv_0= 'context' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_namespaces_3_0= ruleNamespace ) )* otherlv_4= '}'
            {
            otherlv_0=(Token)match(input,13,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getContextAccess().getContextKeyword_0());
            		
            // InternalCqrsDsl.g:120:3: ( (lv_name_1_0= ruleFQN ) )
            // InternalCqrsDsl.g:121:4: (lv_name_1_0= ruleFQN )
            {
            // InternalCqrsDsl.g:121:4: (lv_name_1_0= ruleFQN )
            // InternalCqrsDsl.g:122:5: lv_name_1_0= ruleFQN
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

            otherlv_2=(Token)match(input,14,FOLLOW_6); 

            			newLeafNode(otherlv_2, grammarAccess.getContextAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalCqrsDsl.g:143:3: ( (lv_namespaces_3_0= ruleNamespace ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==16) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalCqrsDsl.g:144:4: (lv_namespaces_3_0= ruleNamespace )
            	    {
            	    // InternalCqrsDsl.g:144:4: (lv_namespaces_3_0= ruleNamespace )
            	    // InternalCqrsDsl.g:145:5: lv_namespaces_3_0= ruleNamespace
            	    {

            	    					newCompositeNode(grammarAccess.getContextAccess().getNamespacesNamespaceParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_6);
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
            	    break loop2;
                }
            } while (true);

            otherlv_4=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getContextAccess().getRightCurlyBracketKeyword_4());
            		

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
    // InternalCqrsDsl.g:170:1: entryRuleNamespace returns [EObject current=null] : iv_ruleNamespace= ruleNamespace EOF ;
    public final EObject entryRuleNamespace() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNamespace = null;


        try {
            // InternalCqrsDsl.g:170:50: (iv_ruleNamespace= ruleNamespace EOF )
            // InternalCqrsDsl.g:171:2: iv_ruleNamespace= ruleNamespace EOF
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
    // InternalCqrsDsl.g:177:1: ruleNamespace returns [EObject current=null] : (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' ) ;
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
            // InternalCqrsDsl.g:183:2: ( (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' ) )
            // InternalCqrsDsl.g:184:2: (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:184:2: (otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}' )
            // InternalCqrsDsl.g:185:3: otherlv_0= 'namespace' ( (lv_name_1_0= ruleFQN ) ) otherlv_2= '{' ( (lv_imports_3_0= ruleImport ) )* ( (lv_elements_4_0= ruleAbstractElement ) )* otherlv_5= '}'
            {
            otherlv_0=(Token)match(input,16,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getNamespaceAccess().getNamespaceKeyword_0());
            		
            // InternalCqrsDsl.g:189:3: ( (lv_name_1_0= ruleFQN ) )
            // InternalCqrsDsl.g:190:4: (lv_name_1_0= ruleFQN )
            {
            // InternalCqrsDsl.g:190:4: (lv_name_1_0= ruleFQN )
            // InternalCqrsDsl.g:191:5: lv_name_1_0= ruleFQN
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

            otherlv_2=(Token)match(input,14,FOLLOW_7); 

            			newLeafNode(otherlv_2, grammarAccess.getNamespaceAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalCqrsDsl.g:212:3: ( (lv_imports_3_0= ruleImport ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==17) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCqrsDsl.g:213:4: (lv_imports_3_0= ruleImport )
            	    {
            	    // InternalCqrsDsl.g:213:4: (lv_imports_3_0= ruleImport )
            	    // InternalCqrsDsl.g:214:5: lv_imports_3_0= ruleImport
            	    {

            	    					newCompositeNode(grammarAccess.getNamespaceAccess().getImportsImportParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_7);
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
            	    break loop3;
                }
            } while (true);

            // InternalCqrsDsl.g:231:3: ( (lv_elements_4_0= ruleAbstractElement ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==RULE_DOC||LA4_0==18||LA4_0==25||LA4_0==35||LA4_0==38||LA4_0==41||LA4_0==43||LA4_0==45||(LA4_0>=47 && LA4_0<=48)||LA4_0==53||LA4_0==55||LA4_0==58||(LA4_0>=75 && LA4_0<=77)||LA4_0==80||(LA4_0>=83 && LA4_0<=84)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalCqrsDsl.g:232:4: (lv_elements_4_0= ruleAbstractElement )
            	    {
            	    // InternalCqrsDsl.g:232:4: (lv_elements_4_0= ruleAbstractElement )
            	    // InternalCqrsDsl.g:233:5: lv_elements_4_0= ruleAbstractElement
            	    {

            	    					newCompositeNode(grammarAccess.getNamespaceAccess().getElementsAbstractElementParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_8);
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
            	    break loop4;
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
    // InternalCqrsDsl.g:258:1: entryRuleImport returns [EObject current=null] : iv_ruleImport= ruleImport EOF ;
    public final EObject entryRuleImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImport = null;


        try {
            // InternalCqrsDsl.g:258:47: (iv_ruleImport= ruleImport EOF )
            // InternalCqrsDsl.g:259:2: iv_ruleImport= ruleImport EOF
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
    // InternalCqrsDsl.g:265:1: ruleImport returns [EObject current=null] : (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) ) ;
    public final EObject ruleImport() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        AntlrDatatypeRuleToken lv_importedNamespace_1_1 = null;

        AntlrDatatypeRuleToken lv_importedNamespace_1_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:271:2: ( (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) ) )
            // InternalCqrsDsl.g:272:2: (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) )
            {
            // InternalCqrsDsl.g:272:2: (otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) ) )
            // InternalCqrsDsl.g:273:3: otherlv_0= 'import' ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) )
            {
            otherlv_0=(Token)match(input,17,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getImportAccess().getImportKeyword_0());
            		
            // InternalCqrsDsl.g:277:3: ( ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) ) )
            // InternalCqrsDsl.g:278:4: ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) )
            {
            // InternalCqrsDsl.g:278:4: ( (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard ) )
            // InternalCqrsDsl.g:279:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )
            {
            // InternalCqrsDsl.g:279:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )
            int alt5=2;
            alt5 = dfa5.predict(input);
            switch (alt5) {
                case 1 :
                    // InternalCqrsDsl.g:280:6: lv_importedNamespace_1_1= ruleFQN
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
                    // InternalCqrsDsl.g:296:6: lv_importedNamespace_1_2= ruleFQNWithWildcard
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


    // $ANTLR start "entryRuleAbstractElement"
    // InternalCqrsDsl.g:318:1: entryRuleAbstractElement returns [EObject current=null] : iv_ruleAbstractElement= ruleAbstractElement EOF ;
    public final EObject entryRuleAbstractElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractElement = null;


        try {
            // InternalCqrsDsl.g:318:56: (iv_ruleAbstractElement= ruleAbstractElement EOF )
            // InternalCqrsDsl.g:319:2: iv_ruleAbstractElement= ruleAbstractElement EOF
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
    // InternalCqrsDsl.g:325:1: ruleAbstractElement returns [EObject current=null] : (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_DataProtection_9= ruleDataProtection ) ;
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

        EObject this_DataProtection_9 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:331:2: ( (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_DataProtection_9= ruleDataProtection ) )
            // InternalCqrsDsl.g:332:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_DataProtection_9= ruleDataProtection )
            {
            // InternalCqrsDsl.g:332:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_DataProtection_9= ruleDataProtection )
            int alt6=10;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // InternalCqrsDsl.g:333:3: this_Constraint_0= ruleConstraint
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
                    // InternalCqrsDsl.g:342:3: this_Annotation_1= ruleAnnotation
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
                    // InternalCqrsDsl.g:351:3: this_Type_2= ruleType
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
                    // InternalCqrsDsl.g:360:3: this_Exception_3= ruleException
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
                    // InternalCqrsDsl.g:369:3: this_Event_4= ruleEvent
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
                    // InternalCqrsDsl.g:378:3: this_Command_5= ruleCommand
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
                    // InternalCqrsDsl.g:387:3: this_CommandHandler_6= ruleCommandHandler
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
                    // InternalCqrsDsl.g:396:3: this_Projection_7= ruleProjection
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
                    // InternalCqrsDsl.g:405:3: this_View_8= ruleView
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
                    // InternalCqrsDsl.g:414:3: this_DataProtection_9= ruleDataProtection
                    {

                    			newCompositeNode(grammarAccess.getAbstractElementAccess().getDataProtectionParserRuleCall_9());
                    		
                    pushFollow(FOLLOW_2);
                    this_DataProtection_9=ruleDataProtection();

                    state._fsp--;


                    			current = this_DataProtection_9;
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
    // InternalCqrsDsl.g:426:1: entryRuleType returns [EObject current=null] : iv_ruleType= ruleType EOF ;
    public final EObject entryRuleType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleType = null;


        try {
            // InternalCqrsDsl.g:426:45: (iv_ruleType= ruleType EOF )
            // InternalCqrsDsl.g:427:2: iv_ruleType= ruleType EOF
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
    // InternalCqrsDsl.g:433:1: ruleType returns [EObject current=null] : (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService ) ;
    public final EObject ruleType() throws RecognitionException {
        EObject current = null;

        EObject this_ExternalType_0 = null;

        EObject this_InternalType_1 = null;

        EObject this_Service_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:439:2: ( (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService ) )
            // InternalCqrsDsl.g:440:2: (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService )
            {
            // InternalCqrsDsl.g:440:2: (this_ExternalType_0= ruleExternalType | this_InternalType_1= ruleInternalType | this_Service_2= ruleService )
            int alt7=3;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                switch ( input.LA(2) ) {
                case 43:
                case 45:
                case 47:
                case 48:
                case 55:
                case 58:
                case 75:
                    {
                    alt7=2;
                    }
                    break;
                case 76:
                    {
                    alt7=3;
                    }
                    break;
                case 18:
                    {
                    alt7=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;
                }

                }
                break;
            case 18:
                {
                alt7=1;
                }
                break;
            case 43:
            case 45:
            case 47:
            case 48:
            case 55:
            case 58:
            case 75:
                {
                alt7=2;
                }
                break;
            case 76:
                {
                alt7=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // InternalCqrsDsl.g:441:3: this_ExternalType_0= ruleExternalType
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
                    // InternalCqrsDsl.g:450:3: this_InternalType_1= ruleInternalType
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
                    // InternalCqrsDsl.g:459:3: this_Service_2= ruleService
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
    // InternalCqrsDsl.g:471:1: entryRuleInternalType returns [EObject current=null] : iv_ruleInternalType= ruleInternalType EOF ;
    public final EObject entryRuleInternalType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInternalType = null;


        try {
            // InternalCqrsDsl.g:471:53: (iv_ruleInternalType= ruleInternalType EOF )
            // InternalCqrsDsl.g:472:2: iv_ruleInternalType= ruleInternalType EOF
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
    // InternalCqrsDsl.g:478:1: ruleInternalType returns [EObject current=null] : (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject ) ;
    public final EObject ruleInternalType() throws RecognitionException {
        EObject current = null;

        EObject this_AbstractVO_0 = null;

        EObject this_AbstractEntity_1 = null;

        EObject this_EnumObject_2 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:484:2: ( (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject ) )
            // InternalCqrsDsl.g:485:2: (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject )
            {
            // InternalCqrsDsl.g:485:2: (this_AbstractVO_0= ruleAbstractVO | this_AbstractEntity_1= ruleAbstractEntity | this_EnumObject_2= ruleEnumObject )
            int alt8=3;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                switch ( input.LA(2) ) {
                case 43:
                case 45:
                case 47:
                case 75:
                    {
                    alt8=1;
                    }
                    break;
                case 55:
                case 58:
                    {
                    alt8=2;
                    }
                    break;
                case 48:
                    {
                    alt8=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;
                }

                }
                break;
            case 43:
            case 45:
            case 47:
            case 75:
                {
                alt8=1;
                }
                break;
            case 55:
            case 58:
                {
                alt8=2;
                }
                break;
            case 48:
                {
                alt8=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // InternalCqrsDsl.g:486:3: this_AbstractVO_0= ruleAbstractVO
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
                    // InternalCqrsDsl.g:495:3: this_AbstractEntity_1= ruleAbstractEntity
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
                    // InternalCqrsDsl.g:504:3: this_EnumObject_2= ruleEnumObject
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
    // InternalCqrsDsl.g:516:1: entryRuleAbstractVO returns [EObject current=null] : iv_ruleAbstractVO= ruleAbstractVO EOF ;
    public final EObject entryRuleAbstractVO() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractVO = null;


        try {
            // InternalCqrsDsl.g:516:51: (iv_ruleAbstractVO= ruleAbstractVO EOF )
            // InternalCqrsDsl.g:517:2: iv_ruleAbstractVO= ruleAbstractVO EOF
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
    // InternalCqrsDsl.g:523:1: ruleAbstractVO returns [EObject current=null] : (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId ) ;
    public final EObject ruleAbstractVO() throws RecognitionException {
        EObject current = null;

        EObject this_ValueObject_0 = null;

        EObject this_AbstractEntityId_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:529:2: ( (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId ) )
            // InternalCqrsDsl.g:530:2: (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId )
            {
            // InternalCqrsDsl.g:530:2: (this_ValueObject_0= ruleValueObject | this_AbstractEntityId_1= ruleAbstractEntityId )
            int alt9=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==45||LA9_1==47) ) {
                    alt9=2;
                }
                else if ( (LA9_1==43||LA9_1==75) ) {
                    alt9=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;
                }
                }
                break;
            case 43:
            case 75:
                {
                alt9=1;
                }
                break;
            case 45:
            case 47:
                {
                alt9=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // InternalCqrsDsl.g:531:3: this_ValueObject_0= ruleValueObject
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
                    // InternalCqrsDsl.g:540:3: this_AbstractEntityId_1= ruleAbstractEntityId
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
    // InternalCqrsDsl.g:552:1: entryRuleAbstractEntityId returns [EObject current=null] : iv_ruleAbstractEntityId= ruleAbstractEntityId EOF ;
    public final EObject entryRuleAbstractEntityId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractEntityId = null;


        try {
            // InternalCqrsDsl.g:552:57: (iv_ruleAbstractEntityId= ruleAbstractEntityId EOF )
            // InternalCqrsDsl.g:553:2: iv_ruleAbstractEntityId= ruleAbstractEntityId EOF
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
    // InternalCqrsDsl.g:559:1: ruleAbstractEntityId returns [EObject current=null] : (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId ) ;
    public final EObject ruleAbstractEntityId() throws RecognitionException {
        EObject current = null;

        EObject this_EntityId_0 = null;

        EObject this_AggregateId_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:565:2: ( (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId ) )
            // InternalCqrsDsl.g:566:2: (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId )
            {
            // InternalCqrsDsl.g:566:2: (this_EntityId_0= ruleEntityId | this_AggregateId_1= ruleAggregateId )
            int alt10=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==47) ) {
                    alt10=2;
                }
                else if ( (LA10_1==45) ) {
                    alt10=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;
                }
                }
                break;
            case 45:
                {
                alt10=1;
                }
                break;
            case 47:
                {
                alt10=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // InternalCqrsDsl.g:567:3: this_EntityId_0= ruleEntityId
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
                    // InternalCqrsDsl.g:576:3: this_AggregateId_1= ruleAggregateId
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
    // InternalCqrsDsl.g:588:1: entryRuleAbstractEntity returns [EObject current=null] : iv_ruleAbstractEntity= ruleAbstractEntity EOF ;
    public final EObject entryRuleAbstractEntity() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAbstractEntity = null;


        try {
            // InternalCqrsDsl.g:588:55: (iv_ruleAbstractEntity= ruleAbstractEntity EOF )
            // InternalCqrsDsl.g:589:2: iv_ruleAbstractEntity= ruleAbstractEntity EOF
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
    // InternalCqrsDsl.g:595:1: ruleAbstractEntity returns [EObject current=null] : (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate ) ;
    public final EObject ruleAbstractEntity() throws RecognitionException {
        EObject current = null;

        EObject this_Entity_0 = null;

        EObject this_Aggregate_1 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:601:2: ( (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate ) )
            // InternalCqrsDsl.g:602:2: (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate )
            {
            // InternalCqrsDsl.g:602:2: (this_Entity_0= ruleEntity | this_Aggregate_1= ruleAggregate )
            int alt11=2;
            switch ( input.LA(1) ) {
            case RULE_DOC:
                {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==55) ) {
                    alt11=1;
                }
                else if ( (LA11_1==58) ) {
                    alt11=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }
                }
                break;
            case 55:
                {
                alt11=1;
                }
                break;
            case 58:
                {
                alt11=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // InternalCqrsDsl.g:603:3: this_Entity_0= ruleEntity
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
                    // InternalCqrsDsl.g:612:3: this_Aggregate_1= ruleAggregate
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
    // InternalCqrsDsl.g:624:1: entryRuleExternalType returns [EObject current=null] : iv_ruleExternalType= ruleExternalType EOF ;
    public final EObject entryRuleExternalType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExternalType = null;


        try {
            // InternalCqrsDsl.g:624:53: (iv_ruleExternalType= ruleExternalType EOF )
            // InternalCqrsDsl.g:625:2: iv_ruleExternalType= ruleExternalType EOF
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
    // InternalCqrsDsl.g:631:1: ruleExternalType returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? ) ;
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
            // InternalCqrsDsl.g:637:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? ) )
            // InternalCqrsDsl.g:638:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? )
            {
            // InternalCqrsDsl.g:638:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )? )
            // InternalCqrsDsl.g:639:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'type' ( (lv_element_2_0= 'element' ) )? ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )?
            {
            // InternalCqrsDsl.g:639:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==RULE_DOC) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalCqrsDsl.g:640:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:640:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:641:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_9); 

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

            otherlv_1=(Token)match(input,18,FOLLOW_10); 

            			newLeafNode(otherlv_1, grammarAccess.getExternalTypeAccess().getTypeKeyword_1());
            		
            // InternalCqrsDsl.g:661:3: ( (lv_element_2_0= 'element' ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==19) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalCqrsDsl.g:662:4: (lv_element_2_0= 'element' )
                    {
                    // InternalCqrsDsl.g:662:4: (lv_element_2_0= 'element' )
                    // InternalCqrsDsl.g:663:5: lv_element_2_0= 'element'
                    {
                    lv_element_2_0=(Token)match(input,19,FOLLOW_4); 

                    					newLeafNode(lv_element_2_0, grammarAccess.getExternalTypeAccess().getElementElementKeyword_2_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getExternalTypeRule());
                    					}
                    					setWithLastConsumed(current, "element", lv_element_2_0, "element");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:675:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:676:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:676:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:677:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_11); 

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

            // InternalCqrsDsl.g:693:3: (otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==20) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalCqrsDsl.g:694:4: otherlv_4= 'generics' ( (lv_generics_5_0= RULE_INT ) )
                    {
                    otherlv_4=(Token)match(input,20,FOLLOW_12); 

                    				newLeafNode(otherlv_4, grammarAccess.getExternalTypeAccess().getGenericsKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:698:4: ( (lv_generics_5_0= RULE_INT ) )
                    // InternalCqrsDsl.g:699:5: (lv_generics_5_0= RULE_INT )
                    {
                    // InternalCqrsDsl.g:699:5: (lv_generics_5_0= RULE_INT )
                    // InternalCqrsDsl.g:700:6: lv_generics_5_0= RULE_INT
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
    // InternalCqrsDsl.g:721:1: entryRuleDuration returns [EObject current=null] : iv_ruleDuration= ruleDuration EOF ;
    public final EObject entryRuleDuration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDuration = null;


        try {
            // InternalCqrsDsl.g:721:49: (iv_ruleDuration= ruleDuration EOF )
            // InternalCqrsDsl.g:722:2: iv_ruleDuration= ruleDuration EOF
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
    // InternalCqrsDsl.g:728:1: ruleDuration returns [EObject current=null] : ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) ) ;
    public final EObject ruleDuration() throws RecognitionException {
        EObject current = null;

        Token lv_time_0_0=null;
        Enumerator lv_unit_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:734:2: ( ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) ) )
            // InternalCqrsDsl.g:735:2: ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) )
            {
            // InternalCqrsDsl.g:735:2: ( ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) ) )
            // InternalCqrsDsl.g:736:3: ( (lv_time_0_0= RULE_INT ) ) ( (lv_unit_1_0= ruleTimeUnit ) )
            {
            // InternalCqrsDsl.g:736:3: ( (lv_time_0_0= RULE_INT ) )
            // InternalCqrsDsl.g:737:4: (lv_time_0_0= RULE_INT )
            {
            // InternalCqrsDsl.g:737:4: (lv_time_0_0= RULE_INT )
            // InternalCqrsDsl.g:738:5: lv_time_0_0= RULE_INT
            {
            lv_time_0_0=(Token)match(input,RULE_INT,FOLLOW_13); 

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

            // InternalCqrsDsl.g:754:3: ( (lv_unit_1_0= ruleTimeUnit ) )
            // InternalCqrsDsl.g:755:4: (lv_unit_1_0= ruleTimeUnit )
            {
            // InternalCqrsDsl.g:755:4: (lv_unit_1_0= ruleTimeUnit )
            // InternalCqrsDsl.g:756:5: lv_unit_1_0= ruleTimeUnit
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
    // InternalCqrsDsl.g:777:1: entryRuleWeakConsistency returns [EObject current=null] : iv_ruleWeakConsistency= ruleWeakConsistency EOF ;
    public final EObject entryRuleWeakConsistency() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleWeakConsistency = null;


        try {
            // InternalCqrsDsl.g:777:56: (iv_ruleWeakConsistency= ruleWeakConsistency EOF )
            // InternalCqrsDsl.g:778:2: iv_ruleWeakConsistency= ruleWeakConsistency EOF
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
    // InternalCqrsDsl.g:784:1: ruleWeakConsistency returns [EObject current=null] : ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) ) ;
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
            // InternalCqrsDsl.g:790:2: ( ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) ) )
            // InternalCqrsDsl.g:791:2: ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) )
            {
            // InternalCqrsDsl.g:791:2: ( ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) ) )
            // InternalCqrsDsl.g:792:3: ( (lv_acceptableDoc_0_0= RULE_DOC ) )? otherlv_1= 'acceptable' ( (lv_acceptable_2_0= ruleDuration ) ) ( (lv_detectionDoc_3_0= RULE_DOC ) )? otherlv_4= 'detection' ( (lv_detection_5_0= ruleInconsistencyDetection ) ) ( (lv_resolutionDoc_6_0= RULE_DOC ) )? otherlv_7= 'resolution' ( (lv_resolution_8_0= ruleInconsistencyResolution ) )
            {
            // InternalCqrsDsl.g:792:3: ( (lv_acceptableDoc_0_0= RULE_DOC ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==RULE_DOC) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalCqrsDsl.g:793:4: (lv_acceptableDoc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:793:4: (lv_acceptableDoc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:794:5: lv_acceptableDoc_0_0= RULE_DOC
                    {
                    lv_acceptableDoc_0_0=(Token)match(input,RULE_DOC,FOLLOW_14); 

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

            otherlv_1=(Token)match(input,21,FOLLOW_12); 

            			newLeafNode(otherlv_1, grammarAccess.getWeakConsistencyAccess().getAcceptableKeyword_1());
            		
            // InternalCqrsDsl.g:814:3: ( (lv_acceptable_2_0= ruleDuration ) )
            // InternalCqrsDsl.g:815:4: (lv_acceptable_2_0= ruleDuration )
            {
            // InternalCqrsDsl.g:815:4: (lv_acceptable_2_0= ruleDuration )
            // InternalCqrsDsl.g:816:5: lv_acceptable_2_0= ruleDuration
            {

            					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getAcceptableDurationParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_15);
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

            // InternalCqrsDsl.g:833:3: ( (lv_detectionDoc_3_0= RULE_DOC ) )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==RULE_DOC) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalCqrsDsl.g:834:4: (lv_detectionDoc_3_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:834:4: (lv_detectionDoc_3_0= RULE_DOC )
                    // InternalCqrsDsl.g:835:5: lv_detectionDoc_3_0= RULE_DOC
                    {
                    lv_detectionDoc_3_0=(Token)match(input,RULE_DOC,FOLLOW_16); 

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

            otherlv_4=(Token)match(input,22,FOLLOW_17); 

            			newLeafNode(otherlv_4, grammarAccess.getWeakConsistencyAccess().getDetectionKeyword_4());
            		
            // InternalCqrsDsl.g:855:3: ( (lv_detection_5_0= ruleInconsistencyDetection ) )
            // InternalCqrsDsl.g:856:4: (lv_detection_5_0= ruleInconsistencyDetection )
            {
            // InternalCqrsDsl.g:856:4: (lv_detection_5_0= ruleInconsistencyDetection )
            // InternalCqrsDsl.g:857:5: lv_detection_5_0= ruleInconsistencyDetection
            {

            					newCompositeNode(grammarAccess.getWeakConsistencyAccess().getDetectionInconsistencyDetectionEnumRuleCall_5_0());
            				
            pushFollow(FOLLOW_18);
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

            // InternalCqrsDsl.g:874:3: ( (lv_resolutionDoc_6_0= RULE_DOC ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==RULE_DOC) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalCqrsDsl.g:875:4: (lv_resolutionDoc_6_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:875:4: (lv_resolutionDoc_6_0= RULE_DOC )
                    // InternalCqrsDsl.g:876:5: lv_resolutionDoc_6_0= RULE_DOC
                    {
                    lv_resolutionDoc_6_0=(Token)match(input,RULE_DOC,FOLLOW_19); 

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

            otherlv_7=(Token)match(input,23,FOLLOW_20); 

            			newLeafNode(otherlv_7, grammarAccess.getWeakConsistencyAccess().getResolutionKeyword_7());
            		
            // InternalCqrsDsl.g:896:3: ( (lv_resolution_8_0= ruleInconsistencyResolution ) )
            // InternalCqrsDsl.g:897:4: (lv_resolution_8_0= ruleInconsistencyResolution )
            {
            // InternalCqrsDsl.g:897:4: (lv_resolution_8_0= ruleInconsistencyResolution )
            // InternalCqrsDsl.g:898:5: lv_resolution_8_0= ruleInconsistencyResolution
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
    // InternalCqrsDsl.g:919:1: entryRuleConsistency returns [EObject current=null] : iv_ruleConsistency= ruleConsistency EOF ;
    public final EObject entryRuleConsistency() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConsistency = null;


        try {
            // InternalCqrsDsl.g:919:52: (iv_ruleConsistency= ruleConsistency EOF )
            // InternalCqrsDsl.g:920:2: iv_ruleConsistency= ruleConsistency EOF
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
    // InternalCqrsDsl.g:926:1: ruleConsistency returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? ) ;
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
            // InternalCqrsDsl.g:932:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? ) )
            // InternalCqrsDsl.g:933:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? )
            {
            // InternalCqrsDsl.g:933:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )? )
            // InternalCqrsDsl.g:934:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'consistency' ( (lv_level_2_0= ruleConsistencyLevel ) ) (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )?
            {
            // InternalCqrsDsl.g:934:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==RULE_DOC) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // InternalCqrsDsl.g:935:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:935:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:936:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_21); 

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

            otherlv_1=(Token)match(input,24,FOLLOW_22); 

            			newLeafNode(otherlv_1, grammarAccess.getConsistencyAccess().getConsistencyKeyword_1());
            		
            // InternalCqrsDsl.g:956:3: ( (lv_level_2_0= ruleConsistencyLevel ) )
            // InternalCqrsDsl.g:957:4: (lv_level_2_0= ruleConsistencyLevel )
            {
            // InternalCqrsDsl.g:957:4: (lv_level_2_0= ruleConsistencyLevel )
            // InternalCqrsDsl.g:958:5: lv_level_2_0= ruleConsistencyLevel
            {

            					newCompositeNode(grammarAccess.getConsistencyAccess().getLevelConsistencyLevelEnumRuleCall_2_0());
            				
            pushFollow(FOLLOW_23);
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

            // InternalCqrsDsl.g:975:3: (otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==14) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // InternalCqrsDsl.g:976:4: otherlv_3= '{' ( (lv_weakConsistency_4_0= ruleWeakConsistency ) ) otherlv_5= '}'
                    {
                    otherlv_3=(Token)match(input,14,FOLLOW_24); 

                    				newLeafNode(otherlv_3, grammarAccess.getConsistencyAccess().getLeftCurlyBracketKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:980:4: ( (lv_weakConsistency_4_0= ruleWeakConsistency ) )
                    // InternalCqrsDsl.g:981:5: (lv_weakConsistency_4_0= ruleWeakConsistency )
                    {
                    // InternalCqrsDsl.g:981:5: (lv_weakConsistency_4_0= ruleWeakConsistency )
                    // InternalCqrsDsl.g:982:6: lv_weakConsistency_4_0= ruleWeakConsistency
                    {

                    						newCompositeNode(grammarAccess.getConsistencyAccess().getWeakConsistencyWeakConsistencyParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_25);
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
    // InternalCqrsDsl.g:1008:1: entryRuleDataProtection returns [EObject current=null] : iv_ruleDataProtection= ruleDataProtection EOF ;
    public final EObject entryRuleDataProtection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataProtection = null;


        try {
            // InternalCqrsDsl.g:1008:55: (iv_ruleDataProtection= ruleDataProtection EOF )
            // InternalCqrsDsl.g:1009:2: iv_ruleDataProtection= ruleDataProtection EOF
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
    // InternalCqrsDsl.g:1015:1: ruleDataProtection returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' ) ;
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
            // InternalCqrsDsl.g:1021:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' ) )
            // InternalCqrsDsl.g:1022:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' )
            {
            // InternalCqrsDsl.g:1022:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}' )
            // InternalCqrsDsl.g:1023:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'data-protection' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_levelDoc_4_0= RULE_DOC ) )? otherlv_5= 'protection' ( (lv_level_6_0= ruleProtectionLevel ) ) ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )? ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )? ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )? ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )? ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )? otherlv_26= '}'
            {
            // InternalCqrsDsl.g:1023:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==RULE_DOC) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // InternalCqrsDsl.g:1024:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1024:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1025:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_26); 

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

            otherlv_1=(Token)match(input,25,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getDataProtectionAccess().getDataProtectionKeyword_1());
            		
            // InternalCqrsDsl.g:1045:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1046:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1046:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1047:5: lv_name_2_0= RULE_ID
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

            otherlv_3=(Token)match(input,14,FOLLOW_27); 

            			newLeafNode(otherlv_3, grammarAccess.getDataProtectionAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalCqrsDsl.g:1067:3: ( (lv_levelDoc_4_0= RULE_DOC ) )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==RULE_DOC) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalCqrsDsl.g:1068:4: (lv_levelDoc_4_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1068:4: (lv_levelDoc_4_0= RULE_DOC )
                    // InternalCqrsDsl.g:1069:5: lv_levelDoc_4_0= RULE_DOC
                    {
                    lv_levelDoc_4_0=(Token)match(input,RULE_DOC,FOLLOW_28); 

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

            otherlv_5=(Token)match(input,26,FOLLOW_29); 

            			newLeafNode(otherlv_5, grammarAccess.getDataProtectionAccess().getProtectionKeyword_5());
            		
            // InternalCqrsDsl.g:1089:3: ( (lv_level_6_0= ruleProtectionLevel ) )
            // InternalCqrsDsl.g:1090:4: (lv_level_6_0= ruleProtectionLevel )
            {
            // InternalCqrsDsl.g:1090:4: (lv_level_6_0= ruleProtectionLevel )
            // InternalCqrsDsl.g:1091:5: lv_level_6_0= ruleProtectionLevel
            {

            					newCompositeNode(grammarAccess.getDataProtectionAccess().getLevelProtectionLevelEnumRuleCall_6_0());
            				
            pushFollow(FOLLOW_30);
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

            // InternalCqrsDsl.g:1108:3: ( ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )* )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==RULE_DOC) ) {
                int LA24_1 = input.LA(2);

                if ( (LA24_1==27) ) {
                    alt24=1;
                }
            }
            else if ( (LA24_0==27) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCqrsDsl.g:1109:4: ( (lv_categoryDoc_7_0= RULE_DOC ) )? otherlv_8= 'category' ( (lv_categories_9_0= ruleSpecialCategory ) ) (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )*
                    {
                    // InternalCqrsDsl.g:1109:4: ( (lv_categoryDoc_7_0= RULE_DOC ) )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==RULE_DOC) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // InternalCqrsDsl.g:1110:5: (lv_categoryDoc_7_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1110:5: (lv_categoryDoc_7_0= RULE_DOC )
                            // InternalCqrsDsl.g:1111:6: lv_categoryDoc_7_0= RULE_DOC
                            {
                            lv_categoryDoc_7_0=(Token)match(input,RULE_DOC,FOLLOW_31); 

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

                    otherlv_8=(Token)match(input,27,FOLLOW_32); 

                    				newLeafNode(otherlv_8, grammarAccess.getDataProtectionAccess().getCategoryKeyword_7_1());
                    			
                    // InternalCqrsDsl.g:1131:4: ( (lv_categories_9_0= ruleSpecialCategory ) )
                    // InternalCqrsDsl.g:1132:5: (lv_categories_9_0= ruleSpecialCategory )
                    {
                    // InternalCqrsDsl.g:1132:5: (lv_categories_9_0= ruleSpecialCategory )
                    // InternalCqrsDsl.g:1133:6: lv_categories_9_0= ruleSpecialCategory
                    {

                    						newCompositeNode(grammarAccess.getDataProtectionAccess().getCategoriesSpecialCategoryEnumRuleCall_7_2_0());
                    					
                    pushFollow(FOLLOW_33);
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

                    // InternalCqrsDsl.g:1150:4: (otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) ) )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==28) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:1151:5: otherlv_10= ',' ( (lv_categories_11_0= ruleSpecialCategory ) )
                    	    {
                    	    otherlv_10=(Token)match(input,28,FOLLOW_32); 

                    	    					newLeafNode(otherlv_10, grammarAccess.getDataProtectionAccess().getCommaKeyword_7_3_0());
                    	    				
                    	    // InternalCqrsDsl.g:1155:5: ( (lv_categories_11_0= ruleSpecialCategory ) )
                    	    // InternalCqrsDsl.g:1156:6: (lv_categories_11_0= ruleSpecialCategory )
                    	    {
                    	    // InternalCqrsDsl.g:1156:6: (lv_categories_11_0= ruleSpecialCategory )
                    	    // InternalCqrsDsl.g:1157:7: lv_categories_11_0= ruleSpecialCategory
                    	    {

                    	    							newCompositeNode(grammarAccess.getDataProtectionAccess().getCategoriesSpecialCategoryEnumRuleCall_7_3_1_0());
                    	    						
                    	    pushFollow(FOLLOW_33);
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
                    	    break loop23;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:1176:3: ( ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) ) )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==RULE_DOC) ) {
                int LA26_1 = input.LA(2);

                if ( (LA26_1==29) ) {
                    alt26=1;
                }
            }
            else if ( (LA26_0==29) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // InternalCqrsDsl.g:1177:4: ( (lv_subjectDoc_12_0= RULE_DOC ) )? otherlv_13= 'subject' ( (lv_subject_14_0= RULE_STRING ) )
                    {
                    // InternalCqrsDsl.g:1177:4: ( (lv_subjectDoc_12_0= RULE_DOC ) )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==RULE_DOC) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // InternalCqrsDsl.g:1178:5: (lv_subjectDoc_12_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1178:5: (lv_subjectDoc_12_0= RULE_DOC )
                            // InternalCqrsDsl.g:1179:6: lv_subjectDoc_12_0= RULE_DOC
                            {
                            lv_subjectDoc_12_0=(Token)match(input,RULE_DOC,FOLLOW_34); 

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

                    otherlv_13=(Token)match(input,29,FOLLOW_35); 

                    				newLeafNode(otherlv_13, grammarAccess.getDataProtectionAccess().getSubjectKeyword_8_1());
                    			
                    // InternalCqrsDsl.g:1199:4: ( (lv_subject_14_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1200:5: (lv_subject_14_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1200:5: (lv_subject_14_0= RULE_STRING )
                    // InternalCqrsDsl.g:1201:6: lv_subject_14_0= RULE_STRING
                    {
                    lv_subject_14_0=(Token)match(input,RULE_STRING,FOLLOW_36); 

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

            // InternalCqrsDsl.g:1218:3: ( ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) ) )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==RULE_DOC) ) {
                int LA28_1 = input.LA(2);

                if ( (LA28_1==30) ) {
                    alt28=1;
                }
            }
            else if ( (LA28_0==30) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // InternalCqrsDsl.g:1219:4: ( (lv_purposeDoc_15_0= RULE_DOC ) )? otherlv_16= 'purpose' ( (lv_purpose_17_0= RULE_STRING ) )
                    {
                    // InternalCqrsDsl.g:1219:4: ( (lv_purposeDoc_15_0= RULE_DOC ) )?
                    int alt27=2;
                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==RULE_DOC) ) {
                        alt27=1;
                    }
                    switch (alt27) {
                        case 1 :
                            // InternalCqrsDsl.g:1220:5: (lv_purposeDoc_15_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1220:5: (lv_purposeDoc_15_0= RULE_DOC )
                            // InternalCqrsDsl.g:1221:6: lv_purposeDoc_15_0= RULE_DOC
                            {
                            lv_purposeDoc_15_0=(Token)match(input,RULE_DOC,FOLLOW_37); 

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

                    otherlv_16=(Token)match(input,30,FOLLOW_35); 

                    				newLeafNode(otherlv_16, grammarAccess.getDataProtectionAccess().getPurposeKeyword_9_1());
                    			
                    // InternalCqrsDsl.g:1241:4: ( (lv_purpose_17_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1242:5: (lv_purpose_17_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1242:5: (lv_purpose_17_0= RULE_STRING )
                    // InternalCqrsDsl.g:1243:6: lv_purpose_17_0= RULE_STRING
                    {
                    lv_purpose_17_0=(Token)match(input,RULE_STRING,FOLLOW_38); 

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

            // InternalCqrsDsl.g:1260:3: ( ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) ) )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==RULE_DOC) ) {
                int LA30_1 = input.LA(2);

                if ( (LA30_1==31) ) {
                    alt30=1;
                }
            }
            else if ( (LA30_0==31) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // InternalCqrsDsl.g:1261:4: ( (lv_basisDoc_18_0= RULE_DOC ) )? otherlv_19= 'lawful-basis' ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) )
                    {
                    // InternalCqrsDsl.g:1261:4: ( (lv_basisDoc_18_0= RULE_DOC ) )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0==RULE_DOC) ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // InternalCqrsDsl.g:1262:5: (lv_basisDoc_18_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1262:5: (lv_basisDoc_18_0= RULE_DOC )
                            // InternalCqrsDsl.g:1263:6: lv_basisDoc_18_0= RULE_DOC
                            {
                            lv_basisDoc_18_0=(Token)match(input,RULE_DOC,FOLLOW_39); 

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

                    otherlv_19=(Token)match(input,31,FOLLOW_40); 

                    				newLeafNode(otherlv_19, grammarAccess.getDataProtectionAccess().getLawfulBasisKeyword_10_1());
                    			
                    // InternalCqrsDsl.g:1283:4: ( (lv_lawfulBasis_20_0= ruleLawfulBasis ) )
                    // InternalCqrsDsl.g:1284:5: (lv_lawfulBasis_20_0= ruleLawfulBasis )
                    {
                    // InternalCqrsDsl.g:1284:5: (lv_lawfulBasis_20_0= ruleLawfulBasis )
                    // InternalCqrsDsl.g:1285:6: lv_lawfulBasis_20_0= ruleLawfulBasis
                    {

                    						newCompositeNode(grammarAccess.getDataProtectionAccess().getLawfulBasisLawfulBasisEnumRuleCall_10_2_0());
                    					
                    pushFollow(FOLLOW_41);
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

            // InternalCqrsDsl.g:1303:3: ( ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )? )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==RULE_DOC||LA33_0==32) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalCqrsDsl.g:1304:4: ( (lv_retentionDoc_21_0= RULE_DOC ) )? otherlv_22= 'retention' ( (lv_retention_23_0= ruleDuration ) ) (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )?
                    {
                    // InternalCqrsDsl.g:1304:4: ( (lv_retentionDoc_21_0= RULE_DOC ) )?
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==RULE_DOC) ) {
                        alt31=1;
                    }
                    switch (alt31) {
                        case 1 :
                            // InternalCqrsDsl.g:1305:5: (lv_retentionDoc_21_0= RULE_DOC )
                            {
                            // InternalCqrsDsl.g:1305:5: (lv_retentionDoc_21_0= RULE_DOC )
                            // InternalCqrsDsl.g:1306:6: lv_retentionDoc_21_0= RULE_DOC
                            {
                            lv_retentionDoc_21_0=(Token)match(input,RULE_DOC,FOLLOW_42); 

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

                    otherlv_22=(Token)match(input,32,FOLLOW_12); 

                    				newLeafNode(otherlv_22, grammarAccess.getDataProtectionAccess().getRetentionKeyword_11_1());
                    			
                    // InternalCqrsDsl.g:1326:4: ( (lv_retention_23_0= ruleDuration ) )
                    // InternalCqrsDsl.g:1327:5: (lv_retention_23_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:1327:5: (lv_retention_23_0= ruleDuration )
                    // InternalCqrsDsl.g:1328:6: lv_retention_23_0= ruleDuration
                    {

                    						newCompositeNode(grammarAccess.getDataProtectionAccess().getRetentionDurationParserRuleCall_11_2_0());
                    					
                    pushFollow(FOLLOW_43);
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

                    // InternalCqrsDsl.g:1345:4: (otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) ) )?
                    int alt32=2;
                    int LA32_0 = input.LA(1);

                    if ( (LA32_0==33) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // InternalCqrsDsl.g:1346:5: otherlv_24= 'then' ( (lv_erasure_25_0= ruleErasureStrategy ) )
                            {
                            otherlv_24=(Token)match(input,33,FOLLOW_44); 

                            					newLeafNode(otherlv_24, grammarAccess.getDataProtectionAccess().getThenKeyword_11_3_0());
                            				
                            // InternalCqrsDsl.g:1350:5: ( (lv_erasure_25_0= ruleErasureStrategy ) )
                            // InternalCqrsDsl.g:1351:6: (lv_erasure_25_0= ruleErasureStrategy )
                            {
                            // InternalCqrsDsl.g:1351:6: (lv_erasure_25_0= ruleErasureStrategy )
                            // InternalCqrsDsl.g:1352:7: lv_erasure_25_0= ruleErasureStrategy
                            {

                            							newCompositeNode(grammarAccess.getDataProtectionAccess().getErasureErasureStrategyEnumRuleCall_11_3_1_0());
                            						
                            pushFollow(FOLLOW_25);
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
    // InternalCqrsDsl.g:1379:1: entryRuleDataProtectionInstance returns [EObject current=null] : iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF ;
    public final EObject entryRuleDataProtectionInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataProtectionInstance = null;


        try {
            // InternalCqrsDsl.g:1379:63: (iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF )
            // InternalCqrsDsl.g:1380:2: iv_ruleDataProtectionInstance= ruleDataProtectionInstance EOF
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
    // InternalCqrsDsl.g:1386:1: ruleDataProtectionInstance returns [EObject current=null] : (otherlv_0= 'protected-by' ( ( ruleFQN ) ) ) ;
    public final EObject ruleDataProtectionInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:1392:2: ( (otherlv_0= 'protected-by' ( ( ruleFQN ) ) ) )
            // InternalCqrsDsl.g:1393:2: (otherlv_0= 'protected-by' ( ( ruleFQN ) ) )
            {
            // InternalCqrsDsl.g:1393:2: (otherlv_0= 'protected-by' ( ( ruleFQN ) ) )
            // InternalCqrsDsl.g:1394:3: otherlv_0= 'protected-by' ( ( ruleFQN ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getDataProtectionInstanceAccess().getProtectedByKeyword_0());
            		
            // InternalCqrsDsl.g:1398:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:1399:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:1399:4: ( ruleFQN )
            // InternalCqrsDsl.g:1400:5: ruleFQN
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
    // InternalCqrsDsl.g:1418:1: entryRuleConstraint returns [EObject current=null] : iv_ruleConstraint= ruleConstraint EOF ;
    public final EObject entryRuleConstraint() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstraint = null;


        try {
            // InternalCqrsDsl.g:1418:51: (iv_ruleConstraint= ruleConstraint EOF )
            // InternalCqrsDsl.g:1419:2: iv_ruleConstraint= ruleConstraint EOF
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
    // InternalCqrsDsl.g:1425:1: ruleConstraint returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) ;
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
            // InternalCqrsDsl.g:1431:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' ) )
            // InternalCqrsDsl.g:1432:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:1432:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}' )
            // InternalCqrsDsl.g:1433:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constraint' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )? (otherlv_7= 'exception' ( ( ruleFQN ) ) )? otherlv_9= '{' ( (lv_attributes_10_0= ruleAttribute ) )* (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )? otherlv_13= '}'
            {
            // InternalCqrsDsl.g:1433:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==RULE_DOC) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // InternalCqrsDsl.g:1434:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1434:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1435:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_45); 

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

            otherlv_1=(Token)match(input,35,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getConstraintAccess().getConstraintKeyword_1());
            		
            // InternalCqrsDsl.g:1455:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1456:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1456:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1457:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_46); 

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

            // InternalCqrsDsl.g:1473:3: (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )* )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==36) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // InternalCqrsDsl.g:1474:4: otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= '|' ( ( ruleFQN ) ) )*
                    {
                    otherlv_3=(Token)match(input,36,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getConstraintAccess().getInputKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:1478:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:1479:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:1479:5: ( ruleFQN )
                    // InternalCqrsDsl.g:1480:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConstraintRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getConstraintAccess().getInputTypeCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_47);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:1494:4: (otherlv_5= '|' ( ( ruleFQN ) ) )*
                    loop35:
                    do {
                        int alt35=2;
                        int LA35_0 = input.LA(1);

                        if ( (LA35_0==37) ) {
                            alt35=1;
                        }


                        switch (alt35) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:1495:5: otherlv_5= '|' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_5=(Token)match(input,37,FOLLOW_4); 

                    	    					newLeafNode(otherlv_5, grammarAccess.getConstraintAccess().getVerticalLineKeyword_3_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:1499:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:1500:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:1500:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:1501:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getConstraintRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getConstraintAccess().getInputTypeCrossReference_3_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_47);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop35;
                        }
                    } while (true);


                    }
                    break;

            }

            // InternalCqrsDsl.g:1517:3: (otherlv_7= 'exception' ( ( ruleFQN ) ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==38) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalCqrsDsl.g:1518:4: otherlv_7= 'exception' ( ( ruleFQN ) )
                    {
                    otherlv_7=(Token)match(input,38,FOLLOW_4); 

                    				newLeafNode(otherlv_7, grammarAccess.getConstraintAccess().getExceptionKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:1522:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:1523:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:1523:5: ( ruleFQN )
                    // InternalCqrsDsl.g:1524:6: ruleFQN
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

            otherlv_9=(Token)match(input,14,FOLLOW_48); 

            			newLeafNode(otherlv_9, grammarAccess.getConstraintAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:1543:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop38:
            do {
                int alt38=2;
                int LA38_0 = input.LA(1);

                if ( ((LA38_0>=RULE_DOC && LA38_0<=RULE_ID)||LA38_0==71) ) {
                    alt38=1;
                }


                switch (alt38) {
            	case 1 :
            	    // InternalCqrsDsl.g:1544:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:1544:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:1545:5: lv_attributes_10_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getConstraintAccess().getAttributesAttributeParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_48);
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
            	    break loop38;
                }
            } while (true);

            // InternalCqrsDsl.g:1562:3: (otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) ) )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==39) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // InternalCqrsDsl.g:1563:4: otherlv_11= 'message' ( (lv_message_12_0= RULE_STRING ) )
                    {
                    otherlv_11=(Token)match(input,39,FOLLOW_35); 

                    				newLeafNode(otherlv_11, grammarAccess.getConstraintAccess().getMessageKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:1567:4: ( (lv_message_12_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:1568:5: (lv_message_12_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:1568:5: (lv_message_12_0= RULE_STRING )
                    // InternalCqrsDsl.g:1569:6: lv_message_12_0= RULE_STRING
                    {
                    lv_message_12_0=(Token)match(input,RULE_STRING,FOLLOW_25); 

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
    // InternalCqrsDsl.g:1594:1: entryRuleBusinessRule returns [EObject current=null] : iv_ruleBusinessRule= ruleBusinessRule EOF ;
    public final EObject entryRuleBusinessRule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRule = null;


        try {
            // InternalCqrsDsl.g:1594:53: (iv_ruleBusinessRule= ruleBusinessRule EOF )
            // InternalCqrsDsl.g:1595:2: iv_ruleBusinessRule= ruleBusinessRule EOF
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
    // InternalCqrsDsl.g:1601:1: ruleBusinessRule returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' ) ;
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
            // InternalCqrsDsl.g:1607:2: ( ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' ) )
            // InternalCqrsDsl.g:1608:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' )
            {
            // InternalCqrsDsl.g:1608:2: ( ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}' )
            // InternalCqrsDsl.g:1609:3: ( (lv_doc_0_0= RULE_DOC ) ) otherlv_1= 'business-rule' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'exception' ( ( ruleFQN ) ) otherlv_5= '{' ( (lv_consistency_6_0= ruleConsistency ) ) otherlv_7= '}'
            {
            // InternalCqrsDsl.g:1609:3: ( (lv_doc_0_0= RULE_DOC ) )
            // InternalCqrsDsl.g:1610:4: (lv_doc_0_0= RULE_DOC )
            {
            // InternalCqrsDsl.g:1610:4: (lv_doc_0_0= RULE_DOC )
            // InternalCqrsDsl.g:1611:5: lv_doc_0_0= RULE_DOC
            {
            lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_49); 

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

            otherlv_1=(Token)match(input,40,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getBusinessRuleAccess().getBusinessRuleKeyword_1());
            		
            // InternalCqrsDsl.g:1631:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1632:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1632:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1633:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_50); 

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

            otherlv_3=(Token)match(input,38,FOLLOW_4); 

            			newLeafNode(otherlv_3, grammarAccess.getBusinessRuleAccess().getExceptionKeyword_3());
            		
            // InternalCqrsDsl.g:1653:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:1654:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:1654:4: ( ruleFQN )
            // InternalCqrsDsl.g:1655:5: ruleFQN
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

            otherlv_5=(Token)match(input,14,FOLLOW_51); 

            			newLeafNode(otherlv_5, grammarAccess.getBusinessRuleAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:1673:3: ( (lv_consistency_6_0= ruleConsistency ) )
            // InternalCqrsDsl.g:1674:4: (lv_consistency_6_0= ruleConsistency )
            {
            // InternalCqrsDsl.g:1674:4: (lv_consistency_6_0= ruleConsistency )
            // InternalCqrsDsl.g:1675:5: lv_consistency_6_0= ruleConsistency
            {

            					newCompositeNode(grammarAccess.getBusinessRuleAccess().getConsistencyConsistencyParserRuleCall_6_0());
            				
            pushFollow(FOLLOW_25);
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
    // InternalCqrsDsl.g:1700:1: entryRuleAnnotation returns [EObject current=null] : iv_ruleAnnotation= ruleAnnotation EOF ;
    public final EObject entryRuleAnnotation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnnotation = null;


        try {
            // InternalCqrsDsl.g:1700:51: (iv_ruleAnnotation= ruleAnnotation EOF )
            // InternalCqrsDsl.g:1701:2: iv_ruleAnnotation= ruleAnnotation EOF
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
    // InternalCqrsDsl.g:1707:1: ruleAnnotation returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' ) ;
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
            // InternalCqrsDsl.g:1713:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' ) )
            // InternalCqrsDsl.g:1714:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' )
            {
            // InternalCqrsDsl.g:1714:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}' )
            // InternalCqrsDsl.g:1715:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'annotation' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_attributes_4_0= ruleAttribute ) )* otherlv_5= '}'
            {
            // InternalCqrsDsl.g:1715:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==RULE_DOC) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalCqrsDsl.g:1716:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1716:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1717:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_52); 

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

            otherlv_1=(Token)match(input,41,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getAnnotationAccess().getAnnotationKeyword_1());
            		
            // InternalCqrsDsl.g:1737:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1738:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1738:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1739:5: lv_name_2_0= RULE_ID
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

            otherlv_3=(Token)match(input,14,FOLLOW_53); 

            			newLeafNode(otherlv_3, grammarAccess.getAnnotationAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalCqrsDsl.g:1759:3: ( (lv_attributes_4_0= ruleAttribute ) )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( ((LA41_0>=RULE_DOC && LA41_0<=RULE_ID)||LA41_0==71) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // InternalCqrsDsl.g:1760:4: (lv_attributes_4_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:1760:4: (lv_attributes_4_0= ruleAttribute )
            	    // InternalCqrsDsl.g:1761:5: lv_attributes_4_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAnnotationAccess().getAttributesAttributeParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_53);
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
            	    break loop41;
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
    // InternalCqrsDsl.g:1786:1: entryRuleException returns [EObject current=null] : iv_ruleException= ruleException EOF ;
    public final EObject entryRuleException() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleException = null;


        try {
            // InternalCqrsDsl.g:1786:50: (iv_ruleException= ruleException EOF )
            // InternalCqrsDsl.g:1787:2: iv_ruleException= ruleException EOF
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
    // InternalCqrsDsl.g:1793:1: ruleException returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' ) ;
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
            // InternalCqrsDsl.g:1799:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' ) )
            // InternalCqrsDsl.g:1800:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' )
            {
            // InternalCqrsDsl.g:1800:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}' )
            // InternalCqrsDsl.g:1801:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'exception' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )? otherlv_5= '{' ( (lv_attributes_6_0= ruleAttribute ) )* otherlv_7= 'message' ( (lv_message_8_0= RULE_STRING ) ) otherlv_9= '}'
            {
            // InternalCqrsDsl.g:1801:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt42=2;
            int LA42_0 = input.LA(1);

            if ( (LA42_0==RULE_DOC) ) {
                alt42=1;
            }
            switch (alt42) {
                case 1 :
                    // InternalCqrsDsl.g:1802:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1802:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1803:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_50); 

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

            otherlv_1=(Token)match(input,38,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getExceptionAccess().getExceptionKeyword_1());
            		
            // InternalCqrsDsl.g:1823:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:1824:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1824:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:1825:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_54); 

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

            // InternalCqrsDsl.g:1841:3: (otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) ) )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==42) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // InternalCqrsDsl.g:1842:4: otherlv_3= 'cid' ( (lv_cid_4_0= RULE_INT ) )
                    {
                    otherlv_3=(Token)match(input,42,FOLLOW_12); 

                    				newLeafNode(otherlv_3, grammarAccess.getExceptionAccess().getCidKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:1846:4: ( (lv_cid_4_0= RULE_INT ) )
                    // InternalCqrsDsl.g:1847:5: (lv_cid_4_0= RULE_INT )
                    {
                    // InternalCqrsDsl.g:1847:5: (lv_cid_4_0= RULE_INT )
                    // InternalCqrsDsl.g:1848:6: lv_cid_4_0= RULE_INT
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

            otherlv_5=(Token)match(input,14,FOLLOW_55); 

            			newLeafNode(otherlv_5, grammarAccess.getExceptionAccess().getLeftCurlyBracketKeyword_4());
            		
            // InternalCqrsDsl.g:1869:3: ( (lv_attributes_6_0= ruleAttribute ) )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);

                if ( ((LA44_0>=RULE_DOC && LA44_0<=RULE_ID)||LA44_0==71) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // InternalCqrsDsl.g:1870:4: (lv_attributes_6_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:1870:4: (lv_attributes_6_0= ruleAttribute )
            	    // InternalCqrsDsl.g:1871:5: lv_attributes_6_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getExceptionAccess().getAttributesAttributeParserRuleCall_5_0());
            	    				
            	    pushFollow(FOLLOW_55);
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
            	    break loop44;
                }
            } while (true);

            otherlv_7=(Token)match(input,39,FOLLOW_35); 

            			newLeafNode(otherlv_7, grammarAccess.getExceptionAccess().getMessageKeyword_6());
            		
            // InternalCqrsDsl.g:1892:3: ( (lv_message_8_0= RULE_STRING ) )
            // InternalCqrsDsl.g:1893:4: (lv_message_8_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:1893:4: (lv_message_8_0= RULE_STRING )
            // InternalCqrsDsl.g:1894:5: lv_message_8_0= RULE_STRING
            {
            lv_message_8_0=(Token)match(input,RULE_STRING,FOLLOW_25); 

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
    // InternalCqrsDsl.g:1918:1: entryRuleValueObject returns [EObject current=null] : iv_ruleValueObject= ruleValueObject EOF ;
    public final EObject entryRuleValueObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleValueObject = null;


        try {
            // InternalCqrsDsl.g:1918:52: (iv_ruleValueObject= ruleValueObject EOF )
            // InternalCqrsDsl.g:1919:2: iv_ruleValueObject= ruleValueObject EOF
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
    // InternalCqrsDsl.g:1925:1: ruleValueObject returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' ) ;
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
            // InternalCqrsDsl.g:1931:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' ) )
            // InternalCqrsDsl.g:1932:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' )
            {
            // InternalCqrsDsl.g:1932:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}' )
            // InternalCqrsDsl.g:1933:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'value-object' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_6_0= ruleInvariants ) )? ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )? otherlv_8= '{' ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) ) ( (lv_attributes_10_0= ruleAttribute ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* otherlv_13= '}'
            {
            // InternalCqrsDsl.g:1933:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==RULE_DOC) ) {
                alt45=1;
            }
            switch (alt45) {
                case 1 :
                    // InternalCqrsDsl.g:1934:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:1934:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:1935:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_56); 

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

            // InternalCqrsDsl.g:1951:3: ( (lv_annotations_1_0= ruleAnnotationInstance ) )*
            loop46:
            do {
                int alt46=2;
                int LA46_0 = input.LA(1);

                if ( (LA46_0==75) ) {
                    alt46=1;
                }


                switch (alt46) {
            	case 1 :
            	    // InternalCqrsDsl.g:1952:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    {
            	    // InternalCqrsDsl.g:1952:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    // InternalCqrsDsl.g:1953:5: lv_annotations_1_0= ruleAnnotationInstance
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getAnnotationsAnnotationInstanceParserRuleCall_1_0());
            	    				
            	    pushFollow(FOLLOW_56);
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
            	    break loop46;
                }
            } while (true);

            otherlv_2=(Token)match(input,43,FOLLOW_4); 

            			newLeafNode(otherlv_2, grammarAccess.getValueObjectAccess().getValueObjectKeyword_2());
            		
            // InternalCqrsDsl.g:1974:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:1975:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:1975:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:1976:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_57); 

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

            // InternalCqrsDsl.g:1992:3: (otherlv_4= 'base' ( ( ruleFQN ) ) )?
            int alt47=2;
            int LA47_0 = input.LA(1);

            if ( (LA47_0==44) ) {
                alt47=1;
            }
            switch (alt47) {
                case 1 :
                    // InternalCqrsDsl.g:1993:4: otherlv_4= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_4=(Token)match(input,44,FOLLOW_4); 

                    				newLeafNode(otherlv_4, grammarAccess.getValueObjectAccess().getBaseKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:1997:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:1998:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:1998:5: ( ruleFQN )
                    // InternalCqrsDsl.g:1999:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getValueObjectRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getValueObjectAccess().getBaseExternalTypeCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_58);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2014:3: ( (lv_invariants_6_0= ruleInvariants ) )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==72) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // InternalCqrsDsl.g:2015:4: (lv_invariants_6_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2015:4: (lv_invariants_6_0= ruleInvariants )
                    // InternalCqrsDsl.g:2016:5: lv_invariants_6_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getValueObjectAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_59);
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

            // InternalCqrsDsl.g:2033:3: ( (lv_dataProtection_7_0= ruleDataProtectionInstance ) )?
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==34) ) {
                alt49=1;
            }
            switch (alt49) {
                case 1 :
                    // InternalCqrsDsl.g:2034:4: (lv_dataProtection_7_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2034:4: (lv_dataProtection_7_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2035:5: lv_dataProtection_7_0= ruleDataProtectionInstance
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

            otherlv_8=(Token)match(input,14,FOLLOW_60); 

            			newLeafNode(otherlv_8, grammarAccess.getValueObjectAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:2056:3: ( (lv_metaInfo_9_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2057:4: (lv_metaInfo_9_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2057:4: (lv_metaInfo_9_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2058:5: lv_metaInfo_9_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getValueObjectAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_61);
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

            // InternalCqrsDsl.g:2075:3: ( (lv_attributes_10_0= ruleAttribute ) )*
            loop50:
            do {
                int alt50=2;
                int LA50_0 = input.LA(1);

                if ( (LA50_0==RULE_DOC) ) {
                    int LA50_1 = input.LA(2);

                    if ( (LA50_1==RULE_ID||LA50_1==71) ) {
                        alt50=1;
                    }


                }
                else if ( (LA50_0==RULE_ID||LA50_0==71) ) {
                    alt50=1;
                }


                switch (alt50) {
            	case 1 :
            	    // InternalCqrsDsl.g:2076:4: (lv_attributes_10_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2076:4: (lv_attributes_10_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2077:5: lv_attributes_10_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_61);
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
            	    break loop50;
                }
            } while (true);

            // InternalCqrsDsl.g:2094:3: ( (lv_constructors_11_0= ruleConstructor ) )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==RULE_DOC) ) {
                    int LA51_1 = input.LA(2);

                    if ( (LA51_1==59) ) {
                        alt51=1;
                    }


                }
                else if ( (LA51_0==59) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // InternalCqrsDsl.g:2095:4: (lv_constructors_11_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:2095:4: (lv_constructors_11_0= ruleConstructor )
            	    // InternalCqrsDsl.g:2096:5: lv_constructors_11_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_62);
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
            	    break loop51;
                }
            } while (true);

            // InternalCqrsDsl.g:2113:3: ( (lv_methods_12_0= ruleMethod ) )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==RULE_DOC||LA52_0==62) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // InternalCqrsDsl.g:2114:4: (lv_methods_12_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:2114:4: (lv_methods_12_0= ruleMethod )
            	    // InternalCqrsDsl.g:2115:5: lv_methods_12_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getValueObjectAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_63);
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
            	    break loop52;
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
    // InternalCqrsDsl.g:2140:1: entryRuleEntityId returns [EObject current=null] : iv_ruleEntityId= ruleEntityId EOF ;
    public final EObject entryRuleEntityId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntityId = null;


        try {
            // InternalCqrsDsl.g:2140:49: (iv_ruleEntityId= ruleEntityId EOF )
            // InternalCqrsDsl.g:2141:2: iv_ruleEntityId= ruleEntityId EOF
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
    // InternalCqrsDsl.g:2147:1: ruleEntityId returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) ;
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
            // InternalCqrsDsl.g:2153:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:2154:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:2154:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:2155:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:2155:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==RULE_DOC) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // InternalCqrsDsl.g:2156:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2156:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2157:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_64); 

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

            otherlv_1=(Token)match(input,45,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getEntityIdAccess().getEntityIdKeyword_1());
            		
            // InternalCqrsDsl.g:2177:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2178:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2178:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2179:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_65); 

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

            // InternalCqrsDsl.g:2195:3: (otherlv_3= 'identifies' ( ( ruleFQN ) ) )?
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==46) ) {
                alt54=1;
            }
            switch (alt54) {
                case 1 :
                    // InternalCqrsDsl.g:2196:4: otherlv_3= 'identifies' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,46,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getEntityIdAccess().getIdentifiesKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2200:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2201:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2201:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2202:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityIdAccess().getEntityEntityCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_57);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2217:3: (otherlv_5= 'base' ( ( ruleFQN ) ) )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==44) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // InternalCqrsDsl.g:2218:4: otherlv_5= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,44,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getEntityIdAccess().getBaseKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:2222:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2223:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2223:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2224:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityIdAccess().getBaseExternalTypeCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_58);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2239:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==72) ) {
                alt56=1;
            }
            switch (alt56) {
                case 1 :
                    // InternalCqrsDsl.g:2240:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2240:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:2241:5: lv_invariants_7_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getEntityIdAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_59);
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

            // InternalCqrsDsl.g:2258:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt57=2;
            int LA57_0 = input.LA(1);

            if ( (LA57_0==34) ) {
                alt57=1;
            }
            switch (alt57) {
                case 1 :
                    // InternalCqrsDsl.g:2259:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2259:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2260:5: lv_dataProtection_8_0= ruleDataProtectionInstance
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

            otherlv_9=(Token)match(input,14,FOLLOW_60); 

            			newLeafNode(otherlv_9, grammarAccess.getEntityIdAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:2281:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2282:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2282:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2283:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getEntityIdAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_61);
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

            // InternalCqrsDsl.g:2300:3: ( (lv_attributes_11_0= ruleAttribute ) )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==RULE_DOC) ) {
                    int LA58_1 = input.LA(2);

                    if ( (LA58_1==RULE_ID||LA58_1==71) ) {
                        alt58=1;
                    }


                }
                else if ( (LA58_0==RULE_ID||LA58_0==71) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // InternalCqrsDsl.g:2301:4: (lv_attributes_11_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2301:4: (lv_attributes_11_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2302:5: lv_attributes_11_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEntityIdAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_61);
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
            	    break loop58;
                }
            } while (true);

            // InternalCqrsDsl.g:2319:3: ( (lv_constructors_12_0= ruleConstructor ) )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==RULE_DOC) ) {
                    int LA59_1 = input.LA(2);

                    if ( (LA59_1==59) ) {
                        alt59=1;
                    }


                }
                else if ( (LA59_0==59) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // InternalCqrsDsl.g:2320:4: (lv_constructors_12_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:2320:4: (lv_constructors_12_0= ruleConstructor )
            	    // InternalCqrsDsl.g:2321:5: lv_constructors_12_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getEntityIdAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_62);
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
            	    break loop59;
                }
            } while (true);

            // InternalCqrsDsl.g:2338:3: ( (lv_methods_13_0= ruleMethod ) )*
            loop60:
            do {
                int alt60=2;
                int LA60_0 = input.LA(1);

                if ( (LA60_0==RULE_DOC||LA60_0==62) ) {
                    alt60=1;
                }


                switch (alt60) {
            	case 1 :
            	    // InternalCqrsDsl.g:2339:4: (lv_methods_13_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:2339:4: (lv_methods_13_0= ruleMethod )
            	    // InternalCqrsDsl.g:2340:5: lv_methods_13_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getEntityIdAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_63);
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
            	    break loop60;
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
    // InternalCqrsDsl.g:2365:1: entryRuleAggregateId returns [EObject current=null] : iv_ruleAggregateId= ruleAggregateId EOF ;
    public final EObject entryRuleAggregateId() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregateId = null;


        try {
            // InternalCqrsDsl.g:2365:52: (iv_ruleAggregateId= ruleAggregateId EOF )
            // InternalCqrsDsl.g:2366:2: iv_ruleAggregateId= ruleAggregateId EOF
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
    // InternalCqrsDsl.g:2372:1: ruleAggregateId returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) ;
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
            // InternalCqrsDsl.g:2378:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:2379:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:2379:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:2380:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate-id' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifies' ( ( ruleFQN ) ) )? (otherlv_5= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_constructors_12_0= ruleConstructor ) )* ( (lv_methods_13_0= ruleMethod ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:2380:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt61=2;
            int LA61_0 = input.LA(1);

            if ( (LA61_0==RULE_DOC) ) {
                alt61=1;
            }
            switch (alt61) {
                case 1 :
                    // InternalCqrsDsl.g:2381:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2381:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2382:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_66); 

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

            otherlv_1=(Token)match(input,47,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregateIdAccess().getAggregateIdKeyword_1());
            		
            // InternalCqrsDsl.g:2402:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2403:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2403:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2404:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_65); 

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

            // InternalCqrsDsl.g:2420:3: (otherlv_3= 'identifies' ( ( ruleFQN ) ) )?
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==46) ) {
                alt62=1;
            }
            switch (alt62) {
                case 1 :
                    // InternalCqrsDsl.g:2421:4: otherlv_3= 'identifies' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,46,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getAggregateIdAccess().getIdentifiesKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2425:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2426:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2426:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2427:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregateIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getAggregateIdAccess().getAggregateAggregateCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_57);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2442:3: (otherlv_5= 'base' ( ( ruleFQN ) ) )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==44) ) {
                alt63=1;
            }
            switch (alt63) {
                case 1 :
                    // InternalCqrsDsl.g:2443:4: otherlv_5= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,44,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getAggregateIdAccess().getBaseKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:2447:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2448:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2448:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2449:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregateIdRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getAggregateIdAccess().getBaseExternalTypeCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_58);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2464:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt64=2;
            int LA64_0 = input.LA(1);

            if ( (LA64_0==72) ) {
                alt64=1;
            }
            switch (alt64) {
                case 1 :
                    // InternalCqrsDsl.g:2465:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2465:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:2466:5: lv_invariants_7_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getAggregateIdAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_59);
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

            // InternalCqrsDsl.g:2483:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==34) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // InternalCqrsDsl.g:2484:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2484:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2485:5: lv_dataProtection_8_0= ruleDataProtectionInstance
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

            otherlv_9=(Token)match(input,14,FOLLOW_60); 

            			newLeafNode(otherlv_9, grammarAccess.getAggregateIdAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:2506:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2507:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2507:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2508:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getAggregateIdAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_61);
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

            // InternalCqrsDsl.g:2525:3: ( (lv_attributes_11_0= ruleAttribute ) )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==RULE_DOC) ) {
                    int LA66_1 = input.LA(2);

                    if ( (LA66_1==RULE_ID||LA66_1==71) ) {
                        alt66=1;
                    }


                }
                else if ( (LA66_0==RULE_ID||LA66_0==71) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // InternalCqrsDsl.g:2526:4: (lv_attributes_11_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2526:4: (lv_attributes_11_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2527:5: lv_attributes_11_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateIdAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_61);
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
            	    break loop66;
                }
            } while (true);

            // InternalCqrsDsl.g:2544:3: ( (lv_constructors_12_0= ruleConstructor ) )*
            loop67:
            do {
                int alt67=2;
                int LA67_0 = input.LA(1);

                if ( (LA67_0==RULE_DOC) ) {
                    int LA67_1 = input.LA(2);

                    if ( (LA67_1==59) ) {
                        alt67=1;
                    }


                }
                else if ( (LA67_0==59) ) {
                    alt67=1;
                }


                switch (alt67) {
            	case 1 :
            	    // InternalCqrsDsl.g:2545:4: (lv_constructors_12_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:2545:4: (lv_constructors_12_0= ruleConstructor )
            	    // InternalCqrsDsl.g:2546:5: lv_constructors_12_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateIdAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_62);
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
            	    break loop67;
                }
            } while (true);

            // InternalCqrsDsl.g:2563:3: ( (lv_methods_13_0= ruleMethod ) )*
            loop68:
            do {
                int alt68=2;
                int LA68_0 = input.LA(1);

                if ( (LA68_0==RULE_DOC||LA68_0==62) ) {
                    alt68=1;
                }


                switch (alt68) {
            	case 1 :
            	    // InternalCqrsDsl.g:2564:4: (lv_methods_13_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:2564:4: (lv_methods_13_0= ruleMethod )
            	    // InternalCqrsDsl.g:2565:5: lv_methods_13_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateIdAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_63);
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
            	    break loop68;
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
    // InternalCqrsDsl.g:2590:1: entryRuleEnumObject returns [EObject current=null] : iv_ruleEnumObject= ruleEnumObject EOF ;
    public final EObject entryRuleEnumObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumObject = null;


        try {
            // InternalCqrsDsl.g:2590:51: (iv_ruleEnumObject= ruleEnumObject EOF )
            // InternalCqrsDsl.g:2591:2: iv_ruleEnumObject= ruleEnumObject EOF
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
    // InternalCqrsDsl.g:2597:1: ruleEnumObject returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' ) ;
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
            // InternalCqrsDsl.g:2603:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' ) )
            // InternalCqrsDsl.g:2604:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:2604:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}' )
            // InternalCqrsDsl.g:2605:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'enum' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'base' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* otherlv_10= 'instances' otherlv_11= '{' ( (lv_instances_12_0= ruleEnumInstance ) )+ otherlv_13= '}' otherlv_14= '}'
            {
            // InternalCqrsDsl.g:2605:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==RULE_DOC) ) {
                alt69=1;
            }
            switch (alt69) {
                case 1 :
                    // InternalCqrsDsl.g:2606:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2606:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2607:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_67); 

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

            otherlv_1=(Token)match(input,48,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getEnumObjectAccess().getEnumKeyword_1());
            		
            // InternalCqrsDsl.g:2627:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2628:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2628:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2629:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_57); 

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

            // InternalCqrsDsl.g:2645:3: (otherlv_3= 'base' ( ( ruleFQN ) ) )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==44) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // InternalCqrsDsl.g:2646:4: otherlv_3= 'base' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,44,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getEnumObjectAccess().getBaseKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2650:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2651:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2651:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2652:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEnumObjectRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEnumObjectAccess().getBaseExternalTypeCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_58);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2667:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt71=2;
            int LA71_0 = input.LA(1);

            if ( (LA71_0==72) ) {
                alt71=1;
            }
            switch (alt71) {
                case 1 :
                    // InternalCqrsDsl.g:2668:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:2668:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:2669:5: lv_invariants_5_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getEnumObjectAccess().getInvariantsInvariantsParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_59);
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

            // InternalCqrsDsl.g:2686:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==34) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // InternalCqrsDsl.g:2687:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:2687:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:2688:5: lv_dataProtection_6_0= ruleDataProtectionInstance
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

            otherlv_7=(Token)match(input,14,FOLLOW_68); 

            			newLeafNode(otherlv_7, grammarAccess.getEnumObjectAccess().getLeftCurlyBracketKeyword_6());
            		
            // InternalCqrsDsl.g:2709:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:2710:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:2710:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:2711:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getEnumObjectAccess().getMetaInfoTypeMetaInfoParserRuleCall_7_0());
            				
            pushFollow(FOLLOW_69);
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

            // InternalCqrsDsl.g:2728:3: ( (lv_attributes_9_0= ruleAttribute ) )*
            loop73:
            do {
                int alt73=2;
                int LA73_0 = input.LA(1);

                if ( ((LA73_0>=RULE_DOC && LA73_0<=RULE_ID)||LA73_0==71) ) {
                    alt73=1;
                }


                switch (alt73) {
            	case 1 :
            	    // InternalCqrsDsl.g:2729:4: (lv_attributes_9_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:2729:4: (lv_attributes_9_0= ruleAttribute )
            	    // InternalCqrsDsl.g:2730:5: lv_attributes_9_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEnumObjectAccess().getAttributesAttributeParserRuleCall_8_0());
            	    				
            	    pushFollow(FOLLOW_69);
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
            	    break loop73;
                }
            } while (true);

            otherlv_10=(Token)match(input,49,FOLLOW_5); 

            			newLeafNode(otherlv_10, grammarAccess.getEnumObjectAccess().getInstancesKeyword_9());
            		
            otherlv_11=(Token)match(input,14,FOLLOW_70); 

            			newLeafNode(otherlv_11, grammarAccess.getEnumObjectAccess().getLeftCurlyBracketKeyword_10());
            		
            // InternalCqrsDsl.g:2755:3: ( (lv_instances_12_0= ruleEnumInstance ) )+
            int cnt74=0;
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( ((LA74_0>=RULE_DOC && LA74_0<=RULE_ID)||LA74_0==50) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // InternalCqrsDsl.g:2756:4: (lv_instances_12_0= ruleEnumInstance )
            	    {
            	    // InternalCqrsDsl.g:2756:4: (lv_instances_12_0= ruleEnumInstance )
            	    // InternalCqrsDsl.g:2757:5: lv_instances_12_0= ruleEnumInstance
            	    {

            	    					newCompositeNode(grammarAccess.getEnumObjectAccess().getInstancesEnumInstanceParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_71);
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
            	    if ( cnt74 >= 1 ) break loop74;
                        EarlyExitException eee =
                            new EarlyExitException(74, input);
                        throw eee;
                }
                cnt74++;
            } while (true);

            otherlv_13=(Token)match(input,15,FOLLOW_25); 

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
    // InternalCqrsDsl.g:2786:1: entryRuleEnumInstance returns [EObject current=null] : iv_ruleEnumInstance= ruleEnumInstance EOF ;
    public final EObject entryRuleEnumInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumInstance = null;


        try {
            // InternalCqrsDsl.g:2786:53: (iv_ruleEnumInstance= ruleEnumInstance EOF )
            // InternalCqrsDsl.g:2787:2: iv_ruleEnumInstance= ruleEnumInstance EOF
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
    // InternalCqrsDsl.g:2793:1: ruleEnumInstance returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ) ;
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
            // InternalCqrsDsl.g:2799:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? ) )
            // InternalCqrsDsl.g:2800:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? )
            {
            // InternalCqrsDsl.g:2800:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )? )
            // InternalCqrsDsl.g:2801:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_deprecated_1_0= 'deprecated' ) )? ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )?
            {
            // InternalCqrsDsl.g:2801:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt75=2;
            int LA75_0 = input.LA(1);

            if ( (LA75_0==RULE_DOC) ) {
                alt75=1;
            }
            switch (alt75) {
                case 1 :
                    // InternalCqrsDsl.g:2802:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2802:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2803:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_72); 

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

            // InternalCqrsDsl.g:2819:3: ( (lv_deprecated_1_0= 'deprecated' ) )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==50) ) {
                alt76=1;
            }
            switch (alt76) {
                case 1 :
                    // InternalCqrsDsl.g:2820:4: (lv_deprecated_1_0= 'deprecated' )
                    {
                    // InternalCqrsDsl.g:2820:4: (lv_deprecated_1_0= 'deprecated' )
                    // InternalCqrsDsl.g:2821:5: lv_deprecated_1_0= 'deprecated'
                    {
                    lv_deprecated_1_0=(Token)match(input,50,FOLLOW_4); 

                    					newLeafNode(lv_deprecated_1_0, grammarAccess.getEnumInstanceAccess().getDeprecatedDeprecatedKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getEnumInstanceRule());
                    					}
                    					setWithLastConsumed(current, "deprecated", lv_deprecated_1_0, "deprecated");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:2833:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:2834:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2834:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:2835:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_73); 

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

            // InternalCqrsDsl.g:2851:3: (otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')' )?
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( (LA78_0==51) ) {
                alt78=1;
            }
            switch (alt78) {
                case 1 :
                    // InternalCqrsDsl.g:2852:4: otherlv_3= '(' ( (lv_params_4_0= ruleLiteral ) ) (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )* otherlv_7= ')'
                    {
                    otherlv_3=(Token)match(input,51,FOLLOW_74); 

                    				newLeafNode(otherlv_3, grammarAccess.getEnumInstanceAccess().getLeftParenthesisKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:2856:4: ( (lv_params_4_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:2857:5: (lv_params_4_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:2857:5: (lv_params_4_0= ruleLiteral )
                    // InternalCqrsDsl.g:2858:6: lv_params_4_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getEnumInstanceAccess().getParamsLiteralParserRuleCall_3_1_0());
                    					
                    pushFollow(FOLLOW_75);
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

                    // InternalCqrsDsl.g:2875:4: (otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) ) )*
                    loop77:
                    do {
                        int alt77=2;
                        int LA77_0 = input.LA(1);

                        if ( (LA77_0==28) ) {
                            alt77=1;
                        }


                        switch (alt77) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:2876:5: otherlv_5= ',' ( (lv_params_6_0= ruleLiteral ) )
                    	    {
                    	    otherlv_5=(Token)match(input,28,FOLLOW_74); 

                    	    					newLeafNode(otherlv_5, grammarAccess.getEnumInstanceAccess().getCommaKeyword_3_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:2880:5: ( (lv_params_6_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:2881:6: (lv_params_6_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:2881:6: (lv_params_6_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:2882:7: lv_params_6_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getEnumInstanceAccess().getParamsLiteralParserRuleCall_3_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_75);
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
                    	    break loop77;
                        }
                    } while (true);

                    otherlv_7=(Token)match(input,52,FOLLOW_2); 

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
    // InternalCqrsDsl.g:2909:1: entryRuleEvent returns [EObject current=null] : iv_ruleEvent= ruleEvent EOF ;
    public final EObject entryRuleEvent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEvent = null;


        try {
            // InternalCqrsDsl.g:2909:46: (iv_ruleEvent= ruleEvent EOF )
            // InternalCqrsDsl.g:2910:2: iv_ruleEvent= ruleEvent EOF
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
    // InternalCqrsDsl.g:2916:1: ruleEvent returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' ) ;
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
            // InternalCqrsDsl.g:2922:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' ) )
            // InternalCqrsDsl.g:2923:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' )
            {
            // InternalCqrsDsl.g:2923:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}' )
            // InternalCqrsDsl.g:2924:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_annotations_1_0= ruleAnnotationInstance ) )* otherlv_2= 'event' ( (lv_name_3_0= RULE_ID ) ) (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )? otherlv_6= '{' ( (lv_attributes_7_0= ruleAttribute ) )* (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )? otherlv_10= '}'
            {
            // InternalCqrsDsl.g:2924:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt79=2;
            int LA79_0 = input.LA(1);

            if ( (LA79_0==RULE_DOC) ) {
                alt79=1;
            }
            switch (alt79) {
                case 1 :
                    // InternalCqrsDsl.g:2925:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:2925:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:2926:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_76); 

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

            // InternalCqrsDsl.g:2942:3: ( (lv_annotations_1_0= ruleAnnotationInstance ) )*
            loop80:
            do {
                int alt80=2;
                int LA80_0 = input.LA(1);

                if ( (LA80_0==75) ) {
                    alt80=1;
                }


                switch (alt80) {
            	case 1 :
            	    // InternalCqrsDsl.g:2943:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    {
            	    // InternalCqrsDsl.g:2943:4: (lv_annotations_1_0= ruleAnnotationInstance )
            	    // InternalCqrsDsl.g:2944:5: lv_annotations_1_0= ruleAnnotationInstance
            	    {

            	    					newCompositeNode(grammarAccess.getEventAccess().getAnnotationsAnnotationInstanceParserRuleCall_1_0());
            	    				
            	    pushFollow(FOLLOW_76);
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
            	    break loop80;
                }
            } while (true);

            otherlv_2=(Token)match(input,53,FOLLOW_4); 

            			newLeafNode(otherlv_2, grammarAccess.getEventAccess().getEventKeyword_2());
            		
            // InternalCqrsDsl.g:2965:3: ( (lv_name_3_0= RULE_ID ) )
            // InternalCqrsDsl.g:2966:4: (lv_name_3_0= RULE_ID )
            {
            // InternalCqrsDsl.g:2966:4: (lv_name_3_0= RULE_ID )
            // InternalCqrsDsl.g:2967:5: lv_name_3_0= RULE_ID
            {
            lv_name_3_0=(Token)match(input,RULE_ID,FOLLOW_77); 

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

            // InternalCqrsDsl.g:2983:3: (otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) ) )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( (LA81_0==54) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // InternalCqrsDsl.g:2984:4: otherlv_4= 'copies-attributes-of' ( ( ruleFQN ) )
                    {
                    otherlv_4=(Token)match(input,54,FOLLOW_4); 

                    				newLeafNode(otherlv_4, grammarAccess.getEventAccess().getCopiesAttributesOfKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:2988:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:2989:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:2989:5: ( ruleFQN )
                    // InternalCqrsDsl.g:2990:6: ruleFQN
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

            otherlv_6=(Token)match(input,14,FOLLOW_48); 

            			newLeafNode(otherlv_6, grammarAccess.getEventAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:3009:3: ( (lv_attributes_7_0= ruleAttribute ) )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( ((LA82_0>=RULE_DOC && LA82_0<=RULE_ID)||LA82_0==71) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // InternalCqrsDsl.g:3010:4: (lv_attributes_7_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3010:4: (lv_attributes_7_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3011:5: lv_attributes_7_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEventAccess().getAttributesAttributeParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_48);
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
            	    break loop82;
                }
            } while (true);

            // InternalCqrsDsl.g:3028:3: (otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) ) )?
            int alt83=2;
            int LA83_0 = input.LA(1);

            if ( (LA83_0==39) ) {
                alt83=1;
            }
            switch (alt83) {
                case 1 :
                    // InternalCqrsDsl.g:3029:4: otherlv_8= 'message' ( (lv_message_9_0= RULE_STRING ) )
                    {
                    otherlv_8=(Token)match(input,39,FOLLOW_35); 

                    				newLeafNode(otherlv_8, grammarAccess.getEventAccess().getMessageKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:3033:4: ( (lv_message_9_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:3034:5: (lv_message_9_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:3034:5: (lv_message_9_0= RULE_STRING )
                    // InternalCqrsDsl.g:3035:6: lv_message_9_0= RULE_STRING
                    {
                    lv_message_9_0=(Token)match(input,RULE_STRING,FOLLOW_25); 

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
    // InternalCqrsDsl.g:3060:1: entryRuleEntity returns [EObject current=null] : iv_ruleEntity= ruleEntity EOF ;
    public final EObject entryRuleEntity() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntity = null;


        try {
            // InternalCqrsDsl.g:3060:47: (iv_ruleEntity= ruleEntity EOF )
            // InternalCqrsDsl.g:3061:2: iv_ruleEntity= ruleEntity EOF
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
    // InternalCqrsDsl.g:3067:1: ruleEntity returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' ) ;
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
            // InternalCqrsDsl.g:3073:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' ) )
            // InternalCqrsDsl.g:3074:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' )
            {
            // InternalCqrsDsl.g:3074:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}' )
            // InternalCqrsDsl.g:3075:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'entity' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? (otherlv_5= 'root' ( ( ruleFQN ) ) )? ( (lv_invariants_7_0= ruleInvariants ) )? ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )? otherlv_9= '{' ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) ) ( (lv_attributes_11_0= ruleAttribute ) )* ( (lv_businessRules_12_0= ruleBusinessRule ) )* ( (lv_constructors_13_0= ruleConstructor ) )* ( (lv_methods_14_0= ruleMethod ) )* ( (lv_elements_15_0= ruleAbstractElement ) )* otherlv_16= '}'
            {
            // InternalCqrsDsl.g:3075:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt84=2;
            int LA84_0 = input.LA(1);

            if ( (LA84_0==RULE_DOC) ) {
                alt84=1;
            }
            switch (alt84) {
                case 1 :
                    // InternalCqrsDsl.g:3076:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3076:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3077:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_78); 

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

            otherlv_1=(Token)match(input,55,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getEntityAccess().getEntityKeyword_1());
            		
            // InternalCqrsDsl.g:3097:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3098:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3098:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3099:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_79); 

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

            // InternalCqrsDsl.g:3115:3: (otherlv_3= 'identifier' ( ( ruleFQN ) ) )?
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( (LA85_0==56) ) {
                alt85=1;
            }
            switch (alt85) {
                case 1 :
                    // InternalCqrsDsl.g:3116:4: otherlv_3= 'identifier' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,56,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getEntityAccess().getIdentifierKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:3120:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3121:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3121:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3122:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityAccess().getIdTypeEntityIdCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_80);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3137:3: (otherlv_5= 'root' ( ( ruleFQN ) ) )?
            int alt86=2;
            int LA86_0 = input.LA(1);

            if ( (LA86_0==57) ) {
                alt86=1;
            }
            switch (alt86) {
                case 1 :
                    // InternalCqrsDsl.g:3138:4: otherlv_5= 'root' ( ( ruleFQN ) )
                    {
                    otherlv_5=(Token)match(input,57,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getEntityAccess().getRootKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:3142:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3143:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3143:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3144:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getEntityAccess().getRootAggregateCrossReference_4_1_0());
                    					
                    pushFollow(FOLLOW_58);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3159:3: ( (lv_invariants_7_0= ruleInvariants ) )?
            int alt87=2;
            int LA87_0 = input.LA(1);

            if ( (LA87_0==72) ) {
                alt87=1;
            }
            switch (alt87) {
                case 1 :
                    // InternalCqrsDsl.g:3160:4: (lv_invariants_7_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:3160:4: (lv_invariants_7_0= ruleInvariants )
                    // InternalCqrsDsl.g:3161:5: lv_invariants_7_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getEntityAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_59);
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

            // InternalCqrsDsl.g:3178:3: ( (lv_dataProtection_8_0= ruleDataProtectionInstance ) )?
            int alt88=2;
            int LA88_0 = input.LA(1);

            if ( (LA88_0==34) ) {
                alt88=1;
            }
            switch (alt88) {
                case 1 :
                    // InternalCqrsDsl.g:3179:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:3179:4: (lv_dataProtection_8_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:3180:5: lv_dataProtection_8_0= ruleDataProtectionInstance
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

            otherlv_9=(Token)match(input,14,FOLLOW_81); 

            			newLeafNode(otherlv_9, grammarAccess.getEntityAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:3201:3: ( (lv_metaInfo_10_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:3202:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:3202:4: (lv_metaInfo_10_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:3203:5: lv_metaInfo_10_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getEntityAccess().getMetaInfoTypeMetaInfoParserRuleCall_8_0());
            				
            pushFollow(FOLLOW_82);
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

            // InternalCqrsDsl.g:3220:3: ( (lv_attributes_11_0= ruleAttribute ) )*
            loop89:
            do {
                int alt89=2;
                int LA89_0 = input.LA(1);

                if ( (LA89_0==RULE_DOC) ) {
                    int LA89_1 = input.LA(2);

                    if ( (LA89_1==RULE_ID||LA89_1==71) ) {
                        alt89=1;
                    }


                }
                else if ( (LA89_0==RULE_ID||LA89_0==71) ) {
                    alt89=1;
                }


                switch (alt89) {
            	case 1 :
            	    // InternalCqrsDsl.g:3221:4: (lv_attributes_11_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3221:4: (lv_attributes_11_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3222:5: lv_attributes_11_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getAttributesAttributeParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_82);
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
            	    break loop89;
                }
            } while (true);

            // InternalCqrsDsl.g:3239:3: ( (lv_businessRules_12_0= ruleBusinessRule ) )*
            loop90:
            do {
                int alt90=2;
                int LA90_0 = input.LA(1);

                if ( (LA90_0==RULE_DOC) ) {
                    int LA90_1 = input.LA(2);

                    if ( (LA90_1==40) ) {
                        alt90=1;
                    }


                }


                switch (alt90) {
            	case 1 :
            	    // InternalCqrsDsl.g:3240:4: (lv_businessRules_12_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:3240:4: (lv_businessRules_12_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:3241:5: lv_businessRules_12_0= ruleBusinessRule
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getBusinessRulesBusinessRuleParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_83);
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
            	    break loop90;
                }
            } while (true);

            // InternalCqrsDsl.g:3258:3: ( (lv_constructors_13_0= ruleConstructor ) )*
            loop91:
            do {
                int alt91=2;
                int LA91_0 = input.LA(1);

                if ( (LA91_0==RULE_DOC) ) {
                    int LA91_1 = input.LA(2);

                    if ( (LA91_1==59) ) {
                        alt91=1;
                    }


                }
                else if ( (LA91_0==59) ) {
                    alt91=1;
                }


                switch (alt91) {
            	case 1 :
            	    // InternalCqrsDsl.g:3259:4: (lv_constructors_13_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:3259:4: (lv_constructors_13_0= ruleConstructor )
            	    // InternalCqrsDsl.g:3260:5: lv_constructors_13_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getConstructorsConstructorParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_83);
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
            	    break loop91;
                }
            } while (true);

            // InternalCqrsDsl.g:3277:3: ( (lv_methods_14_0= ruleMethod ) )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==RULE_DOC) ) {
                    int LA92_1 = input.LA(2);

                    if ( (LA92_1==62) ) {
                        alt92=1;
                    }


                }
                else if ( (LA92_0==62) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // InternalCqrsDsl.g:3278:4: (lv_methods_14_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:3278:4: (lv_methods_14_0= ruleMethod )
            	    // InternalCqrsDsl.g:3279:5: lv_methods_14_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getMethodsMethodParserRuleCall_12_0());
            	    				
            	    pushFollow(FOLLOW_84);
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
            	    break loop92;
                }
            } while (true);

            // InternalCqrsDsl.g:3296:3: ( (lv_elements_15_0= ruleAbstractElement ) )*
            loop93:
            do {
                int alt93=2;
                int LA93_0 = input.LA(1);

                if ( (LA93_0==RULE_DOC||LA93_0==18||LA93_0==25||LA93_0==35||LA93_0==38||LA93_0==41||LA93_0==43||LA93_0==45||(LA93_0>=47 && LA93_0<=48)||LA93_0==53||LA93_0==55||LA93_0==58||(LA93_0>=75 && LA93_0<=77)||LA93_0==80||(LA93_0>=83 && LA93_0<=84)) ) {
                    alt93=1;
                }


                switch (alt93) {
            	case 1 :
            	    // InternalCqrsDsl.g:3297:4: (lv_elements_15_0= ruleAbstractElement )
            	    {
            	    // InternalCqrsDsl.g:3297:4: (lv_elements_15_0= ruleAbstractElement )
            	    // InternalCqrsDsl.g:3298:5: lv_elements_15_0= ruleAbstractElement
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getElementsAbstractElementParserRuleCall_13_0());
            	    				
            	    pushFollow(FOLLOW_8);
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
            	    break loop93;
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
    // InternalCqrsDsl.g:3323:1: entryRuleAggregate returns [EObject current=null] : iv_ruleAggregate= ruleAggregate EOF ;
    public final EObject entryRuleAggregate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAggregate = null;


        try {
            // InternalCqrsDsl.g:3323:50: (iv_ruleAggregate= ruleAggregate EOF )
            // InternalCqrsDsl.g:3324:2: iv_ruleAggregate= ruleAggregate EOF
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
    // InternalCqrsDsl.g:3330:1: ruleAggregate returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' ) ;
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
            // InternalCqrsDsl.g:3336:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:3337:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:3337:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:3338:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'aggregate' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'identifier' ( ( ruleFQN ) ) )? ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? otherlv_7= '{' ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) ) ( (lv_attributes_9_0= ruleAttribute ) )* ( (lv_businessRules_10_0= ruleBusinessRule ) )* ( (lv_constructors_11_0= ruleConstructor ) )* ( (lv_methods_12_0= ruleMethod ) )* ( (lv_elements_13_0= ruleAbstractElement ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:3338:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt94=2;
            int LA94_0 = input.LA(1);

            if ( (LA94_0==RULE_DOC) ) {
                alt94=1;
            }
            switch (alt94) {
                case 1 :
                    // InternalCqrsDsl.g:3339:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3339:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3340:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_85); 

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

            otherlv_1=(Token)match(input,58,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getAggregateAccess().getAggregateKeyword_1());
            		
            // InternalCqrsDsl.g:3360:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3361:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3361:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3362:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_86); 

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

            // InternalCqrsDsl.g:3378:3: (otherlv_3= 'identifier' ( ( ruleFQN ) ) )?
            int alt95=2;
            int LA95_0 = input.LA(1);

            if ( (LA95_0==56) ) {
                alt95=1;
            }
            switch (alt95) {
                case 1 :
                    // InternalCqrsDsl.g:3379:4: otherlv_3= 'identifier' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,56,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getAggregateAccess().getIdentifierKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:3383:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3384:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3384:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3385:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getAggregateRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getAggregateAccess().getIdTypeAggregateIdCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_58);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3400:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt96=2;
            int LA96_0 = input.LA(1);

            if ( (LA96_0==72) ) {
                alt96=1;
            }
            switch (alt96) {
                case 1 :
                    // InternalCqrsDsl.g:3401:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:3401:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:3402:5: lv_invariants_5_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getAggregateAccess().getInvariantsInvariantsParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_59);
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

            // InternalCqrsDsl.g:3419:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt97=2;
            int LA97_0 = input.LA(1);

            if ( (LA97_0==34) ) {
                alt97=1;
            }
            switch (alt97) {
                case 1 :
                    // InternalCqrsDsl.g:3420:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:3420:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:3421:5: lv_dataProtection_6_0= ruleDataProtectionInstance
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

            otherlv_7=(Token)match(input,14,FOLLOW_81); 

            			newLeafNode(otherlv_7, grammarAccess.getAggregateAccess().getLeftCurlyBracketKeyword_6());
            		
            // InternalCqrsDsl.g:3442:3: ( (lv_metaInfo_8_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:3443:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:3443:4: (lv_metaInfo_8_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:3444:5: lv_metaInfo_8_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getAggregateAccess().getMetaInfoTypeMetaInfoParserRuleCall_7_0());
            				
            pushFollow(FOLLOW_82);
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

            // InternalCqrsDsl.g:3461:3: ( (lv_attributes_9_0= ruleAttribute ) )*
            loop98:
            do {
                int alt98=2;
                int LA98_0 = input.LA(1);

                if ( (LA98_0==RULE_DOC) ) {
                    int LA98_1 = input.LA(2);

                    if ( (LA98_1==RULE_ID||LA98_1==71) ) {
                        alt98=1;
                    }


                }
                else if ( (LA98_0==RULE_ID||LA98_0==71) ) {
                    alt98=1;
                }


                switch (alt98) {
            	case 1 :
            	    // InternalCqrsDsl.g:3462:4: (lv_attributes_9_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:3462:4: (lv_attributes_9_0= ruleAttribute )
            	    // InternalCqrsDsl.g:3463:5: lv_attributes_9_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getAttributesAttributeParserRuleCall_8_0());
            	    				
            	    pushFollow(FOLLOW_82);
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
            	    break loop98;
                }
            } while (true);

            // InternalCqrsDsl.g:3480:3: ( (lv_businessRules_10_0= ruleBusinessRule ) )*
            loop99:
            do {
                int alt99=2;
                int LA99_0 = input.LA(1);

                if ( (LA99_0==RULE_DOC) ) {
                    int LA99_1 = input.LA(2);

                    if ( (LA99_1==40) ) {
                        alt99=1;
                    }


                }


                switch (alt99) {
            	case 1 :
            	    // InternalCqrsDsl.g:3481:4: (lv_businessRules_10_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:3481:4: (lv_businessRules_10_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:3482:5: lv_businessRules_10_0= ruleBusinessRule
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getBusinessRulesBusinessRuleParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_83);
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
            	    break loop99;
                }
            } while (true);

            // InternalCqrsDsl.g:3499:3: ( (lv_constructors_11_0= ruleConstructor ) )*
            loop100:
            do {
                int alt100=2;
                int LA100_0 = input.LA(1);

                if ( (LA100_0==RULE_DOC) ) {
                    int LA100_1 = input.LA(2);

                    if ( (LA100_1==59) ) {
                        alt100=1;
                    }


                }
                else if ( (LA100_0==59) ) {
                    alt100=1;
                }


                switch (alt100) {
            	case 1 :
            	    // InternalCqrsDsl.g:3500:4: (lv_constructors_11_0= ruleConstructor )
            	    {
            	    // InternalCqrsDsl.g:3500:4: (lv_constructors_11_0= ruleConstructor )
            	    // InternalCqrsDsl.g:3501:5: lv_constructors_11_0= ruleConstructor
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getConstructorsConstructorParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_83);
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
            	    break loop100;
                }
            } while (true);

            // InternalCqrsDsl.g:3518:3: ( (lv_methods_12_0= ruleMethod ) )*
            loop101:
            do {
                int alt101=2;
                int LA101_0 = input.LA(1);

                if ( (LA101_0==RULE_DOC) ) {
                    int LA101_1 = input.LA(2);

                    if ( (LA101_1==62) ) {
                        alt101=1;
                    }


                }
                else if ( (LA101_0==62) ) {
                    alt101=1;
                }


                switch (alt101) {
            	case 1 :
            	    // InternalCqrsDsl.g:3519:4: (lv_methods_12_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:3519:4: (lv_methods_12_0= ruleMethod )
            	    // InternalCqrsDsl.g:3520:5: lv_methods_12_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getMethodsMethodParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_84);
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
            	    break loop101;
                }
            } while (true);

            // InternalCqrsDsl.g:3537:3: ( (lv_elements_13_0= ruleAbstractElement ) )*
            loop102:
            do {
                int alt102=2;
                int LA102_0 = input.LA(1);

                if ( (LA102_0==RULE_DOC||LA102_0==18||LA102_0==25||LA102_0==35||LA102_0==38||LA102_0==41||LA102_0==43||LA102_0==45||(LA102_0>=47 && LA102_0<=48)||LA102_0==53||LA102_0==55||LA102_0==58||(LA102_0>=75 && LA102_0<=77)||LA102_0==80||(LA102_0>=83 && LA102_0<=84)) ) {
                    alt102=1;
                }


                switch (alt102) {
            	case 1 :
            	    // InternalCqrsDsl.g:3538:4: (lv_elements_13_0= ruleAbstractElement )
            	    {
            	    // InternalCqrsDsl.g:3538:4: (lv_elements_13_0= ruleAbstractElement )
            	    // InternalCqrsDsl.g:3539:5: lv_elements_13_0= ruleAbstractElement
            	    {

            	    					newCompositeNode(grammarAccess.getAggregateAccess().getElementsAbstractElementParserRuleCall_12_0());
            	    				
            	    pushFollow(FOLLOW_8);
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
            	    break loop102;
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
    // InternalCqrsDsl.g:3564:1: entryRuleConstructor returns [EObject current=null] : iv_ruleConstructor= ruleConstructor EOF ;
    public final EObject entryRuleConstructor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstructor = null;


        try {
            // InternalCqrsDsl.g:3564:52: (iv_ruleConstructor= ruleConstructor EOF )
            // InternalCqrsDsl.g:3565:2: iv_ruleConstructor= ruleConstructor EOF
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
    // InternalCqrsDsl.g:3571:1: ruleConstructor returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' ) ;
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
            // InternalCqrsDsl.g:3577:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' ) )
            // InternalCqrsDsl.g:3578:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' )
            {
            // InternalCqrsDsl.g:3578:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}' )
            // InternalCqrsDsl.g:3579:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'constructor' ( (lv_name_2_0= RULE_ID ) ) ( (lv_preconditions_3_0= rulePreconditions ) )? ( (lv_businessRules_4_0= ruleBusinessRules ) )? (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )? otherlv_9= '{' ( (lv_parameters_10_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_services_12_0= ruleService ) )* ( (lv_events_13_0= ruleEvent ) )* otherlv_14= '}'
            {
            // InternalCqrsDsl.g:3579:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt103=2;
            int LA103_0 = input.LA(1);

            if ( (LA103_0==RULE_DOC) ) {
                alt103=1;
            }
            switch (alt103) {
                case 1 :
                    // InternalCqrsDsl.g:3580:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3580:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3581:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_87); 

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

            otherlv_1=(Token)match(input,59,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getConstructorAccess().getConstructorKeyword_1());
            		
            // InternalCqrsDsl.g:3601:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3602:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3602:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3603:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_88); 

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

            // InternalCqrsDsl.g:3619:3: ( (lv_preconditions_3_0= rulePreconditions ) )?
            int alt104=2;
            int LA104_0 = input.LA(1);

            if ( (LA104_0==73) ) {
                alt104=1;
            }
            switch (alt104) {
                case 1 :
                    // InternalCqrsDsl.g:3620:4: (lv_preconditions_3_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:3620:4: (lv_preconditions_3_0= rulePreconditions )
                    // InternalCqrsDsl.g:3621:5: lv_preconditions_3_0= rulePreconditions
                    {

                    					newCompositeNode(grammarAccess.getConstructorAccess().getPreconditionsPreconditionsParserRuleCall_3_0());
                    				
                    pushFollow(FOLLOW_89);
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

            // InternalCqrsDsl.g:3638:3: ( (lv_businessRules_4_0= ruleBusinessRules ) )?
            int alt105=2;
            int LA105_0 = input.LA(1);

            if ( (LA105_0==74) ) {
                alt105=1;
            }
            switch (alt105) {
                case 1 :
                    // InternalCqrsDsl.g:3639:4: (lv_businessRules_4_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:3639:4: (lv_businessRules_4_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:3640:5: lv_businessRules_4_0= ruleBusinessRules
                    {

                    					newCompositeNode(grammarAccess.getConstructorAccess().getBusinessRulesBusinessRulesParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_90);
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

            // InternalCqrsDsl.g:3657:3: (otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )* )?
            int alt107=2;
            int LA107_0 = input.LA(1);

            if ( (LA107_0==60) ) {
                alt107=1;
            }
            switch (alt107) {
                case 1 :
                    // InternalCqrsDsl.g:3658:4: otherlv_5= 'fires' ( ( ruleFQN ) ) (otherlv_7= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_5=(Token)match(input,60,FOLLOW_4); 

                    				newLeafNode(otherlv_5, grammarAccess.getConstructorAccess().getFiresKeyword_5_0());
                    			
                    // InternalCqrsDsl.g:3662:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3663:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3663:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3664:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getConstructorRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getConstructorAccess().getFiredEventsEventCrossReference_5_1_0());
                    					
                    pushFollow(FOLLOW_91);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:3678:4: (otherlv_7= ',' ( ( ruleFQN ) ) )*
                    loop106:
                    do {
                        int alt106=2;
                        int LA106_0 = input.LA(1);

                        if ( (LA106_0==28) ) {
                            alt106=1;
                        }


                        switch (alt106) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:3679:5: otherlv_7= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_7=(Token)match(input,28,FOLLOW_4); 

                    	    					newLeafNode(otherlv_7, grammarAccess.getConstructorAccess().getCommaKeyword_5_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:3683:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:3684:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:3684:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:3685:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getConstructorRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getConstructorAccess().getFiredEventsEventCrossReference_5_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_91);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop106;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_9=(Token)match(input,14,FOLLOW_92); 

            			newLeafNode(otherlv_9, grammarAccess.getConstructorAccess().getLeftCurlyBracketKeyword_6());
            		
            // InternalCqrsDsl.g:3705:3: ( (lv_parameters_10_0= ruleParameter ) )*
            loop108:
            do {
                int alt108=2;
                alt108 = dfa108.predict(input);
                switch (alt108) {
            	case 1 :
            	    // InternalCqrsDsl.g:3706:4: (lv_parameters_10_0= ruleParameter )
            	    {
            	    // InternalCqrsDsl.g:3706:4: (lv_parameters_10_0= ruleParameter )
            	    // InternalCqrsDsl.g:3707:5: lv_parameters_10_0= ruleParameter
            	    {

            	    					newCompositeNode(grammarAccess.getConstructorAccess().getParametersParameterParserRuleCall_7_0());
            	    				
            	    pushFollow(FOLLOW_92);
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
            	    break loop108;
                }
            } while (true);

            // InternalCqrsDsl.g:3724:3: ( ( ruleFQN ) )?
            int alt109=2;
            int LA109_0 = input.LA(1);

            if ( (LA109_0==RULE_ID) ) {
                alt109=1;
            }
            switch (alt109) {
                case 1 :
                    // InternalCqrsDsl.g:3725:4: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3725:4: ( ruleFQN )
                    // InternalCqrsDsl.g:3726:5: ruleFQN
                    {

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getConstructorRule());
                    					}
                    				

                    					newCompositeNode(grammarAccess.getConstructorAccess().getServiceServiceCrossReference_8_0());
                    				
                    pushFollow(FOLLOW_93);
                    ruleFQN();

                    state._fsp--;


                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3740:3: ( (lv_services_12_0= ruleService ) )*
            loop110:
            do {
                int alt110=2;
                int LA110_0 = input.LA(1);

                if ( (LA110_0==RULE_DOC) ) {
                    int LA110_1 = input.LA(2);

                    if ( (LA110_1==76) ) {
                        alt110=1;
                    }


                }
                else if ( (LA110_0==76) ) {
                    alt110=1;
                }


                switch (alt110) {
            	case 1 :
            	    // InternalCqrsDsl.g:3741:4: (lv_services_12_0= ruleService )
            	    {
            	    // InternalCqrsDsl.g:3741:4: (lv_services_12_0= ruleService )
            	    // InternalCqrsDsl.g:3742:5: lv_services_12_0= ruleService
            	    {

            	    					newCompositeNode(grammarAccess.getConstructorAccess().getServicesServiceParserRuleCall_9_0());
            	    				
            	    pushFollow(FOLLOW_93);
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
            	    break loop110;
                }
            } while (true);

            // InternalCqrsDsl.g:3759:3: ( (lv_events_13_0= ruleEvent ) )*
            loop111:
            do {
                int alt111=2;
                int LA111_0 = input.LA(1);

                if ( (LA111_0==RULE_DOC||LA111_0==53||LA111_0==75) ) {
                    alt111=1;
                }


                switch (alt111) {
            	case 1 :
            	    // InternalCqrsDsl.g:3760:4: (lv_events_13_0= ruleEvent )
            	    {
            	    // InternalCqrsDsl.g:3760:4: (lv_events_13_0= ruleEvent )
            	    // InternalCqrsDsl.g:3761:5: lv_events_13_0= ruleEvent
            	    {

            	    					newCompositeNode(grammarAccess.getConstructorAccess().getEventsEventParserRuleCall_10_0());
            	    				
            	    pushFollow(FOLLOW_94);
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
            	    break loop111;
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
    // InternalCqrsDsl.g:3786:1: entryRuleReturnType returns [EObject current=null] : iv_ruleReturnType= ruleReturnType EOF ;
    public final EObject entryRuleReturnType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReturnType = null;


        try {
            // InternalCqrsDsl.g:3786:51: (iv_ruleReturnType= ruleReturnType EOF )
            // InternalCqrsDsl.g:3787:2: iv_ruleReturnType= ruleReturnType EOF
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
    // InternalCqrsDsl.g:3793:1: ruleReturnType returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( ( ruleFQN ) ) ) ;
    public final EObject ruleReturnType() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:3799:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( ( ruleFQN ) ) ) )
            // InternalCqrsDsl.g:3800:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( ( ruleFQN ) ) )
            {
            // InternalCqrsDsl.g:3800:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( ( ruleFQN ) ) )
            // InternalCqrsDsl.g:3801:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'returns' ( ( ruleFQN ) )
            {
            // InternalCqrsDsl.g:3801:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt112=2;
            int LA112_0 = input.LA(1);

            if ( (LA112_0==RULE_DOC) ) {
                alt112=1;
            }
            switch (alt112) {
                case 1 :
                    // InternalCqrsDsl.g:3802:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3802:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3803:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_95); 

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

            otherlv_1=(Token)match(input,61,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getReturnTypeAccess().getReturnsKeyword_1());
            		
            // InternalCqrsDsl.g:3823:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:3824:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:3824:4: ( ruleFQN )
            // InternalCqrsDsl.g:3825:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getReturnTypeRule());
            					}
            				

            					newCompositeNode(grammarAccess.getReturnTypeAccess().getTypeTypeCrossReference_2_0());
            				
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
    // $ANTLR end "ruleReturnType"


    // $ANTLR start "entryRuleMethod"
    // InternalCqrsDsl.g:3843:1: entryRuleMethod returns [EObject current=null] : iv_ruleMethod= ruleMethod EOF ;
    public final EObject entryRuleMethod() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMethod = null;


        try {
            // InternalCqrsDsl.g:3843:47: (iv_ruleMethod= ruleMethod EOF )
            // InternalCqrsDsl.g:3844:2: iv_ruleMethod= ruleMethod EOF
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
    // InternalCqrsDsl.g:3850:1: ruleMethod returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' ) ;
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
            // InternalCqrsDsl.g:3856:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' ) )
            // InternalCqrsDsl.g:3857:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' )
            {
            // InternalCqrsDsl.g:3857:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}' )
            // InternalCqrsDsl.g:3858:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'method' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'ref' ( ( ruleFQN ) ) )? ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? otherlv_11= '{' ( (lv_parameters_12_0= ruleParameter ) )* ( ( ruleFQN ) )? ( (lv_returnType_14_0= ruleReturnType ) )? ( (lv_services_15_0= ruleService ) )* ( (lv_events_16_0= ruleEvent ) )* otherlv_17= '}'
            {
            // InternalCqrsDsl.g:3858:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt113=2;
            int LA113_0 = input.LA(1);

            if ( (LA113_0==RULE_DOC) ) {
                alt113=1;
            }
            switch (alt113) {
                case 1 :
                    // InternalCqrsDsl.g:3859:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:3859:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:3860:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_96); 

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

            otherlv_1=(Token)match(input,62,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getMethodAccess().getMethodKeyword_1());
            		
            // InternalCqrsDsl.g:3880:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:3881:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:3881:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:3882:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_97); 

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

            // InternalCqrsDsl.g:3898:3: (otherlv_3= 'ref' ( ( ruleFQN ) ) )?
            int alt114=2;
            int LA114_0 = input.LA(1);

            if ( (LA114_0==63) ) {
                alt114=1;
            }
            switch (alt114) {
                case 1 :
                    // InternalCqrsDsl.g:3899:4: otherlv_3= 'ref' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,63,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getMethodAccess().getRefKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:3903:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3904:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3904:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3905:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getMethodRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getMethodAccess().getRefMethodMethodCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_88);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:3920:3: ( (lv_preconditions_5_0= rulePreconditions ) )?
            int alt115=2;
            int LA115_0 = input.LA(1);

            if ( (LA115_0==73) ) {
                alt115=1;
            }
            switch (alt115) {
                case 1 :
                    // InternalCqrsDsl.g:3921:4: (lv_preconditions_5_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:3921:4: (lv_preconditions_5_0= rulePreconditions )
                    // InternalCqrsDsl.g:3922:5: lv_preconditions_5_0= rulePreconditions
                    {

                    					newCompositeNode(grammarAccess.getMethodAccess().getPreconditionsPreconditionsParserRuleCall_4_0());
                    				
                    pushFollow(FOLLOW_89);
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

            // InternalCqrsDsl.g:3939:3: ( (lv_businessRules_6_0= ruleBusinessRules ) )?
            int alt116=2;
            int LA116_0 = input.LA(1);

            if ( (LA116_0==74) ) {
                alt116=1;
            }
            switch (alt116) {
                case 1 :
                    // InternalCqrsDsl.g:3940:4: (lv_businessRules_6_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:3940:4: (lv_businessRules_6_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:3941:5: lv_businessRules_6_0= ruleBusinessRules
                    {

                    					newCompositeNode(grammarAccess.getMethodAccess().getBusinessRulesBusinessRulesParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_90);
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

            // InternalCqrsDsl.g:3958:3: (otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            int alt118=2;
            int LA118_0 = input.LA(1);

            if ( (LA118_0==60) ) {
                alt118=1;
            }
            switch (alt118) {
                case 1 :
                    // InternalCqrsDsl.g:3959:4: otherlv_7= 'fires' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_7=(Token)match(input,60,FOLLOW_4); 

                    				newLeafNode(otherlv_7, grammarAccess.getMethodAccess().getFiresKeyword_6_0());
                    			
                    // InternalCqrsDsl.g:3963:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:3964:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:3964:5: ( ruleFQN )
                    // InternalCqrsDsl.g:3965:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getMethodRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getMethodAccess().getFiredEventsEventCrossReference_6_1_0());
                    					
                    pushFollow(FOLLOW_91);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:3979:4: (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    loop117:
                    do {
                        int alt117=2;
                        int LA117_0 = input.LA(1);

                        if ( (LA117_0==28) ) {
                            alt117=1;
                        }


                        switch (alt117) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:3980:5: otherlv_9= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_9=(Token)match(input,28,FOLLOW_4); 

                    	    					newLeafNode(otherlv_9, grammarAccess.getMethodAccess().getCommaKeyword_6_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:3984:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:3985:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:3985:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:3986:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getMethodRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getMethodAccess().getFiredEventsEventCrossReference_6_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_91);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop117;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_11=(Token)match(input,14,FOLLOW_98); 

            			newLeafNode(otherlv_11, grammarAccess.getMethodAccess().getLeftCurlyBracketKeyword_7());
            		
            // InternalCqrsDsl.g:4006:3: ( (lv_parameters_12_0= ruleParameter ) )*
            loop119:
            do {
                int alt119=2;
                alt119 = dfa119.predict(input);
                switch (alt119) {
            	case 1 :
            	    // InternalCqrsDsl.g:4007:4: (lv_parameters_12_0= ruleParameter )
            	    {
            	    // InternalCqrsDsl.g:4007:4: (lv_parameters_12_0= ruleParameter )
            	    // InternalCqrsDsl.g:4008:5: lv_parameters_12_0= ruleParameter
            	    {

            	    					newCompositeNode(grammarAccess.getMethodAccess().getParametersParameterParserRuleCall_8_0());
            	    				
            	    pushFollow(FOLLOW_98);
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
            	    break loop119;
                }
            } while (true);

            // InternalCqrsDsl.g:4025:3: ( ( ruleFQN ) )?
            int alt120=2;
            int LA120_0 = input.LA(1);

            if ( (LA120_0==RULE_ID) ) {
                alt120=1;
            }
            switch (alt120) {
                case 1 :
                    // InternalCqrsDsl.g:4026:4: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:4026:4: ( ruleFQN )
                    // InternalCqrsDsl.g:4027:5: ruleFQN
                    {

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getMethodRule());
                    					}
                    				

                    					newCompositeNode(grammarAccess.getMethodAccess().getServiceServiceCrossReference_9_0());
                    				
                    pushFollow(FOLLOW_99);
                    ruleFQN();

                    state._fsp--;


                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4041:3: ( (lv_returnType_14_0= ruleReturnType ) )?
            int alt121=2;
            int LA121_0 = input.LA(1);

            if ( (LA121_0==RULE_DOC) ) {
                int LA121_1 = input.LA(2);

                if ( (LA121_1==61) ) {
                    alt121=1;
                }
            }
            else if ( (LA121_0==61) ) {
                alt121=1;
            }
            switch (alt121) {
                case 1 :
                    // InternalCqrsDsl.g:4042:4: (lv_returnType_14_0= ruleReturnType )
                    {
                    // InternalCqrsDsl.g:4042:4: (lv_returnType_14_0= ruleReturnType )
                    // InternalCqrsDsl.g:4043:5: lv_returnType_14_0= ruleReturnType
                    {

                    					newCompositeNode(grammarAccess.getMethodAccess().getReturnTypeReturnTypeParserRuleCall_10_0());
                    				
                    pushFollow(FOLLOW_93);
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

            // InternalCqrsDsl.g:4060:3: ( (lv_services_15_0= ruleService ) )*
            loop122:
            do {
                int alt122=2;
                int LA122_0 = input.LA(1);

                if ( (LA122_0==RULE_DOC) ) {
                    int LA122_1 = input.LA(2);

                    if ( (LA122_1==76) ) {
                        alt122=1;
                    }


                }
                else if ( (LA122_0==76) ) {
                    alt122=1;
                }


                switch (alt122) {
            	case 1 :
            	    // InternalCqrsDsl.g:4061:4: (lv_services_15_0= ruleService )
            	    {
            	    // InternalCqrsDsl.g:4061:4: (lv_services_15_0= ruleService )
            	    // InternalCqrsDsl.g:4062:5: lv_services_15_0= ruleService
            	    {

            	    					newCompositeNode(grammarAccess.getMethodAccess().getServicesServiceParserRuleCall_11_0());
            	    				
            	    pushFollow(FOLLOW_93);
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
            	    break loop122;
                }
            } while (true);

            // InternalCqrsDsl.g:4079:3: ( (lv_events_16_0= ruleEvent ) )*
            loop123:
            do {
                int alt123=2;
                int LA123_0 = input.LA(1);

                if ( (LA123_0==RULE_DOC||LA123_0==53||LA123_0==75) ) {
                    alt123=1;
                }


                switch (alt123) {
            	case 1 :
            	    // InternalCqrsDsl.g:4080:4: (lv_events_16_0= ruleEvent )
            	    {
            	    // InternalCqrsDsl.g:4080:4: (lv_events_16_0= ruleEvent )
            	    // InternalCqrsDsl.g:4081:5: lv_events_16_0= ruleEvent
            	    {

            	    					newCompositeNode(grammarAccess.getMethodAccess().getEventsEventParserRuleCall_12_0());
            	    				
            	    pushFollow(FOLLOW_94);
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
            	    break loop123;
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
    // InternalCqrsDsl.g:4106:1: entryRuleTypeMetaInfo returns [EObject current=null] : iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF ;
    public final EObject entryRuleTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTypeMetaInfo = null;


        try {
            // InternalCqrsDsl.g:4106:53: (iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF )
            // InternalCqrsDsl.g:4107:2: iv_ruleTypeMetaInfo= ruleTypeMetaInfo EOF
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
    // InternalCqrsDsl.g:4113:1: ruleTypeMetaInfo returns [EObject current=null] : ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? ) ;
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
            // InternalCqrsDsl.g:4119:2: ( ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? ) )
            // InternalCqrsDsl.g:4120:2: ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? )
            {
            // InternalCqrsDsl.g:4120:2: ( () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )? )
            // InternalCqrsDsl.g:4121:3: () (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )? (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )? (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )? (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )? (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )?
            {
            // InternalCqrsDsl.g:4121:3: ()
            // InternalCqrsDsl.g:4122:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getTypeMetaInfoAccess().getTypeMetaInfoAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:4128:3: (otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) ) )?
            int alt124=2;
            int LA124_0 = input.LA(1);

            if ( (LA124_0==64) ) {
                alt124=1;
            }
            switch (alt124) {
                case 1 :
                    // InternalCqrsDsl.g:4129:4: otherlv_1= 'slabel' ( (lv_slabel_2_0= RULE_STRING ) )
                    {
                    otherlv_1=(Token)match(input,64,FOLLOW_35); 

                    				newLeafNode(otherlv_1, grammarAccess.getTypeMetaInfoAccess().getSlabelKeyword_1_0());
                    			
                    // InternalCqrsDsl.g:4133:4: ( (lv_slabel_2_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4134:5: (lv_slabel_2_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4134:5: (lv_slabel_2_0= RULE_STRING )
                    // InternalCqrsDsl.g:4135:6: lv_slabel_2_0= RULE_STRING
                    {
                    lv_slabel_2_0=(Token)match(input,RULE_STRING,FOLLOW_100); 

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

            // InternalCqrsDsl.g:4152:3: (otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) ) )?
            int alt125=2;
            int LA125_0 = input.LA(1);

            if ( (LA125_0==65) ) {
                alt125=1;
            }
            switch (alt125) {
                case 1 :
                    // InternalCqrsDsl.g:4153:4: otherlv_3= 'label' ( (lv_label_4_0= RULE_STRING ) )
                    {
                    otherlv_3=(Token)match(input,65,FOLLOW_35); 

                    				newLeafNode(otherlv_3, grammarAccess.getTypeMetaInfoAccess().getLabelKeyword_2_0());
                    			
                    // InternalCqrsDsl.g:4157:4: ( (lv_label_4_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4158:5: (lv_label_4_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4158:5: (lv_label_4_0= RULE_STRING )
                    // InternalCqrsDsl.g:4159:6: lv_label_4_0= RULE_STRING
                    {
                    lv_label_4_0=(Token)match(input,RULE_STRING,FOLLOW_101); 

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

            // InternalCqrsDsl.g:4176:3: (otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) ) )?
            int alt126=2;
            int LA126_0 = input.LA(1);

            if ( (LA126_0==66) ) {
                alt126=1;
            }
            switch (alt126) {
                case 1 :
                    // InternalCqrsDsl.g:4177:4: otherlv_5= 'tooltip' ( (lv_tooltip_6_0= RULE_STRING ) )
                    {
                    otherlv_5=(Token)match(input,66,FOLLOW_35); 

                    				newLeafNode(otherlv_5, grammarAccess.getTypeMetaInfoAccess().getTooltipKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:4181:4: ( (lv_tooltip_6_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4182:5: (lv_tooltip_6_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4182:5: (lv_tooltip_6_0= RULE_STRING )
                    // InternalCqrsDsl.g:4183:6: lv_tooltip_6_0= RULE_STRING
                    {
                    lv_tooltip_6_0=(Token)match(input,RULE_STRING,FOLLOW_102); 

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

            // InternalCqrsDsl.g:4200:3: (otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) ) )?
            int alt127=2;
            int LA127_0 = input.LA(1);

            if ( (LA127_0==67) ) {
                alt127=1;
            }
            switch (alt127) {
                case 1 :
                    // InternalCqrsDsl.g:4201:4: otherlv_7= 'prompt' ( (lv_prompt_8_0= RULE_STRING ) )
                    {
                    otherlv_7=(Token)match(input,67,FOLLOW_35); 

                    				newLeafNode(otherlv_7, grammarAccess.getTypeMetaInfoAccess().getPromptKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:4205:4: ( (lv_prompt_8_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:4206:5: (lv_prompt_8_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:4206:5: (lv_prompt_8_0= RULE_STRING )
                    // InternalCqrsDsl.g:4207:6: lv_prompt_8_0= RULE_STRING
                    {
                    lv_prompt_8_0=(Token)match(input,RULE_STRING,FOLLOW_103); 

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

            // InternalCqrsDsl.g:4224:3: (otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )* )?
            int alt129=2;
            int LA129_0 = input.LA(1);

            if ( (LA129_0==68) ) {
                alt129=1;
            }
            switch (alt129) {
                case 1 :
                    // InternalCqrsDsl.g:4225:4: otherlv_9= 'examples' ( (lv_examples_10_0= ruleLiteral ) )*
                    {
                    otherlv_9=(Token)match(input,68,FOLLOW_104); 

                    				newLeafNode(otherlv_9, grammarAccess.getTypeMetaInfoAccess().getExamplesKeyword_5_0());
                    			
                    // InternalCqrsDsl.g:4229:4: ( (lv_examples_10_0= ruleLiteral ) )*
                    loop128:
                    do {
                        int alt128=2;
                        int LA128_0 = input.LA(1);

                        if ( ((LA128_0>=RULE_INT && LA128_0<=RULE_DECIMAL)||(LA128_0>=87 && LA128_0<=89)) ) {
                            alt128=1;
                        }


                        switch (alt128) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:4230:5: (lv_examples_10_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:4230:5: (lv_examples_10_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:4231:6: lv_examples_10_0= ruleLiteral
                    	    {

                    	    						newCompositeNode(grammarAccess.getTypeMetaInfoAccess().getExamplesLiteralParserRuleCall_5_1_0());
                    	    					
                    	    pushFollow(FOLLOW_104);
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
                    	    break loop128;
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
    // InternalCqrsDsl.g:4253:1: entryRuleGenericArgs returns [EObject current=null] : iv_ruleGenericArgs= ruleGenericArgs EOF ;
    public final EObject entryRuleGenericArgs() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGenericArgs = null;


        try {
            // InternalCqrsDsl.g:4253:52: (iv_ruleGenericArgs= ruleGenericArgs EOF )
            // InternalCqrsDsl.g:4254:2: iv_ruleGenericArgs= ruleGenericArgs EOF
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
    // InternalCqrsDsl.g:4260:1: ruleGenericArgs returns [EObject current=null] : ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' ) ;
    public final EObject ruleGenericArgs() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:4266:2: ( ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' ) )
            // InternalCqrsDsl.g:4267:2: ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' )
            {
            // InternalCqrsDsl.g:4267:2: ( (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>' )
            // InternalCqrsDsl.g:4268:3: (otherlv_0= '<' )+ ( ( ruleFQN ) ) (otherlv_2= ',' ( ( ruleFQN ) ) )* otherlv_4= '>'
            {
            // InternalCqrsDsl.g:4268:3: (otherlv_0= '<' )+
            int cnt130=0;
            loop130:
            do {
                int alt130=2;
                int LA130_0 = input.LA(1);

                if ( (LA130_0==69) ) {
                    alt130=1;
                }


                switch (alt130) {
            	case 1 :
            	    // InternalCqrsDsl.g:4269:4: otherlv_0= '<'
            	    {
            	    otherlv_0=(Token)match(input,69,FOLLOW_105); 

            	    				newLeafNode(otherlv_0, grammarAccess.getGenericArgsAccess().getLessThanSignKeyword_0());
            	    			

            	    }
            	    break;

            	default :
            	    if ( cnt130 >= 1 ) break loop130;
                        EarlyExitException eee =
                            new EarlyExitException(130, input);
                        throw eee;
                }
                cnt130++;
            } while (true);

            // InternalCqrsDsl.g:4274:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4275:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4275:4: ( ruleFQN )
            // InternalCqrsDsl.g:4276:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getGenericArgsRule());
            					}
            				

            					newCompositeNode(grammarAccess.getGenericArgsAccess().getArgsTypeCrossReference_1_0());
            				
            pushFollow(FOLLOW_106);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4290:3: (otherlv_2= ',' ( ( ruleFQN ) ) )*
            loop131:
            do {
                int alt131=2;
                int LA131_0 = input.LA(1);

                if ( (LA131_0==28) ) {
                    alt131=1;
                }


                switch (alt131) {
            	case 1 :
            	    // InternalCqrsDsl.g:4291:4: otherlv_2= ',' ( ( ruleFQN ) )
            	    {
            	    otherlv_2=(Token)match(input,28,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getGenericArgsAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:4295:4: ( ( ruleFQN ) )
            	    // InternalCqrsDsl.g:4296:5: ( ruleFQN )
            	    {
            	    // InternalCqrsDsl.g:4296:5: ( ruleFQN )
            	    // InternalCqrsDsl.g:4297:6: ruleFQN
            	    {

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getGenericArgsRule());
            	    						}
            	    					

            	    						newCompositeNode(grammarAccess.getGenericArgsAccess().getArgsTypeCrossReference_2_1_0());
            	    					
            	    pushFollow(FOLLOW_106);
            	    ruleFQN();

            	    state._fsp--;


            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop131;
                }
            } while (true);

            otherlv_4=(Token)match(input,70,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4320:1: entryRuleAttribute returns [EObject current=null] : iv_ruleAttribute= ruleAttribute EOF ;
    public final EObject entryRuleAttribute() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAttribute = null;


        try {
            // InternalCqrsDsl.g:4320:50: (iv_ruleAttribute= ruleAttribute EOF )
            // InternalCqrsDsl.g:4321:2: iv_ruleAttribute= ruleAttribute EOF
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
    // InternalCqrsDsl.g:4327:1: ruleAttribute returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) ;
    public final EObject ruleAttribute() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token lv_nullable_1_0=null;
        Token lv_name_4_0=null;
        EObject lv_generics_3_0 = null;

        EObject lv_invariants_5_0 = null;

        EObject lv_dataProtection_6_0 = null;

        EObject lv_overridden_7_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4333:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) )
            // InternalCqrsDsl.g:4334:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            {
            // InternalCqrsDsl.g:4334:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            // InternalCqrsDsl.g:4335:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_invariants_5_0= ruleInvariants ) )? ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            {
            // InternalCqrsDsl.g:4335:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt132=2;
            int LA132_0 = input.LA(1);

            if ( (LA132_0==RULE_DOC) ) {
                alt132=1;
            }
            switch (alt132) {
                case 1 :
                    // InternalCqrsDsl.g:4336:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4336:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4337:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_107); 

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

            // InternalCqrsDsl.g:4353:3: ( (lv_nullable_1_0= 'nullable' ) )?
            int alt133=2;
            int LA133_0 = input.LA(1);

            if ( (LA133_0==71) ) {
                alt133=1;
            }
            switch (alt133) {
                case 1 :
                    // InternalCqrsDsl.g:4354:4: (lv_nullable_1_0= 'nullable' )
                    {
                    // InternalCqrsDsl.g:4354:4: (lv_nullable_1_0= 'nullable' )
                    // InternalCqrsDsl.g:4355:5: lv_nullable_1_0= 'nullable'
                    {
                    lv_nullable_1_0=(Token)match(input,71,FOLLOW_4); 

                    					newLeafNode(lv_nullable_1_0, grammarAccess.getAttributeAccess().getNullableNullableKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getAttributeRule());
                    					}
                    					setWithLastConsumed(current, "nullable", lv_nullable_1_0, "nullable");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4367:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4368:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4368:4: ( ruleFQN )
            // InternalCqrsDsl.g:4369:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAttributeRule());
            					}
            				

            					newCompositeNode(grammarAccess.getAttributeAccess().getTypeTypeCrossReference_2_0());
            				
            pushFollow(FOLLOW_105);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4383:3: ( (lv_generics_3_0= ruleGenericArgs ) )?
            int alt134=2;
            int LA134_0 = input.LA(1);

            if ( (LA134_0==69) ) {
                alt134=1;
            }
            switch (alt134) {
                case 1 :
                    // InternalCqrsDsl.g:4384:4: (lv_generics_3_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:4384:4: (lv_generics_3_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:4385:5: lv_generics_3_0= ruleGenericArgs
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

            // InternalCqrsDsl.g:4402:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCqrsDsl.g:4403:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4403:4: (lv_name_4_0= RULE_ID )
            // InternalCqrsDsl.g:4404:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_108); 

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

            // InternalCqrsDsl.g:4420:3: ( (lv_invariants_5_0= ruleInvariants ) )?
            int alt135=2;
            int LA135_0 = input.LA(1);

            if ( (LA135_0==72) ) {
                alt135=1;
            }
            switch (alt135) {
                case 1 :
                    // InternalCqrsDsl.g:4421:4: (lv_invariants_5_0= ruleInvariants )
                    {
                    // InternalCqrsDsl.g:4421:4: (lv_invariants_5_0= ruleInvariants )
                    // InternalCqrsDsl.g:4422:5: lv_invariants_5_0= ruleInvariants
                    {

                    					newCompositeNode(grammarAccess.getAttributeAccess().getInvariantsInvariantsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_109);
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

            // InternalCqrsDsl.g:4439:3: ( (lv_dataProtection_6_0= ruleDataProtectionInstance ) )?
            int alt136=2;
            int LA136_0 = input.LA(1);

            if ( (LA136_0==34) ) {
                alt136=1;
            }
            switch (alt136) {
                case 1 :
                    // InternalCqrsDsl.g:4440:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    {
                    // InternalCqrsDsl.g:4440:4: (lv_dataProtection_6_0= ruleDataProtectionInstance )
                    // InternalCqrsDsl.g:4441:5: lv_dataProtection_6_0= ruleDataProtectionInstance
                    {

                    					newCompositeNode(grammarAccess.getAttributeAccess().getDataProtectionDataProtectionInstanceParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_23);
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

            // InternalCqrsDsl.g:4458:3: ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            int alt137=2;
            int LA137_0 = input.LA(1);

            if ( (LA137_0==14) ) {
                alt137=1;
            }
            switch (alt137) {
                case 1 :
                    // InternalCqrsDsl.g:4459:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    {
                    // InternalCqrsDsl.g:4459:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    // InternalCqrsDsl.g:4460:5: lv_overridden_7_0= ruleOverriddenTypeMetaInfo
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
    // InternalCqrsDsl.g:4481:1: entryRuleParameter returns [EObject current=null] : iv_ruleParameter= ruleParameter EOF ;
    public final EObject entryRuleParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameter = null;


        try {
            // InternalCqrsDsl.g:4481:50: (iv_ruleParameter= ruleParameter EOF )
            // InternalCqrsDsl.g:4482:2: iv_ruleParameter= ruleParameter EOF
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
    // InternalCqrsDsl.g:4488:1: ruleParameter returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) ;
    public final EObject ruleParameter() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token lv_nullable_1_0=null;
        Token lv_name_4_0=null;
        EObject lv_generics_3_0 = null;

        EObject lv_preconditions_5_0 = null;

        EObject lv_businessRules_6_0 = null;

        EObject lv_overridden_7_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4494:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? ) )
            // InternalCqrsDsl.g:4495:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            {
            // InternalCqrsDsl.g:4495:2: ( ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )? )
            // InternalCqrsDsl.g:4496:3: ( (lv_doc_0_0= RULE_DOC ) )? ( (lv_nullable_1_0= 'nullable' ) )? ( ( ruleFQN ) ) ( (lv_generics_3_0= ruleGenericArgs ) )? ( (lv_name_4_0= RULE_ID ) ) ( (lv_preconditions_5_0= rulePreconditions ) )? ( (lv_businessRules_6_0= ruleBusinessRules ) )? ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            {
            // InternalCqrsDsl.g:4496:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt138=2;
            int LA138_0 = input.LA(1);

            if ( (LA138_0==RULE_DOC) ) {
                alt138=1;
            }
            switch (alt138) {
                case 1 :
                    // InternalCqrsDsl.g:4497:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:4497:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:4498:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_107); 

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

            // InternalCqrsDsl.g:4514:3: ( (lv_nullable_1_0= 'nullable' ) )?
            int alt139=2;
            int LA139_0 = input.LA(1);

            if ( (LA139_0==71) ) {
                alt139=1;
            }
            switch (alt139) {
                case 1 :
                    // InternalCqrsDsl.g:4515:4: (lv_nullable_1_0= 'nullable' )
                    {
                    // InternalCqrsDsl.g:4515:4: (lv_nullable_1_0= 'nullable' )
                    // InternalCqrsDsl.g:4516:5: lv_nullable_1_0= 'nullable'
                    {
                    lv_nullable_1_0=(Token)match(input,71,FOLLOW_4); 

                    					newLeafNode(lv_nullable_1_0, grammarAccess.getParameterAccess().getNullableNullableKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getParameterRule());
                    					}
                    					setWithLastConsumed(current, "nullable", lv_nullable_1_0, "nullable");
                    				

                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:4528:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4529:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4529:4: ( ruleFQN )
            // InternalCqrsDsl.g:4530:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getParameterRule());
            					}
            				

            					newCompositeNode(grammarAccess.getParameterAccess().getTypeTypeCrossReference_2_0());
            				
            pushFollow(FOLLOW_105);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4544:3: ( (lv_generics_3_0= ruleGenericArgs ) )?
            int alt140=2;
            int LA140_0 = input.LA(1);

            if ( (LA140_0==69) ) {
                alt140=1;
            }
            switch (alt140) {
                case 1 :
                    // InternalCqrsDsl.g:4545:4: (lv_generics_3_0= ruleGenericArgs )
                    {
                    // InternalCqrsDsl.g:4545:4: (lv_generics_3_0= ruleGenericArgs )
                    // InternalCqrsDsl.g:4546:5: lv_generics_3_0= ruleGenericArgs
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

            // InternalCqrsDsl.g:4563:3: ( (lv_name_4_0= RULE_ID ) )
            // InternalCqrsDsl.g:4564:4: (lv_name_4_0= RULE_ID )
            {
            // InternalCqrsDsl.g:4564:4: (lv_name_4_0= RULE_ID )
            // InternalCqrsDsl.g:4565:5: lv_name_4_0= RULE_ID
            {
            lv_name_4_0=(Token)match(input,RULE_ID,FOLLOW_110); 

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

            // InternalCqrsDsl.g:4581:3: ( (lv_preconditions_5_0= rulePreconditions ) )?
            int alt141=2;
            int LA141_0 = input.LA(1);

            if ( (LA141_0==73) ) {
                alt141=1;
            }
            switch (alt141) {
                case 1 :
                    // InternalCqrsDsl.g:4582:4: (lv_preconditions_5_0= rulePreconditions )
                    {
                    // InternalCqrsDsl.g:4582:4: (lv_preconditions_5_0= rulePreconditions )
                    // InternalCqrsDsl.g:4583:5: lv_preconditions_5_0= rulePreconditions
                    {

                    					newCompositeNode(grammarAccess.getParameterAccess().getPreconditionsPreconditionsParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_111);
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

            // InternalCqrsDsl.g:4600:3: ( (lv_businessRules_6_0= ruleBusinessRules ) )?
            int alt142=2;
            int LA142_0 = input.LA(1);

            if ( (LA142_0==74) ) {
                alt142=1;
            }
            switch (alt142) {
                case 1 :
                    // InternalCqrsDsl.g:4601:4: (lv_businessRules_6_0= ruleBusinessRules )
                    {
                    // InternalCqrsDsl.g:4601:4: (lv_businessRules_6_0= ruleBusinessRules )
                    // InternalCqrsDsl.g:4602:5: lv_businessRules_6_0= ruleBusinessRules
                    {

                    					newCompositeNode(grammarAccess.getParameterAccess().getBusinessRulesBusinessRulesParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_23);
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

            // InternalCqrsDsl.g:4619:3: ( (lv_overridden_7_0= ruleOverriddenTypeMetaInfo ) )?
            int alt143=2;
            int LA143_0 = input.LA(1);

            if ( (LA143_0==14) ) {
                alt143=1;
            }
            switch (alt143) {
                case 1 :
                    // InternalCqrsDsl.g:4620:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    {
                    // InternalCqrsDsl.g:4620:4: (lv_overridden_7_0= ruleOverriddenTypeMetaInfo )
                    // InternalCqrsDsl.g:4621:5: lv_overridden_7_0= ruleOverriddenTypeMetaInfo
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
    // InternalCqrsDsl.g:4642:1: entryRuleInvariants returns [EObject current=null] : iv_ruleInvariants= ruleInvariants EOF ;
    public final EObject entryRuleInvariants() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInvariants = null;


        try {
            // InternalCqrsDsl.g:4642:51: (iv_ruleInvariants= ruleInvariants EOF )
            // InternalCqrsDsl.g:4643:2: iv_ruleInvariants= ruleInvariants EOF
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
    // InternalCqrsDsl.g:4649:1: ruleInvariants returns [EObject current=null] : (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) ;
    public final EObject ruleInvariants() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_constraintInstances_1_0 = null;

        EObject lv_constraintInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4655:2: ( (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) )
            // InternalCqrsDsl.g:4656:2: (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            {
            // InternalCqrsDsl.g:4656:2: (otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            // InternalCqrsDsl.g:4657:3: otherlv_0= 'invariants' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            {
            otherlv_0=(Token)match(input,72,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getInvariantsAccess().getInvariantsKeyword_0());
            		
            // InternalCqrsDsl.g:4661:3: ( (lv_constraintInstances_1_0= ruleConstraintInstance ) )
            // InternalCqrsDsl.g:4662:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            {
            // InternalCqrsDsl.g:4662:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            // InternalCqrsDsl.g:4663:5: lv_constraintInstances_1_0= ruleConstraintInstance
            {

            					newCompositeNode(grammarAccess.getInvariantsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_112);
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

            // InternalCqrsDsl.g:4680:3: (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            loop144:
            do {
                int alt144=2;
                int LA144_0 = input.LA(1);

                if ( (LA144_0==28) ) {
                    alt144=1;
                }


                switch (alt144) {
            	case 1 :
            	    // InternalCqrsDsl.g:4681:4: otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,28,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getInvariantsAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:4685:4: ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    // InternalCqrsDsl.g:4686:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    {
            	    // InternalCqrsDsl.g:4686:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    // InternalCqrsDsl.g:4687:6: lv_constraintInstances_3_0= ruleConstraintInstance
            	    {

            	    						newCompositeNode(grammarAccess.getInvariantsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_112);
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
            	    break loop144;
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
    // InternalCqrsDsl.g:4709:1: entryRulePreconditions returns [EObject current=null] : iv_rulePreconditions= rulePreconditions EOF ;
    public final EObject entryRulePreconditions() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePreconditions = null;


        try {
            // InternalCqrsDsl.g:4709:54: (iv_rulePreconditions= rulePreconditions EOF )
            // InternalCqrsDsl.g:4710:2: iv_rulePreconditions= rulePreconditions EOF
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
    // InternalCqrsDsl.g:4716:1: rulePreconditions returns [EObject current=null] : (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) ;
    public final EObject rulePreconditions() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_constraintInstances_1_0 = null;

        EObject lv_constraintInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4722:2: ( (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* ) )
            // InternalCqrsDsl.g:4723:2: (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            {
            // InternalCqrsDsl.g:4723:2: (otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )* )
            // InternalCqrsDsl.g:4724:3: otherlv_0= 'preconditions' ( (lv_constraintInstances_1_0= ruleConstraintInstance ) ) (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            {
            otherlv_0=(Token)match(input,73,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getPreconditionsAccess().getPreconditionsKeyword_0());
            		
            // InternalCqrsDsl.g:4728:3: ( (lv_constraintInstances_1_0= ruleConstraintInstance ) )
            // InternalCqrsDsl.g:4729:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            {
            // InternalCqrsDsl.g:4729:4: (lv_constraintInstances_1_0= ruleConstraintInstance )
            // InternalCqrsDsl.g:4730:5: lv_constraintInstances_1_0= ruleConstraintInstance
            {

            					newCompositeNode(grammarAccess.getPreconditionsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_112);
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

            // InternalCqrsDsl.g:4747:3: (otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) ) )*
            loop145:
            do {
                int alt145=2;
                int LA145_0 = input.LA(1);

                if ( (LA145_0==28) ) {
                    alt145=1;
                }


                switch (alt145) {
            	case 1 :
            	    // InternalCqrsDsl.g:4748:4: otherlv_2= ',' ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,28,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getPreconditionsAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:4752:4: ( (lv_constraintInstances_3_0= ruleConstraintInstance ) )
            	    // InternalCqrsDsl.g:4753:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    {
            	    // InternalCqrsDsl.g:4753:5: (lv_constraintInstances_3_0= ruleConstraintInstance )
            	    // InternalCqrsDsl.g:4754:6: lv_constraintInstances_3_0= ruleConstraintInstance
            	    {

            	    						newCompositeNode(grammarAccess.getPreconditionsAccess().getConstraintInstancesConstraintInstanceParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_112);
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
            	    break loop145;
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
    // InternalCqrsDsl.g:4776:1: entryRuleBusinessRules returns [EObject current=null] : iv_ruleBusinessRules= ruleBusinessRules EOF ;
    public final EObject entryRuleBusinessRules() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRules = null;


        try {
            // InternalCqrsDsl.g:4776:54: (iv_ruleBusinessRules= ruleBusinessRules EOF )
            // InternalCqrsDsl.g:4777:2: iv_ruleBusinessRules= ruleBusinessRules EOF
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
    // InternalCqrsDsl.g:4783:1: ruleBusinessRules returns [EObject current=null] : (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* ) ;
    public final EObject ruleBusinessRules() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_businessRuleInstances_1_0 = null;

        EObject lv_businessRuleInstances_3_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4789:2: ( (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* ) )
            // InternalCqrsDsl.g:4790:2: (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* )
            {
            // InternalCqrsDsl.g:4790:2: (otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )* )
            // InternalCqrsDsl.g:4791:3: otherlv_0= 'business-rules' ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) ) (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )*
            {
            otherlv_0=(Token)match(input,74,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getBusinessRulesAccess().getBusinessRulesKeyword_0());
            		
            // InternalCqrsDsl.g:4795:3: ( (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance ) )
            // InternalCqrsDsl.g:4796:4: (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance )
            {
            // InternalCqrsDsl.g:4796:4: (lv_businessRuleInstances_1_0= ruleBusinessRuleInstance )
            // InternalCqrsDsl.g:4797:5: lv_businessRuleInstances_1_0= ruleBusinessRuleInstance
            {

            					newCompositeNode(grammarAccess.getBusinessRulesAccess().getBusinessRuleInstancesBusinessRuleInstanceParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_112);
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

            // InternalCqrsDsl.g:4814:3: (otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) ) )*
            loop146:
            do {
                int alt146=2;
                int LA146_0 = input.LA(1);

                if ( (LA146_0==28) ) {
                    alt146=1;
                }


                switch (alt146) {
            	case 1 :
            	    // InternalCqrsDsl.g:4815:4: otherlv_2= ',' ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) )
            	    {
            	    otherlv_2=(Token)match(input,28,FOLLOW_4); 

            	    				newLeafNode(otherlv_2, grammarAccess.getBusinessRulesAccess().getCommaKeyword_2_0());
            	    			
            	    // InternalCqrsDsl.g:4819:4: ( (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance ) )
            	    // InternalCqrsDsl.g:4820:5: (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance )
            	    {
            	    // InternalCqrsDsl.g:4820:5: (lv_businessRuleInstances_3_0= ruleBusinessRuleInstance )
            	    // InternalCqrsDsl.g:4821:6: lv_businessRuleInstances_3_0= ruleBusinessRuleInstance
            	    {

            	    						newCompositeNode(grammarAccess.getBusinessRulesAccess().getBusinessRuleInstancesBusinessRuleInstanceParserRuleCall_2_1_0());
            	    					
            	    pushFollow(FOLLOW_112);
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
            	    break loop146;
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
    // InternalCqrsDsl.g:4843:1: entryRuleOverriddenTypeMetaInfo returns [EObject current=null] : iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF ;
    public final EObject entryRuleOverriddenTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOverriddenTypeMetaInfo = null;


        try {
            // InternalCqrsDsl.g:4843:63: (iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF )
            // InternalCqrsDsl.g:4844:2: iv_ruleOverriddenTypeMetaInfo= ruleOverriddenTypeMetaInfo EOF
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
    // InternalCqrsDsl.g:4850:1: ruleOverriddenTypeMetaInfo returns [EObject current=null] : (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' ) ;
    public final EObject ruleOverriddenTypeMetaInfo() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject lv_metaInfo_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4856:2: ( (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' ) )
            // InternalCqrsDsl.g:4857:2: (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' )
            {
            // InternalCqrsDsl.g:4857:2: (otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}' )
            // InternalCqrsDsl.g:4858:3: otherlv_0= '{' ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) ) otherlv_2= '}'
            {
            otherlv_0=(Token)match(input,14,FOLLOW_113); 

            			newLeafNode(otherlv_0, grammarAccess.getOverriddenTypeMetaInfoAccess().getLeftCurlyBracketKeyword_0());
            		
            // InternalCqrsDsl.g:4862:3: ( (lv_metaInfo_1_0= ruleTypeMetaInfo ) )
            // InternalCqrsDsl.g:4863:4: (lv_metaInfo_1_0= ruleTypeMetaInfo )
            {
            // InternalCqrsDsl.g:4863:4: (lv_metaInfo_1_0= ruleTypeMetaInfo )
            // InternalCqrsDsl.g:4864:5: lv_metaInfo_1_0= ruleTypeMetaInfo
            {

            					newCompositeNode(grammarAccess.getOverriddenTypeMetaInfoAccess().getMetaInfoTypeMetaInfoParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_25);
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
    // InternalCqrsDsl.g:4889:1: entryRuleConstraintInstance returns [EObject current=null] : iv_ruleConstraintInstance= ruleConstraintInstance EOF ;
    public final EObject entryRuleConstraintInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstraintInstance = null;


        try {
            // InternalCqrsDsl.g:4889:59: (iv_ruleConstraintInstance= ruleConstraintInstance EOF )
            // InternalCqrsDsl.g:4890:2: iv_ruleConstraintInstance= ruleConstraintInstance EOF
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
    // InternalCqrsDsl.g:4896:1: ruleConstraintInstance returns [EObject current=null] : ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) ;
    public final EObject ruleConstraintInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_params_2_0 = null;

        EObject lv_params_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4902:2: ( ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) )
            // InternalCqrsDsl.g:4903:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            {
            // InternalCqrsDsl.g:4903:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            // InternalCqrsDsl.g:4904:3: ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            {
            // InternalCqrsDsl.g:4904:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4905:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4905:4: ( ruleFQN )
            // InternalCqrsDsl.g:4906:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getConstraintInstanceRule());
            					}
            				

            					newCompositeNode(grammarAccess.getConstraintInstanceAccess().getConstraintConstraintCrossReference_0_0());
            				
            pushFollow(FOLLOW_73);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:4920:3: (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            int alt148=2;
            int LA148_0 = input.LA(1);

            if ( (LA148_0==51) ) {
                alt148=1;
            }
            switch (alt148) {
                case 1 :
                    // InternalCqrsDsl.g:4921:4: otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')'
                    {
                    otherlv_1=(Token)match(input,51,FOLLOW_74); 

                    				newLeafNode(otherlv_1, grammarAccess.getConstraintInstanceAccess().getLeftParenthesisKeyword_1_0());
                    			
                    // InternalCqrsDsl.g:4925:4: ( (lv_params_2_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:4926:5: (lv_params_2_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:4926:5: (lv_params_2_0= ruleLiteral )
                    // InternalCqrsDsl.g:4927:6: lv_params_2_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getConstraintInstanceAccess().getParamsLiteralParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_75);
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

                    // InternalCqrsDsl.g:4944:4: (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )*
                    loop147:
                    do {
                        int alt147=2;
                        int LA147_0 = input.LA(1);

                        if ( (LA147_0==28) ) {
                            alt147=1;
                        }


                        switch (alt147) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:4945:5: otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) )
                    	    {
                    	    otherlv_3=(Token)match(input,28,FOLLOW_74); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getConstraintInstanceAccess().getCommaKeyword_1_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:4949:5: ( (lv_params_4_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:4950:6: (lv_params_4_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:4950:6: (lv_params_4_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:4951:7: lv_params_4_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getConstraintInstanceAccess().getParamsLiteralParserRuleCall_1_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_75);
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
                    	    break loop147;
                        }
                    } while (true);

                    otherlv_5=(Token)match(input,52,FOLLOW_2); 

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
    // InternalCqrsDsl.g:4978:1: entryRuleBusinessRuleInstance returns [EObject current=null] : iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF ;
    public final EObject entryRuleBusinessRuleInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessRuleInstance = null;


        try {
            // InternalCqrsDsl.g:4978:61: (iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF )
            // InternalCqrsDsl.g:4979:2: iv_ruleBusinessRuleInstance= ruleBusinessRuleInstance EOF
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
    // InternalCqrsDsl.g:4985:1: ruleBusinessRuleInstance returns [EObject current=null] : ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) ;
    public final EObject ruleBusinessRuleInstance() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_params_2_0 = null;

        EObject lv_params_4_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:4991:2: ( ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? ) )
            // InternalCqrsDsl.g:4992:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            {
            // InternalCqrsDsl.g:4992:2: ( ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )? )
            // InternalCqrsDsl.g:4993:3: ( ( ruleFQN ) ) (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            {
            // InternalCqrsDsl.g:4993:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:4994:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:4994:4: ( ruleFQN )
            // InternalCqrsDsl.g:4995:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBusinessRuleInstanceRule());
            					}
            				

            					newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getBusinessRuleBusinessRuleCrossReference_0_0());
            				
            pushFollow(FOLLOW_73);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:5009:3: (otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')' )?
            int alt150=2;
            int LA150_0 = input.LA(1);

            if ( (LA150_0==51) ) {
                alt150=1;
            }
            switch (alt150) {
                case 1 :
                    // InternalCqrsDsl.g:5010:4: otherlv_1= '(' ( (lv_params_2_0= ruleLiteral ) ) (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )* otherlv_5= ')'
                    {
                    otherlv_1=(Token)match(input,51,FOLLOW_74); 

                    				newLeafNode(otherlv_1, grammarAccess.getBusinessRuleInstanceAccess().getLeftParenthesisKeyword_1_0());
                    			
                    // InternalCqrsDsl.g:5014:4: ( (lv_params_2_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:5015:5: (lv_params_2_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:5015:5: (lv_params_2_0= ruleLiteral )
                    // InternalCqrsDsl.g:5016:6: lv_params_2_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getParamsLiteralParserRuleCall_1_1_0());
                    					
                    pushFollow(FOLLOW_75);
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

                    // InternalCqrsDsl.g:5033:4: (otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) ) )*
                    loop149:
                    do {
                        int alt149=2;
                        int LA149_0 = input.LA(1);

                        if ( (LA149_0==28) ) {
                            alt149=1;
                        }


                        switch (alt149) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5034:5: otherlv_3= ',' ( (lv_params_4_0= ruleLiteral ) )
                    	    {
                    	    otherlv_3=(Token)match(input,28,FOLLOW_74); 

                    	    					newLeafNode(otherlv_3, grammarAccess.getBusinessRuleInstanceAccess().getCommaKeyword_1_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5038:5: ( (lv_params_4_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:5039:6: (lv_params_4_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:5039:6: (lv_params_4_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:5040:7: lv_params_4_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getBusinessRuleInstanceAccess().getParamsLiteralParserRuleCall_1_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_75);
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
                    	    break loop149;
                        }
                    } while (true);

                    otherlv_5=(Token)match(input,52,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5067:1: entryRuleAnnotationInstance returns [EObject current=null] : iv_ruleAnnotationInstance= ruleAnnotationInstance EOF ;
    public final EObject entryRuleAnnotationInstance() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnnotationInstance = null;


        try {
            // InternalCqrsDsl.g:5067:59: (iv_ruleAnnotationInstance= ruleAnnotationInstance EOF )
            // InternalCqrsDsl.g:5068:2: iv_ruleAnnotationInstance= ruleAnnotationInstance EOF
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
    // InternalCqrsDsl.g:5074:1: ruleAnnotationInstance returns [EObject current=null] : (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? ) ;
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
            // InternalCqrsDsl.g:5080:2: ( (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? ) )
            // InternalCqrsDsl.g:5081:2: (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? )
            {
            // InternalCqrsDsl.g:5081:2: (otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )? )
            // InternalCqrsDsl.g:5082:3: otherlv_0= '@' ( ( ruleFQN ) ) (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )?
            {
            otherlv_0=(Token)match(input,75,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getAnnotationInstanceAccess().getCommercialAtKeyword_0());
            		
            // InternalCqrsDsl.g:5086:3: ( ( ruleFQN ) )
            // InternalCqrsDsl.g:5087:4: ( ruleFQN )
            {
            // InternalCqrsDsl.g:5087:4: ( ruleFQN )
            // InternalCqrsDsl.g:5088:5: ruleFQN
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAnnotationInstanceRule());
            					}
            				

            					newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getAnnotationAnnotationCrossReference_1_0());
            				
            pushFollow(FOLLOW_73);
            ruleFQN();

            state._fsp--;


            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCqrsDsl.g:5102:3: (otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')' )?
            int alt152=2;
            int LA152_0 = input.LA(1);

            if ( (LA152_0==51) ) {
                alt152=1;
            }
            switch (alt152) {
                case 1 :
                    // InternalCqrsDsl.g:5103:4: otherlv_2= '(' ( (lv_params_3_0= ruleLiteral ) ) (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )* otherlv_6= ')'
                    {
                    otherlv_2=(Token)match(input,51,FOLLOW_74); 

                    				newLeafNode(otherlv_2, grammarAccess.getAnnotationInstanceAccess().getLeftParenthesisKeyword_2_0());
                    			
                    // InternalCqrsDsl.g:5107:4: ( (lv_params_3_0= ruleLiteral ) )
                    // InternalCqrsDsl.g:5108:5: (lv_params_3_0= ruleLiteral )
                    {
                    // InternalCqrsDsl.g:5108:5: (lv_params_3_0= ruleLiteral )
                    // InternalCqrsDsl.g:5109:6: lv_params_3_0= ruleLiteral
                    {

                    						newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getParamsLiteralParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_75);
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

                    // InternalCqrsDsl.g:5126:4: (otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) ) )*
                    loop151:
                    do {
                        int alt151=2;
                        int LA151_0 = input.LA(1);

                        if ( (LA151_0==28) ) {
                            alt151=1;
                        }


                        switch (alt151) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5127:5: otherlv_4= ',' ( (lv_params_5_0= ruleLiteral ) )
                    	    {
                    	    otherlv_4=(Token)match(input,28,FOLLOW_74); 

                    	    					newLeafNode(otherlv_4, grammarAccess.getAnnotationInstanceAccess().getCommaKeyword_2_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5131:5: ( (lv_params_5_0= ruleLiteral ) )
                    	    // InternalCqrsDsl.g:5132:6: (lv_params_5_0= ruleLiteral )
                    	    {
                    	    // InternalCqrsDsl.g:5132:6: (lv_params_5_0= ruleLiteral )
                    	    // InternalCqrsDsl.g:5133:7: lv_params_5_0= ruleLiteral
                    	    {

                    	    							newCompositeNode(grammarAccess.getAnnotationInstanceAccess().getParamsLiteralParserRuleCall_2_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_75);
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
                    	    break loop151;
                        }
                    } while (true);

                    otherlv_6=(Token)match(input,52,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5160:1: entryRuleService returns [EObject current=null] : iv_ruleService= ruleService EOF ;
    public final EObject entryRuleService() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleService = null;


        try {
            // InternalCqrsDsl.g:5160:48: (iv_ruleService= ruleService EOF )
            // InternalCqrsDsl.g:5161:2: iv_ruleService= ruleService EOF
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
    // InternalCqrsDsl.g:5167:1: ruleService returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' ) ;
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
            // InternalCqrsDsl.g:5173:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' ) )
            // InternalCqrsDsl.g:5174:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' )
            {
            // InternalCqrsDsl.g:5174:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}' )
            // InternalCqrsDsl.g:5175:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'service' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_businessRules_4_0= ruleBusinessRule ) )* ( (lv_methods_5_0= ruleMethod ) )* otherlv_6= '}'
            {
            // InternalCqrsDsl.g:5175:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt153=2;
            int LA153_0 = input.LA(1);

            if ( (LA153_0==RULE_DOC) ) {
                alt153=1;
            }
            switch (alt153) {
                case 1 :
                    // InternalCqrsDsl.g:5176:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5176:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5177:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_114); 

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

            otherlv_1=(Token)match(input,76,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getServiceAccess().getServiceKeyword_1());
            		
            // InternalCqrsDsl.g:5197:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5198:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5198:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5199:5: lv_name_2_0= RULE_ID
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

            otherlv_3=(Token)match(input,14,FOLLOW_63); 

            			newLeafNode(otherlv_3, grammarAccess.getServiceAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalCqrsDsl.g:5219:3: ( (lv_businessRules_4_0= ruleBusinessRule ) )*
            loop154:
            do {
                int alt154=2;
                int LA154_0 = input.LA(1);

                if ( (LA154_0==RULE_DOC) ) {
                    int LA154_1 = input.LA(2);

                    if ( (LA154_1==40) ) {
                        alt154=1;
                    }


                }


                switch (alt154) {
            	case 1 :
            	    // InternalCqrsDsl.g:5220:4: (lv_businessRules_4_0= ruleBusinessRule )
            	    {
            	    // InternalCqrsDsl.g:5220:4: (lv_businessRules_4_0= ruleBusinessRule )
            	    // InternalCqrsDsl.g:5221:5: lv_businessRules_4_0= ruleBusinessRule
            	    {

            	    					newCompositeNode(grammarAccess.getServiceAccess().getBusinessRulesBusinessRuleParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_63);
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
            	    break loop154;
                }
            } while (true);

            // InternalCqrsDsl.g:5238:3: ( (lv_methods_5_0= ruleMethod ) )*
            loop155:
            do {
                int alt155=2;
                int LA155_0 = input.LA(1);

                if ( (LA155_0==RULE_DOC||LA155_0==62) ) {
                    alt155=1;
                }


                switch (alt155) {
            	case 1 :
            	    // InternalCqrsDsl.g:5239:4: (lv_methods_5_0= ruleMethod )
            	    {
            	    // InternalCqrsDsl.g:5239:4: (lv_methods_5_0= ruleMethod )
            	    // InternalCqrsDsl.g:5240:5: lv_methods_5_0= ruleMethod
            	    {

            	    					newCompositeNode(grammarAccess.getServiceAccess().getMethodsMethodParserRuleCall_5_0());
            	    				
            	    pushFollow(FOLLOW_63);
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
            	    break loop155;
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
    // InternalCqrsDsl.g:5265:1: entryRuleCommand returns [EObject current=null] : iv_ruleCommand= ruleCommand EOF ;
    public final EObject entryRuleCommand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommand = null;


        try {
            // InternalCqrsDsl.g:5265:48: (iv_ruleCommand= ruleCommand EOF )
            // InternalCqrsDsl.g:5266:2: iv_ruleCommand= ruleCommand EOF
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
    // InternalCqrsDsl.g:5272:1: ruleCommand returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' ) ;
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
            // InternalCqrsDsl.g:5278:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' ) )
            // InternalCqrsDsl.g:5279:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' )
            {
            // InternalCqrsDsl.g:5279:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}' )
            // InternalCqrsDsl.g:5280:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'target' ( ( ruleFQN ) ) )? (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )? otherlv_7= '{' ( (lv_attributes_8_0= ruleAttribute ) )* (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )? otherlv_11= '}'
            {
            // InternalCqrsDsl.g:5280:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt156=2;
            int LA156_0 = input.LA(1);

            if ( (LA156_0==RULE_DOC) ) {
                alt156=1;
            }
            switch (alt156) {
                case 1 :
                    // InternalCqrsDsl.g:5281:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5281:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5282:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_115); 

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

            otherlv_1=(Token)match(input,77,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getCommandAccess().getCommandKeyword_1());
            		
            // InternalCqrsDsl.g:5302:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5303:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5303:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5304:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_116); 

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

            // InternalCqrsDsl.g:5320:3: (otherlv_3= 'target' ( ( ruleFQN ) ) )?
            int alt157=2;
            int LA157_0 = input.LA(1);

            if ( (LA157_0==78) ) {
                alt157=1;
            }
            switch (alt157) {
                case 1 :
                    // InternalCqrsDsl.g:5321:4: otherlv_3= 'target' ( ( ruleFQN ) )
                    {
                    otherlv_3=(Token)match(input,78,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getCommandAccess().getTargetKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:5325:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5326:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5326:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5327:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCommandRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getCommandAccess().getTargetAbstractMethodCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_117);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalCqrsDsl.g:5342:3: (otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) ) )?
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( (LA158_0==79) ) {
                alt158=1;
            }
            switch (alt158) {
                case 1 :
                    // InternalCqrsDsl.g:5343:4: otherlv_5= 'sla' ( (lv_acceptable_6_0= ruleDuration ) )
                    {
                    otherlv_5=(Token)match(input,79,FOLLOW_12); 

                    				newLeafNode(otherlv_5, grammarAccess.getCommandAccess().getSlaKeyword_4_0());
                    			
                    // InternalCqrsDsl.g:5347:4: ( (lv_acceptable_6_0= ruleDuration ) )
                    // InternalCqrsDsl.g:5348:5: (lv_acceptable_6_0= ruleDuration )
                    {
                    // InternalCqrsDsl.g:5348:5: (lv_acceptable_6_0= ruleDuration )
                    // InternalCqrsDsl.g:5349:6: lv_acceptable_6_0= ruleDuration
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

            otherlv_7=(Token)match(input,14,FOLLOW_48); 

            			newLeafNode(otherlv_7, grammarAccess.getCommandAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalCqrsDsl.g:5371:3: ( (lv_attributes_8_0= ruleAttribute ) )*
            loop159:
            do {
                int alt159=2;
                int LA159_0 = input.LA(1);

                if ( ((LA159_0>=RULE_DOC && LA159_0<=RULE_ID)||LA159_0==71) ) {
                    alt159=1;
                }


                switch (alt159) {
            	case 1 :
            	    // InternalCqrsDsl.g:5372:4: (lv_attributes_8_0= ruleAttribute )
            	    {
            	    // InternalCqrsDsl.g:5372:4: (lv_attributes_8_0= ruleAttribute )
            	    // InternalCqrsDsl.g:5373:5: lv_attributes_8_0= ruleAttribute
            	    {

            	    					newCompositeNode(grammarAccess.getCommandAccess().getAttributesAttributeParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_48);
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
            	    break loop159;
                }
            } while (true);

            // InternalCqrsDsl.g:5390:3: (otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) ) )?
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==39) ) {
                alt160=1;
            }
            switch (alt160) {
                case 1 :
                    // InternalCqrsDsl.g:5391:4: otherlv_9= 'message' ( (lv_message_10_0= RULE_STRING ) )
                    {
                    otherlv_9=(Token)match(input,39,FOLLOW_35); 

                    				newLeafNode(otherlv_9, grammarAccess.getCommandAccess().getMessageKeyword_7_0());
                    			
                    // InternalCqrsDsl.g:5395:4: ( (lv_message_10_0= RULE_STRING ) )
                    // InternalCqrsDsl.g:5396:5: (lv_message_10_0= RULE_STRING )
                    {
                    // InternalCqrsDsl.g:5396:5: (lv_message_10_0= RULE_STRING )
                    // InternalCqrsDsl.g:5397:6: lv_message_10_0= RULE_STRING
                    {
                    lv_message_10_0=(Token)match(input,RULE_STRING,FOLLOW_25); 

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
    // InternalCqrsDsl.g:5422:1: entryRuleCommandHandler returns [EObject current=null] : iv_ruleCommandHandler= ruleCommandHandler EOF ;
    public final EObject entryRuleCommandHandler() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommandHandler = null;


        try {
            // InternalCqrsDsl.g:5422:55: (iv_ruleCommandHandler= ruleCommandHandler EOF )
            // InternalCqrsDsl.g:5423:2: iv_ruleCommandHandler= ruleCommandHandler EOF
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
    // InternalCqrsDsl.g:5429:1: ruleCommandHandler returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( (otherlv_4= RULE_ID ) ) (otherlv_5= ',' ( (otherlv_6= RULE_ID ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? ) ;
    public final EObject ruleCommandHandler() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_9=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5435:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( (otherlv_4= RULE_ID ) ) (otherlv_5= ',' ( (otherlv_6= RULE_ID ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? ) )
            // InternalCqrsDsl.g:5436:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( (otherlv_4= RULE_ID ) ) (otherlv_5= ',' ( (otherlv_6= RULE_ID ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? )
            {
            // InternalCqrsDsl.g:5436:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( (otherlv_4= RULE_ID ) ) (otherlv_5= ',' ( (otherlv_6= RULE_ID ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )? )
            // InternalCqrsDsl.g:5437:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'command-handler' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'handles' ( (otherlv_4= RULE_ID ) ) (otherlv_5= ',' ( (otherlv_6= RULE_ID ) ) )* (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            {
            // InternalCqrsDsl.g:5437:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt161=2;
            int LA161_0 = input.LA(1);

            if ( (LA161_0==RULE_DOC) ) {
                alt161=1;
            }
            switch (alt161) {
                case 1 :
                    // InternalCqrsDsl.g:5438:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5438:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5439:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_118); 

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

            otherlv_1=(Token)match(input,80,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getCommandHandlerAccess().getCommandHandlerKeyword_1());
            		
            // InternalCqrsDsl.g:5459:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5460:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5460:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5461:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_119); 

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

            otherlv_3=(Token)match(input,81,FOLLOW_4); 

            			newLeafNode(otherlv_3, grammarAccess.getCommandHandlerAccess().getHandlesKeyword_3());
            		
            // InternalCqrsDsl.g:5481:3: ( (otherlv_4= RULE_ID ) )
            // InternalCqrsDsl.g:5482:4: (otherlv_4= RULE_ID )
            {
            // InternalCqrsDsl.g:5482:4: (otherlv_4= RULE_ID )
            // InternalCqrsDsl.g:5483:5: otherlv_4= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCommandHandlerRule());
            					}
            				
            otherlv_4=(Token)match(input,RULE_ID,FOLLOW_120); 

            					newLeafNode(otherlv_4, grammarAccess.getCommandHandlerAccess().getCommandsCommandCrossReference_4_0());
            				

            }


            }

            // InternalCqrsDsl.g:5494:3: (otherlv_5= ',' ( (otherlv_6= RULE_ID ) ) )*
            loop162:
            do {
                int alt162=2;
                int LA162_0 = input.LA(1);

                if ( (LA162_0==28) ) {
                    alt162=1;
                }


                switch (alt162) {
            	case 1 :
            	    // InternalCqrsDsl.g:5495:4: otherlv_5= ',' ( (otherlv_6= RULE_ID ) )
            	    {
            	    otherlv_5=(Token)match(input,28,FOLLOW_4); 

            	    				newLeafNode(otherlv_5, grammarAccess.getCommandHandlerAccess().getCommaKeyword_5_0());
            	    			
            	    // InternalCqrsDsl.g:5499:4: ( (otherlv_6= RULE_ID ) )
            	    // InternalCqrsDsl.g:5500:5: (otherlv_6= RULE_ID )
            	    {
            	    // InternalCqrsDsl.g:5500:5: (otherlv_6= RULE_ID )
            	    // InternalCqrsDsl.g:5501:6: otherlv_6= RULE_ID
            	    {

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getCommandHandlerRule());
            	    						}
            	    					
            	    otherlv_6=(Token)match(input,RULE_ID,FOLLOW_120); 

            	    						newLeafNode(otherlv_6, grammarAccess.getCommandHandlerAccess().getCommandsCommandCrossReference_5_1_0());
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop162;
                }
            } while (true);

            // InternalCqrsDsl.g:5513:3: (otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )* )?
            int alt164=2;
            int LA164_0 = input.LA(1);

            if ( (LA164_0==82) ) {
                alt164=1;
            }
            switch (alt164) {
                case 1 :
                    // InternalCqrsDsl.g:5514:4: otherlv_7= 'uses' ( ( ruleFQN ) ) (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_7=(Token)match(input,82,FOLLOW_4); 

                    				newLeafNode(otherlv_7, grammarAccess.getCommandHandlerAccess().getUsesKeyword_6_0());
                    			
                    // InternalCqrsDsl.g:5518:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5519:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5519:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5520:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getCommandHandlerRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getCommandHandlerAccess().getAggregatesAggregateCrossReference_6_1_0());
                    					
                    pushFollow(FOLLOW_112);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:5534:4: (otherlv_9= ',' ( ( ruleFQN ) ) )*
                    loop163:
                    do {
                        int alt163=2;
                        int LA163_0 = input.LA(1);

                        if ( (LA163_0==28) ) {
                            alt163=1;
                        }


                        switch (alt163) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5535:5: otherlv_9= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_9=(Token)match(input,28,FOLLOW_4); 

                    	    					newLeafNode(otherlv_9, grammarAccess.getCommandHandlerAccess().getCommaKeyword_6_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5539:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:5540:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:5540:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:5541:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getCommandHandlerRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getCommandHandlerAccess().getAggregatesAggregateCrossReference_6_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_112);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop163;
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
    // InternalCqrsDsl.g:5561:1: entryRuleProjection returns [EObject current=null] : iv_ruleProjection= ruleProjection EOF ;
    public final EObject entryRuleProjection() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProjection = null;


        try {
            // InternalCqrsDsl.g:5561:51: (iv_ruleProjection= ruleProjection EOF )
            // InternalCqrsDsl.g:5562:2: iv_ruleProjection= ruleProjection EOF
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
    // InternalCqrsDsl.g:5568:1: ruleProjection returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? ) ;
    public final EObject ruleProjection() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5574:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? ) )
            // InternalCqrsDsl.g:5575:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? )
            {
            // InternalCqrsDsl.g:5575:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )? )
            // InternalCqrsDsl.g:5576:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'projection' ( (lv_name_2_0= RULE_ID ) ) (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )?
            {
            // InternalCqrsDsl.g:5576:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt165=2;
            int LA165_0 = input.LA(1);

            if ( (LA165_0==RULE_DOC) ) {
                alt165=1;
            }
            switch (alt165) {
                case 1 :
                    // InternalCqrsDsl.g:5577:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5577:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5578:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_121); 

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

            otherlv_1=(Token)match(input,83,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getProjectionAccess().getProjectionKeyword_1());
            		
            // InternalCqrsDsl.g:5598:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5599:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5599:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5600:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_122); 

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

            // InternalCqrsDsl.g:5616:3: (otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )* )?
            int alt167=2;
            int LA167_0 = input.LA(1);

            if ( (LA167_0==36) ) {
                alt167=1;
            }
            switch (alt167) {
                case 1 :
                    // InternalCqrsDsl.g:5617:4: otherlv_3= 'input' ( ( ruleFQN ) ) (otherlv_5= ',' ( ( ruleFQN ) ) )*
                    {
                    otherlv_3=(Token)match(input,36,FOLLOW_4); 

                    				newLeafNode(otherlv_3, grammarAccess.getProjectionAccess().getInputKeyword_3_0());
                    			
                    // InternalCqrsDsl.g:5621:4: ( ( ruleFQN ) )
                    // InternalCqrsDsl.g:5622:5: ( ruleFQN )
                    {
                    // InternalCqrsDsl.g:5622:5: ( ruleFQN )
                    // InternalCqrsDsl.g:5623:6: ruleFQN
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getProjectionRule());
                    						}
                    					

                    						newCompositeNode(grammarAccess.getProjectionAccess().getEventsEventCrossReference_3_1_0());
                    					
                    pushFollow(FOLLOW_112);
                    ruleFQN();

                    state._fsp--;


                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalCqrsDsl.g:5637:4: (otherlv_5= ',' ( ( ruleFQN ) ) )*
                    loop166:
                    do {
                        int alt166=2;
                        int LA166_0 = input.LA(1);

                        if ( (LA166_0==28) ) {
                            alt166=1;
                        }


                        switch (alt166) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:5638:5: otherlv_5= ',' ( ( ruleFQN ) )
                    	    {
                    	    otherlv_5=(Token)match(input,28,FOLLOW_4); 

                    	    					newLeafNode(otherlv_5, grammarAccess.getProjectionAccess().getCommaKeyword_3_2_0());
                    	    				
                    	    // InternalCqrsDsl.g:5642:5: ( ( ruleFQN ) )
                    	    // InternalCqrsDsl.g:5643:6: ( ruleFQN )
                    	    {
                    	    // InternalCqrsDsl.g:5643:6: ( ruleFQN )
                    	    // InternalCqrsDsl.g:5644:7: ruleFQN
                    	    {

                    	    							if (current==null) {
                    	    								current = createModelElement(grammarAccess.getProjectionRule());
                    	    							}
                    	    						

                    	    							newCompositeNode(grammarAccess.getProjectionAccess().getEventsEventCrossReference_3_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_112);
                    	    ruleFQN();

                    	    state._fsp--;


                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop166;
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
    // InternalCqrsDsl.g:5664:1: entryRuleView returns [EObject current=null] : iv_ruleView= ruleView EOF ;
    public final EObject entryRuleView() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleView = null;


        try {
            // InternalCqrsDsl.g:5664:45: (iv_ruleView= ruleView EOF )
            // InternalCqrsDsl.g:5665:2: iv_ruleView= ruleView EOF
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
    // InternalCqrsDsl.g:5671:1: ruleView returns [EObject current=null] : ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( (otherlv_4= RULE_ID ) ) ) ;
    public final EObject ruleView() throws RecognitionException {
        EObject current = null;

        Token lv_doc_0_0=null;
        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5677:2: ( ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( (otherlv_4= RULE_ID ) ) ) )
            // InternalCqrsDsl.g:5678:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( (otherlv_4= RULE_ID ) ) )
            {
            // InternalCqrsDsl.g:5678:2: ( ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( (otherlv_4= RULE_ID ) ) )
            // InternalCqrsDsl.g:5679:3: ( (lv_doc_0_0= RULE_DOC ) )? otherlv_1= 'view' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'uses' ( (otherlv_4= RULE_ID ) )
            {
            // InternalCqrsDsl.g:5679:3: ( (lv_doc_0_0= RULE_DOC ) )?
            int alt168=2;
            int LA168_0 = input.LA(1);

            if ( (LA168_0==RULE_DOC) ) {
                alt168=1;
            }
            switch (alt168) {
                case 1 :
                    // InternalCqrsDsl.g:5680:4: (lv_doc_0_0= RULE_DOC )
                    {
                    // InternalCqrsDsl.g:5680:4: (lv_doc_0_0= RULE_DOC )
                    // InternalCqrsDsl.g:5681:5: lv_doc_0_0= RULE_DOC
                    {
                    lv_doc_0_0=(Token)match(input,RULE_DOC,FOLLOW_123); 

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

            otherlv_1=(Token)match(input,84,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getViewAccess().getViewKeyword_1());
            		
            // InternalCqrsDsl.g:5701:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCqrsDsl.g:5702:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCqrsDsl.g:5702:4: (lv_name_2_0= RULE_ID )
            // InternalCqrsDsl.g:5703:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_124); 

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

            otherlv_3=(Token)match(input,82,FOLLOW_4); 

            			newLeafNode(otherlv_3, grammarAccess.getViewAccess().getUsesKeyword_3());
            		
            // InternalCqrsDsl.g:5723:3: ( (otherlv_4= RULE_ID ) )
            // InternalCqrsDsl.g:5724:4: (otherlv_4= RULE_ID )
            {
            // InternalCqrsDsl.g:5724:4: (otherlv_4= RULE_ID )
            // InternalCqrsDsl.g:5725:5: otherlv_4= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getViewRule());
            					}
            				
            otherlv_4=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_4, grammarAccess.getViewAccess().getProjectionProjectionCrossReference_4_0());
            				

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
    // $ANTLR end "ruleView"


    // $ANTLR start "entryRuleLiteral"
    // InternalCqrsDsl.g:5740:1: entryRuleLiteral returns [EObject current=null] : iv_ruleLiteral= ruleLiteral EOF ;
    public final EObject entryRuleLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleLiteral = null;


        try {
            // InternalCqrsDsl.g:5740:48: (iv_ruleLiteral= ruleLiteral EOF )
            // InternalCqrsDsl.g:5741:2: iv_ruleLiteral= ruleLiteral EOF
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
    // InternalCqrsDsl.g:5747:1: ruleLiteral returns [EObject current=null] : (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral ) ;
    public final EObject ruleLiteral() throws RecognitionException {
        EObject current = null;

        EObject this_NullLiteral_0 = null;

        EObject this_BooleanLiteral_1 = null;

        EObject this_NumberLiteral_2 = null;

        EObject this_StringLiteral_3 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5753:2: ( (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral ) )
            // InternalCqrsDsl.g:5754:2: (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral )
            {
            // InternalCqrsDsl.g:5754:2: (this_NullLiteral_0= ruleNullLiteral | this_BooleanLiteral_1= ruleBooleanLiteral | this_NumberLiteral_2= ruleNumberLiteral | this_StringLiteral_3= ruleStringLiteral )
            int alt169=4;
            switch ( input.LA(1) ) {
            case 89:
                {
                alt169=1;
                }
                break;
            case 87:
            case 88:
                {
                alt169=2;
                }
                break;
            case RULE_INT:
            case RULE_HEX:
            case RULE_DECIMAL:
                {
                alt169=3;
                }
                break;
            case RULE_STRING:
                {
                alt169=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 169, 0, input);

                throw nvae;
            }

            switch (alt169) {
                case 1 :
                    // InternalCqrsDsl.g:5755:3: this_NullLiteral_0= ruleNullLiteral
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
                    // InternalCqrsDsl.g:5764:3: this_BooleanLiteral_1= ruleBooleanLiteral
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
                    // InternalCqrsDsl.g:5773:3: this_NumberLiteral_2= ruleNumberLiteral
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
                    // InternalCqrsDsl.g:5782:3: this_StringLiteral_3= ruleStringLiteral
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


    // $ANTLR start "entryRuleFQN"
    // InternalCqrsDsl.g:5794:1: entryRuleFQN returns [String current=null] : iv_ruleFQN= ruleFQN EOF ;
    public final String entryRuleFQN() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFQN = null;


        try {
            // InternalCqrsDsl.g:5794:43: (iv_ruleFQN= ruleFQN EOF )
            // InternalCqrsDsl.g:5795:2: iv_ruleFQN= ruleFQN EOF
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
    // InternalCqrsDsl.g:5801:1: ruleFQN returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleFQN() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5807:2: ( (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) )
            // InternalCqrsDsl.g:5808:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            {
            // InternalCqrsDsl.g:5808:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            // InternalCqrsDsl.g:5809:3: this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_125); 

            			current.merge(this_ID_0);
            		

            			newLeafNode(this_ID_0, grammarAccess.getFQNAccess().getIDTerminalRuleCall_0());
            		
            // InternalCqrsDsl.g:5816:3: (kw= '.' this_ID_2= RULE_ID )*
            loop170:
            do {
                int alt170=2;
                int LA170_0 = input.LA(1);

                if ( (LA170_0==85) ) {
                    int LA170_2 = input.LA(2);

                    if ( (LA170_2==RULE_ID) ) {
                        alt170=1;
                    }


                }


                switch (alt170) {
            	case 1 :
            	    // InternalCqrsDsl.g:5817:4: kw= '.' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,85,FOLLOW_4); 

            	    				current.merge(kw);
            	    				newLeafNode(kw, grammarAccess.getFQNAccess().getFullStopKeyword_1_0());
            	    			
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_125); 

            	    				current.merge(this_ID_2);
            	    			

            	    				newLeafNode(this_ID_2, grammarAccess.getFQNAccess().getIDTerminalRuleCall_1_1());
            	    			

            	    }
            	    break;

            	default :
            	    break loop170;
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
    // InternalCqrsDsl.g:5834:1: entryRuleFQNWithWildcard returns [String current=null] : iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF ;
    public final String entryRuleFQNWithWildcard() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFQNWithWildcard = null;


        try {
            // InternalCqrsDsl.g:5834:55: (iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF )
            // InternalCqrsDsl.g:5835:2: iv_ruleFQNWithWildcard= ruleFQNWithWildcard EOF
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
    // InternalCqrsDsl.g:5841:1: ruleFQNWithWildcard returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_FQN_0= ruleFQN kw= '.' kw= '*' ) ;
    public final AntlrDatatypeRuleToken ruleFQNWithWildcard() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        AntlrDatatypeRuleToken this_FQN_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5847:2: ( (this_FQN_0= ruleFQN kw= '.' kw= '*' ) )
            // InternalCqrsDsl.g:5848:2: (this_FQN_0= ruleFQN kw= '.' kw= '*' )
            {
            // InternalCqrsDsl.g:5848:2: (this_FQN_0= ruleFQN kw= '.' kw= '*' )
            // InternalCqrsDsl.g:5849:3: this_FQN_0= ruleFQN kw= '.' kw= '*'
            {

            			newCompositeNode(grammarAccess.getFQNWithWildcardAccess().getFQNParserRuleCall_0());
            		
            pushFollow(FOLLOW_126);
            this_FQN_0=ruleFQN();

            state._fsp--;


            			current.merge(this_FQN_0);
            		

            			afterParserOrEnumRuleCall();
            		
            kw=(Token)match(input,85,FOLLOW_127); 

            			current.merge(kw);
            			newLeafNode(kw, grammarAccess.getFQNWithWildcardAccess().getFullStopKeyword_1());
            		
            kw=(Token)match(input,86,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5873:1: entryRuleBooleanLiteral returns [EObject current=null] : iv_ruleBooleanLiteral= ruleBooleanLiteral EOF ;
    public final EObject entryRuleBooleanLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBooleanLiteral = null;


        try {
            // InternalCqrsDsl.g:5873:55: (iv_ruleBooleanLiteral= ruleBooleanLiteral EOF )
            // InternalCqrsDsl.g:5874:2: iv_ruleBooleanLiteral= ruleBooleanLiteral EOF
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
    // InternalCqrsDsl.g:5880:1: ruleBooleanLiteral returns [EObject current=null] : ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) ) ;
    public final EObject ruleBooleanLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_1=null;
        Token lv_value_1_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5886:2: ( ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) ) )
            // InternalCqrsDsl.g:5887:2: ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) )
            {
            // InternalCqrsDsl.g:5887:2: ( () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) ) )
            // InternalCqrsDsl.g:5888:3: () ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) )
            {
            // InternalCqrsDsl.g:5888:3: ()
            // InternalCqrsDsl.g:5889:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getBooleanLiteralAccess().getBooleanLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:5895:3: ( ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) ) )
            // InternalCqrsDsl.g:5896:4: ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) )
            {
            // InternalCqrsDsl.g:5896:4: ( (lv_value_1_1= 'false' | lv_value_1_2= 'true' ) )
            // InternalCqrsDsl.g:5897:5: (lv_value_1_1= 'false' | lv_value_1_2= 'true' )
            {
            // InternalCqrsDsl.g:5897:5: (lv_value_1_1= 'false' | lv_value_1_2= 'true' )
            int alt171=2;
            int LA171_0 = input.LA(1);

            if ( (LA171_0==87) ) {
                alt171=1;
            }
            else if ( (LA171_0==88) ) {
                alt171=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 171, 0, input);

                throw nvae;
            }
            switch (alt171) {
                case 1 :
                    // InternalCqrsDsl.g:5898:6: lv_value_1_1= 'false'
                    {
                    lv_value_1_1=(Token)match(input,87,FOLLOW_2); 

                    						newLeafNode(lv_value_1_1, grammarAccess.getBooleanLiteralAccess().getValueFalseKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getBooleanLiteralRule());
                    						}
                    						setWithLastConsumed(current, "value", lv_value_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:5909:6: lv_value_1_2= 'true'
                    {
                    lv_value_1_2=(Token)match(input,88,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5926:1: entryRuleNullLiteral returns [EObject current=null] : iv_ruleNullLiteral= ruleNullLiteral EOF ;
    public final EObject entryRuleNullLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNullLiteral = null;


        try {
            // InternalCqrsDsl.g:5926:52: (iv_ruleNullLiteral= ruleNullLiteral EOF )
            // InternalCqrsDsl.g:5927:2: iv_ruleNullLiteral= ruleNullLiteral EOF
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
    // InternalCqrsDsl.g:5933:1: ruleNullLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= 'null' ) ) ) ;
    public final EObject ruleNullLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:5939:2: ( ( () ( (lv_value_1_0= 'null' ) ) ) )
            // InternalCqrsDsl.g:5940:2: ( () ( (lv_value_1_0= 'null' ) ) )
            {
            // InternalCqrsDsl.g:5940:2: ( () ( (lv_value_1_0= 'null' ) ) )
            // InternalCqrsDsl.g:5941:3: () ( (lv_value_1_0= 'null' ) )
            {
            // InternalCqrsDsl.g:5941:3: ()
            // InternalCqrsDsl.g:5942:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNullLiteralAccess().getNullLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:5948:3: ( (lv_value_1_0= 'null' ) )
            // InternalCqrsDsl.g:5949:4: (lv_value_1_0= 'null' )
            {
            // InternalCqrsDsl.g:5949:4: (lv_value_1_0= 'null' )
            // InternalCqrsDsl.g:5950:5: lv_value_1_0= 'null'
            {
            lv_value_1_0=(Token)match(input,89,FOLLOW_2); 

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
    // InternalCqrsDsl.g:5966:1: entryRuleNumberLiteral returns [EObject current=null] : iv_ruleNumberLiteral= ruleNumberLiteral EOF ;
    public final EObject entryRuleNumberLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNumberLiteral = null;


        try {
            // InternalCqrsDsl.g:5966:54: (iv_ruleNumberLiteral= ruleNumberLiteral EOF )
            // InternalCqrsDsl.g:5967:2: iv_ruleNumberLiteral= ruleNumberLiteral EOF
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
    // InternalCqrsDsl.g:5973:1: ruleNumberLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= ruleNumber ) ) ) ;
    public final EObject ruleNumberLiteral() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_value_1_0 = null;



        	enterRule();

        try {
            // InternalCqrsDsl.g:5979:2: ( ( () ( (lv_value_1_0= ruleNumber ) ) ) )
            // InternalCqrsDsl.g:5980:2: ( () ( (lv_value_1_0= ruleNumber ) ) )
            {
            // InternalCqrsDsl.g:5980:2: ( () ( (lv_value_1_0= ruleNumber ) ) )
            // InternalCqrsDsl.g:5981:3: () ( (lv_value_1_0= ruleNumber ) )
            {
            // InternalCqrsDsl.g:5981:3: ()
            // InternalCqrsDsl.g:5982:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNumberLiteralAccess().getNumberLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:5988:3: ( (lv_value_1_0= ruleNumber ) )
            // InternalCqrsDsl.g:5989:4: (lv_value_1_0= ruleNumber )
            {
            // InternalCqrsDsl.g:5989:4: (lv_value_1_0= ruleNumber )
            // InternalCqrsDsl.g:5990:5: lv_value_1_0= ruleNumber
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
    // InternalCqrsDsl.g:6011:1: entryRuleStringLiteral returns [EObject current=null] : iv_ruleStringLiteral= ruleStringLiteral EOF ;
    public final EObject entryRuleStringLiteral() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStringLiteral = null;


        try {
            // InternalCqrsDsl.g:6011:54: (iv_ruleStringLiteral= ruleStringLiteral EOF )
            // InternalCqrsDsl.g:6012:2: iv_ruleStringLiteral= ruleStringLiteral EOF
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
    // InternalCqrsDsl.g:6018:1: ruleStringLiteral returns [EObject current=null] : ( () ( (lv_value_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleStringLiteral() throws RecognitionException {
        EObject current = null;

        Token lv_value_1_0=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6024:2: ( ( () ( (lv_value_1_0= RULE_STRING ) ) ) )
            // InternalCqrsDsl.g:6025:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            {
            // InternalCqrsDsl.g:6025:2: ( () ( (lv_value_1_0= RULE_STRING ) ) )
            // InternalCqrsDsl.g:6026:3: () ( (lv_value_1_0= RULE_STRING ) )
            {
            // InternalCqrsDsl.g:6026:3: ()
            // InternalCqrsDsl.g:6027:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getStringLiteralAccess().getStringLiteralAction_0(),
            					current);
            			

            }

            // InternalCqrsDsl.g:6033:3: ( (lv_value_1_0= RULE_STRING ) )
            // InternalCqrsDsl.g:6034:4: (lv_value_1_0= RULE_STRING )
            {
            // InternalCqrsDsl.g:6034:4: (lv_value_1_0= RULE_STRING )
            // InternalCqrsDsl.g:6035:5: lv_value_1_0= RULE_STRING
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
    // InternalCqrsDsl.g:6055:1: entryRuleNumber returns [String current=null] : iv_ruleNumber= ruleNumber EOF ;
    public final String entryRuleNumber() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleNumber = null;



        	HiddenTokens myHiddenTokenState = ((XtextTokenStream)input).setHiddenTokens();

        try {
            // InternalCqrsDsl.g:6057:2: (iv_ruleNumber= ruleNumber EOF )
            // InternalCqrsDsl.g:6058:2: iv_ruleNumber= ruleNumber EOF
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
    // InternalCqrsDsl.g:6067:1: ruleNumber returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) ) ;
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
            // InternalCqrsDsl.g:6074:2: ( (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) ) )
            // InternalCqrsDsl.g:6075:2: (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) )
            {
            // InternalCqrsDsl.g:6075:2: (this_HEX_0= RULE_HEX | ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? ) )
            int alt175=2;
            int LA175_0 = input.LA(1);

            if ( (LA175_0==RULE_HEX) ) {
                alt175=1;
            }
            else if ( (LA175_0==RULE_INT||LA175_0==RULE_DECIMAL) ) {
                alt175=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 175, 0, input);

                throw nvae;
            }
            switch (alt175) {
                case 1 :
                    // InternalCqrsDsl.g:6076:3: this_HEX_0= RULE_HEX
                    {
                    this_HEX_0=(Token)match(input,RULE_HEX,FOLLOW_2); 

                    			current.merge(this_HEX_0);
                    		

                    			newLeafNode(this_HEX_0, grammarAccess.getNumberAccess().getHEXTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6084:3: ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? )
                    {
                    // InternalCqrsDsl.g:6084:3: ( (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )? )
                    // InternalCqrsDsl.g:6085:4: (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL ) (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )?
                    {
                    // InternalCqrsDsl.g:6085:4: (this_INT_1= RULE_INT | this_DECIMAL_2= RULE_DECIMAL )
                    int alt172=2;
                    int LA172_0 = input.LA(1);

                    if ( (LA172_0==RULE_INT) ) {
                        alt172=1;
                    }
                    else if ( (LA172_0==RULE_DECIMAL) ) {
                        alt172=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 172, 0, input);

                        throw nvae;
                    }
                    switch (alt172) {
                        case 1 :
                            // InternalCqrsDsl.g:6086:5: this_INT_1= RULE_INT
                            {
                            this_INT_1=(Token)match(input,RULE_INT,FOLLOW_125); 

                            					current.merge(this_INT_1);
                            				

                            					newLeafNode(this_INT_1, grammarAccess.getNumberAccess().getINTTerminalRuleCall_1_0_0());
                            				

                            }
                            break;
                        case 2 :
                            // InternalCqrsDsl.g:6094:5: this_DECIMAL_2= RULE_DECIMAL
                            {
                            this_DECIMAL_2=(Token)match(input,RULE_DECIMAL,FOLLOW_125); 

                            					current.merge(this_DECIMAL_2);
                            				

                            					newLeafNode(this_DECIMAL_2, grammarAccess.getNumberAccess().getDECIMALTerminalRuleCall_1_0_1());
                            				

                            }
                            break;

                    }

                    // InternalCqrsDsl.g:6102:4: (kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL ) )?
                    int alt174=2;
                    int LA174_0 = input.LA(1);

                    if ( (LA174_0==85) ) {
                        alt174=1;
                    }
                    switch (alt174) {
                        case 1 :
                            // InternalCqrsDsl.g:6103:5: kw= '.' (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL )
                            {
                            kw=(Token)match(input,85,FOLLOW_128); 

                            					current.merge(kw);
                            					newLeafNode(kw, grammarAccess.getNumberAccess().getFullStopKeyword_1_1_0());
                            				
                            // InternalCqrsDsl.g:6108:5: (this_INT_4= RULE_INT | this_DECIMAL_5= RULE_DECIMAL )
                            int alt173=2;
                            int LA173_0 = input.LA(1);

                            if ( (LA173_0==RULE_INT) ) {
                                alt173=1;
                            }
                            else if ( (LA173_0==RULE_DECIMAL) ) {
                                alt173=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 173, 0, input);

                                throw nvae;
                            }
                            switch (alt173) {
                                case 1 :
                                    // InternalCqrsDsl.g:6109:6: this_INT_4= RULE_INT
                                    {
                                    this_INT_4=(Token)match(input,RULE_INT,FOLLOW_2); 

                                    						current.merge(this_INT_4);
                                    					

                                    						newLeafNode(this_INT_4, grammarAccess.getNumberAccess().getINTTerminalRuleCall_1_1_1_0());
                                    					

                                    }
                                    break;
                                case 2 :
                                    // InternalCqrsDsl.g:6117:6: this_DECIMAL_5= RULE_DECIMAL
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
    // InternalCqrsDsl.g:6134:1: ruleTimeUnit returns [Enumerator current=null] : ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) ) ;
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
            // InternalCqrsDsl.g:6140:2: ( ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) ) )
            // InternalCqrsDsl.g:6141:2: ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) )
            {
            // InternalCqrsDsl.g:6141:2: ( (enumLiteral_0= 'millis' ) | (enumLiteral_1= 'seconds' ) | (enumLiteral_2= 'minutes' ) | (enumLiteral_3= 'hours' ) | (enumLiteral_4= 'days' ) | (enumLiteral_5= 'weeks' ) | (enumLiteral_6= 'months' ) | (enumLiteral_7= 'years' ) )
            int alt176=8;
            switch ( input.LA(1) ) {
            case 90:
                {
                alt176=1;
                }
                break;
            case 91:
                {
                alt176=2;
                }
                break;
            case 92:
                {
                alt176=3;
                }
                break;
            case 93:
                {
                alt176=4;
                }
                break;
            case 94:
                {
                alt176=5;
                }
                break;
            case 95:
                {
                alt176=6;
                }
                break;
            case 96:
                {
                alt176=7;
                }
                break;
            case 97:
                {
                alt176=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 176, 0, input);

                throw nvae;
            }

            switch (alt176) {
                case 1 :
                    // InternalCqrsDsl.g:6142:3: (enumLiteral_0= 'millis' )
                    {
                    // InternalCqrsDsl.g:6142:3: (enumLiteral_0= 'millis' )
                    // InternalCqrsDsl.g:6143:4: enumLiteral_0= 'millis'
                    {
                    enumLiteral_0=(Token)match(input,90,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getMillisEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getTimeUnitAccess().getMillisEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6150:3: (enumLiteral_1= 'seconds' )
                    {
                    // InternalCqrsDsl.g:6150:3: (enumLiteral_1= 'seconds' )
                    // InternalCqrsDsl.g:6151:4: enumLiteral_1= 'seconds'
                    {
                    enumLiteral_1=(Token)match(input,91,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getSecondsEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getTimeUnitAccess().getSecondsEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6158:3: (enumLiteral_2= 'minutes' )
                    {
                    // InternalCqrsDsl.g:6158:3: (enumLiteral_2= 'minutes' )
                    // InternalCqrsDsl.g:6159:4: enumLiteral_2= 'minutes'
                    {
                    enumLiteral_2=(Token)match(input,92,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getMinutesEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getTimeUnitAccess().getMinutesEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6166:3: (enumLiteral_3= 'hours' )
                    {
                    // InternalCqrsDsl.g:6166:3: (enumLiteral_3= 'hours' )
                    // InternalCqrsDsl.g:6167:4: enumLiteral_3= 'hours'
                    {
                    enumLiteral_3=(Token)match(input,93,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getHoursEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getTimeUnitAccess().getHoursEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:6174:3: (enumLiteral_4= 'days' )
                    {
                    // InternalCqrsDsl.g:6174:3: (enumLiteral_4= 'days' )
                    // InternalCqrsDsl.g:6175:4: enumLiteral_4= 'days'
                    {
                    enumLiteral_4=(Token)match(input,94,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getDaysEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getTimeUnitAccess().getDaysEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:6182:3: (enumLiteral_5= 'weeks' )
                    {
                    // InternalCqrsDsl.g:6182:3: (enumLiteral_5= 'weeks' )
                    // InternalCqrsDsl.g:6183:4: enumLiteral_5= 'weeks'
                    {
                    enumLiteral_5=(Token)match(input,95,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getWeeksEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getTimeUnitAccess().getWeeksEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:6190:3: (enumLiteral_6= 'months' )
                    {
                    // InternalCqrsDsl.g:6190:3: (enumLiteral_6= 'months' )
                    // InternalCqrsDsl.g:6191:4: enumLiteral_6= 'months'
                    {
                    enumLiteral_6=(Token)match(input,96,FOLLOW_2); 

                    				current = grammarAccess.getTimeUnitAccess().getMonthsEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getTimeUnitAccess().getMonthsEnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:6198:3: (enumLiteral_7= 'years' )
                    {
                    // InternalCqrsDsl.g:6198:3: (enumLiteral_7= 'years' )
                    // InternalCqrsDsl.g:6199:4: enumLiteral_7= 'years'
                    {
                    enumLiteral_7=(Token)match(input,97,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6209:1: ruleConsistencyLevel returns [Enumerator current=null] : ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) ) ;
    public final Enumerator ruleConsistencyLevel() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6215:2: ( ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) ) )
            // InternalCqrsDsl.g:6216:2: ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) )
            {
            // InternalCqrsDsl.g:6216:2: ( (enumLiteral_0= 'weak' ) | (enumLiteral_1= 'strong' ) )
            int alt177=2;
            int LA177_0 = input.LA(1);

            if ( (LA177_0==98) ) {
                alt177=1;
            }
            else if ( (LA177_0==99) ) {
                alt177=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 177, 0, input);

                throw nvae;
            }
            switch (alt177) {
                case 1 :
                    // InternalCqrsDsl.g:6217:3: (enumLiteral_0= 'weak' )
                    {
                    // InternalCqrsDsl.g:6217:3: (enumLiteral_0= 'weak' )
                    // InternalCqrsDsl.g:6218:4: enumLiteral_0= 'weak'
                    {
                    enumLiteral_0=(Token)match(input,98,FOLLOW_2); 

                    				current = grammarAccess.getConsistencyLevelAccess().getWeakEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getConsistencyLevelAccess().getWeakEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6225:3: (enumLiteral_1= 'strong' )
                    {
                    // InternalCqrsDsl.g:6225:3: (enumLiteral_1= 'strong' )
                    // InternalCqrsDsl.g:6226:4: enumLiteral_1= 'strong'
                    {
                    enumLiteral_1=(Token)match(input,99,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6236:1: ruleInconsistencyDetection returns [Enumerator current=null] : ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) ) ;
    public final Enumerator ruleInconsistencyDetection() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6242:2: ( ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) ) )
            // InternalCqrsDsl.g:6243:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) )
            {
            // InternalCqrsDsl.g:6243:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) )
            int alt178=3;
            switch ( input.LA(1) ) {
            case 100:
                {
                alt178=1;
                }
                break;
            case 101:
                {
                alt178=2;
                }
                break;
            case 102:
                {
                alt178=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 178, 0, input);

                throw nvae;
            }

            switch (alt178) {
                case 1 :
                    // InternalCqrsDsl.g:6244:3: (enumLiteral_0= 'never' )
                    {
                    // InternalCqrsDsl.g:6244:3: (enumLiteral_0= 'never' )
                    // InternalCqrsDsl.g:6245:4: enumLiteral_0= 'never'
                    {
                    enumLiteral_0=(Token)match(input,100,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyDetectionAccess().getNeverEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getInconsistencyDetectionAccess().getNeverEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6252:3: (enumLiteral_1= 'manually' )
                    {
                    // InternalCqrsDsl.g:6252:3: (enumLiteral_1= 'manually' )
                    // InternalCqrsDsl.g:6253:4: enumLiteral_1= 'manually'
                    {
                    enumLiteral_1=(Token)match(input,101,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyDetectionAccess().getManuallyEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getInconsistencyDetectionAccess().getManuallyEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6260:3: (enumLiteral_2= 'automatic' )
                    {
                    // InternalCqrsDsl.g:6260:3: (enumLiteral_2= 'automatic' )
                    // InternalCqrsDsl.g:6261:4: enumLiteral_2= 'automatic'
                    {
                    enumLiteral_2=(Token)match(input,102,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6271:1: ruleInconsistencyResolution returns [Enumerator current=null] : ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) ) ;
    public final Enumerator ruleInconsistencyResolution() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6277:2: ( ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) ) )
            // InternalCqrsDsl.g:6278:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) )
            {
            // InternalCqrsDsl.g:6278:2: ( (enumLiteral_0= 'never' ) | (enumLiteral_1= 'manually' ) | (enumLiteral_2= 'automatic' ) | (enumLiteral_3= 'workflow' ) )
            int alt179=4;
            switch ( input.LA(1) ) {
            case 100:
                {
                alt179=1;
                }
                break;
            case 101:
                {
                alt179=2;
                }
                break;
            case 102:
                {
                alt179=3;
                }
                break;
            case 103:
                {
                alt179=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 179, 0, input);

                throw nvae;
            }

            switch (alt179) {
                case 1 :
                    // InternalCqrsDsl.g:6279:3: (enumLiteral_0= 'never' )
                    {
                    // InternalCqrsDsl.g:6279:3: (enumLiteral_0= 'never' )
                    // InternalCqrsDsl.g:6280:4: enumLiteral_0= 'never'
                    {
                    enumLiteral_0=(Token)match(input,100,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyResolutionAccess().getNeverEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getInconsistencyResolutionAccess().getNeverEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6287:3: (enumLiteral_1= 'manually' )
                    {
                    // InternalCqrsDsl.g:6287:3: (enumLiteral_1= 'manually' )
                    // InternalCqrsDsl.g:6288:4: enumLiteral_1= 'manually'
                    {
                    enumLiteral_1=(Token)match(input,101,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyResolutionAccess().getManuallyEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getInconsistencyResolutionAccess().getManuallyEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6295:3: (enumLiteral_2= 'automatic' )
                    {
                    // InternalCqrsDsl.g:6295:3: (enumLiteral_2= 'automatic' )
                    // InternalCqrsDsl.g:6296:4: enumLiteral_2= 'automatic'
                    {
                    enumLiteral_2=(Token)match(input,102,FOLLOW_2); 

                    				current = grammarAccess.getInconsistencyResolutionAccess().getAutomaticEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getInconsistencyResolutionAccess().getAutomaticEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6303:3: (enumLiteral_3= 'workflow' )
                    {
                    // InternalCqrsDsl.g:6303:3: (enumLiteral_3= 'workflow' )
                    // InternalCqrsDsl.g:6304:4: enumLiteral_3= 'workflow'
                    {
                    enumLiteral_3=(Token)match(input,103,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6314:1: ruleProtectionLevel returns [Enumerator current=null] : ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) ) ;
    public final Enumerator ruleProtectionLevel() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6320:2: ( ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) ) )
            // InternalCqrsDsl.g:6321:2: ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) )
            {
            // InternalCqrsDsl.g:6321:2: ( (enumLiteral_0= 'none' ) | (enumLiteral_1= 'personal' ) | (enumLiteral_2= 'sensitive' ) )
            int alt180=3;
            switch ( input.LA(1) ) {
            case 104:
                {
                alt180=1;
                }
                break;
            case 105:
                {
                alt180=2;
                }
                break;
            case 106:
                {
                alt180=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 180, 0, input);

                throw nvae;
            }

            switch (alt180) {
                case 1 :
                    // InternalCqrsDsl.g:6322:3: (enumLiteral_0= 'none' )
                    {
                    // InternalCqrsDsl.g:6322:3: (enumLiteral_0= 'none' )
                    // InternalCqrsDsl.g:6323:4: enumLiteral_0= 'none'
                    {
                    enumLiteral_0=(Token)match(input,104,FOLLOW_2); 

                    				current = grammarAccess.getProtectionLevelAccess().getNoneEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getProtectionLevelAccess().getNoneEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6330:3: (enumLiteral_1= 'personal' )
                    {
                    // InternalCqrsDsl.g:6330:3: (enumLiteral_1= 'personal' )
                    // InternalCqrsDsl.g:6331:4: enumLiteral_1= 'personal'
                    {
                    enumLiteral_1=(Token)match(input,105,FOLLOW_2); 

                    				current = grammarAccess.getProtectionLevelAccess().getPersonalEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getProtectionLevelAccess().getPersonalEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6338:3: (enumLiteral_2= 'sensitive' )
                    {
                    // InternalCqrsDsl.g:6338:3: (enumLiteral_2= 'sensitive' )
                    // InternalCqrsDsl.g:6339:4: enumLiteral_2= 'sensitive'
                    {
                    enumLiteral_2=(Token)match(input,106,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6349:1: ruleLawfulBasis returns [Enumerator current=null] : ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) ) ;
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
            // InternalCqrsDsl.g:6355:2: ( ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) ) )
            // InternalCqrsDsl.g:6356:2: ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) )
            {
            // InternalCqrsDsl.g:6356:2: ( (enumLiteral_0= 'consent' ) | (enumLiteral_1= 'explicit_consent' ) | (enumLiteral_2= 'contract' ) | (enumLiteral_3= 'legal_obligation' ) | (enumLiteral_4= 'vital_interests' ) | (enumLiteral_5= 'public_task' ) | (enumLiteral_6= 'legitimate_interests' ) )
            int alt181=7;
            switch ( input.LA(1) ) {
            case 107:
                {
                alt181=1;
                }
                break;
            case 108:
                {
                alt181=2;
                }
                break;
            case 109:
                {
                alt181=3;
                }
                break;
            case 110:
                {
                alt181=4;
                }
                break;
            case 111:
                {
                alt181=5;
                }
                break;
            case 112:
                {
                alt181=6;
                }
                break;
            case 113:
                {
                alt181=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 181, 0, input);

                throw nvae;
            }

            switch (alt181) {
                case 1 :
                    // InternalCqrsDsl.g:6357:3: (enumLiteral_0= 'consent' )
                    {
                    // InternalCqrsDsl.g:6357:3: (enumLiteral_0= 'consent' )
                    // InternalCqrsDsl.g:6358:4: enumLiteral_0= 'consent'
                    {
                    enumLiteral_0=(Token)match(input,107,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getConsentEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getLawfulBasisAccess().getConsentEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6365:3: (enumLiteral_1= 'explicit_consent' )
                    {
                    // InternalCqrsDsl.g:6365:3: (enumLiteral_1= 'explicit_consent' )
                    // InternalCqrsDsl.g:6366:4: enumLiteral_1= 'explicit_consent'
                    {
                    enumLiteral_1=(Token)match(input,108,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getExplicit_consentEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getLawfulBasisAccess().getExplicit_consentEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6373:3: (enumLiteral_2= 'contract' )
                    {
                    // InternalCqrsDsl.g:6373:3: (enumLiteral_2= 'contract' )
                    // InternalCqrsDsl.g:6374:4: enumLiteral_2= 'contract'
                    {
                    enumLiteral_2=(Token)match(input,109,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getContractEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getLawfulBasisAccess().getContractEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6381:3: (enumLiteral_3= 'legal_obligation' )
                    {
                    // InternalCqrsDsl.g:6381:3: (enumLiteral_3= 'legal_obligation' )
                    // InternalCqrsDsl.g:6382:4: enumLiteral_3= 'legal_obligation'
                    {
                    enumLiteral_3=(Token)match(input,110,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getLegal_obligationEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getLawfulBasisAccess().getLegal_obligationEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:6389:3: (enumLiteral_4= 'vital_interests' )
                    {
                    // InternalCqrsDsl.g:6389:3: (enumLiteral_4= 'vital_interests' )
                    // InternalCqrsDsl.g:6390:4: enumLiteral_4= 'vital_interests'
                    {
                    enumLiteral_4=(Token)match(input,111,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getVital_interestsEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getLawfulBasisAccess().getVital_interestsEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:6397:3: (enumLiteral_5= 'public_task' )
                    {
                    // InternalCqrsDsl.g:6397:3: (enumLiteral_5= 'public_task' )
                    // InternalCqrsDsl.g:6398:4: enumLiteral_5= 'public_task'
                    {
                    enumLiteral_5=(Token)match(input,112,FOLLOW_2); 

                    				current = grammarAccess.getLawfulBasisAccess().getPublic_taskEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getLawfulBasisAccess().getPublic_taskEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:6405:3: (enumLiteral_6= 'legitimate_interests' )
                    {
                    // InternalCqrsDsl.g:6405:3: (enumLiteral_6= 'legitimate_interests' )
                    // InternalCqrsDsl.g:6406:4: enumLiteral_6= 'legitimate_interests'
                    {
                    enumLiteral_6=(Token)match(input,113,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6416:1: ruleSpecialCategory returns [Enumerator current=null] : ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) ) ;
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
            // InternalCqrsDsl.g:6422:2: ( ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) ) )
            // InternalCqrsDsl.g:6423:2: ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) )
            {
            // InternalCqrsDsl.g:6423:2: ( (enumLiteral_0= 'health' ) | (enumLiteral_1= 'genetic' ) | (enumLiteral_2= 'biometric' ) | (enumLiteral_3= 'racial' ) | (enumLiteral_4= 'political' ) | (enumLiteral_5= 'religious' ) | (enumLiteral_6= 'philosophical' ) | (enumLiteral_7= 'trade_union' ) | (enumLiteral_8= 'sex_life' ) | (enumLiteral_9= 'sexual_orientation' ) )
            int alt182=10;
            switch ( input.LA(1) ) {
            case 114:
                {
                alt182=1;
                }
                break;
            case 115:
                {
                alt182=2;
                }
                break;
            case 116:
                {
                alt182=3;
                }
                break;
            case 117:
                {
                alt182=4;
                }
                break;
            case 118:
                {
                alt182=5;
                }
                break;
            case 119:
                {
                alt182=6;
                }
                break;
            case 120:
                {
                alt182=7;
                }
                break;
            case 121:
                {
                alt182=8;
                }
                break;
            case 122:
                {
                alt182=9;
                }
                break;
            case 123:
                {
                alt182=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 182, 0, input);

                throw nvae;
            }

            switch (alt182) {
                case 1 :
                    // InternalCqrsDsl.g:6424:3: (enumLiteral_0= 'health' )
                    {
                    // InternalCqrsDsl.g:6424:3: (enumLiteral_0= 'health' )
                    // InternalCqrsDsl.g:6425:4: enumLiteral_0= 'health'
                    {
                    enumLiteral_0=(Token)match(input,114,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getHealthEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getSpecialCategoryAccess().getHealthEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6432:3: (enumLiteral_1= 'genetic' )
                    {
                    // InternalCqrsDsl.g:6432:3: (enumLiteral_1= 'genetic' )
                    // InternalCqrsDsl.g:6433:4: enumLiteral_1= 'genetic'
                    {
                    enumLiteral_1=(Token)match(input,115,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getGeneticEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getSpecialCategoryAccess().getGeneticEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6440:3: (enumLiteral_2= 'biometric' )
                    {
                    // InternalCqrsDsl.g:6440:3: (enumLiteral_2= 'biometric' )
                    // InternalCqrsDsl.g:6441:4: enumLiteral_2= 'biometric'
                    {
                    enumLiteral_2=(Token)match(input,116,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getBiometricEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getSpecialCategoryAccess().getBiometricEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6448:3: (enumLiteral_3= 'racial' )
                    {
                    // InternalCqrsDsl.g:6448:3: (enumLiteral_3= 'racial' )
                    // InternalCqrsDsl.g:6449:4: enumLiteral_3= 'racial'
                    {
                    enumLiteral_3=(Token)match(input,117,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getRacialEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getSpecialCategoryAccess().getRacialEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:6456:3: (enumLiteral_4= 'political' )
                    {
                    // InternalCqrsDsl.g:6456:3: (enumLiteral_4= 'political' )
                    // InternalCqrsDsl.g:6457:4: enumLiteral_4= 'political'
                    {
                    enumLiteral_4=(Token)match(input,118,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getPoliticalEnumLiteralDeclaration_4().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_4, grammarAccess.getSpecialCategoryAccess().getPoliticalEnumLiteralDeclaration_4());
                    			

                    }


                    }
                    break;
                case 6 :
                    // InternalCqrsDsl.g:6464:3: (enumLiteral_5= 'religious' )
                    {
                    // InternalCqrsDsl.g:6464:3: (enumLiteral_5= 'religious' )
                    // InternalCqrsDsl.g:6465:4: enumLiteral_5= 'religious'
                    {
                    enumLiteral_5=(Token)match(input,119,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getReligiousEnumLiteralDeclaration_5().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_5, grammarAccess.getSpecialCategoryAccess().getReligiousEnumLiteralDeclaration_5());
                    			

                    }


                    }
                    break;
                case 7 :
                    // InternalCqrsDsl.g:6472:3: (enumLiteral_6= 'philosophical' )
                    {
                    // InternalCqrsDsl.g:6472:3: (enumLiteral_6= 'philosophical' )
                    // InternalCqrsDsl.g:6473:4: enumLiteral_6= 'philosophical'
                    {
                    enumLiteral_6=(Token)match(input,120,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getPhilosophicalEnumLiteralDeclaration_6().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_6, grammarAccess.getSpecialCategoryAccess().getPhilosophicalEnumLiteralDeclaration_6());
                    			

                    }


                    }
                    break;
                case 8 :
                    // InternalCqrsDsl.g:6480:3: (enumLiteral_7= 'trade_union' )
                    {
                    // InternalCqrsDsl.g:6480:3: (enumLiteral_7= 'trade_union' )
                    // InternalCqrsDsl.g:6481:4: enumLiteral_7= 'trade_union'
                    {
                    enumLiteral_7=(Token)match(input,121,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getTrade_unionEnumLiteralDeclaration_7().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_7, grammarAccess.getSpecialCategoryAccess().getTrade_unionEnumLiteralDeclaration_7());
                    			

                    }


                    }
                    break;
                case 9 :
                    // InternalCqrsDsl.g:6488:3: (enumLiteral_8= 'sex_life' )
                    {
                    // InternalCqrsDsl.g:6488:3: (enumLiteral_8= 'sex_life' )
                    // InternalCqrsDsl.g:6489:4: enumLiteral_8= 'sex_life'
                    {
                    enumLiteral_8=(Token)match(input,122,FOLLOW_2); 

                    				current = grammarAccess.getSpecialCategoryAccess().getSex_lifeEnumLiteralDeclaration_8().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_8, grammarAccess.getSpecialCategoryAccess().getSex_lifeEnumLiteralDeclaration_8());
                    			

                    }


                    }
                    break;
                case 10 :
                    // InternalCqrsDsl.g:6496:3: (enumLiteral_9= 'sexual_orientation' )
                    {
                    // InternalCqrsDsl.g:6496:3: (enumLiteral_9= 'sexual_orientation' )
                    // InternalCqrsDsl.g:6497:4: enumLiteral_9= 'sexual_orientation'
                    {
                    enumLiteral_9=(Token)match(input,123,FOLLOW_2); 

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
    // InternalCqrsDsl.g:6507:1: ruleErasureStrategy returns [Enumerator current=null] : ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) ) ;
    public final Enumerator ruleErasureStrategy() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;
        Token enumLiteral_4=null;


        	enterRule();

        try {
            // InternalCqrsDsl.g:6513:2: ( ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) ) )
            // InternalCqrsDsl.g:6514:2: ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) )
            {
            // InternalCqrsDsl.g:6514:2: ( (enumLiteral_0= 'delete' ) | (enumLiteral_1= 'anonymize' ) | (enumLiteral_2= 'pseudonymize' ) | (enumLiteral_3= 'archive' ) | (enumLiteral_4= 'review' ) )
            int alt183=5;
            switch ( input.LA(1) ) {
            case 124:
                {
                alt183=1;
                }
                break;
            case 125:
                {
                alt183=2;
                }
                break;
            case 126:
                {
                alt183=3;
                }
                break;
            case 127:
                {
                alt183=4;
                }
                break;
            case 128:
                {
                alt183=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 183, 0, input);

                throw nvae;
            }

            switch (alt183) {
                case 1 :
                    // InternalCqrsDsl.g:6515:3: (enumLiteral_0= 'delete' )
                    {
                    // InternalCqrsDsl.g:6515:3: (enumLiteral_0= 'delete' )
                    // InternalCqrsDsl.g:6516:4: enumLiteral_0= 'delete'
                    {
                    enumLiteral_0=(Token)match(input,124,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getDeleteEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getErasureStrategyAccess().getDeleteEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:6523:3: (enumLiteral_1= 'anonymize' )
                    {
                    // InternalCqrsDsl.g:6523:3: (enumLiteral_1= 'anonymize' )
                    // InternalCqrsDsl.g:6524:4: enumLiteral_1= 'anonymize'
                    {
                    enumLiteral_1=(Token)match(input,125,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getAnonymizeEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getErasureStrategyAccess().getAnonymizeEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCqrsDsl.g:6531:3: (enumLiteral_2= 'pseudonymize' )
                    {
                    // InternalCqrsDsl.g:6531:3: (enumLiteral_2= 'pseudonymize' )
                    // InternalCqrsDsl.g:6532:4: enumLiteral_2= 'pseudonymize'
                    {
                    enumLiteral_2=(Token)match(input,126,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getPseudonymizeEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getErasureStrategyAccess().getPseudonymizeEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCqrsDsl.g:6539:3: (enumLiteral_3= 'archive' )
                    {
                    // InternalCqrsDsl.g:6539:3: (enumLiteral_3= 'archive' )
                    // InternalCqrsDsl.g:6540:4: enumLiteral_3= 'archive'
                    {
                    enumLiteral_3=(Token)match(input,127,FOLLOW_2); 

                    				current = grammarAccess.getErasureStrategyAccess().getArchiveEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getErasureStrategyAccess().getArchiveEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;
                case 5 :
                    // InternalCqrsDsl.g:6547:3: (enumLiteral_4= 'review' )
                    {
                    // InternalCqrsDsl.g:6547:3: (enumLiteral_4= 'review' )
                    // InternalCqrsDsl.g:6548:4: enumLiteral_4= 'review'
                    {
                    enumLiteral_4=(Token)match(input,128,FOLLOW_2); 

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


    protected DFA5 dfa5 = new DFA5(this);
    protected DFA6 dfa6 = new DFA6(this);
    protected DFA108 dfa108 = new DFA108(this);
    protected DFA119 dfa119 = new DFA119(this);
    static final String dfa_1s = "\6\uffff";
    static final String dfa_2s = "\1\uffff\1\3\2\uffff\1\3\1\uffff";
    static final String dfa_3s = "\1\5\1\4\1\5\1\uffff\1\4\1\uffff";
    static final String dfa_4s = "\1\5\1\125\1\126\1\uffff\1\125\1\uffff";
    static final String dfa_5s = "\3\uffff\1\1\1\uffff\1\2";
    static final String dfa_6s = "\6\uffff}>";
    static final String[] dfa_7s = {
            "\1\1",
            "\1\3\12\uffff\1\3\1\uffff\2\3\6\uffff\1\3\11\uffff\1\3\2\uffff\1\3\2\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\2\3\4\uffff\1\3\1\uffff\1\3\2\uffff\1\3\20\uffff\3\3\2\uffff\1\3\2\uffff\2\3\1\2",
            "\1\4\120\uffff\1\5",
            "",
            "\1\3\12\uffff\1\3\1\uffff\2\3\6\uffff\1\3\11\uffff\1\3\2\uffff\1\3\2\uffff\1\3\1\uffff\1\3\1\uffff\1\3\1\uffff\2\3\4\uffff\1\3\1\uffff\1\3\2\uffff\1\3\20\uffff\3\3\2\uffff\1\3\2\uffff\2\3\1\2",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "279:5: (lv_importedNamespace_1_1= ruleFQN | lv_importedNamespace_1_2= ruleFQNWithWildcard )";
        }
    }
    static final String dfa_8s = "\47\uffff";
    static final String dfa_9s = "\1\4\1\22\3\uffff\1\5\7\uffff\1\53\1\5\1\6\1\53\7\34\1\6\1\53\1\6\11\34\1\6\2\34";
    static final String dfa_10s = "\2\124\3\uffff\1\5\7\uffff\1\125\1\5\1\131\1\125\4\64\2\125\1\64\1\131\1\113\1\11\4\64\2\125\3\64\1\11\2\64";
    static final String dfa_11s = "\2\uffff\1\1\1\2\1\3\1\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\12\32\uffff";
    static final String dfa_12s = "\47\uffff}>";
    static final String[] dfa_13s = {
            "\1\1\15\uffff\1\4\6\uffff\1\14\11\uffff\1\2\2\uffff\1\6\2\uffff\1\3\1\uffff\1\4\1\uffff\1\4\1\uffff\2\4\4\uffff\1\7\1\uffff\1\4\2\uffff\1\4\20\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13",
            "\1\4\6\uffff\1\14\11\uffff\1\2\2\uffff\1\6\2\uffff\1\3\1\uffff\1\4\1\uffff\1\4\1\uffff\2\4\4\uffff\1\7\1\uffff\1\4\2\uffff\1\4\20\uffff\1\5\1\4\1\10\2\uffff\1\11\2\uffff\1\12\1\13",
            "",
            "",
            "",
            "\1\15",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\4\7\uffff\1\17\1\uffff\1\7\25\uffff\1\5\11\uffff\1\16",
            "\1\20",
            "\1\25\1\27\1\24\1\26\115\uffff\1\22\1\23\1\21",
            "\1\4\7\uffff\1\17\1\uffff\1\7\25\uffff\1\5\11\uffff\1\16",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31\40\uffff\1\32",
            "\1\30\27\uffff\1\31\40\uffff\1\32",
            "\1\30\27\uffff\1\31",
            "\1\37\1\41\1\36\1\40\115\uffff\1\34\1\35\1\33",
            "\1\4\11\uffff\1\7\25\uffff\1\5",
            "\1\42\2\uffff\1\43",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31\40\uffff\1\44",
            "\1\30\27\uffff\1\31\40\uffff\1\44",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31",
            "\1\45\2\uffff\1\46",
            "\1\30\27\uffff\1\31",
            "\1\30\27\uffff\1\31"
    };

    static final short[] dfa_8 = DFA.unpackEncodedString(dfa_8s);
    static final char[] dfa_9 = DFA.unpackEncodedStringToUnsignedChars(dfa_9s);
    static final char[] dfa_10 = DFA.unpackEncodedStringToUnsignedChars(dfa_10s);
    static final short[] dfa_11 = DFA.unpackEncodedString(dfa_11s);
    static final short[] dfa_12 = DFA.unpackEncodedString(dfa_12s);
    static final short[][] dfa_13 = unpackEncodedStringArray(dfa_13s);

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = dfa_8;
            this.eof = dfa_8;
            this.min = dfa_9;
            this.max = dfa_10;
            this.accept = dfa_11;
            this.special = dfa_12;
            this.transition = dfa_13;
        }
        public String getDescription() {
            return "332:2: (this_Constraint_0= ruleConstraint | this_Annotation_1= ruleAnnotation | this_Type_2= ruleType | this_Exception_3= ruleException | this_Event_4= ruleEvent | this_Command_5= ruleCommand | this_CommandHandler_6= ruleCommandHandler | this_Projection_7= ruleProjection | this_View_8= ruleView | this_DataProtection_9= ruleDataProtection )";
        }
    }
    static final String dfa_14s = "\7\uffff";
    static final String dfa_15s = "\2\4\1\5\2\uffff\1\5\1\4";
    static final String dfa_16s = "\1\114\1\125\1\114\2\uffff\1\5\1\125";
    static final String dfa_17s = "\3\uffff\1\2\1\1\2\uffff";
    static final String dfa_18s = "\7\uffff}>";
    static final String[] dfa_19s = {
            "\1\2\1\1\11\uffff\1\3\45\uffff\1\3\21\uffff\1\4\3\uffff\2\3",
            "\1\3\1\4\11\uffff\1\3\45\uffff\1\3\17\uffff\1\4\5\uffff\2\3\10\uffff\1\5",
            "\1\4\57\uffff\1\3\21\uffff\1\4\3\uffff\2\3",
            "",
            "",
            "\1\6",
            "\1\3\1\4\11\uffff\1\3\45\uffff\1\3\17\uffff\1\4\5\uffff\2\3\10\uffff\1\5"
    };

    static final short[] dfa_14 = DFA.unpackEncodedString(dfa_14s);
    static final char[] dfa_15 = DFA.unpackEncodedStringToUnsignedChars(dfa_15s);
    static final char[] dfa_16 = DFA.unpackEncodedStringToUnsignedChars(dfa_16s);
    static final short[] dfa_17 = DFA.unpackEncodedString(dfa_17s);
    static final short[] dfa_18 = DFA.unpackEncodedString(dfa_18s);
    static final short[][] dfa_19 = unpackEncodedStringArray(dfa_19s);

    class DFA108 extends DFA {

        public DFA108(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 108;
            this.eot = dfa_14;
            this.eof = dfa_14;
            this.min = dfa_15;
            this.max = dfa_16;
            this.accept = dfa_17;
            this.special = dfa_18;
            this.transition = dfa_19;
        }
        public String getDescription() {
            return "()* loopback of 3705:3: ( (lv_parameters_10_0= ruleParameter ) )*";
        }
    }
    static final String[] dfa_20s = {
            "\1\2\1\1\11\uffff\1\3\45\uffff\1\3\7\uffff\1\3\11\uffff\1\4\3\uffff\2\3",
            "\1\3\1\4\11\uffff\1\3\45\uffff\1\3\7\uffff\1\3\7\uffff\1\4\5\uffff\2\3\10\uffff\1\5",
            "\1\4\57\uffff\1\3\7\uffff\1\3\11\uffff\1\4\3\uffff\2\3",
            "",
            "",
            "\1\6",
            "\1\3\1\4\11\uffff\1\3\45\uffff\1\3\7\uffff\1\3\7\uffff\1\4\5\uffff\2\3\10\uffff\1\5"
    };
    static final short[][] dfa_20 = unpackEncodedStringArray(dfa_20s);

    class DFA119 extends DFA {

        public DFA119(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 119;
            this.eot = dfa_14;
            this.eof = dfa_14;
            this.min = dfa_15;
            this.max = dfa_16;
            this.accept = dfa_17;
            this.special = dfa_18;
            this.transition = dfa_20;
        }
        public String getDescription() {
            return "()* loopback of 4006:3: ( (lv_parameters_12_0= ruleParameter ) )*";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000018000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x04A1AA4802068010L,0x0000000000193800L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x04A1AA4802048010L,0x0000000000193800L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000080020L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000000000L,0x00000003FC000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000400010L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000000000L,0x0000007000000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000000000000L,0x000000F000000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000000000L,0x0000000C00000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000200010L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000004000010L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000000000000L,0x0000070000000000L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x00000001E8008010L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x0000000000000000L,0x0FFC000000000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x00000001F0008010L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x00000001C0008010L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x0000000180008010L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000000L,0x0003F80000000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000100008010L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x0000000200008000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000000000000000L,0xF000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000005000004000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000006000004000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000008000008030L,0x0000000000000080L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_51 = new BitSet(new long[]{0x0000000001000010L});
    public static final BitSet FOLLOW_52 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_53 = new BitSet(new long[]{0x0000000000008030L,0x0000000000000080L});
    public static final BitSet FOLLOW_54 = new BitSet(new long[]{0x0000040000004000L});
    public static final BitSet FOLLOW_55 = new BitSet(new long[]{0x0000008000000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_56 = new BitSet(new long[]{0x0000080000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_57 = new BitSet(new long[]{0x0000100400004000L,0x0000000000000100L});
    public static final BitSet FOLLOW_58 = new BitSet(new long[]{0x0000000400004000L,0x0000000000000100L});
    public static final BitSet FOLLOW_59 = new BitSet(new long[]{0x0000000400004000L});
    public static final BitSet FOLLOW_60 = new BitSet(new long[]{0x4800000000008030L,0x000000000000009FL});
    public static final BitSet FOLLOW_61 = new BitSet(new long[]{0x4800000000008030L,0x0000000000000080L});
    public static final BitSet FOLLOW_62 = new BitSet(new long[]{0x4800000000008010L});
    public static final BitSet FOLLOW_63 = new BitSet(new long[]{0x4000000000008010L});
    public static final BitSet FOLLOW_64 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_65 = new BitSet(new long[]{0x0000500400004000L,0x0000000000000100L});
    public static final BitSet FOLLOW_66 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_67 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_68 = new BitSet(new long[]{0x0002000000000030L,0x000000000000009FL});
    public static final BitSet FOLLOW_69 = new BitSet(new long[]{0x0002000000000030L,0x0000000000000080L});
    public static final BitSet FOLLOW_70 = new BitSet(new long[]{0x0004000000000030L});
    public static final BitSet FOLLOW_71 = new BitSet(new long[]{0x0004000000008030L});
    public static final BitSet FOLLOW_72 = new BitSet(new long[]{0x0004000000000020L});
    public static final BitSet FOLLOW_73 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_74 = new BitSet(new long[]{0x00000000000003C0L,0x0000000003800000L});
    public static final BitSet FOLLOW_75 = new BitSet(new long[]{0x0010000010000000L});
    public static final BitSet FOLLOW_76 = new BitSet(new long[]{0x0020000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_77 = new BitSet(new long[]{0x0040000000004000L});
    public static final BitSet FOLLOW_78 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_79 = new BitSet(new long[]{0x0300000400004000L,0x0000000000000100L});
    public static final BitSet FOLLOW_80 = new BitSet(new long[]{0x0200000400004000L,0x0000000000000100L});
    public static final BitSet FOLLOW_81 = new BitSet(new long[]{0x4CA1AA4802048030L,0x000000000019389FL});
    public static final BitSet FOLLOW_82 = new BitSet(new long[]{0x4CA1AA4802048030L,0x0000000000193880L});
    public static final BitSet FOLLOW_83 = new BitSet(new long[]{0x4CA1AA4802048010L,0x0000000000193800L});
    public static final BitSet FOLLOW_84 = new BitSet(new long[]{0x44A1AA4802048010L,0x0000000000193800L});
    public static final BitSet FOLLOW_85 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_86 = new BitSet(new long[]{0x0100000400004000L,0x0000000000000100L});
    public static final BitSet FOLLOW_87 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_88 = new BitSet(new long[]{0x1000000000004000L,0x0000000000000600L});
    public static final BitSet FOLLOW_89 = new BitSet(new long[]{0x1000000000004000L,0x0000000000000400L});
    public static final BitSet FOLLOW_90 = new BitSet(new long[]{0x1000000000004000L});
    public static final BitSet FOLLOW_91 = new BitSet(new long[]{0x0000000010004000L});
    public static final BitSet FOLLOW_92 = new BitSet(new long[]{0x04A1A80000048030L,0x0000000000001880L});
    public static final BitSet FOLLOW_93 = new BitSet(new long[]{0x04A1A80000048010L,0x0000000000001800L});
    public static final BitSet FOLLOW_94 = new BitSet(new long[]{0x0020000000008010L,0x0000000000000800L});
    public static final BitSet FOLLOW_95 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_96 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_97 = new BitSet(new long[]{0x9000000000004000L,0x0000000000000600L});
    public static final BitSet FOLLOW_98 = new BitSet(new long[]{0x24A1A80000048030L,0x0000000000001880L});
    public static final BitSet FOLLOW_99 = new BitSet(new long[]{0x24A1A80000048010L,0x0000000000001800L});
    public static final BitSet FOLLOW_100 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001EL});
    public static final BitSet FOLLOW_101 = new BitSet(new long[]{0x0000000000000002L,0x000000000000001CL});
    public static final BitSet FOLLOW_102 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000018L});
    public static final BitSet FOLLOW_103 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
    public static final BitSet FOLLOW_104 = new BitSet(new long[]{0x00000000000003C2L,0x0000000003800000L});
    public static final BitSet FOLLOW_105 = new BitSet(new long[]{0x0000000000000020L,0x0000000000000020L});
    public static final BitSet FOLLOW_106 = new BitSet(new long[]{0x0000000010000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_107 = new BitSet(new long[]{0x0000000000000020L,0x0000000000000080L});
    public static final BitSet FOLLOW_108 = new BitSet(new long[]{0x0000000400004002L,0x0000000000000100L});
    public static final BitSet FOLLOW_109 = new BitSet(new long[]{0x0000000400004002L});
    public static final BitSet FOLLOW_110 = new BitSet(new long[]{0x0000000000004002L,0x0000000000000600L});
    public static final BitSet FOLLOW_111 = new BitSet(new long[]{0x0000000000004002L,0x0000000000000400L});
    public static final BitSet FOLLOW_112 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_113 = new BitSet(new long[]{0x0000000000008000L,0x000000000000001FL});
    public static final BitSet FOLLOW_114 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_115 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_116 = new BitSet(new long[]{0x0000000000004000L,0x000000000000C000L});
    public static final BitSet FOLLOW_117 = new BitSet(new long[]{0x0000000000004000L,0x0000000000008000L});
    public static final BitSet FOLLOW_118 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_119 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_120 = new BitSet(new long[]{0x0000000010000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_121 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_122 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_123 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_125 = new BitSet(new long[]{0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_127 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_128 = new BitSet(new long[]{0x0000000000000240L});

}