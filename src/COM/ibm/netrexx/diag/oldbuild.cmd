/* Build the netrexx diagnostic classes.               */
/*                                                     */

/* There categories of classes, here:

  bases:    individual testcases

  middles:  subsystem collections

  tops:     overall tests

*/

bases   ='DiagX'
middles ='DiagSay DiagContinue DiagIf DiagLoop DiagSelect',
         'DiagDecimal DiagExpression',
         'DiagConstant DiagInterface',
         'DiagForward DiagMutual DiagAbstract',
         'DiagConcat DiagStems DiagNop DiagParse DiagComment',
         'DiagArrays DiagRexx DiagUTF8 DiagIndir'
tops    ='DiagAll'

namelist=bases middles tops

opts='-nodiag -keep -comments'
jopts='-ms4M -mx16M'

/* ----------------------------------------------------------------- */
parse arg names
os2=0; from=0
w=wordpos('-os2', names)
if w>0 then do; os2=1; names=delword(names,w,1); end
w=wordpos('-new', names)
if w>0 then do; os2=0; names=delword(names,w,1); end
w=wordpos('-diag', names)
if w>0 then do; opts=opts '-diag'; names=delword(names,w,1); end
w=wordpos('-test', names)
if w>0 then do; opts=opts '-test'; names=delword(names,w,1); end
w=wordpos('-vgc', names)
if w>0 then do; jopts=jopts '-verbosegc'; names=delword(names,w,1); end
w=wordpos('-prof', names)
if w>0 then do; jopts=jopts '-prof'; names=delword(names,w,1); end
w=wordpos('-from', names)
if w>0 then do
  fromname=word(names, w+1)
  names=delword(names,w,2)
  from=1; end

