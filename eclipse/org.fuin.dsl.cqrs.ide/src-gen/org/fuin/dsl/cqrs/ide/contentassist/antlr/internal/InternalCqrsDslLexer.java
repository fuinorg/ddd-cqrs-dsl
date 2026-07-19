package org.fuin.dsl.cqrs.ide.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCqrsDslLexer extends Lexer {
    public static final int T__144=144;
    public static final int T__143=143;
    public static final int RULE_HEX=4;
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
    public static final int RULE_ID=7;
    public static final int T__131=131;
    public static final int T__130=130;
    public static final int RULE_INT=5;
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
    public static final int RULE_DOC=8;
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
    public static final int RULE_DECIMAL=6;
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
    public static final int RULE_STRING=9;
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

    public InternalCqrsDslLexer() {;} 
    public InternalCqrsDslLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalCqrsDslLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "InternalCqrsDsl.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:11:7: ( 'true' )
            // InternalCqrsDsl.g:11:9: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:12:7: ( 'false' )
            // InternalCqrsDsl.g:12:9: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:13:7: ( 'millis' )
            // InternalCqrsDsl.g:13:9: 'millis'
            {
            match("millis"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:14:7: ( 'seconds' )
            // InternalCqrsDsl.g:14:9: 'seconds'
            {
            match("seconds"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:15:7: ( 'minutes' )
            // InternalCqrsDsl.g:15:9: 'minutes'
            {
            match("minutes"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:16:7: ( 'hours' )
            // InternalCqrsDsl.g:16:9: 'hours'
            {
            match("hours"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:17:7: ( 'days' )
            // InternalCqrsDsl.g:17:9: 'days'
            {
            match("days"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:18:7: ( 'weeks' )
            // InternalCqrsDsl.g:18:9: 'weeks'
            {
            match("weeks"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:19:7: ( 'months' )
            // InternalCqrsDsl.g:19:9: 'months'
            {
            match("months"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20:7: ( 'years' )
            // InternalCqrsDsl.g:20:9: 'years'
            {
            match("years"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:21:7: ( 'weak' )
            // InternalCqrsDsl.g:21:9: 'weak'
            {
            match("weak"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:22:7: ( 'strong' )
            // InternalCqrsDsl.g:22:9: 'strong'
            {
            match("strong"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:23:7: ( 'never' )
            // InternalCqrsDsl.g:23:9: 'never'
            {
            match("never"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:24:7: ( 'manually' )
            // InternalCqrsDsl.g:24:9: 'manually'
            {
            match("manually"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:25:7: ( 'automatic' )
            // InternalCqrsDsl.g:25:9: 'automatic'
            {
            match("automatic"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:26:7: ( 'workflow' )
            // InternalCqrsDsl.g:26:9: 'workflow'
            {
            match("workflow"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:27:7: ( 'none' )
            // InternalCqrsDsl.g:27:9: 'none'
            {
            match("none"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:28:7: ( 'personal' )
            // InternalCqrsDsl.g:28:9: 'personal'
            {
            match("personal"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:29:7: ( 'sensitive' )
            // InternalCqrsDsl.g:29:9: 'sensitive'
            {
            match("sensitive"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:30:7: ( 'consent' )
            // InternalCqrsDsl.g:30:9: 'consent'
            {
            match("consent"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:31:7: ( 'explicit_consent' )
            // InternalCqrsDsl.g:31:9: 'explicit_consent'
            {
            match("explicit_consent"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:32:7: ( 'contract' )
            // InternalCqrsDsl.g:32:9: 'contract'
            {
            match("contract"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:33:7: ( 'legal_obligation' )
            // InternalCqrsDsl.g:33:9: 'legal_obligation'
            {
            match("legal_obligation"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:34:7: ( 'vital_interests' )
            // InternalCqrsDsl.g:34:9: 'vital_interests'
            {
            match("vital_interests"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:35:7: ( 'public_task' )
            // InternalCqrsDsl.g:35:9: 'public_task'
            {
            match("public_task"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:36:7: ( 'legitimate_interests' )
            // InternalCqrsDsl.g:36:9: 'legitimate_interests'
            {
            match("legitimate_interests"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:37:7: ( 'health' )
            // InternalCqrsDsl.g:37:9: 'health'
            {
            match("health"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:38:7: ( 'genetic' )
            // InternalCqrsDsl.g:38:9: 'genetic'
            {
            match("genetic"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:39:7: ( 'biometric' )
            // InternalCqrsDsl.g:39:9: 'biometric'
            {
            match("biometric"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:40:7: ( 'racial' )
            // InternalCqrsDsl.g:40:9: 'racial'
            {
            match("racial"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:41:7: ( 'political' )
            // InternalCqrsDsl.g:41:9: 'political'
            {
            match("political"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:42:7: ( 'religious' )
            // InternalCqrsDsl.g:42:9: 'religious'
            {
            match("religious"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:43:7: ( 'philosophical' )
            // InternalCqrsDsl.g:43:9: 'philosophical'
            {
            match("philosophical"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:44:7: ( 'trade_union' )
            // InternalCqrsDsl.g:44:9: 'trade_union'
            {
            match("trade_union"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:45:7: ( 'sex_life' )
            // InternalCqrsDsl.g:45:9: 'sex_life'
            {
            match("sex_life"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:46:7: ( 'sexual_orientation' )
            // InternalCqrsDsl.g:46:9: 'sexual_orientation'
            {
            match("sexual_orientation"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:47:7: ( 'delete' )
            // InternalCqrsDsl.g:47:9: 'delete'
            {
            match("delete"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:48:7: ( 'anonymize' )
            // InternalCqrsDsl.g:48:9: 'anonymize'
            {
            match("anonymize"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:49:7: ( 'pseudonymize' )
            // InternalCqrsDsl.g:49:9: 'pseudonymize'
            {
            match("pseudonymize"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:50:7: ( 'archive' )
            // InternalCqrsDsl.g:50:9: 'archive'
            {
            match("archive"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:51:7: ( 'review' )
            // InternalCqrsDsl.g:51:9: 'review'
            {
            match("review"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:52:7: ( 'project' )
            // InternalCqrsDsl.g:52:9: 'project'
            {
            match("project"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:53:7: ( '{' )
            // InternalCqrsDsl.g:53:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:54:7: ( '}' )
            // InternalCqrsDsl.g:54:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:55:7: ( 'context' )
            // InternalCqrsDsl.g:55:9: 'context'
            {
            match("context"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:56:7: ( 'namespace' )
            // InternalCqrsDsl.g:56:9: 'namespace'
            {
            match("namespace"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:57:7: ( 'import' )
            // InternalCqrsDsl.g:57:9: 'import'
            {
            match("import"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:58:7: ( 'hint' )
            // InternalCqrsDsl.g:58:9: 'hint'
            {
            match("hint"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:59:7: ( 'type' )
            // InternalCqrsDsl.g:59:9: 'type'
            {
            match("type"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:60:7: ( 'generics' )
            // InternalCqrsDsl.g:60:9: 'generics'
            {
            match("generics"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:61:7: ( 'acceptable' )
            // InternalCqrsDsl.g:61:9: 'acceptable'
            {
            match("acceptable"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:62:7: ( 'detection' )
            // InternalCqrsDsl.g:62:9: 'detection'
            {
            match("detection"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:63:7: ( 'resolution' )
            // InternalCqrsDsl.g:63:9: 'resolution'
            {
            match("resolution"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:64:7: ( 'consistency' )
            // InternalCqrsDsl.g:64:9: 'consistency'
            {
            match("consistency"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:65:7: ( 'data-protection' )
            // InternalCqrsDsl.g:65:9: 'data-protection'
            {
            match("data-protection"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:66:7: ( 'protection' )
            // InternalCqrsDsl.g:66:9: 'protection'
            {
            match("protection"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:67:7: ( 'category' )
            // InternalCqrsDsl.g:67:9: 'category'
            {
            match("category"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:68:7: ( ',' )
            // InternalCqrsDsl.g:68:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:69:7: ( 'subject' )
            // InternalCqrsDsl.g:69:9: 'subject'
            {
            match("subject"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:70:7: ( 'purpose' )
            // InternalCqrsDsl.g:70:9: 'purpose'
            {
            match("purpose"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:71:7: ( 'lawful-basis' )
            // InternalCqrsDsl.g:71:9: 'lawful-basis'
            {
            match("lawful-basis"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:72:7: ( 'retention' )
            // InternalCqrsDsl.g:72:9: 'retention'
            {
            match("retention"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:73:7: ( 'then' )
            // InternalCqrsDsl.g:73:9: 'then'
            {
            match("then"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "T__76"
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:74:7: ( 'protected-by' )
            // InternalCqrsDsl.g:74:9: 'protected-by'
            {
            match("protected-by"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__76"

    // $ANTLR start "T__77"
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:75:7: ( 'constraint' )
            // InternalCqrsDsl.g:75:9: 'constraint'
            {
            match("constraint"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__77"

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:76:7: ( 'input' )
            // InternalCqrsDsl.g:76:9: 'input'
            {
            match("input"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:77:7: ( '|' )
            // InternalCqrsDsl.g:77:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:78:7: ( 'exception' )
            // InternalCqrsDsl.g:78:9: 'exception'
            {
            match("exception"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:79:7: ( 'message' )
            // InternalCqrsDsl.g:79:9: 'message'
            {
            match("message"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:80:7: ( 'business-rule' )
            // InternalCqrsDsl.g:80:9: 'business-rule'
            {
            match("business-rule"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:81:7: ( 'annotation' )
            // InternalCqrsDsl.g:81:9: 'annotation'
            {
            match("annotation"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:82:7: ( 'cid' )
            // InternalCqrsDsl.g:82:9: 'cid'
            {
            match("cid"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:83:7: ( 'value-object' )
            // InternalCqrsDsl.g:83:9: 'value-object'
            {
            match("value-object"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:84:7: ( 'base' )
            // InternalCqrsDsl.g:84:9: 'base'
            {
            match("base"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:85:7: ( 'entity-id' )
            // InternalCqrsDsl.g:85:9: 'entity-id'
            {
            match("entity-id"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:86:7: ( 'identifies' )
            // InternalCqrsDsl.g:86:9: 'identifies'
            {
            match("identifies"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "T__89"
    public final void mT__89() throws RecognitionException {
        try {
            int _type = T__89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:87:7: ( 'aggregate-id' )
            // InternalCqrsDsl.g:87:9: 'aggregate-id'
            {
            match("aggregate-id"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__89"

    // $ANTLR start "T__90"
    public final void mT__90() throws RecognitionException {
        try {
            int _type = T__90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:88:7: ( 'enum' )
            // InternalCqrsDsl.g:88:9: 'enum'
            {
            match("enum"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__90"

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:89:7: ( 'instances' )
            // InternalCqrsDsl.g:89:9: 'instances'
            {
            match("instances"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:90:7: ( '(' )
            // InternalCqrsDsl.g:90:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:91:7: ( ')' )
            // InternalCqrsDsl.g:91:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "T__94"
    public final void mT__94() throws RecognitionException {
        try {
            int _type = T__94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:92:7: ( 'event' )
            // InternalCqrsDsl.g:92:9: 'event'
            {
            match("event"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__94"

    // $ANTLR start "T__95"
    public final void mT__95() throws RecognitionException {
        try {
            int _type = T__95;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:93:7: ( 'copies-attributes-of' )
            // InternalCqrsDsl.g:93:9: 'copies-attributes-of'
            {
            match("copies-attributes-of"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__95"

    // $ANTLR start "T__96"
    public final void mT__96() throws RecognitionException {
        try {
            int _type = T__96;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:94:7: ( 'entity' )
            // InternalCqrsDsl.g:94:9: 'entity'
            {
            match("entity"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__96"

    // $ANTLR start "T__97"
    public final void mT__97() throws RecognitionException {
        try {
            int _type = T__97;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:95:7: ( 'identifier' )
            // InternalCqrsDsl.g:95:9: 'identifier'
            {
            match("identifier"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__97"

    // $ANTLR start "T__98"
    public final void mT__98() throws RecognitionException {
        try {
            int _type = T__98;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:96:7: ( 'root' )
            // InternalCqrsDsl.g:96:9: 'root'
            {
            match("root"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__98"

    // $ANTLR start "T__99"
    public final void mT__99() throws RecognitionException {
        try {
            int _type = T__99;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:97:7: ( 'aggregate' )
            // InternalCqrsDsl.g:97:9: 'aggregate'
            {
            match("aggregate"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__99"

    // $ANTLR start "T__100"
    public final void mT__100() throws RecognitionException {
        try {
            int _type = T__100;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:98:8: ( 'constructor' )
            // InternalCqrsDsl.g:98:10: 'constructor'
            {
            match("constructor"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__100"

    // $ANTLR start "T__101"
    public final void mT__101() throws RecognitionException {
        try {
            int _type = T__101;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:99:8: ( 'fires' )
            // InternalCqrsDsl.g:99:10: 'fires'
            {
            match("fires"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__101"

    // $ANTLR start "T__102"
    public final void mT__102() throws RecognitionException {
        try {
            int _type = T__102;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:100:8: ( 'returns' )
            // InternalCqrsDsl.g:100:10: 'returns'
            {
            match("returns"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__102"

    // $ANTLR start "T__103"
    public final void mT__103() throws RecognitionException {
        try {
            int _type = T__103;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:101:8: ( 'method' )
            // InternalCqrsDsl.g:101:10: 'method'
            {
            match("method"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__103"

    // $ANTLR start "T__104"
    public final void mT__104() throws RecognitionException {
        try {
            int _type = T__104;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:102:8: ( 'ref' )
            // InternalCqrsDsl.g:102:10: 'ref'
            {
            match("ref"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__104"

    // $ANTLR start "T__105"
    public final void mT__105() throws RecognitionException {
        try {
            int _type = T__105;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:103:8: ( 'slabel' )
            // InternalCqrsDsl.g:103:10: 'slabel'
            {
            match("slabel"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__105"

    // $ANTLR start "T__106"
    public final void mT__106() throws RecognitionException {
        try {
            int _type = T__106;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:104:8: ( 'label' )
            // InternalCqrsDsl.g:104:10: 'label'
            {
            match("label"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__106"

    // $ANTLR start "T__107"
    public final void mT__107() throws RecognitionException {
        try {
            int _type = T__107;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:105:8: ( 'tooltip' )
            // InternalCqrsDsl.g:105:10: 'tooltip'
            {
            match("tooltip"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__107"

    // $ANTLR start "T__108"
    public final void mT__108() throws RecognitionException {
        try {
            int _type = T__108;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:106:8: ( 'prompt' )
            // InternalCqrsDsl.g:106:10: 'prompt'
            {
            match("prompt"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__108"

    // $ANTLR start "T__109"
    public final void mT__109() throws RecognitionException {
        try {
            int _type = T__109;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:107:8: ( 'examples' )
            // InternalCqrsDsl.g:107:10: 'examples'
            {
            match("examples"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__109"

    // $ANTLR start "T__110"
    public final void mT__110() throws RecognitionException {
        try {
            int _type = T__110;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:108:8: ( '<' )
            // InternalCqrsDsl.g:108:10: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__110"

    // $ANTLR start "T__111"
    public final void mT__111() throws RecognitionException {
        try {
            int _type = T__111;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:109:8: ( '>' )
            // InternalCqrsDsl.g:109:10: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__111"

    // $ANTLR start "T__112"
    public final void mT__112() throws RecognitionException {
        try {
            int _type = T__112;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:110:8: ( 'invariants' )
            // InternalCqrsDsl.g:110:10: 'invariants'
            {
            match("invariants"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__112"

    // $ANTLR start "T__113"
    public final void mT__113() throws RecognitionException {
        try {
            int _type = T__113;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:111:8: ( 'preconditions' )
            // InternalCqrsDsl.g:111:10: 'preconditions'
            {
            match("preconditions"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__113"

    // $ANTLR start "T__114"
    public final void mT__114() throws RecognitionException {
        try {
            int _type = T__114;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:112:8: ( 'business-rules' )
            // InternalCqrsDsl.g:112:10: 'business-rules'
            {
            match("business-rules"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__114"

    // $ANTLR start "T__115"
    public final void mT__115() throws RecognitionException {
        try {
            int _type = T__115;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:113:8: ( '@' )
            // InternalCqrsDsl.g:113:10: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__115"

    // $ANTLR start "T__116"
    public final void mT__116() throws RecognitionException {
        try {
            int _type = T__116;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:114:8: ( 'service' )
            // InternalCqrsDsl.g:114:10: 'service'
            {
            match("service"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__116"

    // $ANTLR start "T__117"
    public final void mT__117() throws RecognitionException {
        try {
            int _type = T__117;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:115:8: ( 'command' )
            // InternalCqrsDsl.g:115:10: 'command'
            {
            match("command"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__117"

    // $ANTLR start "T__118"
    public final void mT__118() throws RecognitionException {
        try {
            int _type = T__118;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:116:8: ( 'target' )
            // InternalCqrsDsl.g:116:10: 'target'
            {
            match("target"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__118"

    // $ANTLR start "T__119"
    public final void mT__119() throws RecognitionException {
        try {
            int _type = T__119;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:117:8: ( 'sla' )
            // InternalCqrsDsl.g:117:10: 'sla'
            {
            match("sla"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__119"

    // $ANTLR start "T__120"
    public final void mT__120() throws RecognitionException {
        try {
            int _type = T__120;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:118:8: ( 'command-handler' )
            // InternalCqrsDsl.g:118:10: 'command-handler'
            {
            match("command-handler"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__120"

    // $ANTLR start "T__121"
    public final void mT__121() throws RecognitionException {
        try {
            int _type = T__121;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:119:8: ( 'handles' )
            // InternalCqrsDsl.g:119:10: 'handles'
            {
            match("handles"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__121"

    // $ANTLR start "T__122"
    public final void mT__122() throws RecognitionException {
        try {
            int _type = T__122;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:120:8: ( 'uses' )
            // InternalCqrsDsl.g:120:10: 'uses'
            {
            match("uses"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__122"

    // $ANTLR start "T__123"
    public final void mT__123() throws RecognitionException {
        try {
            int _type = T__123;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:121:8: ( 'projection' )
            // InternalCqrsDsl.g:121:10: 'projection'
            {
            match("projection"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__123"

    // $ANTLR start "T__124"
    public final void mT__124() throws RecognitionException {
        try {
            int _type = T__124;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:122:8: ( 'view' )
            // InternalCqrsDsl.g:122:10: 'view'
            {
            match("view"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__124"

    // $ANTLR start "T__125"
    public final void mT__125() throws RecognitionException {
        try {
            int _type = T__125;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:123:8: ( 'rest-path' )
            // InternalCqrsDsl.g:123:10: 'rest-path'
            {
            match("rest-path"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__125"

    // $ANTLR start "T__126"
    public final void mT__126() throws RecognitionException {
        try {
            int _type = T__126;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:124:8: ( 'cron-schedule' )
            // InternalCqrsDsl.g:124:10: 'cron-schedule'
            {
            match("cron-schedule"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__126"

    // $ANTLR start "T__127"
    public final void mT__127() throws RecognitionException {
        try {
            int _type = T__127;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:125:8: ( 'process-manager' )
            // InternalCqrsDsl.g:125:10: 'process-manager'
            {
            match("process-manager"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__127"

    // $ANTLR start "T__128"
    public final void mT__128() throws RecognitionException {
        try {
            int _type = T__128;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:126:8: ( 'correlation-id' )
            // InternalCqrsDsl.g:126:10: 'correlation-id'
            {
            match("correlation-id"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__128"

    // $ANTLR start "T__129"
    public final void mT__129() throws RecognitionException {
        try {
            int _type = T__129;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:127:8: ( 'process-states' )
            // InternalCqrsDsl.g:127:10: 'process-states'
            {
            match("process-states"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__129"

    // $ANTLR start "T__130"
    public final void mT__130() throws RecognitionException {
        try {
            int _type = T__130;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:128:8: ( 'reacts-to' )
            // InternalCqrsDsl.g:128:10: 'reacts-to'
            {
            match("reacts-to"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__130"

    // $ANTLR start "T__131"
    public final void mT__131() throws RecognitionException {
        try {
            int _type = T__131;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:129:8: ( 'in-state' )
            // InternalCqrsDsl.g:129:10: 'in-state'
            {
            match("in-state"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__131"

    // $ANTLR start "T__132"
    public final void mT__132() throws RecognitionException {
        try {
            int _type = T__132;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:130:8: ( 'correlate-by' )
            // InternalCqrsDsl.g:130:10: 'correlate-by'
            {
            match("correlate-by"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__132"

    // $ANTLR start "T__133"
    public final void mT__133() throws RecognitionException {
        try {
            int _type = T__133;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:131:8: ( 'issues-commands' )
            // InternalCqrsDsl.g:131:10: 'issues-commands'
            {
            match("issues-commands"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__133"

    // $ANTLR start "T__134"
    public final void mT__134() throws RecognitionException {
        try {
            int _type = T__134;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:132:8: ( 'transition-to' )
            // InternalCqrsDsl.g:132:10: 'transition-to'
            {
            match("transition-to"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__134"

    // $ANTLR start "T__135"
    public final void mT__135() throws RecognitionException {
        try {
            int _type = T__135;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:133:8: ( 'arm-timeout' )
            // InternalCqrsDsl.g:133:10: 'arm-timeout'
            {
            match("arm-timeout"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__135"

    // $ANTLR start "T__136"
    public final void mT__136() throws RecognitionException {
        try {
            int _type = T__136;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:134:8: ( ':' )
            // InternalCqrsDsl.g:134:10: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__136"

    // $ANTLR start "T__137"
    public final void mT__137() throws RecognitionException {
        try {
            int _type = T__137;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:135:8: ( '[' )
            // InternalCqrsDsl.g:135:10: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__137"

    // $ANTLR start "T__138"
    public final void mT__138() throws RecognitionException {
        try {
            int _type = T__138;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:136:8: ( ']' )
            // InternalCqrsDsl.g:136:10: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__138"

    // $ANTLR start "T__139"
    public final void mT__139() throws RecognitionException {
        try {
            int _type = T__139;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:137:8: ( 'null' )
            // InternalCqrsDsl.g:137:10: 'null'
            {
            match("null"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__139"

    // $ANTLR start "T__140"
    public final void mT__140() throws RecognitionException {
        try {
            int _type = T__140;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:138:8: ( '.' )
            // InternalCqrsDsl.g:138:10: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__140"

    // $ANTLR start "T__141"
    public final void mT__141() throws RecognitionException {
        try {
            int _type = T__141;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:139:8: ( '*' )
            // InternalCqrsDsl.g:139:10: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__141"

    // $ANTLR start "T__142"
    public final void mT__142() throws RecognitionException {
        try {
            int _type = T__142;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:140:8: ( 'element' )
            // InternalCqrsDsl.g:140:10: 'element'
            {
            match("element"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__142"

    // $ANTLR start "T__143"
    public final void mT__143() throws RecognitionException {
        try {
            int _type = T__143;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:141:8: ( 'deprecated' )
            // InternalCqrsDsl.g:141:10: 'deprecated'
            {
            match("deprecated"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__143"

    // $ANTLR start "T__144"
    public final void mT__144() throws RecognitionException {
        try {
            int _type = T__144;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:142:8: ( 'optional' )
            // InternalCqrsDsl.g:142:10: 'optional'
            {
            match("optional"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__144"

    // $ANTLR start "T__145"
    public final void mT__145() throws RecognitionException {
        try {
            int _type = T__145;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:143:8: ( 'cancel-timeout' )
            // InternalCqrsDsl.g:143:10: 'cancel-timeout'
            {
            match("cancel-timeout"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__145"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20809:9: ( ( '^' )? ( 'A' .. 'Z' | 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // InternalCqrsDsl.g:20809:11: ( '^' )? ( 'A' .. 'Z' | 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // InternalCqrsDsl.g:20809:11: ( '^' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='^') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // InternalCqrsDsl.g:20809:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalCqrsDsl.g:20809:36: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalCqrsDsl.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20811:13: ( ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // InternalCqrsDsl.g:20811:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // InternalCqrsDsl.g:20811:15: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\"') ) {
                alt5=1;
            }
            else if ( (LA5_0=='\'') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalCqrsDsl.g:20811:16: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // InternalCqrsDsl.g:20811:20: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop3:
                    do {
                        int alt3=3;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0=='\\') ) {
                            alt3=1;
                        }
                        else if ( ((LA3_0>='\u0000' && LA3_0<='!')||(LA3_0>='#' && LA3_0<='[')||(LA3_0>=']' && LA3_0<='\uFFFF')) ) {
                            alt3=2;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:20811:21: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCqrsDsl.g:20811:66: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:20811:86: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // InternalCqrsDsl.g:20811:91: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop4:
                    do {
                        int alt4=3;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0=='\\') ) {
                            alt4=1;
                        }
                        else if ( ((LA4_0>='\u0000' && LA4_0<='&')||(LA4_0>='(' && LA4_0<='[')||(LA4_0>=']' && LA4_0<='\uFFFF')) ) {
                            alt4=2;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // InternalCqrsDsl.g:20811:92: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalCqrsDsl.g:20811:137: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_HEX"
    public final void mRULE_HEX() throws RecognitionException {
        try {
            int _type = RULE_HEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20813:10: ( ( '0x' | '0X' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '_' )+ ( '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) ) )? )
            // InternalCqrsDsl.g:20813:12: ( '0x' | '0X' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '_' )+ ( '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) ) )?
            {
            // InternalCqrsDsl.g:20813:12: ( '0x' | '0X' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='0') ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1=='x') ) {
                    alt6=1;
                }
                else if ( (LA6_1=='X') ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // InternalCqrsDsl.g:20813:13: '0x'
                    {
                    match("0x"); 


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:20813:18: '0X'
                    {
                    match("0X"); 


                    }
                    break;

            }

            // InternalCqrsDsl.g:20813:24: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' | '_' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='F')||LA7_0=='_'||(LA7_0>='a' && LA7_0<='f')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalCqrsDsl.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='f') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            // InternalCqrsDsl.g:20813:58: ( '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='#') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalCqrsDsl.g:20813:59: '#' ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) )
                    {
                    match('#'); 
                    // InternalCqrsDsl.g:20813:63: ( ( 'b' | 'B' ) ( 'i' | 'I' ) | ( 'l' | 'L' ) )
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='B'||LA8_0=='b') ) {
                        alt8=1;
                    }
                    else if ( (LA8_0=='L'||LA8_0=='l') ) {
                        alt8=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 0, input);

                        throw nvae;
                    }
                    switch (alt8) {
                        case 1 :
                            // InternalCqrsDsl.g:20813:64: ( 'b' | 'B' ) ( 'i' | 'I' )
                            {
                            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}

                            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;
                        case 2 :
                            // InternalCqrsDsl.g:20813:84: ( 'l' | 'L' )
                            {
                            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;

                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_HEX"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20815:10: ( '0' .. '9' ( '0' .. '9' | '_' )* )
            // InternalCqrsDsl.g:20815:12: '0' .. '9' ( '0' .. '9' | '_' )*
            {
            matchRange('0','9'); 
            // InternalCqrsDsl.g:20815:21: ( '0' .. '9' | '_' )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>='0' && LA10_0<='9')||LA10_0=='_') ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalCqrsDsl.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||input.LA(1)=='_' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_DECIMAL"
    public final void mRULE_DECIMAL() throws RecognitionException {
        try {
            int _type = RULE_DECIMAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20817:14: ( RULE_INT ( ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT )? ( ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' ) | ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' ) )? )
            // InternalCqrsDsl.g:20817:16: RULE_INT ( ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT )? ( ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' ) | ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' ) )?
            {
            mRULE_INT(); 
            // InternalCqrsDsl.g:20817:25: ( ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='E'||LA12_0=='e') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalCqrsDsl.g:20817:26: ( 'e' | 'E' ) ( '+' | '-' )? RULE_INT
                    {
                    if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    // InternalCqrsDsl.g:20817:36: ( '+' | '-' )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0=='+'||LA11_0=='-') ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // InternalCqrsDsl.g:
                            {
                            if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;

                    }

                    mRULE_INT(); 

                    }
                    break;

            }

            // InternalCqrsDsl.g:20817:58: ( ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' ) | ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' ) )?
            int alt13=3;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='B'||LA13_0=='b') ) {
                alt13=1;
            }
            else if ( (LA13_0=='D'||LA13_0=='F'||LA13_0=='L'||LA13_0=='d'||LA13_0=='f'||LA13_0=='l') ) {
                alt13=2;
            }
            switch (alt13) {
                case 1 :
                    // InternalCqrsDsl.g:20817:59: ( 'b' | 'B' ) ( 'i' | 'I' | 'd' | 'D' )
                    {
                    if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    if ( input.LA(1)=='D'||input.LA(1)=='I'||input.LA(1)=='d'||input.LA(1)=='i' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;
                case 2 :
                    // InternalCqrsDsl.g:20817:87: ( 'l' | 'L' | 'd' | 'D' | 'f' | 'F' )
                    {
                    if ( input.LA(1)=='D'||input.LA(1)=='F'||input.LA(1)=='L'||input.LA(1)=='d'||input.LA(1)=='f'||input.LA(1)=='l' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DECIMAL"

    // $ANTLR start "RULE_DOC"
    public final void mRULE_DOC() throws RecognitionException {
        try {
            int _type = RULE_DOC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20819:10: ( '/**' ( options {greedy=false; } : . )* '*/' )
            // InternalCqrsDsl.g:20819:12: '/**' ( options {greedy=false; } : . )* '*/'
            {
            match("/**"); 

            // InternalCqrsDsl.g:20819:18: ( options {greedy=false; } : . )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0=='*') ) {
                    int LA14_1 = input.LA(2);

                    if ( (LA14_1=='/') ) {
                        alt14=2;
                    }
                    else if ( ((LA14_1>='\u0000' && LA14_1<='.')||(LA14_1>='0' && LA14_1<='\uFFFF')) ) {
                        alt14=1;
                    }


                }
                else if ( ((LA14_0>='\u0000' && LA14_0<=')')||(LA14_0>='+' && LA14_0<='\uFFFF')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalCqrsDsl.g:20819:46: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DOC"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20821:17: ( '/*' ~ ( '*' ) ( options {greedy=false; } : . )* '*/' )
            // InternalCqrsDsl.g:20821:19: '/*' ~ ( '*' ) ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            if ( (input.LA(1)>='\u0000' && input.LA(1)<=')')||(input.LA(1)>='+' && input.LA(1)<='\uFFFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalCqrsDsl.g:20821:31: ( options {greedy=false; } : . )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0=='*') ) {
                    int LA15_1 = input.LA(2);

                    if ( (LA15_1=='/') ) {
                        alt15=2;
                    }
                    else if ( ((LA15_1>='\u0000' && LA15_1<='.')||(LA15_1>='0' && LA15_1<='\uFFFF')) ) {
                        alt15=1;
                    }


                }
                else if ( ((LA15_0>='\u0000' && LA15_0<=')')||(LA15_0>='+' && LA15_0<='\uFFFF')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCqrsDsl.g:20821:59: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20823:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalCqrsDsl.g:20823:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalCqrsDsl.g:20823:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>='\u0000' && LA16_0<='\t')||(LA16_0>='\u000B' && LA16_0<='\f')||(LA16_0>='\u000E' && LA16_0<='\uFFFF')) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalCqrsDsl.g:20823:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            // InternalCqrsDsl.g:20823:40: ( ( '\\r' )? '\\n' )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='\n'||LA18_0=='\r') ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // InternalCqrsDsl.g:20823:41: ( '\\r' )? '\\n'
                    {
                    // InternalCqrsDsl.g:20823:41: ( '\\r' )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0=='\r') ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // InternalCqrsDsl.g:20823:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalCqrsDsl.g:20825:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalCqrsDsl.g:20825:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalCqrsDsl.g:20825:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt19=0;
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>='\t' && LA19_0<='\n')||LA19_0=='\r'||LA19_0==' ') ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalCqrsDsl.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt19 >= 1 ) break loop19;
                        EarlyExitException eee =
                            new EarlyExitException(19, input);
                        throw eee;
                }
                cnt19++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    public void mTokens() throws RecognitionException {
        // InternalCqrsDsl.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | T__120 | T__121 | T__122 | T__123 | T__124 | T__125 | T__126 | T__127 | T__128 | T__129 | T__130 | T__131 | T__132 | T__133 | T__134 | T__135 | T__136 | T__137 | T__138 | T__139 | T__140 | T__141 | T__142 | T__143 | T__144 | T__145 | RULE_ID | RULE_STRING | RULE_HEX | RULE_INT | RULE_DECIMAL | RULE_DOC | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS )
        int alt20=142;
        alt20 = dfa20.predict(input);
        switch (alt20) {
            case 1 :
                // InternalCqrsDsl.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // InternalCqrsDsl.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // InternalCqrsDsl.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // InternalCqrsDsl.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // InternalCqrsDsl.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // InternalCqrsDsl.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // InternalCqrsDsl.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // InternalCqrsDsl.g:1:52: T__20
                {
                mT__20(); 

                }
                break;
            case 9 :
                // InternalCqrsDsl.g:1:58: T__21
                {
                mT__21(); 

                }
                break;
            case 10 :
                // InternalCqrsDsl.g:1:64: T__22
                {
                mT__22(); 

                }
                break;
            case 11 :
                // InternalCqrsDsl.g:1:70: T__23
                {
                mT__23(); 

                }
                break;
            case 12 :
                // InternalCqrsDsl.g:1:76: T__24
                {
                mT__24(); 

                }
                break;
            case 13 :
                // InternalCqrsDsl.g:1:82: T__25
                {
                mT__25(); 

                }
                break;
            case 14 :
                // InternalCqrsDsl.g:1:88: T__26
                {
                mT__26(); 

                }
                break;
            case 15 :
                // InternalCqrsDsl.g:1:94: T__27
                {
                mT__27(); 

                }
                break;
            case 16 :
                // InternalCqrsDsl.g:1:100: T__28
                {
                mT__28(); 

                }
                break;
            case 17 :
                // InternalCqrsDsl.g:1:106: T__29
                {
                mT__29(); 

                }
                break;
            case 18 :
                // InternalCqrsDsl.g:1:112: T__30
                {
                mT__30(); 

                }
                break;
            case 19 :
                // InternalCqrsDsl.g:1:118: T__31
                {
                mT__31(); 

                }
                break;
            case 20 :
                // InternalCqrsDsl.g:1:124: T__32
                {
                mT__32(); 

                }
                break;
            case 21 :
                // InternalCqrsDsl.g:1:130: T__33
                {
                mT__33(); 

                }
                break;
            case 22 :
                // InternalCqrsDsl.g:1:136: T__34
                {
                mT__34(); 

                }
                break;
            case 23 :
                // InternalCqrsDsl.g:1:142: T__35
                {
                mT__35(); 

                }
                break;
            case 24 :
                // InternalCqrsDsl.g:1:148: T__36
                {
                mT__36(); 

                }
                break;
            case 25 :
                // InternalCqrsDsl.g:1:154: T__37
                {
                mT__37(); 

                }
                break;
            case 26 :
                // InternalCqrsDsl.g:1:160: T__38
                {
                mT__38(); 

                }
                break;
            case 27 :
                // InternalCqrsDsl.g:1:166: T__39
                {
                mT__39(); 

                }
                break;
            case 28 :
                // InternalCqrsDsl.g:1:172: T__40
                {
                mT__40(); 

                }
                break;
            case 29 :
                // InternalCqrsDsl.g:1:178: T__41
                {
                mT__41(); 

                }
                break;
            case 30 :
                // InternalCqrsDsl.g:1:184: T__42
                {
                mT__42(); 

                }
                break;
            case 31 :
                // InternalCqrsDsl.g:1:190: T__43
                {
                mT__43(); 

                }
                break;
            case 32 :
                // InternalCqrsDsl.g:1:196: T__44
                {
                mT__44(); 

                }
                break;
            case 33 :
                // InternalCqrsDsl.g:1:202: T__45
                {
                mT__45(); 

                }
                break;
            case 34 :
                // InternalCqrsDsl.g:1:208: T__46
                {
                mT__46(); 

                }
                break;
            case 35 :
                // InternalCqrsDsl.g:1:214: T__47
                {
                mT__47(); 

                }
                break;
            case 36 :
                // InternalCqrsDsl.g:1:220: T__48
                {
                mT__48(); 

                }
                break;
            case 37 :
                // InternalCqrsDsl.g:1:226: T__49
                {
                mT__49(); 

                }
                break;
            case 38 :
                // InternalCqrsDsl.g:1:232: T__50
                {
                mT__50(); 

                }
                break;
            case 39 :
                // InternalCqrsDsl.g:1:238: T__51
                {
                mT__51(); 

                }
                break;
            case 40 :
                // InternalCqrsDsl.g:1:244: T__52
                {
                mT__52(); 

                }
                break;
            case 41 :
                // InternalCqrsDsl.g:1:250: T__53
                {
                mT__53(); 

                }
                break;
            case 42 :
                // InternalCqrsDsl.g:1:256: T__54
                {
                mT__54(); 

                }
                break;
            case 43 :
                // InternalCqrsDsl.g:1:262: T__55
                {
                mT__55(); 

                }
                break;
            case 44 :
                // InternalCqrsDsl.g:1:268: T__56
                {
                mT__56(); 

                }
                break;
            case 45 :
                // InternalCqrsDsl.g:1:274: T__57
                {
                mT__57(); 

                }
                break;
            case 46 :
                // InternalCqrsDsl.g:1:280: T__58
                {
                mT__58(); 

                }
                break;
            case 47 :
                // InternalCqrsDsl.g:1:286: T__59
                {
                mT__59(); 

                }
                break;
            case 48 :
                // InternalCqrsDsl.g:1:292: T__60
                {
                mT__60(); 

                }
                break;
            case 49 :
                // InternalCqrsDsl.g:1:298: T__61
                {
                mT__61(); 

                }
                break;
            case 50 :
                // InternalCqrsDsl.g:1:304: T__62
                {
                mT__62(); 

                }
                break;
            case 51 :
                // InternalCqrsDsl.g:1:310: T__63
                {
                mT__63(); 

                }
                break;
            case 52 :
                // InternalCqrsDsl.g:1:316: T__64
                {
                mT__64(); 

                }
                break;
            case 53 :
                // InternalCqrsDsl.g:1:322: T__65
                {
                mT__65(); 

                }
                break;
            case 54 :
                // InternalCqrsDsl.g:1:328: T__66
                {
                mT__66(); 

                }
                break;
            case 55 :
                // InternalCqrsDsl.g:1:334: T__67
                {
                mT__67(); 

                }
                break;
            case 56 :
                // InternalCqrsDsl.g:1:340: T__68
                {
                mT__68(); 

                }
                break;
            case 57 :
                // InternalCqrsDsl.g:1:346: T__69
                {
                mT__69(); 

                }
                break;
            case 58 :
                // InternalCqrsDsl.g:1:352: T__70
                {
                mT__70(); 

                }
                break;
            case 59 :
                // InternalCqrsDsl.g:1:358: T__71
                {
                mT__71(); 

                }
                break;
            case 60 :
                // InternalCqrsDsl.g:1:364: T__72
                {
                mT__72(); 

                }
                break;
            case 61 :
                // InternalCqrsDsl.g:1:370: T__73
                {
                mT__73(); 

                }
                break;
            case 62 :
                // InternalCqrsDsl.g:1:376: T__74
                {
                mT__74(); 

                }
                break;
            case 63 :
                // InternalCqrsDsl.g:1:382: T__75
                {
                mT__75(); 

                }
                break;
            case 64 :
                // InternalCqrsDsl.g:1:388: T__76
                {
                mT__76(); 

                }
                break;
            case 65 :
                // InternalCqrsDsl.g:1:394: T__77
                {
                mT__77(); 

                }
                break;
            case 66 :
                // InternalCqrsDsl.g:1:400: T__78
                {
                mT__78(); 

                }
                break;
            case 67 :
                // InternalCqrsDsl.g:1:406: T__79
                {
                mT__79(); 

                }
                break;
            case 68 :
                // InternalCqrsDsl.g:1:412: T__80
                {
                mT__80(); 

                }
                break;
            case 69 :
                // InternalCqrsDsl.g:1:418: T__81
                {
                mT__81(); 

                }
                break;
            case 70 :
                // InternalCqrsDsl.g:1:424: T__82
                {
                mT__82(); 

                }
                break;
            case 71 :
                // InternalCqrsDsl.g:1:430: T__83
                {
                mT__83(); 

                }
                break;
            case 72 :
                // InternalCqrsDsl.g:1:436: T__84
                {
                mT__84(); 

                }
                break;
            case 73 :
                // InternalCqrsDsl.g:1:442: T__85
                {
                mT__85(); 

                }
                break;
            case 74 :
                // InternalCqrsDsl.g:1:448: T__86
                {
                mT__86(); 

                }
                break;
            case 75 :
                // InternalCqrsDsl.g:1:454: T__87
                {
                mT__87(); 

                }
                break;
            case 76 :
                // InternalCqrsDsl.g:1:460: T__88
                {
                mT__88(); 

                }
                break;
            case 77 :
                // InternalCqrsDsl.g:1:466: T__89
                {
                mT__89(); 

                }
                break;
            case 78 :
                // InternalCqrsDsl.g:1:472: T__90
                {
                mT__90(); 

                }
                break;
            case 79 :
                // InternalCqrsDsl.g:1:478: T__91
                {
                mT__91(); 

                }
                break;
            case 80 :
                // InternalCqrsDsl.g:1:484: T__92
                {
                mT__92(); 

                }
                break;
            case 81 :
                // InternalCqrsDsl.g:1:490: T__93
                {
                mT__93(); 

                }
                break;
            case 82 :
                // InternalCqrsDsl.g:1:496: T__94
                {
                mT__94(); 

                }
                break;
            case 83 :
                // InternalCqrsDsl.g:1:502: T__95
                {
                mT__95(); 

                }
                break;
            case 84 :
                // InternalCqrsDsl.g:1:508: T__96
                {
                mT__96(); 

                }
                break;
            case 85 :
                // InternalCqrsDsl.g:1:514: T__97
                {
                mT__97(); 

                }
                break;
            case 86 :
                // InternalCqrsDsl.g:1:520: T__98
                {
                mT__98(); 

                }
                break;
            case 87 :
                // InternalCqrsDsl.g:1:526: T__99
                {
                mT__99(); 

                }
                break;
            case 88 :
                // InternalCqrsDsl.g:1:532: T__100
                {
                mT__100(); 

                }
                break;
            case 89 :
                // InternalCqrsDsl.g:1:539: T__101
                {
                mT__101(); 

                }
                break;
            case 90 :
                // InternalCqrsDsl.g:1:546: T__102
                {
                mT__102(); 

                }
                break;
            case 91 :
                // InternalCqrsDsl.g:1:553: T__103
                {
                mT__103(); 

                }
                break;
            case 92 :
                // InternalCqrsDsl.g:1:560: T__104
                {
                mT__104(); 

                }
                break;
            case 93 :
                // InternalCqrsDsl.g:1:567: T__105
                {
                mT__105(); 

                }
                break;
            case 94 :
                // InternalCqrsDsl.g:1:574: T__106
                {
                mT__106(); 

                }
                break;
            case 95 :
                // InternalCqrsDsl.g:1:581: T__107
                {
                mT__107(); 

                }
                break;
            case 96 :
                // InternalCqrsDsl.g:1:588: T__108
                {
                mT__108(); 

                }
                break;
            case 97 :
                // InternalCqrsDsl.g:1:595: T__109
                {
                mT__109(); 

                }
                break;
            case 98 :
                // InternalCqrsDsl.g:1:602: T__110
                {
                mT__110(); 

                }
                break;
            case 99 :
                // InternalCqrsDsl.g:1:609: T__111
                {
                mT__111(); 

                }
                break;
            case 100 :
                // InternalCqrsDsl.g:1:616: T__112
                {
                mT__112(); 

                }
                break;
            case 101 :
                // InternalCqrsDsl.g:1:623: T__113
                {
                mT__113(); 

                }
                break;
            case 102 :
                // InternalCqrsDsl.g:1:630: T__114
                {
                mT__114(); 

                }
                break;
            case 103 :
                // InternalCqrsDsl.g:1:637: T__115
                {
                mT__115(); 

                }
                break;
            case 104 :
                // InternalCqrsDsl.g:1:644: T__116
                {
                mT__116(); 

                }
                break;
            case 105 :
                // InternalCqrsDsl.g:1:651: T__117
                {
                mT__117(); 

                }
                break;
            case 106 :
                // InternalCqrsDsl.g:1:658: T__118
                {
                mT__118(); 

                }
                break;
            case 107 :
                // InternalCqrsDsl.g:1:665: T__119
                {
                mT__119(); 

                }
                break;
            case 108 :
                // InternalCqrsDsl.g:1:672: T__120
                {
                mT__120(); 

                }
                break;
            case 109 :
                // InternalCqrsDsl.g:1:679: T__121
                {
                mT__121(); 

                }
                break;
            case 110 :
                // InternalCqrsDsl.g:1:686: T__122
                {
                mT__122(); 

                }
                break;
            case 111 :
                // InternalCqrsDsl.g:1:693: T__123
                {
                mT__123(); 

                }
                break;
            case 112 :
                // InternalCqrsDsl.g:1:700: T__124
                {
                mT__124(); 

                }
                break;
            case 113 :
                // InternalCqrsDsl.g:1:707: T__125
                {
                mT__125(); 

                }
                break;
            case 114 :
                // InternalCqrsDsl.g:1:714: T__126
                {
                mT__126(); 

                }
                break;
            case 115 :
                // InternalCqrsDsl.g:1:721: T__127
                {
                mT__127(); 

                }
                break;
            case 116 :
                // InternalCqrsDsl.g:1:728: T__128
                {
                mT__128(); 

                }
                break;
            case 117 :
                // InternalCqrsDsl.g:1:735: T__129
                {
                mT__129(); 

                }
                break;
            case 118 :
                // InternalCqrsDsl.g:1:742: T__130
                {
                mT__130(); 

                }
                break;
            case 119 :
                // InternalCqrsDsl.g:1:749: T__131
                {
                mT__131(); 

                }
                break;
            case 120 :
                // InternalCqrsDsl.g:1:756: T__132
                {
                mT__132(); 

                }
                break;
            case 121 :
                // InternalCqrsDsl.g:1:763: T__133
                {
                mT__133(); 

                }
                break;
            case 122 :
                // InternalCqrsDsl.g:1:770: T__134
                {
                mT__134(); 

                }
                break;
            case 123 :
                // InternalCqrsDsl.g:1:777: T__135
                {
                mT__135(); 

                }
                break;
            case 124 :
                // InternalCqrsDsl.g:1:784: T__136
                {
                mT__136(); 

                }
                break;
            case 125 :
                // InternalCqrsDsl.g:1:791: T__137
                {
                mT__137(); 

                }
                break;
            case 126 :
                // InternalCqrsDsl.g:1:798: T__138
                {
                mT__138(); 

                }
                break;
            case 127 :
                // InternalCqrsDsl.g:1:805: T__139
                {
                mT__139(); 

                }
                break;
            case 128 :
                // InternalCqrsDsl.g:1:812: T__140
                {
                mT__140(); 

                }
                break;
            case 129 :
                // InternalCqrsDsl.g:1:819: T__141
                {
                mT__141(); 

                }
                break;
            case 130 :
                // InternalCqrsDsl.g:1:826: T__142
                {
                mT__142(); 

                }
                break;
            case 131 :
                // InternalCqrsDsl.g:1:833: T__143
                {
                mT__143(); 

                }
                break;
            case 132 :
                // InternalCqrsDsl.g:1:840: T__144
                {
                mT__144(); 

                }
                break;
            case 133 :
                // InternalCqrsDsl.g:1:847: T__145
                {
                mT__145(); 

                }
                break;
            case 134 :
                // InternalCqrsDsl.g:1:854: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 135 :
                // InternalCqrsDsl.g:1:862: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 136 :
                // InternalCqrsDsl.g:1:874: RULE_HEX
                {
                mRULE_HEX(); 

                }
                break;
            case 137 :
                // InternalCqrsDsl.g:1:883: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 138 :
                // InternalCqrsDsl.g:1:892: RULE_DECIMAL
                {
                mRULE_DECIMAL(); 

                }
                break;
            case 139 :
                // InternalCqrsDsl.g:1:905: RULE_DOC
                {
                mRULE_DOC(); 

                }
                break;
            case 140 :
                // InternalCqrsDsl.g:1:914: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 141 :
                // InternalCqrsDsl.g:1:930: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 142 :
                // InternalCqrsDsl.g:1:946: RULE_WS
                {
                mRULE_WS(); 

                }
                break;

        }

    }


    protected DFA20 dfa20 = new DFA20(this);
    static final String DFA20_eotS =
        "\1\uffff\22\44\2\uffff\1\44\7\uffff\1\44\5\uffff\1\44\2\uffff\2\154\2\uffff\100\44\1\uffff\1\154\4\uffff\24\44\1\u00e8\46\44\1\u0113\27\44\1\u012e\6\44\1\uffff\4\44\2\uffff\1\u0139\2\44\1\u013c\1\u013d\22\44\1\uffff\2\44\1\u0152\1\44\1\u0154\5\44\1\u015a\3\44\1\u015e\1\44\1\u0160\4\44\1\uffff\24\44\1\uffff\5\44\1\u0181\7\44\1\u0189\4\44\1\u018f\7\44\1\uffff\1\44\1\u0198\6\44\1\u019f\1\44\1\uffff\2\44\2\uffff\2\44\1\u01a5\1\u01a6\16\44\1\u01b5\1\44\1\uffff\1\44\2\uffff\3\44\1\u01bb\1\uffff\1\44\1\u01bd\1\u01be\1\uffff\1\44\1\uffff\33\44\1\uffff\4\44\1\uffff\1\u01df\4\44\1\u01e4\1\44\1\uffff\5\44\1\uffff\4\44\1\uffff\3\44\1\uffff\1\44\1\u01f3\4\44\1\uffff\4\44\1\u01fc\2\uffff\1\u01fd\1\44\1\u01ff\2\44\1\u0202\5\44\1\u0208\1\44\1\u020a\1\uffff\1\u020b\1\44\1\u020d\2\44\1\uffff\1\44\2\uffff\17\44\1\u0220\17\44\1\u0232\1\uffff\4\44\1\uffff\1\44\1\uffff\4\44\1\u023c\1\44\1\u023e\4\44\1\u0243\1\uffff\7\44\1\u024b\2\uffff\1\u024c\1\uffff\1\44\1\u024e\1\uffff\1\u024f\3\44\1\u0253\1\uffff\1\u0254\2\uffff\1\u0255\1\uffff\7\44\1\u025d\4\44\1\u0262\3\44\1\u0267\1\44\1\uffff\2\44\1\u026c\4\44\1\u0271\1\uffff\1\u0273\2\44\1\uffff\3\44\2\uffff\1\u0279\2\44\1\uffff\1\44\1\u027d\3\44\1\uffff\1\44\1\uffff\2\44\1\u0284\2\uffff\3\44\1\uffff\3\44\2\uffff\1\u028b\2\uffff\1\44\1\u028d\1\44\3\uffff\2\44\1\u0291\4\44\1\uffff\2\44\1\u0298\1\44\1\uffff\4\44\1\uffff\2\44\1\uffff\1\44\1\uffff\3\44\1\u02a6\3\uffff\1\44\1\u02a9\2\44\1\u02ac\1\uffff\3\44\1\uffff\1\u02b0\5\44\1\uffff\3\44\1\u02b9\2\44\1\uffff\1\u02bc\1\uffff\1\44\1\u02be\1\44\1\uffff\1\u02c0\1\u02c1\1\u02c2\2\44\1\u02c6\1\uffff\1\44\1\u02c8\5\44\2\uffff\4\44\1\uffff\2\44\1\uffff\1\44\1\u02d5\1\uffff\3\44\1\uffff\1\u02d9\1\uffff\1\u02db\1\44\1\u02dd\1\u02de\2\44\1\uffff\2\44\1\uffff\1\44\1\uffff\1\u02e5\3\uffff\1\u02e6\1\u02e7\2\uffff\1\44\1\uffff\2\44\1\u02eb\1\u02ec\1\uffff\2\44\1\u02ef\2\44\1\uffff\1\44\1\uffff\3\44\3\uffff\1\u02f7\2\uffff\1\u02f8\1\u02f9\1\u02fa\1\u02fb\1\uffff\1\44\3\uffff\1\u02fd\2\44\2\uffff\1\44\1\u0301\1\uffff\1\u0302\5\44\6\uffff\1\44\1\uffff\1\44\1\u030b\1\44\3\uffff\4\44\1\uffff\1\44\1\u0313\1\uffff\1\u0314\4\44\1\u031a\1\44\2\uffff\4\44\2\uffff\4\44\1\u0324\1\44\1\u0326\1\u0327\1\44\1\uffff\1\44\2\uffff\1\44\1\u032b\1\44\1\uffff\1\44\1\u032e\1\uffff";
    static final String DFA20_eofS =
        "\u032f\uffff";
    static final String DFA20_minS =
        "\1\11\3\141\1\145\2\141\2\145\1\141\1\143\1\145\1\141\1\154\2\141\1\145\2\141\2\uffff\1\144\7\uffff\1\163\5\uffff\1\160\2\uffff\2\60\1\52\1\uffff\1\141\1\160\1\145\1\157\1\162\1\154\1\162\1\154\2\156\1\163\1\143\1\162\1\142\1\141\1\165\1\141\2\156\1\164\1\154\1\141\1\162\1\141\1\166\1\156\1\155\1\154\1\164\1\156\2\143\1\147\1\162\1\142\1\154\1\151\2\145\1\155\1\156\1\144\1\157\1\141\1\164\2\145\1\147\1\142\1\145\1\154\1\156\1\157\2\163\1\143\1\141\1\157\1\160\1\55\1\145\1\163\1\145\1\164\1\uffff\1\60\2\uffff\1\0\1\uffff\1\145\1\144\1\145\1\156\1\154\1\147\1\163\1\145\1\154\1\165\1\164\1\165\1\163\1\150\1\157\1\163\1\137\1\166\1\157\1\152\1\60\1\162\1\154\1\164\1\144\1\163\1\141\2\145\1\162\3\153\1\162\3\145\1\154\1\157\1\156\1\157\1\150\1\55\1\145\1\162\1\163\1\154\1\160\1\151\1\154\1\165\2\143\1\163\1\151\1\155\1\162\1\145\1\143\1\60\1\156\1\154\1\145\1\155\1\151\1\155\1\156\1\155\1\141\1\146\1\145\1\141\1\167\1\165\1\145\1\155\1\151\1\145\3\151\1\157\1\145\1\60\1\143\1\164\1\157\1\165\1\164\1\141\1\uffff\1\156\1\165\1\163\1\151\2\uffff\1\60\1\145\1\163\2\60\1\164\2\145\1\163\1\151\1\164\1\150\2\141\1\157\1\156\1\151\1\154\1\141\1\151\1\156\2\145\1\uffff\1\163\1\164\1\60\1\154\1\60\1\55\1\164\1\143\1\145\1\163\1\60\1\146\1\163\1\162\1\60\1\163\1\60\1\155\1\171\1\164\1\151\1\uffff\1\160\1\145\1\157\1\151\1\157\1\164\1\157\1\144\2\145\1\160\1\145\1\157\3\145\1\141\1\145\1\147\1\145\1\uffff\1\55\1\151\2\160\1\164\1\60\1\164\1\145\1\154\1\164\1\165\2\154\1\60\1\145\1\162\1\145\1\156\1\60\1\141\1\147\1\145\1\154\1\55\1\156\1\162\1\uffff\1\164\1\60\1\162\1\164\1\141\1\162\1\164\1\145\1\60\1\157\1\uffff\1\137\1\151\2\uffff\1\151\1\164\2\60\1\163\1\145\1\163\1\154\1\147\2\144\1\164\1\151\1\154\1\143\1\147\1\143\1\154\1\60\1\150\1\uffff\1\145\2\uffff\1\145\1\164\1\143\1\60\1\uffff\1\154\2\60\1\uffff\1\160\1\uffff\1\141\1\155\1\141\1\166\1\164\1\147\1\156\1\143\1\163\1\151\1\163\1\157\2\143\1\164\1\163\2\156\1\163\1\162\1\141\1\170\1\163\1\156\1\154\1\157\1\154\1\uffff\1\143\1\164\1\154\1\171\1\uffff\1\60\1\156\1\137\1\151\1\154\1\60\1\137\1\uffff\1\55\2\151\1\164\1\145\1\uffff\1\154\1\151\1\167\1\165\1\uffff\1\164\1\156\1\163\1\uffff\1\164\1\60\1\156\2\151\1\163\1\uffff\1\156\1\165\1\164\1\160\1\60\2\uffff\1\60\1\163\1\60\1\154\1\145\1\60\1\163\1\151\1\146\1\137\1\145\1\60\1\164\1\60\1\uffff\1\60\1\163\1\60\1\151\1\141\1\uffff\1\157\2\uffff\1\141\1\164\1\151\1\164\1\145\3\141\1\137\1\145\1\143\1\157\1\156\2\164\1\60\1\163\1\144\2\164\1\141\1\143\1\164\1\55\1\144\1\141\1\162\1\55\2\151\1\145\1\55\1\uffff\1\164\1\157\1\155\1\55\1\uffff\1\151\1\uffff\2\143\1\162\1\163\1\60\1\157\1\60\1\164\1\151\1\163\1\55\1\60\1\uffff\1\143\1\141\1\146\1\55\1\141\1\156\1\151\1\60\2\uffff\1\60\1\uffff\1\171\1\60\1\uffff\1\60\1\166\1\145\1\157\1\60\1\uffff\1\60\2\uffff\1\60\1\uffff\1\157\1\164\1\167\1\143\1\151\1\172\1\151\1\60\1\142\1\164\1\154\1\164\1\60\1\141\1\160\1\171\1\60\1\145\1\uffff\1\55\1\151\1\60\1\145\1\151\1\143\1\164\1\60\1\uffff\1\55\1\164\1\171\1\uffff\1\164\1\157\1\163\2\uffff\1\60\1\142\1\141\1\uffff\1\156\1\60\1\163\1\151\1\163\1\uffff\1\165\1\uffff\1\151\1\157\1\60\2\uffff\1\145\1\156\1\151\1\uffff\1\154\1\151\1\157\2\uffff\1\60\2\uffff\1\145\1\60\1\162\3\uffff\1\156\1\145\1\60\1\145\1\143\1\145\1\157\1\uffff\1\154\1\145\1\60\1\141\1\uffff\1\154\1\150\1\155\1\157\1\uffff\1\157\1\144\1\155\1\164\1\uffff\2\156\1\164\1\60\3\uffff\1\145\1\60\1\137\1\156\1\60\1\uffff\1\154\2\164\1\uffff\1\60\1\143\1\55\1\163\1\157\1\156\1\uffff\1\163\1\164\1\145\1\60\1\157\1\156\1\uffff\1\60\1\uffff\1\151\1\60\1\144\1\uffff\3\60\1\156\1\145\1\55\1\uffff\1\163\1\60\2\151\2\156\1\55\2\uffff\1\151\1\143\1\164\1\157\1\uffff\1\157\1\55\1\uffff\1\143\1\60\1\uffff\1\151\2\145\1\uffff\1\60\1\162\1\60\1\156\2\60\1\163\1\162\1\uffff\1\156\1\55\1\uffff\1\145\1\uffff\1\60\3\uffff\2\60\2\uffff\1\153\1\uffff\1\143\1\172\2\60\1\uffff\1\157\1\171\1\60\1\162\1\156\1\uffff\1\157\1\uffff\1\147\1\137\1\162\1\uffff\1\165\1\uffff\1\60\2\uffff\4\60\1\uffff\1\156\3\uffff\1\60\1\141\1\145\2\uffff\1\156\1\60\1\uffff\1\60\1\55\1\156\1\141\1\151\1\145\1\154\5\uffff\1\164\1\uffff\1\154\1\60\1\163\3\uffff\1\163\1\164\1\156\1\163\1\145\1\141\1\60\1\uffff\1\60\1\145\1\151\2\164\1\163\1\164\2\uffff\1\156\1\157\1\145\1\163\2\uffff\1\151\1\164\1\156\1\162\1\60\1\157\2\60\1\145\1\uffff\1\156\2\uffff\1\163\1\60\1\164\1\uffff\1\163\1\60\1\uffff";
    static final String DFA20_maxS =
        "\1\175\1\171\1\151\1\157\1\165\1\157\1\145\1\157\1\145\3\165\1\162\1\170\1\145\1\151\1\145\1\165\1\157\2\uffff\1\163\7\uffff\1\163\5\uffff\1\160\2\uffff\1\170\1\154\1\57\1\uffff\1\165\1\160\1\145\1\157\1\162\1\154\1\162\3\156\1\164\1\170\1\162\1\142\1\141\1\165\1\141\2\156\1\171\1\164\1\145\1\162\1\141\1\166\1\156\1\155\1\154\1\164\1\157\1\155\1\143\1\147\2\162\1\154\1\151\1\145\1\157\1\162\1\164\1\144\1\157\1\160\1\165\2\145\1\147\1\167\1\164\1\154\1\156\1\157\2\163\1\143\1\166\1\157\1\160\1\166\1\145\1\163\1\145\1\164\1\uffff\1\154\2\uffff\1\uffff\1\uffff\1\145\1\156\1\145\1\156\1\154\1\147\1\163\1\145\1\154\1\165\1\164\1\165\1\163\1\150\1\157\1\163\1\165\1\166\1\157\1\152\1\172\1\162\1\154\1\164\1\144\1\163\1\141\2\145\1\162\3\153\1\162\3\145\1\154\1\157\1\156\1\157\1\150\1\55\1\145\1\162\1\163\1\154\1\160\1\151\1\154\1\165\1\164\1\143\1\164\1\151\1\155\1\162\1\145\1\143\1\172\1\156\1\154\1\145\1\155\1\151\1\155\1\156\1\155\1\151\1\146\1\145\1\141\1\167\1\165\1\145\1\155\1\151\1\145\3\151\1\164\1\165\1\172\1\143\1\164\1\157\1\165\1\164\1\141\1\uffff\1\156\1\165\1\163\1\151\2\uffff\1\172\1\145\1\163\2\172\1\164\2\145\1\163\1\151\1\164\1\150\2\141\1\157\1\156\1\151\1\154\1\141\1\151\1\156\2\145\1\uffff\1\163\1\164\1\172\1\154\1\172\1\55\1\164\1\143\1\145\1\163\1\172\1\146\1\163\1\162\1\172\1\163\1\172\1\155\1\171\1\164\1\151\1\uffff\1\160\1\145\1\157\1\151\1\157\1\164\1\157\1\144\2\145\1\160\1\145\1\157\1\164\1\162\1\145\1\141\1\145\1\147\1\145\1\uffff\1\55\1\151\2\160\1\164\1\172\1\164\1\145\1\154\1\164\1\165\2\154\1\172\1\145\1\164\1\145\1\156\1\172\1\141\1\147\1\145\1\154\1\55\1\156\1\162\1\uffff\1\164\1\172\1\162\1\164\1\141\1\162\1\164\1\145\1\172\1\157\1\uffff\1\137\1\151\2\uffff\1\151\1\164\2\172\1\163\1\145\1\163\1\154\1\147\2\144\1\164\1\151\1\154\1\143\1\147\1\143\1\154\1\172\1\150\1\uffff\1\145\2\uffff\1\145\1\164\1\143\1\172\1\uffff\1\154\2\172\1\uffff\1\160\1\uffff\1\141\1\155\1\141\1\166\1\164\1\147\1\156\1\143\1\163\1\151\1\163\1\157\2\143\1\164\1\163\2\156\1\163\1\162\1\141\1\170\1\163\1\156\1\154\1\157\1\154\1\uffff\1\143\1\164\1\154\1\171\1\uffff\1\172\1\156\1\137\1\151\1\154\1\172\1\137\1\uffff\1\55\2\151\1\164\1\145\1\uffff\1\154\1\151\1\167\1\165\1\uffff\1\164\1\156\1\163\1\uffff\1\164\1\172\1\156\2\151\1\163\1\uffff\1\156\1\165\1\164\1\160\1\172\2\uffff\1\172\1\163\1\172\1\154\1\145\1\172\1\163\1\151\1\146\1\137\1\145\1\172\1\164\1\172\1\uffff\1\172\1\163\1\172\1\151\1\141\1\uffff\1\157\2\uffff\1\141\1\164\1\151\1\164\1\145\3\141\1\137\1\145\1\143\1\157\1\156\2\164\1\172\1\163\1\144\2\164\1\165\1\143\1\164\1\55\1\144\1\141\1\162\1\55\2\151\1\145\1\172\1\uffff\1\164\1\157\1\155\1\55\1\uffff\1\151\1\uffff\2\143\1\162\1\163\1\172\1\157\1\172\1\164\1\151\1\163\1\55\1\172\1\uffff\1\143\1\141\1\146\1\55\1\141\1\156\1\151\1\172\2\uffff\1\172\1\uffff\1\171\1\172\1\uffff\1\172\1\166\1\145\1\157\1\172\1\uffff\1\172\2\uffff\1\172\1\uffff\1\157\1\164\1\167\1\143\1\151\1\172\1\151\1\172\1\142\1\164\1\154\1\164\1\172\1\141\1\160\1\171\1\172\1\151\1\uffff\1\55\1\151\1\172\1\145\1\151\1\143\1\164\1\172\1\uffff\1\172\1\164\1\171\1\uffff\1\164\1\157\1\163\2\uffff\1\172\1\142\1\141\1\uffff\1\156\1\172\1\163\1\151\1\163\1\uffff\1\165\1\uffff\1\151\1\157\1\172\2\uffff\1\145\1\156\1\151\1\uffff\1\154\1\151\1\157\2\uffff\1\172\2\uffff\1\145\1\172\1\162\3\uffff\1\156\1\145\1\172\1\145\1\143\1\145\1\157\1\uffff\1\154\1\145\1\172\1\141\1\uffff\1\154\1\150\1\155\1\157\1\uffff\1\157\1\144\1\163\1\164\1\uffff\2\156\1\164\1\172\3\uffff\1\151\1\172\1\137\1\156\1\172\1\uffff\1\154\2\164\1\uffff\1\172\1\143\1\55\1\163\1\157\1\156\1\uffff\1\163\1\164\1\145\1\172\1\157\1\156\1\uffff\1\172\1\uffff\1\151\1\172\1\144\1\uffff\3\172\1\156\1\145\1\172\1\uffff\1\163\1\172\2\151\2\156\1\55\2\uffff\1\151\1\143\1\164\1\157\1\uffff\1\157\1\55\1\uffff\1\143\1\172\1\uffff\1\151\2\145\1\uffff\1\172\1\162\1\172\1\156\2\172\2\163\1\uffff\1\156\1\55\1\uffff\1\145\1\uffff\1\172\3\uffff\2\172\2\uffff\1\153\1\uffff\1\143\3\172\1\uffff\1\157\1\171\1\172\1\162\1\156\1\uffff\1\157\1\uffff\1\147\1\137\1\162\1\uffff\1\165\1\uffff\1\172\2\uffff\4\172\1\uffff\1\156\3\uffff\1\172\1\141\1\145\2\uffff\1\156\1\172\1\uffff\1\172\1\55\1\156\1\141\1\151\1\145\1\154\5\uffff\1\164\1\uffff\1\154\1\172\1\163\3\uffff\1\163\1\164\1\156\1\163\1\145\1\141\1\172\1\uffff\1\172\1\145\1\151\2\164\1\163\1\164\2\uffff\1\156\1\157\1\145\1\163\2\uffff\1\151\1\164\1\156\1\162\1\172\1\157\2\172\1\145\1\uffff\1\156\2\uffff\1\163\1\172\1\164\1\uffff\1\163\1\172\1\uffff";
    static final String DFA20_acceptS =
        "\23\uffff\1\53\1\54\1\uffff\1\72\1\103\1\120\1\121\1\142\1\143\1\147\1\uffff\1\174\1\175\1\176\1\u0080\1\u0081\1\uffff\1\u0086\1\u0087\3\uffff\1\u008e\100\uffff\1\u0088\1\uffff\1\u0089\1\u008a\1\uffff\1\u008d\132\uffff\1\167\4\uffff\1\u008b\1\u008c\27\uffff\1\153\25\uffff\1\173\24\uffff\1\110\32\uffff\1\134\12\uffff\1\1\2\uffff\1\61\1\77\24\uffff\1\60\1\uffff\1\7\1\67\4\uffff\1\13\3\uffff\1\21\1\uffff\1\177\33\uffff\1\162\4\uffff\1\116\7\uffff\1\160\5\uffff\1\112\4\uffff\1\161\3\uffff\1\126\6\uffff\1\156\5\uffff\1\2\1\131\16\uffff\1\6\5\uffff\1\10\1\uffff\1\12\1\15\40\uffff\1\122\4\uffff\1\136\1\uffff\1\111\14\uffff\1\102\10\uffff\1\152\1\3\1\uffff\1\11\2\uffff\1\133\5\uffff\1\14\1\uffff\1\135\1\33\1\uffff\1\45\22\uffff\1\140\10\uffff\1\123\3\uffff\1\u0085\3\uffff\1\113\1\124\3\uffff\1\75\5\uffff\1\36\1\uffff\1\51\3\uffff\1\166\1\57\3\uffff\1\171\3\uffff\1\137\1\5\1\uffff\1\105\1\4\3\uffff\1\150\1\73\1\155\7\uffff\1\50\4\uffff\1\74\4\uffff\1\52\4\uffff\1\24\4\uffff\1\55\1\154\1\151\5\uffff\1\u0082\3\uffff\1\34\6\uffff\1\132\6\uffff\1\16\1\uffff\1\43\3\uffff\1\20\6\uffff\1\22\7\uffff\1\163\1\165\4\uffff\1\26\2\uffff\1\71\2\uffff\1\141\3\uffff\1\62\10\uffff\1\u0084\2\uffff\1\23\1\uffff\1\64\1\uffff\1\56\1\17\1\46\2\uffff\1\115\1\127\1\uffff\1\37\4\uffff\1\100\5\uffff\1\170\1\uffff\1\104\3\uffff\1\35\1\uffff\1\40\1\uffff\1\76\1\117\4\uffff\1\172\1\uffff\1\u0083\1\107\1\63\3\uffff\1\157\1\70\2\uffff\1\101\7\uffff\1\65\1\144\1\114\1\125\1\42\1\uffff\1\31\3\uffff\1\66\1\130\1\164\7\uffff\1\47\7\uffff\1\41\1\145\4\uffff\1\146\1\106\11\uffff\1\30\1\uffff\1\25\1\27\3\uffff\1\44\2\uffff\1\32";
    static final String DFA20_specialS =
        "\156\uffff\1\0\u02c0\uffff}>";
    static final String[] DFA20_transitionS = {
            "\2\51\2\uffff\1\51\22\uffff\1\51\1\uffff\1\45\4\uffff\1\45\1\30\1\31\1\42\1\uffff\1\26\1\uffff\1\41\1\50\1\46\11\47\1\36\1\uffff\1\32\1\uffff\1\33\1\uffff\1\34\32\44\1\37\1\uffff\1\40\1\44\2\uffff\1\12\1\21\1\14\1\6\1\15\1\2\1\20\1\5\1\25\2\44\1\16\1\3\1\11\1\43\1\13\1\44\1\22\1\4\1\1\1\35\1\17\1\7\1\44\1\10\1\44\1\23\1\27\1\24",
            "\1\56\6\uffff\1\54\6\uffff\1\55\2\uffff\1\52\6\uffff\1\53",
            "\1\57\7\uffff\1\60",
            "\1\63\3\uffff\1\64\3\uffff\1\61\5\uffff\1\62",
            "\1\65\6\uffff\1\70\7\uffff\1\66\1\67",
            "\1\74\3\uffff\1\72\3\uffff\1\73\5\uffff\1\71",
            "\1\75\3\uffff\1\76",
            "\1\77\11\uffff\1\100",
            "\1\101",
            "\1\104\3\uffff\1\102\11\uffff\1\103\5\uffff\1\105",
            "\1\111\3\uffff\1\112\6\uffff\1\107\3\uffff\1\110\2\uffff\1\106",
            "\1\113\2\uffff\1\116\6\uffff\1\115\2\uffff\1\120\1\117\1\uffff\1\114",
            "\1\122\7\uffff\1\123\5\uffff\1\121\2\uffff\1\124",
            "\1\130\1\uffff\1\126\7\uffff\1\127\1\uffff\1\125",
            "\1\132\3\uffff\1\131",
            "\1\134\7\uffff\1\133",
            "\1\135",
            "\1\140\7\uffff\1\136\13\uffff\1\137",
            "\1\141\3\uffff\1\142\11\uffff\1\143",
            "",
            "",
            "\1\146\10\uffff\1\144\1\145\4\uffff\1\147",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\150",
            "",
            "",
            "",
            "",
            "",
            "\1\151",
            "",
            "",
            "\12\153\10\uffff\1\155\1\uffff\3\155\5\uffff\1\155\13\uffff\1\152\6\uffff\1\153\2\uffff\1\155\1\uffff\3\155\5\uffff\1\155\13\uffff\1\152",
            "\12\153\10\uffff\1\155\1\uffff\3\155\5\uffff\1\155\22\uffff\1\153\2\uffff\1\155\1\uffff\3\155\5\uffff\1\155",
            "\1\156\4\uffff\1\157",
            "",
            "\1\161\23\uffff\1\160",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170\1\uffff\1\171",
            "\1\172",
            "\1\173",
            "\1\174\1\175",
            "\1\176\12\uffff\1\177\3\uffff\1\u0081\5\uffff\1\u0080",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u008a\4\uffff\1\u0089",
            "\1\u008b\3\uffff\1\u008d\3\uffff\1\u008c",
            "\1\u008f\3\uffff\1\u008e",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0098\1\u0097",
            "\1\u0099\11\uffff\1\u009a",
            "\1\u009b",
            "\1\u009c",
            "\1\u009d",
            "\1\u009e\17\uffff\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a4\11\uffff\1\u00a3",
            "\1\u00a7\1\u00a5\1\uffff\1\u00a6\1\uffff\1\u00a8",
            "\1\u00aa\5\uffff\1\u00a9",
            "\1\u00ab",
            "\1\u00ac",
            "\1\u00af\1\uffff\1\u00ae\14\uffff\1\u00ad",
            "\1\u00b0\1\u00b1",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "\1\u00b6\24\uffff\1\u00b5",
            "\1\u00b8\16\uffff\1\u00b7",
            "\1\u00b9",
            "\1\u00ba",
            "\1\u00bb",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00c4\4\uffff\1\u00c3\5\uffff\1\u00bf\6\uffff\1\u00c1\1\u00c2\1\uffff\1\u00c0",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00ca\102\uffff\1\u00c7\2\uffff\1\u00c8\2\uffff\1\u00c9",
            "\1\u00cb",
            "\1\u00cc",
            "\1\u00cd",
            "\1\u00ce",
            "",
            "\12\153\10\uffff\1\155\1\uffff\3\155\5\uffff\1\155\22\uffff\1\153\2\uffff\1\155\1\uffff\3\155\5\uffff\1\155",
            "",
            "",
            "\52\u00d0\1\u00cf\uffd5\u00d0",
            "",
            "\1\u00d1",
            "\1\u00d2\11\uffff\1\u00d3",
            "\1\u00d4",
            "\1\u00d5",
            "\1\u00d6",
            "\1\u00d7",
            "\1\u00d8",
            "\1\u00d9",
            "\1\u00da",
            "\1\u00db",
            "\1\u00dc",
            "\1\u00dd",
            "\1\u00de",
            "\1\u00df",
            "\1\u00e0",
            "\1\u00e1",
            "\1\u00e2\25\uffff\1\u00e3",
            "\1\u00e4",
            "\1\u00e5",
            "\1\u00e6",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\1\44\1\u00e7\30\44",
            "\1\u00e9",
            "\1\u00ea",
            "\1\u00eb",
            "\1\u00ec",
            "\1\u00ed",
            "\1\u00ee",
            "\1\u00ef",
            "\1\u00f0",
            "\1\u00f1",
            "\1\u00f2",
            "\1\u00f3",
            "\1\u00f4",
            "\1\u00f5",
            "\1\u00f6",
            "\1\u00f7",
            "\1\u00f8",
            "\1\u00f9",
            "\1\u00fa",
            "\1\u00fb",
            "\1\u00fc",
            "\1\u00fd",
            "\1\u00fe",
            "\1\u00ff",
            "\1\u0100",
            "\1\u0101",
            "\1\u0102",
            "\1\u0103",
            "\1\u0104",
            "\1\u0105",
            "\1\u0106",
            "\1\u010a\6\uffff\1\u0107\2\uffff\1\u0109\6\uffff\1\u0108",
            "\1\u010b",
            "\1\u010c\1\u010d",
            "\1\u010e",
            "\1\u010f",
            "\1\u0110",
            "\1\u0111",
            "\1\u0112",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0114",
            "\1\u0115",
            "\1\u0116",
            "\1\u0117",
            "\1\u0118",
            "\1\u0119",
            "\1\u011a",
            "\1\u011b",
            "\1\u011c\7\uffff\1\u011d",
            "\1\u011e",
            "\1\u011f",
            "\1\u0120",
            "\1\u0121",
            "\1\u0122",
            "\1\u0123",
            "\1\u0124",
            "\1\u0125",
            "\1\u0126",
            "\1\u0127",
            "\1\u0128",
            "\1\u0129",
            "\1\u012a\4\uffff\1\u012b",
            "\1\u012c\17\uffff\1\u012d",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u012f",
            "\1\u0130",
            "\1\u0131",
            "\1\u0132",
            "\1\u0133",
            "\1\u0134",
            "",
            "\1\u0135",
            "\1\u0136",
            "\1\u0137",
            "\1\u0138",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u013a",
            "\1\u013b",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u013e",
            "\1\u013f",
            "\1\u0140",
            "\1\u0141",
            "\1\u0142",
            "\1\u0143",
            "\1\u0144",
            "\1\u0145",
            "\1\u0146",
            "\1\u0147",
            "\1\u0148",
            "\1\u0149",
            "\1\u014a",
            "\1\u014b",
            "\1\u014c",
            "\1\u014d",
            "\1\u014e",
            "\1\u014f",
            "",
            "\1\u0150",
            "\1\u0151",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0153",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0155",
            "\1\u0156",
            "\1\u0157",
            "\1\u0158",
            "\1\u0159",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u015b",
            "\1\u015c",
            "\1\u015d",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u015f",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0161",
            "\1\u0162",
            "\1\u0163",
            "\1\u0164",
            "",
            "\1\u0165",
            "\1\u0166",
            "\1\u0167",
            "\1\u0168",
            "\1\u0169",
            "\1\u016a",
            "\1\u016b",
            "\1\u016c",
            "\1\u016d",
            "\1\u016e",
            "\1\u016f",
            "\1\u0170",
            "\1\u0171",
            "\1\u0172\3\uffff\1\u0173\12\uffff\1\u0174",
            "\1\u0176\14\uffff\1\u0175",
            "\1\u0177",
            "\1\u0178",
            "\1\u0179",
            "\1\u017a",
            "\1\u017b",
            "",
            "\1\u017c",
            "\1\u017d",
            "\1\u017e",
            "\1\u017f",
            "\1\u0180",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0182",
            "\1\u0183",
            "\1\u0184",
            "\1\u0185",
            "\1\u0186",
            "\1\u0187",
            "\1\u0188",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u018a",
            "\1\u018c\1\uffff\1\u018b",
            "\1\u018d",
            "\1\u018e",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0190",
            "\1\u0191",
            "\1\u0192",
            "\1\u0193",
            "\1\u0194",
            "\1\u0195",
            "\1\u0196",
            "",
            "\1\u0197",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0199",
            "\1\u019a",
            "\1\u019b",
            "\1\u019c",
            "\1\u019d",
            "\1\u019e",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u01a0",
            "",
            "\1\u01a1",
            "\1\u01a2",
            "",
            "",
            "\1\u01a3",
            "\1\u01a4",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u01a7",
            "\1\u01a8",
            "\1\u01a9",
            "\1\u01aa",
            "\1\u01ab",
            "\1\u01ac",
            "\1\u01ad",
            "\1\u01ae",
            "\1\u01af",
            "\1\u01b0",
            "\1\u01b1",
            "\1\u01b2",
            "\1\u01b3",
            "\1\u01b4",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u01b6",
            "",
            "\1\u01b7",
            "",
            "",
            "\1\u01b8",
            "\1\u01b9",
            "\1\u01ba",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u01bc",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u01bf",
            "",
            "\1\u01c0",
            "\1\u01c1",
            "\1\u01c2",
            "\1\u01c3",
            "\1\u01c4",
            "\1\u01c5",
            "\1\u01c6",
            "\1\u01c7",
            "\1\u01c8",
            "\1\u01c9",
            "\1\u01ca",
            "\1\u01cb",
            "\1\u01cc",
            "\1\u01cd",
            "\1\u01ce",
            "\1\u01cf",
            "\1\u01d0",
            "\1\u01d1",
            "\1\u01d2",
            "\1\u01d3",
            "\1\u01d4",
            "\1\u01d5",
            "\1\u01d6",
            "\1\u01d7",
            "\1\u01d8",
            "\1\u01d9",
            "\1\u01da",
            "",
            "\1\u01db",
            "\1\u01dc",
            "\1\u01dd",
            "\1\u01de",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u01e0",
            "\1\u01e1",
            "\1\u01e2",
            "\1\u01e3",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u01e5",
            "",
            "\1\u01e6",
            "\1\u01e7",
            "\1\u01e8",
            "\1\u01e9",
            "\1\u01ea",
            "",
            "\1\u01eb",
            "\1\u01ec",
            "\1\u01ed",
            "\1\u01ee",
            "",
            "\1\u01ef",
            "\1\u01f0",
            "\1\u01f1",
            "",
            "\1\u01f2",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u01f4",
            "\1\u01f5",
            "\1\u01f6",
            "\1\u01f7",
            "",
            "\1\u01f8",
            "\1\u01f9",
            "\1\u01fa",
            "\1\u01fb",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u01fe",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0200",
            "\1\u0201",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0203",
            "\1\u0204",
            "\1\u0205",
            "\1\u0206",
            "\1\u0207",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0209",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u020c",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u020e",
            "\1\u020f",
            "",
            "\1\u0210",
            "",
            "",
            "\1\u0211",
            "\1\u0212",
            "\1\u0213",
            "\1\u0214",
            "\1\u0215",
            "\1\u0216",
            "\1\u0217",
            "\1\u0218",
            "\1\u0219",
            "\1\u021a",
            "\1\u021b",
            "\1\u021c",
            "\1\u021d",
            "\1\u021e",
            "\1\u021f",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0221",
            "\1\u0222",
            "\1\u0223",
            "\1\u0224",
            "\1\u0225\23\uffff\1\u0226",
            "\1\u0227",
            "\1\u0228",
            "\1\u0229",
            "\1\u022a",
            "\1\u022b",
            "\1\u022c",
            "\1\u022d",
            "\1\u022e",
            "\1\u022f",
            "\1\u0230",
            "\1\u0231\2\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u0233",
            "\1\u0234",
            "\1\u0235",
            "\1\u0236",
            "",
            "\1\u0237",
            "",
            "\1\u0238",
            "\1\u0239",
            "\1\u023a",
            "\1\u023b",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u023d",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u023f",
            "\1\u0240",
            "\1\u0241",
            "\1\u0242",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u0244",
            "\1\u0245",
            "\1\u0246",
            "\1\u0247",
            "\1\u0248",
            "\1\u0249",
            "\1\u024a",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u024d",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0250",
            "\1\u0251",
            "\1\u0252",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u0256",
            "\1\u0257",
            "\1\u0258",
            "\1\u0259",
            "\1\u025a",
            "\1\u025b",
            "\1\u025c",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u025e",
            "\1\u025f",
            "\1\u0260",
            "\1\u0261",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0263",
            "\1\u0264",
            "\1\u0265",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\10\44\1\u0266\21\44",
            "\1\u0269\3\uffff\1\u0268",
            "",
            "\1\u026a",
            "\1\u026b",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u026d",
            "\1\u026e",
            "\1\u026f",
            "\1\u0270",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u0272\2\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0274",
            "\1\u0275",
            "",
            "\1\u0276",
            "\1\u0277",
            "\1\u0278",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u027a",
            "\1\u027b",
            "",
            "\1\u027c",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u027e",
            "\1\u027f",
            "\1\u0280",
            "",
            "\1\u0281",
            "",
            "\1\u0282",
            "\1\u0283",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\1\u0285",
            "\1\u0286",
            "\1\u0287",
            "",
            "\1\u0288",
            "\1\u0289",
            "\1\u028a",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\1\u028c",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u028e",
            "",
            "",
            "",
            "\1\u028f",
            "\1\u0290",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0292",
            "\1\u0293",
            "\1\u0294",
            "\1\u0295",
            "",
            "\1\u0296",
            "\1\u0297",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0299",
            "",
            "\1\u029a",
            "\1\u029b",
            "\1\u029c",
            "\1\u029d",
            "",
            "\1\u029e",
            "\1\u029f",
            "\1\u02a0\5\uffff\1\u02a1",
            "\1\u02a2",
            "",
            "\1\u02a3",
            "\1\u02a4",
            "\1\u02a5",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "",
            "\1\u02a8\3\uffff\1\u02a7",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02aa",
            "\1\u02ab",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u02ad",
            "\1\u02ae",
            "\1\u02af",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02b1",
            "\1\u02b2",
            "\1\u02b3",
            "\1\u02b4",
            "\1\u02b5",
            "",
            "\1\u02b6",
            "\1\u02b7",
            "\1\u02b8",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02ba",
            "\1\u02bb",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u02bd",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02bf",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02c3",
            "\1\u02c4",
            "\1\u02c5\2\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u02c7",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02c9",
            "\1\u02ca",
            "\1\u02cb",
            "\1\u02cc",
            "\1\u02cd",
            "",
            "",
            "\1\u02ce",
            "\1\u02cf",
            "\1\u02d0",
            "\1\u02d1",
            "",
            "\1\u02d2",
            "\1\u02d3",
            "",
            "\1\u02d4",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u02d6",
            "\1\u02d7",
            "\1\u02d8",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02da",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02dc",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02df",
            "\1\u02e1\1\u02e0",
            "",
            "\1\u02e2",
            "\1\u02e3",
            "",
            "\1\u02e4",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\1\u02e8",
            "",
            "\1\u02e9",
            "\1\u02ea",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u02ed",
            "\1\u02ee",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02f0",
            "\1\u02f1",
            "",
            "\1\u02f2",
            "",
            "\1\u02f3",
            "\1\u02f4",
            "\1\u02f5",
            "",
            "\1\u02f6",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u02fc",
            "",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u02fe",
            "\1\u02ff",
            "",
            "",
            "\1\u0300",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0303",
            "\1\u0304",
            "\1\u0305",
            "\1\u0306",
            "\1\u0307",
            "\1\u0308",
            "",
            "",
            "",
            "",
            "",
            "\1\u0309",
            "",
            "\1\u030a",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u030c",
            "",
            "",
            "",
            "\1\u030d",
            "\1\u030e",
            "\1\u030f",
            "\1\u0310",
            "\1\u0311",
            "\1\u0312",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0315",
            "\1\u0316",
            "\1\u0317",
            "\1\u0318",
            "\1\u0319",
            "\1\u031b",
            "",
            "",
            "\1\u031c",
            "\1\u031d",
            "\1\u031e",
            "\1\u031f",
            "",
            "",
            "\1\u0320",
            "\1\u0321",
            "\1\u0322",
            "\1\u0323",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0325",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0328",
            "",
            "\1\u0329",
            "",
            "",
            "\1\u032a",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u032c",
            "",
            "\1\u032d",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | T__120 | T__121 | T__122 | T__123 | T__124 | T__125 | T__126 | T__127 | T__128 | T__129 | T__130 | T__131 | T__132 | T__133 | T__134 | T__135 | T__136 | T__137 | T__138 | T__139 | T__140 | T__141 | T__142 | T__143 | T__144 | T__145 | RULE_ID | RULE_STRING | RULE_HEX | RULE_INT | RULE_DECIMAL | RULE_DOC | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_110 = input.LA(1);

                        s = -1;
                        if ( (LA20_110=='*') ) {s = 207;}

                        else if ( ((LA20_110>='\u0000' && LA20_110<=')')||(LA20_110>='+' && LA20_110<='\uFFFF')) ) {s = 208;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}