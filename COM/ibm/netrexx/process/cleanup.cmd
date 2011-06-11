/* Clean up detritus:

   erase *.java.keep
   erase *.stdout
   erase *.crossref

   */

say 'Cleanup' directory()'...'

call qerase '*.java.keep'
call qerase '*.stdout'
call qerase '*.std'
call qerase '*.crossref'
exit


/* Quiet erase */
qerase: procedure
 say 'Erasing' arg(1)'...'
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
   call sysfiledelete dir||file
   end
 return
