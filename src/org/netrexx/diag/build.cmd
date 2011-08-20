/* Build the netrexx diagnostic classes.               */
/*                                                     */

/* There categories of classes, here:

  bases:    individual testcases

  middles:  subsystem collections

  tops:     overall tests

*/

bases   ='DiagX'
middles ='DiagSay DiagContinue DiagIf DiagLoop DiagSelect',
         'DiagExpression',               /* DiagDecimal now in \Decimal */
         'DiagConstant DiagInterface',
         'DiagForward DiagMutual DiagAbstract',
         'DiagConcat DiagStems DiagNop DiagParse DiagComment',
         'DiagArrays DiagRexx DiagUTF8 DiagIndir DiagMinor'
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
w=wordpos('-exec', names)
if w>0 then do; opts=opts '-exec'; names=delword(names,w,1); end
w=wordpos('-nojava', names)
if w>0 then do; opts=opts '-nojava'; names=delword(names,w,1); end
w=wordpos('-verbose5', names)
if w>0 then do; opts=opts '-verbose5'; names=delword(names,w,1); end
w=wordpos('-test', names)
if w>0 then do; opts=opts '-test'; names=delword(names,w,1); end
w=wordpos('-format', names)
if w>0 then do; opts=opts '-format -comments'; names=delword(names,w,1); end
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

javahome=javahome()

javaclasses=javahome'\classes'
javadisk=left(javahome, 2)
'@cls'
say 'Classes:' javaclasses

/* ---- ensure target class directories exist ---- */
here=directory()
parse var here heredisk ':' herepath
parse var javaclasses javadisk ':' javapath

call directory javaclasses
dir='COM'
if stream(dir, 'c', 'query datetime')='' then '@md' dir
call directory dir

dir='ibm'
if stream(dir, 'c', 'query datetime')='' then '@md' dir
call directory dir

dir='netrexx'
if stream(dir, 'c', 'query datetime')='' then '@md' dir
call directory dir

dir='diag'
if stream(dir, 'c', 'query datetime')='' then '@md' dir

call directory here

/* ---- off we go... ---- */
parse source os .
if os='OS/2' then jopts=jopts '-norestart'

startsecs=time('s')
call qerase '*.class'
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
    '@copy' name'.nrx'  name1'.nrx  >nul'
    '@copy' name'2.nrx' name2'.nrx  >nul'
    '@copy' name'3.nrx' name3'.nrx  >nul'
    '@copy' name'4.nrx' name4'.nrx  >nul'
    '@java' jopts '-mx12000k COM.ibm.netrexx.process.NetRexxC',
            opts name1 name2 name3 name4 '| tee' name'.stdout'
    '@copy' name1'.crossref' herepath '>nul'
    '@copy' name2'.crossref' herepath '>nul'
    '@copy' name3'.crossref' herepath '>nul'
    '@copy' name4'.crossref' herepath '>nul'

    '@copy' name1'.java.keep' herepath '>nul'
    '@copy' name2'.java.keep' herepath '>nul'
    '@copy' name3'.java.keep' herepath '>nul'
    '@copy' name4'.java.keep' herepath '>nul'

    call qerase name1'.nrx'          /* done with the source file copies */
    call qerase name2'.nrx'
    call qerase name3'.nrx'
    call qerase name4'.nrx'

    call qerase name1'.crossref'     /* .. and the crossrefs over there */
    call qerase name2'.crossref'
    call qerase name3'.crossref'
    call qerase name4'.crossref'

    call qerase name1'.java.keep'    /* .. and the .javas */
    call qerase name2'.java.keep'
    call qerase name3'.java.keep'
    call qerase name4'.java.keep'
    target=name'.class';  if stream(target, 'c', 'query exists')='' then exit 2
    target=name'2.class'; if stream(target, 'c', 'query exists')='' then exit 2
    target=name'3.class'; if stream(target, 'c', 'query exists')='' then exit 2
    target=name'4.class'; if stream(target, 'c', 'query exists')='' then exit 2

    '@copy' name'.class' targdir '>nul';  if rc=0 then call qerase name'.class >nul'
    '@copy' name'2.class' targdir '>nul'; if rc=0 then call qerase name'2.class >nul'
    '@copy' name'3.class' targdir '>nul'; if rc=0 then call qerase name'3.class >nul'
    '@copy' name'4.class' targdir '>nul'; if rc=0 then call qerase name'4.class >nul'
    if rc<>0 then exit rc
    /* .class files are in the right place */
    say name'*.class copied to' targdir
    end

   else /* not *the* special case */ do
    if name='DiagUTF8' then uopt='-utf8'; else uopt=''
    '@java' jopts '-mx24000k COM.ibm.netrexx.process.NetRexxC',
                  opts uopt name '| tee' name'.stdout'
    if rc\=0 then exit rc  /* hidden by tee, in fact... */
    target=name'.class'
    if stream(target, 'c', 'query exists')='' then exit 1
    if wordpos(name, 'DiagExpression DiagMinor DiagForward DiagAbstract')>0 then
      target=name'*.class'
    '@copy' target targdir
    if rc<>0 then exit rc
    say target 'copied to' targdir
    call qerase target
    end
  end
stopsecs=time('s'); elap=stopsecs-startsecs
if elap<0 then elap=elap+86400  /* better be done in a day */
mins=format(elap/60,,1)/1

say '--- All OK --- ['mins 'minutes]'

if run then do
  say
  '@start java COM.ibm.netrexx.diag.DiagAll'
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

/* JAVAHOME -- return the path to the Java home directory */
javahome:
  parse source os .
  if os='OS/2' then env='OS2ENVIRONMENT'
               else env='ENVIRONMENT'
  path=value('PATH',,env)
  javaexe='javac.exe'
  do while path<>''
    parse var path dir';' path
    if right(dir, 1)<>'\' then dir=dir'\'
    exe=dir||javaexe
    if stream(exe, 'c', 'query datetime')<>'' then do
      /* say 'found' exe */
      home=left(dir, lastpos('\', dir, length(dir)-1)-1)
      return  home
      end
    end
  call error 0, 'java.exe.not.found', 'java.exe needs to be on PATH'

/* Quiet erase */
qerase: procedure
 parse source os .
 if pos(95,os)=0 then do
   '@erase' arg(1) '1>nul 2>nul'
   return
   end
 /* Win95 doesn't have stderr redirection, or the /f flag */
 parse arg spec
 p=lastpos('\', spec)
 if p=0 then dir=''
  else do
   p=p+1
   parse var spec dir =(p) spec
   end
 'dir' spec '/b | rxqueue'
 do queued()
   parse pull file
   '@erase' dir||file
   end
 return
