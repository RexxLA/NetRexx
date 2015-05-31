/* Generated from 'moveInfo.nrx' 31 May 2015 22:07:37 [v3.04] *//* Options: Comments Compact Decimal Java Logo Replace Sourcedir Trace2 UTF8 Verbose3 */public class moveInfo{private static final netrexx.lang.Rexx $01=new netrexx.lang.Rexx(1);private static final netrexx.lang.Rexx $02=new netrexx.lang.Rexx('/');private static final char[] $03={2,4,46,110,114,120,10,1,0,0};private static final netrexx.lang.Rexx $04=netrexx.lang.Rexx.toRexx("\\input{");private static final netrexx.lang.Rexx $05=new netrexx.lang.Rexx('}');private static final netrexx.lang.Rexx $06=netrexx.lang.Rexx.toRexx(".tex");private static final netrexx.lang.Rexx $07=netrexx.lang.Rexx.toRexx("\\section{");private static final netrexx.lang.Rexx $08=netrexx.lang.Rexx.toRexx("\\index{");private static final netrexx.lang.Rexx $09=new netrexx.lang.Rexx(0);private static final java.lang.String $0="moveInfo.nrx";public static void main(java.lang.String $0s[]) throws java.io.IOException,java.io.FileNotFoundException{netrexx.lang.Rexx path;java.io.File f;java.lang.String l[];java.io.PrintWriter c;netrexx.lang.Rexx i=null;java.lang.String fn=null;java.io.BufferedReader b=null;netrexx.lang.Rexx fno=null;java.io.PrintWriter o=null;netrexx.lang.Rexx write_it=null;netrexx.lang.Rexx line=null;path=netrexx.lang.Rexx.toRexx("/Users/rvjansen/Sites/netrexx/netrexx/netrexxc/trunk/src/org/netrexx/njpipes/stages");

f=new java.io.File(netrexx.lang.Rexx.toString(path));
l=f.list();
c=new java.io.PrintWriter((java.io.Writer)(new java.io.FileWriter("stagesChapter.tex")));
c.println("\\chapter{Stages}");
{netrexx.lang.Rexx $1=new netrexx.lang.Rexx(l.length).OpSub(null,$01);i=new netrexx.lang.Rexx((byte)0);i:for(;i.OpLtEq(null,$1);i=i.OpAdd(null,new netrexx.lang.Rexx(1))){
fn=l[i.toint()];
b=new java.io.BufferedReader((java.io.Reader)(new java.io.FileReader(netrexx.lang.Rexx.toString((path.OpCc(null,$02)).OpCc(null,netrexx.lang.Rexx.toRexx(fn))))));

{netrexx.lang.Rexx $2[]=new netrexx.lang.Rexx[1];netrexx.lang.RexxParse.parse(netrexx.lang.Rexx.toRexx(fn),$03,$2);fno=$2[0];}
c.println((java.lang.Object)(($04.OpCc(null,fno)).OpCc(null,$05)));
o=new java.io.PrintWriter((java.io.Writer)(new java.io.FileWriter(netrexx.lang.Rexx.toString(fno.OpCc(null,$06)))));
o.println((java.lang.Object)(($07.OpCc(null,fno)).OpCc(null,$05)));
o.println((java.lang.Object)(($08.OpCc(null,fno)).OpCc(null,$05)));
o.println("\\begin{shaded}");
o.println("\\begin{alltt}");

write_it=new netrexx.lang.Rexx((byte)0);
{$3:for(;;){
line=netrexx.lang.Rexx.toRexx(b.readLine());
if (line==null) break $3;
if ((line.pos(netrexx.lang.Rexx.toRexx("/**"))).OpGt(null,$09)) write_it=new netrexx.lang.Rexx((byte)1);
if ((line.pos(netrexx.lang.Rexx.toRexx("*/"))).OpGt(null,$09)) write_it=new netrexx.lang.Rexx((byte)0);
if (write_it.toboolean()) o.println((java.lang.Object)line);
}} // loop forever
o.println("\\end{alltt}");
o.println("\\end{shaded}");

b.close();
o.close();
}}/*i*/ // loop i
c.close();return;}private moveInfo(){return;}}