if names='' then /* everything */ do
  names=namelist
  if from then do
    cut=wordpos(translate(fromname), translate(names))
    if cut=0 then do; say '-from "'fromname'" not found'; exit 1; end
    names=subword(names, cut)
    end
  opts=opts '-notrace'        /* just in case... */
  run=1
  end
 else /* just one expected */ do
  parse upper var names name rest
  if rest<>'' then do; say 'Just one name expected'; exit; end
  s=lastpos('\', name); name=substr(name, s+1)  /* drop path */
  parse var name name '.'                       /* drop ext  */
  n=wordpos(name, translate(namelist))
  if n=0 then do; say 'Unknown name' name; exit; end
  names=word(namelist, n)
  run=0
  end

javahome='d:\java11'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then javahome='f:\java11'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then javahome='h:\java11'
if stream(javahome'\bin\java.exe', 'c', 'query exists')='' then do
  say 'Cannot find java home'; exit 1; end
javaclasses=javahome'\classes'
javadisk=left(javahome, 2)
say 'Classes:' javaclasses

/* ---- ensure target class directories exist ---- */
here=directory()
parse var here heredisk ':' herepath
parse var javaclasses javadisk ':' javapath
'@'javadisk':'
'@cd' javapath
'@md COM 2>nul'
'@cd COM'
'@md ibm 2>nul'
'@cd ibm'
'@md netrexx 2>nul'
'@cd netrexx'
'@md diag    2>nul'
'@'heredisk':'
'@cd' herepath

/* ---- off we go... ---- */
/**
'@java -version 2> $.$'; parse value linein('$.$') with '1.' subv ' '
call lineout '$.$'; '@erase $.$'
if subv>=0.2 then jopts=jopts '-norestart'; else jopts=jopts '-noasyncgc'
if subv>=0.2 then jopts=jopts '-norestart'; else jopts=jopts '-noasyncgc'
**/
jopts=jopts '-norestart'

startsecs=time('s')
'@erase *.class 2>nul'
do words(names)
  parse var names name names
  say name'...'
  subdir=getpacksub(name)
  targdir=javaclasses'\COM\ibm\netrexx\'subdir

  /* Special case: for DiagMutual we need the source files to be in the
     classpath so javac can find them, and we compile them together... */
  if name='DiagMutual' then do
    name1=targdir'\'name
    name2=targdir'\'name'2'   /* DiagMutual2 */
    name3=targdir'\'name'3'   /* DiagMutual3 */
    name4=targdir'\'name'4'   /* DiagMutual4 */
    '@copy' name'.nrx'  name1'.nrx  1>nul'
    '@copy' name'2.nrx' name2'.nrx  1>nul'
    '@copy' name'3.nrx' name3'.nrx  1>nul'
    '@copy' name'4.nrx' name4'.nrx  1>nul'
    '@java' jopts '-mx12000k COM.ibm.netrexx.process.NetRexxC',
            opts name1 name2 name3 name4 '| tee' name'.stdout'
    '@copy' name1'.crossref' herepath '1>nul'
    '@copy' name2'.crossref' herepath '1>nul'
    '@copy' name3'.crossref' herepath '1>nul'
    '@copy' name4'.crossref' herepath '1>nul'

    '@copy' name1'.java.keep' herepath '1>nul'
    '@copy' name2'.java.keep' herepath '1>nul'
    '@copy' name3'.java.keep' herepath '1>nul'
    '@copy' name4'.java.keep' herepath '1>nul'

    '@erase' name1'.nrx 1>nul'          /* done with the source file copies */
    '@erase' name2'.nrx 1>nul'
    '@erase' name3'.nrx 1>nul'
    '@erase' name4'.nrx 1>nul'

    '@erase' name1'.crossref 1>nul'     /* .. and the crossrefs over there */
    '@erase' name2'.crossref 1>nul'
    '@erase' name3'.crossref 1>nul'
    '@erase' name4'.crossref 1>nul'

    '@erase' name1'.java.keep 1>nul'    /* .. and the .javas */
    '@erase' name2'.java.keep 1>nul'
    '@erase' name3'.java.keep 1>nul'
    '@erase' name4'.java.keep 1>nul'
    target=name'.class';  if stream(target, 'c', 'query exists')='' then exit 2
    target=name'2.class'; if stream(target, 'c', 'query exists')='' then exit 2
    target=name'3.class'; if stream(target, 'c', 'query exists')='' then exit 2
    target=name'4.class'; if stream(target, 'c', 'query exists')='' then exit 2

    '@copy' name'.class' targdir '2>nul';  if rc=0 then '@erase' name'.class 2>nul'
    '@copy' name'2.class' targdir '2>nul'; if rc=0 then '@erase' name'2.class 2>nul'
    '@copy' name'3.class' targdir '2>nul'; if rc=0 then '@erase' name'3.class 2>nul'
    '@copy' name'4.class' targdir '2>nul'; if rc=0 then '@erase' name'4.class 2>nul'
    if rc<>0 then exit rc
    /* .class files are in the right place */
    say name'.class copied to' targdir
    end

   else /* not *the* special case */ do
    if name='DiagUTF8' then uopt='-utf8'; else uopt=''
    '@java' jopts '-mx12000k COM.ibm.netrexx.process.NetRexxC',
                  opts uopt name '| tee' name'.stdout'
    if rc\=0 then exit rc  /* hidden by tee, in fact... */
    target=name'.class'
    if stream(target, 'c', 'query exists')='' then exit 1
    '@copy' target targdir '2>nul'
    if rc<>0 then exit rc
    if name='DiagForward' then do
      '@copy DiagForward2.class' targdir '2>nul'
      if rc=0 then '@erase DiagForward2.class 2>nul'
      '@copy DiagForward3.class' targdir '2>nul'
      if rc=0 then '@erase DiagForward3.class 2>nul'
      '@copy DiagForward4.class' targdir '2>nul'
      if rc=0 then '@erase DiagForward4.class 2>nul'
      if rc<>0 then exit rc
      end
    if name='DiagAbstract' then do
      '@copy DiagAbstract2.class' targdir '2>nul'
      if rc=0 then '@erase DiagAbstract2.class 2>nul'
      end
    say target 'copied to' targdir
    '@erase' target
    end
  end
stopsecs=time('s'); elap=stopsecs-startsecs
if elap<0 then elap=elap+86400  /* better be done in a day */
mins=format(elap/60,,1)/1

say '--- All OK --- ['mins 'minutes]'

if run then do
  say
  '@java COM.ibm.netrexx.diag.DiagAll'
  end

exit 0


/* GETPACKSUB -- return subdirectory (from package clause) */
getpacksub: procedure
  parse arg name
  in=name'.nrx'

  try=200; pack=''
  do try
    line=linein(in)
    parse var line pre 'package ' pack post
    if pack<>'' then if pre post=' ' then leave
    end
  call lineout in
  parse var pack 'netrexx.' sub
  if sub<>'' then return sub
  say "No 'package xxx.netrexx.yyy' line found in '"in"', first" try "lines"
  exit 8





