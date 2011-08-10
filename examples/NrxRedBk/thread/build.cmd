call nrc -nocrossref ThrdTst1 ThrdTst2 
cd consumer
call nrc -nocrossref Consumer 
cd ..
cd synch
call nrc -nocrossref Synch
cd ..
cd philfork
call nrc -nocrossref PFtext PFgui
cd ..