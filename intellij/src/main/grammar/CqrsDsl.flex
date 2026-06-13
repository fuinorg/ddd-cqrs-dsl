package org.fuin.dsl.cqrs.intellij.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.fuin.dsl.cqrs.intellij.psi.CqrsTypes.*;

%%

%public
%class _CqrsDslLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

%{
  public _CqrsDslLexer() {
    this((java.io.Reader) null);
  }
%}

WHITE_SPACE=[\ \t\f\r\n]+

LINE_COMMENT="//"[^\r\n]*
DOC_COMMENT="/**"([^*]|"*"+[^*/])*"*"+"/"
BLOCK_COMMENT="/*"([^*]|"*"+[^*/])*"*"+"/"

ID=[A-Za-z][A-Za-z_0-9]*

STRING=\"([^\\\"]|\\[^])*\"|'([^\\']|\\[^])*'

HEX=0[xX][0-9a-fA-F_]+("#"(([bB][iI])|[lL]))?
INT=[0-9][0-9_]*
DECIMAL={INT}([eE][+-]?{INT})?([bB][iIdD]|[lLdDfF])?
NUMBER={HEX}|({INT}|{DECIMAL})("."({INT}|{DECIMAL}))?

%%

<YYINITIAL> {

  {WHITE_SPACE}              { return WHITE_SPACE; }

  {LINE_COMMENT}             { return LINE_COMMENT; }
  {DOC_COMMENT}              { return DOC_COMMENT; }
  {BLOCK_COMMENT}            { return BLOCK_COMMENT; }

  // ---- Hyphenated keywords (must precede the ID rule) ----
  "value-object"            { return KW_VALUE_OBJECT; }
  "entity-id"               { return KW_ENTITY_ID; }
  "aggregate-id"            { return KW_AGGREGATE_ID; }
  "business-rules"          { return KW_BUSINESS_RULES; }
  "business-rule"           { return KW_BUSINESS_RULE; }
  "command-handler"         { return KW_COMMAND_HANDLER; }
  "copies-attributes-of"    { return KW_COPIES_ATTRIBUTES_OF; }

  // ---- Structural keywords ----
  "context"                 { return KW_CONTEXT; }
  "namespace"               { return KW_NAMESPACE; }
  "import"                  { return KW_IMPORT; }
  "type"                    { return KW_TYPE; }
  "element"                 { return KW_ELEMENT; }
  "generics"                { return KW_GENERICS; }
  "constraint"              { return KW_CONSTRAINT; }
  "input"                   { return KW_INPUT; }
  "exception"               { return KW_EXCEPTION; }
  "annotation"              { return KW_ANNOTATION; }
  "cid"                     { return KW_CID; }
  "message"                 { return KW_MESSAGE; }
  "base"                    { return KW_BASE; }
  "identifies"              { return KW_IDENTIFIES; }
  "enum"                    { return KW_ENUM; }
  "instances"               { return KW_INSTANCES; }
  "deprecated"              { return KW_DEPRECATED; }
  "event"                   { return KW_EVENT; }
  "entity"                  { return KW_ENTITY; }
  "identifier"              { return KW_IDENTIFIER; }
  "root"                    { return KW_ROOT; }
  "aggregate"               { return KW_AGGREGATE; }
  "constructor"             { return KW_CONSTRUCTOR; }
  "fires"                   { return KW_FIRES; }
  "returns"                 { return KW_RETURNS; }
  "method"                  { return KW_METHOD; }
  "ref"                     { return KW_REF; }
  "slabel"                  { return KW_SLABEL; }
  "label"                   { return KW_LABEL; }
  "tooltip"                 { return KW_TOOLTIP; }
  "prompt"                  { return KW_PROMPT; }
  "examples"                { return KW_EXAMPLES; }
  "invariants"              { return KW_INVARIANTS; }
  "preconditions"           { return KW_PRECONDITIONS; }
  "service"                 { return KW_SERVICE; }
  "command"                 { return KW_COMMAND; }
  "target"                  { return KW_TARGET; }
  "sla"                     { return KW_SLA; }
  "handles"                 { return KW_HANDLES; }
  "uses"                    { return KW_USES; }
  "projection"              { return KW_PROJECTION; }
  "view"                    { return KW_VIEW; }
  "consistency"             { return KW_CONSISTENCY; }
  "acceptable"              { return KW_ACCEPTABLE; }
  "detection"               { return KW_DETECTION; }
  "resolution"              { return KW_RESOLUTION; }
  "nullable"                { return KW_NULLABLE; }

  // ---- Time unit keywords ----
  "millis"                  { return KW_MILLIS; }
  "seconds"                 { return KW_SECONDS; }
  "minutes"                 { return KW_MINUTES; }
  "hours"                   { return KW_HOURS; }
  "days"                    { return KW_DAYS; }

  // ---- Consistency / detection / resolution value keywords ----
  "weak"                    { return KW_WEAK; }
  "strong"                  { return KW_STRONG; }
  "never"                   { return KW_NEVER; }
  "manually"                { return KW_MANUALLY; }
  "automatic"               { return KW_AUTOMATIC; }
  "workflow"                { return KW_WORKFLOW; }

  // ---- Literal keywords ----
  "true"                    { return KW_TRUE; }
  "false"                   { return KW_FALSE; }
  "null"                    { return KW_NULL; }

  // ---- Punctuation ----
  "{"                       { return LBRACE; }
  "}"                       { return RBRACE; }
  "("                       { return LPAREN; }
  ")"                       { return RPAREN; }
  "<"                       { return LT; }
  ">"                       { return GT; }
  "@"                       { return AT; }
  ","                       { return COMMA; }
  "."                       { return DOT; }
  "|"                       { return PIPE; }
  "*"                       { return STAR; }

  // ---- Terminals ----
  {STRING}                  { return STRING; }
  {NUMBER}                  { return NUMBER; }
  {ID}                      { return ID; }

}

[^]                         { return BAD_CHARACTER; }
